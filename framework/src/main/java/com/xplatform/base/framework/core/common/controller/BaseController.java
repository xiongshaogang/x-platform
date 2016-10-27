package com.xplatform.base.framework.core.common.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xplatform.base.framework.core.cache.UcgCache;
import com.xplatform.base.framework.core.cache.manager.UcgCacheManager;
import com.xplatform.base.framework.core.common.service.CommonService;
import com.xplatform.base.framework.core.interceptors.DateConvertEditor;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.ServletUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.poi.excel.entity.result.ExcelImportResult;

/**
 * 基础控制器，其他控制器需集成此控制器获得initBinder自动转换的功能
 * @author  张代浩
 * 
 */
@Controller
@RequestMapping("/baseController")
public class BaseController {
	@Resource
	private UcgCacheManager ucgCacheManager;

	public int PAGESIZE = 10;

	public <T> Page<T> buildFilter(Page<T> page, HttpServletRequest request) {

		if (page == null) {
			return null;
		}
		if (StringUtil.isNotEmpty(request.getParameter("page"))) {
			page.setPageNo(Integer.parseInt(request.getParameter("page")));
		}
		if (StringUtil.isNotEmpty(request.getParameter("rows"))) {
			page.setPageSize(Integer.parseInt(request.getParameter("rows")));
		}
		page.setParameter(ServletUtil.getFilterMap(request));
		return page;
	}

	/**
	 * 初始化分页排序条件(单字段排序)
	 * 
	 * @param page 分页对象
	 * @return 分页对象
	 */
	public <T> Page<T> buildOrder(Page<T> page, HttpServletRequest request, String defaultOrderBy, String defaultOrder) {

		if (page == null) {
			return null;
		}

		Sort sort = this.buildOrder(request);

		if (sort != null) {
			page.setOrder(sort.getDirection());
			//属性转字段
			//page.setOrderBy(StringUtil.toColumnName(sort.getProperty()));
			page.setOrderBy(sort.getProperty());
		}
		//设置默认排序方式
		if (!page.isOrderBySetted()) {
			//page.setOrderBy(StringUtil.toColumnName(defaultOrderBy));
			page.setOrderBy(defaultOrderBy);
			page.setOrder(defaultOrder);
		}
		return page;
	}

	/**
	 * 初始化排序条件(单字段排序)
	 * 
	 * @return 排序对象
	 */
	public Sort buildOrder(HttpServletRequest request) {
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		Sort sortEntry = null;
		if(StringUtil.isNotEmpty(sort)){
			sortEntry=new Sort();
			sortEntry.setProperty(sort);
			sortEntry.setDirection(order);
		}
		/*if (sortJson != null && sortJson.length() > 2) {
			sortJson = StringUtil.remove(sortJson, "[");
			sortJson = StringUtil.remove(sortJson, "]");
			sort = JSONHelper.fromJsonToObject(sortJson, Sort.class);
		}*/
		return sortEntry;
	}

	@SuppressWarnings("serial")
	public class Sort implements Serializable {

		private String property;
		private String direction;

		public String getDirection() {
			return direction;
		}

		public void setDirection(String direction) {
			this.direction = direction;
		}

		public String getProperty() {
			return property;
		}

		public void setProperty(String property) {
			this.property = property;
		}
	}

	/**
	 * 将前台传递过来的日期格式的字符串，自动转化为Date类型
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		//		SimpleDateFormat dateFormat = new SimpleDateFormat(
		//				"yyyy-MM-dd hh:mm:ss");
		//		binder.registerCustomEditor(Date.class, new CustomDateEditor(
		//				dateFormat, true));
		binder.registerCustomEditor(Date.class, new DateConvertEditor());
	}

	/**
	 * 分页公共方法(非easyui)
	 * 
	 * @author Alexander
	 * @date 20131022
	 */
	public List<?> pageBaseMethod(HttpServletRequest request, DetachedCriteria dc, CommonService commonService,
			int pageRow) {
		// 当前页
		// 总条数
		// 总页数

		int currentPage = 1;

		int totalRow = 0;
		int totalPage = 0;
		// 获取当前页
		String str_currentPage = request.getParameter("str_currentPage");
		currentPage = str_currentPage == null || "".equals(str_currentPage) ? 1 : Integer.parseInt(str_currentPage);
		// 获取每页的条数
		String str_pageRow = request.getParameter("str_pageRow");
		pageRow = str_pageRow == null || "".equals(str_pageRow) ? pageRow : Integer.parseInt(str_pageRow);

		// 统计的总行数
		dc.setProjection(Projections.rowCount());

		totalRow = Integer.parseInt(commonService.findByDetached(dc).get(0).toString());
		totalPage = (totalRow + pageRow - 1) / pageRow;

		currentPage = currentPage < 1 ? 1 : currentPage;
		currentPage = currentPage > totalPage ? totalPage : currentPage;
		// 清空统计函数
		dc.setProjection(null);
		// dc.setResultTransformer(dc.DISTINCT_ROOT_ENTITY);
		List<?> list = commonService.pageList(dc, (currentPage - 1) * pageRow, pageRow);

		request.setAttribute("currentPage", currentPage);
		request.setAttribute("pageRow", pageRow);
		request.setAttribute("totalRow", totalRow);
		request.setAttribute("totalPage", totalPage);
		return list;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年11月24日 下午6:02:45
	 * @Decription 设置excel导出data到缓存中
	 *
	 * @param request
	 * @param list
	 * @param sessionId
	 */
	public void setExcelExportData(HttpServletRequest request, List list, String sessionId) {
		String name = request.getParameter("gridName");
		//获得导出缓存操作对象
		UcgCache ucgCache = ucgCacheManager.getExportCacheBean();
		ucgCache.put(name + "_excel_export_" + sessionId, list);
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年11月24日 下午6:02:45
	 * @Decription 获取缓存中的导入结果对象
	 *
	 * @param request
	 * @param list
	 * @param sessionId
	 */
	public ExcelImportResult getExcelImportData(HttpServletRequest request, String sessionId) {
		String name = request.getParameter("name");
		//获得导出缓存操作对象
		UcgCache ucgCache = ucgCacheManager.getExportCacheBean();
		return (ExcelImportResult)ucgCache.get(name + "_excel_import_" + sessionId);
	}
	
	/**
	 * @author xiaqiang
	 * @createtime 2014年12月1日 下午10:48:00
	 * @Decription 设置缓存中的导入结果对象
	 *
	 * @param request
	 * @param result
	 * @param sessionId
	 */
	public void setExcelImportData(HttpServletRequest request, ExcelImportResult result, String sessionId) {
		String name = request.getParameter("name");
		//设置导出缓存操作对象
		UcgCache ucgCache = ucgCacheManager.getExportCacheBean();
		ucgCache.put(name + "_excel_import_" + sessionId, result);
	}
}
