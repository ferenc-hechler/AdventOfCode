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
 * see: https://adventofcode.com/2021/day/22
 *
 */
public class Y21Day22 {

	private final static String INPUT_RX = "^(on|off) x=([0-9-]+)..([0-9-]+),y=([0-9-]+)..([0-9-]+),z=([0-9-]+)..([0-9-]+)$";


	public static class Cube {
		private long factor;
		private long minX; 
		private long maxX; 
		private long minY; 
		private long maxY; 
		private long minZ; 
		private long maxZ;
		public Cube(long minX, long maxX, long minY, long maxY, long minZ, long maxZ, long factor) {
			this.factor = factor;
			this.minX = minX;
			this.maxX = maxX;
			this.minY = minY;
			this.maxY = maxY;
			this.minZ = minZ;
			this.maxZ = maxZ;
		} 
		public long countPower() {
			return factor*(maxX-minX+1)*(maxY-minY+1)*(maxZ-minZ+1);
		}
		public boolean overlaps(Cube other) {
			return  (maxX>other.minX)&&(maxY>other.minY)&&(maxZ>other.minZ) && 
					(minX<other.maxX)&&(minY<other.maxY)&&(minZ<other.maxZ); 
		}
		public Cube intersect(Cube other, long factor) {
			if (!overlaps(other)) {
				return null;
			}
			return new Cube(
				Math.max(minX, other.minX), Math.min(maxX, other.maxX), 
				Math.max(minY, other.minY), Math.min(maxY, other.maxY), 
				Math.max(minZ, other.minZ), Math.min(maxZ, other.maxZ), 
				factor);
		}
		public long getFactor() {
			return factor;
		}
	}
	
	
	
	public static void mainPart2() throws FileNotFoundException {

		List<Cube> cubes = new ArrayList<>();  
		
		try (Scanner scanner = new Scanner(new File("input/y21/day22.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				long factor = line.replaceFirst(INPUT_RX, "$1").equals("on") ? 1L : 0L;
				long minX=Long.parseLong(line.replaceFirst(INPUT_RX, "$2")); 
				long maxX=Long.parseLong(line.replaceFirst(INPUT_RX, "$3")); 
				long minY=Long.parseLong(line.replaceFirst(INPUT_RX, "$4")); 
				long maxY=Long.parseLong(line.replaceFirst(INPUT_RX, "$5")); 
				long minZ=Long.parseLong(line.replaceFirst(INPUT_RX, "$6")); 
				long maxZ=Long.parseLong(line.replaceFirst(INPUT_RX, "$7"));
				
				Cube newCube = new Cube(minX, maxX, minY, maxY, minZ, maxZ, factor); 
				List<Cube> intersectionCubes = new ArrayList<>();
				for (Cube cube:cubes) {
					if (newCube.overlaps(cube)) {
						// compensate intersection
						if (cube.getFactor()==0) {
							// no compensation neccessary for overlapping with a 0 cube
							continue;
						}
						Cube compensateCube = newCube.intersect(cube, -cube.getFactor());
						intersectionCubes.add(compensateCube);
					}
				}
				cubes.add(newCube);
				cubes.addAll(intersectionCubes);
				long count = 0;
				for (Cube cube:cubes) {
					count += cube.countPower();
				}
				System.out.println(count);
			}
		}
	}

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
		mainPart2();
	}

	
}
