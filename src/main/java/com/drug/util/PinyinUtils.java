package com.drug.util;

import net.sourceforge.pinyin4j.PinyinHelper;

public class PinyinUtils {

	public static String getPinYin(String str) {
		char[] chars = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (char c : chars) {
			int i = (int) c;
			if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
				sb.append(Character.toLowerCase(c));
			} else {
				String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c);
				if (null != pinyin && pinyin.length > 0) {
					sb.append(pinyin[0].substring(0, 1));
				}
			}
		}
		return sb.toString();
	}
}
