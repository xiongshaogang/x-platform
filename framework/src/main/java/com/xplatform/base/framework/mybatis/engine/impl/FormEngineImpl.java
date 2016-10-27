package com.xplatform.base.framework.mybatis.engine.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.xplatform.base.framework.mybatis.engine.FormEngine;

/**
 * 
 * <STRONG>类描述</STRONG> : 表单数据维护引擎 <p>
 *   
 * @version 1.0 <p>
 * @author jiagq@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : 2012-9-17 上午10:35:06<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   修改时间                     修改内容
 * ---------------         -------------------         -----------------------------------
 * jiagq@huilan.com        2012-9-17 上午10:35:06
 *</pre>
 */
@Component
public class FormEngineImpl implements FormEngine {
	/**
	 * 表单新增和更新的数据处理
	 * <pre>
	 * </pre>
	 * @param formParamMap 表单提交参数map
	 * @return VoForm对象
	 *//*
	public  VoForm saveBuild(final Map<String, String[]> formParamMap,String pageId){
		return AttributeEngine.saveBuild(formParamMap,pageId);
	}
	public  VoForm saveBuild(final Map<String, String[]> formParamMap,String pageId,String formKey,String formPageKey){
		return AttributeEngine.saveBuild(formParamMap,pageId,formKey,formPageKey);
	}
	*//**
	 * 表单删除操作的数据处理
	 * <pre>
	 * </pre>
	 * @param ids id字符串
	 * @return 处理后的表单值对象集合
	 *//*
	public  List<VoForm> deleteBuild(String ids){
		return AttributeEngine.batchOperateData(ids);
	}
	
	*//**
	 * 审核数据对象构造
	 * <pre>
	 * </pre>
	 * @param id 数据id
	 * @return VoForm对象
	 *//*
	public  List<VoForm> approveBuild(String ids){
		return AttributeEngine.batchOperateData(ids);
	}
	*//**
	 * 发布数据对象构造
	 * <pre>
	 * </pre>
	 * @param id 数据id
	 * @return VoForm对象
	 *//*
	public List<VoForm> publishBuild(String ids){
		return AttributeEngine.batchOperateData(ids);
	}*/
}
