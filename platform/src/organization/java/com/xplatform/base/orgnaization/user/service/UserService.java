package com.xplatform.base.orgnaization.user.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.common.UploadFile;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.user.entity.UserEntity;

/**
 * 
 * description : 用户管理service
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:34:52
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- xiehs 2014年5月24日 上午11:34:52
 *
 */
public interface UserService {

	/**
	 * 新增用户
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription
	 * @param job
	 * @return
	 */
	public String save(UserEntity user) throws BusinessException;

	/**
	 * 删除用户
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException;

	/**
	 * 批量删除用户
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws BusinessException;

	/**
	 * 更新用户
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param job
	 * @return
	 */
	public void update(UserEntity job) throws BusinessException;

	/**
	 * 查询一条用户记录
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public UserEntity get(String id);

	/**
	 * 
	 * @author lixt
	 * @createtime 2015年10月21日 上午14:08:19
	 * @Decription 通过phone查询单条记录
	 * @param phone
	 *            ,true
	 * @return
	 * @return
	 */
	public UserEntity getUser(String phone, boolean flag);

	/**
	 * 查询用户列表
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<UserEntity> queryList();

	/**
	 * hibernate用户分页列表
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:34:16
	 * @Decription
	 * @param cq
	 * @param b
	 */
	public void getDataGridReturn(CriteriaQuery cq, boolean b);

	/**
	 * 判断字段记录是否唯一
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:25:48
	 * @Decription
	 * @param param
	 * @return
	 */
	public boolean isUnique(Map<String, String> param, String propertyName);

	/**
	 * 初始化密码
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:25:48
	 * @Decription
	 * @param param
	 * @return
	 */
	public void initPwd(String userId) throws BusinessException;

	/**
	 * 检查用户是否存在，用于登陆
	 * 
	 * @author xiehs
	 * @createtime 2014年6月5日 上午10:32:01
	 * @Decription
	 *
	 * @param user
	 * @return
	 */
	public UserEntity checkUserExits(String param);

	// public Page<EmpDeptVo> queryEmpDeptList(Page<EmpDeptVo> empPage);

	public UserEntity getUserByName(String userName);

	public UserEntity getUserByNameAndPwd(UserEntity user);

	public void branchSave(List list) throws BusinessException;

	public List<String> getUserOrgTreeIndex(Map<String, String> param);

	public List<String> getManagerOrgList(Map<String, String> param);

	/**
	 * 用户头像上传附件
	 * 
	 * @param uploadFile
	 * @return
	 */
	public Object uploadFile(UploadFile uploadFile);

	/**
	 * 通过手机号检查用户是否存在
	 * 
	 * @author xiaqiang
	 * @createtime 2015年5月20日 上午10:26:54
	 *
	 * @param phone
	 */
	public Boolean checkUserExsits(String phone);

	/**
	 * 向对应手机发送验证码(包含创建用户的处理)
	 * 
	 * @author xiaqiang
	 * @createtime 2015年5月20日 上午10:49:24
	 *
	 * @param phone
	 */
	public void sendVerifyCode(String phone);

	/**
	 * 检查手机、验证码是否对应,验证码是否过期
	 * 
	 * @author xiaqiang
	 * @createtime 2015年5月20日 上午11:22:13
	 *
	 * @param phone
	 * @param verifyCode
	 */
	public Boolean checkVerifyCode(String phone, String verifyCode);

	/**
	 * 正式注册用户
	 * 
	 * @author xiaqiang
	 * @createtime 2015年5月20日 上午11:30:52
	 *
	 * @param phone
	 * @param password
	 * @throws BusinessException
	 */
	public void doRegisterUser(String phone, String password) throws BusinessException;

	public void deleteEntity(String id);

	/**
	 * 更新用户上一次使用组织
	 * 
	 * @author xiaqiang
	 * @createtime 2015年5月23日 上午11:27:26
	 *
	 * @param userId
	 * @param institutionId
	 */
	public void updateUserLastInstitution(String userId, String institutionId);

	/**
	 * 查询角色下包含的用户集合
	 * 
	 * @author xiaqiang
	 * @createtime 2015年5月24日 上午11:34:58
	 *
	 * @param roleId
	 * @return
	 */
	public List<UserEntity> queryUsersByRole(String roleId);

	public boolean getExistPhone(String phone);

	/**
	 * 通过用户名(name字段)模糊查询
	 * 
	 * @param name
	 * @return
	 */
	public List<UserEntity> queryUsersByLikeName(String name);
	
	/**
	 * 通过用户名(name字段)模糊查询
	 * 
	 * @param name
	 * @return
	 */
	public List<Map<String,Object>> queryUsersByLikeKey(String key,String userId);

	/**
	 * 通过用户Id查询用户名称(name)
	 * 
	 * @param userId
	 * @return
	 */
	public String queryName(String userId);

	/**
	 * 通过用户名phone判断是否注册用户
	 * 
	 * @param phones
	 * @return
	 */
	public List<Map<Object, Object>> isRegister(String phones) throws BusinessException;

	/**
	 * 修改用户昵称
	 * 
	 * @param userId
	 * @param name
	 * @return
	 */
	public void doChangeNickname(String userId, String name) throws Exception;

	/**
	 * 批量查询用户信息
	 * 
	 * @param ids
	 * @return
	 */
	public List<Map<String, Object>> getUsersInfo(String ids);
	
	/**
	 * 修改用户密码
	 * 
	 * @param ids
	 * @return
	 */
	public AjaxJson doForgetChangePassword(String phone,String password,String verifyCode) throws Exception;
	
	/**
	 * 查询某人当前所在的所有机构
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<OrgnaizationEntity> queryCompany(String userId) throws BusinessException;
	
	public List<OrgnaizationEntity> queryHomeCompany(String userId) throws BusinessException ;
	/**
	 * 退出团队，如果
	 * @param orgId
	 * @param userId
	 * @return
	 */
	public boolean deleteToCompany(String orgId,String userId) throws BusinessException ;
	
	/**
	 * 查询机构下的管理员
	 * @param orgId
	 * @param userId
	 * @return
	 */
	public List<UserEntity> queryOrgManager(String orgId) ;
	
	public List<UserEntity> findUserByPhone(String phone);

	/**
	 * 更新用户网盘根目录
	 * @param userId
	 */
	public void updateRootTypeId(String typeId,String userId);
}
