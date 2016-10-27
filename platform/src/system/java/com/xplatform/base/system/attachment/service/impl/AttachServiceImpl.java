/**
 * Copyright (c) 2014
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.xplatform.base.system.attachment.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import jodd.util.StringBand;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.HttpClientUtils;
import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.hibernate.qbc.PageList;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.FTPUtil;
import com.xplatform.base.framework.core.util.FileUtils;
import com.xplatform.base.framework.core.util.ImageUtils;
import com.xplatform.base.framework.core.util.InputStreamCache;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.MD5Util;
import com.xplatform.base.framework.core.util.MapKit;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.PropertiesUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.pdf.HttpUtils;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.def.ConfigConst;
import com.xplatform.base.platform.common.service.AuthorityService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.system.attachment.dao.AttachDao;
import com.xplatform.base.system.attachment.dao.FTPAttachDao;
import com.xplatform.base.system.attachment.entity.AttachEntity;
import com.xplatform.base.system.attachment.entity.FTPAttachEntity;
import com.xplatform.base.system.attachment.model.AttachJsonModel;
import com.xplatform.base.system.attachment.mybatis.dao.AttachMybatisDao;
import com.xplatform.base.system.attachment.mybatis.vo.AttachVo;
import com.xplatform.base.system.attachment.service.AttachService;
import com.xplatform.base.system.dict.entity.DictTypeEntity;
import com.xplatform.base.system.dict.service.DictTypeService;
import com.xplatform.base.system.type.entity.TypeEntity;
import com.xplatform.base.system.type.service.TypeService;

/**
 * description :
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月2日 下午6:08:57
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年7月2日 下午6:08:57 
 *
*/
@Service("attachService")
public class AttachServiceImpl extends BaseServiceImpl<AttachEntity> implements AttachService {
	@Resource
	public void setBaseDao(AttachDao attachDao) {
		super.setBaseDao(attachDao);
	}
	
	@Resource
	private CommonDao commonDao;
	@Resource
	private AttachDao attachDao;
	@Resource
	private TypeService typeService;
	@Resource
	private FTPAttachDao ftpAttachDao;
	@Resource
	private AttachMybatisDao attachMybatisDao;
	@Resource
	private AuthorityService authorityService;

	@Override
	public String getFunPath(String typeId) {
		//功能路径(common、data)等
		String funPath = "";
		if (StringUtils.isNotEmpty(typeId)) {
//			TypeEntity root = typeService.getRootById(typeId);
//			if ("file".equals(root.getSysType())) {
//				funPath = "data";
//			}
			funPath = "data";
		} else {
			//没有传入分类id,就放到common大目录下
			funPath = "common";
		}
		return funPath;
	}

	@Override
	public String getDatePath() {
		String datePath = "";
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = (cal.get(Calendar.MONTH)) + 1;
		int day_of_month = cal.get(Calendar.DAY_OF_MONTH);
		datePath = year + File.separator + month + File.separator + day_of_month;
		return datePath;
	}

	@Override
	public String getUploadBasePath() {
		PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
		return p.readProperty("uploadpath");
	}

	@Override
	public Boolean isNeedThumbnail(){
		PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
		return new Boolean(p.readProperty("needThumbnail"));
	}

	@Override
	public String getThumbnailDirName() throws BusinessException {
		PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
		return p.readProperty("thumbnailDirName");
	}

	@Override
	public Integer getScaleWidth() throws BusinessException {
		PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
		if (StringUtils.isNotEmpty(p.readProperty("scaleWidth"))) {
			return new Integer(p.readProperty("scaleWidth"));
		}
		return null;
	}

	@Override
	public Integer getScaleHeight() throws BusinessException {
		PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
		if (StringUtils.isNotEmpty(p.readProperty("scaleHeight"))) {
			return new Integer(p.readProperty("scaleHeight"));
		}
		return null;
	}

	@Override
	public String getAfterDocBaseDir() throws BusinessException {
		PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
		return p.readProperty("afterDocBaseDir");
	}

	@Override
	public String getTypePath(String typeId) throws BusinessException {
		String typePath = "";
		TypeEntity typeEntity = typeService.getType(typeId);
		typePath = typeService.parseTreeIndexToCodePath(typeEntity.getTreeIndex(), File.separator);
		if (BusinessConst.menuType_NAME_personal.equals(typeEntity.getType())) {
			typePath = typeEntity.getCreateUserId() + File.separator + typePath;
		} else if (BusinessConst.menuType_NAME_work.equals(typeEntity.getType())) {
			typePath = typeEntity.getOrgId() + File.separator + typePath;
		} else if (BusinessConst.menuType_NAME_org.equals(typeEntity.getType())) {
			typePath = typeEntity.getOrgId() + File.separator + typePath;
		}
		return typePath;
	}

	@Override
	public List<AttachEntity> queryAttachEntityByMD5(String MD5) throws BusinessException {
		String hql = "FROM AttachEntity a WHERE a.MD5=?";
		List<AttachEntity> attachEntityList = attachDao.findHql(hql, MD5);
		return attachEntityList;
	}

	@Override
	public Boolean isMD5FileExsits(String MD5, boolean validFileExists, String storageType) throws BusinessException {
		Boolean flag = false;
		List<AttachEntity> attachEntityList = this.queryAttachEntityByMD5(MD5);
		if (attachEntityList.size() == 0) {
			return flag;
		} else {
			if (validFileExists) {
				if ("0".equals(storageType)) {
					//本地再验证一遍真实文件是否存在
					for (AttachEntity attachEntity : attachEntityList) {
						if (StringUtils.isNotEmpty(attachEntity.getAbsolutePath())) {
							if (FileUtils.isFileExist(attachEntity.getAbsolutePath())) {
								flag = true;
								return flag;
							}
						}
					}
				} else if ("1".equals(storageType)) {
					//FTP服务器方式暂未实现
				}
			} else {
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public String queryAbsolutePathByMD5(String MD5) throws BusinessException {
		String absolutePath = null;
		List<AttachEntity> attachEntityList = this.queryAttachEntityByMD5(MD5);
		for (AttachEntity attachEntity : attachEntityList) {
			if (StringUtils.isNotEmpty(attachEntity.getAbsolutePath())) {
				if (FileUtils.isFileExist(attachEntity.getAbsolutePath())) {
					absolutePath = attachEntity.getAbsolutePath();
					return absolutePath;
				}
			}
		}
		return absolutePath;
	}

	@Override
	public void deleteFile(String aid) throws BusinessException {
		try {
			AttachEntity entity = this.get(aid);
			List<AttachEntity> list = this.queryAttachEntityByMD5(entity.getMD5());
			String storageId = entity.getStorageId();
			String storageType = entity.getStorageType();
			String attachType = entity.getAttachType();

			// 删除附件记录
			attachDao.deleteEntityById(aid);
			// 删除物理文件(如果是最后一条MD5记录则进行删除,否则保留物理文件,因为其他附件还要使用该物理文件)
			if (list.size() == 1) {
				if ("0".equals(storageType)) {
					String absolutePath = entity.getAbsolutePath();
					// 删除本地文件
					FileUtils.delete(absolutePath);
				} else if ("1".equals(storageType)) {
					FTPAttachEntity ftp = ftpAttachDao.get(storageId);
					String remoteFilePath = ftp.getRemoteFilePath();
					String remoteThumbnailFilePath = ftp.getRemoteThumbnailFilePath();
					// 删除FTP附件关联记录
					ftpAttachDao.deleteEntityById(storageId);

					// 删除FTP文件
					FTPUtil ftpUtil = new FTPUtil();
					ftpUtil.connectServer();
					ftpUtil.deleteFile(remoteFilePath);
					if ("img".equals(attachType) && isNeedThumbnail()) {
						ftpUtil.deleteFile(remoteThumbnailFilePath);
					}
					ftpUtil.closeConnect();
				}
			}
		} catch (Exception e) {
			throw new BusinessException("附件删除失败");
		}
	}

	@Override
	public void deleteMulFile(String aids) throws BusinessException {
		String[] array = aids.split(",");
		for (String aid : array) {
			this.deleteFile(aid);
		}
	}

	@Override
	public FTPAttachEntity queryFTPAttachEntityByMD5(String md5) throws BusinessException {
		AttachEntity entity = this.queryAttachEntityByMD5(md5).get(0);
		String ftpID = entity.getStorageId();
		return ftpAttachDao.get(ftpID);
	}

	@Override
	public String getStorageType() {
		PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
		return p.readProperty("dataManager_StorageType");
	}

	@Override
	public FTPAttachEntity queryFTPAttachEntityByAttach(String attachId) {
		AttachEntity entity = attachDao.get(attachId);
		return ftpAttachDao.get(entity.getStorageId());
	}

	@Override
	public Boolean getIsChangeType() {
		PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
		if (StringUtils.isNotEmpty(p.readProperty("isChangeType"))) {
			return new Boolean(p.readProperty("isChangeType"));
		}
		return null;
	}

	@Override
	public String getRootTreeCode() {
		PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
		if (StringUtils.isNotEmpty(p.readProperty("rootTreeCode"))) {
			return p.readProperty("rootTreeCode");
		}
		return null;
	}

	@Override
	public Boolean getOnlyAuthority() {
		PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
		if (StringUtils.isNotEmpty(p.readProperty("onlyAuthority"))) {
			return new Boolean(p.readProperty("onlyAuthority"));
		}
		return null;
	}

	@Override
	public Boolean getContainSelf(){
		PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
		if (StringUtils.isNotEmpty(p.readProperty("containSelf"))) {
			return new Boolean(p.readProperty("containSelf"));
		}
		return null;
	}

	@Override
	public Integer getPortraitPlainHeight() throws BusinessException {
		PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
		if (StringUtils.isNotEmpty(p.readProperty("portraitPlainHeight"))) {
			return new Integer(p.readProperty("portraitPlainHeight"));
		}
		return null;
	}

	@Override
	public Integer getPortraitPlainWidth() throws BusinessException {
		PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
		if (StringUtils.isNotEmpty(p.readProperty("portraitPlainWidth"))) {
			return new Integer(p.readProperty("portraitPlainWidth"));
		}
		return null;
	}

	@Override
	public void storeLocalFile(Map<String, Object> param) throws Exception {
		InputStream in = null;
		String realUUIDFileName = param.get("realUUIDFileName").toString();
		if (BeanUtils.isNotEmpty(param.get("in"))) {
			in = (InputStream) param.get("in");
		} else {
			MultipartFile mpf = (MultipartFile) param.get("mpf");
			in = mpf.getInputStream();
		}

		String attachType = param.get("attachType").toString();
		String absoluteUploadPath = param.get("absoluteUploadPath").toString();
		String thumbnailAbPath = param.get("thumbnailAbPath").toString();

		// 1.建立父文件夹
		FileUtils.createFolder(absoluteUploadPath, false);
		// 2.复制文件到文件夹
 		FileCopyUtils.copy(in, new FileOutputStream(absoluteUploadPath + realUUIDFileName));
		/* 图片缩略图压缩逻辑 */
		if ("img".equals(attachType) && isNeedThumbnail()) {
			ImageUtils.scale(absoluteUploadPath + realUUIDFileName, thumbnailAbPath + realUUIDFileName, getScaleHeight(), getScaleWidth(), true);
		}
	}

	@Override
	public void storeLocalPortraitFile(Map<String, Object> param) throws Exception {
		String realUUIDFileName = param.get("realUUIDFileName").toString();
		MultipartFile mpf = (MultipartFile) param.get("mpf");
		String attachType = param.get("attachType").toString();
		String absoluteUploadPath = param.get("absoluteUploadPath").toString();
		String thumbnailAbPath = param.get("thumbnailAbPath").toString();

		String imageType = FileUtils.getExtend(mpf.getOriginalFilename());
		Integer x = Integer.parseInt(param.get("x").toString());
		Integer y = Integer.parseInt(param.get("y").toString());
		Integer w = Integer.parseInt(param.get("w").toString());
		Integer h = Integer.parseInt(param.get("h").toString());
		// 1.建立父文件夹
		FileUtils.createFolder(absoluteUploadPath, false);
		// 2.头像裁剪逻辑,返回输入流
		InputStream is = ImageUtils.abscut(mpf.getInputStream(), imageType, x, y, w, h, getPortraitPlainHeight(),
				getPortraitPlainWidth());
		is = ImageUtils.scale(is, imageType, getPortraitPlainHeight(), getPortraitPlainWidth(), true);
		// 3.复制文件到文件夹
		FileCopyUtils.copy(is, new FileOutputStream(absoluteUploadPath + realUUIDFileName));
		/* 图片缩略图压缩逻辑 */
		if ("img".equals(attachType) && isNeedThumbnail()) {
			ImageUtils.scale(absoluteUploadPath + realUUIDFileName, thumbnailAbPath + realUUIDFileName,
					getScaleHeight(), getScaleWidth(), true);
		}
	}

	@Override
	public void storeFTPPortraitFile(Map<String, Object> param) throws Exception {
		String realUUIDFileName = param.get("realUUIDFileName").toString();
		MultipartFile mpf = (MultipartFile) param.get("mpf");
		String attachType = param.get("attachType").toString();
		String remotePath = param.get("remotePath").toString();
		String remoteThumbnailPath = param.get("remoteThumbnailPath").toString();

		String imageType = FileUtils.getExtend(mpf.getOriginalFilename());
		Integer x = Integer.parseInt(param.get("x").toString());
		Integer y = Integer.parseInt(param.get("y").toString());
		Integer w = Integer.parseInt(param.get("w").toString());
		Integer h = Integer.parseInt(param.get("h").toString());

		FTPUtil ftpUtil = new FTPUtil();
		ftpUtil.connectServer();
		// 1.头像裁剪逻辑,返回输入流
		InputStream is = ImageUtils.abscut(mpf.getInputStream(), imageType, x, y, w, h, getPortraitPlainHeight(),
				getPortraitPlainWidth());
		// 2.上传头像裁剪后文件(自带创建文件夹逻辑)
		ftpUtil.uploadFile(is, realUUIDFileName, remotePath.replaceAll("\\\\", "/"));
		// 3.假如图片需要缩略图
		if ("img".equals(attachType) && isNeedThumbnail()) {
			/* 图片缩略图压缩逻辑 */
			InputStream thumbnailIS = ImageUtils.scale(mpf.getInputStream(), imageType, getScaleHeight(),
					getScaleWidth(), true);
			//将缩略图流上传到FTP服务器
			ftpUtil.uploadFile(thumbnailIS, realUUIDFileName, remoteThumbnailPath.replaceAll("\\\\", "/"));
		}
		ftpUtil.closeConnect();
	}

	@Override
	public void storeFTPFile(Map<String, Object> param) throws Exception {
		String realUUIDFileName = param.get("realUUIDFileName").toString();
		MultipartFile mpf = (MultipartFile) param.get("mpf");
		String attachType = param.get("attachType").toString();
		String remotePath = param.get("remotePath").toString();
		String remoteThumbnailPath = param.get("remoteThumbnailPath").toString();

		String imageType = FileUtils.getExtendWithDot(mpf.getOriginalFilename());

		FTPUtil ftpUtil = new FTPUtil();
		ftpUtil.connectServer();
		// 1.上传原始文件(自带创建文件夹逻辑)
		ftpUtil.uploadFile(mpf.getInputStream(), realUUIDFileName, remotePath.replaceAll("\\\\", "/"));
		// 2.假如图片需要缩略图
		if ("img".equals(attachType) && isNeedThumbnail()) {
			/* 图片缩略图压缩逻辑 */
			InputStream thumbnailIS = ImageUtils.scale(mpf.getInputStream(), imageType, getScaleHeight(),
					getScaleWidth(), true);
			//将缩略图流上传到FTP服务器
			ftpUtil.uploadFile(thumbnailIS, realUUIDFileName, remoteThumbnailPath.replaceAll("\\\\", "/"));
		}
		ftpUtil.closeConnect();
	}
	
	@Override
	public List<AttachVo> queryAttachVoList(Map<String, Object> param) {
		List<AttachVo> list = new ArrayList<AttachVo>();
		// 查出所有可看到的子文件和文件夹
		list = attachMybatisDao.queryAttachVoList(param);
		return list;
	}

	@Override
	public List<AttachVo> queryAttachVoListByAdmin(Map<String, Object> param) {
		List<AttachVo> list = new ArrayList<AttachVo>();
		list = attachMybatisDao.queryAttachVoListByAdmin(param);
		return list;
	}

	@Override
	public List<AttachVo> queryAuthorityToVo(Map<String, Object> param, List<String> operationCode) {
		List<AttachVo> list=queryAllAttachVoList(param);
		if (operationCode.size() > 0) {
			for (AttachVo vo : list) {
				// 如果是文件类型
				if (vo.getFileFlag().equals(1)) {
					for (String code : operationCode) {
						if (BusinessConst.attach_downloadAuthority.equals(code)) {
							vo.setDownloadAuthority("1");
						} else if (BusinessConst.attach_uploadAuthority.equals(code)) {
							vo.setUploadAuthority("1");
						} else if (BusinessConst.attach_renameAuthority.equals(code)) {
							vo.setRenameAuthority("1");
						} else if (BusinessConst.attach_deleteAuthority.equals(code)) {
							vo.setDeleteAuthority("1");
						} else if (BusinessConst.attach_moveAuthority.equals(code)) {
							vo.setMoveAuthority("1");
						} else if (BusinessConst.attach_createFolderAuthority.equals(code)) {
							vo.setCreateFolderAuthority("1");
						}
					}
				}
			}
		}
		return list;
	}
	
	@Override
	public List<AttachVo> setMulTypeButtonAuthority(List<AttachVo> list, String currentMenu, String currentOrg) {
		List<String> operationCodeList = new ArrayList<String>();//最终权限
		if (BusinessConst.menuType_CODE_personal.equals(currentMenu)) {
			for (AttachVo vo : list) {
				operationCodeList.clear();
				// 文件夹类型权限
				if (vo.getFileFlag().equals(0)) {
					operationCodeList.add(BusinessConst.attach_uploadAuthority);
					operationCodeList.add(BusinessConst.attach_renameAuthority);
					operationCodeList.add(BusinessConst.attach_deleteAuthority);
					operationCodeList.add(BusinessConst.attach_moveAuthority);
					operationCodeList.add(BusinessConst.attach_createFolderAuthority);
				} else if (vo.getFileFlag().equals(1)) {
					//文件类型权限
					operationCodeList.add(BusinessConst.attach_downloadAuthority);
					operationCodeList.add(BusinessConst.attach_renameAuthority);
					operationCodeList.add(BusinessConst.attach_deleteAuthority);
					operationCodeList.add(BusinessConst.attach_moveAuthority);
				}
				setButtonAuthority(vo, operationCodeList);
			}
		} else if (BusinessConst.menuType_CODE_work.equals(currentMenu)) {
			for (AttachVo vo : list) {
				operationCodeList.clear();
				// 文件类型权限
				if (vo.getFileFlag().equals(1)) {
					operationCodeList.add(BusinessConst.attach_downloadAuthority);
				}
				setButtonAuthority(vo, operationCodeList);
				// 工作栏目下无文件夹,故无权限分配
			}
		} else if (BusinessConst.menuType_CODE_org.equals(currentMenu)) {
			if (StringUtil.isNotEmpty(currentOrg)) {
				// 传了当前机构currentOrg说明是公司下第一级目录
				for (AttachVo vo : list) {
					operationCodeList.clear();
					if (vo.getFileFlag().equals(0)) {
						// 文件夹类型权限
						// 公共文件夹本体所有人无权限,如果是非公共文件夹
						if (vo.getIsPublic().equals(0)) {
							// 如果是当前文件夹创建人,有全权限;否则无权限
							if (ClientUtil.getUserId().equals(vo.getCreateUserId())) {
								operationCodeList.add(BusinessConst.attach_downloadAuthority);
								operationCodeList.add(BusinessConst.attach_uploadAuthority);
								operationCodeList.add(BusinessConst.attach_renameAuthority);
								operationCodeList.add(BusinessConst.attach_deleteAuthority);
								operationCodeList.add(BusinessConst.attach_createFolderAuthority);
								operationCodeList.add(BusinessConst.attach_folderAuthority);
							}
						}
					}
					setButtonAuthority(vo, operationCodeList);
					// 第一级无文件,故无权限分配
				}
			} else {
				// 后级文件项权限
				for (AttachVo vo : list) {
					operationCodeList.clear();
					// 文件夹类型权限
					if (vo.getFileFlag().equals(0)) {
						// 如果是当前文件夹创建人,有全权限
						if (ClientUtil.getUserId().equals(vo.getCreateUserId())) {
							operationCodeList.add(BusinessConst.attach_uploadAuthority);
							operationCodeList.add(BusinessConst.attach_renameAuthority);
							operationCodeList.add(BusinessConst.attach_deleteAuthority);
							operationCodeList.add(BusinessConst.attach_moveAuthority);
							operationCodeList.add(BusinessConst.attach_createFolderAuthority);
						}
					} else if (vo.getFileFlag().equals(1)) {
						// 文件类型权限
						if (ClientUtil.getUserId().equals(vo.getCreateUserId())) {
							// 如果是当前文件创建人,有全权限
							operationCodeList.add(BusinessConst.attach_downloadAuthority);
							operationCodeList.add(BusinessConst.attach_renameAuthority);
							operationCodeList.add(BusinessConst.attach_deleteAuthority);
							operationCodeList.add(BusinessConst.attach_moveAuthority);
						} else {
							// 否则只有下载权限
							operationCodeList.add(BusinessConst.attach_downloadAuthority);
						}
					}
					setButtonAuthority(vo, operationCodeList);
				}
			}
		}

		return list;
	}
	
	@Override
	public AttachVo setButtonAuthority(AttachVo vo, List<String> operationCode) {
		if (operationCode.size() > 0) {
			// 如果是文件类型
			for (String code : operationCode) {
				if (BusinessConst.attach_downloadAuthority.equals(code)) {
					vo.setDownloadAuthority("1");
				} else if (BusinessConst.attach_uploadAuthority.equals(code)) {
					vo.setUploadAuthority("1");
				} else if (BusinessConst.attach_renameAuthority.equals(code)) {
					vo.setRenameAuthority("1");
				} else if (BusinessConst.attach_deleteAuthority.equals(code)) {
					vo.setDeleteAuthority("1");
				} else if (BusinessConst.attach_moveAuthority.equals(code)) {
					vo.setMoveAuthority("1");
				} else if (BusinessConst.attach_createFolderAuthority.equals(code)) {
					vo.setCreateFolderAuthority("1");
				} else if (BusinessConst.attach_folderAuthority.equals(code)) {
					vo.setFolderAuthority("1");
				}
			}
		}
		return vo;
	}
	
	public List<AttachVo> queryAllAttachVoList(Map<String, Object> param){
		List<AttachVo> allAttachlist = new ArrayList<AttachVo>();
//		List<AttachVo> typeAttachlist = new ArrayList<AttachVo>();
//		List<AttachVo> frAttachlist = new ArrayList<AttachVo>();
//		Map<String,AttachVo> map=new HashMap<String,AttachVo>();
//		for(AttachVo vo:typeAttachlist){
//			map.put(vo.getId(), vo);
//		}
		allAttachlist = attachMybatisDao.queryAttachVoList(param);
//		typeAttachlist=attachMybatisDao.queryAttachVoList(param);
//		for(AttachVo vo:frAttachlist){
//			if(map.containsKey(vo.getId())){
//				map.remove(vo.getId());
//			}
//		}
		return allAttachlist;
	}

	@Override
	public List<String> queryAuthority(Map<String, Object> param) {
		List<String> list=attachMybatisDao.queryAuthority(param);
		if(authorityService.currentUserIsAdmin()){
			list.add("attachManager_batchFileAuthority_other");
		}
		return list;
	}

	@Override
	public List<AttachVo> queryUploadAttach(Map<String, Object> param) {
		return attachMybatisDao.queryUploadAttach(param);
	}

	@Override
	public List<AttachEntity> queryAttachsByIds(String aIds) throws BusinessException {
		List<AttachEntity> attachList = new ArrayList<AttachEntity>();
		if (StringUtil.isNotEmpty(aIds)) {
			String hql = "FROM AttachEntity WHERE id in (:ids)";
			Query q = this.commonDao.getSession().createQuery(hql);
			q.setParameterList("ids", aIds.split(","));
			attachList = q.list();
		}
		return attachList;
	}

	@Override
	public List<AttachVo> queryPersonalAttachs(Map<String, Object> param){
		return attachMybatisDao.queryPersonalAttachs(param);
	}
	
	@Override
	public List<AttachVo> queryNotifyTypeAttachs(Map<String, Object> param) {
		return attachMybatisDao.queryNotifyTypeAttachs(param);
	}
	
	@Override
	public List<AttachVo> queryRootOrgAttachs(Map<String, Object> param) {
		return attachMybatisDao.queryRootOrgAttachs(param);
	}
	
	@Override
	public List<AttachVo> queryTypeOrgAttachs(Map<String, Object> param) {
		return attachMybatisDao.queryTypeOrgAttachs(param);
	}

	@Override
	public void updateAttachName(String name, String id) throws BusinessException {
		String onlyName = FileUtils.getFilePrefix2(name);
		String hql = "UPDATE FROM AttachEntity SET attachName=?,onlyName=? WHERE id=?";
		attachDao.executeHql(hql, name, onlyName, id);
	}
	
	@Override
	public AttachJsonModel convertAttach(AttachEntity newAttachEntity) {
		AttachJsonModel filesJson = new AttachJsonModel();
		String backJsonThumbnailPath = ConfigConst.attachThumbnailRequest + newAttachEntity.getId();
		String backJsonDownloadUrl = ConfigConst.attachRequest + newAttachEntity.getId();
		String backJsonDeleteUrl = "attachController.do?deleteFile&aId=" + newAttachEntity.getId();

		filesJson.setBusinessKey(newAttachEntity.getBusinessKey());
		filesJson.setBusinessType(newAttachEntity.getBusinessType());
		filesJson.setBusinessExtra(newAttachEntity.getBusinessExtra());
		filesJson.setOtherKey(newAttachEntity.getOtherKey());
		filesJson.setOtherKeyType(newAttachEntity.getOtherKeyType());

		filesJson.setId(newAttachEntity.getId());
		filesJson.setName(newAttachEntity.getAttachName());
		filesJson.setExt(newAttachEntity.getExt());
		filesJson.setIconType(newAttachEntity.getIconType());
		filesJson.setSize(newAttachEntity.getAttachSize());
		filesJson.setSizeStr(newAttachEntity.getAttachSizeStr());
		filesJson.setInfo("上传成功");
		filesJson.setDownloadUrl(backJsonDownloadUrl);
		filesJson.setThumbnailUrl(backJsonThumbnailPath);
		filesJson.setDeleteUrl(backJsonDeleteUrl);
		filesJson.setDeleteType("DELETE");
		return filesJson;
	}

	@Override
	public List<AttachJsonModel> copyCloudAttach(String attachIds, String businessKey, String businessType, String businessExtra, String otherKey,
			String otherKeyType) throws BusinessException {
		List<AttachJsonModel> voList = new ArrayList<AttachJsonModel>();
		List<AttachEntity> list = getIds(attachIds);
		for (AttachEntity attach : list) {
			AttachEntity newAttach = new AttachEntity();
			BeanUtils.copyNotNullProperties(newAttach, attach);
			newAttach.setBusinessKey(businessKey);
			newAttach.setBusinessType(businessType);
			newAttach.setBusinessExtra(businessExtra);
			newAttach.setOtherKey(otherKey);
			newAttach.setOtherKeyType(otherKeyType);

			newAttach.setTypeEntity(null);
			newAttach.setCreateTime(new Date());
			newAttach.setCreateUserId(ClientUtil.getUserId());
			newAttach.setCreateUserName(ClientUtil.getName());
			newAttach.setUpdateTime(new Date());
			newAttach.setUpdateUserId(ClientUtil.getUserId());
			newAttach.setUpdateUserName(ClientUtil.getName());
			save(newAttach);
			AttachJsonModel vo = convertAttach(newAttach);
			voList.add(vo);
		}
		return voList;
	}
	
	@Override
	public void doPublishToDisk(String typeId, String otherKey, String otherKeyType) throws BusinessException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("otherKey", otherKey);
		params.put("otherKeyType", otherKeyType);
		List<AttachEntity> list = queryAttachs(params);

		for (AttachEntity attach : list) {
			AttachEntity newAttach = new AttachEntity();
			BeanUtils.copyNotNullProperties(newAttach, attach);
			newAttach.setBusinessKey(null);
			newAttach.setBusinessType(null);
			newAttach.setBusinessExtra(null);
			newAttach.setOtherKey(null);
			newAttach.setOtherKeyType(null);

			newAttach.setTypeEntity(new TypeEntity(typeId));
			newAttach.setCreateTime(new Date());
			newAttach.setCreateUserId(ClientUtil.getUserId());
			newAttach.setCreateUserName(ClientUtil.getName());
			newAttach.setUpdateTime(new Date());
			newAttach.setUpdateUserId(ClientUtil.getUserId());
			newAttach.setUpdateUserName(ClientUtil.getName());

			String newName = getUnrepeatName(typeId, newAttach.getAttachName());
			newAttach.setAttachName(newName);
			newAttach.setOnlyName(FileUtils.getFilePrefix2(newName));
			save(newAttach);
		}
	}
	
	@Override
	public void updateBelongType(String finalValue, String targetTypeId) throws BusinessException {
		List<Map> finalValueList = JSONHelper.toList(finalValue, Map.class);
		String attachIds = "";
		String typeIds = "";
		for (Map item : finalValueList) {
			String id = item.get("id").toString();
			String fileFlag = item.get("fileFlag").toString();
//			if ("0".equals(fileFlag)) {
//				typeIds += id + ",";
//			} else if ("1".equals(fileFlag)) {
//				attachIds += id + ",";
//			}
			
//		if (StringUtil.isNotEmpty(typeIds)) {
//			String sql2 = "UPDATE t_sys_type SET parentId=:parentId WHERE id in (:typeIds)";
//			executeSql(sql2, MapKit.create("parentId", targetTypeId).put("typeIds", StringUtil.parseString2ListByPattern(typeIds)).getMap());
//		}
			
			if ("0".equals(fileFlag)) {
				TypeEntity typeEntity = typeService.getType(id);
				String newName = typeService.getUnrepeatName(targetTypeId, typeEntity.getName());
				if (!newName.equals(typeEntity.getName())) {
					typeService.updateTypeName(newName, id);
				}
				String sql1 = "UPDATE t_sys_type SET parentId=? WHERE id = ?";
				executeSql(sql1, targetTypeId, id);
			} else if ("1".equals(fileFlag)) {
				AttachEntity attachEntity = get(id);
				String newName = getUnrepeatName(targetTypeId, attachEntity.getAttachName());
				if (!newName.equals(attachEntity.getAttachName())) {
					updateAttachName(newName, id);
				}
				String sql1 = "UPDATE t_sys_attachment SET typeEntity=? WHERE id = ?";
				executeSql(sql1, targetTypeId, id);
			}
		}

	}
	
	@Override
	public void deleteMul(String finalValue) throws BusinessException {
		List<Map> finalValueList = JSONHelper.toList(finalValue, Map.class);
		String attachIds = "";
		String typeIds = "";
		for (Map item : finalValueList) {
			String id = item.get("id").toString();
			String fileFlag = item.get("fileFlag").toString();

			if ("0".equals(fileFlag)) {
				typeIds += id + ",";
			} else if ("1".equals(fileFlag)) {
				attachIds += id + ",";
			}
		}

		if (StringUtil.isNotEmpty(typeIds)) {
			typeService.deleteByIds(typeIds);
		}
		
		if (StringUtil.isNotEmpty(attachIds)) {
			deleteByIds(attachIds);
		}
	}
	
	@Override
	public AjaxJson doSaveChatFileToDisk(String remoteUrl, String typeId, String fileName) throws Exception {
		AjaxJson j = new AjaxJson();
		String afterDocBasePath = getAfterDocBaseDir();
		String datePath = getDatePath();
		String funPath = "chattemp";
		String typePath = ClientUtil.getUserId();
		String uploadBasePath = getUploadBasePath();
		String relativeUploadPath = afterDocBasePath + File.separator + funPath + File.separator + typePath + File.separator + datePath + File.separator;
		String absoluteUploadPath = uploadBasePath + File.separator + relativeUploadPath;
		String absoluteFilePath = absoluteUploadPath + File.separator + fileName;

		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Accept", "application/octet-stream"));
		File file = HttpUtils.downLoadFile(new URL(remoteUrl), headers, absoluteFilePath);

		if (file.exists()) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("isNeedToType", true);
			params.put("typeId", typeId);
			params.put("originalFilename", fileName);
			params.put("size", file.length());
			params.put("contentType", Files.probeContentType(Paths.get(absoluteFilePath)));
			params.put("in", new FileInputStream(file));

			j = doUploadFiles(params);
			file.delete();
			if (j.isSuccess()) {
				j.setMsg("保存成功");
			}
			return j;
		}
		return null;
	}

	@Override
	public AjaxJson doUploadFiles(Map<String, Object> params) {
		AjaxJson j = new AjaxJson();
		LinkedList<AttachJsonModel> files = new LinkedList<AttachJsonModel>();
		AttachJsonModel filesJson = new AttachJsonModel();
		String originalFilename = BeanUtils.emptyString(params.get("originalFilename"));
		String srcName = BeanUtils.emptyString(params.get("originalFilename"));
		try {

			Boolean isNeedToType = Boolean.parseBoolean(BeanUtils.isEmpty(params.get("isNeedToType"), "false").toString());
			Boolean autoCreateType = Boolean.parseBoolean(BeanUtils.isEmpty(params.get("autoCreateType"), "false").toString());
			Boolean isShowType = Boolean.parseBoolean(BeanUtils.isEmpty(params.get("isShowType"), "true").toString());
			String typeId = BeanUtils.emptyString(params.get("typeId"));
			String defaultTypeId = BeanUtils.emptyString(params.get("defaultTypeId"));
			String defaultTypeCode = BeanUtils.emptyString(params.get("defaultTypeCode"));
			String parentTypeCode = BeanUtils.emptyString(params.get("parentTypeCode"));
			String businessKey = BeanUtils.emptyString(params.get("businessKey"));
			String businessType = BeanUtils.emptyString(params.get("businessType"));
			String businessExtra = BeanUtils.emptyString(params.get("businessExtra"));
			String otherKey = BeanUtils.emptyString(params.get("otherKey"));
			String otherKeyType = BeanUtils.emptyString(params.get("otherKeyType"));
			InputStream in = (InputStream) params.get("in");

			InputStreamCache inputStreamCache=new InputStreamCache(in);
			String contentType = BeanUtils.emptyString(params.get("contentType"));
			Long size = (Long) BeanUtils.isEmpty(params.get("size"), 0L);

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

			Map<String, Object> param = new HashMap<String, Object>();

			/* 1.上传时数据参数预处理 */
			// TODO 先判断请求中是否包含前台解析过的MD5
			String md5 = MD5Util.getMD5(inputStreamCache.getInputStream()); // 获得md5码判断是否已存在该物理文件
			Boolean isMD5Exsits = isMD5FileExsits(md5, false, "1");
			String ext = null;
			String onlyName = ""; // 纯文件名(不带后缀名)
			
			TypeEntity typeEntity = null;
			if (StringUtil.isNotEmpty(typeId) && isNeedToType) {
				typeEntity = typeService.getType(typeId);
				// 若归属到文件夹,则需要加上重名自动改名逻辑
				originalFilename = getUnrepeatName(typeId, originalFilename);
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
				afterDocBasePath = getAfterDocBaseDir();
				datePath = getDatePath();
				funPath = getFunPath(typeId);
				if (isNeedToType) {
					typePath = getTypePath(typeId);
				} else {
					typePath = ClientUtil.getUserId();
				}

				uploadBasePath = getUploadBasePath();
				thumbnailDirPath = getThumbnailDirName();
				relativeUploadPath = afterDocBasePath + File.separator + funPath + File.separator + typePath + File.separator + datePath + File.separator;
				absoluteUploadPath = uploadBasePath + File.separator + relativeUploadPath;
				thumbnailRePath = afterDocBasePath + File.separator + thumbnailDirPath + File.separator + typePath + File.separator + datePath + File.separator;
				thumbnailAbPath = uploadBasePath + File.separator + thumbnailRePath;

				absoluteFilePath = absoluteUploadPath + realUUIDFileName;
				relativeFilePath = relativeUploadPath + realUUIDFileName;
				thumbnailAbFilePath = thumbnailAbPath + realUUIDFileName;
				thumbnailReFilePath = thumbnailRePath + realUUIDFileName;

				getPathFlag = true;
			}

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
			// 判断相同MD5码文件是否存在,存在就不进行重复存储,不存在才进入存储逻辑
			if (!isMD5Exsits) {
				param.put("in", inputStreamCache.getInputStream());
				param.put("realUUIDFileName", realUUIDFileName);
				param.put("attachType", attachType);
				param.put("absoluteUploadPath", absoluteUploadPath);
				param.put("thumbnailAbPath", thumbnailAbPath);
				storeLocalFile(param);
			}

			/* 3.保存AttachEntity数据 */
			newAttachEntity.setStorageType("0");
			// 如果相同MD5码物理文件存在,非首次上传,并且用相同MD5码的附件的各类路径
			if (isMD5Exsits) {
				newAttachEntity.setIsFirstUpload("N");
				List<AttachEntity> attachEntityList = queryAttachEntityByMD5(md5);
				newAttachEntity.setRelativePath(attachEntityList.get(0).getRelativePath());
				newAttachEntity.setAbsolutePath(attachEntityList.get(0).getAbsolutePath());
				newAttachEntity.setThumbnailAbPath(attachEntityList.get(0).getThumbnailAbPath());
				newAttachEntity.setThumbnailRePath(attachEntityList.get(0).getThumbnailRePath());
			} else {
				newAttachEntity.setIsFirstUpload("Y");
				newAttachEntity.setRelativePath(relativeFilePath.replaceAll("\\\\", "/"));
				newAttachEntity.setAbsolutePath(absoluteFilePath.replaceAll("\\\\", "/"));
				if ("img".equals(attachType) && isNeedThumbnail()) {
					newAttachEntity.setThumbnailAbPath(thumbnailAbFilePath.replaceAll("\\\\", "/"));
					newAttachEntity.setThumbnailRePath(thumbnailReFilePath.replaceAll("\\\\", "/"));
				}
			}
			newAttachEntity.setTypeEntity(typeEntity);
			newAttachEntity.setBusinessKey(businessKey);
			newAttachEntity.setBusinessType(businessType);
			newAttachEntity.setBusinessExtra(businessExtra);
			newAttachEntity.setOtherKey(otherKey);
			newAttachEntity.setOtherKeyType(otherKeyType);
			save(newAttachEntity);

			/* 5.构造返回数据(用于jquery file upload组件显示使用) */
			filesJson = convertAttach(newAttachEntity);
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
	
	@Override
	public Long querySameNameAttach(String typeId, String name) {
		String sql = "select COUNT(*) from t_sys_attachment where typeEntity='" + typeId + "'  AND attachName = '" + name + "'";
		return attachDao.getCountForJdbc(sql);
	}
	
	@Override
	public Long querySerialNameAttach(String typeId, String name) {
		String ext = FileUtils.getExtendWithDot(name);
		String onlyName = StringUtil.replaceSQLRegexp(FileUtils.getFilePrefix2(name));
		String sql = "select COUNT(*) from t_sys_attachment where typeEntity='" + typeId + "'  AND onlyName REGEXP '^" + onlyName + "(\\\\([0-9]+\\\\))?$'";
		if (StringUtil.isNotEmpty(ext)) {
			sql += "AND ext='" + ext + "'";
		}
		return attachDao.getCountForJdbc(sql);
	}
	
	@Override
	public String getUnrepeatName(String typeId, String name) {
		Long count1 = querySameNameAttach(typeId, name);
		if (count1 > 0) {
			Long count2 = querySerialNameAttach(typeId, name);
			String ext = FileUtils.getExtendWithDot(name);
			String onlyName = FileUtils.getFilePrefix2(name);
			return new StringBuffer(onlyName).append("(").append(count2).append(")").append(ext).toString();
		}
		return name;
	}
	
	@Override
	public List<AttachEntity> queryAttachs(Map<String, Object> params){
		CriteriaQuery cq = new CriteriaQuery(AttachEntity.class);
		if (BeanUtils.isNotEmpty(params.get("businessKey"))) {
			cq.eq("businessKey", params.get("businessKey").toString());
		}
		if (BeanUtils.isNotEmpty(params.get("businessType"))) {
			cq.eq("businessType", params.get("businessType").toString());
		}
		if (BeanUtils.isNotEmpty(params.get("businessExtra"))) {
			cq.eq("businessExtra", params.get("businessExtra").toString());
		}
		if (BeanUtils.isNotEmpty(params.get("otherKey"))) {
			cq.eq("otherKey", params.get("otherKey").toString());
		}
		if (BeanUtils.isNotEmpty(params.get("otherKeyType"))) {
			cq.eq("otherKeyType", params.get("otherKeyType").toString());
		}
		cq.add();
		PageList pageList = getPageList(cq, false);
		return pageList.getResultList();
	}
}
