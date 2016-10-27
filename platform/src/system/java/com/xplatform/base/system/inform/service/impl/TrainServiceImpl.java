package com.xplatform.base.system.inform.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.system.inform.dao.TrainDao;
import com.xplatform.base.system.inform.entity.TrainEntity;
import com.xplatform.base.system.inform.service.TrainService;

@Service("trainService")
public class TrainServiceImpl implements TrainService{
	
private static final Logger logger = Logger.getLogger(TrainServiceImpl.class);
	
	@Resource
	private TrainDao trainDao;

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		try {
			this.trainDao.getDataGridReturn(cq, b);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("查询培训通知列表失败");
		}
	}

	@Override
	public TrainEntity getTrainById(String id) {
		// TODO Auto-generated method stub
		return this.trainDao.get(TrainEntity.class, id);
	}

	@Override
	public void updateTrainEntity(TrainEntity train) {
		// TODO Auto-generated method stub
		TrainEntity me = this.getTrainById(train.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(train, me);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("实体更新出错TrainEntity id="+train.getId());
		}
		this.trainDao.merge(me);
	}

	@Override
	public void saveTrainEntity(TrainEntity train) {
		// TODO Auto-generated method stub
		this.trainDao.save(train);
		
	}
	
	@Override
	public void deleteByTrainId(String id) {
		// TODO Auto-generated method stub
		//假删除
		TrainEntity me = this.getTrainById(id);
		me.setStatus("2");
		this.trainDao.merge(me);
		//this.trainDao.deleteEntityById(TrainEntity.class, id);//真删除
	}

}
