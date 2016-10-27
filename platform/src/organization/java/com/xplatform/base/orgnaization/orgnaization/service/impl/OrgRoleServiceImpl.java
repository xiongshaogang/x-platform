package com.xplatform.base.orgnaization.orgnaization.service.impl;

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

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.orgnaization.orgnaization.dao.OrgRoleDao;
import com.xplatform.base.orgnaization.orgnaization.dao.OrgnaizationDao;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgRoleEntity;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.orgnaization.service.OrgRoleService;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;

@Service("orgRoleService")
public class OrgRoleServiceImpl implements OrgRoleService {
	private static final Logger logger = Logger.getLogger(OrgRoleServiceImpl.class);
	@Resource
	private OrgRoleDao orgRoleDao;
	
	@Resource
	private OrgnaizationDao orgnaizationDao;
	
	@Resource
	private BaseService baseService;

	@Override
	public String save(OrgRoleEntity orgRole) throws BusinessException {
		String pk="";
		try {
			pk=this.orgRoleDao.addOrgRole(orgRole);
		} catch (Exception e) {
			logger.error("部门保存失败");
			throw new BusinessException("部门保存失败");
		}
		logger.info("岗位保存成功");
		return pk;
	}

	@Override
	public void delete(String id) throws BusinessException {
		try {
			this.orgRoleDao.deleteOrgRole(id);
		} catch (Exception e) {
			logger.error("部门删除失败");
			throw new BusinessException("部门删除败");
		}
		logger.info("部门删除成功");
	}

	@Override
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("部门批量删除成功");
	}

	@Override
	public void update(OrgRoleEntity orgRole) throws BusinessException {
		try {
			this.orgRoleDao.updateOrgRole(orgRole);
		} catch (Exception e) {
			logger.error("部门更新失败");
			throw new BusinessException("部门更新失败");
		}
		logger.info("部门更新成功");
	}
	
	@Override
	public void updateOrgRole(String roleIds, String orgId) throws BusinessException {
		try {
			//OrgnaizationEntity org = orgnaizationDao.get(OrgnaizationEntity.class, orgId);
			OrgnaizationEntity org = new OrgnaizationEntity();
			org.setId(orgId);
			List<OrgRoleEntity> orgRoleList = orgRoleDao.queryOrgRoleByOrgIdList(orgId);
			Map<String, OrgRoleEntity> orgRoleMap = new HashMap<String, OrgRoleEntity>();
			if(!orgRoleList.isEmpty()){
				for(OrgRoleEntity orgRole : orgRoleList){
					orgRoleMap.put(orgRole.getRole().getId(), orgRole);
				}
			}
			
			Set<String> set = new HashSet<String>();
			if(StringUtils.isNotEmpty(roleIds)){
				String[] roleArr=roleIds.split(",");
				for(String roleId : roleArr){
					set.add(roleId);
				}
			}
			updateOrgRoleCompare(set,org, orgRoleMap);
		} catch (Exception e) {
			logger.error("部门角色操作失败");
			throw new BusinessException("部门角色操作失败");
		}
	}
	
	private void updateOrgRoleCompare(Set<String> set,OrgnaizationEntity org, Map<String, OrgRoleEntity> map) throws BusinessException {
		List<OrgRoleEntity> saveList = new ArrayList<OrgRoleEntity>();
		List<OrgRoleEntity> deleteList = new ArrayList<OrgRoleEntity>();
		
		//添加的数据
		for(String roleId : set){
			if(map.containsKey(roleId)){
				map.remove(roleId);
			}else{
				OrgRoleEntity orgRole = new OrgRoleEntity();
				RoleEntity role = new RoleEntity();
				role.setId(roleId);
				orgRole.setRole(role);
				orgRole.setOrg(org);
				saveList.add(orgRole);
			}
		}
		
		//构造删除数据
		for(Entry<String, OrgRoleEntity> entry:map.entrySet()){
			deleteList.add(entry.getValue());
		}
		
		this.orgRoleDao.batchSave(saveList);
		this.orgRoleDao.deleteAllEntitie(deleteList);
	}

	
	@Override
	public void updateRoleOrg(String roleId, String orgIds) throws BusinessException{
		//数据库中该角色已经存在的机构
		List<OrgRoleEntity> orgRoles = orgRoleDao.queryOrgRolesByRoleId(roleId);
		Map<String, OrgRoleEntity> ownsRoleOrgs = new HashMap<String, OrgRoleEntity>();
		if(!orgRoles.isEmpty()){
			for(OrgRoleEntity orgRole : orgRoles){
				ownsRoleOrgs.put(orgRole.getOrg().getId(), orgRole);
			}
		}
		//页面选择的机构
		Set<String> selOrgIds = new HashSet<String>();
		if(StringUtil.isNotEmpty(orgIds)){
			String[] orgIdArr =  orgIds.split(",");
			for(String orgId : orgIdArr){
				selOrgIds.add(orgId);
			}
		}
		
		List<OrgRoleEntity> addList = new ArrayList<OrgRoleEntity>();
		List<OrgRoleEntity> deleteList = new ArrayList<OrgRoleEntity>();
		//循环页面勾选的机构
		for(String orgId : selOrgIds){
			//数据库中已有该记录，不执行保存 .
			if(ownsRoleOrgs.containsKey(orgId)){
				ownsRoleOrgs.remove(orgId);
			}else{
				OrgRoleEntity orgRole = new OrgRoleEntity();
				OrgnaizationEntity org = new OrgnaizationEntity();
				org.setId(orgId);
				RoleEntity role = new RoleEntity();
				role.setId(roleId);
				orgRole.setOrg(org);
				orgRole.setRole(role);
				addList.add(orgRole);
			}
		}
		//循环结束，ownsRoleOrgs中的记录为需要删除的
		for(Entry<String, OrgRoleEntity> entry : ownsRoleOrgs.entrySet()){
			deleteList.add(entry.getValue());
		}
		try{
			orgRoleDao.batchSave(addList);
			orgRoleDao.deleteAllEntitie(deleteList);
			logger.info("保存角色机构成功");
		} catch (Exception e) {
			ExceptionUtil.throwBusinessException("保存角色机构失败", logger);
		}
	}
	
	@Override
	public OrgRoleEntity get(String id) throws BusinessException {
		OrgRoleEntity jobRole=null;
		try {
			jobRole=this.orgRoleDao.getOrgRole(id);
		} catch (Exception e) {
			logger.error("部门角色获取失败");
			throw new BusinessException("部门角色获取失败");
		}
		logger.info("部门角色获取成功");
		return jobRole;
	}

	@Override
	public List<OrgRoleEntity> queryList() throws BusinessException {
		List<OrgRoleEntity> jobRoleList=new ArrayList<OrgRoleEntity>();
		try {
			jobRoleList=this.orgRoleDao.queryOrgRoleList();
		} catch (Exception e) {
			logger.error("部门获取列表失败");
			throw new BusinessException("部门获取列表失败");
		}
		logger.info("部门获取列表成功");
		return jobRoleList;
	}
	
	public List<OrgRoleEntity> queryOrgRoleByOrgIdList(String orgId) {
		List<OrgRoleEntity> orgRoleList=new ArrayList<OrgRoleEntity>();
		orgRoleList=this.orgRoleDao.queryOrgRoleByOrgIdList(orgId);
		return orgRoleList;
	}

	@Override
	public List<OrgRoleEntity> queryOrgRolesByRoleId(String roleId) {
		List<OrgRoleEntity> orgRoleList = new ArrayList<OrgRoleEntity>();
		orgRoleList = this.orgRoleDao.queryOrgRolesByRoleId(roleId);
		return orgRoleList;
	}
	
	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) {
		this.orgRoleDao.getDataGridReturn(cq, true);
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(OrgRoleEntity.class, param, propertyName);
	}
	


	public void setOrgRoleDao(OrgRoleDao orgRoleDao) {
		this.orgRoleDao = orgRoleDao;
	}

	public void setOrgnaizationDao(OrgnaizationDao orgnaizationDao) {
		this.orgnaizationDao = orgnaizationDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}
}
