package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Test {

	public static void main(String[] args) {
		File file = new File("F:\\Java\\doc2vec\\file\\code.vec");
		Map<Integer, float[]> vecSet = getContent(file);
		// for (Integer key : vecSet.keySet()) {
		// System.out.println("" + key + ": " + vecSet.get(key)[0]);
		// }
		Map<Integer, Float> similar = getTopK(vecSet, vecSet.get(0));
		Map<Integer, Float> resultMap = sortMapByValue(similar);
		int i = 0;
		for (Integer key : resultMap.keySet()) {
			System.out.println("" + key + ": " + resultMap.get(key));
			if(i>10)
				break;
			i++;
		}
	}

	private static Map<Integer, float[]> getContent(File file) {
		BufferedReader bufread = null;
		String temp = null;
		Map<Integer, float[]> vecSet = new HashMap<Integer, float[]>();
		try {
			bufread = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			int j = 0;
			while ((temp = bufread.readLine()) != null) {
				String[] stringVector = temp.split(" ");
				float[] vec = new float[200];
				int i = 0;
				for (String s : stringVector) {
					// System.out.println(s);
					// System.out.println(Float.parseFloat(s));
					vec[i] = Float.parseFloat(s);
					i++;
				}
				vecSet.put(j, vec);
				j++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bufread.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vecSet;
	}

	private static Map<Integer, Float> getTopK(Map<Integer, float[]> vecSet, float[] vec) {
		Map<Integer, Float> similar = new HashMap<Integer, Float>();
		int num = 0;
		float max = 0.0f;
		for (int i = 0; i < 1000; i++) {
			float temp = getSimilar(vecSet.get(i), vec);
			if (temp > max && i != 0) {
				num = i;
				max = temp;
			}
			similar.put(i, temp);
		}
		System.out.println("max: " + max);
		System.out.println("maxnum: " + num);
		System.out.println("!!!!!!: " + getSimilar(vecSet.get(0), vecSet.get(1)));
		return similar;
	}

	private static float getSimilar(float[] vec1, float[] vec2) {
		float similar = 0.0f;
		float vecmulti = 0.0f;
		// System.out.println("11111"+vec1[199]);
		// System.out.println("11111"+vec2[1]);
		for (int i = 0; i < 200; i++) {
			vecmulti += vec1[i] * vec2[i];
		}
		float mold1 = getMoldLength(vec1);
		float mold2 = getMoldLength(vec2);
		similar = vecmulti / (mold1 * mold2);
		return similar;
	}

	private static float getMoldLength(float[] vec) {
		float ans = 0.0f;
		for (int i = 0; i < 200; i++) {
			ans += vec[i] * vec[i];
		}
		return (float) Math.sqrt(ans);
	}

	public static Map<Integer, Float> sortMapByValue(Map<Integer, Float> oriMap) {
		if (oriMap == null || oriMap.isEmpty()) {
			return null;
		}
		Map<Integer, Float> sortedMap = new LinkedHashMap<Integer, Float>();
		List<Map.Entry<Integer, Float>> entryList = new ArrayList<Map.Entry<Integer, Float>>(oriMap.entrySet());
		Collections.sort(entryList, new MapValueComparator());

		Iterator<Map.Entry<Integer, Float>> iter = entryList.iterator();
		Map.Entry<Integer, Float> tmpEntry = null;
		while (iter.hasNext()) {
			tmpEntry = iter.next();
			sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
		}
		return sortedMap;
	}
}

class MapValueComparator implements Comparator<Map.Entry<Integer, Float>> {
	@Override
	public int compare(Entry<Integer, Float> me1, Entry<Integer, Float> me2) {
		return me2.getValue().compareTo(me1.getValue());
	}
}
