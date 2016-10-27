package com.xplatform.base.orgnaization.orgnaization.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.orgnaization.dao.OrgRoleDao;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgRoleEntity;

@SuppressWarnings("unchecked")
@Repository("orgRoleDao")
public class OrgRoleDaoImpl extends CommonDao implements OrgRoleDao {

	@Override
	public String addOrgRole(OrgRoleEntity orgRole) {
		return (String) this.save(orgRole);
	}

	@Override
	public void deleteOrgRole(String id) {
		this.deleteEntityById(OrgRoleEntity.class, id);
	}

	@Override
	public void updateOrgRole(OrgRoleEntity orgRole) {
		this.updateEntitie(orgRole);
	}

	@Override
	public OrgRoleEntity getOrgRole(String id) {
		return (OrgRoleEntity) this.get(OrgRoleEntity.class, id);
	}

	@Override
	public List<OrgRoleEntity> queryOrgRoleList() {
		return this.findByQueryString("from OrgRoleEntity");
	}
	
	@Override
	public OrgRoleEntity getOrgRole(String roleId, String orgId){
		String hql = " from OrgRoleEntity where role_id = ? and org_id = ? ";
		return (OrgRoleEntity) this.findHql(hql, new Object[]{roleId, orgId}).get(0);
	}
	
	@Override
	public List<OrgRoleEntity> queryOrgRoleByOrgIdList(String orgId) {
		String hql = " from OrgRoleEntity where org_id = ? ";
		return this.findHql(hql, new Object[]{orgId});
	}
	
	@Override
	public List<OrgRoleEntity> queryOrgRolesByRoleId(String roleId) {
		String hql = " from OrgRoleEntity where role_id = ? ";
		return this.findHql(hql, new Object[]{roleId});
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		this.getDataGridReturn(cq, false);
	}

}
