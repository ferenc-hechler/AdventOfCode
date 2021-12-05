package de.hechler.adventofcode.y21;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Utils {

	public static <T> List<T> toList(T[] array) {
		if (array==null) {
			return null;
		}
		if (array.length == 0) {
			return Collections.emptyList();
		}
		List<T> result = new ArrayList<T>();
		for (T t:array) {
			result.add(t);
		}
		return result;
	}
	
	public static List<Integer> toList(int[] array) {
		if (array==null) {
			return null;
		}
		if (array.length == 0) {
			return Collections.emptyList();
		}
		List<Integer> result = new ArrayList<Integer>();
		for (int n:array) {
			result.add(n);
		}
		return result;
	}
	
	public interface ConvertFunction<S,D>  {
		public D convert(S source);
	}
	
	public static <S,D> D[] mapArray(S[] source, ConvertFunction<S,D> conv) {
		
		if (source==null) {
			return null;
		}
		int len = source.length;
		D[] result = null;
		for (int i=0; i<len; i++) {
			D d = conv.convert(source[i]);
			if (d != null) {
				if (result == null) {
					result = (D[])Array.newInstance(d.getClass(), len);
				}
				result[i] = d;
			}
		}
		if (result == null) {
			throw new RuntimeException("sorry, at least 1 none null converted element has to exist (type erasure)");
		}
		return result;
	}
	
	public interface Convert2intFunction<S>  {
		public int convert2int(S source);
	}
	
	public static <S> int[] map2intArray(S[] source, Convert2intFunction<S> conv) {
		if (source==null) {
			return null;
		}
		int len = source.length;
		int[] result = new int[len];
		for (int i=0; i<len; i++) {
			result[i] = conv.convert2int(source[i]);
		}
		return result;
	}

	public static String toFixString(int value, int len) {
		String result = Integer.toString(value);
		if (result.length()<len) {
			result = "                             ".substring(0, len-result.length())+result;
		}
		return result;
	}

	public static String toString(int[][] intMatrix) {
		int maxLen = 1;
		for (int[] row: intMatrix) {
			for (int n:row) {
				maxLen = Math.max(maxLen, Integer.toString(n).length());
			}
		}
		StringBuilder result = new StringBuilder();
		for (int[] row: intMatrix) {
			for (int n: row) {
				result.append(toFixString(n, maxLen)).append(" ");
			}
			result.append("\n");
		}
		return result.toString();
	}
	
	
}
