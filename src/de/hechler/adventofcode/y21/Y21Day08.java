package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * see: https://adventofcode.com/2021/day/8
 *
 */
public class Y21Day08 {

	/*
	 * 
	 *   0:      1:      2:      3:      4:
	 *  aaaa    ....    aaaa    aaaa    ....
	 * b    c  .    c  .    c  .    c  b    c
	 * b    c  .    c  .    c  .    c  b    c
	 *  ....    ....    dddd    dddd    dddd
	 * e    f  .    f  e    .  .    f  .    f
	 * e    f  .    f  e    .  .    f  .    f
	 *  gggg    ....    gggg    gggg    ....
	 * 
	 *   5:      6:      7:      8:      9:
	 *  aaaa    aaaa    aaaa    aaaa    aaaa
	 * b    .  b    .  .    c  b    c  b    c
	 * b    .  b    .  .    c  b    c  b    c
	 *  dddd    dddd    ....    dddd    dddd
	 * .    f  e    f  .    f  e    f  .    f
	 * .    f  e    f  .    f  e    f  .    f
	 *  gggg    gggg    ....    gggg    gggg
	 *  
	 */
	
	private final static int D_A =  1; 
	private final static int D_B =  2; 
	private final static int D_C =  4; 
	private final static int D_D =  8; 
	private final static int D_E = 16; 
	private final static int D_F = 32; 
	private final static int D_G = 64;
	
	private final static int N_0 = D_A + D_B + D_C + D_E + D_F + D_G;
	private final static int N_1 = D_C + D_F;
	private final static int N_2 = D_A + D_C + D_D + D_E + D_G;
	private final static int N_3 = D_A + D_C + D_D + D_F + D_G;
	private final static int N_4 = D_B + D_C + D_D + D_F;
	private final static int N_5 = D_A + D_B + D_D + D_F + D_G;
	private final static int N_6 = D_A + D_B + D_D + D_E + D_F + D_G;
	private final static int N_7 = D_A + D_C + D_F;
	private final static int N_8 = D_A + D_B + D_C + D_D + D_E + D_F + D_G;
	private final static int N_9 = D_A + D_B + D_C + D_D + D_F + D_G;
	
	private final static int MASK_ALL = D_A + D_B + D_C + D_D + D_E + D_F + D_G;
	
	private final static int[] DS = {D_A, D_B, D_C, D_D, D_E, D_F, D_G};
	private final static int[] MASKS = {N_0, N_1, N_2, N_3, N_4, N_5, N_6, N_7, N_8, N_9};
	private final static int[] BITS = {6, 2, 5, 5, 4, 5, 6, 3, 7, 6};
	private final static int[][] NUMS_BY_SIZE = {{}, {}, {1}, {7}, {4}, {2, 3, 5}, {0, 6, 9}, {8}};
	
	private final static String INPUT_RX = "([a-g ]+)[|]([a-g ]+)";
	
	public static void mainPart1() throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File("input/y21/day08.txt"))) {
			int[] sum1478 = new int[1];
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				List<String> input = Utils.toList(line.replaceFirst(INPUT_RX, "$1").trim().split(" +"));
				List<String> output = Utils.toList(line.replaceFirst(INPUT_RX, "$2").trim().split(" +"));
				System.out.println(input+ " -> " + output);
				output.forEach(inp -> {
					int len = inp.length();
					if ((len == 2) || (len == 4) || (len == 3) || (len == 7)) {
						sum1478[0]++;
					}
				});
			}
			System.out.println(sum1478[0]);
		}
	}

	
	private static class Segment {
		private int mask;
		public Segment() {
			mask = MASK_ALL; 
		}
		public int getMask() {
			return mask;
		}
		public boolean isUnique() {
			return Integer.bitCount(mask) == 1;
		}
		public boolean restrict(int restrictMask) {
			int oldMask = mask;
			mask = mask & restrictMask;
			return oldMask != mask;
		}
		public boolean removeMask(int maskToRemove) {
			int oldMask = mask;
			mask = mask & ~maskToRemove;
			return oldMask != mask;
		}
		@Override
		public String toString() {
			return Integer.toBinaryString(mask);
		}
	}
	
	private static class DigiNum {
		private List<Segment> segments;
		public DigiNum() {
			segments = new ArrayList<>();
		}
		public void addSegment(Segment segment) {
			segments.add(segment);
		}
		public List<Integer> getPossibleNumbers() {
			List<Integer> result = new ArrayList<>();
			int allOr = 0;
			for (Segment segment:segments) {
				allOr |= segment.getMask();
			}
			for (int n:NUMS_BY_SIZE[segments.size()]) {
				if ((MASKS[n] & allOr) == MASKS[n]) {
					result.add(n);
				}
			}
			return result;
		}
		public boolean restrict(int n) {
			boolean changed = false;
			for (Segment segment:segments) {
				boolean changes = segment.restrict(MASKS[n]);
				changed = changed || changes;
			}
			return changed;
		}
		/**
		 * iterate over all subsets of the segments (except trivial empty set and complete set).
		 * check if union of all possible mask from this group has exactly the number of bits of the group size.
		 * this means, these bits can not be part of the remaining segments. 
		 * Remove these masks from the remaining segments.  
		 * @return true, if there were changes
		 */
		public boolean restrictMasks() {
			boolean changed = false;
			List<List<Segment>> subSetsSegments = Utils.createSubsets(segments); 
			for (List<Segment> subSetSegments:subSetsSegments) {
				int allOr = 0;
				for (Segment segment:subSetSegments) {
					allOr |= segment.getMask();
				}
				if (Integer.bitCount(allOr) == subSetSegments.size()) {
					Set<Segment> remaining = new HashSet<>(segments);
					remaining.removeAll(subSetSegments);
					for (Segment segment:remaining) {
						boolean changes = segment.removeMask(allOr);
						changed = changed || changes;
					}
				}
			}
			return changed;
		}
		@Override
		public String toString() {
			return "("+getPossibleNumbers()+")";
		}
	}
	
	
	
	public static void mainPart2() throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File("input/y21/day08simple.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				List<String> input = Utils.toList(line.replaceFirst(INPUT_RX, "$1").trim().split(" +"));
				List<String> output = Utils.toList(line.replaceFirst(INPUT_RX, "$2").trim().split(" +"));
				List<String> allInput = new ArrayList<>(input);
				allInput.addAll(output);
				System.out.println("---------------");
				System.out.println(allInput);
				Map<Character, Segment> segments = new HashMap<>();
				for (char c='a'; c<='g'; c++) {
					segments.put(c, new Segment());
				}
				List<DigiNum> digits = new ArrayList<>();
				for (String inp:allInput) {
					DigiNum digi = new DigiNum();
					inp.chars().forEach(c -> {
						digi.addSegment(segments.get((char)c));
					});
					digits.add(digi);
				}
				boolean changed = true;
				while (changed) {
					changed = false;
					for (DigiNum digi:digits) {
						List<Integer> possibileNums = digi.getPossibleNumbers();
						if (possibileNums.size() == 1) {
							boolean changes = digi.restrict(possibileNums.get(0));
							changed = changed || changes;
						}
						boolean changes = digi.restrictMasks();
						changed = changed || changes;
					}
					System.out.println(digits);
				}
				System.out.println("FINAL:");
				System.out.println(digits);
				System.out.println(segments);
			}
		}
	}

	
	
	public static void main(String[] args) throws FileNotFoundException {
		mainPart2();
	}

	
}
