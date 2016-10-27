package com.xplatform.base.develop.codegenerate.generatetype.onetomany;

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
import com.xplatform.base.develop.codegenerate.generatetype.one.CgformCodeGenerate;
import com.xplatform.base.develop.codegenerate.pojo.CreateFileProperty;
import com.xplatform.base.develop.codegenerate.pojo.GenerateVo;
import com.xplatform.base.develop.codegenerate.pojo.onetomany.CodeParamEntity;
import com.xplatform.base.develop.codegenerate.pojo.onetomany.SubTableEntity;
import com.xplatform.base.develop.codegenerate.util.CodeDateUtils;
import com.xplatform.base.develop.codegenerate.util.CodeResourceUtil;
import com.xplatform.base.develop.codegenerate.util.NonceUtils;
import com.xplatform.base.develop.codegenerate.util.def.FtlDef;
import com.xplatform.base.framework.core.util.StringUtil;

import freemarker.template.TemplateException;

public class CgformCodeGenerateOneToMany implements ICallBack {
	private static final Log log = LogFactory
			.getLog(CgformCodeGenerateOneToMany.class);

	private String entityPackage = "test";
	private String entityName = "Person";
	private String tableName = "person";
	private String ftlDescription = "用户";
	private String primaryKeyPolicy = "uuid";
	private String sequenceCode = "";
	public static String FTL_MODE_A = "A";
	public static String FTL_MODE_B = "B";
	private List<GenerateVo> subList;
	List<CreateFileProperty> subcreateFileProperty;
	private GenerateVo cgformConfig;

	private static CreateFileProperty createFileProperty = new CreateFileProperty();
	private GenerateVo mainG;
	private Map<String, GenerateVo> subsG;
	private Map<String, Object> data = new HashMap<String, Object>();

	static {
		createFileProperty.setActionFlag(true);
		createFileProperty.setServiceIFlag(true);
		createFileProperty.setJspFlag(true);
		createFileProperty.setServiceImplFlag(true);
		createFileProperty.setPageFlag(true);
		createFileProperty.setEntityFlag(true);
		createFileProperty.setDaoFlag(true);
		createFileProperty.setDaoImplFlag(true);
	}

	public CgformCodeGenerateOneToMany() {
	}

	public CgformCodeGenerateOneToMany(CreateFileProperty createFileProperty2,
			GenerateVo generateEntity,List<GenerateVo> subList,List<CreateFileProperty> subcreateFileProperty) {
		this.entityName = generateEntity.getEntityName();
		this.entityPackage = generateEntity.getEntityPackage();
		this.tableName = generateEntity.getTableName();
		this.ftlDescription = generateEntity.getFtlDescription();
		createFileProperty = createFileProperty2;
		createFileProperty.setJspMode("01");
		this.primaryKeyPolicy = generateEntity.getPrimaryKeyPolicy();
		this.sequenceCode = "";
		this.cgformConfig = generateEntity;
		this.subList = subList;
		this.subcreateFileProperty = subcreateFileProperty;
		this.mainG = generateEntity;
	}
	
	public CgformCodeGenerateOneToMany(CreateFileProperty createFileProperty2,
			GenerateVo generateEntity) {
		this.entityName = generateEntity.getEntityName();
		this.entityPackage = generateEntity.getEntityPackage();
		this.tableName = generateEntity.getTableName();
		this.ftlDescription = generateEntity.getFtlDescription();
		createFileProperty = createFileProperty2;
		createFileProperty.setJspMode("01");
		this.primaryKeyPolicy = generateEntity.getPrimaryKeyPolicy();
		this.sequenceCode = "";
		this.cgformConfig = generateEntity;
	}
	
	public CgformCodeGenerateOneToMany(CreateFileProperty createFileProperty2,
			GenerateVo generateEntity,GenerateVo mianG) {
		this.entityName = generateEntity.getEntityName();
		this.entityPackage = generateEntity.getEntityPackage();
		this.tableName = generateEntity.getTableName();
		this.ftlDescription = generateEntity.getFtlDescription();
		createFileProperty = createFileProperty2;
		createFileProperty.setJspMode("01");
		this.primaryKeyPolicy = generateEntity.getPrimaryKeyPolicy();
		this.sequenceCode = "";
		this.cgformConfig = generateEntity;
		this.mainG = mianG;
	}
	public Map<String, Object> execute() {
		if(this.data.isEmpty()){
		//Map data = new HashMap();
		Map<String, Object> fieldMeta = new HashMap<String, Object>();

		data.put("bussiPackage", CodeResourceUtil.bussiPackage);

		data.put("entityPackage", this.entityPackage);
		
		data.put("jsp_path", CodeResourceUtil.getJspPath());

		data.put("entityName", this.entityName);

		data.put("tableName", this.tableName);
		
		data.put("mainG", mainG);

		data.put("ftl_description", this.ftlDescription);
		
		data.put("subNum", subList==null?0:subList.size());
		
		data.put("subList", subList);

		data.put(FtlDef.JEECG_TABLE_ID,
				CodeResourceUtil.JEECG_GENERATE_TABLE_ID);

		data.put(FtlDef.JEECG_PRIMARY_KEY_POLICY, this.primaryKeyPolicy);
		data.put(FtlDef.JEECG_SEQUENCE_CODE, this.sequenceCode);

		data.put("ftl_create_time", CodeDateUtils.dateToString(new Date()));

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
		data.clear();
		CgformCodeFactoryOneToMany codeFactoryOneToMany = new CgformCodeFactoryOneToMany();
		codeFactoryOneToMany.setCallBack(new CgformCodeGenerateOneToMany(
				createFileProperty,cgformConfig,subList,subcreateFileProperty));
		
		this.entityPackage = this.entityPackage.replace(".", "/");
		if (createFileProperty.isJspFlag()) {
			codeFactoryOneToMany.invoke("onetomany/cgform_jspListTemplate.ftl","jspList");
			codeFactoryOneToMany.invoke("onetomany/cgform_jspEditTemplate.ftl","jspEdit");
		}
		this.entityPackage = this.entityPackage.replace("/", ".");
		if (createFileProperty.isServiceImplFlag()) {
			codeFactoryOneToMany.invoke(
					"onetomany/cgform_serviceImplTemplate.ftl", "serviceImpl");
		}
		if (createFileProperty.isServiceIFlag()) {
			codeFactoryOneToMany.invoke(
					"onetomany/cgform_serviceITemplate.ftl", "service");
		}
		if (createFileProperty.isActionFlag()) {
			codeFactoryOneToMany.invoke(
					"onetomany/cgform_controllerTemplate.ftl", "controller");
		}
		if (createFileProperty.isEntityFlag()) {
			codeFactoryOneToMany.invoke("onetomany/cgform_entityTemplate.ftl",
					"entity");
		}
		if(createFileProperty.isDaoFlag()){
			codeFactoryOneToMany.invoke("onetomany/cgform_daoTemplate.ftl",
					"dao");
		}
		if(createFileProperty.isDaoImplFlag()){
			codeFactoryOneToMany.invoke("onetomany/cgform_daoImplTemplate.ftl",
					"daoImpl");
		}
		for(int i=0;i<subList.size();i++){
		//for(GenerateEntity sub : subList){
			data.clear();
			GenerateVo sub = subList.get(i);
			codeFactoryOneToMany.setCallBack(new CgformCodeGenerateOneToMany(
					subcreateFileProperty.get(i),sub,mainG));
			this.entityPackage = sub.getEntityPackage().replace(".", "/");
			if (createFileProperty.isJspFlag()) {
				codeFactoryOneToMany.invoke("onetomany/subform_jspEditTemplate.ftl","jspEdit");
			}
			this.entityPackage = this.entityPackage.replace("/", ".");
			if (createFileProperty.isServiceImplFlag()) {
				codeFactoryOneToMany.invoke(
						"onetomany/subform_serviceImplTemplate.ftl", "serviceImpl");
			}
			if (createFileProperty.isServiceIFlag()) {
				codeFactoryOneToMany.invoke(
						"onetomany/subform_serviceITemplate.ftl", "service");
			}
			if (createFileProperty.isActionFlag()) {
				codeFactoryOneToMany.invoke(
						"onetomany/subform_controllerTemplate.ftl", "controller");
			}
			if (createFileProperty.isEntityFlag()) {
				codeFactoryOneToMany.invoke("onetomany/subform_entityTemplate.ftl",
						"entity");
			}
			if(createFileProperty.isDaoFlag()){
				codeFactoryOneToMany.invoke("onetomany/subform_daoTemplate.ftl",
						"dao");
			}
			if(createFileProperty.isDaoImplFlag()){
				codeFactoryOneToMany.invoke("onetomany/subform_daoImplTemplate.ftl",
						"daoImpl");
			}
		}
	}
}