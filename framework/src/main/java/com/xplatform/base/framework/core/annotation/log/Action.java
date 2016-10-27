package com.xplatform.base.framework.core.annotation.log;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Resource;

@Target({ java.lang.annotation.ElementType.METHOD,java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Action {
	public abstract String description(); //日志描述

	public abstract String moduleCode();// 模块名称code

	// public abstract String exectype();

	public abstract String detail(); //详细信息

	public abstract ActionExecOrder execOrder(); //执行的时机
}
