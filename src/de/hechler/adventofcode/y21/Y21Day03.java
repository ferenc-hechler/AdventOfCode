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

/**
 * see: https://adventofcode.com/2021/day/1
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


	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
