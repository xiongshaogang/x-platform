package com.xplatform.base.system.category.dao;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.system.category.entity.CategoryEntity;

public interface CategoryDao extends ICommonDao {

	/**
	 * @author luoheng
	 * 保存系统分类
	 * @param catalogue
	 * @return
	 */
	public String saveCategory(CategoryEntity category);
	
	/**
	 * 修改系统分类
	 * @author luoheng
	 * @param catalogue
	 */
	public void updateCategory(CategoryEntity category);
	
	/**
	 * 删除系统分类
	 * @author luoheng
	 * @param catalogue
	 */
	public void deleteCategory(String id);
	
	/**
	 * 根据ID获取系统分类信息
	 * @author luoheng
	 * @param id
	 * @return
	 */
	public CategoryEntity getCategory(String id);
	
	/**
	 * 查询所有系统分类信息
	 * @author luoheng
	 * @return
	 */
	public List<CategoryEntity> queryCategoryList();
	
	/**
	 * @author luoheng
	 * 根据类型查询系统类型树
	 * @param param(sysType, parentId)
	 * @return
	 */
	public List<CategoryEntity> queryCategoryByTree(Map<String, String> param);
}