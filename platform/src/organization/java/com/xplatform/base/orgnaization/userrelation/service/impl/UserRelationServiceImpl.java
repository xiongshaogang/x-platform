package com.xplatform.base.orgnaization.userrelation.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.service.UserService;
import com.xplatform.base.orgnaization.userrelation.dao.UserRelationDao;
import com.xplatform.base.orgnaization.userrelation.entity.UserRelationEntity;
import com.xplatform.base.orgnaization.userrelation.service.UserRelationService;
import com.xplatform.base.platform.common.def.BusinessConst;

@Service("userRelationService")
public class UserRelationServiceImpl extends BaseServiceImpl<UserRelationEntity> implements UserRelationService {

	@Resource
	private UserRelationDao userRelationDao;
	@Resource
	private UserService userService;

	@Resource
	public void setBaseDao(UserRelationDao userRelationDao) {
		super.setBaseDao(userRelationDao);
	}

	@Override
	public void saveUserRelation(UserRelationEntity userRelation) throws Exception {
		this.userRelationDao.save(userRelation);
	}

	@Override
	public Integer deleteUserRelation(String first, String second) throws Exception {
		String hql = "DELETE FROM UserRelationEntity  where (apply=? AND receive=?) OR (apply=? AND receive=?)";
		return this.userRelationDao.executeHql(hql, first, second, second, first);
	}

	@Override
	public UserRelationEntity getUserRelation(String first, String second) {
		String hql = "FROM UserRelationEntity  where (apply=? AND receive=?) OR (apply=? AND receive=?)";
		return this.userRelationDao.findUniqueByHql(hql, first, second, second, first);
	}

	@Override
	public List<UserRelationEntity> queryAllMyFirendRelation(String userId) {
		String hql = "FROM UserRelationEntity t WHERE (t.apply = ? or t.receive = ? ) AND relationCode=?";
		return this.userRelationDao.findHql(hql, new Object[] { userId, userId, BusinessConst.RelationCode_CODE_friend });
	}

	@Override
	public List<UserEntity> queryAllMyFirend(String userId) {
		List<UserEntity> userList = new ArrayList<UserEntity>();
		List<UserRelationEntity> list = queryAllMyFirendRelation(userId);
		for (UserRelationEntity relation : list) {
			UserEntity user = null;
			if (relation.getApply().equals(userId)) {
				user = userService.get(relation.getReceive());
			} else if (relation.getReceive().equals(userId)) {
				user = userService.get(relation.getApply());
			}
			userList.add(user);
		}
		return userList;
	}
}
