package com.xplatform.base.orgnaization.role.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xplatform.base.framework.core.common.entity.OperationEntity;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgRoleEntity;
import com.xplatform.base.orgnaization.user.entity.UserRoleEntity;
import com.xplatform.base.system.type.entity.TypeRoleEntity;

/**
 * description : 职位/角色实体
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2015年5月21日 下午5:41:29
 * 
 *
 */

@Entity
@Table(name = "t_org_role")
// 将对象类型的属性排除在转换json之外
@JsonIgnoreProperties({ "hibernateLazyInitializer", "roleModuleList", "orgRole", "catalogueRoleList" })
public class RoleEntity extends OperationEntity {

	public static final String ADMIN = "admin";// 组织管理员
	public static final String MEMBER = "member";// 普通会员
	public static final String EMPLOYEE = "employee";// 普通员工
	public static final String SUPER_ADMIN = "superAdmin";// 平台管理员
	public static final String COMPANY_ADMIN = "companyAdmin";// 平台管理员
	public static final String BOSS = "boss";// 平台管理员
	
	private String name;// 角色名称

	private String code;// 角色code

	private String flag;// 删除标识

	private String description;// 备注

	private String allowDelete;// 是否允许删除

	private String allowEdit;// 是否允许编辑

	private Integer definedFlag;// 定义表示(1.代表系统默认角色 2.代表用户自定义角色3.代表管理员创建的公共角色)
	
	private String orgId;//群组id
	
	private List<RoleModuleEntity> roleModuleList = new ArrayList<RoleModuleEntity>();

	private List<OrgRoleEntity> orgRole = new ArrayList<OrgRoleEntity>();

	private List<TypeRoleEntity> catalogueRoleList = new ArrayList<TypeRoleEntity>();

	@Column(name = "name", nullable = true, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "code", nullable = false, length = 50)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "flag", nullable = false, length = 1)
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Column(name = "allowDelete", columnDefinition = "char", nullable = false, length = 1)
	public String getAllowDelete() {
		return allowDelete;
	}

	public void setAllowDelete(String allowDelete) {
		this.allowDelete = allowDelete;
	}

	@Column(name = "allowEdit", columnDefinition = "char", nullable = false, length = 1)
	public String getAllowEdit() {
		return allowEdit;
	}

	public void setAllowEdit(String allowEdit) {
		this.allowEdit = allowEdit;
	}

	@Column(name = "description", nullable = true, length = 1000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
	public List<RoleModuleEntity> getRoleModuleList() {
		return roleModuleList;
	}

	public void setRoleModuleList(List<RoleModuleEntity> roleModuleList) {
		this.roleModuleList = roleModuleList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
	public List<OrgRoleEntity> getOrgRole() {
		return orgRole;
	}

	public void setOrgRole(List<OrgRoleEntity> orgRole) {
		this.orgRole = orgRole;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
	public List<TypeRoleEntity> getCatalogueRoleList() {
		return catalogueRoleList;
	}

	public void setCatalogueRoleList(List<TypeRoleEntity> catalogueRoleList) {
		this.catalogueRoleList = catalogueRoleList;
	}

	@Column(name = "definedFlag")
	public Integer getDefinedFlag() {
		return definedFlag;
	}

	public void setDefinedFlag(Integer definedFlag) {
		this.definedFlag = definedFlag;
	}

	@Column(name = "orgId", nullable = true, length = 32)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}


}
