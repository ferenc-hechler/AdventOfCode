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
 * see: https://adventofcode.com/2021/day/8
 *
 */
public class Y21Day08 {

	/*
	 * 
	 *   0:      1:      2:      3:      4:
	 *  aaaa    ....    aaaa    aaaa    ....
	 * b    c  .    c  .    c  .    c  b    c
	 * b    c  .    c  .    c  .    c  b    c
	 *  ....    ....    dddd    dddd    dddd
	 * e    f  .    f  e    .  .    f  .    f
	 * e    f  .    f  e    .  .    f  .    f
	 *  gggg    ....    gggg    gggg    ....
	 * 
	 *   5:      6:      7:      8:      9:
	 *  aaaa    aaaa    aaaa    aaaa    aaaa
	 * b    .  b    .  .    c  b    c  b    c
	 * b    .  b    .  .    c  b    c  b    c
	 *  dddd    dddd    ....    dddd    dddd
	 * .    f  e    f  .    f  e    f  .    f
	 * .    f  e    f  .    f  e    f  .    f
	 *  gggg    gggg    ....    gggg    gggg
	 *  
	 */
	
	private final static int D_A =  1; 
	private final static int D_B =  2; 
	private final static int D_C =  4; 
	private final static int D_D =  8; 
	private final static int D_E = 16; 
	private final static int D_F = 32; 
	private final static int D_G = 64;
	
	private final static int N_0 = D_A + D_B + D_C + D_E + D_F + D_G;
	private final static int N_1 = D_C + D_F;
	private final static int N_2 = D_A + D_C + D_D + D_E + D_G;
	private final static int N_3 = D_A + D_C + D_D + D_F + D_G;
	private final static int N_4 = D_B + D_C + D_D + D_F;
	private final static int N_5 = D_A + D_B + D_D + D_F + D_G;
	private final static int N_6 = D_A + D_B + D_D + D_E + D_F + D_G;
	private final static int N_7 = D_A + D_C + D_F;
	private final static int N_8 = D_A + D_B + D_C + D_D + D_E + D_F + D_G;
	private final static int N_9 = D_A + D_B + D_C + D_D + D_F + D_G;
	
	
	private final static int[] DS = {D_A, D_B, D_C, D_D, D_E, D_F, D_G};
	private final static int[] NUMNS = {N_0, N_1, N_2, N_3, N_4, N_5, N_6, N_7, N_8, N_9};
	private final static int[] BITS = {6, 2, 5, 5, 4, 5, 6, 3, 7, 6};
	
	private final static String INPUT_RX = "([a-g ]+)[|]([a-g ]+)";
	
	public static void mainPart1() throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File("input/y21/day08.txt"))) {
			int[] sum1478 = new int[1];
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				List<String> input = Utils.toList(line.replaceFirst(INPUT_RX, "$1").trim().split(" +"));
				List<String> output = Utils.toList(line.replaceFirst(INPUT_RX, "$2").trim().split(" +"));
				System.out.println(input+ " -> " + output);
				output.forEach(inp -> {
					int len = inp.length();
					if ((len == 2) || (len == 4) || (len == 3) || (len == 7)) {
						sum1478[0]++;
					}
				});
			}
			System.out.println(sum1478[0]);
		}
	}

	
	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
