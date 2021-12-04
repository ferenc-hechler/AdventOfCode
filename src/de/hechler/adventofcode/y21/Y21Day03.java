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
 * see: https://adventofcode.com/2021/day/3
 *
 */
public class Y21Day03 {

	
	private final static String INPUT_RX = "^([01]+)$";
	
	public static void mainPart1() throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File("input/y21/day03.txt"))) {
			int bits = 0;
			int sumZeros[] = null;
			int sumOnes[] = null;
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				if (bits == 0) {
					bits = line.length();
					sumZeros = new int[bits];
					sumOnes = new int[bits];
				}
				if (line.length() != bits) {
					throw new RuntimeException("invalid input line '"+line+"', expected length '"+bits+"'");
				}
				String binNum = line.replaceFirst(INPUT_RX, "$1");
				for (int i=0; i<bits; i++) {
					sumZeros[i] += (binNum.charAt(i) == '0' ? 1 : 0); 
					sumOnes[i] += (binNum.charAt(i) == '1' ? 1 : 0); 
				}
			}
			long gamma = 0;
			long epsilon = 0;
			for (int i=0; i<bits; i++) {
				gamma = gamma << 1;
				epsilon = epsilon << 1;
				if (sumOnes[i] > sumZeros[i]) {
					gamma += 1;
				}
				else {
					epsilon += 1;
				}
			}
			System.out.println("gamma: "+gamma);
			System.out.println("epsilon: "+epsilon);
			System.out.println("result: "+(gamma*epsilon));
		}
	}

	public static void mainPart2() throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File("input/y21/day03.txt"))) {
			List<String> scanResult = new ArrayList<>();
			int bits = 0;
			int sumZeros[] = null;
			int sumOnes[] = null;
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				if (bits == 0) {
					bits = line.length();
					sumZeros = new int[bits];
					sumOnes = new int[bits];
				}
				if (line.length() != bits) {
					throw new RuntimeException("invalid input line '"+line+"', expected length '"+bits+"'");
				}
				String binNum = line.replaceFirst(INPUT_RX, "$1");
				for (int i=0; i<bits; i++) {
					sumZeros[i] += (binNum.charAt(i) == '0' ? 1 : 0); 
					sumOnes[i] += (binNum.charAt(i) == '1' ? 1 : 0); 
				}
				scanResult.add(binNum);
			}
			System.out.println("#scans: "+scanResult.size());
			String oxygeneGeneratorRating = searchOxygeneRating(scanResult, 0, sumZeros, sumOnes);
			System.out.println("oxygeneGeneratorRating: "+oxygeneGeneratorRating);
			String co2ScrubberRating = searchCO2ScrubberRating(scanResult, 0, sumZeros, sumOnes);
			System.out.println("co2ScrubberRating: "+co2ScrubberRating);
			int oxygene = Integer.parseInt(oxygeneGeneratorRating, 2);
			int co2 = Integer.parseInt(co2ScrubberRating, 2);
			System.out.println(oxygene + " x " + co2 + " = " + (oxygene * co2));
		}
	}

	
	private static String searchOxygeneRating(List<String> filteredResult, int bitPos, int[] sumZeros, int[] sumOnes) {
		int[] sums = calcSums(filteredResult, bitPos);
		char mostCommonChar = (sums[0] > sums[1]) ? '0' : '1';
		List<String> filterMostCommon = filteredResult.stream().filter(bitNum -> bitNum.charAt(bitPos)==mostCommonChar).collect(Collectors.toList());
		if (filterMostCommon.size()==1) {
			return filterMostCommon.get(0);
		}
		return searchOxygeneRating(filterMostCommon, bitPos+1, sumZeros, sumOnes);
	}

	private static int[] calcSums(List<String> scans, int bitPos) {
		final int[] result = {0,0};
		scans.forEach(scan -> result[scan.charAt(bitPos)=='0'?0:1]++);
		return result;
	}

	private static String searchCO2ScrubberRating(List<String> filteredResult, int bitPos, int[] sumZeros, int[] sumOnes) {
		int[] sums = calcSums(filteredResult, bitPos);
		char mostCommonChar = (sums[0] <= sums[1]) ? '0' : '1';
		List<String> filterMostCommon = filteredResult.stream().filter(bitNum -> bitNum.charAt(bitPos)==mostCommonChar).collect(Collectors.toList());
		if (filterMostCommon.size()==1) {
			return filterMostCommon.get(0);
		}
		return searchCO2ScrubberRating(filterMostCommon, bitPos+1, sumZeros, sumOnes);
	}

	public static void main(String[] args) throws FileNotFoundException {
		mainPart2();
	}

	
}
