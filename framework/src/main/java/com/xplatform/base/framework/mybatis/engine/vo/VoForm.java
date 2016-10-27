package com.xplatform.base.framework.mybatis.engine.vo;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.mybatis.engine.type.EntitytTypeEnum;


/**
 * 
 * <STRONG>类描述</STRONG> :表单处理结果值对象  <p>
 *   
 * @version 1.0 <p>
 * @author jiagq@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : 2012-8-27 下午04:27:04<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   修改时间                     修改内容
 * ---------------         -------------------         -----------------------------------
 * jiagq@huilan.com        2012-8-27 下午04:27:04
 *</pre>
 */
public class VoForm{
	/**
	 * 表单属性值对象集合
	 */
    private Map<String,VoAttribute> voAttributeMap ;
    /**
     * 频道key
     */
    private String pageKey;
    /**
     * 表单key
     */
    private String formKey;
    /**
     * 登录用户名
     */
    private String loginName;
    /**
     * 登录用户名
     */
    private String ip ;
    
	public String getProperty(String property){
    	String result = "";
    	List<String> typeList = EntitytTypeEnum.entitytTypeList;
    	for(String type:typeList){
    		VoAttribute attr=this.getVoAttributeMap().get(type);
    		if(attr!=null){
    		if(attr.getAttributeMap().get(property) != null){
    			result = this.getVoAttributeMap().get(type).getAttributeMap().get(property);
    			break;
    		}
    		}
    	}
    	return result;
    }
	public VoForm() {
		super();
	}
	public Map<String, VoAttribute> getVoAttributeMap() {
		return voAttributeMap;
	}
	public void setVoAttributeMap(Map<String, VoAttribute> voAttributeMap) {
		this.voAttributeMap = voAttributeMap;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}
	public String getPageKey() {
		return pageKey;
	}
	public void setPageKey(String pageKey) {
		this.pageKey = pageKey;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
}
