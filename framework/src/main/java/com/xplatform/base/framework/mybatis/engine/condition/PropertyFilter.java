/**
 * Copyright (c) 1999-2012 www.huilan.com
 *
 * Licensed under the Huilan License, Version 1.0 (the "License");
 */
package com.xplatform.base.framework.mybatis.engine.condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.xplatform.base.framework.core.constant.AppConstant;
import com.xplatform.base.framework.core.util.ServletUtil;
import com.xplatform.base.framework.core.util.StringUtil;


/**  
 * <STRONG>类描述</STRONG> : 与具体ORM实现无关的属性过滤条件封装类, 主要记录页面中简单的搜索过滤条件. <p>
 *   
 * @version 1.0 <p>
 * @author jiagq@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : 2012-9-3 下午02:27:29<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   修改时间                      修改内容
 * ---------------         -------------------         -----------------------------------
 * jiagq@huilan.com        2012-9-3 下午02:27:29
 *</pre>  
 */
public class PropertyFilter {

	/** 多个属性间OR关系的分隔符 */
	public static final String OR_SEPARATOR = "_OR_";
	/** 属性比较类型*/
	public enum MatchType {
		/** 等于， 对应SQL条件中的"field = value*/
		EQ,
		/** 大于， 对应SQL条件中的"field > value*/
		GT,
		/** 大于或等于， 对应SQL条件中的"field >= value*/
		GE,
		/** 小于或等于，对应SQL条件中的"field < value*/
		LT,
		/** 小于，对应SQL条件中的"field <= value*/
		LE,
		/** 对应SQL条件中的"field like value*/
		LIKE,
		/** 对应SQL条件中的"field like value*/
		LIKEL,
		/** 对应SQL条件中的"field like value*/
		LIKER,
		/** 对应SQL条件中的"field like value*/
		LIKEP,
		/** 对应SQL条件中的"field like value*/
		LIKEF,
		/** 在两值之间，对应SQL条件中的"between*/
		BTS,
		/** 在两值之间，对应SQL条件中的"between*/
		BTE,
		/** 在一个集合范围之内，对应SQL条件中的"IN*/
		IN;
	}
	/** 组合关系类型*/
	public enum RelationType {
		/**OR */
		OR,
		/**ONLY */
		ONLY;
	}
	/**
	 * 比较类型
	 */
	private MatchType matchType = null;
	/**
	 * 组合关系类型
	 */
	private RelationType relationType = null;
	/**
	 * 比较值
	 */
	private String matchValue = null;
	/**
	 * 属性名集合
	 */
	private String[] propertyNames = null;
	/**
	 * 空构造函数
	 */
	public PropertyFilter(){
	}
	/**
	 * @param filterName 比较属性字符串,含待比较的比较类型、属性值类型及属性列表. 
	 *                   eg. 
	 *                       EQ_NAME
	 *                       IN_CODE
	 *                       BTS_SIZE 和BTE_SIZE 需要成对出现 用于区间条件
	 *                       
	 * @param value 待比较的值.
	 */
	public PropertyFilter(final String filterName, final String value,final Map<String, String []> filterParamMap){
		String matchTypeCode = StringUtils.substringBefore(filterName, AppConstant.UNDERLINE);
		if (StringUtil.equals(matchTypeCode, matchType.BTE.toString())){
			return;
		}
		String nameStr = StringUtil.removeStart(filterName, matchTypeCode+AppConstant.UNDERLINE);
		/*System.out.println("filterName:" + filterName);
		System.out.println("matchTypeCode:" + matchTypeCode);
		System.out.println("nameStr:" + nameStr);
		System.out.println("value:" + value);*/
		try {
			if (StringUtil.equals(matchTypeCode, matchType.BTS.toString())){
				String bts = value;
				String[] bteArr = filterParamMap.get(matchType.BTE+AppConstant.UNDERLINE+nameStr);
				if (bteArr!=null){
				String bte = bteArr[0];
				if (StringUtil.isNotEmpty(bts)&&StringUtil.isNotEmpty(bte)){
						String[] name = {nameStr};
						propertyNames =name;
						matchValue = bts+AppConstant.SPLIT_STR+bte;
						relationType = RelationType.ONLY;
						matchType = Enum.valueOf(MatchType.class, matchTypeCode);
						
					} else {
	                  return;
					}	
				}else {
					  return;	
				}
			}else if(StringUtil.equals(matchTypeCode, matchType.IN.toString())){
				String[] name = {nameStr};
				propertyNames =name;
				relationType = RelationType.ONLY;
				//在值串前后加单引号
				matchValue = AppConstant.S_MARK+value+AppConstant.S_MARK;
				//在逗号的两次加单引号
				matchValue = StringUtil.replace(matchValue, AppConstant.COMMA, AppConstant.S_MARK+AppConstant.COMMA+AppConstant.S_MARK);
				matchType = Enum.valueOf(MatchType.class, matchTypeCode);
			}else{
				propertyNames = StringUtils.splitByWholeSeparator(nameStr, OR_SEPARATOR);
				if(hasMultiProperties()){
				relationType = RelationType.OR;
			   }else {
				relationType = RelationType.ONLY;
			   }
				matchValue = value;
				matchType = Enum.valueOf(MatchType.class, matchTypeCode);
			}
		/*	System.out.println("relationType:" + relationType);
			System.out.println("propertyNames.length:" + propertyNames.length);
			System.out.println("matchValue:" + matchValue);
			System.out.println("matchType:" + matchType);
			System.out.println("=======================");*/
		} catch (RuntimeException e){
			e.printStackTrace();
			throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性比较类型!", e);
		}
	}  
	/**
	 * 从HttpRequest中创建PropertyFilter列表, 默认Filter属性名前缀为filter.
	 * 
	 * @see #buildFromHttpRequest(HttpServletRequest, String)
	 * @param request HttpServletRequest
	 * @return 属性过滤器集合
	 */
	public static List<PropertyFilter> buildFromHttpRequest(final HttpServletRequest request) {
		return buildFromHttpRequest(request, "filter");
	}
	/**
	 * 从HttpRequest中创建PropertyFilter列表
	 * PropertyFilter命名规则为Filter属性前缀_比较类型属性类型_属性名.
	 * eg.
	 * filter_EQ_name
	 * filter_LIKE_name_OR_email
	 * @param request HttpServletRequest
	 * @param filterPrefix 过滤属性前缀
	 * @return 属性过滤器集合
	 */
	public static List<PropertyFilter> buildFromHttpRequest(final HttpServletRequest request, final String filterPrefix) {
		List<PropertyFilter> filterList = new ArrayList<PropertyFilter>();
		try {
			//从request中获取含属性前缀名的参数,构造去除前缀名后的参数Map.
			Map<String, String []> filterParamMap = ServletUtil.getParametersStartWithPrefix(request, filterPrefix + AppConstant.UNDERLINE);
			//分析参数Map,构造PropertyFilter列表
			for (Map.Entry<String, String []> entry : filterParamMap.entrySet()){
				String filterName = entry.getKey();
				String[] values = (String[]) entry.getValue();
				//如果value值为空,则忽略此filter.
				if (values!=null&&StringUtil.isNotEmpty(values[0].toString())){
					PropertyFilter filter = new PropertyFilter(filterName, values[0],filterParamMap);
					if (filter.propertyNames!=null){
						filterList.add(filter);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filterList;
	}
	public static List<PropertyFilter> buildFromHttpRequest(final Map<String, String []> filterParamMap) {
		List<PropertyFilter> filterList = new ArrayList<PropertyFilter>();
		try {
			//从request中获取含属性前缀名的参数,构造去除前缀名后的参数Map.
			//分析参数Map,构造PropertyFilter列表
			for (Map.Entry<String, String []> entry : filterParamMap.entrySet()){
				String filterName = entry.getKey();
				String[] values = (String[]) entry.getValue();
				//如果value值为空,则忽略此filter.
				if (values!=null&&StringUtil.isNotEmpty(values[0].toString())){
					PropertyFilter filter = new PropertyFilter(filterName, values[0],filterParamMap);
					if (filter.propertyNames!=null){
						filterList.add(filter);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filterList;
	}
	/**
	 * 获取比较方式.
	 * @return MatchType
	 */
	public MatchType getMatchType() {
		return matchType;
	}

	/**
	 * 获取比较值.
	 * @return matchValue
	 */
	public Object getMatchValue() {
		return matchValue;
	}
	/**
	 * 获取比较属性名称列表.
	 * @return propertyNames
	 */
	public String[] getPropertyNames() {
		return propertyNames;
	}
	/**
	 * 获取唯一的比较属性名称.
	 * @return propertyName
	 */
	public String getPropertyName() {
		Assert.isTrue(propertyNames.length == 1, "There are not only one property in this filter.");
		return propertyNames[0];
	}
	/**
	 * 是否比较多个属性.
	 * @return true/false
	 */
	public boolean hasMultiProperties() {
		return (propertyNames.length > 1);
	}
	public RelationType getRelationType() {
		return relationType;
	}
}
