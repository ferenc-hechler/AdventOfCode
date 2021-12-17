package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import de.hechler.adventofcode.y21.GeoUtils.Area;
import de.hechler.adventofcode.y21.GeoUtils.Point;

/**
 * see: https://adventofcode.com/2021/day/16
 *
 */
public class Y21Day17 {

	private final static String INPUT_RX = "^target area: x=([0-9]+)..([0-9]+), y=(-[0-9]+)..(-[0-9]+)$";

	
	public static void mainPart1() throws FileNotFoundException {
		
		try (Scanner scanner = new Scanner(new File("input/y21/day17.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				int fromX = Integer.parseInt(line.replaceFirst(INPUT_RX, "$1"));
				int toX = Integer.parseInt(line.replaceFirst(INPUT_RX, "$2"));
				int fromY = Integer.parseInt(line.replaceFirst(INPUT_RX, "$3"));
				int toY = Integer.parseInt(line.replaceFirst(INPUT_RX, "$4"));
				Area targetArea = Area.createFromTo(fromX, fromY, toX, toY);
				System.out.println(targetArea);
				int highestY = 0;
				for (int dx=0; dx<=toX; dx++) {
					for (int dy=0; dy<=-fromY; dy++) {
						int highY = simulateThrow(targetArea, dx,dy);
						if (highY > highestY) {
							highestY = highY;
							System.out.println("("+dx+","+dy+"): highestY="+highestY);
						}
					}
				}
				break;
			}
		}
	}
	
	
	
	private static int simulateThrow(Area targetArea, int dx, int dy) {
		Point position = new Point(0,0);
		int highestY = 0;
		while (position.y()+dy>=targetArea.fromY()) {
			position = position.offset(dx, dy);
//			System.out.println(position);
			highestY = Math.max(highestY, position.y());
			dx = Math.max(0, dx-1);
			dy--;
		}
		return targetArea.contains(position) ? highestY : 0;
	}



	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
