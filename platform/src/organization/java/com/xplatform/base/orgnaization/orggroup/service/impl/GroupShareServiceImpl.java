package com.xplatform.base.orgnaization.orggroup.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.orgnaization.orggroup.dao.GroupShareDao;
import com.xplatform.base.orgnaization.orggroup.entity.GroupShareEntity;
import com.xplatform.base.orgnaization.orggroup.service.GroupShareService;
import com.xplatform.base.platform.common.def.ConfigConst;


@Service("groupShareService")
public class GroupShareServiceImpl extends BaseServiceImpl<GroupShareEntity> implements GroupShareService {

	@Resource
	private GroupShareDao groupShareDao;
	
	//生成二维码的连接
	private String shareQRCodeRUL="http://qr.topscan.com/api.php?text=";
	
	@Resource
	public void setBaseDao(GroupShareDao groupShareDao) {
		super.setBaseDao(groupShareDao);
	}

	@Override
	public AjaxJson saveGroupShare(GroupShareEntity groupShare) throws BusinessException {
		// TODO Auto-generated method stub
		AjaxJson ajaxResult = new AjaxJson();
		Date today = new Date();
		groupShare.setId(UUIDGenerator.generate());
		groupShare.setRandomCode(StringUtil.random(6));
		groupShare.setExpireTime(new Date(today.getTime() + (long)7*24*60*60*1000));
		this.groupShareDao.save(groupShare);
		ajaxResult.setMsg("成功创建群组二维码");
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}

	@Override
	public AjaxJson updateGroupShare(GroupShareEntity groupShare) throws BusinessException {
		AjaxJson ajaxResult = new AjaxJson();
		GroupShareEntity oldEntity = this.groupShareDao.getEntity(groupShare.getId());
		
		MyBeanUtils.copyBeanNotNull2Bean(groupShare, oldEntity);
		this.groupShareDao.updateEntitie(oldEntity);
		ajaxResult.setMsg("成功更新群组二维码");
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}

	@Override
	public AjaxJson deleteGroupShare(String groupId) throws BusinessException {
		AjaxJson ajaxResult = new AjaxJson();
		this.groupShareDao.executeSql("DELETE FROM t_org_group_share WHERE groupId=?", groupId);
		ajaxResult.setMsg("成功删除群组二维码");
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}

	@Override
	public AjaxJson doQueryGroupShare(String orgId,String serverHost) throws Exception {
		AjaxJson ajaxResult = new AjaxJson();
		String sql = "from GroupShareEntity where orgId = ?";
		GroupShareEntity groupShare = this.groupShareDao.findUniqueByHql(sql, orgId);
		Map<String, Object> map = new HashMap<String, Object>();
		String domainName = "";
		if(ConfigConst.DOMAIN_NAME.equals(serverHost)){
			domainName = serverHost;
		}else{
			domainName = ConfigConst.serverUrl;
		}
		if (groupShare != null) {
			// DateFormat dateFormat = new
			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date today = new Date();
			// Date expireTime =
			// dateFormat.parse(groupShare.getExpireTime().toString());
			if (today.after(groupShare.getExpireTime())) {
				Date TempToday = new Date();
				groupShare.setRandomCode(StringUtil.random(6));
				groupShare.setExpireTime(new Date(TempToday.getTime() + (long) 7 * 24 * 60 * 60 * 1000));
				this.update(groupShare);
				// ajaxResult.setObj(groupShare);ConfigConst.serverUrl
				String content = domainName + "/userOrgController.do?saveUserOrgs&orgId=" + groupShare.getOrgId() + "&orgIds="+ groupShare.getOrgId()+"&type=orgUser&randomCode="
						+ groupShare.getRandomCode();
				// String URL =
				// "orgGroupController.do?getRQCode&groupId="+groupid;
				// map.put("url", URL);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				map.put("expireTime", formatter.format(groupShare.getExpireTime()));
				ajaxResult.setObj(map);
				ajaxResult.setStatus(content);
				ajaxResult.setMsg("成功获取群组二维码");
				ajaxResult.setSuccess(true);
			} else {
				String content = domainName + "/userOrgController.do?saveUserOrgs&orgId=" + groupShare.getOrgId() + "&orgIds="+ groupShare.getOrgId()+"&type=orgUser&randomCode="
						+ groupShare.getRandomCode();
				// String URL =
				// "orgGroupController.do?getRQCode&groupId="+groupid;
				// map.put("url", URL);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				map.put("expireTime", formatter.format(groupShare.getExpireTime()));
				ajaxResult.setObj(map);
				ajaxResult.setStatus(content);
				ajaxResult.setMsg("成功获取群组二维码");
				ajaxResult.setSuccess(true);
			}
		} else {
			GroupShareEntity groupShareEntity = new GroupShareEntity();

			Date today = new Date();
			groupShareEntity.setId(UUIDGenerator.generate());
			groupShareEntity.setOrgId(orgId);
			groupShareEntity.setRandomCode(StringUtil.random(6));
			groupShareEntity.setExpireTime(new Date(today.getTime() + (long) 7 * 24 * 60 * 60 * 1000));
			this.groupShareDao.save(groupShareEntity);

			String content = domainName + "/userOrgController.do?saveUserOrgs&orgId=" + groupShareEntity.getOrgId() + "&orgIds="+ groupShareEntity.getOrgId()+"&type=orgUser&randomCode="
					+ groupShareEntity.getRandomCode();
			// String URL = "orgGroupController.do?getRQCode&groupId="+groupid;
			// map.put("url", URL);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			map.put("expireTime", formatter.format(groupShareEntity.getExpireTime()));
			ajaxResult.setObj(map);
			ajaxResult.setStatus(content);
			ajaxResult.setMsg("成功获取群组二维码");
			ajaxResult.setSuccess(true);
		}
		return ajaxResult;
	}

	@Override
	public GroupShareEntity queryGroupShare(String groupid, String randomCode) throws Exception {
	
		String sql = "from GroupShareEntity where groupId = ? and randomCode = ?";
		GroupShareEntity groupShare = this.groupShareDao.findUniqueByHql(sql, groupid,randomCode);
		return groupShare;
	}
	
	
}
