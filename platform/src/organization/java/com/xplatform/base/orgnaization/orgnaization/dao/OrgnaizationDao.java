package com.xplatform.base.orgnaization.orgnaization.dao;

import java.util.List;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;

/**
 * 
 * description :  组织机构管理 dao
 *
 * @version 1.0
 * @author hexj
 * @createtime : 2014年12月9日 上午11:50:04
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * hexj        2014年12月9日 上午11:50:04
 *
 */
public interface OrgnaizationDao extends ICommonDao{
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月9日 上午9:56:38
	 * @Decription 新增
	 * @param org
	 * @return
	 */
	public String addOrg(OrgnaizationEntity org);
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月9日 上午9:57:06
	 * @Decription 删除
	 * @param id
	 * @return
	 */
	public void deleteOrg(String id);
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月9日 上午9:56:56
	 * @Decription 修改
	 * @param org
	 * @return
	 */
	public void updateOrg (OrgnaizationEntity org);
	
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月9日上午11:08:19
	 * @Decription 通过id查询单条记录
	 * @param id
	 * @return
	 */
	public OrgnaizationEntity getOrg(String orgId);
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月9日 上午9:57:48
	 * @Decription 查询所有的记录
	 * @return
	 */
	public List<OrgnaizationEntity> queryOrgList(String params);
	public List<OrgnaizationEntity> queryOrgList();
	
	
	
}
