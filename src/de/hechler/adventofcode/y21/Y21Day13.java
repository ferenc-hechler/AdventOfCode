package de.hechler.adventofcode.y21;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * see: https://adventofcode.com/2021/day/13
 *
 */
public class Y21Day13 {

	private final static String INPUT_RX_1 = "^([0-9]+),([0-9]+)$";
	private final static String INPUT_RX_2 = "^fold along ([xy]+)=([0-9]+)$";


	public static void mainPart1() throws FileNotFoundException {
		
		Set<Point> dots = new HashSet<Point>();
		try (Scanner scanner = new Scanner(new File("input/y21/day13.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					break;
				}
				if (!line.matches(INPUT_RX_1)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX_1+"'");
				}
				Integer xPos = Integer.parseInt(line.replaceFirst(INPUT_RX_1, "$1"));
				Integer yPos = Integer.parseInt(line.replaceFirst(INPUT_RX_1, "$2"));
				dots.add(new Point(xPos, yPos));
			}
			System.out.println("#"+dots.size()+" - "+dots);
			Set<Point> foldedDots = dots;
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX_2)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX_2+"'");
				}
				boolean foldX = line.replaceFirst(INPUT_RX_2, "$1").equals("x");
				Integer foldValue = Integer.parseInt(line.replaceFirst(INPUT_RX_2, "$2"));
				System.out.println(line);
				foldedDots = fold(foldedDots, foldX, foldValue);
				System.out.println("#"+foldedDots.size()+" - "+foldedDots);
				break;
			}
		}
	}

	public static void mainPart2() throws FileNotFoundException {
		
		Set<Point> dots = new HashSet<Point>();
		try (Scanner scanner = new Scanner(new File("input/y21/day13.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					break;
				}
				if (!line.matches(INPUT_RX_1)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX_1+"'");
				}
				Integer xPos = Integer.parseInt(line.replaceFirst(INPUT_RX_1, "$1"));
				Integer yPos = Integer.parseInt(line.replaceFirst(INPUT_RX_1, "$2"));
				dots.add(new Point(xPos, yPos));
			}
			System.out.println("#"+dots.size()+" - "+dots);
			show(dots);
			Set<Point> foldedDots = dots;
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX_2)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX_2+"'");
				}
				boolean foldX = line.replaceFirst(INPUT_RX_2, "$1").equals("x");
				Integer foldValue = Integer.parseInt(line.replaceFirst(INPUT_RX_2, "$2"));
				System.out.println(line);
				foldedDots = fold(foldedDots, foldX, foldValue);
				show(foldedDots);
				System.out.println("#"+foldedDots.size()+" - "+foldedDots);
			}
		}
	}

	private static void show(Set<Point> dots) {
		final Utils.Counter maxX = new Utils.Counter();
		final Utils.Counter maxY = new Utils.Counter();
		dots.forEach((p) -> {
			maxX.max(p.x);
			maxY.max(p.y);
		});
		for (int y=0; y<=maxY.get(); y++) {
			for (int x=0; x<=maxX.get(); x++) {
				System.out.print(dots.contains(new Point(x, y))?"#":".");
			}
			System.out.println();
		}
	}

	private static Set<Point> fold(Set<Point> dots, final boolean foldX, final Integer foldValue) {
		return dots.stream().map((p) -> {
			if (foldX && (p.x > foldValue)) {
				return new Point(2*foldValue - p.x, p.y); 
			}
			if ((!foldX) && (p.y > foldValue)) {
				return new Point(p.x, 2*foldValue - p.y); 
			}
			return p;				
		}).collect(Collectors.toSet());
	}

	public static void main(String[] args) throws FileNotFoundException {
		mainPart2();
	}

	
}
