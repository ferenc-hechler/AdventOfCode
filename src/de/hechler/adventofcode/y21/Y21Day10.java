package de.hechler.adventofcode.y21;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * see: https://adventofcode.com/2021/day/9
 *
 */
public class Y21Day10 {

	private final static String INPUT_RX = "^([()\\[\\]{}<>]+)$";
	
	
	public static void mainPart1() throws FileNotFoundException {
		
		Map<Character, Character> open2closeMap = new HashMap<>();
		open2closeMap.put('(', ')');
		open2closeMap.put('[', ']');
		open2closeMap.put('{', '}');
		open2closeMap.put('<', '>');
		Map<Character, Integer> errorValue = new HashMap<>();
		errorValue.put(')', 3);
		errorValue.put(']', 57);
		errorValue.put('}', 1197);
		errorValue.put('>', 25137);

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
							int error = errorValue.get(c);
							sumError += error;
							System.out.println("expected '"+expectedClosingCharacter+"' at position "+i+", but found '"+c+"': " + error+ " Points");
						}
					}
				}
			}
			System.out.println(sumError);
		}
	}


	
	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
