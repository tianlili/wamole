package com.baidu.wamole.data;

import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.jetty.util.B64Code;

/**
 * 
 * 
 * @see com.baidu.wamole.data.Exported, com.baidu.wamole.data.URLEncode
 * @author yangbo
 * 
 */
public class JsonParser {
	public static StringBuilder objToJson(Object obj) {
		return objToJson(obj, true);
	}

	public static StringBuilder listToJson(List<?> list) {
		return listToJson(list, true);
	}

	private static StringBuilder objToJson(Object obj, boolean recurse) {
		StringBuilder sb = new StringBuilder();
		for (Method m : obj.getClass().getMethods()) {
			if (m.isAnnotationPresent(Exported.class)
					&& (recurse || m.getAnnotation(Exported.class).recurse())) {
				sb.append(methodToJson(m, obj));
			}
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
			sb.insert(0, "{");
			sb.append("}");
			return sb;
		} else
			return null;
	}

	private static StringBuilder methodToJson(Method m, Object obj) {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(parseNameFromGetMethod(m.getName()));
			sb.append(":");
			Object fieldObject = m.invoke(obj);
			String value = "";

			// 尝试转换数组
			if (fieldObject instanceof List) {
				value = listToJson((List<?>) fieldObject, false).toString();
			} else {
				Exported eAnno = m.getAnnotation(Exported.class);
				value = m.invoke(obj).toString();
				if (eAnno.encode())
					value = B64Code.encode(value, eAnno.enc());
				value = "\"" + value + "\"";
			}
			sb.append(value);
			sb.append(",");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}

	private static StringBuilder listToJson(List<?> list, boolean recurse) {
		StringBuilder sb = new StringBuilder("[");
		for (Object o : list) {
			try {
				StringBuilder sb0 = objToJson(o, recurse);
				if (sb0 != null) {
					sb.append(sb0);
					sb.append(',');
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (sb.length() > 1) {
			sb.setLength(sb.length() - 1);
		}
		sb.append("]");
		return sb;
	}

	/**
	 * from getMethod to method
	 * 
	 * @param methodName
	 * @return
	 */
	private static String parseNameFromGetMethod(String methodName) {
		int start = 3;
		if (methodName.startsWith("is")) {
			start = 2;
		}
		return Character.toLowerCase(methodName.charAt(start))
				+ methodName.substring(start + 1);
	}
}
