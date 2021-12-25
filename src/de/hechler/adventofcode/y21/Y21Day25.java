package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import de.hechler.adventofcode.y21.GeoUtils.Area;

/**
 * see: https://adventofcode.com/2021/day/25
 *
 */
public class Y21Day25 {

	private final static String INPUT_RX = "^([>v.]+)$";

	
	public static void mainPart1() throws FileNotFoundException {
		
		List<String> inputField = new ArrayList<>();
		
		try (Scanner scanner = new Scanner(new File("input/y21/day25.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				inputField.add(line);
			}
			System.out.println(inputField);
			int height = inputField.size();
			int width = inputField.get(0).length();
			char[][] world = new char[height][width];
			for (int row=0; row<height; row++) {
				String rowInput = inputField.get(row);
				for (int col=0; col<width; col++) {
					world[row][col] = rowInput.charAt(col); 
				}
			}
			int cnt = 0;
			while (world != null) {
				System.out.println("CNT: " + (cnt++));
				System.out.println(Utils.toString(world, ""));
				world = tick(world);
			}
			System.out.println("STEPS: "+cnt);
		}
	}
	
	
	
	private static char[][] tick(char[][] world) {
		boolean changed = false;
		int height = world.length;
		int width = world[0].length;
		char[][] result = new char[height][width];
		for (int row=0; row<height; row++) {
			for (int col=0; col<width; col++) {
				result[row][col] = '.';
			}
		}
		for (int row=0; row<height; row++) {
			for (int col=0; col<width; col++) {
				if (world[row][col] == '>') { // east
					int eastCol = (col+1)%width;
					if ((world[row][eastCol] == '.') && (result[row][eastCol] == '.')) {
						changed = true;
						result[row][eastCol] = '>';
					}
					else {
						result[row][col] = '>';
					}
				}
			}
		}
		for (int row=0; row<height; row++) {
			for (int col=0; col<width; col++) {
				if (world[row][col] == 'v') { // south
					int southRow = (row+1)%height;
					if ((world[southRow][col] != 'v') && (result[southRow][col] == '.')) {
						changed = true;
						result[southRow][col] = 'v';
					}
					else {
						result[row][col] = 'v';
					}
				}
			}
		}
		if (!changed) {
			result = null;
		}
		return result;
	}



	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
