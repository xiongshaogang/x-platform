package com.xplatform.base.platform.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.framework.core.util.DBTypeUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.oConvertUtils;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.role.entity.RoleEntity;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.vo.Client;

/**
 * 项目参数工具类
 * 
 */
public class ClientUtil {

	private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("sysConfig");

	public static Client getClient() {
		HttpSession session = ContextHolderUtils.getSession();
		if (session.getId() != null) {
			return ClientManager.getInstance().getClient(session.getId());
		}
		return null;
	}

	/**
	 * 获取session定义名称
	 * 
	 * @return
	 */
	public static final String getSessionattachmenttitle(String sessionName) {
		return bundle.getString(sessionName);
	}

	@Deprecated
	/*
	 * public static final List<DeptEntity> getDept() { HttpSession session =
	 * ContextHolderUtils.getSession(); if
	 * (ClientManager.getInstance().getClient(session.getId()) != null) {
	 * UserEntity user =
	 * ClientManager.getInstance().getClient(session.getId()).getUser();
	 * SysUserService sysUserService =
	 * ApplicationContextUtil.getBean("sysUserService");
	 * sysUserService.getDeptsByUserId(user.getId()); } return null; }
	 */
	/*
	 * @Deprecated public static final List<TSRoleFunction>
	 * getSessionTSRoleFunction(String roleId) { HttpSession session =
	 * ContextHolderUtils.getSession(); if
	 * (session.getAttributeNames().hasMoreElements()) { List<TSRoleFunction>
	 * TSRoleFunctionList = (List<TSRoleFunction>)session.getAttribute(roleId);
	 * if (TSRoleFunctionList != null) { return TSRoleFunctionList; } else {
	 * return null; } } else { return null; } }
	 */
	/**
	 * 获得请求路径
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestPath(HttpServletRequest request) {
		String requestPath = request.getRequestURI() + "?" + request.getQueryString();
		if (requestPath.indexOf("&") > -1) {// 去掉其他参数
			requestPath = requestPath.substring(0, requestPath.indexOf("&"));
		}
		requestPath = requestPath.substring(request.getContextPath().length() + 1);// 去掉项目路径
		return requestPath;
	}

	/**
	 * 获取配置文件参数
	 * 
	 * @param name
	 * @return
	 */
	public static final String getConfigByName(String name) {
		return bundle.getString(name);
	}

	/**
	 * 获取配置文件参数
	 * 
	 * @param name
	 * @return
	 */
	public static final Map<Object, Object> getConfigMap(String path) {
		ResourceBundle bundle = ResourceBundle.getBundle(path);
		Set set = bundle.keySet();
		return oConvertUtils.SetToMap(set);
	}

	public static String getSysPath() {
		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		String temp = path.replaceFirst("file:/", "").replaceFirst("WEB-INF/classes/", "");
		String separator = System.getProperty("file.separator");
		String resultPath = temp.replaceAll("/", separator + separator).replaceAll("%20", " ");
		return resultPath;
	}

	/**
	 * 获取项目根目录
	 * 
	 * @return
	 */
	public static String getPorjectPath() {
		String nowpath; // 当前tomcat的bin目录的路径 如
						// D:\java\software\apache-tomcat-6.0.14\bin
		String tempdir;
		nowpath = System.getProperty("user.dir");
		tempdir = nowpath.replace("bin", "webapps"); // 把bin 文件夹变到 webapps文件里面
		tempdir += "\\"; // 拼成D:\java\software\apache-tomcat-6.0.14\webapps\sz_pro
		return tempdir;
	}

	public static String getClassPath() {
		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		String temp = path.replaceFirst("file:/", "");
		String separator = System.getProperty("file.separator");
		String resultPath = temp.replaceAll("/", separator + separator);
		return resultPath;
	}

	public static String getSystempPath() {
		return System.getProperty("java.io.tmpdir");
	}

	public static String getSeparator() {
		return System.getProperty("file.separator");
	}

	public static String getParameter(String field) {
		HttpServletRequest request = ContextHolderUtils.getRequest();
		return request.getParameter(field);
	}

	/**
	 * 获取数据库类型
	 * 
	 * @return
	 * @throws Exception
	 */
	public static final String getJdbcUrl() {
		return DBTypeUtil.getDBType().toLowerCase();
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月16日 下午5:10:34
	 * @Decription 获得用户实体
	 *
	 * @return
	 */
	public static UserEntity getUserEntity() {
		HttpSession session = ContextHolderUtils.getSession();
		if (StringUtil.isNotEmpty(session.getId())) {
			Client client = ClientManager.getInstance().getClient(
					session.getId());
			if (client != null) {
				UserEntity user = client.getUser();
				return user;
			}
		}
		return null;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月16日 下午9:50:23
	 * @Decription 获取用户Id
	 *
	 * @return
	 */
	public static String getUserId() {
		UserEntity entity = getUserEntity();
		if (entity != null) {
			return entity.getId();
		}
		return null;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月16日 下午9:50:33
	 * @Decription 获取用户名
	 *
	 * @return
	 */
	public static String getUsername() {
		UserEntity entity = getUserEntity();
		if (entity != null) {
			return entity.getUserName();
		}
		return null;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月16日 下午9:50:33
	 * @Decription 获取用户名
	 *
	 * @return
	 */
	public static String getName() {
		UserEntity entity = getUserEntity();
		if (entity != null) {
			return entity.getName();
		}
		return null;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年11月20日 下午3:49:01
	 * @Decription 获得HttpSession对象
	 *
	 * @return
	 */
	public static HttpSession getSession() {
		return ContextHolderUtils.getSession();
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年11月20日 下午3:49:01
	 * @Decription 获得SessionId
	 *
	 * @return
	 */
	public static String getSessionId() {
		return getSession().getId();
	}

	/**
	 * 找到登陆人的所有角色
	 * 
	 * @return
	 */
	/*public static List<RoleEntity> getRoleList() {
		HttpSession session = ContextHolderUtils.getSession();
		if (StringUtil.isNotEmpty(session.getId())) {
			Client client = ClientManager.getInstance().getClient(session.getId());
			return client.getRoleList();
		}
		return null;

	}*/
	
	/*public static List<String> getRoleIds() {
		List<RoleEntity> roles = getRoleList();
		List<String> roleIds=new ArrayList<String>();
		if(roles!=null && roles.size()>0){
			for(RoleEntity role:roles){
				roleIds.add(role.getId());
			}
		}
		return roleIds;
	}*/
	
	/*public static List<String> getRoleQuoteIds() {
		return StringUtil.addQuotes(getRoleIds());
	}*/

	/**
	 * 找到登陆人的所有有权限机构
	 * 
	 * @return
	 */
	/*public static List<OrgnaizationEntity> getOrgList() {
		Client client = getClient();
		if (client != null) {
			return client.getOrgList();
		}
		return null;
	}*/
	
	/*public static List<String> getOrgIds() {
		List<OrgnaizationEntity> orgList=getOrgList();
		List<String> orgIds=new ArrayList<String>();
		if(orgList!=null && orgList.size()>0){
			for(OrgnaizationEntity org:orgList){
				orgIds.add(org.getId());
			}
		}
		return orgIds;
	}*/
	
	/*public static List<String> getOrgQuoteIds() {
		return StringUtil.addQuotes(getOrgIds());
	}*/

	/**
	 * 找到登陆人的所有有权限机构
	 * 
	 * @return
	 */
	/*public static List<OrgnaizationEntity> getOrgListByPId(String pId) {
		List<OrgnaizationEntity> orgEntityList = new ArrayList<OrgnaizationEntity>();
		List<OrgnaizationEntity> allOrgList = getManagerOrgList();
		for (OrgnaizationEntity orgnaization : allOrgList) {
			if (orgnaization.getParent() == null && ("-9999".equals(pId)) || (orgnaization.getParent() != null && pId.equals(orgnaization.getParent().getId()))) {
				orgEntityList.add(orgnaization);
			}
		}
		return orgEntityList;
	}*/
	
	/**
	 * 找到登陆人的所有有权限机构的字符串形式(','分割)
	 * 
	 * @return
	 */
	/*public static List<String> getOrgIdsList() {
		Client client = getClient();
		if (client != null) {
			List<String> managerOrgList = client.getManagerOrgList();
			return managerOrgList;
		}
		return null;
	}*/
	
	/**
	 * 找到登陆人的所有有权限机构的字符串形式(','分割)
	 * 
	 * @return
	 */
	/*public static String getOrgIds() {
		Client client = getClient();
		if (client != null) {
			List<String> managerOrgList = client.getManagerOrgList();
			String orgIds = StringUtil.listToStringSlipStr(managerOrgList, ",");
			return orgIds;
		}
		return null;
	}*/
	
	/*public static List<OrgnaizationEntity> getManagerOrgList() {
		Client client = getClient();
		if (client != null) {
			return client.getManagerOrgList();
		}
		return null;
	}*/
	
	/*public static List<String> getManagerOrgIds() {
		List<OrgnaizationEntity> orgList=getManagerOrgList();
		List<String> orgIds=new ArrayList<String>();
		if(orgList!=null && orgList.size()>0){
			for(OrgnaizationEntity org:orgList){
				orgIds.add(org.getId());
			}
		}
		return orgIds;
	}*/
	
	/*public static List<String> getManagerOrgQuoteIds() {
		return StringUtil.addQuotes(getManagerOrgIds());
	}*/
	
	/**
	 * 获得当前所在组织Id
	 * @return
	 */
	public static String getCurrentOrgId() {
		UserEntity user = getUserEntity();
		if (user != null) {
			return user.getOrgIds();
		} else {
			return null;
		}
	}
	
	public static void main(String[] args) {
		com.xplatform.base.framework.core.util.LogUtil.info(getPorjectPath());
		com.xplatform.base.framework.core.util.LogUtil.info(getSysPath());

	}
}
