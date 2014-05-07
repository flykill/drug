package com.drug.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.drug.exception.DrugException;

public class PropertiesUtil {

	private static Map<String, Properties> PROPERTIES_MAP = new ConcurrentHashMap<String, Properties>();

	public static Properties getProperties(String fileKey) {
		if (PROPERTIES_MAP.containsKey(fileKey)) {
			return PROPERTIES_MAP.get(fileKey);
		}
		Properties properties = new Properties();
		try {
			InputStream input = PropertiesUtil.class
					.getResourceAsStream(fileKey);
			properties.load(input);
		} catch (IOException e) {
			throw new DrugException("解析 配置文件出错!");
		}
		PROPERTIES_MAP.put(fileKey, properties);
		return properties;
	}

	public static Properties getDictionaryDB() {
		return getProperties("/dictionary/db.properties");
	}

	public static Properties getDictionary() {
		return getProperties("/dictionary/dictionary.properties");
	}
}
