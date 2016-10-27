package com.xplatform.base.framework.mybatis.engine.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;





import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.engine.query.criterion.Criterion;
import com.xplatform.base.framework.mybatis.engine.vo.VoForm;

/**
 * 
 * <STRONG>抽象的条件查询</STRONG> :  <p>
 *   
 * @version 1.0 <p>
 * @author mengfx@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : Sep 14, 2012 1:44:31 PM<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   	修改时间                     				修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Sep 14, 2012 1:44:31 PM
 *</pre>
 */
public abstract class AbstractRelationQuery<T extends RelationQuery<T, U>, U> extends AbstractFieldQuery<T, U>
		implements RelationQuery<T, U> {
	
	/**
	 * 关系查询SQL集合
	 */
	protected Map<String,String> relationsSqlMap = new HashMap<String,String> ();
	
	/**
	 * 关系查询参数
	 */
	protected List<Relation> relationList = new ArrayList();
	
	/**
	 * 设置查询参数
	 */
	public RelationQuery<T,U> relations(List<Relation> relationList) {
		
		if(relationList == null){
			throw new BusinessRuntimeException("属性为空");
		}
		
		this.relationList = relationList;
		this.buildRelations();
		return this;
	}
	
	/**
	 * 
	 * <pre>
	 * 生成条件查询SQL集合
	 * </pre>
	 */
	protected void buildRelations() {
		
		if(formList == null){
			throw new BusinessRuntimeException("属性为空");
		}
		
		for(VoForm form :formList){
			List<Criterion> formCriterionList = new ArrayList(); 
			StringBuffer relationSql = new StringBuffer();
			
			for(Relation relation:relationList){
				Map<String, List<Criterion>> criterionMap = relation.getCriterions();
				Set<String> keySet = criterionMap.keySet();
				
				for(String key: keySet){
					if(StringUtil.equals(form.getFormKey(),key)) {
						formCriterionList.addAll(criterionMap.get(key));
					}
				}
				
			}
			String relationStr = "";
			relationsSqlMap.put(form.getFormKey(),relationSql.toString());
		}
		
	}
	/**
	 * 
	 * <pre>
	 * 生成带条件查询
	 * </pre>
	 */
	protected void genderSql(){
		
		if(formList == null){
			throw new BusinessRuntimeException("属性值为空");
		}
		
		int i = 0;
		for(VoForm form :formList){
			if(i != 0){
				sql.append(" UNION ");
			}
			sql.append("SELECT ").append(fieldsSqlList.get(i)).append(" FROM ").append(tableNameSqlList.get(i))
			.append(initConditionSqlList.get(i));
			
			if(relationsSqlMap != null &&relationsSqlMap.get(form.getFormKey())!= null){
				sql.append(relationsSqlMap.get(form.getFormKey()));
			}
			
			i ++;
		}
		
		
		/*String tempSql = sql.toString();
		//替换表名
		
		tempSql = StringUtil.replace(tempSql, "EPS_ENTITY", "EPS_TP_ENTITY").replace("EPS_TP_ENTITY_PAGE", "EPS_ENTITY_PAGE")
		.replace("EPS_TP_ENTITY_STATE", "EPS_ENTITY_STATE")
		.replace("EPS_TP_ENTITY_RELATION", "EPS_ENTITY_RELATION");
		
		sql.append(" UNION ").append(tempSql);*/
	}

}
