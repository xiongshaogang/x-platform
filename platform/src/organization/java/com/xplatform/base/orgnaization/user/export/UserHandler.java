package com.xplatform.base.orgnaization.user.export;

import com.xplatform.base.poi.handler.impl.ExcelDataHandlerDefaultImpl;

public class UserHandler extends ExcelDataHandlerDefaultImpl {

	@Override
	public Object exportHandler(Object obj, String name, Object value) {
		if (name.equals("userName")) {
			return String.valueOf(value) + "处理类处理用户名";
		} else if (name.equals("错误次数")) {
			return String.valueOf(value) + "次";
		}
		return super.exportHandler(obj, name, value);
	}

	@Override
	public Object importHandler(Object obj, String name, Object value) {
		if (name.equals("用户名")) {
			return String.valueOf(value) + "人名";
		} else if (name.equals("locked")) {
			if (value == null) {
				return "100";
			}else if(value!=null&&Integer.valueOf(value.toString()).equals(2)){
				return "4";
			}
		}
		return super.importHandler(obj, name, value);
	}

}
