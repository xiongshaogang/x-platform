package com.xplatform.base.workflow.core.bpm.util;

public class BpmConst{
  public static final String PRE_ORG_ID = "preOrgId";
  public static final String START_ORG_ID = "startOrgId";
  public static final String StartUser = "startUser";
  public static final String PrevUser = "prevUser";
  public static final String StartEvent = "start";
  public static final String EndEvent = "end";
  public static final String CreateEvent = "create";
  public static final String CompleteEvent = "complete";
  public static final String AssignmentEvent = "assignment";
  public static final String PROCESS_EXT_VARNAME = "outPassVars";
  public static final String PROCESS_INNER_VARNAME = "innerPassVars";
  public static final String FLOW_RUN_SUBJECT = "subject_";
  public static final String FLOW_SERIALNO = "serialNo_";
  public static final String FLOW_INFORM_TYPE = "informType";
  public static final String IS_EXTERNAL_CALL = "isExtCall";
  
  public static final String StartScript = "start";//开始脚本
  public static final String EndScript = "end";//结束脚本
  public static final String ScriptNodeScript = "script";//脚本结点
  public static final String PreScript = "pre";//前置
  public static final String RearScript = "rear";//后置
  public static final String AllotScript = "allot";//分配
  
  public static final String NODE_APPROVAL_STATUS = "approvalStatus";
  public static final String NODE_APPROVAL_CONTENT = "approvalContent";
  public static final Integer TASK_BACK = Integer.valueOf(1);

  public static final Integer TASK_BACK_TOSTART = Integer.valueOf(2);

  public static final Short OnLineForm = Short.valueOf((short)0);

  public static final Short UrlForm = Short.valueOf((short)1);
  public static final String FORM_PK_REGEX = "\\{pk\\}";
  public static final String FLOW_BUSINESSKEY = "businessKey";
  public static final String FLOW_RUNID = "flowRunId";
  public static final String SIGN_USERIDS = "signUsers";
  public static final String SUBPRO_MULTI_USERIDS = "subAssignIds";
  public static final String SUBPRO_EXT_MULTI_USERIDS = "subExtAssignIds";
  public static final String MESSAGE_TYPE_MAIL = "1";
  public static final String MESSAGE_TYPE_SMS = "2";
  public static final String MESSAGE_TYPE_INNER = "3";
  public static final String EMPTY_USER = "0";
  public static String PREVIEW_FORMUSER = "__formUserId__";

  public static String PREVIEW_FORMORG = "__formOrgId__";

  public static String PREVIEW_FORMPOS = "__formPosId__";

  public static String PREVIEW_FORMROLE = "__formRoleId__";

  public static String LOCAL_DATASOURCE = "LOCAL";
}