package com.xplatform.base.workflow.support.msgtemplate.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.CommonDao;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.workflow.support.msgtemplate.dao.MsgTemplateDao;
import com.xplatform.base.workflow.support.msgtemplate.entity.MsgTemplateEntity;
/**
 * 
 * description :信息模版dao实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 上午11:23:11
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 上午11:23:11
 *
 */
@Repository("msgTemplateDao")
public class MsgTemplateDaoImpl extends CommonDao implements MsgTemplateDao {

	@Override
	public String addMsgTemplate(MsgTemplateEntity MsgTemplate) {
		// TODO Auto-generated method stub
		return (String) this.save(MsgTemplate);
	}

	@Override
	public void deleteMsgTemplate(String id) {
		// TODO Auto-generated method stub
		this.deleteEntityById(MsgTemplateEntity.class, id);
	}

	@Override
	public void updateMsgTemplate(MsgTemplateEntity MsgTemplate) {
		// TODO Auto-generated method stub
		this.updateEntitie(MsgTemplate);
	}

	@Override
	public MsgTemplateEntity getMsgTemplate(String id) {
		// TODO Auto-generated method stub
		return (MsgTemplateEntity) this.get(MsgTemplateEntity.class, id);
	}

	@Override
	public List<MsgTemplateEntity> queryMsgTemplateList() {
		// TODO Auto-generated method stub
		return this.findByQueryString("from MsgTemplateEntity");
	}

	@Override
	public void DataGrid(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		this.getDataGridReturn(cq, false);
	}

}
