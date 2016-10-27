package com.xplatform.base.system.type.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.system.type.entity.TypeEntity;
import com.xplatform.base.system.type.mybatis.vo.TypeTreeVo;

public interface TypeService {

	/**
	 * 保存系统分类
	 * 
	 * @author luoheng
	 * @param type
	 * @return
	 * @throws BusinessException
	 */
	public String saveType(TypeEntity type) throws BusinessException;

	/**
	 * 修改系统分类
	 * 
	 * @author luoheng
	 * @param type
	 * @throws BusinessException
	 */
	public void updateType(TypeEntity type) throws BusinessException;

	/**
	 * 删除系统分类
	 * 
	 * @author luoheng
	 * @param id
	 * @throws BusinessException
	 */
	public void delete(String id) throws BusinessException;

	/**
	 * 批量删除系统分类
	 * 
	 * @author luoheng
	 * @param id
	 * @throws BusinessException
	 */
	public void batchDelete(String ids) throws BusinessException;

	/**
	 * 根据ID获取系统分类信息
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public TypeEntity getType(String id);

	/**
	 * 获取所有系统分类信息
	 * 
	 * @author luoheng
	 * @return
	 * @throws BusinessException
	 */
	public List<TypeEntity> queryTypeList() throws BusinessException;

	/**
	 * 获取系统分类分页列表
	 * 
	 * @author luoheng
	 * @param cq
	 * @param b
	 * @throws BusinessException
	 */
	public void getDataGridReturn(CriteriaQuery cq, boolean b);

	/**
	 * 根据属性、值查询实体
	 * 
	 * @author luoheng
	 * @param propertyName
	 * @param value
	 * @return
	 * @throws BusinessException
	 */
	public List<TypeEntity> queryListByPorperty(String propertyName, String value) ;

	/**
	 * 根据权限查询系统类型树
	 * 
	 * @author luoheng
	 * @param propertyName
	 * @param value
	 * @return
	 * @throws BusinessException
	 */
	public List<TypeTreeVo> queryTypeTree(String userId, String parentId) throws BusinessException;

	/**
	 * 根据用户ID查询该用户可操作的系统类型
	 * 
	 * @param userId
	 * @param parentId
	 * @return
	 * @throws BusinessException
	 */
	public List<TypeTreeVo> queryUserTypeTree(String userId, String parentId) throws BusinessException;

	/**
	 * 根据类型、用户权限查询系统类型
	 * 
	 * @author luoheng
	 * @param param
	 *            (userId、sysType)
	 * @see com/xplatform/base/system/type/maybatis/mapper/Type.xml
	 *      queryTypeRoleTreeBySysType
	 * @return
	 * @throws BusinessException
	 */
	public List<TypeTreeVo> queryTypeRoleTreeBySysType(Map<String, String> param) throws BusinessException;

	public List<TypeTreeVo> queryTypeRoleTreeBySysTypeTree(Map<String, String> param) throws BusinessException;

	/**
	 * 根据用户权限分页查询系统类型
	 * 
	 * @author luoheng
	 * @param userId
	 * @param parentId
	 * @param dataGrid
	 * @return
	 */
	public Page<TypeTreeVo> findByPage(Map<String, String> param, DataGrid dataGrid);

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月4日 下午5:11:42
	 * @Decription 通过code得到分类Id
	 *
	 * @param code
	 * @return
	 */
	public String queryTypeIdByCode(String code);

	/**
	 * 根据用户ID查询该用户可操作的文件夹类型
	 * 
	 * @param userId
	 * @param parentId
	 * @return
	 * @throws BusinessException
	 */
	public List<TypeTreeVo> queryUserFileTypeTree(String userId, String parentId);

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月7日 下午6:37:03
	 * @Decription 更新文件分类用户权限
	 *
	 * @param checkedTypeIds
	 *            选中的id集合
	 * @param pIds
	 *            未选中的父id集合
	 * @param userId
	 *            用户id
	 * @throws BusinessException
	 */
	public void updateUserFileType(String checkedTypeIds, String pIds, String userId) throws BusinessException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月8日 下午1:23:19
	 * @Decription
	 *
	 * @param resourceIds
	 * @param typeId
	 * @param userId
	 * @throws BusinessException
	 */
	public void updateUserFileTypeAuthority(String resourceIds, String typeId, String userId) throws BusinessException;

	/**
	 * 根据角色ID查询该用户可操作的文件夹类型
	 * 
	 * @param userId
	 * @param parentId
	 * @return
	 * @throws BusinessException
	 */
	public List<TypeTreeVo> queryRoleFileTypeTree(String roleId, String parentId);

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月7日 下午6:37:03
	 * @Decription 更新文件分类角色权限
	 *
	 * @param checkedTypeIds
	 *            选中的id集合
	 * @param pIds
	 *            未选中的父id集合
	 * @param userId
	 *            角色id
	 * @throws BusinessException
	 */
	public void updateRoleFileType(String checkedTypeIds, String pIds, String roleId) throws BusinessException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月8日 下午1:23:19
	 * @Decription
	 *
	 * @param resourceIds
	 * @param typeId
	 * @param userId
	 * @throws BusinessException
	 */
	public void updateRoleFileTypeAuthority(String resourceIds, String typeId, String roleId) throws BusinessException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月8日 下午6:00:09
	 * @Decription 查找带勾选的根节点记录
	 *
	 * @param roleId
	 *            角色Id
	 * @param typeId
	 *            根节点分类Id
	 * @return
	 * @throws BusinessException
	 */
	public List<TypeTreeVo> queryRoleFileTypeRoot(String roleId, String typeId) ;

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月8日 下午6:56:31
	 * @Decription 查找带勾选的根节点记录
	 *
	 * @param userId
	 *            用户Id
	 * @param typeId
	 *            根节点分类Id
	 * @return
	 * @throws BusinessException
	 */
	public List<TypeTreeVo> queryUserFileTypeRoot(String userId, String typeId);

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月11日 上午10:03:21
	 * @Decription 通过条件获得分类Id
	 *
	 * @param parentId
	 *            父Id
	 * @param sysType
	 *            系统类型数据字典值
	 * @return
	 * @throws BusinessException
	 */
	public String queryTypeIdByCondition(String parentId, String sysType) ;

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月11日 下午2:59:52
	 * @Decription
	 *
	 * @param treeIndex
	 *            传入的树id路径
	 * @param separator
	 *            返回时名称的分隔符
	 * @return 返回用separator分割的树名称路径
	 * @throws BusinessException
	 */
	public String parseTreeIndexToCodePath(String treeIndex, String separator) throws BusinessException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月11日 下午3:21:24
	 * @Decription 通过id获得treeIndex再截取出第一个id,得到父实体
	 *
	 * @param typeId
	 *            当前节点ID
	 * @return
	 * @throws BusinessException
	 */
	public TypeEntity getRootById(String typeId) ;

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月3日 下午4:39:47
	 * @Decription 通过code获得目录实体
	 *
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public TypeEntity queryTypeEntityByCode(String code) ;

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月16日 下午10:07:29
	 * @Decription 查询用户或角色能够看到的文件夹
	 *
	 * @param param
	 * @return
	 */
	public List<TypeTreeVo> queryViewableTypeTree(Map<String, String> param);

	/**
	 * 删除单个资料相关文件角色权限
	 * 
	 * @param attachId
	 *            资料id
	 * @throws BusinessException
	 */
	public void deleteFileRolesByAttachId(String attachId) throws BusinessException;

	/**
	 * 删除多个资料相关文件角色权限
	 * 
	 * @param attachIds
	 * @throws BusinessException
	 */
	public void deleteFileRolesByAttachIds(String attachIds) throws BusinessException;

	/**
	 * 通过资料Id集合和角色Id集合批量增、删
	 * @param attachIds
	 * @param roleIds
	 * @throws BusinessException
	 */
	public void batchUpdateFileRoles(String attachIds, String roleIds) throws BusinessException;
	
	/**
	 * 保存文件用户权限记录
	 * @param attachIds
	 * @param userIds
	 * @throws BusinessException
	 */
	public void saveFileUsers(String attachIds, String userIds) throws BusinessException ;
	
	/**
	 * 删除文件用户权限记录
	 * @param attachId
	 * @param userId
	 * @throws BusinessException
	 */
	public void deleteFileUsers(String attachId, String userId) throws BusinessException ;
	
	/**
	 * 查询当前组织根目录下的文件夹
	 * @param userId
	 * @return
	 */
	public TypeEntity queryCurrentOrgRootType(String orgId);
	
	/**
	 * 查询是否有code开头的type记录
	 * @param code
	 * @return
	 */
	public List<TypeEntity> queryTypeListByLikeCode(String code);
	
	/**
	 * 查询无重复的type的code(不断+1)
	 * @param code
	 * @return
	 */
	public String getUnRepeatCode(String code);
	
	/**
	 * 修改文件夹名
	 * @param name 新文件名
	 * @param id 文件id
	 */
	public void updateName(String name,String id) throws BusinessException;
	
	/**
	 * 批量更新文件夹组织权限
	 * @param typeIds
	 * @param orgIds
	 * @throws BusinessException
	 */
	public void batchUpdateFileTypeOrgs(String typeIds, String orgIds) throws BusinessException;
	
	/**
	 * 批量更新文件夹用户权限
	 * @param typeIds
	 * @param userIds
	 * @throws BusinessException
	 */
	public void batchUpdateFileTypeUsers(String typeIds, String userIds) throws BusinessException;
	
	/**
	 * 根据综合选择更新文件夹查看权限
	 * @param typeIds
	 * @param finalValue
	 * @throws BusinessException
	 */
	public void updateFileTypeAuthority(String typeIds, String finalValue) throws BusinessException;
	
	/**
	 * 查询综合选择字符串
	 * @param typeId
	 * @return
	 * @throws BusinessException
	 */
	public List<Map<String, Object>> queryOrgTypeAuthority(String typeId) throws BusinessException;
	
	/**
	 * 初始化个人根文件夹
	 * @param userId
	 * @throws BusinessException
	 */
	public String initPersonalType(String userId) throws BusinessException ;
	
	/**
	 * 初始化公司根文件夹
	 * @param orgId
	 * @throws BusinessException
	 */
	public String initOrgType(String orgId) throws BusinessException ;
	
	/**
	 * 查询个人根目录文件夹
	 * @param userId
	 * @return
	 */
	public String doQueryPersonalRootType(String userId) throws BusinessException;
	
	/**
	 * 查询公司根目录文件夹
	 * @param orgId
	 * @return
	 */
	public String queryOrgRootType(String orgId);
	
	/**
	 * 批量删除文件夹
	 * @param ids
	 * @throws BusinessException
	 */
	public void deleteByIds(String ids) throws BusinessException;
	
	/**
	 * 查询同名文件夹,并返回查询到的个数
	 * @param typeId
	 * @param name
	 * @return
	 */
	public Long querySameNameType(String typeId, String name);
	
	/**
	 * 查询同系列名文件夹,并返回查询到的个数
	 * @param typeId
	 * @param name
	 * @return
	 */
	public Long querySerialNameType(String typeId, String name);
	
	/**
	 * 获得重名处理后的文件夹名
	 * @param typeId
	 * @param name
	 * @return
	 */
	public String getUnrepeatName(String typeId, String name);
	
	/**
	 * 更新文件夹名称
	 * @param name
	 * @param id
	 * @throws BusinessException
	 */
	public void updateTypeName(String name, String id) throws BusinessException;
}
