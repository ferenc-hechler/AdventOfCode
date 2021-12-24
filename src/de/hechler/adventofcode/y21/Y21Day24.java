package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * see: https://adventofcode.com/2021/day/22
 *
 */
public class Y21Day24 {


	public static void mainPart1() throws FileNotFoundException {

		StringBuilder program = new StringBuilder();
		program.append(
				  "public static boolean aluSimulator() {\n"
				+ "  int w=0;\n"
				+ "  int x=0;\n"
				+ "  int y=0;\n"
				+ "  int z=0;\n"
				+ "  int i=0;\n"
			);
		try (Scanner scanner = new Scanner(new File("input/y21/day24example.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				program.append("  ").append(convert(line)).append("\n");
			}
		}
		program.append(
				  "  return z==0;\n"
				+ "}\n");
		System.out.println(program.toString());
	}

	private static String convert(String line) {
		String[] asm=line.split(" ");
		String cmd = asm[0];
		String a = asm[1];
		String b = asm.length>=3 ? asm[2] : null;
		switch(cmd) {
		case "inp":
			return a +" = inp(i++);";
		case "add":
			return a + " = add("+a+","+b+");";
		case "mul":
			return a + " = mul("+a+","+b+");";
		case "div":
			return a + " = div("+a+","+b+");";
		case "mod":
			return a + " = mod("+a+","+b+");";
		case "eql":
			return a + " = eql("+a+","+b+");";
		default:
			throw new RuntimeException("Unknown command '"+cmd+"'");
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		mainPart1();
	}

	
}
