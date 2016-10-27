package com.xplatform.base.framework.mybatis.utils;

import com.xplatform.base.framework.mybatis.entity.Page;

/**
 * 
 * description :分页sql封装
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月17日 下午12:14:43
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月17日 下午12:14:43
 *
 */
public class PageSqlUtil {
	/**
	 * 
	 * <pre>
	 * 产生分页SQL(如果pageSize传0,就不进行分页,获得全部数据)
	 * </pre>
	 * @param dialect
	 * @param sql
	 * @param page
	 * @return
	 */
	public static String generatePageSql(String dialect, String sql, Page page) {
		if (page != null && dialect != null && !"".equals(dialect.trim()) && page.getPageSize() != 0) {
			StringBuffer pageSql = new StringBuffer();
			int pageSize = page.getPageSize();
			int first = page.getFirst();
			int last = page.getLast();
			if ("mysql".equals(dialect)) {
				pageSql.append(sql);
				pageSql.append(" limit " + (first - 1) + "," + pageSize);
			} else if ("oracle".equals(dialect)) {
				pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
				pageSql.append(sql);
				pageSql.append(") tmp_tb where ROWNUM<=");
				pageSql.append(last);
				pageSql.append(") where row_id>=");
				pageSql.append(first);
			} else if ("sqlserver".equals(dialect)) {
				sql = sql.toUpperCase().replaceFirst("SELECT", "SELECT top " + (first - 1 + pageSize) + " 0 __tc__,");
				pageSql.append("select * from (select row_number()over(order by __tc__)__rn__,* from (");
				pageSql.append(sql);
				pageSql.append(")AA)BB where __rn__>" + (first - 1));
			}
			return pageSql.toString();
		} else {
			return sql;
		}
	}
}
