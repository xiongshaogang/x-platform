package com.xplatform.base.system.type.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
import com.xplatform.base.orgnaization.resouce.entity.ResourceEntity;
import com.xplatform.base.orgnaization.user.entity.UserEntity;

/**
 * description : 用户文件夹操作权限表
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
@Table(name = "t_sys_filetype_user_authority")
@SuppressWarnings("serial")
public class FileTypeUserAuthorityEntity extends OperationEntity implements java.io.Serializable {
	private UserEntity userEntity; //用户实体
	private TypeEntity typeEntity; //分类目录实体
	private ResourceEntity resourceEntity;//权限实体

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "type_entity")
	@ForeignKey(name="null")
	public TypeEntity getTypeEntity() {
		return typeEntity;
	}

	public void setTypeEntity(TypeEntity typeEntity) {
		this.typeEntity = typeEntity;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "resource_entity")
	@ForeignKey(name="null")
	public ResourceEntity getResourceEntity() {
		return resourceEntity;
	}

	public void setResourceEntity(ResourceEntity resourceEntity) {
		this.resourceEntity = resourceEntity;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_entity")
	@ForeignKey(name="null")
	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
}
