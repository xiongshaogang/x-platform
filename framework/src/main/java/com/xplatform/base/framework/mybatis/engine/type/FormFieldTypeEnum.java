package com.xplatform.base.framework.mybatis.engine.type;

import java.util.LinkedHashMap;
import java.util.Map;

public enum FormFieldTypeEnum {
	/** 普通文本(text) */
	FIELD_TEXT("text"),
	/** 文本域(textarea) */
	FIELD_TEXTAREA("textarea"),
	/** 插件文本(pluginText) */
	FIELD_PLUGIN_TEXT("pluginText"),
	/** 插件隐藏域(pluginHidden) */
	FIELD_PLUGIN_HIDDEN("pluginHidden"),
	/** 用户信息文本(userText) */
	FIELD_USER_TEXT("userText"),
	/** 用户信息隐藏域(userHidden) */
	FIELD_USER_HIDDEN("userHidden"),
	/** 单选树(tree) */
	FIELD_TREE("tree"),
	/** 复选树(multitree) */
	FIELD_MULTITREE("multitree"),
	/** 分类单选树(typeTree) */
	FIELD_TYPE_TREE("typeTree"),
	/** 分类复选树(typeMultitree) */
	FIELD_TYPE_MULTITREE("typeMultitree"),
	/** 运行时控件(runtime) */
	FIELD_RUNTIME("runtime"),
	/** 单选框(radio) */
	FIELD_RADIO("radio"),
	/** 复选框(checkbox) */
	FIELD_CHECKBOX("checkbox"),
	/** 单选列表框(select) */
	FIELD_SELECT("select"),
	/** 复选列表框(multiselect) */
	FIELD_MULTISELECT("multiselect"),
	/** 隐藏域(hidden) */
	FIELD_HIDDEN("hidden"),
	/** 后台类型(back) */
	FIELD_BACK("back"),
	/** 移动电话(mphone) */
	FIELD_MPHONE("mphone"),
	/** 固定电话(telephone) */
	FIELD_TELEPHONE("telephone"),
	/** 银行卡(bankcard) */
	FIELD_BANKCARD("bankcard"),
	/** 身份证(idcard) */
	FIELD_IDCARD("idcard"),
	/**电子邮件(email) */
	FIELD_EMAIL("email"),
	/** 网址(url) */
	FIELD_URL("url"),
	/** 密码框(password) */
	FIELD_PASSWORD("password"),
	/** 整数(integer) */
	FIELD_INTEGER("integer"),
	/** 小数(floatpoint) */
	FIELD_FLOATPOINT("floatpoint"),
	/** 日期(date) */
	FIELD_DATE("date"),
	/** 时间(time) */
	FIELD_TIME("time"),
	/** 大文本(clob) */
	FIELD_CLOB("clob"),
	/** 是非选择(boolean) */
	FIELD_BOOLEAN("boolean");

	
	
	private static Map<String,String> stringTypes = new  LinkedHashMap<String,String>();
	private static Map<String,String> integerTypes = new  LinkedHashMap<String,String>();
	private static Map<String,String> floatpointTypes = new  LinkedHashMap<String,String>();
	private static Map<String,String> timeTypes = new  LinkedHashMap<String,String>();
	private static Map<String,String> clobTypes = new  LinkedHashMap<String,String>();
	private static Map<String,String> booleanTypes = new  LinkedHashMap<String,String>();
	static
	{
	 init();
	}
	private static void init()
	{
		stringTypes.put("普通文本", FIELD_TEXT.getPattern());
		stringTypes.put("文本域", FIELD_TEXTAREA.getPattern());
		stringTypes.put("插件文本", FIELD_PLUGIN_TEXT.getPattern());
		stringTypes.put("插件隐藏域", FIELD_PLUGIN_HIDDEN.getPattern());
		stringTypes.put("用户信息文本", FIELD_USER_TEXT.getPattern());
		stringTypes.put("用户信息隐藏域", FIELD_USER_HIDDEN.getPattern());
		stringTypes.put("单选树", FIELD_TREE.getPattern());
		stringTypes.put("复选树", FIELD_MULTITREE.getPattern());
		stringTypes.put("分类单选树", FIELD_TYPE_TREE.getPattern());
		stringTypes.put("分类复选树", FIELD_TYPE_MULTITREE.getPattern());
		stringTypes.put("运行时控件", FIELD_RUNTIME.getPattern());
		stringTypes.put("单选框", FIELD_RADIO.getPattern());
		stringTypes.put("复选框", FIELD_CHECKBOX.getPattern());
		stringTypes.put("单选列表框", FIELD_SELECT.getPattern());
		stringTypes.put("复选列表框", FIELD_MULTISELECT.getPattern());
		stringTypes.put("隐藏域", FIELD_HIDDEN.getPattern());
		stringTypes.put("移动电话", FIELD_MPHONE.getPattern());
		stringTypes.put("固定电话", FIELD_TELEPHONE.getPattern());
		stringTypes.put("银行卡", FIELD_BANKCARD.getPattern());
		stringTypes.put("身份证", FIELD_IDCARD.getPattern());
		stringTypes.put("电子邮件", FIELD_EMAIL.getPattern());
		stringTypes.put("网址", FIELD_URL.getPattern());
		stringTypes.put("密码框", FIELD_PASSWORD.getPattern());
		stringTypes.put("电子邮件", FIELD_EMAIL.getPattern());
	
		
		integerTypes.put("整数", FIELD_INTEGER.getPattern());
		
		floatpointTypes.put("小数", FIELD_FLOATPOINT.getPattern());
		
		timeTypes.put("日期", FIELD_DATE.getPattern());
		timeTypes.put("时间", FIELD_TIME.getPattern());
		
		booleanTypes.put("是非选择", FIELD_BOOLEAN.getPattern());
		
		clobTypes.put("大文本", FIELD_CLOB.getPattern());
		clobTypes.put("文本域", FIELD_TEXTAREA.getPattern());
		
		
		
	}
	
    /**
     * 表单元素类型参数
     */
    private String pattern;
    /**
     * 设置表单元素类型
     * 
     * @param pattern 表单元素类型
     */
    private   FormFieldTypeEnum(String pattern){
        this.pattern = pattern;
    }
    /**
     * 获取字符串类型对应的表单元素类型集合
     * <pre>
     * </pre>
     * @return
     */
    public static  Map<String,String> getStringTypes(){
    	return stringTypes;
    }
    /**
     * 获取整型数据对应的表单元素类型集合
     * <pre>
     * </pre>
     * @return
     */
    public static  Map<String,String> getIntegerTypes(){
    	return integerTypes;
    }
    /**
     * 获取浮点数据对应的表单元素类型集合
     * <pre>
     * </pre>
     * @return
     */
    public static  Map<String,String> getFloatpointTypes(){
    	return floatpointTypes;
    }
    /**
     * 获取时间类型对应的表单元素类型集合
     * <pre>
     * </pre>
     * @return
     */
    public static  Map<String,String> getTimeTypes(){
    	return timeTypes;
    }
    /**
     * 获取大文本类型对应的表单元素类型集合
     * <pre>
     * </pre>
     * @return
     */
    public static  Map<String,String> getClobTypes(){
    	return clobTypes;
    }
    /**
     * 获取布尔类型对应的表单元素类型集合
     * <pre>
     * </pre>
     * @return
     */
    public static  Map<String,String> getBooleanTypes(){
    	return booleanTypes;
    }
    
    /**
     * 取得字典类型值
     * 
     * @return 表单元素类型
     */
    public String getPattern(){
        return pattern;
    }
    public static void main(String[] args) {
		System.out.println("stringTypes:" + getStringTypes());
		System.out.println("integerTypes:" + getIntegerTypes());
		System.out.println("floatpointTypes:" + getFloatpointTypes());
		System.out.println("timeTypes:" + getTimeTypes());
		System.out.println("clobTypes:" + getClobTypes());
		System.out.println("booleanTypes:" + getBooleanTypes());
	}
}
