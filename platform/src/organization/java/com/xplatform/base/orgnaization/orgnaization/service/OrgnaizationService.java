package com.xplatform.base.orgnaization.orgnaization.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.orgnaization.mybatis.vo.OrgTreeVo;

public interface OrgnaizationService {
	
	/**
	 * 新增部门
	 * @author xiehs/hexj
	 * @createtime 2014年12月9日 上午11:32:41
	 * @Decription 
	 * @param job
	 * @return
	 */
	public String save(OrgnaizationEntity Orgnaization) throws BusinessException ;
	
	/**
	 * 删除部门
	 * @author xiehs/hexj
	 * @createtime 2014年12月9日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id) throws BusinessException ;
	
	/**
	 * 批量删除部门
	 * @author xiehs/hexj
	 * @createtime 2014年12月9日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids) throws Exception;
	
	/**
	 * 更新部门
	 * @author xiehs/hexj
	 * @createtime 2014年12月9日 上午11:33:25
	 * @Decription
	 *
	 * @param job
	 * @return
	 */
	public void update(OrgnaizationEntity Orgnaization) throws BusinessException ;
	
	/**
	 * 查询一条部门记录
	 * @author xiehs/hexj
	 * @createtime 2014年12月9日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public OrgnaizationEntity get(String id);
	
	/**
	 * 查询部门列表
	 * @author xiehs/hexj
	 * @createtime 2014年12月9日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<OrgnaizationEntity> queryList(String params) ;
	public List<OrgnaizationEntity> queryList();
	
	/**
	 * 通过属性查询部门列表
	 * @author xiehs/hexj
	 * @createtime 2014年12月9日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<OrgnaizationEntity> queryListByPorperty(String propertyName,String value);
	
	/**
	 * hibernate部门分页列表
	 * @author xiehs/hexj
	 * @createtime 2014年12月9日 上午11:34:16
	 * @Decription   
	 * @param cq
	 * @param b
	 */
    public void getDataGridReturn(CriteriaQuery cq, boolean b);
	
	/**
	 * 判断字段记录是否唯一
	 * @author xiehs/hexj
	 * @createtime 2014年12月9日 上午11:25:48
	 * @Decription 
	 * @param param
	 * @return
	 */
	public boolean isUnique(Map<String,String> param,String propertyName);
	
	/**
	 * 查询员工分配可控制的机构树
	 * @param userId
	 * @param parentId
	 * @return
	 * @throws BusinessException
	 */
	public List<OrgTreeVo> queryOrgTreeByGrade(String userId, String parentId);
	
	/**
	 * 查询员工分配部门机构树
	 * @param userId
	 * @param parentId
	 * @return
	 * @throws BusinessException
	 */
	public List<OrgTreeVo> queryEmpOrgTree(String userId, String parentId);
	
	/**
	 * 查询可控制机构树
	 * @param userId
	 * @param parentId
	 * @return
	 * @throws BusinessException
	 */
	public List<OrgTreeVo> queryOrgTree(String userId, String parentId) throws BusinessException;
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年10月4日 下午2:17:06
	 * @Decription 通过符号隔开的ids查询对应的部门名称
	 *
	 * @param deptIds ids
	 * @param split 分隔符
	 * @return
	 */
	public String queryOrgNameByIds(String deptIds,String split);

	public List<OrgnaizationEntity> queryControllerOrg(String empId) throws IOException, ClassNotFoundException;

	public List<OrgnaizationEntity> getLowOrg(OrgnaizationEntity org);
	
	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月25日 下午4:25:47
	 * @Decription 获机构下所有岗位
	 *
	 * @param org
	 * @return
	 */
	public List<OrgnaizationEntity> getLowJob(OrgnaizationEntity org);
	
	public void deleteCascade(String id) throws BusinessException;
	
	public List<OrgnaizationEntity> getUpOrg(String userId);

}
