package com.xplatform.base.poi.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.xplatform.base.poi.excel.entity.ImportParams;
import com.xplatform.base.poi.excel.entity.result.ExcelImportResult;
import com.xplatform.base.poi.excel.entity.vo.ExcelImportVo;
import com.xplatform.base.poi.excel.imports.ExcelImportServer;

/**
 * Excel 导入工具
 * 
 * @author JueYue
 * @date 2013-9-24
 * @version 1.0
 */
@SuppressWarnings({ "hiding", "unchecked" })
public class ExcelImportUtil {

	/**
	 * Excel 导入 数据源本地文件,返回校验结果 字段类型 Integer,Long,Double,Date,String,Boolean
	 * 
	 * @param file
	 * @param pojoClass
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static ExcelImportResult importExcelForResult(File file,
			Class<? extends ExcelImportVo> pojoClass, ImportParams params) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			return new ExcelImportServer().importExcelByIs(in, pojoClass,
					params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Excel 导入 数据源本地文件,不返回校验结果 导入 字 段类型 Integer,Long,Double,Date,String,Boolean
	 * 
	 * @param file
	 * @param pojoClass
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static List importExcel(File file, Class<? extends ExcelImportVo> pojoClass,
			ImportParams params) {
		FileInputStream in = null;
		List result = null;
		try {
			in = new FileInputStream(file);
			result = new ExcelImportServer().importExcelByIs(in, pojoClass,
					params).getAllList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Excel 导入 数据源IO流,返回校验结果 字段类型 Integer,Long,Double,Date,String,Boolean
	 * 
	 * @param file
	 * @param pojoClass
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static ExcelImportResult importExcelByIsForResult(
			InputStream inputstream, Class<? extends ExcelImportVo> pojoClass, ImportParams params)
			throws Exception {
		return new ExcelImportServer().importExcelByIs(inputstream, pojoClass,
				params);
	}

	/**
	 * Excel 导入 数据源IO流,不返回校验结果 导入 字段类型 Integer,Long,Double,Date,String,Boolean
	 * 
	 * @param file
	 * @param pojoClass
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static List importExcelByIs(InputStream inputstream,
			Class<? extends ExcelImportVo> pojoClass, ImportParams params) throws Exception {
		return importExcelByIsForResult(inputstream,pojoClass,params).getAllList();
	}

}
