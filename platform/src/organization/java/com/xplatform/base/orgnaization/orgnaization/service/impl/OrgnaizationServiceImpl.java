package com.xplatform.base.orgnaization.orgnaization.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.engine.vo.resultMsg;
import com.xplatform.base.orgnaization.orgnaization.dao.OrgnaizationDao;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.orgnaization.mybatis.dao.OrgMybatisDao;
import com.xplatform.base.orgnaization.orgnaization.mybatis.vo.OrgTreeVo;
import com.xplatform.base.orgnaization.orgnaization.service.OrgnaizationService;
import com.xplatform.base.orgnaization.user.entity.UserOrgEntity;
import com.xplatform.base.platform.common.service.SysUserService;

@Service("orgnaizationService")
public class OrgnaizationServiceImpl implements OrgnaizationService {
	private static final Logger logger = Logger.getLogger(OrgnaizationServiceImpl.class);
	@Resource
	private OrgnaizationDao orgnaizationDao;
	
	@Resource
	private BaseService baseService;
	
	@Resource 
	private SysUserService sysUserService;
	
	@Resource
	private OrgMybatisDao orgMybatisDao;

	@Override
	@Action(moduleCode="organizationManager",description="组织机构新增",detail="组织机构${org.name}新增成功", execOrder = ActionExecOrder.BEFORE)
	public String save(OrgnaizationEntity org) throws BusinessException {
		String pk="";
		try {
			OrgnaizationEntity orgEntity = this.get(org.getParent().getId());
			//第一步，保存信息
			pk=this.orgnaizationDao.addOrg(org);
			if(orgEntity != null){
				//第二步，修改树的详细信息
				org.setId(pk);
				if(orgEntity.getTreeIndex() != null)
					org.setTreeIndex(orgEntity.getTreeIndex()+","+pk);
				else
					org.setTreeIndex(pk);
				if(orgEntity.getLevel() != null)
					org.setLevel(orgEntity.getLevel()+1);
				else org.setLevel(1);
				org.setIsLeaf("1");
				this.orgnaizationDao.merge(org);
				//第三步，更新父节点的信息
				if(orgEntity.getIsLeaf() == null || orgEntity.getIsLeaf().equals("1")){//父节点是叶子节点
					if(orgEntity.getParent() == null){
						OrgnaizationEntity parent = new OrgnaizationEntity();
						parent.setId("-1");
						orgEntity.setParent(parent);
					}
					orgEntity.setIsLeaf("0");
					this.orgnaizationDao.merge(orgEntity);
				}
			}else{
				org.setId(pk);
				org.setTreeIndex(pk);
				org.setLevel(1);
				org.setIsLeaf("1");
				this.orgnaizationDao.merge(org);
			}
			
			
			//添加一个匿名部门
		} catch (Exception e) {
			logger.error("组织保存失败");
			throw new BusinessException("组织保存失败");
		}
		logger.info("组织保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="organizationManager",description="删除组织机构",detail="id为${id}的机构删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		try {
			OrgnaizationEntity org = this.get(id);
			OrgnaizationEntity parent=null;
			//获取父节点
			if(org!=null && org.getParent()!=null){
				parent = org.getParent();
			}
			//设置父节点是否叶子节点
			if(parent!=null && parent.getChildren()!=null && parent.getChildren().size()<=1){
				parent.setIsLeaf("1");
			}
			this.orgnaizationDao.deleteOrg(id);
			//修改节点
			if(parent != null){
				this.update(parent);
			}
		} catch (Exception e) {
			logger.error("组织删除失败");
			throw new BusinessException("组织删除失败");
		}
		logger.info("组织删除成功");
	}
	
	public void deleteCascade(String id) throws BusinessException {
		try {
			List<OrgnaizationEntity> orgList= this.orgnaizationDao.findHql("select distinct uo from OrgnaizationEntity uo where uo.treeIndex like ?","%"+id+"%");
			if(orgList!=null && orgList.size()>0){
				this.orgnaizationDao.deleteAllEntitie(orgList);
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("组织删除失败");
			throw new BusinessException("组织删除失败");
		}
		
		
	}

	@Override
	//@Action(moduleCode="organizationManager",description="组织机构批量删除",detail="组织机构${name}批量删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws Exception {
		if(StringUtil.isNotEmpty(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("组织批量删除成功");
	}

	@Override
	@Action(moduleCode="organizationManager",description="组织机构修改",detail="组织机构${name}修改成功", execOrder = ActionExecOrder.BEFORE)
	public void update(OrgnaizationEntity org) throws BusinessException {
		try {
			OrgnaizationEntity oldEntity = get(org.getId());
			MyBeanUtils.copyBeanNotNull2Bean(org, oldEntity);
			this.orgnaizationDao.updateOrg(oldEntity);
		} catch (Exception e) {
			logger.error("组织更新失败");
			throw new BusinessException("组织更新失败");
		}
		logger.info("组织更新成功");
	}

	@Override
	public OrgnaizationEntity get(String id){
		OrgnaizationEntity org = null;
		org = this.orgnaizationDao.getOrg(id);
		return org;
	}

	// this method modify by lxt 20150522
	@Override
	public List<OrgnaizationEntity> queryList(String params) {
		List<OrgnaizationEntity> orgList = new ArrayList<OrgnaizationEntity>();
		orgList = this.orgnaizationDao.queryOrgList(params);
		return orgList;
	}

	@Override
	public List<OrgnaizationEntity> queryList() {
		List<OrgnaizationEntity> orgList = new ArrayList<OrgnaizationEntity>();
		orgList = this.orgnaizationDao.queryOrgList();
		return orgList;
	}
	
	
	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b){
		this.orgnaizationDao.getDataGridReturn(cq, true);
	}
	
	/**
	 * 查询员工分配可控制的机构树
	 * @param userId
	 * @param parentId
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public List<OrgTreeVo> queryOrgTreeByGrade(String userId, String parentId) {
		Map<String,String> param = new HashMap<String,String>();
		param.put("userId", userId);
		param.put("parentId", parentId);
		List<OrgTreeVo> orgTreeList = new ArrayList<OrgTreeVo>();
		orgTreeList = this.orgMybatisDao.queryOrgTreeByGrade(param);
		return orgTreeList;
	}
	
	/**
	 * 查询员工分配部门树
	 * @param userId
	 * @param parentId
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public List<OrgTreeVo> queryEmpOrgTree(String userId, String parentId){
		Map<String,String> param = new HashMap<String,String>();
		param.put("userId", userId);
		param.put("parentId", parentId);
		List<OrgTreeVo> orgTreeList = new ArrayList<OrgTreeVo>();
		orgTreeList = this.orgMybatisDao.queryEmpOrgTree(param);
		return orgTreeList;
	}
	
	/**
	 * 查询可控制机构树
	 * @param userId
	 * @param parentId
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public List<OrgTreeVo> queryOrgTree(String userId, String parentId) throws BusinessException {
		Map<String,String> param=new HashMap<String,String>();
		param.put("userId", userId);
		param.put("parentId", parentId);
		List<OrgTreeVo> orgTreeList = new ArrayList<OrgTreeVo>();
		try {
			orgTreeList = this.orgMybatisDao.queryOrgTree(param);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("查询组织失败");
		}
		return orgTreeList;
	}

	@Override
	public List<OrgnaizationEntity> queryListByPorperty(String propertyName, String value) {
		return this.orgnaizationDao.findByProperty(OrgnaizationEntity.class, propertyName, value);
	}
	
	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		return this.baseService.isUnique(OrgnaizationEntity.class, param, propertyName);
	}
	
	@Override
	public String queryOrgNameByIds(String deptIds, String split) {
		String names = "";
		String[] array = deptIds.split(split);
		for (String id : array) {
			OrgnaizationEntity entity = get(id);
			names += entity.getName() + split;
		}
		return StringUtil.removeDot(names);
	}

	@Override
	public List<OrgnaizationEntity> queryControllerOrg(String empId) throws IOException, ClassNotFoundException {
		
		List<OrgnaizationEntity> resultV = new ArrayList<OrgnaizationEntity>();//可操作的
		List<OrgnaizationEntity> resultN = new ArrayList<OrgnaizationEntity>();//不可操作的
		//1.查找可操作的机构
		List<OrgnaizationEntity> orgUnderJob = this.sysUserService.getUnderJobsByUserId(empId);
		resultV.addAll(orgUnderJob);
		//2.查找自己所在的所有的机构的所有祖先节点
		List<OrgnaizationEntity> orgJob = this.sysUserService.getAllOrganizationsByUserId(empId);
		if(orgJob != null){
			for(OrgnaizationEntity org : orgJob){
				while(!(org.getParent() == null)){
					org = this.get(org.getParent().getId());
					if(!resultN.contains(org)){
						resultN.add(org);
					}
				}
			}
		}
		//3.祖先节点
		List<OrgnaizationEntity> delete = new ArrayList<OrgnaizationEntity>();
		for(OrgnaizationEntity result : resultN){
			if(resultV.contains(result)){
				delete.add(result);
			}
		}
		resultN.removeAll(delete);
		//4.设置可操作性
		resultV = BeanUtils.deepCopy(resultV);
		resultN = BeanUtils.deepCopy(resultN);
		for(OrgnaizationEntity result : resultV){
			result.setAvailable("1");
		}
		for(OrgnaizationEntity result : resultN){
			result.setAvailable("0");
		}
		
		resultV.addAll(resultN);//同步结果集
		
		
		return resultV;
	}
	
	@Override
	public List<OrgnaizationEntity> getUpOrg(String userId) {
		// TODO Auto-generated method stub
		List<OrgnaizationEntity> orgJob = this.sysUserService.getAllOrganizationsByUserId(userId);
		Map<String,OrgnaizationEntity> result = new HashMap<String,OrgnaizationEntity>();//不可操作的
		if(orgJob != null && orgJob.size()>0){
			for(OrgnaizationEntity org : orgJob){
				if(org!=null && !StringUtil.equals(OrgnaizationEntity.DEFAULT_ID, org.getId())){
					result.put(org.getId(), org);
				}
				while(!(org.getParent() == null)){
					org = this.get(org.getParent().getId());
					if(org!=null && !StringUtil.equals(OrgnaizationEntity.DEFAULT_ID, org.getId())){
						result.put(org.getId(), org);
					}
				}
			}
		}
		return new ArrayList<OrgnaizationEntity>(result.values());
	}

	@Override
	public List<OrgnaizationEntity> getLowOrg(OrgnaizationEntity org) {
		// TODO Auto-generated method stub
		return this.orgnaizationDao.findHql("from OrgnaizationEntity o where o.treeIndex like ?", new Object[]{org.getTreeIndex()+'%'});
	}
	
	@Override
	public List<OrgnaizationEntity> getLowJob(OrgnaizationEntity org){
		return this.orgnaizationDao.findHql("from OrgnaizationEntity o where o.type='job' and o.treeIndex like ?", org.getTreeIndex()+'%');
	}
}
