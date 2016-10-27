package com.xplatform.base.system.message.config.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.system.message.config.mybatis.vo.InnerMessageVo;

/**
 * description : 对应SysUser.xml里的sql
 *
 * @version 1.0
 * @author xiaqiang 
 * @createtime : 2014年10月29日 下午4:50:13
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年10月29日 下午4:50:13
 *
*/

public interface MessageMybatisDao {
	//查找登录人所接收到的站内信
	public List<InnerMessageVo> queryInnerMessageByUser(Map<String, Object> param);
	//查找登录人所接收到的站内信
	//public List<InnerMessageVo> queryInnerMessageByPage(Map<String, Object> param);

	//查询系统消息分页列表数据
	public List<InnerMessageVo> queryInnerMessageByPage(Page<InnerMessageVo> page);
}
