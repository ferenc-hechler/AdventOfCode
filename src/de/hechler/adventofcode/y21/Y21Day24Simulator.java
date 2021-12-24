package de.hechler.adventofcode.y21;

/**
 * see: https://adventofcode.com/2021/day/22
 *
 */
public class Y21Day24Simulator {

	public static int[] input = null;

	public static int inp(int idx) {
		return input[idx];
	}

	public static int add(int a, int b) {
		return a + b;
	}

	public static int mul(int a, int b) {
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
		x = mul(x, 0);
		x = add(x, z);
		x = mod(x, 26);
		z = div(z, 1);
		x = add(x, 14);
		x = eql(x, w);
		x = eql(x, 0);
		y = mul(y, 0);
		y = add(y, 25);
		y = mul(y, x);
		y = add(y, 1);
		z = mul(z, y);
		y = mul(y, 0);
		y = add(y, w);
		y = add(y, 12);
		y = mul(y, x);
		z = add(z, y);
		w = inp(i++);
		x = mul(x, 0);
		x = add(x, z);
		x = mod(x, 26);
		z = div(z, 1);
		x = add(x, 11);
		x = eql(x, w);
		x = eql(x, 0);
		y = mul(y, 0);
		y = add(y, 25);
		y = mul(y, x);
		y = add(y, 1);
		z = mul(z, y);
		y = mul(y, 0);
		y = add(y, w);
		y = add(y, 8);
		y = mul(y, x);
		z = add(z, y);
		w = inp(i++);
		x = mul(x, 0);
		x = add(x, z);
		x = mod(x, 26);
		z = div(z, 1);
		x = add(x, 11);
		x = eql(x, w);
		x = eql(x, 0);
		y = mul(y, 0);
		y = add(y, 25);
		y = mul(y, x);
		y = add(y, 1);
		z = mul(z, y);
		y = mul(y, 0);
		y = add(y, w);
		y = add(y, 7);
		y = mul(y, x);
		z = add(z, y);
		w = inp(i++);
		x = mul(x, 0);
		x = add(x, z);
		x = mod(x, 26);
		z = div(z, 1);
		x = add(x, 14);
		x = eql(x, w);
		x = eql(x, 0);
		y = mul(y, 0);
		y = add(y, 25);
		y = mul(y, x);
		y = add(y, 1);
		z = mul(z, y);
		y = mul(y, 0);
		y = add(y, w);
		y = add(y, 4);
		y = mul(y, x);
		z = add(z, y);
		w = inp(i++);
		x = mul(x, 0);
		x = add(x, z);
		x = mod(x, 26);
		z = div(z, 26);
		x = add(x, -11);
		x = eql(x, w);
		x = eql(x, 0);
		y = mul(y, 0);
		y = add(y, 25);
		y = mul(y, x);
		y = add(y, 1);
		z = mul(z, y);
		y = mul(y, 0);
		y = add(y, w);
		y = add(y, 4);
		y = mul(y, x);
		z = add(z, y);
		w = inp(i++);
		x = mul(x, 0);
		x = add(x, z);
		x = mod(x, 26);
		z = div(z, 1);
		x = add(x, 12);
		x = eql(x, w);
		x = eql(x, 0);
		y = mul(y, 0);
		y = add(y, 25);
		y = mul(y, x);
		y = add(y, 1);
		z = mul(z, y);
		y = mul(y, 0);
		y = add(y, w);
		y = add(y, 1);
		y = mul(y, x);
		z = add(z, y);
		w = inp(i++);
		x = mul(x, 0);
		x = add(x, z);
		x = mod(x, 26);
		z = div(z, 26);
		x = add(x, -1);
		x = eql(x, w);
		x = eql(x, 0);
		y = mul(y, 0);
		y = add(y, 25);
		y = mul(y, x);
		y = add(y, 1);
		z = mul(z, y);
		y = mul(y, 0);
		y = add(y, w);
		y = add(y, 10);
		y = mul(y, x);
		z = add(z, y);
		w = inp(i++);
		x = mul(x, 0);
		x = add(x, z);
		x = mod(x, 26);
		z = div(z, 1);
		x = add(x, 10);
		x = eql(x, w);
		x = eql(x, 0);
		y = mul(y, 0);
		y = add(y, 25);
		y = mul(y, x);
		y = add(y, 1);
		z = mul(z, y);
		y = mul(y, 0);
		y = add(y, w);
		y = add(y, 8);
		y = mul(y, x);
		z = add(z, y);
		w = inp(i++);
		x = mul(x, 0);
		x = add(x, z);
		x = mod(x, 26);
		z = div(z, 26);
		x = add(x, -3);
		x = eql(x, w);
		x = eql(x, 0);
		y = mul(y, 0);
		y = add(y, 25);
		y = mul(y, x);
		y = add(y, 1);
		z = mul(z, y);
		y = mul(y, 0);
		y = add(y, w);
		y = add(y, 12);
		y = mul(y, x);
		z = add(z, y);
		w = inp(i++);
		x = mul(x, 0);
		x = add(x, z);
		x = mod(x, 26);
		z = div(z, 26);
		x = add(x, -4);
		x = eql(x, w);
		x = eql(x, 0);
		y = mul(y, 0);
		y = add(y, 25);
		y = mul(y, x);
		y = add(y, 1);
		z = mul(z, y);
		y = mul(y, 0);
		y = add(y, w);
		y = add(y, 10);
		y = mul(y, x);
		z = add(z, y);
		w = inp(i++);
		x = mul(x, 0);
		x = add(x, z);
		x = mod(x, 26);
		z = div(z, 26);
		x = add(x, -13);
		x = eql(x, w);
		x = eql(x, 0);
		y = mul(y, 0);
		y = add(y, 25);
		y = mul(y, x);
		y = add(y, 1);
		z = mul(z, y);
		y = mul(y, 0);
		y = add(y, w);
		y = add(y, 15);
		y = mul(y, x);
		z = add(z, y);
		w = inp(i++);
		x = mul(x, 0);
		x = add(x, z);
		x = mod(x, 26);
		z = div(z, 26);
		x = add(x, -8);
		x = eql(x, w);
		x = eql(x, 0);
		y = mul(y, 0);
		y = add(y, 25);
		y = mul(y, x);
		y = add(y, 1);
		z = mul(z, y);
		y = mul(y, 0);
		y = add(y, w);
		y = add(y, 4);
		y = mul(y, x);
		z = add(z, y);
		w = inp(i++);
		x = mul(x, 0);
		x = add(x, z);
		x = mod(x, 26);
		z = div(z, 1);
		x = add(x, 13);
		x = eql(x, w);
		x = eql(x, 0);
		y = mul(y, 0);
		y = add(y, 25);
		y = mul(y, x);
		y = add(y, 1);
		z = mul(z, y);
		y = mul(y, 0);
		y = add(y, w);
		y = add(y, 10);
		y = mul(y, x);
		z = add(z, y);
		w = inp(i++);
		x = mul(x, 0);
		x = add(x, z);
		x = mod(x, 26);
		z = div(z, 26);
		x = add(x, -11);
		x = eql(x, w);
		x = eql(x, 0);
		y = mul(y, 0);
		y = add(y, 25);
		y = mul(y, x);
		y = add(y, 1);
		z = mul(z, y);
		y = mul(y, 0);
		y = add(y, w);
		y = add(y, 9);
		y = mul(y, x);
		z = add(z, y);
		return z == 0;
	}

	public static void result(int w, int x, int y, int z) {
		System.out.println("w=" + w + ", x=" + x + ", y=" + y + ", z=" + z);
	}

	public static void run() {
		input = new int[14];
		for (int i=0; i<14; i++) {
			input[i] = 9;
		}
		int cnt = 0;
		boolean valid = aluSimulator();
		while (!valid) {
			decrementInput(); 
			valid = aluSimulator();
			if (cnt++ >= 10000000) {
				cnt = 0;
				System.out.println(Utils.toList(input));
			}
		}
		System.out.println(Utils.toList(input));
	}

	private static void decrementInput() {
		for (int pos=13; pos>=0; pos--) {
			if (--input[pos] > 0) {
				return;
			}
			input[pos] = 9;
		};
	}

	public static void main(String[] args) {
		run();
	}

}
