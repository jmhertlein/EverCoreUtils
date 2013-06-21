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
package net.jmhertlein.core.location;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import org.bukkit.Server;

/**
 *
 * @author joshua
 */
public class Location implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6832150941951045383L;
	private String world;
    private int x, y, z;

    /**
     *
     * @param world
     * @param x
     * @param y
     * @param z
     */
    public Location(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     *
     * @return
     */
    public String getWorld() {
        return world;
    }

    /**
     *
     * @param world
     */
    public void setWorld(String world) {
        this.world = world;
    }

    /**
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     *
     * @return
     */
    public int getZ() {
        return z;
    }

    /**
     *
     * @param z
     */
    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Location other = (Location) obj;
        if ((this.world == null) ? (other.world != null) : !this.world.equals(other.world)) {
            return false;
        }
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.z != other.z) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.world != null ? this.world.hashCode() : 0);
        hash = 47 * hash + this.x;
        hash = 47 * hash + this.y;
        hash = 47 * hash + this.z;
        return hash;
    }

    /**
     *
     * @param loc
     * @return
     */
    public static Location convertFromBukkitLocation(org.bukkit.Location loc) {
        return new Location(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    /**
     *
     * @param s
     * @param loc
     * @return
     */
    public static org.bukkit.Location convertToBukkitLocation(Server s, Location loc) {
        return new org.bukkit.Location(s.getWorld(loc.world), loc.getX(), loc.getY(), loc.getZ());
    }

    public List<String> toList() {
        LinkedList<String> ret = new LinkedList<>();
        ret.add("" + x);
        ret.add("" + y);
        ret.add("" + z);
        ret.add(world);

        return ret;
    }

    public static Location fromList(List<String> list) {
        Location ret = new Location(null, 0, 0, 0);

        ret.x = Integer.parseInt(list.get(0));
        ret.y = Integer.parseInt(list.get(1));
        ret.z = Integer.parseInt(list.get(2));
        ret.world = list.get(3);

        return ret;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d, %s)", x, y, z, world);
    }

    public static Location parseLocation(String s) throws NumberFormatException {
        s = s.replaceFirst("[(]", "").replaceFirst("[)]", "");
        Scanner scan = new Scanner(s);
        scan.useDelimiter(",");

        int x = Integer.parseInt(scan.next().trim()),
                y = Integer.parseInt(scan.next().trim()),
                z = Integer.parseInt(scan.next().trim());

        String world = scan.next().trim();
        scan.close();
        return new Location(world, x, y, z);
    }
}
