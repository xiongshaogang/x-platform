package com.xplatform.base.framework.core.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * description : Timestamp 处理器 (JsonObject的日期问题)
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年11月27日 下午6:38:15
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年11月27日 下午6:38:15
 *
*/
	
public class DateJsonValueProcessor implements JsonValueProcessor{  
     /** 
     * 字母 日期或时间元素 表示 示例 
     * G Era 标志符 Text AD 
     * y 年 Year 1996; 96 
     * M 年中的月份 Month July; Jul; 07 
     * w 年中的周数 Number 27 
     * W 月份中的周数 Number 2 
     * D 年中的天数 Number 189 
     * d 月份中的天数 Number 10 
     * F 月份中的星期 Number 2 
     * E 星期中的天数 Text Tuesday; Tue
     * a Am/pm 标记 Text PM 
     * H 一天中的小时数（0-23） Number 0 
     * k 一天中的小时数（1-24） Number 24
     * K am/pm 中的小时数（0-11） Number 0 
     * h am/pm 中的小时数（1-12） Number 12 
     * m 小时中的分钟数 Number 30 
     * s 分钟中的秒数 Number 55 
     * S 毫秒数 Number 978 
     * z 时区 General time zone Pacific Standard Time; PST; GMT-08:00 
     * Z 时区 RFC 822 time zone -0800 
     */  
    public static final String Default_DATE_PATTERN = "yyyy-MM-dd";  
    private DateFormat dateFormat;  
  
    public DateJsonValueProcessor(String datePattern) {  
        try {  
            dateFormat = new SimpleDateFormat(datePattern);  
        } catch (Exception e) {  
            dateFormat = new SimpleDateFormat(Default_DATE_PATTERN);   
        }  
    }  
  
    @Override  
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {  
        return process(value);  
    }  
  
    @Override  
    public Object processObjectValue(String key, Object value,JsonConfig jsonConfig) {  
        return process(value);  
    }  
  
    private Object process(Object value) {  
        if (value == null) {  
            return "";  
        } else {  
            return dateFormat.format(value);  
        }  
    }  
  
} 