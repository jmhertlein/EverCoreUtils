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
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
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
public class ConnectionManager {

    private final ObjectInputStream ois;
    private final ObjectOutputStream oos;
    private final Socket s;
    private PacketReceiveListener listener;
    private final Queue<Object> unprocessedBuffer;
    private Thread readThread;
    private volatile boolean shutdown;

    private Callable shutdownHook;

    public ConnectionManager(final Socket s, final ObjectOutputStream oos, final ObjectInputStream ois) {
        this.ois = ois;
        this.oos = oos;
        this.s = s;
        unprocessedBuffer = new ConcurrentLinkedQueue<>();
        shutdown = false;
    }

    public Socket getSocket() {
        return s;
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public void startListenThread() {
        readThread = new Thread() {
            @Override
            public void run() {
                while (!shutdown) {
                    Object p;
                    try {
                        p = ois.readObject();
                    } catch (EOFException eofe) {
                        System.out.println("Received EOF, shutting down listener thread.");
                        shutdown();
                        continue;
                    } catch (IOException | ClassNotFoundException ex) {
                        if (shutdown)
                            System.out.println("Read aborted due to shutdown.");
                        else
                            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);

                        continue;
                    }

                    if(listener == null)
                        unprocessedBuffer.add(p);
                    else
                        try {
                        listener.onPacketReceive(p);
                        } catch(Throwable t) {
                            System.err.println("EXCEPTION IN LISTENER THREAD, CAUGHT TO PREVENT CONNECTION LISTENER THREAD FROM DYING:");
                            t.printStackTrace(System.err);
                        }

                }
            }
        };

        readThread.start();
    }

    public synchronized void writeObject(Object data) throws IOException {
        oos.writeObject(data);
    }

    /**
     * Shuts down the listener thread and calls the shutdown hook.
     */
    public void shutdown() {
        shutdown = true;
        try {
            oos.close();
            ois.close();
            s.close();
        } catch (IOException ex) {
        }

        if (shutdownHook != null)
            try {
                shutdownHook.call();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
    }

    @Deprecated
    public void addPacketReceiveListener(PacketReceiveListener l) {
        setPacketReceiveListener(l);
    }

    public void setPacketReceiveListener(PacketReceiveListener l) {
        listener = l;

        for(Object o : unprocessedBuffer) {
            listener.onPacketReceive(o);
        }

        unprocessedBuffer.clear();
    }

    public Callable getShutdownHook() {
        return shutdownHook;
    }

    public void setShutdownHook(Callable shutdownHook) {
        this.shutdownHook = shutdownHook;
    }

}
