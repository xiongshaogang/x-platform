package com.xplatform.base.poi.excel.imports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.xplatform.base.poi.excel.annotation.Excel;
import com.xplatform.base.poi.excel.annotation.ExcelTarget;
import com.xplatform.base.poi.excel.annotation.ExcelVerify;
import com.xplatform.base.poi.excel.entity.ImportParams;
import com.xplatform.base.poi.excel.entity.params.ExcelCollectionParams;
import com.xplatform.base.poi.excel.entity.params.ExcelImportEntity;
import com.xplatform.base.poi.excel.entity.params.ExcelVerifyEntity;
import com.xplatform.base.poi.excel.entity.result.ExcelImportResult;
import com.xplatform.base.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.xplatform.base.poi.excel.entity.vo.ExcelImportVo;
import com.xplatform.base.poi.excel.imports.verifys.VerifyHandlerServer;
import com.xplatform.base.poi.util.POIPublicUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excel 导入服务
 * 
 * @author JueYue
 * @date 2014年6月26日 下午9:20:51
 */
@SuppressWarnings({ "rawtypes", "unchecked", "hiding" })
public class ExcelImportServer {

	private final static Logger logger = LoggerFactory.getLogger(ExcelImportServer.class);

	private CellValueServer cellValueServer;

	private VerifyHandlerServer verifyHandlerServer;

	private boolean verfiyFail = false;

	public ExcelImportServer() {
		cellValueServer = new CellValueServer();
		verifyHandlerServer = new VerifyHandlerServer();
	}

	/**
	 * Excel 导入 field 字段类型 Integer,Long,Double,Date,String,Boolean
	 * 
	 * @param inputstream
	 * @param pojoClass
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ExcelImportResult importExcelByIs(InputStream inputstream, Class<? extends ExcelImportVo> pojoClass,
			ImportParams params) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Excel import start ,class is {}", pojoClass);
		}
		ExcelImportResult excelImportResult = new ExcelImportResult();

		Workbook book = null;
		boolean isXSSFWorkbook = true;
		if (!(inputstream.markSupported())) {
			inputstream = new PushbackInputStream(inputstream, 8);
		}
		if (POIFSFileSystem.hasPOIFSHeader(inputstream)) {
			book = new HSSFWorkbook(inputstream);
			isXSSFWorkbook = false;
		} else if (POIXMLDocument.hasOOXMLHeader(inputstream)) {
			book = new XSSFWorkbook(OPCPackage.open(inputstream));
		}
		Map<String, PictureData> pictures;

		List allList = new ArrayList();
		List errorList = new ArrayList();
		List correctList = new ArrayList();
		Map<Integer, List> allListMap = excelImportResult.getAllListResult();
		Map<Integer, List> correctListMap = excelImportResult.getCorrectListResult();
		Map<Integer, List> errorListMap = excelImportResult.getErrorListResult();
		Map<Integer, List> allArrayMap = excelImportResult.getAllArrayResult();
		Map<Integer, List> correctArrayMap = excelImportResult.getCorrectArrayResult();
		Map<Integer, List> errorArrayMap = excelImportResult.getErrorArrayResult();
		Integer correctCount = 0;
		Integer errorCount = 0;
		Integer allCount = 0;
		for (int i = 0; i < params.getSheetNum(); i++) {
			if (isXSSFWorkbook) {
				pictures = POIPublicUtil.getSheetPictrues07((XSSFSheet) book.getSheetAt(i), (XSSFWorkbook) book);
			} else {
				pictures = POIPublicUtil.getSheetPictrues03((HSSFSheet) book.getSheetAt(i), (HSSFWorkbook) book);
			}

			//实例化保存各种结果集的对象
			List<ExcelImportVo> allListResult = new ArrayList();
			List correctListResult = new ArrayList();
			List errorListResult = new ArrayList();
			List<String[]> allArrayResult = new ArrayList<String[]>();
			List<String[]> correctArrayResult = new ArrayList<String[]>();
			List<String[]> errorArrayResult = new ArrayList<String[]>();
			String[] titleArray = null;
			//执行解析操作
			allListResult = (List<ExcelImportVo>) importExcel(allListResult, book.getSheetAt(i), pojoClass, params,
					pictures, allArrayResult);

			//备份一份最终插入值(java深拷贝)
			List<ExcelImportVo> oldListResult = POIPublicUtil.deepCopy(allListResult);

			//获取标题栏
			if (allArrayResult.size() >= 1) {
				titleArray = allArrayResult.get(0);
				correctArrayResult.add(titleArray);
				errorArrayResult.add(titleArray);
			}
			//结果集的分类与值还原处理
			for (int j = 0; j < allListResult.size(); j++) {
				ExcelImportVo vo = allListResult.get(j);
				String[] arrayEle = allArrayResult.get(j + 1);//此处+1是为了跳过标题栏
				arrayEle[arrayEle.length - 1] = vo.getErrorMsg();//添加错误信息
				if (vo.getValidResult()) {
					correctListResult.add(vo);
					correctArrayResult.add(arrayEle);
					correctCount++;
				} else {
					errorListResult.add(vo);
					errorArrayResult.add(arrayEle);
					errorCount++;
				}
				if (vo.getOldValueMap().size() > 0) {
					Map<String, Object> oldValueMap = vo.getOldValueMap();
					for (Map.Entry<String, Object> entry : oldValueMap.entrySet()) {
						String key = entry.getKey();
						Object value = entry.getValue();
						POIPublicUtil.setFieldValue(vo, key, value);
					}
				}
				allCount++;
			}
			allList.addAll(oldListResult);
			errorList.addAll(errorListResult);
			correctList.addAll(correctList);
			allListMap.put(i, allListResult);
			correctListMap.put(i, correctListResult);
			errorListMap.put(i, errorListResult);
			allArrayMap.put(i, allArrayResult);
			correctArrayMap.put(i, correctArrayResult);
			errorArrayMap.put(i, errorArrayResult);

		}
		if (params.isNeedSave()) {
			saveThisExcel(params, pojoClass, isXSSFWorkbook, book);
		}
		excelImportResult.setAllList(allList);
		excelImportResult.setErrorList(errorList);
		excelImportResult.setCorrectList(correctList);
		excelImportResult.setAllListResult(allListMap);
		excelImportResult.setCorrectListResult(correctListMap);
		excelImportResult.setErrorListResult(errorListMap);
		excelImportResult.setCorrectCount(correctCount);
		excelImportResult.setErrorCount(errorCount);
		excelImportResult.setAllCount(allCount);
		excelImportResult.setVerfiyFail(verfiyFail);
		excelImportResult.setWorkbook(book);
		return excelImportResult;
	}

	private void saveThisExcel(ImportParams params, Class<?> pojoClass, boolean isXSSFWorkbook, Workbook book)
			throws Exception {
		String path = POIPublicUtil.getWebRootPath(getSaveExcelUrl(params, pojoClass));
		File savefile = new File(path);
		if (!savefile.exists()) {
			savefile.mkdirs();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyMMddHHmmss");
		FileOutputStream fos = new FileOutputStream(path + "/" + format.format(new Date()) + "_"
				+ Math.round(Math.random() * 100000) + (isXSSFWorkbook == true ? ".xlsx" : ".xls"));
		book.write(fos);
		fos.close();
	}

	/**
	 * 获取保存的Excel 的真实路径
	 * 
	 * @param params
	 * @param pojoClass
	 * @return
	 * @throws Exception
	 */
	private String getSaveExcelUrl(ImportParams params, Class<?> pojoClass) throws Exception {
		String url = "";
		if (params.getSaveUrl().equals("upload/excelUpload")) {
			url = pojoClass.getName().split("\\.")[pojoClass.getName().split("\\.").length - 1];
			return params.getSaveUrl() + "/" + url.substring(0, url.lastIndexOf("Entity"));
		}
		return params.getSaveUrl();
	}

	private <T> List importExcel(Collection<T> result, Sheet sheet, Class<?> pojoClass, ImportParams params,
			Map<String, PictureData> pictures, List<String[]> arrayResult) throws Exception {
		List collection = new ArrayList();
		Map<String, ExcelImportEntity> excelParams = new HashMap<String, ExcelImportEntity>();
		List<ExcelCollectionParams> excelCollection = new ArrayList<ExcelCollectionParams>();
		Field fileds[] = POIPublicUtil.getClassFields(pojoClass);
		ExcelTarget etarget = pojoClass.getAnnotation(ExcelTarget.class);
		String targetId = null;
		if (etarget != null) {
			targetId = etarget.value();
		}
		getAllExcelField(targetId, fileds, excelParams, excelCollection, pojoClass, null);
		Iterator<Row> rows = sheet.rowIterator();
		for (int j = 0; j < params.getTitleRows(); j++) {
			rows.next();
		}
		Row row = null;
		Iterator<Cell> cellTitle;
		//放置有关表头的Map(key为列数,value为标题名称)
		Map<Integer, String> titlemap = new HashMap<Integer, String>();
		for (int j = 0; j < params.getHeadRows(); j++) {
			row = rows.next();
			String[] array = new String[params.getEndCell() + 2];
			cellTitle = row.cellIterator();
			int i = row.getFirstCellNum();
			while (cellTitle.hasNext()) {
				Cell cell = cellTitle.next();
				//设置表头的单元格类型(否则如果直接填入数字等内容,读取会报错)
				cell.setCellType(Cell.CELL_TYPE_STRING);
				String value = cell.getStringCellValue();
				if (!StringUtils.isEmpty(value)) {
					titlemap.put(i, value);
					array[i] = value;
				}
				i = i + 1;
			}
			array[i] = "导入错误明细";
			arrayResult.add(array);
		}
		Object object = null;
		String picId;
		int arrayIndex = 0;

		while (rows.hasNext()) {
			row = rows.next();
			Cell errorCell = row.getCell(params.getEndCell() + 1);
			if (errorCell == null) {
				errorCell = row.createCell(params.getEndCell() + 1);
			}
			errorCell.setCellValue("");
			// 判断是集合元素还是不是集合元素,如果是就继续加入这个集合,不是就创建新的对象
			if (params.getKeyIndex() >= 0) {
				if ((row.getCell(params.getKeyIndex()) == null || StringUtils.isEmpty(getKeyValue(row.getCell(params
						.getKeyIndex())))) && object != null) {
					for (ExcelCollectionParams param : excelCollection) {
						addListContinue(object, param, row, titlemap, targetId, pictures, params, arrayResult,
								arrayIndex);
					}
				}
			} else {
				object = POIPublicUtil.createObject(pojoClass, targetId);
				String[] array = new String[params.getEndCell() + 2];
				//				for (int i = row.getFirstCellNum(), le = row.getLastCellNum(); i < le; i++) {}
				for (int i = params.getStartCell(), le = params.getEndCell(); i <= le; i++) {
					Cell cell = row.getCell(i);
					String titleString = (String) titlemap.get(i);
					if (excelParams.containsKey(titleString)) {
						if (excelParams.get(titleString).getType() == 2) {
							picId = row.getRowNum() + "_" + i;
							saveImage(object, picId, excelParams, titleString, pictures, params);
						} else {
							saveFieldValue(params, object, cell, excelParams, titleString, row);
						}
						String dateFormat = excelParams.get(titleString).getFormat();
						String cellValue = "";
						//说明是日期类型
						if (cell != null && !"".equals(dateFormat) && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							cellValue = new SimpleDateFormat(dateFormat).format(cell.getDateCellValue());
						} else {
							cellValue = cellValueServer.getCellStrValue(cell, dateFormat);
						}
						array[i] = cellValue;
					}
				}
				if (arrayResult != null) {
					arrayResult.add(array);
				}
				for (ExcelCollectionParams param : excelCollection) {
					addListContinue(object, param, row, titlemap, targetId, pictures, params, arrayResult, arrayIndex);
				}
				collection.add(object);
			}
			arrayIndex++;
			//若放错误信息的额外增加单元格不存在非空值,说明是正确的,则将整列移除(为了最后得到的都是错误列)
			Cell errorMsg = row.getCell(params.getEndCell() + 1);
			if (errorMsg == null || StringUtils.isEmpty(errorMsg.toString())) {
				//				POIPublicUtil.removeRow(sheet, row.getRowNum()); //删除方法依旧有问题,暂不使用
			}
		}
		return collection;
	}

	/**
	 * 保存字段值(获取值,校验值,追加错误信息)
	 * 
	 * @param params
	 * @param object 传入刚构造的单个无值对象 比如TeacherEntity
	 * @param cell
	 * @param excelParams
	 * @param titleString
	 * @param row
	 * @throws Exception
	 */
	private void saveFieldValue(ImportParams params, Object object, Cell cell,
			Map<String, ExcelImportEntity> excelParams, String titleString, Row row) throws Exception {
		ExcelVerifyHanlderResult verifyResult;
		Object value = cellValueServer.getValue(params.getDataHanlder(), object, cell, excelParams, titleString);
		verifyResult = verifyHandlerServer.verifyData(object, value, titleString, excelParams.get(titleString)
				.getVerify(), params.getVerifyHanlder());
		if (verifyResult.isSuccess()) {
			setValues(excelParams.get(titleString), object, value);
		} else {
			//出错的话,给最终实体设置错误提示
			Object oldErrorObj = POIPublicUtil.getFieldValue(object, "errorMsg");
			String oldErrorMsg = "";
			if (oldErrorObj != null) {
				oldErrorMsg = oldErrorObj.toString();
			}
			POIPublicUtil.setFieldValue(object, "errorMsg", oldErrorMsg + verifyResult.getMsg());
			POIPublicUtil.setFieldValue(object, "validResult", false);
			Cell errorCell = row.getCell(params.getEndCell() + 1);
			if (errorCell == null) {
				errorCell = row.createCell(params.getEndCell() + 1);
			}
			errorCell.setCellValue(oldErrorMsg + verifyResult.getMsg());
			verfiyFail = true;
		}

		//最终实体设置本行验证结果

	}

	/**
	 * 获取key的值,针对不同类型获取不同的值
	 * 
	 * @Author JueYue
	 * @date 2013-11-21
	 * @param cell
	 * @return
	 */
	private String getKeyValue(Cell cell) {
		Object obj = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			obj = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			obj = cell.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			obj = cell.getNumericCellValue();
			break;
		}
		return obj == null ? null : obj.toString();
	}

	/**
	 * 
	 * @param object
	 * @param picId
	 * @param excelParams
	 * @param titleString
	 * @param pictures
	 * @param params
	 * @throws Exception
	 */
	private void saveImage(Object object, String picId, Map<String, ExcelImportEntity> excelParams, String titleString,
			Map<String, PictureData> pictures, ImportParams params) throws Exception {
		if (pictures == null) {
			return;
		}
		PictureData image = pictures.get(picId);
		byte[] data = image.getData();
		String fileName = "pic" + Math.round(Math.random() * 100000000000L);
		fileName += "." + POIPublicUtil.getFileExtendName(data);
		if (excelParams.get(titleString).getSaveType() == 1) {
			String path = POIPublicUtil.getWebRootPath(getSaveUrl(excelParams.get(titleString), object));
			File savefile = new File(path);
			if (!savefile.exists()) {
				savefile.mkdirs();
			}
			savefile = new File(path + "/" + fileName);
			FileOutputStream fos = new FileOutputStream(savefile);
			fos.write(data);
			fos.close();
			setValues(excelParams.get(titleString), object, getSaveUrl(excelParams.get(titleString), object) + "/"
					+ fileName);
		} else {
			setValues(excelParams.get(titleString), object, data);
		}
	}

	/**
	 * 获取保存的真实路径
	 * 
	 * @param excelImportEntity
	 * @param object
	 * @return
	 * @throws Exception
	 */
	private String getSaveUrl(ExcelImportEntity excelImportEntity, Object object) throws Exception {
		String url = "";
		if (excelImportEntity.getSaveUrl().equals("upload")) {
			if (excelImportEntity.getMethods() != null && excelImportEntity.getMethods().size() > 0) {
				object = getFieldBySomeMethod(excelImportEntity.getMethods(), object);
			}
			url = object.getClass().getName().split("\\.")[object.getClass().getName().split("\\.").length - 1];
			return excelImportEntity.getSaveUrl() + "/" + url.substring(0, url.lastIndexOf("Entity"));
		}
		return excelImportEntity.getSaveUrl();
	}

	/***
	 * 向List里面继续添加元素
	 * 
	 * @param exclusions
	 * @param object
	 * @param param
	 * @param row
	 * @param titlemap
	 * @param targetId
	 * @param pictures
	 * @param params
	 */
	private void addListContinue(Object object, ExcelCollectionParams param, Row row, Map<Integer, String> titlemap,
			String targetId, Map<String, PictureData> pictures, ImportParams params, List<String[]> arrayResult,
			int arrayIndex) throws Exception {
		//获得最终对象的集合对象
		Collection collection = (Collection) POIPublicUtil.getMethod(param.getName(), object.getClass()).invoke(object,
				new Object[] {});
		Object entity = POIPublicUtil.createObject(param.getType(), targetId);
		String picId;
		boolean isUsed = false;// 是否需要加上这个对象
		String[] array = new String[params.getEndCell() + 2];
		for (int i = params.getStartCell(), le = params.getEndCell(); i <= le; i++) {
			Cell cell = row.getCell(i);
			String titleString = (String) titlemap.get(i);
			if (param.getExcelParams().containsKey(titleString)) {
				if (param.getExcelParams().get(titleString).getType() == 2) {
					picId = row.getRowNum() + "_" + i;
					saveImage(object, picId, param.getExcelParams(), titleString, pictures, params);
				} else {
					saveFieldValue(params, entity, cell, param.getExcelParams(), titleString, row);
				}
				//构造用于前台展示的List<String[]>

				String dataFormat = param.getExcelParams().get(titleString).getFormat();
				String cellValue = cellValueServer.getCellStrValue(cell, dataFormat);
				array[i] = cellValue;
				isUsed = true;
			}
		}
		if (arrayResult != null) {
			arrayResult.add(array);
		}
		if (isUsed) {
			collection.add(entity);
		}
	}

	/**
	 * @Description 执行Set方法
	 * @param entity
	 * @param object 传入刚构造的单个无值对象 比如TeacherEntity
	 * @param value
	 * @throws Exception
	 */
	private void setValues(ExcelImportEntity entity, Object object, Object value) throws Exception {
		if (entity.getMethods() != null) {
			setFieldBySomeMethod(entity.getMethods(), object, value);
		} else {
			entity.getMethod().invoke(object, value);
		}
	}

	/**
	 * 多个get 最后再set(猜测是针对@ExcelEntity,先多次set构造出实体来,再把构造出的实体属性真正set到最终实体上)
	 * 
	 * @param setMethods
	 * @param object
	 */
	private void setFieldBySomeMethod(List<Method> setMethods, Object object, Object value) throws Exception {
		Object t = getFieldBySomeMethod(setMethods, object);
		setMethods.get(setMethods.size() - 1).invoke(t, value);
	}

	private Object getFieldBySomeMethod(List<Method> list, Object t) throws Exception {
		Method m;
		for (int i = 0; i < list.size() - 1; i++) {
			m = list.get(i);
			t = m.invoke(t, new Object[] {});
		}
		return t;
	}

	/**
	 * 获取需要导出的全部字段
	 * 
	 * 
	 * @param exclusions
	 * @param targetId
	 *            目标ID
	 * @param fields
	 * @param excelCollection
	 * @throws Exception
	 */
	private void getAllExcelField(String targetId, Field[] fields, Map<String, ExcelImportEntity> excelParams,
			List<ExcelCollectionParams> excelCollection, Class<?> pojoClass, List<Method> getMethods) throws Exception {
		ExcelImportEntity excelEntity = null;
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (POIPublicUtil.isNotUserExcelUserThis(null, field, targetId)) {
				continue;
			}
			if (POIPublicUtil.isCollection(field.getType())) {
				//集合对象设置属性
				ExcelCollectionParams collection = new ExcelCollectionParams();
				collection.setName(field.getName());
				Map<String, ExcelImportEntity> temp = new HashMap<String, ExcelImportEntity>();
				ParameterizedType pt = (ParameterizedType) field.getGenericType();
				Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];
				collection.setType(clz);
				getExcelFieldList(targetId, POIPublicUtil.getClassFields(clz), clz, temp, null);
				collection.setExcelParams(temp);
				excelCollection.add(collection);
			} else if (POIPublicUtil.isJavaClass(field)) {
				//基本类型构造配置
				addEntityToMap(targetId, field, excelEntity, pojoClass, getMethods, excelParams);
			} else {
				//实体类型,继续递归构造配置
				List<Method> newMethods = new ArrayList<Method>();
				if (getMethods != null) {
					newMethods.addAll(getMethods);
				}
				newMethods.add(POIPublicUtil.getMethod(field.getName(), pojoClass));
				getAllExcelField(targetId, POIPublicUtil.getClassFields(field.getType()), excelParams, excelCollection,
						field.getType(), newMethods);
			}
		}
	}

	private void getExcelFieldList(String targetId, Field[] fields, Class<?> pojoClass,
			Map<String, ExcelImportEntity> temp, List<Method> getMethods) throws Exception {
		ExcelImportEntity excelEntity = null;
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (POIPublicUtil.isNotUserExcelUserThis(null, field, targetId)) {
				continue;
			}
			if (POIPublicUtil.isJavaClass(field)) {
				addEntityToMap(targetId, field, excelEntity, pojoClass, getMethods, temp);
			} else {
				List<Method> newMethods = new ArrayList<Method>();
				if (getMethods != null) {
					newMethods.addAll(getMethods);
				}
				newMethods.add(POIPublicUtil.getMethod(field.getName(), pojoClass, field.getType()));
				getExcelFieldList(targetId, POIPublicUtil.getClassFields(field.getType()), field.getType(), temp,
						newMethods);
			}
		}
	}

	/**
	 * 把这个注解解析放到类型对象中
	 * 
	 * @param targetId
	 * @param field
	 * @param excelEntity
	 * @param pojoClass
	 * @param getMethods
	 * @param temp
	 * @throws Exception
	 */
	private void addEntityToMap(String targetId, Field field, ExcelImportEntity excelEntity, Class<?> pojoClass,
			List<Method> getMethods, Map<String, ExcelImportEntity> temp) throws Exception {
		Excel excel = field.getAnnotation(Excel.class);
		excelEntity = new ExcelImportEntity();
		excelEntity.setType(excel.type());
		excelEntity.setSaveUrl(excel.savePath());
		excelEntity.setSaveType(excel.imageType());
		excelEntity.setReplace(excel.replace());
		excelEntity.setDatabaseFormat(excel.databaseFormat());
		excelEntity.setVerify(getImportVerify(field));
		getExcelField(targetId, field, excelEntity, excel, pojoClass);
		if (getMethods != null) {
			List<Method> newMethods = new ArrayList<Method>();
			newMethods.addAll(getMethods);
			newMethods.add(excelEntity.getMethod());
			excelEntity.setMethods(newMethods);
		}
		//TODO 这里利用ExcelImportEntity的name来和importExcel方法中的titlemap的key做匹配,也就是说用配置的@Excel的name和导入excel中的表头作匹配,若表头有重名,比如都叫"创建时间",是否会出问题?
		temp.put(excelEntity.getName(), excelEntity);

	}

	/**
	 * 获取导入校验参数
	 * 
	 * @param field
	 * @return
	 */
	private ExcelVerifyEntity getImportVerify(Field field) {
		ExcelVerify verify = field.getAnnotation(ExcelVerify.class);
		if (verify != null) {
			ExcelVerifyEntity entity = new ExcelVerifyEntity();
			entity.setEmail(verify.isEmail());
			entity.setInterHandler(verify.interHandler());
			entity.setMaxLength(verify.maxLength());
			entity.setMinLength(verify.minLength());
			entity.setMobile(verify.isMobile());
			entity.setNotNull(verify.notNull());
			entity.setRegex(verify.regex());
			entity.setRegexTip(verify.regexTip());
			entity.setTel(verify.isTel());
			return entity;
		}
		return null;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年11月25日 上午11:14:14
	 * @Decription 构造基本属性
	 *
	 * @param targetId
	 * @param field
	 * @param excelEntity
	 * @param excel
	 * @param pojoClass
	 * @throws Exception
	 */
	private void getExcelField(String targetId, Field field, ExcelImportEntity excelEntity, Excel excel,
			Class<?> pojoClass) throws Exception {
		String fieldname = field.getName();
		excelEntity.setName(getExcelName(excel.name(), targetId));
		excelEntity.setField(fieldname);
		excelEntity.setMethod(POIPublicUtil.getMethod(fieldname, pojoClass, field.getType()));
		if (StringUtils.isEmpty(excel.importFormat())) {
			excelEntity.setFormat(excel.format());
		} else {
			excelEntity.setFormat(excel.importFormat());
		}
	}

	/**
	 * 判断在这个单元格显示的名称(不同导出情况时,取不同的导出标题)
	 * 
	 * @param exportName
	 * @param targetId
	 * @return
	 */
	private String getExcelName(String exportName, String targetId) {
		if (exportName.indexOf("_") < 0) {
			return exportName;
		}
		String[] arr = exportName.split(",");
		for (String str : arr) {
			if (str.indexOf(targetId) != -1) {
				return str.split("_")[0];
			}
		}
		return null;
	}

}
