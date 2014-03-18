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
import java.security.PublicKey;
import java.util.Properties;
import java.util.concurrent.Callable;
import javax.crypto.SecretKey;
import net.jmhertlein.core.crypto.Keys;

/**
 * An object representing the client in a client/server model.
 *
 * Each client, upon creation, is given an integer ID that is unique to that JVM instance.
 *
 * @author Joshua Michael Hertlein <jmhertlein@gmail.com>
 */
public class ClientSession {
    private static int nextID = 0;

    //persist these
    private final PublicKey pubKey;
    private String username;

    //don't persist these
    private final SecretKey sessionKey;
    private final int sessionID;
    private final Socket s;
    private ConnectionManager connection;

    /**
     * Creates a client session, with the username the public key maps to in the Properties object
     *
     * @param s
     * @param k
     * @param sessionKey
     * @param p
     */
    public ClientSession(Socket s, PublicKey k, SecretKey sessionKey, Properties p) {
        this.sessionKey = sessionKey;
        this.username = p.getProperty(Keys.getBASE64ForKey(k));
        this.sessionID = nextID;
        this.s = s;
        this.pubKey = k;

        nextID++;
    }

    public void initChannels(ObjectOutputStream oos, ObjectInputStream ois) {
        connection = new ConnectionManager(oos, ois);
        connection.startListenThread();
    }

    public ConnectionManager getConnection() {
        return connection;
    }

    public Socket getSocket() {
        return s;
    }

    /**
     * Sets a function to be called when the client-side of the connection is closed.
     *
     * @param c
     */
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

    public void setPacketListener(PacketReceiveListener l) {
        connection.setPacketReceiveListener(l);
    }

    @Override
    public String toString() {
        return "[" + sessionID + "|" + username + "]";
    }

    public PublicKey getPubKey() {
        return pubKey;
    }

    public void save(Properties p) {
        p.setProperty(Keys.getBASE64ForKey(pubKey), username);
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
