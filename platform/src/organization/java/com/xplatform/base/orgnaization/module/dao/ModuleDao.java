package com.xplatform.base.orgnaization.module.dao;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.module.entity.ModuleEntity;

/**
 * 
 * description : 部门管理dao
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:08:58
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 上午11:08:58
 *
 */
public interface ModuleDao extends ICommonDao{
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午9:56:38
	 * @Decription 新增
	 *
	 * @param Module
	 * @return
	 */
	public String addModule(ModuleEntity module);
	
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午9:57:06
	 * @Decription 删除
	 * @param id
	 * @return
	 */
	public void deleteModule(String id);
	
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午9:56:56
	 * @Decription 修改
	 * @param Module
	 * @return
	 */
	public void updateModule (ModuleEntity module);
	
	
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:08:19
	 * @Decription 通过id查询单条记录
	 * @param id
	 * @return
	 */
	public ModuleEntity getModule(String module);
	
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午9:57:48
	 * @Decription 查询所有的记录
	 * @return
	 */
	public List<ModuleEntity> queryModuleList();
	
	/**
	 * 根据用户ID删除用户所属权限
	 * @author luoheng
	 * @param userId
	 */
	public void deleteUserModuleByUserId(String userId);
	
}
