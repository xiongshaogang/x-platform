package com.xplatform.base.system.inform.service;

import java.util.Map;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.inform.entity.AnnunciateEntity;

public interface AnnunciateService {

	void getDataGridReturn(CriteriaQuery cq, boolean b);

	void deleteByAnnunciateId(String id);

	AnnunciateEntity getAnnunciateById(String id);

	void updateAnnunciateEntity(AnnunciateEntity annunciate);

	void saveAnnunciateEntity(AnnunciateEntity annunciate);

	String getReference(AnnunciateEntity me);

	Map<String, String> getApproveResult(String id);
	
	public void sendMsg(String id);

}
