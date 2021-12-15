package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * see: https://adventofcode.com/2021/day/6
 *
 */
public class Y21Day06 {

	
	private static List<Integer> lanternFishes;

	public static void mainPart1() throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File("input/y21/day06.txt"))) {
			lanternFishes = Utils.toList(scanner.nextLine().split("\\s*,\\s*")).stream().map(Integer::parseInt).collect(Collectors.toList());
			System.out.println(lanternFishes);
		}
		for (int day=1; day<=80; day++) {
			final List<Integer> newBornLanternFishes = new ArrayList<>();
			lanternFishes = lanternFishes.stream().map(age -> {
				if (age == 0) {
					newBornLanternFishes.add(8);
					return 6;
				}
				return age-1;
			}).collect(Collectors.toList());
			lanternFishes.addAll(newBornLanternFishes);
		}
		System.out.println(lanternFishes.size());
	}

	
	public static void mainPart2() throws FileNotFoundException {
		final long [] fishAges = new long[9];
		try (Scanner scanner = new Scanner(new File("input/y21/day06.txt"))) {
			Utils.toList(scanner.nextLine().split("\\s*,\\s*")).stream().map(Integer::parseInt).forEach(age -> fishAges[age]++);
			System.out.println(Utils.toList(fishAges));
		}
		for (int day=1; day<=256; day++) {
			long newBorn = fishAges[0];
			for (int age=1; age<=8; age++) {
				fishAges[age-1] = fishAges[age];
			}
			fishAges[6] += newBorn;
			fishAges[8] = newBorn;
		}
		long result = Utils.sum(fishAges);
		System.out.println(result);
	}




	public static void main(String[] args) throws FileNotFoundException {
		mainPart2();
	}

	
}
