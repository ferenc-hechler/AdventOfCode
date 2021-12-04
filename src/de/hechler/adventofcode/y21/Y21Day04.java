package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * see: https://adventofcode.com/2021/day/4
 *
 */
public class Y21Day04 {

	
	private static class BingoBoard {
		int[][] board;
		boolean[][] hit;
		public BingoBoard() {
			board = new int[5][5];
			hit = new boolean[5][5];
		}
		public void init(String row1, String row2, String row3, String row4, String row5) {
			init(convertRow(row1), convertRow(row2), convertRow(row3), convertRow(row4), convertRow(row5));
		}
		private void init(int[] row1, int[] row2, int[] row3, int[] row4, int[] row5) {
			board[0] = row1;
			board[1] = row2;
			board[2] = row3;
			board[3] = row4;
			board[4] = row5;
		}
		private int[] convertRow(String row) {
			return Utils.map2intArray(row.split("\\s+"), Integer::parseInt);
		}
		public void drawn(int drawnNumber) {
			for (int row=0; row<5; row++) {
				for (int col=0; col<5; col++) {
					if (board[row][col] == drawnNumber) {
						hit[row][col]=true;
					}
				}
			}
		}
		@Override
		public String toString() {
			StringBuilder result = new StringBuilder();
			for (int row=0; row<5; row++) {
				for (int col=0; col<5; col++) {
					result.append(Utils.toFixString(board[row][col], 2));
					result.append(hit[row][col]?"H":" ").append(" ");
				}
				result.append("\n");
			}
			return result.toString();
		}
		public boolean hasWon() {
			for (int row=0; row<5; row++) {
				int sumHit = 0;
				for (int col=0; col<5; col++) {
					sumHit += (hit[row][col]?1:0);
				}
				if (sumHit == 5) {
					return true;
				}
			}
			for (int col=0; col<5; col++) {
				int sumHit = 0;
				for (int row=0; row<5; row++) {
					sumHit += (hit[row][col]?1:0);
				}
				if (sumHit == 5) {
					return true;
				}
			}
			return false;
		}
		public int calcSumUnhit() {
			int result = 0;
			for (int row=0; row<5; row++) {
				for (int col=0; col<5; col++) {
					if (!hit[row][col]) {
						result += board[row][col];
					}
				}
			}
			return result;
		}
	}
	
	public static void mainPart1() throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File("input/y21/day04.txt"))) {
			List<BingoBoard> bbs = new ArrayList<>();
			int[] drawnNumbers = Utils.map2intArray(scanner.nextLine().split("\\s*,\\s*"), Integer::parseInt);
			System.out.println(Utils.toList(drawnNumbers));
			System.out.println();
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				String row1 = line;
				String row2 = scanner.nextLine().trim();
				String row3 = scanner.nextLine().trim();
				String row4 = scanner.nextLine().trim();
				String row5 = scanner.nextLine().trim();
				BingoBoard bb = new BingoBoard();
				bb.init(row1, row2, row3, row4, row5);
				bbs.add(bb);
				System.out.println(bb.toString());
			}
			
			outer:
			for (int drawnNumber:drawnNumbers) {
				System.out.println("----------- " + drawnNumber + " ----------------");
				for(BingoBoard bb:bbs) {
					bb.drawn(drawnNumber);
					if (bb.hasWon()) {
						System.out.println(bb);
						int boardScore = bb.calcSumUnhit();
						int result = boardScore*drawnNumber;
						System.out.println("boardScore: "+boardScore);
						System.out.println("result: "+result);
						System.out.println();
						break outer; 
					}
				}
			}
		}
	}




	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
