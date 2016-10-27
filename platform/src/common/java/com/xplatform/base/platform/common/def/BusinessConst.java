package com.xplatform.base.platform.common.def;

public interface BusinessConst {

	/************************ 数据字典begin ************************/
	String IETYPE_CODE_withdraw = "withdraw";
	String IETYPE_NAME_withdraw = "提现"; // 收支类型_提现
	String IETYPE_CODE_childBuy = "childBuy";
	String IETYPE_NAME_childBuy = "分享奖励"; // 收支类型_分享奖励

	String RelationCode_CODE_friend = "friend"; // 好友关系编码
	String RelationCode_NAME_friend = "好友"; // 好友关系

	String MSG_commonTitle = "您有新的消息";
	String MSG_transferBalanceFrom = "已从您的账户转出{0}邦币到手机尾号{1}的用户，已提醒对方查收。";
	String MSG_transferBalanceTo = "您手机尾号{0}的朋友转给您{1}邦币，请注意查收。";

	String DICT_TRANSACTIONTYPE = "transactionType";// 交易类型
	String DICT_TRANSPART = "transPart";// 交易双方类型
	String DICT_TRANSSTATUS = "transStatus";// 交易状态

	String sendChannel_CODE_msg = "msg";
	String sendChannel_NAME_msg = "消息";// 发送方式_消息
	String sendChannel_CODE_push = "push";
	String sendChannel_NAME_push = "推送";// 发送方式_推送
	String sendChannel_CODE_sms = "sms";
	String sendChannel_NAME_sms = "短信";// 发送方式_短信
	String sendChannel_CODE_email = "email";
	String sendChannel_NAME_email = "邮箱";// 发送方式_邮箱
	String sendChannel_CODE_IM = "IM";
	String sendChannel_NAME_IM = "即时通讯";// 发送方式_即时通讯
	String sendChannel_CODE_push_android = "push_android";
	String sendChannel_NAME_push_android = "安卓推送";// 发送方式_安卓推送
	String sendChannel_CODE_push_ios = "push_ios";
	String sendChannel_NAME_push_ios = "ios推送";// 发送方式_ios推送
	
	String OPENTYPE_CODE_text = "text";
	String OPENTYPE_NAME_text = "文本消息"; // 消息打开类型_文本消息
	String OPENTYPE_CODE_url = "url";
	String OPENTYPE_NAME_url = "打开URL"; // 消息打开类型_打开URL
	String OPENTYPE_CODE_view = "view";
	String OPENTYPE_NAME_view = "打开应用指定页面"; // 消息打开类型_打开应用指定页面

	String FUNCTYPE_CODE_notice = "notice";
	String FUNCTYPE_NAME_notice = "公告"; // 消息功能来源_公告
	String FUNCTYPE_CODE_pmessage = "pmessage";
	String FUNCTYPE_NAME_pmessage = "个人消息"; // 消息功能来源_个人消息

	String CASTTYPE_CODE_broadcast = "broadcast";
	String CASTTYPE_NAME_broadcast = "广播"; // 广播类型_广播
	String CASTTYPE_CODE_unicast = "unicast";
	String CASTTYPE_NAME_unicast = "单播"; // 广播类型_单播
	String CASTTYPE_CODE_listcast = "listcast";
	String CASTTYPE_NAME_listcast = "列播"; // 广播类型_列播
	String CASTTYPE_CODE_groupcast = "groupcast";
	String CASTTYPE_NAME_groupcast = "组播"; // 广播类型_组播
	String CASTTYPE_CODE_filecast = "filecast";
	String CASTTYPE_NAME_filecast = "文件播"; // 广播类型_文件播
	String CASTTYPE_CODE_customizedcast = "customizedcast";
	String CASTTYPE_NAME_customizedcast = "自定义播"; // 广播类型_自定义播
	
	String PUSHTYPE_CODE_umeng = "umeng";
	String PUSHTYPE_NAME_umeng = "友盟"; // 推送平台_友盟

	String FUNCTYPE_CODE_group = "group";// 发送对象_分组(点到局部)
	String FUNCTYPE_CODE_private = "private";// 发送对象_私信(点到点,点到多)
	String FUNCTYPE_CODE_global = "global";// 发送对象_公告(点到全部)

	String SendType_CODE_group = "group";// 发送表类型_分组表
	String SendType_CODE_main = "main";// 发送表类型_主表

	String SendPart_CODE_main = "main";// 发送表类型_主送
	String SendPart_CODE_bcc = "bcc";// 发送表类型_暗送
	String SendPart_CODE_cc = "cc";// 发送表类型_抄送

	String SourceType_CODE_workflow = "workflow";
	String SourceType_NAME_workflow = "工作流消息";// 系统消息业务类型_工作流消息
	String SourceType_CODE_circulate = "circulate";
	String SourceType_NAME_circulate = "传阅消息";// 系统消息业务类型_传阅消息
	String SourceType_CODE_flowNotice = "flowNotice";
	String SourceType_NAME_flowNotice = "流程通知";// 系统消息业务类型_流程通知

	// String SourceType_CODE_circulate = "circulate";
	// String SourceType_NAME_circulate = "传阅";// 系统消息业务类型_传阅
	// String SourceType_CODE_divert = "divert";
	// String SourceType_NAME_divert = "转办";// 系统消息业务类型_转办
	// String SourceType_CODE_circulate = "circulate";//
	// String SourceType_NAME_circulate = "传阅";// 系统消息业务类型_传阅
	// String SourceType_CODE_circulate = "circulate";//
	// String SourceType_NAME_circulate = "传阅";// 系统消息业务类型_传阅

	String menuType_CODE_personal = "personal";
	String menuType_NAME_personal = "个人";// 资料归属分类_个人
	String menuType_CODE_work = "work";
	String menuType_NAME_work = "工作";// 资料归属分类_工作
	String menuType_CODE_org = "org";
	String menuType_NAME_org = "公司";// 资料归属分类_公司

	String attach_downloadAuthority = "attachManager_download_other";// 权限_下载
	String attach_uploadAuthority = "attachManager_upload_other";// 权限_上传
	String attach_renameAuthority = "attachManager_rename_other";// 权限_重命名
	String attach_deleteAuthority = "attachManager_delete_other";// 权限_删除
	String attach_moveAuthority = "attachManager_move_other";// 权限_移动
	String attach_mulMoveAuthority = "attachManager_mulMove_other";// 权限_批量移动
	String attach_mulDeleteAuthority = "attachManager_mulDelete_other";// 权限_批量删除
	
	String attach_createFolderAuthority = "attachManager_createFolder_other";// 权限_创建文件夹
	String attach_folderAuthority = "attachManager_folderAuthority_other";// 权限_文件夹授权权限
	/************************ 数据字典 end ************************/

	Integer TableType_single = 1;// 单表
	Integer TableType_main = 2;// 主表
	Integer TableType_sub = 3;// 从表

	String ViewType_add = "add";// 新增页面viewType
	String ViewType_update = "update";// 编辑页面viewType
	String ViewType_detail = "detail";// 查看页面viewType
	String ViewType_nextProcess = "nextProcess";// 从待办进入的viewType(未办)
	String ViewType_viewProcess = "viewProcess";// 从待办进入的viewType(已办)

	String ButtonType_save = "save";// 保存按钮类型
	String ButtonType_nextProcess = "nextProcess";// 待办按钮类型
	String ButtonType_startFlow = "startFlow";// 流程发起按钮类型
	String ButtonType_view = "view";// 流程发起按钮类型
	String ButtonType_saveAndSend = "saveAndSend";// 发送按钮类型(选人+保存)

	String ViewFlag_datasDatagridView = "datasDatagridView";// 列表视图
	String ViewFlag_datasThumbnailView = "datasThumbnailView";// 缩略图视图
	/************************ 自定义表单物理表固定字段begin ************************/
	String primaryKey = "id";// 创建时间
	String createTime = "createTime";// 创建时间
	String createUserId = "createUserId";// 创建人Id
	String createUserName = "createUserName";// 创建人name
	String updateTime = "updateTime";// 更新时间
	String updateUserId = "updateUserId";// 更新人Id
	String updateUserName = "updateUserName";// 更新人name
	String foreignKey = "mainId";// 从表的主表外键Id对应字段名

	// 全部要化为小写
	String mapaddress = "mapaddress"; // 位置控件全名称
	String lonandlat = "lonandlat"; // 位置控件经纬度

	String mulSelect_user = "user";// 综合选择_用户
	String mulSelect_org = "org";// 综合选择_组织机构
	String mulSelect_role = "role";// 综合选择_角色
	/************************ 自定义表单物理表固定字段end ************************/
	String SysParam_user = "system";

	/** 逻辑true */
	String BOOL_TRUE = "Y";
	/** 逻辑false */
	String BOOL_FALSE = "N";
	/** 显示模式Date */
	String TYPE_DATE = "Date";
	/** 显示模式String */
	String TYPE_STRING = "String";
	/** 显示模式Integer */
	String TYPE_INTEGER = "Integer";
	/** 显示模式Double */
	String TYPE_DOUBLE = "Double";
	/** 查询操作= */
	String OP_EQ = " = ";
	/** 查询操作>= */
	String OP_RQ = " >= ";
	/** 查询操作<= */
	String OP_LQ = " <= ";
	/** 查询操作like */
	String OP_LIKE = " LIKE ";

	/** 系统字典分组表 */
	String SYS_DICGROUP = "t_s_typegroup";
	/** 系统字典表 */
	String SYS_DIC = "t_s_type";

	/** 智能表单生成的表的前缀为jform_ */
	String jform_ = "jform_";

	/** sql增强insert */
	String SQL_INSERT = "insert";
	/** sql增强update */
	String SQL_UPDATE = "update";
	/** 表单版本号 */
	String CONFIG_VERSION = "jformVersion";
	/** 手机号码正则校验 */
	String REGEX_MOBILE = "^(0|86|17951)?(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$";
}
