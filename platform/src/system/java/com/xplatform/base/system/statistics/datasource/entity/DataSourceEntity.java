package com.xplatform.base.system.statistics.datasource.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**   
 * @Title: Entity
 * @Description: 数据源
 * @author onlineGenerator
 * @date 2014-07-02 11:07:23
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_sys_datasource", schema = "")
@SuppressWarnings("serial")
public class DataSourceEntity extends OperationEntity implements java.io.Serializable {
	private String name;//名称
	private String code;//编码
	private String type;//类型
	private String value;//数据值
	private String description;//描述
	
	@Column(name ="NAME",nullable=true,length=50)
	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}
	@Column(name ="CODE",nullable=true,length=50)
	public String getCode(){
		return this.code;
	}

	public void setCode(String code){
		this.code = code;
	}
	@Column(name ="TYPE",nullable=true,length=50)
	public String getType(){
		return this.type;
	}

	public void setType(String type){
		this.type = type;
	}
	@Column(name ="VALUE",nullable=true,length=1000)
	public String getValue(){
		return this.value;
	}

	public void setValue(String value){
		this.value = value;
	}
	@Column(name ="DESCRIPTION",nullable=true,length=1000)
	public String getDescription(){
		return this.description;
	}

	public void setDescription(String description){
		this.description = description;
	}
}
