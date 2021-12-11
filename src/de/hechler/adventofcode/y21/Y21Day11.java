package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * see: https://adventofcode.com/2021/day/11
 *
 */
public class Y21Day11 {

	private final static String INPUT_RX = "^([0-9]{10})$";
	
	private static class OctopusField {
		private static interface OFVisitor { public void visit(int row, int col, int value); }
		private int[][] energy;
		private int countFlashes;
		public OctopusField() {
			energy = new int[10][10];
			countFlashes = 0;
		}
		public void setRow(int row, String numString) {
			for (int col=0; col<numString.length(); col++) {
				energy[row][col] = numString.charAt(col)-'0';
			}
		}
		public void tick() {
			forEach((r,c,e)->addEnergy(r,c));
			resetflashed();
		}
		public void addEnergy(int row, int col) {
			if (Utils.checkRange(row, 0, 9) && Utils.checkRange(col, 0, 9) ) {
				energy[row][col]++;
				if (energy[row][col] == 10) {
					flash(row, col);
				}
			}
		}
		private void flash(int row, int col) {
			countFlashes++;
			for (int r=row-1; r<=row+1; r++ ) {
				for (int c=col-1; c<=col+1; c++ ) {
					addEnergy(r, c);    // self adding does not hurt r=row, c=col, bc aufter resetFlashed() it will be reset to 0.
				}
			}
		}
		private void resetflashed() {
			forEach((r, c, e) -> {if (e>=10) energy[r][c] = 0;});
		}
		private boolean isEmpty() {
			return Utils.sum(energy) == 0;
		}
		public int getCountFlashes() {
			return countFlashes;
		}
		private void forEach(OFVisitor visitor) {
			for (int row=0; row<10; row++ ) {
				for (int col=0; col<10; col++ ) {
					visitor.visit(row, col, energy[row][col]);
				}
			}
		}
		@Override
		public String toString() {
			return Utils.toString(energy, "");
		}
	}

	public static void mainPart1() throws FileNotFoundException {
		
		try (Scanner scanner = new Scanner(new File("input/y21/day11.txt"))) {
			int row = 0;
			OctopusField octs = new OctopusField();
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				octs.setRow(row++, line);
			}
			for (int steps=0; steps<100; steps++) {
				octs.tick();
				System.out.println("\n");
				System.out.println("STEPS: " + steps);
				System.out.println(octs.toString());
			}
			System.out.println("CountFlashes: "+octs.getCountFlashes());
		}
	}

	public static void mainPart2() throws FileNotFoundException {
		
		try (Scanner scanner = new Scanner(new File("input/y21/day11.txt"))) {
			int row = 0;
			OctopusField octs = new OctopusField();
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				octs.setRow(row++, line);
			}
			int step = 0;
			while (!octs.isEmpty()) {
				step++;
				octs.tick();
			}
			System.out.println("Steps: "+step);
		}
	}

	
	public static void main(String[] args) throws FileNotFoundException {
		mainPart2();
	}

	
}
