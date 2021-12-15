package de.hechler.adventofcode.y21;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.hechler.adventofcode.y21.GeoUtils.Point;

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
	void testPointIterators() {
		Point p = new Point(15,15);
		
		List<Point> fourNeighbours = p.fourNeighbours();
		assertEquals("[(15,14), (14,15), (16,15), (15,16)]", 
				     fourNeighbours.toString());
		
		List<Point> eightNeighbours = p.eightNeighbours();
		assertEquals("[(14,14), (15,14), (16,14), (14,15), (16,15), (14,16), (15,16), (16,16)]", 
				     eightNeighbours.toString());
	}


}
