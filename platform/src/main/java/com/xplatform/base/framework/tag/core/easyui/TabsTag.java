package com.xplatform.base.framework.tag.core.easyui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.xplatform.base.framework.core.util.FreemarkerHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.vo.easyui.Tab;

/**
 * 
 * 类描述：选项卡标签
 * 
 * 张代浩
 * @date： 日期：2012-12-7 时间：上午10:17:45
 * @version 1.0
 */
public class TabsTag extends TagSupport {
	private String id;// 父容器Div ID
	private String width;// 宽度
	private String height;// 高度
	private boolean plain = false;// 简洁模式
	private boolean fit = true;// 铺满浏览器
	private boolean border = true;// 边框
	private Integer scrollIncrement = 100;// 滚动增量
	private Integer scrollDuration = 400;// 滚动时间
	private String tools;// 工具栏
	/* 是否自己书写子div,这样可以不用创建jsp,href方式去加载,而是直接页签内容都在1个jsp里,但是只能同步加载 */
	private boolean customChildDiv = false;
	private String tabPosition = "top";// 选项卡位置
	private String toolPosition = "top";//工具栏位置
	private Integer headerWidth = 150;//选项卡标题宽度，在tabPosition属性设置为'left'或'right'的时候才有效
	private boolean leftDiv = false;//是否产生左侧自定义div
	private String leftDivWidth;//左侧自定义div宽度
	private String leftDivTitle; //标题名称(配置本属性的话,会自动在div中产生居中span,填入标题)
	private boolean rightDiv = false;//是否产生右侧自定义div
	private String rightDivWidth;//右侧自定义div宽度
	private String rightDivTitle; //标题名称(配置本属性的话,会自动在div中产生居中span,填入标题)
	//整体风格(原理是相当于给div多加一个class)
	//现有风格: tabs-ace-style 带蓝线的ace风格(默认) tabs-ace-style2 无边框风格
	private String tabsAlign;//tab页在头上的位置(tabsAlign="center"则tabs页居中)
	private String classStyle = "tabs-ace-style";
	private boolean closeBtn = false;//是否出现关闭按钮(只适用于使用了Easyui弹出框的页签),如果右边只出现关闭按钮(推荐rightDivWidth="7")
	private boolean hBorderLeft = true;//是否出现页签头的左边框(默认true)
	private boolean hBorderRight = true;//是否出现页签头的右边框(默认true)
	private boolean hBorderTop = true;//是否出现页签头的上边框(默认true)
	private boolean hBorderBottom = false;//是否出现页签头的下边框(默认false)
	private String leftTabHeight = "35";//tabPosition为left时(选项卡在左侧样式),各选项卡的高度
	private String onLoad;//在ajax选项卡面板加载完远程数据的时候触发。
	private String onSelect;//用户选择一个选项卡面板的时候触发。
	private String onBeforeClose;//在选项卡面板关闭的时候触发，返回false取消关闭操作。下面的例子展示了在关闭选项卡面板之前以何种方式显示确认对话框。
	private String onClose;//在用户关闭一个选项卡面板的时候触发。
	private String onAdd;//在添加一个新选项卡面板的时候触发。
	private String onUpdate;//在更新一个选项卡面板的时候触发。
	private String onContextMenu;//在右键点击一个选项卡面板的时候触发。

	Map<String, Object> root = new HashMap<String, Object>();
	private List<Tab> tabList = new ArrayList<Tab>();

	/* 流程按钮前后处理事件 */
	private String nextProcessBefore = "null";//流转前事件
	private String nextProcessAfter = "null";//流转后事件
	private String changeAssigneeBefore = "null";//任务转办前事件
	private String changeAssigneeAfter = "null";//任务转办后事件
	private String rejectTaskBefore = "null";//驳回前事件
	private String rejectTaskAfter = "null";//驳回后事件
	private String rejectTaskToStartBefore = "null";//驳回到发起人前事件
	private String rejectTaskToStartAfter = "null";//驳回到发起人后事件
	private String redoTaskBefore = "null";//追回前事件
	private String redoTaskAfter = "null";//追回后事件
	private String recoverTaskBefore = "null";//撤销前事件
	private String recoverTaskAfter = "null";//撤销后事件

	public int doStartTag() throws JspTagException {
		root.clear();
		tabList.clear();
		root.put("id", id);
		root.put("width", width);
		root.put("height", height);
		root.put("plain", plain);
		root.put("fit", fit);
		root.put("border", border);
		root.put("scrollIncrement", scrollIncrement);
		root.put("scrollDuration", scrollDuration);
		root.put("tools", tools);
		root.put("customChildDiv", customChildDiv);
		root.put("tabPosition", tabPosition);
		root.put("toolPosition", toolPosition);
		root.put("headerWidth", headerWidth);
		root.put("tabList", tabList);
		root.put("leftDiv", leftDiv);
		root.put("leftDivWidth", leftDivWidth);
		root.put("leftDivTitle", leftDivTitle);
		root.put("rightDiv", rightDiv);
		root.put("rightDivWidth", rightDivWidth);
		root.put("rightDivTitle", rightDivTitle);
		root.put("tabsAlign", tabsAlign);
		root.put("classStyle", classStyle);
		root.put("closeBtn", closeBtn);
		root.put("onLoad", onLoad);
		root.put("onSelect", onSelect);
		root.put("onBeforeClose", onBeforeClose);
		root.put("onClose", onClose);
		root.put("onAdd", onAdd);
		root.put("onUpdate", onUpdate);
		root.put("onContextMenu", onContextMenu);
		root.put("hBorderLeft", hBorderLeft);
		root.put("hBorderRight", hBorderRight);
		root.put("hBorderTop", hBorderTop);
		root.put("hBorderBottom", hBorderBottom);
		root.put("leftTabHeight", leftTabHeight);

		StringBuffer sb = new StringBuffer();
		ServletRequest request = this.pageContext.getRequest();
		Object isWorkFlow = request.getAttribute("isWorkFlow");
		if (isWorkFlow != null) {
			StringBuffer flowSB = new StringBuffer();

			Integer isManage = (Integer) (request.getAttribute("isManage") == null ? 0 : request
					.getAttribute("isManage"));
			Boolean isSignTask = (Boolean) (request.getAttribute("isSignTask") == null ? false : request
					.getAttribute("isSignTask"));
			Boolean isTransfer = (Boolean) (request.getAttribute("isTransfer") == null ? false : request
					.getAttribute("isTransfer"));
			Boolean isCanBack = (Boolean) (request.getAttribute("isCanBack") == null ? false : request
					.getAttribute("isCanBack"));
			Boolean isCanAssignee = (Boolean) (request.getAttribute("isCanAssignee") == null ? false : request
					.getAttribute("isCanAssignee"));
			Boolean isHidePath = (Boolean) (request.getAttribute("isHidePath") == null ? false : request
					.getAttribute("isHidePath"));
			Boolean isAllowDirectExecute = (Boolean) (request.getAttribute("isAllowDirectExecute") == null ? false
					: request.getAttribute("isAllowDirectExecute"));
			Boolean isAllowRetoactive = (Boolean) (request.getAttribute("isAllowRetoactive") == null ? false : request
					.getAttribute("isAllowRetoactive"));
			Boolean isAllowOneVote = (Boolean) (request.getAttribute("isAllowOneVote") == null ? false : request
					.getAttribute("isAllowOneVote"));
			Boolean isFinishedDivert = (Boolean) (request.getAttribute("isFinishedDivert") == null ? false : request
					.getAttribute("isFinishedDivert"));//是否允许转发
			Boolean isCanRedo = (Boolean) (request.getAttribute("isCanRedo") == null ? false : request
					.getAttribute("isCanRedo"));//是否允许追回
			Boolean isCanRecover = (Boolean) (request.getAttribute("isCanRecover") == null ? false : request
					.getAttribute("isCanRecover"));//是否允许撤销
			String taskId = (String) request.getAttribute("taskId");
			String actInstId = (String) request.getAttribute("actInstId");
			String jumpType = (String) request.getAttribute("jumpType");
			/*isManage = 0;
			isSignTask = true;
			isAllowDirectExecute = true;
			isCanBack = true;
			isCanAssignee = true;
			isHidePath = true; 
			isAllowRetoactive = true;
			isAllowOneVote = true;
			taskId = "aa";*/
			root.put("isManage", isManage);
			root.put("isTransfer", isTransfer);
			root.put("isSignTask", isSignTask);
			root.put("isCanBack", isCanBack);
			root.put("isCanAssignee", isCanAssignee);
			root.put("isHidePath", isHidePath);
			root.put("isAllowDirectExecute", isAllowDirectExecute);
			root.put("isAllowRetoactive", isAllowRetoactive);
			root.put("isAllowOneVote", isAllowOneVote);
			root.put("isFinishedDivert", isFinishedDivert);
			root.put("isCanRedo", isCanRedo);
			root.put("isCanRecover", isCanRecover);
			root.put("taskId", taskId);
			root.put("actInstId", actInstId);
			if (StringUtil.isNotEmpty(jumpType)) {
				if (StringUtil.indexOf(jumpType, "1") != -1 || StringUtil.indexOf(jumpType, "2") != -1
						|| StringUtil.indexOf(jumpType, "3") != -1) {
					root.put("isCanJump", true);
				} else {
					root.put("isCanJump", false);
				}
			} else {
				root.put("isCanJump", false);
			}

			root.put("jumpType", jumpType);

			root.put("nextProcessBefore", nextProcessBefore);
			root.put("nextProcessAfter", nextProcessAfter);
			root.put("changeAssigneeBefore", changeAssigneeBefore);
			root.put("changeAssigneeAfter", changeAssigneeAfter);
			root.put("rejectTaskBefore", rejectTaskBefore);
			root.put("rejectTaskAfter", rejectTaskAfter);
			root.put("rejectTaskToStartBefore", rejectTaskToStartBefore);
			root.put("rejectTaskToStartAfter", rejectTaskToStartAfter);
			root.put("redoTaskBefore", redoTaskBefore);
			root.put("redoTaskAfter", redoTaskAfter);
			root.put("recoverTaskBefore", recoverTaskBefore);
			root.put("recoverTaskAfter", recoverTaskAfter);

			FreemarkerHelper viewEngine1 = new FreemarkerHelper();
			flowSB.append(viewEngine1.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/tabs_flow_button.ftl", root));

			String flowContent = flowSB.toString();
			flowContent = StringUtil.replaceHtml(flowContent);
			flowContent = StringUtil.replaceAll(flowContent, "\"", "\\\"");
			root.put("flowButton", flowContent);
		}
		FreemarkerHelper viewEngine = new FreemarkerHelper();
		sb.append(viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/tabs_start.ftl", root));

		try {
			JspWriter out = this.pageContext.getOut();
			out.print(sb.toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		try {
			JspWriter out = this.pageContext.getOut();
			StringBuffer sb = new StringBuffer();

			FreemarkerHelper viewEngine = new FreemarkerHelper();
			sb.append(viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/tabs_end.ftl", root));
			//			out.print(end().toString());
			out.print(sb.toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	//	public StringBuffer end() {
	//		StringBuffer sb = new StringBuffer();
	//
	//		FreemarkerHelper viewEngine = new FreemarkerHelper();
	//		sb.append(viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/tabs_end.ftl", root));
	//
	//		if (!customChildDiv) {
	//			for (Tab tab : tabList) {
	//				if (tab.isIframe()) {
	//					sb.append("<div title=\"" + tab.getTitle()
	//							+ "\" style=\"margin:0px;padding:0px;overflow:hidden;\">");
	//					if (tab.isAysn()) {
	//						sb.append("<iframe id=\"\'" + tab.getId() + "\'\" scrolling=\"no\" frameborder=\"0\" width=\""
	//								+ oConvertUtils.getString(tab.getWidth(), "100%") + "\" height=\""
	//								+ oConvertUtils.getString(tab.getHeigth(), "99.5%") + "\"></iframe>");
	//					} else {
	//						sb.append("<iframe id=\"\'" + tab.getId() + "\'\" scrolling=\"no\" frameborder=\"0\"  src=\""
	//								+ tab.getHref() + "\" width=\"" + oConvertUtils.getString(tab.getWidth(), "100%")
	//								+ "\" height=\"" + oConvertUtils.getString(tab.getHeigth(), "99.5%") + "\"></iframe>");
	//					}
	//
	//					sb.append("</div>");
	//				} else {
	//					sb.append("<div title=\"" + tab.getTitle() + "\" href=\"" + tab.getHref()
	//							+ "\" style=\"margin:0px;padding:0px;overflow:hidden;\"></div>");
	//				}
	//			}
	//		}
	//		sb.append("</div>");
	//		sb.append("<script type=\"text/javascript\">");
	//		sb.append("$(function(){");
	//		sb.append("$(\'#" + id + "\').tabs(");
	//		sb.append("{");
	//		sb.append("onSelect : function(title) {");
	//		sb.append("var p = $(this).tabs(\'getTab\', title);");
	//		if (tabList.size() > 0) {
	//			for (Tab tab : tabList) {
	//				if (tab.isAysn()) {
	//					sb.append("if (title == \'" + tab.getTitle() + "\') {");
	//					sb.append("p.find(\'iframe\').attr(\'src\',");
	//					sb.append("\'" + tab.getHref() + "\');}");
	//				}
	//			}
	//		}
	//		sb.append("}");
	//		sb.append("});");
	//		sb.append("});");
	//		sb.append("</script>");
	//
	//		return sb;
	//	}

	public void setTab(String id, String title, boolean iframe, String href, String iconCls, boolean cache,
			String content, String width, String height, boolean closable, boolean aysn) {
		Tab tab = new Tab();
		tab.setId(id);
		tab.setTitle(title);
		tab.setHref(href);
		tab.setCache(cache);
		tab.setIframe(iframe);
		tab.setContent(content);
		tab.setHeight(height);
		tab.setIcon(iconCls);
		tab.setWidth(width);
		tab.setClosable(closable);
		tab.setAysn(aysn);
		tabList.add(tab);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public boolean isPlain() {
		return plain;
	}

	public void setPlain(boolean plain) {
		this.plain = plain;
	}

	public boolean isFit() {
		return fit;
	}

	public void setFit(boolean fit) {
		this.fit = fit;
	}

	public boolean isBorder() {
		return border;
	}

	public void setBorder(boolean border) {
		this.border = border;
	}

	public Integer getScrollIncrement() {
		return scrollIncrement;
	}

	public void setScrollIncrement(Integer scrollIncrement) {
		this.scrollIncrement = scrollIncrement;
	}

	public Integer getScrollDuration() {
		return scrollDuration;
	}

	public void setScrollDuration(Integer scrollDuration) {
		this.scrollDuration = scrollDuration;
	}

	public String getTools() {
		return tools;
	}

	public void setTools(String tools) {
		this.tools = tools;
	}

	public String getTabPosition() {
		return tabPosition;
	}

	public void setTabPosition(String tabPosition) {
		this.tabPosition = tabPosition;
	}

	public String getToolPosition() {
		return toolPosition;
	}

	public void setToolPosition(String toolPosition) {
		this.toolPosition = toolPosition;
	}

	public Integer getHeaderWidth() {
		return headerWidth;
	}

	public void setHeaderWidth(Integer headerWidth) {
		this.headerWidth = headerWidth;
	}

	public boolean isCustomChildDiv() {
		return customChildDiv;
	}

	public void setCustomChildDiv(boolean customChildDiv) {
		this.customChildDiv = customChildDiv;
	}

	public boolean isLeftDiv() {
		return leftDiv;
	}

	public void setLeftDiv(boolean leftDiv) {
		this.leftDiv = leftDiv;
	}

	public String getLeftDivWidth() {
		return leftDivWidth;
	}

	public void setLeftDivWidth(String leftDivWidth) {
		this.leftDivWidth = leftDivWidth;
	}

	public String getLeftDivTitle() {
		return leftDivTitle;
	}

	public void setLeftDivTitle(String leftDivTitle) {
		this.leftDivTitle = leftDivTitle;
	}

	public boolean isRightDiv() {
		return rightDiv;
	}

	public void setRightDiv(boolean rightDiv) {
		this.rightDiv = rightDiv;
	}

	public String getRightDivWidth() {
		return rightDivWidth;
	}

	public void setRightDivWidth(String rightDivWidth) {
		this.rightDivWidth = rightDivWidth;
	}

	public String getRightDivTitle() {
		return rightDivTitle;
	}

	public void setRightDivTitle(String rightDivTitle) {
		this.rightDivTitle = rightDivTitle;
	}

	public String getTabsAlign() {
		return tabsAlign;
	}

	public void setTabsAlign(String tabsAlign) {
		this.tabsAlign = tabsAlign;
	}

	public String getClassStyle() {
		return classStyle;
	}

	public void setClassStyle(String classStyle) {
		this.classStyle = classStyle;
	}

	public boolean isCloseBtn() {
		return closeBtn;
	}

	public void setCloseBtn(boolean closeBtn) {
		this.closeBtn = closeBtn;
	}

	public String getOnLoad() {
		return onLoad;
	}

	public void setOnLoad(String onLoad) {
		this.onLoad = onLoad;
	}

	public String getOnSelect() {
		return onSelect;
	}

	public void setOnSelect(String onSelect) {
		this.onSelect = onSelect;
	}

	public String getOnBeforeClose() {
		return onBeforeClose;
	}

	public void setOnBeforeClose(String onBeforeClose) {
		this.onBeforeClose = onBeforeClose;
	}

	public String getOnClose() {
		return onClose;
	}

	public void setOnClose(String onClose) {
		this.onClose = onClose;
	}

	public String getOnAdd() {
		return onAdd;
	}

	public void setOnAdd(String onAdd) {
		this.onAdd = onAdd;
	}

	public String getOnUpdate() {
		return onUpdate;
	}

	public void setOnUpdate(String onUpdate) {
		this.onUpdate = onUpdate;
	}

	public String getOnContextMenu() {
		return onContextMenu;
	}

	public void setOnContextMenu(String onContextMenu) {
		this.onContextMenu = onContextMenu;
	}

	public boolean ishBorderLeft() {
		return hBorderLeft;
	}

	public void sethBorderLeft(boolean hBorderLeft) {
		this.hBorderLeft = hBorderLeft;
	}

	public boolean ishBorderRight() {
		return hBorderRight;
	}

	public void sethBorderRight(boolean hBorderRight) {
		this.hBorderRight = hBorderRight;
	}

	public boolean ishBorderTop() {
		return hBorderTop;
	}

	public void sethBorderTop(boolean hBorderTop) {
		this.hBorderTop = hBorderTop;
	}

	public boolean ishBorderBottom() {
		return hBorderBottom;
	}

	public void sethBorderBottom(boolean hBorderBottom) {
		this.hBorderBottom = hBorderBottom;
	}

	public String getLeftTabHeight() {
		return leftTabHeight;
	}

	public void setLeftTabHeight(String leftTabHeight) {
		this.leftTabHeight = leftTabHeight;
	}

	public String getNextProcessBefore() {
		return nextProcessBefore;
	}

	public void setNextProcessBefore(String nextProcessBefore) {
		this.nextProcessBefore = nextProcessBefore;
	}

	public String getNextProcessAfter() {
		return nextProcessAfter;
	}

	public void setNextProcessAfter(String nextProcessAfter) {
		this.nextProcessAfter = nextProcessAfter;
	}

}
