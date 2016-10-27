package com.xplatform.base.system.statistics.stat.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.xplatform.base.system.type.mybatis.vo.TypeTreeVo;

public interface StatisticsMybatisDao {

	List<Map<String,Object>> queryDataList(Map<String, Object> param);

	List<Map<String, Object>> queryStatisticsList(Map<String, Object> params);
}
