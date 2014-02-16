/*
 * Copyright (C) 2014 Joshua M Hertlein
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

package net.jmhertlein.core.geom;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author joshua
 */
public class Polygons {
    public static boolean polygonEdgesIntersect(List<Point2D.Float> container, List<Point2D.Float> other) {
        Point2D[] containerArray = new Point2D[container.size()], otherArray = new Point2D[other.size()];
        containerArray = container.toArray(containerArray);
        otherArray = other.toArray(otherArray);
        
        List<Line2D.Float> containerLines = new LinkedList<>(), otherLines = new LinkedList<>();
        
        for(int a = 0, b = 1; b < containerArray.length; a++, b++)
            containerLines.add(new Line2D.Float(containerArray[a], containerArray[b]));
        containerLines.add(new Line2D.Float(containerArray[containerArray.length-1], containerArray[0]));
        
        for(int a = 0, b = 1; b < otherArray.length; a++, b++)
            otherLines.add(new Line2D.Float(otherArray[a], otherArray[b]));
        otherLines.add(new Line2D.Float(otherArray[otherArray.length-1], otherArray[0]));
        
        for(Line2D.Float a : containerLines) {
            for(Line2D.Float b : otherLines) {
                if(a.intersectsLine(b))
                    return true;
            }
        }
        return false;
    }
}
