package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.awt.Point;

/**
 * see: https://adventofcode.com/2021/day/9
 *
 */
public class Y21Day09 {

	
	private final static String INPUT_RX = "^([0-9]+)$";
	
	private static class Floor {
		private int[][] height;
		private int rows;
		private int cols;
		public Floor(int[][] matrix) {
			this.height = matrix;
			this.rows = matrix.length;
			this.cols = matrix[0].length;
		}
		public List<Point> findLocalMinima() {
			List<Point> result = new ArrayList<>();
			for (int y=0; y<rows; y++) {
				for (int x=0; x<cols; x++) {
					if (checkLocalMinimum(y, x)) {
						result.add(new Point(x, y));
					}
				}
			}
			return result;
		}
		public int getHeight(int row, int col) { return height[row][col]; }
		public int getHeight(Point pos) { return height[pos.y][pos.x]; }
		private boolean checkLocalMinimum(int row, int col) {
			int h = getHeight(row, col);
			return (h<getHeight(row-1, col, h+1)) && (h<getHeight(row, col+1, h+1)) && (h<getHeight(row+1, col, h+1)) && (h<getHeight(row, col-1, h+1)); 
		}
		public int getHeight(int row, int col, int defaultValue) {
			if ((row<0) || (row >= rows) || (col<0) || (col>=cols)) {
				return defaultValue;
			}
			return getHeight(row, col); 
		}
		@Override public String toString() { return Utils.toList(height).toString().replace("], ", "\n").replace("[", "").replace("]", "").replace(", ", ""); }
	}
	
	
	public static void mainPart1() throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File("input/y21/day09.txt"))) {
			List<List<Integer>> inputNumbers = new ArrayList<>();
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				List<Integer> row = Utils.toList(Utils.toIntArray(line.toCharArray())).stream().map(c -> c-'0').collect(Collectors.toList());
				inputNumbers.add(row);
			}
			Floor floor = new Floor(Utils.toIntMatrix(inputNumbers));
			System.out.println(floor);
			List<Point> minima = floor.findLocalMinima();
			System.out.println(minima);
			int result = 0;
			for (Point minimum:minima) {
				result += floor.getHeight(minimum)+1;
			}
			System.out.println(result);
		}
	}

	
	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
