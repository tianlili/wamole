package com.baidu.wamole.data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;

public class JsonParser {
	public static StringBuilder objToJson(Object obj) {
		StringBuilder sb = new StringBuilder();
		for (Method m : obj.getClass().getMethods()) {
			if (checkExported(m, obj, Exported.class)) {
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

	public static StringBuilder methodToJson(Method m, Object obj) {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(parseNameFromGetMethod(m.getName()));
			sb.append(":");
			Object fieldObject = m.invoke(obj);
			String value = "";
			// 尝试转换数组
			if (fieldObject instanceof List) {
				value = listToJson((List<?>) fieldObject).toString();
			} else {
				value = m.invoke(obj).toString();
				if (checkExported(m, obj, URLEncode.class))
					value = URLEncoder.encode(value,
							m.getAnnotation(URLEncode.class).enc());
				value = "\"" + value + "\"";
			}
			sb.append(value);
			sb.append(",");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}

	public static StringBuilder listToJson(List<?> list) {
		StringBuilder sb = new StringBuilder("[");
		for (Object o : list) {
			try {
				StringBuilder sb0 = objToJson(o);
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

	private static boolean checkExported(Method method, Object obj,
			Class<? extends Annotation> annotationClass) {
		boolean result = false;
		if (method.isAnnotationPresent(annotationClass))
			result = true;
		// else {
		// String name = method.getName();
		// Class<?>[] cs = method.getParameterTypes();
		// Class<?> sc = obj.getClass().getGenericSuperclass().getClass();
		//
		// try {
		// Method superMethod = sc.getMethod(name, cs);
		// result = checkExported(superMethod, obj, annotationClass);
		// } catch (SecurityException e) {
		// } catch (NoSuchMethodException e) {
		// }
		// }
		return result;
	}
}
