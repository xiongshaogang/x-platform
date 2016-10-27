package com.xplatform.base.system.type.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.system.type.dao.TypeDao;
import com.xplatform.base.system.type.entity.TypeEntity;

@Repository("typeDao")
public class TypeDaoImpl extends CommonDao implements TypeDao {

	/**
	 * @author luoheng
	 * 保存系统分类
	 * @param type
	 * @return
	 */
	@Override
	public String saveType(TypeEntity type){
		return (String) this.save(type);
	}
	
	/**
	 * 修改系统分类
	 * @author luoheng
	 * @param type
	 */
	@Override
	public void updateType(TypeEntity type){
		this.updateEntitie(type);
	}
	
	/**
	 * 删除系统分类
	 * @author luoheng
	 * @param catalogue
	 */
	@Override
	public void deleteType(String id){
		this.deleteEntityById(TypeEntity.class, id);
	}
	
	/**
	 * 根据ID获取系统分类信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	@Override
	public TypeEntity getType(String id){
		return (TypeEntity) this.get(TypeEntity.class, id);
	}
	
	/**
	 * 查询所有系统分类信息
	 * @author luoheng
	 * @return
	 */
	@Override
	public List<TypeEntity> queryTypeList(){
		return this.findByQueryString("from TypeEntity");
	}
	
	/**
	 * @author luoheng
	 * 根据类型查询系统类型树
	 * @param param(sysType, parentId)
	 * @return
	 */
	public List<TypeEntity> queryTypeByTree(Map<String, String> param){
		String hql = "from TypeEntity where type = ? and parent_id = ?";
		List<TypeEntity> typeList = this.findHql(hql, new Object[]{param.get("sysType"), param.get("parentId")});
		return typeList;
	}
}
