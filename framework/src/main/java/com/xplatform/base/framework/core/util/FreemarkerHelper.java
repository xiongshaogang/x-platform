package com.xplatform.base.framework.core.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 
 * description :模版解析类
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月17日 下午5:11:04
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- xiehs 2014年5月17日 下午5:11:04
 *
 */
public class FreemarkerHelper {
	private static Configuration _tplConfig = new Configuration();
	static {
		_tplConfig.setClassForTemplateLoading(FreemarkerHelper.class, "/");
	}

	/**
	 * 解析ftl
	 * 
	 * @param tplName
	 *            模板名
	 * @param encoding
	 *            编码
	 * @param paras
	 *            参数
	 * @return
	 */
	public static String parseTemplate(String tplName, String encoding, Map<String, Object> paras) {
		try {
			StringWriter swriter = new StringWriter();
			Template mytpl = null;
			mytpl = _tplConfig.getTemplate(tplName, encoding);
			mytpl.process(paras, swriter);
			return swriter.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}

	}

	public static String parseTemplate(String tplName, Map<String, Object> paras) {
		return parseTemplate(tplName, "utf-8", paras);
	}

	/**
	 * 字符串和文件模版合并
	 * 
	 * @author xiehs
	 * @createtime 2014年6月17日 上午11:33:43
	 * @Decription
	 *
	 * @param templateName
	 * @param model
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static String mergeTemplateIntoString(String templateName, Object model) throws IOException, TemplateException {
		Template template = _tplConfig.getTemplate(templateName);
		return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
	}

	/**
	 * 解析字符串模版
	 * 
	 * @author xiehs
	 * @createtime 2014年6月17日 上午11:32:45
	 * @Decription
	 *
	 * @param obj
	 *            数据
	 * @param templateSource
	 *            字符串模版
	 * @return 解析后字符串值
	 * @throws TemplateException
	 * @throws IOException
	 */
	public static String parseByStringTemplate(Object obj, String templateSource) throws TemplateException, IOException {
		Configuration cfg = new Configuration();
		StringTemplateLoader loader = new StringTemplateLoader();
		cfg.setTemplateLoader(loader);
		cfg.setClassicCompatible(true);
		loader.putTemplate("freemaker", templateSource);
		Template template = cfg.getTemplate("freemaker");
		StringWriter writer = new StringWriter();
		template.process(obj, writer);
		return writer.toString();
	}
}