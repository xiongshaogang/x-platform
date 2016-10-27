package com.xplatform.base.develop.codegenerate.generatetype.onetomany;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.xplatform.base.develop.codegenerate.generate.ICallBack;
import com.xplatform.base.develop.codegenerate.util.CodeResourceUtil;
import com.xplatform.base.develop.codegenerate.util.CodeStringUtils;

public class CgformCodeFactoryOneToMany {
	private ICallBack callBack;

	/**
	 * 得到freemaker的配置信息
	 * @return
	 * @throws IOException
	 */
	public Configuration getConfiguration() throws IOException {
		Configuration cfg = new Configuration();
		String path = getTemplatePath();
		File templateDirFile = new File(URLDecoder.decode(path, "UTF-8"));
		cfg.setDirectoryForTemplateLoading(templateDirFile);
		cfg.setLocale(Locale.CHINA);
		cfg.setDefaultEncoding("UTF-8");
		return cfg;
	}

	/**
	 * 生成文件
	 * @param templateFileName
	 * @param type
	 * @param data
	 * @throws TemplateException
	 * @throws IOException
	 */
	public void generateFile(String templateFileName, String type, Map data)
			throws TemplateException, IOException {
		try {
			String entityPackage = data.get("entityPackage").toString();
			String entityName = data.get("entityName").toString();
			String fileNamePath = getCodePath(type, entityPackage, entityName);
			String fileDir = StringUtils.substringBeforeLast(fileNamePath, "/");
			Template template = getConfiguration()
					.getTemplate(templateFileName);
			FileUtils.forceMkdir(new File(fileDir + "/"));
			Writer out = new OutputStreamWriter(new FileOutputStream(
					fileNamePath), CodeResourceUtil.SYSTEM_ENCODING);
			template.process(data, out);
			out.close();
		} catch (TemplateException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public String getProjectPath() {
		String path = CodeResourceUtil.getProject_path() + "/";
		return path;
	}
	
	public String getProjectPath(String type) {
		String path = CodeResourceUtil.getProject_path() + "/";
		if(type.contains("js")){
			path = StringUtils.substringBeforeLast(path.substring(0,path.length()-3), "/") +"/webapp/";
		}/*else{
			path = StringUtils.substringBeforeLast(path.substring(0,path.length()-3), "/") +"/"+ CodeResourceUtil.getJava_path() + "/";
		}*/
		return path;
	}

	public String getClassPath() {
		String path = getClass().getResource("/").getPath();
		return path;
	}

	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getContextClassLoader()
				.getResource("./").getPath());
	}

	/**
	 * 获取模版路径
	 * @return
	 */
	public String getTemplatePath() {
		String path = getClassPath() + CodeResourceUtil.TEMPLATEPATH;
		return path;
	}

	public String getCodePath(String type, String entityPackage,
			String entityName) {
		String path = getProjectPath(type);
		StringBuilder str = new StringBuilder();
		if (StringUtils.isNotBlank(type)) {
			String codeType = ((CodeType) Enum.valueOf(CodeType.class, type))
					.getValue();
			str.append(path);
			if (("jsp".equals(type)) || ("jspList".equals(type))
					|| ("jspEdit".equals(type)))
				str.append(CodeResourceUtil.JSPPATH);
			else {
				str.append(CodeResourceUtil.CODEPATH);
			}
			str.append(StringUtils.lowerCase(entityPackage).replace(".", "/"));
			str.append("/");
			if ("Action".equalsIgnoreCase(codeType))
				str.append(StringUtils.lowerCase("action"));
			else if ("ServiceImpl".equalsIgnoreCase(codeType))
				str.append(StringUtils.lowerCase("service/impl"));
			else if ("Service".equalsIgnoreCase(codeType))
				str.append(StringUtils.lowerCase("service"));
			else if("Dao".equalsIgnoreCase(codeType)){
				str.append(StringUtils.lowerCase("dao"));
			}else if("DaoImpl".equalsIgnoreCase(codeType)){
				str.append(StringUtils.lowerCase("dao/impl"));
			}else if("Edit".equalsIgnoreCase(codeType)){
			}else if (!"List".equalsIgnoreCase(codeType)) {
				str.append(StringUtils.lowerCase(codeType));
			}
			str.append("/");

			if (("jsp".equals(type)) || ("jspList".equals(type)) || "jspEdit".equals(type)) {
				String jspName = StringUtils.capitalize(entityName);

				str.append(CodeStringUtils.getInitialSmall(jspName));
				str.append(codeType);
				str.append(".jsp");
			} else if (("jsp_add".equals(type)) || ("jspList_add".equals(type))) {
				String jsName = StringUtils.capitalize(entityName);

				str.append(CodeStringUtils.getInitialSmall(jsName));
				str.append(codeType);
				str.append("-add.jsp");
			} else if (("jsp_update".equals(type))
					|| ("jspList_update".equals(type))) {
				String jsName = StringUtils.capitalize(entityName);

				str.append(CodeStringUtils.getInitialSmall(jsName));
				str.append(codeType);
				str.append("-update.jsp");
			} else if (("js".equals(type)) || ("jsList".equals(type))) {
				String jsName = StringUtils.capitalize(entityName);

				str.append(CodeStringUtils.getInitialSmall(jsName));
				str.append(codeType);
				str.append(".js");
			} else {
				str.append(StringUtils.capitalize(entityName));
				str.append(codeType);
				str.append(".java");
			}
		} else {
			throw new IllegalArgumentException("type is null");
		}
		return str.toString();
	}

	public void invoke(String templateFileName, String type)
			throws TemplateException, IOException {
		Map data = new HashMap();
		data = this.callBack.execute();
		generateFile(templateFileName, type, data);
	}

	public ICallBack getCallBack() {
		return this.callBack;
	}

	public void setCallBack(ICallBack callBack) {
		this.callBack = callBack;
	}

	public static enum CodeType {
		serviceImpl("ServiceImpl"), service("Service"), controller(
				"Controller"), page("Page"), entity("Entity"), jsp(""), jsp_add(
				""), jsp_update(""), js(""), jsList("List"), jspList("List"), jspSubList(
				"SubList"),dao("Dao"),daoImpl("DaoImpl"),jspEdit("Edit");

		private String type;

		private CodeType(String type) {
			this.type = type;
		}

		public String getValue() {
			return this.type;
		}
	}
}