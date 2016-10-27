/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
package com.xplatform.base.framework.mybatis.engine.query.criterion;

import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;
import com.xplatform.base.framework.core.constant.AppConstant;
import com.xplatform.base.framework.core.util.StringUtil;



/**
 * superclass for "simple" comparisons (with SQL binary operators)
 * @author Gavin King
 */
public class SimpleExpression implements Criterion {

	private final String propertyName;
	private boolean ignoreCase;
	private final String op;

	protected SimpleExpression(String propertyName, String op) {
		this.propertyName = propertyName;
		this.op = op;
	}

	protected SimpleExpression(String propertyName, String op, boolean ignoreCase) {
		this.propertyName = propertyName;
		this.ignoreCase = ignoreCase;
		this.op = op;
	}

	public SimpleExpression ignoreCase() {
		ignoreCase = true;
		return this;
	}

	public String toSqlString()throws BusinessRuntimeException{
		
		return this.propertyName + this.op + "#{" +StringUtil.replace(this.propertyName, AppConstant.DOT, AppConstant.UNDERLINE)+"}";
	}
	public String toFrontSqlString() throws BusinessRuntimeException {
		int index = StringUtil.indexOf(propertyName, AppConstant.DOT);
		String str = "parameter"+StringUtil.substring(propertyName, index);
		return this.propertyName + this.op + "#{" +str+"}";
	}
	
	public String toString() {
		return toSqlString();
	}

	protected final String getOp() {
		return op;
	}

}
