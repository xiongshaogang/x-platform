package com.xplatform.base.orgnaization.userrelation.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import com.xplatform.base.framework.core.common.dao.impl.BaseDaoImpl;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.hibernate.qbc.HqlQuery;
import com.xplatform.base.framework.core.common.hibernate.qbc.PageList;
import com.xplatform.base.framework.core.common.model.common.DBTable;
import com.xplatform.base.framework.core.common.model.common.UploadFile;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.common.model.json.TreeGrid;
import com.xplatform.base.framework.core.extend.template.Template;
import com.xplatform.base.framework.tag.vo.datatable.DataTableReturn;
import com.xplatform.base.framework.tag.vo.easyui.ComboTreeModel;
import com.xplatform.base.framework.tag.vo.easyui.TreeGridModel;
import com.xplatform.base.orgnaization.userrelation.dao.UserRelationDao;
import com.xplatform.base.orgnaization.userrelation.entity.UserRelationEntity;

@Repository("userRelationDao")
public class UserRelationDaoImpl extends BaseDaoImpl<UserRelationEntity> implements UserRelationDao {

}
