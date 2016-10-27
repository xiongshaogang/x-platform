package com.xplatform.base.orgnaization.module.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.orgnaization.module.dao.ModuleDao;
import com.xplatform.base.orgnaization.module.entity.ModuleEntity;
import com.xplatform.base.orgnaization.module.mybatis.dao.ModuleMybatisDao;
import com.xplatform.base.orgnaization.module.mybatis.vo.ModuleTreeVo;
import com.xplatform.base.orgnaization.module.service.ModuleService;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.role.entity.RoleModuleEntity;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.system.log.service.OperLogService;

/**
 * 
 * description :模块service实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 下午12:30:12
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 下午12:30:12
 *
 */
@Service("moduleService")
public class ModuleServiceImpl implements ModuleService  {
	private static final Logger logger = Logger.getLogger(ModuleServiceImpl.class);
	@Resource
	private ModuleDao moduleDao;
	
	
	@Resource
	private ModuleMybatisDao moduleMybatisDao;
	
	@Resource
	private OperLogService operLogService;

	public void setOperLogService(OperLogService operLogService) {
		this.operLogService = operLogService;
	}

	@Override
	@Action(moduleCode="moduleManager",description="模块新增",detail="模块${name}添加成功", execOrder = ActionExecOrder.BEFORE)
	public String save(ModuleEntity module) throws BusinessException {
		String pk = "";
		// 设置当前登录组织
//		module.setInstitutionId(ClientUtil.getInstitutionId());
		try {
			ModuleEntity parent = this.get(module.getParent().getId());
			// 第一步，保存信息
			pk = this.moduleDao.addModule(module);
			if (parent != null) {
				// 第二步，修改树的详细信息
				module.setId(pk);
				module.setTreeIndex(parent.getTreeIndex() + "," + pk);
				module.setLevel(parent.getLevel() + 1);
				module.setIsLeaf("1");
				this.moduleDao.updateModule(module);
				// 第三步，更新父节点的信息
				if (StringUtil.equals(parent.getIsLeaf(), "1")) {// 父节点是叶子节点
					parent.setIsLeaf("0");
					if (parent.getParent() == null) {
						ModuleEntity parent1 = new ModuleEntity();
						parent1.setId("-1");
						parent.setParent(parent1);
					}
					this.moduleDao.updateModule(parent);
				}
			} else {
				module.setId(pk);
				module.setTreeIndex(pk);
				module.setLevel(1);
				module.setIsLeaf("1");
				this.moduleDao.updateModule(module);
			}
		} catch (Exception e) {
			ExceptionUtil.throwBusinessException("模块保存失败", logger);
		}
		logger.info("模块保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="moduleManager",description="模块删除",detail="模块${moduleService.getName(id)}删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			ModuleEntity module=get(id);
			ModuleEntity parent=null;
			//获取父节点
			if(module!=null && module.getParent()!=null){
				parent=module.getParent();
			}
			//设置父节点是否叶子节点
			if(parent!=null && parent.getChildren()!=null && parent.getChildren().size()<=1){
				parent.setIsLeaf("1");
			}
			//删除节点
			this.moduleDao.deleteModule(id);
			//修改节点
			if(parent!=null){
				this.update(parent);
			}
		} catch (Exception e) {
			logger.error("模块删除失败");
			throw new BusinessException("模块删除失败");
		}
		logger.info("模块删除成功");
	
	}

	@Override
	public void batchDelete(String ids) throws BusinessException {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("模块批量删除成功");
	}

	@Override
	public void update(ModuleEntity module) throws BusinessException {
		try {
			ModuleEntity oldEntity = get(module.getId());
			MyBeanUtils.copyBeanNotNull2Bean(module, oldEntity);
			if (oldEntity.getParent() == null) {
				ModuleEntity parent = new ModuleEntity();
				parent.setId("-1");
				oldEntity.setParent(parent);
			}
			this.moduleDao.updateModule(oldEntity);
		} catch (Exception e) {
			ExceptionUtil.throwBusinessException("模块更新失败", logger);
		}
		logger.info("模块更新成功");
	}

	@Override
	public ModuleEntity get(String id){
		ModuleEntity Module=null;
		Module=this.moduleDao.getModule(id);
		return Module;
	}
	
	public String getName(String id) {
		String name = "";
		ModuleEntity Module = null;
		Module = this.moduleDao.getModule(id);
		if (Module != null) {
			name = Module.getName();
		}
		return name;
	}

	@Override
	public List<ModuleEntity> queryList() throws BusinessException{
		List<ModuleEntity> ModuleList=new ArrayList<ModuleEntity>();
		try {
			ModuleList=this.moduleDao.queryModuleList();
		} catch (Exception e) {
			logger.error("模块获取列表失败");
			throw new BusinessException("模块获取列表失败");
		}
		logger.info("模块获取列表成功");
		return ModuleList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b){
		this.moduleDao.getDataGridReturn(cq, true);
	}

	@Override
	public List<ModuleEntity> queryModulesByCondition(String parentId, String institutionId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("parent.id", parentId);
		params.put("institutionId", institutionId);
		return this.moduleDao.findByPropertys(ModuleEntity.class, params);
	}
	
	@Override
	public void updateRoleModuleAuthority(String moduleIds,String roleId) throws BusinessException{
		RoleEntity role=this.moduleDao.getEntity(RoleEntity.class,roleId);
		//构造当前角色下的模块权限
		List<RoleModuleEntity> list=this.moduleDao.findHql("from RoleModuleEntity rm where rm.role.id=?", roleId);
		Map<String,RoleModuleEntity> map=new HashMap<String,RoleModuleEntity>();
		if(list!=null){
			for(RoleModuleEntity roleModuleEntity:list){
				map.put(roleModuleEntity.getModule().getId(), roleModuleEntity);
			}
		}
		//选择的模块权限
		Set<String> set = new HashSet<String>();
		if(StringUtils.isNotEmpty(moduleIds)){
			String[] moduleArr=moduleIds.split(",");
			for (String s : moduleArr) {
				set.add(s);
			}
		}
		//模块权限比较更新
		updateRoleModuleCompare(set,role,map);
		logger.info("更新角色模块成功");
	}
	
	/**
	 * 权限比较
	 * @param set 最新的权限列表
	 * @param role 当前角色
	 * @param map 旧的权限列表
	 * @param entitys 最后保存的权限列表
	 */
	private void updateRoleModuleCompare(Set<String> set,RoleEntity role, Map<String, RoleModuleEntity> map) throws BusinessException {
		List<RoleModuleEntity> saveEntitys = new ArrayList<RoleModuleEntity>();
		List<RoleModuleEntity> deleteEntitys = new ArrayList<RoleModuleEntity>();
		//构造插入数据，排除删除数据
		for (String s : set) {
			if(map.containsKey(s)){
				map.remove(s);
			}else{
				RoleModuleEntity rm = new RoleModuleEntity();
				ModuleEntity module = this.moduleDao.get(ModuleEntity.class,s);
				rm.setModule(module);
				rm.setRole(role);
				saveEntitys.add(rm);
			}
		}
		//构造删除数据
		for(Entry<String, RoleModuleEntity> entry:map.entrySet()){
			deleteEntitys.add(entry.getValue());
		}
		try{
			this.moduleDao.batchSave(saveEntitys);
			this.moduleDao.deleteAllEntitie(deleteEntitys);
			//删除模块后 删除模块的操作按钮权限
			String sql = "delete from t_org_role_resource where role_id=? and module_id=?";
			for(RoleModuleEntity deleteEntity : deleteEntitys){
				moduleDao.executeSql(sql, new Object[]{deleteEntity.getRole().getId(), deleteEntity.getModule().getId()});
			}
		}catch(Exception e){
			throw new BusinessException("保存模块权限失败");
		}
	}
	
	/*@Override
	public void updateUserModuleAuthority(String moduleIds,String userId)  throws BusinessException{
		UserEntity user=this.moduleDao.getEntity(UserEntity.class,userId);
		//构造当前角色下的模块权限
		List<UserModuleEntity> list=this.moduleDao.findHql("from UserModuleEntity rm where rm.user.id=?", userId);
		Map<String,UserModuleEntity> map=new HashMap<String,UserModuleEntity>();
		if(list!=null){
			for(UserModuleEntity userModuleEntity:list){
				map.put(userModuleEntity.getModule().getId(), userModuleEntity);
			}
		}
		//选择的模块权限
		Set<String> set = new HashSet<String>();
		if(StringUtils.isNotEmpty(moduleIds)){
			String[] moduleArr=moduleIds.split(",");
			for (String s : moduleArr) {
				set.add(s);
			}
		}
		//模块权限比较更新
		updateUserModuleCompare(set,user,map);
	}
	
	*//**
	 * 权限比较
	 * @param set 最新的权限列表
	 * @param role 当前角色
	 * @param map 旧的权限列表
	 * @param entitys 最后保存的权限列表
	 *//*
	private void updateUserModuleCompare(Set<String> set,UserEntity user, Map<String, UserModuleEntity> map)  throws BusinessException{
		List<UserModuleEntity> saveEntitys = new ArrayList<UserModuleEntity>();
		List<UserModuleEntity> deleteEntitys = new ArrayList<UserModuleEntity>();
		//构造插入数据，排除删除数据
		for (String s : set) {
			if(map.containsKey(s)){
				map.remove(s);
			}else{
				UserModuleEntity rm = new UserModuleEntity();
				ModuleEntity module = this.moduleDao.get(ModuleEntity.class,s);
				rm.setModule(module);
				rm.setUser(user);
				saveEntitys.add(rm);
			}
		}
		//构造删除数据
		for(Entry<String, UserModuleEntity> entry:map.entrySet()){
			deleteEntitys.add(entry.getValue());
		}
		try {
			this.moduleDao.batchSave(saveEntitys);
			this.moduleDao.deleteAllEntitie(deleteEntitys);
			//删除模块后 删除模块的操作按钮权限
			String sql = "delete from t_org_user_resource  where user_id=? and module_id=?";
			for(UserModuleEntity deleteEntity : deleteEntitys){
				moduleDao.executeSql(sql, new Object[]{deleteEntity.getUser().getId(), deleteEntity.getModule().getId()});
			}
		} catch (Exception e) {
			throw new BusinessException("更新模块权限失败");
		}
	}*/
	
	/**
	 * 根据用户ID删除用户所属权限
	 * @author luoheng
	 * @param userId
	 */
	@Override
	public void deleteUserModuleByUserId(String userId) throws BusinessException {
		try {
			moduleDao.deleteUserModuleByUserId(userId);
			logger.error("用户权限删除成功");
		} catch (Exception e) {
			logger.error("用户权限删除失败");
			throw new BusinessException("用户权限删除失败");
		}
	}
	
	@Override
	public List<ModuleTreeVo> queryModuleAuthorityByRole(String roleId, String parentId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("roleId", roleId);
		param.put("parentId", parentId);
		List<ModuleTreeVo> list = null;
		list = this.moduleMybatisDao.queryModuleAuthorityByRole(param);
		return list;
	}

	@Override
	public List<ModuleTreeVo> queryModuleAuthorityByUser(String userId,
			String parentId){
		Map<String,String> param=new HashMap<String,String>();
		param.put("userId", userId);
		param.put("parentId", parentId);
		List<ModuleTreeVo> list=null;
		list= this.moduleMybatisDao.queryModuleAuthorityByUser(param);
		return list;
	}
	
	@Override
	public List<ModuleEntity> findByProperty(Class t, String propertyName,
			Object value) {
		// TODO Auto-generated method stub
		return moduleDao.findByProperty(t, propertyName, value);
	}
	
	@Override
	public ModuleEntity getRoot(String parentId) {
		String rootId=moduleDao.getRoot(ModuleEntity.class,parentId);
		return moduleDao.getModule(rootId);
	}

	public void setmoduleDao(ModuleDao moduleDao) {
		this.moduleDao = moduleDao;
	}

	public void setModuleMybatisDao(ModuleMybatisDao moduleMybatisDao) {
		this.moduleMybatisDao = moduleMybatisDao;
	}
	
}
