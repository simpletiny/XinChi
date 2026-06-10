package com.xinchi.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XMLUtils {

	public static Map<String, String> toMap(String source) {
		Map<String, String> map = new HashMap<>();
		try {
			Document doc = DocumentHelper.parseText(source);
			Element root = doc.getRootElement();
			List<Element> elements = root.elements();
			// 遍历所有子元素
			for (Element element : elements) {
				map.put(element.getName(), element.getText());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static String mapToXml(Map<String, String> source) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (Map.Entry<String, String> entry : source.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			// 值不为空才添加标签
			if (value != null && !value.isEmpty()) {
				sb.append("<").append(key).append("><![CDATA[").append(value).append("]]></").append(key).append(">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

}
