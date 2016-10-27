package com.xplatform.base.system.attachment.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.xplatform.base.system.attachment.mybatis.vo.AttachVo;

/**
 * description : 资料管理DAO
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月2日 下午5:11:32
 *
 */

public interface AttachMybatisDao {
	/**
	 * 获得文件夹权限下的文件+文件夹集合
	 * @param param
	 * @return
	 */
	public List<AttachVo> queryAttachVoList(Map<String, Object> param);

	public List<AttachVo> queryAttachVoListByAdmin(Map<String, Object> param);

	public List<String> queryAuthority(Map<String, Object> param);

	public List<AttachVo> queryUploadAttach(Map<String, Object> param);
	
	/**
	 * 获得文件角色权限下的文件集合
	 * @param param
	 * @return
	 */
	public List<AttachVo> queryFileRoleAttachVoList(Map<String, Object> param);
	
	public List<AttachVo> queryPersonalAttachs(Map<String, Object> param);
	
	public List<AttachVo> queryNotifyTypeAttachs(Map<String, Object> param);
	
	public List<AttachVo> queryRootOrgAttachs(Map<String, Object> param);
	
	public List<AttachVo> queryTypeOrgAttachs(Map<String, Object> param);
}
