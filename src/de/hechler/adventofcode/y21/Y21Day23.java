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
public class Y21Day23 {

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
	 *	      ########################
	 * 
	 */
	
	public static class World {
		private int[] field;

		private final static int[][] MOVES_SIDEROOM_TO_FLOOR = {
				{11, 2, 1, 0},
				{11, 2, 1},
				{11, 2, 3},
				{11, 2, 3, 4, 5},
				{11, 2, 3, 4, 5, 6, 7},
				{11, 2, 3, 4, 5, 6, 7, 8, 9},
				{11, 2, 3, 4, 5, 6, 7, 8, 9, 10},
				
				{12, 4, 3, 2, 1, 0},
				{12, 4, 3, 2, 1},
				{12, 4, 3},
				{12, 4, 5},
				{12, 4, 5, 6, 7},
				{12, 4, 5, 6, 7, 8, 9},
				{12, 4, 5, 6, 7, 8, 9, 10},
				
				{13, 6, 5, 4, 3, 2, 1, 0},
				{13, 6, 5, 4, 3, 2, 1},
				{13, 6, 5, 4, 3},
				{13, 6, 5},
				{13, 6, 7},
				{13, 6, 7, 8, 9},
				{13, 6, 7, 8, 9, 10},

				{14, 8, 7, 6, 5, 4, 3, 2, 1, 0},
				{14, 8, 7, 6, 5, 4, 3, 2, 1},
				{14, 8, 7, 6, 5, 4, 3},
				{14, 8, 7, 6, 5},
				{14, 8, 7},
				{14, 8, 9},
				{14, 8, 9, 10},

				{15, 11, 2, 1, 0},
				{15, 11, 2, 1},
				{15, 11, 2, 3},
				{15, 11, 2, 3, 4, 5},
				{15, 11, 2, 3, 4, 5, 6, 7},
				{15, 11, 2, 3, 4, 5, 6, 7, 8, 9},
				{15, 11, 2, 3, 4, 5, 6, 7, 8, 9, 10},
				
				{16, 12, 4, 3, 2, 1, 0},
				{16, 12, 4, 3, 2, 1},
				{16, 12, 4, 3},
				{16, 12, 4, 5},
				{16, 12, 4, 5, 6, 7},
				{16, 12, 4, 5, 6, 7, 8, 9},
				{16, 12, 4, 5, 6, 7, 8, 9, 10},
				
				{17, 13, 6, 5, 4, 3, 2, 1, 0},
				{17, 13, 6, 5, 4, 3, 2, 1},
				{17, 13, 6, 5, 4, 3},
				{17, 13, 6, 5},
				{17, 13, 6, 7},
				{17, 13, 6, 7, 8, 9},
				{17, 13, 6, 7, 8, 9, 10},

				{18, 14, 8, 7, 6, 5, 4, 3, 2, 1, 0},
				{18, 14, 8, 7, 6, 5, 4, 3, 2, 1},
				{18, 14, 8, 7, 6, 5, 4, 3},
				{18, 14, 8, 7, 6, 5},
				{18, 14, 8, 7},
				{18, 14, 8, 9},
				{18, 14, 8, 9, 10},
		};
		private static Map<Integer, int[]> source2targetMap;
		private static void fillSource2targetMap() {
			source2targetMap = new HashMap<>();
			for (int[] moveToFloor:MOVES_SIDEROOM_TO_FLOOR) {
				int floor = moveToFloor[moveToFloor.length-1];
				int sideway= moveToFloor[0];
				source2targetMap.put(sideway*100+floor, moveToFloor);
				int[] way = new int[moveToFloor.length];
				for (int i=0; i<moveToFloor.length; i++) {
					way[i] = moveToFloor[moveToFloor.length-1-i];
				}
				source2targetMap.put(floor*100+sideway, way); 
			}
		}
		

		public World(int[] field) {
			this.field = field;
		}
		
		public World(int f11, int f12, int f13, int f14, 
					 int f15, int f16, int f17, int f18) {
			field = new int[19];
			field[11] = f11;
			field[12] = f12;
			field[13] = f13;
			field[14] = f14;
			field[15] = f15;
			field[16] = f16;
			field[17] = f17;
			field[18] = f18;
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
			// if bottom is free move there 
			for (int i=11; i<=14; i++) {
				int player = i-10;
				if (field[i]==0) {
					// inner field is empty check bottom
					int j = i+4;
					if ((field[j]!=player) && (field[j]!=0)) {
						for (int f=0; f<=10; f++) {
								if (checkMove(j, f)) {
									result.add(createMove(j, f));
								}
							}
						}
					}
				else if (field[i]!=player) {
					// bottom field can be moved
					for (int f=0; f<=10; f++) {
						if (field[f] == 0) {
							if (checkMove(i, f)) {
								result.add(createMove(i, f));
							}
						}
					}
				}
			}
		}
			
		private void addMovesToTarget(List<World> result) {
			// if bottom is free move there 
			for (int i=15; i<=18; i++) {
				int player = i-14;
				if (field[i]==0) {
					for (int f=0; f<=10; f++) {
						if (field[f] == player) {
							if (checkMove(f, i)) {
								result.add(createMove(f, i));
							}
						}
					}
				}
				else if (field[i]==player) {
					// bottom is filled with correct player, check second level
					int j = i-4;
					if (field[j]==0) {
						for (int f=0; f<=10; f++) {
							if (field[f] == player) {
								if (checkMove(f, j)) {
									result.add(createMove(f, j));
								}
							}
						}
					}
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
				if (field[i] != 0) {
					return false;
				}
			}
			return true;
		}

		private World createMove(int from, int to) {
			int[] movedField = new int[field.length];
			System.arraycopy(field, 0, movedField, 0, field.length);
			movedField[from] = 0; 
			movedField[to] = field[from]; 
			return new World(movedField);
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
			result.append("  #########\r\n");
			return result.toString();
		}
	}
	
	public static void mainPart1() throws FileNotFoundException {

		try (Scanner scanner = new Scanner(new File("input/y21/day23example2.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (!line.matches(INPUT_RX1)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX1 '"+INPUT_RX1+"'");
				}
				line = scanner.nextLine();
				if (!line.matches(INPUT_RX2)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX2 '"+INPUT_RX2+"'");
				}
				int[] field = new int[19];
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
				if (!line.matches(INPUT_RX5)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX5 '"+INPUT_RX4+"'");
				}

//				System.out.println("INPUT: "+amber1+","+bronce1+","+copper1+","+desert1);
//				System.out.println("       "+amber2+","+bronce2+","+copper2+","+desert2);
//				
				World world = new World(field);
				System.out.println(world.toString());
				
				List<World> nextMoves = world.calcNextMoves();
				
				while (!nextMoves.isEmpty()) {
					System.out.println("NEXT: "+nextMoves.size());
					List<World> currentMoves = nextMoves;
					nextMoves = new ArrayList<>();
					for (World currentWorld:currentMoves) {
						System.out.println(currentWorld);
						nextMoves.addAll(currentWorld.calcNextMoves());
					}
				}
			}
			
			
			
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		World.fillSource2targetMap();
		mainPart1();
	}

	
}
