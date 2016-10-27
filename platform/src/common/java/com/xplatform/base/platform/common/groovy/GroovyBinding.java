package com.xplatform.base.platform.common.groovy;

import groovy.lang.Binding;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GroovyBinding extends Binding {
	private Map variables;
	private static ThreadLocal<Map<String, Object>> localVars = new ThreadLocal();

	public static Map<String, Object> propertyMap = new HashMap();

	public GroovyBinding() {
	}

	public GroovyBinding(Map variables) {
		localVars.set(variables);
	}

	public GroovyBinding(String[] args) {
		this();
		setVariable("args", args);
	}

	public Object getVariable(String name) {
		Map map = (Map) localVars.get();
		Object result = null;
		if ((map != null) && (map.containsKey(name))) {
			result = map.get(name);
		} else {
			result = propertyMap.get(name);
		}

		return result;
	}

	public void setVariable(String name, Object value) {
		if (localVars.get() == null) {
			Map vars = new LinkedHashMap();
			vars.put(name, value);
			localVars.set(vars);
		} else {
			((Map) localVars.get()).put(name, value);
		}
	}

	public Map getVariables() {
		if (localVars.get() == null) {
			return new LinkedHashMap();
		}

		return (Map) localVars.get();
	}

	public void clearVariables() {
		localVars.remove();
	}

	public Object getProperty(String property) {
		return propertyMap.get(property);
	}

	public void setProperty(String property, Object newValue) {
		propertyMap.put(property, newValue);
	}
}