package com.xplatform.base.poi.excel.export;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import com.xplatform.base.poi.excel.annotation.ExcelTarget;
import com.xplatform.base.poi.excel.entity.ExportParams;
import com.xplatform.base.poi.excel.entity.params.ExcelExportEntity;
import com.xplatform.base.poi.excel.export.base.ExcelExportBase;
import com.xplatform.base.poi.exception.excel.ExcelExportException;
import com.xplatform.base.poi.exception.excel.enums.ExcelExportEnum;
import com.xplatform.base.poi.util.POIPublicUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excel导出服务
 * 
 * @author JueYue
 * @date 2014年6月17日 下午5:30:54
 */
public class ExcelExportServer extends ExcelExportBase {
	
	private final static Logger logger = LoggerFactory.getLogger(ExcelExportServer.class);

	private static final short cellFormat = HSSFDataFormat
			.getBuiltinFormat("TEXT");

	// 最大行数,超过自动多Sheet
	//TODO (优化)这个数值是否做成可配? 
	private final int MAX_NUM = 60000;

	public void createSheet(HSSFWorkbook workbook, ExportParams entity,
			Class<?> pojoClass, Collection<?> dataSet) {
		if(logger.isDebugEnabled()){
			logger.debug("Excel export start ,class is {}",pojoClass);
		}
		if (workbook == null || entity == null || pojoClass == null
				|| dataSet == null) {
			throw new ExcelExportException(ExcelExportEnum.PARAMETER_ERROR);
		}
		Sheet sheet = null;
		try {
			sheet = workbook.createSheet(entity.getSheetName());
		} catch (Exception e) {
			// 重复遍历,出现了重名现象,创建非指定的名称Sheet
			sheet = workbook.createSheet();
		}
		try {
			dataHanlder = entity.getDataHanlder();
			if (dataHanlder != null) {
				needHanlderList = Arrays.asList(dataHanlder
						.getNeedHandlerFields());
			}
			// 创建表格样式属性
			Map<String, HSSFCellStyle> styles = createStyles(workbook);
			Drawing patriarch = sheet.createDrawingPatriarch();
			List<ExcelExportEntity> excelParams = new ArrayList<ExcelExportEntity>();
			// 得到所有字段
			Field fileds[] = POIPublicUtil.getClassFields(pojoClass);
			ExcelTarget etarget = pojoClass.getAnnotation(ExcelTarget.class);
			String targetId = etarget == null ? null : etarget.value();
			//本方法就是为了构造出List<ExcelExportEntity> 导出时各列的配置List
			getAllExcelField(entity.getExclusions(), targetId, fileds,
					excelParams, pojoClass, null);
			//按照ExcelExportEntity中的orderNum排好列的顺序(利用比较器与Collections.sort)
			sortAllParams(excelParams);
			//创建标题栏和表头
			int index = createHeaderAndTitle(entity, sheet, workbook,
					excelParams);
			int titleHeight = index;
			//设置各列宽度
			setCellWith(excelParams, sheet);
			//遍历各列设置,获得最大高度,作为所有行的高度
			short rowHeight = getRowHeight(excelParams);
			//创建并填充数据栏区域
			Iterator<?> its = dataSet.iterator();
			List<Object> tempList = new ArrayList<Object>();
			while (its.hasNext()) {
				Object t = its.next();
				index += createCells(patriarch, index, t, excelParams, sheet,
						workbook, styles, rowHeight);
				tempList.add(t);
				if (index >= MAX_NUM)
					break;
			}
			mergeCells(sheet, excelParams, titleHeight);

			//构造超过MAX_NUM单sheet最大数据之后的数据iterator
			//TODO (优化)多次iterator是否太耗性能,是否有优化方法
			its = dataSet.iterator();
			for (int i = 0, le = tempList.size(); i < le; i++) {
				its.next();
				its.remove();
			}
			// 发现还有剩余list 继续循环创建Sheet(多sheet情况)
			if (dataSet.size() > 0) {
				createSheet(workbook, entity, pojoClass, dataSet);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcelExportException(ExcelExportEnum.EXPORT_ERROR,
					e.getCause());
		}
	}

	private int createHeaderAndTitle(ExportParams entity, Sheet sheet,
			HSSFWorkbook workbook, List<ExcelExportEntity> excelParams) {
		int rows = 0, feildWidth = getFieldWidth(excelParams);
		if (entity.getTitle() != null) {
			rows += createHeaderRow(entity, sheet, workbook, feildWidth);
		}
		rows += createTitleRow(entity, sheet, workbook, rows, excelParams);
		//设置冻结列(冻结标题行和表头)
		sheet.createFreezePane(0, rows, 0, rows);
		return rows;
	}

	/**
	 * 创建表头
	 * 
	 * @param title
	 * @param index
	 */
	private int createTitleRow(ExportParams title, Sheet sheet,
			HSSFWorkbook workbook, int index,
			List<ExcelExportEntity> excelParams) {
		Row row = sheet.createRow(index);
		int rows = getRowNums(excelParams);
		//TODO (优化)这个表头第一列高度是否做成可配?
		row.setHeight((short) 450);
		Row listRow = null;
		//若表头列有2列,则创建第二列
		if (rows == 2) {
			listRow = sheet.createRow(index + 1);
			//TODO (优化)这个表头第二列高度是否做成可配?
			listRow.setHeight((short) 450);
		}
		int cellIndex = 0;
		//TODO (优化)表头的底色等样式在此设置,是否做成可配?
		CellStyle titleStyle = getTitleStyle(workbook, title);
		for (int i = 0, exportFieldTitleSize = excelParams.size(); i < exportFieldTitleSize; i++) {
			ExcelExportEntity entity = excelParams.get(i);
			createStringCell(row, cellIndex, entity.getName(), titleStyle,
					entity);
			//有子列的分2行
			if (entity.getList() != null) {
				List<ExcelExportEntity> sTitel = entity.getList();
				//2行的合并首行
				sheet.addMergedRegion(new CellRangeAddress(index, index,
						cellIndex, cellIndex + sTitel.size() - 1));
				for (int j = 0, size = sTitel.size(); j < size; j++) {
					createStringCell(listRow, cellIndex, sTitel.get(j)
							.getName(), titleStyle, entity);
					cellIndex++;
				}
			} else if (rows == 2) {
				//其他列就需要合并行
				sheet.addMergedRegion(new CellRangeAddress(index, index + 1,
						cellIndex, cellIndex));
			}
			cellIndex++;
		}
		return rows;

	}

	/**
	 * 判断表头是只有一行还是两行(如果ExcelExportEntity中还有List<ExcelExportEntity>就有1对多关系导出,就有2行,否则1行)
	 * 
	 * @param excelParams
	 * @return
	 */
	private int getRowNums(List<ExcelExportEntity> excelParams) {
		for (int i = 0; i < excelParams.size(); i++) {
			if (excelParams.get(i).getList() != null) {
				return 2;
			}
		}
		return 1;
	}

	/**
	 * 创建 表头改变
	 * 
	 * @param entity
	 * @param sheet
	 * @param workbook
	 * @param feildWidth
	 */
	public int createHeaderRow(ExportParams entity, Sheet sheet,
			HSSFWorkbook workbook, int feildWidth) {
		Row row = sheet.createRow(0);
		row.setHeight(entity.getTitleHeight());
		createStringCell(row, 0, entity.getTitle(),
				getHeaderStyle(workbook, entity), null);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, feildWidth));
		if (entity.getSecondTitle() != null) {
			row = sheet.createRow(1);
			row.setHeight(entity.getSecondTitleHeight());
			createStringCell(row, 0, entity.getSecondTitle(), getSecondHeaderStyle(workbook, entity), null);
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, feildWidth));
			return 2;
		}
		return 1;
	}

	/**
	 * 字段说明的Style
	 * 
	 * @param workbook
	 * @return
	 */
	public HSSFCellStyle getTitleStyle(HSSFWorkbook workbook,
			ExportParams entity) {
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFillForegroundColor(entity.getHeaderColor()); // 填充的背景颜色
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); // 填充图案
		titleStyle.setWrapText(true);
		return titleStyle;
	}

	/**
	 * 大标题的Style
	 * 
	 * @param workbook
	 * @return
	 */
	public HSSFCellStyle getHeaderStyle(HSSFWorkbook workbook,
			ExportParams entity) {
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		if (entity.getTitleFontSize() != (short) 0) {
			font.setFontHeightInPoints(entity.getTitleFontSize());
		}
		titleStyle.setFont(font);
		titleStyle.setFillForegroundColor(entity.getColor());
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return titleStyle;
	}
	
	/**
	 * 第二行标题的Style
	 * 
	 * @param workbook
	 * @return
	 */
	public HSSFCellStyle getSecondHeaderStyle(HSSFWorkbook workbook,
			ExportParams entity) {
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		if (entity.getSecondTitleFontSize() != (short) 0) {
			font.setFontHeightInPoints(entity.getSecondTitleFontSize());
		}
		titleStyle.setFont(font);
		titleStyle.setFillForegroundColor(entity.getColor());
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return titleStyle;
	}

	public HSSFCellStyle getTwoStyle(HSSFWorkbook workbook, boolean isWarp) {
		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderLeft((short) 1); // 左边框
		style.setBorderRight((short) 1); // 右边框
		style.setBorderBottom((short) 1);
		style.setBorderTop((short) 1);
		style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index); // 填充的背景颜色
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); // 填充图案
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setDataFormat(cellFormat);
		if (isWarp) {
			style.setWrapText(true);
		}
		return style;
	}

	public HSSFCellStyle getOneStyle(HSSFWorkbook workbook, boolean isWarp) {
		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderLeft((short) 1); // 左边框
		style.setBorderRight((short) 1); // 右边框
		style.setBorderBottom((short) 1);
		style.setBorderTop((short) 1);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setDataFormat(cellFormat);
		if (isWarp) {
			style.setWrapText(true);
		}
		return style;
	}

	private Map<String, HSSFCellStyle> createStyles(HSSFWorkbook workbook) {
		Map<String, HSSFCellStyle> map = new HashMap<String, HSSFCellStyle>();
		map.put("one", getOneStyle(workbook, false));
		map.put("oneWrap", getOneStyle(workbook, true));
		map.put("two", getTwoStyle(workbook, false));
		map.put("twoWrap", getTwoStyle(workbook, true));
		return map;
	}

	public CellStyle getStyles(Map<String, HSSFCellStyle> map, boolean needOne,
			boolean isWrap) {
		if (needOne && isWrap) {
			return map.get("oneWrap");
		}
		if (needOne) {
			return map.get("one");
		}
		if (needOne == false && isWrap) {
			return map.get("twoWrap");
		}
		return map.get("two");
	}

}
