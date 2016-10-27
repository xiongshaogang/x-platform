package com.xplatform.base.framework.core.common.exception;

public class BusinessRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BusinessRuntimeException(String message){
		super(message);
	}
	
	public BusinessRuntimeException(Throwable cause)
	{
		super(cause);
	}
	
	public BusinessRuntimeException(String message,Throwable cause)
	{
		super(message,cause);
	}
}
