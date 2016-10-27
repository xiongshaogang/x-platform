package com.xplatform.base.system.ntko.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.system.message.config.dao.MessageDao;
import com.xplatform.base.system.message.config.entity.MailConfigEntity;
import com.xplatform.base.system.ntko.dao.NtkoDao;
import com.xplatform.base.system.ntko.entity.HtmlFileEntity;
import com.xplatform.base.system.ntko.entity.OfficeFileEntity;
import com.xplatform.base.system.ntko.service.NtkoService;

@Service("ntkoService")
public class NtkoServiceImpl implements NtkoService {
	
	@Resource
	private NtkoDao ntkoDao;

	
	public void setNtkoDao(NtkoDao ntkoDao) {
		this.ntkoDao = ntkoDao;
	}


	@Override
	public OfficeFileEntity queryOfficeFileById(String deleteFileId) {
		// TODO Auto-generated method stub
		return this.ntkoDao.getEntity(OfficeFileEntity.class, deleteFileId);
	}


	@Override
	public void updateOfficeFile(OfficeFileEntity of) {
		// TODO Auto-generated method stub
		OfficeFileEntity old = this.queryOfficeFileById(of.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(of, old);
			this.ntkoDao.merge(old);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void saveOfficeFile(OfficeFileEntity of) {
		// TODO Auto-generated method stub
		this.ntkoDao.save(of);
	}


	@Override
	public OfficeFileEntity getOfficeEntityByBusId(String id) {
		// TODO Auto-generated method stub
		List<OfficeFileEntity> list = this.ntkoDao.findByProperty(OfficeFileEntity.class, "busId", id);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}


	@Override
	public HtmlFileEntity getHtmlFileEntityByBusId(String busId) {
		// TODO Auto-generated method stub
		List<HtmlFileEntity> list = this.ntkoDao.findByProperty(HtmlFileEntity.class, "busId", busId);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}


	@Override
	public void updateHtmlFile(HtmlFileEntity hfe) {
		// TODO Auto-generated method stub
		HtmlFileEntity old = this.ntkoDao.get(HtmlFileEntity.class, hfe.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(hfe, old);
			this.ntkoDao.merge(old);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void saveHtmlFile(HtmlFileEntity hfe) {
		// TODO Auto-generated method stub
		this.ntkoDao.save(hfe);
	}

}
