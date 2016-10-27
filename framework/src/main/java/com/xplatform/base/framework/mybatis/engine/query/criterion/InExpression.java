/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
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
 */
package com.xplatform.base.framework.mybatis.engine.query.criterion;

import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;


/**
 * Constrains the property to a specified list of values
 * @author Gavin King
 */
public class InExpression implements Criterion {

	private final String propertyName;
	private final boolean ignoreCase;//true 区分大小写 false不区分大小写
	private final String values;
	public InExpression(String propertyName,boolean ignoreCase,String values){
		this.propertyName = propertyName;
		this.ignoreCase = ignoreCase;
		this.values = values;
	}
	public InExpression(String propertyName,String values){
		this(propertyName,false,values);
	}
	public String toSqlString() throws BusinessRuntimeException {
		
		StringBuffer result = new StringBuffer();
		result.append(this.propertyName).append(" IN (");
		result.append(values);
		//result.append("${").append(Strings.replace(this.propertyName, AppConstant.DOT, AppConstant.UNDERLINE)).append("}");
		result.append(")");
		return result.toString();
	}
	public String toFrontSqlString() throws BusinessRuntimeException {
		return toSqlString();
	}
	public String toString() {
		return toSqlString();
	}
}
