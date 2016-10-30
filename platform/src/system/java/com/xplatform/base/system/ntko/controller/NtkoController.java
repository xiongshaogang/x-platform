package com.xplatform.base.system.ntko.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.system.ntko.entity.HtmlFileEntity;
import com.xplatform.base.system.ntko.entity.OfficeFileEntity;
import com.xplatform.base.system.ntko.service.NtkoService;

@Controller
@RequestMapping("/ntkoController")
public class NtkoController extends BaseController {
	
	private NtkoService ntkoService;

	@Resource
	public void setNtkoService(NtkoService ntkoService) {
		this.ntkoService = ntkoService;
	}

	private static final ResourceBundle bundle = ResourceBundle
			.getBundle("sysConfig");

	public FileItem officeFileItem = null;
	public FileItem attachFileItem = null;
	public String fileNameDisk = "";
	private AjaxJson result = new AjaxJson();

	@RequestMapping(params = "uploadOffice")
	@ResponseBody
	public String uploadOffice(MultipartHttpServletRequest request)
			throws UnsupportedEncodingException {
		String busId = request.getParameter("busId");
		String officeFile = request.getParameter("officeFile");
		String htmlFile = request.getParameter("htmlFile");
		String fileType = request.getParameter("fileType");
		String htmlName = request.getParameter("htmlFileName");
		MultipartFile mf =  request.getFile(officeFile);
		List<MultipartFile> html =  request.getFiles(htmlFile);
		if(mf !=null){
			fileNameDisk = busId+".officefile."+ mf.getOriginalFilename();
			OfficeFileEntity  of = ntkoService.getOfficeEntityByBusId(busId);
			if(saveFileToDisk(mf)){
				if (of == null) {
					of = new OfficeFileEntity();
					of.setBusId(busId);
				}
				of.setFileName(mf.getOriginalFilename());
				of.setFileSize(String.valueOf(mf.getSize()));
				of.setFileType(fileType);
				of.setFileNameDisk(bundle.getString("relativeOfficeFileUrl") +fileNameDisk);
				of.setAbsoluteFileDisk(bundle.getString("absoluteOfficeFileDir") + fileNameDisk);
				if(StringUtil.isNotEmpty(of.getId())){
					this.ntkoService.updateOfficeFile(of);
				}else{
					this.ntkoService.saveOfficeFile(of);
				}
			}else{
				System.out.println("保存文件出错");
			}
		}
		if(html.size() > 0){
			String  dirPath = busId+".htmlfile."+htmlName ;
			HtmlFileEntity hfe =  ntkoService.getHtmlFileEntityByBusId(busId);
			if(hfe == null){
				hfe = new HtmlFileEntity();
			}
			hfe.setBusId(busId);
			if(saveHtmlFileToDisk(html,dirPath,htmlName,hfe)){
				if(StringUtil.isNotEmpty(hfe.getId())){
					this.ntkoService.updateHtmlFile(hfe);
				}else{
					this.ntkoService.saveHtmlFile(hfe);
				}
			}
		}
		return "保存成功";
	}

	/*------------------------------------------------------------
	保存文档到服务器磁盘，返回值true，保存成功，返回值为false时，保存失败。
	--------------------------------------------------------------*/
	public boolean saveFileToDisk(MultipartFile mf) {
		boolean flag = true;
		String path ="";
		try {
			if (!fileNameDisk.equalsIgnoreCase("") && mf != null) {
				path = bundle.getString("absoluteOfficeFileDir") + fileNameDisk;
				File file = new File(path);
				if(file.exists()){
					if(file.delete()){
						FileCopyUtils.copy(mf.getInputStream(), new FileOutputStream(path));
					}
				}else{
					FileCopyUtils.copy(mf.getInputStream(), new FileOutputStream(path));
				}
				
			}
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			System.out.println("error saveFileToDisk:" + e.getMessage());
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	public boolean saveHtmlFileToDisk(List<MultipartFile> html, String newDir,String htmlName,HtmlFileEntity hfe) {
		boolean flag = true;
		MultipartFile mf = null;
		htmlName = htmlName.replace(".html", "")+".files";
		//创建用于保存html文件的目录,目录名称取为:数据库中该的id+".htmlfile."+html文件的名称
		String dirPath = bundle.getString("absoluteHtmlFileDir").trim()+ newDir;
		File htmlFileDir=new File(dirPath);
		if(htmlFileDir.exists())
		{
			deleteFolder(htmlFileDir);
		}
		if(htmlFileDir.mkdir())
		{
			File upLoadFile = null ;
			String fileName = "";
			for(int i=0;i<html.size();i++)
			{
				    mf=html.get(i);
					fileName = mf.getOriginalFilename();	
					if(fileName!=null)
					{
						fileName=fileName.substring(fileName.lastIndexOf("\\")+1);
						if(fileName.contains(".html")){
							hfe.setFileName(htmlName);
							hfe.setFileSize(String.valueOf(mf.getSize()));
							String absoluteDir = dirPath+File.separator+fileName;
							hfe.setAbsoluteFileDir(absoluteDir);
							hfe.setFileNameDisk(bundle.getString("relativeHtmlFileUrl")+newDir+File.separator+fileName);
							upLoadFile =  new File(dirPath+File.separator+fileName);
						}else{
							File file = new File(dirPath+File.separator+htmlName);
							if(!file.exists()){
								file.mkdir();
							}
							upLoadFile =  new File(dirPath+File.separator+htmlName+File.separator+fileName);
						}
						try {
							FileCopyUtils.copy(mf.getInputStream(), new FileOutputStream(upLoadFile));
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							flag = false;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							flag = false;
						}
					}
			}
		}
		return flag;
	}
	
	public boolean deleteFolder(File dir)   
	{   
		  boolean result=false;
	      File filelist[]=dir.listFiles();   
	      int listlen=filelist.length;  
	      if(dir.exists()) 
	      {
		      for(int i=0;i<listlen;i++)   
		      {   
		          if(filelist[i].isDirectory())   
		          {   
		              deleteFolder(filelist[i]);   
		          }   
		          else   
		          {   
		              filelist[i].delete();   
		          }   
		      }   
		      dir.delete();//删除当前目录 
		      result = true;
	      }
	      else
	      { result = true;}
	      return result;
	 }
	
	@RequestMapping(params = "getOfEntity")
	@ResponseBody
	public AjaxJson getOfEntity(HttpServletRequest request){
		String id = request.getParameter("id");
		OfficeFileEntity of = this.ntkoService.getOfficeEntityByBusId(id);
		if(of == null){
			result.setSuccess(false);
		}else{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("fileNameDisk", of.getFileNameDisk());
			map.put("fileName", of.getFileName());
			map.put("fileType", of.getFileType());
			map.put("busId", of.getBusId());
			result.setSuccess(true);
			result.setObj(map);
		}
		return result;
	}
}
