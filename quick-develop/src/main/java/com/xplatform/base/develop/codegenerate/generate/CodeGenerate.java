package com.xplatform.base.develop.codegenerate.generate;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.xplatform.base.develop.codegenerate.database.UcgReadTable;
import com.xplatform.base.develop.codegenerate.pojo.Columnt;
import com.xplatform.base.develop.codegenerate.pojo.CreateFileProperty;
import com.xplatform.base.develop.codegenerate.util.CodeDateUtils;
import com.xplatform.base.develop.codegenerate.util.CodeResourceUtil;
import com.xplatform.base.develop.codegenerate.util.NonceUtils;
import com.xplatform.base.develop.codegenerate.util.def.FtlDef;

public class CodeGenerate implements ICallBack {
	private static final Log log = LogFactory.getLog(CodeGenerate.class);

	private static String entityPackage = "test";
	private static String entityName = "Person";
	private static String tableName = "person";
	private static String ftlDescription = "公告";
	private static String primaryKeyPolicy = "uuid";
	private static String sequenceCode = "";
	private static String[] foreignKeys;
	private List<Columnt> originalColumns = new ArrayList();
	public static int FIELD_ROW_NUM = 1;
	private static CreateFileProperty createFileProperty = new CreateFileProperty();

	private List<Columnt> columns = new ArrayList();
	private UcgReadTable dbFiledUtil = new UcgReadTable();

	static {
		createFileProperty.setActionFlag(true);
		createFileProperty.setServiceIFlag(true);
		createFileProperty.setJspFlag(true);
		createFileProperty.setServiceImplFlag(true);
		createFileProperty.setJspMode("01");
		createFileProperty.setPageFlag(true);
		createFileProperty.setEntityFlag(true);
	}

	public CodeGenerate() {
	}

	public CodeGenerate(String entityPackage, String entityName,
			String tableName, String ftlDescription,
			CreateFileProperty createFileProperty, int fieldRowNum,
			String primaryKeyPolicy, String sequenceCode) {
		this.entityName = entityName;
		this.entityPackage = entityPackage;
		this.tableName = tableName;
		this.ftlDescription = ftlDescription;
		this.createFileProperty = createFileProperty;
		this.FIELD_ROW_NUM = fieldRowNum;
		this.primaryKeyPolicy = primaryKeyPolicy;
		this.sequenceCode = sequenceCode;
	}

	public CodeGenerate(String entityPackage, String entityName,
			String tableName, String ftlDescription,
			CreateFileProperty createFileProperty, String primaryKeyPolicy,
			String sequenceCode) {
		this.entityName = entityName;
		this.entityPackage = entityPackage;
		this.tableName = tableName;
		this.ftlDescription = ftlDescription;
		this.createFileProperty = createFileProperty;
		this.primaryKeyPolicy = primaryKeyPolicy;
		this.sequenceCode = sequenceCode;
	}

	public CodeGenerate(String entityPackage, String entityName,
			String tableName, String ftlDescription,
			CreateFileProperty createFileProperty, String primaryKeyPolicy,
			String sequenceCode, String[] foreignKeys) {
		this.entityName = entityName;
		this.entityPackage = entityPackage;
		this.tableName = tableName;
		this.ftlDescription = ftlDescription;
		this.createFileProperty = createFileProperty;
		this.primaryKeyPolicy = primaryKeyPolicy;
		this.sequenceCode = sequenceCode;
		this.foreignKeys = foreignKeys;
	}

	/**
	 * 封装模版和辅助的信息数据
	 */
	public Map<String, Object> execute() {
		Map<String,Object> data = new HashMap<String,Object>();

		data.put("bussiPackage", CodeResourceUtil.bussiPackage);

		data.put("entityPackage", entityPackage);

		data.put("entityName", entityName);

		data.put("tableName", tableName);

		data.put("ftl_description", ftlDescription);

		data.put(FtlDef.JEECG_TABLE_ID,
				CodeResourceUtil.JEECG_GENERATE_TABLE_ID);

		data.put(FtlDef.JEECG_PRIMARY_KEY_POLICY, primaryKeyPolicy);
		data.put(FtlDef.JEECG_SEQUENCE_CODE, sequenceCode);

		data.put("ftl_create_time", CodeDateUtils.dateToString(new Date()));

		data.put("foreignKeys", foreignKeys);

		data
				.put(
						FtlDef.FIELD_REQUIRED_NAME,
						Integer
								.valueOf(StringUtils
										.isNotEmpty(CodeResourceUtil.JEECG_UI_FIELD_REQUIRED_NUM) ? Integer
										.parseInt(CodeResourceUtil.JEECG_UI_FIELD_REQUIRED_NUM)
										: -1));

		data
				.put(
						FtlDef.SEARCH_FIELD_NUM,
						Integer
								.valueOf(StringUtils
										.isNotEmpty(CodeResourceUtil.JEECG_UI_FIELD_SEARCH_NUM) ? Integer
										.parseInt(CodeResourceUtil.JEECG_UI_FIELD_SEARCH_NUM)
										: -1));

		data.put(FtlDef.FIELD_ROW_NAME, Integer.valueOf(FIELD_ROW_NUM));
		try {
			this.columns = this.dbFiledUtil.readTableColumn(tableName);
			data.put("columns", this.columns);

			this.originalColumns = this.dbFiledUtil
					.readOriginalTableColumn(tableName);
			data.put("originalColumns", this.originalColumns);

			for (Columnt c : this.originalColumns) {
				if (c.getFieldName().toLowerCase().equals(
						CodeResourceUtil.JEECG_GENERATE_TABLE_ID.toLowerCase()))
					data.put("primary_key_type", c.getFieldType());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		long serialVersionUID = NonceUtils.randomLong()
				+ NonceUtils.currentMills();
		data.put("serialVersionUID", String.valueOf(serialVersionUID));
		return data;
	}

	public void generateToFile() {
		log.info("----jeecg---Code----Generation----[单表模型:" + tableName
				+ "]------- 生成中。。。");

		CodeFactory codeFactory = new CodeFactory();
		codeFactory.setCallBack(new CodeGenerate());

		if (createFileProperty.isJspFlag()) {
			if ("03".equals(createFileProperty.getJspMode())) {
				codeFactory.invoke("onetomany/jspSubTemplate.ftl", "jspList");
			} else {
				if ("01".equals(createFileProperty.getJspMode())) {
					codeFactory.invoke("one/jspTableTemplate.ftl", "jsp");
				}
				if ("02".equals(createFileProperty.getJspMode())) {
					codeFactory.invoke("one/jspDivTemplate.ftl", "jsp");
				}
				codeFactory.invoke("one/jspListTemplate.ftl", "jspList");
			}
		}
		if (createFileProperty.isServiceImplFlag()) {
			codeFactory.invoke("one/serviceImplTemplate.ftl", "serviceImpl");
		}
		if (createFileProperty.isServiceIFlag()) {
			codeFactory.invoke("one/serviceITemplate.ftl", "service");
		}
		if (createFileProperty.isActionFlag()) {
			codeFactory.invoke("one/controllerTemplate.ftl", "controller");
		}
		if (createFileProperty.isEntityFlag()) {
			codeFactory.invoke("one/entityTemplate.ftl", "entity");
		}
		
		//新增dao层
		codeFactory.invoke("one/daoTemplate.ftl", "dao");
		codeFactory.invoke("one/daoImplTemplate.ftl", "daoImpl");
		
		log.info("----jeecg----Code----Generation-----[单表模型：" + tableName
				+ "]------ 生成完成。。。");
	}

	public static void main(String[] args) {
		System.out
				.println("----jeecg--------- Code------------- Generation -----[单表模型]------- 生成中。。。");
		new CodeGenerate().generateToFile();
		System.out
				.println("----jeecg--------- Code------------- Generation -----[单表模型]------- 生成完成。。。");
	}
}