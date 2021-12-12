package de.hechler.adventofcode.y21;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import de.hechler.adventofcode.y21.Y21Day12.Cave.Path;

/**
 * see: https://adventofcode.com/2021/day/12
 *
 */
public class Y21Day12 {

	private final static String INPUT_RX = "^([A-Za-z]+)-([A-Za-z]+)$";

	
	public static class Cave {
		
		public class Path {
			private boolean doubleVisitPossible;
			private Set<String> blockedNodes;
			private List<String> visitedNodes;
			public Path() {
				this.blockedNodes = new HashSet<>();
				this.visitedNodes = new ArrayList<String>();
				this.visitedNodes.add("start");
				this.blockedNodes.add("start");
				this.doubleVisitPossible = true;
			}
			public Path(Path parentPath, String nextNode) {
				this(parentPath, nextNode, parentPath.doubleVisitPossible);
			}
			public Path(Path parentPath, String nextNode, boolean doubleVisitPossible) {
				this.blockedNodes = new HashSet<>(parentPath.blockedNodes);
				this.visitedNodes = new ArrayList<>(parentPath.visitedNodes);
				this.visitedNodes.add(nextNode);
				if (nextNode.matches("^[a-z].*$")) {
					this.blockedNodes.add(nextNode);
				}
				this.doubleVisitPossible = doubleVisitPossible;
			}
			public List<Path> getNextPaths() {
				if ("end".equals(getLastVisitedNode())) {
					return Collections.emptyList();
				}
				List<Path> result = new ArrayList<>();
				Set<String> nextPossibleNodes = getTargets(getLastVisitedNode());
				if (doubleVisitPossible) {
					Set<String> doubleVisitNodes = new HashSet<>(nextPossibleNodes);
					doubleVisitNodes.retainAll(blockedNodes);
					doubleVisitNodes.remove("start");
					doubleVisitNodes.remove("end");
					for (String doubleVisitNode:doubleVisitNodes) {
						result.add(new Path(this, doubleVisitNode, false));
					}
				}
				nextPossibleNodes.removeAll(blockedNodes);
				for (String nextPossibleNode:nextPossibleNodes) {
					result.add(new Path(this, nextPossibleNode));
				}
				return result;
			}
			public boolean isFinished() { return "end".equals(getLastVisitedNode()); }
			private String getLastVisitedNode() { return visitedNodes.get(visitedNodes.size()-1); }
			@Override
			public String toString() {
				return visitedNodes.toString();
			}
		}

		private Map<String, Set<String>> ways;
		
		public Cave() {
			ways = new HashMap<>();
		}
		public void addWay(String from, String to) {
			Set<String> tos = ways.get(from);
			if (tos == null) {
				tos = new HashSet<>();
				ways.put(from, tos);
			}
			tos.add(to);
		}
		public Set<String> getTargets(String from) {
			Set<String> result = ways.get(from);
			if (result == null) {
				result = Collections.emptySet();
			}
			return new HashSet<>(result);
		}
		public Path getStartPath() {
			return new Path();
		}
		public List<Path> findNextPaths(List<Path> previousPaths) {
			List<Path> result = new ArrayList<>();
			for (Path previousPath:previousPaths) {
				result.addAll(previousPath.getNextPaths());
			}
			return result;
		}
		@Override
		public String toString() {
			return ways.toString();
		}
	}

	

	public static void mainPart1() throws FileNotFoundException {
		
		Cave cave = new Cave();
		try (Scanner scanner = new Scanner(new File("input/y21/day12.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				String fromNode = line.replaceFirst(INPUT_RX, "$1");
				String toNode = line.replaceFirst(INPUT_RX, "$2");
				cave.addWay(fromNode, toNode);
				cave.addWay(toNode, fromNode);  // bidirectional ways.
			}
		}
		System.out.println(cave);
		List<Path> nextPaths = new ArrayList<>();
		nextPaths.add(cave.getStartPath());
		Utils.Counter pathCounter = new Utils.Counter();
		while (!nextPaths.isEmpty()) {
			nextPaths = cave.findNextPaths(nextPaths);
			nextPaths.forEach(p -> {
				if (p.isFinished() ) {
					pathCounter.inc();
					System.out.println(p);
				}
			});
		}
		System.out.println(pathCounter);
	}

	public static void mainPart2() throws FileNotFoundException {
		
		Cave cave = new Cave();
		try (Scanner scanner = new Scanner(new File("input/y21/day12.txt"))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.matches(INPUT_RX)) {
					throw new RuntimeException("invalid input line '"+line+"', not matching RX '"+INPUT_RX+"'");
				}
				String fromNode = line.replaceFirst(INPUT_RX, "$1");
				String toNode = line.replaceFirst(INPUT_RX, "$2");
				cave.addWay(fromNode, toNode);
				cave.addWay(toNode, fromNode);  // bidirectional ways.
			}
		}
		System.out.println(cave);
		List<Path> nextPaths = new ArrayList<>();
		nextPaths.add(cave.getStartPath());
		Utils.Counter pathCounter = new Utils.Counter();
		while (!nextPaths.isEmpty()) {
			nextPaths = cave.findNextPaths(nextPaths);
			nextPaths.forEach(p -> {
				if (p.isFinished() ) {
					pathCounter.inc();
					System.out.println(p);
				}
			});
		}
		System.out.println(pathCounter);
	}

	public static void main(String[] args) throws FileNotFoundException {
		mainPart2();
	}

	
}
