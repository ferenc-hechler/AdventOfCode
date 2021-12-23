package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * see: https://adventofcode.com/2021/day/3
 *
 */
public class Y21Day23p2 {

	private final static String INPUT_RX1 = "^#############$";
	private final static String INPUT_RX2 = "^#([A-D.]{11})#$";
	private final static String INPUT_RX3 = "^###([A-D.])#([A-D.])#([A-D.])#([A-D.])###$";
	private final static String INPUT_RX4 = "^  #([A-D.])#([A-D.])#([A-D.])#([A-D.])#$";
	private final static String INPUT_RX5 = "^  #########$";

	
	
	/**
	 *	####################################
	 *	#  0  1  2  3  4  5  6  7  8  9 10 #
	 *	####### 11  # 12  # 13  # 14 #######
	 *	      # 15  # 16  # 17  # 18 #
	 *	      # 19  # 20  # 21  # 22 #
	 *	      # 23  # 24  # 25  # 26 #
	 *	      ########################
	 * 
	 */
	
	public static class World {
		private int[] field;
		private int costs;
		private int minSolveCosts;

		private final static int[][] MOVES_SIDEROOM_TO_FLOOR = {
				{23, 19, 15, 11, 2, 1, 0},
				{23, 19, 15, 11, 2, 1},
				{23, 19, 15, 11, 2, 3},
				{23, 19, 15, 11, 2, 3, 4, 5},
				{23, 19, 15, 11, 2, 3, 4, 5, 6, 7},
				{23, 19, 15, 11, 2, 3, 4, 5, 6, 7, 8, 9},
				{23, 19, 15, 11, 2, 3, 4, 5, 6, 7, 8, 9, 10},
				
				{24, 20, 16, 12, 4, 3, 2, 1, 0},
				{24, 20, 16, 12, 4, 3, 2, 1},
				{24, 20, 16, 12, 4, 3},
				{24, 20, 16, 12, 4, 5},
				{24, 20, 16, 12, 4, 5, 6, 7},
				{24, 20, 16, 12, 4, 5, 6, 7, 8, 9},
				{24, 20, 16, 12, 4, 5, 6, 7, 8, 9, 10},
				
				{25, 21, 17, 13, 6, 5, 4, 3, 2, 1, 0},
				{25, 21, 17, 13, 6, 5, 4, 3, 2, 1},
				{25, 21, 17, 13, 6, 5, 4, 3},
				{25, 21, 17, 13, 6, 5},
				{25, 21, 17, 13, 6, 7},
				{25, 21, 17, 13, 6, 7, 8, 9},
				{25, 21, 17, 13, 6, 7, 8, 9, 10},

				{26, 22, 18, 14, 8, 7, 6, 5, 4, 3, 2, 1, 0},
				{26, 22, 18, 14, 8, 7, 6, 5, 4, 3, 2, 1},
				{26, 22, 18, 14, 8, 7, 6, 5, 4, 3},
				{26, 22, 18, 14, 8, 7, 6, 5},
				{26, 22, 18, 14, 8, 7},
				{26, 22, 18, 14, 8, 9},
				{26, 22, 18, 14, 8, 9, 10},
		};
		private static Map<Integer, int[]> source2targetMap;
		private static Map<Integer, Integer> source2targetDist;
		private static void fillSource2targetMap() {
			source2targetMap = new HashMap<>();
			source2targetDist = new HashMap<>();
			for (int[] moveToFloor:MOVES_SIDEROOM_TO_FLOOR) {
				for (int skip=0; skip<=3; skip++) {
					int[] way = new int[moveToFloor.length-skip];
					System.arraycopy(moveToFloor, skip, way, 0, way.length);
					int floor = way[way.length-1];
					int sideway= way[0];
					source2targetMap.put(sideway*100+floor, way);
					source2targetDist.put(sideway*100+floor, way.length-1);
					int[] revWay = new int[way.length];
					for (int i=0; i<way.length; i++) {
						revWay[i] = way[way.length-1-i];
					}
					source2targetMap.put(floor*100+sideway, revWay); 
					source2targetDist.put(floor*100+sideway, revWay.length-1);
				}
			}
		}
		

		public World(int costs, int minSolveCosts, int[] field) {
			this.costs = costs;
			this.minSolveCosts = minSolveCosts;
			this.field = field;
		}

		private final static int[] compensateDepth = {0, 0, 1, 3, 6};
		
		public void recalcMinSolveCostForField() {
			minSolveCosts = 0;
			for (int i=0; i<=26; i++) {
				minSolveCosts += calcMinSolveCostForField(i);
			}
			for (int player=1; player<=4; player++) {
				int depth = depth(targetForPlayer(player));
				if (depth > 1) {
					minSolveCosts -= compensateDepth[depth]*MOVE_COSTS[player];
				}
			}
			
		}

		public int calcMinSolveCostForField(int n) {
			int player = field[n];
			if (player == 0) {
				return 0;
			}
			int targetForPlayer = targetForPlayer(player);
			if (targetForPlayer == -1) {
				return 0;
			}
			if (isFloor(n)) {
				int steps = source2targetDist.get(n*100+targetForPlayer);
				return steps*MOVE_COSTS[player];
			}
			if ((n == targetForPlayer+4) || (n == targetForPlayer+8) || (n == targetForPlayer+12) || (n == targetForPlayer+16)) {
				return 0;
			}
			int sourceDepth = depth(n);
			int targetDepth = depth(targetForPlayer);
			int playerFromTarget = playerForTarget(n);
			int dist = Math.max(1, Math.abs(playerFromTarget-player));
			int steps = sourceDepth+targetDepth+dist*2;
			return steps*MOVE_COSTS[player];
		}
		
		private int depth(int n) {
			return (n-7)/4;
		}


		private int targetForPlayer(int player) {
			for (int lastPlayerTarget=22+player; lastPlayerTarget > 10; lastPlayerTarget-=4) {
				if (field[lastPlayerTarget] != player) {
					return lastPlayerTarget;
				}
			}
			return -1;
		}


		private static boolean isFloor(int n) {
			return n<=10;
		}


		public boolean isFinished() {
			return (field[11] == 1) && (field[15] == 1) && 
				   (field[12] == 2) && (field[16] == 2) && 
				   (field[13] == 3) && (field[17] == 3) && 
				   (field[14] == 4) && (field[18] == 4); 
		}
		
		public List<World> calcNextMoves() {
			List<World> result = new ArrayList<>();
			addMovesToTarget(result);
			if (!result.isEmpty()) {
				return result.subList(0, 1);
			}
			addAllMovesToFloor(result);
			return result;
		}
		
		private void addAllMovesToFloor(List<World> result) {
			for (int sideway=1; sideway<=4; sideway++) {
				int firstNonFree = findFirstMovable(sideway);
				if (firstNonFree == -1) {
					continue;
				}
				if (field[firstNonFree] == sideway) {
					int target = targetForPlayer(sideway);
					if ((target == -1) || (firstNonFree >= target)) {
						// already at target position, nothing to do...
						continue;
					}
				}
				for (int i=0; i<=10; i++) {
					if (checkMove(firstNonFree, i)) {
						result.add(createMove(firstNonFree, i));
					}
				}
			}
		}
			
		private int findFirstMovable(int sideway) {
			for (int pos=10+sideway; pos<=26; pos+=4) {
				if (field[pos] != 0) {
					return pos;
				}
			}
			return -1;
		}


		private void addMovesToTarget(List<World> result) {
			// if bottom is free move there 
			for (int i=0; i<=10; i++) {
				int player = field[i];
				if (player == 0) {
					continue;
				}
				int target = targetForPlayer(player);
				if (checkMove(i, target)) {
					result.add(createMove(i, target));
					return;
				}
			}
		}

		private int playerForTarget(int target) {
			return ((target+1)%4)+1;
		}
		
		private boolean checkMove(int source, int target) {
			int[] way = source2targetMap.get(source*100+target);
			if (way==null) {
				return false;
			}
			for (int i=1; i<way.length; i++) {
				if (field[way[i]] != 0) {
					return false;
				}
			}
			return true;
		}

		private final static int[] MOVE_COSTS = {0, 1, 10, 100, 1000};
		
		private World createMove(int from, int to) {
			int[] movedField = new int[field.length];
			System.arraycopy(field, 0, movedField, 0, field.length);
			int player = movedField[from]; 
			movedField[from] = 0; 
			movedField[to] = player; 
			int moveCosts = MOVE_COSTS[player]*(source2targetDist.get(from*100+to));
			World result = new World(costs+moveCosts, minSolveCosts, movedField);
			result.minSolveCosts = minSolveCosts - calcMinSolveCostForField(from) + result.calcMinSolveCostForField(to); 
			return result;
		}

		private final static String[] p = {".", "A", "B", "C", "D"};
		private final static String PLAYERNUMS = ".ABCD";
		
		@Override
		public String toString() {
			StringBuffer result = new StringBuffer();
			result.append("#############\r\n");
			result.append("#");
			for (int i=0; i<=10; i++) {
				result.append(p[field[i]]);
			}
			result.append("#\r\n");
			result.append("###");
			for (int i=11; i<=14; i++) {
				result.append(p[field[i]]);
				result.append("#");
			}
			result.append("##\r\n");
			result.append("  #");
			for (int i=15; i<=18; i++) {
				result.append(p[field[i]]);
				result.append("#");
			}
			result.append("\r\n");
			result.append("  #");
			for (int i=19; i<=22; i++) {
				result.append(p[field[i]]);
				result.append("#");
			}
			result.append("\r\n");
			result.append("  #");
			for (int i=23; i<=26; i++) {
				result.append(p[field[i]]);
				result.append("#");
			}
			result.append("\r\n");
			result.append("  #########\r\n");
			return result.toString();
		}


		public int getCosts() {
			return costs;
		}
	}
	
	public static void mainPart2() throws FileNotFoundException {

		try (Scanner scanner = new Scanner(new File("input/y21/day23p2.txt"))) {
			String line = scanner.nextLine();
			if (!line.matches(INPUT_RX1)) {
				throw new RuntimeException("invalid input line '"+line+"', not matching RX1 '"+INPUT_RX1+"'");
			}
			line = scanner.nextLine();
			if (!line.matches(INPUT_RX2)) {
				throw new RuntimeException("invalid input line '"+line+"', not matching RX2 '"+INPUT_RX2+"'");
			}
			int[] field = new int[27];
			String floorPlayers = line.replaceFirst(INPUT_RX2, "$1");
			for (int i=0; i<=10; i++) {
				field[i] = World.PLAYERNUMS.indexOf(floorPlayers.charAt(i));
			}
			line = scanner.nextLine();
			if (!line.matches(INPUT_RX3)) {
				throw new RuntimeException("invalid input line '"+line+"', not matching RX3 '"+INPUT_RX3+"'");
			}
			field[11] = World.PLAYERNUMS.indexOf(line.replaceFirst(INPUT_RX3, "$1").charAt(0));
			field[12] = World.PLAYERNUMS.indexOf(line.replaceFirst(INPUT_RX3, "$2").charAt(0));
			field[13] = World.PLAYERNUMS.indexOf(line.replaceFirst(INPUT_RX3, "$3").charAt(0));
			field[14] = World.PLAYERNUMS.indexOf(line.replaceFirst(INPUT_RX3, "$4").charAt(0));
			line = scanner.nextLine();
			if (!line.matches(INPUT_RX4)) {
				throw new RuntimeException("invalid input line '"+line+"', not matching RX4 '"+INPUT_RX4+"'");
			}
			field[15] = World.PLAYERNUMS.indexOf(line.replaceFirst(INPUT_RX4, "$1").charAt(0));
			field[16] = World.PLAYERNUMS.indexOf(line.replaceFirst(INPUT_RX4, "$2").charAt(0));
			field[17] = World.PLAYERNUMS.indexOf(line.replaceFirst(INPUT_RX4, "$3").charAt(0));
			field[18] = World.PLAYERNUMS.indexOf(line.replaceFirst(INPUT_RX4, "$4").charAt(0));
			line = scanner.nextLine();
			if (!line.matches(INPUT_RX4)) {
				throw new RuntimeException("invalid input line '"+line+"', not matching RX4 '"+INPUT_RX4+"'");
			}
			field[19] = World.PLAYERNUMS.indexOf(line.replaceFirst(INPUT_RX4, "$1").charAt(0));
			field[20] = World.PLAYERNUMS.indexOf(line.replaceFirst(INPUT_RX4, "$2").charAt(0));
			field[21] = World.PLAYERNUMS.indexOf(line.replaceFirst(INPUT_RX4, "$3").charAt(0));
			field[22] = World.PLAYERNUMS.indexOf(line.replaceFirst(INPUT_RX4, "$4").charAt(0));
			line = scanner.nextLine();
			if (!line.matches(INPUT_RX4)) {
				throw new RuntimeException("invalid input line '"+line+"', not matching RX4 '"+INPUT_RX4+"'");
			}
			field[23] = World.PLAYERNUMS.indexOf(line.replaceFirst(INPUT_RX4, "$1").charAt(0));
			field[24] = World.PLAYERNUMS.indexOf(line.replaceFirst(INPUT_RX4, "$2").charAt(0));
			field[25] = World.PLAYERNUMS.indexOf(line.replaceFirst(INPUT_RX4, "$3").charAt(0));
			field[26] = World.PLAYERNUMS.indexOf(line.replaceFirst(INPUT_RX4, "$4").charAt(0));
			line = scanner.nextLine();
			if (!line.matches(INPUT_RX5)) {
				throw new RuntimeException("invalid input line '"+line+"', not matching RX5 '"+INPUT_RX4+"'");
			}

//				System.out.println("INPUT: "+amber1+","+bronce1+","+copper1+","+desert1);
//				System.out.println("       "+amber2+","+bronce2+","+copper2+","+desert2);
//				
			World world = new World(0, 0, field);
			world.recalcMinSolveCostForField();
			System.out.println("MINSOLVE: " + world.minSolveCosts);
			System.out.println(world.toString());
			
			List<World> nextMoves = world.calcNextMoves();
			int bestCosts = Integer.MAX_VALUE;
			while (false || !nextMoves.isEmpty()) {
				System.out.println("NEXT: "+nextMoves.size());
				List<World> currentMoves = nextMoves;
				nextMoves = new ArrayList<>();
				for (World currentWorld:currentMoves) {
//					System.out.println(currentWorld);
					if (currentWorld.isFinished()) {
						int costs = currentWorld.getCosts();
						if (costs < bestCosts) {
							bestCosts = costs;
							System.out.println(bestCosts+ " - "+currentWorld.minSolveCosts);
						}
					}
					else {
						nextMoves.addAll(currentWorld.calcNextMoves());
					}
				}
			}
			System.out.println("BEST: "+bestCosts);
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		World.fillSource2targetMap();
		mainPart2();
	}

	
}
