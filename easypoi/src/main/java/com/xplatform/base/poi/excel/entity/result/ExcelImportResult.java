package com.xplatform.base.poi.excel.entity.result;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 导入返回类
 * 
 * @author JueYue
 * @date 2014年6月29日 下午5:12:10
 */
@SuppressWarnings("rawtypes")
public class ExcelImportResult implements Serializable {
	public ExcelImportResult() {

	}
	/**
	 * 多sheet合并后的所有List
	 */
	private List allList;
	/**
	 * 多sheet合并后的错误List
	 */
	private List errorList;
	/**
	 * 多sheet合并后的正确List
	 */
	private List correctList;
	/**
	 * 正确的结果集,key为sheet序号,value为该sheet的正确List结果集(值已被还原处理,如原来为文字,处理后为code,在此处又被还原为文字)
	 */
	private Map correctListResult = new ListOrderedMap();
	/**
	 * 错误的结果集,key为sheet序号,value为该sheet的错误List结果集(值已被还原处理,如原来为文字,处理后为code,在此处又被还原为文字)
	 */
	private Map errorListResult = new ListOrderedMap();
	/**
	 * 所有的结果集,key为sheet序号,value为该sheet的所有List结果集(值已被还原处理,如原来为文字,处理后为code,在此处又被还原为文字)
	 */
	private Map allListResult = new ListOrderedMap();

	/**
	 * 用于呈现的正确结果集,key为sheet序号,value为该sheet的正确Array结果集(用于jquery.handsontable 展现)
	 */
	private Map correctArrayResult = new ListOrderedMap();
	/**
	 * 用于呈现的错误结果集,key为sheet序号,value为该sheet的正确Array结果集(用于jquery.handsontable 展现)
	 */
	private Map errorArrayResult = new ListOrderedMap();
	/**
	 * 用于呈现的全部结果集,key为sheet序号,value为该sheet的正确Array结果集(用于jquery.handsontable 展现)
	 */
	private Map allArrayResult = new ListOrderedMap();
	/**
	 * 正确的记录数
	 */
	Integer correctCount = 0;
	/**
	 * 错误的记录数
	 */
	Integer errorCount = 0;
	/**
	 * 所有记录数
	 */
	Integer allCount = 0;
	/**
	 * 是否存在校验失败
	 */
	private boolean verfiyFail;
	/**
	 * 数据源
	 */
	private Workbook workbook;

	public List getAllList() {
		return allList;
	}

	public void setAllList(List allList) {
		this.allList = allList;
	}

	public List getErrorList() {
		return errorList;
	}

	public void setErrorList(List errorList) {
		this.errorList = errorList;
	}

	public List getCorrectList() {
		return correctList;
	}

	public void setCorrectList(List correctList) {
		this.correctList = correctList;
	}

	public boolean isVerfiyFail() {
		return verfiyFail;
	}

	public void setVerfiyFail(boolean verfiyFail) {
		this.verfiyFail = verfiyFail;
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}

	public Map getCorrectListResult() {
		return correctListResult;
	}

	public void setCorrectListResult(Map correctListResult) {
		this.correctListResult = correctListResult;
	}

	public Map getErrorListResult() {
		return errorListResult;
	}

	public void setErrorListResult(Map errorListResult) {
		this.errorListResult = errorListResult;
	}

	public Map getAllListResult() {
		return allListResult;
	}

	public void setAllListResult(Map allListResult) {
		this.allListResult = allListResult;
	}

	public Map getCorrectArrayResult() {
		return correctArrayResult;
	}

	public void setCorrectArrayResult(Map correctArrayResult) {
		this.correctArrayResult = correctArrayResult;
	}

	public Map getErrorArrayResult() {
		return errorArrayResult;
	}

	public void setErrorArrayResult(Map errorArrayResult) {
		this.errorArrayResult = errorArrayResult;
	}

	public Map getAllArrayResult() {
		return allArrayResult;
	}

	public void setAllArrayResult(Map allArrayResult) {
		this.allArrayResult = allArrayResult;
	}

	public Integer getCorrectCount() {
		return correctCount;
	}

	public void setCorrectCount(Integer correctCount) {
		this.correctCount = correctCount;
	}

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	public Integer getAllCount() {
		return allCount;
	}

	public void setAllCount(Integer allCount) {
		this.allCount = allCount;
	}

}
