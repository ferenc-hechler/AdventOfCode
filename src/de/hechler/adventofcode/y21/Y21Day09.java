package de.hechler.adventofcode.y21;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * see: https://adventofcode.com/2021/day/9
 *
 */
public class Y21Day09 {

	
	private final static String INPUT_RX = "^([0-9]+)$";
	
	private static class Floor {
		private int[][] height;
		private int[][] flood;
		private int[][] nextFlood;
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
		private boolean checkLocalMinimum(int row, int col) {
			int h = getHeight(row, col);
			return (h<getHeight(row-1, col, h+1)) && (h<getHeight(row, col+1, h+1)) && (h<getHeight(row+1, col, h+1)) && (h<getHeight(row, col-1, h+1)); 
		}
		public void initFlood(List<Point> minima) {
			flood = new int[rows][cols];
			nextFlood = new int[rows][cols];
			for (int i=0; i<minima.size(); i++) {
				Point minimum = minima.get(i);
				flood[minimum.y][minimum.x] = (i+1);
				nextFlood[minimum.y][minimum.x] = (i+1);
			}
		}
		/**
		 * 
		 * @param h
		 */
		public void flood(int h, Set<Integer> ignoreBasins) {
			calcNextFlood(h, ignoreBasins);
			calcFlood(h, ignoreBasins);
		}

		private void calcFlood(int h, Set<Integer> ignoreBasins) {
			boolean changed = true;
			while (changed) {
				changed = directFlood(h, ignoreBasins);
			}
		}
		private boolean directFlood(int h, Set<Integer> ignoreBasins) {
			boolean changed = false;
			for (int r=0; r<rows; r++) {
				for (int c=0; c<cols; c++) {
					if (getHeight(r, c) > h) {
						continue;
					}
					int basin = getFlood(r, c);
					if ((basin != 0) || ignoreBasins.contains(basin)) {
						continue;
					}
					Set<Integer> neighbourBasins = getNeighbourBasins(r, c);
					if (neighbourBasins.isEmpty()) {
						continue;
					}
					int newBasin = neighbourBasins.iterator().next();
					if (ignoreBasins.contains(newBasin)) {
						continue;
					}
					flood[r][c] = newBasin; 
					changed = true;
				}
			}
			return changed;
		}

		private void calcNextFlood(int h, Set<Integer> conflictBasins) {
			boolean changed = true;
			while (changed) {
				changed = directNextFlood(h, conflictBasins);
			}
		}
		private boolean directNextFlood(int h, Set<Integer> conflictBasins) {
			boolean changed = false;
			for (int r=0; r<rows; r++) {
				for (int c=0; c<cols; c++) {
					if (getHeight(r, c) > h) {
						continue;
					}
					int basin = getNextFlood(r, c);
					if (conflictBasins.contains(basin)) {
						continue;
					}
					Set<Integer> neighbourBasins = getNeighbourBasins(r, c);
					if (basin != 0) {
						neighbourBasins.add(basin);
						if (neighbourBasins.size() > 1) {
							conflictBasins.addAll(neighbourBasins);
						}
						continue;
					}
					if (neighbourBasins.isEmpty()) {
						continue;
					}
					if (neighbourBasins.size() > 1) {
						conflictBasins.addAll(neighbourBasins);
						continue;
					}
					nextFlood[r][c] = neighbourBasins.iterator().next();
					changed = true;
				}
			}
			return changed;
		}
		private Set<Integer> getNeighbourBasins(int r, int c) {
			Set<Integer> result = new HashSet<>();
			result.add(getNextFlood( r-1, c   ));
			result.add(getNextFlood( r,   c+1 ));
			result.add(getNextFlood( r+1, c   ));
			result.add(getNextFlood( r,   c-1 ));
			result.remove(0);
			return result;
		}
		private int getNextFlood(int r, int c) {
			if (!isValidPosition(r,c)) {
				return 0;
			}
			return nextFlood[r][c];
		}
		
		private int getFlood(int r, int c) {
			if (!isValidPosition(r,c)) {
				return 0;
			}
			return flood[r][c];
		}
		
		/**
		 * @return defaultValue if row, col is out of range.
		 */
		public int getHeight(int row, int col, int defaultValue) {
			if (!isValidPosition(row, col)) {
				return defaultValue;
			}
			return getHeight(row, col); 
		}
		
		private boolean isValidPosition(int r, int c) {
			return (r>=0) && (r<rows) && (c>=0) && (c<cols);
		}
		public int getHeight(int row, int col) { return height[row][col]; }
		public int getHeight(Point pos) { return height[pos.y][pos.x]; }
		
		@Override public String toString() { return Utils.toList(height).toString().replace("], ", "\n").replace("[", "").replace("]", "").replace(", ", ""); }
		public String toNextFlood() {
			StringBuilder result = new StringBuilder();
			for (int row=0; row<rows; row++) {
				for (int col=0; col<cols; col++) {
					String f = ".";
					int basin = getNextFlood(row, col);
					if (basin != 0) {
						f = Integer.toString(basin);
					}
					result.append(f);
				}
				result.append("\n");
			}
			return result.toString();
		}
		public String toFlood() {
			StringBuilder result = new StringBuilder();
			for (int row=0; row<rows; row++) {
				for (int col=0; col<cols; col++) {
					String f = ".";
					int basin = getFlood(row, col);
					if (basin != 0) {
						f = Integer.toString(basin);
					}
					result.append(f);
				}
				result.append("\n");
			}
			return result.toString();
		}
		public Map<Integer, Integer> collectBasinSizes() {
			Map<Integer, Integer> result = new HashMap<>();
			for (int row=0; row<rows; row++) {
				for (int col=0; col<cols; col++) {
					int basin = getFlood(row, col);
					if (basin != 0) {
						Integer sum = result.get(basin);
						if (sum == null) {
							sum = 0;
						}
						result.put(basin, sum+1);
					}
				}
			}
			return result;
		}
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

	public static void mainPart2() throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File("input/y21/day09test.txt"))) {
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
			floor.initFlood(minima);
			System.out.println(floor.toNextFlood());
			Set<Integer> finishedBasins = new HashSet<>();
			for (int h=1; h<9; h++) {
				System.out.println("----- flooding "+h+" -----");
				floor.flood(h, finishedBasins);
				System.out.println(floor.toFlood());
				System.out.println("FINISHED: " + finishedBasins);
			}
			
			Map<Integer, Integer> basinSizes = floor.collectBasinSizes();
			System.out.println(basinSizes);
			List<Integer> bSizes = new ArrayList<>(basinSizes.values());
			Collections.sort(bSizes, (s1,s2)-> Integer.compare(s2, s1));
			System.out.println(bSizes);
			System.out.println("3 Biggest: " + (bSizes.get(0)*bSizes.get(1)*bSizes.get(2)));
		}
	}

	
	public static void main(String[] args) throws FileNotFoundException {
		mainPart2();
	}

	
}
