package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * see: https://adventofcode.com/2021/day/19
 *
 */
public class Y21Day19 {

	private final static String INPUT_RX1 = "^--- scanner ([0-9]+) ---$";
	private final static String INPUT_RX2 = "^([0-9-]+),([0-9-]+),([0-9-]+)$";

	public static void mainPart1() throws FileNotFoundException {

		try (Scanner scanner = new Scanner(new File("input/y21/day19example.txt"))) {
			List<String> terms = new ArrayList<>();
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX1)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX1 '"+INPUT_RX1+"'");
				}
				int scannerID = Integer.parseInt(line.replaceFirst(INPUT_RX1, "$1"));
				System.out.println();
				System.out.println("SCANNER="+scannerID);
				while (scanner.hasNext()) {
					line = scanner.nextLine().trim();
					if (line.isEmpty()) {
						break;
					}
					if (!line.matches(INPUT_RX2)) {
						throw new RuntimeException("invalid input line '"+line+"', not matching RX2 '"+INPUT_RX2+"'");
					}
					int x=Integer.parseInt(line.replaceFirst(INPUT_RX2, "$1"));
					int y=Integer.parseInt(line.replaceFirst(INPUT_RX2, "$2"));
					int z=Integer.parseInt(line.replaceFirst(INPUT_RX2, "$3"));
					System.out.println("("+x+","+y+","+z+")");
				}
			}
		}
	}
	

	
	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
