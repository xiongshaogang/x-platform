package com.xplatform.base.platform.common.aop;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.xplatform.base.framework.core.util.oConvertUtils;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.utils.ClientUtil;

import org.springframework.stereotype.Component;

/**
 * Hiberate拦截器：实现创建人，创建时间，创建人名称自动注入; 修改人,修改时间,修改人名自动注入;
 * 
 * @author 张代浩
 */
@Component
public class HiberAspect extends EmptyInterceptor {
	private static final Logger logger = Logger.getLogger(HiberAspect.class);
	private static final long serialVersionUID = 1L;

	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		UserEntity currentUser = null;
		try {
			currentUser = ClientUtil.getUserEntity();
		} catch (RuntimeException e) {
			logger.warn("当前session为空,无法获取用户");
		}
		if (currentUser == null) {
			// 添加数据
			for (int index = 0; index < propertyNames.length; index++) {
				if ("createTime".equals(propertyNames[index])) {
					/** 使用拦截器将对象的"创建时间"属性赋上值 */
					if (oConvertUtils.isEmpty(state[index])) {
						state[index] = new Date();
					}
					continue;
				} else if ("updateTime".equals(propertyNames[index])) {
					/** 使用拦截器将对象的"更新时间"属性赋上值 */
					if (oConvertUtils.isEmpty(state[index])) {
						state[index] = new Date();
					}
					continue;
				}
			}
			return true;
		}
		try {
			// 添加数据
			for (int index = 0; index < propertyNames.length; index++) {
				if ("createTime".equals(propertyNames[index])) {
					/** 使用拦截器将对象的"创建时间"属性赋上值 */
					if (oConvertUtils.isEmpty(state[index])) {
						state[index] = new Date();
					}
					continue;
				} else if ("createUserId".equals(propertyNames[index])) {
					/** 使用拦截器将对象的"创建人Id"属性赋上值 */
					if (oConvertUtils.isEmpty(state[index])) {
						state[index] = currentUser.getId();
					}
					continue;
				} else if ("createUserName".equals(propertyNames[index])) {
					/** 使用拦截器将对象的"创建人名称"属性赋上值 */
					if (oConvertUtils.isEmpty(state[index])) {
						state[index] = currentUser.getName();
					}
					continue;
				} else if ("createDeptId".equals(propertyNames[index])) {
					/** 使用拦截器将对象的"创建人部门id"属性赋上值 */
					if (oConvertUtils.isEmpty(state[index])) {
						state[index] = currentUser.getOrgId();
					}
					continue;
				} else if ("createDeptName".equals(propertyNames[index])) {
					/** 使用拦截器将对象的"创建人部门名称"属性赋上值 */
					if (oConvertUtils.isEmpty(state[index])) {
						state[index] = currentUser.getOrgName();
					}
					continue;
				} else if ("updateTime".equals(propertyNames[index])) {
					/** 使用拦截器将对象的"更新时间"属性赋上值 */
					if (oConvertUtils.isEmpty(state[index])) {
						state[index] = new Date();
					}
					continue;
				} else if ("updateUserId".equals(propertyNames[index])) {
					/** 使用拦截器将对象的"更新人Id"属性赋上值 */
					if (oConvertUtils.isEmpty(state[index])) {
						state[index] = currentUser.getId();
					}
					continue;
				} else if ("updateUserName".equals(propertyNames[index])) {
					/** 使用拦截器将对象的"更新人名称"属性赋上值 */
					if (oConvertUtils.isEmpty(state[index])) {
						state[index] = currentUser.getName();
					}
					continue;
					/* 找到名为"创建人部门id"的属性 */
				} else if ("updateDeptId".equals(propertyNames[index])) {
					/** 使用拦截器将对象的"更新人部门id"属性赋上值 */
					if (oConvertUtils.isEmpty(state[index])) {
						state[index] = currentUser.getOrgId();
					}
					continue;
					/* 找到名为"创建人部门名称"的属性 */
				} else if ("updateDeptName".equals(propertyNames[index])) {
					/** 使用拦截器将对象的"更新人部门名称"属性赋上值 */
					if (oConvertUtils.isEmpty(state[index])) {
						state[index] = currentUser.getOrgName();
					}
					continue;
				} 
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		UserEntity currentUser = null;
		try {
			currentUser = ClientUtil.getUserEntity();
		} catch (RuntimeException e1) {
			logger.warn("当前session为空,无法获取用户");
		}
		// 添加数据
		for (int index = 0; index < propertyNames.length; index++) {
			if (currentUser == null) {
				if ("updateTime".equals(propertyNames[index])) {
					/** 使用拦截器将对象的"修改时间"属性赋上值 */
					currentState[index] = new Date();
					continue;
				}
			}else{
				if ("updateTime".equals(propertyNames[index])) {
					/** 使用拦截器将对象的"修改时间"属性赋上值 */
					currentState[index] = new Date();
					continue;
				} else if ("updateUserId".equals(propertyNames[index])) {
					/** 使用拦截器将对象的"更新人Id"属性赋上值 */
					currentState[index] = currentUser.getId();
					continue;
				} else if ("updateUserName".equals(propertyNames[index])) {
					/** 使用拦截器将对象的"更新人名称"属性赋上值 */
					currentState[index] = currentUser.getName();
					continue;
				} else if ("updateDeptId".equals(propertyNames[index])) {
					/** 使用拦截器将对象的"更新人部门id"属性赋上值 */
					currentState[index] = currentUser.getOrgId();
					continue;
				} else if ("updateDeptName".equals(propertyNames[index])) {
					/** 使用拦截器将对象的"更新人部门名称"属性赋上值 */
					currentState[index] = currentUser.getOrgName();
					continue;
				} 
			}
			
		}
		return true;
	}
}
