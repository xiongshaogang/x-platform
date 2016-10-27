package com.xplatform.base.system.category.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.system.category.entity.CategoryEntity;
import com.xplatform.base.system.category.mybatis.vo.CategoryTreeVo;

public interface CategoryService {
	/**
	 * 保存系统分类
	 * 
	 * @author luoheng
	 * @param type
	 * @return
	 * @throws BusinessException
	 */
	public String saveCategory(CategoryEntity category) throws BusinessException;

	/**
	 * 修改系统分类
	 * 
	 * @author luoheng
	 * @param category
	 * @throws BusinessException
	 */
	public void updateCategory(CategoryEntity category) throws BusinessException;

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
	public CategoryEntity getCategory(String id);

	/**
	 * 获取所有系统分类信息
	 * 
	 * @author luoheng
	 * @return
	 * @throws BusinessException
	 */
	public List<CategoryEntity> queryCategoryList() throws BusinessException;

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
	public List<CategoryEntity> queryListByPorperty(String propertyName, String value) ;

	/**
	 * 根据权限查询系统类型树
	 * 
	 * @author luoheng
	 * @param propertyName
	 * @param value
	 * @return
	 * @throws BusinessException
	 */
	public List<CategoryTreeVo> queryCategoryTree(String userId, String parentId) throws BusinessException;

	/**
	 * 根据用户ID查询该用户可操作的系统类型
	 * 
	 * @param userId
	 * @param parentId
	 * @return
	 * @throws BusinessException
	 */
	public List<CategoryTreeVo> queryUserCategoryTree(String userId, String parentId) throws BusinessException;

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
	public List<CategoryTreeVo> queryTypeRoleTreeBySysType(Map<String, String> param) throws BusinessException;

	public List<CategoryTreeVo> queryTypeRoleTreeBySysTypeTree(Map<String, String> param) throws BusinessException;

	/**
	 * 根据用户权限分页查询系统类型
	 * 
	 * @author luoheng
	 * @param userId
	 * @param parentId
	 * @param dataGrid
	 * @return
	 */
	public Page<CategoryTreeVo> findByPage(Map<String, String> param, DataGrid dataGrid);

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月4日 下午5:11:42
	 * @Decription 通过code得到分类Id
	 *
	 * @param code
	 * @return
	 */
	public String queryCategoryIdByCode(String code);

	/**
	 * 根据用户ID查询该用户可操作的文件夹类型
	 * 
	 * @param userId
	 * @param parentId
	 * @return
	 * @throws BusinessException
	 */
	public List<CategoryTreeVo> queryUserFileCategoryTree(String userId, String parentId);

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
	public void updateUserFileCategory(String checkedCategoryIds, String pIds, String userId) throws BusinessException;

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
	public void updateUserFileCategoryAuthority(String resourceIds, String categoryId, String userId) throws BusinessException;

	/**
	 * 根据角色ID查询该用户可操作的文件夹类型
	 * 
	 * @param userId
	 * @param parentId
	 * @return
	 * @throws BusinessException
	 */
	public List<CategoryTreeVo> queryRoleFileCategoryTree(String roleId, String parentId);

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
	public void updateRoleFileCategory(String checkedCategoryIds, String pIds, String roleId) throws BusinessException;

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
	public void updateRoleFileCategoryAuthority(String resourceIds, String categoryId, String roleId) throws BusinessException;

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
	public List<CategoryTreeVo> queryRoleFileCategoryRoot(String roleId, String categoryId) ;

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
	public List<CategoryTreeVo> queryUserFileCategoryRoot(String userId, String categoryId);

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
	public String queryCategoryIdByCondition(String parentId, String sysType) ;

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
	public CategoryEntity getRootById(String categoryId) ;

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月3日 下午4:39:47
	 * @Decription 通过code获得目录实体
	 *
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public CategoryEntity queryCategoryEntityByCode(String code) ;

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月16日 下午10:07:29
	 * @Decription 查询用户或角色能够看到的文件夹
	 *
	 * @param param
	 * @return
	 */
	public List<CategoryTreeVo> queryViewableCategoryTree(Map<String, String> param);

	/**
	 * 删除单个资料相关文件角色权限
	 * 
	 * @param dataId
	 *            资料id
	 * @throws BusinessException
	 */
	public void deleteFileRolesByDataId(String dataId) throws BusinessException;

	/**
	 * 删除多个资料相关文件角色权限
	 * 
	 * @param dataIds
	 * @throws BusinessException
	 */
	public void deleteFileRolesByDataIds(String dataIds) throws BusinessException;
}
