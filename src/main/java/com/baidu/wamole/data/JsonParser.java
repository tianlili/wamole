package com.baidu.wamole.data;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

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

	public static StringBuilder classToJson(Class<?> c) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", c.getSimpleName());
		map.put("type", c.getName());
		for (Method m : c.getDeclaredMethods()) {
			if (m.isAnnotationPresent(Exported.class)
					&& Modifier.isStatic(m.getModifiers())) {
				try {
					map.put(parseNameFromGetMethod(m), m.invoke(null)
							.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return mapToJson(map);
	}

	public static StringBuilder listToJson(List<?> list) {
		return listToJson(list, true);
	}

	public static StringBuilder classListToJson(List<? extends Class<?>> list) {
		StringBuilder sb = new StringBuilder();
		for (Class<?> c : list) {
			sb.append(classToJson(c) + ",");
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
			sb.insert(0, "[");
			sb.append("]");
		}
		return sb;
	}

	public static StringBuilder mapToJson(Map<?, ?> map) {
		StringBuilder sb = new StringBuilder();
		for (Object key : map.keySet()) {
			sb.append(key + " : \"" + map.get(key) + "\",");
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
			sb.insert(0, "{");
			sb.append("}");
		}
		return sb;
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
				if (value == null) {
					sb.setLength(0);
					return sb;
				}

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
		int start = 0;
		if (methodName.startsWith("is")) {
			start = 2;
		} else if (methodName.startsWith("get")) {
			start = 3;
		}
		return Character.toLowerCase(methodName.charAt(start))
				+ methodName.substring(start + 1);
	}

	private static String parseNameFromGetMethod(Method m) {
		return parseNameFromGetMethod(m.getName());
	}

	/**
	 * 从json到对象注入
	 * <p>
	 * {a.type, a.x1, a.x2, b.type, b.x1, b.x2...}
	 * 
	 * @param c
	 *            希望提取的对象类
	 * @param uriInfo
	 * @return
	 */
	public static Map<String, Object> jsonToObject(UriInfo uriInfo) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		try {
			MultivaluedMap<String, String> data = uriInfo.getPathParameters();
			//拆分成三维数组，方便处理
			HashMap<String, MultivaluedMap<String, String>> map = new HashMap<String, MultivaluedMap<String,String>>();
			for(String key : data.keySet()){
				String item = key.split("\\.")[0];
				String _key = key.substring(item.length());
				if(map.containsKey(item))
					map.get(item).add(_key, data.getFirst(key));
			}
			
			for(String prefix : map.keySet()){
				MultivaluedMap<String, String> _data = map.get(prefix);
				Class<?> c = Class.forName(_data.getFirst("type"));
				Object o = c.newInstance();				
				bindObj(o, _data);
				result.put(prefix, o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static void bindObj(Object o, MultivaluedMap<String, String> map){
		for (Method m : o.getClass().getDeclaredMethods()) {
			if (m.isAnnotationPresent(Imported.class))
				try {
					List<String> value = map.get(parseNameFromGetMethod(m));
					if (value.size() == 1)
						m.invoke(o, value.get(0));
					// TODO, 数组形式后续开发
					// else
					// m.invoke(o, value.toArray());
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
}
