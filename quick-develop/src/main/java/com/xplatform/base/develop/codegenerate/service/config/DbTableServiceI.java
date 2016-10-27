package com.xplatform.base.develop.codegenerate.service.config;

import org.springframework.jdbc.core.JdbcTemplate;

import com.xplatform.base.develop.metadata.entity.MetaDataEntity;




/**
 * 表的操作
 * @author jueyue
 *
 */
public interface DbTableServiceI {
	
	/**
	 * 创建表
	 * @param tableProperty
	 * @return SQL
	 */
	String createTableSQL(MetaDataEntity tableProperty);
	
	/**
	 * 删除表
	 * @param tableProperty
	 * @return SQL
	 */
	String dropTableSQL(MetaDataEntity tableProperty);
	/**
	 * 判断表格是否存在
	 * @param tableName
	 * @return SQL
	 */
	String createIsExitSql(String tableName);
	/**
	 * 更新表
	 * @param cgFormHead
	 * @param jdbcTemplate 
	 * @return SQL
	 */
	String updateTableSQL(MetaDataEntity cgFormHead, JdbcTemplate jdbcTemplate);

}
