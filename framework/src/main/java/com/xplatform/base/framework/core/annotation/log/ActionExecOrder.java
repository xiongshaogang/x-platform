package com.xplatform.base.framework.core.annotation.log;

public enum ActionExecOrder{
  BEFORE("before"), 
  AFTER("after"), 
  EXCEPTION("exception");

  public String name;

  private ActionExecOrder(String order) { 
	  this.name = order;
  }

  public String toString(){
    return this.name;
  }
}
