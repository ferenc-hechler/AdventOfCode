package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

/**
 * see: https://adventofcode.com/2021/day/10
 *
 */
public class Y21Day10 {

	private final static String INPUT_RX = "^([()\\[\\]{}<>]+)$";
	
	private final static Map<Character, Character> open2closeMap = Map.of(
            '(', ')',
            '[', ']',
            '{', '}',
            '<', '>');
	private final static Map<Character, Integer> errorValuesMap = Map.of(
            ')', 3,
            ']', 57,
            '}', 1197,
            '>', 25137);
	private final static Map<Character, Long> closingValuesMap = Map.of(
			')', 1L,
			']', 2L,
			'}', 3L,
			'>', 4L);

	
	public static void mainPart1() throws FileNotFoundException {
		
		try (Scanner scanner = new Scanner(new File("input/y21/day10.txt"))) {
			int sumError = 0;
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				System.out.println(line);
				Stack<Character> expectedClosingCharacters = new Stack<>();
				expectedClosingCharacters.add('~');
				for (int i=0; i<line.length(); i++) {
					char c = line.charAt(i);
					Character closingCharacter = open2closeMap.get(c);
					if (closingCharacter != null) {
						expectedClosingCharacters.push(closingCharacter);
					}
					else {
						char expectedClosingCharacter = expectedClosingCharacters.pop();
						if (expectedClosingCharacter != c) {
							int error = errorValuesMap.get(c);
							sumError += error;
							System.out.println("expected '"+expectedClosingCharacter+"' at position "+i+", but found '"+c+"': " + error+ " Points");
						}
					}
				}
			}
			System.out.println(sumError);
		}
	}

	public static void mainPart2() throws FileNotFoundException {
		
		List<Long> closingCosts = new ArrayList<>();
		
		try (Scanner scanner = new Scanner(new File("input/y21/day10.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				System.out.println(line);
				Stack<Character> expectedClosingCharacters = new Stack<>();
				expectedClosingCharacters.add('~');
				long error = 0;
				for (char c:line.toCharArray()) {
					Character closingCharacter = open2closeMap.get(c);
					if (closingCharacter != null) {
						expectedClosingCharacters.push(closingCharacter);
					}
					else if (expectedClosingCharacters.pop() != c) {
						error = errorValuesMap.get(c);
						break;
					}
				}
				if (error == 0) {
					Collections.reverse(expectedClosingCharacters);
					long closingCost = expectedClosingCharacters.stream()
							.filter(c -> c!='~')
							.map(c -> closingValuesMap.get(c))
							.reduce(0L, (sum, cost) -> sum*5L+cost);
					System.out.println(closingCost);
					closingCosts.add(closingCost);
				}
			}
			System.out.println("median ClosingCost: "+Utils.median(closingCosts));
		}
	}

	
	public static void main(String[] args) throws FileNotFoundException {
		mainPart2();
	}

	
}
