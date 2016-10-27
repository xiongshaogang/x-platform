package com.xplatform.base.orgnaization.appversion.service.impl;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.orgnaization.appversion.dao.AppVersionDao;
import com.xplatform.base.orgnaization.appversion.entity.AppVersionEntity;
import com.xplatform.base.orgnaization.appversion.service.AppVersionService;



@Service("appVersonService")
public class AppVersionServiceImpl extends BaseServiceImpl<AppVersionEntity> implements AppVersionService{

	@Resource
	private AppVersionDao appVersonDao;
	
	@Override
	public DataGridReturn getDataGridReturn(CriteriaQuery cq, boolean flag) {
		return this.appVersonDao.getDataGridReturn(cq, true);
	}

	@Override
	public void saveAppVersion(AppVersionEntity appVersonEntity) throws BusinessException{
		 this.appVersonDao.save(appVersonEntity);
	}

	@Override
	public List<AppVersionEntity> queryAppVersonList() throws BusinessException{
		List<AppVersionEntity> list = appVersonDao.findHql("from AppVersionEntity order by createTime desc");
		return list;
	}

	@Override
	public  List<AppVersionEntity> getNewApp(String versionNumber,int type) throws BusinessException {
		//String hql = "form AppVersionEntity where versionNumber > '"+versionNumber+"'  ORDER BY versionNumber DESC";
		String sql = " from AppVersionEntity where versionNumber > '"+versionNumber+"' and type="+type+" ORDER BY versionNumber DESC";
		//return this.appVersonDao.findHql(hql);
		return this.appVersonDao.findByQueryString(sql);
	}
	
	public AppVersionEntity getLatestApp(int type) {
		String hql = " FROM AppVersionEntity where type=? ORDER BY versionNumber DESC";
		List<AppVersionEntity> list = this.appVersonDao.findHql(hql,type);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

}
