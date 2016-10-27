package com.xplatform.base.system.xml.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.service.BaseService;
import com.xplatform.base.system.xml.dao.XmlDao;
import com.xplatform.base.system.xml.entity.XmlEntity;
import com.xplatform.base.system.xml.service.XmlService;

@Service("xmlService")
public class XmlServiceImpl implements XmlService {
	
	@Resource
	private XmlDao xmlDao;
	
	@Resource
	private BaseService baseService;

	public void setXmlDao(XmlDao xmlDao) {
		this.xmlDao = xmlDao;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	public void save(XmlEntity xml) {
		// TODO Auto-generated method stub
		this.xmlDao.save(xml);
	}

	@Override
	public List<XmlEntity> queryList() {
		// TODO Auto-generated method stub
		return xmlDao.findByQueryString("from XmlEntity");
	}

}
