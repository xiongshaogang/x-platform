/**
 * Copyright (c) 1999-2012 www.huilan.com
 *
 * Licensed under the Huilan License, Version 1.0 (the "License");
 */
package com.xplatform.base.framework.mybatis.engine.condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.xplatform.base.framework.core.constant.AppConstant;
import com.xplatform.base.framework.core.util.DateUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.engine.condition.PropertyFilter.MatchType;
import com.xplatform.base.framework.mybatis.engine.condition.PropertyFilter.RelationType;
import com.xplatform.base.framework.mybatis.engine.query.Relation;
import com.xplatform.base.framework.mybatis.engine.query.criterion.Criterion;
import com.xplatform.base.framework.mybatis.engine.query.criterion.Restrictions;
import com.xplatform.base.framework.mybatis.engine.query.impl.FormRelationImpl;
import com.xplatform.base.framework.mybatis.engine.query.impl.PageRelationImpl;
import com.xplatform.base.framework.mybatis.engine.query.impl.RelaRelationImpl;
import com.xplatform.base.framework.mybatis.engine.query.impl.StateRelationImpl;
import com.xplatform.base.framework.mybatis.engine.vo.VoAttribute;
import com.xplatform.base.framework.mybatis.engine.vo.VoForm;

/**
 * <STRONG>类描述</STRONG> : 查询条件构建工具类
 * <p>
 * 
 * @version 1.0
 *          <p>
 * @author jiagq@huilan.com
 *         <p>
 * 
 * <STRONG>创建时间</STRONG> : 2012-9-3 上午09:56:31
 * <p>
 * <STRONG>修改历史</STRONG> :
 * <p>
 * 
 * <pre>
 * 修改人                   修改时间                      修改内容
 * ---------------         -------------------         -----------------------------------
 * jiagq@huilan.com        2012-9-3 上午09:56:31
 * </pre>
 */
public class CriterionBuilder {
	private List<Criterion> relationList=new ArrayList<Criterion>();
	private Map<String,String> valueMap=new HashMap<String,String>();;
	
	public void buildQueryCondition(HttpServletRequest request,String prefix){
		//Map<String, String[]> formParamMap = Servlets.getParameters(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		for (PropertyFilter filter : filters) {
			Criterion criterion = buildCriterion(filter,prefix);
			this.relationList.add(criterion);
		}
	}
	
	/**
	 * 
	 * @param filter ,需要过滤的字段
	 * @param prefix ,表名称
	 * @return
	 */
	private Criterion buildCriterion(final PropertyFilter filter,String prefix) {
		MatchType matchType = filter.getMatchType();
		Criterion criterion = null;
		// 根据MatchType构造criterion
		switch (matchType) {
		case EQ:
			criterion = eq(filter,prefix);
			break;
		case GT:
			criterion = gt(filter,prefix);
			break;
		case GE:
			criterion = ge(filter,prefix);
			break;
		case LT:
			criterion = lt(filter,prefix);
			break;
		case LE:
			criterion = le(filter,prefix);
			break;
		case LIKE:
			criterion = like(filter,prefix);
			break;
		case BTS:
			criterion = between(filter,prefix);
			break;
		case IN:
			criterion = in(filter,prefix);
			break;
		default:
			criterion = eq(filter,prefix);
		}
		return criterion;
	}
	/**
     * 等于的过滤条件
     * <pre>
     * </pre>
     * @param filter
     * @param formValueMap
     * @param formFieldMap
     * @return
     */
	private Criterion eq(PropertyFilter filter,String prefix) {
		String[] propertyNames = filter.getPropertyNames();
		RelationType relationType = filter.getRelationType();
		String valueStr = filter.getMatchValue().toString();
		Criterion criterion = null;
		String name = propertyNames[0];
		String key = "";
		List<Criterion> criterions = new ArrayList<Criterion>();
		if (relationType == RelationType.ONLY) {
			String tempName = name;
			name = prefix  + AppConstant.DOT + tempName;
			key = prefix  + AppConstant.UNDERLINE + tempName;
			criterion = Restrictions.eq(name);
			valueMap.put(key, valueStr);
		} else {
			for (String propertyName : propertyNames) {
				String tempName = propertyName;
				propertyName = prefix + AppConstant.DOT + tempName;
				key = prefix  + AppConstant.UNDERLINE + tempName;
				criterions.add(Restrictions.eq(propertyName));
				valueMap.put(key, valueStr);
			}
			criterion = Restrictions.or(criterions);
		}
		return criterion;
	}
	
	/**
     * 大于的过滤条件
     * <pre>
     * </pre>
     * @param filter
     * @param formValueMap
     * @param formFieldMap
     * @return
     */
	private Criterion gt(final PropertyFilter filter,String prefix) {
		String[] propertyNames = filter.getPropertyNames();
		RelationType relationType = filter.getRelationType();
		String valueStr = filter.getMatchValue().toString();
		Criterion criterion = null;
		String name = propertyNames[0];
		String key = "";
		List<Criterion> criterions = new ArrayList<Criterion>();
		if (relationType == RelationType.ONLY) {
			String tempName = name;
			name = prefix  + AppConstant.DOT + tempName;
			key = prefix  + AppConstant.UNDERLINE + tempName;
			criterion = Restrictions.gt(name);
			valueMap.put(key, valueStr);
		} else {
			for (String propertyName : propertyNames) {
				String tempName = propertyName;
				propertyName = prefix + AppConstant.DOT + tempName;
				key = prefix  + AppConstant.UNDERLINE + tempName;
				criterions.add(Restrictions.gt(propertyName));
				valueMap.put(key, valueStr);
			}
			criterion = Restrictions.or(criterions);
		}
		return criterion;
	}
	
	/**
     * 大于或等于的过滤条件
     * <pre>
     * </pre>
     * @param filter
     * @param formValueMap
     * @param formFieldMap
     * @return
     */
	private Criterion ge(final PropertyFilter filter,
			String prefix) {
		String[] propertyNames = filter.getPropertyNames();
		RelationType relationType = filter.getRelationType();
		String valueStr = filter.getMatchValue().toString();
		Criterion criterion = null;
		String name = propertyNames[0];
		String key = "";
		List<Criterion> criterions = new ArrayList<Criterion>();
		if (relationType == RelationType.ONLY) {
			String tempName = name;
			name = prefix  + AppConstant.DOT + tempName;
			key = prefix  + AppConstant.UNDERLINE + tempName;
			criterion = Restrictions.ge(name);
			valueMap.put(key, valueStr);
		} else {
			for (String propertyName : propertyNames) {
				String tempName = propertyName;
				propertyName = prefix + AppConstant.DOT + tempName;
				key = prefix  + AppConstant.UNDERLINE + tempName;
				criterions.add(Restrictions.ge(propertyName));
				valueMap.put(key, valueStr);
			}
			criterion = Restrictions.or(criterions);
		}
		return criterion;
	}
	
	/**
	 * 小于的过滤条件
	 * <pre>
	 * </pre>
	 * @param filter
	 * @param formValueMap
	 * @param formFieldMap
	 * @return
	 */
	private Criterion lt(final PropertyFilter filter,
			String prefix) {
		String[] propertyNames = filter.getPropertyNames();
		RelationType relationType = filter.getRelationType();
		String valueStr = filter.getMatchValue().toString();
		Criterion criterion = null;
		String name = propertyNames[0];
		String key = "";
		List<Criterion> criterions = new ArrayList<Criterion>();
		if (relationType == RelationType.ONLY) {
			String tempName = name;
			name = prefix  + AppConstant.DOT + tempName;
			key = prefix  + AppConstant.UNDERLINE + tempName;
			criterion = Restrictions.lt(name);
			valueMap.put(key, valueStr);
		} else {
			for (String propertyName : propertyNames) {
				String tempName = propertyName;
				propertyName = prefix + AppConstant.DOT + tempName;
				key = prefix  + AppConstant.UNDERLINE + tempName;
				criterions.add(Restrictions.lt(propertyName));
				valueMap.put(key, valueStr);
			}
			criterion = Restrictions.or(criterions);
		}
		return criterion;
	}
	
	/**
     * 小于或等于过滤条件
     * <pre>
     * </pre>
     * @param filter
     * @param formValueMap
     * @param formFieldMap
     * @return
     */
	private Criterion le(final PropertyFilter filter,
			String prefix) {
		String[] propertyNames = filter.getPropertyNames();
		RelationType relationType = filter.getRelationType();
		String valueStr = filter.getMatchValue().toString();
		Criterion criterion = null;
		String name = propertyNames[0];
		String key = "";
		List<Criterion> criterions = new ArrayList<Criterion>();
		if (relationType == RelationType.ONLY) {
			String tempName = name;
			name = prefix  + AppConstant.DOT + tempName;
			key = prefix  + AppConstant.UNDERLINE + tempName;
			criterion = Restrictions.le(name);
			valueMap.put(key, valueStr);
		} else {
			for (String propertyName : propertyNames) {
				String tempName = propertyName;
				propertyName = prefix + AppConstant.DOT + tempName;
				key = prefix  + AppConstant.UNDERLINE + tempName;
				criterions.add(Restrictions.le(propertyName));
				valueMap.put(key, valueStr);
			}
			criterion = Restrictions.or(criterions);
		}
		return criterion;
	}
	
	/**
     * like过滤条件
     * <pre>
     * </pre>
     * @param filter
     * @param formValueMap
     * @param formFieldMap
     * @return
     */
	private Criterion like(final PropertyFilter filter,
			String prefix) {
		String[] propertyNames = filter.getPropertyNames();
		RelationType relationType = filter.getRelationType();
		String valueStr = filter.getMatchValue().toString();
		Criterion criterion = null;
		String name = propertyNames[0];
		String key = "";
		List<Criterion> criterions = new ArrayList<Criterion>();
		if (relationType == RelationType.ONLY) {
			String tempName = name;
			name = prefix  + AppConstant.DOT + tempName;
			key = prefix  + AppConstant.UNDERLINE + tempName;
			criterion = Restrictions.like(name,valueStr);
			valueMap.put(key, valueStr);
		} else {
			for (String propertyName : propertyNames) {
				String tempName = propertyName;
				propertyName = prefix + AppConstant.DOT + tempName;
				key = prefix  + AppConstant.UNDERLINE + tempName;
				criterions.add(Restrictions.like(propertyName,valueStr));
				valueMap.put(key, valueStr);
			}
			criterion = Restrictions.or(criterions);
		}
		return criterion;
	}
	
	/**
	 * between过滤条件
	 * <pre>
	 * </pre>
	 * @param filter
	 * @param formValueMap
	 * @param formFieldMap
	 * @return
	 */
	private Criterion between(final PropertyFilter filter,
			String prefix) {
		String[] propertyNames = filter.getPropertyNames();
		String valueStr = filter.getMatchValue().toString();
		String[] valueArr = StringUtil.split(valueStr, AppConstant.SPLIT_STR);
		Criterion criterion = null;
		String name = propertyNames[0];
		String tempName = name;
		name = prefix + AppConstant.DOT +  tempName;
		String key = prefix  + AppConstant.UNDERLINE + tempName;
		String name_lo = name + "_LO";
		String name_hi = name + "_HI";
		String key_lo = key + "_LO";
		String key_hi = key + "_HI";
		criterion = Restrictions.between(name, name_lo, name_hi);
		String startVal = valueArr[0];
		String endVal = valueArr[1];
		if (StringUtil.length(startVal)==10&&StringUtil.length(endVal)==10) {
			startVal = DateUtils.processDateStr(startVal);
			endVal = DateUtils.processDateStr(endVal);
			if(StringUtil.isNotEmpty(startVal)&&StringUtil.isNotEmpty(endVal)){
				startVal = startVal+" 00:00:00";
				endVal = endVal+" 23:59:59";
			}
		}
		valueMap.put(key_lo, startVal);
		valueMap.put(key_hi, endVal);
		return criterion;
	}
	
	/**
	    * in过滤条件
	    * <pre>
	    * </pre>
	    * @param filter
	    * @param formValueMap
	    * @param formFieldMap
	    * @return
	    */
		private Criterion in(final PropertyFilter filter,
				String prefix) {
			String[] propertyNames = filter.getPropertyNames();
			String valueStr = filter.getMatchValue().toString();
			Criterion criterion = null;
			String name = propertyNames[0];
			String tempName = name;
			name = prefix + AppConstant.DOT+ tempName;
			String key = prefix  + AppConstant.UNDERLINE + tempName;
			criterion = Restrictions.in(name,valueStr);
			valueMap.put(key, valueStr);
			return criterion;
		}

	public List<Criterion> getRelationList() {
		return relationList;
	}

	public void setRelationList(List<Criterion> relationList) {
		this.relationList = relationList;
	}

	public Map<String, String> getValueMap() {
		return valueMap;
	}

	public void setValueMap(Map<String, String> valueMap) {
		this.valueMap = valueMap;
	}
}
