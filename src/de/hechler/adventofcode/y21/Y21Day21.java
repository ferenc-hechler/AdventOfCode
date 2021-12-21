package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.TreeMap;

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

	/** DISTRIBUTION_THREE_ROLLS[result]=possibilities */
	private final static long[] DISTRIBUTION_THREE_ROLLS = { 0,0,0,1,3,6,7,6,3,1 };

	public static class GameState implements Comparable<GameState> {
		public boolean nextMovePlayer1;
		public int player1Pos;
		public int player2Pos;
		public int player1Score;
		public int player2Score;
		public long countOccurrences;
		public GameState(int player1Pos, int player2Pos, int player1Score, int player2Score, boolean nextMovePlayer1, long countOccurrences) {
			this.player1Pos = player1Pos;
			this.player2Pos = player2Pos;
			this.player1Score = player1Score;
			this.player2Score = player2Score;
			this.nextMovePlayer1 = nextMovePlayer1;
			this.countOccurrences = countOccurrences;
		}
		public int getWinner() {
			if (player1Score >= 21) {
				return 1;
			}
			if (player2Score >= 21) {
				return 2;
			}
			return 0;
		}
		public int getProgress() { return player1Score+player2Score; }
		@Override
		public int hashCode() {
			return Objects.hash(nextMovePlayer1, player1Pos, player1Score, player2Pos, player2Score);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!(obj instanceof GameState))
				return false;
			GameState other = (GameState) obj;
			return nextMovePlayer1 == other.nextMovePlayer1 && player1Pos == other.player1Pos
					&& player1Score == other.player1Score && player2Pos == other.player2Pos
					&& player2Score == other.player2Score;
		}
		@Override
		public int compareTo(GameState other) {
			int result = Integer.compare(getProgress(), other.getProgress());
			if (result != 0) return result;
			result = Boolean.compare(nextMovePlayer1, other.nextMovePlayer1);
			if (result != 0) return result;			
			result = Integer.compare(player1Pos, other.player1Pos);
			if (result != 0) return result;
			result = Integer.compare(player2Pos, other.player2Pos);
			if (result != 0) return result;			
			result = Integer.compare(player1Score, other.player1Score);
			if (result != 0) return result;
			result = Integer.compare(player2Score, other.player2Score);
			return 0;
		}

		public List<GameState> nextPlayerMove() {
			List<GameState> result = new ArrayList<>();
			if (nextMovePlayer1) {
				for (int threeD=3; threeD<=9; threeD++) {
					int newPos = ((player1Pos + threeD -1) % 10) + 1;
					int newScore = player1Score + newPos;
					long cnt = DISTRIBUTION_THREE_ROLLS[threeD];
					result.add(new GameState(newPos, player2Pos, newScore, player2Score, !nextMovePlayer1, cnt*countOccurrences));
				}
			}
			else {
				for (int threeD=3; threeD<=9; threeD++) {
					int newPos = ((player2Pos + threeD -1) % 10) + 1;
					int newScore = player2Score + newPos;
					long cnt = DISTRIBUTION_THREE_ROLLS[threeD];
					result.add(new GameState(player1Pos, newPos, player1Score, newScore, !nextMovePlayer1, cnt*countOccurrences));
				}
			}
			return result;
		}
		@Override
		public String toString() {
			return "GSx" + countOccurrences + "(" + (nextMovePlayer1?"A":"B") + "," + player1Score + ":" + player2Score + ",(" + player1Pos + "," + player2Pos +")";
		}
		
		
	}
	
	public static void mainPart2() throws FileNotFoundException {

		
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

				long[] wins = {0L, 0L, 0L}; 
				TreeMap<GameState, GameState> multiverse = new TreeMap<>(); 
				GameState initialGame = new GameState(start1, start2, 0, 0, true, 1);
				multiverse.put(initialGame, initialGame);
				int cnt = 0;
				while (!multiverse.isEmpty()) {
					GameState game = multiverse.pollFirstEntry().getKey();
					if (cnt++%1000 == 1) {
						System.out.println(multiverse.size()+" - " + game);
					}
					if (game.getWinner() != 0) {
						wins[game.getWinner()] += game.countOccurrences;
						continue;
					}
//					System.out.println("  " + game);
					List<GameState> nextGames = game.nextPlayerMove();
//					System.out.println("  " + nextGames);
					for (GameState nextGame:nextGames) {
						if (multiverse.containsKey(nextGame)) {
							GameState existingNextGame = multiverse.get(nextGame); 
//							System.out.println(nextGame + " = "+ existingNextGame);
							existingNextGame.countOccurrences += nextGame.countOccurrences;
						}
						else {
							multiverse.put(nextGame, nextGame);
						}
					}
				}
				System.out.println("WIN1: "+wins[1]);
				System.out.println("WIN2: "+wins[2]);
				System.out.println("MAX:  "+Math.max(wins[1], wins[2]));
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		mainPart2();
	}

	
}
