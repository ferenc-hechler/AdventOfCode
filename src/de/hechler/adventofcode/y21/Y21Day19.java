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
 * see: https://adventofcode.com/2021/day/19
 *
 */
public class Y21Day19 {

	private final static String INPUT_RX1 = "^--- scanner ([0-9]+) ---$";
	private final static String INPUT_RX2 = "^([0-9-]+),([0-9-]+),([0-9-]+)$";

	public static class AOCScanner {
		private int id;
		private List<Point3D> relBeaconPosList;
		public AOCScanner(int id) {
			this.id = id;
			this.relBeaconPosList = new ArrayList<>();
		}
		public void addBeacon(Point3D relBeaconPos) {
			relBeaconPosList.add(relBeaconPos);
		}
		public List<Integer> calcDistances(Point3D centerPoint) {
			final List<Integer> result = new ArrayList<>();
			relBeaconPosList.forEach(p -> result.add(centerPoint.nondiagonalDistance(p)));
			return result;
		}
		public List<Point3D> getRelBeaconPosList() {
			return relBeaconPosList;
		}
		public AOCScanner createCopy() {
			AOCScanner result = new AOCScanner(id);
			for (Point3D relBeacon:relBeaconPosList) {
				result.addBeacon(relBeacon.createCopy());
			}
			return result;
		}
		public void offset(Point3D offs) {
			relBeaconPosList.forEach(p -> p.add(offs));
		}
		@Override public String toString() {
			final StringBuilder result = new StringBuilder();
			result.append("SC"+id+"[");
			relBeaconPosList.forEach(p -> result.append(p.toString()).append(","));
			result.setCharAt(result.length()-1, ']');
			return result.toString();
		}
		public void rot(RotMatrix rmat) {
			relBeaconPosList.forEach(p -> p.set(rmat.rot(p)));
		}
	}
	
	public static void mainPart1() throws FileNotFoundException {

		try (Scanner scanner = new Scanner(new File("input/y21/day19.txt"))) {
			List<AOCScanner> scrs = new ArrayList<>();
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
				AOCScanner scr = new AOCScanner(scannerID);
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
					scr.addBeacon(new Point3D(x, y, z));
				}
				System.out.println(scr);
				scrs.add(scr);
			}
			List<AOCScanner> known = new ArrayList<>();
			List<AOCScanner> unknown = new ArrayList<>(scrs);
			known.add(scrs.get(0));
			unknown.remove(scrs.get(0));
			while (unknown.size()>0) {
				OUTER:
				for (AOCScanner scrU:unknown) {
					for (AOCScanner scrK:known) {
						AOCScanner scrTransposed = compare(scrK, scrU);
						if (scrTransposed != null) {
							known.add(scrTransposed);
							unknown.remove(scrU);
							break OUTER;
						}
					}
				}
			}
			Set<Point3D> allBeacons = new HashSet<>();
			for (AOCScanner scrK:known) {
				allBeacons.addAll(scrK.getRelBeaconPosList());
			}
			System.out.println(allBeacons);
			System.out.println("RESULT: #"+allBeacons.size());
		}
	}
	

	
	private static AOCScanner compare(AOCScanner scrA, AOCScanner scrB) {
		for (Point3D relBeaconA:scrA.getRelBeaconPosList()) {
			List<Integer> distsA = scrA.calcDistances(relBeaconA);
			Collections.sort(distsA);
			for (Point3D relBeaconB:scrB.getRelBeaconPosList()) {
				List<Integer> distsB = scrB.calcDistances(relBeaconB);
				Collections.sort(distsB);
				List<Integer> distsAB = new ArrayList<>(distsB);
				distsAB.retainAll(distsA);
				if (distsAB.size() >= 12) {
					System.out.println();
					System.out.println(distsA);
					System.out.println(distsB);
					System.out.println(distsAB);
					System.out.println(distsAB.size());
					// guess that relBeaconB and relBeaconA are the same
					AOCScanner scrB2 = scrB.createCopy();
					// move all points so, that relBeaconB is at (0,0,0) 
					scrB2.offset(new Point3D().sub(relBeaconB));
					// now try all rotations (do not care about left-handed and right-handed asymetrie...)
					for (int rx=-3; rx<=3; rx++) {
						if (rx == 0) { continue; }
						for (int ry=-3; ry<=3; ry++) {
							if ((ry==0) || (ry==rx)  || (ry==-rx)) { continue; }
							for (int rz=-3; rz<=3; rz++) {
								if ((rz==0) || (rz==rx)  || (rz==-rx) || (rz==ry)  || (rz==-ry)) { continue; }
								RotMatrix rmat = createRotMatrix(rx, ry, rz);
								AOCScanner scrB3 = scrB2.createCopy();
								scrB3.rot(rmat);
								// move origin to relBeaconA
								scrB3.offset(relBeaconA);
								// check how many identical relBeacons are in both scanners
								List<Point3D> beaconsB = new ArrayList<>(scrB3.getRelBeaconPosList());
								beaconsB.retainAll(scrA.getRelBeaconPosList());
								if (beaconsB.size() >= 12) {
									System.out.println(rmat);
									System.out.println(beaconsB.size());
									System.out.println("A:   "+scrA);
									System.out.println("B:   "+scrB3);
									System.out.println("A+B: "+beaconsB);
									return scrB3;
								}
							}
						}
					}
				}					
			}
		}
		return null;
	}

	private static int[][] R_VECTS = {
			{ 0, 0,-1 },
			{ 0,-1, 0 },
			{-1, 0, 0 },
			{ 0, 0, 0 },
			{ 1, 0, 0 },
			{ 0, 1, 0 },
			{ 0, 0, 1 }
	};

	private static RotMatrix createRotMatrix(int rx, int ry, int rz) {
		int[][] mat = new int[3][];
		mat[0] = R_VECTS[rx+3];
		mat[1] = R_VECTS[ry+3];
		mat[2] = R_VECTS[rz+3];
		return new RotMatrix(mat);
	}



	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
