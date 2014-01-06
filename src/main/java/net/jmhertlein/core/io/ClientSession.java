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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import javax.crypto.SecretKey;


/**
 *
 * @author Joshua Michael Hertlein <jmhertlein@gmail.com>
 */
public class ClientSession {
    private static int nextID = 0;
    
    private final Socket s;
    private final SecretKey sessionKey;
    private final String username;
    private final int sessionID;
    
    private ChanneledConnectionManager connection;
    
    public ClientSession(Socket s, SecretKey sessionKey, String username) {
        this.sessionKey = sessionKey;
        this.username = username;
        this.sessionID = nextID;
        this.s = s;
        
        nextID++;
    }
    
    public void initChannels(ObjectOutputStream oos, ObjectInputStream ois) {
        connection = new ChanneledConnectionManager(oos, ois);
        connection.startListenThread();
    }

    public ChanneledConnectionManager getConnection() {
        return connection;
    }

    public Socket getSocket() {
        return s;
    }
    
    public void setShutdownHook(Callable c) {
        connection.setShutdownHook(c);
    }
    
    public Callable getShutdownHook() {
        return connection.getShutdownHook();
    }
    
    public boolean isDisconnected() {
        return connection.isShutdown();
    }

    public SecretKey getSessionKey() {
        return sessionKey;
    }

    public String getUsername() {
        return username;
    }

    public int getSessionID() {
        return sessionID;
    }
    
    public void addPacketListener(PacketReceiveListener l) {
        connection.addPacketReceiveListener(l);
    }

    @Override
    public String toString() {
        return "[" + sessionID + "|" + username + "]";
    }
}
