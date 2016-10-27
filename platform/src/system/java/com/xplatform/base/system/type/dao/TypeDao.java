package com.xplatform.base.system.type.dao;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.system.type.entity.TypeEntity;

public interface TypeDao extends ICommonDao {

	/**
	 * @author luoheng
	 * 保存系统分类
	 * @param catalogue
	 * @return
	 */
	public String saveType(TypeEntity type);
	
	/**
	 * 修改系统分类
	 * @author luoheng
	 * @param catalogue
	 */
	public void updateType(TypeEntity type);
	
	/**
	 * 删除系统分类
	 * @author luoheng
	 * @param catalogue
	 */
	public void deleteType(String id);
	
	/**
	 * 根据ID获取系统分类信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	public TypeEntity getType(String id);
	
	/**
	 * 查询所有系统分类信息
	 * @author luoheng
	 * @return
	 */
	public List<TypeEntity> queryTypeList();
	
	/**
	 * @author luoheng
	 * 根据类型查询系统类型树
	 * @param param(sysType, parentId)
	 * @return
	 */
	public List<TypeEntity> queryTypeByTree(Map<String, String> param);
}
