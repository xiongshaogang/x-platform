package com.xplatform.base.system.category.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.system.category.dao.CategoryDao;
import com.xplatform.base.system.category.entity.CategoryEntity;

@Repository("categoryDao")
public class CategoryDaoImpl extends CommonDao implements CategoryDao{

	@Override
	public String saveCategory(CategoryEntity category) {
		return (String) this.save(category);
	}

	@Override
	public void updateCategory(CategoryEntity category) {
		this.updateEntitie(category);
	}

	@Override
	public void deleteCategory(String id) {
		this.deleteEntityById(CategoryEntity.class, id);		
	}

	@Override
	public CategoryEntity getCategory(String id) {
		return (CategoryEntity) this.get(CategoryEntity.class, id);
	}

	@Override
	public List<CategoryEntity> queryCategoryList() {
		return this.findByQueryString("from CategoryEntity");
	}

	@Override
	public List<CategoryEntity> queryCategoryByTree(Map<String, String> param) {
		String hql = "from CategoryEntity where category = ? and parentId = ?";
		List<CategoryEntity> categoryList = this.findHql(hql, new Object[]{param.get("sysType"), param.get("parentId")});
		return categoryList;
	}

}
