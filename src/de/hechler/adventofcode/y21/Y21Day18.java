package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import de.hechler.adventofcode.y21.GeoUtils.Area;
import de.hechler.adventofcode.y21.GeoUtils.Point;

/**
 * see: https://adventofcode.com/2021/day/18
 *
 */
public class Y21Day18 {

	private final static String INPUT_RX = "^([0-9\\[\\],]+)$";

	public static abstract class SFExpression {
		public boolean isValue() { return false; }
		public boolean isTerm() { return false; }
		public SFExpression getLeftExpression() { throw new UnsupportedOperationException(); }
		public SFExpression getRightExpression() { throw new UnsupportedOperationException(); }
		public int getValue() { throw new UnsupportedOperationException(); }
		public static SFExpression read(String input) {
			return read(new Tokenizer(input));
		}
		public static SFExpression read(Tokenizer tok) {
			SFExpression result;
			if (tok.peek()=='[') {
				tok.read('[');
				SFExpression leftExp = read(tok);
				tok.read(',');
				SFExpression rightExp = read(tok);
				tok.read(']');
				result = new SFTerm(leftExp, rightExp);
			}
			else {
				result = new SFValue(tok.readNum());
			}
			return result;
		}
	}
	
	public static class SFValue extends SFExpression {
		private int value;
		public SFValue(int value) { this.value = value; }
		public int getValue() { return value; }
		@Override public String toString() { return ""+value; }
	}
	
	public static class SFTerm extends SFExpression {
		private SFExpression leftExpression; 
		private SFExpression rightExpression;
		public SFTerm(SFExpression leftExpression, SFExpression rightExpression) {
			this.leftExpression = leftExpression;
			this.rightExpression = rightExpression;
		}
		public SFExpression getLeftExpression() { return leftExpression; }
		public SFExpression getRightExpression() { return rightExpression; }
		@Override public String toString() { return "["+leftExpression+","+rightExpression+"]"; }
	}
	
	
	public static class Tokenizer {
		private String input;
		private int nextPos;
		public Tokenizer(String input) {
			this.input = input;
			this.nextPos = 0;
		}
		public boolean hasNextToken() {
			return input.length() > nextPos;
		}
		public char peek() {
			return input.charAt(nextPos);
		}
		public void read(int charToRead) {
			if (nextToken()!=charToRead) {
				throw new RuntimeException("unexpected char '"+lastToken()+"' at position "+(nextPos-1)+" '"+charToRead+"' expected");
			}
		}
		public int readNum() {
			char c = nextToken(); 
			if (c<'0' || c>'9') {
				throw new RuntimeException("unexpected char '"+c+"' at position "+(nextPos-1)+" number expected");
			}
			return c-'0';
		}
		public char nextToken() {
			return input.charAt(nextPos++);
		}
		public char lastToken() {
			return input.charAt(nextPos-1);
		}
	}
	
	public static void mainPart1() throws FileNotFoundException {
		
		try (Scanner scanner = new Scanner(new File("input/y21/day18example2.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				SFExpression exp = SFExpression.read(line);
				System.out.println(line + " -> "+exp);
			}
		}
	}
	
	
	
	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
