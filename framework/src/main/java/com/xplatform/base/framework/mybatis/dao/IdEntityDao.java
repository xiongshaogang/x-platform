package com.xplatform.base.framework.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.mybatis.entity.IdEntity;

/**
 * description :获取主键id
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月19日 下午3:53:13
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月19日 下午3:53:13
 *
 */
public interface IdEntityDao{
	
	String getId(Map param);
	
	void insertIdGenerator(IdEntity idEntity);
	
	void updateIdGenerator(IdEntity idEntity);
	
	void deleteIdGenerator(String name);
	
	String selectDbSchemaVersion();
	
	IdEntity selectIdGenerator(String name);
	
	List<IdEntity> selectIdGenerators();
	
	public void updateSequence(String seqName);
}