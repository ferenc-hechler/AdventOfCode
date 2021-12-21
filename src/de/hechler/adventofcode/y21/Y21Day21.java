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
 * see: https://adventofcode.com/2021/day/21
 *
 */
public class Y21Day21 {

	private final static String INPUT_RX = "^Player ([12]) starting position: ([0-9]+)$";

	public static class DeterministicDice {
		int nextValue = 1;
		int cntRolls = 0;
		public int roll() {
			int result = nextValue;
			nextValue = (nextValue%100)+1;
			cntRolls++;
			return result;
		}
	}
	
	public static void mainPart1() throws FileNotFoundException {

		try (Scanner scanner = new Scanner(new File("input/y21/day21.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				int start1 = Integer.parseInt(line.replaceFirst(INPUT_RX, "$2"));
				line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				int start2 = Integer.parseInt(line.replaceFirst(INPUT_RX, "$2"));
				DeterministicDice dice = new DeterministicDice();
				int player1Position=start1;
				int player2Position=start2;
				int player1Score = 0;
				int player2Score = 0;
				while ((player1Score<1000) && (player2Score<1000)) {
					player1Position = ((player1Position + dice.roll() + dice.roll() + dice.roll()-1)%10)+1;
					player1Score += player1Position;
					if (player1Score>=1000) {
						break;
					}
					player2Position = ((player2Position + dice.roll() + dice.roll() + dice.roll()-1)%10)+1;
					player2Score += player2Position;
					System.out.println("1: "+player1Score+", 2: "+player2Score);
				}
				int looser = Math.min(player1Score, player2Score);
				System.out.println("RESULT: "+dice.cntRolls+" * "+looser+" = "+(dice.cntRolls*looser));
			}
		}
	}


	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
