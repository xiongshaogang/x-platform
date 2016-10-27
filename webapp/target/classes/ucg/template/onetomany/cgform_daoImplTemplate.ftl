package ${bussiPackage}.${entityPackage}.dao.impl;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import ${bussiPackage}.${entityPackage}.dao.${entityName}Dao;
import ${bussiPackage}.${entityPackage}.entity.${entityName}Entity;


@Repository("${entityName?uncap_first}Dao")
public class ${entityName}DaoImpl extends CommonDao implements ${entityName}Dao {
    @Override
	public String add${entityName}(${entityName}Entity ${entityName}) {
		// TODO Auto-generated method stub
		return (String) this.save(${entityName});
	}

	@Override
	public void delete${entityName}(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(${entityName}Entity.class, id);
	}

	@Override
	public void update${entityName}(${entityName}Entity ${entityName}) {
		// TODO Auto-generated method stub
		this.updateEntitie(${entityName});
	}

	@Override
	public ${entityName}Entity get${entityName}(String id) {
		// TODO Auto-generated method stub
		return (${entityName}Entity) this.get(${entityName}Entity.class, id);
	}

	@Override
	public List<${entityName}Entity> query${entityName}List() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from ${entityName}Entity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}
}
