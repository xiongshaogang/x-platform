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
import com.xplatform.base.framework.core.util.StringUtil;


/**
 * A criterion representing a "like" expression
 * 
 * @author Scott Marlow
 * @author Steve Ebersole
 * AND  UPPER(F.NAME) LIKE UPPER('%${parameter.name}%')
 */
public class LikeExpression implements Criterion {
	private final String propertyName;
	private final boolean ignoreCase;//true 区分大小写 false不区分大小写
	private final String value;

	public LikeExpression(String propertyName, boolean ignoreCase,String value) {
		this.propertyName = propertyName;
		this.ignoreCase = ignoreCase;
		this.value = value;
	}

	public LikeExpression(String propertyName,String value){
		this(propertyName,false,value);
	}
	public String toSqlString() throws BusinessRuntimeException {
		
//		if(ignoreCase){
//			return propertyName +" LIKE '%${"+propertyName+"}%'";
//			
//		}else{
//			return "UPPER("+ propertyName +") LIKE UPPER('%${"+StringUtil.replace(this.propertyName, AppConstant.DOT, AppConstant.UNDERLINE)+"}%')";
//		}
		if (StringUtil.equals(propertyName, "STATE.ARTICLE_STATE")) {
			if (StringUtil.equals(value, "ALL")) {
				return propertyName +" != 'RUBBISH'";
			}else if (!StringUtil.equals(value, "ALL")) {
				return propertyName +" = '"+value+"'";
			}
		}
		return propertyName +" LIKE '%"+value+"%'";
	}
	
	public String toFrontSqlString() throws BusinessRuntimeException {
		return toSqlString();
	}
	public String toString() {
		return toSqlString();
	}
}
