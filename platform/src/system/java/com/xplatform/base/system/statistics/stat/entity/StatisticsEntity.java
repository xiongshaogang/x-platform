package com.xplatform.base.system.statistics.stat.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
import com.xplatform.base.system.type.entity.TypeEntity;

/**   
 * @Title: Entity
 * @Description: 分类统计
 * @author onlineGenerator
 * @date 2014-07-03 10:42:37
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_sys_statistics", schema = "")
@SuppressWarnings("serial")
public class StatisticsEntity extends OperationEntity  implements java.io.Serializable {
	private String name;//统计名称
	
	private String code;//统计code
	
	private String datasourceId;//数据源类型
	
	private String showType;//默认显示图标类型
	
	private String authorityType;//权限类型
	
	private TypeEntity type;
	
	@Column(name ="NAME",nullable=true,length=100)
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
	
	@Column(name ="DATASOURCE_ID",nullable=true,length=30)
	public String getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}
	
	
	@Column(name ="SHOW_TYPE",nullable=true,length=30)
	public String getShowType(){
		return this.showType;
	}


	public void setShowType(String showType){
		this.showType = showType;
	}
	@Column(name ="AUTHORITY_TYPE",nullable=true,length=30)
	public String getAuthorityType(){
		return this.authorityType;
	}

	public void setAuthorityType(String authorityType){
		this.authorityType = authorityType;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id")
	public TypeEntity getType() {
		return type;
	}
	public void setType(TypeEntity type) {
		this.type = type;
	}
}
