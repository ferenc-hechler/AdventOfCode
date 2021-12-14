package de.hechler.adventofcode.y21;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import de.hechler.adventofcode.y21.Utils.Counter;
import de.hechler.adventofcode.y21.Utils.LongCounter;

/**
 * see: https://adventofcode.com/2021/day/14
 *
 */
public class Y21Day14 {

	private final static String INPUT_RX_1 = "^([A-Z]+)$";
	private final static String INPUT_RX_2 = "^([A-Z][A-Z]) -> ([A-Z])$";


	public static void mainPart1() throws FileNotFoundException {
		
		Map<String, String> rules = new HashMap<>();
		try (Scanner scanner = new Scanner(new File("input/y21/day14.txt"))) {
			String line = scanner.nextLine().trim();
			if (!line.matches(INPUT_RX_1)) {
				throw new RuntimeException("invalid input line '"+line+"', not matching RX1 '"+INPUT_RX_1+"'");
			}
			String polymere = line.replaceFirst(INPUT_RX_1, "$1");

			while (scanner.hasNext()) {
				line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX_2)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX2 '"+INPUT_RX_2+"'");
				}
				String base = line.replaceFirst(INPUT_RX_2, "$1");
				String insertion = line.replaceFirst(INPUT_RX_2, "$2");
				rules.put(base, insertion);
			}
			System.out.println(polymere);
			System.out.println(rules);
			
			for (int i=1; i<=10; i++) {
				polymere = transform(polymere, rules);
				System.out.println(i+": "+polymere);
			}
			final Map<Character, Utils.Counter> countChars = new HashMap<>();
			for (char c:polymere.toCharArray()) {
				Counter counter = countChars.get(c);
				if (counter == null) {
					counter = new Utils.Counter();
					countChars.put(c, counter);
				}
				counter.inc();
			}
			System.out.println(countChars);
			List<Integer> counts = countChars.values().stream().map(cntr -> cntr.get()).collect(Collectors.toList());
			Collections.sort(counts);
			int min = counts.get(0);
			int max = counts.get(counts.size()-1);
			System.out.println("max: "+max);
			System.out.println("min: "+min);
			System.out.println("max-min: "+(max-min));
		}
	}

	
	
	private static String transform(String polymere, Map<String, String> rules) {
		StringBuilder result = new StringBuilder();
		char previousElement = '?';
		for (char c:polymere.toCharArray()) {
			if (previousElement == '?') {
				result.append(""+c);
				previousElement = c;
				continue;
			}
			String base = ""+previousElement + c;
			String insertion = rules.get(base);
			result.append(insertion).append(""+c);
			previousElement = c;
		}
		return result.toString();
	}


	private static class PairCounter {
		private Map<String, LongCounter> countPairs;
		public PairCounter() {
			countPairs = new HashMap<>();
		}
		public void addPair(String pair) {
			incPair(pair, 1);
		}
		public void incPair(String pair, long num) {
			LongCounter cntr = countPairs.get(pair);
			if (cntr == null) {
				cntr = new LongCounter();
				countPairs.put(pair, cntr);
			}
			cntr.inc(num);
		}
		public Set<String> getPairs() {
			return countPairs.keySet();
		}
		public long getCount(String pair) {
			return countPairs.get(pair).get();
		}
		@Override
		public String toString() {
			return countPairs.toString();
		}
	}
	
	public static void mainPart2() throws FileNotFoundException {
		
		Map<String, String> rules = new HashMap<>();
		try (Scanner scanner = new Scanner(new File("input/y21/day14.txt"))) {
			String line = scanner.nextLine().trim();
			if (!line.matches(INPUT_RX_1)) {
				throw new RuntimeException("invalid input line '"+line+"', not matching RX1 '"+INPUT_RX_1+"'");
			}
			String polymere = line.replaceFirst(INPUT_RX_1, "$1");

			while (scanner.hasNext()) {
				line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX_2)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX2 '"+INPUT_RX_2+"'");
				}
				String base = line.replaceFirst(INPUT_RX_2, "$1");
				String insertion = line.replaceFirst(INPUT_RX_2, "$2");
				rules.put(base, insertion);
			}
			System.out.println(polymere);
			System.out.println(rules);

			String origPolymere = polymere;

			
			PairCounter structure = buildPolyStructure(polymere);
			Map<Character, Long> counts = countElements(structure);
			System.out.println(counts);
			
			for (int i=1; i<=40; i++) {
				structure = transform(structure, rules); 
				System.out.println(i+": "+structure);
				counts = countElements(structure);
				System.out.println("   counts: "+counts);
			}
			
			counts = countElements(structure);
			long max = Utils.maxL(counts.values());
			long min = Utils.minL(counts.values());
			System.out.println("max: "+max);
			System.out.println("min: "+min);
			System.out.println("max-min: "+(max-min));
		}
	}



	private static Map<Character, Long> countElements(PairCounter structure) {
		Map<Character, LongCounter> result = new HashMap<>();
		Set<String> pairs = structure.getPairs();
		for (String pair:pairs) {
			long cnt = structure.getCount(pair);
			LongCounter cntr = result.get(pair.charAt(0));
			if (cntr == null) {
				cntr = new LongCounter();
				result.put(pair.charAt(0), cntr);
			}
			cntr.inc(cnt);
		}
		final Map<Character, Long> lresult = new HashMap<>();
		result.forEach((c, cntr) -> lresult.put(c, cntr.get()));
		return lresult;
	}



	private static PairCounter transform(PairCounter structure, Map<String, String> rules) {
		PairCounter result = new PairCounter();
		Set<String> pairs = structure.getPairs();
		for (String pair:pairs) {
			if (pair.endsWith("?")) {
				result.addPair(pair);
				continue;
			}
			String insertion = rules.get(pair);
			long cnt = structure.getCount(pair);
			result.incPair(pair.charAt(0)+insertion, cnt);
			result.incPair(insertion+pair.charAt(1), cnt);
		}
		return result;
	}



	private static PairCounter buildPolyStructure(String polymere) {
		PairCounter polyStructure = new PairCounter();
		for (int i=0; i<polymere.length(); i++) {
			String pair = (polymere+"?").substring(i, i+2);
			polyStructure.addPair(pair);
		}
		return polyStructure;
	}



	private static Map<Character, Long> countElements(String polymere) {
		final Map<Character, Utils.LongCounter> countChars = new HashMap<>();
		for (char c:polymere.toCharArray()) {
			LongCounter counter = countChars.get(c);
			if (counter == null) {
				counter = new Utils.LongCounter();
				countChars.put(c, counter);
			}
			counter.inc();
		}
		final Map<Character, Long> result = new HashMap<>();
		countChars.forEach((c, cntr) -> result.put(c, cntr.get()));
		return result;
	}

	
	

	public static void main(String[] args) throws FileNotFoundException {
		mainPart2();
	}

	
}
