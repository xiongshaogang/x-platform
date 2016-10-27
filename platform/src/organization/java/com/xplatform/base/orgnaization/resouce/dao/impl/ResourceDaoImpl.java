package com.xplatform.base.orgnaization.resouce.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.resouce.dao.ResourceDao;
import com.xplatform.base.orgnaization.resouce.entity.ResourceEntity;
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
@Repository("resourceDao")
public class ResourceDaoImpl extends CommonDao implements ResourceDao {

	@Override
	public String addResource(ResourceEntity resource) {
		// TODO Auto-generated method stub
		return (String) this.save(resource);
	}

	@Override
	public void deleteResource(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(ResourceEntity.class, id);
	}

	@Override
	public void updateResource(ResourceEntity resource) {
		// TODO Auto-generated method stub
		this.updateEntitie(resource);
	}

	@Override
	public ResourceEntity getResource(String id) {
		// TODO Auto-generated method stub
		return (ResourceEntity) this.get(ResourceEntity.class, id);
	}

	@Override
	public List<ResourceEntity> queryResourceList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from ResourceEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, b);
	}

}
