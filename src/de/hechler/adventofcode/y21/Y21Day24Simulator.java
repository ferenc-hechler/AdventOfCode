package de.hechler.adventofcode.y21;

/**
 * see: https://adventofcode.com/2021/day/22
 *
 */
public class Y21Day24Simulator {

	public static String input = null;

	public static int inp(int idx) {
		return input.charAt(0) - '0';
	}

	public static int add(int a, int b) {
		return a + b;
	}

	public static int mod(int a, int b) {
		return a % b;
	}

	public static int div(int a, int b) {
		return a / b;
	}

	public static int eql(int a, int b) {
		return a == b ? 1 : 0;
	}

	public static boolean aluSimulator() {
		int w = 0;
		int x = 0;
		int y = 0;
		int z = 0;
		int i = 0;
		w = inp(i++);
		z = add(z, w);
		z = mod(z, 2);
		w = div(w, 2);
		y = add(y, w);
		y = mod(y, 2);
		w = div(w, 2);
		x = add(x, w);
		x = mod(x, 2);
		w = div(w, 2);
		w = mod(w, 2);
		result(w, x, y, z);
		return z == 0;
	}

	public static void result(int w, int x, int y, int z) {
		System.out.println("w=" + w + ", x=" + x + ", y=" + y + ", z=" + z);
	}

	public static void run(String inValues) {
		input = inValues;
		aluSimulator();
	}

	public static void main(String[] args) {
		run("9");
	}

}
