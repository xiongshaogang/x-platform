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
	public String save(${entityName}Entity ${entityName?uncap_first}) throws BusinessException {
		// TODO Auto-generated method stub
		String pk="";
		try {
			${entityName}Entity parent=this.get(${entityName?uncap_first}.getParent().getId());
			//第一步，保存信息
			pk=this.${entityName?uncap_first}Dao.add${entityName}(${entityName?uncap_first});
			if(parent!=null){
				//第二步，修改树的详细信息
				${entityName?uncap_first}.setId(pk);
				${entityName?uncap_first}.setTreeIndex(parent.getTreeIndex()+","+pk);
				${entityName?uncap_first}.setLevel(parent.getLevel()+1);
				${entityName?uncap_first}.setIsLeaf("1");
				this.${entityName?uncap_first}Dao.update${entityName}(${entityName?uncap_first});
				//第三步，更新父节点的信息
				if("1".equals(parent.getIsLeaf())){//父节点是叶子节点
					parent.setIsLeaf("0");
					this.${entityName?uncap_first}Dao.update${entityName}(parent);
				}
			}else{
				${entityName?uncap_first}.setId(pk);
				${entityName?uncap_first}.setTreeIndex(pk);
				${entityName?uncap_first}.setLevel(1);
				${entityName?uncap_first}.setIsLeaf("1");
				this.${entityName?uncap_first}Dao.update${entityName}(${entityName?uncap_first});
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("${ftl_description}保存失败");
			throw new BusinessException("${ftl_description}保存失败");
		}
		logger.info("${ftl_description}保存成功");
		return pk;
	}

	@Override
	@Action(moduleCode="${cgformConfig.baseFormEntity.moduleCode}",description="${ftl_description}删除",detail="${ftl_description}删除成功", execOrder = ActionExecOrder.AFTER)
	public String delete(String id) throws BusinessException {
		// TODO Auto-generated method stub
		String str = "";
		try {
			${entityName}Entity ${entityName?uncap_first} = this.get(id);
			if("0".equals(${entityName?uncap_first}.getIsLeaf())){
				str = "请先删除子节点";
			}else{
				${entityName}Entity parent=null;
				//获取父节点
				if(${entityName?uncap_first}!=null && !StringUtil.isEmpty(${entityName?uncap_first}.getParent().getId())){
					parent=${entityName?uncap_first}.getParent();
					//设置父节点是否叶子节点
					if(parent.getChildren()!=null && parent.getChildren().size()<=1){
						parent.setIsLeaf("1");
					}
					//修改节点
					this.update(parent);
				}
				this.${entityName?uncap_first}Dao.delete${entityName}(id);
				str = "删除成功";
			}
			
		} catch (Exception e) {
			logger.error("${ftl_description}表删除失败");
			str = "删除失败";
			throw new BusinessException("${ftl_description}表删除失败");
		}
		logger.info("${ftl_description}表删除成功");
		return str;
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
	
	@Override
	public List<${entityName}Entity> queryListByPorperty(String propertyName, String value) throws BusinessException {
		// TODO Auto-generated method stub
		return this.${entityName?uncap_first}Dao.findByProperty(${entityName}Entity.class, propertyName, value);
	}
}