/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
package com.xplatform.base.framework.mybatis.engine.query.criterion;

import java.util.List;

/**
 * 
 * <STRONG>查询条件拼装</STRONG> :  <p>
 *   
 * @version 1.0 <p>
 * @author mengfx@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : Sep 3, 2012 3:10:46 PM<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   	修改时间                     				修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Sep 3, 2012 3:10:46 PM
 *</pre>
 */
public class Restrictions {

	Restrictions() {
		//cannot be instantiated
	}

	/**
	 * 
	 * <pre>
	 * ID相等
	 * ID=#{ID}
	 * </pre>
	 * @return
	 */
	public static Criterion idEq() {
		return new IdentifierEqExpression();
	}
	
	/**
	 * 
	 * <pre>
	 * 属性等于
	 * propertyName=#{propertyName}
	 * </pre>
	 * @param propertyName
	 * @return
	 */
	public static SimpleExpression eq(String propertyName) {
		return new SimpleExpression(propertyName, "=");
	}
	
	/**
	 * 
	 * <pre>
	 * 属性不等等于
	 * propertyName<>#{propertyName}
	 * </pre>
	 * @param propertyName
	 * @return
	 */
	public static SimpleExpression ne(String propertyName) {
		return new SimpleExpression(propertyName,  "<>");
	}
	
	/**
	 * 
	 * <pre>
	 * like查询默认不区分大小写
	 * </pre>
	 * @param propertyName
	 * @return
	 */
	public static LikeExpression like(String propertyName,String value) {
		return new LikeExpression(propertyName,value);
	}
	/**
	 * 
	 * <pre>
	 * like查询默认不区分大小写
	 * </pre>
	 * @param propertyName
	 * @return
	 */
	public static LikeLExpression likeL(String propertyName,String value) {
		return new LikeLExpression(propertyName,value);
	}
	/**
	 * 
	 * <pre>
	 * like查询默认不区分大小写
	 * </pre>
	 * @param propertyName
	 * @return
	 */
	public static LikeRExpression likeR(String propertyName,String value) {
		return new LikeRExpression(propertyName,value);
	}
	/**
	 * 
	 * <pre>
	 * like查询默认不区分大小写
	 * </pre>
	 * @param propertyName
	 * @return
	 */
	public static LikePExpression likeP(String propertyName,String value) {
		return new LikePExpression(propertyName,value);
	}
	/**
	 * 
	 * <pre>
	 * like查询默认不区分大小写
	 * </pre>
	 * @param propertyName
	 * @return
	 */
	public static LikeFExpression likeF(String propertyName,String value) {
		return new LikeFExpression(propertyName,value);
	}
	/**
	 * 
	 * <pre>
	 * like查询
	 * </pre>
	 * @param propertyName
	 * @param ignoreCase true区分大小写 false不区分大小写
	 * @return
	 */
	public static LikeExpression like(String propertyName, boolean ignoreCase,String value) {
		return new LikeExpression(propertyName,ignoreCase,value);
	}
	
	/**
	 * 
	 * <pre>
	 * 大于
	 * eg：propertyName>#{propertyName}
	 * </pre>
	 * @param propertyName
	 * @return
	 */
	public static SimpleExpression gt(String propertyName) {
		return new SimpleExpression(propertyName, ">");
	}
	
	/**
	 * 
	 * <pre>
	 * 小于
	 * eg：propertyName<#{propertyName}
	 * </pre>
	 * @param propertyName
	 * @return
	 */
	public static SimpleExpression lt(String propertyName) {
		return new SimpleExpression(propertyName, "<");
	}
	
	/**
	 * 
	 * <pre>
	 * 小于等于
	 * eg：propertyName<=#{propertyName}
	 * </pre>
	 * @param propertyName
	 * @return
	 */
	public static SimpleExpression le(String propertyName) {
		return new SimpleExpression(propertyName, "<=");
	}
	
	/**
	 * 
	 * <pre>
	 * 大于等于
	 * eg：propertyName>=#{propertyName}
	 * </pre>
	 * @param propertyName
	 * @return
	 */
	public static SimpleExpression ge(String propertyName) {
		return new SimpleExpression(propertyName, ">=");
	}
	
	/**
	 * 
	 * <pre>
	 * between
	 * eg: propertyName between #{name_1} and #{name_2} 
	 * </pre>
	 * @param propertyName
	 * @param lo
	 * @param hi
	 * @return
	 */
	public static Criterion between(String propertyName, String lo, String hi) {
		return new BetweenExpression(propertyName, lo, hi);
	}
	
	/**
	 * 
	 * <pre>
	 * in
	 * eg: propertyName in(#{propertyName})
	 * </pre>
	 * @param propertyName
	 * @return
	 */
	public static Criterion in(String propertyName,String values) {
		return new InExpression(propertyName,values);
	}
	
	
	/**
	 * 
	 * <pre>
	 * isNull
	 * eg: propertyName IS NULL
	 * </pre>
	 * @param propertyName
	 * @return
	 */
	public static Criterion isNull(String propertyName) {
		return new NullExpression(propertyName);
	}
	
	/**
	 * 
	 * <pre>
	 * 对比两个字段值是否相等
	 * eg: propertyName = otherPropertyName
	 * </pre>
	 * @param propertyName
	 * @param otherPropertyName
	 * @return
	 */
	public static PropertyExpression eqProperty(String propertyName, String otherPropertyName) {
		return new PropertyExpression(propertyName, otherPropertyName, "=");
	}
	
	public static PropertyExpression neProperty(String propertyName, String otherPropertyName) {
		return new PropertyExpression(propertyName, otherPropertyName, "<>");
	}
	
	public static PropertyExpression ltProperty(String propertyName, String otherPropertyName) {
		return new PropertyExpression(propertyName, otherPropertyName, "<");
	}
	
	public static PropertyExpression leProperty(String propertyName, String otherPropertyName) {
		return new PropertyExpression(propertyName, otherPropertyName, "<=");
	}
	
	public static PropertyExpression gtProperty(String propertyName, String otherPropertyName) {
		return new PropertyExpression(propertyName, otherPropertyName, ">");
	}
	
	public static PropertyExpression geProperty(String propertyName, String otherPropertyName) {
		return new PropertyExpression(propertyName, otherPropertyName, ">=");
	}
	
	/**
	 * 
	 * <pre>
	 * isNotNull
	 * eg: propertyName IS NOT NULL
	 * </pre>
	 * @param propertyName
	 * @return
	 */
	public static Criterion isNotNull(String propertyName) {
		return new NotNullExpression(propertyName);
	}
	
	/**
	 * 
	 * <pre>
	 * 连接查询条件 关联 AND
	 * </pre>
	 * @param criterions
	 * @return
	 */
	public static LogicalExpression and(List<Criterion> criterions) {
		return new LogicalExpression(criterions,  "and");
	}
	
	/**
	 * 
	 * <pre>
	 * 连接查询条件 关联 OR
	 * </pre>
	 * @param criterions
	 * @return
	 */
	public static LogicalExpression or(List<Criterion> criterions) {
		return new LogicalExpression(criterions,  "or");
	}
	
	/**
	 * 
	 * <pre>
	 * 查询条件 非
	 * </pre>
	 * @param expression
	 * @return
	 */
	public static Criterion not(Criterion expression) {
		return new NotExpression(expression);
	}
}
