package com.xplatform.base.system.formlogo.dao.impl;

import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.BaseDaoImpl;
import com.xplatform.base.system.formlogo.dao.FormLogoDao;
import com.xplatform.base.system.formlogo.entity.FormLogoEntity;

@Repository("formLogoDao")
public class FormLogoDaoImpl extends BaseDaoImpl<FormLogoEntity> implements FormLogoDao{

}
