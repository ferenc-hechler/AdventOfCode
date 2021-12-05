package de.hechler.adventofcode.y21;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * see: https://adventofcode.com/2021/day/5
 *
 */
public class Y21Day05 {

	
	private final static String INPUT_RX="([0-9]+),([0-9]+) -> ([0-9]+),([0-9]+)";
	

	private static class HVLine {
		Point from;
		Point to;
		public HVLine(Point from, Point to) {
			this.from = from;
			this.to = to;
		}
		public Set<Point> generateLinePoints() {
			Set<Point> result = new HashSet<>();
			if (from.x == to.x) {
				int minY = Math.min(from.y, to.y);
				int maxY = Math.max(from.y, to.y);
				for (int y=minY; y<=maxY; y++) {
					result.add(new Point(from.x, y));
				}
			}
			else if (from.y == to.y) {
				int minX = Math.min(from.x, to.x);
				int maxX = Math.max(from.x, to.x);
				for (int x=minX; x<=maxX; x++) {
					result.add(new Point(x, from.y));
				}
			}
			else {
				// throw new RuntimeException("unsuported HVLine "+toString());
			}
			return result;
		}
		@Override
		public String toString() {
			return "[("+from.x+","+from.y+")->("+to.x+","+to.y+")]";
		}
	}
	
	public static void mainPart1() throws FileNotFoundException {
		int maxX = 0; 
		int maxY = 0;
		List<HVLine> hvLines = new ArrayList<>();
		try (Scanner scanner = new Scanner(new File("input/y21/day05.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				int x1 = Integer.parseInt(line.replaceFirst(INPUT_RX, "$1"));
				int y1 = Integer.parseInt(line.replaceFirst(INPUT_RX, "$2"));
				int x2 = Integer.parseInt(line.replaceFirst(INPUT_RX, "$3"));
				int y2 = Integer.parseInt(line.replaceFirst(INPUT_RX, "$4"));
				maxX = Math.max(Math.max(maxX, x1),x2);
				maxY = Math.max(Math.max(maxY, y1),y2);
				Point from = new Point(x1, y1);
				Point to = new Point(x2, y2);
				HVLine hvLine = new HVLine(from, to);
				hvLines.add(hvLine);
				System.out.println(hvLine);
			}
			final int[][] matrix = new int[maxY+1][maxX+1];
			hvLines.forEach(hvLine -> hvLine.generateLinePoints().forEach(p -> matrix[p.y][p.x]++));
			System.out.println();
			System.out.println(Utils.toString(matrix));
			System.out.println();
			int sumMin2 = 0;
			for (int[] row:matrix) {
				for (int n:row) {
					if (n>=2) {
						sumMin2++;
					}
				}
			}
			System.out.println("#2+="+sumMin2);
		}
	}


	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
