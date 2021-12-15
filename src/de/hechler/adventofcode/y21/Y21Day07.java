package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * see: https://adventofcode.com/2021/day/6
 *
 */
public class Y21Day07 {

	
	public static void mainPart1() throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File("input/y21/day07.txt"))) {
			List<Integer> crabPositions = Utils.toList(scanner.nextLine().split("\\s*,\\s*")).stream().map(Integer::parseInt).collect(Collectors.toList());
			System.out.println(crabPositions);
			int minPos = Utils.min(crabPositions);
			int maxPos = Utils.max(crabPositions);
			int[] cost = new int[maxPos+1];

			for (int crabPosition:crabPositions) {
				for (int pos=minPos; pos<=maxPos; pos++) {
					cost[pos] += Math.abs(pos-crabPosition);
				}
			}
			System.out.println(Utils.toList(cost));
			int result = Utils.min(cost);
			System.out.println("minimum cost: "+result);
		}
	}

	/**
	 * sum(1..n) = n(n+1)/2
	 * 
	 * @throws FileNotFoundException
	 */
	public static void mainPart2() throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File("input/y21/day07.txt"))) {
			List<Integer> crabPositions = Utils.toList(scanner.nextLine().split("\\s*,\\s*")).stream().map(Integer::parseInt).collect(Collectors.toList());
			System.out.println(crabPositions);
			int minPos = Utils.min(crabPositions);
			int maxPos = Utils.max(crabPositions);
			int[] cost = new int[maxPos+1];

			for (int crabPosition:crabPositions) {
				for (int pos=minPos; pos<=maxPos; pos++) {
					int n = Math.abs(pos-crabPosition);
					cost[pos] += n*(n+1)/2;
				}
			}
			System.out.println(Utils.toList(cost));
			int result = Utils.min(cost);
			System.out.println("minimum cost: "+result);
		}
	}

	
	public static void main(String[] args) throws FileNotFoundException {
		mainPart2();
	}

	
}
