package com.xplatform.base.orgnaization.module.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.module.dao.ModuleDao;
import com.xplatform.base.orgnaization.module.entity.ModuleEntity;
/**
 * 
 * description :模块dao实现
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
@Repository("moduleDao")
public class ModuleDaoImpl extends CommonDao implements ModuleDao {

	@Override
	public String addModule(ModuleEntity module) {
		return (String) this.save(module);
	}

	@Override
	public void deleteModule(String id) {
		this.deleteEntityById(ModuleEntity.class, id);
	}

	@Override
	public void updateModule(ModuleEntity module) {
		this.updateEntitie(module);
	}

	@Override
	public ModuleEntity getModule(String id) {
		return (ModuleEntity) this.get(ModuleEntity.class, id);
	}

	@Override
	public List<ModuleEntity> queryModuleList() {
		return this.findByQueryString("from ModuleEntity");
	}
	
	/**
	 * 根据用户ID删除用户所属权限
	 * @author luoheng
	 * @param userId
	 */
	@Override
	public void deleteUserModuleByUserId(String userId){
		String hql = "delete from UserModuleEntity ur where ur.user.id = :userId";
		Query queryObject = this.getSession().createQuery(hql);
		queryObject.setParameter("userId", userId);
		queryObject.executeUpdate();
	}

}
