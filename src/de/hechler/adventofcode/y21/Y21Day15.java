package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * see: https://adventofcode.com/2021/day/14
 *
 */
public class Y21Day15 {

	private final static String INPUT_RX = "^([0-9]+)$";

	
	public static void mainPart1() throws FileNotFoundException {
		
		try (Scanner scanner = new Scanner(new File("input/y21/day15example.txt"))) {
			List<List<Integer>> rowlist = new ArrayList<>();
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX2 '"+INPUT_RX+"'");
				}
				List<Integer> row = Utils.toList(Utils.toIntArray(line.replaceFirst(INPUT_RX, "$1").toCharArray())).stream().map(c -> c-'0').collect(Collectors.toList());
				rowlist.add(row);
			}
			
			int[][] matrix = Utils.toIntMatrix(rowlist);
			System.out.println(Utils.toString(matrix, ""));
			int rows = matrix.length;
			int cols = matrix[0].length;
			int[][] paths = new int[rows][cols];
			for (int row=0; row<rows;row++) {
				for (int col=0; col<cols;col++) {
					paths[row][col] = Integer.MAX_VALUE;
				}
			}
			paths[0][0] = 0;
			for (int i=0; i<1000; i++) {
				for (int row=0; row<rows;row++) {
					for (int col=0; col<cols;col++) {
						int min = findMinNeighbour(paths, row, col, rows, cols);
						if (min != Integer.MAX_VALUE) {
							paths[row][col] = Math.min(paths[row][col],min + matrix[row][col]);
						}
					}
				}
//				System.out.println(Utils.toString(paths));
				for (int row=rows-1; row>=0;row--) {
					for (int col=cols-1; col>=0;col--) {
						int min = findMinNeighbour(paths, row, col, rows, cols);
						if (min != Integer.MAX_VALUE) {
							paths[row][col] = Math.min(paths[row][col],min + matrix[row][col]);
						}
					}
				}
				if (i%100==0) {
					System.out.println(paths[rows-1][cols-1]);
				}
			}
			System.out.println(paths[rows-1][cols-1]);
		}
	}


	public static void mainPart2() throws FileNotFoundException {
		
		try (Scanner scanner = new Scanner(new File("input/y21/day15.txt"))) {
			List<List<Integer>> rowlist = new ArrayList<>();
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX2 '"+INPUT_RX+"'");
				}
				List<Integer> row = Utils.toList(Utils.toIntArray(line.replaceFirst(INPUT_RX, "$1").toCharArray())).stream().map(c -> c-'0').collect(Collectors.toList());
				rowlist.add(row);
			}
			
			rowlist = createBigMatrix(rowlist);
			
			int[][] matrix = Utils.toIntMatrix(rowlist);
			System.out.println(Utils.toString(matrix, ""));
			int rows = matrix.length;
			int cols = matrix[0].length;
			int[][] paths = new int[rows][cols];
			for (int row=0; row<rows;row++) {
				for (int col=0; col<cols;col++) {
					paths[row][col] = Integer.MAX_VALUE;
				}
			}
			paths[0][0] = 0;
			for (int i=0; i<1000; i++) {
				for (int row=0; row<rows;row++) {
					for (int col=0; col<cols;col++) {
						int min = findMinNeighbour(paths, row, col, rows, cols);
						if (min != Integer.MAX_VALUE) {
							paths[row][col] = Math.min(paths[row][col],min + matrix[row][col]);
						}
					}
				}
//				System.out.println(Utils.toString(paths));
				for (int row=rows-1; row>=0;row--) {
					for (int col=cols-1; col>=0;col--) {
						int min = findMinNeighbour(paths, row, col, rows, cols);
						if (min != Integer.MAX_VALUE) {
							paths[row][col] = Math.min(paths[row][col],min + matrix[row][col]);
						}
					}
				}
				if (i%100==0) {
					System.out.println(paths[rows-1][cols-1]);
				}
			}
			System.out.println(paths[rows-1][cols-1]);
		}
	}


	private static List<List<Integer>> createBigMatrix(List<List<Integer>> rowlist) {
		List<List<Integer>> result = new ArrayList<>();
		for (int y=0; y<5; y++) {
			for (List<Integer> row:rowlist) {
				List<Integer> resultRow = new ArrayList<>();
				for (int x=0; x<5; x++) {
					final int add = x+y;
					resultRow.addAll(row.stream().map(n -> (n+add-1)%9+1).collect(Collectors.toList()));
				}
				result.add(resultRow);
			}
		}
		return result;
	}


	private static int findMinNeighbour(int[][] paths, int row, int col, int rows, int cols) {
		int result = get(paths, row-1, col, rows, cols);
		result = Math.min(result,  get(paths, row, col-1, rows, cols));
		result = Math.min(result,  get(paths, row+1, col, rows, cols));
		result = Math.min(result,  get(paths, row, col+1, rows, cols));
		return result;
	}


	private static int get(int[][] mat, int row, int col, int rows, int cols) {
		if ((row<0) || (row>=rows) || (col<0) || (col>=cols)) {
			return Integer.MAX_VALUE;
		}
		return mat[row][col];
	}


	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
