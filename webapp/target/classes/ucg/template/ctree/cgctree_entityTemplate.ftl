package ${bussiPackage}.${entityPackage}.entity;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
import ${cgformConfig.baseFormEntity.refPackage}.entity.${cEntityName}Entity;

/**   
 * @Title: Entity
 * @Description: ${ftl_description}
 * @author onlineGenerator
 * @date ${ftl_create_time}
 * @version V1.0   
 *
 */
@Entity
@Table(name = "${tableName}", schema = "")
@SuppressWarnings("serial")
public class ${entityName}Entity extends OperationEntity  implements java.io.Serializable {
	<#list columns as po>
	<#if fieldMeta[po.fieldName] != cField?upper_case && po.fieldName != 'id' && po.fieldName != 'createTime' && po.fieldName != 'createUserId' && po.fieldName != 'createUserName' && po.fieldName != 'updateTime' && po.fieldName != 'updateUserId' && po.fieldName != 'updateUserName'>
	private ${po.fieldType} ${po.fieldName};//${po.fieldLabel}
	
	</#if>
	</#list>
	private ${cEntityName}Entity ${cEntityName?uncap_first};
	
	<#list columns as po>
	<#if fieldMeta[po.fieldName] != cField?upper_case && po.fieldName != 'id' && po.fieldName != 'createTime' && po.fieldName != 'createUserId' && po.fieldName != 'createUserName' && po.fieldName != 'updateTime' && po.fieldName != 'updateUserId' && po.fieldName != 'updateUserName'>
	@Column(name ="${fieldMeta[po.fieldName]}",nullable=<#if po.isNullable == 'Y'>true<#else>false</#if><#if po.fieldPersion != 0>,scale=${po.fieldPersion}</#if><#if po.fieldLength !=0>,length=${po.fieldLength?c}</#if>)
	public ${po.fieldType} get${po.fieldName?cap_first}(){
		return this.${po.fieldName};
	}

	public void set${po.fieldName?cap_first}(${po.fieldType} ${po.fieldName}){
		this.${po.fieldName} = ${po.fieldName};
	}
	</#if>
	</#list>
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "${cField}")
	public ${cEntityName}Entity get${cEntityName}() {
		return ${cEntityName?uncap_first};
	}
	public void set${cEntityName}(${cEntityName}Entity ${cEntityName?uncap_first}) {
		this.${cEntityName?uncap_first} = ${cEntityName?uncap_first};
	}
}
