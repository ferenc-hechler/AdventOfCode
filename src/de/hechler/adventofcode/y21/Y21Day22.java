package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import de.hechler.adventofcode.y21.Utils3D.Point3D;

/**
 * see: https://adventofcode.com/2021/day/22
 *
 */
public class Y21Day22 {

	private final static String INPUT_RX = "^(on|off) x=([0-9-]+)..([0-9-]+),y=([0-9-]+)..([0-9-]+),z=([0-9-]+)..([0-9-]+)$";

	public static void mainPart1() throws FileNotFoundException {

		Set<Point3D> onCells = new HashSet<>(); 
		
		try (Scanner scanner = new Scanner(new File("input/y21/day22.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				boolean switchOn = line.replaceFirst(INPUT_RX, "$1").equals("on");
				int minX=Integer.parseInt(line.replaceFirst(INPUT_RX, "$2")); 
				int maxX=Integer.parseInt(line.replaceFirst(INPUT_RX, "$3")); 
				int minY=Integer.parseInt(line.replaceFirst(INPUT_RX, "$4")); 
				int maxY=Integer.parseInt(line.replaceFirst(INPUT_RX, "$5")); 
				int minZ=Integer.parseInt(line.replaceFirst(INPUT_RX, "$6")); 
				int maxZ=Integer.parseInt(line.replaceFirst(INPUT_RX, "$7"));
				for (int x=minX; x<=maxX; x++) {
					if ((x<-50) || (x>50)) {
						continue;
					}
					for (int y=minY; y<=maxY; y++) {
						if ((y<-50) || (y>50)) {
							continue;
						}
						for (int z=minZ; z<=maxZ; z++) {
							if ((z<-50) || (z>50)) {
								continue;
							}
							if (switchOn) {
								onCells.add(new Point3D(x,y,z));
							}
							else {
								onCells.remove(new Point3D(x,y,z));
							}
						}
					}
				}
				System.out.println(onCells.size());
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
