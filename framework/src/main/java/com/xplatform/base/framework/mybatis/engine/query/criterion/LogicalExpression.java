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


import java.util.List;

import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;


/**
 * Superclass of binary logical expressions
 * @author Gavin King
 */
public class LogicalExpression implements Criterion {

	private final List<Criterion >criterions;
	private final String op;

	protected LogicalExpression(List<Criterion >criterions,String op) {
		this.criterions = criterions;
		this.op = op;
	}


	public String toSqlString() throws BusinessRuntimeException {
		String result = "";
		if(this.criterions == null || this.criterions.size()== 0){
			return null;
		}
		
		int i = 0;
		result += "(";
		for(Criterion criterion:criterions){
			if(i != 0){
				result += " " + this.op +" ";
			}
			result += criterion.toSqlString();
			i ++;
		}
		result += ")";
		return result;
	}
	public String toFrontSqlString() throws BusinessRuntimeException {
		return toSqlString();
	}
	public String getOp() {
		return op;
	}


}
