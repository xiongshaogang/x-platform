package com.xplatform.base.system.statistics.field.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;


/**   
 * @Title: Entity
 * @Description: 数据源字段
 * @author onlineGenerator
 * @date 2014-07-02 16:00:08
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_sys_statistics_field", schema = "")
@SuppressWarnings("serial")
public class FieldEntity extends OperationEntity implements java.io.Serializable {
	private String name;//字段名称
	private String showName;//显示名称
	private String type;//字段类型
	private Integer num;//排序号
	private String description;//字段描述
	private String isshow;//字段是否显示
	private String issum;//是否求和
	private String issearch;//是否查询
	private String searchActivex;//字段查询控件
	private String searchCondition;//查询条件
	private String dictCode;//字典值Code
	private String isx;//是否X轴
	private String isy;//是否Y轴
	private String datasourceId;//数据源ID
	
	@Column(name ="NAME",nullable=true,length=100)
	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}
	@Column(name ="SHOW_NAME",nullable=true,length=100)
	public String getShowName(){
		return this.showName;
	}

	public void setShowName(String showName){
		this.showName = showName;
	}
	@Column(name ="TYPE",nullable=true,length=20)
	public String getType(){
		return this.type;
	}

	public void setType(String type){
		this.type = type;
	}
	@Column(name ="NUM",nullable=true,length=10)
	public Integer getNum(){
		return this.num;
	}

	public void setNum(Integer num){
		this.num = num;
	}
	@Column(name ="DESCRIPTION",nullable=true,length=100)
	public String getDescription(){
		return this.description;
	}

	public void setDescription(String description){
		this.description = description;
	}
	@Column(name ="ISSHOW",nullable=true,length=1)
	public String getIsshow(){
		return this.isshow;
	}

	public void setIsshow(String isshow){
		this.isshow = isshow;
	}
	@Column(name ="ISSUM",nullable=true,length=1)
	public String getIssum(){
		return this.issum;
	}

	public void setIssum(String issum){
		this.issum = issum;
	}
	@Column(name ="ISSEARCH",nullable=true,length=1)
	public String getIssearch(){
		return this.issearch;
	}

	public void setIssearch(String issearch){
		this.issearch = issearch;
	}
	@Column(name ="SEARCH_ACTIVEX",nullable=true,length=30)
	public String getSearchActivex(){
		return this.searchActivex;
	}

	public void setSearchActivex(String searchActivex){
		this.searchActivex = searchActivex;
	}
	@Column(name ="SEARCH_CONDITION",nullable=true,length=20)
	public String getSearchCondition(){
		return this.searchCondition;
	}

	public void setSearchCondition(String searchCondition){
		this.searchCondition = searchCondition;
	}
	
	@Column(name ="dict_code",nullable=true,length=50)
	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	@Column(name ="ISX",nullable=true,length=1)
	public String getIsx(){
		return this.isx;
	}

	public void setIsx(String isx){
		this.isx = isx;
	}
	@Column(name ="ISY",nullable=true,length=1)
	public String getIsy(){
		return this.isy;
	}

	public void setIsy(String isy){
		this.isy = isy;
	}

	@Column(name ="DATASOURCE_ID",nullable=true,length=33)
	public String getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}
}
