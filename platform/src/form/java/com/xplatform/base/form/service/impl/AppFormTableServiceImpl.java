package com.xplatform.base.form.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.form.dao.AppFormTableDao;
import com.xplatform.base.form.entity.AppFormField;
import com.xplatform.base.form.entity.AppFormTable;
import com.xplatform.base.form.entity.FlowFormEntity;
import com.xplatform.base.form.service.AppFormFieldService;
import com.xplatform.base.form.service.AppFormTableService;
import com.xplatform.base.form.service.FlowFormService;
import com.xplatform.base.form.service.util.DbTableProcess;
import com.xplatform.base.form.service.util.FormGenerateUtils;
import com.xplatform.base.form.service.util.QueryParamUtil;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.service.CommonService;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.DBTypeUtil;
import com.xplatform.base.framework.core.util.DateUtils;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.MapKit;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.def.ConfigConst;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.system.message.config.entity.MessageReceiveEntity;
import com.xplatform.base.system.message.config.service.MessageService;

@Service("appFormTableService")
public class AppFormTableServiceImpl extends BaseServiceImpl<AppFormTable> implements AppFormTableService {

	@Resource
	private AppFormTableDao appFormTableDao;
	@Resource
	private FlowFormService flowFormService;
	@Resource
	private AppFormFieldService appFormFieldService;
	@Resource
	private AppFormTableService appFormTableService;
	@Resource
	private CommonService commonService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private MessageService messageService;

	@Resource
	public void setBaseDao(AppFormTableDao appFormTableDao) {
		super.setBaseDao(appFormTableDao);
	}

	@Override
	public void generatePhysicalTable(AppFormTable table, Integer tableType) throws Exception {
		// 获得表的所有字段
		List<AppFormField> formFieldList = this.appFormFieldService.queryAppFormField(table.getId());
		// 增加固定字段
		List<AppFormField> list = new ArrayList<AppFormField>();
		AppFormField field1 = new AppFormField();
		field1.setCode(BusinessConst.primaryKey);
		field1.setType("String");
		field1.setName("主键");
		field1.setLength(32);
		list.add(field1);
		AppFormField field2 = new AppFormField();
		field2.setCode(BusinessConst.createTime);
		field2.setType("Date");
		field2.setName("创建时间");
		field2.setNotNull(0);
		list.add(field2);
		AppFormField field3 = new AppFormField();
		field3.setCode(BusinessConst.createUserId);
		field3.setType("String");
		field3.setName("创建人Id");
		field3.setNotNull(0);
		field3.setLength(100);
		list.add(field3);
		AppFormField field4 = new AppFormField();
		field4.setCode(BusinessConst.createUserName);
		field4.setType("String");
		field4.setName("创建人name");
		field4.setNotNull(0);
		field4.setLength(100);
		list.add(field4);
		AppFormField field5 = new AppFormField();
		field5.setCode(BusinessConst.updateTime);
		field5.setType("Date");
		field5.setName("更新时间");
		field5.setNotNull(0);
		list.add(field5);
		AppFormField field6 = new AppFormField();
		field6.setCode(BusinessConst.updateUserId);
		field6.setType("String");
		field6.setName("更新人Id");
		field6.setNotNull(0);
		field6.setLength(100);
		list.add(field6);
		AppFormField field7 = new AppFormField();
		field7.setCode(BusinessConst.updateUserName);
		field7.setType("String");
		field7.setName("更新人name");
		field7.setNotNull(0);
		field7.setLength(100);
		list.add(field7);
		// 如果是从表,要额外增加外键字段
		if (BusinessConst.TableType_sub.equals(tableType)) {
			AppFormField field8 = new AppFormField();
			field8.setCode(BusinessConst.foreignKey);
			field8.setType("String");
			field8.setName("主表外键");
			field8.setNotNull(0);
			field8.setLength(32);
			list.add(field8);
		}
		
		// 特殊处理的字段
		Iterator<AppFormField> it=formFieldList.iterator();
		List<AppFormField> addAppFormFields=new ArrayList<AppFormField>();
		Boolean hasRemove=false;
		while (it.hasNext()) {
			hasRemove=false;
			AppFormField field = it.next();
			if (field.getIsDB().equals(0)) {
				// 说明不生成数据库字段
				it.remove();
				hasRemove=true;
			}
			//位置控件
			if ("position".equals(field.getcType())) {
				// 再添加全地址和经纬度字段
				AppFormField mapAddressField = new AppFormField();
				mapAddressField.setCode(field.getCode() + BusinessConst.mapaddress);// 控件code+固定名称作为物理表字段名
				mapAddressField.setType("String");
				mapAddressField.setName("位置名称");
				mapAddressField.setNotNull(0);
				mapAddressField.setLength(300);
				addAppFormFields.add(mapAddressField);
				AppFormField lonAndLatField = new AppFormField();
				lonAndLatField.setCode(field.getCode() + BusinessConst.lonandlat);
				lonAndLatField.setType("String");
				lonAndLatField.setName("位置经纬度");
				lonAndLatField.setNotNull(0);
				lonAndLatField.setLength(100);
				addAppFormFields.add(lonAndLatField);
				// 移除掉之前的字段
				if (!hasRemove) {
					it.remove();
				}
			}
		}
		list.addAll(formFieldList);
		list.addAll(addAppFormFields);
		DbTableProcess.createOrDropTable(table, list, getSession());
	}

	@Override
	public void updatePhysicalTable(AppFormTable table) throws Exception {
		List<AppFormField> formFieldList = this.appFormFieldService.queryAppFormField(table.getId());
		
		Iterator<AppFormField> it=formFieldList.iterator();
		List<AppFormField> addAppFormFields=new ArrayList<AppFormField>();
		while (it.hasNext()) {
			AppFormField field = it.next();
			if (field.getIsDB().equals(0)) {
				// 说明不生成数据库字段
				it.remove();
			}
			if ("position".equals(field.getcType())) {
				// 再添加全地址和经纬度字段
				AppFormField mapAddressField = new AppFormField();
				mapAddressField.setCode(field.getCode() + BusinessConst.mapaddress);// 控件code+固定名称作为物理表字段名
				mapAddressField.setType("String");
				mapAddressField.setName("位置名称");
				mapAddressField.setNotNull(0);
				mapAddressField.setLength(300);
				addAppFormFields.add(mapAddressField);
				AppFormField lonAndLatField = new AppFormField();
				lonAndLatField.setCode(field.getCode() + BusinessConst.lonandlat);
				lonAndLatField.setType("String");
				lonAndLatField.setName("位置经纬度");
				lonAndLatField.setNotNull(0);
				lonAndLatField.setLength(100);
				addAppFormFields.add(lonAndLatField);
				// 移除掉之前的字段
				it.remove();
			}
		}
		formFieldList.addAll(addAppFormFields);
		List<String> updateTable = DbTableProcess.updateTable(table, formFieldList, getSession());
		for (String sql : updateTable) {
			if (StringUtil.isNotEmpty(sql)) {
				this.executeSql(sql);
			}
		}
	}

	/**
	 * 通过tableName表名查找table记录
	 * 
	 * @param tableName
	 * @return
	 */
	@Override
	public AppFormTable queryAppFormTable(String tableName) throws BusinessException {
		return this.findUniqueByProperty("tableName", tableName);
	}

	@Override
	public Object getPkValue(String tableName) throws BusinessException {
		Object pkValue = null;
		AppFormTable table = this.queryAppFormTable(tableName);
		String dbType = DBTypeUtil.getDBType();
		// TODO 这里暂不考虑Oracle等其他数据库以及Native等主键类型
		pkValue = UUIDGenerator.generate();
		return pkValue;
	}

	/**
	 * 插入操作时将系统变量给字段赋值
	 * 
	 * @param data
	 */
	private void fillInsertSysVar(Map<String, Object> data) {
		data.put(BusinessConst.createTime, new Date());
		data.put(BusinessConst.createUserId, ClientUtil.getUserId());
		data.put(BusinessConst.createUserName, ClientUtil.getName());
	}

	/**
	 * 更新操作时将系统变量给字段赋值
	 * 
	 * @param data
	 */
	private void fillUpdateSysVar(Map<String, Object> data) {
		data.put(BusinessConst.updateTime, new Date());
		data.put(BusinessConst.updateUserId, ClientUtil.getUserId());
		data.put(BusinessConst.updateUserName, ClientUtil.getName());
	}

	/**
	 * 数据类型适配-根据表单配置的字段类型将前台传递的值将map-value转换成相应的类型
	 * 
	 * @param tableName
	 *            表单名
	 * @param data
	 *            数据
	 * @throws ParseException
	 */
	private Map<String, Object> dataAdapter(Map<String, AppFormField> fieldConfigs, Map<String, Object> data) throws ParseException {
		// 2.迭代处理将要持久化的数据
		Iterator it = fieldConfigs.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			// 根据表单配置的字段名 获取 前台数据
			Object beforeV = data.get(key.toString().toLowerCase());
			// 获取字段配置-字段类型
			AppFormField fieldConfig = fieldConfigs.get(key);
			if (fieldConfig != null && BeanUtils.isNotEmpty(beforeV)) {
				String type = fieldConfig.getType();
				Integer dateType = fieldConfig.getDateType();
				// 根据类型进行值的适配
				if ("Date".equalsIgnoreCase(type)) {
					// 日期->java.util.Date
					if (data.containsKey(key)) {
						Object newV = null;
						String format = "";
						if (dateType.equals(0)) {
							format = "yyyy-MM-dd";
						} else if (dateType.equals(1)) {
							format = "yyyy-MM-dd HH:mm";
						}
						newV = new SimpleDateFormat(format).parse(String.valueOf(beforeV));
						data.put(String.valueOf(key), newV);
					}
				} else if ("Datetime".equalsIgnoreCase(type)) {
					if (data.containsKey(key)) {
						Object newV = null;
						newV = new SimpleDateFormat("HH:mm").parse(String.valueOf(beforeV));
						data.put(String.valueOf(key), newV);
					}
				} else if ("int".equalsIgnoreCase(type)) {
					if (data.containsKey(key)) {
						Object newV = new Integer(0);
						newV = Integer.parseInt(String.valueOf(beforeV));
						data.put(String.valueOf(key), newV);
					}
				} else if ("BigDecimal".equalsIgnoreCase(type)) {
					if (data.containsKey(key)) {
						Object newV = new Double(0);
						newV = new BigDecimal(String.valueOf(beforeV));
						data.put(String.valueOf(key), newV);
					}
				}
			}
		}
		return data;
	}

	@Override
	public Object insertTable(String tableName, Map<String, Object> data) throws Exception {
		// 得到本表需要维护的字段
		Map<String, AppFormField> fieldConfigs = appFormFieldService.queryAppFormFieldMap(tableName);
		// 外键并不去除
		fieldConfigs.put(BusinessConst.foreignKey, null);
		// 先取出主键值
		Object pkValue = null;
		// pkValue = getPkValue(tableName);
		pkValue = data.get("id");
		// 去除多余属性,只留表字段
		data = FormGenerateUtils.attributeMapFilter(data, fieldConfigs.keySet().toArray());
		// 填充系统默认值,如创建人、时间等
		fillInsertSysVar(data);

		data.put("id", pkValue);
		// 对date,int,double的值进行转换处理
		dataAdapter(fieldConfigs, data);
		String comma = "";
		StringBuffer insertKey = new StringBuffer();
		StringBuffer insertValue = new StringBuffer();

		for (Entry<String, Object> entry : data.entrySet()) {
			// 判断key是否为表配置的属性
			// 插入SQL语法,兼容多数据库,去掉单引号
			insertKey.append(comma + entry.getKey());
			if (entry.getValue() != null && entry.getValue().toString().length() > 0) {
				insertValue.append(comma + ":" + entry.getKey());
			} else {
				insertValue.append(comma + "null");
			}
			comma = ",";
		}
		String sql = "INSERT INTO " + tableName + " (" + insertKey + ") VALUES (" + insertValue + ")";
		this.executeSqlReturnKey(sql, data);
		// TODO sql扩展
		return pkValue;
	}

	@Override
	public Object insertTableMore(String mainTableName, Map<String, List<Map<String, Object>>> mapMore) throws Exception {
		// 插入主表信息
		Map<String, Object> mainMap = mapMore.get(mainTableName).get(0);
		Object mainId = insertTable(mainTableName, mainMap);
		// 去除掉主表信息
		mapMore = FormGenerateUtils.attributeMapFilter(mapMore, new String[] { mainTableName });
		// 插入附表信息
		Iterator it = mapMore.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String ok = (String) entry.getKey();
			List<Map<String, Object>> ov = (List<Map<String, Object>>) entry.getValue();
			for (Map<String, Object> fieldMap : ov) {
				// TODO 这里默认从表都是相同的主表外键名名
				fieldMap.put(BusinessConst.foreignKey, mainId);
				insertTable(ok, fieldMap);
			}
		}
		return mainId;
	}

	@Override
	public boolean updateTable(String tableName, Object id, Map<String, Object> data) throws Exception {
		Map<String, AppFormField> fieldConfigs = appFormFieldService.queryAppFormFieldMap(tableName);
		// 去除多余属性,只留表字段
		data = FormGenerateUtils.attributeMapFilter(data, fieldConfigs.keySet().toArray());
		// 去除掉id,防止更新id
		data.remove("id");
		// 填充系统默认值,如创建人、时间等
		fillUpdateSysVar(data);
		// 对date,int,double的值进行转换处理
		dataAdapter(fieldConfigs, data);
		String comma = "";
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("update ").append(tableName).append(" set ");
		for (Entry<String, Object> entry : data.entrySet()) {
			// 判断key是否为表配置的属性
			if (entry.getValue() != null && entry.getValue().toString().length() > 0) {
				sqlBuffer.append(comma).append(entry.getKey()).append("=:" + entry.getKey() + " ");
			} else {
				// TODO 更新时,页面没出现的值,是否需要设空,还是说不更新即可呢
				sqlBuffer.append(comma).append(entry.getKey()).append("=null");
			}
			comma = ", ";
		}
		sqlBuffer.append(" where id='").append(id).append("'");
		int num = this.executeSql(sqlBuffer.toString(), data);
		return num > 0 ? true : false;
	}

	@Override
	public boolean updateTableMore(String mainTableName, Map<String, List<Map<String, Object>>> mapMore) throws Exception {
		// 更新主表信息
		Map<String, Object> mainMap = mapMore.get(mainTableName).get(0);
		String mainTableId = mainMap.get("id").toString();
		// 更新Table
		updateTable(mainTableName, mainTableId, mainMap);
		// 去除掉主表信息
		mapMore = FormGenerateUtils.attributeMapFilter(mapMore, new String[] { mainTableName });
		AppFormTable mainTable = this.queryAppFormTable(mainTableName);
		List<String> subTables = StringUtil.parseString2ListByPattern(mainTable.getSubTables());
		// 更新附表信息
		Iterator it = mapMore.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String ok = (String) entry.getKey();
			List<Map<String, Object>> ov = (List<Map<String, Object>>) entry.getValue();

			// 根据主表id获取附表记录集合
			Map<Object, Map<String, Object>> subTableDateMap = getSubTableIdData(ok, mainTableId.toString());
			for (Map<String, Object> fieldMap : ov) {
				String editType = fieldMap.get("editType").toString();
				Object subId = fieldMap.get("id") == null ? "" : fieldMap.get("id");
				// TODO 这里默认从表都是相同的主表外键名名
				fieldMap.put(BusinessConst.foreignKey, mainTableId);
				if ("add".equals(editType)) {
					// 新插入的记录
					insertTable(ok, fieldMap);
				} else {
					updateTable(ok, subId, fieldMap);
					// 剔除已经更新了的数据
					if (subTableDateMap.containsKey(subId)) {
						subTableDateMap.remove(subId);
					}
				}
			}
			// 处理要清空的关联子表记录
			Iterator<String> subIterator = subTables.iterator();
			while (subIterator.hasNext()) {
				String sub = subIterator.next();
				if (ok.equals(sub)) {
					subIterator.remove();
				}
			}
			// subTableDateMap中剩余的数据就是删除的数据
			if (subTableDateMap.size() > 0) {
				Iterator itSub = subTableDateMap.entrySet().iterator();
				while (itSub.hasNext()) {
					Map.Entry entrySub = (Map.Entry) itSub.next();
					Object subId = entrySub.getKey();
					deleteSubTableDataById(subId, ok);
				}
			}
		}
		
		// subTables中剩余的数据就是要清空的关联子表数据
		for (String sub : subTables) {
			deleteSubTableDataByMainId(mainTableId, sub);
		}
		return true;
	}

	/**
	 * 根据id删除对应表记录
	 * 
	 * @param id
	 * @param tableName
	 */
	private void deleteSubTableDataById(Object id, String tableName) {
		StringBuilder sql = new StringBuilder();
		sql.append(" DELETE FROM ").append(tableName).append(" WHERE id = ? ");
		this.executeSql(sql.toString(), id);
	}

	/**
	 * 根据mainId删除对应表记录
	 * 
	 * @param id
	 * @param tableName
	 */
	private void deleteSubTableDataByMainId(Object mainId, String tableName) {
		StringBuilder sql = new StringBuilder();
		sql.append(" DELETE FROM ").append(tableName).append(" WHERE " + BusinessConst.foreignKey + " = ? ");
		this.executeSql(sql.toString(), mainId);
	}

	/**
	 * 根据主表id获取在用附表Id集合
	 * 
	 * @param fkFieldList
	 *            主表附表关联键
	 * @param mainTableName
	 *            主表名
	 * @param subTableName
	 *            附表名
	 * @param mainTableId
	 *            主表id
	 * @return
	 */
	private Map<Object, Map<String, Object>> getSubTableIdData(String subTableName, String mainTableId) {
		String sql = "SELECT id FROM " + subTableName + " WHERE mainId=?";

		List<Map<String, Object>> subTableIdDataList = this.findForJdbc(sql, mainTableId);
		Map<Object, Map<String, Object>> dataMap = new HashMap<Object, Map<String, Object>>();
		if (subTableIdDataList != null) {
			for (Map<String, Object> map : subTableIdDataList) {
				dataMap.put(map.get("id"), map);
			}
		}
		return dataMap;
	}

	@Override
	public Map<String, Object> querySingleTableData(String tableName, String id) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select * from ").append(tableName);
		if (StringUtil.isNotEmpty(id)) {
			sqlBuffer.append(" where id='").append(id).append("'");
		}
		Map<String, Object> map = this.findOneForJdbc(sqlBuffer.toString());
		return map;
	}

	@Override
	public List<Map<String, Object>> querySubTableData(String tableName, String mainId) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select * from ").append(tableName);
		sqlBuffer.append(" where ").append(BusinessConst.foreignKey).append("=? order by createTime asc");
		List<Map<String, Object>> list = this.findForJdbc(sqlBuffer.toString(), mainId);
		return list;
	}

	@Override
	public boolean deleteData(String table, Object id) throws Exception {
		try {
			AppFormTable formTable = this.queryAppFormTable(table);
			Map<String, Object> data = this.querySingleTableData(table, id.toString());
			if (data != null) {
				data = FormGenerateUtils.mapConvert(data);
			}
			// 1. 删除主表
			StringBuilder deleteSql = new StringBuilder();
			deleteSql.append("DELETE FROM " + table + " WHERE id = ?");
			if (!QueryParamUtil.sql_inj(id.toString())) {
				this.executeSql(deleteSql.toString(), id);
			}
			// step.2 判断是否有明细表,进行连带删除
			String[] subTables = formTable.getSubTables() == null ? new String[0] : formTable.getSubTables().split(",");
			for (String subTable : subTables) {
				String dsql = "DELETE FROM " + subTable + " " + "WHERE " + BusinessConst.foreignKey + " = ? ";
				this.executeSql(dsql, id);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public List<AppFormTable> getAppFormTable(String formCode) throws Exception {
		String hql = "from AppFormTable where formCode=?";
		return this.appFormTableDao.findHql(hql, formCode);
	}
	
	@Override
	public AppFormTable queryAppFormTableByFormCode(String formCode) {
		String hql = "FROM AppFormTable WHERE formCode=?";
		return this.appFormTableDao.findUniqueByHql(hql, formCode);
	}

	@Override
	public List<AppFormTable> queryAppFormTableList(String tableName) throws BusinessException {
		String hql = "from AppFormTable where mainTable=?";
		return this.appFormTableDao.findHql(hql, tableName);
	}

	@Override
	public List<AppFormTable> getIsUseAFTList(String tableName) throws BusinessException {
		String hql = "from AppFormTable where mainTable=? and isOverdue = 0";
		return this.appFormTableDao.findHql(hql, tableName);
	}

	@Override
	public Map<String, Object> getFieldData(String formCode, String userId,int page, int rows) throws Exception {
		String field = "id,createUserName,createTime";
		List<AppFormField> AFFList = this.appFormFieldService.getAFFList(formCode);
		Map<String, Object> fieldMap = new HashMap<String, Object>();
		for (AppFormField appFormField : AFFList) {
			fieldMap.put(appFormField.getCode(), appFormField.getName());
		}
		Map<String, Object> fieldData = new HashMap<String, Object>();
		List<AppFormTable> appFormTableList = this.getAppFormTable(formCode);

		String title = "";
		String mainTableId = "";
		String mainTableName = "";
		for (AppFormTable AFT : appFormTableList) {
			if (AFT.getMainTable() == null) {
				mainTableId = AFT.getId();
				mainTableName = AFT.getTableName();
				List<AppFormField> affList = this.appFormFieldService.getAFFListByTableId(formCode, AFT.getId());
				for (AppFormField AFF : affList) {
					if (AFF.getIsTitle() == 1) {
						// fieldData.put("title", AFF.getName());
						title = AFF.getCode();
						field = field + "," + title;
						fieldMap.put(AFF.getCode(), AFF.getName());
					}
				}
			}
		}
		List<AppFormField> appFormFieldList = this.appFormFieldService.getNotIsOverdueAFFList(formCode, mainTableId);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("createUserId", userId);
		for (int i = 0; i < appFormFieldList.size(); i++) {
			if(i == 3){
				break;
			}
			if (field == "") {
				field = appFormFieldList.get(i).getCode();
			} else {
				field = field + "," + appFormFieldList.get(i).getCode();
			}
		}
		List<Map<String, Object>> sqlData = querySingle(mainTableName, field, map, page, rows);
		List<Map<String, Object>> topMapList = new ArrayList<Map<String, Object>>();
		String time = "";
		for (int i = 0; i < sqlData.size(); i++) {
			Map<String, Object> rowMap = new HashMap<String, Object>();
			List<Map<String, Object>> columns = new ArrayList<Map<String, Object>>();
			Map<String, Object> tempMap = sqlData.get(i);
			rowMap.put("id", tempMap.get("id"));
			if(tempMap.get("createTime") != null && "java.sql.Timestamp".equals(tempMap.get("createTime").getClass().getName())){
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				rowMap.put("createTime", sdf.format((Timestamp)tempMap.get("createTime")));
				time = sdf.format((Timestamp)tempMap.get("createTime"));
				time = time.substring(0, 16);
			}else{
				rowMap.put("createTime", "");
			}
			FlowFormEntity flowForm = this.flowFormService.getFlowFormByCodeAll(formCode);
			if(flowForm != null){
				rowMap.put("moduleName", flowForm.getName());
			}
			
			Map<String, Object> userInfo = new HashMap<String, Object>();
			userInfo.put("name", "申请人");
			userInfo.put("value", tempMap.get("createUserName")+"（"+time+"）");
			rowMap.put("userInfo", userInfo);
			//rowMap.put("title", title);
			tempMap.remove("id");
			tempMap.remove("createTime");
			tempMap.remove("createUserName");
			
			
			for (String key : tempMap.keySet()) {
				if(key.equals(title)){
					Map<String, Object> titleContent = new HashMap<String, Object>();
					if(fieldMap.get(key) != null){
						titleContent.put("name", fieldMap.get(key));
					}else{
						titleContent.put("name", "");
					}
					if(tempMap.get(key) != null){
						if(tempMap.get(key) != null && "java.sql.Timestamp".equals(tempMap.get(key).getClass().getName())){
							titleContent.put("value", DateUtils.formatTime(((Timestamp)tempMap.get(key)).getTime()));
							rowMap.put("title",DateUtils.formatTime(((Timestamp)tempMap.get(key)).getTime()));
						}else{
							titleContent.put("value", tempMap.get(key));
							rowMap.put("title",tempMap.get(key));
						}
					}else{
						titleContent.put("value", "");
						rowMap.put("title","");
					}
					rowMap.put("titleContent",titleContent);
				}else{
					Map<String, Object> nameAndValueMap = new HashMap<String, Object>();
					nameAndValueMap.put("name", fieldMap.get(key));
					if(tempMap.get(key) != null){
						if(tempMap.get(key) != null && "java.sql.Timestamp".equals(tempMap.get(key).getClass().getName())){
							nameAndValueMap.put("value", DateUtils.formatTime(((Timestamp)tempMap.get(key)).getTime()));
						}else{
							nameAndValueMap.put("value", tempMap.get(key).toString());
						}
					}else{
						nameAndValueMap.put("value", "");
					}
					
					columns.add(nameAndValueMap);
				}
				// System.out.println("key= "+ key + " and value= " +
				// tempMap.get(key));
			}
			rowMap.put("extraData", columns);
			topMapList.add(rowMap);
		}

		fieldData.put("extras", topMapList);
		fieldData.put("tableName", mainTableName);
		return fieldData;
	}

	@Override
	public List<Map<String, Object>> querySingle(String table, String field, Map params) throws BusinessException {
		return querySingle(table, field, params, 0, 0);
	}
	
	@Override
	public List<Map<String, Object>> querySingle(String table, String field, Map params, int page, int rows) throws BusinessException {
		StringBuilder sqlB = new StringBuilder();
		dealQuerySql(table, field, params, sqlB);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if (page != 0 && rows != 0) {
			result = commonService.findForJdbcParam(sqlB.toString(), page, rows);
		} else {
			result = commonService.findForJdbc(sqlB.toString());
		}
		return result;
	}

	private void dealQuerySql(String table, String field, Map params, StringBuilder sqlB) {
		sqlB.append(" SELECT ");
		for (String f : field.split(",")) {
			sqlB.append(f);
			sqlB.append(",");
		}
		sqlB.deleteCharAt(sqlB.length() - 1);
		sqlB.append(" FROM " + table);
		if (params.size() >= 1) {
			sqlB.append(" WHERE 1=1 ");
			Iterator it = params.keySet().iterator();
			while (it.hasNext()) {
				String key = String.valueOf(it.next());
				String value = String.valueOf(params.get(key));
				if (!StringUtil.isEmpty(value) && !"null".equals(value)) {
					sqlB.append(" AND ");
					sqlB.append(" " + key + "='" + value + "'");
				}
			}
		}
		sqlB.append(" ORDER BY createTime DESC ");
	}

	@Override
	public AppFormTable queryMainTable(String formCode) {
		String hql = "FROM AppFormTable WHERE formCode=? ";
		AppFormTable table = appFormTableDao.findUniqueByHql(hql, formCode);
		return table;
	}

	@Override
	public AppFormTable getAppFTByCid(String formCode, String cid) throws BusinessException {
		String hql = "from AppFormTable where formCode=? and cId=?";
		return this.appFormTableDao.findUniqueByHql(hql, formCode, cid);
	}
	
	@Override
	public List<Map<String,Object>> getTableDataSum(String formCode,String userId) throws BusinessException {
		String sql = "select COUNT(*) as COUNT from t_auto_"+formCode +" where createUserId = ?";
		return this.findForJdbc(sql,userId);
	}

	@Override
	public Map<String, Object> getOneFieldData(String formCode, String businessKey) throws Exception {
		FlowFormEntity nowflowForm = flowFormService.queryLastestVersionFlowForm(formCode);// 最新版本的FlowForm

		List<String> fieldList = new ArrayList<String>();
		fieldList.add("id");
		fieldList.add("createUserName");
		fieldList.add("createTime");

		List<String> mainFields = new ArrayList<String>();

		// String field = "id,createUserName,createTime";
		// fieldMap存放字段field的code:中文名对应数据
		Map<String, Object> fieldMap = new HashMap<String, Object>();

		Map<String, Object> rowMap = new HashMap<String, Object>();
		AppFormTable appFormTable = queryAppFormTableByFormCode(formCode);
		if (appFormTable.getMainTable() != null) {
			ExceptionUtil.throwException("列表无法查询明细字段");
		}

		String title = "";
		String mainFormTableName = ConfigConst.tablePrefix + nowflowForm.getMainFormCode();// 主模板表名
		String tableName = appFormTable.getTableName();
		List<AppFormField> affList = this.appFormFieldService.getAFFListByTableId(formCode, appFormTable.getId());
		for (int i = 0; i < affList.size(); i++) {
			AppFormField aff = affList.get(i);
			if (aff.getIsOverdue().equals(0)) {
				if (aff.getIsTitle().equals(1)) {
					title = aff.getCode();
					if (aff.getIsConnectionField().equals(1)) {
						mainFields.add(aff.getCode());
					}else{
						fieldList.add(aff.getCode());
					}
				}
				if (aff.getIsShow().equals(1)) {
					if (aff.getIsConnectionField().equals(1)) {
						mainFields.add(aff.getCode());
					}else{
						fieldList.add(aff.getCode());
					}
				}
				fieldMap.put(aff.getCode(), aff.getName());
			}
			// 内容区最多显示3个字段
			if (i == 3) {
				break;
			}
		}

		Map<String, Object> finalData=new LinkedHashMap<String, Object>();
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

		// 当前表数据
		data = querySingle(tableName, StringUtil.toString(fieldList), MapKit.create("id", businessKey).getMap(), 1, 1);
		
		// 说明子模板列表有使用主模板的字段作为标题/内容
		if (mainFields.size() > 0) {
			String parentBusinessKey = "";
			List<Map<String, Object>> mainData = new ArrayList<Map<String, Object>>();
			// 获得主模板的businessKey,暂时只能从流程方面获取
			Map<String, Object> parentBusinessKeyMap = flowFormService.findOneForJdbc("SELECT t.parent_id FROM t_flow_instance t WHERE t.business_key=?",
					businessKey);
			if (parentBusinessKeyMap != null && parentBusinessKeyMap.get("parent_id") != null) {
				parentBusinessKey = parentBusinessKeyMap.get("parent_id").toString();
			}
			// 需要多查询一些用到的主表数据
			mainData = querySingle(mainFormTableName, StringUtil.toString(mainFields), MapKit.create("id", parentBusinessKey).getMap(), 1, 1);
			finalData.putAll(mainData.get(0));
		}
		
		finalData.putAll(data.get(0));
		
		String time = "";
		List<Map<String, Object>> columns = new ArrayList<Map<String, Object>>();
		
		// tempMap为查询出来的数据源
		Map<String, Object> tempMap = finalData;
		rowMap.put("id", tempMap.get("id"));
		// rowMap.put("createTime", tempMap.get("createTime"));
		// rowMap.put("title", title);
		if (tempMap.get("createTime") != null && "java.sql.Timestamp".equals(tempMap.get("createTime").getClass().getName())) {
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// time = sdf.format((Timestamp)tempMap.get("createTime"));
			rowMap.put("createTime", sdf.format((Timestamp) tempMap.get("createTime")));
			time = sdf.format((Timestamp) tempMap.get("createTime"));
			time = time.substring(0, 16);
		} else {
			rowMap.put("createTime", "");
		}
		FlowFormEntity flowForm = this.flowFormService.getFlowFormByCodeAll(formCode);
		if (flowForm != null) {
			rowMap.put("moduleName", flowForm.getName());
		}
		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("name", "申请人");
		userInfo.put("value", tempMap.get("createUserName") + "（" + time + "）");
		rowMap.put("userInfo", userInfo);
		tempMap.remove("id");
		tempMap.remove("createTime");
		tempMap.remove("createUserName");
		for (String key : tempMap.keySet()) {
			// 标题栏的数据处理逻辑
			if (key.equals(title)) {
				Map<String, Object> titleContent = new HashMap<String, Object>();
				// 处理Name的数据
				if (fieldMap.get(key) != null) {
					titleContent.put("name", fieldMap.get(key));
				} else {
					titleContent.put("name", "");
				}
				// 处理value的数据
				if (tempMap.get(key) != null) {
					// 如果是时间类型要进行格式化处理
					if (tempMap.get(key) != null && "java.sql.Timestamp".equals(tempMap.get(key).getClass().getName())) {
						titleContent.put("value", DateUtils.formatTime(((Timestamp) tempMap.get(key)).getTime()));
						rowMap.put("title", DateUtils.formatTime(((Timestamp) tempMap.get(key)).getTime()));
					} else {
						titleContent.put("value", tempMap.get(key));
						rowMap.put("title", tempMap.get(key));
					}
				} else {
					titleContent.put("value", "");
					rowMap.put("title", "");
				}
				rowMap.put("titleContent", titleContent);
				/*
				 * if(tempMap.get(key) != null){ if(tempMap.get(key) != null &&
				 * "java.sql.Timestamp"
				 * .equals(tempMap.get(key).getClass().getName())){
				 * rowMap.put("title",
				 * DateUtils.formatTime(((Timestamp)tempMap.get
				 * (key)).getTime())); }else{ rowMap.put("title",
				 * tempMap.get(key)); } }else{ rowMap.put("title", ""); }
				 */} else {
				// 其它的放到列表内容区域
				Map<String, Object> nameAndValueMap = new HashMap<String, Object>();

				nameAndValueMap.put("name", fieldMap.get(key));
				if (tempMap.get(key) != null) {
					if (tempMap.get(key) != null && "java.sql.Timestamp".equals(tempMap.get(key).getClass().getName())) {
						nameAndValueMap.put("value", DateUtils.formatTime(((Timestamp) tempMap.get(key)).getTime()));
					} else {
						nameAndValueMap.put("value", tempMap.get(key).toString());
					}
				} else {
					nameAndValueMap.put("value", "");
				}
				columns.add(nameAndValueMap);
			}
			// System.out.println("key= "+ key + " and value= " +
			// tempMap.get(key));
		}
		rowMap.put("extraData", columns);
		// fieldData.put("extras", rowMap);

		return rowMap;

	}

	@Override
	public List<AppFormField> queryConnectionFields(String tableName) {
		String hql = "FROM AppFormField WHERE tableName=? AND isOverdue=0 AND isConnectionField=1";
		return this.appFormFieldService.findHql(hql, tableName);
	}
	
	@Override
	public List<Map<String, String>> queryCirculateReceivers(String businessKey) {
		// 已传阅人列表
		List<Map<String, String>> circulateList = new ArrayList<Map<String, String>>();// 已传阅人的集合
		List<MessageReceiveEntity> messageReceiveList = messageService.queryMessageReceivesBySource(BusinessConst.SourceType_CODE_circulate, businessKey);

		for (MessageReceiveEntity messageReceive : messageReceiveList) {
			UserEntity circulateUser = sysUserService.getUserById(messageReceive.getReceiveId());
			Map<String, String> circulateMap = new HashMap<String, String>();
			circulateMap.put("userId", circulateUser.getId());
			circulateMap.put("userName", circulateUser.getName());
			circulateMap.put("portrait", circulateUser.getPortrait());
			circulateList.add(circulateMap);
		}
		
		return circulateList;
	}
}
