package com.xplatform.base.orgnaization.orggroup.service;

import java.io.InputStream;
import java.io.OutputStream;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.orgnaization.orggroup.entity.GroupShareEntity;

public interface GroupShareService extends BaseService<GroupShareEntity>{

	/**
	 * 创基一个新的分享群二维码对象
	 * @param groupShare
	 * @return
	 * @throws BusinessException
	 */
	public AjaxJson saveGroupShare(GroupShareEntity groupShare) throws BusinessException; 
	
	/**
	 * 修改群组二维码
	 * @param groupShare
	 * @return
	 * @throws BusinessException
	 */
	public AjaxJson updateGroupShare(GroupShareEntity groupShare) throws BusinessException; 
	
	/**
	 * 删除群组二维码
	 * @param groupShare
	 * @return
	 * @throws BusinessException
	 */
	public AjaxJson deleteGroupShare(String id) throws BusinessException; 
	
	/**
	 * 查询群组二维码
	 * @param groupShare
	 * @return
	 * @throws BusinessException
	 */
	public AjaxJson doQueryGroupShare(String groupid,String serverHost) throws Exception; 
	
	/**
	 * 查询群组二维码
	 * @param groupShare
	 * @return
	 * @throws BusinessException
	 */
	public GroupShareEntity queryGroupShare(String groupid,String randomCode) throws Exception; 
	
}
