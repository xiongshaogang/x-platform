package com.xplatform.base.form.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import jodd.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.form.dao.FlowFormDao;
import com.xplatform.base.form.entity.AppFormField;
import com.xplatform.base.form.entity.AppFormUser;
import com.xplatform.base.form.entity.FlowFormEntity;
import com.xplatform.base.form.service.AppFormApproveUserService;
import com.xplatform.base.form.service.AppFormFieldService;
import com.xplatform.base.form.service.AppFormTableService;
import com.xplatform.base.form.service.AppFormUserDataService;
import com.xplatform.base.form.service.AppFormUserService;
import com.xplatform.base.form.service.FlowFormService;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.service.CommonService;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.PinyinUtil;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.platform.common.def.ConfigConst;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.system.type.entity.TypeEntity;
/**
 * 
 * description :资源service实现
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
@Service("flowFormService")
public class FlowFormServiceImpl extends BaseServiceImpl<FlowFormEntity> implements FlowFormService {
	private static final Logger logger = Logger.getLogger(FlowFormServiceImpl.class);
	@Resource
	private FlowFormDao flowFormDao;
	@Resource
	private AppFormApproveUserService appFormApproveUserService;
	@Resource
	private AppFormUserService appFormUserService;
	@Resource
	private AppFormUserDataService appFormUserDataService;
	@Resource
	private AppFormFieldService appFormFieldService;
	@Resource
	private AppFormTableService appFormTableService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private CommonService commonService;
	private AjaxJson result = new AjaxJson();
	@Resource
	public void setBaseDao(FlowFormDao flowFormDao) {
		super.setBaseDao(flowFormDao);
	}

	@Override
	public String saveFlowForm(JSONObject jsonObject,Integer isDeploy,String jsonStr,String content) throws BusinessException {
		
		JSONObject fFJsonObject = (JSONObject)jsonObject.get("FlowFormEntity");
		FlowFormEntity flowForm = new FlowFormEntity();
		flowForm = this.commonSave(flowForm, fFJsonObject);
		flowForm.setContent(content);
		flowForm.setFieldJson(jsonObject.getString("AppFormField"));
		flowForm.setFieldJsonTemp(jsonStr);
		flowForm.setStatus(isDeploy);

		//判断是否走审批流程
		if(fFJsonObject.getInt("isStartAssign") == 1){
			flowForm.setIsStartAssign(1);
			flowForm.setIsFlow(1);
			flowForm.setIsEdit(1);
			flowForm.setViewType("detail");
		}else{
			flowForm.setIsStartAssign(0);
			JSONObject afauJson = (JSONObject)jsonObject.get("AppFormApproveUser");
			if (afauJson != null) {
				JSONArray jsonArray = JSONArray.fromObject(afauJson.get("userList"));
				if (jsonArray.size() > 0) { // 如果有审批人
					flowForm.setIsFlow(1);
					flowForm.setIsEdit(1);
					flowForm.setViewType("detail");
				} else {// 没有审批人
					flowForm.setIsFlow(0);
					flowForm.setIsEdit(0);
					flowForm.setViewType("update");
				}
			}else{
				// 没有审批人
				flowForm.setIsFlow(0);
				flowForm.setIsEdit(0);
				flowForm.setViewType("update");
			}
		}
		

		
		String pk = "";
		//-----------------------------------------保存FlowFormEntity表单----------------

		//初始化设置版本为1,发布状态为0,并设置TypeEntity和code
		flowForm.setCode(getCode(PinyinUtil.converterToFirstSpell(flowForm.getName())));
		flowForm.setVersion(0);
		
		//1.保存实体
		pk = (String)this.flowFormDao.save(flowForm);
		//2.维护treeIndex
		if(!(flowForm.getParentId().equals("-1"))){
			FlowFormEntity parent = this.get(flowForm.getParentId());
			
			//flowForm.setId(pk);
			flowForm.setTreeIndex(parent.getTreeIndex() + "," + flowForm.getCode());
			flowForm.setLevel(parent.getLevel() + 1);
			flowForm.setIsLeaf("1");
			flowForm.setMainFormCode(parent.getCode());
			this.flowFormDao.updateEntitie(flowForm);
			//3.更新父节点信息
			if (StringUtil.equals(parent.getIsLeaf(), "1")) {// 父节点是叶子节点
				parent.setIsLeaf("0");
				this.flowFormDao.updateEntitie(parent);
			}
		}else{
			//flowForm.setId(pk);
			flowForm.setTreeIndex(flowForm.getCode());
			flowForm.setLevel(1);
			flowForm.setIsLeaf("1");
			flowForm.setMainFormCode(flowForm.getCode());
			this.flowFormDao.updateEntitie(flowForm);
		}
		//----------------------------------------保存AppFormUser表单----------------------------
		this.saveFunction(flowForm, jsonObject, pk, fFJsonObject);
/*		JSONObject apuJsonObject = (JSONObject)jsonObject.get("AppFormUser");
		if(apuJsonObject != null){
		this.appFormUserService.saveOrDeleteAppForm(pk, apuJsonObject);
		}else{
			if(!(flowForm.getParentId().equals("-1"))){
				List<AppFormUser> appFormUserList = this.appFormUserService.getAppFormList(flowForm.getParentId());
				for(AppFormUser appFormUser : appFormUserList){
					AppFormUser APU = new AppFormUser();
					APU.setFormId(pk);
					APU.setUserId(appFormUser.getUserId());
					APU.setViewStatus(appFormUser.getViewStatus());
					this.appFormUserService.save(APU);
				}
			}else{
				AppFormUser appFormUser = new AppFormUser();
				appFormUser.setFormId(pk);
				appFormUser.setUserId(ClientUtil.getUserId());
				appFormUser.setViewStatus(1);
				this.appFormUserService.save(appFormUser);
			}
		}
		
		//----------------------------------------保存AppFormUserData---------------------------
		JSONObject apudJsonObject = (JSONObject)jsonObject.get("AppFormUserData");
		if(apudJsonObject != null){
		this.appFormUserDataService.saveOrDeleteAppFormData(pk, apudJsonObject);
		}
		//----------------------------------------保存AppFormApproveUser------------------------
		if(fFJsonObject.getInt("isStartAssign") == 0){
			JSONObject afauJsonObject = (JSONObject)jsonObject.get("AppFormApproveUser");
			if(afauJsonObject != null){
			this.appFormApproveUserService.saveOrDeleteAFAU(pk, afauJsonObject);
			}
		}*/
		//----------------------------------------保存AppFormField------------------------
		/*JSONObject affJsonObject = (JSONObject)jsonObject.get("AppFormField");
		if(affJsonObject != null){
		this.appFormFieldService.saveAppFormField(pk,affJsonObject);
		}*/
		
		return pk;
	}

	@Override
	public String updateFlowForm(JSONObject jsonObject,Integer isDeploy,String jsonStr,String content) throws Exception {
		JSONObject fFJsonObject = (JSONObject)jsonObject.get("FlowFormEntity");
		String pk="";
		if(isDeploy == 0){
			//修改逻辑
		    pk = fFJsonObject.getString("id");
			FlowFormEntity flowForm = new FlowFormEntity();
			flowForm = this.commonSave(flowForm, fFJsonObject);
			flowForm.setContent(content);
			//flowForm.setIsEdit(fFJsonObject.getInt("isEdit"));
			flowForm.setFieldJson(jsonObject.optString("AppFormField"));
			flowForm.setFieldJsonTemp(jsonStr);
			flowForm.setStatus(isDeploy);
			if(StringUtil.isNotEmpty(fFJsonObject.optString("createUserId"))){
				flowForm.setCreateUserId(fFJsonObject.optString("createUserId"));
				flowForm.setCreateUserName(fFJsonObject.optString("createUserName"));
				flowForm.setUpdateUserId(fFJsonObject.optString("createUserId"));
				flowForm.setUpdateUserName(fFJsonObject.optString("createUserName"));
			}
/*			if(fFJsonObject.getInt("isSaveEdit") == 1){
				flowForm.setViewType("update");
			}else{
				flowForm.setViewType("detail");
			}*/
			//判断是否走审批流程
			if(fFJsonObject.optInt("isStartAssign") == 1){
				flowForm.setIsStartAssign(1);
				flowForm.setIsFlow(1);
				flowForm.setIsEdit(1);
				flowForm.setViewType("detail");
			}else{
				flowForm.setIsStartAssign(0);
				JSONObject afauJson = (JSONObject)jsonObject.opt("AppFormApproveUser");
				if (afauJson != null) {
					JSONArray jsonArray = JSONArray.fromObject(afauJson.opt("userList"));
					if (jsonArray.size() > 0) { // 如果有审批人
						flowForm.setIsFlow(1);
						flowForm.setIsEdit(1);
						flowForm.setViewType("detail");
					} else {// 没有审批人
						flowForm.setIsFlow(0);
						flowForm.setIsEdit(0);
						flowForm.setViewType("update");
					}
				}else{
					// 没有审批人
					flowForm.setIsFlow(0);
					flowForm.setIsEdit(0);
					flowForm.setViewType("update");
				}
			}

			
			flowForm.setId(fFJsonObject.optString("id"));
			FlowFormEntity isExistFF = this.get(fFJsonObject.optString("id"));
			if(isExistFF != null){
				flowForm.setCode(isExistFF.getCode());
				flowForm.setVersion(isExistFF.getVersion());
				MyBeanUtils.copyBeanNotNull2Bean(flowForm, isExistFF);
			}else{
				flowForm.setCode(getCode(PinyinUtil.converterToFirstSpell(flowForm.getName())));
				flowForm.setVersion(fFJsonObject.getInt("version")+1);
			}
			
			//2.维护treeIndex
			if(!(isExistFF.getParentId().equals("-1"))){
				FlowFormEntity parent = this.get(flowForm.getParentId());
				
				//isExistFF.setId(pk);
				isExistFF.setTreeIndex(parent.getTreeIndex() + "," + flowForm.getCode());
				isExistFF.setLevel(parent.getLevel() + 1);
				isExistFF.setIsLeaf("1");
				isExistFF.setMainFormCode(parent.getCode());
				//3.更新父节点信息
				if (StringUtil.equals(parent.getIsLeaf(), "1")) {// 父节点是叶子节点
					parent.setIsLeaf("0");
					this.flowFormDao.updateEntitie(parent);
				}
			}else{
				//isExistFF.setId(pk);
				isExistFF.setTreeIndex(flowForm.getCode());
				isExistFF.setLevel(1);
				isExistFF.setIsLeaf("1");
				isExistFF.setMainFormCode(isExistFF.getCode());
			}
			
			this.flowFormDao.updateEntitie(isExistFF);
			
			//----------------------------------------保存AppFormUser表单----------------------------
			this.saveFunction(flowForm, jsonObject, pk, fFJsonObject);
/*			JSONObject apuJsonObject = (JSONObject)jsonObject.get("AppFormUser");
			if(apuJsonObject != null){
			this.appFormUserService.saveOrDeleteAppForm(pk, apuJsonObject);
			}else{
				if(!(flowForm.getParentId().equals("-1"))){
					List<AppFormUser> appFormUserList = this.appFormUserService.getAppFormList(flowForm.getParentId());
					for(AppFormUser appFormUser : appFormUserList){
						AppFormUser APU = new AppFormUser();
						APU.setFormId(pk);
						APU.setUserId(appFormUser.getUserId());
						APU.setViewStatus(appFormUser.getViewStatus());
						this.appFormUserService.save(APU);
					}
				}else{
					AppFormUser appFormUser = new AppFormUser();
					appFormUser.setFormId(pk);
					appFormUser.setUserId(ClientUtil.getUserId());
					appFormUser.setViewStatus(1);
					this.appFormUserService.save(appFormUser);
				}
			}
			
			//----------------------------------------保存AppFormUserData---------------------------
			JSONObject apudJsonObject = (JSONObject)jsonObject.get("AppFormUserData");
			if(apudJsonObject != null){
			this.appFormUserDataService.saveOrDeleteAppFormData(pk, apudJsonObject);
			}
			//----------------------------------------保存AppFormApproveUser------------------------
			if(fFJsonObject.getInt("isStartAssign") == 0){
				JSONObject afauJsonObject = (JSONObject)jsonObject.get("AppFormApproveUser");
				if(afauJsonObject != null){
				this.appFormApproveUserService.saveOrDeleteAFAU(pk, afauJsonObject);
				}
			}*/
			//----------------------------------------保存AppFormField------------------------
		/*	JSONObject affJsonObject = (JSONObject)jsonObject.get("AppFormField");
			if(affJsonObject != null){
			this.appFormFieldService.saveAppFormField(affJsonObject,flowForm.getCode());
			}*/
			
		}else{
			//发布逻辑
			FlowFormEntity flowForm = new FlowFormEntity();
			flowForm = this.commonSave(flowForm, fFJsonObject);
			//flowForm.setIsEdit(fFJsonObject.getInt("isEdit"));
			flowForm.setStatus(isDeploy);
			flowForm.setContent(content);
			//flowForm.setFieldJson(jsonObject.getString("AppFormField"));
			flowForm.setFieldJson(jsonObject.getString("AppFormField"));
			flowForm.setFieldJsonTemp(jsonStr);

			//-----------------------------------------保存FlowFormEntity表单----------------

			//判断是否走审批流程
			if(fFJsonObject.getInt("isStartAssign") == 1){
				flowForm.setIsStartAssign(1);
				flowForm.setIsFlow(1);
				flowForm.setIsEdit(1);
				flowForm.setViewType("detail");
			}else{
				flowForm.setIsStartAssign(0);
				JSONObject afauJson = (JSONObject)jsonObject.get("AppFormApproveUser");
				if (afauJson != null) {
					JSONArray jsonArray = JSONArray.fromObject(afauJson.get("userList"));
					if (jsonArray.size() > 0) { // 如果有审批人
						flowForm.setIsFlow(1);
						flowForm.setIsEdit(1);
						flowForm.setViewType("detail");
					} else {// 没有审批人
						flowForm.setIsFlow(0);
						flowForm.setIsEdit(0);
						flowForm.setViewType("update");
					}
				}else{
					// 没有审批人
					flowForm.setIsFlow(0);
					flowForm.setIsEdit(0);
					flowForm.setViewType("update");
				}
			}
			

			
			FlowFormEntity isExistFF = this.get(fFJsonObject.getString("id"));
			if(isExistFF != null){
				flowForm.setCode(isExistFF.getCode());
				//判断是否邀加赠版本
				if(isExistFF.getStatus() == 0){
					flowForm.setVersion(isExistFF.getVersion()+1);
					this.flowFormDao.deleteEntityById(isExistFF.getId());
				}else{
					flowForm.setVersion(isExistFF.getVersion()+1);
				}
				
				//isExistFF.setStatus(0);
			}else{
				flowForm.setCode(getCode(PinyinUtil.converterToFirstSpell(flowForm.getName())));
				flowForm.setVersion(fFJsonObject.getInt("version")+1);
			}


		//	flowForm.setType(type);

			//1.保存实或者更新实体
/*			if(isExistFF != null){
				flowForm.setId(UUIDGenerator.generate());
				pk = flowForm.getId();
				//MyBeanUtils.copyBeanNotNull2Bean(flowForm, isExistFF);
				//this.flowFormDao.updateEntitie(isExistFF);
				this.flowFormDao.save(flowForm);
				//pk = isExistFF.getId();
				
				//2.维护treeIndex
				if(!(isExistFF.getParentId().equals("-1"))){
					FlowFormEntity parent = this.get(isExistFF.getParentId());
					
					//isExistFF.setId(pk);
					isExistFF.setTreeIndex(parent.getTreeIndex() + "," + pk);
					isExistFF.setLevel(parent.getLevel() + 1);
					isExistFF.setIsLeaf("1");
					this.flowFormDao.updateEntitie(isExistFF);
					//3.更新父节点信息
					if (StringUtil.equals(parent.getIsLeaf(), "1")) {// 父节点是叶子节点
						parent.setIsLeaf("0");
						this.flowFormDao.updateEntitie(parent);
					}
				}else{
					//isExistFF.setId(pk);
					isExistFF.setTreeIndex(pk);
					isExistFF.setLevel(1);
					isExistFF.setIsLeaf("1");
					this.flowFormDao.updateEntitie(isExistFF);
				}
			}else{*/
				pk = (String)this.flowFormDao.save(flowForm);
				
				//2.维护treeIndex
				if(!(flowForm.getParentId().equals("-1"))){
					FlowFormEntity parent = this.get(flowForm.getParentId());
					
					//flowForm.setId(pk);
					flowForm.setTreeIndex(parent.getTreeIndex() + "," + flowForm.getCode());
					flowForm.setLevel(parent.getLevel() + 1);
					flowForm.setIsLeaf("1");
					flowForm.setMainFormCode(parent.getCode());
					this.flowFormDao.updateEntitie(flowForm);
					//3.更新父节点信息
					if (StringUtil.equals(parent.getIsLeaf(), "1")) {// 父节点是叶子节点
						parent.setIsLeaf("0");
						this.flowFormDao.updateEntitie(parent);
					}
				}else{
					flowForm.setMainFormCode(flowForm.getCode());
					//flowForm.setId(pk);
					if(flowForm.getVersion() == 1){
						flowForm.setTreeIndex(flowForm.getCode());
						flowForm.setLevel(1);
						flowForm.setIsLeaf("1");
						this.flowFormDao.updateEntitie(flowForm);
					}else{
						flowForm.setTreeIndex(isExistFF.getTreeIndex());
						flowForm.setLevel(isExistFF.getLevel());
						flowForm.setIsLeaf(isExistFF.getIsLeaf());
					}
				}
			
			//保存appFormUser和appFormData、appFormApproveUser方法
			this.saveFunction(flowForm, jsonObject, pk, fFJsonObject);
			
			//----------------------------------------保存AppFormField------------------------
			JSONObject affJsonObject = (JSONObject)jsonObject.get("AppFormField");
			if(affJsonObject != null){
			this.appFormFieldService.saveAppFormField(affJsonObject,flowForm.getCode(),flowForm.getMainFormCode());
			}
		}
		return pk;
		
		
	}
	
	/**
	 * 设置flowform表单的基本属性的公共方法
	 * @param flowForm
	 * @param fFJsonObject
	 * @return
	 * @throws BusinessException
	 */
	public FlowFormEntity commonSave(FlowFormEntity flowForm,JSONObject fFJsonObject) throws BusinessException{
		//设置组织机构id
		if(StringUtil.isNotEmpty(ClientUtil.getUserEntity().getOrgIds())){
			flowForm.setOrgId(ClientUtil.getUserEntity().getOrgIds());
		}else{
			flowForm.setOrgId("-1");
		}
		String name = fFJsonObject.getString("name").replaceAll(" ", "");
		name = name.replaceAll("\\pP|\\pS", "");
		flowForm.setName(name);
		
		if(StringUtil.isNotEmpty(fFJsonObject.optString("description"))){
			flowForm.setDescription(fFJsonObject.getString("description"));
		}else{
			flowForm.setDescription("");
		}
		
		//判断是否发布到共享文件夹
		if(StringUtil.isNotEmpty(fFJsonObject.optString("isSharefolder"))){
			flowForm.setIsSharefolder(fFJsonObject.getInt("isSharefolder"));
		}else{
			flowForm.setIsSharefolder(0);
		}
		
		if(StringUtil.isNotEmpty(fFJsonObject.optString("parentId"))){
			flowForm.setParentId(fFJsonObject.getString("parentId"));
		}else{
			flowForm.setParentId("-1");
		}
		
		if(fFJsonObject.optString("logo") == null  || fFJsonObject.optString("logo") ==""){
			flowForm = this.getRandomLogo(flowForm);
		}else{
			flowForm.setLogo(fFJsonObject.getString("logo"));
		}
		
		//设置typeId
		TypeEntity type = new TypeEntity();
		type.setId(ConfigConst.flowFormDefaultTypeId);
		flowForm.setType(type);
		//写死
		flowForm.setIsAutoDefinition(1);
		//设置url，但缺少content
		flowForm.setUrl(ConfigConst.DEFAULT_URL);
		
		//判断是否有父节点，无父节点则为app应用
		if(flowForm.getParentId().equals("-1")){
			flowForm.setIsApp(1);
		}else{
			flowForm.setIsApp(0);
		}
		
		if(fFJsonObject.opt("notifyType") != null){
			flowForm.setNotifyType(fFJsonObject.getInt("notifyType"));
		}else{
			flowForm.setNotifyType(0);
		}
		
		return flowForm;
	}
	
	public void saveFunction(FlowFormEntity flowForm,JSONObject jsonObject,String pk,JSONObject fFJsonObject) throws BusinessException{
		//----------------------------------------保存AppFormUser表单----------------------------
		JSONObject apuJsonObject = (JSONObject)jsonObject.get("AppFormUser");
		if(apuJsonObject != null){
		this.appFormUserService.saveOrDeleteAppForm(pk, apuJsonObject);
		}else{
			if(!(flowForm.getParentId().equals("-1"))){
				List<AppFormUser> appFormUserList = this.appFormUserService.getAppFormList(flowForm.getParentId());
				for(AppFormUser appFormUser : appFormUserList){
					AppFormUser APU = new AppFormUser();
					APU.setFormId(pk);
					APU.setUserId(appFormUser.getUserId());
					APU.setViewStatus(appFormUser.getViewStatus());
					APU.setType(appFormUser.getType());
					this.appFormUserService.save(APU);
				}
			}else{
				AppFormUser appFormUser = new AppFormUser();
				appFormUser.setFormId(pk);
				appFormUser.setUserId(ClientUtil.getUserId());
				appFormUser.setViewStatus(1);
				appFormUser.setType("user");
				AppFormUser isExist = this.appFormUserService.getAppFormUser(pk, ClientUtil.getUserId());
				if(isExist == null){
					this.appFormUserService.save(appFormUser);
				}
			}
		}
		
		//----------------------------------------保存AppFormUserData---------------------------
		/*JSONObject apudJsonObject = (JSONObject)jsonObject.get("AppFormUserData");
		if(apudJsonObject != null){
		this.appFormUserDataService.saveOrDeleteAppFormData(pk, apudJsonObject);
		}*/
		//----------------------------------------保存AppFormApproveUser------------------------
		if(fFJsonObject.getInt("isStartAssign") == 0){
			JSONObject afauJsonObject = (JSONObject)jsonObject.get("AppFormApproveUser");
			if(afauJsonObject != null){
			this.appFormApproveUserService.saveOrDeleteAFAU(pk, afauJsonObject);
			}
		}
	}

	@Override
	public Map<Object,Object> getFlowForm(String formId,String formCode,int status) throws BusinessException {
		Map<Object,Object> map = new HashMap<Object,Object>();
		//String sql = "select * from FlowFormEntity where version = (select max(version) from FlowFormEntity where fromId=? and status=?)";
		String sql = "from FlowFormEntity where id=?";
		FlowFormEntity flowFormList = this.flowFormDao.findUniqueByHql(sql, formId);
		map.put("FlowFormEntity", flowFormList);
		List<Map<String,Object>> appFormUserList = this.appFormUserService.queryAppFormList(formId);
		map.put("AppFormUser", appFormUserList);
		List<Map<String,Object>> appFormUserDataList = this.appFormUserDataService.queryFormDataList(formId);
		map.put("AppFormUserData", appFormUserDataList);
		List<Map<String,Object>> appFormApproveUser = this.appFormApproveUserService.queryAFAUList(formId);
		map.put("AppFormApproveUser", appFormApproveUser);
		List<Map<String,Object>> appFormField = this.appFormFieldService.queryAFFList(formCode);
		map.put("AppFormField", appFormField);
		String currentCids = this.getCidList(formId);
		map.put("currentCids", currentCids);
		if(!("-1".equals(flowFormList.getParentId()))){
			FlowFormEntity parentFlowFormList = this.get(flowFormList.getParentId());
			map.put("moduleName", parentFlowFormList.getName());
		}
		
		return map;
	}

	/**
	 * 获取我发布的模板
	 */
	@Override
	public List<Map<String,Object>> queryMyCreateFlowFormList(String userId,String orgId) throws BusinessException {
		//String hql = "from FlowFormEntity";
		String hql = "SELECT a.id,a.name,a.code,a.description,a.url,a.content,a.version,a.parentId,a.logo,a.isApp,a.isFlow,a.isEdit,a.type_id,a.status,a.fieldJson,a.createTime "
				+ "FROM t_flow_form a "
				+ "WHERE a.createUserId=? and a.orgId=? and a.status !=2  and a.version = (select max(m.version) from t_flow_form m where m.code=a.code) ORDER BY a.createTime DESC";
		List<Map<String,Object>> list= this.flowFormDao.findForJdbc(hql, userId,orgId);
		Map<String,Map<String,Object>> result=new  HashMap<String,Map<String,Object>>();
		if(list!=null && list.size()>0){
			//把主模板加入数组中
			for(Map<String,Object> p:list){
				if(p.get("parentId")!=null && StringUtil.equals("-1", (String)p.get("parentId"))){//主模版
					result.put((String)p.get("id"), p);
				}
			}
			//加入子模版
			for(Map<String,Object> map:list){
				if(map.get("parentId")!=null && !StringUtil.equals("-1", (String)map.get("parentId"))){//关联模版
					Map<String,Object> parent=result.get((String)map.get("parentId"));
					if(parent!=null){
						if(parent.get("children")!=null){
							List<Map<String,Object>> children= (List<Map<String,Object>>)parent.get("children");
							children.add(map);
						}else{
							List<Map<String,Object>> children= new ArrayList<Map<String,Object>>();
							children.add(map);
							parent.put("children", children);
						}
					}
				}
			}
			return new ArrayList<Map<String,Object>>(result.values());
		}
		return null;
	}
	
	/**
	 * 获取我能看见的模板
	 */
	@Override
	public List<Map<String,Object>> queryFlowFormList(String userId) throws BusinessException {
		//String hql = "from FlowFormEntity";
		String hql = "SELECT a.id,a.name,a.code,a.description,a.url,a.content,a.version,a.parentId,a.logo,a.isApp,a.isFlow,a.isEdit,a.type_id,a.status,a.fieldJson FROM t_flow_form a,t_app_form_user b WHERE a.ID=b.formId and a.status =1 AND b.userId=?";
		return this.flowFormDao.findForJdbc(hql, userId);
	}
	
	@Override
	public List<Map<String,Object>> queryAPPList(String userId,String orgId) throws BusinessException {
		//String hql = "from FlowFormEntity";
		String orgIds = this.sysUserService.getOrgIdsByUserId(userId);
		String orgSql = "";
		if(StringUtil.isNotEmpty(orgIds) && !StringUtil.equals("'"+OrgnaizationEntity.DEFAULT_ID+"'", userId)){
			orgSql = " UNION ALL "
				+" SELECT a.id,a.name,a.code,a.parentId,a.createTime,a.description,a.url,a.content,a.isApp,a.isEdit,a.type_id,a.fieldJson,a.version,a.logo,a.`status`,a.isFlow,a.notifyType,a.viewType,b.viewStatus "
				+" FROM "
				+" t_flow_form a,t_app_form_user b "
				+" WHERE "
				+" a.ID = b.formId AND b.userId IN ( "
				+orgIds
				+" ) "
				+" AND b.type = 'org' AND a.orgId = '"+orgId+"' ";
		}
		String sql = "SELECT DISTINCT * "
				+" FROM "
				+" (SELECT a.id,a.name,a.code,a.parentId,a.createTime,a.description,a.url,a.content,a.isApp,a.isEdit,a.type_id,a.fieldJson,a.version,a.logo,a.`status`,a.isFlow,a.notifyType,a.viewType,b.viewStatus "
				+" FROM t_flow_form a,t_app_form_user b "
				+" WHERE "
				+" a.ID = b.formId AND b.userId = '"+userId+"' AND a.orgId = '"+orgId+"' AND b.type = 'user' "
				+orgSql/*" UNION ALL "
				+" SELECT a.id,a.name,a.code,a.parentId,a.createTime,a.description,a.url,a.content,a.isApp,a.isEdit,a.type_id,a.fieldJson,a.version,a.logo,a.`status`,a.isFlow,a.notifyType,a.viewType,b.viewStatus "
				+" FROM "
				+" t_flow_form a,t_app_form_user b "
				+" WHERE "
				+" a.ID = b.formId AND b.userId IN ( "
				+" SELECT m.id "
				+" FROM "
				+" t_org_orgnaization m "
				+" WHERE "
				+" m.treeIndex LIKE '%"+orgId+"%' "
				+" ) "
				+" AND b.type = 'org' "*/
				+" UNION ALL "
				+" SELECT t.id,t.name,t.code,t.parentId,t.createTime,t.description,t.url,t.content,t.isApp,t.isEdit,t.type_id,t.fieldJson,t.version,t.logo,t.`status`,t.isFlow,t.notifyType,t.viewType,b.viewStatus "
				+" FROM "
				+" t_flow_form t,t_app_form_user b "
				+" WHERE "
				+" b.userId IN ( "
				+" SELECT roleId "
				+" FROM "
				+" t_org_user_role "
				+" WHERE "
				+" userId = '"+userId+"' "
				+" and orgId = '"+orgId+"' "
				+" ) "
				+" AND t.id = b.formId AND b.type = 'role' AND t.orgId = '"+orgId+"'"
				+" ) n "
				+" WHERE "
				+" n.`status` = 1 AND n.viewStatus = 1 AND n.parentId = '-1' "
				+" AND n.version = ( "
				+" SELECT "
				+" max(m.version) "
				+" FROM "
				+" t_flow_form m "
				+" WHERE "
				+" m.code = n.code "
				+" ) "
				+"AND n.code NOT IN("
				+"select formCode "
				+" from "
				+" t_app_form_forbidden f "
				+" where "
				+" f.userId = '"+userId+"' "
				+" and f.orgId = '"+orgId+"' "
				+" )"
				+" ORDER BY "
				+"n.createTime DESC";
		
		//String hql = "SELECT a.id,a.name,a.code,a.description,a.url,a.content,a.version,a.parentId,a.logo,a.isApp,a.isFlow,a.isEdit,a.viewType,a.type_id,a.status,a.fieldJson FROM t_flow_form a,t_app_form_user b WHERE a.ID=b.formId AND b.userId=? and a.orgId=? and b.viewStatus = 1 and a.status = 1 and a.parentId ='-1' and a.version = (select max(m.version) from t_flow_form m where m.code=a.code) ORDER BY a.createTime DESC";    
		//return this.flowFormDao.findForJdbc(hql, userId,orgId);
		return this.flowFormDao.findForJdbc(sql);
	}
	
	@Override
	public List<Map<String,Object>> queryAPPAllList(String userId,String orgId) throws BusinessException {
		//String hql = "from FlowFormEntity";
		String orgIds = this.sysUserService.getOrgIdsByUserId(userId);
		String orgSql = "";
		if(StringUtil.isNotEmpty(orgIds) && !StringUtil.equals("'"+OrgnaizationEntity.DEFAULT_ID+"'", userId)){
			orgSql = " UNION ALL "
				+" SELECT a.id,a.name,a.code,a.parentId,a.createTime,a.description,a.url,a.content,a.isApp,a.isEdit,a.type_id,a.fieldJson,a.version,a.logo,a.`status`,a.isFlow,a.notifyType,b.viewStatus "
				+" FROM "
				+" t_flow_form a,t_app_form_user b "
				+" WHERE "
				+" a.ID = b.formId AND b.userId IN ( "
				+orgIds
				+" ) "
				+" AND b.type = 'org' AND a.orgId = '"+orgId+"' ";
		}
		String sql = "SELECT DISTINCT * "
				+" FROM "
				+" (SELECT a.id,a.name,a.code,a.parentId,a.createTime,a.description,a.url,a.content,a.isApp,a.isEdit,a.type_id,a.fieldJson,a.version,a.logo,a.`status`,a.isFlow,a.notifyType,b.viewStatus "
				+" FROM t_flow_form a,t_app_form_user b "
				+" WHERE "
				+" a.ID = b.formId AND b.userId = '"+userId+"' AND a.orgId = '"+orgId+"' AND b.type = 'user' "
				+orgSql/*" UNION ALL "
				+" SELECT a.id,a.name,a.code,a.parentId,a.createTime,a.description,a.url,a.content,a.isApp,a.isEdit,a.type_id,a.fieldJson,a.version,a.logo,a.`status`,a.isFlow,a.notifyType,b.viewStatus "
				+" FROM "
				+" t_flow_form a,t_app_form_user b "
				+" WHERE "
				+" a.ID = b.formId AND b.userId IN ( "
				+" SELECT m.id "
				+" FROM "
				+" t_org_orgnaization m "
				+" WHERE "
				+" m.treeIndex LIKE '%"+orgId+"%' "
				+" ) "
				+" AND b.type = 'org' "*/
				+" UNION ALL "
				+" SELECT t.id,t. NAME,t. CODE,t.parentId,t.createTime,t.description,t.url,t.content,t.isApp,t.isEdit,t.type_id,t.fieldJson,t.version,t.logo,t.`status`,t.isFlow,t.notifyType,b.viewStatus "
				+" FROM "
				+" t_flow_form t,t_app_form_user b "
				+" WHERE "
				+" b.userId IN ( "
				+" SELECT roleId "
				+" FROM "
				+" t_org_user_role "
				+" WHERE "
				+" userId = '"+userId+"' "
				+" and orgId = '"+orgId+"' "
				+" ) "
				+" AND t.id = b.formId AND b.type = 'role' AND t.orgId = '"+orgId+"'"
				+" ) n "
				+"WHERE "
				+"n.`status` = 1 AND n.viewStatus in (0,1) AND n.parentId = '-1' "
				+"AND n.version = ("
				+"SELECT "
				+" max(m.version) "
				+" FROM "
				+" t_flow_form m "
				+" WHERE "
				+" m. code = n. code "
				+")"
				+"AND n.code NOT IN("
				+"select formCode "
				+" from "
				+" t_app_form_forbidden f "
				+" where "
				+" f.userId = '"+userId+"' "
				+" and f.orgId = '"+orgId+"' "
				+" )"
				+" ORDER BY "
				+" n.createTime DESC";
		List<Map<String,Object>> available = this.flowFormDao.findForJdbc(sql);
		if(StringUtil.isNotEmpty(orgIds) && !StringUtil.equals("'"+OrgnaizationEntity.DEFAULT_ID+"'", userId)){
			orgSql = " UNION ALL "
				+" SELECT a.id,a.name,a.code,a.parentId,a.createTime,a.description,a.url,a.content,a.isApp,a.isEdit,a.type_id,a.fieldJson,a.version,a.logo,a.`status`,a.isFlow,a.notifyType "
				+" FROM "
				+" t_flow_form a,t_app_form_user b "
				+" WHERE "
				+" a.ID = b.formId AND b.userId IN ( "
				+orgIds
				+" ) "
				+" AND b.type = 'org' AND a.orgId = '"+orgId+"' ";
		}
		String sql1 = "SELECT DISTINCT * "
				+" FROM "
				+" (SELECT a.id,a.name,a.code,a.parentId,a.createTime,a.description,a.url,a.content,a.isApp,a.isEdit,a.type_id,a.fieldJson,a.version,a.logo,a.`status`,a.isFlow,a.notifyType "
				+" FROM t_flow_form a,t_app_form_user b "
				+" WHERE "
				+" a.ID = b.formId AND b.userId = '"+userId+"' AND a.orgId = '"+orgId+"' AND b.type = 'user' "
				+orgSql/*" UNION ALL "
				+" SELECT a.id,a.name,a.code,a.parentId,a.createTime,a.description,a.url,a.content,a.isApp,a.isEdit,a.type_id,a.fieldJson,a.version,a.logo,a.`status`,a.isFlow,a.notifyType "
				+" FROM "
				+" t_flow_form a,t_app_form_user b "
				+" WHERE "
				+" a.ID = b.formId AND b.userId IN ( "
				+" SELECT m.id "
				+" FROM "
				+" t_org_orgnaization m "
				+" WHERE "
				+" m.treeIndex LIKE '%"+orgId+"%' "
				+" ) "
				+" AND b.type = 'org' "*/
				+" UNION ALL "
				+" SELECT t.id,t. NAME,t. CODE,t.parentId,t.createTime,t.description,t.url,t.content,t.isApp,t.isEdit,t.type_id,t.fieldJson,t.version,t.logo,t.`status`,t.isFlow,t.notifyType "
				+" FROM "
				+" t_flow_form t,t_app_form_user b "
				+" WHERE "
				+" b.userId IN ( "
				+" SELECT roleId "
				+" FROM "
				+" t_org_user_role "
				+" WHERE "
				+" userId = '"+userId+"' "
				+" and orgId = '"+orgId+"' "
				+" ) "
				+" AND t.id = b.formId AND b.type = 'role' AND t.orgId = '"+orgId+"'"
				+" ) n "
				+"WHERE "
				+"n.`status` = 1 AND n.parentId = '-1' "
				+"AND n.version = ("
				+"SELECT "
				+" max(m.version) "
				+" FROM "
				+" t_flow_form m "
				+" WHERE "
				+" m. code = n. code "
				+")"
				+"AND n.code IN("
				+"select formCode "
				+" from "
				+" t_app_form_forbidden f "
				+" where "
				+" f.userId = '"+userId+"' "
				+" and f.orgId = '"+orgId+"' "
				+" )"
				+" ORDER BY "
				+" n.createTime DESC";
		List<Map<String,Object>> forbidden = this.flowFormDao.findForJdbc(sql1);
		
		for(int i=0;i<forbidden.size();i++){
			Map<String,Object> mapData = forbidden.get(i);
			mapData.put("viewStatus", 0);
			available.add(mapData);
		}
		//String hql = "SELECT a.id,a.name,a.code,a.version,a.logo,a.isFlow,a.notifyType,b.viewStatus FROM t_flow_form a,t_app_form_user b WHERE a.ID=b.formId AND b.userId=? and a.orgId=? and a.status = 1 and b.viewStatus in (0,1) and a.parentId ='-1' and a.version = (select max(m.version) from t_flow_form m where m.code=a.code) ORDER BY a.createTime DESC";    
		//return this.flowFormDao.findForJdbc(hql, userId,orgId);
		return available;
	}
	
	//-----------------------------------------测试----------------------------------------------------------------------
	@Override
	public List<Map<String, Object>> querySingle(String table, String field, Map params,
			int page, int rows) throws BusinessException {
		StringBuilder sqlB = new StringBuilder();
		dealQuerySql(table,field,params,sqlB);
		List<Map<String, Object>> result = commonService.findForJdbcParam(sqlB.toString(), page, rows);
		return result;
	}
	
	private void dealQuerySql(String table, String field, Map params,StringBuilder sqlB){
		sqlB.append(" SELECT ");
		for (String f : field.split(",")) {
			sqlB.append(f);
			sqlB.append(",");
		}
		sqlB.deleteCharAt(sqlB.length() - 1);
		sqlB.append(" FROM " + table);
		if (params.size() >= 1) {
			sqlB.append(" WHERE 1=1 ");
			Iterator it = params.keySet().iterator();
			while (it.hasNext()) {
				String key = String.valueOf(it.next());
				String value = String.valueOf(params.get(key));
				if (!StringUtil.isEmpty(value) && !"null".equals(value)) {
						sqlB.append(" AND ");
						sqlB.append(" " + key +  value );
				}
			}
		}
	}



	@Override
	public FlowFormEntity getFlowFormByCode(String code) throws BusinessException {
		String sql = "from FlowFormEntity where code=? and version = (select max(version) from FlowFormEntity where code = ?)";
		return this.flowFormDao.findUniqueByHql(sql, code,code);
	}
	
	@Override
	public FlowFormEntity getFlowFormByCodeAll(String code) throws BusinessException {
		String sql = "from FlowFormEntity where code=? and version = (select max(version) from FlowFormEntity where code = ? )";
		return this.flowFormDao.findUniqueByHql(sql, code,code);
	}
	
	//判断当前code是否与数据库中code一样，一样则改，不一样则返回code
	public String getCode(String code) throws BusinessException {

		for (int i = 0; i < 1; i++) {
			FlowFormEntity isExist = this.getFlowFormByCode(code);
			if (isExist != null) {
				if (isExist.getCode().equals(code)) {
					String number = isExist.getCode().replaceAll("\\D+", "");
					if (StringUtil.isNotEmpty(number)) {
						int newNumner = Integer.parseInt(number) + 1;
						code = code.replaceAll("\\d+", "");
						code = code + newNumner;
						i = -1;
					} else {
						code = code + "1";
						i = -1;
					}
				}
			}
		}
		return code;
	}

	@Override
	public List<FlowFormEntity> queryRelaFlFoList(String formId,String formCode) throws BusinessException {
		String hql = "";
		FlowFormEntity flowForm = this.getFlowFormByCode(formCode);
		if("-1".equals(flowForm.getParentId())){
			hql = "from FlowFormEntity t where t.treeIndex like '%"+formCode+",%' and t.status = 1 and t.code != ? and t.version=(select max(m.version) from FlowFormEntity m where m.code=t.code)";
		}else{
			hql = "from FlowFormEntity t where t.treeIndex like '%,"+formCode+",%' and t.status = 1 and t.code != ? and t.version=(select max(m.version) from FlowFormEntity m where m.code=t.code)";
		}
		return this.flowFormDao.findHql(hql,formCode);
	}

	@Override
	public String getCidList(String formCode) throws BusinessException {
		String cids = "";
		String hql = "from AppFormField where formCode=?";
		List<AppFormField> appFormFieldList = this.appFormFieldService.findHql(hql, formCode);
		Map<String,String> mapList = new HashMap<String,String>();
		for(AppFormField AFF : appFormFieldList){
			if(StringUtil.isNotEmpty(AFF.getMyCid())){
				if(mapList.containsKey(AFF.getMyCid())){
					//存在，则不往里添加
				}else{
					mapList.put(AFF.getMyCid(),AFF.getMyCid());
				}
			}else{
				if(mapList.containsKey(AFF.getParentId())){
					//存在，则不往里添加
				}else{
					mapList.put(AFF.getParentId(),AFF.getParentId());
				}
			}
		}
		  for (String key : mapList.keySet()) {
			   System.out.println("key= "+ key + " and value= " + mapList.get(key));
			   if(cids == ""){
				   cids = mapList.get(key);
			   }else{
				   cids = cids + ","+mapList.get(key);
			   }
			  }
		return cids;
	}

	@Override
	public List<FlowFormEntity> queryMyFlowFormList(String userId,String code) throws BusinessException {
		String hql = "SELECT a from FlowFormEntity a,AppFormUser b where a.status=1 and a.id=b.formId and b.userId=? and a.code != ? and a.version=(select max(m.version) from FlowFormEntity m where m.code=a.code)";
		return this.flowFormDao.findHql(hql,userId,code);
	}
	
	@Override
	public List<FlowFormEntity> queryMyFlowFormList1(String userId,String orgId) throws BusinessException {
		String hql = "SELECT a from FlowFormEntity a,AppFormUser b where a.status=1 and a.id=b.formId and b.userId=? and a.orgId=? and a.version=(select max(m.version) from FlowFormEntity m where m.code=a.code) ORDER BY a.createTime DESC ";
		return this.flowFormDao.findHql(hql,userId,orgId);
	}
	
	@Override
	public FlowFormEntity queryLastestVersionFlowForm(String formCode) {
		String hql = "SELECT a FROM FlowFormEntity a WHERE version=(SELECT MAX(version) as version FROM FlowFormEntity as b WHERE code=?) AND code=?";
		return this.flowFormDao.findUniqueByHql(hql, formCode, formCode);
	}

	@Override
	public List<Map<String, Object>> myApplication(String userId,int page,int rows) throws Exception {
		List<Map<String, Object>> myApplicationData = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> allMyAppCode = this.queryAPPAllList(userId,ClientUtil.getUserEntity().getOrgId());
		for(int i=0;i<allMyAppCode.size();i++){
			String code = (String)allMyAppCode.get(i).get("code");
			Map<String, Object> myMap = this.appFormTableService.getFieldData(code, userId, page, rows);
			List<Map<String, Object>> listData = (List<Map<String, Object>>)myMap.get("extras");
			if(listData.size()>0){
				myApplicationData.add(myMap);
			}
		}
		return myApplicationData;
	}
	
	public FlowFormEntity getRandomLogo(FlowFormEntity flowForm) throws BusinessException {
		String finalPath = ApplicationContextUtil.getRealPath("/basic/img/logo");
		File file = new File(finalPath);
		String test[];
		if(file.list().length>0){
			test = file.list();
			for (int i = 0; i < test.length; i++) {
				test[i] = test[i].substring(0, test[i].length() - 4);
			}
			Random r = new Random();
			flowForm.setLogo(test[r.nextInt(test.length)]);
		}
		
		return flowForm;
	}

	@Override
	public List<Map<String, Object>> queryOrgAppList(String orgId) throws BusinessException {
		String sql = " SELECT a.id,a.name,a.code,a.parentId,a.mainFormCode,a.createTime,a.description,a.url,a.content,a.isApp,a.isEdit,a.type_id,a.fieldJson,a.version,a.logo,a.`status`,a.isFlow,a.notifyType,a.viewType "
				+" FROM "
				+" t_flow_form a "
				+" WHERE "
				+" a.orgId = '"+orgId+"'"
				+" AND a.`status` = 1 AND a.parentId = '-1' "
				+" AND a.version = ("
				+"SELECT "
				+" max(m.version) "
				+" FROM "
				+" t_flow_form m "
				+" WHERE "
				+" m. code = a. code "
				+")"
				+" ORDER BY "
				+" a.createTime DESC";
		
		return this.flowFormDao.findForJdbc(sql);
	}
	
	@Override
	public List<FlowFormEntity> queryFlowFormByParentId(String parentId) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("parentId", parentId);
		return this.flowFormDao.findByObjectPropertys(param);
	}
	
}
