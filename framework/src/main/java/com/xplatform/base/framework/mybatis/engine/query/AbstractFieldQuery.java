package com.xplatform.base.framework.mybatis.engine.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.engine.type.FormFieldTypeEnum;
import com.xplatform.base.framework.mybatis.engine.vo.FormViewField;
import com.xplatform.base.framework.mybatis.engine.vo.VoAttribute;
import com.xplatform.base.framework.mybatis.engine.vo.VoForm;
/**
 * 
 * <STRONG>抽象字段查询</STRONG> :  <p>
 *   
 * @version 1.0 <p>
 * @author mengfx@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : Sep 14, 2012 1:36:36 PM<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   	修改时间                     				修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Sep 14, 2012 1:36:36 PM
 *</pre>
 */
public abstract class AbstractFieldQuery<T extends FieldQuery<T, U>, U> extends
		AbstractQuery<T, U> implements FieldQuery<T, U> {

	/**
	 * 定义查询字段的SQL片段集合
	 */
	protected List<String> fieldsSqlList = new ArrayList<String>();
	
	protected List<String> clobFieldSqlList = new ArrayList<String>();
	
	protected List<String> clobFieldName = new ArrayList<String>();

	/**
	 * 设置查询字段参数并生成查询表名 和字段的集合
	 */
	public FieldQuery<T, U> fields(List<VoForm> formList,List<FormViewField> sumFieldList) {
		
		if(formList == null || sumFieldList == null){
			throw new BusinessRuntimeException("属性为空");
		}
		
		this.formList = formList;
		this.sumFieldList = sumFieldList;
		this.buildFields();
		return this;
	}

	/**
	 * 
	 * <pre>
	 * 生成查询表名 和字段的集合
	 * </pre>
	 */
	protected void buildFields() {
		//生成表名集合
		buildNames();
		
		//生成初始化条件SQL集合
		buildInitConditionSql();
		
		
		if(formList == null || sumFieldList == null){
			throw new BusinessRuntimeException("属性值为空");
		}
		for (VoForm form : formList) {
			List<String> fieldList = new ArrayList();
			List<String> clobFieldList = new ArrayList();
			Map<String, VoAttribute> map = form.getVoAttributeMap();
			Set<String> keySet = map.keySet();
			
			for (FormViewField commfield : sumFieldList) {
				
				String queryFieldStr = "";
				String alias = commfield.getColumnAlias();// 别名
				String columnName = " NULL ";
				
				if(!StringUtil.equals(commfield.getColumnType(),FormFieldTypeEnum.FIELD_CLOB.getPattern())){
					for(String key:keySet){
						VoAttribute vo = map.get(key);
						if(vo.getFieldList()!= null){
							for(FormViewField field: vo.getFieldList()){
								if (StringUtil.equals(field.getColumnName(), "ENTITY_TAG")) {
									//System.out.println("xx:" + 11);
								}
								if(field.getColumnName().equals("FORM_KEY") && commfield.equals("FORM_KEY")){
								}
								if(StringUtil.equals(field.getColumnId(), commfield.getColumnId())){
									columnName = vo.getAlias()+"."+field.getColumnName();
								}
							}
						}
					}
					if("KEY".equals(alias)){
						queryFieldStr = columnName + " \"" +alias+"\"";
					}else{
						queryFieldStr = columnName + " " + alias;
					}
					
					fieldList.add(queryFieldStr);
				}else{
					for(String key:keySet){
						VoAttribute vo = map.get(key);
						if(vo.getFieldList()!= null){
							
							for(FormViewField field: vo.getFieldList()){
								
								if(StringUtil.equals(field.getColumnId(), commfield.getColumnId())){
									columnName = vo.getAlias()+"."+field.getColumnName();
								}
							}
						}
					}
					if("KEY".equals(alias)){
						queryFieldStr = columnName + " \"" +alias+"\"";
					}else{
						queryFieldStr = columnName + " " + alias;
					}
					clobFieldList.add(queryFieldStr);
					clobFieldName.add(alias);
				}
			}
			
			StringBuffer fieldsSql = new StringBuffer();
			int i = 0;
			for (String field : fieldList) {
				if (i != 0) {
					fieldsSql.append(",");
				}
				fieldsSql.append(field);
				i++;
			}

			this.fieldsSqlList.add(fieldsSql.toString());
			
			clobFieldSqlList.addAll(clobFieldList);
			
		}

	}
	
	/**
	 * 
	 * <pre>
	 * 生成表名集合
	 * </pre>
	 */
	protected void buildNames() {
		tableNameSqlList = new ArrayList();
		
		if(formList == null){
			throw new BusinessRuntimeException("属性值为空");
		}
		
		for (VoForm form : formList) {
			List<String> tableNameList = new ArrayList();
			
			Map<String, VoAttribute> map = form.getVoAttributeMap();
			Set<String> keySet = map.keySet();
			for(String key:keySet){
				VoAttribute vo = map.get(key);
				String tempName = "";
				
				tempName = vo.getTableName();
				
				tableNameList.add(tempName + " " + vo.getAlias());
			}
			
//			if (form.getEntityTableAttribute() != null) {
//				tableNameList.add(form.getEntityTableAttribute().getTableName()
//						+ " " + form.getEntityTableAttribute().getAlias());
//			}
//			if (form.getTypeTableAttribute() != null) {
//
//				tableNameList.add(form.getTypeTableAttribute().getTableName()
//						+ " " + form.getTypeTableAttribute().getAlias());
//			}
//			if (form.getExtTableAttribute() != null) {
//				tableNameList.add(form.getExtTableAttribute().getTableName()
//						+ " " + form.getExtTableAttribute().getAlias());
//			}
//			if (form.getPageTableAttribute() != null) {
//				tableNameList.add(form.getPageTableAttribute().getTableName()
//						+ " " + form.getPageTableAttribute().getAlias());
//			}
//			if (form.getStateTableAttribute() != null) {
//				tableNameList.add(form.getStateTableAttribute().getTableName()
//						+ " " + form.getStateTableAttribute().getAlias());
//			}
//
//			if (form.getRelationTableAttribute() != null) {
//				tableNameList.add(form.getRelationTableAttribute()
//						.getTableName()
//						+ " " + form.getRelationTableAttribute().getAlias());
//			}
//			
			StringBuffer tableNameSql = new StringBuffer();
			int i = 0;
			for (String tableName : tableNameList) {
				if (i != 0) {
					tableNameSql.append(",");
				}
				tableNameSql.append(tableName);
				i++;
			}		
			this.tableNameSqlList.add(tableNameSql.toString());
		}

	}
	
	/**
	 * 
	 * <pre>
	 * 生成初始化条件SQL集合
	 * </pre>
	 */
	protected void buildInitConditionSql() {
		this.initConditionSqlList = new ArrayList();
		
		if(formList == null){
			throw new BusinessRuntimeException("属性值为空");
		}
		
		for(VoForm form:formList){
			StringBuffer initConditionSql = new StringBuffer();
			
			initConditionSql.append(" WHERE 1=1 ");
			Map<String, VoAttribute> map = form.getVoAttributeMap();
			Set<String> keySet = map.keySet();
			for(String key:keySet){
				VoAttribute vo = map.get(key);
				if(vo.getAlias()== "TYPE"){
					initConditionSql.append(" AND ").append("MAIN").append(".ID")
					.append("=").append(vo.getAlias()).append(".ID");
				}
				
				if(vo.getAlias()== "EXT"){
					initConditionSql.append(" AND ").append("MAIN").append(".ID")
					.append("=").append(vo.getAlias()).append(".ID");
				}
				
				if(vo.getAlias()== "PAGE"){
					initConditionSql.append(" AND ").append("MAIN").append(".ID")
					.append("=").append(vo.getAlias()).append(".ENTITY_ID");
				}
				
				if(vo.getAlias()== "STATE"){
					initConditionSql.append(" AND ").append("MAIN").append(".ID")
					.append("=").append(vo.getAlias()).append(".ENTITY_ID");
				}
				
				if(vo.getAlias()== "RELATION"){
					initConditionSql.append(" AND ").append("MAIN").append(".ID")
					.append("=").append(vo.getAlias()).append(".ENTITY_ID");
				}
			}
			
			initConditionSqlList.add(initConditionSql.toString());
		}
	}


}
