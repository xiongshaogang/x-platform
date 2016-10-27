package com.xplatform.base.workflow.node.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.groovy.GroovyScriptEngine;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.workflow.node.dao.NodeSignDao;
import com.xplatform.base.workflow.node.entity.NodeRuleEntity;
import com.xplatform.base.workflow.node.entity.NodeSignEntity;
import com.xplatform.base.workflow.node.entity.NodeSignPrivilegeEntity;
import com.xplatform.base.workflow.node.service.NodeSignService;

/**
 * 
 * description :会签规则设置service实现
 *
 * @version 1.0
 * @author binyong
 * 
 *
 */
@Service("nodeSignService")
public class NodeSignServiceImpl implements NodeSignService {
	
	@Resource
	private NodeSignDao nodeSignDao;
	
	@Resource
	private SysUserService sysUserService;
	
	@Resource
	private GroovyScriptEngine groovyScriptEngine;

	@Override
	public NodeSignEntity getNodeSignByNodeIdAndActDefId(String nodeId,
			String actDefId) {
		// TODO Auto-generated method stub
		Map<String,String> param = new HashMap<String,String>();
		param.put("nodeId", nodeId);
		param.put("actDefId", actDefId);
		List<NodeSignEntity> list =  this.nodeSignDao.findByPropertys(NodeSignEntity.class, param);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<NodeSignPrivilegeEntity> getNodeSignPrivilege(
			NodeSignEntity nodeSign) {
		// TODO Auto-generated method stub
		return this.nodeSignDao.findByProperty(NodeSignPrivilegeEntity.class, "nodeSignId", nodeSign.getId());
	}
	
	@Override
	public void update(NodeSignEntity NodeSign) throws BusinessException {
		// TODO Auto-generated method stub
		try {
			NodeSignEntity oldEntity = this.nodeSignDao.get(NodeSignEntity.class, NodeSign.getId());
			MyBeanUtils.copyBeanNotNull2Bean(NodeSign, oldEntity);
			this.nodeSignDao.updateNodeSign(oldEntity);
		} catch (Exception e) {
			throw new BusinessException("会签规则更新失败");
		}
	}

	@Override
	public String save(NodeSignEntity NodeSign) throws BusinessException {
		// TODO Auto-generated method stub
		String pk="";
		try {
			pk=this.nodeSignDao.addNodeSign(NodeSign);
		} catch (Exception e) {
			throw new BusinessException("会签规则保存失败");
		}
		return pk;
	}
	
	@Override
	public void deleteNodeSignPrivilegeById(String id) {
		// TODO Auto-generated method stub
		this.nodeSignDao.executeSql("delete from t_flow_node_sign_privilege where node_sign_id = ?", new Object[]{id});
	}
	
	@Override
	public void savePrivilege(NodeSignPrivilegeEntity nspe) {
		// TODO Auto-generated method stub
		this.nodeSignDao.save(nspe);
	}
	
	/** 根据流程定义id和节点id获取会签节点*/
	@Override
	public NodeSignEntity getByDefIdAndNodeId(String actDefId, String nodeId){
		Map<String,String> param =new HashMap<String,String>();
		param.put("actDefId", actDefId);
		param.put("nodeId", nodeId);
		List<NodeSignEntity> list=this.nodeSignDao.findByPropertys(NodeSignEntity.class, param);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public void setNodeSignDao(NodeSignDao nodeSignDao) {
		this.nodeSignDao = nodeSignDao;
	}

	@Override
	public boolean checkNodeSignPrivilege(String defId, String nodeId,
			String privilegeType, String userId,String actInstId) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list=this.nodeSignDao.findForJdbc("select p.id,p.privilege_user_ids userIds,p.privilege_user_type userType from t_flow_node_sign s,t_flow_node_sign_privilege p where s.id=p.node_sign_id and s.act_id=? and s.node_id=? and (p.privilege_type='0' or p.privilege_type=?)", defId,nodeId,privilegeType);
		if (BeanUtils.isEmpty(list)) {
			return false;
		}
		List<String> allowList = null;
		for(Map<String, Object> map:list){
			String userIds=map.get("userIds").toString();
			String userType=map.get("userType").toString();
			//匹配用户是否具有权限
			if(StringUtil.equals("emp", userType)){
				if(StringUtil.isNotBlank(userIds)){
					allowList = Arrays.asList(userIds.split(","));
					/*EmployeeEntity emp=this.sysUserService.getEmployeeByUserId(userId);
					if(emp==null){return false;}*/
					return containList(allowList, userId);
				}
			//判断部门
			}else if(StringUtil.equals("org", userType)){
				if(StringUtil.isNotBlank(userIds)){
					allowList = Arrays.asList(userIds.split(","));
					List<OrgnaizationEntity> deptList=this.sysUserService.getDeptsByUserId(userId);
					if(deptList==null){return false;}
					for(OrgnaizationEntity dept:deptList){
						boolean flag=containList(allowList, dept.getId());
						if(flag){
							return true;
						}
					}
				}
			//判断角色	
			}else if(StringUtil.equals("role", userType)){
				if(StringUtil.isNotBlank(userIds)){
					allowList = Arrays.asList(userIds.split(","));
					List<RoleEntity> roleList=this.sysUserService.getRoleListByUserId(userIds);
					if(roleList==null){return false;}
					for(RoleEntity role:roleList){
						boolean flag=containList(allowList, role.getId());
						if(flag){
							return true;
						}
					}
				}
			//判断岗位
			}else if(StringUtil.equals("job", userType)){
				if(StringUtil.isNotBlank(userIds)){
					allowList = Arrays.asList(userIds.split(","));
					List<OrgnaizationEntity> jobList=this.sysUserService.getJobsByUserId(userId);
					if(jobList==null){return false;}
					for(OrgnaizationEntity job:jobList){
						boolean flag=containList(allowList, job.getId());
						if(flag){
							return true;
						}
					}
				}
			//判断部门负责人
			}else if(StringUtil.equals("deptManager", userType)){
				if(StringUtil.isNotBlank(userIds)){
					allowList = Arrays.asList(userIds.split(","));
					List<UserEntity> userList=this.sysUserService.getDeptManagersByUserId(userId);
					if(userList==null || userList.size()==0){return false;}
					for(UserEntity user:userList){
						if(containList(allowList, user.getId())){
							return true;
						}
					}
					return false;
				}
			//判断部门分管领导
			}else if(StringUtil.equals("deptLeader", userType)){
				if(StringUtil.isNotBlank(userIds)){
					allowList = Arrays.asList(userIds.split(","));
					List<UserEntity> userList=this.sysUserService.getBranchLeadersByUserId(userId);
					if(userList==null || userList.size()==0){return false;}
					for(UserEntity user:userList){
						if(containList(allowList, user.getId())){
							return true;
						}
					}
					return false;
				}
			//脚本判断权限
			}else if(StringUtil.equals("script", userType)){
				Map<String,Object> vars = new HashMap<String,Object>();
				vars.put("defId", defId);
				vars.put("nodeId", nodeId);
				vars.put("privilegeType", privilegeType);
				vars.put("userId", userId);
				vars.put("actInstId", actInstId);
				Object result = this.groovyScriptEngine.executeObject(userIds, vars);
				if(BeanUtils.isEmpty(result)){
					continue;
				}else if(result instanceof UserEntity){
					UserEntity resutltUser=(UserEntity)result;
					if (userId.equals(resutltUser.getId())){
						return true;
					}
				}else if(result instanceof java.util.List){
					List<UserEntity> userList=(List<UserEntity>)result;
					if(userList!=null && userList.size()>0){
						for(UserEntity user1:userList){
							if (userId.equals(user1.getId())){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean containList(List<String> list, String id) {
		for (String str : list) {
			if (str.equals(id)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void deleteByActDefId(String actDefId) throws BusinessException {
		// TODO Auto-generated method stub
		this.nodeSignDao.executeHql("delete from NodeSignEntity t where t.actDefId='"+actDefId+"'");
	}
}
