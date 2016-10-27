/**
 * 2012-4-19
 */
package com.xplatform.base.framework.mybatis.dao;

import java.io.Serializable;
import java.util.List;

/**
 * description :mybaits预定义增删改查操作
 * @param <T> DAO操作的对象类型
 * @param <PK> 主键类型
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月17日 下午2:35:23
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月17日 下午2:35:23
 *
 */
public interface SimpleSqlDao<T, PK extends Serializable> {
	
	/**
	 * 新增
	 * <pre>
	 * </pre>
	 * @param entity
	 */
	void save(T entity); 
	/**
	 * 更新
	 * <pre>
	 * </pre>
	 * @param entity
	 */
	void update(T entity);
	/**
	 * 删除
	 * <pre>
	 * </pre>
	 * @param pk
	 */
	void delete(PK pk); 
	/**
	 * 根据主键取当前泛型唯一对象
	 * 
	 * @param id 主键
	 * @return 对象
	 */
	T get(final PK id);
	
	/**
	 * 根据条件取当前泛型唯一对象
	 * 
	 * @param object 条件参数对象
	 * @return 对象
	 */
	T findUnique(final Object object);
	
	/**
	 * 取当前泛型对象所有数据
	 * 
	 * @return 对象集合
	 */
	List<T> getAll();
	
	/**
	 * 根据条件取当前泛型对象集合
	 * 
	 * @param object 条件参数对象
	 * @return 泛型对象集合
	 */
	List<T> find(final Object object);
	
	
	/**
	 * 判断对象的属性值在数据库内是否唯一.
	 * 
	 * 在修改对象的情景下,如果属性新修改的值(value)等于属性原来的值(orgValue)则不作比较.
	 *  newValue 新属性值
	 *  oldValue 旧属性值
	 * @return 数据库中与新属性值相同属性值的个数
	 */
	public int isPropertyUnique(final Object object);
}