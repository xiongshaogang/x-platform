package com.xplatform.base.orgnaization.role.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.role.dao.RoleDao;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
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
@Repository("roleDao")
public class RoleDaoImpl extends CommonDao implements RoleDao {

	@Override
	public String addRole(RoleEntity role) {
		// TODO Auto-generated method stub
		return (String) this.save(role);
	}

	@Override
	public void deleteRole(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(RoleEntity.class, id);
	}

	@Override
	public void updateRole(RoleEntity role) {
		// TODO Auto-generated method stub
		this.updateEntitie(role);
	}

	@Override
	public RoleEntity getRole(String id) {
		// TODO Auto-generated method stub
		return (RoleEntity) this.get(RoleEntity.class, id);
	}

	@Override
	public List<RoleEntity> queryRoleListByAuthority(String userId){
		// TODO Auto-generated method stub
		return this.findByQueryString("from RoleEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
