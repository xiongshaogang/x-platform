package com.xplatform.base.framework.mybatis.engine.builder.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;
import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;
import com.xplatform.base.framework.mybatis.engine.builder.AbstractSqlBuilder;
import com.xplatform.base.framework.mybatis.engine.builder.DeleteSqlBuilder;

public class DeleteSqlBuilderImpl extends AbstractSqlBuilder implements
		DeleteSqlBuilder {

	/**
	 * 日志
	 */
	protected Logger logger = LoggerFactory.getLogger(DeleteSqlBuilderImpl.class);

	public String build() throws BusinessRuntimeException {

		if (this.buildChecked()) {
			this.buildDelete();
			return sql.toString();
		}
		return null;
	}

	/**
	 * 
	 * <pre>
	 * 产生删除语句
	 * </pre>
	 */
	private void buildDelete() {

		sql.append(DELETE).append(FROM).append(table.toUpperCase());
		sql.append(WHERE);

		int i = 0;

		for (String name : conditionAttributes) {

			if (i != 0) {
				sql.append(AND);
			}

			sql.append(name).append(EQUAL_SIGN);
			sql.append(WELL_SIGN).append(L_BRACKET).append(name).append(R_BRACKET);
			i++;
		}

	}

	protected boolean buildChecked() throws BusinessRuntimeException {

		if (checkTableName() && checkConditionAttr()) {
			return true;
		}
		return false;
	}

}
