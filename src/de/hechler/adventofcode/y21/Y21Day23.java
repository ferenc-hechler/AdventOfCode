package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import de.hechler.adventofcode.y21.Utils3D.Point3D;

/**
 * see: https://adventofcode.com/2021/day/3
 *
 */
public class Y21Day23 {

	private final static String INPUT_RX1 = "^#############$";
	private final static String INPUT_RX2 = "^#...........#$";
	private final static String INPUT_RX3 = "^###([A-D])#([A-D])#([A-D])#([A-D])###$";
	private final static String INPUT_RX4 = "^  #([A-D])#([A-D])#([A-D])#([A-D])#$";
	private final static String INPUT_RX5 = "^  #########$";

	public static void mainPart1() throws FileNotFoundException {

		try (Scanner scanner = new Scanner(new File("input/y21/day23example.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (!line.matches(INPUT_RX1)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX1 '"+INPUT_RX1+"'");
				}
				line = scanner.nextLine();
				if (!line.matches(INPUT_RX2)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX2 '"+INPUT_RX2+"'");
				}
				line = scanner.nextLine();
				if (!line.matches(INPUT_RX3)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX3 '"+INPUT_RX3+"'");
				}
				int amber1  = line.replaceFirst(INPUT_RX3, "$1").charAt(0)-'A'+1;
				int bronce1 = line.replaceFirst(INPUT_RX3, "$2").charAt(0)-'A'+1;
				int copper1 = line.replaceFirst(INPUT_RX3, "$3").charAt(0)-'A'+1;
				int desert1 = line.replaceFirst(INPUT_RX3, "$4").charAt(0)-'A'+1;
				line = scanner.nextLine();
				if (!line.matches(INPUT_RX4)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX4 '"+INPUT_RX4+"'");
				}
				int amber2  = line.replaceFirst(INPUT_RX4, "$1").charAt(0)-'A'+1;
				int bronce2 = line.replaceFirst(INPUT_RX4, "$2").charAt(0)-'A'+1;
				int copper2 = line.replaceFirst(INPUT_RX4, "$3").charAt(0)-'A'+1;
				int desert2 = line.replaceFirst(INPUT_RX4, "$4").charAt(0)-'A'+1;
				line = scanner.nextLine();
				if (!line.matches(INPUT_RX5)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX5 '"+INPUT_RX4+"'");
				}

				System.out.println("INPUT: "+amber1+","+bronce1+","+copper1+","+desert1);
				System.out.println("       "+amber2+","+bronce2+","+copper2+","+desert2);
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
