package com.xplatform.base.poi.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.Validate;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xplatform.base.poi.excel.annotation.Excel;
import com.xplatform.base.poi.excel.annotation.ExcelCollection;
import com.xplatform.base.poi.excel.annotation.ExcelEntity;
import com.xplatform.base.poi.excel.annotation.ExcelIgnore;
import com.xplatform.base.poi.excel.entity.vo.PoiBaseConstants;

public class POIPublicUtil {
	private final static Logger logger = LoggerFactory.getLogger(POIPublicUtil.class);

	public static String getWebRootPath(String filePath) {
		// 这个path还是要测试的
		String path = POIPublicUtil.class.getClassLoader().getResource("").getPath() + filePath;
		path = path.replace("WEB-INF/classes/", "");
		path = path.replace("file:/", "");
		return path;
	}

	/**
	 * 获取class的 包括父类的
	 * 
	 * @param clazz
	 * @return
	 */
	public static Field[] getClassFields(Class<?> clazz) {
		List<Field> list = new ArrayList<Field>();
		Field[] fields;
		do {
			fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				list.add(fields[i]);
			}
			clazz = clazz.getSuperclass();
		} while (clazz != Object.class && clazz != null);
		return list.toArray(fields);
	}

	/**
	 * 判断是不是集合的实现类
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isCollection(Class<?> clazz) {
		return Collection.class.isAssignableFrom(clazz);
	}

	/**
	 * 判断是否不要在这个excel操作中
	 * 
	 * @param
	 * @param field
	 * @param targetId
	 * @return
	 */
	public static boolean isNotUserExcelUserThis(List<String> exclusionsList, Field field, String targetId) {
		boolean boo = true;
		if (field.getAnnotation(ExcelIgnore.class) != null) {
			boo = true;
		} else if (boo && field.getAnnotation(ExcelCollection.class) != null
				&& isUseInThis(field.getAnnotation(ExcelCollection.class).name(), targetId)
				&& (exclusionsList == null || !exclusionsList.contains(field.getName()))) {
			boo = false;
		} else if (boo && field.getAnnotation(Excel.class) != null
				&& isUseInThis(field.getAnnotation(Excel.class).name(), targetId)
				&& (exclusionsList == null || !exclusionsList.contains(field.getName()))) {
			boo = false;
		} else if (boo && field.getAnnotation(ExcelEntity.class) != null
				&& isUseInThis(field.getAnnotation(ExcelEntity.class).name(), targetId)
				&& (exclusionsList == null || !exclusionsList.contains(field.getName()))) {
			boo = false;
		}
		return boo;
	}

	/**
	 * 判断是不是使用
	 * 
	 * @param exportName
	 * @param targetId
	 * @return
	 */
	private static boolean isUseInThis(String exportName, String targetId) {
		return targetId == null || exportName.equals("") || exportName.indexOf("_") < 0
				|| exportName.indexOf(targetId) != -1;
	}

	/**
	 * 是不是java基础类
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isJavaClass(Field field) {
		Class<?> fieldType = field.getType();
		boolean isBaseClass = false;
		if (fieldType.isArray()) {
			isBaseClass = false;
		} else if (fieldType.isPrimitive() || fieldType.getPackage() == null
				|| fieldType.getPackage().getName().equals("java.lang")
				|| fieldType.getPackage().getName().equals("java.math")
				|| fieldType.getPackage().getName().equals("java.util")) {
			isBaseClass = true;
		}
		return isBaseClass;
	}

	/**
	 * 彻底创建一个对象(主要包含newInstance之后,其中的集合类型new什么类型,其中的实体类型继续向下newInstance)
	 * 
	 * @param clazz
	 * @return
	 */
	public static Object createObject(Class<?> clazz, String targetId) {
		Object obj = null;
		Method setMethod;
		try {
			obj = clazz.newInstance();
			Field[] fields = getClassFields(clazz);
			for (Field field : fields) {
				if (isNotUserExcelUserThis(null, field, targetId)) {
					continue;
				}
				if (isCollection(field.getType())) {
					//仅仅是实例化集合类型的功能(比如new ArrayList),从ExcelCollection中获得是ArrayList还是LinkedList
					ExcelCollection collection = field.getAnnotation(ExcelCollection.class);
					setMethod = getMethod(field.getName(), clazz, field.getType());
					setMethod.invoke(obj, collection.type().newInstance());
				} else if (!isJavaClass(field)) {
					//如果不是java基础类型,又不是集合类型说明是自定义的实体类型
					setMethod = getMethod(field.getName(), clazz, field.getType());
					//递归继续向下创建
					setMethod.invoke(obj, createObject(field.getType(), targetId));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("创建对象异常");
		}
		return obj;

	}

	/**
	 * 获取GET方法
	 * 
	 * @param name
	 * @param pojoClass
	 * @return
	 * @throws Exception
	 */
	public static Method getMethod(String name, Class<?> pojoClass) throws Exception {
		StringBuffer getMethodName = new StringBuffer(PoiBaseConstants.GET);
		getMethodName.append(name.substring(0, 1).toUpperCase());
		getMethodName.append(name.substring(1));
		Method method = null;
		try {
			method = pojoClass.getMethod(getMethodName.toString(), new Class[] {});
		} catch (Exception e) {
			method = pojoClass.getMethod(getMethodName.toString().replace(PoiBaseConstants.GET, PoiBaseConstants.IS),
					new Class[] {});
		}
		return method;
	}

	/**
	 * 获取SET方法
	 * 
	 * @param name
	 * @param pojoClass
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static Method getMethod(String name, Class<?> pojoClass, Class<?> type) throws Exception {
		StringBuffer getMethodName = new StringBuffer(PoiBaseConstants.SET);
		getMethodName.append(name.substring(0, 1).toUpperCase());
		getMethodName.append(name.substring(1));
		return pojoClass.getMethod(getMethodName.toString(), new Class[] { type });
	}

	/**
	 * @param photoByte
	 * @return
	 */
	public static String getFileExtendName(byte[] photoByte) {
		String strFileExtendName = "JPG";
		if ((photoByte[0] == 71) && (photoByte[1] == 73) && (photoByte[2] == 70) && (photoByte[3] == 56)
				&& ((photoByte[4] == 55) || (photoByte[4] == 57)) && (photoByte[5] == 97)) {
			strFileExtendName = "GIF";
		} else if ((photoByte[6] == 74) && (photoByte[7] == 70) && (photoByte[8] == 73) && (photoByte[9] == 70)) {
			strFileExtendName = "JPG";
		} else if ((photoByte[0] == 66) && (photoByte[1] == 77)) {
			strFileExtendName = "BMP";
		} else if ((photoByte[1] == 80) && (photoByte[2] == 78) && (photoByte[3] == 71)) {
			strFileExtendName = "PNG";
		}
		return strFileExtendName;
	}

	/**
	 * 获取Excel2003图片
	 * 
	 * @param sheet
	 *            当前sheet对象
	 * @param workbook
	 *            工作簿对象
	 * @return Map key:图片单元格索引（1_1）String，value:图片流PictureData
	 */
	public static Map<String, PictureData> getSheetPictrues03(HSSFSheet sheet, HSSFWorkbook workbook) {
		Map<String, PictureData> sheetIndexPicMap = new HashMap<String, PictureData>();
		List<HSSFPictureData> pictures = workbook.getAllPictures();
		if (pictures.size() != 0) {
			for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {
				HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();
				if (shape instanceof HSSFPicture) {
					HSSFPicture pic = (HSSFPicture) shape;
					int pictureIndex = pic.getPictureIndex() - 1;
					HSSFPictureData picData = pictures.get(pictureIndex);
					String picIndex = String.valueOf(anchor.getRow1()) + "_" + String.valueOf(anchor.getCol1());
					sheetIndexPicMap.put(picIndex, picData);
				}
			}
			return sheetIndexPicMap;
		} else {
			return null;
		}
	}

	/**
	 * 获取Excel2007图片
	 * 
	 * @param sheet
	 *            当前sheet对象
	 * @param workbook
	 *            工作簿对象
	 * @return Map key:图片单元格索引（1_1）String，value:图片流PictureData
	 */
	public static Map<String, PictureData> getSheetPictrues07(XSSFSheet sheet, XSSFWorkbook workbook) {
		Map<String, PictureData> sheetIndexPicMap = new HashMap<String, PictureData>();
		for (POIXMLDocumentPart dr : sheet.getRelations()) {
			if (dr instanceof XSSFDrawing) {
				XSSFDrawing drawing = (XSSFDrawing) dr;
				List<XSSFShape> shapes = drawing.getShapes();
				for (XSSFShape shape : shapes) {
					XSSFPicture pic = (XSSFPicture) shape;
					XSSFClientAnchor anchor = pic.getPreferredSize();
					CTMarker ctMarker = anchor.getFrom();
					String picIndex = ctMarker.getRow() + "_" + ctMarker.getCol();
					sheetIndexPicMap.put(picIndex, pic.getPictureData());
				}
			}
		}
		return sheetIndexPicMap;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年11月25日 下午6:38:36
	 * @Decription 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 *
	 * @param obj
	 * @param fieldName
	 * @param value
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			logger.error("Could not find field [" + fieldName + "] on target [" + obj + "]");
			//			throw new IllegalArgumentException();
			return;
		}

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 
	 * @param obj 目标对象
	 * @param fieldName 属性名称
	 * @return 转型后的类型
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(fieldName, "fieldName can't be blank");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {
				//logger.info(e.getMessage());
				//NOSONAR
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 * 
	 * @param obj 目标对象
	 * @param fieldName 属性名称
	 * @return 属性值
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e.getMessage());
		}
		return result;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年11月26日 下午6:00:08
	 * @Decription java通过序列化和反序列化的实现的深度复制(据说效率低)
	 *
	 * @param src
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static List deepCopy(List src) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(src);
		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);
		List dest = (List) in.readObject();
		return dest;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年12月4日 下午11:26:38
	 * @Decription excel删除整行的方法
	 *
	 * @param sheet
	 * @param rowIndex
	 */
	public static void removeRow(Sheet sheet, int rowIndex) {
		int lastRowNum = sheet.getLastRowNum();
		if (rowIndex >= 0 && rowIndex < lastRowNum)
			sheet.shiftRows(rowIndex + 1, lastRowNum, -1);//将行号为rowIndex+1一直到行号为lastRowNum的单元格全部上移一行，以便删除rowIndex行  
		if (rowIndex == lastRowNum) {
			Row removingRow = sheet.getRow(rowIndex);
			if (removingRow != null)
				sheet.removeRow(removingRow);
		}
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年12月7日 下午1:14:47
	 * @Decription 从InputStream中解析获得Workbook
	 *
	 * @param inputStream
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static Workbook getWorkBook(InputStream inputStream) throws InvalidFormatException, IOException {
		Workbook book = null;
		if (!(inputStream.markSupported())) {
			inputStream = new PushbackInputStream(inputStream, 8);
		}
		if (POIFSFileSystem.hasPOIFSHeader(inputStream)) {
			book = new HSSFWorkbook(inputStream);
		} else if (POIXMLDocument.hasOOXMLHeader(inputStream)) {
			book = new XSSFWorkbook(OPCPackage.open(inputStream));
		}
		return book;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年12月7日 下午1:25:16
	 * @Decription 通过构造临时文件将workbook转化和和其无关的文件流
	 *
	 * @param workbook
	 * @return
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 */
	public static File copyWorkBookToFile(Workbook workbook) throws IOException, InvalidFormatException {
		String tempFileName = UUID.randomUUID().toString();
		File file = new File(tempFileName);
		FileOutputStream out = new FileOutputStream(file);
		workbook.write(out);
		out.flush();
		out.close();
		return file;
	}
}
