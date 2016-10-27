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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.BaseTreeEntity;

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
public class ${entityName}Entity extends BaseTreeEntity  implements java.io.Serializable {
	<#list columns as po>
	<#if po.fieldName != 'parentId' && po.fieldName != 'id' && po.fieldName != 'level' && po.fieldName != 'treeIndex' && po.fieldName != 'isLeaf' && po.fieldName != 'iconCls' && po.fieldName != 'checked' && po.fieldName != 'createTime' && po.fieldName != 'createUserId' && po.fieldName != 'createUserName' && po.fieldName != 'updateTime' && po.fieldName != 'updateUserId' && po.fieldName != 'updateUserName'>
	private ${po.fieldType} ${po.fieldName};//${po.fieldLabel}
	
	</#if>
	</#list>
	private ${entityName}Entity parent;     // 父节点
	private List<${entityName}Entity> children =new ArrayList<${entityName}Entity>(); //子节点
	
	<#list columns as po>
	<#if po.fieldName != 'parentId' && po.fieldName != 'id' && po.fieldName != 'level' && po.fieldName != 'treeIndex' && po.fieldName != 'isLeaf' && po.fieldName != 'iconCls' && po.fieldName != 'checked' && po.fieldName != 'createTime' && po.fieldName != 'createUserId' && po.fieldName != 'createUserName' && po.fieldName != 'updateTime' && po.fieldName != 'updateUserId' && po.fieldName != 'updateUserName'>
	
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
	@JoinColumn(name = "parent_id")
	public ${entityName}Entity getParent() {
		return parent;
	}
	public void setParent(${entityName}Entity parent) {
		this.parent = parent;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	public List<${entityName}Entity> getChildren() {
		return children;
	}
	public void setChildren(List<${entityName}Entity> children) {
		this.children = children;
	}
}
