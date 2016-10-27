package com.xplatform.base.workflow.instance.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.workflow.instance.mybatis.vo.InstanceVo;
import com.xplatform.base.workflow.node.mybatis.vo.NodeUserVo;
import com.xplatform.base.workflow.task.mybatis.vo.ProcessTask;

public interface InstanceMybatisDao {
	
	/**
	 * 我的请求列表
	 * @author xiehs
	 * @createtime 2014年10月8日 下午9:32:49
	 * @Decription
	 *
	 * @param page
	 * @return
	 */
	public List<InstanceVo> getRequstInstanceListByPage(Page<InstanceVo> page);
	
	/**
	 * 我的办结列表
	 * @author xiehs
	 * @createtime 2014年10月8日 下午9:33:04
	 * @Decription
	 *
	 * @param page
	 * @return
	 */
	public List<InstanceVo> getCompleteInstanceListByPage(Page<InstanceVo> page);
	
}
