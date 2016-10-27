package com.xplatform.base.framework.mybatis.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.generator.IdGenerator;
import com.xplatform.base.framework.mybatis.service.IdEntityService;

/**
 * 
 * description :主键生成策略
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月17日 下午1:38:37
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月17日 下午1:38:37
 *
 */
public class Identities {
	/**
     * 主键生成类型枚举
     */
	enum IdType {
		//根据数据库生成
		HILO,
		//UUID生成
		UUID,
		//数据库序列生成
		SEQUENCE;
	}
	/**
	 * 私有构造器，工具类无需实例化。
	 */
	private Identities(){
	}
	/**
	 * 封装JDK自带的UUID, 通过Random数字生成,中间有-分割
	 * 
	 * @return UUID
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();
	}

    /**
     * 通过数据库生成主键
     * <pre>
     * </pre>
     * @return
     */
	public static String dbid(){
		
		IdGenerator idGenerator = ApplicationContextUtil.getBean(IdGenerator.class);
		
		return idGenerator.getNextId();
	}
	/**
	 * 通过数据库序列生成主键
	 * <pre>
	 * </pre>
	 * @return
	 */
	public static String sequence(){
		
		
		
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("dbconfig.properties");
		Properties props =null;
		String dbType  ="oracle";
		try {
			props = PropertiesLoaderUtils.loadProperties(resource);
			if(StringUtil.isNotEmpty(props.getProperty("jdbc.dbType"))){
				dbType = props.getProperty("jdbc.dbType");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		IdEntityService idEntityService = ApplicationContextUtil.getBean("idEntityService");
		Map param=new HashMap();
		param.put("sequenceName", "eps_sequence");
		param.put("dbType", dbType);
		if("sqlserver".equals(dbType)){//因为sqlserver不支持函数更新数据，所以把更新序列的语句写到程序里了
			idEntityService.updateSequence("eps_sequence");
		}
		return idEntityService.getPrimaryKey(param);
	}
    /**
     * 根据属性文件上的配置信息启用相应的主键生成策略
     * <pre>
     * </pre>
     * @return
     */
	public static String getNextId(){
		String id =null;
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("dbconfig.properties");
		Properties props =null;
		String idType  =null;
		try {
			props = PropertiesLoaderUtils.loadProperties(resource);
			idType = props.getProperty("id_generator_type");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (StringUtil.equals(idType, IdType.UUID.toString())) {
			id = uuid();
		}else if(StringUtil.equals(idType, IdType.HILO.toString())) {
			id = dbid();
		}else if(StringUtil.equals(idType, IdType.SEQUENCE.toString())) {
			id = sequence();
		}
		return id;
	}
}
