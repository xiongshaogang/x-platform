package com.xplatform.base.framework.core.util.pdf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.lang.ObjectUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.springframework.context.i18n.LocaleContextHolder;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.FileUtils;

public class PDFUtil {

	private static final ResourceBundle config = ResourceBundle
			.getBundle("sysConfig");
	
	
	private static String uploadFileRootDir;
	private static String fontFileRootDir; // 字体文件路径
	private static String pdfFileRootDir;// 生成PDF文件的路径
	
	private static String pdfFileRootDir2;// 生成PDF文件条形码的路径
	private static String domainegrant;
	
	private String SIMSUN = "SimSun.ttc"; // 字体-宋体
	private String SIMHEI = "SimHei.ttf"; // 字体-黑体
	private String ARIAL = "Arial.ttf"; // 字体-Arial
	private final String ARIAL_B = "ArialBd.ttf"; // 字体-Arial 粗体
	private final String TIMES = "Times.ttf"; // 字体-Arial
	private final String TIMES_B = "TimesBd.ttf"; // 字体-Arial 粗体
	private final String ARIAL_I = "ArialI.ttf"; // 字体-Arial
	private final String SONGT = "SongT.ttf"; // 字体-Arial
	private final String JDHEI = "JDHei.ttf"; // 字体-经典黑体加粗
	private final String MSHEI = "MSHei.ttf"; // 字体-Arial
	private final String STXINGKAI = "STxingkai.ttf"; // 字体-Arial
	private final String KAITI = "KaiTi_GB2312.ttf";
	private final String STCAIYUN = "STCaiyun.ttf"; // 彩云空心字体
	private static final String UTF8 = "UTF-8"; // 编码-UTF-8
	private final int FONT_SIZE_9 = 9;
	private final int FONT_SIZE_10 = 10;

	private static final String PDF_SUFFIX_NAME = "pdf"; // PDF后缀名(文件类型)
	private static final String BARCODE_PATH = "prp_version_barcode"; // 用于查找条形码的路径和文件
	private static final String PIC_SUFFIX_NAME = "jpg"; // PDF后缀名(文件类型)
	

	private static final Color WATERMARK_FONT_COLOR = Color.LIGHT_GRAY; // 水印字体默认颜色
	private static final int WATERMARK_FONT_SIZE = 60; // 水印默认字体大小
	private static final int ROTATION = 45; // 水印旋转角度
	private static final String PAGETYPE = "pageType"; // 页码类型
	private static final String HEADERLEFT = "headerLeft"; // 页眉位置:左 一般为图片
	private static final String HEADERCENTER = "headerCenter"; // 页眉位置:中
	private static final String HEADERRIGHT = "headerRight"; // 页眉位置:右
	private static final String FOOTERLEFT = "footerLeft"; // 页脚位置：中
	private static final String FOOTERCENTER = "footerCenter";// 页脚位置：中
	private static final String FOOTERRIGHT = "footerRight";// 页脚位置：右

	
	static{
		uploadFileRootDir = getUploadFileRootDir();
		fontFileRootDir = getFontFileRootDir();
		pdfFileRootDir = getPdfFileRootDir();
		pdfFileRootDir2 = getPdfFileRootDir2();
		domainegrant = getDomainegrant();
	}
	
	
	public static String getUploadFileRootDir() {
		return config.getString("uploadFileRootDir");
	}

	public static String getFontFileRootDir() {
		return config.getString("fontDir");
	}

	public static String getPdfFileRootDir() {
		return config.getString("pdfDir");
	}

	public static String getPdfFileRootDir2() {
		return config.getString("pdfDir2");
	}

	public static String getDomainegrant() {
		return config.getString("serverUrl");
	}

	/**
	 * 生成申报书条形码.
	 * 
	 */
	public String createBarCode(String code) throws Exception {

		try {
			// Create the barcode bean
			Code128Bean bean = new Code128Bean();
			final int dpi = 100;
			// Configure the barcode generator
			bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); // makes the narrow
			bean.doQuietZone(false);
			// Open output file
			File filePath = new File(pdfFileRootDir2);
			// 判断文件夹是否存在,如果不存在则创建文件夹
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			String fileName = getTempName(pdfFileRootDir2, PIC_SUFFIX_NAME);
			File outputFile = new File(fileName);
			OutputStream out = new FileOutputStream(outputFile);
			try {
				// Set up the canvas provider for monochrome JPEG output
				BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/jpeg", dpi,
						BufferedImage.TYPE_BYTE_BINARY, false, 0);
				// Generate the barcode
				bean.generateBarcode(canvas, code);
				// Signal end of generation
				canvas.finish();
			} finally {
				out.close();
			}
			return fileName;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 在pdf文件中添加水印.
	 * 
	 * @param outputFile
	 *            水印输出文件
	 * @param waterMarkName
	 *            水印名字
	 * @param rotation
	 *            旋转角度
	 */

	public void waterMark(String outputFile, String waterMarkName, float rotation) throws Exception {
		waterMark(outputFile, waterMarkName, rotation, WATERMARK_FONT_COLOR); // 默认颜色为浅灰色
	}

	public void waterMark(String outputFile, String waterMarkName, Color color) throws Exception {
		waterMark(outputFile, waterMarkName, ROTATION, color); // 默认角度为45
	}

	/**
	 * 在pdf文件中添加水印.
	 * 
	 * @param outputFile
	 *            水印输出文件
	 * @param waterMarkName
	 *            水印名字
	 * @throws Exception
	 */
	public void waterMark(String outputFile, String waterMarkName) throws Exception {
		waterMark(outputFile, waterMarkName, ROTATION, WATERMARK_FONT_COLOR); // 默认角度为45,默认颜色为浅灰色
	}

	public void waterMark(String outputFile, String waterMarkName, boolean isHomePage) throws Exception {
		waterMark(outputFile, waterMarkName, ROTATION, WATERMARK_FONT_COLOR, isHomePage); // 默认角度为45,默认颜色为浅灰色
	}

	public void waterMark(String outputFile, String waterMarkName, float rotation, Color color) throws Exception {
		waterMark(outputFile, waterMarkName, rotation, color, true);
	}

	public void waterMark(String outputFile, String waterMarkName, float rotation, Color color, boolean isHomePage)
			throws Exception {

		int total = 0;
		String tempPdf = getTempName(pdfFileRootDir, PDF_SUFFIX_NAME);
		FileUtils.copyFile(outputFile, tempPdf); // 复制一个临时文件
		PdfReader reader = new PdfReader(tempPdf);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFile));
		total = reader.getNumberOfPages() + 1;
		PdfContentByte under;

		// 生成水印图片
		Image image = null;
		if (waterMarkName != null && !waterMarkName.trim().equals("")) {
			createJpgByFont(waterMarkName, buildAbsoluteFilePath("PDF") + waterMarkName + "_tmp.jpg");
			image = Image.getInstance(buildAbsoluteFilePath("PDF") + waterMarkName + "_tmp.jpg");
			image.setAbsolutePosition(40, 200);
		}

		// com.lowagie.text.Font font = FontFactory.getFont(buildAbsoluteFilePath("FONT") + SIMHEI, BaseFont.IDENTITY_H,
		// BaseFont.NOT_EMBEDDED, WATERMARK_FONT_SIZE, Font.PLAIN, color);
		int start = 0;
		if (isHomePage) {
			start = 1;
		}
		PdfContentByte pcb;
		for (int i = 1; i < total; i++) {
			Rectangle pageSize = reader.getPageSize(i);
			pcb = stamper.getOverContent(i); // 将水印置于顶层 解决上传PDF文件图片或文字遮住水印
			PdfGState gs = new PdfGState();
			gs.setFillOpacity(0.3f); // 将水印调淡，不让遮盖PDF上的文字
			pcb.setGState(gs);
			showText2PDF(pcb, Element.ALIGN_CENTER, waterMarkName, pageSize.getWidth() / 2, pageSize.getHeight() / 2,
					rotation, WATERMARK_FONT_SIZE, SIMHEI, BaseColor.LIGHT_GRAY);

		}
		for (int i = 1; i < total; i++) {
			Rectangle pageSize = reader.getPageSize(i);
			under = stamper.getUnderContent(i);

			// 添加水印图
			if (null != image) {
				// under.addImage(image);
			}

			// under.beginText();

			// 添加水印
			// ColumnText.showTextAligned(under, Element.ALIGN_CENTER, new Phrase("111111111111"),
			// pageSize.getWidth() / 2, pageSize.getHeight() / 2, rotation);
			// under.endText();

			// 增加内容

			// BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			BaseFont bf = BaseFont.createFont(buildAbsoluteFilePath("FONT") + SIMHEI, BaseFont.IDENTITY_H,
					BaseFont.NOT_EMBEDDED);
			under.beginText();
			if (i > start) { // 首页不设置页码 ，从第二页开始
				under.setTextMatrix(280, 20);
				under.setFontAndSize(bf, 10);
				// under.showText("第" + (i - 1) + "页  共" + (total - 2) + "页");

				under.showTextAlignedKerned(Element.ALIGN_CENTER, (i - start) + "/" + (total - start - 1),
						pageSize.getWidth() / 2, 30, 0);
			}
			under.endText();

		}

		stamper.close();
		// 删除临时文件
		deleteFile(tempPdf);// 删除临时文件
	}

	/**
	 * @功能:直接在PDF上显示文字 --解决Adobe Acrobat 7.0 Professional 版本以下查看报错
	 * @param PdfContentByte
	 * @param alignment
	 *            Element.xxx对齐方式
	 * @param content
	 *            文字内容
	 * @param x
	 *            -y 坐标X Y
	 * @param rotation
	 *            旋转角度
	 * @param fontSize
	 *            字体大小，默认为10
	 * @param font
	 *            可选参数有：ARIAL,SONGT,SIMHEI ...
	 * @param color
	 *            字体颜色
	 * */
	public void showText2PDF(PdfContentByte canvas, int alignment, String content, float x, float y, float rotation) {
		com.itextpdf.text.Font f = FontFactory.getFont(buildAbsoluteFilePath("FONT") + SIMHEI, BaseFont.IDENTITY_H,
				BaseFont.NOT_EMBEDDED, FONT_SIZE_10, Font.PLAIN, BaseColor.BLACK);
		ColumnText.showTextAligned(canvas, alignment, new Phrase(content, f), x, y, rotation);
	}

	public void showText2PDF(PdfContentByte canvas, int alignment, String content, float x, float y) {
		com.itextpdf.text.Font f = FontFactory.getFont(buildAbsoluteFilePath("FONT") + SONGT, BaseFont.IDENTITY_H,
				BaseFont.NOT_EMBEDDED, FONT_SIZE_10, Font.PLAIN, BaseColor.BLACK);
		ColumnText.showTextAligned(canvas, alignment, new Phrase(content, f), x, y, 0);
	}

	public void showText2PDF(PdfContentByte canvas, int alignment, String content, float x, float y, int fontSize) {
		com.itextpdf.text.Font f = FontFactory.getFont(buildAbsoluteFilePath("FONT") + SONGT, BaseFont.IDENTITY_H,
				BaseFont.NOT_EMBEDDED, fontSize, Font.PLAIN, BaseColor.BLACK);
		ColumnText.showTextAligned(canvas, alignment, new Phrase(content, f), x, y, 0);
	}

	public void showText2PDF(PdfContentByte canvas, int alignment, String content, float x, float y, int fontSize,
			String font) {
		com.itextpdf.text.Font f = FontFactory.getFont(buildAbsoluteFilePath("FONT") + font, BaseFont.IDENTITY_H,
				BaseFont.NOT_EMBEDDED, fontSize, Font.PLAIN, BaseColor.BLACK);
		ColumnText.showTextAligned(canvas, alignment, new Phrase(content, f), x, y, 0);
	}

	public void showText2PDF(PdfContentByte canvas, int alignment, String content, float x, float y, float rotation,
			int fontSize, String font) {
		com.itextpdf.text.Font f = FontFactory.getFont(buildAbsoluteFilePath("FONT") + font, BaseFont.IDENTITY_H,
				BaseFont.NOT_EMBEDDED, fontSize, Font.PLAIN, BaseColor.BLACK);
		ColumnText.showTextAligned(canvas, alignment, new Phrase(content, f), x, y, rotation);
	}

	public void showText2PDF(PdfContentByte canvas, int alignment, String content, float x, float y, float rotation,
			int fontSize, String font, BaseColor color) {
		com.itextpdf.text.Font f = FontFactory.getFont(buildAbsoluteFilePath("FONT") + font, BaseFont.IDENTITY_H,
				BaseFont.NOT_EMBEDDED, fontSize, Font.PLAIN, color);
		ColumnText.showTextAligned(canvas, alignment, new Phrase(content, f), x, y, rotation);
	}

	public void showText2PDF(PdfContentByte canvas, int alignment, String content, float x, float y, float rotation,
			int fontSize) {
		com.itextpdf.text.Font f = FontFactory.getFont(buildAbsoluteFilePath("FONT") + SONGT, BaseFont.IDENTITY_H,
				BaseFont.NOT_EMBEDDED, fontSize, Font.PLAIN, BaseColor.BLACK);
		ColumnText.showTextAligned(canvas, alignment, new Phrase(content, f), x, y, rotation);
	}

	/**
	 * 生成水印的图片
	 * 
	 * 
	 */
	private void createJpgByFont(String str, String jpgName) {

		try {
			// 宽度 高度
			final int smallWidth = 60;
			BufferedImage bimage = new BufferedImage((str.length() + 5) * smallWidth / 2, smallWidth,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bimage.createGraphics();
			g.setColor(Color.WHITE); // 背景色
			g.fillRect(0, 0, smallWidth * (str.length() + 2), smallWidth); // 画一个矩形

			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// 去除锯齿(当设置的字体过大的时候,会出现锯齿)
			g.setColor(Color.LIGHT_GRAY); // 字的颜色
			File file = new File(buildAbsoluteFilePath("FONT") + "Arial.ttf"); // 字体文件
			Font font = Font.createFont(Font.TRUETYPE_FONT, file);
			// 根据字体文件所在位置,创建新的字体对象(此语句在jdk1.5下面才支持)
			g.setFont(font.deriveFont((float) smallWidth));

			g.drawString(str, 0, smallWidth);

			// 旋转图片
			bimage = rotateImage(bimage, -45);

			// 在指定坐标除添加文字
			g.dispose();
			FileOutputStream out = new FileOutputStream(jpgName); // 指定输出文件
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage);
			param.setQuality(50f, true);
			encoder.encode(bimage, param); // 存盘
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 图片旋转
	 * 
	 * 
	 */
	private BufferedImage rotateImage(final BufferedImage bufferedimage, final int degree) {
		int w = bufferedimage.getWidth();
		int h = bufferedimage.getHeight();
		int b = w > h ? w : h;
		int type = bufferedimage.getColorModel().getTransparency();
		BufferedImage img;
		Graphics2D graphics2d;
		graphics2d = (img = new BufferedImage(b, b, type)).createGraphics();
		graphics2d.setColor(Color.WHITE); // 背景色
		graphics2d.fillRect(0, 0, b, b); // 画一个矩形

		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2d.setColor(Color.WHITE); // 背景色
		graphics2d.rotate(Math.toRadians(degree), w, h);
		graphics2d.drawImage(bufferedimage, -(int) (b * 0.28) / 2, 0, null);
		graphics2d.dispose();
		return img;
	}

	/**
	 * 
	 * 拼接PDF （可根据所在的页码插入）.
	 * 
	 * 
	 * */
	@SuppressWarnings("unused")
	private String mosaicPdf(String subfile, String outputFile, Integer index) throws Exception {

		String outputFileCopy = getTempName(pdfFileRootDir, PDF_SUFFIX_NAME);
		FileUtils.copyFile(outputFile, outputFileCopy); // 复制一个临时文件
		String tempFile = getTempName(pdfFileRootDir, PDF_SUFFIX_NAME);
		Document document = new Document();
		// 建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
		PdfWriter.getInstance(document, new FileOutputStream(tempFile));
		document.open();
		Paragraph chunk = new Paragraph(" ");
		document.add(chunk);
		document.close();
		PdfReader reader = new PdfReader(outputFileCopy);
		PdfReader reader2 = new PdfReader(subfile);
		PdfReader reader3 = new PdfReader(tempFile);

		PdfStamper stamper = new PdfStamper(reader3, new FileOutputStream(outputFile));
		try {
			int total = reader.getNumberOfPages();
			int total2 = reader2.getNumberOfPages();

			PdfContentByte under;

			// 从现有的别的pdf合并过来
			for (int i = 1; i < total + total2; i++) {
				stamper.insertPage(i, PageSize.A4);
				under = stamper.getUnderContent(i);
				if (i > index && i < index + total2) { // 根据index页码在PDF中插入需要插入的PDF
					under.addTemplate(stamper.getImportedPage(reader2, i - index), 1, 0, 0, 1, 0, 0);
				} else {
					if (i >= index + total2) {
						under.addTemplate(stamper.getImportedPage(reader, i - total2), 1, 0, 0, 1, 0, 0);
					} else {
						under.addTemplate(stamper.getImportedPage(reader, i), 1, 0, 0, 1, 0, 0);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stamper.close();
			deleteFile(tempFile); // 删除临时文件
			deleteFile(outputFileCopy);
		}
		return null;
	}

	/**
	 * 
	 * 合并PDF.
	 * 
	 * @param list
	 *            需要合并的PDF路径
	 * @param fileName
	 *            合并生成的PDF文件名
	 * */
	public String mosaicPdf(List<String> list, String fileName) throws Exception {

		fileName = fileName.replace("/", "_");
		fileName = fileName.replace("\\", "_");

		if (list != null && list.size() > 0) {
			String savePath = getFileName(pdfFileRootDir, fileName, PDF_SUFFIX_NAME);
			Document document = new Document(new PdfReader(list.get(0)).getPageSize(1));
			PdfCopy copy = new PdfCopy(document, new FileOutputStream(savePath));
			document.open();
			for (int i = 0; i < list.size(); i++) {
				PdfReader reader = new PdfReader(list.get(i));
				int n = reader.getNumberOfPages();
				for (int j = 1; j <= n; j++) {
					document.newPage();
					PdfImportedPage page = copy.getImportedPage(reader, j);
					copy.addPage(page);
				}
			}
			document.close();
			/*
			 * String savePath = getFileName(pdfFileRootDir, fileName, PDF_SUFFIX_NAME); PdfReader reader = new
			 * PdfReader(list.get(0)); PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(savePath)); int
			 * total = reader.getNumberOfPages(); PdfContentByte under; int count = 0; for (String str : list) {
			 * PdfReader reader2 = new PdfReader(str); int total2 = reader2.getNumberOfPages() + 1; count = count +
			 * total2; for (int i = 1; i < total2; i++) { stamper.insertPage(count + i, PageSize.A4); under =
			 * stamper.getUnderContent(count + i); under.addTemplate(stamper.getImportedPage(reader2, i), 1, 0, 0, 1, 0,
			 * 0); } } stamper.close();
			 */
			for (String str : list) {
				if (str.indexOf("pdf/") != -1 || str.indexOf("pdf\\") != -1) {
					deleteFile(str);
				}
			}
			return savePath;

		} else {
			return null;
		}

	}

	/**
	 * 
	 * 设置页眉和页脚.
	 * 
	 * @throws IOException
	 * @param pdfFile
	 *            需要设置页眉页码的pdf文件
	 * @param header
	 *            页眉内容
	 * @param isHomePage
	 *            是否首页显示页码
	 * 
	 * */
	public void setPDFHeader(String pdfFile, Map<String, Object> header) throws IOException {
		setPDFHeader(pdfFile, header, true, false); // 默认首页不显示页码
	}

	/**
	 * 设置页眉和页脚.不兼容Adobe Acrobat 7.0
	 * 
	 * @param pdfFile
	 *            需要设置页眉页码的pdf文件
	 * @param header
	 *            页眉页脚内容
	 * @param isNoHomePage
	 *            首页是否显示页码
	 * @param isNoLastPape
	 *            尾页是否显示页码
	 * @throws IOException
	 */
	public void setPDFHeader(String pdfFile, Map<String, Object> header, boolean isNoHomePage, boolean isNoLastPape)
			throws IOException {
		int total = 0;
		String tempPdf = getTempName(pdfFileRootDir, PDF_SUFFIX_NAME);
		try {
			FileUtils.copyFile(pdfFile, tempPdf); // 复制一个临时文件
			PdfReader reader = new PdfReader(tempPdf);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pdfFile));
			total = reader.getNumberOfPages();
			PdfContentByte under;
			BaseFont bf = BaseFont.createFont(buildAbsoluteFilePath("FONT") + SIMHEI, BaseFont.IDENTITY_H,
					BaseFont.NOT_EMBEDDED);
			// BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			// if (isNoLastPape) { // 末页是否要页码
			// total = total - 1;
			// }
			String pageType = ObjectUtils.toString(header.get(PAGETYPE));
			if (pageType == null || "".equals(pageType)) {
				pageType = " 第[@current@]页  共[@total@]页 ";
			}
			for (int i = 1; i <= total; i++) {
				// 增加内容
				Rectangle pageSize = reader.getPageSize(i);
				under = stamper.getUnderContent(i);
				under.beginText();
				if (isNoHomePage) { // 判断首页是否要页码
					int count = 1;// 首页不设置页码第2页从1开始

					if (i > 1 && i != total) { // 首页不设置页码 ，从第二页开始
						under.setFontAndSize(bf, 10);
						under.showTextAlignedKerned(Element.ALIGN_CENTER,
								pageType.replace("[@current@]", i - count + "").replace("[@total@]", total - 1 + ""),
								pageSize.getWidth() / 2, 30, 0);

					} else {
						// 最后一页不要页码
						if (i == total && !isNoLastPape) {
							under.setFontAndSize(bf, 10);
							under.showTextAlignedKerned(Element.ALIGN_CENTER,
									pageType.replace("[@current@]", i - count + "")
											.replace("[@total@]", total - 1 + ""), pageSize.getWidth() / 2, 30, 0);
						}
					}

				} else {
					under.setFontAndSize(bf, 10);
					under.showTextAlignedKerned(Element.ALIGN_CENTER,
							pageType.replace("[@current@]", i + "").replace("[@total@]", total + ""),
							pageSize.getWidth() / 2, 30, 0);
				}

				// 设置页眉（由于本人不知道itext能不能支持对已存在pdf修改，只能暂时用这种方法 ：huangt注）
				String headerLeft = ObjectUtils.toString(header.get(HEADERLEFT));
				String headerCenter = ObjectUtils.toString(header.get(HEADERCENTER));
				String headerRight = ObjectUtils.toString(header.get(HEADERRIGHT));
				if (i > 1 && i < total) {
					under.setFontAndSize(bf, 10);
					setHeaderByMap(under, pageSize, header, true, true);
					under.saveState();
					// 如果没有传入页眉参数则不画线
					if ((headerLeft != null && !headerLeft.equals(""))
							|| (headerCenter != null && !headerCenter.equals(""))
							|| (headerRight != null && !headerRight.equals(""))) {

						under.setLineWidth(0.5f);
						under.moveTo(35, pageSize.getHeight() - 30);
						under.lineTo(pageSize.getWidth() - 35, pageSize.getHeight() - 30);
					}
					under.stroke();
					under.restoreState();
					under.endText();
				} else {
					under.setFontAndSize(bf, 10);
					if (i == total) {
						setHeaderByMap(under, pageSize, header, false, true);
						under.saveState();

						// 如果没有传入页眉参数则不画线
						if ((headerLeft != null && !headerLeft.equals(""))
								|| (headerCenter != null && !headerCenter.equals(""))
								|| (headerRight != null && !headerRight.equals(""))) {

							under.setLineWidth(0.5f);
							under.moveTo(35, pageSize.getHeight() - 30);
							under.lineTo(pageSize.getWidth() - 35, pageSize.getHeight() - 30);
						}
					} else if (i == 1) {
						setHeaderByMap(under, pageSize, header, false, false);
						under.saveState();
					}
					under.stroke();
					under.restoreState();
					under.endText();
				}

			}
			stamper.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 删除临时文件
		deleteFile(tempPdf);
	}

	/**
	 * 根据页眉页脚入参（位置，类型:用以@开头）设置页眉页脚.
	 * 
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws DocumentException
	 * 
	 * */
	public void setHeaderByMap(PdfContentByte under, Rectangle pageSize, Map<String, Object> header, boolean isFoot,
			boolean isHead) throws Exception {
		if (isHead) {
			String headerLeft = ObjectUtils.toString(header.get(HEADERLEFT));
			String headerCenter = ObjectUtils.toString(header.get(HEADERCENTER));
			String headerRight = ObjectUtils.toString(header.get(HEADERRIGHT));

			// 页眉左边
			if (isPath4Header(headerLeft)) {
				setHeaderByImage(under, pageSize, 35, pageSize.getHeight() - 25, getImagePath(headerLeft));
			} else {
				under.showTextAlignedKerned(Element.ALIGN_LEFT, headerLeft, 35, pageSize.getHeight() - 25, 0);
			}
			// 页眉中间
			if (isPath4Header(headerCenter)) {
				setHeaderByImage(under, pageSize, pageSize.getWidth() / 2, pageSize.getHeight() - 25,
						getImagePath(headerCenter));
			} else {
				under.showTextAlignedKerned(Element.ALIGN_CENTER, headerCenter, pageSize.getWidth() / 2,
						pageSize.getHeight() - 25, 0);
			}
			// 页眉右边
			if (isPath4Header(headerRight)) {
				setHeaderByImage(under, pageSize, pageSize.getWidth() - 35, pageSize.getHeight() - 25,
						getImagePath(headerRight), true);
			} else {
				under.showTextAlignedKerned(Element.ALIGN_RIGHT, headerRight, pageSize.getWidth() - 35,
						pageSize.getHeight() - 25, 0);
			}

		}
		if (isFoot) {
			String footerLeft = ObjectUtils.toString(header.get(FOOTERLEFT));
			String footerCenter = ObjectUtils.toString(header.get(FOOTERCENTER));
			String footerRight = ObjectUtils.toString(header.get(FOOTERRIGHT));

			// 页脚左边
			if (isPath4Header(footerLeft)) {
				setHeaderByImage(under, pageSize, 35, 25, getImagePath(footerLeft));
			} else {
				under.showTextAlignedKerned(Element.ALIGN_LEFT, footerLeft, 35, 25, 0);
			}
			// 页脚中间
			if (isPath4Header(footerCenter)) {
				setHeaderByImage(under, pageSize, pageSize.getWidth() / 2, 25, getImagePath(footerCenter));
			} else {
				under.showTextAlignedKerned(Element.ALIGN_CENTER, footerCenter, pageSize.getWidth() / 2, 25, 0);
			}
			// 页脚右边
			if (isPath4Header(footerRight)) {
				setHeaderByImage(under, pageSize, pageSize.getWidth() - 35, 25, getImagePath(footerRight), true);
			} else {
				under.showTextAlignedKerned(Element.ALIGN_RIGHT, footerRight, pageSize.getWidth() - 35, 25, 0);
			}
		}

	}

	public void setHeaderByImage(PdfContentByte under, Rectangle pageSize, float x, float y, String imagePath)
			throws Exception {
		if (imagePath != null && !"".equals(imagePath)) {
			Image image = Image.getInstance(imagePath);
			image.setAbsolutePosition(x, y);
			under.addImage(image);
		}
	}

	public void setHeaderByImage(PdfContentByte under, Rectangle pageSize, float x, float y, String imagePath,
			boolean isRight) throws Exception { // 右边的图片需要减去图片的宽度
		if (imagePath != null && !"".equals(imagePath)) {
			Image image = Image.getInstance(imagePath);
			if (isRight) {
				image.setAbsolutePosition(x - image.getWidth(), y);
			} else {
				image.setAbsolutePosition(x, y);
			}
			under.addImage(image);
		}
	}

	/**
	 * 页眉页脚插入图时,判断是否是图片路径,并返回图片的路径(因为传过来的值是以@开头的为路径).
	 * 
	 * */
	public boolean isPath4Header(String headerStr) {
		if (headerStr.startsWith("@")) {

			return true;
		}
		return false;
	}

	public String getImagePath(String headerStr) {
		return headerStr.substring(1);
	}

	/**
	 * 生成PDF.
	 * 
	 * @param urlStr
	 *            要生成PDF的页面URL
	 * @param map
	 *            页面需要用到的参数
	 * @return
	 * @throws Exception
	 */
	public String exportPdfFile(String urlStr, Map<String, Object> map) throws Exception {

		String outputFile = getTempName(pdfFileRootDir, PDF_SUFFIX_NAME);
		
		File filePath = new File(outputFile.substring(0, outputFile.lastIndexOf(File.separator)));

		// urlStr = URLDecoder.decode(urlStr, "UTF-8");
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		OutputStream os;
		try {
			String str ="";
			os = new FileOutputStream(outputFile);

			ITextRenderer renderer = new ITextRenderer();
			System.out.println("-------------------------------------------------"+urlStr);
			str = getHtmlFile(urlStr, map);	
			System.out.println(str);
			// renderer.setDocument(new File(str));
			renderer.setDocumentFromString(str);
			ITextFontResolver fontResolver = renderer.getFontResolver();
			fontResolver.addFont(buildAbsoluteFilePath("FONT") + SIMSUN, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED); // 宋体字
			fontResolver.addFont(buildAbsoluteFilePath("FONT") + SIMHEI, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED); // 黑体
			fontResolver.addFont(buildAbsoluteFilePath("FONT") + ARIAL, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED); // Arail
			renderer.layout();
			renderer.createPDF(os);
			os.flush();
			os.close();

			// 返回生成PDF文件的路径和名字 ,以保存数据库
			return outputFile;
		} catch (FileNotFoundException e) {
			// logger.error("不存在文件！" + e.getMessage());
			throw new Exception(e);
		} catch (DocumentException e) {
			// logger.error("生成pdf时出错了！" + e.getMessage());
			throw new Exception(e);
		} catch (IOException e) {
			// logger.error("pdf出错了！" + e.getMessage());
			throw new Exception(e);
		}

	}

	// 读取页面内容 add by huangt 2012.6.1
	public static String getHtmlFile(String urlStr, Map<String, Object> map) throws Exception {
		URL url;
		if (urlStr.indexOf("?") != -1) {
			urlStr = urlStr + "&locale=" + LocaleContextHolder.getLocale().toString();
		} else {
			urlStr = urlStr + "?locale=" + LocaleContextHolder.getLocale().toString();
		}

		// param = "xmlData=" + xmlData + "&jspUrl=" + map.get("jspUrl").toString(); // 传参
		/**
		 * @功能 修改原来获取网页内容的方式（获取网页内容出现信息不全） ，采用HttpClient 连接池 方式。
		 * */
		HttpClient httpClient = HttpUtils.getHttpClient();
		// 创建httppost
		HttpPost httpPost = new HttpPost(urlStr);
		// 创建参数队列
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();

		formparams.add(new BasicNameValuePair("keyCode", ObjectUtils.toString(map.get("keyCode"))));
		formparams.add(new BasicNameValuePair("type", ObjectUtils.toString(map.get("type"))));
		formparams.add(new BasicNameValuePair("jspUrl", ObjectUtils.toString(map.get("jspUrl"))));
		formparams.add(new BasicNameValuePair("include", ObjectUtils.toString(map.get("include"))));
		if (map.containsKey("barCodePath")) {
			formparams.add(new BasicNameValuePair("barCodePath", ObjectUtils.toString(map.get("barCodePath"))));
		}

		try {
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, HTTP.UTF_8);
			httpPost.setEntity(uefEntity);
			HttpResponse response;
			httpClient.getParams().setParameter("http.socket.timeout", new Integer(30000));
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() != 200) {
				httpPost.abort(); // 终止 此次链接
				return null;
			}
			if (entity != null) {
				final String htmlStr = EntityUtils.toString(entity, HTTP.UTF_8);
				return htmlStr;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpPost.abort();
			// httpClient.getConnectionManager().shutdown();
		}
		return null;

	}

	/**
	 * 删除临时文件 fileName:如果传入文件名,即不生成序列化的临时文件名.
	 * */
	public static void deleteFile(String fileName) {
		File file = new File(fileName);
		file.delete();
	}

	/**
	 * 生成序列化的临时文件名 fileDir:文件路径 type:文件类型,如jpg pdf 等 fileName:如果传入文件名,即不生成序列化的临时文件名.
	 * */
	public static String getTempName(String fileDir, String type) {
		String fileStr = fileDir + UUID.randomUUID().toString() + "." + type;
		if("pdf/".equals(fileDir)){
			try {
				fileStr = ApplicationContextUtil.getServletContext().getRealPath("/").replaceAll("%20", " ").replaceAll("/", File.separator+File.separator) + File.separator +"userfiles"+File.separator+"pdf"+File.separator + UUID.randomUUID().toString() + "." + type;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fileStr;
	}

	/**
	 * 生成文件名 fileDir:文件路径 type:文件类型,如jpg pdf 等 fileName:如果传入文件名,即不生成序列化的临时文件名.
	 * */
	public static String getFileName(String fileDir, String fileName, String type) {
		String fileStr = fileDir + fileName + "." + type;
		if("pdf/".equals(fileDir)){
			try {
				fileStr = ApplicationContextUtil.getServletContext().getRealPath("/").replaceAll("%20", " ").replaceAll("/", File.separator+File.separator) + File.separator +"userfiles"+File.separator+"pdf"+File.separator + fileName +"." + type;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fileStr;
	}

	public static String toUnicode(String s) throws Exception {

		byte[] bytes = s.getBytes("Unicode");
		String str;
		StringBuffer sb = new StringBuffer();
		for (int j = 2; j < bytes.length; j++) {
			str = Integer.toHexString(0xFF & bytes[j]);
			if (str.length() < 2) {
				sb.append(0);
			}
			sb.append(str);
		}
		return sb.toString();
	}

	private String buildAbsoluteFilePath(String type) {
		String apath = uploadFileRootDir;
		if (!apath.endsWith(File.separator)) {
			apath += File.separator;
		}
		if ("PDF".equalsIgnoreCase(type)) {

			apath += pdfFileRootDir;
		}
		if ("FONT".equalsIgnoreCase(type)) {
			apath += fontFileRootDir;
		}
		// if (!apath.endsWith(File.separator)) {
		// apath += File.separator;
		// }
		return apath;
	}

	/**
	 * 打印分割线(不兼容Adobe Acrobat 7.0)
	 * 
	 * @param outputFile
	 *            PDF文件路径
	 * @param dashWord
	 *            分割线名称
	 * @param isAll
	 *            是否全部打印 false:只打印第一页 true：全部打印
	 */
	public void dashLine(String outputFile, String dashWord, boolean isAll) throws Exception {
		int total = 0;
		String tempPdf = getTempName(pdfFileRootDir, PDF_SUFFIX_NAME);
		FileUtils.copyFile(outputFile, tempPdf); // 复制一个临时文件
		com.lowagie.text.pdf.PdfReader reader = new com.lowagie.text.pdf.PdfReader(tempPdf);
		com.lowagie.text.pdf.PdfStamper stamper = new com.lowagie.text.pdf.PdfStamper(reader, new FileOutputStream(
				outputFile));
		total = reader.getNumberOfPages() + 1;
		com.lowagie.text.pdf.PdfContentByte under;
		int size = isAll ? total : 2;
		for (int i = 1; i < size; i++) {
			under = stamper.getUnderContent(i);// 控制第几页设置装订线
			under.beginText();
			under.saveState();
			under.setColorStroke(WATERMARK_FONT_COLOR);
			under.setLineWidth(0.5f);
			under.setLineDash(1, 1, 0);
			under.moveTo(25, 200);
			under.lineTo(25, PageSize.A4.getHeight() - 150);
			under.stroke();
			under.restoreState();
			com.lowagie.text.Font tFont = com.lowagie.text.FontFactory.getFont(buildAbsoluteFilePath("FONT")
					+ "SimHei.ttf", BaseFont.IDENTITY_V, BaseFont.NOT_EMBEDDED, 10, Font.PLAIN, WATERMARK_FONT_COLOR);
			com.lowagie.text.pdf.ColumnText.showTextAligned(under, Element.ALIGN_LEFT, new com.lowagie.text.Phrase(
					dashWord, tFont), 25, PageSize.A4.getHeight() - (PageSize.A4.getHeight() - 2 * 150) / 2, 0);
			under.endText();
		}

		stamper.close();
		// 删除临时文件
		deleteFile(tempPdf);// 删除临时文件
	}

	/**
	 * 打印分割线（以图片的方式）
	 * 
	 * @param outputFile
	 *            PDF文件路径
	 * @param isAll
	 *            是否全部打印 false:只打印第一页 true：全部打印
	 */
	public void dashLineByImage(String outputFile, boolean isAll) throws Exception {
		int total = 0;
		String tempPdf = getTempName(pdfFileRootDir, PDF_SUFFIX_NAME);
		FileUtils.copyFile(outputFile, tempPdf); // 复制一个临时文件
		PdfReader reader = new PdfReader(tempPdf);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFile));
		total = reader.getNumberOfPages() + 1;
		PdfContentByte under;

		// 生成水印图片 加入装订线图片
		if (!new File(buildAbsoluteFilePath("PDF") + "zdx.jpg").exists()) {// 判断文件是否存在，如果不存在则创建新的图片
			creatZDXJPGFont();
		}
		Image image = Image.getInstance(buildAbsoluteFilePath("PDF") + "zdx.jpg");
		image.setAbsolutePosition(0, 100);

		int size = isAll ? total : 2;
		for (int i = 1; i < size; i++) {
			under = stamper.getUnderContent(i);// 控制第几页设置装订线
			// 加入装订线图片
			under.addImage(image);
		}

		stamper.close();
		// 删除临时文件
		deleteFile(tempPdf);// 删除临时文件
	}

	public void creatZDXJPGFont() {
		BufferedImage ImageNew = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB);// 图片大小为500*500
		Graphics2D g2 = (Graphics2D) ImageNew.getGraphics();
		g2.setColor(Color.white);// 图片背景为白色
		g2.fillRect(0, 0, 600, 600);// 背景面积及公位置
		g2.setColor(Color.LIGHT_GRAY);// 线条颜色
		Stroke bs;
		// LINE_TYPE_DASHED
		bs = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 2, 2 }, 0);
		g2.setStroke(bs);
		g2.drawLine(25, 10, 25, 600);

		g2.setFont(new Font("", Font.PLAIN, 12).deriveFont(1));
		g2.drawString("线", 20, 500);
		g2.drawString("订", 20, 300);
		g2.drawString("装", 20, 100);

		g2.setStroke(g2.getStroke());
		g2.dispose();// 释放资源
		File outFile = new File(buildAbsoluteFilePath("PDF") + "zdx.jpg");
		try {
			ImageIO.write(ImageNew, "jpg", outFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除该目录下一天以前的文件
	 * 
	 */
	public void deleteFiles(String dir){
		File file = new File(dir);
		if(file.isDirectory()){
			File[] files = file.listFiles();
				for(File f : files){
					if(f.lastModified() < (System.currentTimeMillis() - 3600 * 24 *1000)){
					   f.delete();
					}
				}
		}
	}
}