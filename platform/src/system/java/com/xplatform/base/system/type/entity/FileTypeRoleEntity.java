package com.xplatform.base.system.type.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;

/**
 * description : 角色文件夹权限表
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月2日 下午4:59:51
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年7月2日 下午4:59:51
 *
*/
@Entity
@Table(name = "t_sys_filetype_role")
@SuppressWarnings("serial")
public class FileTypeRoleEntity extends OperationEntity implements java.io.Serializable {
	private RoleEntity roleEntity; //用户实体
	private TypeEntity typeEntity; //分类目录实体
	private String isManage;//是否可管理(1为可管理,2则是为了显示出整个树而记录的父Id的记录)

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "type_entity")
	@ForeignKey(name = "null")
	public TypeEntity getTypeEntity() {
		return typeEntity;
	}

	public void setTypeEntity(TypeEntity typeEntity) {
		this.typeEntity = typeEntity;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "role_entity")
	@ForeignKey(name = "null")
	public RoleEntity getRoleEntity() {
		return roleEntity;
	}

	public void setRoleEntity(RoleEntity roleEntity) {
		this.roleEntity = roleEntity;
	}

	@Column(name = "isManage", columnDefinition = "char")
	public String getIsManage() {
		return isManage;
	}

	public void setIsManage(String isManage) {
		this.isManage = isManage;
	}

}
