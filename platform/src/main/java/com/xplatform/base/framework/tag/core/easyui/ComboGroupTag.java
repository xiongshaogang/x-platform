package com.xplatform.base.framework.tag.core.easyui;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.xplatform.base.framework.core.util.FreemarkerHelper;
import com.xplatform.base.framework.core.util.StringUtil;

/**
 * 
 * description :combo组合
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月20日 下午4:49:08
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月20日 下午4:49:08
 *
 */
public class ComboGroupTag extends TagSupport {
	 /** 
	  * serialVersionUID
	  */
	private static final long serialVersionUID = 1L;
	private List<Map<String,Object>> comboGroup =new LinkedList<Map<String,Object>>();
	private List<String> comboList=new LinkedList<String>();
	public int doStartTag() throws JspTagException {
		this.comboGroup.clear();
		this.comboList.clear();
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		try {
			JspWriter out = this.pageContext.getOut();
			out.print(end());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public String end() {
		FreemarkerHelper viewEngine= new FreemarkerHelper();
		String html="";
		//构造数据结构
		for(Map<String,Object> combo:comboGroup){
			if(StringUtil.equals("combobox", (String)combo.get("type"))){
				html += viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/combobox.ftl", combo);
			}else if(StringUtil.equals("combogrid", (String)combo.get("type"))){
				html += viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/comboGrid.ftl", combo);
			}else if(StringUtil.equals("combotree", (String)combo.get("type"))){
				html += viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/comboTree.ftl", combo);
			}
		}
		//html += viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/comboGroup.ftl", new HashMap().put("", comboGroup));
		 
		return html;
	}

	public void setComboGroup(String name,Map<String, Object> combo) {
		this.comboGroup.add(combo);
		this.comboList.add(name);
	}

	
}
