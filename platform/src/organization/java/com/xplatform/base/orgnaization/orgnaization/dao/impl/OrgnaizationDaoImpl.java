package com.xplatform.base.orgnaization.orgnaization.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.orgnaization.orgnaization.dao.OrgnaizationDao;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;

@SuppressWarnings("unchecked")
@Repository("orgnaizationDao")
public class OrgnaizationDaoImpl extends CommonDao implements OrgnaizationDao {

	@Override
	public String addOrg(OrgnaizationEntity org) {
		
		return (String) this.save(org);
	}

	@Override
	public void deleteOrg(String id) {
		this.deleteEntityById(OrgnaizationEntity.class, id);
	}

	@Override
	public void updateOrg(OrgnaizationEntity dept) {
		this.updateEntitie(dept);
	}

	@Override
	public OrgnaizationEntity getOrg(String id) {
		return (OrgnaizationEntity) this.get(OrgnaizationEntity.class, id);
	}

	@Override
	public List<OrgnaizationEntity> queryOrgList() {
		return this.findByQueryString("from OrgnaizationEntity");
	}
	
	@Override
	public List<OrgnaizationEntity> queryOrgList(String params) {
		String hql = "from OrgnaizationEntity where institutionId = ?";
		List<OrgnaizationEntity> orgnaizationList = this.findHql(hql, params);
		return orgnaizationList;
	}

}
