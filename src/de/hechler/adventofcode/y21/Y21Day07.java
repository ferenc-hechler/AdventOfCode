package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
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
	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
