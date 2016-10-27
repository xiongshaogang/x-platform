package com.xplatform.base.framework.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Map返回值封装，常用于业务层需要多个返回值 1个kv值使用:MapKit.create("test1", "a").getMap();
 * 多个kv值使用:MapKit.create("test1", "a").put("test2","b").getMap();
 */
public class MapKit {

	private Map<String, Object> map = new HashMap<String, Object>();

	public MapKit() {

	}

	public MapKit(String key, Object value) {
		map.put(key, value);
	}

	public static MapKit create() {
		return new MapKit();
	}

	public static MapKit create(String key, Object value) {
		return new MapKit(key, value);
	}

	public MapKit put(String key, Object value) {
		map.put(key, value);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Object key) {
		return (T) map.get(key);
	}

	/**
	 * key 存在，但 value 可能为 null
	 */
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	/**
	 * key 存在，并且 value 不为 null
	 */
	public boolean notNull(Object key) {
		return map.get(key) != null;
	}

	/**
	 * key 不存在，或者 key 存在但 value 为null
	 */
	public boolean isNull(Object key) {
		return map.get(key) == null;
	}

	/**
	 * key 存在，并且 value 为 true，则返回 true
	 */
	public boolean isTrue(Object key) {
		Object value = map.get(key);
		return (value instanceof Boolean && ((Boolean) value == true));
	}

	/**
	 * key 存在，并且 value 为 false，则返回 true
	 */
	public boolean isFalse(Object key) {
		Object value = map.get(key);
		return (value instanceof Boolean && ((Boolean) value == false));
	}

	@SuppressWarnings("unchecked")
	public <T> T remove(Object key) {
		return (T) map.remove(key);
	}

	public Map<String, Object> getMap() {
		return map;
	}
}
