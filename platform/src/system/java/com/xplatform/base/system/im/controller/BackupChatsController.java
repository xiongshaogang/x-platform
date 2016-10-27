package com.xplatform.base.system.im.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.system.im.entity.BackupChatsEntity;
import com.xplatform.base.system.im.service.BackupChatsService;

@Controller
@RequestMapping("/backupChatsController")
public class BackupChatsController {

@Resource
BackupChatsService backupChatsService;

/**
 * 跳转系统类型管理界面
 * @author lxt
 * @param request
 * @return
 */
@RequestMapping(params = "backupChats")
public ModelAndView type(HttpServletRequest request) {
	return new ModelAndView("platform/system/im/backupChatsList");
}


/**
 * 单表hibernate组装表格数据
 * @author lxt
 * @param backupChats
 * @param request
 * @param response
 * @param dataGrid
 */
@RequestMapping(params = "datagrid")
public void datagrid(BackupChatsEntity backupChats, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid){
	CriteriaQuery cq = new CriteriaQuery(BackupChatsEntity.class, dataGrid);
	
	HqlGenerateUtil.installHql(cq, backupChats, request.getParameterMap());
	cq.add();
	this.backupChatsService.getDataGridReturn(cq, true);
	TagUtil.datagrid(response, dataGrid);
	
}

	
}
