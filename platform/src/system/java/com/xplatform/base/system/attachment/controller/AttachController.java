/**
 * Copyright (c) 2014
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.xplatform.base.system.attachment.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.form.entity.AppFormField;
import com.xplatform.base.form.service.AppFormFieldService;
import com.xplatform.base.form.service.AppFormTableService;
import com.xplatform.base.form.service.FlowFormService;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.common.model.json.TreeNode;
import com.xplatform.base.framework.core.common.service.CommonService;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.FTPUtil;
import com.xplatform.base.framework.core.util.FileUtils;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.MD5Util;
import com.xplatform.base.framework.core.util.MapKit;
import com.xplatform.base.framework.core.util.PinyinUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.TreeMapper;
import com.xplatform.base.framework.core.util.pdf.HttpUtils;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.service.UserService;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.def.ConfigConst;
import com.xplatform.base.platform.common.service.AuthorityService;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.system.attachment.entity.AttachEntity;
import com.xplatform.base.system.attachment.entity.FTPAttachEntity;
import com.xplatform.base.system.attachment.model.AttachJsonModel;
import com.xplatform.base.system.attachment.mybatis.vo.AttachVo;
import com.xplatform.base.system.attachment.service.AttachService;
import com.xplatform.base.system.attachment.service.FTPAttachService;
import com.xplatform.base.system.type.entity.TypeEntity;
import com.xplatform.base.system.type.service.TypeService;

/**
 * description :
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月1日 上午11:09:43
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- xiaqiang 2014年7月1日 上午11:09:43
 *
 */
@Controller
@RequestMapping("/attachController")
public class AttachController extends BaseController {
	@Resource
	private SysUserService sysUserService;
	@Resource
	private AttachService attachService;
	@Resource
	private TypeService typeService;
	@Resource
	private FTPAttachService ftpAttachService;
	@Resource
	private UserService userService;
	@Resource
	private AuthorityService authorityService;
	@Resource
	private FlowFormService flowFormService;
	@Resource
	private AppFormFieldService appFormFieldService;
	@Resource
	private AppFormTableService appFormTableService;
	@Resource
	private CommonService commonService;

	@RequestMapping(params = "uploadFiles")
	@ResponseBody
	public AjaxJson uploadFiles(MultipartHttpServletRequest request, HttpServletResponse response) throws BusinessException {
		AjaxJson j = new AjaxJson();
		Boolean isNeedToType = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isNeedToType"), "false"));
		Boolean autoCreateType = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("autoCreateType"), "false"));
		Boolean isShowType = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isShowType"), "true"));
		String typeId = request.getParameter("typeId");
		String defaultTypeId = request.getParameter("defaultTypeId");
		String defaultTypeCode = request.getParameter("defaultTypeCode");
		String parentTypeCode = request.getParameter("parentTypeCode");
		String storageType = request.getParameter("storageType");
		String businessKey = StringUtil.isEmpty(request.getParameter("businessKey"), null);
		String businessType = StringUtil.isEmpty(request.getParameter("businessType"), null);
		String businessExtra = StringUtil.isEmpty(request.getParameter("businessExtra"), null);
		String otherKey = StringUtil.isEmpty(request.getParameter("otherKey"), null);
		String otherKeyType = StringUtil.isEmpty(request.getParameter("otherKeyType"), null);
		// 如果没有传入储存的位置,则从配置文件中读取
		if (StringUtils.isEmpty(storageType)) {
			storageType = attachService.getStorageType();
		}

		if (isNeedToType) {// 如果需要关联目录
			if (autoCreateType) { // 如果需要自动创建目录
				if (StringUtil.isNotEmpty("defaultTypeCode") && StringUtil.isNotEmpty("parentTypeCode")) {
					TypeEntity type = typeService.queryTypeEntityByCode(defaultTypeCode);
					if (BeanUtils.isEmpty(type)) {// 若原目录不存在
						TypeEntity parentType = typeService.queryTypeEntityByCode(parentTypeCode);
						if (BeanUtils.isNotEmpty(parentType)) {
							TypeEntity newType = new TypeEntity();
							newType.setCode(defaultTypeCode);
							newType.setName(defaultTypeCode);
							newType.setType("file");
							newType.setParent(parentType);
							typeId = typeService.saveType(newType);
						}
					} else {
						typeId = type.getId();
					}
				}
			} else {
				if (!isShowType) {
					// 若不显示目录区域,则取defaultTypeId的值
					typeId = defaultTypeId;
				}
				if (StringUtils.isEmpty(typeId)) {
					typeId = typeService.queryTypeIdByCondition("-1", "file");
				}
			}
		} else { // 如果不需要关联目录
			typeId = null;
		}

		LinkedList<AttachJsonModel> files = new LinkedList<AttachJsonModel>();
		AttachJsonModel filesJson = new AttachJsonModel();
		MultipartFile mpf = null;
		Iterator<String> itr = request.getFileNames();

		Boolean getPathFlag = false; // 是否已经获得各path路径(避免重复获得)
		String datePath = "";
		String afterDocBasePath = "";
		String funPath = "";
		String typePath = "";
		String uploadBasePath = "";
		String thumbnailDirPath = "";
		String relativeUploadPath = "";
		String absoluteUploadPath = "";
		String absoluteFilePath = "";
		String relativeFilePath = "";
		String thumbnailAbPath = "";
		String thumbnailRePath = "";
		String thumbnailAbFilePath = "";
		String thumbnailReFilePath = "";
		String originalFilename = "";
		String srcName="";
		String remotePath = "";
		String remoteFilePath = "";
		String remoteThumbnailPath = "";
		String remoteThumbnailFilePath = "";

		Map<String, Object> param = new HashMap<String, Object>();
		try {
			while (itr.hasNext()) {
				/* 1.上传时数据参数预处理 */
				mpf = request.getFile(itr.next());
				// TODO 先判断请求中是否包含前台解析过的MD5
				String md5 = MD5Util.getMD5(mpf.getInputStream()); // 获得md5码判断是否已存在该物理文件
				Boolean isMD5Exsits = attachService.isMD5FileExsits(md5, false, "1");
				originalFilename = mpf.getOriginalFilename();// 原始文件名称
				srcName = mpf.getOriginalFilename();// 原始文件名称
				Long size = mpf.getSize();// 文件大小(字节byte)
				String contentType = mpf.getContentType();// 文件类型
				String ext = null;
				String onlyName = ""; // 纯文件名(不带后缀名)
				
				TypeEntity typeEntity = null;
				if (StringUtil.isNotEmpty(typeId) && isNeedToType) {
					typeEntity = typeService.getType(typeId);
					// 若归属到文件夹,则需要加上重名自动改名逻辑
					originalFilename = attachService.getUnrepeatName(typeId, originalFilename);
				}
				
				// 文件后缀名处理
				if (originalFilename.lastIndexOf(".") >= 0) {
					ext = FileUtils.getExtendWithDot(originalFilename);
					onlyName = FileUtils.getFilePrefix2(originalFilename);
				}
				String attachType = FileUtils.getAttachTypeByExt(ext);
				String iconType = FileUtils.getIconTypeByExt(ext);
				String attachSizeStr = FileUtils.convertFileSize(size);
				String realUUIDFileName = UUID.randomUUID().toString() + ext;

				// 没有实例化并且相同MD5的物理文件不存在,则需要进行物理文件的创建和拷贝了
				if (!getPathFlag && !isMD5Exsits) {
					afterDocBasePath = attachService.getAfterDocBaseDir();
					datePath = attachService.getDatePath();
					funPath = attachService.getFunPath(typeId);
					if (isNeedToType) {
						typePath = attachService.getTypePath(typeId);
					} else {
						typePath = ClientUtil.getUserId();
					}

					uploadBasePath = attachService.getUploadBasePath();
					thumbnailDirPath = attachService.getThumbnailDirName();
					relativeUploadPath = afterDocBasePath + File.separator + funPath + File.separator + typePath + File.separator + datePath + File.separator;
					absoluteUploadPath = uploadBasePath + File.separator + relativeUploadPath;
					thumbnailRePath = afterDocBasePath + File.separator + thumbnailDirPath + File.separator + typePath + File.separator + datePath
							+ File.separator;
					thumbnailAbPath = uploadBasePath + File.separator + thumbnailRePath;

					absoluteFilePath = absoluteUploadPath + realUUIDFileName;
					relativeFilePath = relativeUploadPath + realUUIDFileName;
					thumbnailAbFilePath = thumbnailAbPath + realUUIDFileName;
					thumbnailReFilePath = thumbnailRePath + realUUIDFileName;

					remotePath = File.separator + funPath + File.separator + typePath + File.separator + datePath + File.separator;
					remoteFilePath = remotePath + realUUIDFileName;
					remoteThumbnailPath = File.separator + thumbnailDirPath + File.separator + typePath + File.separator + datePath + File.separator;
					remoteThumbnailFilePath = remoteThumbnailPath + realUUIDFileName;
					getPathFlag = true;
				}

				param.put("mpf", mpf);
				param.put("realUUIDFileName", realUUIDFileName);
				param.put("attachType", attachType);
				param.put("absoluteUploadPath", absoluteUploadPath);
				param.put("thumbnailAbPath", thumbnailAbPath);
				param.put("remotePath", remotePath);
				param.put("remoteThumbnailPath", remoteThumbnailPath);

				/* 附件通用属性的事先设置(无论用何种存储方式,都需要记录的属性) */
				AttachEntity newAttachEntity = new AttachEntity();
				newAttachEntity.setSrcName(srcName);
				newAttachEntity.setAttachName(originalFilename);
				newAttachEntity.setOnlyName(onlyName);
				newAttachEntity.setAttachSize(size);
				newAttachEntity.setExt(ext);
				newAttachEntity.setAttachRemark(null);
				newAttachEntity.setAttachContentType(contentType);
				newAttachEntity.setAttachSizeStr(attachSizeStr);
				newAttachEntity.setAttachType(attachType);
				newAttachEntity.setIconType(iconType);
				newAttachEntity.setMD5(md5);

				/* 2.保存物理文件 */
				if ("0".equals(storageType)) {// 如果存储位置是本地
					// 判断相同MD5码文件是否存在,存在就不进行重复存储,不存在才进入存储逻辑
					if (!isMD5Exsits) {
						attachService.storeLocalFile(param);
					}
				} else if ("1".equals(storageType)) {// 如果存储位置是FTP服务器
					// 判断相同MD5码文件是否存在,存在就不进行重复存储,不存在才进入存储逻辑
					if (!isMD5Exsits) {
						attachService.storeFTPFile(param);
					}
				}

				/* 3.保存AttachEntity数据 */
				if ("0".equals(storageType)) {
					newAttachEntity.setStorageType("0");
					// 如果相同MD5码物理文件存在,非首次上传,并且用相同MD5码的附件的各类路径
					if (isMD5Exsits) {
						newAttachEntity.setIsFirstUpload("N");
						List<AttachEntity> attachEntityList = attachService.queryAttachEntityByMD5(md5);
						newAttachEntity.setRelativePath(attachEntityList.get(0).getRelativePath());
						newAttachEntity.setAbsolutePath(attachEntityList.get(0).getAbsolutePath());
						newAttachEntity.setThumbnailAbPath(attachEntityList.get(0).getThumbnailAbPath());
						newAttachEntity.setThumbnailRePath(attachEntityList.get(0).getThumbnailRePath());
					} else {
						newAttachEntity.setIsFirstUpload("Y");
						newAttachEntity.setRelativePath(relativeFilePath.replaceAll("\\\\", "/"));
						newAttachEntity.setAbsolutePath(absoluteFilePath.replaceAll("\\\\", "/"));
						if ("img".equals(attachType) && attachService.isNeedThumbnail()) {
							newAttachEntity.setThumbnailAbPath(thumbnailAbFilePath.replaceAll("\\\\", "/"));
							newAttachEntity.setThumbnailRePath(thumbnailReFilePath.replaceAll("\\\\", "/"));
						}
					}
				} else if ("1".equals(storageType)) {// 如果存储位置是FTP服务器
					/* 3.1 保存FTP附件关联表记录 */
					FTPAttachEntity ftp = new FTPAttachEntity();

					// 如果相同MD5码物理文件存在,非首次上传,并且用相同MD5码的附件的各类路径
					if (isMD5Exsits) {
						newAttachEntity.setIsFirstUpload("N");
						FTPAttachEntity ftpAttachEntity = attachService.queryFTPAttachEntityByMD5(md5);
						ftp.setRemoteFilePath(ftpAttachEntity.getRemoteFilePath());
						ftp.setRemotePath(ftpAttachEntity.getRemotePath());
						ftp.setFileName(ftpAttachEntity.getFileName());
						ftp.setRemoteThumbnailFilePath(ftpAttachEntity.getRemoteThumbnailFilePath());
						ftp.setRemoteThumbnailPath(ftpAttachEntity.getRemoteThumbnailPath());
					} else {
						newAttachEntity.setIsFirstUpload("Y");
						ftp.setRemoteFilePath(remoteFilePath.replaceAll("\\\\", "/"));
						ftp.setRemotePath(remotePath.replaceAll("\\\\", "/"));
						ftp.setFileName(realUUIDFileName);
						if ("img".equals(attachType) && attachService.isNeedThumbnail()) {
							ftp.setRemoteThumbnailFilePath(remoteThumbnailFilePath.replaceAll("\\\\", "/"));
							ftp.setRemoteThumbnailPath(remoteThumbnailPath.replaceAll("\\\\", "/"));
						}
					}
					String storageId = (String) ftpAttachService.save(ftp);

					/* 3.2 设置本方式附件属性 */
					newAttachEntity.setStorageType("1");
					newAttachEntity.setStorageId(storageId);
				}
				newAttachEntity.setTypeEntity(typeEntity);
				newAttachEntity.setBusinessKey(businessKey);
				newAttachEntity.setBusinessType(businessType);
				newAttachEntity.setBusinessExtra(businessExtra);
				newAttachEntity.setOtherKey(otherKey);
				newAttachEntity.setOtherKeyType(otherKeyType);
				attachService.save(newAttachEntity);

				/* 5.构造返回数据(用于jquery file upload组件显示使用) */
				filesJson = attachService.convertAttach(newAttachEntity);
			}
		} catch (IOException e) {
			j.setSuccess(false);
			filesJson.setName(originalFilename);
			filesJson.setInfo("上传传输过程发生错误");
			e.printStackTrace();
		} catch (Exception e) {
			j.setSuccess(false);
			filesJson.setName(originalFilename);
			filesJson.setInfo("上传失败");
			e.printStackTrace();
		}
		files.add(filesJson);
		j.setObj(files);
		return j;
	}

	@RequestMapping(params = "downloadFile")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
		String aId = request.getParameter("aId");

		AttachEntity attachEntity = attachService.get(aId);
		if (attachEntity == null) {
			return;
		}
		try {
			if (StringUtil.isNotEmpty(attachEntity.getStorageType())) {
				if ("0".equals(attachEntity.getStorageType())) {
					FileUtils.downLoadFile(request, response, attachEntity.getAbsolutePath(), attachEntity.getAttachName());
				} else if ("1".equals(attachEntity.getStorageType())) {
					FTPAttachEntity ftpAttachEntity = attachService.queryFTPAttachEntityByAttach(aId);
					FTPUtil ftpUtil = new FTPUtil();
					ftpUtil.connectServer();
					InputStream is = ftpUtil.downloadFile(ftpAttachEntity.getRemoteFilePath());
					FileUtils.downLoadFile(request, response, is, attachEntity.getAttachName());
					ftpUtil.closeConnect();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月2日 下午2:13:23
	 * @Decription 通过请求方法的方式获得缩略图图片(非路径方式)
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "getThumbnailImage")
	public void getThumbnailImage(HttpServletRequest request, HttpServletResponse response) {
		String aId = request.getParameter("aId");

		AttachEntity attachEntity = attachService.get(aId);
		try {
			if (StringUtil.isNotEmpty(attachEntity.getStorageType())) {
				if ("1".equals(attachEntity.getStorageType())) {
					FTPAttachEntity ftpAttachEntity = attachService.queryFTPAttachEntityByAttach(aId);

					FTPUtil ftpUtil = new FTPUtil();
					ftpUtil.connectServer();
					InputStream is = ftpUtil.downloadFile(ftpAttachEntity.getRemoteThumbnailFilePath());
					FileUtils.viewImg(request, response, is);
					ftpUtil.closeConnect();
				} else if ("0".equals(attachEntity.getStorageType())) {
					// 如果本地缩略图文件存在
					if (FileUtils.isFileExist(attachEntity.getThumbnailAbPath())) {
						File file = new File(attachEntity.getThumbnailAbPath());
						InputStream fis = new FileInputStream(file);
						FileUtils.viewImg(request, response, fis);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月2日 下午2:13:23
	 * @Decription 通过请求方法的方式获得原图图片(非路径方式)
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "getPlainImage")
	public void getPlainImage(HttpServletRequest request, HttpServletResponse response) {
		String aId = request.getParameter("aId");

		AttachEntity attachEntity = attachService.get(aId);
		try {
			if (StringUtil.isNotEmpty(attachEntity.getStorageType())) {
				if ("1".equals(attachEntity.getStorageType())) {
					FTPAttachEntity ftpAttachEntity = attachService.queryFTPAttachEntityByAttach(aId);

					FTPUtil ftpUtil = new FTPUtil();
					ftpUtil.connectServer();
					InputStream is = ftpUtil.downloadFile(ftpAttachEntity.getRemoteFilePath());
					FileUtils.viewImg(request, response, is);
					ftpUtil.closeConnect();
				} else if ("0".equals(attachEntity.getStorageType())) {
					// 如果本地缩略图文件存在
					if (FileUtils.isFileExist(attachEntity.getAbsolutePath())) {
						File file = new File(attachEntity.getAbsolutePath());
						InputStream fis = new FileInputStream(file);
						FileUtils.viewImg(request, response, fis);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(params = "mulDeleteFile")
	@ResponseBody
	public AjaxJson mulDeleteFile(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson result = new AjaxJson();
		String aIds = request.getParameter("aIds");
		try {
			attachService.deleteMulFile(aIds);
			result.setMsg("文件删除成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setMsg("文件删除失败" + e.getMessage());
		}
		return result;
	}

	@RequestMapping(params = "viewDataEdit")
	public ModelAndView viewDataEdit(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		if (StringUtil.isNotEmpty(id)) {
			AttachEntity attachEntity = attachService.get(id);
			model.addAttribute("attachPage", attachEntity);
		}
		return new ModelAndView("platform/system/data/viewDataEdit");
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月15日 上午11:20:20
	 * @Decription 进入通用附件上传页面
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "commonAttachUpload")
	public ModelAndView commonAttachUpload(HttpServletRequest request, HttpServletResponse response) {
		/** 1.数据预处理 */
		String uploadId = StringUtil.isEmpty(request.getParameter("uploadId"), "commmon");
		String storageType = StringUtil.isEmpty(request.getParameter("storageType"), attachService.getStorageType());// 存储位置(0-本地,1-ftp服务器)
		String businessKey = request.getParameter("businessKey");
		String businessType = request.getParameter("businessType");
		String businessExtra = request.getParameter("businessExtra");
		String otherKey = request.getParameter("otherKey");
		String otherKeyType = request.getParameter("otherKeyType");
		Boolean isShowType = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isShowType"), "true"));
		Boolean isChangeType = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isChangeType"), attachService.getIsChangeType().toString()));// 是否修改目录树(true产生可修改的目录树,false则是用一个文本去显示上传到的目录)
		String typeTreeUrl = StringUtil.isEmpty(request.getParameter("typeTreeUrl"), "typeController.do?queryCommonAttachUploadTree");// 上传目录自定义url
		Boolean isNeedToType = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isNeedToType"), "true"));// 是否需要关联至至某个目录下,比如像个人头像、系统邮件附件等无关附件目录的附件,可以isNeedToType:false,系统会放到一个额外文件夹下,无需指定defaultTypeCode等参数了
		String parentTypeCode = request.getParameter("parentTypeCode");// 默认父目录值(用于自动创建目录功能,识别创建在哪个父目录下)(目录code,非id)
		String defaultTypeCode = request.getParameter("defaultTypeCode");// 目录树默认值(目录code,一般人工指定时传code,页面动态获取时传id)
		String defaultTypeId = request.getParameter("defaultTypeId");// 目录树默认值(目录id,一般人工指定时传code,页面动态获取时传id)
		String rootTreeCode = StringUtil.isEmpty(request.getParameter("rootTreeCode"), attachService.getRootTreeCode());// 根节点从哪个目录开始
		Boolean onlyAuthority = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("onlyAuthority"), attachService.getOnlyAuthority().toString()));// 是否只显示有权看到的目录(默认不进行权限过滤)
		Boolean containSelf = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("containSelf"), attachService.getContainSelf().toString()));// 是否包含传入的rootTreeCode节点自身
		String exParams = request.getParameter("exParams");// 其他复合属性(用以配置jquery-file-upload的各种属性)
		String finishUploadCallback = request.getParameter("finishUploadCallback");// 点击完成上传后的回调函数,函数可传一个固定名称的参数allFiles,例如finishUploadCallback:"test1(allFiles)",result是本次上传的所有文件
		Boolean isReloadGrid = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isReloadGrid"), "false"));// 是否执行刷新列表操作
		Boolean isLoadData = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isLoadData"), "true"));// 是否在上传弹出页加载已上传附件
		Boolean autoCreateType = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("autoCreateType"), "false"));// 如果没有此code(defaultTypeCode或defaultTypeId)的目录,是否自动创建

		JSONObject exParamsObj;

		if (StringUtil.isNotEmpty(exParams)) {
			exParamsObj = JSONHelper.toJSONObject(exParams);
		} else {
			exParamsObj = new JSONObject();
		}

		StringBuffer typeTreeUrlSB = new StringBuffer(typeTreeUrl);
		typeTreeUrlSB.append("&defaultTypeCode=" + defaultTypeCode);
		typeTreeUrlSB.append("&rootTreeCode=" + rootTreeCode);
		typeTreeUrlSB.append("&onlyAuthority=" + onlyAuthority);
		typeTreeUrlSB.append("&containSelf=" + containSelf);

		/** 2.获得已上传的附件 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("businessKey", businessKey);
		param.put("businessType", businessType);
		param.put("businessExtra", businessExtra);
		param.put("otherKey", otherKey);
		param.put("otherKeyType", otherKeyType);
		List<AttachVo> dataVoList = attachService.queryUploadAttach(param);

		List<AttachJsonModel> filesJsonList = new ArrayList<AttachJsonModel>();

		/** 3.构造返回数据(用于jquery file upload组件显示使用) */
		for (AttachVo dataVo : dataVoList) {
			AttachJsonModel filesJson = new AttachJsonModel();
			String id = dataVo.getId();
			String name = dataVo.getName();
			String downloadUrl = "attachController.do?downloadFile&aId=" + id;
			String attachType = dataVo.getAttachType();
			String thumbnailPath = "attachController.do?getThumbnailImage&aId=" + id;
			Long attachSize = dataVo.getAttachSize();
			String deleteUrl = "attachController.do?deleteFile&aId=" + id;
			filesJson.setId(id);
			filesJson.setName(name);
			filesJson.setDownloadUrl(downloadUrl);

			if ("img".equals(attachType) && attachService.isNeedThumbnail() && StringUtils.isNotEmpty(thumbnailPath)) {
				filesJson.setThumbnailUrl(thumbnailPath);
			} else {
				filesJson.setThumbnailUrl(null);
			}

			filesJson.setSize(attachSize);
			filesJson.setInfo("上传成功");
			filesJson.setDeleteUrl(deleteUrl);
			filesJson.setDeleteType("DELETE");
			filesJsonList.add(filesJson);
		}

		// 如果需要和目录关联
		if (isNeedToType) {
			if (autoCreateType) {// 是否有自动创建目录需求
				isShowType = false;// 如果是自动创建,就不显示目录了
				request.setAttribute("autoCreateType", autoCreateType);
			} else { // 上传到一个已有的目录下
				TypeEntity entity = null;
				if (StringUtil.isNotEmpty(defaultTypeCode)) {
					entity = typeService.queryTypeEntityByCode(defaultTypeCode);
				} else if (StringUtil.isNotEmpty(defaultTypeId)) {
					entity = typeService.getType(defaultTypeId);
				} else {
					entity = typeService.queryTypeEntityByCode(rootTreeCode);
				}
				if (entity != null) {
					request.setAttribute("defaultTypeId", entity.getId());
					request.setAttribute("defaultTypeName", entity.getName());
				}
			}
		} else {
			isShowType = false;// 如果设置了无需目录,则也不再显示目录区域了
		}
		request.setAttribute("uploadId", uploadId);
		request.setAttribute("defaultTypeCode", defaultTypeCode);
		request.setAttribute("typeTreeUrl", typeTreeUrlSB.toString());
		request.setAttribute("result", JSONHelper.toJSONString(filesJsonList));
		request.setAttribute("storageType", storageType);
		request.setAttribute("businessKey", businessKey);
		request.setAttribute("businessType", businessType);
		request.setAttribute("businessExtra", businessExtra);
		request.setAttribute("otherKey", otherKey);
		request.setAttribute("otherKeyType", otherKeyType);
		request.setAttribute("rootTreeCode", rootTreeCode);
		request.setAttribute("onlyAuthority", onlyAuthority);
		request.setAttribute("containSelf", containSelf);
		request.setAttribute("isNeedToType", isNeedToType);
		request.setAttribute("isChangeType", isChangeType);
		request.setAttribute("isShowType", isShowType);
		request.setAttribute("isReloadGrid", isReloadGrid);
		request.setAttribute("isLoadData", isLoadData);
		request.setAttribute("parentTypeCode", parentTypeCode);
		request.setAttribute("finishUploadCallback", finishUploadCallback);
		request.setAttribute("jfExParams", exParamsObj.toString());
		return new ModelAndView("common/upload/commonAttachUpload");
	}

	@RequestMapping(params = "queryAttachUpload")
	@ResponseBody
	public List<AttachVo> queryAttachUpload(HttpServletRequest request, AttachEntity attachEntity) {
		String businessKey = request.getParameter("businessKey");
		String businessType = request.getParameter("businessType");
		String businessExtra = request.getParameter("businessExtra");
		String otherKey = request.getParameter("otherKey");
		String otherKeyType = request.getParameter("otherKeyType");

		/** 1.获得已上传的附件 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("businessKey", businessKey);
		param.put("businessType", businessType);
		param.put("businessExtra", businessExtra);
		param.put("otherKey", otherKey);
		param.put("otherKeyType", otherKeyType);
		List<AttachVo> dataVoList = attachService.queryUploadAttach(param);
		return dataVoList;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月30日 下午5:29:05
	 * @Decription 通用附件查看进入列表视图
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "datagridView")
	public ModelAndView datagridView(HttpServletRequest request) {
		// String attachType = request.getParameter("attachType");
		// String businessKey = request.getParameter("businessKey");
		// String businessType = request.getParameter("businessType");
		// String businessExtra = request.getParameter("businessExtra");
		// String otherKey = request.getParameter("otherKey");
		// String otherKeyType = request.getParameter("otherKeyType");
		// request.setAttribute("attachType", attachType);
		// request.setAttribute("businessKey", businessKey);
		// request.setAttribute("businessType", businessType);
		// request.setAttribute("businessExtra", businessExtra);
		// request.setAttribute("otherKey", otherKey);
		// request.setAttribute("otherKeyType", otherKeyType);
		String uploadId = request.getParameter("uploadId");
		String gridUrl = request.getParameter("gridUrl");
		request.setAttribute("gridUrl", gridUrl);
		request.setAttribute("uploadId", uploadId);
		return new ModelAndView("common/upload/attachView_datagridView");
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月30日 下午5:29:23
	 * @Decription 通用附件查看进入平铺视图
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "thumbnailView")
	public ModelAndView thumbnailView(HttpServletRequest request) {
		// String attachType = request.getParameter("attachType");
		// String businessKey = request.getParameter("businessKey");
		// String businessType = request.getParameter("businessType");
		// String businessExtra = request.getParameter("businessExtra");
		// String otherKey = request.getParameter("otherKey");
		// String otherKeyType = request.getParameter("otherKeyType");
		//
		// request.setAttribute("attachType", attachType);
		// request.setAttribute("businessKey", businessKey);
		// request.setAttribute("businessType", businessType);
		// request.setAttribute("businessExtra", businessExtra);
		// request.setAttribute("otherKey", otherKey);
		// request.setAttribute("otherKeyType", otherKeyType);
		String thumbnailUrl = request.getParameter("thumbnailUrl");
		String uploadId = request.getParameter("uploadId");
		request.setAttribute("thumbnailUrl", thumbnailUrl);
		request.setAttribute("uploadId", uploadId);
		return new ModelAndView("common/upload/attachView_thumbnailView");
	}

	@RequestMapping(params = "attachViewDatagrid")
	public void attachViewDatagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		// 获取查询条件
		String businessKey = StringUtil.isEmpty(request.getParameter("businessKey"), null);
		String businessType = StringUtil.isEmpty(request.getParameter("businessType"), null);
		String businessExtra = StringUtil.isEmpty(request.getParameter("businessExtra"), null);
		String otherKey = StringUtil.isEmpty(request.getParameter("otherKey"), null);
		String otherKeyType = StringUtil.isEmpty(request.getParameter("otherKeyType"), null);
		String attachType = StringUtil.isEmpty(request.getParameter("attachType"), null);
		Boolean isDownload = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isDownload"), "true"));
		Boolean isEdit = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isEdit"), "false"));
		Boolean isDelete = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isDelete"), "false"));
		Boolean isView = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isView"), "true"));

		/** 获得已上传的附件 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("attachType", attachType);
		param.put("businessKey", businessKey);
		param.put("businessType", businessType);
		param.put("businessExtra", businessExtra);
		param.put("otherKey", otherKey);
		param.put("otherKeyType", otherKeyType);
		List<AttachVo> dataVoList = attachService.queryUploadAttach(param);
		/** 设置按钮权限 */
		for (AttachVo vo : dataVoList) {
			if (isDownload) {
				vo.setDownloadAuthority("Y");
			} else {
				vo.setDownloadAuthority("N");
			}
//			if (isEdit) {
//				vo.setEditAuthority("Y");
//			} else {
//				vo.setEditAuthority("N");
//			}
			if (isDelete) {
				vo.setDeleteAuthority("Y");
			} else {
				vo.setDeleteAuthority("N");
			}
//			if (isView) {
//				vo.setViewAuthority("Y");
//			} else {
//				vo.setViewAuthority("N");
//			}
		}
		dataGrid.setResults(dataVoList);
		Map<String, Object> exParam = new HashMap<String, Object>();
		String operationCodes = "isUpload,isMulDelete";
		exParam.put("operationCodes", operationCodes);
		// 另外传递上传、批量删除等总体权限按钮到前台(通过datagrid的exParam传递过去)
		dataGrid.setExParam(exParam);
		TagUtil.datagrid(response, dataGrid);
	}

	@RequestMapping(params = "attachViewThumbnailData")
	public void attachViewThumbnailData(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 获取查询条件
			String businessKey = StringUtil.isEmpty(request.getParameter("businessKey"), null);
			String businessType = StringUtil.isEmpty(request.getParameter("businessType"), null);
			String businessExtra = StringUtil.isEmpty(request.getParameter("businessExtra"), null);
			String otherKey = StringUtil.isEmpty(request.getParameter("otherKey"), null);
			String otherKeyType = StringUtil.isEmpty(request.getParameter("otherKeyType"), null);
			String attachType = StringUtil.isEmpty(request.getParameter("attachType"), null);
			Boolean isDownload = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isDownload"), "true"));
			Boolean isEdit = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isEdit"), "false"));
			Boolean isDelete = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isDelete"), "false"));
			Boolean isView = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isView"), "true"));

			/** 获得已上传的附件 */
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("attachType", attachType);
			param.put("businessKey", businessKey);
			param.put("businessType", businessType);
			param.put("businessExtra", businessExtra);
			param.put("otherKey", otherKey);
			param.put("otherKeyType", otherKeyType);
			List<AttachVo> dataVoList = attachService.queryUploadAttach(param);
			/** 设置按钮权限 */
			for (AttachVo vo : dataVoList) {
				if (isDownload) {
					vo.setDownloadAuthority("Y");
				} else {
					vo.setDownloadAuthority("N");
				}
//				if (isEdit) {
//					vo.setEditAuthority("Y");
//				} else {
//					vo.setEditAuthority("N");
//				}
				if (isDelete) {
					vo.setDeleteAuthority("Y");
				} else {
					vo.setDeleteAuthority("N");
				}
//				if (isView) {
//					vo.setViewAuthority("Y");
//				} else {
//					vo.setViewAuthority("N");
//				}
			}

			StringBuilder sb = new StringBuilder();
			String operationCodes = "isUpload,isMulDelete";
			sb.append("{\"thumbnailData\":");
			JSONArray json = JSONArray.fromObject(dataVoList);
			sb.append(json.toString());
			sb.append(",\"exParam\":{\"operationCodes\":\"" + operationCodes + "\"}}");

			TagUtil.responseWrite(response, sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月15日 上午11:20:20
	 * @Decription 进入通用附件上传浏览页面
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "commonUploadAndView")
	public ModelAndView commonUploadAndView(HttpServletRequest request, HttpServletResponse response) {
		/** 1.数据预处理 */
		String uploadId = StringUtil.isEmpty(request.getParameter("uploadId"), "common");// 上传控件的id(若同一时刻可能有多个上传控件时,用于区分与取值)
		String storageType = StringUtil.isEmpty(request.getParameter("storageType"), attachService.getStorageType());// 存储位置(0-本地,1-ftp服务器)
		String businessKey = request.getParameter("businessKey");
		String businessType = request.getParameter("businessType");
		String businessExtra = request.getParameter("businessExtra");
		String otherKey = request.getParameter("otherKey");
		String otherKeyType = request.getParameter("otherKeyType");
		Boolean isShowType = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isShowType"), "true"));
		Boolean isChangeType = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isChangeType"), attachService.getIsChangeType().toString()));// 是否修改目录树(true产生可修改的目录树,false则是用一个文本去显示上传到的目录)
		Boolean isNeedToType = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isNeedToType"), "true"));// 是否需要关联至至某个目录下,比如像个人头像、系统邮件附件等无关附件目录的附件,可以isNeedToType:false,系统会放到一个额外文件夹下,无需指定defaultTypeCode等参数了
		String parentTypeCode = request.getParameter("parentTypeCode");// 默认父目录值(用于自动创建目录功能,识别创建在哪个父目录下)(目录code,非id)
		String defaultTypeCode = request.getParameter("defaultTypeCode");// 目录树默认值(目录code,非id)
		String defaultTypeId = request.getParameter("defaultTypeId");// 目录树默认值,(目录id,一般人工指定时传code,页面动态获取时传id)
		String rootTreeCode = StringUtil.isEmpty(request.getParameter("rootTreeCode"), attachService.getRootTreeCode());// 根节点从哪个目录开始
		Boolean onlyAuthority = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("onlyAuthority"), attachService.getOnlyAuthority().toString()));// 是否只显示有权看到的目录(默认不进行权限过滤)
		Boolean containSelf = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("containSelf"), attachService.getContainSelf().toString()));// 是否包含传入的rootTreeCode节点自身
		String exParams = request.getParameter("exParams");// 其他复合属性(用以配置jquery-file-upload的各种属性)
		String finishUploadCallback = request.getParameter("finishUploadCallback");// 点击完成上传后的回调函数,函数可传一个固定名称的参数allFiles,例如finishUploadCallback:"test1(allFiles)",result是本次上传的所有文件
		Boolean isReloadGrid = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isReloadGrid"), "false"));// 完成上传是否执行刷新列表操作
		Boolean isLoadData = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isLoadData"), "false"));// 是否在上传弹出页加载已上传附件
		Boolean isDownload = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isDownload"), "true"));
		Boolean isEdit = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isEdit"), "true"));
		Boolean isDelete = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isDelete"), "true"));
		Boolean isView = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isView"), "true"));
		Boolean isUpload = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isUpload"), "true"));
		Boolean isMulDelete = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isMulDelete"), "true"));
		Boolean isFileTypeFilter = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("isFileTypeFilter"), "true"));// 是否显示"图片,文件,音/视频"等分类
		String gridUrl = StringUtil.isEmpty(request.getParameter("gridUrl"), "attachController.do?attachViewDatagrid"); // 列表加载数据url
		String thumbnailUrl = StringUtil.isEmpty(request.getParameter("thumbnailUrl"), "attachController.do?attachViewThumbnailData");// 缩略图加载数据url
		Boolean autoCreateType = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("autoCreateType"), "false"));// 如果没有此code(defaultTypeCode或defaultTypeId)的目录,是否自动创建
		Boolean noBusinessKey = Boolean.parseBoolean(StringUtil.isEmpty(request.getParameter("noBusinessKey"), "false"));// 为true的话就不进行Businesskey的为空检验(不关联业务表的情况)
		JSONObject exParamsObj;

		// 如果设置了无需目录,则也不再显示目录区域了
		if (!isNeedToType) {
			isShowType = false;
		}
		if (StringUtil.isNotEmpty(exParams)) {
			exParamsObj = JSONHelper.toJSONObject(exParams);
		} else {
			exParamsObj = new JSONObject();
		}

		request.setAttribute("uploadId", uploadId);
		request.setAttribute("noBusinessKey", noBusinessKey);
		request.setAttribute("storageType", storageType);
		request.setAttribute("businessKey", businessKey);
		request.setAttribute("businessType", businessType);
		request.setAttribute("businessExtra", businessExtra);
		request.setAttribute("otherKey", otherKey);
		request.setAttribute("otherKeyType", otherKeyType);
		request.setAttribute("parentTypeCode", parentTypeCode);
		request.setAttribute("defaultTypeCode", defaultTypeCode);
		request.setAttribute("defaultTypeId", defaultTypeId);
		request.setAttribute("rootTreeCode", rootTreeCode);
		request.setAttribute("onlyAuthority", onlyAuthority);
		request.setAttribute("containSelf", containSelf);
		request.setAttribute("isNeedToType", isNeedToType);
		request.setAttribute("isChangeType", isChangeType);
		request.setAttribute("isShowType", isShowType);
		request.setAttribute("finishUploadCallback", finishUploadCallback);
		request.setAttribute("isDownload", isDownload);
		request.setAttribute("isEdit", isEdit);
		request.setAttribute("isDelete", isDelete);
		request.setAttribute("isView", isView);
		request.setAttribute("isUpload", isUpload);
		request.setAttribute("isMulDelete", isMulDelete);
		request.setAttribute("isReloadGrid", isReloadGrid);
		request.setAttribute("isLoadData", isLoadData);
		request.setAttribute("jfExParams", exParamsObj.toString());
		request.setAttribute("isFileTypeFilter", isFileTypeFilter);
		request.setAttribute("gridUrl", gridUrl);
		request.setAttribute("thumbnailUrl", thumbnailUrl);
		request.setAttribute("autoCreateType", autoCreateType);
		return new ModelAndView("common/upload/common_dataFrame");
	}

	@RequestMapping(params = "companyData")
	@ResponseBody
	public List<UserEntity> companyData(HttpServletRequest request, DataGrid dataGrid) {
		List<UserEntity> list = userService.queryList();

		dataGrid.setResults(list);
		return list;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月3日 上午9:49:57
	 * @Decription 进入附件管理功能
	 *
	 * @param request
	 * @return
	 * @throws BusinessException 
	 */
	@RequestMapping(params = "dataFrame")
	public ModelAndView dataFrame(HttpServletRequest request) throws BusinessException {
		// 先获得个人根文件夹
		String typeId = typeService.doQueryPersonalRootType(ClientUtil.getUserId());
		request.setAttribute("typeId", typeId);
		return new ModelAndView("platform/system/data/dataFrame");
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月3日 下午6:42:20
	 * @Decription 进入附件管理的datagrid视图
	 *
	 * @param request
	 * @return
	 */

	@RequestMapping(params = "datasDatagridView")
	public ModelAndView datasDatagridView(HttpServletRequest request) {
		String typeId = request.getParameter("typeId");
		String attachType = request.getParameter("attachType");
		request.setAttribute("typeId", typeId);
		request.setAttribute("attachType", attachType);
		return new ModelAndView("platform/system/data/datagridView");
	}

	/**
	 * 进入附件管理的平铺视图
	 * 
	 * @author luoheng
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "datasThumbnailView")
	public ModelAndView datasThumbnailView(HttpServletRequest request) {
		String typeId = request.getParameter("typeId");
		String attachType = request.getParameter("attachType");
		request.setAttribute("typeId", typeId);
		request.setAttribute("attachType", attachType);
		return new ModelAndView("platform/system/data/thumbnailView");
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月4日 上午10:16:36
	 * @Decription 请求附件列表数据方法
	 *
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @throws BusinessException 
	 */

	@RequestMapping(params = "datagrid")
	@ResponseBody
	public AjaxJson datagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) throws BusinessException {
		AjaxJson j = new AjaxJson();
		// 获取查询条件

		String menuType = StringUtil.isEmpty(request.getParameter("menuType"), BusinessConst.menuType_CODE_personal);// 无类型默认显示个人
		String viewFlag = StringUtil.isEmpty(request.getParameter("viewFlag"), BusinessConst.ViewFlag_datasDatagridView);// 无类型默认显示列表样式
		String parentTypeId = request.getParameter("parentTypeId");
		String isPublic = request.getParameter("isPublic");
		String fileFlag = request.getParameter("fileFlag");
		String attachType = request.getParameter("attachType");
		String order = request.getParameter("order");
		String sort = request.getParameter("sort");
		String userId = ClientUtil.getUserId();
		List<AttachVo> attachVoList = new ArrayList<AttachVo>();
		List<String> operationCode = new ArrayList<String>();
		// 数据list额外的数据(如父文件夹权限按钮)
		Map<String, Object> exParam = new HashMap<String, Object>();
		// 封装查询条件
		Map<String, Object> params = new HashMap<String, Object>();
		if (BusinessConst.menuType_CODE_personal.equals(menuType)) {
			if(StringUtil.isEmpty(parentTypeId)){
				// 获得个人根文件夹
				parentTypeId = typeService.doQueryPersonalRootType(ClientUtil.getUserId());
			}
			
			params.put("userId", userId);
			params.put("parentTypeId", parentTypeId);
			attachVoList = attachService.queryPersonalAttachs(params);

			// 第一级目录界面有创建文件夹的权限
			operationCode.add(BusinessConst.attach_createFolderAuthority);
			operationCode.add(BusinessConst.attach_uploadAuthority);
			operationCode.add(BusinessConst.attach_mulMoveAuthority);
			operationCode.add(BusinessConst.attach_mulDeleteAuthority);
			exParam.put("operationCodes", StringUtils.join(operationCode, ","));
			// 文件/文件夹项授权
			attachService.setMulTypeButtonAuthority(attachVoList, menuType, null);
		} else if (BusinessConst.menuType_CODE_work.equals(menuType)) {
			String formCode = request.getParameter("formCode");
			String titleBusinessKey = request.getParameter("titleBusinessKey");
			if (formCode.startsWith("workReceiveFile")) {
				// 该用户收到的文件(被传阅型)
				String dataOrgId=formCode.split("workReceiveFile")[1];
				List<String> formCodes=new ArrayList<String>();
				List<Map<String, Object>> forms=this.flowFormService.queryOrgAppList(dataOrgId);
				for (Map<String, Object> form : forms) {
					formCodes.add(StringUtil.addQuotes(form.get("mainFormCode").toString()));
				}
				params.put("userId", ClientUtil.getUserId());
				params.put("formCodes", formCodes);
				if(formCodes.size()>0){
					attachVoList = attachService.queryNotifyTypeAttachs(params);	
				}
			} else {
				if (StringUtil.isNotEmpty(titleBusinessKey)) {
					// 说明是按标题分类并点击的
					params.put("otherKey", titleBusinessKey);
					params.put("otherKeyType", formCode);
				} else {
					// 无标题型点击
					params.put("createUserId", ClientUtil.getUserId());
					params.put("otherKeyType", formCode);
				}
				attachVoList = attachService.queryUploadAttach(params);
			}
			// 目录界面无操作权限,故无代码指定

			// 文件/文件夹项授权
			attachService.setMulTypeButtonAuthority(attachVoList, menuType, null);
		} else if (BusinessConst.menuType_CODE_org.equals(menuType)) {
			String dataOrgId = request.getParameter("dataOrgId");

			if (StringUtil.isEmpty(parentTypeId)) {
				// 说明是第一级目录
				parentTypeId = typeService.queryOrgRootType(dataOrgId);
				if(StringUtil.isNotEmpty(parentTypeId)){
					params.put("dataOrgId", dataOrgId);
					params.put("userId", userId);

					List<String> orgIds = StringUtil.addQuotes(sysUserService.getDistinctUpOrgIds(userId));
					params.put("orgIds", orgIds);
					params.put("parentTypeId", parentTypeId);
					attachVoList = attachService.queryRootOrgAttachs(params);

					// 第一级目录界面有创建文件夹的权限
					operationCode.add(BusinessConst.attach_createFolderAuthority);
					exParam.put("operationCodes", StringUtils.join(operationCode, ","));
					// 文件/文件夹项授权
					attachService.setMulTypeButtonAuthority(attachVoList, menuType, dataOrgId);
				}
			} else {
				params.put("parentTypeId", parentTypeId);
				params.put("userId", userId);
				attachVoList = attachService.queryTypeOrgAttachs(params);

				if("0".equals(isPublic)){
					//如果是普通文件夹
					operationCode.add(BusinessConst.attach_createFolderAuthority);
					operationCode.add(BusinessConst.attach_uploadAuthority);
					operationCode.add(BusinessConst.attach_mulMoveAuthority);
					operationCode.add(BusinessConst.attach_mulDeleteAuthority);
				}else{
					//如果是公共文件夹
					if(authorityService.isOrgAdmin(dataOrgId)){
						//如果是该机构管理员有上传、创建文件夹、批量删除、批量移动权限
						operationCode.add(BusinessConst.attach_createFolderAuthority);
						operationCode.add(BusinessConst.attach_uploadAuthority);
						operationCode.add(BusinessConst.attach_mulMoveAuthority);
						operationCode.add(BusinessConst.attach_mulDeleteAuthority);
					}
				}
				
				exParam.put("operationCodes", StringUtils.join(operationCode, ","));
				// 文件/文件夹项授权
				attachService.setMulTypeButtonAuthority(attachVoList, menuType, null);
			}
			// 封装查询条件
			// Map<String, Object> exParam = new HashMap<String, Object>();
			// 如果typeId为空说明首次进入页面,首次进入则显示"附件"根目录下的子文件和子文件夹
			// if (StringUtil.isEmpty(parentTypeId)) {
			// parentTypeId = typeService.queryTypeIdByCondition("-1", "File");
			// }
			// param.put("parentTypeId", parentTypeId);

			// param.put("roleIds", ClientUtil.getRoleQuoteIds());

			// 获得该文件夹下按钮权限
			// List<String> operationCodeList =
			// attachService.queryAuthority(param);
			// String operationCodes = StringUtils.join(operationCodeList, ",");
			// exParam.put("operationCodes", operationCodes);
			// 另外传递上传、批量删除等总体权限按钮到前台(通过datagrid的exParam传递过去)
			// dataGrid.setExParam(exParam);
			// 获得有权看到的子文件夹和文件,并传入operationCodeList为VO设置按钮权限
			// if (this.authorityService.currentUserIsAdmin()) {
			// attachVoList = attachService.queryAttachVoListByAdmin(param);
			// } else {
			// // 不是管理员，进行权限过滤
			// attachVoList = attachService.queryAuthorityToVo(param,
			// operationCodeList);
			// }
		}

		// 判断请求来源
		if (HttpUtils.isMoblie(request) || BusinessConst.ViewFlag_datasThumbnailView.equals(viewFlag)) {
			j.setObj(MapKit.create("result", attachVoList).put("exParam", exParam).getMap());
			return j;
		} else {
			dataGrid.setExParam(exParam);
			dataGrid.setResults(attachVoList);
			TagUtil.datagrid(response, dataGrid);
			return null;
		}

	}

	/**
	 * 请求附件平铺数据方法
	 * 
	 * @author luoheng
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "thumbnailData")
	public void thumbnailData(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 获取查询条件
			String typeId = request.getParameter("typeId");
			String fileFlag = request.getParameter("fileFlag");
			String attachType = request.getParameter("attachType");

			// 封装查询条件
			Map<String, Object> param = new HashMap<String, Object>();
			if (StringUtil.isNotEmpty(typeId)) {
				param.put("typeId", typeId);
			} else {
				typeId = typeService.queryTypeIdByCondition("-1", "File");
				param.put("typeId", typeId);
			}

			/**
			 * if (StringUtil.isNotEmpty(fileFlag)) param.put("fileFlag",
			 * fileFlag);
			 **/
			if (StringUtil.isNotEmpty(attachType))
				param.put("attachType", attachType);

			UserEntity user = ClientUtil.getUserEntity();
			param.put("userId", user.getId());
			// 如果关联了员工,需要往查询条件里防止员工Id
			/*
			 * if ("employee".equals(user.getUserType())) { param.put("empId",
			 * user.getUserTypeId()); }
			 */
			// 查询值
			List<String> operationCodeList = attachService.queryAuthority(param);
			String operationCodes = StringUtils.join(operationCodeList, ",");

			List<AttachVo> attachVoList = new ArrayList<AttachVo>();
			if (this.authorityService.currentUserIsAdmin()) {// 不是管理员，进行权限过滤
				attachVoList = attachService.queryAttachVoListByAdmin(param);
			} else {
				attachVoList = attachService.queryAuthorityToVo(param, operationCodeList);
			}

			StringBuilder sb = new StringBuilder();
			sb.append("{\"thumbnailData\":");
			JSONArray json = JSONArray.fromObject(attachVoList);
			sb.append(json.toString());
			sb.append(",\"exParam\":{\"operationCodes\":\"" + operationCodes + "\"}}");

			// 往前台传输
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			response.getWriter().write(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月15日 上午11:20:20
	 * @Decription 进入附件上传页面
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "dataUpload")
	public ModelAndView dataUpload(HttpServletRequest request, HttpServletResponse response) {
		String typeId = request.getParameter("typeId");
		TypeEntity entity = typeService.getType(typeId);
		if (entity != null) {
			request.setAttribute("typeId", typeId);
			request.setAttribute("typeName", entity.getName());
		}
		return new ModelAndView("platform/system/data/dataUpload");
	}

	/**
	 * 文件授权操作
	 * 
	 * @param request
	 */
	@RequestMapping(params = "saveFileAuthority")
	@ResponseBody
	public AjaxJson saveFileAuthority(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		String message = "";
		try {
			String aIds = request.getParameter("aIds");// 附件Ids
			String ids = request.getParameter("ids");// 角色Ids
			aIds = StringUtil.removeDot(aIds);// 附件Ids
			typeService.batchUpdateFileRoles(aIds, ids);
			message = "文件角色权限更新成功";
		} catch (Exception e) {
			message = "文件角色权限更新失败";
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 查询工作下的分类树 
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "queryWorkTree")
	@ResponseBody
	public AjaxJson queryWorkTree(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		AjaxJson j = new AjaxJson();

		String id = request.getParameter("id");
		String level = request.getParameter("level");
		String userId = ClientUtil.getUserId();
		List<TreeNode> treeList = new ArrayList<TreeNode>();
		// 需要转换的额外属性
		Map<String, String> propertyMapping = new HashMap<String, String>(); 
		
		if (StringUtil.isEmpty(id) && "0".equals(level)) {
			propertyMapping.put(TreeMapper.PropertyType.ICONCLS.getValue(),"logo");
			// 说明是机构级
			List<OrgnaizationEntity> orgList = userService.queryHomeCompany(userId);
			treeList = TreeMapper.buildJsonTree(orgList, propertyMapping);

			for (TreeNode node : treeList) {
				node.setState("closed");
				node.setLevel("1");
			}
		} else if ("1".equals(level)) {
			// 说明是应用级
			// 1.该用户具有权限的应用
			List<Map<String, Object>> ownAppList = flowFormService.queryAPPAllList(userId, id);
			for (Map<String, Object> map : ownAppList) {
				String state = "1".equals(map.get("isFlow").toString()) ? "open" : "closed";
				TreeNode node = new TreeNode();
				node.setId(map.get("code").toString());
				node.setLevel("2");
				node.setText(map.get("name").toString());
				node.setIconCls(map.get("logo").toString());
				node.setState(state);
				treeList.add(node);
			}
			// 2.该用户收到的文件(被传阅型)
			TreeNode node = new TreeNode();
			node.setId("workReceiveFile" + id);
			node.setLevel("2");
			node.setText("收到的文件");
			node.setState("open");
			treeList.add(node);
		} else if ("2".equals(level)) {
			// 说明是标题级,此时的id其实是应用formCode
			AppFormField titleField = appFormFieldService.queryTitleField(id);
			if (titleField != null) {
				// 说明要生成下级
				String tableName = ConfigConst.tablePrefix + id;// 主模板表名
				String fields = "id," + titleField.getCode();
				List<Map<String, Object>> titleDatas = appFormTableService.querySingle(tableName, fields, MapKit.create("createUserId", userId).getMap());
				for (Map<String, Object> map : titleDatas) {
					String text = map.get(titleField.getCode()) == null ? "" : map.get(titleField.getCode()).toString();
					TreeNode node = new TreeNode();
					node.setId(id);
					node.setLevel("3");
					node.setText(text);
					node.setState("open");
					node.setAttributes(MapKit.create("titleBusinessKey", map.get("id").toString()).getMap());
					treeList.add(node);
				}
			}
		}
		// 判断请求来源
		if (HttpUtils.isMoblie(request)) {
			j.setObj(treeList);
			return j;
		} else {
			TagUtil.tree(response, treeList);
			return null;
		}
	}

	/**
	 * 查询公司下的分类树 `
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "queryOrgTree")
	@ResponseBody
	public AjaxJson queryOrgTree(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		AjaxJson j = new AjaxJson();

		String id = request.getParameter("id");
		String userId = ClientUtil.getUserId();
		List<TreeNode> treeList = new ArrayList<TreeNode>();
		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ATTRIBUTES.getValue(), "rootTypeId");
		propertyMapping.put(TreeMapper.PropertyType.ICONCLS.getValue(),"logo");
		if (StringUtil.isEmpty(id)) {
			// 说明是机构级do
			List<OrgnaizationEntity> orgList = userService.queryHomeCompany(userId);
			treeList = TreeMapper.buildJsonTree(orgList, propertyMapping);
			for (TreeNode node : treeList) {
				node.setState("open");
				node.setLevel("1");
			}
		}
		// 判断请求来源
		if (HttpUtils.isMoblie(request)) {
			j.setObj(treeList);
			return j;
		} else {
			TagUtil.tree(response, treeList);
			return null;
		}
	}

	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "createFolder")
	@ResponseBody
	public AjaxJson createFolder(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		AjaxJson j = new AjaxJson();
		String parentTypeId = request.getParameter("parentTypeId");
		String typeName = request.getParameter("typeName");
		String menuType = request.getParameter("menuType");
		String dataOrgId = request.getParameter("dataOrgId");
		Integer isPublic = Integer.parseInt(StringUtil.isEmpty(request.getParameter("isPublic"),"0"));
		
		List<TypeEntity> brotherTypes= typeService.queryListByPorperty("parent.id", parentTypeId);
		for(TypeEntity type:brotherTypes){
			if(typeName.trim().equals(type.getName())){
				j.setSuccess(false);
				j.setMsg("该名称已在本目录存在");
				return j;
			}
		}
		if (BusinessConst.menuType_CODE_personal.equals(menuType)) {
		} else if (BusinessConst.menuType_CODE_org.equals(menuType)) {
			if (StringUtil.isEmpty(parentTypeId)) {
				// 说明在第一级新建文件夹
				TypeEntity orgRootType = typeService.queryCurrentOrgRootType(dataOrgId);
				if (orgRootType != null) {
					parentTypeId = orgRootType.getId();
				}
			}
		}
		String typeCode = PinyinUtil.converterToFirstSpell(typeName);
		typeCode = typeService.getUnRepeatCode(typeCode);
		TypeEntity rootType = new TypeEntity();
		rootType.setParent(new TypeEntity(parentTypeId));
		rootType.setName(typeName);
		rootType.setCode(typeCode);
		rootType.setType(menuType);
		rootType.setOrgId(dataOrgId);
		rootType.setIsPublic(isPublic);
		typeService.saveType(rootType);
		j.setMsg("文件夹创建成功");
		return j;
	}
	
	/**
	 * 公司文件夹授权操作
	 * 
	 * @param request
	 */
	@RequestMapping(params = "saveOrgTypeAuthority")
	@ResponseBody
	public AjaxJson saveOrgTypeAuthority(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		try {
			String typeIds = request.getParameter("typeIds");// 文件夹Ids
			String finalValue = request.getParameter("finalValue");// 综合选择Ids
			typeService.updateFileTypeAuthority(typeIds, finalValue);
			result.setMsg("文件夹权限更新成功");
		} catch (Exception e) {
			result.setMsg("文件夹权限更新失败");
		}
		return result;
	}
	
	/**
	 * 进入重命名的页面
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "goRename")
	public ModelAndView goRename(HttpServletRequest request) {
		String id = request.getParameter("id");
		String fileFlag = request.getParameter("fileFlag");
		String name = request.getParameter("name");
		String parentTypeId = request.getParameter("parentTypeId");
		request.setAttribute("id", id);
		request.setAttribute("parentTypeId", parentTypeId);
		request.setAttribute("fileFlag", fileFlag);
		request.setAttribute("name", name);
		return new ModelAndView("platform/system/data/renameEdit");
	}
	
	/**
	 * 重命名方法
	 * @param request
	 * @return
	 * @throws BusinessException 
	 */
	@RequestMapping(params = "doRename")
	@ResponseBody
	public AjaxJson doRename(HttpServletRequest request) throws BusinessException {
		AjaxJson j = new AjaxJson();
		String fileFlag = request.getParameter("fileFlag");
		String id = request.getParameter("id");
		String parentTypeId = request.getParameter("parentTypeId");
		String name = request.getParameter("name");
		
		List<TypeEntity> brotherTypes= typeService.queryListByPorperty("parent.id", parentTypeId);
		for(TypeEntity type:brotherTypes){
			if(name.trim().equals(type.getName())){
				j.setSuccess(false);
				j.setMsg("该名称已在本目录存在");
				return j;
			}
		}

		List<AttachVo> attachVoList=attachService.queryUploadAttach(MapKit.create("typeEntity", parentTypeId).getMap());
		for (AttachVo vo : attachVoList) {
			if (name.trim().equals(vo.getName())) {
				j.setSuccess(false);
				j.setMsg("该名称已在本目录存在");
				return j;
			}
		}
		if ("0".equals(fileFlag)) {
			typeService.updateName(name, id);
		} else if ("1".equals(fileFlag)) {
			attachService.updateAttachName(name, id);
		}
		j.setMsg("修改成功");
		return j;
	}
	
	@RequestMapping(params = "deleteFile")
	@ResponseBody
	public AjaxJson deleteFile(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		AjaxJson result = new AjaxJson();
		String aId = request.getParameter("aId");
		attachService.deleteFile(aId);
		result.setMsg("文件删除成功");
		return result;
	}
	
	/**
	 * 文件夹和文件的删除
	 * @param request
	 * @param response
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		AjaxJson result = new AjaxJson();

		String id = request.getParameter("id");
		String fileFlag = request.getParameter("fileFlag");
		String finalValue = request.getParameter("finalValue");
		
		//兼容单个删除
		if (StringUtil.isNotEmpty(id) && StringUtil.isNotEmpty(fileFlag)) {
			JSONArray array = new JSONArray();
			JSONObject singleObj = new JSONObject();
			singleObj.accumulate("id", id);
			singleObj.accumulate("fileFlag", fileFlag);
			array.add(singleObj);
			finalValue = array.toString();
		}
		attachService.deleteMul(finalValue);
		result.setMsg("删除成功");
		return result;
	}
	
	@RequestMapping(params = "queryOrgTypeAuthority")
	@ResponseBody
	public AjaxJson queryOrgTypeAuthority(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		AjaxJson j = new AjaxJson();
		String typeId = request.getParameter("typeId");
		j.setObj(typeService.queryOrgTypeAuthority(typeId));
		return j;
	}
	
	/**
	 * 从云盘选择文件
	 * @param request
	 * @param response
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "selectCloudAttach")
	@ResponseBody
	public AjaxJson selectCloudAttach(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		AjaxJson j = new AjaxJson();
		String attachIds = StringUtil.isEmpty(request.getParameter("attachIds"), null);// 选中的多条附件
		String businessKey = StringUtil.isEmpty(request.getParameter("businessKey"), null);
		String businessType = StringUtil.isEmpty(request.getParameter("businessType"), null);
		String businessExtra = StringUtil.isEmpty(request.getParameter("businessExtra"), null);
		String otherKey = StringUtil.isEmpty(request.getParameter("otherKey"), null);
		String otherKeyType = StringUtil.isEmpty(request.getParameter("otherKeyType"), null);

		List<AttachJsonModel> list = attachService.copyCloudAttach(attachIds, businessKey, businessType, businessExtra, otherKey, otherKeyType);
		j.setObj(list);
		return j;
	}
	
	/**
	 * 为多场景(比如移动、保存到云盘等)查询文件夹结构
	 * @param request
	 * @param response
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "queryFolderForSelect")
	@ResponseBody
	public AjaxJson queryFolderForSelect(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		AjaxJson j = new AjaxJson();
		String menuType = request.getParameter("menuType");
		String folderIds=request.getParameter("folderIds");
		String parentTypeId=request.getParameter("parentTypeId");
		String chargePublic=StringUtil.isEmpty(request.getParameter("chargePublic"),"0");
		String dataOrgId = request.getParameter("dataOrgId");
		String userId=ClientUtil.getUserId();
		// 封装查询条件
		Map<String, Object> params = new HashMap<String, Object>();
		List<AttachVo> attachVoList = new ArrayList<AttachVo>();
		if (BusinessConst.menuType_CODE_personal.equals(menuType)) {
			if(StringUtil.isEmpty(parentTypeId)){
				// 获得个人根文件夹
				parentTypeId = typeService.doQueryPersonalRootType(ClientUtil.getUserId());
			}
			
			params.put("userId", userId);
			params.put("parentTypeId", parentTypeId);
			attachVoList = attachService.queryPersonalAttachs(params);
			
		} else if (BusinessConst.menuType_CODE_org.equals(menuType)) {
			if (StringUtil.isEmpty(parentTypeId)) {
				parentTypeId = typeService.queryOrgRootType(dataOrgId);
				if (StringUtil.isNotEmpty(parentTypeId)) {
					// 说明是第一级目录
					params.put("dataOrgId", dataOrgId);
					params.put("userId", userId);

					List<String> orgIds = StringUtil.addQuotes(sysUserService.getDistinctUpOrgIds(userId));
					params.put("orgIds", orgIds);
					params.put("parentTypeId", parentTypeId);
					attachVoList = attachService.queryRootOrgAttachs(params);
					
					
					if ("1".equals(chargePublic)) {
						//根据是否管理员来判断显示公共文件夹,不是的话要剔除公共文件夹
						if(!authorityService.isOrgAdmin(dataOrgId)){
							Iterator<AttachVo> it = attachVoList.iterator();
							while (it.hasNext()) {
								AttachVo vo = it.next();
								if (vo.getFileFlag().equals(0)) {
									if (vo.getCode().equals(dataOrgId)) {
										it.remove();
									}
								}
							}
						}
					}

				}
			} else {
				params.put("parentTypeId", parentTypeId);
				params.put("userId", userId);
				attachVoList = attachService.queryTypeOrgAttachs(params);
			}
		}
		
		//如果有当前选中的文件夹要进行排除
		if (StringUtil.isNotEmpty(folderIds)) {
			Iterator<AttachVo> it = attachVoList.iterator();
			while (it.hasNext()) {
				AttachVo vo = it.next();
				if (vo.getFileFlag().equals(0)) {
					if (folderIds.indexOf(vo.getId()) != -1) {
						it.remove();
					}
				}
			}
		}
		j.setObj(attachVoList);
		return j;
	}
	
	/**
	 * 移动文件夹及文件到新文件夹下
	 * @param request
	 * @param response
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "moveToFolder")
	@ResponseBody
	public AjaxJson moveToFolder(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		AjaxJson j = new AjaxJson();
		String finalValue = request.getParameter("finalValue");// 文件夹和文件的json数据
		String targetTypeId = request.getParameter("targetTypeId");// 移动到的目标文件夹
		String sourceTypeId = request.getParameter("sourceTypeId");// 移动前的文件夹
		
		if(sourceTypeId.equals(targetTypeId)){
			j.setSuccess(false);
			j.setMsg("所选文件夹与原文件夹相同");
			return j;
		}
		String id = request.getParameter("id");
		String fileFlag = request.getParameter("fileFlag");

		// 兼容单个移动
		if (StringUtil.isNotEmpty(id) && StringUtil.isNotEmpty(fileFlag)) {
			JSONArray array = new JSONArray();
			JSONObject singleObj = new JSONObject();
			singleObj.accumulate("id", id);
			singleObj.accumulate("fileFlag", fileFlag);
			array.add(singleObj);
			finalValue = array.toString();
		}

		attachService.updateBelongType(finalValue, targetTypeId);
		j.setMsg("移动成功");
		return j;
	}
	
	/**
	 * 将表单的附件发布到云盘某个文件夹
	 * @param request
	 * @param response
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "doPublishToDisk")
	@ResponseBody
	public AjaxJson doPublishToDisk(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		AjaxJson j = new AjaxJson();
		String typeId = StringUtil.isEmpty(request.getParameter("typeId"), null);// 选中的多条附件
		String otherKey = StringUtil.isEmpty(request.getParameter("otherKey"), null);
		String otherKeyType = StringUtil.isEmpty(request.getParameter("otherKeyType"), null);

		attachService.doPublishToDisk(typeId, otherKey, otherKeyType);
		j.setMsg("发布成功");
		return j;
	}
	
	@RequestMapping(params = "doSaveChatFileToDisk")
	@ResponseBody
	public AjaxJson doSaveChatFileToDisk(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxJson j = new AjaxJson();
		String remoteUrl = request.getParameter("remoteUrl");// 环信远程地址
		String typeId = request.getParameter("typeId"); // 保存的网盘文件夹Id
		String fileName = request.getParameter("fileName"); // 文件名

		return attachService.doSaveChatFileToDisk(remoteUrl, typeId, fileName);
	}
}
