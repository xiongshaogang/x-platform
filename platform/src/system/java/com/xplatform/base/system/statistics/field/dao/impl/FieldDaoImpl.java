package com.xplatform.base.system.statistics.field.dao.impl;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.statistics.field.dao.FieldDao;
import com.xplatform.base.system.statistics.field.entity.FieldEntity;


@Repository("fieldDao")
public class FieldDaoImpl extends CommonDao implements FieldDao {
    @Override
	public String addField(FieldEntity Field) {
		// TODO Auto-generated method stub
		return (String) this.save(Field);
	}

	@Override
	public void deleteField(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(FieldEntity.class, id);
	}

	@Override
	public void updateField(FieldEntity Field) {
		// TODO Auto-generated method stub
		this.updateEntitie(Field);
	}

	@Override
	public FieldEntity getField(String id) {
		// TODO Auto-generated method stub
		return (FieldEntity) this.get(FieldEntity.class, id);
	}

	@Override
	public List<FieldEntity> queryFieldList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from FieldEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

	@Override
	public List<FieldEntity> queryByDatasourceIdList(String datasourceId) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		map.put("datasourceId", datasourceId);
		return this.findByPropertys(FieldEntity.class, map);
	}
}
