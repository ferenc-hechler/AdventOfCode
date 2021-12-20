package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import de.hechler.adventofcode.y21.Utils3D.Point3D;
import de.hechler.adventofcode.y21.Utils3D.RotMatrix;

/**
 * see: https://adventofcode.com/2021/day/20
 *
 */
public class Y21Day20 {

	private final static String INPUT_RX1 = "^([.#]{512})$";
	private final static String INPUT_RX2 = "^([.#]+)$";

	public static void mainPart1() throws FileNotFoundException {

		try (Scanner scanner = new Scanner(new File("input/y21/day20.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX1)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX1 '"+INPUT_RX1+"'");
				}
				final int[] alg = new int[512]; 
				for (int i=0; i<512; i++) {
					alg[i] = line.charAt(i) == '#' ? 1 : 0;
				}
				List<String> inputLines = new  ArrayList<>();
				while (scanner.hasNext()) {
					line = scanner.nextLine().trim();
					if (line.isEmpty()) {
						continue;
					}
					if (!line.matches(INPUT_RX2)) {
						throw new RuntimeException("invalid input line '"+line+"', not matching RX2 '"+INPUT_RX2+"'");
					}
					inputLines.add(line);
				}
				int countIterations = 2;
				int border = countIterations+1;
				int height = inputLines.size();
				int width = inputLines.get(0).length();
				int[][] world = new int[height+2*border][width+2*border];
				int infiniteValue = 0;
				for (int y=0; y<height; y++) {
					for (int x=0; x<width; x++) {
						world[y+border][x+border] = inputLines.get(y).charAt(x)=='#' ? 1 : 0;
					}
				}
				System.out.println(Utils.toString(world, ""));
				for (int step=1; step<=2; step++) {
					infiniteValue = alg[511*infiniteValue];
					world = calcNextTick(world, alg);
					fillBorder(world, infiniteValue);
					System.out.println(Utils.toString(world, ""));
					System.out.println("#"+Utils.sum(world));
				}
			}
		}
	}

	private static int[][] calcNextTick(int[][] world, int[] alg) {
		int maxY = world.length-1;
		int maxX = world[0].length-1;
		int[][] result = new int[maxY+1][maxX+1]; 
		for (int y=1; y<maxY; y++) {
			for (int x=1; x<maxX; x++) {
				int neighbours = 0;
				neighbours = (neighbours<<1) | world[y-1][x-1];
				neighbours = (neighbours<<1) | world[y-1][x  ];
				neighbours = (neighbours<<1) | world[y-1][x+1];
				neighbours = (neighbours<<1) | world[y  ][x-1];
				neighbours = (neighbours<<1) | world[y  ][x  ];
				neighbours = (neighbours<<1) | world[y  ][x+1];
				neighbours = (neighbours<<1) | world[y+1][x-1];
				neighbours = (neighbours<<1) | world[y+1][x  ];
				neighbours = (neighbours<<1) | world[y+1][x+1];
				result[y][x] = alg[neighbours];
			}
		}
		return result;
	}

	private static void fillBorder(int[][] world, int infiniteValue) {
		int maxY = world.length-1;
		int maxX = world[0].length-1;
		for (int y=0; y<=maxY; y++) {
			world[y][0] = infiniteValue;
			world[y][maxX] = infiniteValue;
		}
		for (int x=0; x<=maxX; x++) {
			world[0][x] = infiniteValue;
			world[maxY][x] = infiniteValue;
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
