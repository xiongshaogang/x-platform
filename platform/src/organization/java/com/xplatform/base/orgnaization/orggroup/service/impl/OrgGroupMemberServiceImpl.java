package com.xplatform.base.orgnaization.orggroup.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.framework.core.util.MapKit;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.orgnaization.orggroup.dao.OrgGroupMemberDao;
import com.xplatform.base.orgnaization.orggroup.entity.OrgGroupEntity;
import com.xplatform.base.orgnaization.orggroup.entity.OrgGroupMemberEntity;
import com.xplatform.base.orgnaization.orggroup.service.OrgGroupMemberService;
import com.xplatform.base.orgnaization.user.dao.UserDao;
import com.xplatform.base.orgnaization.user.entity.UserEntity;

@Service("orgGroupMemberService")
public class OrgGroupMemberServiceImpl extends BaseServiceImpl<OrgGroupMemberEntity> implements OrgGroupMemberService {

	@Resource
	private OrgGroupMemberDao orgGroupMemberDao;

	@Resource
	private UserDao userDao;

	@Resource
	public void setBaseDao(OrgGroupMemberDao orgGroupMemberDao) {
		super.setBaseDao(orgGroupMemberDao);
	}

	/**
	 * 添加一个群组下的成员
	 * 
	 * @param orgGroupMember
	 * @throws BusinessException
	 */
	@Override
	public String saveOrgGroMem(OrgGroupMemberEntity orgGroupMember, String phones) throws BusinessException {
		boolean flag = true;
		String message = "添加群组成员失败";
		List<UserEntity> userList = new ArrayList<UserEntity>();
		if (StringUtil.isNotEmpty(phones)) {
			String[] phoneArr = StringUtil.split(phones, ",");
			for (String phone : phoneArr) {
				UserEntity user = this.userDao.getUser(phone, true);
				if (user != null) {
					userList.add(user);
				} else {
					flag = false;
					break;
				}
			}
			if (flag) {

				for (int i = 0; i < userList.size(); i++) {
					OrgGroupMemberEntity orgGroupMemberEntity = new OrgGroupMemberEntity();
					orgGroupMemberEntity.setId(UUIDGenerator.generate());
					orgGroupMemberEntity.setUserId(userList.get(i).getId());
					orgGroupMemberEntity.setGroupId(orgGroupMember.getGroupId());
					this.orgGroupMemberDao.save(orgGroupMemberEntity);
				}
				message = "添加群组成员成功";
			}
		}

		return message;
	}

	/**
	 * 删除一个群组下所有成员
	 * 
	 * @param groupId
	 * @throws BusinessException
	 */

	@Override
	public void deleteOrgGroMem(String groupId) throws BusinessException {
		this.orgGroupMemberDao.executeSql("DELETE FROM t_org_group_member WHERE groupId=?", groupId);
	}

	/**
	 * 更新一个群组下的成员
	 * 
	 * @param orgGroupMember
	 * @throws BusinessException
	 */
	@Override
	public void updateOrgGroMem(OrgGroupMemberEntity orgGroupMember) throws Exception {
		OrgGroupMemberEntity oldEntity = this.orgGroupMemberDao.getEntity(orgGroupMember.getId());

		MyBeanUtils.copyBeanNotNull2Bean(orgGroupMember, oldEntity);
		this.orgGroupMemberDao.updateEntitie(oldEntity);
	}

	@Override
	public List<Map<String, Object>> queryGroupUsers(String groupId) {
		// String hql =
		// "select a.id,a.name,a.phone,a.portrait,a.sortKey,a.searchKey from t_org_user a,(select id,userId from t_org_group_member t where t.groupId = ? GROUP BY userId) b  where a.id = b.userId";
		String sql = "SELECT DISTINCT u.id,u.name,u.phone,u.portrait,u.sortKey,u.searchKey FROM t_org_group_member gm left join t_org_user u ON gm.userId=u.id WHERE gm.groupId=? ORDER BY gm.isOwner DESC";
		return this.orgGroupMemberDao.findForJdbc(sql, groupId);
	}

	@Override
	public List<Map<String, Object>> queryGroupPortrait(String groupId) {
		String sql = "SELECT DISTINCT u.portrait,u.name FROM t_org_group_member gm left join t_org_user u ON gm.userId=u.id WHERE gm.groupId=?  ORDER BY gm.isOwner DESC";
		List<Map<String, Object>> list = this.orgGroupMemberDao.findForJdbcParam(sql, 1, 4, groupId);
		return list;
	}

	@Override
	public OrgGroupMemberEntity getOrgGroMem(String groupId, String userId) throws BusinessException {
		String sql = "from OrgGroupMemberEntity where groupId=? and userId=?";
		return this.orgGroupMemberDao.findUniqueByHql(sql, groupId,userId);
	}
}
