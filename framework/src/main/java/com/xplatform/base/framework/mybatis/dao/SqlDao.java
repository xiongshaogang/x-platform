/**
 * 2012-4-19
 */
package com.xplatform.base.framework.mybatis.dao;

import java.io.Serializable;
import java.util.List;

import com.xplatform.base.framework.mybatis.entity.Page;

/**
 * description :分页操作dao
 * 
 * @param <T> DAO操作的对象类型
 * @param <PK> 主键类型
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月19日 下午3:53:44
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月19日 下午3:53:44
 *
 */
public interface SqlDao<T, PK extends Serializable> extends SimpleSqlDao<T, PK> {
	
	/**
	 * 根据分页条件取当前泛型对象集合
	 * 
	 * @param page 分页对象
	 * @return 泛型对象集合
	 */
	List<T> queryAllByPage(Page<T> page);
	
}
