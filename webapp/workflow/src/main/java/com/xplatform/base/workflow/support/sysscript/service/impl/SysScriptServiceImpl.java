/**
 * Copyright (c) 2014
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.xplatform.base.workflow.support.sysscript.service.impl;

import java.util.Iterator;
import java.util.List;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.platform.common.script.IScript;
import com.xplatform.base.workflow.support.sysscript.dao.SysScriptDao;
import com.xplatform.base.workflow.support.sysscript.entity.SysScriptEntity;
import com.xplatform.base.workflow.support.sysscript.service.SysScriptService;

/**
 * description : 
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月2日 下午6:08:57
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年7月2日 下午6:08:57 
 *
*/
@Service("sysScriptService")
public class SysScriptServiceImpl implements SysScriptService, IScript {

	private SysScriptDao sysScriptDao;

	@Override
	@Action(moduleCode = "sysScriptManager", description = "脚本新增", detail = "脚本${entity.name}添加成功", execOrder = ActionExecOrder.BEFORE)
	public String save(SysScriptEntity entity) throws BusinessException {
		return (String) sysScriptDao.add(entity);
	}

	@Override
	@Action(moduleCode = "sysScriptManager", description = "脚本删除", detail = "id为${id}的脚本删除成功", execOrder = ActionExecOrder.BEFORE)
	public void delete(String id) throws BusinessException {
		sysScriptDao.delete(id);
	}

	@Override
	@Action(moduleCode = "sysScriptManager", description = "脚本批量删除", detail = "id集合为${ids}的脚本删除成功", execOrder = ActionExecOrder.BEFORE)
	public void batchDelete(String ids) throws BusinessException {
		if (StringUtil.isNotEmpty(ids)) {
			String[] idArray = ids.split(",");
			for (String id : idArray) {
				this.delete(id);
			}
		}
	}

	@Override
	@Action(moduleCode = "sysScriptManager", description = "脚本更新", detail = "脚本${entity.name}更新成功", execOrder = ActionExecOrder.BEFORE)
	public void update(SysScriptEntity entity) throws BusinessException {
		SysScriptEntity oldEntity = sysScriptDao.get(entity.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(entity, oldEntity);
			sysScriptDao.update(oldEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new BusinessException("脚本更新失败", e);
		}
	}

	@Override
	public SysScriptEntity get(String id){
		return sysScriptDao.get(id);
	}

	@Override
	public List<SysScriptEntity> queryList(){
		return sysScriptDao.queryList();
	}

	@Override
	public List<SysScriptEntity> queryListByPorperty(String propertyName, String value) throws BusinessException {
		return sysScriptDao.findByProperty(SysScriptEntity.class, propertyName, value);
	}

	@Override
	public DataGridReturn getDataGridReturn(CriteriaQuery cq, boolean b) throws BusinessException {
		return sysScriptDao.getDataGridReturn(cq, b);
	}

	@Override
	public JSONArray getMethods(String className, String methodName) throws BusinessException, ClassNotFoundException,
			NotFoundException {
		JSONArray jarray = new JSONArray();
		Class clazz = Class.forName(className);
		ClassPool parent = ClassPool.getDefault();
		ClassClassPath classPath = new ClassClassPath(this.getClass());
		parent.insertClassPath(classPath);
		CtClass cc = parent.get(clazz.getName());
		CtMethod[] methods = cc.getDeclaredMethods();

		for (int i = 0; i < methods.length; i++) {
			CtMethod method = methods[i];
			String mName = method.getName();
			//获得方法修饰符
			int modifirer = method.getModifiers();
			//返回值
			String returnType = method.getReturnType().getName();
			//只取public的方法
			if ((mName.toLowerCase()).contains(methodName.toLowerCase()) && modifirer == 1) {
				JSONObject jobj = new JSONObject();
				JSONArray jaryPara = new JSONArray();
				// 使用javaassist的反射方法获取方法的参数名  
				MethodInfo methodInfo = method.getMethodInfo();
				CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
				LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
						.getAttribute(LocalVariableAttribute.tag);
				if (attr == null) {
					// exception  
				}
				CtClass[] paraAry = method.getParameterTypes();
				for (int j = 0; j < paraAry.length; j++) {
					int pos = Modifier.isStatic(method.getModifiers()) ? 0 : 1;
					String paraName = attr.variableName(j + pos);
					CtClass para = paraAry[j];
					String paraType = para.getName();
					jaryPara.add(new JSONObject().accumulate("paraName", paraName).accumulate("paraType", paraType)
							.accumulate("paraDesc", ""));
				}
				jobj.accumulate("methodName", mName).accumulate("returnType", returnType).accumulate("para", jaryPara);
				jarray.add(jobj);
			}
		}
		//		Method[] methods = t.getDeclaredMethods();
		//		for (int i = 0; i < methods.length; i++) {
		//			Method method = methods[i];
		//			String mName = method.getName();
		//			//获得方法修饰符
		//			int modifirer = method.getModifiers();
		//			//返回值
		//			String returnType = method.getReturnType().getCanonicalName();
		//			//只取public的方法
		//			if ((mName.toLowerCase()).contains(methodName.toLowerCase()) && modifirer == 1) {
		//				JSONObject jobj = new JSONObject();
		//				JSONArray jaryPara = new JSONArray();
		//				Class[] paraAry = method.getParameterTypes();
		//				for (int j = 0; j < paraAry.length; j++) {
		//					String paraName = "arg" + j;
		//					Class para = paraAry[j];
		//					String paraType = para.getCanonicalName();
		//					jaryPara.add(new JSONObject().accumulate("paraName", paraName).accumulate("paraType", paraType)
		//							.accumulate("paraDesc", ""));
		//				}
		//				jobj.accumulate("methodName", mName).accumulate("returnType", returnType).accumulate("para", jaryPara);
		//				jarray.add(jobj);
		//			}
		//		}
		return jarray;
	}

	@Override
	public List<SysScriptEntity> queryList(String type, String enable){
		return sysScriptDao.findHql("FROM SysScriptEntity WHERE typeDict=? and enableDict=?", type, enable);
	}

	@Override
	public String getArgumentsString(String argumentsJson){
		JSONArray array = JSONHelper.toJSONArray(argumentsJson);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = array.getJSONObject(i);
			Iterator it = obj.keys();
			String paraType = "";
			String paraName = "";
			while (it.hasNext()) {
				String key = it.next().toString();
				String value = obj.get(key).toString();
				if ("paraType".equals(key)) {
					paraType = value;
				} else if ("paraName".equals(key)) {
					paraName = value;
				}
			}
//			sb.append(paraType + " " + paraName + ",");
			sb.append(paraName + ",");
		}
		String arguments = StringUtil.removeDot(sb.toString());
		return arguments;
	}

	public SysScriptDao getSysScriptDao() {
		return sysScriptDao;
	}

	@Resource
	public void setSysScriptDao(SysScriptDao sysScriptDao) {
		this.sysScriptDao = sysScriptDao;
	}

}
