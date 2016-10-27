package com.xplatform.base.workflow.support.sysscript.dao;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.workflow.support.sysscript.entity.SysScriptEntity;

/**
 * description : 脚本管理DAO
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月2日 下午5:11:32
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年7月2日 下午5:11:32
 *
*/

public interface SysScriptDao extends ICommonDao {
	public String add(SysScriptEntity entity);

	public void delete(String id);

	public void update(SysScriptEntity SysScriptEntity);

	public SysScriptEntity get(String id);

	public List<SysScriptEntity> queryList();

}
