/**
 * Copyright (c) 1999-2012 www.huilan.com
 *
 * Licensed under the Huilan License, Version 1.0 (the "License");
 */
package com.xplatform.base.framework.mybatis.engine.condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.mybatis.engine.query.criterion.Criterion;

/**
 * 
 * <STRONG>类描述</STRONG> : 过滤信息类 <p>
 *   
 * @version 1.0 <p>
 * @author jiagq@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : 2012-9-11 下午04:07:44<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   修改时间                     修改内容
 * ---------------         -------------------         -----------------------------------
 * jiagq@huilan.com        2012-9-11 下午04:07:44
 *</pre>
 */
public class Condition{
	/**
	 * form过滤条件信息
	 */
	List<Criterion> formCriterionList = new ArrayList<Criterion>();
	/**
	 * page过滤条件信息
	 */
	List<Criterion> pageCriterionList = new ArrayList<Criterion>();
	/**
	 * state过滤条件信息
	 */
	List<Criterion> stateCriterionList = new ArrayList<Criterion>();
	/**
	 * rela过滤条件信息
	 */
	List<Criterion> relaCriterionList = new ArrayList<Criterion>();
	/**
	 * 表单过滤条件值信息
	 */
	Map<String,String> formValueMap ;
	
	public Condition() {
		super();
	}
	public Map<String, String> getFormValueMap() {
		return formValueMap;
	}
	public void setFormValueMap(Map<String, String> formValueMap) {
		this.formValueMap = formValueMap;
	}


	public List<Criterion> getFormCriterionList() {
		return formCriterionList;
	}
	public void setFormCriterionList(List<Criterion> formCriterionList) {
		this.formCriterionList = formCriterionList;
	}
	public List<Criterion> getPageCriterionList() {
		return pageCriterionList;
	}
	public void setPageCriterionList(List<Criterion> pageCriterionList) {
		this.pageCriterionList = pageCriterionList;
	}
	public List<Criterion> getStateCriterionList() {
		return stateCriterionList;
	}
	public void setStateCriterionList(List<Criterion> stateCriterionList) {
		this.stateCriterionList = stateCriterionList;
	}
	public List<Criterion> getRelaCriterionList() {
		return relaCriterionList;
	}
	public void setRelaCriterionList(List<Criterion> relaCriterionList) {
		this.relaCriterionList = relaCriterionList;
	}
	
}
