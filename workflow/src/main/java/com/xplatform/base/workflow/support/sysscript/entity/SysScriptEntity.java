package com.xplatform.base.workflow.support.sysscript.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import com.xplatform.base.framework.core.common.entity.IdEntity;
import com.xplatform.base.framework.core.common.entity.OperationEntity;
import com.xplatform.base.system.attachment.entity.AttachEntity;
import com.xplatform.base.system.type.entity.TypeEntity;

/**
 * description : 系统脚本实体
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年8月4日 下午6:32:46
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年8月4日 下午6:32:46
 *
*/

@Entity
@Table(name = "t_flow_support_script")
@SuppressWarnings("serial")
public class SysScriptEntity extends OperationEntity implements java.io.Serializable {
	private String name;//脚本名
	private String scriptContent;//脚本内容
	private String typeDict;//脚本逻辑分类(数据字典code:scriptType)
	private String sourceDict;//脚本来源分类(数据字典code:scriptSource)
	private String className;//类名
	private String classInsName;//类实例名(已被spring注入的)
	private String methodName;//方法名称
	private String returnType;//返回值
	private String argument;//参数json数据
	private String enableDict;//是否启用脚本(数据字典code:YNType)
	private String remark;//脚本描述

	@Column(name = "name", length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "script_content", length = 4000)
	public String getScriptContent() {
		return scriptContent;
	}

	public void setScriptContent(String scriptContent) {
		this.scriptContent = scriptContent;
	}

	@Column(name = "type_dict", length = 100)
	public String getTypeDict() {
		return typeDict;
	}

	public void setTypeDict(String typeDict) {
		this.typeDict = typeDict;
	}

	@Column(name = "remark", length = 4000)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "source_dict", length = 100)
	public String getSourceDict() {
		return sourceDict;
	}

	public void setSourceDict(String sourceDict) {
		this.sourceDict = sourceDict;
	}

	@Column(name = "class_name", length = 200)
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Column(name = "class_ins_name", length = 100)
	public String getClassInsName() {
		return classInsName;
	}

	public void setClassInsName(String classInsName) {
		this.classInsName = classInsName;
	}

	@Column(name = "method_name", length = 100)
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Column(name = "return_type", length = 100)
	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	@Column(name = "argument", length = 4000)
	public String getArgument() {
		return argument;
	}

	public void setArgument(String argument) {
		this.argument = argument;
	}

	@Column(name = "enable_dict", length = 100)
	public String getEnableDict() {
		return enableDict;
	}

	public void setEnableDict(String enableDict) {
		this.enableDict = enableDict;
	}

}
