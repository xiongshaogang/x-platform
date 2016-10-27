package com.xplatform.base.framework.core.extend.hqlsearch.parse.impl;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.extend.hqlsearch.parse.IHqlParse;

public class ShortParseImpl implements IHqlParse {

	
	public void addCriteria(CriteriaQuery cq, String name, Object value) {
		if (HqlGenerateUtil.isNotEmpty(value))
			cq.eq(name, value);
	}

	
	public void addCriteria(CriteriaQuery cq, String name, Object value,
			String beginValue, String endValue) {
		if (HqlGenerateUtil.isNotEmpty(beginValue)) {
			cq.ge(name, Short.parseShort(beginValue));
		}
		if (HqlGenerateUtil.isNotEmpty(endValue)) {
			cq.le(name, Short.parseShort(endValue));
		}
		if (HqlGenerateUtil.isNotEmpty(value)) {
			cq.eq(name, value);
		}
	}

}
