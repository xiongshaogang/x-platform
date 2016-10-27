package ${bussiPackage}.${entityPackage}.dao;

import java.util.List;
import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import ${bussiPackage}.${entityPackage}.entity.${entityName}Entity;

public interface ${entityName}Dao extends ICommonDao {
/**
	 * @Decription 新增
	 */
	public String add${entityName}(${entityName}Entity ${entityName?uncap_first});
	
	/**
	 * @Decription 删除
	 */
	public void delete${entityName}(String id);
	
	/**
	 * 
	 * @Decription 修改
	 */
	public void update${entityName} (${entityName}Entity ${entityName?uncap_first});
	
	
	/**
	 * 
	 * @Decription 通过id查询单条记录
	 */
	public ${entityName}Entity get${entityName}(String id);
	
	/**
	 * 
	 * @Decription 查询所有的记录
	 */
	public List<${entityName}Entity> query${entityName}List();
	
}
