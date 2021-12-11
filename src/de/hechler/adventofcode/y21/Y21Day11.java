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
		private int[][] energy;
		private int countFlashes;
		public OctopusField() {
			countFlashes = 0;
			energy = new int[10][10];
		}
		public void setRow(int row, String numString) {
			for (int col=0; col<numString.length(); col++) {
				energy[row][col] = numString.charAt(col)-'0';
			}
		}
		public void tick() {
			for (int row=0; row<10; row++ ) {
				for (int col=0; col<10; col++ ) {
					addEnergy(row, col);
				}
			}
			resetflashed();
		}
		public void addEnergy(int row, int col) {
			if ((row<0) || (row>9) || (col<0) || (col>9)) {
				return;
			}
			energy[row][col]++;
			if (energy[row][col] == 10) {
				flash(row, col);
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
			for (int row=0; row<10; row++ ) {
				for (int col=0; col<10; col++ ) {
					if (energy[row][col] >= 10) {
						energy[row][col] = 0;
					}
				}
			}
		}
		private boolean isEmpty() {
			return Utils.sum(energy) == 0;
		}
		public int getCountFlashes() {
			return countFlashes;
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

	
	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
