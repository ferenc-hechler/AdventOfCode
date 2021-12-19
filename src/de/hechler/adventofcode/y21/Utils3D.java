package de.hechler.adventofcode.y21;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Utils3D {
	
	/** do not allow instances to be created */
	private Utils3D() {} 
	
	
	public static class Point3D {
		private int x;
		private int y;
		private int z;
		public Point3D() { this(0, 0, 0); }
		public Point3D(int x, int y, int z) { this.x = x; this.y = y; this.z = z; }
		public Point3D(Point3D p) { this(p.x, p.y, p.z); }
		public int x() { return x; }
		public int y() { return y; }
		public int z() { return z; }
		public Point3D offset(int dx, int dy, int dz) { return new Point3D(x+dx, y+dy, z+dz); }
		public Point3D add(Point3D other) {
			x += other.x;
			y += other.y;
			z += other.z;
			return this; 
		}
		public Point3D sub(Point3D other) {
			x -= other.x;
			y -= other.y;
			z -= other.z;
			return this; 
		}
		public void set(Point3D other) {
			x = other.x;
			y = other.y;
			z = other.z;
		}
		public double distance(Point3D other) {
			return Math.sqrt(sqareDistance(other)); 
		}
		public long sqareDistance(Point3D other) {
			return (x-other.x)*(x-other.x) + (y-other.y)*(y-other.y) + (z-other.z)*(z-other.z); 
		}
		public int nondiagonalDistance(Point3D other) {
			return Math.abs(x-other.x) + Math.abs(y-other.y) + Math.abs(z-other.z); 
		}
		public List<Point3D> offsets(int... deltas) {
			List<Point3D> result = new ArrayList<>();
			for (int i=0; i<deltas.length; i+=3) {
				result.add(offset(deltas[i], deltas[i+1], deltas[i+2]));
			}
			return result; 
		}
		public List<Point3D> sixNeighbours() { 
			return offsets( 0,-1,0, -1,0,0, 1,0,0, 0,1,0, 0,0,-1, 0,0,1 ); 
		}
		public List<Point3D> twentysixNeighbours() {
			List<Point3D> result = new ArrayList<>();
			for (int nx=x-1; nx<=x+1; nx++) {
				for (int ny=y-1; ny<=y+1; ny++) {
					for (int nz=z-1; nz<=z+1; nz++) {
						if (equals(nx, ny, nz)) {
							continue;
						}
						result.add(new Point3D(nx,ny,nz));
					}
				}
			}
			return result; 
		}
		@Override
		public int hashCode() { return Objects.hash(x, y, z); }
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Point3D other = (Point3D) obj;
			return x == other.x && y == other.y && z == other.z;
		}
		public boolean equals(int otherX, int otherY, int otherZ) { return x == otherX && y == otherY && z == otherZ; }
		@Override public String toString() { return "("+x+","+y+","+z+")"; }
		public Point3D createCopy() {
			return new Point3D(this);
		}
	}

	
	public static class RotMatrix {
		int[][] mat;
		public RotMatrix() {
			this.mat = new int[3][3];
		}
		public RotMatrix(int[][] mat) {
			this.mat = mat;
		}
		public Point3D rot(Point3D p) {
			int x = p.x*mat[0][0] + p.y*mat[1][0] + p.z*mat[2][0]; 
			int y = p.x*mat[0][1] + p.y*mat[1][1] + p.z*mat[2][1]; 
			int z = p.x*mat[0][2] + p.y*mat[1][2] + p.z*mat[2][2];
			return new Point3D(x,y,z);
		}
		@Override public String toString() {
			return Utils.toString(mat);
		}
	}

	
	public static class Area3D implements Iterable<Point3D> {
		protected int minX;
		protected int minY;
		protected int minZ;
		protected int maxX;
		protected int maxY;
		protected int maxZ;
		public Area3D(int x, int y, int z, int w, int h, int d) {
			this.minX = x;
			this.minY = y;
			this.minZ = z;
			this.maxX = x+w;
			this.maxY = y+h;
			this.maxZ = z+d;
		}
		public static Area3D createFromTo(int fromX, int fromY, int fromZ, int toX, int toY, int toZ) {
			return new Area3D(fromX, fromY, fromZ, toX-fromX+1, toY-fromY+1, toZ-fromZ+1);
		}
		public int x() { return minX; }
		public int y() { return minY; }
		public int z() { return minZ; }
		public int w() { return maxX-minX; }
		public int h() { return maxY-minY; }
		public int d() { return maxZ-minZ; }
		public int fromX() { return minX; }
		public int fromY() { return minY; }
		public int fromZ() { return minZ; }
		public int toX() { return maxX-1; }
		public int toY() { return maxY-1; }
		public int toZ() { return maxZ-1; }
		public boolean contains(Point3D p) { return contains(p.x, p.y, p.z); }
		public boolean contains(int px, int py, int pz) { return px>=minX && py>=minY && pz>=minZ && px<maxX && py<maxY && pz<maxZ; }
		public boolean overlaps(Area3D otherArea) { 
			return  (maxX>otherArea.minX)&&(maxY>otherArea.minY)&&(maxZ>otherArea.minZ) && 
					(minX<otherArea.maxX)&&(minY<otherArea.maxY)&&(minZ<otherArea.maxZ); 
		}

		@Override public Iterator<Point3D> iterator() {
			return new Iterator3D(fromX(), fromY(), fromZ(), toX(), toY(), toZ()); 
		}
		public Iterator<Point3D> reverseIterator() { 
			return new ReverseIterator3D(fromX(), fromY(), fromZ(), toX(), toY(), toZ()); 
		}
		@Override public String toString() { return "[("+minX+","+minY+","+minZ+"|"+w()+","+h()+","+d()+")]"; }
	}

	
//	public static class CheckPoints implements Iterator<Point> {
//		protected Iterator<Point> origIterator;
//		protected Predicate<Point> checkFunction;
//		protected Point nextP;
//		protected CheckPoints(Predicate<Point> checkFunction, Iterator<Point> origIterator) {
//			this.origIterator = origIterator;
//			this.checkFunction = checkFunction;
//			selectNextP(); 
//		}
//		private void selectNextP() {
//			nextP = null;
//			while (origIterator.hasNext()) {
//				Point origNext = origIterator.next();
//				if (checkFilter(origNext)) {
//					nextP = origNext;
//					break;
//				}
//		    }
//		}
//		@Override public boolean hasNext() { return nextP != null; }
//		@Override public Point next() {
//			if (!hasNext()) {
//				return null;
//			}
//			Point result = nextP;
//			selectNextP();
//			return result;
//		}
//		protected boolean checkFilter(Point point) { return checkFunction.test(point); }
//	}
//
//	public static class SkipPoint extends CheckPoints {
//		public SkipPoint(final Point skipP, Iterator<Point> origIterator) {
//			super(p -> !skipP.equals(p), origIterator);
//		}
//	}
//
//	public static class ClipArea extends CheckPoints {
//		public ClipArea(final int fromX, final int fromY, final int toX, final int toY, Iterator<Point> origIterator) {
//			super(p -> fromX<=p.x&&p.x<=toX&&fromY<=p.y&&p.y<=toY, origIterator);
//		}
//		public ClipArea(final Area area, Iterator<Point> origIterator) {
//			super(area::contains, origIterator);
//		}
//	}
//
	
	public static class Iterator3D implements Iterator<Point3D> {
		protected int fromX;
		protected int fromY;
		protected int fromZ;
		protected int toX;
		protected int toY;
		protected int toZ;
		protected int nextX;
		protected int nextY;
		protected int nextZ;
		public Iterator3D(int fromX, int fromY, int fromZ, int toX, int toY, int toZ) {
			this.fromX = fromX;
			this.fromY = fromY;
			this.fromZ = fromZ;
			this.toX = toX;
			this.toY = toY;
			this.toZ = toZ;
			this.nextX = fromX;
			this.nextY = fromY;
			this.nextZ = fromZ;
		}
		@Override public boolean hasNext() { return nextZ <= toZ; }
		@Override public Point3D next() {
			if (!hasNext()) {
				return null;
			}
			Point3D result = new Point3D(nextX, nextY, nextZ);
			incrementNext();
			return result;
		}
		protected void incrementNext() {
			if (nextX < toX) {
				nextX++;
				return;
			}
			nextX = fromX;
			if (nextY < toY) {
				nextY++;
				return;
			}
			nextY = fromY;
			nextZ++;
		}
	}
	
	public static class ReverseIterator3D extends Iterator3D {
		public ReverseIterator3D(int fromX, int fromY, int fromZ, int toX, int toY, int toZ) {
			super(fromX, fromY, fromZ, toX, toY, toZ);
			this.nextX = toX;
			this.nextY = toY;
			this.nextZ = toZ;
		}
		@Override public boolean hasNext() { return nextZ >= fromZ; }
		@Override protected void incrementNext() {
			if (nextX > fromX) {
				nextX--;
				return;
			}
			nextX = toX;
			if (nextY > fromY) {
				nextY--;
				return;
			}
			nextY = toY;
			nextZ--;
		}
	}

	
}
