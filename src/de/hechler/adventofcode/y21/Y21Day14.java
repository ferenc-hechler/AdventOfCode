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



	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
