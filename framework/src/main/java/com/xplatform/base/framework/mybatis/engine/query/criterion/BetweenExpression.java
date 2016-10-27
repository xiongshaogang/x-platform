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


import sun.awt.AppContext;

import com.xplatform.base.framework.core.common.exception.BusinessRuntimeException;
import com.xplatform.base.framework.core.constant.AppConstant;
import com.xplatform.base.framework.core.util.StringUtil;

/**
 * Constrains a property to between two values
 * @author Gavin King
 */
@SuppressWarnings("serial")
public class BetweenExpression implements Criterion {

	private final String propertyName;
	private final String lo;
	private final String hi;

	protected BetweenExpression(String propertyName, String lo, String hi) {
		this.propertyName = propertyName;
		this.lo = lo;
		this.hi = hi;
	}

	public String toSqlString()throws BusinessRuntimeException{
		return propertyName + " BETWEEN #{" + StringUtil.replace(lo, AppConstant.DOT, AppConstant.UNDERLINE) + "} AND #{" + StringUtil.replace(hi, AppConstant.DOT, AppConstant.UNDERLINE)+"}";

		//TODO: get SQL rendering out of this package!
	}
	public String toFrontSqlString() throws BusinessRuntimeException {
		String loStr = "parameter."+lo;
		String hiStr = "parameter."+hi;
		return propertyName + " BETWEEN #{" + loStr + "} AND #{" + hiStr+"}";
	}

	public String toString() {
		int index1 = StringUtil.indexOf(lo, AppConstant.DOT);
		int index2 = StringUtil.indexOf(hi, AppConstant.DOT);
		String loStr = "parameter"+StringUtil.substring(lo, index1);
		String hiStr = "parameter"+StringUtil.substring(hi, index2);
		return propertyName + " BETWEEN #{" + loStr + "} AND #{" + hiStr+"}";
	}



}
