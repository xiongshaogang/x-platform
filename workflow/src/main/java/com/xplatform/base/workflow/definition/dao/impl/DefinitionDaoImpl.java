package com.xplatform.base.workflow.definition.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.definition.dao.DefinitionDao;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
/**
 * 
 * description :岗位dao实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:23:11
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 上午11:23:11
 *
 */
@Repository("definitionDao")
public class DefinitionDaoImpl extends CommonDao implements DefinitionDao {
	
	@Override
	public String addDefinition(DefinitionEntity Definition) {
		// TODO Auto-generated method stub
		return (String) this.save(Definition);
	}

	@Override
	public void deleteDefinition(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(DefinitionEntity.class, id);
	}

	@Override
	public void updateDefinition(DefinitionEntity Definition) {
		// TODO Auto-generated method stub
		this.updateEntitie(Definition);
	}

	@Override
	public DefinitionEntity getDefinition(String id) {
		// TODO Auto-generated method stub
		return (DefinitionEntity) this.get(DefinitionEntity.class, id);
	}

	@Override
	public List<DefinitionEntity> queryDefinitionList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from DefinitionEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, b);
	}
	
	public void updateSubVersions(String parentId, String code){
		this.executeSql("UPDATE t_flow_definition SET parent_id=?,is_main=0 where code=? and id!=?", parentId,code,parentId);
	}

}
