package com.xplatform.base.develop.codegenerate.generatetype.ctree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xplatform.base.develop.codeconfig.entity.GenerateFieldEntity;
import com.xplatform.base.develop.codegenerate.database.UcgReadTable;
import com.xplatform.base.develop.codegenerate.generate.ICallBack;
import com.xplatform.base.develop.codegenerate.pojo.CreateFileProperty;
import com.xplatform.base.develop.codegenerate.pojo.GenerateVo;
import com.xplatform.base.develop.codegenerate.util.CodeDateUtils;
import com.xplatform.base.develop.codegenerate.util.CodeResourceUtil;
import com.xplatform.base.develop.codegenerate.util.NonceUtils;
import com.xplatform.base.develop.codegenerate.util.def.FtlDef;
import com.xplatform.base.framework.core.util.StringUtil;

import freemarker.template.TemplateException;

public class CgcTreeCodeGenerate implements ICallBack {
	private static final Log log = LogFactory.getLog(CgcTreeCodeGenerate.class);

	private String entityPackage = "test";
	private String entityName = "Person";
	private String tableName = "person";
	private String cEntityName ="";
	private String cField="";
	private String ftlDescription = "公告";
	private String primaryKeyPolicy = "uuid";
	private String sequenceCode = "";
	private String[] foreignKeys;
	public static int FIELD_ROW_NUM = 1;
	private GenerateVo cgformConfig;
	private Map<String, Object> data = new HashMap<String, Object>();
	private static CreateFileProperty createFileProperty = new CreateFileProperty();

	static {
		createFileProperty.setActionFlag(true);
		createFileProperty.setServiceIFlag(true);
		createFileProperty.setJspFlag(true);
		createFileProperty.setServiceImplFlag(true);
		createFileProperty.setJspMode("01");
		createFileProperty.setPageFlag(true);
		createFileProperty.setEntityFlag(true);
		createFileProperty.setDaoFlag(true);
		createFileProperty.setDaoImplFlag(true);
	}

	public CgcTreeCodeGenerate() {
	}

	public CgcTreeCodeGenerate(CreateFileProperty createFileProperty2,
			GenerateVo generateEntity) {
		this.entityName = generateEntity.getEntityName();
		this.entityPackage = generateEntity.getEntityPackage();
		this.tableName = generateEntity.getTableName();
		this.ftlDescription = generateEntity.getFtlDescription();
		this.cEntityName = generateEntity.getcEntityName();
		this.cField = generateEntity.getcField();
		FIELD_ROW_NUM = 1;
		createFileProperty = createFileProperty2;
		createFileProperty.setJspMode("01");
		this.primaryKeyPolicy = generateEntity.getPrimaryKeyPolicy();
		this.sequenceCode = "";
		this.cgformConfig = generateEntity;
	}

	public Map<String, Object> execute() {
		if(this.data.isEmpty()){
		//Map data = new HashMap();
		Map<String, Object> fieldMeta = new HashMap<String, Object>();

		data.put("bussiPackage", CodeResourceUtil.bussiPackage);

		data.put("entityPackage", this.entityPackage);
		
		data.put("jsp_path", CodeResourceUtil.getJspPath());

		data.put("entityName", this.entityName);
		
		data.put("cEntityName", this.cEntityName);
		
		data.put("cField", this.cField);

		data.put("tableName", this.tableName);

		data.put("ftl_description", this.ftlDescription);

		data.put(FtlDef.JEECG_TABLE_ID,
				CodeResourceUtil.JEECG_GENERATE_TABLE_ID);

		data.put(FtlDef.JEECG_PRIMARY_KEY_POLICY, this.primaryKeyPolicy);
		data.put(FtlDef.JEECG_SEQUENCE_CODE, this.sequenceCode);

		data.put("ftl_create_time", CodeDateUtils.dateToString(new Date()));

		data.put("foreignKeys", this.foreignKeys);

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
			List<GenerateFieldEntity> columns = this.cgformConfig.getColumns();
			String type;
			for (GenerateFieldEntity cf : columns) {
				type = cf.getFieldType();
				if ("string".equalsIgnoreCase(type))
					cf.setFieldType("String");
				else if ("Date".equalsIgnoreCase(type))
					cf.setFieldType("Date");
				else if ("double".equalsIgnoreCase(type))
					cf.setFieldType("Double");
				else if ("int".equalsIgnoreCase(type))
					cf.setFieldType("Integer");
				else if ("BigDecimal".equalsIgnoreCase(type))
					cf.setFieldType("BigDecimal");
				else if ("Text".equalsIgnoreCase(type))
					cf.setFieldType("String");
				else if ("Blob".equalsIgnoreCase(type)) {
					cf.setFieldType("Blob");
				}
				String fieldName = cf.getFieldName();
				String fieldNameV = UcgReadTable.formatField(fieldName);
				cf.setFieldName(fieldNameV);
				fieldMeta.put(fieldNameV, fieldName.toUpperCase());
			}
			List pageColumns = new ArrayList();
			for (GenerateFieldEntity cf : columns) {
				if ((StringUtil.isNotEmpty(cf.getEditShow()))
						&& ("Y".equalsIgnoreCase(cf.getEditShow()))) {
					pageColumns.add(cf);
				}
			}

			data.put("cgformConfig", this.cgformConfig);
			data.put("fieldMeta", fieldMeta);
			data.put("columns", columns);
			data.put("pageColumns", pageColumns);
			data
					.put(
							"buttonSqlMap",
							this.cgformConfig.getButtonSqlMap() == null ? new HashMap(
									0)
									: this.cgformConfig.getButtonSqlMap());
		} catch (Exception e) {
			e.printStackTrace();
		}
		long serialVersionUID = NonceUtils.randomLong()
				+ NonceUtils.currentMills();
		data.put("serialVersionUID", String.valueOf(serialVersionUID));
		this.data =data;
		}
		return this.data;
	}

	public void generateToFile() throws TemplateException, IOException {
		log.info("----jeecg---Code----Generation----[单表树模型:" + this.tableName
				+ "]------- 生成中。。。");

		CgcTreeCodeFactory codeFactory = new CgcTreeCodeFactory();
		
		codeFactory.setCallBack(new CgcTreeCodeGenerate(createFileProperty,this.cgformConfig));
		
        this.entityPackage = this.entityPackage.replace(".", "/");
		if (createFileProperty.isJspFlag()) {
			codeFactory.invoke("ctree/cgctree_jspListTemplate.ftl", "jspList");
			codeFactory.invoke("ctree/cgctree_jspEditTemplate.ftl", "jspEdit");
		}
		this.entityPackage = this.entityPackage.replace("/", ".");
		if (createFileProperty.isActionFlag()) {
			codeFactory.invoke("ctree/cgctree_controllerTemplate.ftl", "controller");
		}
		if (createFileProperty.isServiceImplFlag()) {
			codeFactory.invoke("ctree/cgctree_serviceImplTemplate.ftl", "serviceImpl");
		}
		if (createFileProperty.isServiceIFlag()) {
			codeFactory.invoke("ctree/cgctree_serviceITemplate.ftl", "service");
		}
		if (createFileProperty.isEntityFlag()) {
			codeFactory.invoke("ctree/cgctree_entityTemplate.ftl", "entity");
		}
		if(createFileProperty.isDaoFlag()){
			codeFactory.invoke("ctree/cgctree_daoTemplate.ftl", "dao");
		}
		if(createFileProperty.isDaoImplFlag()){
			codeFactory.invoke("ctree/cgctree_daoImplTemplate.ftl", "daoImpl");
		}
		log.info("----ucg----Code----Generation-----[单表树模型：" + this.tableName
				+ "]------ 生成完成。。。");
	}

	public GenerateVo getCgformConfig() {
		return this.cgformConfig;
	}

	public void setCgformConfig(GenerateVo cgformConfig) {
		this.cgformConfig = cgformConfig;
	}
}