package com.xplatform.base.framework.mybatis.engine.builder.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;
import com.xplatform.base.framework.core.constant.AppConstant;
import com.xplatform.base.framework.mybatis.engine.builder.AbstractSqlBuilder;
import com.xplatform.base.framework.mybatis.engine.builder.SaveSqlBuilder;

public class SaveSqlBuilderImpl extends AbstractSqlBuilder implements
		SaveSqlBuilder {

	/**
	 * 日志
	 */
	protected Logger logger = LoggerFactory.getLogger(SaveSqlBuilderImpl.class);

	public String build() throws BusinessRuntimeException {

		if (this.buildChecked()) {
			this.buildInsert();

			return sql.toString();
		}
		return null;
	}

	/**
	 * 
	 * <pre>
	 * 产生插入语句
	 * </pre>
	 */
	private void buildInsert() {

		sql.append(INSERT_INTO );

		sql.append(table.toUpperCase());

		sql.append(AppConstant.L_BRACKET);

		int i = 0;

		for (String name : valueAttributes) {

			if (i != 0) {
				sql.append(AppConstant.COMMA);
			}

			sql.append(name);

			i++;
		}

		sql.append(AppConstant.R_BRACKET);

		sql.append(VALUES).append(AppConstant.L_BRACKET);

		i = 0;

		for (String name : valueAttributes) {

			if (i != 0) {
				sql.append(AppConstant.COMMA);
			}

			sql.append(WELL_SIGN).append(L_BRACKET).append(name).append(AppConstant.COMMA).append("jdbcType=VARCHAR").append(R_BRACKET);
			i++;
		}

		sql.append(AppConstant.R_BRACKET);
	}

	protected boolean buildChecked() {

		if(checkTableName()&&checkValueAttr()){
			return true;
		}
		return false;
	}

}
