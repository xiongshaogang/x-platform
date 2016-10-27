package com.xplatform.base.framework.tag.core.easyui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;

import com.xplatform.base.framework.core.constant.Globals;
import com.xplatform.base.framework.core.util.FreemarkerHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.oConvertUtils;
import com.xplatform.base.framework.tag.vo.easyui.OptTypeDirection;
import com.xplatform.base.framework.tag.vo.easyui.TreeModel;
import com.xplatform.base.framework.tag.vo.easyui.TreeUrl;
import com.xplatform.base.platform.common.utils.ClientUtil;

/**
 * 
 * description :树形控件封装标签
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月17日 下午4:25:47
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- -------------------  -----------------------------------
 * xiehs           2014年5月17日 下午4:25:47
 * xiaqiang        2014年6月3日 下午4:25:47
 */
public class TreeTag extends TreeModel {
	private static final long serialVersionUID = 1L;
	private List<TreeUrl> urlList = new ArrayList<TreeUrl>();// 树右键操作列表
	private List<TreeUrl> toolBarList = new ArrayList<TreeUrl>();// 工具条列表
	Map<String, Object> root = new HashMap<String, Object>();

	private String id; //树控件id
	private String gridId; //右列表id
	private String gridTreeFilter; //右列表被左树过滤的字段的名称
	private boolean onlyLeafClick = false;//是否只能子节点被点击
	private boolean clickFirstNode = false;//是否首次加载时点击第一个节点
	private String clickPreFun; //树节点点击前事件
	private boolean showOptMenu = false;//是否展现树的操作按钮
	private String loadFilter;//返回过滤过的数据进行展示。返回数据是标准树格式。该函数具备以下参数：data：加载的原始数据,parent: DOM对象，代表父节点。 
	
	private String onLoadSuccess; //自定义加载成功事件
	private String onDblClick;//双击一个节点的时候触发。
	private String onBeforeLoad;//在请求加载远程数据之前触发，返回false可以取消加载操作。

	public int doStartTag() throws JspTagException {
		// 清空资源
		urlList.clear();
		toolBarList.clear();
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		try {
			JspWriter out = this.pageContext.getOut();
			FreemarkerHelper viewEngine = new FreemarkerHelper();
			root.clear();
			root.put("id", id);
			root.put("gridId", gridId);
			root.put("url", url);
			root.put("animate", animate);
			root.put("checkbox", checkbox);
			root.put("cascadeCheck", cascadeCheck);
			root.put("onlyLeafCheck", onlyLeafCheck);
			root.put("onlyLeafClick", onlyLeafClick);
			root.put("dnd", dnd);
			root.put("lines", lines);
			root.put("clickFirstNode", clickFirstNode);
			root.put("clickPreFun", clickPreFun);
			root.put("showOptMenu", showOptMenu);
			root.put("urlList", urlList);
			root.put("gridTreeFilter", gridTreeFilter);
			root.put("loadFilter", loadFilter);
			root.put("onLoadSuccess", onLoadSuccess);
			root.put("onDblClick", onDblClick);
			root.put("onBeforeLoad", onBeforeLoad);
			String html = viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/tree.ftl", root);
			out.print(html);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	/**
	 * 设置工具条,暂时没用
	 */
	public void setToolbar() {
	}

	/**
	 * 设置树右键弹出的操作按钮
	 */

	public void setFunUrl(String title, String url, String exp, String funname, String icon, String buttonType,
			String operationCode, String width, String height, String preinstallWidth, String callback, String message) {
		TreeUrl treeUrl = new TreeUrl();
		treeUrl.setTitle(title);
		treeUrl.setUrl(url);
		treeUrl.setExp(exp);
		treeUrl.setFunname(funname);
		treeUrl.setIcon(icon);
		treeUrl.setButtonType(OptTypeDirection.valueOf(buttonType));
		treeUrl.setWidth(width);
		treeUrl.setHeight(height);
		treeUrl.setPreinstallWidth(preinstallWidth);
		treeUrl.setCallback(callback);
		treeUrl.setMessage(message);
		installOperationCode(treeUrl, operationCode, urlList);

	}

	/**
	 * 描述：组装菜单按钮操作权限
	 * dateGridUrl：url
	 * operationCode：操作码
	 * optList： 操作列表
	 * @version 1.0
	 */
	private void installOperationCode(TreeUrl dataGridUrl, String operationCode, List optList) {
		if (ClientUtil.getUserEntity().getUserName().equals("admin") || !Globals.BUTTON_AUTHORITY_CHECK) {
			optList.add(dataGridUrl);
		} else if (StringUtil.isNotEmpty(operationCode)) {
			Set<String> operationCodes = (Set<String>) super.pageContext.getRequest().getAttribute("operationCodes");
			if (null != operationCodes) {
				for (String MyoperationCode : operationCodes) {
					if (MyoperationCode.equals(operationCode)) {
						optList.add(dataGridUrl);
					}
				}
			}
		} else {
			optList.add(dataGridUrl);
		}
	}

	public Map<String, Object> getRoot() {
		return root;
	}

	public void setRoot(Map<String, Object> root) {
		this.root = root;
	}

	public List<TreeUrl> getUrlList() {
		return urlList;
	}

	public void setUrlList(List<TreeUrl> urlList) {
		this.urlList = urlList;
	}

	public List<TreeUrl> getToolBarList() {
		return toolBarList;
	}

	public void setToolBarList(List<TreeUrl> toolBarList) {
		this.toolBarList = toolBarList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGridId() {
		return gridId;
	}

	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	public boolean getOnlyLeafClick() {
		return onlyLeafClick;
	}

	public void setOnlyLeafClick(boolean onlyLeafClick) {
		this.onlyLeafClick = onlyLeafClick;
	}

	public boolean getClickFirstNode() {
		return clickFirstNode;
	}

	public void setClickFirstNode(boolean clickFirstNode) {
		this.clickFirstNode = clickFirstNode;
	}

	public String getClickPreFun() {
		return clickPreFun;
	}

	public void setClickPreFun(String clickPreFun) {
		this.clickPreFun = clickPreFun;
	}

	public boolean getShowOptMenu() {
		return showOptMenu;
	}

	public void setShowOptMenu(boolean showOptMenu) {
		this.showOptMenu = showOptMenu;
	}

	public String getGridTreeFilter() {
		return gridTreeFilter;
	}

	public void setGridTreeFilter(String gridTreeFilter) {
		this.gridTreeFilter = gridTreeFilter;
	}

	public String getOnLoadSuccess() {
		return onLoadSuccess;
	}

	public void setOnLoadSuccess(String onLoadSuccess) {
		this.onLoadSuccess = onLoadSuccess;
	}

	public String getOnDblClick() {
		return onDblClick;
	}

	public void setOnDblClick(String onDblClick) {
		this.onDblClick = onDblClick;
	}

	public String getLoadFilter() {
		return loadFilter;
	}

	public void setLoadFilter(String loadFilter) {
		this.loadFilter = loadFilter;
	}

	public String getOnBeforeLoad() {
		return onBeforeLoad;
	}

	public void setOnBeforeLoad(String onBeforeLoad) {
		this.onBeforeLoad = onBeforeLoad;
	}

}
