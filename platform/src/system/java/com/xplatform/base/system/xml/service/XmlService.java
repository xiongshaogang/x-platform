package com.xplatform.base.system.xml.service;

import java.util.List;

import com.xplatform.base.system.xml.entity.XmlEntity;

public interface XmlService  {

	void save(XmlEntity xml);

	List<XmlEntity> queryList();

}
