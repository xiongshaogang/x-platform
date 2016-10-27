package com.xplatform.base.platform.common.utils;

import java.util.Comparator;

import com.xplatform.base.orgnaization.module.mybatis.vo.ModuleTreeVo;

/**
* @ClassName: NumberComparator 
* @Description: TODO(字符串比较器) 
* @author jeecg 
* @date 2013-1-31 下午06:18:35 
*
 */
public class NumberComparator implements Comparator<Object> {
	private boolean ignoreCase = true;

	public NumberComparator() {
	}

	public NumberComparator(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public int compare(Object obj1, Object obj2) {
		int o1 = 0;
		int o2 = 0;
		if (ignoreCase) {
			ModuleTreeVo c1 = (ModuleTreeVo) obj1;
			ModuleTreeVo c2 = (ModuleTreeVo) obj2;
			o1 = c1.getOrderby();
			o2 = c2.getOrderby();
			return o1-o2;
		}
		return 0;
	}

	private int getNumber(String str) {
		int num = Integer.MAX_VALUE;
		int bits = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
				bits++;
			} else {
				break;
			}
		}
		if (bits > 0) {
			num = Integer.parseInt(str.substring(0, bits));
		}
		return num;
	}
}
