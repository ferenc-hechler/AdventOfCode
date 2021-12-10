package de.hechler.adventofcode.y21;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
	
	public static List<Long> toList(long[] array) {
		if (array==null) {
			return null;
		}
		if (array.length == 0) {
			return Collections.emptyList();
		}
		List<Long> result = new ArrayList<Long>();
		for (long n:array) {
			result.add(n);
		}
		return result;
	}
	
	public static int[] toIntArray(char[] cArr) {
		if (cArr==null) {
			return null;
		}
		int[] result = new int[cArr.length];
		for (int i=0; i<cArr.length; i++) {
			result[i] = (int)cArr[i];
		}
		return result;
	}

	public static int[] toIntArray(List<Integer> iArr) {
		if (iArr==null) {
			return null;
		}
		int[] result = new int[iArr.size()];
		for (int i=0; i<iArr.size(); i++) {
			result[i] = iArr.get(i);
		}
		return result;
	}

	public static int[][] toIntMatrix(List<List<Integer>> matrix) {
		if (matrix==null) {
			return null;
		}
		int rows = matrix.size();
		if (rows == 0) {
			return new int[0][0];
		}
		int cols = matrix.get(0).size();
		int[][] result = new int[rows][cols];
		for (int r=0; r<rows; r++) {
			result[r] = toIntArray(matrix.get(r));
		}
		return result;
	}
	
	public static List<List<Integer>> toList(int[][] matrix) {
		if (matrix==null) {
			return null;
		}
		List<List<Integer>> result = new ArrayList<>();
		for (int row=0; row<matrix.length; row++) {
			result.add(toList(matrix[row]));
		}
		return result;
	}
	
	public interface ConvertFunction<S,D>  {
		public D convert(S source);
	}
	
	@SuppressWarnings("unchecked")
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

	public static long sum(long[] values) {
		long result = 0;
		for (long value:values) {
			result += value;
		}
		return result;
	}

	public static Long median(List<Long> values) {
		if ((values == null) || values.isEmpty()) {
			return null;
		}
		List<Long> sortedList = new ArrayList<>(values);
		Collections.sort(sortedList);
		return sortedList.get(sortedList.size()/2);
	}

	public static int min(List<Integer> values) {
		Integer result = values.stream().min(Integer::compare).get();
		return result;
	}
	
	public static int max(List<Integer> crabPositions) {
		Integer result = crabPositions.stream().max(Integer::compare).get();
		return result;
	}

	public static int min(int[] values) {
		int result = Integer.MAX_VALUE;
		for (int value:values) {
			result = Math.min(value, result);
		}
		return result;
	}
	
	public static int max(int[] values) {
		int result = Integer.MIN_VALUE;
		for (int value:values) {
			result = Math.max(value, result);
		}
		return result;
	}

	/**
	 * create all subsets of the given set.
	 * from: https://java2blog.com/find-subsets-set-power-set/
	 * @param <T>
	 * @param completeSet
	 * @return
	 */
	public static <T> List<List<T>> createSubsets(List<T> completeSet) {
		List<List<T>> result = new ArrayList<>();
        subsetsHelper(result, new ArrayList<>(), completeSet, 0);
		return result;
	}
	
	
    private static <T> void subsetsHelper(List<List<T>> list , List<T> resultList, List<T> ts, int start){
        list.add(new ArrayList<>(resultList));
        for(int i = start; i < ts.size(); i++){
           // add element
            resultList.add(ts.get(i));
           // Explore
            subsetsHelper(list, resultList, ts, i + 1);
           // remove
            resultList.remove(resultList.size() - 1);
        }
    }
	
}
