package de.hechler.others.pathfinder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import de.hechler.adventofcode.y21.GeoUtils.Point;

public class PathFinder {

	private final static String[] NUM_PIXELS = {
			
			"###"+
			"#.#"+
			"#.#"+
			"#.#"+
			"###",
			
			"..#"+
			"..#"+
			"..#"+
			"..#"+
			"..#",
			
			"###"+
			"..#"+
			"###"+
			"#.."+
			"###",
			
			"###"+
			"..#"+
			"###"+
			"..#"+
			"###",
			
			"#.#"+
			"#.#"+
			"###"+
			"..#"+
			"..#",
			
			"###"+
			"#.."+
			"###"+
			"..#"+
			"###",
			
			"###"+
			"#.."+
			"###"+
			"#.#"+
			"###",
			
			"###"+
			"..#"+
			"..#"+
			"..#"+
			"..#",
			
			"###"+
			"#.#"+
			"###"+
			"#.#"+
			"###",
			
			"###"+
			"#.#"+
			"###"+
			"..#"+
			"###",
			
			"###"+
			"###"+
			"###"+
			"###"+
			"###"
			
	};

	public static class Walker {
		
		private int[][] matrix;
		private int[][] shortestPath;
		private int rows;
		private int cols;
		private int lastMinChanged;
		private int minChanged; 
		
		public Walker(int[][] matrix) {
			this.matrix = matrix;
			this.rows = matrix.length;
			this.cols = matrix[0].length;
			initShortestPath();
		}
		public void initShortestPath() {
			this.shortestPath = new int[rows][cols];
			for (int row=0; row<rows; row++) {
				for (int col=0; col<cols; col++) {
					shortestPath[row][col] = Integer.MAX_VALUE;
				}
			}
			for (int row=0; row<rows; row++) {
				shortestPath[row][0] = matrix[row][0];
			}
			lastMinChanged = 0;
		}
		
		public void iterate() {
			minChanged = Integer.MAX_VALUE; 
			for (int row=0; row<rows; row++) {
				for (int col=0; col<cols; col++) {
					propagate(row, col);
				}
			}
			lastMinChanged = minChanged;
			minChanged = Integer.MAX_VALUE; 
			if (noProgress()) {
				return;
			}
			for (int row=rows-1; row>=0; row--) {
				for (int col=cols-1; col>=0; col--) {
					propagate(row, col);
				}
			}
			lastMinChanged = minChanged;
		}
		
		private void propagate(int row, int col) {
			int s = shortestPath[row][col];
			if (s == Integer.MAX_VALUE) {
				return;
			}
			if (s < lastMinChanged) {
				return;
			}
			int cost = matrix[row][col];
			int evenOdd = cost & 0x1;
			for (int dr=-1; dr<=1; dr++) {
				for (int dc=-1; dc<=1; dc++) {
					if ((dr==0) && (dc==0)) {
						continue;
					}
					int rowP = row+dr;
					int colP = col+dc;
					if ((rowP<0) || (rowP>=rows) || (colP<0) || (colP>=cols)) {
						continue;
					}
					int costP = matrix[rowP][colP];
					if (costP == -1) {
						continue;
					}
					int evenOddP = costP & 0x1;
					if (evenOddP == evenOdd) {
						continue;
					}
					int sP = s+costP;
					if (sP >= shortestPath[rowP][colP]) {
						continue;
					}
					shortestPath[rowP][colP] = sP;
					minChanged = Math.min(minChanged, sP);
				}
			}
		}
		
		public boolean noProgress() {
			return lastMinChanged == Integer.MAX_VALUE;
		}
		public int[][] getShortestPathArray() {
			return shortestPath;
		}
		public List<Point> calcShortestWay() {
			int bestRow = 0;
			for (int row=1; row<rows; row++) {
				if (shortestPath[row][cols-1]<shortestPath[bestRow][cols-1]) {
					bestRow = row;
				}
			}
			List<Point> result = new ArrayList<>();
			Point p = new Point(cols-1, bestRow);
			result.add(p);
			while (p.x()>0) {
				int row = p.y();
				int col = p.x();
				int s = shortestPath[row][col];
				int cost = matrix[row][col];
				int evenOdd = cost & 0x1;
				for (int dr=-1; dr<=1; dr++) {
					for (int dc=-1; dc<=1; dc++) {
						if ((dr==0) && (dc==0)) {
							continue;
						}
						int rowP = row+dr;
						int colP = col+dc;
						if ((rowP<0) || (rowP>=rows) || (colP<0) || (colP>=cols)) {
							continue;
						}
						int costP = matrix[rowP][colP];
						if (costP == -1) {
							continue;
						}
						int evenOddP = costP & 0x1;
						if (evenOddP == evenOdd) {
							continue;
						}
						int sP = s-cost;
						if (sP != shortestPath[rowP][colP]) {
							continue;
						}
						p = new Point(colP, rowP);
					}
				}
				result.add(p);
			}
			
			return result;
		}
	}
	
	private static int[][] readMatrix(String inputFilename) throws IOException {
		Map<String, Integer> bits2int = new HashMap<>();
		for (int i=0; i<11; i++) {
			bits2int.put(NUM_PIXELS[i], i==10?-1:i);
		}
		
		BufferedImage img = ImageIO.read(new File(inputFilename));
		int xOffset = 96;
		int yOffset = 97;
		int numSize = 10;
		int rows = 101;
		int cols = 101;
		
		int[][] result = new int[rows][cols];
		
		for (int row=0; row<rows; row++) {
			for (int col=0; col<cols; col++) {
				String pixels10 = ""; 
				String pixels1 = ""; 
				for (int y=0; y<5; y++) {
					for (int x=0; x<3; x++) {
						int rgb = img.getRGB(xOffset+col*numSize+x,yOffset+row*numSize+y);
						int green = (rgb >> 8) & 0xff;
						pixels10+=(green > 200)?'.':'#';
						rgb = img.getRGB(xOffset+col*numSize+x+4,yOffset+row*numSize+y);
						green = (rgb >> 8) & 0xff;
						pixels1+=(green > 200)?'.':'#';
					}
				}
				int n10 = bits2int.get(pixels10);
				int n1 = bits2int.get(pixels1);
				int n = 10*n10+n1;
				if ((n1==-1) || (n10 == -1)) {
					if ((n1!=-1) || (n10 != -1)) {
						throw new RuntimeException("error in row "+row+" and col "+col);
					}
					n = -1;
				}
				result[row][col] = n;
				System.out.print(n+" ");
			}
			System.out.println();
		}
		return result;
	}


	private static void show(int[][] matrix, int len) {
		for (int row=0; row<matrix.length; row++) {
			for (int col=0; col<matrix[row].length; col++) {
				if (matrix[row][col] == Integer.MAX_VALUE) {
					System.out.print(" ********************".substring(0, len));
				}
				else {
					System.out.print(padl(matrix[row][col], len));
				}
			}
			System.out.println();
		}
	}
	
	private static String padl(int n, int len) {
		String result = Integer.toString(n);
		int rLen = result.length();
		if (rLen >= len) {
			return result;
		}
		return "               ".substring(0, len-rLen)+result;
	}

	private static void transpose(int[][] matrix) {
		for (int row=0; row<matrix.length; row++) {
			for (int col=0; col<row; col++) {
				int h = matrix[row][col];
				matrix[row][col] = matrix[col][row];
				matrix[col][row] = h;
			}
		}
	}

	private static void writeSolution(String inputFilename, String outputFilename, List<Point> wayPoints, boolean transposed) throws IOException {
		BufferedImage img = ImageIO.read(new File(inputFilename));
		int xOffset = 95;
		int yOffset = 95;
		int numSize = 10;
		int rows = 101;
		int cols = 101;
		int row;
		int col;
		
		for (Point wayPoint:wayPoints) {
			if (transposed) {
				row = wayPoint.x();
				col = wayPoint.y();
			}
			else {
				row = wayPoint.y();
				col = wayPoint.x();
			}
			for (int y=0; y<numSize-1; y++) {
				for (int x=0; x<numSize-1; x++) {
					int rgb = img.getRGB(xOffset+col*numSize+x,yOffset+row*numSize+y);
					rgb = rgb | 0xff0000;
					img.setRGB(xOffset+col*numSize+x,yOffset+row*numSize+y, rgb);
				}
			}
		}
		ImageIO.write(img, "png", new File(outputFilename));
		showImg(img);
	}

	private static void showImg(BufferedImage img) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new JLabel(new ImageIcon(img)));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // if you want the X button to close the app
	}

	public static void solve(String inputFile, String outputFile, boolean transpose) throws IOException {

		int[][] matrix = readMatrix(inputFile);
		if (transpose) {
			transpose(matrix);
		}
		show(matrix, 3);
		Walker walker = new Walker(matrix);
		int cnt = 0;
		while (!walker.noProgress()) {
			walker.iterate();
			cnt++;
			System.out.println("iteration "+cnt);
		}
		show(walker.getShortestPathArray(), 5);
		System.out.println();
		List<Point> way = walker.calcShortestWay();
		for (Point p:way) {
			System.out.println(p+" +"+matrix[p.y()][p.x()]+" = "+walker.getShortestPathArray()[p.y()][p.x()]);
		}
		System.out.println("COST: "+walker.getShortestPathArray()[way.get(0).y()][way.get(0).x()]);
		System.out.println(way);
		writeSolution(inputFile, outputFile, walker.calcShortestWay(), transpose);
		System.out.println();
	}

	public static void main(String[] args) throws IOException {
		solve("input/pathfinder/pathfinder.png", "input/pathfinder/pathfinder_solution_OST_WEST.png", false); 
		solve("input/pathfinder/pathfinder.png", "input/pathfinder/pathfinder_solution_NORD_SUED.png", true); 
	}
	
	
}
