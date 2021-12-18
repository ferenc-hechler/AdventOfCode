package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * see: https://adventofcode.com/2021/day/18
 *
 */
public class Y21Day18 {

	private final static String INPUT_RX = "^([0-9\\[\\],]+)$";

	public static abstract class SFExpression {
		protected SFTerm parent; 
		public SFExpression() { parent = null; }
		public boolean isValue() { return false; }
		public boolean isTerm() { return false; }
		public SFExpression getLeftExpression() { throw new UnsupportedOperationException(); }
		public SFExpression getRightExpression() { throw new UnsupportedOperationException(); }
		public int getValue() { throw new UnsupportedOperationException(); }
		public int getMagnitude() { throw new UnsupportedOperationException(); }
		public static SFTerm readTerm(String input) {
			return read(new Tokenizer(input)).asTerm();
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
		public SFValue asValue() { return isValue() ? (SFValue)this : null; }
		public SFTerm asTerm() { return isTerm() ? (SFTerm)this : null; }
		protected boolean explode(int depth) { return false; }
		protected boolean split() { return false; }
		protected void setParent(SFTerm parent) { this.parent=parent; }
		protected void propagateToLeft(int n) { throw new UnsupportedOperationException(); }
		protected void propagateToRight(int n) { throw new UnsupportedOperationException(); }
	}
	
	public static class SFValue extends SFExpression {
		private int value;
		public SFValue(int value) { this.value = value; }
		public int getValue() { return value; }
		public boolean isValue() { return true; }
		protected void propagateToLeft(int n) { value += n; }
		protected void propagateToRight(int n) { value += n; }
		public int getMagnitude() { return value; }
		@Override public String toString() { return ""+value; }
	}
	
	public static class SFTerm extends SFExpression {
		private SFExpression leftExpression; 
		private SFExpression rightExpression;
		public SFTerm(SFExpression leftExpression, SFExpression rightExpression) {
			this.leftExpression = leftExpression;
			this.rightExpression = rightExpression;
			this.parent = null;
			leftExpression.setParent(this);
			rightExpression.setParent(this);
			
		}
		public SFExpression getLeftExpression() { return leftExpression; }
		public SFExpression getRightExpression() { return rightExpression; }
		public boolean isTerm() { return true; }
		public boolean isSimpleExpression() { return leftExpression.isValue() && rightExpression.isValue(); }
		public SFTerm add(SFTerm other) {
			SFTerm result = new SFTerm(this, other);
			result.reduce();
			return result;
		}
		private void reduce() {
			boolean changed = true;
			while (changed) {
				changed = explode(1);
				if (changed) { continue; }
				changed = split();
			}
		}
		@Override protected boolean explode(int depth) {
			if (isSimpleExpression()) {
				return false;
			}
			boolean changed = false;
			if (depth < 4) {
				changed |= leftExpression.explode(depth+1);
				changed |= rightExpression.explode(depth+1);
				return changed;
			}
			if (leftExpression.isTerm()) {
//				System.out.println("BEFORE EXPLODE LEFT:  "+getRootParent());
				assert leftExpression.asTerm().isSimpleExpression();
				int vLeft = leftExpression.asTerm().getLeftExpression().asValue().getValue();
				int vRight = leftExpression.asTerm().getRightExpression().asValue().getValue();
				leftExpression = new SFValue(0);
				leftExpression.setParent(this);
				SFTerm parentWithNewLeftChild = findParentWithNewLeftChild();
				if (parentWithNewLeftChild != null) {
					parentWithNewLeftChild.getLeftExpression().propagateToRight(vLeft);
				}
				rightExpression.propagateToLeft(vRight);
//				System.out.println("AFTER EXPLODE LEFT:   "+getRootParent());
				changed = true;
			}
			if (rightExpression.isTerm()) {
//				System.out.println("BEFORE EXPLODE RIGHT: "+getRootParent());
				assert rightExpression.asTerm().isSimpleExpression();
				int vLeft = rightExpression.asTerm().getLeftExpression().asValue().getValue();
				int vRight = rightExpression.asTerm().getRightExpression().asValue().getValue();
				rightExpression = new SFValue(0);
				rightExpression.setParent(this);
				leftExpression.propagateToRight(vLeft);
				SFTerm parentWithNewRightChild = findParentWithNewRightChild();
				if (parentWithNewRightChild != null) {
					parentWithNewRightChild.getRightExpression().propagateToLeft(vRight);
				}
//				System.out.println("AFTER EXPLODE RIGHT:  "+getRootParent());
				changed = true;
			}
			return changed;
		}
		private SFTerm getRootParent() {
			if (parent == null) {
				return this;
			}
			return parent.getRootParent();
		}
		@Override protected boolean split() {
			if (leftExpression.isValue()) {
				if (leftExpression.getValue() > 9) {
//					System.out.println("BEFORE SPLIT:         "+getRootParent());
					int n = leftExpression.getValue();
					leftExpression = new SFTerm(new SFValue(n/2), new SFValue((n+1)/2));
					leftExpression.setParent(this);
//					System.out.println("AFTER SPLIT:          "+getRootParent());
					return true;
				}
			}
			else {
				if (leftExpression.split()) {
					return true;
				}
			}
			if (rightExpression.isValue()) { 
				if (rightExpression.getValue() > 9) {
					int n = rightExpression.getValue();
					rightExpression = new SFTerm(new SFValue(n/2), new SFValue((n+1)/2));
					rightExpression.setParent(this);
					return true;
				}
			}
			else {
				if (rightExpression.split()) {
					return true;
				}
			}
			return false;
		};
		private SFTerm findParentWithNewLeftChild() {
			if (parent == null) {
				return null;
			}
			if (parent.getLeftExpression() != this) {
				return parent;
			}
			return parent.findParentWithNewLeftChild();
		}
		private SFTerm findParentWithNewRightChild() {
			if (parent == null) {
				return null;
			}
			if (parent.getRightExpression() != this) {
				return parent;
			}
			return parent.findParentWithNewRightChild();
		}
		@Override protected void propagateToLeft(int n) { 
			leftExpression.propagateToLeft(n);
		}
		@Override protected void propagateToRight(int n) { 
			rightExpression.propagateToRight(n);
		}
		public int getMagnitude() { return 3*leftExpression.getMagnitude()+2*rightExpression.getMagnitude(); }
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
		
//		SFTerm testTerm = SFExpression.readTerm("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]");
//		System.out.println(testTerm);
//		testTerm.reduce(1);
//		System.out.println(testTerm);
		
		try (Scanner scanner = new Scanner(new File("input/y21/day18.txt"))) {
			SFTerm lastTerm = null;
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				SFTerm term = SFExpression.readTerm(line);
				if (lastTerm == null) {
					System.out.println(term);
					lastTerm = term;
				}
				else{
					System.out.println();
					System.out.println(lastTerm + " + " + term);
					lastTerm = lastTerm.add(term);
					System.out.println(" = " + lastTerm);
					System.out.println("   MAG: " + lastTerm.getMagnitude());
				}
			}
		}
	}
	
	public static void mainPart2() throws FileNotFoundException {

		try (Scanner scanner = new Scanner(new File("input/y21/day18.txt"))) {
			List<String> terms = new ArrayList<>();
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				terms.add(line);
			}
			int maxMag = 0;
			for (String term1:terms) {
				for (String term2:terms) {
					SFTerm sfTerm1 = SFExpression.readTerm(term1);
					SFTerm sfTerm2 = SFExpression.readTerm(term2);
					sfTerm1 = sfTerm1.add(sfTerm2);
					int mag = sfTerm1.getMagnitude(); 
					if (mag > maxMag) {
						maxMag = mag;
						System.out.println(term1+" + "+term2 + " = "+sfTerm1);
						System.out.println("MAG=" + mag);
					}
				}
			}
		}
	}
	

	
	public static void main(String[] args) throws FileNotFoundException {
		mainPart2();
	}

	
}
