package com.xplatform.base.system.dict.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.system.dict.entity.DictTypeEntity;
import com.xplatform.base.system.dict.entity.DictValueEntity;

/**
 * 
 * description : 字典值管理service
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:34:52
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 上午11:34:52
 *
 */
public interface DictValueService extends BaseService<DictValueEntity>{

	/**
	 * 新增字典值
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param job
	 * @return
	 */
	public String save(DictValueEntity DictValue) throws BusinessException;

	/**
	 * 删除字典值
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException;

	/**
	 * 更新字典值
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param job
	 * @return
	 */
	public void update(DictValueEntity DictValue) throws BusinessException;

	

	/**
	 * 通过属性查询字典值列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<DictValueEntity> queryListByPorpertys(Map<String, String> param);

	

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月25日 下午4:29:33
	 * @Decription 通过type的code查找type下的值
	 *
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public List<DictValueEntity> findValuesByType(String code);

	public List<DictValueEntity> queryListByTypeidAndParentid(Map<String, String> param);

	/**
	 * @author xiaqiang
	 * @createtime 2014年10月5日 上午10:31:10
	 * @Decription 判断同一type下的value是否唯一
	 *
	 * @param typeId
	 * @param value
	 * @return
	 * @throws BusinessException
	 */
	public Boolean isValueUnique(String typeId, String value);
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年11月28日 下午10:13:24
	 * @Decription 通过value值查找DictValueEntity集合
	 *
	 * @param value
	 * @return
	 * @throws BusinessException
	 */
	public List<DictValueEntity> findValuesByValue(String value) throws BusinessException;
}
