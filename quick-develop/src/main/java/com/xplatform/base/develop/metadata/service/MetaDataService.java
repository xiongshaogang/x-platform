package com.xplatform.base.develop.metadata.service;

import java.util.List;

import com.xplatform.base.develop.metadata.entity.MetaDataEntity;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.common.service.CommonService;

public interface MetaDataService extends CommonService{

	public DataGridReturn getDataGridReturn(CriteriaQuery cq, boolean b);

	public MetaDataEntity getEntity(Class<MetaDataEntity> class1, String id);

	public void saveOrUpdate(MetaDataEntity formHead);

	public void save(MetaDataEntity formHead);

	public void update(MetaDataEntity formHead);

	public boolean dbSynch(MetaDataEntity MetaData, String synMethod) throws BusinessException;

	public boolean appendSubTableStr4Main(MetaDataEntity MetaData);

	public void saveTable(MetaDataEntity MetaData, String string);

	public boolean judgeTableIsExit(String name);

	public void deleteMetaData(MetaDataEntity MetaData);

	public boolean removeSubTableStr4Main(MetaDataEntity MetaData);

	public List<MetaDataEntity> getMetaDataList();

	public String getTableName(String id);

}
