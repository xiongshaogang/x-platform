package com.xplatform.base.develop.codegenerate.service.impl.config.util;

import java.util.Comparator;

import com.xplatform.base.develop.metadata.entity.MetaDataFieldEntity;


/**
 * 字段 顺序  排序
 * @author jueyue
 *  2013年7月6日
 */
public class FieldNumComparator implements Comparator<MetaDataFieldEntity> {

	
	public int compare(MetaDataFieldEntity o1, MetaDataFieldEntity o2) {
		if (o1 == null || o1.getOrderNum() == null || o2 == null || o2.getOrderNum() == null)
			return -1;
		Integer thisVal = o1.getOrderNum();
		Integer anotherVal = o2.getOrderNum();
		return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
	}

}
