package com.xplatform.base.framework.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.xplatform.base.framework.core.util.StringUtil;

/**
 * Copyright (c) 2014
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */

/**
 * description :
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年8月27日 下午5:11:36
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年8月27日 下午5:11:36 
 *
*/
public class FTPUtil {
	private static final Logger logger = Logger.getLogger(FTPUtil.class);
	private String ip = "113.108.215.43"; // IP地址
	private int port = -1; // 端口
	private String username = "upload"; // 用户名
	private String password = "ucg"; // 密码
	private FTPClient ftpClient = new FTPClient(); // FTP客户端

	public FTPUtil(String ip, int port, String username, String password) {
		this.ip = ip;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	public FTPUtil() {
		PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
		ip = p.readProperty("ftp_ip");
		port = new Integer(p.readProperty("ftp_port"));
		username = p.readProperty("ftp_username");
		password = p.readProperty("ftp_password");
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月27日 下午10:31:32
	 * @Decription FTP开启连接
	 *
	 * @return 返回是否连接成功
	 */
	public boolean connectServer() {
		try {
			if (this.port != -1) {
				ftpClient.connect(ip, port);
			} else {
				ftpClient.connect(ip);
			}
			//设置编码
			ftpClient.setControlEncoding("UTF-8");
			//设置被动传输模式
			ftpClient.enterLocalPassiveMode();
			// 设置以二进制方式传输
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			//设置文件流传输模式
			ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
			if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				logger.info("成功登陆服务器!");
				if (ftpClient.login(username, password))
					return true;
			} else {
				logger.warn("连接FTP服务器[" + ip + "]时出现错误,返回replyCode:" + ftpClient.getReplyCode());
			}
		} catch (SocketException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return false;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月27日 下午10:33:01
	 * @Decription 关闭连接
	 *
	 */
	public void closeConnect() {
		try {
			if (null != this.ftpClient && ftpClient.isConnected()) {
				// 登出FTP服务器  
				boolean result = this.ftpClient.logout();
				if (result) {
					logger.info("成功登出服务器!");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("登出FTP异常!" + e.getMessage());
		} finally {
			try {
				// 关闭FTP服务器的连接  
				this.ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				logger.warn("关闭FTP服务器的连接异常！");
			}
		}
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月27日 下午11:54:13
	 * @Decription 创建目录,请用"/"分割,支持多级路径
	 *
	 * @param path 例如"/电影/动作电影/前十动作电影"
	 * @param changeToDir 是否指向刚创建的目录
	 * @return
	 */
	public boolean createDir(String path, boolean changeToDir) {
		logger.info("开始创建目录[" + path + "]");
		String[] dirArray = path.split("/");
		try {
			for (String dir : dirArray) {
				if (!ftpClient.changeWorkingDirectory(dir)) {
					if (StringUtil.isEmpty(dir)) {
						continue;
					}
					dir = new String(dir.getBytes("GBK"), "ISO-8859-1");
					if (ftpClient.makeDirectory(dir)) {
						logger.info("目录[" + dir + "]创建成功");
						ftpClient.changeWorkingDirectory(dir);
					}
				}
			}
			if (!changeToDir) {
				ftpClient.changeWorkingDirectory("/");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.warn("目录[" + path + "]创建失败");
		}
		return true;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月27日 下午11:42:09
	 * @Decription
	 *
	 * @param inputStream 输入流
	 * @param remoteFileName 存储在服务器的文件
	 * @param remoteUploadPath 存储在服务器的目录路径(不存在需要创建)
	 * @return
	 */
	public boolean uploadFile(InputStream inputStream, String remoteFileName, String remoteUploadPath) {
		boolean result = false;
		try {
			if (!isExistsFile(remoteUploadPath)) {
				createDir(remoteUploadPath, false);
			}
			ftpClient.changeWorkingDirectory(remoteUploadPath);
			logger.info("文件[" + remoteFileName + "]开始上传...");
			ftpClient.storeFile(remoteFileName, inputStream);
			result = true;
			logger.info("文件[" + remoteFileName + "]上传成功");
			ftpClient.changeWorkingDirectory("/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.warn("文件[" + remoteFileName + "]上传过程中发生错误,错误代码:" + e.getMessage());
		} finally {

			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.warn("输入流关闭失败");
			}
		}
		return result;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月28日 上午11:41:21
	 * @Decription 下载文件到输入流
	 *
	 * @param remotePath 下载路径
	 * @return
	 */
	public InputStream downloadFile(String remotePath) {
		InputStream in = null;
		try {
			if (isExistsFile(remotePath)) {
				logger.info("文件[" + remotePath + "]下载中...");
				in = ftpClient.retrieveFileStream(remotePath);
				logger.info("文件[" + remotePath + "]下载成功");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.warn("文件[" + remotePath + "]下载出错");
		}
		return in;
	}

	public void uploadDirectory(String localDir, String remotePath) {
		logger.info("文件夹[" + localDir + "]上传中...");
		try {
			File src = new File(localDir);
			remotePath = remotePath + src.getName() + "/";
			createDir(remotePath, false);
			File[] allFile = src.listFiles();
			for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
				if (!allFile[currentFile].isDirectory()) {
					File f = allFile[currentFile];
					InputStream in = new FileInputStream(f);
					uploadFile(in, f.getName(), remotePath);
				}
			}
			for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
				if (allFile[currentFile].isDirectory()) {
					// 递归  
					uploadDirectory(allFile[currentFile].getPath(), remotePath);
				}
			}
		} catch (Exception e) {
			logger.warn("文件夹[" + localDir + "]上传出错...");
			e.printStackTrace();
		}
		logger.info("文件夹[" + localDir + "]完成上传...");
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月28日 上午9:52:37
	 * @Decription 是否存在文件或文件夹
	 *
	 * @param remotePath 文件或文件夹路径
	 * @return
	 */
	public boolean isExistsFile(String remotePath) {
		try {
			FTPFile[] files = ftpClient.listFiles(remotePath);
			if (files.length < 1) {
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.warn("文件[" + remotePath + "]删除出错...");
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月2日 下午3:04:20
	 * @Decription 删除FTP服务器文件
	 *
	 * @param remoteFilePath 远程文件路径
	 * @return
	 */
	public boolean deleteFile(String remoteFilePath) {
		boolean flag = false;
		try {
			if (isExistsFile(remoteFilePath)) {
				flag = ftpClient.deleteFile(remoteFilePath);
				if (flag) {
					logger.info("文件[" + remoteFilePath + "]删除成功");
				}
			}
		} catch (IOException e) {
			logger.warn("文件[" + remoteFilePath + "]删除出错");
			e.printStackTrace();
		}
		return flag;
	}

	public static void main(String[] args) {

		//		FTPClient ftpClient = new FTPClient();
		//		FileInputStream fis = null;
		//
		//		try {
		//			ftpClient.connect("113.108.215.43");
		//			ftpClient.login("upload", "ucg");
		//
		//			File srcFile = new File("D:\\jobEdit.jsp");
		//			fis = new FileInputStream(srcFile);
		//			//设置上传目录 
		//			ftpClient.changeWorkingDirectory("/123");
		//			ftpClient.setBufferSize(1024);
		//			ftpClient.setControlEncoding("UTF-8");
		//			//设置文件类型（二进制） 
		//			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		//			ftpClient.storeFile("3.jsp", fis);
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//			throw new RuntimeException("FTP客户端出错！", e);
		//		} finally {
		//			IOUtils.closeQuietly(fis);
		//			try {
		//				ftpClient.disconnect();
		//			} catch (IOException e) {
		//				e.printStackTrace();
		//				throw new RuntimeException("关闭FTP连接发生异常！", e);
		//			}
		//		}
	}

}
