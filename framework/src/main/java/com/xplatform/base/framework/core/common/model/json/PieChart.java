package com.xplatform.base.framework.core.common.model.json;

import java.util.List;

public class PieChart {

	private String name;
	private String type;//类型
	private String radius ;
	private List<String> center;
	private List data;//数据
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRadius() {
		return radius;
	}
	public void setRadius(String radius) {
		this.radius = radius;
	}
	public List getData() {
		return data;
	}
	public void setData(List data) {
		this.data = data;
	}
	public List<String> getCenter() {
		return center;
	}
	public void setCenter(List<String> center) {
		this.center = center;
	}
	
	
}
