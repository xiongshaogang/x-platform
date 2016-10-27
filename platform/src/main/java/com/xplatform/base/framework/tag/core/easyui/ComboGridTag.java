package com.xplatform.base.framework.tag.core.easyui;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import jodd.util.StringUtil;

import com.xplatform.base.framework.core.cache.UcgCache;
import com.xplatform.base.framework.core.cache.manager.UcgCacheManager;
import com.xplatform.base.framework.core.cache.manager.impl.UcgCacheManagerImpl;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.CacheUtil;
import com.xplatform.base.framework.core.util.FreemarkerHelper;
import com.xplatform.base.framework.core.util.JSONHelper;


/**
 * 
 * description :下拉选择框标签
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月19日 下午7:39:18
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月19日 下午7:39:18
 *
 */
public class ComboGridTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	protected String id;// ID
	protected String text;// 显示文本
	protected String name;//控件名称
	protected Integer width;//宽度
	protected Integer listWidth;//下拉框宽度
	protected Integer listHeight;//下拉框高度
	protected boolean editable=false;//定义是否可以直接到文本域中键入文本
	private boolean multiple=false;//是否多选
	protected String url;//远程数据
	private String dictCode;
	private String data;
	private String parent;
	private boolean group;
	
	
	public int doStartTag() throws JspTagException {
		
		return EVAL_PAGE;
	}
	public int doEndTag() throws JspTagException {
		Map<String,Object> root=new HashMap<String,Object>();
		root.put("id", id);
		root.put("text", text);
		root.put("name", name);
		root.put("width", width);
		root.put("listWidth", listWidth);
		root.put("listHeight", listHeight);
		root.put("editable", editable);
		root.put("url", url);
		root.put("multiple", multiple);
		root.put("dictCode", dictCode);
		root.put("parent", parent);
		if(StringUtil.isNotEmpty(data)){
			root.put("data", data);
		}else if(StringUtil.isNotEmpty(dictCode)){
			UcgCache cache=CacheUtil.getDictCache();
			Map<String,List<String>> map=(Map<String,List<String>>)cache.get("dict_"+dictCode);
			String data=JSONHelper.map2json(map);
			root.put("data", data);
		}
		try {
			if(group){
				Tag t = findAncestorWithClass(this, ComboGroupTag.class);
				ComboGroupTag parent = (ComboGroupTag) t;
				Map<String,Map<String,Object>> combo=new HashMap<String,Map<String,Object>>();
				combo.put(name, root);
				parent.setComboGroup(name,root);
			}else{
				JspWriter out = this.pageContext.getOut();
				out.print(end(root));
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
	public String end(Map<String,Object> root) {
		FreemarkerHelper viewEngine= new FreemarkerHelper();
		String html = viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/combobox.ftl", root);
		return html;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getText() {
		return text;
	}
	public String getName() {
		return name;
	}
	public String getUrl() {
		return url;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public void setListWidth(Integer listWidth) {
		this.listWidth = listWidth;
	}
	public void setListHeight(Integer listHeight) {
		this.listHeight = listHeight;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}
	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}
	public void setData(String data) {
		this.data = data;
	}
	public void setGroup(boolean group) {
		this.group = group;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	
}
