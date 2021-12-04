package de.hechler.adventofcode.y21;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
	}

}
