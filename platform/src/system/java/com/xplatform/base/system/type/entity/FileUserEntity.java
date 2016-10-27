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
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.system.attachment.entity.AttachEntity;

/**
 * description : 文件用户权限表
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月2日 下午4:59:51
 *
 */
@Entity
@Table(name = "t_sys_file_user")
@SuppressWarnings("serial")
public class FileUserEntity extends OperationEntity implements java.io.Serializable {
	private UserEntity userEntity; // 用户实体
	private AttachEntity attachEntity; // 资料实体

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "attach_entity")
	@ForeignKey(name = "null")
	public AttachEntity getAttachEntity() {
		return attachEntity;
	}

	public void setAttachEntity(AttachEntity attachEntity) {
		this.attachEntity = attachEntity;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_entity")
	@ForeignKey(name = "null")
	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

}
