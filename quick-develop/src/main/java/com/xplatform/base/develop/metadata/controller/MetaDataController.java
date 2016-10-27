package com.xplatform.base.develop.metadata.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.develop.codegenerate.database.UcgReadTable;
import com.xplatform.base.develop.codegenerate.pojo.Columnt;
import com.xplatform.base.develop.codegenerate.pojo.DataBaseConst;
import com.xplatform.base.develop.metadata.entity.MetaDataEntity;
import com.xplatform.base.develop.metadata.entity.MetaDataFieldEntity;
import com.xplatform.base.develop.metadata.service.MetaDataFieldService;
import com.xplatform.base.develop.metadata.service.MetaDataService;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.xml.CollectionUtils;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;

/**
 * 
 * @author sony
 * 数据源管理
 */
@Controller
@RequestMapping("/metaDataController")
public class MetaDataController {
	public static final Logger logger = Logger.getLogger(MetaDataController.class);
	@Resource
	private MetaDataService metaDataService;
	@Resource
	private MetaDataFieldService metaDataFieldService;

	AjaxJson j = new AjaxJson();

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 自动生成表属性列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "metaData")
	public ModelAndView metaData(HttpServletRequest request) {
		return new ModelAndView("develop/metadata/metaDataList");
	}

	/**
	 * easyui AJAX请求数据 主表信息
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(MetaDataEntity FormHead, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(MetaDataEntity.class, dataGrid);
		// 查询条件组装器
		HqlGenerateUtil.installHql(cq, FormHead, request.getParameterMap());
		try {
			// 自定义追加查询条件
		} catch (Exception e) {
		}
		cq.add();
		this.metaDataService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 新增或修改
	 * 
	 * @return
	 */
	@RequestMapping(params = "metaDataEdit")
	public ModelAndView metaDataEdit(HttpServletRequest request) {
		String id = request.getParameter("id");
		MetaDataEntity metaData = null;
		if (StringUtil.isNotEmpty(id)) {
			metaData = metaDataService.getEntity(MetaDataEntity.class, id);
		} else {
			metaData = new MetaDataEntity();
			metaData.setJformVersion(1);
		}
		request.setAttribute("metaData", metaData);
		return new ModelAndView("develop/metadata/metaDataEdit");
	}

	/**
	 * 保存源数据表信息
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "saveOrUpdata")
	@ResponseBody
	public AjaxJson saveOrUpdata(MetaDataEntity formHead, HttpServletRequest request) throws BusinessException {
		message = "数据保存成功";
		String insertedRows = request.getParameter("insertedRows");
		String updatedRows = request.getParameter("updatedRows");
		String deletedRows = request.getParameter("deletedRows");
		Integer jformVersion = 0;
		try {
			if (StringUtil.isNotEmpty(formHead.getId())) {
				// 编辑页面
				MetaDataEntity t = metaDataService.getEntity(MetaDataEntity.class, formHead.getId());
				MyBeanUtils.copyBeanNotNull2Bean(formHead, t);
				jformVersion = t.getJformVersion() + 1;
				t.setJformVersion(jformVersion);
				metaDataService.update(t);
			} else {
				// 新增页面
				jformVersion = 1;
				formHead.setJformVersion(jformVersion);
				formHead.setIsDbsynch("N");
				metaDataService.save(formHead);
				saveDefaultColumns(formHead);
			}
		} catch (Exception e) {
			message = "主表数据保存失败";
			throw new BusinessException(message);
		}
		List<Map<String, Object>> insertList = JSONHelper.toList(insertedRows);
		List<Map<String, Object>> updateList = JSONHelper.toList(updatedRows);
		List<Map<String, Object>> deleteList = JSONHelper.toList(deletedRows);

		for (int i = 0; i < insertList.size(); i++) {
			Map<String, Object> map = insertList.get(i);
			// 除了这七个字段外的数据执行插入操作
			if (!isUnOperateField(map.get("fieldName").toString())) {
				MetaDataFieldEntity field = new MetaDataFieldEntity();
				field.setTable(formHead);
				field.setFieldName(StringUtil.isEmpty(map.get("fieldName").toString(), ""));
				field.setContent(StringUtil.isEmpty(map.get("content").toString(), ""));
				field.setLength(StringUtil.isEmpty(map.get("length").toString()) ? 0 : Integer.valueOf(map.get("length").toString()));
				field.setPointLength(StringUtil.isEmpty(map.get("pointLength").toString()) ? 0 : Integer.valueOf(map.get("pointLength").toString()));
				field.setFieldDefault(StringUtil.isEmpty(map.get("fieldDefault").toString(), ""));
				field.setType(StringUtil.isEmpty(map.get("type").toString(), ""));
				field.setIsKey("N");
				field.setIsNull(StringUtil.isEmpty(map.get("isNull").toString(), ""));
				try {
					metaDataFieldService.save(field);
				} catch (Exception e) {
					message = map.get("fieldName").toString() + "字段保存失败";
					throw new BusinessException(message);
				}
			}
		}
		for (int i = 0; i < updateList.size(); i++) {
			Map<String, Object> map = updateList.get(i);
			// 默认字段不能更改
			if (!isUnOperateField(map.get("fieldName").toString())) {
				MetaDataFieldEntity field = metaDataFieldService.get(map.get("id").toString());
				field.setTable(formHead);
				field.setFieldName(StringUtil.isEmpty(map.get("fieldName").toString(), ""));
				field.setContent(StringUtil.isEmpty(map.get("content").toString(), ""));
				field.setLength(StringUtil.isEmpty(map.get("length").toString()) ? 0 : Integer.valueOf(map.get("length").toString()));
				field.setPointLength(StringUtil.isEmpty(map.get("pointLength").toString()) ? 0 : Integer.valueOf(map.get("pointLength").toString()));
				field.setFieldDefault(StringUtil.isEmpty(map.get("fieldDefault").toString(), ""));
				field.setType(StringUtil.isEmpty(map.get("type").toString(), ""));
				field.setIsKey("N");
				field.setIsNull(StringUtil.isEmpty(map.get("isNull").toString(), ""));
				try {
					metaDataFieldService.update(field);
				} catch (Exception e) {
					message = map.get("fieldName").toString() + "字段更新失败";
					throw new BusinessException(message);
				}
			}
		}

		for (int i = 0; i < deleteList.size(); i++) {
			Map<String, Object> map = deleteList.get(i);
			// 默认字段不能删除
			if (!isUnOperateField(map.get("fieldName").toString())) {
				try {
					metaDataFieldService.delete(map.get("id").toString());
				} catch (Exception e) {
					message = map.get("fieldName").toString() + "字段删除失败";
					throw new BusinessException(message);
				}
			}
		}
		j.setMsg(message);
		Map map = CollectionUtils.map("jformVersion", jformVersion);
		map.put("tableId", formHead.getId());
		j.setObj(map);
		return j;
	}

	/**
	 * 判断是否是不需要维护的字段
	 * 
	 * @author xiaqiang
	 * @createtime 2015年5月4日 下午3:54:49
	 *
	 * @param fieldName
	 * @return
	 */
	private Boolean isUnOperateField(String fieldName) {
		fieldName = fieldName.toLowerCase();
		return StringUtils.equals(fieldName, "id") || StringUtils.equals(fieldName, "createUserId") || StringUtils.equals(fieldName, "createUserName")
				|| StringUtils.equals(fieldName, "createTime") || StringUtils.equals(fieldName, "updateUserId")
				|| StringUtils.equals(fieldName, "updateUserName") || StringUtils.equals(fieldName, "updateTime");
	}

	/**
	 * 增加默认的列
	 * 
	 * @param FormHead
	 * @throws Exception
	 */
	public void saveDefaultColumns(MetaDataEntity FormHead) throws Exception {
		MetaDataFieldEntity FormField = new MetaDataFieldEntity();
		FormField.setTable(FormHead);
		FormField.setFieldName("id");
		FormField.setContent("主键");
		FormField.setLength(32);
		FormField.setPointLength(0);
		FormField.setType("string");
		FormField.setIsKey("Y");
		FormField.setIsNull("N");
		metaDataFieldService.save(FormField);
		FormField = new MetaDataFieldEntity();
		FormField.setTable(FormHead);
		FormField.setFieldName("createUserId");
		FormField.setContent("创建人ID");
		FormField.setLength(32);
		FormField.setPointLength(0);
		FormField.setType("string");
		FormField.setIsKey("N");
		FormField.setIsNull("Y");
		metaDataFieldService.save(FormField);
		FormField = new MetaDataFieldEntity();
		FormField.setTable(FormHead);
		FormField.setFieldName("createUserName");
		FormField.setContent("创建人name");
		FormField.setLength(100);
		FormField.setPointLength(0);
		FormField.setType("string");
		FormField.setIsKey("N");
		FormField.setIsNull("Y");
		metaDataFieldService.save(FormField);
		FormField = new MetaDataFieldEntity();
		FormField.setTable(FormHead);
		FormField.setFieldName("createTime");
		FormField.setContent("创建时间");
		FormField.setLength(32);
		FormField.setPointLength(0);
		FormField.setType("Date");
		FormField.setIsKey("N");
		FormField.setIsNull("Y");
		metaDataFieldService.save(FormField);
		FormField = new MetaDataFieldEntity();
		FormField.setTable(FormHead);
		FormField.setFieldName("updateUserId");
		FormField.setContent("更新人ID");
		FormField.setLength(32);
		FormField.setPointLength(0);
		FormField.setType("string");
		FormField.setIsKey("N");
		FormField.setIsNull("Y");
		metaDataFieldService.save(FormField);
		FormField = new MetaDataFieldEntity();
		FormField.setTable(FormHead);
		FormField.setFieldName("updateUserName");
		FormField.setContent("更新人name");
		FormField.setLength(100);
		FormField.setPointLength(0);
		FormField.setType("string");
		FormField.setIsKey("N");
		FormField.setIsNull("Y");
		metaDataFieldService.save(FormField);
		FormField = new MetaDataFieldEntity();
		FormField.setTable(FormHead);
		FormField.setFieldName("updateTime");
		FormField.setContent("更新时间");
		FormField.setLength(32);
		FormField.setPointLength(0);
		FormField.setType("Date");
		FormField.setIsKey("N");
		FormField.setIsNull("Y");
		metaDataFieldService.save(FormField);
	}

	/**
	 * 同步表单配置到数据库
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doDbSynch")
	@ResponseBody
	public AjaxJson doDbSynch(MetaDataEntity cgFormHead, String synMethod, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		cgFormHead = metaDataService.getEntity(MetaDataEntity.class, cgFormHead.getId());
		// 同步数据库
		try {
			boolean bl = metaDataService.dbSynch(cgFormHead, synMethod);
			if (bl) {
				// 追加主表的附表串
				metaDataService.appendSubTableStr4Main(cgFormHead);
				message = "同步成功";
				j.setMsg(message);
			} else {
				message = "同步失败";
				j.setMsg(message);
				return j;
			}
		} catch (BusinessException e) {
			j.setMsg(e.getMessage());
			return j;
		}
		return j;
	}

	/**
	 * 生成表类型的选择
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "metaDataSynChoice")
	public ModelAndView metaDataSynChoice(HttpServletRequest request) {
		return new ModelAndView("develop/metadata/metaDataSynChoice");
	}

	/**
	 * 自动生成表属性列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "trans")
	public ModelAndView trans(HttpServletRequest request) {
		return new ModelAndView("develop/metadata/transList");
	}

	@RequestMapping(params = "transEditor")
	@ResponseBody
	public AjaxJson transEditor(HttpServletRequest request, String id) throws Exception {
		id = request.getParameter("id");
		AjaxJson j = new AjaxJson();
		String ids[] = id.split(",");
		String no = "";
		String yes = "";
		for (int i = 0; i < ids.length; i++) {
			if (StringUtil.isNotEmpty(ids[i])) {
				List<MetaDataEntity> cffList = metaDataService.findByProperty(MetaDataEntity.class, "tableName", ids[i]);
				if (cffList.size() > 0) {
					if (no != "")
						no += ",";
					no += ids[i];
					continue;
				}
				List<Columnt> list = new UcgReadTable().readOriginalTableColumn(ids[i]);
				MetaDataEntity cgFormHead = new MetaDataEntity();
				cgFormHead.setJformType("tableType_1");
				cgFormHead.setIsDbsynch("Y");
				cgFormHead.setTableName(ids[i].toLowerCase());
				cgFormHead.setContent(ids[i]);
				cgFormHead.setJformVersion(1);
				List<MetaDataFieldEntity> columnsList = new ArrayList<MetaDataFieldEntity>();
				for (int k = 0; k < list.size(); k++) {
					Columnt columnt = list.get(k);
					String fieldName = columnt.getFieldDbName();
					MetaDataFieldEntity cgFormField = new MetaDataFieldEntity();
					cgFormField.setFieldName(columnt.getFieldDbName().toLowerCase());
					if (StringUtil.isNotEmpty(columnt.getFiledComment()))
						cgFormField.setContent(columnt.getFiledComment());
					else
						cgFormField.setContent(columnt.getFieldName());
					cgFormField.setIsKey("N");
					cgFormField.setOrderNum(k + 2);
					cgFormField.setLength(0);
					cgFormField.setPointLength(0);
					cgFormField.setIsNull(columnt.getNullable());
					if ("id".equalsIgnoreCase(fieldName)) {
						String[] pkTypeArr = { "java.lang.Integer", "java.lang.Long" };
						String idFiledType = columnt.getFieldType();
						if (Arrays.asList(pkTypeArr).contains(idFiledType)) {
							// 如果主键是数字类型,则设置为自增长
							cgFormHead.setJformPkType("NATIVE");
						} else {
							// 否则设置为UUID
							cgFormHead.setJformPkType("UUID");
						}
						cgFormField.setIsKey("Y");
					}
					if ("java.lang.Integer".equalsIgnoreCase(columnt.getFieldType())) {
						cgFormField.setType(DataBaseConst.INT);
					} else if ("java.lang.Long".equalsIgnoreCase(columnt.getFieldType())) {
						cgFormField.setType(DataBaseConst.INT);
					} else if ("java.util.Date".equalsIgnoreCase(columnt.getFieldType())) {
						cgFormField.setType(DataBaseConst.DATE);
						// cgFormField.setShowType("date");
					} else if ("java.lang.Double".equalsIgnoreCase(columnt.getFieldType()) || "java.lang.Float".equalsIgnoreCase(columnt.getFieldType())) {
						cgFormField.setType(DataBaseConst.DOUBLE);
					} else if ("java.math.BigDecimal".equalsIgnoreCase(columnt.getFieldType())) {
						cgFormField.setType(DataBaseConst.BIGDECIMAL);
					} else if (columnt.getFieldType().contains("blob")) {
						cgFormField.setType(DataBaseConst.BLOB);
						columnt.setCharmaxLength(null);
					} else {
						cgFormField.setType(DataBaseConst.STRING);
					}
					if (StringUtil.isNotEmpty(columnt.getCharmaxLength())) {
						if (Long.valueOf(columnt.getCharmaxLength()) >= 3000) {
							cgFormField.setType(DataBaseConst.TEXT);
							// cgFormField.setShowType(DataBaseConst.TEXTAREA);
							try {// 有可能长度超出int的长度
								cgFormField.setLength(Integer.valueOf(columnt.getCharmaxLength()));
							} catch (Exception e) {
							}
						} else {
							cgFormField.setLength(Integer.valueOf(columnt.getCharmaxLength()));
						}
					} else {
						if (StringUtil.isNotEmpty(columnt.getPrecision())) {
							cgFormField.setLength(Integer.valueOf(columnt.getPrecision()));
						}
						// update-begin--Author:zhangdaihao Date:20140212
						// for：[001]oracle下number类型，数据库表导出表单，默认长度为0同步失败
						else {
							if (cgFormField.getType().equals(DataBaseConst.INT)) {
								cgFormField.setLength(10);
							}
						}
						// update-end--Author:zhangdaihao Date:20140212
						// for：[001]oracle下number类型，数据库表导出表单，默认长度为0同步失败
						if (StringUtil.isNotEmpty(columnt.getScale()))
							cgFormField.setPointLength(Integer.valueOf(columnt.getScale()));

					}
					columnsList.add(cgFormField);
				}
				cgFormHead.setColumns(columnsList);
				metaDataService.saveTable(cgFormHead, "");
				if (yes != "")
					yes += ",";
				yes += ids[i];
			}
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("no", no);
		map.put("yes", yes);
		j.setObj(map);
		return j;
	}

	/**
	 * 判断表格是够已经创建
	 * 
	 * @return AjaxJson 中的success
	 */
	@RequestMapping(params = "checkIsExit")
	@ResponseBody
	public AjaxJson checkIsExit(String name, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		j.setSuccess(metaDataService.judgeTableIsExit(name));
		return j;
	}

	/**
	 * 删除表与表属性,并删除物理表
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(MetaDataEntity cgFormHead, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		cgFormHead = metaDataService.getEntity(MetaDataEntity.class, cgFormHead.getId());
		message = "删除成功";
		metaDataService.deleteMetaData(cgFormHead);
		metaDataService.removeSubTableStr4Main(cgFormHead);

		j.setMsg(message);
		return j;
	}

	/**
	 * 仅移除表与表属性,不删除物理表
	 * 
	 * @return
	 */
	@RequestMapping(params = "rem")
	@ResponseBody
	public AjaxJson rem(MetaDataEntity cgFormHead, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		cgFormHead = metaDataService.getEntity(MetaDataEntity.class, cgFormHead.getId());
		message = "移除成功";
		metaDataService.delete(cgFormHead);
		metaDataService.removeSubTableStr4Main(cgFormHead);

		j.setMsg(message);
		return j;
	}

	/**
	 * 取得该表所有记录
	 */
	@RequestMapping(params = "metaDataEntityList")
	@ResponseBody
	public List<MetaDataEntity> metaDataEntityList(HttpServletRequest request) {
		List<MetaDataEntity> list = metaDataService.getMetaDataList();
		return list;

	}

}
