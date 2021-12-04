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
 * see: https://adventofcode.com/2021/day/2
 *
 */
public class Y21Day02 {

	
	private final static String INPUT_RX = "^([a-z*]+)\\s+([0-9]+)$";
	
	public static void mainPart1() throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File("input/y21/day02.txt"))) {
			int sumHorizontal = 0;
			int sumDepth = 0;
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				String command = line.replaceFirst(INPUT_RX, "$1");
				int value = Integer.parseInt(line.replaceFirst(INPUT_RX, "$2")); 
				switch (command) {
				case "forward":{
					sumHorizontal += value;
					break;
				}
				case "up":{
					sumDepth -= value;
					break;
				}
				case "down":{
					sumDepth += value;
					break;
				}
				default:
					throw new RuntimeException("unknown command in line '"+line+"'");
				}
			}
			System.out.println("sumHorizontal: "+sumHorizontal);
			System.out.println("sumDepth: "+sumDepth);
			System.out.println("result: "+(sumHorizontal*sumDepth));
		}
	}

	public static void mainPart2() throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File("input/y21/day02.txt"))) {
			int sumHorizontal = 0;
			int sumDepth = 0;
			int aim = 0;
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				String command = line.replaceFirst(INPUT_RX, "$1");
				int value = Integer.parseInt(line.replaceFirst(INPUT_RX, "$2")); 
				switch (command) {
				case "forward":{
					sumHorizontal += value;
					sumDepth += aim*value;
					break;
				}
				case "up":{
					aim -= value;
					break;
				}
				case "down":{
					aim += value;
					break;
				}
				default:
					throw new RuntimeException("unknown command in line '"+line+"'");
				}
			}
			System.out.println("sumHorizontal: "+sumHorizontal);
			System.out.println("sumDepth: "+sumDepth);
			System.out.println("result: "+(sumHorizontal*sumDepth));
		}
	}


	public static void main(String[] args) throws FileNotFoundException {
		mainPart2();
	}

	
}
