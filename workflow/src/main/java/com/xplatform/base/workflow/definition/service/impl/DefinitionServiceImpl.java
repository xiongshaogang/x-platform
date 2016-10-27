package com.xplatform.base.workflow.definition.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import jodd.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.form.entity.AppFormApproveUser;
import com.xplatform.base.form.entity.FlowInstanceUserEntity;
import com.xplatform.base.form.service.AppFormApproveUserService;
import com.xplatform.base.form.service.FlowInstanceUserService;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.DateUtils;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.system.type.mybatis.vo.TypeTreeVo;
import com.xplatform.base.system.type.service.TypeService;
import com.xplatform.base.workflow.core.bpm.model.NodeCache;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.definition.dao.DefinitionDao;
import com.xplatform.base.workflow.definition.entity.DefinitionEntity;
import com.xplatform.base.workflow.definition.service.DefinitionService;
import com.xplatform.base.workflow.instance.service.ProcessInstFormService;
import com.xplatform.base.workflow.instance.service.ProcessInstanceService;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;
import com.xplatform.base.workflow.node.service.NodeMessageService;
import com.xplatform.base.workflow.node.service.NodeRuleService;
import com.xplatform.base.workflow.node.service.NodeScriptService;
import com.xplatform.base.workflow.node.service.NodeSetService;
import com.xplatform.base.workflow.node.service.NodeSignService;
import com.xplatform.base.workflow.node.service.NodeUserService;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.service.TaskDueService;
import com.xplatform.base.workflow.task.service.TaskDueStateService;
import com.xplatform.base.workflow.task.service.TaskNodeStatusService;
import com.xplatform.base.workflow.task.service.TaskOpinionService;
import com.xplatform.base.workflow.task.service.TaskSignDataService;

/**
 * 
 * description :流程定义service实现
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
@Service("definitionService")
public class DefinitionServiceImpl implements DefinitionService {
	private static final Logger logger = Logger.getLogger(DefinitionServiceImpl.class);
	@Resource
	private DefinitionDao definitionDao;
	@Resource
	private NodeSetService nodeSetService;
	@Resource
	private BaseService baseService;
	@Resource
	private TypeService typeService;
	@Resource
	private FlowService flowService;
	@Resource
	private ProcessInstFormService processInstFormService;
	@Resource
	private NodeMessageService nodeMessageService;
	@Resource
	private NodeRuleService nodeRuleService;
	@Resource
	private NodeScriptService nodeScriptService;
	@Resource
	private TaskNodeStatusService taskNodeStatusService;
	@Resource
	private TaskDueService taskDueService;
	@Resource
	private TaskDueStateService taskDueStateService;
	@Resource
	private ProcessInstanceService processInstanceService;
	@Resource
	private TaskOpinionService taskOpinionService;
	@Resource
	private NodeUserService nodeUserService;
	@Resource
	private TaskSignDataService taskSignDataService;
	@Resource
	private NodeSignService nodeSignService;
	@Resource
	private AppFormApproveUserService appFormApproveUserService;
	@Resource
	private FlowInstanceUserService flowInstanceUserService;
	@Override
	public String save(DefinitionEntity Definition) throws BusinessException {
		// TODO Auto-generated method stub
		String pk="";
		try {
			pk=this.definitionDao.addDefinition(Definition);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("流程定义保存失败");
			throw new BusinessException("流程定义保存失败");
		}
		logger.info("流程定义保存成功");
		return pk;
	}
	
	public String saveOrUpdate(DefinitionEntity definition,boolean isDeploy,String actXml){
		String oldDefId = definition.getId();
		String pk="";
		definition.setStatus("Y");//设置状态
		String published = isDeploy ? DefinitionEntity.PUBLISHED_YES: DefinitionEntity.PUBLISHED_NO;
		definition.setPublished(published);
		try {
			//没有id,新增流程定义（可以发布，也可以不发布）
			if (definition==null || (definition!=null && StringUtil.isEmpty(definition.getId()))) {
				if (isDeploy) {
					Deployment deployment = this.flowService.deploy(definition.getName(), actXml);
					ProcessDefinitionEntity ent = this.flowService.getProcessDefinitionByDeployId(deployment.getId());
					definition.setActDeployId(deployment.getId());
					definition.setActId(ent.getId());
					definition.setActKey(ent.getKey());
				}
				definition.setVersion(1);
				definition.setIsMain(DefinitionEntity.MAIN);
				pk=save(definition);
				definition.setId(pk);
				if (isDeploy) {//发布，那么需要加入节点信息到nodeSet
					this.nodeSetService.saveOrUpdateNodeSet(actXml, definition, true);
				}
			} else if (isDeploy) {//修改时，新增版本
				//第一步：发布流程
				Deployment deployment = this.flowService.deploy(definition.getName(), actXml);
				ProcessDefinitionEntity ent = this.flowService.getProcessDefinitionByDeployId(deployment.getId());
				definition.setActDeployId(deployment.getId());
				definition.setActId(ent.getId());
				definition.setActKey(ent.getKey());
				definition.setVersion(Integer.valueOf(ent.getVersion()));
				definition.setIsMain(DefinitionEntity.MAIN);
				DefinitionEntity oldEntity = get(oldDefId);
				MyBeanUtils.copyBeanNotNull2Bean(oldEntity,definition);
				definition.setId(null);
				//第二部：保存一份到自己定义的流程定义表中
				this.definitionDao.getSession().clear();
			    pk=save(definition);
				definition.setId(pk);
				//第三步：保存流程节点到自己定义的流程节点表中
				this.nodeSetService.saveOrUpdateNodeSet(actXml, definition, true);
				//第四步：同步全局节点（之前设置的全局节点）
				syncStartGlobal(oldDefId, pk, definition.getActId());
				//第五步：修改以前的版本
				this.definitionDao.updateSubVersions(pk, definition.getCode());
				//第六步：修改当前的流程定义
				definition.setIsMain(DefinitionEntity.MAIN);
				definition.setParent(definition);
				this.definitionDao.updateDefinition(definition);
			} else {//只是修改，不添加版本
				if (definition.getActDeployId() != null) {
					//第一步：修改activit的xml
					this.flowService.wirteDefXml(definition.getActDeployId().toString(), actXml);
					//第二步：修改节点信息
					this.nodeSetService.saveOrUpdateNodeSet(actXml, definition, false);
					String actDefId = definition.getActId();
					definition.setPublished("Y");//设置为已发布，只是修改了内容
					//第三步：清空缓存
					NodeCache.clear(actDefId);
				}
				//第四步：修改流程定义扩展信息
				this.update(definition);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return pk;
	}
	
	private void syncStartGlobal(String oldDefId, String newDefId,
			String newActDefId) throws Exception {
		List<NodeSetEntity> list = this.nodeSetService.getByOther(oldDefId);
		try {
			for (NodeSetEntity nodeSet : list) {
				nodeSet.setDefId(newDefId);
				nodeSet.setActDefId(newActDefId);
				nodeSet.setId(null);
				this.nodeSetService.save(nodeSet);
				int a=3;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new Exception();
		}
		
	}

	@Override
	public void delete(String id,boolean isOnlyVersion) throws BusinessException {
		// TODO Auto-generated method stub
		try {
			if(StringUtil.isEmpty(id)){
				throw new BusinessException("删除失败，选择流程为空");
			}
			DefinitionEntity definition = get(id);
			if(definition==null){
				throw new BusinessException("删除失败，流程为空");
			}
			//还没有发布，直接删除流程定义扩展
			if (definition.getActDeployId() == null) {
				this.definitionDao.deleteDefinition(id);
				return;
			}
			//发布了，删除一个版本流程定义和所有的配置
			if (isOnlyVersion) {
				deleteActivitiDefinition(definition);
				return;
			}
			//发布了，删除所有版本流程定义和所有的配置
			String actFlowKey = definition.getActKey();
			List<DefinitionEntity> list = this.getByActDefKey(actFlowKey);
			for (DefinitionEntity def : list) {
				deleteActivitiDefinition(def);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("流程定义删除失败");
			throw new BusinessException("流程定义删除失败");
		}
		logger.info("流程定义删除成功");
	}
	
	private void deleteActivitiDefinition(DefinitionEntity definition) throws BusinessException{
		String actDeployId = definition.getActDeployId();
		String defId = definition.getId();
		String actDefId = definition.getActId();
		if (StringUtil.isNotEmpty(actDefId)) {
			this.processInstFormService.deleteByActDefId(actDefId);
			this.nodeMessageService.deleteByActDefId(actDefId);
			this.nodeRuleService.deleteByActDefId(actDefId);
			this.nodeScriptService.deleteByActDefId(actDefId);
			this.taskNodeStatusService.deleteByActDefId(actDefId);
			this.taskDueService.deleteByActDefId(actDefId);
			this.processInstanceService.deleteByActDefId(actDefId);
			this.taskOpinionService.deleteByActDefId(actDefId);
			this.taskDueStateService.deleteByActDefId(actDefId);
			this.nodeUserService.deleteByDefId(defId);
			this.taskSignDataService.deleteByActDefId(actDefId);
			this.nodeSignService.deleteByActDefId(actDefId);
		}
		if (StringUtil.isNotEmpty(actDeployId)) {
			this.delProcDefByActDeployId(actDeployId);
		}
		//删除节点配置
		this.nodeSetService.deleteByDefId(defId);
		//删除流程定义扩展
		this.definitionDao.deleteDefinition(defId);
	}
	
	private void delProcDefByActDeployId(String actDeployId){
		this.definitionDao.executeSql("DELETE FROM ACT_RE_PROCDEF WHERE DEPLOYMENT_ID_=?", actDeployId);
		this.definitionDao.executeSql("DELETE FROM ACT_GE_BYTEARRAY WHERE DEPLOYMENT_ID_=?", actDeployId);
		this.definitionDao.executeSql("DELETE FROM ACT_RE_DEPLOYMENT WHERE ID_=?", actDeployId);
	}

	@Override
	public void batchDelete(String ids,boolean isOnlyVersion) throws BusinessException {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(ids)){
			String[] idArr=StringUtil.split(ids, ",");
			for(String id:idArr){
				this.delete(id,isOnlyVersion);
			}
		}
		logger.info("流程定义批量删除成功");
	}

	@Override
	public void update(DefinitionEntity Definition) throws BusinessException {
		// TODO Auto-generated method stub
		try {
			DefinitionEntity oldEntity = get(Definition.getId());
			MyBeanUtils.copyBeanNotNull2Bean(Definition, oldEntity);
			this.definitionDao.merge(oldEntity);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("流程定义更新失败");
			throw new BusinessException("流程定义更新失败");
			
		}
		logger.info("流程定义更新成功");
	}

	@Override
	public DefinitionEntity get(String id){
		// TODO Auto-generated method stub
		DefinitionEntity Definition=null;
		try {
			Definition=this.definitionDao.getDefinition(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("流程定义获取失败");
		}
		logger.info("流程定义获取成功");
		return Definition;
	}
	
	public List<DefinitionEntity> findByPropertitys(Map<String,String> param){
		return this.definitionDao.findByPropertys(DefinitionEntity.class, param);
	}

	@Override
	public List<DefinitionEntity> queryList(){
		// TODO Auto-generated method stub
		List<DefinitionEntity> DefinitionList=new ArrayList<DefinitionEntity>();
		try {
			DefinitionList=this.definitionDao.queryDefinitionList();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("流程定义获取列表失败");
		}
		logger.info("流程定义获取列表成功");
		return DefinitionList;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b){
		// TODO Auto-generated method stub
		try {
			this.definitionDao.getDataGridReturn(cq, true);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("流程定义获取分页列表失败");
		}
		logger.info("流程定义获取分页列表成功");
	}

	@Override
	public boolean isUnique(Map<String, String> param,String propertyName){
		logger.info(propertyName+"字段唯一校验");
		String newValue = param.get("newValue");
		String oldValue = param.get("oldValue");
		if (newValue == null || StringUtil.equals(newValue, oldValue)) {//修改同一条记录
			return true;
		}
		List<DefinitionEntity> list= this.definitionDao.findByProperty(DefinitionEntity.class, propertyName, newValue);
		if(list!=null &&list.size()>0){//数据库记录不唯一，返回false
			return false;
		}else{
			return true;
		}
	}
	
	
	public String getTypeListByKey(String typeKey,String userId) {
		Map<String,String> param=new HashMap<String,String>();
		param.put("userId", userId);
		param.put("sysType", typeKey);
		List<TypeTreeVo> list = null;
		try {
			list = typeService.queryTypeRoleTreeBySysType(param);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//this.globalTypeDao.getByCatKey(catKEY);

		StringBuffer sb = new StringBuffer("<folder id='0' label='全部'>");
		if (BeanUtils.isNotEmpty(list)) {
			for (TypeTreeVo gt : list){
				if ("-1".equals(gt.getParentId())) {
					sb.append("<folder id='" + gt.getId() + "' label='"
							+ gt.getName() + "'>");
					sb.append(getBmpChildList(list, gt.getId()));
					sb.append("</folder>");
				}
			}	
		}
		sb.append("</folder>");
		return sb.toString();
	}

	private String getBmpChildList(List<TypeTreeVo> list, String parentId) {
		StringBuffer sb = new StringBuffer("");
		if (BeanUtils.isNotEmpty(list)) {
			for (TypeTreeVo gt : list) {
				if ( gt.getParentId().equals(parentId)) {
					sb.append("<folder id='" + gt.getId() + "' label='"
							+ gt.getName() + "'>");
					sb.append(getBmpChildList(list, gt.getId()));
					sb.append("</folder>");
				}
			}
		}
		return sb.toString();
	}
	
	public void deploy(DefinitionEntity definition,String actDefXml) throws BusinessException{
		try {
			Deployment deployment = this.flowService.deploy(definition.getName(), actDefXml);
			ProcessDefinitionEntity ent = this.flowService.getProcessDefinitionByDeployId(deployment.getId());
			definition.setActDeployId(deployment.getId());
			definition.setActId(ent.getId());
			definition.setActKey(ent.getKey());
			definition.setStatus(DefinitionEntity.STATUS_ENABLED);
			definition.setPublished("Y");
			update(definition);
			this.nodeSetService.saveOrUpdateNodeSet(actDefXml, definition, false);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BusinessException("流程发布失败");
		}
	}
	
	@Override
	public DefinitionEntity getByActDefId(String actDefId){
		List<DefinitionEntity> list=this.definitionDao.findByProperty(DefinitionEntity.class, "actId", actDefId);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	@Override
	public DefinitionEntity getMainDefByActDefKey(String actDefKey){
		Map<String,String> param=new HashMap<String,String>();
		param.put("actKey", actDefKey);
		param.put("isMain", "1");
		List<DefinitionEntity> list=this.definitionDao.findByPropertys(DefinitionEntity.class,param);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	@Override
	public List<DefinitionEntity> getByActDefKey(String actDefKey){
		Map<String,String> param=new HashMap<String,String>();
		param.put("actKey", actDefKey);
		List<DefinitionEntity> list=this.definitionDao.findByPropertys(DefinitionEntity.class,param);
		return list;
	}
	
	@Override
	public Map.Entry[] getTimeSortedHashtableByKey(Map map) {
		Set set = map.entrySet();
		Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
		Arrays.sort(entries, new Comparator() {
			/**
			* 如果d1小于d2,返回一个负数;如果d1大于d2，返回一个正数;如果他们相等，则返回0;
			*/
			public int compare(Object arg0, Object arg1) {
				Object key1 = ((Map.Entry) arg0).getKey();
				Object key2 = ((Map.Entry) arg1).getKey();
				Date d1 = null;
				Date d2 = null;
				try {
					d1 = DateUtils.parseDateWZTime(key1.toString());
					d2 = DateUtils.parseDateWZTime(key2.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return d1.getTime() < d2.getTime() ? 1 : -1;
			}
		});
		return entries;
	}
	
	public JSONObject getTimeLineJSONData(Map.Entry[] entrys) {
		JSONObject data = new JSONObject();
		JSONArray dataItems = new JSONArray();
		//构造各个日期组data数据(每个日期下可能有多审批意见)
		for (Map.Entry<String, List<TaskOpinionEntity>> entry : entrys) {
			JSONObject dataItem = new JSONObject();
			dataItem.accumulate("t_lable", entry.getKey());
			dataItem.accumulate("t_lable_type", "label-primary");
			List<TaskOpinionEntity> list1 = entry.getValue();
			//构造每个日期下的t_items数据
			JSONArray t_items = new JSONArray();
			for (TaskOpinionEntity t1 : list1) {
				JSONObject j1 = new JSONObject();
				j1.accumulate("t_item_info_img", t1.getPortrait80());
				j1.accumulate("t_item_info_time",t1.getUpdateTime().toLocaleString());
				j1.accumulate("t_item_content_header_color", "");
				j1.accumulate("t_item_content_header_user", t1.getExeUserName());
				j1.accumulate("t_item_content_header_apporveStr", t1.getCheckStatusStr());
				j1.accumulate("t_item_content_header_task", t1.getTaskName());
				j1.accumulate("t_item_content_header_taskId", t1.getTaskId());
				j1.accumulate("t_item_content_header_cost", t1.getDurTime());
				j1.accumulate("t_item_content_header_time", DateUtils.formatDatetimeWZTime(t1.getEndTime()));

				/* 构造t_item_content_header_toolbar begin */
				JSONArray header_toolbar = new JSONArray();
				JSONObject j2 = new JSONObject();
				j2.accumulate("tool_action", "collapse");
				j2.accumulate("tool_icon", "awsm-icon-chevron-up");
				j2.accumulate("tool_link", "#");
				header_toolbar.add(j2);
				/* 构造t_item_content_header_toolbar end */

				j1.accumulate("t_item_content_header_toolbar", header_toolbar);
				j1.accumulate("t_item_content_body_content", t1.getOpinion());

				/* 构造t_item_content_body_toolbar begin */
				JSONArray body_toolbars = new JSONArray();
				JSONObject j3 = new JSONObject();
				j3.accumulate("tool_position_class", "pull_left");

				JSONArray tool_tools = new JSONArray();
				JSONObject j4 = new JSONObject();
				j4.accumulate("tool_icon", "awsm-icon-flag-checkered");
				j4.accumulate("tool_color", "grey");
				j4.accumulate("tool_text", t1.getCheckStatusStr());
				tool_tools.add(j4);

				j3.accumulate("tool_tools", tool_tools);
				body_toolbars.add(j3);
				/* 构造t_item_content_body_toolbar end */
				j1.accumulate("t_item_content_body_toolbar", body_toolbars);

				t_items.add(j1);
			}

			dataItem.accumulate("t_items", t_items);
			dataItems.add(dataItem);
		}
		data.accumulate("data", dataItems);
		return data;
	}
	
	@Override
	public Map<String,Object> getDeployXml(String formId,String businessKey){
		StringBuffer sb=new StringBuffer();
		Map<String,Object> result=new HashMap<String,Object>();
		if(StringUtil.isNotEmpty(businessKey)){
			try {
				List<FlowInstanceUserEntity> list=flowInstanceUserService.queryFIUListByBus(businessKey);
				String start="<diagram xmlns:bg=\"bpm.graphic\" xmlns:ciied=\"com.ibm.ilog.elixir.diagram\" xmlns:fg=\"flash.geom\">"
							  +"<bg:StartEvent id=\"startEvent1\" height=\"49\" width=\"31\" x=\"350\" y=\"20\">"
							    +"<label>开始</label>"
							    +"<ports>"
							      +"<ciied:Port id=\"port1\" y=\"1\"/>"
							    +"</ports>"
							  +"</bg:StartEvent>";
				sb.append(start);
				if(!(list!=null && list.size()>0)){
					return null;
				}
				int j=0;
				for(int i=0;i<list.size();i++){
					j++;
					FlowInstanceUserEntity user=list.get(i);
					user.setTaskNodeId("task"+j);
					String task="<bg:SequenceFlow id=\"sequenceFlow"+j+"\" endPort=\"port"+(j*2)+"\" startPort=\"port"+(j*2-1)+"\"></bg:SequenceFlow>"
							  +"<bg:Task id=\"task"+j+"\" height=\"50\" user=\"true\" width=\"90\" x=\"321.5\" y=\""+(20+j*80)+"\">"
							  +"<label>"+user.getUserName()+"审批"+"</label>"
							    +"<ports>"
							      +"<ciied:Port id=\"port"+(j*2)+"\" y=\"0\"/>"
							      +"<ciied:Port id=\"port"+(j*2+1)+"\" y=\"1\"/>"
							      +"</ports>"
							  +"</bg:Task>";
					sb.append(task);
				}
				String end="<bg:SequenceFlow id=\"sequenceFlow"+(j+1)+"\" endPort=\"port"+((j+1)*2)+"\" startPort=\"port"+((j+1)*2-1)+"\"></bg:SequenceFlow>"
							  +"<bg:EndEvent id=\"endEvent1\" height=\"49\" width=\"34\" x=\"349.5\" y=\""+(20+(j+1)*80)+"\">"
							  +"<label>结束1</label>"
							    +"<ports>"
							      +"<ciied:Port id=\"port"+((j+1)*2)+"\" y=\"0\"/>"
							      +"</ports>"
							  +"</bg:EndEvent>"
							+"</diagram>";
				sb.append(end);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			List<AppFormApproveUser> userList=appFormApproveUserService.findByProperty("formId", formId);
			result.put("userList", userList);
			String start="<diagram xmlns:bg=\"bpm.graphic\" xmlns:ciied=\"com.ibm.ilog.elixir.diagram\" xmlns:fg=\"flash.geom\">"
							  +"<bg:StartEvent id=\"startEvent1\" height=\"49\" width=\"31\" x=\"350\" y=\"20\">"
							    +"<label>开始</label>"
							    +"<ports>"
							      +"<ciied:Port id=\"port1\" y=\"1\"/>"
							    +"</ports>"
							  +"</bg:StartEvent>";
			sb.append(start);
			if((userList!=null && userList.size()>0)){
				int j=0;
				for(int i=0;i<userList.size();i++){
					j++;
					AppFormApproveUser user=userList.get(i);
					user.setTaskId("task"+j);
					String task="<bg:SequenceFlow id=\"sequenceFlow"+j+"\" endPort=\"port"+(j*2)+"\" startPort=\"port"+(j*2-1)+"\"></bg:SequenceFlow>"
							  +"<bg:Task id=\"task"+j+"\" height=\"50\" user=\"true\" width=\"90\" x=\"321.5\" y=\""+(20+j*80)+"\">"
							  +"<label>"+user.getUserName()+"审批"+"</label>"
							    +"<ports>"
							      +"<ciied:Port id=\"port"+(j*2)+"\" y=\"0\"/>"
							      +"<ciied:Port id=\"port"+(j*2+1)+"\" y=\"1\"/>"
							      +"</ports>"
							  +"</bg:Task>";
					sb.append(task);
				}
				String end="<bg:SequenceFlow id=\"sequenceFlow"+(j+1)+"\" endPort=\"port"+((j+1)*2)+"\" startPort=\"port"+((j+1)*2-1)+"\"></bg:SequenceFlow>"
							  +"<bg:EndEvent id=\"endEvent1\" height=\"49\" width=\"34\" x=\"349.5\" y=\""+(20+(j+1)*80)+"\">"
							  +"<label>结束1</label>"
							    +"<ports>"
							      +"<ciied:Port id=\"port"+((j+1)*2)+"\" y=\"0\"/>"
							      +"</ports>"
							  +"</bg:EndEvent>"
							+"</diagram>";
				sb.append(end);
			}else{
				String end="<bg:SequenceFlow id=\"sequenceFlow"+(1)+"\" endPort=\"port"+((1)*2)+"\" startPort=\"port"+((1)*2-1)+"\"></bg:SequenceFlow>"
						  +"<bg:EndEvent id=\"endEvent1\" height=\"49\" width=\"34\" x=\"349.5\" y=\""+(20+(1)*80)+"\">"
						  +"<label>结束1</label>"
						    +"<ports>"
						      +"<ciied:Port id=\"port"+((1)*2)+"\" y=\"0\"/>"
						      +"</ports>"
						  +"</bg:EndEvent>"
						+"</diagram>";
				sb.append(end);
			}
		}
		result.put("defXml", sb.toString());
		return result;
	}

	
}
