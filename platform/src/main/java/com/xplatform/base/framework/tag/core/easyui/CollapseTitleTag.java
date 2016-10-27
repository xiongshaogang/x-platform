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
import com.xplatform.base.framework.core.common.model.json.ComboBox;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.CacheUtil;
import com.xplatform.base.framework.core.util.FreemarkerHelper;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.system.dict.service.DictValueService;

/**
 * 
 * description :表单中收展标题栏封装标签
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年5月28日 下午7:39:18
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- xiaqiang 2014年5月28日 下午7:39:18
 *
 */
public class CollapseTitleTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private String id;// 控件ID
	private String title;// 标题
	private boolean collapsible = true;// 是否可折叠
	private boolean collapsed = false;// 初始状态是否折叠
	private Integer width;// 宽度
	private Integer height;// 高度
	private boolean border = true;// 是否显示边框
	private String href;// 若用于远程页面加载的url
	private boolean cache = true;// 远程加载是否缓存
	private String loadingMessage;// 远程正在加载时提示语句
	private String openAnimation;// 定义打开面板的动画，可用值有：'slide','fade','show'
	private Integer openDuration;// 定义打开面板的持续时间
	private String closeAnimation;// 定义关闭面板的动画，可用值有：'slide','fade','show'
	private Integer closeDuration;// 定义关闭面板的持续时间
	private String method;// 使用HTTP的哪一种方法读取内容页。可用值：'get','post'
	private String queryParams;// 在加载内容页的时候添加的请求参数
	private Boolean fit = false;// 当设置为true的时候面板大小将自适应父容器
	private Boolean doSize = true;// 如果设置为true，在面板被创建的时候将重置大小和重新布局(出现滚动条后,页面的第一个CollapseTitleTag宽度不正确时,设置第一个的doSize为false)
	private Boolean noheader = false;// 如果设置为true，那么将不会创建面板标题
	private String style;// 主体Div的style属性
	Map<String, Object> root = new HashMap<String, Object>();

	public int doStartTag() throws JspTagException {
		root.clear();
		root.put("id", id);
		root.put("width", width);
		root.put("height", height);
		root.put("title", title);
		root.put("collapsible", collapsible);
		root.put("collapsed", collapsed);
		root.put("border", border);
		root.put("href", href);
		root.put("cache", cache);
		root.put("loadingMessage", loadingMessage);
		root.put("openAnimation", openAnimation);
		root.put("openDuration", openDuration);
		root.put("closeAnimation", closeAnimation);
		root.put("closeDuration", closeDuration);
		root.put("method", method);
		root.put("queryParams", queryParams);
		root.put("fit", fit);
		root.put("doSize", doSize);
		root.put("noheader", noheader);
		root.put("style", style);

		JspWriter out = this.pageContext.getOut();

		FreemarkerHelper viewEngine = new FreemarkerHelper();
		String begin_str = viewEngine.parseTemplate("/com/xplatform/base/framework/tag/core/ftl/collapseTitle_begin.ftl", root);
		try {
			out.print(begin_str);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		JspWriter out = this.pageContext.getOut();
		String end_str = "</div>";
		try {
			out.print(end_str);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isCollapsible() {
		return collapsible;
	}

	public void setCollapsible(boolean collapsible) {
		this.collapsible = collapsible;
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public boolean isBorder() {
		return border;
	}

	public void setBorder(boolean border) {
		this.border = border;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public boolean isCache() {
		return cache;
	}

	public void setCache(boolean cache) {
		this.cache = cache;
	}

	public String getLoadingMessage() {
		return loadingMessage;
	}

	public void setLoadingMessage(String loadingMessage) {
		this.loadingMessage = loadingMessage;
	}

	public Map<String, Object> getRoot() {
		return root;
	}

	public void setRoot(Map<String, Object> root) {
		this.root = root;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getOpenAnimation() {
		return openAnimation;
	}

	public void setOpenAnimation(String openAnimation) {
		this.openAnimation = openAnimation;
	}

	public Integer getOpenDuration() {
		return openDuration;
	}

	public void setOpenDuration(Integer openDuration) {
		this.openDuration = openDuration;
	}

	public String getCloseAnimation() {
		return closeAnimation;
	}

	public void setCloseAnimation(String closeAnimation) {
		this.closeAnimation = closeAnimation;
	}

	public Integer getCloseDuration() {
		return closeDuration;
	}

	public void setCloseDuration(Integer closeDuration) {
		this.closeDuration = closeDuration;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(String queryParams) {
		this.queryParams = queryParams;
	}

	public Boolean getFit() {
		return fit;
	}

	public void setFit(Boolean fit) {
		this.fit = fit;
	}

	public Boolean getDoSize() {
		return doSize;
	}

	public void setDoSize(Boolean doSize) {
		this.doSize = doSize;
	}

	public Boolean getNoheader() {
		return noheader;
	}

	public void setNoheader(Boolean noheader) {
		this.noheader = noheader;
	}

}
