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
package net.jmhertlein.core.persist;

import java.io.*;

/**
 * A simple tool to do IO with deflated/serialized objects in files.
 *
 * @author Josh
 */
@Deprecated
public class Porter {

    private File target;
    private FileOutputStream fos;
    private ObjectOutputStream oos;
    private FileInputStream fis;
    private ObjectInputStream ois;

    /**
     * Makes a new Porter, pointed at the tarfile
     *
     * @param tarfile the file to target
     */
    public Porter(String tarfile) {

        target = new File(tarfile);
        fos = null;
        oos = null;
        fis = null;
        ois = null;

    }

    /**
     * Prepares the porter for output
     *
     * @return true if the porter is ready, false if it is not because some
     *         exception occurred
     * @see close()
     */
    public boolean primeOutput() {

        try {
            fos = new FileOutputStream(target);
            oos = new ObjectOutputStream(fos);
        } catch (Exception ex) {
            return false;
        }
        return true;

    }

    /**
     * Prepares the porter for input
     *
     * @return true if the porter is ready, false if it is not because some
     *         exception occurred
     * @see close()
     */
    public boolean primeInput() {
        try {
            fis = new FileInputStream(target);
            ois = new ObjectInputStream(fis);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * Outputs/writes the Object to the file as a serialized java object
     *
     * @param o the object to serialize
     *
     * @return true if output was successful, false otherwise
     */
    public boolean output(Object o) {
        try {
            oos.writeObject(o);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Reads in the object from the file
     *
     * @return the Object, or null if any error occurred
     */
    public Object input() {
        Object o = null;

        try {
            o = ois.readObject();
        } catch (Exception e) {
        }

        return o;
    }

    /**
     * Closes the porter's open connections to the file. Should be used if
     * primeOutput or primeInput were used.
     */
    public void close() {
        try {
            if (oos != null)
                oos.close();

            if (ois != null)
                ois.close();

            if (fos != null)
                fos.close();

            if (fis != null)
                fis.close();
        } catch (IOException ex) {
        }

    }

    /**
     *
     * @return the path of the file to which which Porter is pointing
     */
    public String getPath() {
        return target.toString();
    }
}
