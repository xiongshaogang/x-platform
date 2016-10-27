package com.xplatform.base.system.attachment.service;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.system.attachment.entity.AttachEntity;
import com.xplatform.base.system.attachment.entity.FTPAttachEntity;
import com.xplatform.base.system.attachment.model.AttachJsonModel;
import com.xplatform.base.system.attachment.mybatis.vo.AttachVo;
import com.xplatform.base.system.dict.entity.DictTypeEntity;

/**
 * description : 附件Service操作类
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月2日 下午6:04:23
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年7月2日 下午6:04:23
 *
*/

public interface AttachService extends BaseService<AttachEntity> {
	/**
	 * @author xiaqiang
	 * @createtime 2014年7月11日 下午3:41:25
	 * @Decription E:/uploadFile/data/zl/dy/gndy/2014/7/11/abc.png 当中的data部分 标识大功能区分
	 *
	 * @param typeId
	 * @return
	 * @throws BusinessException
	 */
	public String getFunPath(String typeId);

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月11日 下午3:42:58
	 * @Decription E:/uploadFile/data/zl/dy/gndy/2014/7/11/abc.png 
	 * 当中的2014/7/11部分 标识年月日路径
	 *
	 * @return
	 * @throws BusinessException
	 */
	public String getDatePath() ;

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月11日 下午3:43:32
	 * @Decription E:/uploadFile/data/zl/dy/gndy/2014/7/11/abc.png 
	 * 当中的E:/uploadFile/ 标识基本上传目录
	 *
	 * @return
	 * @throws BusinessException
	 */
	public String getUploadBasePath() ;

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月11日 下午3:44:00
	 * @Decription E:/uploadFile/data/zl/dy/gndy/2014/7/11/abc.png 
	 * 当中的zl/dy/gndy 标识分类目录路径(分类目录的code)
	 * @param typeId
	 * @return
	 * @throws BusinessException
	 */
	public String getTypePath(String typeId) throws BusinessException ;

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月13日 下午10:54:25
	 * @Decription 通过MD5码查询附件记录
	 *
	 * @param MD5
	 * @return
	 * @throws BusinessException
	 */
	public List<AttachEntity> queryAttachEntityByMD5(String MD5) throws BusinessException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月13日 下午10:55:16
	 * @Decription 判断传入MD5码的附件记录是否存在,并且物理文件是否存在(双重检测)
	 *
	 * @param MD5
	 * @param validFileExists 是否验证物理文件
	 * @param storageType 存储方式(意味着验证物理文件的方式也不一样)
	 * @return
	 * @throws BusinessException
	 */
	public Boolean isMD5FileExsits(String MD5, boolean validFileExists, String storageType) throws BusinessException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月13日 下午11:37:22
	 * @Decription 通过MD5码,查询绝对路径(如果存在返回路径,否则返回Null)
	 *
	 * @param MD5
	 * @return
	 * @throws BusinessException
	 */
	public String queryAbsolutePathByMD5(String MD5) throws BusinessException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月14日 上午10:12:46
	 * @Decription 读取配置文件是否需要压缩存放缩略图
	 *
	 * @return
	 * @throws BusinessException
	 */
	public Boolean isNeedThumbnail();

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月14日 上午10:23:26
	 * @Decription 读取配置文件中存放缩略图根文件夹名
	 *
	 * @return
	 * @throws BusinessException
	 */
	public String getThumbnailDirName() throws BusinessException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月14日 上午10:28:57
	 * @Decription 读取配置文件获得压缩后的图片宽度
	 *
	 * @return
	 * @throws BusinessException
	 */
	public Integer getScaleWidth() throws BusinessException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月14日 上午10:28:57
	 * @Decription 读取配置文件获得压缩后的图片高度
	 *
	 * @return
	 * @throws BusinessException
	 */
	public Integer getScaleHeight() throws BusinessException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月14日 下午3:11:54
	 * @Decription 读取配置文件获得tomcat等中间件所配虚拟目录文件夹后的文件目录(用于构造相对路径)
	 *
	 * @return
	 * @throws BusinessException
	 */
	public String getAfterDocBaseDir() throws BusinessException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月15日 上午9:20:59
	 * @Decription 通过附件id,删除相关信息
	 *
	 * @param id
	 * @throws BusinessException
	 */
	public void deleteFile(String aid) throws BusinessException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月15日 下午4:38:36
	 * @Decription 通过附件id集合删除相关信息
	 *
	 * @param aids
	 * @throws BusinessException
	 */
	public void deleteMulFile(String aids) throws BusinessException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月1日 下午5:14:04
	 * @Decription 通过md5码查询FTP附件表(只取1条记录)
	 *
	 * @param md5
	 * @return
	 * @throws BusinessException
	 */
	public FTPAttachEntity queryFTPAttachEntityByMD5(String md5) throws BusinessException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月1日 下午6:12:32
	 * @Decription 从配置文件中读取资料管理默认存储方式
	 *
	 * @return
	 * @throws BusinessException
	 */
	public String getStorageType() ;

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月2日 下午3:30:40
	 * @Decription 通过附件Id查找对应的FTP附件记录
	 *
	 * @param attachId
	 * @return
	 * @throws BusinessException
	 */
	public FTPAttachEntity queryFTPAttachEntityByAttach(String attachId) ;

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月17日 下午2:38:55
	 * @Decription 从配置文件中读取通用上传组件配置[能否修改上目录]
	 *
	 * @return
	 * @throws BusinessException
	 */
	public Boolean getIsChangeType();

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月17日 下午2:40:07
	 * @Decription 从配置文件中读取通用上传组件配置[默认根目录code]
	 *
	 * @return
	 * @throws BusinessException
	 */
	public String getRootTreeCode();

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月17日 下午2:40:35
	 * @Decription 从配置文件中读取通用上传组件配置[查看的目录树是否经过权限过滤]
	 *
	 * @return
	 * @throws BusinessException
	 */
	public Boolean getOnlyAuthority();

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月17日 下午2:41:04
	 * @Decription 从配置文件中读取通用上传组件配置[加载的目录树是否包含rootTreeCode这个自身节点]
	 *
	 * @return
	 * @throws BusinessException
	 */
	public Boolean getContainSelf();

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月23日 下午1:21:05
	 * @Decription 保存本地文件方法
	 *
	 * @param param 接收Map类型,包含属性有:
	 *  String realUUIDFileName 随机产生的无重复文件名
		MultipartFile mpf 提交上来的文件对象
		String attachType 附件类型
		String absoluteUploadPath 原附件绝对路径文件夹(不含文件名)
		String thumbnailAbPath 缩略图绝对路径文件夹(不含文件名)
	 * @throws Exception
	 */
	public void storeLocalFile(Map<String, Object> param) throws Exception;

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月23日 下午1:21:17
	 * @Decription 保存FTP文件方法
	 *
	 * @param param 接收Map类型,包含属性有:
	 *  String realUUIDFileName 随机产生的无重复文件名
		MultipartFile mpf 提交上来的文件对象
		String attachType 附件类型
		String remotePath 原附件上传的远程FTP文件夹(不含文件名)
		String remoteThumbnailPath 缩略图上传的远程FTP文件夹(不含文件名)
	 * @throws Exception
	 */
	public void storeFTPFile(Map<String, Object> param) throws Exception;

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月1日 下午6:12:32
	 * @Decription 从配置文件中读取个人头像默认高度
	 *
	 * @return
	 * @throws BusinessException
	 */
	public Integer getPortraitPlainHeight() throws BusinessException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月1日 下午6:12:32
	 * @Decription 从配置文件中读取个人头像默认宽度
	 *
	 * @return
	 * @throws BusinessException
	 */
	public Integer getPortraitPlainWidth() throws BusinessException;

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月23日 下午1:21:05
	 * @Decription 保存本地文件方法
	 *
	 * @param param 接收Map类型,包含属性有:
	    String realUUIDFileName 随机产生的无重复文件名
		MultipartFile mpf 提交上来的文件对象
		String attachType 附件类型
		String remotePath 原附件上传的远程FTP文件夹(不含文件名)
		String remoteThumbnailPath 缩略图上传的远程FTP文件夹(不含文件名)
		Integer x 切割图片的x坐标
		Integer y 切割图片的y坐标
		Integer w 切割图片的宽度
		Integer h 切割图片的高度
	 * @throws Exception
	 */
	public void storeLocalPortraitFile(Map<String, Object> param) throws Exception;

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月23日 下午1:21:05
	 * @Decription 保存本地文件方法
	 *
	 * @param param 接收Map类型,包含属性有:
	    String realUUIDFileName 随机产生的无重复文件名
		MultipartFile mpf 提交上来的文件对象
		String attachType 附件类型
		String remotePath 原附件上传的远程FTP文件夹(不含文件名)
		String remoteThumbnailPath 缩略图上传的远程FTP文件夹(不含文件名)
		Integer x 切割图片的x坐标
		Integer y 切割图片的y坐标
		Integer w 切割图片的宽度
		Integer h 切割图片的高度
	 * @throws Exception
	 */
	public void storeFTPPortraitFile(Map<String, Object> param) throws Exception;
	
	
	/**
	 * 通过综合条件查询资料Vo
	 * 
	 * @author xiaqiang
	 * @createtime 2014年7月3日 下午6:07:32
	 * @Decription
	 *
	 * @param param
	 * @return
	 */

	public List<AttachVo> queryAttachVoList(Map<String, Object> param) ;

	public List<AttachVo> queryAttachVoListByAdmin(Map<String, Object> param) ;

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月10日 下午1:21:55
	 * @Decription 给有权查看的文件,添加操作权限(4种)
	 *
	 * @param param
	 * @return
	 */
	public List<AttachVo> queryAuthorityToVo(Map<String, Object> param, List<String> operationCode);

	/**
	 * @author xiaqiang
	 * @createtime 2014年7月10日 下午9:36:17
	 * @Decription 通过条件查询拥有的操作code,以List<String> 的形式返回
	 *
	 * @param param
	 * @return
	 */
	public List<String> queryAuthority(Map<String, Object> param) ;

	/**
	 * @author xiaqiang
	 * @createtime 2014年9月5日 下午12:55:31
	 * @Decription 通过综合条件查询资料附件列表
	 *
	 * @param businessKey
	 *            业务主键
	 * @param businessType
	 *            业务类型标识(可以是实体名,如AttachEntity)
	 * @param businessExtra
	 *            用于区分同一业务记录所关联附件的不同类型
	 * @param otherKey
	 *            用于本资料记录为从表产生,但是需要关联主表的情况,本字段记录主表ID(或者当做一个查询条件用于多种用途,增加附件灵活性)
	 * @param otherKeyType
	 *            otherKey的来源类型(一般取实体名比如:AttachEntity)
	 * @return
	 * @throws BusinessException
	 */
	public List<AttachVo> queryUploadAttach(Map<String, Object> param);


	/**
	 * 通过附件Ids查找资料记录集合
	 * 
	 * @param aIds
	 * @return
	 * @throws BusinessException
	 */
	public List<AttachEntity> queryAttachsByIds(String aIds) throws BusinessException;
	
	/**
	 * 查询个人类别下的文件和文件夹
	 * @param param
	 * @return
	 */
	public List<AttachVo> queryPersonalAttachs(Map<String, Object> param);
	
	/**
	 * 查询传阅型产生的文件
	 * @param userId
	 *            传阅接收人Id
	 * @param formCode
	 *            模板code
	 * @return
	 */
	public List<AttachVo> queryNotifyTypeAttachs(Map<String, Object> param);
	
	/**
	 * 查询公司栏目下第一级文件夹
	 * @param param
	 * @return
	 */
	public List<AttachVo> queryRootOrgAttachs(Map<String, Object> param);
	
	/**
	 * 查询公司栏目下后级文件夹和文件
	 * @param param
	 * @return
	 */
	public List<AttachVo> queryTypeOrgAttachs(Map<String, Object> param) ;
	
	/**
	 * 设置单附件按钮权限
	 * @param vo
	 * @param operationCode
	 * @return
	 */
	public AttachVo setButtonAuthority(AttachVo vo, List<String> operationCode);
	
	/**
	 * 通用按钮权限设置
	 * @param list
	 * @param operationCode
	 * @param currentMenu
	 * @param currentOrg
	 * @return
	 */
	public List<AttachVo> setMulTypeButtonAuthority(List<AttachVo> list,String currentMenu,String currentOrg);
	
	/**
	 * 修改文件名
	 * @param name 新文件名
	 * @param id 文件id
	 */
	public void updateAttachName(String name, String id) throws BusinessException;
	
	/**
	 * 从云盘选取逻辑,复制一套附件
	 * @param attachIds
	 * @param businessKey
	 * @param businessType
	 * @param businessExtra
	 * @param otherKey
	 * @param otherKeyType
	 * @throws BusinessException
	 */
	public List<AttachJsonModel> copyCloudAttach(String attachIds, String businessKey, String businessType, String businessExtra, String otherKey, String otherKeyType)
			throws BusinessException ;

	/**
	 * 将附件转换为返回前端的VO
	 * @param newAttachEntity
	 * @return
	 */
	public AttachJsonModel convertAttach(AttachEntity newAttachEntity);
	
	/**
	 * 更新归属文件夹
	 * @param finalValue
	 * @param targetTypeId
	 */
	public void updateBelongType(String finalValue, String targetTypeId) throws BusinessException;
	
	/**
	 * 删除文件夹和文件综合类型
	 * @param finalValue
	 * @throws BusinessException
	 */
	public void deleteMul(String finalValue) throws BusinessException;
	
	/**
	 * 发布到云盘(复制整套表单记录)
	 * @param typeId
	 * @param otherKey
	 * @param otherKeyType
	 * @throws BusinessException
	 */
	public void doPublishToDisk(String typeId, String otherKey, String otherKeyType) throws BusinessException;

	/**
	 * 将环信文件保存到云盘
	 * @param remoteUrl
	 * @param typeId
	 * @param fileName
	 */
	public AjaxJson doSaveChatFileToDisk(String remoteUrl, String typeId, String fileName) throws Exception;
	
	/**
	 * 通用上传方法
	 * @param params
	 * @return
	 */
	public AjaxJson doUploadFiles(Map<String, Object> params);
	
	/**
	 * 查询同名附件,并返回查询到的个数
	 * @param typeId
	 * @param name
	 * @return
	 */
	public Long querySameNameAttach(String typeId, String name);
	
	/**
	 * 查询同系列名的附件(后面带(1)这样的数字的),并返回查询到的个数
	 * @param typeId
	 * @param name
	 * @return
	 */
	public Long querySerialNameAttach(String typeId, String name) ;
	
	/**
	 * 获得重名处理后的文件名
	 * @param typeId
	 * @param name
	 * @return
	 */
	public String getUnrepeatName(String typeId, String name);
	
	/**
	 * 查询附件实体结果集(通过businessKey等)
	 * @param params
	 * @return
	 */
	public List<AttachEntity> queryAttachs(Map<String, Object> params);
}
