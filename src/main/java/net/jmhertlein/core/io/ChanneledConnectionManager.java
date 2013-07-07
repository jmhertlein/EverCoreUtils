/*
 * Copyright (C) 2013 Joshua Michael Hertlein <jmhertlein@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.jmhertlein.core.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Joshua Michael Hertlein <jmhertlein@gmail.com>
 *
 * When passed two Object{Input,Output}Streams, this class will take over
 * management of them, pulling off objects that are sent over them into internal
 * buffers depending on which channel they are written to.
 *
 * This allows multiple "channels" of communication over a single input/output
 * stream pair, which is especially useful if the streams that are passed to the
 * manager are one end of a Socket.
 *
 */
public class ChanneledConnectionManager {

    private final ObjectInputStream ois;
    private final ObjectOutputStream oos;
    private final Map<Integer, Queue<Object>> bufferMap;
    private final Queue<PacketReceiveListener> listeners;
    private Thread readThread;
    private volatile boolean shutdown;

    public ChanneledConnectionManager(final ObjectOutputStream oos, final ObjectInputStream ois) {
        this.ois = ois;
        this.oos = oos;
        bufferMap = new ConcurrentHashMap<>();
        shutdown = false;
        listeners = new ConcurrentLinkedQueue<>();
    }

    public void startListenThread() {
        readThread = new Thread() {
            @Override
            public void run() {
                while (!shutdown) {
                    ChannelPacket p;
                    try {
                        p = (ChannelPacket) ois.readObject();
                    } catch(EOFException eofe) {
                        System.out.println("Received EOF, shutting down listener thread.");
                        shutdown();
                        continue;
                    } catch (IOException | ClassNotFoundException ex) {
                        if(shutdown)
                            System.out.println("Read aborted due to shutdown.");
                        else
                            Logger.getLogger(ChanneledConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
                        
                        continue;
                    }
                    
                    boolean addToBuffer = true;
                    for(PacketReceiveListener l : listeners) {
                        addToBuffer &= l.onPacketReceive(p.getData(), p.getChannel());
                    }
                    if(!addToBuffer)
                        continue;
                    
                    Queue<Object> buffer = getChannelBuffer(p.getChannel());
                    
                    buffer.add(p.getData());
                    synchronized (buffer) {
                        buffer.notifyAll();
                    }
                }
            }
        };

        readThread.start();
    }

    /**
     * Attempts to read a received object from the specified channel.
     *
     * If an object has already been received on the channel, it will return
     * immediately.
     *
     * However, if no object has been received yet, this method will block until
     * one has been received.
     *
     * @param channel
     * @return
     * @throws InterruptedException if interrupted while waiting to receive an
     * object
     */
    public Object readObject(int channel) throws InterruptedException {
        Queue<Object> channelBuffer = getChannelBuffer(channel);

        if (channelBuffer.isEmpty()) {
            synchronized (channelBuffer) {
                channelBuffer.wait();
            }
        }

        return channelBuffer.remove();
    }

    public Object readObject() throws InterruptedException {
        return readObject(0);
    }

    public synchronized void writeObject(int channel, Object data) throws IOException {
        oos.writeObject(new ChannelPacket(data, channel));
    }

    public void writeObject(Object data) throws IOException {
        writeObject(0, data);
    }

    public void shutdown() {
        shutdown = true;
        try {
            oos.close();
            ois.close();
        } catch (IOException ex) {
        }
    }

    private synchronized Queue<Object> getChannelBuffer(int channel) {
        Queue<Object> buffer = bufferMap.get(channel);
        if (buffer == null) {
            buffer = new ConcurrentLinkedQueue<>();
            bufferMap.put(channel, buffer);
        }
        return buffer;
    }
    
    public void addPacketReceiveListener(PacketReceiveListener l) {
        listeners.add(l);
    }
}
