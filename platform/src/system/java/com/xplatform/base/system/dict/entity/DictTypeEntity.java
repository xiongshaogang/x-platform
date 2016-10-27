package com.xplatform.base.system.dict.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :数据字典类型
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年6月10日 上午8:51:44
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年6月10日 上午8:51:44
 *
 */
@Entity
@Table(name = "t_sys_dict_type", schema = "")
public class DictTypeEntity extends OperationEntity {
	
	private String name;  //类型名称
	private String code;  //类型编码
	private String description; //描述
	private String type; //字典类型的类型，系统（system）/用户(user)
	private String valueType; //值类型  树(tree)/下拉框(combo)
	
	private List<DictValueEntity> dictValueList;
	
	
	@Column(name ="name",nullable=true,length=100)
	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}
	
	@Column(name ="code",nullable=true,length=32)
	public String getCode(){
		return this.code;
	}
	
	public void setCode(String code){
		this.code = code;
	}
	
	@Column(name ="description",nullable=true,length=1000)
	public String getDescription(){
		return this.description;
	}

	
	public void setDescription(String description){
		this.description = description;
	}
	
	@Column(name ="type",nullable=true,length=30)
	public String getType(){
		return this.type;
	}

	
	public void setType(String type){
		this.type = type;
	}
	
	@Column(name ="valueType",nullable=true,length=30)
	public String getValueType(){
		return this.valueType;
	}

	
	public void setValueType(String valueType){
		this.valueType = valueType;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy="dictType")
	public List<DictValueEntity> getDictValueList() {
		return dictValueList;
	}

	public void setDictValueList(List<DictValueEntity> dictValueList) {
		this.dictValueList = dictValueList;
	}
	
}
