package de.hechler.adventofcode.y21;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.hechler.adventofcode.y21.GeoUtils.Area;
import de.hechler.adventofcode.y21.GeoUtils.Point;
import de.hechler.adventofcode.y21.GeoUtils.SkipPoint;

class GeoUtilsTest {

	
	@Test
	void testPointCtor() {
		Point p = new Point(2, 3);
		assertEquals("(2,3)", p.toString());
		assertEquals(2, p.x());
		assertEquals(3, p.y());
		p = new Point(0, 0);
		assertEquals("(0,0)", p.toString());
		p = new Point(-31, -21);
		assertEquals("(-31,-21)", p.toString());
		p = new Point();
		assertEquals("(0,0)", p.toString());
		p = new Point(7,8);
		Point p2 = new Point(7,8);
		assertEquals(p, p2);
		p2 = new Point(8,7);
		assertNotEquals(p, p2);
		p = new Point(p2);
		assertEquals(p, p2);
	}

	
	@Test
	void testPointOffset() {
		Point p = new Point(15,15);
		Point offsPoint = p.offset(3,4);
		assertEquals("(18,19)", offsPoint.toString());
		List<Point> offsPoints = p.offsets(3,4, 0,0, -20,-21);
		assertEquals("[(18,19), (15,15), (-5,-6)]", offsPoints.toString());
	}

	
	@Test
	void testPointNeighbourIterators() {
		Point p = new Point(15,15);
		
		List<Point> fourNeighbours = p.fourNeighbours();
		assertEquals("[(15,14), (14,15), (16,15), (15,16)]", 
				     fourNeighbours.toString());
		
		List<Point> eightNeighbours = p.eightNeighbours();
		assertEquals("[(14,14), (15,14), (16,14), (14,15), (16,15), (14,16), (15,16), (16,16)]", 
				     eightNeighbours.toString());

		p = new Point(0,0);
		fourNeighbours = p.fourNeighbours();
		assertEquals("[(0,-1), (-1,0), (1,0), (0,1)]", 
				     fourNeighbours.toString());
		
		eightNeighbours = p.eightNeighbours();
		assertEquals("[(-1,-1), (0,-1), (1,-1), (-1,0), (1,0), (-1,1), (0,1), (1,1)]", 
				     eightNeighbours.toString());
	
	}


	
	@Test
	void testAreaCtor() {
		Area a = new Area(10, 20, 5, 3);
		assertEquals("[(10,20|5,3)]", a.toString());

		Area a2 = Area.createFromTo(10, 20, 14, 22);
		assertEquals("[(10,20|5,3)]", a2.toString());
		assertEquals(a, a2);
		
		Area a3 = new Area(-2, -2, 5, 5);
		assertEquals("[(-2,-2|5,5)]", a3.toString());
		assertNotEquals(a, a3);
		
	}

	@Test
	void testAreaContains() {
		Area a = new Area(10, 100, 20, 50);
		
		assertTrue(a.contains(new Point(10,100)));
		assertTrue(a.contains(new Point(10,149)));
		assertTrue(a.contains(new Point(29,100)));
		assertTrue(a.contains(new Point(29,149)));
		
		assertFalse(a.contains(new Point(9,100)));
		assertFalse(a.contains(new Point(10,99)));
		assertFalse(a.contains(new Point(30,149)));
		assertFalse(a.contains(new Point(29,150)));

		assertTrue(a.contains(10,100));
		assertTrue(a.contains(10,149));
		assertTrue(a.contains(29,100));
		assertTrue(a.contains(29,149));
		
		assertFalse(a.contains(9,100));
		assertFalse(a.contains(10,99));
		assertFalse(a.contains(30,149));
		assertFalse(a.contains(29,150));

		assertFalse(a.contains(0,0));
		assertFalse(a.contains(-1,-1));
		
		Area a2 = new Area(-10, -10, 20, 20);
		assertTrue(a2.contains(0,0));
		assertFalse(a2.contains(10,100));
		
	}

	@Test
	void testAreaIterators() {
		List<Point> ps = new ArrayList<>();
		Area a = new Area (10,10, 4,3);
		for (Point p:a) {
			ps.add(p);
		}
		assertEquals("[(10,10), (11,10), (12,10), (13,10), "
				    + "(10,11), (11,11), (12,11), (13,11), "
				    + "(10,12), (11,12), (12,12), (13,12)]", 
				    ps.toString());
		
		List<Point> rps = new ArrayList<>();
		Iterator<Point> rit = a.reverseIterator();
		while (rit.hasNext()) {
			rps.add(rit.next());
		}
		assertEquals("[(13,12), (12,12), (11,12), (10,12), "
				    + "(13,11), (12,11), (11,11), (10,11), "
				    + "(13,10), (12,10), (11,10), (10,10)]", 
			        rps.toString());
		
		ps = new ArrayList<>();
		Iterator<Point> it = new SkipPoint(new Point(12,11), a.iterator()); 
		while (it.hasNext()) {
			ps.add(it.next());
		}
		assertEquals("[(10,10), (11,10), (12,10), (13,10), "
				    + "(10,11), (11,11), (13,11), "
				    + "(10,12), (11,12), (12,12), (13,12)]", 
			   ps.toString());
		
		ps = new ArrayList<>();
		it = new SkipPoint(new Point(10,10), a.iterator()); 
		while (it.hasNext()) {
			ps.add(it.next());
		}
		assertEquals("[(11,10), (12,10), (13,10), "
				    + "(10,11), (11,11), (12,11), (13,11), "
				    + "(10,12), (11,12), (12,12), (13,12)]", 
			   ps.toString());
		
		ps = new ArrayList<>();
		it = new SkipPoint(new Point(13,12), a.iterator()); 
		while (it.hasNext()) {
			ps.add(it.next());
		}
		assertEquals("[(10,10), (11,10), (12,10), (13,10), "
				    + "(10,11), (11,11), (12,11), (13,11), "
				    + "(10,12), (11,12), (12,12)]", 
			   ps.toString());
		
	}

}
