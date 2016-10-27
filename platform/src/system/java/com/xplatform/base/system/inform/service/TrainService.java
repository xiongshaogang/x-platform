package com.xplatform.base.system.inform.service;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.system.inform.entity.TrainEntity;

public interface TrainService {
	
	void getDataGridReturn(CriteriaQuery cq, boolean b);

	/**
	 * 根据id获取实体
	 * */
	TrainEntity getTrainById(String id);


	void updateTrainEntity(TrainEntity meeting);

	void saveTrainEntity(TrainEntity meeting);

	void deleteByTrainId(String id);

}
