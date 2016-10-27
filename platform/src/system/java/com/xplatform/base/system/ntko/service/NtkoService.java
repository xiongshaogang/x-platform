package com.xplatform.base.system.ntko.service;

import com.xplatform.base.system.ntko.entity.HtmlFileEntity;
import com.xplatform.base.system.ntko.entity.OfficeFileEntity;


public interface NtkoService {

	OfficeFileEntity queryOfficeFileById(String deleteFileId);

	void updateOfficeFile(OfficeFileEntity of);

	void saveOfficeFile(OfficeFileEntity of);

	OfficeFileEntity getOfficeEntityByBusId(String id);

	HtmlFileEntity getHtmlFileEntityByBusId(String busId);

	void updateHtmlFile(HtmlFileEntity hfe);

	void saveHtmlFile(HtmlFileEntity hfe);

}
