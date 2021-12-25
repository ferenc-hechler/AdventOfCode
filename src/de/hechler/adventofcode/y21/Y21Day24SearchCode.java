package de.hechler.adventofcode.y21;

import java.util.ArrayList;
import java.util.List;

/**
 * see: https://adventofcode.com/2021/day/22
 *
 */
public class Y21Day24SearchCode {

	private final static int[][] ARGS_LIST = { 
			{ 1, 14, 12 }, 
			{ 1, 11, 8 }, 
			{ 1, 11, 7 }, 
			{ 1, 14, 4 }, 
			{ 26, -11, 4 },
			{ 1, 12, 1 }, 
			{ 26, -1, 10 }, 
			{ 1, 10, 8 }, 
			{ 26, -3, 12 }, 
			{ 26, -4, 10 }, 
			{ 26, -13, 15 }, 
			{ 26, -8, 4 },
			{ 1, 13, 10 }, 
			{ 26, -11, 9 } 
		};


	public static List<Integer> monad(int step, int z) {
		if (step == 14) {
			if (z==0) {
				return new ArrayList<>();
			}
			return null;
		}
		int[] args = ARGS_LIST[step];
		int m = (z%26)+args[1];
		if ((m>=1) && (m<=9)) {
			z=(z/args[0]);
			List<Integer> result = monad(step+1, z);
			if (result != null) {
				result.add(0, m);
			}
			return result;
		}
		for (int w=9; w>=1; w--) {
			if (step <= 2) {
				System.out.println("   ".substring(0, step)+w);
			}
			int y = w+args[2];
			int z2=(z/args[0])*26+y;
			List<Integer> result = monad(step+1, z2);
			if (result != null) {
				result.add(0, w);
				return result;
			}
		}
		return null;
	}

	public static List<Integer> monadSmall(int step, int z) {
		if (step == 14) {
			if (z==0) {
				return new ArrayList<>();
			}
			return null;
		}
		int[] args = ARGS_LIST[step];
		int m = (z%26)+args[1];
		if ((m>=1) && (m<=9)) {
			z=(z/args[0]);
			List<Integer> result = monadSmall(step+1, z);
			if (result != null) {
				result.add(0, m);
			}
			return result;
		}
		for (int w=1; w<=9; w++) {
			if (step <= 2) {
				System.out.println("   ".substring(0, step)+w);
			}
			int y = w+args[2];
			int z2=(z/args[0])*26+y;
			List<Integer> result = monadSmall(step+1, z2);
			if (result != null) {
				result.add(0, w);
				return result;
			}
		}
		return null;
	}



	public static void mainPart1() {
		List<Integer> result = monad(0, 0);
		System.out.println(result);
	}

	public static void mainPart2() {
		List<Integer> result = monadSmall(0, 0);
		System.out.println(result);
	}

	public static void main(String[] args) {
		mainPart2();
	}

}
