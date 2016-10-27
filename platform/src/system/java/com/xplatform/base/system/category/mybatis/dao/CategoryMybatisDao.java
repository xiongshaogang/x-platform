package com.xplatform.base.system.category.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.xplatform.base.system.category.mybatis.vo.CategoryTreeVo;


public interface CategoryMybatisDao {


	/**
	 * 根据权限查询系统类型树
	 * @author luoheng
	 * @param param
	 * @return
	 */
	List<CategoryTreeVo> queryCategoryTree(Map<String, String> param);
	
	/**
	 * 根据用户ID查询该用户可操作的系统类型
	 * @author luoheng
	 * @param param
	 * @return
	 */
	List<CategoryTreeVo> queryUserCategoryTree(Map<String, String> param);
	
	/**
	 * 根据类型、用户权限查询系统类型（数据返回所有List）
	 * @author luoheng
	 * @param param(userId、parentId、sysType)
	 * @see com/xplatform/base/system/type/maybatis/mapper/Type.xml queryTypeRoleTreeBySysType
	 * @return
	 */
	List<CategoryTreeVo> queryTypeRoleTreeBySysTypeList(Map<String, String> param);
	
	/**
	 * 根据类型、用户权限查询系统类型（数据返回Tree List）
	 * @param param
	 * @return
	 */
	List<CategoryTreeVo> queryTypeRoleTreeBySysTypeTree(Map<String, String> param);
	
	/**
	 * 根据用户权限分页查询系统类型
	 * @author luoheng
	 * @param param
	 * @return
	 */
	List<CategoryTreeVo> findByPage(Map<String, String> param);

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月4日 下午5:47:35
	 * @Decription 根据用户ID查询该用户可操作的文件夹类型
	 * 
	 * @see com/xplatform/base/system/type/maybatis/mapper/Type.xml#queryUserFileTypeTree
	 * @param param
	 * @return
	 */
	public List<CategoryTreeVo> queryUserFileCategoryTree(Map<String, String> param);
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年7月4日 下午5:47:35
	 * @Decription 根据角色ID查询该用户可操作的文件夹类型
	 * 
	 * @see com/xplatform/base/system/type/maybatis/mapper/Type.xml#queryRoleFileTypeTree
	 * @param param
	 * @return
	 */
	public List<CategoryTreeVo> queryRoleFileCategoryTree(Map<String, String> param);
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年7月4日 下午5:47:35
	 * @Decription 查找带勾选的根节点记录
	 * 
	 * @see com/xplatform/base/system/type/maybatis/mapper/Type.xml#queryRoleFileTypeRoot
	 * @param param
	 * @return
	 */
	public List<CategoryTreeVo> queryRoleFileCategoryRoot(Map<String, String> param);
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年7月4日 下午5:47:35
	 * @Decription 查找带勾选的根节点记录
	 * 
	 * @see com/xplatform/base/system/type/maybatis/mapper/Type.xml#queryUserFileTypeRoot
	 * @param param
	 * @return
	 */
	public List<CategoryTreeVo> queryUserFileCategoryRoot(Map<String, String> param);
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年9月16日 下午10:07:29
	 * @Decription 查询用户或角色能够看到的文件夹
	 *
	 * @param param
	 * @return
	 */
	public List<CategoryTreeVo> queryViewableCategoryTree(Map<String, String> param);
}
