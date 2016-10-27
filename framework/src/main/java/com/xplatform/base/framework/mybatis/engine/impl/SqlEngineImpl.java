package com.xplatform.base.framework.mybatis.engine.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.xplatform.base.framework.core.util.ReflectHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.engine.Query;
import com.xplatform.base.framework.mybatis.engine.SqlBuilder;
import com.xplatform.base.framework.mybatis.engine.SqlEngine;
import com.xplatform.base.framework.mybatis.engine.query.FormRelation;
import com.xplatform.base.framework.mybatis.engine.query.PageQuery;
import com.xplatform.base.framework.mybatis.engine.query.Relation;
import com.xplatform.base.framework.mybatis.engine.query.criterion.Criterion;
import com.xplatform.base.framework.mybatis.engine.query.criterion.Restrictions;
import com.xplatform.base.framework.mybatis.engine.query.impl.FormRelationImpl;
import com.xplatform.base.framework.mybatis.engine.query.impl.PageQueryImpl;
import com.xplatform.base.framework.mybatis.engine.vo.VoAttribute;
import com.xplatform.base.framework.mybatis.engine.vo.VoForm;

/**
 * 
 * <STRONG>SQL引擎的实现类</STRONG> :  <p>
 *   
 * @version 1.0 <p>
 * @author mengfx@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : Aug 28, 2012 9:22:23 AM<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   	修改时间                     				修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Aug 28, 2012 9:22:23 AM
 *</pre>
 */
@Component
public class SqlEngineImpl implements SqlEngine {

	/**
	 * 
	 * <pre>
	 * 通过类名创建 SqlBuilder 
	 * </pre>
	 * @param <T> 
	 * @param cls  SqlBuilder实现类
	 * @return
	 */
	public <T extends SqlBuilder> T createSqlBuilder(Class<T> cls) {
		
		if(cls == null){
			return null;
		}
		
		try {
			return cls.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	
	/**
	 * 
	 * <pre>
	 * 通过类名创建 SqlBuilder 并给表名、参数列表、条件列表 赋值
	 * </pre>
	 * @param <T>
	 * @param cls
	 * @param table 表名
	 * @param valueAttributes 参数列表
	 * @param conditionAttributes 条件列表
	 * @return
	 */
	public <T extends SqlBuilder> T createSqlBuilder(Class<T> cls, String table, Set<String> valueAttributes, Set<String> conditionAttributes) {
		
		if(cls == null){
			return null;
		}
		
		try {
			
			T builder = cls.newInstance();
			
			if(StringUtil.isNotEmpty(table)){
				ReflectHelper.invokeMethod(builder, "table", String.class, table);
			}
			
			if(valueAttributes != null && valueAttributes.size() > 0){
				ReflectHelper.invokeMethod(builder, "valueAttributes", Set.class, valueAttributes);
			}
			
			if(conditionAttributes != null && conditionAttributes.size() > 0){
				ReflectHelper.invokeMethod(builder, "conditionAttributes", Set.class, conditionAttributes);
			}
			
			return builder;
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}


	public <T extends Query> T createQuery(Class<T> cls) {
		if(cls == null){
			return null;
		}
		
		try {
			return cls.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		
//		Set<String> v = new HashSet<String>();
//		
//		v.add("id");
//		v.add("name");
//		
		SqlEngine sqlEngine = new SqlEngineImpl();
		
//		SaveSqlBuilder saveSqlBuilder = sqlEngine.createSqlBuilder(SaveSqlBuilderImpl.class);
		
//		SaveSqlBuilder saveSqlBuilder = sqlEngine.createSqlBuilder(SaveSqlBuilderImpl.class,"ssss",v,null);
//		
//		saveSqlBuilder.table("test");
//		saveSqlBuilder.valueAttributes(v);
//		
//		String sql = saveSqlBuilder.build();
//		
//		System.out.println(sql);
		
//		List<String> tables = new ArrayList<String>();
//		tables.add("eps_abc");
//		
//		List<String> columns = new ArrayList<String>();
//		columns.add("id");
//		columns.add("name");
//		columns.add("code");
//		
//		Criterion ad = Restrictions.idEq();
//		List<Criterion> criterions = new ArrayList<Criterion>();
//		criterions.add(ad);
//		SimpleExpression name = Restrictions.eq("name");
//		criterions.add(Restrictions.between("ID", "ID_1", "ID_2"));
//		criterions.add(Restrictions.like("name",true));
//		criterions.add(Restrictions.in("name","abc"));
//		
//		List<Criterion> criterions1 = new ArrayList<Criterion>();
//		criterions1.add(Restrictions.or(criterions));
//		FormQuery query = sqlEngine.createQuery(FormQueryImpl.class);
		
		/*PageQuery query = sqlEngine.createQuery(PageQueryImpl.class);
		VoForm form = new VoForm();
		form.setFormKey("FORM1");
		List<VoForm> formList = Lists.newArrayList();
		Map<String,VoAttribute > map = new HashMap<String,VoAttribute >();
		String tableName = "EPS_ENTITY";
		String alies = "MAIN";
		List<FormViewField> attributeList = Lists.newArrayList();
		attributeList.add(new FormViewField("1","ID", "ID"));
		attributeList.add(new FormViewField("2","KEY","KEY"));
		VoAttribute entityTableAttribute = new VoAttribute(tableName,alies,attributeList);
		
		map.put("MAIN", entityTableAttribute);
		
		tableName = "EPS_ENTITY_TYPE";
		alies = "TYPE";
		attributeList = Lists.newArrayList();
		attributeList.add(new FormViewField("4","CODE", "CODE"));
		VoAttribute typeTableAttribute = new VoAttribute(tableName,alies,attributeList);
		
		map.put("TYPE", typeTableAttribute);
		
		tableName = "EPS_ENTITY_TYPE_EXT";
		alies = "EXT";
		attributeList = Lists.newArrayList();
		attributeList.add(new FormViewField("5","NAME", "NAME"));
		VoAttribute extTableAttribute = new VoAttribute(tableName,alies,attributeList);
		
		map.put("EXT", extTableAttribute);
		form.setVoAttributeMap(map);
		
		formList.add(form);
		
		
		form = new VoForm();
		form.setFormKey("FORM2");
		map = new HashMap<String,VoAttribute >();
		tableName = "EPS_ENTITY";
		alies = "MAIN";
		attributeList = Lists.newArrayList();
		attributeList.add(new FormViewField("1","ID", "ID"));
		attributeList.add(new FormViewField("2","KEY","KEY"));
		entityTableAttribute = new VoAttribute(tableName,alies,attributeList);
		
		map.put("MAIN", entityTableAttribute);
		
		tableName = "EPS_ENTITY_TYPE";
		alies = "TYPE";
		attributeList = Lists.newArrayList();
		attributeList.add(new FormViewField("4","CODE", "CODE"));
		typeTableAttribute = new VoAttribute(tableName,alies,attributeList);
		
		map.put("TYPE", typeTableAttribute);
		
		tableName = "EPS_ENTITY_TYPE_EXT";
		alies = "EXT";
		attributeList = Lists.newArrayList();
		attributeList.add(new FormViewField("5","NAME", "NAME"));
		extTableAttribute = new VoAttribute(tableName,alies,attributeList);
		
//		map.put("EXT", extTableAttribute);
		form.setVoAttributeMap(map);
		formList.add(form);
		
		List<FormViewField> commAttributeList = Lists.newArrayList();
		commAttributeList.add(new FormViewField("1","ID", "ID"));
		commAttributeList.add(new FormViewField("2","KEY","KEY"));
		commAttributeList.add(new FormViewField("3","AGE","AGE"));
		commAttributeList.add(new FormViewField("5","NAME", "NAME"));
		commAttributeList.add(new FormViewField("4","CODE", "CODE"));
		commAttributeList.add(new FormViewField("7","CODE", "CODE1"));
		
		query.fields(formList, commAttributeList);
		
		List<Relation> relationList = Lists.newArrayList();
		FormRelation formRelation = new FormRelationImpl();
		Map<String,List<Criterion>> criterionMap = new HashMap<String,List<Criterion>> ();
		List<Criterion> criterionList = Lists.newArrayList();
		criterionList.add(Restrictions.eq("MAIN.KEY"));
		criterionMap.put("FORM1", criterionList);
//		criterionMap.put("FORM2", criterionList);
		formRelation.criterions(criterionMap);
		relationList.add(formRelation);
		query.relations(relationList);
		
		query.values(new HashMap<String,String>());
		
		Page page = new Page();
		page.setOrderBy("ID");
		page.setOrder("ASC");
		
		query.page(page);*/
		
		
	}	
}
