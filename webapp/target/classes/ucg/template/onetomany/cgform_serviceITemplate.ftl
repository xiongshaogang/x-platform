package ${bussiPackage}.${entityPackage}.service;

import java.util.List;
import java.util.Map;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import ${bussiPackage}.${entityPackage}.entity.${entityName}Entity;

public interface ${entityName}Service{
	
 	/**
	 * 新增${ftl_description}
	 */
	public String save(${entityName}Entity ${entityName?uncap_first}) throws Exception;
	
	/**
	 * 删除${ftl_description}
	 */
	public void delete(String id) throws Exception;
	
	/**
	 * 批量删除${ftl_description}
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新${ftl_description}
	 */
	public void update(${entityName}Entity ${entityName?uncap_first}) throws Exception;
	
	/**
	 * 查询一条${ftl_description}记录
	 */
	public ${entityName}Entity get(String id);
	
	/**
	 * 查询${ftl_description}列表
	 */
	public List<${entityName}Entity> queryList();
	
	/**
	 * hibernate${ftl_description}分页列表
	 */
    public void getDataGridReturn(CriteriaQuery cq, boolean b);
	
	/**
	 * 判断字段记录是否唯一
	 */
	public boolean isUnique(Map<String,String> param,String propertyName);
 	
}
