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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import org.bukkit.Server;
import org.bukkit.block.BlockFace;

/**
 * A Serializable replacement for Bukkit's Location class
 *
 * Only stores the block-location, though (i.e. x,y,z are ints, not floats)
 *
 * @author joshua
 */
public class Location implements Serializable {

    //it looks like yaw 180 and -180 are NORTH, 0 is SOUTH, -90 is EAST, and 90 is WEST
    private final static Map<BlockFace, Float> DIRECTION_MAP = new HashMap<BlockFace, Float>() {
        {
            this.put(BlockFace.NORTH, 180f);
            this.put(BlockFace.SOUTH, 0f);
            this.put(BlockFace.EAST, -90f);
            this.put(BlockFace.WEST, 90f);

            this.put(BlockFace.SOUTH_EAST, -45f);
            this.put(BlockFace.SOUTH_WEST, 45f);
            this.put(BlockFace.NORTH_EAST, -135f);
            this.put(BlockFace.NORTH_WEST, 135f);

            this.put(BlockFace.SOUTH_SOUTH_WEST, 22f);
            this.put(BlockFace.SOUTH_SOUTH_EAST, -22f);

            this.put(BlockFace.WEST_NORTH_WEST, 112f);
            this.put(BlockFace.WEST_SOUTH_WEST, 67f);

            this.put(BlockFace.EAST_NORTH_EAST, -112f);
            this.put(BlockFace.EAST_SOUTH_EAST, -67f);

            this.put(BlockFace.NORTH_NORTH_WEST, 157f);
            this.put(BlockFace.NORTH_NORTH_EAST, -157f);
        }
    };

    private static final long serialVersionUID = 6832150941951045383L;
    private String world;
    private float x, y, z;
    private float pitch, yaw;

    private Location() {
    }

    /**
     *
     * @param world
     * @param x
     * @param y
     * @param z
     */
    public Location(String world, float x, float y, float z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = 0;
        this.yaw = 0;
    }

    public Location(String world, float x, float y, float z, float pitch, float yaw) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
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
    public float getX() {
        return x;
    }

    /**
     *
     * @param x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     *
     * @return
     */
    public float getY() {
        return y;
    }

    /**
     *
     * @param y
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     *
     * @return
     */
    public float getZ() {
        return z;
    }

    /**
     *
     * @param z
     */
    public void setZ(float z) {
        this.z = z;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }


    /**
     *
     * @param loc
     *
     * @return
     */
    public static Location convertFromBukkitLocation(org.bukkit.Location loc) {
        return new Location(loc.getWorld().getName(), (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), loc.getPitch(), loc.getYaw());
    }

    /**
     *
     * @param s
     * @param loc
     *
     * @return
     */
    public static org.bukkit.Location convertToBukkitLocation(Server s, Location loc) {
        return new org.bukkit.Location(s.getWorld(loc.world), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    public List<String> toList() {
        LinkedList<String> ret = new LinkedList<>();
        ret.add("" + x);
        ret.add("" + y);
        ret.add("" + z);
        ret.add(world);
        ret.add("" + pitch);
        ret.add("" + yaw);

        return ret;
    }

    public static Location fromList(List<String> list) {
        Location ret = new Location();

        ret.x = Float.parseFloat(list.get(0));
        ret.y = Float.parseFloat(list.get(1));
        ret.z = Float.parseFloat(list.get(2));
        ret.world = list.get(3);
        ret.pitch = list.size() >= 5 ? Float.parseFloat(list.get(4)) : 0;
        ret.yaw = list.size() >= 6 ? Float.parseFloat(list.get(5)) : 0;

        return ret;
    }

    @Override
    public String toString() {
        return String.format("(%f, %f, %f, %s, %f, %f)", x, y, z, world, pitch, yaw);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.world);
        hash = 23 * hash + Float.floatToIntBits(this.x);
        hash = 23 * hash + Float.floatToIntBits(this.y);
        hash = 23 * hash + Float.floatToIntBits(this.z);
        hash = 23 * hash + Float.floatToIntBits(this.pitch);
        hash = 23 * hash + Float.floatToIntBits(this.yaw);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Location other = (Location) obj;
        if (!Objects.equals(this.world, other.world))
            return false;
        if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x))
            return false;
        if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y))
            return false;
        if (Float.floatToIntBits(this.z) != Float.floatToIntBits(other.z))
            return false;
        if (Float.floatToIntBits(this.pitch) != Float.floatToIntBits(other.pitch))
            return false;
        if (Float.floatToIntBits(this.yaw) != Float.floatToIntBits(other.yaw))
            return false;
        return true;
    }
    

    public static Location parseLocation(String s) throws NumberFormatException {
        s = s.replaceFirst("[(]", "").replaceFirst("[)]", "");
        Scanner scan = new Scanner(s);
        scan.useDelimiter(",");

        Float x = Float.parseFloat(scan.next().trim()),
                y = Float.parseFloat(scan.next().trim()),
                z = Float.parseFloat(scan.next().trim());
        String world = scan.next().trim();
        float pitch = Float.parseFloat(scan.next().trim()),
                yaw = Float.parseFloat(scan.next().trim());

        scan.close();
        return new Location(world, x, y, z, pitch, yaw);
    }

    /**
     *
     * @param yaw
     *
     * @return the block face that is closest to the direction the yaw is
     *         pointing
     */
    public static BlockFace getBlockFaceFromYaw(float yaw) {
        //initialize it with the yaw for NORTH that we didn't put in the map
        BlockFace smallestDeltaFace = BlockFace.NORTH;
        float smallestDeltaYaw = Math.abs(-180 - yaw);
        for (Map.Entry<BlockFace, Float> entry : DIRECTION_MAP.entrySet()) {
            float curDelta = Math.abs(entry.getValue() - yaw);
            if (curDelta < smallestDeltaYaw) {
                smallestDeltaYaw = curDelta;
                smallestDeltaFace = entry.getKey();
            }
        }

        return smallestDeltaFace;
    }

    /**
     * Calculates a yaw value that represents the opposite direction of the
     * specified yaw.
     *
     * Specifically: 1. Shift yaw up by 180 (shift range from [-180,180] to
     * [0,360] 2. Add 180 to the yaw (to point it in the opposite direction) 3.
     * Mod the yaw by 360 (make the addition 'wrap around' if we passed 360 4.
     * Shift the yaw down by 180 (shift range from [0,360] back to [-180,180]
     *
     * @param yaw a yaw value in the range [-180,180]
     *
     * @return a yaw value pointing in the opposite direction of the specified
     *         yaw
     */
    public static float getYawInOppositeDirection(float yaw) {
        return ((yaw + 360) % 360) - 180;
    }
}
