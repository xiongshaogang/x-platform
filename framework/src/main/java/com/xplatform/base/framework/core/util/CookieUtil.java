package com.xplatform.base.framework.core.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * description :cookie操作
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月19日 下午8:15:38
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月19日 下午8:15:38
 *
 */
public class CookieUtil {

	/**
	 * 获取所有的cookie
	 * @param request
	 * @return
	 */
	public static List<Cookie> getAllCookie(HttpServletRequest request) {
		// 获取request里面的cookie cookie里面存值方式也是 键值对的方式
		List<Cookie> list=new ArrayList<Cookie>();
		Cookie[] cookie = request.getCookies();
		for (int i = 0; i < cookie.length; i++) {
			Cookie cook = cookie[i];
			list.add(cook);
		}
		return list;
	}
	
	/**
	 * 通过cookie名称获取cookie的value
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getValueByName(HttpServletRequest request,String name){
		List<Cookie> list=getAllCookie(request);
		if(list.size()<=0){return null;}
		for(Cookie cookie:list){
			if(StringUtil.equals(cookie.getName(), name)){
				return cookie.getValue();
			}
		}
		return null;
	}

}
