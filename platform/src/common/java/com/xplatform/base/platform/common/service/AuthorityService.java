package com.xplatform.base.platform.common.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.xplatform.base.orgnaization.module.entity.ModuleEntity;
import com.xplatform.base.orgnaization.module.mybatis.vo.ModuleTreeVo;
import com.xplatform.base.orgnaization.resouce.mybatis.vo.ResourceVo;

public interface AuthorityService {
	
	/**
	 * 判断用户是否平台管理员
	 * @author xiehs
	 * @createtime 2014年6月3日 下午1:18:46
	 * @Decription
	 *
	 * @param param
	 * @return
	 */
	public boolean isAdmin(Map<String,String> param);
	
	/**
	 * 判断用户是否管理员
	 * @author xiaqiang
	 * @createtime 2014年10月27日 下午1:18:46
	 * @Decription
	 *
	 * @param userId
	 * @return
	 */
	public boolean isAdmin(String userId);
	
	/**
	 * 获取用户权限值对象
	 * <pre>
	 * </pre>
	 * @param map
	 * @return
	 */
	public List<ModuleTreeVo> getUserModule(Map<String, Object> map);
	
	/**
	 * 获取所有模块权限值
	 * <pre>
	 * </pre>
	 * @param map
	 * @return
	 */
	public List<ModuleTreeVo> getAllModule(Map<String, Object> param);
	
	/**
	 * 查询用户在指定模块所拥有的资源
	 * <pre>
	 * </pre>
	 * @param map
	 * @return
	 */
	public List<ResourceVo> getUserModuleResources(Map<String, Object> map);
    
    /**
     * 检查当前用户是否对参数url有访问权限
     * @param url 访问的url
     * @return 是否
     */
	public boolean checkUrlResource(String url);
    
    /**
     * 通过资源url获取该url所在的模块
     * @author xiehs
     * @createtime 2014年6月4日 上午11:51:04
     * @Decription
     *
     * @param url
     * @return
     */
    public ModuleEntity getUrlModule(String url);
	
    /**
     * 通过资源查模块
     * @author xiehs
     * @createtime 2014年6月12日 下午3:42:46
     * @Decription
     *
     * @param url
     * @return
     */
    public ModuleEntity getResourceModule(String url,HttpServletRequest request);
    
    
    /**
     * 当前登录人是否是管理员角色
     * @return
     */
    public boolean currentUserIsAdmin() ;
    
    /**
     * 当前登录人是否是机构管理员角色
     * @return
     */
    public boolean isOrgAdmin(String userId,String orgId);
    
    /**
     * 当前登录人是否是机构管理员角色
     * @param orgId
     * @return
     */
    public boolean isOrgAdmin(String orgId);
}
