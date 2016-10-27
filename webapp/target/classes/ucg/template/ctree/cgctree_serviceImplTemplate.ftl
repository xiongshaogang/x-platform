package ${bussiPackage}.${entityPackage}.service.impl;

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
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import ${bussiPackage}.${entityPackage}.service.${entityName}Service;
import ${bussiPackage}.${entityPackage}.dao.${entityName}Dao;
import ${bussiPackage}.${entityPackage}.entity.${entityName}Entity;

/**
 * 
 * description :${ftl_description}service实现
 * @version 1.0
 * @createtime : ${ftl_create_time}
 * 
 */
@Service("${entityName?uncap_first}Service")
public class ${entityName}ServiceImpl implements ${entityName}Service {

    private static final Logger logger = Logger.getLogger(${entityName}ServiceImpl.class);

    @Resource
	private ${entityName}Dao ${entityName?uncap_first}Dao;
	
	@Resource
	private BaseService baseService;

	public void set${entityName}Dao(${entityName}Dao ${entityName?uncap_first}Dao) {
		this.${entityName?uncap_first}Dao = ${entityName?uncap_first}Dao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	
 	@Override
 	@Action(moduleCode="${cgformConfig.baseFormEntity.moduleCode}",description="${ftl_description}保存",detail="${ftl_description}保存成功", execOrder = ActionExecOrder.AFTER)
	public String save(${entityName}Entity ${entityName?uncap_first}) throws Exception {
		// TODO Auto-generated method stub
		String pk="";
		try {
			pk=this.${entityName?uncap_first}Dao.add${entityName}(${entityName?uncap_first});
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("${ftl_description}保存失败");
		}
		logger.info("${ftl_description}保存成功");
		return pk;
	}
	
	
	@Override
	@Action(moduleCode="${cgformConfig.baseFormEntity.moduleCode}",description="${ftl_description}删除",detail="${ftl_description}删除成功", execOrder = ActionExecOrder.AFTER)
	public void delete(String id) throws Exception {
		// TODO Auto-generated method stub
		try {
			this.${entityName?uncap_first}Dao.delete${entityName}(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("${ftl_description}删除失败");
		}
		logger.info("${ftl_description}删除成功");
	}


	@Override
	@Action(moduleCode="${cgformConfig.baseFormEntity.moduleCode}",description="${ftl_description}批量删除",detail="${ftl_description}批量删除成功", execOrder = ActionExecOrder.AFTER)
	public void batchDelete(String ids) throws Exception {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id);
			}
		}
		logger.info("${ftl_description}批量删除成功");
	}

	@Override
	@Action(moduleCode="${cgformConfig.baseFormEntity.moduleCode}",description="${ftl_description}更新",detail="${ftl_description}更新成功", execOrder = ActionExecOrder.AFTER)
	public void update(${entityName}Entity ${entityName?uncap_first}) throws BusinessException {
		// TODO Auto-generated method stub
		try {
			${entityName}Entity oldEntity = get(${entityName?uncap_first}.getId());
			MyBeanUtils.copyBeanUpdateBean(oldEntity,${entityName?uncap_first});
			this.${entityName?uncap_first}Dao.merge(${entityName?uncap_first});
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("${ftl_description}更新失败");
			throw new BusinessException("${ftl_description}更新失败");
		}
		logger.info("${ftl_description}更新成功");
	}

	@Override
	public ${entityName}Entity get(String id){
		// TODO Auto-generated method stub
		${entityName}Entity ${entityName?uncap_first}=null;
		try {
			${entityName?uncap_first}=this.${entityName?uncap_first}Dao.get${entityName}(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("${ftl_description}获取失败");
		}
		logger.info("${ftl_description}获取成功");
		return ${entityName?uncap_first};
	}

	@Override
	public List<${entityName}Entity> queryList(){
		// TODO Auto-generated method stub
		List<${entityName}Entity> ${entityName?uncap_first}List=new ArrayList<${entityName}Entity>();
		try {
			${entityName?uncap_first}List=this.${entityName?uncap_first}Dao.query${entityName}List();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("${ftl_description}获取列表失败");
		}
		logger.info("${ftl_description}获取列表成功");
		return ${entityName?uncap_first}List;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b){
		// TODO Auto-generated method stub
		try {
			this.${entityName?uncap_first}Dao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("${ftl_description}获取分页列表失败");
		}
		logger.info("${ftl_description}获取分页列表成功");
	}
	
}