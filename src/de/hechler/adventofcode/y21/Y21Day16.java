package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * see: https://adventofcode.com/2021/day/16
 *
 */
public class Y21Day16 {

	private static final String INPUT_RX = "^([0-9A-F]+)$";

	private static final String[] OP = {"+", "*", "MIN", "MAX", "#", ">", "<", "="};
	
	public abstract static class BasePacket {
		protected int version;
		protected int typeID;
		public BasePacket(int version, int typeID) {
			this.version = version;
			this.typeID = typeID;
		}
		public int countVersions() { return version; }
		public abstract long calcResult();
		public String toInfixString() { return toString(); };
		@Override public String toString() { return OP[typeID]; }
	}
		
	public static class LiteralPacket extends BasePacket {
		public long value;
		public LiteralPacket(int version, int typeID, long value) {
			super(version, typeID);
			this.value = value;
		}
		@Override public String toString() { return ""+value; }
		@Override public long calcResult() { return value; }
	}

	public static class OperatorPacket extends BasePacket {
		public List<BasePacket> subPackets;
		public OperatorPacket(int version, int typeID) {
			super(version, typeID);
			subPackets = new ArrayList<>();
		}
		public void addSubPacket(BasePacket pak) {
			subPackets.add(pak);
		}
		@Override
		public int countVersions() {
			int result = version;
			for (BasePacket pak:subPackets) {
				result += pak.countVersions();
			}
			return result;
		}
		@Override
		public String toString() {
			StringBuilder result = new StringBuilder();
			result.append(OP[typeID]).append("(");
			String seperator = "";
			for (BasePacket pak:subPackets) {
				result.append(seperator);
				result.append(pak.toString());
				seperator = ",";
			}
			result.append(")");
			return result.toString();
		}
		@Override
		public String toInfixString() {
			if (OP[typeID].length()>1) {
				return toString(); 
			}
			StringBuilder result = new StringBuilder();
			result.append("(");
			String op = "";
			for (BasePacket pak:subPackets) {
				result.append(op);
				result.append(pak.toInfixString());
				op = OP[typeID];
			}
			result.append(")");
			return result.toString();
		}
		@Override
		public long calcResult() {
			long result, v1, v2;
			switch(typeID) {
			case 0: // sum
				result = 0;
				for (BasePacket pak:subPackets) {
					result += pak.calcResult();
				}
				break;
			case 1: // product
				result = 1;
				for (BasePacket pak:subPackets) {
					result *= pak.calcResult();
				}
				break;
			case 2: // minimum
				result = Long.MAX_VALUE;
				for (BasePacket pak:subPackets) {
					result = Math.min(result, pak.calcResult());
				}
				break;
			case 3: // maximum
				result = Long.MIN_VALUE;
				for (BasePacket pak:subPackets) {
					result = Math.max(result, pak.calcResult());
				}
				break;
			case 5: // greater than
				if (subPackets.size()!=2) throw new RuntimeException("invalid number of subpackets");
				v1 = subPackets.get(0).calcResult();
				v2 = subPackets.get(1).calcResult();
				result = (v1 > v2) ? 1 : 0;
				break;
			case 6: // less than
				if (subPackets.size()!=2) throw new RuntimeException("invalid number of subpackets");
				v1 = subPackets.get(0).calcResult();
				v2 = subPackets.get(1).calcResult();
				result = (v1 < v2) ? 1 : 0;
				break;
			case 7: // equal
				if (subPackets.size()!=2) throw new RuntimeException("invalid number of subpackets");
				v1 = subPackets.get(0).calcResult();
				v2 = subPackets.get(1).calcResult();
				result = (v1 == v2) ? 1 : 0;
				break;
			default:
				throw new RuntimeException("invalid operator type "+typeID);
			}
			return result;
		}
	}
	

	private static class BitStream {
		private int[] bits;
		private int nextPos;
		public BitStream(int[] bits) {
			this.bits = bits;
			this.nextPos = 0;
		}
		public boolean hasNext() {
			return nextPos < bits.length;
		}
		public int next() {
			int result = bits[nextPos];
			nextPos++;
			return result;
		}
		public BasePacket nextPacket() {
			int version = decodeBits(3);
			int typeID = decodeBits(3);
			if (typeID == 4) {
				long value = 0;
				boolean lastValue = false;
				while (!lastValue) {
					lastValue = next() == 0;
					value = (value << 4) | decodeBits(4);
				}
				return new LiteralPacket(version, typeID, value);
			}
			OperatorPacket result = new OperatorPacket(version, typeID);
			int lengthTypeID = next();
			if (lengthTypeID == 0) {
				int lengthInBits = decodeBits(15);
				int endPos = nextPos + lengthInBits;
				while (nextPos < endPos) {
					result.addSubPacket(nextPacket());
				}
			}
			else {
				int numSubPackets = decodeBits(11);
				for (int i=0; i<numSubPackets; i++) {
					result.addSubPacket(nextPacket());
				}
			}
			return result;
		}
		public int decodeBits(int size) {
			int result = 0;
			for (int i=0; i<size; i++) {
				result = (result << 1) | next();
			}
			return result;
		}
		@Override
		public String toString() {
			StringBuilder result = new StringBuilder();
			int from = Math.max(0, nextPos-5);
			int to = Math.min(bits.length-1, nextPos+15);
			result.append("[...");
			for (int i=from; i<=to; i++) {
				if (i == nextPos) {
					result.append("|");
				}
				result.append(""+bits[i]);
			}
			result.append("...]");
			return result.toString();
		}
	}
	
	private static int[] decodeHex(String hex) {
		List<Integer> result = new ArrayList<>(); 
		for (char c:hex.toCharArray()) {
			int n = Integer.parseInt(""+c, 16);
			result.add((n>>3)&0x01);
			result.add((n>>2)&0x01);
			result.add((n>>1)&0x01);
			result.add( n    &0x01);
		}
		return Utils.toIntArray(result);
	}

	public static void mainPart1() throws FileNotFoundException {
		
		try (Scanner scanner = new Scanner(new File("input/y21/day16.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX2 '"+INPUT_RX+"'");
				}
				System.out.println(line);
				int[] bits = decodeHex(line);
				BitStream bs = new BitStream(bits);
				BasePacket pak = bs.nextPacket();
				System.out.println(pak.toString()+" - "+bs.toString());
				System.out.println("#V="+pak.countVersions());
			}
		}
	}
	
	public static void mainPart2() throws FileNotFoundException {
		
		try (Scanner scanner = new Scanner(new File("input/y21/day16.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX2 '"+INPUT_RX+"'");
				}
				System.out.println(line);
				int[] bits = decodeHex(line);
				BitStream bs = new BitStream(bits);
				BasePacket pak = bs.nextPacket();
				System.out.println(pak.toString()+" - "+bs.toString());
				System.out.println(pak.toInfixString());
				System.out.println("="+pak.calcResult());
			}
		}
	}



	public static void main(String[] args) throws FileNotFoundException {
		mainPart2();
	}

	
}
