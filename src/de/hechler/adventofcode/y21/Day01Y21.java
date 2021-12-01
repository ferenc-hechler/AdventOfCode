package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
public class Day01Y21 {

	
	public static void mainPart1() throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File("input/y21/day01.txt"))) {
			int sumInc = 0;
			int lastDepth = Integer.MAX_VALUE;
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				int depth = Integer.parseInt(line);
				if (depth > lastDepth) {
					sumInc+=1;
				}
				lastDepth = depth;
			}
			System.out.println("INCS: "+sumInc);
		}
	}


	public static void mainPart2() throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File("input/y21/day01.txt"))) {
			int sumInc = 0;
			int lastWinDepth = Integer.MAX_VALUE;
			List<Integer> window = new ArrayList<>();
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (window.size()==3) {
					window.remove(0);
				}
				int depth = Integer.parseInt(line);
				window.add(depth);
				if (window.size() == 3) {
					int winDepth = window.get(0)+window.get(1)+window.get(2);
					if (winDepth > lastWinDepth) {
						sumInc+=1;
					}
					lastWinDepth = winDepth;
				}
			}
			System.out.println("INCS: "+sumInc);
		}
	}


	public static void main(String[] args) throws FileNotFoundException {
		mainPart2();
	}

	
}
