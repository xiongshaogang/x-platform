package com.xplatform.base.orgnaization.user.dao;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.dao.ICommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.entity.UserRoleEntity;

/**
 * 
 * description : 分组管理dao
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
public interface UserDao extends ICommonDao{
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午9:56:38
	 * @Decription 新增
	 *
	 * @param User
	 * @return
	 */
	public String addUser(UserEntity user);
	
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午9:57:06
	 * @Decription 删除
	 * @param id
	 * @return
	 */
	public void deleteUser(String id);
	
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午9:56:56
	 * @Decription 修改
	 * @param User
	 * @return
	 */
	public void updateUser (UserEntity user);
	
	
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:08:19
	 * @Decription 通过id查询单条记录
	 * @param id
	 * @return
	 */
	public UserEntity getUser(String id);
	
	/**
	 * 
	 * @author lixt
	 * @createtime 2015年10月21日 上午14:08:19
	 * @Decription 通过phone查询单条记录
	 * @param phone,true
	 * @return
	 */
	public UserEntity getUser(String phone,boolean flag);
	
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午9:57:48
	 * @Decription 查询所有的记录
	 * @return
	 */
	public List<UserEntity> queryUserList();
	
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午9:58:12
	 * @Decription hibernate分页查询
	 * @param cq
	 * @param b
	 */
	public void DataGrid(CriteriaQuery cq, boolean b);
	
	/**
	 * 
	 * @author xiehs
	 * @createtime 2014年6月3日 下午2:08:29
	 * @Decription 获取用户的角色
	 * @param param
	 */
	public List<UserRoleEntity> getUserRole(Map<String,String> param);
	
}
