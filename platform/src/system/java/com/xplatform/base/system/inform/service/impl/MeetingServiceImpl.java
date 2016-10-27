package com.xplatform.base.system.inform.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.system.dict.dao.DictTypeDao;
import com.xplatform.base.system.inform.dao.MeetingDao;
import com.xplatform.base.system.inform.entity.MeetingEntity;
import com.xplatform.base.system.inform.service.MeetingService;
import com.xplatform.base.system.message.config.service.impl.MessageServiceImpl;

@Service("meetingService")
public class MeetingServiceImpl implements MeetingService{
	
	private static final Logger logger = Logger.getLogger(MeetingServiceImpl.class);
	
	@Resource
	private MeetingDao meetingDao;

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean b) {
		// TODO Auto-generated method stub
		try {
			this.meetingDao.getDataGridReturn(cq, b);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("查询会议通知列表失败");
		}
	}

	@Override
	public MeetingEntity getMeetingById(String id) {
		// TODO Auto-generated method stub
		return this.meetingDao.get(MeetingEntity.class, id);
	}

	@Override
	public void updateMeetingEntity(MeetingEntity meeting) {
		// TODO Auto-generated method stub
		MeetingEntity me = this.getMeetingById(meeting.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(meeting, me);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("实体更新出错MeetingEntity id="+meeting.getId());
		}
		this.meetingDao.merge(me);
	}

	@Override
	public void saveMeetingEntity(MeetingEntity meeting) {
		// TODO Auto-generated method stub
		this.meetingDao.save(meeting);
		
	}
	
	@Override
	public void deleteByMeetingId(String id) {
		// TODO Auto-generated method stub
		//假删除
		MeetingEntity me = this.getMeetingById(id);
		me.setStatus("2");
		this.meetingDao.merge(me);
		//this.meetingDao.deleteEntityById(MeetingEntity.class, id);//真删除
	}

	
	public void setMeetingDao(MeetingDao meetingDao) {
		this.meetingDao = meetingDao;
	}

}
