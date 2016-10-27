package com.xplatform.base.form.service.util;

import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.orm.hibernate4.SessionFactoryUtils;

import com.xplatform.base.develop.codegenerate.exception.DBException;
import com.xplatform.base.develop.codegenerate.service.config.DbTableHandleI;
import com.xplatform.base.develop.codegenerate.service.impl.config.util.ColumnMeta;
import com.xplatform.base.develop.codegenerate.service.impl.config.util.DbTableUtil;
import com.xplatform.base.develop.metadata.entity.MetaDataFieldEntity;
import com.xplatform.base.form.entity.AppFormField;
import com.xplatform.base.form.entity.FieldVO;
import com.xplatform.base.form.entity.AppFormTable;
import com.xplatform.base.platform.common.def.BusinessConst;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 通过hibernate和脚本来处理来同步数据库(与FieldVO,FlowFormEntity配套)
 * 对于修改数据库的字段，考虑各种数据库的情况，字段名称全部、类型修改成大写
 */
public class DbTableProcess {
	private static final Logger logger = Logger.getLogger(DbTableProcess.class);
	private static DbTableHandleI dbTableHandle;

	public DbTableProcess(Session session) {
		dbTableHandle = DbTableUtil.getTableHandle(session);
	}

	public static void createOrDropTable(AppFormTable table, List<AppFormField> fields, Session session) throws Exception {
		Template t;
		// /org/jeecgframework/web/cgform/engine/hibernate
		t = getConfig("/com/xplatform/base/develop/codegenerate/template/generatetable").getTemplate("tableTemplate.ftl");
		Writer out = new StringWriter();
		// 模板对于数字超过1000，会自动格式为1,,000(禁止转换)
		t.setNumberFormat("0.#####################");
		t.process(getRootMap(table, fields, DbTableUtil.getDataType(session)), out);
		String xml = out.toString();
		logger.info(xml);
		// recoverTable(table);
		createTable(xml, table, session);
	}

	@SuppressWarnings("all")
	private static Object getRootMap(AppFormTable table, List<AppFormField> fields, String dataType) {
		Map map = new HashMap();
		// 设置默认值
		// for (MetaDataFieldEntity field : table.getColumns()) {
		// field.setFieldDefault(judgeIsNumber(field.getFieldDefault()));
		// }
		// 主键放到list的第一位，防止hibernate配置文件出错
		// for (MetaDataFieldEntity field : columns) {
		// if ("Y".equals(field.getIsKey())) {
		// columns.remove(field);
		// list.add(field);
		// list.addAll(columns);
		// break;
		// }
		// }
		map.put("table", table);
		map.put("fields", fields);
		map.put("dataType", dataType);
		return map;
	}

	// 给表字段去除增加的单引号
	@SuppressWarnings("all")
	// private static void recoverTable(AppFormTable table) {
	// for (MetaDataFieldEntity field : table.getColumns()) {
	// field.setFieldDefault(recoverData(field.getFieldDefault()));
	// }
	// }
	public static List<String> updateTable(AppFormTable table, List<AppFormField> fields, Session session) throws DBException, SQLException {
		dbTableHandle = DbTableUtil.getTableHandle(session);
		String tableName = DbTableUtil.getDataType(session).equals("ORACLE") ? table.getTableName().toUpperCase() : table.getTableName();
		String alterTable = "alter table  " + tableName + " ";
		List<String> strings = new ArrayList<String>();
		// 对表的修改列和删除列的处理，解决hibernate没有该机制
		// 当前数据库中所有字段的Map(待修改的)
		Map<String, ColumnMeta> dataBaseColumnMetaMap = getColumnMetadataFormDataBase(null, tableName, session);
		// 最新的字段配置Map(新配置的)
		Map<String, ColumnMeta> cgFormColumnMetaMap = getColumnMetadataFormCgForm(table, fields);
		// 剔除掉固定字段
		String[] filterName = { BusinessConst.createTime, BusinessConst.createUserId, BusinessConst.createUserName, BusinessConst.updateTime,
				BusinessConst.updateUserId, BusinessConst.updateUserName, BusinessConst.primaryKey, BusinessConst.foreignKey };
		dataBaseColumnMetaMap = FormGenerateUtils.attributeMapFilter(dataBaseColumnMetaMap, filterName);
		cgFormColumnMetaMap = FormGenerateUtils.attributeMapFilter(cgFormColumnMetaMap, filterName);

		// 表如果不存在该列，则要对表做增加动作
		for (String columnName : cgFormColumnMetaMap.keySet()) {
			if (!dataBaseColumnMetaMap.containsKey(columnName)) {
				ColumnMeta cgFormColumnMeta = cgFormColumnMetaMap.get(columnName);
				strings.add(alterTable + getAddColumnSql(cgFormColumnMeta));
			}
		}

		// 删除数据库的列
		for (String columnName : dataBaseColumnMetaMap.keySet()) {
			if ((!cgFormColumnMetaMap.containsKey(columnName.toLowerCase()))) {
				strings.add(alterTable + getDropColumnSql(columnName));
			}
		}
		logger.info(strings.toString());
		return strings;
	}

	/**
	 * 通过hibernate配置生成表
	 * 
	 * @param xml
	 * @param table
	 * @param session
	 * @throws Exception
	 */
	private static void createTable(String xml, AppFormTable table, Session session) throws Exception {

		// FIXME 考虑JNDI的情况
		// 重新构建一个Configuration
		org.hibernate.cfg.Configuration newconf = new org.hibernate.cfg.Configuration();
		newconf.addXML(xml).setProperty("hibernate.dialect", ((SessionImpl) session).getFactory().getDialect().getClass().getName());
		// .setProperty("hibernate.connection.username",propertiesUtil.readProperty("jdbc.username.jeecg"))
		// .setProperty("hibernate.connection.password",propertiesUtil.readProperty("jdbc.password.jeecg"))
		// .setProperty("hibernate.dialect",propertiesUtil.readProperty("hibernate.dialect"))
		// .setProperty("hibernate.connection.url",propertiesUtil.readProperty("jdbc.url.jeecg"))
		// .setProperty("hibernate.connection.driver_class",propertiesUtil.readProperty("jdbc.driver.class"));
		//
		SchemaExport dbExport;
		dbExport = new SchemaExport(newconf, SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection());
		dbExport.execute(true, true, false, true);

	}

	/**
	 * 模版配置
	 * 
	 * @param resource
	 * @return
	 */
	private static Configuration getConfig(String resource) {

		Configuration cfg = new Configuration();
		cfg.setDefaultEncoding("UTF-8");
		cfg.setClassForTemplateLoading(DbTableProcess.class, resource);
		return cfg;
	}

	/**
	 * 获取数据库中列的描述
	 * 
	 * @param tableName
	 * @param session
	 * @return
	 * @throws SQLException
	 */
	public static Map<String, ColumnMeta> getColumnMetadataFormDataBase(String schemaName, String tableName, Session session) throws SQLException {
		Connection conn = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
		DatabaseMetaData dbMetaData = conn.getMetaData();
		ResultSet rs = dbMetaData.getColumns(null, schemaName, tableName, "%");
		ColumnMeta columnMeta;
		Map<String, ColumnMeta> columnMap = new HashMap<String, ColumnMeta>();
		while (rs.next()) {
			columnMeta = new ColumnMeta();
			columnMeta.setTableName(tableName);
			columnMeta.setColumnName(rs.getString("COLUMN_NAME").toLowerCase());
			columnMeta.setColunmType(dbTableHandle.getMatchClassTypeByDataType(rs.getString("TYPE_NAME"), rs.getInt("DECIMAL_DIGITS")));
			columnMeta.setColumnSize(rs.getInt("COLUMN_SIZE"));
			columnMeta.setDecimalDigits(rs.getInt("DECIMAL_DIGITS"));
			columnMeta.setIsNullable(rs.getInt("NULLABLE") == 1 ? "Y" : "N");
			columnMeta.setComment(rs.getString("REMARKS"));
			columnMeta.setFieldDefault(judgeIsNumber(rs.getString("COLUMN_DEF")) == null ? "" : judgeIsNumber(rs.getString("COLUMN_DEF")));
			logger.info("getColumnMetadataFormDataBase --->COLUMN_NAME:" + rs.getString("COLUMN_NAME") + " TYPE_NAME :" + rs.getString("TYPE_NAME")
					+ " DECIMAL_DIGITS:" + rs.getInt("DECIMAL_DIGITS") + " COLUMN_SIZE:" + rs.getInt("COLUMN_SIZE"));
			columnMap.put(rs.getString("COLUMN_NAME").toLowerCase(), columnMeta);
		}

		return columnMap;
	}

	/**
	 * 返回cgForm中列的描述信息
	 * 
	 * @param table
	 * @return
	 */
	public static Map<String, ColumnMeta> getColumnMetadataFormCgForm(AppFormTable table, List<AppFormField> fields) {
		Map<String, ColumnMeta> map = new HashMap<String, ColumnMeta>();
		ColumnMeta columnMeta;
		for (AppFormField field : fields) {
			columnMeta = new ColumnMeta();
			columnMeta.setTableName(table.getTableName().toLowerCase());
			columnMeta.setColumnId(field.getId());
			columnMeta.setColumnName(field.getCode().toLowerCase());
			columnMeta.setColumnSize(field.getLength() == null ? 0 : field.getLength());
			columnMeta.setColunmType(field.getType().toLowerCase());
			columnMeta.setIsNullable(field.getNotNull() == 0 ? "Y" : "N");
			columnMeta.setComment(field.getName());
			columnMeta.setDecimalDigits(field.getScale() == null ? 0 : field.getScale());
			// columnMeta.setFieldDefault(judgeIsNumber(cgFormFieldEntity.getFieldDefault()));
			map.put(field.getCode().toLowerCase(), columnMeta);

		}
		return map;
	}

	/**
	 * 返回cgForm中列名的新和旧的对应关系
	 * 
	 * @param table
	 * @return
	 */
	public static Map<String, String> getNewAndOldFieldName(AppFormTable table) {
		Map<String, String> map = new HashMap<String, String>();
		// List<MetaDataFieldEntity> cgFormFieldEntities = table.getColumns();
		// for (MetaDataFieldEntity cgFormFieldEntity : cgFormFieldEntities) {
		// map.put(cgFormFieldEntity.getFieldName(),
		// cgFormFieldEntity.getOldFieldName());
		// }
		return map;
	}

	/**
	 * 创建删除字段的sql
	 * 
	 * @param fieldName
	 * @return
	 */
	private static String getDropColumnSql(String fieldName) {
		// ALTER TABLE `test` DROP COLUMN `aaaa`;
		return dbTableHandle.getDropColumnSql(fieldName);
	}

	/**
	 * 创建更新字段的sql
	 * 
	 * @param newColumn
	 * @param agoColumn
	 * @return
	 */
	private static String getUpdateColumnSql(ColumnMeta cgformcolumnMeta, ColumnMeta datacolumnMeta) throws DBException {
		// modify birthday varchar2(10) not null;
		// return " MODIFY COLUMN  "+getFieldDesc(columnMeta)+",";
		return dbTableHandle.getUpdateColumnSql(cgformcolumnMeta, datacolumnMeta);
	}

	/**
	 * 处理特殊sql
	 * 
	 * @param cgformcolumnMeta
	 * @param datacolumnMeta
	 * @return
	 */
	private static String getUpdateSpecialSql(ColumnMeta cgformcolumnMeta, ColumnMeta datacolumnMeta) {
		return dbTableHandle.getSpecialHandle(cgformcolumnMeta, datacolumnMeta);
	}

	/**
	 * 修改列名
	 * 
	 * @param columnMeta
	 * @return
	 */
	private static String getReNameFieldName(ColumnMeta columnMeta) {
		// CHANGE COLUMN `name1` `name2` varchar(50) NULL COMMENT '姓名';
		// return "CHANGE COLUMN  "+columnMeta.getOldColumnName()
		// +" "+getFieldDesc(columnMeta)+",";
		return dbTableHandle.getReNameFieldName(columnMeta);
	}

	/**
	 * 创建增加字段的sql
	 * 
	 * @param column
	 * @param agoColumn
	 * @return
	 */
	private static String getAddColumnSql(ColumnMeta columnMeta) {
		// return " ADD COLUMN "+getFieldDesc(columnMeta)+",";
		return dbTableHandle.getAddColumnSql(columnMeta);
	}

	/**
	 * 添加注释的sql
	 *
	 * @Author JueYue
	 * @date 2013年12月1日
	 * @param cgFormColumnMeta
	 * @return
	 */
	private String getCommentSql(ColumnMeta columnMeta) {
		return dbTableHandle.getCommentSql(columnMeta);
	}

	private int updateFieldName(String columnName, String id, Session session) {
		return session.createSQLQuery("update cgform_field set old_field_name= '" + columnName + "' where id='" + id + "'").executeUpdate();
	}

	/**
	 * 判断是不数字,不是数字的话加上''
	 *
	 * @Author JueYue
	 * @date 2013年11月27日
	 * @param text
	 * @return
	 */
	private static String judgeIsNumber(String text) {
		if (StringUtils.isNotEmpty(text)) {
			try {
				Double.valueOf(text);
			} catch (Exception e) {
				text = "'" + text + "'";
			}
		}
		return text;
	}

	/**
	 * 对judgeIsNumber处理的数据进行复原
	 *
	 * @Author JueYue
	 * @date 2013年11月27日
	 * @param text
	 * @return
	 */
	private static String recoverData(String text) {
		if (StringUtils.isNotEmpty(text)) {
			try {
				Double.valueOf(text);
			} catch (Exception e) {
				text = text.substring(1, text.length() - 1);
			}
		}
		return text;
	}
}
