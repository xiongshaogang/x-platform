package com.xplatform.base.develop.codegenerate.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.LogFactory;
import com.xplatform.base.develop.codegenerate.pojo.Columnt;
import com.xplatform.base.develop.codegenerate.pojo.TableConvert;
import com.xplatform.base.develop.codegenerate.util.CodeResourceUtil;
import com.xplatform.base.develop.codegenerate.util.CodeStringUtils;

/**
 * 
 * @author xiehs
 *
 */
public class UcgReadTable {
	private static final Log log = LogFactory.getLog(UcgReadTable.class);
	private static final long serialVersionUID = -5324160085184088010L;
	private Connection conn;
	private Statement stmt;
	private String sql;
	private ResultSet rs;

	public static void main(String[] args) throws SQLException {
		try {
			// 传递一个表，得多所有的列
			List<Columnt> cls = new UcgReadTable()
					.readTableColumn("t_s_user");
			for (Columnt c : cls) {
				System.out.println(c.getFieldName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(ArrayUtils.toString(new UcgReadTable()
				.readAllTableNames()));
	}

	/**
	 * 读取所有的表名
	 * @return
	 * @throws SQLException
	 */
	public List<String> readAllTableNames() throws SQLException {
		List<String> tableNames = new ArrayList<String>(0);
		try {
			Class.forName(CodeResourceUtil.DIVER_NAME);
			System.out.print(CodeResourceUtil.PASSWORD);
			this.conn = DriverManager.getConnection(CodeResourceUtil.URL,
					CodeResourceUtil.USERNAME, CodeResourceUtil.PASSWORD);
			this.stmt = this.conn.createStatement(1005, 1007);
			if (CodeResourceUtil.DATABASE_TYPE.equals("mysql")) {
				this.sql = MessageFormat
						.format(
								"select distinct table_name from information_schema.columns where table_schema = {0}",
								new Object[] { TableConvert
										.getV(CodeResourceUtil.DATABASE_NAME) });
			}
			if (CodeResourceUtil.DATABASE_TYPE.equals("oracle")) {
				this.sql = " select distinct colstable.table_name as  table_name from user_tab_cols colstable";
			}

			if (CodeResourceUtil.DATABASE_TYPE.equals("postgresql")) {
				this.sql = "SELECT distinct c.relname AS  table_name FROM pg_class c";
			}

			if (CodeResourceUtil.DATABASE_TYPE.equals("sqlserver")) {
				this.sql = "select distinct c.name as  table_name from sys.objects c ";
			}

			this.rs = this.stmt.executeQuery(this.sql);
			while (this.rs.next()) {
				String tableName = this.rs.getString(1);
				tableNames.add(tableName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (this.stmt != null) {
					this.stmt.close();
					this.stmt = null;
					System.gc();
				}
				if (this.conn != null) {
					this.conn.close();
					this.conn = null;
					System.gc();
				}
			} catch (SQLException e) {
				throw e;
			}
		}
		return tableNames;
	}

	/**
	 * 读取所有的列的信息
	 * @param paramString
	 * @return
	 */
	public List<Columnt> readTableColumn(String paramString) {
		List<Columnt> localArrayList1 = new ArrayList<Columnt>();
		Columnt localColumnt1;
		try {
			Class.forName(CodeResourceUtil.DIVER_NAME);
			this.conn = DriverManager.getConnection(CodeResourceUtil.URL,
					CodeResourceUtil.USERNAME, CodeResourceUtil.PASSWORD);
			this.stmt = this.conn.createStatement(1005, 1007);
			if (CodeResourceUtil.DATABASE_TYPE.equals("mysql"))
				this.sql = MessageFormat
						.format(
								"select column_name,data_type,column_comment,numeric_precision,numeric_scale,character_maximum_length,is_nullable nullable from information_schema.columns where table_name = {0} and table_schema = {1}",
								new Object[] {
										TableConvert.getV(paramString
												.toUpperCase()),
										TableConvert
												.getV(CodeResourceUtil.DATABASE_NAME) });
			if (CodeResourceUtil.DATABASE_TYPE.equals("oracle"))
				this.sql = MessageFormat
						.format(
								" select colstable.column_name column_name, colstable.data_type data_type, commentstable.comments column_comment, colstable.Data_Precision column_precision, colstable.Data_Scale column_scale,colstable.Char_Length,colstable.nullable from user_tab_cols colstable  inner join user_col_comments commentstable  on colstable.column_name = commentstable.column_name  where colstable.table_name = commentstable.table_name  and colstable.table_name = {0}",
								new Object[] { TableConvert.getV(paramString
										.toUpperCase()) });
			if (CodeResourceUtil.DATABASE_TYPE.equals("postgresql"))
				this.sql = MessageFormat
						.format(
								"SELECT a.attname AS  field,t.typname AS type,col_description(a.attrelid,a.attnum) as comment,null as column_precision,null as column_scale,null as Char_Length,a.attnotnull  FROM pg_class c,pg_attribute  a,pg_type t  WHERE c.relname = {0} and a.attnum > 0  and a.attrelid = c.oid and a.atttypid = t.oid  ORDER BY a.attnum ",
								new Object[] { TableConvert.getV(paramString
										.toLowerCase()) });
			if (CodeResourceUtil.DATABASE_TYPE.equals("sqlserver"))
				this.sql = MessageFormat
						.format(
								"select cast(a.name as varchar(50)) column_name,  cast(b.name as varchar(50)) data_type,  cast(e.value as varchar(200)) comment,  cast(ColumnProperty(a.object_id,a.Name,'''Precision''') as int) num_precision,  cast(ColumnProperty(a.object_id,a.Name,'''Scale''') as int) num_scale,  a.max_length,  (case when a.is_nullable=1 then '''y''' else '''n''' end) nullable   from sys.columns a left join sys.types b on a.user_type_id=b.user_type_id left join sys.objects c on a.object_id=c.object_id and c.type='''U''' left join sys.extended_properties e on e.major_id=c.object_id and e.minor_id=a.column_id and e.class=1 where c.name={0}",
								new Object[] { TableConvert.getV(paramString
										.toLowerCase()) });
			this.rs = this.stmt.executeQuery(this.sql);
			this.rs.last();
			int i = this.rs.getRow();
			if (i > 0) {
				localColumnt1 = new Columnt();
				if (CodeResourceUtil.JEECG_FILED_CONVERT)
					localColumnt1.setFieldName(formatField(this.rs.getString(1)
							.toLowerCase()));
				else
					localColumnt1.setFieldName(this.rs.getString(1)
							.toLowerCase());
				localColumnt1
						.setFieldDbName(this.rs.getString(1).toUpperCase());
				localColumnt1.setFieldType(formatField(this.rs.getString(2)
						.toLowerCase()));
				localColumnt1.setPrecision(this.rs.getString(4));
				localColumnt1.setScale(this.rs.getString(5));
				localColumnt1.setCharmaxLength(this.rs.getString(6));
				localColumnt1.setNullable(TableConvert.getNullAble(this.rs
						.getString(7)));
				formatFieldClassType(localColumnt1);
				localColumnt1.setFiledComment(StringUtils.isBlank(this.rs
						.getString(3)) ? localColumnt1.getFieldName() : this.rs
						.getString(3));
				String[] arrayOfString = new String[0];
				if (CodeResourceUtil.JEECG_GENERATE_UI_FILTER_FIELDS != null)
					arrayOfString = CodeResourceUtil.JEECG_GENERATE_UI_FILTER_FIELDS
							.toLowerCase().split(",");
				if ((!CodeResourceUtil.JEECG_GENERATE_TABLE_ID
						.equals(localColumnt1.getFieldName()))
						&& (!CodeStringUtils.isIn(localColumnt1
								.getFieldDbName().toLowerCase(), arrayOfString)))
					localArrayList1.add(localColumnt1);
				while (this.rs.previous()) {
					Columnt localColumnt2 = new Columnt();
					if (CodeResourceUtil.JEECG_FILED_CONVERT){
						localColumnt2.setFieldName(formatField(this.rs
								.getString(1).toLowerCase()));
					}else{
						localColumnt2.setFieldName(this.rs.getString(1)
								.toLowerCase());
					}
					localColumnt2.setFieldDbName(this.rs.getString(1)
							.toUpperCase());
					if ((CodeResourceUtil.JEECG_GENERATE_TABLE_ID
							.equals(localColumnt2.getFieldName()))
							|| (CodeStringUtils.isIn(localColumnt2
									.getFieldDbName().toLowerCase(),
									arrayOfString))){
						continue;
					}
					localColumnt2.setFieldType(formatField(this.rs.getString(2).toLowerCase()));
					localColumnt2.setPrecision(this.rs.getString(4));
					localColumnt2.setScale(this.rs.getString(5));
					localColumnt2.setCharmaxLength(this.rs.getString(6));
					localColumnt2.setNullable(TableConvert.getNullAble(this.rs.getString(7)));
					formatFieldClassType(localColumnt2);
					localColumnt2.setFiledComment(StringUtils.isBlank(this.rs
							.getString(3)) ? localColumnt2.getFieldName()
							: this.rs.getString(3));
					localArrayList1.add(localColumnt2);
				}
			} else {
				throw new RuntimeException("该表不存在或者表中没有字段");
			}
		} catch (ClassNotFoundException localClassNotFoundException) {
			// throw localClassNotFoundException;
		} catch (SQLException localSQLException1) {
			// throw localSQLException1;
		} finally {
			try {
				if (this.stmt != null) {
					this.stmt.close();
					this.stmt = null;
					System.gc();
				}
				if (this.conn != null) {
					this.conn.close();
					this.conn = null;
					System.gc();
				}
			} catch (SQLException localSQLException2) {
				// throw localSQLException2;
			}
		}
		ArrayList<Columnt> localArrayList2 = new ArrayList<Columnt>();
		for (int j = localArrayList1.size() - 1; j >= 0; j--) {
			localColumnt1 = (Columnt) localArrayList1.get(j);
			localArrayList2.add(localColumnt1);
		}
		return localArrayList2;
	}

	/**
	 * 得到数据库中字段的原始列信息
	 * @param paramString
	 * @return
	 */
	public List<Columnt> readOriginalTableColumn(String paramString) {
		ArrayList<Columnt> localArrayList1 = new ArrayList<Columnt>();
		Columnt localColumnt1;
		try {
			Class.forName(CodeResourceUtil.DIVER_NAME);
			this.conn = DriverManager.getConnection(CodeResourceUtil.URL,
					CodeResourceUtil.USERNAME, CodeResourceUtil.PASSWORD);
			this.stmt = this.conn.createStatement(1005, 1007);
			if (CodeResourceUtil.DATABASE_TYPE.equals("mysql"))
				this.sql = MessageFormat
						.format(
								"select column_name,data_type,column_comment,numeric_precision,numeric_scale,character_maximum_length,is_nullable nullable from information_schema.columns where table_name = {0} and table_schema = {1}",
								new Object[] {
										TableConvert.getV(paramString
												.toUpperCase()),
										TableConvert
												.getV(CodeResourceUtil.DATABASE_NAME) });
			if (CodeResourceUtil.DATABASE_TYPE.equals("oracle"))
				this.sql = MessageFormat
						.format(
								" select colstable.column_name column_name, colstable.data_type data_type, commentstable.comments column_comment, colstable.Data_Precision column_precision, colstable.Data_Scale column_scale,colstable.Char_Length,colstable.nullable from user_tab_cols colstable  inner join user_col_comments commentstable  on colstable.column_name = commentstable.column_name  where colstable.table_name = commentstable.table_name  and colstable.table_name = {0}",
								new Object[] { TableConvert.getV(paramString
										.toUpperCase()) });
			if (CodeResourceUtil.DATABASE_TYPE.equals("postgresql"))
				this.sql = MessageFormat
						.format(
								"SELECT a.attname AS  field,t.typname AS type,col_description(a.attrelid,a.attnum) as comment,null as column_precision,null as column_scale,null as Char_Length,a.attnotnull  FROM pg_class c,pg_attribute  a,pg_type t  WHERE c.relname = {0} and a.attnum > 0  and a.attrelid = c.oid and a.atttypid = t.oid  ORDER BY a.attnum ",
								new Object[] { TableConvert.getV(paramString
										.toLowerCase()) });
			if (CodeResourceUtil.DATABASE_TYPE.equals("sqlserver"))
				this.sql = MessageFormat
						.format(
								"select cast(a.name as varchar(50)) column_name,  cast(b.name as varchar(50)) data_type,  cast(e.value as varchar(200)) comment,  cast(ColumnProperty(a.object_id,a.Name,'''Precision''') as int) num_precision,  cast(ColumnProperty(a.object_id,a.Name,'''Scale''') as int) num_scale,  a.max_length,  (case when a.is_nullable=1 then '''y''' else '''n''' end) nullable   from sys.columns a left join sys.types b on a.user_type_id=b.user_type_id left join sys.objects c on a.object_id=c.object_id and c.type='''U''' left join sys.extended_properties e on e.major_id=c.object_id and e.minor_id=a.column_id and e.class=1 where c.name={0}",
								new Object[] { TableConvert.getV(paramString
										.toLowerCase()) });
			this.rs = this.stmt.executeQuery(this.sql);
			this.rs.last();
			int i = this.rs.getRow();
			if (i > 0) {
				localColumnt1 = new Columnt();
				if (CodeResourceUtil.JEECG_FILED_CONVERT)
					localColumnt1.setFieldName(formatField(this.rs.getString(1)
							.toLowerCase()));
				else
					localColumnt1.setFieldName(this.rs.getString(1)
							.toLowerCase());
				localColumnt1
						.setFieldDbName(this.rs.getString(1).toUpperCase());
				localColumnt1.setPrecision(TableConvert.getNullString(this.rs
						.getString(4)));
				localColumnt1.setScale(TableConvert.getNullString(this.rs
						.getString(5)));
				localColumnt1.setCharmaxLength(TableConvert
						.getNullString(this.rs.getString(6)));
				localColumnt1.setNullable(TableConvert.getNullAble(this.rs
						.getString(7)));
				localColumnt1.setFieldType(formatDataType(this.rs.getString(2)
						.toLowerCase(), localColumnt1.getPrecision(),
						localColumnt1.getScale()));
				formatFieldClassType(localColumnt1);
				localColumnt1.setFiledComment(StringUtils.isBlank(this.rs
						.getString(3)) ? localColumnt1.getFieldName() : this.rs
						.getString(3));
				localArrayList1.add(localColumnt1);
				while (this.rs.previous()) {
					Columnt localColumnt2 = new Columnt();
					if (CodeResourceUtil.JEECG_FILED_CONVERT)
						localColumnt2.setFieldName(formatField(this.rs
								.getString(1).toLowerCase()));
					else
						localColumnt2.setFieldName(this.rs.getString(1)
								.toLowerCase());
					localColumnt2.setFieldDbName(this.rs.getString(1)
							.toUpperCase());
					localColumnt2.setPrecision(TableConvert
							.getNullString(this.rs.getString(4)));
					localColumnt2.setScale(TableConvert.getNullString(this.rs
							.getString(5)));
					localColumnt2.setCharmaxLength(TableConvert
							.getNullString(this.rs.getString(6)));
					localColumnt2.setNullable(TableConvert.getNullAble(this.rs
							.getString(7)));
					localColumnt2.setFieldType(formatDataType(this.rs
							.getString(2).toLowerCase(), localColumnt2
							.getPrecision(), localColumnt2.getScale()));
					formatFieldClassType(localColumnt2);
					localColumnt2.setFiledComment(StringUtils.isBlank(this.rs
							.getString(3)) ? localColumnt2.getFieldName()
							: this.rs.getString(3));
					localArrayList1.add(localColumnt2);
				}
			} else {
				throw new RuntimeException("该表不存在或者表中没有字段");
			}
		} catch (ClassNotFoundException localClassNotFoundException) {
			// throw localClassNotFoundException;
		} catch (SQLException localSQLException1) {
			// throw localSQLException1;
		} finally {
			try {
				if (this.stmt != null) {
					this.stmt.close();
					this.stmt = null;
					System.gc();
				}
				if (this.conn != null) {
					this.conn.close();
					this.conn = null;
					System.gc();
				}
			} catch (SQLException localSQLException2) {
				// throw localSQLException2;
			}
		}
		ArrayList<Columnt> localArrayList2 = new ArrayList<Columnt>();
		for (int j = localArrayList1.size() - 1; j >= 0; j--) {
			localColumnt1 = (Columnt) localArrayList1.get(j);
			localArrayList2.add(localColumnt1);
		}
		return localArrayList2;
	}

	/**
	 * 格式化显示字段
	 * @param field
	 * @return
	 */
	public static String formatField(String field) {
		String[] strs = field.split("_");
		field = "";
		int m = 0;
		for (int length = strs.length; m < length; m++) {
			if (m > 0) {
				String tempStr = strs[m].toLowerCase();
				tempStr = tempStr.substring(0, 1).toUpperCase()
						+ tempStr.substring(1, tempStr.length());
				field = field + tempStr;
			} else {
				field = field + strs[m].toLowerCase();
			}
		}
		return field;
	}

	public static String formatFieldCapital(String field) {
		String[] strs = field.split("_");
		field = "";
		int m = 0;
		for (int length = strs.length; m < length; m++) {
			if (m > 0) {
				String tempStr = strs[m].toLowerCase();
				tempStr = tempStr.substring(0, 1).toUpperCase()
						+ tempStr.substring(1, tempStr.length());
				field = field + tempStr;
			} else {
				field = field + strs[m].toLowerCase();
			}
		}
		field = field.substring(0, 1).toUpperCase() + field.substring(1);
		return field;
	}

	/**
	 * 检查表是否存在
	 * @param tableName
	 * @return
	 */
	public boolean checkTableExist(String tableName) {
		try {
			System.out.println("数据库驱动: " + CodeResourceUtil.DIVER_NAME);
			Class.forName(CodeResourceUtil.DIVER_NAME);
			this.conn = DriverManager.getConnection(CodeResourceUtil.URL,
					CodeResourceUtil.USERNAME, CodeResourceUtil.PASSWORD);
			this.stmt = this.conn.createStatement(1005, 1007);
			//mysql检查表是否存在的sql
			if (CodeResourceUtil.DATABASE_TYPE.equals("mysql")) {
				this.sql = ("select column_name,data_type,column_comment,0,0 from information_schema.columns where table_name = '"
						+ tableName.toUpperCase()
						+ "'"
						+ " and table_schema = '"
						+ CodeResourceUtil.DATABASE_NAME + "'");
			}
			//oracle检查表是否存在的sql
			if (CodeResourceUtil.DATABASE_TYPE.equals("oracle")) {
				this.sql = ("select colstable.column_name column_name, colstable.data_type data_type, commentstable.comments column_comment from user_tab_cols colstable  inner join user_col_comments commentstable  on colstable.column_name = commentstable.column_name  where colstable.table_name = commentstable.table_name  and colstable.table_name = '"
						+ tableName.toUpperCase() + "'");
			}
			//postgresql检查表是否存在的sql
			if (CodeResourceUtil.DATABASE_TYPE.equals("postgresql")) {
				this.sql = MessageFormat
						.format(
								"SELECT a.attname AS  field,t.typname AS type,col_description(a.attrelid,a.attnum) as comment,null as column_precision,null as column_scale,null as Char_Length,a.attnotnull  FROM pg_class c,pg_attribute  a,pg_type t  WHERE c.relname = {0} and a.attnum > 0  and a.attrelid = c.oid and a.atttypid = t.oid  ORDER BY a.attnum ",
								new Object[] { TableConvert.getV(tableName
										.toLowerCase()) });
			}
			//sqlserver检查表是否存在的sql
			if (CodeResourceUtil.DATABASE_TYPE.equals("sqlserver")) {
				this.sql = MessageFormat
						.format(
								"select cast(a.name as varchar(50)) column_name,  cast(b.name as varchar(50)) data_type,  cast(e.value as varchar(200)) comment,  cast(ColumnProperty(a.object_id,a.Name,'''Precision''') as int) num_precision,  cast(ColumnProperty(a.object_id,a.Name,'''Scale''') as int) num_scale,  a.max_length,  (case when a.is_nullable=1 then '''y''' else '''n''' end) nullable   from sys.columns a left join sys.types b on a.user_type_id=b.user_type_id left join sys.objects c on a.object_id=c.object_id and c.type='''U''' left join sys.extended_properties e on e.major_id=c.object_id and e.minor_id=a.column_id and e.class=1 where c.name={0}",
								new Object[] { TableConvert.getV(tableName
										.toLowerCase()) });
			}
			//得到结果集
			this.rs = this.stmt.executeQuery(this.sql);
			this.rs.last();
			int fieldNum = this.rs.getRow();
			if (fieldNum > 0)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * 根据将字段类型生成css中的class
	 * @param columnt
	 */
	private void formatFieldClassType(Columnt columnt) {
		String fieldType = columnt.getFieldType();
		String scale = columnt.getScale();

		columnt.setClassType("inputxt");

		if ("N".equals(columnt.getNullable())) {
			columnt.setOptionType("*");
		}
		if (("datetime".equals(fieldType)) || (fieldType.contains("time")))
			columnt.setClassType("easyui-datetimebox");
		else if ("date".equals(fieldType))
			columnt.setClassType("easyui-datebox");
		else if (fieldType.contains("int"))
			columnt.setOptionType("n");
		else if ("number".equals(fieldType)) {
			if ((StringUtils.isNotBlank(scale))
					&& (Integer.parseInt(scale) > 0))
				columnt.setOptionType("d");
		} else if (("float".equals(fieldType)) || ("double".equals(fieldType))
				|| ("decimal".equals(fieldType)))
			columnt.setOptionType("d");
		else if ("numeric".equals(fieldType))
			columnt.setOptionType("d");
	}

	/**
	 * 讲数据库类型格式化成为java的类型
	 * @param dataType
	 * @param precision
	 * @param scale
	 * @return
	 */
	private String formatDataType(String dataType, String precision,
			String scale) {
		if (dataType.contains("char"))
			dataType = "java.lang.String";
		else if (dataType.contains("int"))
			dataType = "java.lang.Integer";
		else if (dataType.contains("float"))
			dataType = "java.lang.Float";
		else if (dataType.contains("double"))
			dataType = "java.lang.Double";
		else if (dataType.contains("number")) {
			if ((StringUtils.isNotBlank(scale))
					&& (Integer.parseInt(scale) > 0))
				dataType = "java.math.BigDecimal";
			else if ((StringUtils.isNotBlank(precision))
					&& (Integer.parseInt(precision) > 10))
				dataType = "java.lang.Long";
			else
				dataType = "java.lang.Integer";
		} else if (dataType.contains("decimal"))
			dataType = "BigDecimal";
		else if (dataType.contains("date"))
			dataType = "java.util.Date";
		else if (dataType.contains("time")) {
			dataType = "java.util.Date";
		} else if (dataType.contains("blob"))
			dataType = "byte[]";
		else if (dataType.contains("clob"))
			dataType = "java.sql.Clob";
		else if (dataType.contains("numeric"))
			dataType = "BigDecimal";
		else {
			dataType = "java.lang.Object";
		}
		return dataType;
	}
}