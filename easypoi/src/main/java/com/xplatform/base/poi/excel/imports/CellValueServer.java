package com.xplatform.base.poi.excel.imports;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.NumberToTextConverter;

import com.xplatform.base.poi.excel.entity.params.ExcelImportEntity;
import com.xplatform.base.poi.exception.excel.ExcelImportException;
import com.xplatform.base.poi.exception.excel.enums.ExcelImportEnum;
import com.xplatform.base.poi.handler.inter.IExcelDataHandler;
import com.xplatform.base.poi.util.POIPublicUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cell 取值服务
 * 判断类型处理数据 1.判断Excel中的类型 2.根据replace替换值 3.handler处理数据 4.判断返回类型转化数据返回
 * 
 * @author JueYue
 * @date 2014年6月26日 下午10:42:28
 */
public class CellValueServer {

	private static final Logger logger = LoggerFactory.getLogger(CellValueServer.class);

	private List<String> hanlderList = null;

	/**
	 * 获取cell的值(经过replace,dataHandler等操作之后的最终值)
	 * 
	 * @param object 传入刚构造的单个无值对象 比如TeacherEntity
	 * @param excelParams
	 * @param cell
	 * @param titleString
	 */
	public Object getValue(IExcelDataHandler dataHanlder, Object object, Cell cell,
			Map<String, ExcelImportEntity> excelParams, String titleString) throws Exception {
		ExcelImportEntity entity = excelParams.get(titleString);
		Method setMethod = entity.getMethods() != null && entity.getMethods().size() > 0 ? entity.getMethods().get(
				entity.getMethods().size() - 1) : entity.getMethod();
		Type[] ts = setMethod.getGenericParameterTypes();
		//set的第一个参数的类型
		String xclass = ts[0].toString();
		Map<String, Object> oldValueMap = (Map<String, Object>) POIPublicUtil.getFieldValue(object, "oldValueMap");
		Object result = getCellValue(xclass, cell, entity);
		//留存一份旧值
		Object oldResult = result;
		oldResult = getValueByType(xclass, oldResult);
		result = replaceValue(entity.getReplace(), result);
		result = hanlderValue(dataHanlder, object, result, entity);
		if (oldResult == null) {
			if (result != null) {
				//旧值为空,新值不为空了,则也算改变.存放新值
				oldValueMap.put(entity.getField(), result);
			}
		} else {
			if (result != null) {
				if (!oldResult.equals(result)) {
					//旧值不为空,新值不为空,但是不相等,算作改变(最常见情况)
					oldValueMap.put(entity.getField(), oldResult);
				}
			} else {
				//旧值不为空,新值为空了,则也算改变.存放新值
				oldValueMap.put(entity.getField(), result);
			}
		}
		POIPublicUtil.setFieldValue(object, "oldValueMap", oldValueMap);
		result = getValueByType(xclass, result);
		return result;
	}

	/**
	 * 根据返回类型获取返回值(将所有Object都转成set方法中的Object)
	 * 
	 * @param xclass
	 * @param result
	 * @return
	 */
	private Object getValueByType(String xclass, Object result) {
		try {
			if (result != null) {
				if (xclass.equals("class java.util.Date")) {
					return result;
				}
				if (xclass.equals("class java.lang.Boolean") || xclass.equals("boolean")) {
					return Boolean.valueOf(String.valueOf(result));
				}
				if (xclass.equals("class java.lang.Double") || xclass.equals("double")) {
					return Double.valueOf(String.valueOf(result));
				}
				if (xclass.equals("class java.lang.Long") || xclass.equals("long")) {
					return Long.valueOf(String.valueOf(result));
				}
				if (xclass.equals("class java.lang.Integer") || xclass.equals("int")) {
					return Integer.valueOf(String.valueOf(result));
				}
				return String.valueOf(result);
			} else {
				return null;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new ExcelImportException(ExcelImportEnum.GET_VALUE_ERROR);
		}
	}

	/**
	 * 调用处理接口处理值
	 * 
	 * @param dataHanlder
	 * @param object 传入刚构造的单个无值对象 比如TeacherEntity
	 * @param result 传入的值
	 * @param titleString
	 * @return
	 */
	private Object hanlderValue(IExcelDataHandler dataHanlder, Object object, Object result, ExcelImportEntity entity) {
		if (dataHanlder == null) {
			return result;
		} else {
			if (hanlderList == null) {
				hanlderList = Arrays.asList(dataHanlder.getNeedHandlerFields());
			}
			if (hanlderList.contains(entity.getName())) {
				return dataHanlder.importHandler(object, entity.getName(), result);
			} else if (hanlderList.contains((entity.getField()))) {
				return dataHanlder.importHandler(object, entity.getField(), result);
			}
			return result;
		}

	}

	/**
	 * 替换值
	 * 
	 * @param replace
	 * @param result
	 * @return
	 */
	private Object replaceValue(String[] replace, Object result) {
		if (replace != null && replace.length > 0 && result != null) {
			String temp = String.valueOf(result);
			String[] tempArr;
			for (int i = 0; i < replace.length; i++) {
				tempArr = replace[i].split("_");
				if (temp.equals(tempArr[0])) {
					return tempArr[1];
				}
			}
		}
		return result;
	}

	/**
	 * 获取单元格内的值
	 * 
	 * @param xclass
	 * @param cell
	 * @param entity
	 * @return
	 */
	private Object getCellValue(String xclass, Cell cell, ExcelImportEntity entity) {
		Object result = null;
		// 日期格式比较特殊,和cell格式不一致
		if (cell != null) {
			if (xclass.equals("class java.util.Date")) {
				if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
					// 日期格式
					result = cell.getDateCellValue();
				} else {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					result = getDateData(entity, cell.getStringCellValue());
				}
			} else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
				result = cell.getNumericCellValue();
			} else if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
				result = cell.getBooleanCellValue();
			} else {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				result = cell.getStringCellValue();
			}
		}
		return result;
	}

	/**
	 * 获取单元格内的字符值
	 * 
	 * @param cell
	 * @param dateFormat
	 * @return
	 */
	public String getCellStrValue(Cell cell, String dateFormat) {
		String result = "";
		if (dateFormat == null || "".equals(dateFormat)) {
			dateFormat = "yyyy-MM-dd HH:mm:ss";
		}
		// 日期格式比较特殊,和cell格式不一致
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				result = "";
				break;
			case Cell.CELL_TYPE_STRING:
				result = cell.getRichStringCellValue().getString();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				result = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				result = String.valueOf(cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					result = new SimpleDateFormat(dateFormat).format(cell.getDateCellValue());
				} else {
					//					result=String.valueOf(cell.getNumericCellValue());
					result = NumberToTextConverter.toText(cell.getNumericCellValue());
				}
				break;
			case Cell.CELL_TYPE_ERROR:
				result = null;
				break;
			default:
				break;
			}
		}
		return result;
	}

	/**
	 * 获取日期类型数据
	 * 
	 * @Author JueYue
	 * @date 2013年11月26日
	 * @param entity
	 * @param value
	 * @return
	 */
	private Date getDateData(ExcelImportEntity entity, String value) {
		if (StringUtils.isNotEmpty(entity.getFormat()) && StringUtils.isNotEmpty(value)) {
			SimpleDateFormat format = new SimpleDateFormat(entity.getFormat());
			try {
				return format.parse(value);
			} catch (ParseException e) {
				logger.error("时间格式化失败,格式化:{},值:{}", entity.getFormat(), value);
				throw new ExcelImportException(ExcelImportEnum.GET_VALUE_ERROR);
			}
		}
		return null;
	}
}
