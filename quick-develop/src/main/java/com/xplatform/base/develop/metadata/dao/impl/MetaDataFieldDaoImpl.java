package com.xplatform.base.develop.metadata.dao.impl;

import org.springframework.stereotype.Repository;

import java.util.List;

import com.xplatform.base.develop.metadata.dao.MetaDataFieldDao;
import com.xplatform.base.develop.metadata.entity.MetaDataFieldEntity;
import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;


@Repository("metaDataFieldDao")
public class MetaDataFieldDaoImpl extends CommonDao implements MetaDataFieldDao {
    @Override
	public String addFormField(MetaDataFieldEntity FormField) {
		// TODO Auto-generated method stub
		return (String) this.save(FormField);
	}

	@Override
	public void deleteFormField(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(MetaDataFieldEntity.class, id);
	}

	@Override
	public void updateFormField(MetaDataFieldEntity FormField) {
		// TODO Auto-generated method stub
		this.updateEntitie(FormField);
	}

	@Override
	public MetaDataFieldEntity getFormField(String id) {
		// TODO Auto-generated method stub
		return (MetaDataFieldEntity) this.get(MetaDataFieldEntity.class, id);
	}

	@Override
	public List<MetaDataFieldEntity> queryFormFieldList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from TFormFieldEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}
}
