package com.xplatform.base.framework.mybatis.engine.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EntitytTypeEnum {
	/** 主实体EPS_ENTITY表 */
	MAIN,
	/** 类型表 */
	TYPE,
	/** 扩展表 */
    EXT,
	/** 频道表 */
	PAGE,
	/** 状态表*/
	STATE,
	/** 关系表 */
	RELATION;
	private static final String EPS_ENTITY = "EPS_ENTITY";
	private static final String EPS_ENTITY_PAGE = "EPS_ENTITY_PAGE";
	private static final String EPS_ENTITY_STATE = "EPS_ENTITY_STATE";
	private static final String EPS_ENTITY_RELATION = "EPS_ENTITY_RELATION";
	public static List<String> entitytTypeList = new ArrayList<String>();
	public  static Map<String,String> entitytTypeMap = new HashMap<String,String>();
	static
	{
	 init();
	}
	private static void init()
	{
		entitytTypeList.add(MAIN.toString());
		entitytTypeList.add(TYPE.toString());
		entitytTypeList.add(EXT.toString());
		entitytTypeList.add(PAGE.toString());
		entitytTypeList.add(STATE.toString());
		entitytTypeList.add(RELATION.toString());
		
		entitytTypeMap.put(MAIN.toString(), EPS_ENTITY);
		entitytTypeMap.put(TYPE.toString(), "");
		entitytTypeMap.put(EXT.toString(), "");
		entitytTypeMap.put(PAGE.toString(), EPS_ENTITY_PAGE);
		entitytTypeMap.put(STATE.toString(), EPS_ENTITY_STATE);
		entitytTypeMap.put(RELATION.toString(), EPS_ENTITY_RELATION);
		
	}
	 public static List<String> getEntitytTypeList() {
		return entitytTypeList;
	}
	 public static Map<String,String> getEntitytTypeMap(){
		 return entitytTypeMap;
	 }
	 
	 
	 public static void main(String[] args) {
		System.out.println("entitytTypeMap:" + EntitytTypeEnum.entitytTypeMap);
	}
}
