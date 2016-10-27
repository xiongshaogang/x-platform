package com.xplatform.base.framework.mybatis.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.mybatis.dao.IdEntityDao;
import com.xplatform.base.framework.mybatis.entity.IdEntity;

/**
 * 
 * description :mybatis生成idservice
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月17日 下午12:52:08
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月17日 下午12:52:08
 *
 */
@Component
public  class IdEntityService{
	
	protected IdEntityDao idEntityDao;

	@Autowired
	protected void setIdEntityDao(IdEntityDao idEntityDao) {
		this.idEntityDao = idEntityDao;
	}
	/**
	 * 获取主键
	 * <pre>
	 * </pre>
	 * @return
	 */
	public String getPrimaryKey(Map param){
		return this.idEntityDao.getId(param);
	}
	
	public IdEntity findPropertyById(String id){
		return this.idEntityDao.selectIdGenerator(id);
	}
	
	public void updateIdGenerator(IdEntity idEntity){
		this.idEntityDao.updateIdGenerator(idEntity);
	}
	
	public void updateSequence(String seqName){
		this.idEntityDao.updateSequence(seqName);
	}
}
