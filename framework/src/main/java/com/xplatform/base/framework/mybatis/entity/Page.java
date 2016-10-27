/**
 * Copyright (c) 1999-2012 www.huilan.com
 *
 * Licensed under the Huilan License, Version 1.0 (the "License");
 * 
 * $Id: Page.java 1183 2010-08-28 08:05:49Z alex cui $
 */
package com.xplatform.base.framework.mybatis.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * 与具体ORM实现无关的分页参数及查询结果封装.
 * 
 * 注意所有序号从1开始.
 * 
 * @param <T> Page中记录的类型.
 * 
 * @author xiehs
 */
public class Page<T> implements Serializable {
	//-- 公共变量 --//
	/**
	 * 正序排序参数
	 */
	public static final String ASC = "asc";
	
	/**
	 * 倒序排序参数
	 */
	public static final String DESC = "desc";

	//-- 分页参数 --//
	/**
	 * 页号
	 */
	protected int pageNo = 1;
	
	/**
	 * 每页记录数
	 */
	protected int pageSize;
	
	/**
	 * 排序条件
	 */
	protected String orderBy = null;
	
	/**
	 * 排序方向值
	 */
	protected String order = null;
	
	/**
	 * 是否自动执行查询总记录数参数
	 */
	protected boolean autoCount = true;
	
	/**
	 * 参数对象
	 */
	protected Object parameter;

	/**
	 * JSON数据格式，分页成功返回标志
	 */
	protected boolean success = true;

	//-- 返回结果 --//
	/**
	 * 记录集
	 */
	protected List<T> result = new ArrayList<T>();
	
	/**
	 * 总记录数
	 */
	protected long totalCount = 0;

	/**
	 * 构造函数
	 */
	public Page() {
	}

	/**
	 * 初始化每页记录数的构造函数
	 * @param pageSize 每页记录数
	 */
	public Page(int pageSize) {
		this.pageSize = pageSize;
	}

	//-- 分页参数访问函数 --//
	/**
	 *  获得当前页的页号,序号从1开始,默认为1.
	 * @return 页号
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
	 * @param pageNo 页号
	 */
	public void setPageNo(final int pageNo) {
		this.pageNo = pageNo;

		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}

	/**
	 * 返回Page对象自身的setPageNo函数,可用于连续设置。
	 * @param thePageNo 页号
	 * @return 分页对象
	 */
	public Page<T> pageNo(final int thePageNo) {
		setPageNo(thePageNo);
		return this;
	}

	/**
	 * 获得每页的记录数量, 默认为-1.
	 * @return 每页的记录数量
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页的记录数量.
	 * @param pageSize 每页的记录数量
	 */
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 返回Page对象自身的setPageSize函数,可用于连续设置。
	 * @param thePageSize 每页的记录数量
	 * @return 分页对象
	 */
	public Page<T> pageSize(final int thePageSize) {
		setPageSize(thePageSize);
		return this;
	}

	/**
	 * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
	 * @return 开始记录序号
	 */
	public int getFirst() {
		return ((pageNo - 1) * pageSize) + 1;
	}
	
	public int getLast(){
		return pageNo * pageSize;
	}

	/**
	 * 获得排序字段,无默认值. 多个排序字段时用','分隔.
	 * @return 排序字段
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 设置排序字段,多个排序字段时用','分隔.
	 * @param orderBy 排序字段
	 */
	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 返回Page对象自身的setOrderBy函数,可用于连续设置。
	 * @param theOrderBy 排序字段 
	 * @return 分页对象
	 */
	public Page<T> orderBy(final String theOrderBy) {
		setOrderBy(theOrderBy);
		return this;
	}

	/**
	 * 获得排序方向, 无默认值.
	 * @return ASC/DESC
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * 设置排序方式向.
	 * 
	 * @param order 可选值为desc或asc,多个排序字段时用','分隔.
	 */
	public void setOrder(final String order) {
		String lowcaseOrder = StringUtils.lowerCase(order);

		//检查order字符串的合法值
		String[] orders = StringUtils.split(lowcaseOrder, ',');
		for (String orderStr : orders) {
			if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr)) {
				throw new IllegalArgumentException("排序方向的值: " + orderStr + " 不合法值");
			}
		}

		this.order = lowcaseOrder;
	}

	/**
	 * 返回Page对象自身的setOrder函数,可用于连续设置。
	 * @param theOrder 排序方向(ASC/DESC)
	 * @return 分页对象
	 */
	public Page<T> order(final String theOrder) {
		setOrder(theOrder);
		return this;
	}

	/**
	 * 是否已设置排序字段,无默认值.
	 * @return TRUE/FALSE
	 */
	public boolean isOrderBySetted() {
		return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
	}

	/**
	 * 获得查询对象时是否先自动执行count查询获取总记录数, 默认为false.
	 * @return TRUE/FLASE(Default:false)
	 */
	public boolean isAutoCount() {
		return autoCount;
	}

	/**
	 * 设置查询对象时是否自动先执行count查询获取总记录数.
	 * @param autoCount TRUE/FALSE
	 */
	public void setAutoCount(final boolean autoCount) {
		this.autoCount = autoCount;
	}

	/**
	 * 返回Page对象自身的setAutoCount函数,可用于连续设置。
	 * @param theAutoCount TRUE/FALSE
	 * @return 分页对象
	 */
	public Page<T> autoCount(final boolean theAutoCount) {
		setAutoCount(theAutoCount);
		return this;
	}

	//-- 访问查询结果函数 --//

	/**
	 * 获得页内的记录列表.
	 * @return 分页记录列表
	 */
	public List<T> getResult() {
		return result;
	}

	/**
	 * 设置页内的记录列表.
	 * @param result 分页记录列表
	 */
	public void setResult(final List<T> result) {
		this.result = result;
	}

	/**
	 * 获得总记录数, 默认值为-1.
	 * @return 总记录数
	 */
	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * 设置总记录数.
	 * @param totalCount 总记录数
	 */
	public void setTotalCount(final long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 根据pageSize与totalCount计算总页数, 默认值为-1.
	 * @return 总页数
	 */
	public long getTotalPages() {
		
		long count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		if (totalCount <= 0) {
			return 1;
		}

		return count;
	}

	/**
	 * 是否还有下一页.
	 * @return TRUE/FALSE
	 */
	public boolean isHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	/**
	 * 取得下页的页号, 序号从1开始.
	 * 当前页为尾页时仍返回尾页序号.
	 * @return 下页的页号
	 */
	public int getNextPage() {
		if (isHasNext()) {
			return pageNo + 1;
		} else {
			return pageNo;
		}
	}

	/**
	 * 是否还有上一页.
	 * @return TRUE/FALSE
	 */
	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}

	/**
	 * 取得上页的页号, 序号从1开始.
	 * 当前页为首页时返回首页序号.
	 * @return 上页的页号
	 */
	public int getPrePage() {
		if (isHasPre()) {
			return pageNo - 1;
		} else {
			return pageNo;
		}
	}
	
	/**
	 * 取得参数对象
	 * @return 参数对象
	 */
	public Object getParameter() {
		return parameter;
	}

	/**
	 * 设置参数对象
	 * @param parameter 参数对象
	 */
	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}

	/**
	 * JSON数据格式，分页成功返回标志(默认为：TRUE)
	 * @return TRUE/FALSE
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * JSON数据格式，分页成功返回标志设置(默认为：TRUE)
	 * @param success TRUE/FALSE
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
}