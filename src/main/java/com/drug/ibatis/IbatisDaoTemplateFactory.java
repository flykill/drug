package com.drug.ibatis;

import java.util.HashMap;
import java.util.Map;

public class IbatisDaoTemplateFactory {

	private static Map<String, IBatisDaoTemplate> TEMPLATE_MAP = new HashMap<String, IBatisDaoTemplate>();

	public static IBatisDaoTemplate getTemplate(String key) {
		if (TEMPLATE_MAP.containsKey(key)) {
			return TEMPLATE_MAP.get(key);
		}
		IBatisDaoTemplate template = new IBatisDaoTemplate(new IBatisConfig(
				"ibatis/" + key + "_Config.xml").getSqlMapper());
		return template;
	}

}
