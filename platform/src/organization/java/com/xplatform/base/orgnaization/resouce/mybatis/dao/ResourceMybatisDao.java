package com.xplatform.base.orgnaization.resouce.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.xplatform.base.orgnaization.resouce.mybatis.vo.ResourceVo;

public interface ResourceMybatisDao {
	public List<ResourceVo> queryResourceAuthority(Map<String,Object> param);
}
