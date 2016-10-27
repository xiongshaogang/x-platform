package com.xplatform.base.develop.codegenerate.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.xplatform.base.develop.codeconfig.entity.GenerateConfigEntity;
import com.xplatform.base.develop.codeconfig.entity.ConBaseFormEntity;
import com.xplatform.base.develop.codeconfig.entity.GenerateFieldEntity;
import com.xplatform.base.develop.codeconfig.service.GenerateConfigService;
import com.xplatform.base.develop.codeconfig.service.GenerateFieldService;
import com.xplatform.base.develop.codeconfig.service.GenerateService;
import com.xplatform.base.develop.codegenerate.database.UcgReadTable;
import com.xplatform.base.develop.codegenerate.generatetype.ctree.CgcTreeCodeGenerate;
import com.xplatform.base.develop.codegenerate.generatetype.dtree.CgdTreeCodeGenerate;
import com.xplatform.base.develop.codegenerate.generatetype.one.CgformCodeGenerate;
import com.xplatform.base.develop.codegenerate.generatetype.onetomany.CgformCodeGenerateOneToMany;
import com.xplatform.base.develop.codegenerate.generatetype.stree.CgsTreeCodeGenerate;
import com.xplatform.base.develop.codegenerate.pojo.CreateFileProperty;
import com.xplatform.base.develop.codegenerate.pojo.GenerateVo;
import com.xplatform.base.develop.codegenerate.pojo.onetomany.CodeParamEntity;
import com.xplatform.base.develop.codegenerate.pojo.onetomany.SubTableEntity;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.develop.metadata.entity.MetaDataEntity;
import com.xplatform.base.develop.metadata.entity.MetaDataFieldEntity;
import com.xplatform.base.develop.metadata.service.MetaDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @Title:CgGenerateController
 * @description:智能表单代码生成器[根据智能表单配置+代码生成设置->生成代码]
 * @author 赵俊夫
 * @date Sep 7, 2013 12:19:32 PM
 * @version V1.0
 */
@Controller
@RequestMapping("/newGenerateController")
public class NewGenerateController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(NewGenerateController.class);
	
	@Resource
	private GenerateService formTypeService;
	@Resource
	private MetaDataService metaDataService;
	@Resource
	private GenerateConfigService baseFormEntityService;
	@Resource
	private GenerateFieldService formFieldService;
    
	
	/**
	 * 代码生成执行-单表
	 * @param generateEntity
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(params = "dogenerate")
	@ResponseBody
	public AjaxJson dogenerate(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//确定模板实体、实体属性是否已配置
		String model_id = request.getParameter("model_id");//模型类型ID
		String type_id = request.getParameter("type_id");//该模型下具体表单模板ID
		AjaxJson ajax = new AjaxJson();
		
		//通过model_id找到联系表的ID  然后根据联系表ID和type_id确定该模型下的类型记录是否都填写完全
		List<ConBaseFormEntity> list = formTypeService.queryModelData(model_id);
		List<ConBaseFormEntity> conList = new ArrayList<ConBaseFormEntity>();
		for(ConBaseFormEntity con : list){
			GenerateConfigEntity baseFormEntity = baseFormEntityService.getBaseFormEntityByConId(con.getId(),type_id);
			if(baseFormEntity == null){
				if(StringUtil.equals(model_id,"5")){
					conList.add(con);
				}else{
				ajax.setMsg("模板实体类未配置完整。");
				ajax.setSuccess(true);
				return ajax;
				}
			}
		}
		list.removeAll(conList);
		if(StringUtil.equals(model_id,"4")){// 单表
			GenerateConfigEntity baseFormEntity = baseFormEntityService.getBaseFormEntityByConId(list.get(0).getId(),type_id);
			//设置需生成文件类型
			CreateFileProperty createFileProperty = this.getCreateFileProperty(baseFormEntity.getFileType());
			//表单属性以及智能表单配置
			GenerateVo generateEntity = getCgformConfig(baseFormEntity);
			new CgformCodeGenerate(createFileProperty,generateEntity).generateToFile();
		}else if(StringUtil.equals(model_id,"5")){// 一对多
			CreateFileProperty mainFileProperty = null;
			//主表
			GenerateVo mainG = new GenerateVo();
			//子表集合
			List<GenerateVo> subList = new ArrayList<GenerateVo>();
			List<CreateFileProperty> subFilePropertyList = new ArrayList<CreateFileProperty>();
			for(ConBaseFormEntity con : list){
			GenerateConfigEntity baseFormEntity = baseFormEntityService.getBaseFormEntityByConId(con.getId(),type_id);
			if(StringUtil.equals(baseFormEntity.getTableType(),"tableType_2")){
			//主表表单属性以及智能表单配置
			mainFileProperty = this.getCreateFileProperty(baseFormEntity.getFileType());
			mainG = getCgformConfig(baseFormEntity);
			//step.5 一对多(父子表)数据模型,代码生成
			}else{
				//step.4 填充子表的所有智能表单配置
				GenerateVo subG = new GenerateVo();
				CreateFileProperty subFileProperty = this.getCreateFileProperty(baseFormEntity.getFileType());
				subG = this.getCgformConfig(baseFormEntity);
				subList.add(subG);
				subFilePropertyList.add(subFileProperty);
			}
			}
			new CgformCodeGenerateOneToMany(mainFileProperty, mainG,subList,subFilePropertyList).generateToFile();
		}else if(StringUtil.equals(model_id,"6")){//单表树，表格与树自身关联
			GenerateConfigEntity baseFormEntity = baseFormEntityService.getBaseFormEntityByConId(list.get(0).getId(),type_id);
			//设置需生成文件类型
			CreateFileProperty createFileProperty = this.getCreateFileProperty(baseFormEntity.getFileType());
			//表单属性以及智能表单配置
			GenerateVo generateEntity = getCgformConfig(baseFormEntity);
			new CgsTreeCodeGenerate(createFileProperty,generateEntity).generateToFile();
		}else if(StringUtil.equals(model_id, "7")){//表格与列表关联树，
			//主表
			GenerateVo tree = new GenerateVo();
			CreateFileProperty treeFileProperty = null;
			GenerateVo table = new GenerateVo();
			CreateFileProperty tableFileProperty = null;
			for(ConBaseFormEntity con : list){
				GenerateConfigEntity baseFormEntity = baseFormEntityService.getBaseFormEntityByConId(con.getId(),type_id);
				if(StringUtil.equals(con.getType(), "table")){
					tableFileProperty = this.getCreateFileProperty(baseFormEntity.getFileType());
					table = getCgformConfig(baseFormEntity);
				}else if(StringUtil.equals(con.getType(), "tree")){
					treeFileProperty = this.getCreateFileProperty(baseFormEntity.getFileType());
					tree = getCgformConfig(baseFormEntity);
				}
			}
			new CgdTreeCodeGenerate(treeFileProperty,tree).generateToFile();//先生成树代码
			new CgdTreeCodeGenerate(tableFileProperty,table,tree).generateToFile();//后生成表格相关代码
		}else if(StringUtil.equals(model_id, "8")){//引用树模块方式  左树右表格
			GenerateConfigEntity baseFormEntity = baseFormEntityService.getBaseFormEntityByConId(list.get(0).getId(),type_id);
			//设置需生成文件类型
			CreateFileProperty createFileProperty = this.getCreateFileProperty(baseFormEntity.getFileType());
			//表单属性以及智能表单配置
			GenerateVo generateEntity = getCgformConfig(baseFormEntity);
			new CgcTreeCodeGenerate(createFileProperty,generateEntity).generateToFile();
		}
		
		ajax.setMsg("代码生成成功！");
		ajax.setSuccess(true);
		return ajax;
		
	}
	
	/**
	 * 获取要生成代码的类型
	 * @param fileType 文件类型字符串
	 * @return
	 */
	public CreateFileProperty getCreateFileProperty(String fileType){
		CreateFileProperty createFileProperty = new CreateFileProperty();
		String[] file_type = fileType.split(",");
		for(int i=0;i<file_type.length;i++){
			if(StringUtil.equals("filetype_action", file_type[i].toLowerCase())){
				createFileProperty.setActionFlag(true);
			}else if(StringUtil.equals("filetype_servicei", file_type[i].toLowerCase())){
				createFileProperty.setServiceIFlag(true);
			}else if(StringUtil.equals("filetype_serviceimpl", file_type[i].toLowerCase())){
				createFileProperty.setServiceImplFlag(true);
			}else if(StringUtil.equals("filetype_dao", file_type[i].toLowerCase())){
				createFileProperty.setDaoFlag(true);
			}else if(StringUtil.equals("filetype_daoimpl", file_type[i].toLowerCase())){
				createFileProperty.setDaoImplFlag(true);
			}else if(StringUtil.equals("filetype_page", file_type[i].toLowerCase())){
				createFileProperty.setPageFlag(true);
			}else if(StringUtil.equals("filetype_entity", file_type[i].toLowerCase())){
				createFileProperty.setEntityFlag(true);
			}else if(StringUtil.equals("filetype_jsp", file_type[i].toLowerCase())){
				createFileProperty.setJspFlag(true);
			}
		}
		return createFileProperty;
	}
	
	
	/**
	 * 获取智能表单的所有配置
	 * @param cgFormHead
	 * @param generateEntity
	 * @throws Exception 
	 */
	private GenerateVo getCgformConfig(GenerateConfigEntity baseFormEntity) throws Exception {
		GenerateVo generateEntity = new GenerateVo();
		MetaDataEntity cgFormHead = metaDataService.getEntity(MetaDataEntity.class,baseFormEntity.getEntityId());
		generateEntity.setTableName(cgFormHead.getTableName());
		generateEntity.setEntityPackage(baseFormEntity.getPack());
		generateEntity.setEntityName(StringUtils.capitalize(baseFormEntity.getEntityClass()));
		generateEntity.setFtlDescription(baseFormEntity.getDescription());
		generateEntity.setBaseFormEntity(baseFormEntity);
		if(StringUtils.isNotEmpty(baseFormEntity.getRefPackage())){
			String[] str = baseFormEntity.getRefPackage().split("[.]");
			generateEntity.setcEntityName(StringUtils.capitalize(str[str.length-1]));
			generateEntity.setcField(baseFormEntity.getConnect());
		}
		//将js中带有online字段名的 转换成java命名
		List<GenerateFieldEntity> list = formFieldService.findFormFieldEntityListByEntityId(baseFormEntity.getId());
		generateEntity.setCgFormHead(cgFormHead);
		generateEntity.setColumns(list);
		//generateEntity.setBaseFormEntity(baseFormEntity);
		return generateEntity;
	}
	
	/**
	 * 获取智能表单中外键：根据是否设置了主表以及主表字段来判断
	 * @param colums
	 * @return
	 */
	private String[] getForeignkeys(List<MetaDataFieldEntity> colums) {
		List<String> fs = new ArrayList<String>(0);
		for(MetaDataFieldEntity c : colums){
			if(StringUtil.isNotEmpty(c.getMainTable()) && StringUtil.isNotEmpty(c.getMainField())){
				fs.add(c.getFieldName().toUpperCase());
			}
		}
		String[] foreignkeys = (String[]) fs.toArray(new String[fs.size()]);
		return foreignkeys;
	}
	/**
	 * 跳转到文件夹目录树
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "goFileTree")
	public ModelAndView goFileTree(HttpServletRequest request) {
		return new ModelAndView("jeecg/cgform/generate/fileTree");
	}
	/**
	 * 返回子目录json
	 * @param parentNode
	 * @return
	 */
	@RequestMapping(params = "doExpandFileTree")
	@ResponseBody
	public Object doExpandFileTree(String parentNode){
		JSONArray fjson = new JSONArray();
		try{
			if(StringUtil.isEmpty(parentNode)){
				//返回磁盘驱动器根目录
				File[] roots = File.listRoots();
				for(File r:roots){
					JSONObject item = new JSONObject();
					item.put("id", r.getAbsolutePath());
					item.put("text", r.getPath());
					item.put("iconCls", "icon-folder");
					if(hasDirs(r)){
						item.put("state", "closed");
					}else{
						item.put("state", "open");
					}
					fjson.add(item);
				}
			}else{
				try {
					parentNode =  new String(parentNode.getBytes("ISO-8859-1"), "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				//返回子目录集
				File parent = new File(parentNode);
				File[] chs = parent.listFiles();
				for(File r:chs){
					JSONObject item = new JSONObject();
					if(r.isDirectory()){
						item.put("id", r.getAbsolutePath());
						item.put("text", r.getPath());
						if(hasDirs(r)){
							item.put("state", "closed");
						}else{
							item.put("state", "open");
						}
						fjson.add(item);
					}else{
						
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("该文件夹不可选择");
		}
		return fjson;
	}
	private boolean hasDirs(File dir){
		try{
			if(dir.listFiles().length==0){
	//			item.put("state", "open");
				return false;
			}else{
	//			item.put("state", "closed");
				return true;
			}
		}catch (Exception e) {
			logger.info(e.getMessage());
			return false;
		}
	}
}
