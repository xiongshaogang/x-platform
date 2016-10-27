package com.xplatform.base.workflow.definition.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import com.xplatform.base.form.entity.AppFormApproveUser;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.util.DateUtils;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;

/**
 * 
 * description : 资源管理service
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:34:52
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 上午11:34:52
 *
 */
public interface DefinitionService {
	
	/**
	 * 新增资源
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:41
	 * @Decription 
	 * @param Definition
	 * @return
	 */
	public String save(DefinitionEntity Definition) throws BusinessException;
	
	/**
	 * 删除资源
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:32:56
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public void delete(String id,boolean isOnlyVersion) throws BusinessException;
	
	/**
	 * 批量删除资源
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:06
	 * @Decription
	 *
	 * @param ids
	 * @return
	 */
	public void batchDelete(String ids,boolean isOnlyVersion) throws BusinessException;
	
	/**
	 * 更新资源
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:25
	 * @Decription
	 *
	 * @param Definition
	 * @return
	 */
	public void update(DefinitionEntity Definition) throws BusinessException;
	
	/**
	 * 查询一条资源记录
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public DefinitionEntity get(String id);
	
	/**
	 * 根据属性查询列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:34
	 * @Decription
	 *
	 * @param id
	 * @return
	 */
	public List<DefinitionEntity> findByPropertitys(Map<String,String> param);
	
	/**
	 * 查询资源列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:33:54
	 * @Decription
	 *
	 * @return
	 */
	public List<DefinitionEntity> queryList();
	
	/**
	 * hibernate资源分页列表
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:34:16
	 * @Decription   
	 * @param cq
	 * @param b
	 */
    public void getDataGridReturn(CriteriaQuery cq, boolean b);
	
	/**
	 * 判断字段记录是否唯一
	 * @author xiehs
	 * @createtime 2014年5月24日 上午11:25:48
	 * @Decription 
	 * @param param
	 * @return
	 */
	public boolean isUnique(Map<String,String> param,String propertyName);
	
	/**
	 * 构造流程的分类
	 * @author xiehs
	 * @createtime 2014年7月2日 下午3:16:56
	 * @Decription
	 *
	 * @param typeKey
	 * @return
	 */
	public String getTypeListByKey(String typeKey,String userId);
	
	/**
	 * 保存或更新流程定义
	 * @author xiehs
	 * @createtime 2014年8月19日 下午4:04:16
	 * @Decription
	 *
	 * @param definition
	 * @param isDeploy
	 * @param actXml
	 */
	public String saveOrUpdate(DefinitionEntity definition,boolean isDeploy,String actXml);
	/**
	 * 流程发布
	 * @author xiehs
	 * @createtime 2014年8月19日 下午4:04:08
	 * @Decription
	 *
	 * @param definition
	 * @param actDefXml
	 */
	public void deploy(DefinitionEntity definition,String actDefXml) throws BusinessException;
	/**
	 * 根据activiti定义的到流程定义扩展
	 * @author xiehs
	 * @createtime 2014年8月19日 下午4:03:33
	 * @Decription
	 *
	 * @param actDefId
	 * @return
	 */
	public DefinitionEntity getByActDefId(String actDefId);
	/**
	 * 根据activiti定义的Key和最新版本得到流程定义扩展
	 * @author xiehs
	 * @createtime 2014年8月26日 上午11:35:17
	 * @Decription
	 *
	 * @param actDefKey
	 * @return
	 */
	public DefinitionEntity getMainDefByActDefKey(String actDefKey);
	/**
	 * 根据activiti定义的Key和得到流程定义扩展集合
	 * @author xiehs
	 * @createtime 2014年10月1日 上午11:35:17
	 * @Decription
	 *
	 * @param actDefKey
	 * @return
	 */
	public List<DefinitionEntity> getByActDefKey(String actDefKey);
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年10月28日 下午2:41:04
	 * @Decription 将map中的key(日期类型)降序排序
	 *
	 * @param 
	 * @return
	 */
	public Map.Entry[] getTimeSortedHashtableByKey(Map map) ;
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年10月28日 下午4:40:13
	 * @Decription 根据排序后的map的Entry[]数组去产生 Timeline插件读取的json数据
	 *
	 * @param entrys
	 * @return
	 */
	public JSONObject getTimeLineJSONData(Map.Entry[] entrys);
	
	/**
	 * 动态表单获取流程定义的xml
	 * @param formId
	 * @return
	 */
	public Map<String,Object> getDeployXml(String formId,String businessKey);
}
