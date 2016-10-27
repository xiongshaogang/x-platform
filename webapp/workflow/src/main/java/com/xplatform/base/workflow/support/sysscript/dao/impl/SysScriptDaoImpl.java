package com.xplatform.base.workflow.support.sysscript.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.workflow.support.sysscript.dao.SysScriptDao;
import com.xplatform.base.workflow.support.sysscript.entity.SysScriptEntity;

/**
 * 
 * description :
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
@Repository("sysScriptDao")
public class SysScriptDaoImpl extends CommonDao implements SysScriptDao {

	@Override
	public String add(SysScriptEntity entity) {
		// TODO Auto-generated method stub
		return (String) this.save(entity);
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(SysScriptEntity.class, id);
	}

	@Override
	public void update(SysScriptEntity entity) {
		// TODO Auto-generated method stub
		this.updateEntitie(entity);
	}

	@Override
	public SysScriptEntity get(String id) {
		// TODO Auto-generated method stub
		return (SysScriptEntity) this.get(SysScriptEntity.class, id);
	}

	public List<SysScriptEntity> queryList() {
		String hql = "FROM SysScriptEntity";
		return this.findByQueryString(hql);
	}
}
