package com.xplatform.base.system.log.vo;

public enum LogType{
	
  NULL("末指定"), 
  USER_MANAGEMENT("用户管理"), 
  SYSTEM_SETTING("系统配置"), 
  PROCESS_AUXILIARY("流程辅助"), 
  WORK_CALENDAR("工作日历"), 
  FORM_MANAGEMENT("表单管理"), 
  PROCESS_MANAGEMENT("流程管理"), 
  PROCESS_CENTER("流程中心"), 
  DESKTOP_MANAGEMENT("桌面管理");

  private final String type;

  private LogType(String type) { 
	  this.type = type;
  }

  public String toString(){
    return this.type;
  }
}
