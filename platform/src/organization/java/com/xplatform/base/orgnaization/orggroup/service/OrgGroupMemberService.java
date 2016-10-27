package com.xplatform.base.orgnaization.orggroup.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.orgnaization.orggroup.entity.OrgGroupMemberEntity;

public interface OrgGroupMemberService extends BaseService<OrgGroupMemberEntity> {

	/**
	 * 添加一个群组下的成员
	 * 
	 * @param orgGroupMember
	 * @throws BusinessException
	 */
	public String saveOrgGroMem(OrgGroupMemberEntity orgGroupMember, String phones) throws BusinessException;

	/**
	 * 更新一个群组下的成员
	 * 
	 * @param orgGroupMember
	 * @throws BusinessException
	 */
	public void updateOrgGroMem(OrgGroupMemberEntity orgGroupMember) throws Exception;

	/**
	 * 删除一个群组下所有成员
	 * 
	 * @param groupId
	 * @throws BusinessException
	 */
	public void deleteOrgGroMem(String groupId) throws BusinessException;

	/**
	 * 查群一个群组下所有成员
	 * 
	 * @param groupId
	 * @throws BusinessException
	 */
	public List<Map<String, Object>> queryGroupUsers(String groupId);

	/**
	 * 查询群的头像(最多4个)
	 * 
	 * @param groupId
	 * @return
	 */
	public List<Map<String, Object>> queryGroupPortrait(String groupId);
	
	/**
	 * 添加一个群组下的成员
	 * 
	 * @param orgGroupMember
	 * @throws BusinessException
	 */
	public OrgGroupMemberEntity getOrgGroMem(String groupId, String userId) throws BusinessException;
}
