package com.xplatform.base.orgnaization.resouce.mybatis.vo;

public class ResourceVo {
	private String id;
	private String name;        // 机构名称
	private String code;        //机构代码
	private String url;         //模块url
	private String optType;     //操作数据字典
	private String optCode;     //操作code
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getOptType() {
		return optType;
	}
	public void setOptType(String optType) {
		this.optType = optType;
	}
	public String getOptCode() {
		return optCode;
	}
	public void setOptCode(String optCode) {
		this.optCode = optCode;
	}
}
