package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
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
	
	private final static int[] MASKS = {N_0, N_1, N_2, N_3, N_4, N_5, N_6, N_7, N_8, N_9};
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

	/**
	 * A Segment is one of the 7 segments in the display.
	 * It is not known, which of the 7 it is at the beginning.
	 * So, the segments begins with a possible MASK for all 7 positions.
	 * Over time there will be restrictions on the segments,
	 * so it can be sharepened, which position it is, until it is
	 * clear which exact position it is. 
	 * which reduce   
	 */
	private static class Segment {
		/** superposition of all posible assignments */
		private int mask;
		public Segment() { mask = MASK_ALL; }
		public int getMask() { return mask; }
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
		@Override public String toString() { return Integer.toBinaryString(mask); }
	}
	
	/**
	 * calculate mask which contains all segments.
	 * @param segments
	 * @return the union of all masks in the list of segments.
	 */
	private static int unionMask(List<Segment> segments) {
		int result = 0;
		for (Segment segment:segments) {
			result |= segment.getMask();
		}
		return result;
	}


	/**
	 * DigiNum is a number which is setup of a list of segments.
	 * From the beginning it is not clear, which segment represents which position.
	 * 
	 * The Segment instances are shared between all digits which build one set of input.
	 * So changes in a segment in digit1 will have impact on the same segment in digit2.
	 */
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
			int allOr = unionMask(segments);
			for (int n:NUMS_BY_SIZE[segments.size()]) {
				boolean ok = (MASKS[n] & allOr) == MASKS[n];
				if (ok) {
					// all complete subgroups must be completely contained in the digit mask
					List<List<Segment>> completeSubgroups = getCompleteSubgroups(segments);
					for (List<Segment> subgroup:completeSubgroups) {
						int subgroupMask = unionMask(subgroup);
						ok = ok && ((subgroupMask & MASKS[n]) == subgroupMask);
					}
				}
				if (ok) {
					result.add(n);
				}
			}
			return result;
		}
		/**
		 * This digit was identified to be the given parameter n.
		 * Based on this knwoledge, the mask of all contained segments can be restricted 
		 * to the mask of the detected number (MASKS[n]).
		 *   
		 * @param n
		 * @return true, if there were any changes in the segments. This is used to detect, when the inference is finished.
		 */
		public boolean restrictToIdentifiedNumber(int n) {
			boolean changed = false;
			for (Segment segment:segments) {
				boolean changes = segment.restrict(MASKS[n]);
				changed = changed || changes;
			}
			return changed;
		}
		/**
		 * Iterate over all complete subgroups of segments.
		 * The union mask of a subgroup can not be part of any other segment outside the subgroup.
		 * So, remove the mask from all other segments.
		 * 
		 * @return true, if there were changes (detect progress).
		 */
		public boolean restrictMasks() {
			boolean changed = false;
			List<List<Segment>> completeSubgroups = getCompleteSubgroups(segments); 
			for (List<Segment> completeSubgroup:completeSubgroups) {
				int subgroupMask = unionMask(completeSubgroup);
				Set<Segment> remaining = new HashSet<>(segments);
				remaining.removeAll(completeSubgroup);
				for (Segment segment:remaining) {
					boolean changes = segment.removeMask(subgroupMask);
					changed = changed || changes;
				}
			}
			return changed;
		}
		/**
		 * Calculate the list of all complete subgroups.
		 * A complete subgroup is a group of segments, 
		 * whose union mask has the same cardinality as the subgroup itself.
		 * This means, each segment is assigned to one of the positions in the union mask and 
		 * the union mask is completely filled with the segments from this subgroup. 
		 * 
		 * @param segments
		 * @return
		 */
		private List<List<Segment>> getCompleteSubgroups(List<Segment> segments) {
			List<List<Segment>> result = new ArrayList<>();
			List<List<Segment>> allSubsets = Utils.createSubsets(segments);
			for (List<Segment> subset:allSubsets) {
				if (subset.isEmpty()) {
					continue;
				}
				int uMask = unionMask(subset);
				if (Integer.bitCount(uMask) == subset.size()) {
					result.add(subset);
				}
			}
			return result;
		}
		@Override
		public String toString() {
			return "("+getPossibleNumbers()+")";
		}
	}
	

	public static void mainPart2() throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File("input/y21/day08.txt"))) {
			int sumCodes = 0;
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
				
				// create a new set of sequences for the letters "a".."g"
				Map<Character, Segment> segments = new HashMap<>();
				for (char c='a'; c<='g'; c++) {
					segments.put(c, new Segment());
				}
				
				// for each input code like "acfg" create a digit
				// not only the left, but also the right side from "|". 
				// All digits share the same segment instances, 
				// so "c" in the digit "acf" is the same instance as "c" in the digit "cf".
				List<DigiNum> digits = new ArrayList<>();
				for (String inp:allInput) {
					DigiNum digi = new DigiNum();
					inp.chars().forEach(c -> {
						digi.addSegment(segments.get((char)c));
					});
					digits.add(digi);
				}
				
				// inference until there are no more changes
				boolean changed = true;
				while (changed) {
					changed = false;
					for (DigiNum digi:digits) {
						List<Integer> possibileNums = digi.getPossibleNumbers();
						if (possibileNums.size() == 1) {
							boolean changes = digi.restrictToIdentifiedNumber(possibileNums.get(0));
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

				// translate code for the output
				int code = 0;
				for (String out:output) {
					DigiNum digi = new DigiNum();
					out.chars().forEach(c -> {
						digi.addSegment(segments.get((char)c));
					});
					List<Integer> codeN = digi.getPossibleNumbers();
					if (codeN.size()!=1) {
						throw new RuntimeException("none unique code detected "+digi);
					}
					code = code*10 + codeN.get(0);
				}
				System.out.println("CODE: "+code);
				sumCodes += code;
			}
			System.out.println("SUM of all codes: "+sumCodes); // SUM of all codes: 1091609
		}
	}

	
	
	public static void main(String[] args) throws FileNotFoundException {
		mainPart2();
	}

	
}
