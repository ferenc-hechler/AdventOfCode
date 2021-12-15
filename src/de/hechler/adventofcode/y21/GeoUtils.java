package de.hechler.adventofcode.y21;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class GeoUtils {
	
	/** do not allow instances to be created */
	private GeoUtils() {} 
	
	
	public static class Point {
		private int x;
		private int y;
		public Point() { this(0, 0); }
		public Point(int x, int y) { this.x = x; this.y = y; }
		public Point(Point p) { this(p.x, p.y); }
		public int x() { return x; }
		public int y() { return y; }
		public Point offset(int dx, int dy) { return new Point(x+dx, y+dy); }
		public List<Point> offsets(int... deltas) {
			List<Point> result = new ArrayList<>();
			for (int i=0; i<deltas.length; i+=2) {
				result.add(offset(deltas[i], deltas[i+1]));
			}
			return result; 
		}
		public List<Point> fourNeighbours() { 
			return offsets( 0,-1, -1,0, 1,0, 0,1 ); 
		}
		public List<Point> eightNeighbours() { 
			return offsets( -1,-1, 0,-1, 1,-1, -1,0, 1,0, -1,1, 0,1, 1,1 ); 
		}
		@Override public int hashCode() { return Objects.hash(x, y); }
		@Override public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Point other = (Point) obj;
			return x == other.x && y == other.y;
		}
		public boolean equals(int otherX, int otherY) { return x == otherX && y == otherY; }
		@Override public String toString() { return "("+x+","+y+")"; }
	}

	
	public static class Area implements Iterable<Point> {
		protected int x;
		protected int y;
		protected int w;
		protected int h;
		public Area(int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
		public static Area create(int fromX, int fromY, int toX, int toY) {
			return new Area(fromX, fromY, toX-fromX+1, toY-fromY+1);
		}
		public boolean contains(Point p) { return contains(p.x, p.y); }
		public boolean contains(int px, int py) { return px>=x && py>=y && px<x+w && py<y+h; }
		@Override public Iterator<Point> iterator() {
			return new MatrixIterator(x, y, x+w-1, y+h-1); 
		}
		public Iterator<Point> reverseIterator() { 
			return new ReverseMatrixIterator(x, y, x+w-1, y+h-1); 
		}
		@Override public String toString() { return "[("+x+","+y+"|"+w+","+h+")]"; }
	}

	
	public abstract static class AbsFilterPoints implements Iterator<Point> {
		protected Iterator<Point> origIterator;
		protected Point nextP;
		protected AbsFilterPoints(Iterator<Point> origIterator) {
			this.origIterator = origIterator;
			this.nextP = null;
			if (origIterator.hasNext()) {
				this.nextP = origIterator.next();
			}
		}
		@Override public boolean hasNext() { return nextP != null; }
		@Override public Point next() {
			if (!hasNext()) {
				return null;
			}
			Point result = nextP;
			selectNextP();
			return result;
		}
		private void selectNextP() {
			nextP = null;
			while (origIterator.hasNext()) {
				Point origNext = origIterator.next();
				if (checkFilter(origNext)) {
					nextP = origNext;
					break;
				}
		    }
		}
		protected abstract boolean checkFilter(Point origNext);
	}

	public static class CheckPoints extends AbsFilterPoints {
		protected Predicate<Point> checkFunction;
		public CheckPoints(Predicate<Point> checkFunction, Iterator<Point> origIterator) {
			super(origIterator);
			this.checkFunction = checkFunction;
		}
		@Override protected boolean checkFilter(Point point) { return checkFunction.test(point); }
	}

	public static class SkipPoint extends CheckPoints {
		public SkipPoint(final Point skipP, Iterator<Point> origIterator) {
			super(p -> !skipP.equals(p), origIterator);
		}
	}

	public static class ClipArea extends CheckPoints {
		public ClipArea(final int minX, final int minY, final int maxX, final int maxY, Iterator<Point> origIterator) {
			super(p -> minX<=p.x&&p.x<=maxX&&minY<=p.y&&p.y<=maxY, origIterator);
		}
	}

	
	public static class MatrixIterator implements Iterator<Point> {
		protected int fromX;
		protected int fromY;
		protected int toX;
		protected int toY;
		protected int nextX;
		protected int nextY;
		public MatrixIterator(int fromX, int fromY, int toX, int toY) {
			this.fromX = fromX;
			this.fromY = fromY;
			this.toX = toX;
			this.toY = toY;
			this.nextX = fromX;
			this.nextY = fromY;
		}
		@Override public boolean hasNext() { return nextY <= toY; }
		@Override public Point next() {
			if (!hasNext()) {
				return null;
			}
			Point result = new Point(nextX, nextY);
			incrementNext();
			return result;
		}
		protected void incrementNext() {
			if (nextX < toX) {
				nextX++;
			}
			else {
				nextX = fromX;
				nextY++;
			}
		}
	}
	
	public static class ReverseMatrixIterator extends MatrixIterator {
		public ReverseMatrixIterator(int fromX, int fromY, int toX, int toY) {
			super(fromX, fromY, toX, toY);
			this.nextX = toX;
			this.nextY = toY;
		}
		@Override public boolean hasNext() { return nextY >= fromY; }
		@Override protected void incrementNext() {
			if (nextX > fromX) {
				nextX--;
			}
			else {
				nextX = toX;
				nextY--;
			}
		}
	}

	
}
