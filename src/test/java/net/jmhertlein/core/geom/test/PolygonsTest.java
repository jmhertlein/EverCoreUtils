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

package net.jmhertlein.core.geom.test;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import net.jmhertlein.core.geom.Polygons;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author joshua
 */
public class PolygonsTest {

    /**
     * Test of polygonEdgesIntersect method, of class Polygons.
     */
    @Test
    public void testPolygonEdgesIntersect() {
        List<Point2D.Float> poly1 = new LinkedList<>(), poly2 = new LinkedList<>(),
                poly3 = new LinkedList<>();
        
        poly1.add(new Point2D.Float(-1, 0));
        poly1.add(new Point2D.Float(1, 0));
        poly1.add(new Point2D.Float(0, 1));
        
        poly2.add(new Point2D.Float(-1, -.5f));
        poly2.add(new Point2D.Float(1, -.5f));
        poly2.add(new Point2D.Float(0, .5f));
        
        poly3.add(new Point2D.Float(-1, 20));
        poly3.add(new Point2D.Float(1, 20));
        poly3.add(new Point2D.Float(0, 21));
        
        assertTrue(Polygons.polygonEdgesIntersect(poly1, poly2));
        assertFalse(Polygons.polygonEdgesIntersect(poly1, poly3));
    }

    @Test
    public void testPentagonEdgesIntersect() {
        List<Point2D.Float> poly1 = new LinkedList<>(), poly2 = new LinkedList<>(),
                poly3 = new LinkedList<>();
        
        poly1.add(new Point2D.Float(-1, 1));
        poly1.add(new Point2D.Float(1, 1));
        poly1.add(new Point2D.Float(2, 0));
        poly1.add(new Point2D.Float(1, -1));
        poly1.add(new Point2D.Float(-1, -1));
        
        poly2.add(new Point2D.Float(-4, -2));
        poly2.add(new Point2D.Float(0, 4));
        poly2.add(new Point2D.Float(4, 2));
        poly2.add(new Point2D.Float(4, -4));
        poly2.add(new Point2D.Float(-5, -3));
        
        poly3.add(new Point2D.Float(-1, 1));
        poly3.add(new Point2D.Float(1, 1));
        poly3.add(new Point2D.Float(-2, 0));
        poly3.add(new Point2D.Float(1, -1));
        poly3.add(new Point2D.Float(-1, -1));
        
        assertFalse(Polygons.polygonEdgesIntersect(poly1, poly2));
        assertTrue(Polygons.polygonEdgesIntersect(poly1, poly3));
    }
  
}
