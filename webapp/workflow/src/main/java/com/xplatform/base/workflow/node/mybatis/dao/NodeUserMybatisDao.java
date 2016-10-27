package com.xplatform.base.workflow.node.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.xplatform.base.workflow.node.mybatis.vo.NodeUserVo;

public interface NodeUserMybatisDao {
	
	public List<NodeUserVo> queryNodeUserListByDefIdAndType(Map<String,String> map);
	
	public List<NodeUserVo> queryNodeUserListByCondition(Map<String,String> map);
}
