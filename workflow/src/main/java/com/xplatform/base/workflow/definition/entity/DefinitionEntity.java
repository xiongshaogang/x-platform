package com.xplatform.base.workflow.definition.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

import com.xplatform.base.framework.core.common.entity.AssignedOperationEntity;
import com.xplatform.base.framework.core.common.entity.OperationEntity;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.system.type.entity.TypeEntity;

/**
 * 
 * description :流程定义扩展实体
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年8月1日 下午2:18:41
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- xiehs 2014年7月1日 下午2:18:41
 *
 */
@Entity
@Table(name = "t_flow_definition")
public class DefinitionEntity extends OperationEntity {

	public static final String DefaultSubjectRule = "{流程标题:title}-{发起人:startUser}-{发起时间:startTime}";
	public static final String MAIN = "1";// 主版本
	public static final String NOT_MAIN = "0";// 历史版本

	public static final String STATUS_ENABLED = "Y";// 能使用
	public static final String STATUS_DISABLED = "N";// 禁用

	public static final String PUBLISHED_YES = "Y";// 能使用
	public static final String PUBLISHED_NO = "N";// 禁用

	/* 基本信息 */
	private String name;// 流程定义名称
	private String code;// 流程定义key
	private String taskNameRule;// 产生任务规则名称
	private String status; // 0-草稿，1.已发布，2.挂起
	private String published;// 是否发布
	private String defXml;// 定义flex的xml
	private String description;// 描述
	private TypeEntity type;// 所属分类
	/* activiti信息 */
	private String actDeployId;// activiti发布id
	private String actKey;// activiti定义key
	private String actId;// activiti定义id
	/* 版本信息 */
	private Integer version;// 版本号
	private DefinitionEntity parent;// 最高版本流程
	private String reason;// 修改的原因
	private String isMain;// 是否主流程，最高版本
	/* 其他参数 */
	private String allowDivert;// 同意转发
	private String allowFinishedDivert;// 是否允许我的办结转发
	private String informStart;// 归档时发送消息给发起人方式
	private String allowFinishedCc;// 是否允许办结抄送
//	private String finishedCcId;// 办结抄送人集合id
//	private String finishedCcName;// 办结抄送人集合name
	private String refDef;// 引用的流程
	private String informType;// 任务通知类型
	private String skipFirstNode;//是否跳过第一个结点

	private String canChoicePath;// 允许选择路径集合
	private Map<Object, Object> canChoicePathNodeMap;

	@Column(name = "name", nullable = true, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "code", nullable = true, length = 50)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "task_rule_name", nullable = true, length = 1000)
	public String getTaskNameRule() {
		return taskNameRule;
	}

	public void setTaskNameRule(String taskNameRule) {
		this.taskNameRule = taskNameRule;
	}

	@Column(name = "status", columnDefinition = "char", nullable = true, length = 1)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "def_xml", nullable = true, length = 8000)
	public String getDefXml() {
		return defXml;
	}

	public void setDefXml(String defXml) {
		this.defXml = defXml;
	}

	@Column(name = "description", nullable = true, length = 1000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "act_deploy_id", nullable = true, length = 1000)
	public String getActDeployId() {
		return actDeployId;
	}

	public void setActDeployId(String actDeployId) {
		this.actDeployId = actDeployId;
	}

	@Column(name = "act_key", nullable = true, length = 100)
	public String getActKey() {
		return actKey;
	}

	public void setActKey(String actKey) {
		this.actKey = actKey;
	}

	@Column(name = "act_id", nullable = true, length = 32)
	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	@Column(name = "version", nullable = true, length = 10)
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	@ForeignKey(name = "null")
	public DefinitionEntity getParent() {
		return parent;
	}

	public void setParent(DefinitionEntity parent) {
		this.parent = parent;
	}

	@Column(name = "reason", nullable = true, length = 1000)
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "is_main", columnDefinition = "char", nullable = true, length = 1)
	public String getIsMain() {
		return isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id")
	@ForeignKey(name = "null")
	public TypeEntity getType() {
		return type;
	}

	public void setType(TypeEntity type) {
		this.type = type;
	}

	@Column(name = "is_publish", columnDefinition = "char", nullable = true, length = 1)
	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	@Column(name = "allow_divert", columnDefinition = "char", nullable = true, length = 1)
	public String getAllowDivert() {
		return allowDivert;
	}

	public void setAllowDivert(String allowDivert) {
		this.allowDivert = allowDivert;
	}

	@Column(name = "inform_type", nullable = true, length = 30)
	public String getInformType() {
		return informType;
	}

	public void setInformType(String informType) {
		this.informType = informType;
	}

	@Column(name = "allow_finished_dirver", columnDefinition = "char", nullable = true, length = 1)
	public String getAllowFinishedDivert() {
		return allowFinishedDivert;
	}

	public void setAllowFinishedDivert(String allowFinishedDivert) {
		this.allowFinishedDivert = allowFinishedDivert;
	}

	@Column(name = "info_start", nullable = true, length = 30)
	public String getInformStart() {
		return informStart;
	}

	public void setInformStart(String informStart) {
		this.informStart = informStart;
	}

	@Column(name = "allow_finished_cc", columnDefinition = "char", nullable = true, length = 1)
	public String getAllowFinishedCc() {
		return allowFinishedCc;
	}

	public void setAllowFinishedCc(String allowFinishedCc) {
		this.allowFinishedCc = allowFinishedCc;
	}

	@Column(name = "ref_def", nullable = true, length = 1000)
	public String getRefDef() {
		return refDef;
	}

	public void setRefDef(String refDef) {
		this.refDef = refDef;
	}

	@Column(name = "skip_first_node",columnDefinition = "char", nullable = true, length = 1)
	public String getSkipFirstNode() {
		return skipFirstNode;
	}

	public void setSkipFirstNode(String skipFirstNode) {
		this.skipFirstNode = skipFirstNode;
	}

	@Transient
	public Map getCanChoicePathNodeMap() {
		if (this.canChoicePathNodeMap == null) {
			Map map = new HashMap();
			if (StringUtil.isEmpty(this.canChoicePath)) {
				this.canChoicePathNodeMap = map;
				return this.canChoicePathNodeMap;
			}
			Pattern regex = Pattern.compile("(\\w+):(\\w+)");
			Matcher regexMatcher = regex.matcher(this.canChoicePath);
			while (regexMatcher.find()) {
				map.put(regexMatcher.group(1), regexMatcher.group(2));
			}
			this.canChoicePathNodeMap = map;
		}
		return this.canChoicePathNodeMap;
	}

	public void updateCanChoicePath() {
		if (this.canChoicePathNodeMap != null) {
			StringBuffer sb = new StringBuffer();
			Iterator iter = this.canChoicePathNodeMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				sb.append(",");
				sb.append(key);
				sb.append(":");
				sb.append(val);
			}
			this.canChoicePath = sb.toString().replaceFirst(",", "");
		}
	}

	@Transient
	public String getCanChoicePath() {
		return canChoicePath;
	}

	public void setCanChoicePath(String canChoicePath) {
		this.canChoicePath = canChoicePath;
	}

	public void setCanChoicePathNodeMap(Map<Object, Object> canChoicePathNodeMap) {
		this.canChoicePathNodeMap = canChoicePathNodeMap;
	}

}
