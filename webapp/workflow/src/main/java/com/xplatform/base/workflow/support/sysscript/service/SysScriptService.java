package com.xplatform.base.workflow.support.sysscript.service;

import java.util.List;

import javassist.NotFoundException;
import net.sf.json.JSONArray;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.workflow.support.sysscript.entity.SysScriptEntity;

/**
 * description : 
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月2日 下午6:04:23
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年7月2日 下午6:04:23
 *
*/

public interface SysScriptService {

	public String save(SysScriptEntity entity) throws BusinessException;

	public void delete(String id) throws BusinessException;

	public void batchDelete(String ids) throws BusinessException;

	public void update(SysScriptEntity entity) throws BusinessException;

	public SysScriptEntity get(String id);

	public List<SysScriptEntity> queryList();

	public List<SysScriptEntity> queryListByPorperty(String propertyName, String value) throws BusinessException;

	public DataGridReturn getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月7日 下午12:23:11
	 * @Decription 通过类名和输入项,模糊查询与输入项近似的类方法
	 *
	 * @param className
	 * @param methodName
	 * @return
	 * @throws BusinessException
	 */
	public JSONArray getMethods(String className, String methodName) throws BusinessException, ClassNotFoundException,
			NotFoundException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月26日 下午3:05:25
	 * @Decription 查询分类下启用的脚本
	 *
	 * @param type 脚本分类
	 * @param enable 启用状态
	 * @return
	 * @throws BusinessException
	 */
	public List<SysScriptEntity> queryList(String type, String enable);

	/**
	 * @author xiaqiang
	 * @createtime 2014年11月5日 下午9:37:04
	 * @Decription 将参数jsonarray转为(参数类型 参数名,参数类型 参数名)的字符串
	 *
	 * @param argumentsJson
	 * @return
	 * @throws BusinessException
	 */
	public String getArgumentsString(String argumentsJson);
}
