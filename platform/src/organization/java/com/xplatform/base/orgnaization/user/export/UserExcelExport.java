package com.xplatform.base.orgnaization.user.export;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

import com.xplatform.base.poi.excel.entity.ExportParams;
import com.xplatform.base.poi.excel.entity.params.ExcelExportEntity;
import com.xplatform.base.poi.excel.export.ExcelExportServer;

public class UserExcelExport extends ExcelExportServer {

	/**
	 * 重写方法: getAllExcelField|描述:
	 * 对解析完的导出配置,重新进行人为设置,比如各entity继承的创建时间,创建人,无法在OperationEntity中统一设置,可以在这里单独设置
	 * 
	 * @param exclusions
	 * @param targetId
	 * @param fields
	 * @param excelParams
	 * @param pojoClass
	 * @param getMethods
	 * @throws Exception
	 * @see com.xplatform.base.poi.excel.export.base.ExportBase#getAllExcelField(java.lang.String[], java.lang.String, java.lang.reflect.Field[], java.util.List, java.lang.Class, java.util.List)
	 */
		
	@Override
	public void getAllExcelField(String[] exclusions, String targetId, Field[] fields,
			List<ExcelExportEntity> excelParams, Class<?> pojoClass, List<Method> getMethods) throws Exception {
		super.getAllExcelField(exclusions, targetId, fields, excelParams, pojoClass, getMethods);
		for(ExcelExportEntity entity:excelParams){
			if("enterDate".equals(entity.getField())){
				entity.setFormat("yyyy-MM");
			}
		}
	}

	@Override
	public HSSFCellStyle getTitleStyle(HSSFWorkbook workbook,
			ExportParams entity) {
		return super.getTitleStyle(workbook, entity);
	}

	@Override
	public HSSFCellStyle getHeaderStyle(HSSFWorkbook workbook,
			ExportParams entity) {
		HSSFCellStyle hSSFCellStyle=super.getHeaderStyle(workbook, entity);
		//修改大标题字体
		hSSFCellStyle.getFont(workbook).setFontHeightInPoints((short)40);;
		return hSSFCellStyle;
	}

	@Override
	public HSSFCellStyle getTwoStyle(HSSFWorkbook workbook, boolean isWarp) {
		return super.getTwoStyle(workbook, isWarp);
	}

	@Override
	public HSSFCellStyle getOneStyle(HSSFWorkbook workbook, boolean isWarp) {
		return super.getOneStyle(workbook, isWarp);
	}

	@Override
	public CellStyle getStyles(Map<String, HSSFCellStyle> map, boolean needOne,
			boolean isWrap) {
		return super.getStyles(map, needOne, isWrap);
	}
	
	

}
