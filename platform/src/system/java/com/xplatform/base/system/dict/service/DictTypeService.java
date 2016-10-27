package com.xplatform.base.system.dict.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.ComboBox;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.system.dict.entity.DictTypeEntity;
import com.xplatform.base.system.dict.entity.DictValueEntity;

/**
 * 
 * description : 字典类型管理service
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
public interface DictTypeService extends BaseService<DictTypeEntity>{

	/**
	 * 初始化数据字典缓存
	 * @author xiehs
	 * @createtime 2014年6月10日 上午9:01:08
	 * @Decription
	 *
	 */
	public void initCache();

	/**
	 * 通过类型code查询缓存中的数据字典
	 * @author xiehs
	 * @createtime 2014年6月10日 上午9:03:35
	 * @Decription
	 *
	 * @param dictCode
	 * @return
	 */
	public <T> T findCacheByCode(String dictCode);

	/**
	 * @author xiaqiang
	 * @createtime 2014年6月16日 上午11:09:08
	 * @Decription 通过字典typeCode查询字典值List
	 *
	 * @param dictCode
	 * @return
	 * @throws BusinessException
	 */

	public List<DictValueEntity> findDictValueEntityListByCode(String dictCode);

	/**
	 * @author xiaqiang
	 * @createtime 2014年6月16日 上午11:19:05
	 * @Decription 通过字典typeCode获得转换成ComboBox后的列表
	 *
	 * @param dictCode
	 * @return
	 * @throws BusinessException
	 */

	public List<ComboBox> transformToComboBox(String dictCode);

	/**
	 * @author xiaqiang
	 * @createtime 2014年6月16日 上午11:23:16
	 * @Decription 通过字典Code获得对应的字典类型(树结构还是List集合结构)
	 *
	 * @param dictCode
	 * @return
	 * @throws BusinessException
	 */

	public String findDictTypeByCode(String dictCode) ;
}
