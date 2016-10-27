package com.xplatform.base.framework.mybatis.engine.builder.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;
import com.xplatform.base.framework.core.constant.AppConstant;
import com.xplatform.base.framework.mybatis.engine.builder.AbstractSqlBuilder;
import com.xplatform.base.framework.mybatis.engine.builder.UpdateSqlBuilder;

/**
 * 
 * <STRONG>类描述</STRONG> :
 * <p>
 * 
 * @version 1.0
 *          <p>
 * @author mengfx@huilan.com
 *         <p>
 * 
 * <STRONG>创建时间</STRONG> : Aug 27, 2012 1:32:13 PM
 * <p>
 * <STRONG>修改历史</STRONG> :
 * <p>
 * 
 * <pre>
 * 修改人                   	修改时间                     				修改内容
 * ---------------         -------------------         -----------------------------------
 * mengfx@huilan.com        Aug 27, 2012 1:32:13 PM
 * </pre>
 */
public class UpdateSqlBuilderImpl extends AbstractSqlBuilder implements
		UpdateSqlBuilder {

	/**
	 * 日志
	 */
	protected Logger logger = LoggerFactory.getLogger(UpdateSqlBuilderImpl.class);

	public String build() throws BusinessRuntimeException {

		if (this.buildChecked()) {
			this.buildUpdate();
			return this.sql.toString();
		}
		return null;
	}
	
	/**
	 * 
	 * <pre>
	 * 产生更新语句
	 * </pre>
	 */
	private void buildUpdate() {

		sql.append(UPDATE).append(table.toUpperCase());
		sql.append(SET);

		int i = 0;

		for (String name : valueAttributes) {

			if (i != 0) {
				sql.append(AppConstant.COMMA);
			}

			sql.append(name).append(EQUAL_SIGN);
			sql.append(WELL_SIGN).append(L_BRACKET).append(name).append(AppConstant.COMMA).append("jdbcType=VARCHAR").append(R_BRACKET);

			i++;
		}

		sql.append(WHERE);

		i = 0;

		for (String name : conditionAttributes) {

			if (i != 0) {
				sql.append(AND);
			}

			sql.append(name).append(EQUAL_SIGN);
			sql.append(WELL_SIGN).append(L_BRACKET).append(name).append(R_BRACKET);
			i++;

		}
	}

	protected boolean buildChecked() {

		if (checkTableName() && checkValueAttr() && checkConditionAttr()) {
			return true;
		}
		return false;

	}

}
