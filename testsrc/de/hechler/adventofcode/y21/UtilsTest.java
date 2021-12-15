package de.hechler.adventofcode.y21;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UtilsTest {

	@Test
	void mapArrayTest() {
		String lineNums = "2, 16, 9, 4, 33, 5";
		String[] stringNums = lineNums.split("\\s*,\\s*");
		Integer[] intNums = Utils.mapArray(stringNums, Integer::parseInt);
		for (int intNum: intNums) {
			System.out.println(intNum);
		}
		assertEquals(  6, intNums.length);
		assertEquals(  2, intNums[0]);
		assertEquals( 16, intNums[1]);
		assertEquals(  9, intNums[2]);
		assertEquals(  4, intNums[3]);
		assertEquals( 33, intNums[4]);
		assertEquals(  5, intNums[5]);
	}

}
