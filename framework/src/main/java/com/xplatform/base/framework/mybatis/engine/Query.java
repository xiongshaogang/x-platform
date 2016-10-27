package com.xplatform.base.framework.mybatis.engine;

import java.util.List;

import com.xplatform.base.framework.mybatis.entity.Page;


/**
 * 
 * <STRONG>查询接口</STRONG> :
 * <p>
 * 
 * @version 1.0
 *          <p>
 * @author mengfx@huilan.com
 *         <p>
 * 
 * <STRONG>创建时间</STRONG> : Sep 3, 2012 9:17:05 AM
 * <p>
 * <STRONG>修改历史</STRONG> :
 * <p>
 * 
 * <pre>
 * 修改人                   	修改时间                     				修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Sep 3, 2012 9:17:05 AM
 * </pre>
 */
public interface Query<T extends Query<?, ?>, U extends Object> {

	/**
	 * 
	 * <pre>
	 * 查询单个对象
	 * </pre>
	 * @return
	 */
	U singleResult();
	
	T asc();

	T desc();

	/**
	 * 
	 * <pre>
	 * 统计查询数量
	 * </pre>
	 * @return
	 */
	long count();

	/**
	 * 
	 * <pre>
	 * 查询列表
	 * </pre>
	 * @return
	 */
	List<U> list();

	/**
	 * 
	 * <pre>
	 * 分页查询
	 * </pre>
	 * @return
	 */
	Page<U> listPage();
}
