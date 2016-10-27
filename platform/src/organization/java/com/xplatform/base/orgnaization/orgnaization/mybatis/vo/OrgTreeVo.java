package com.xplatform.base.orgnaization.orgnaization.mybatis.vo;

import com.xplatform.base.framework.mybatis.entity.IdEntity;

public class OrgTreeVo extends IdEntity {

	private static final long serialVersionUID = 1L;
	private String name;
	private String code;        //机构代码
	private String shortName;    // 机构简称
	private String enName ;      //  英文名
	private String type;         //  机构类型（机构，部门,岗位）
	private String telephone;     // 电话
	private String address;       //  地址
	private String email;         // 邮箱
	private String fax;          //  传真
	private String post;          // 邮编
	private String managerEmpId; //部门经理
	private String description;   // 备注
	private String iconCls;
	private String checked;
	private String parentId;
	private String isLeaf;
	private int level;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getManagerEmpId() {
		return managerEmpId;
	}
	public void setManagerEmpId(String managerEmpId) {
		this.managerEmpId = managerEmpId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
}
