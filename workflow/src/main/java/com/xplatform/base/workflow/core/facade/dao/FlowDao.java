package com.xplatform.base.workflow.core.facade.dao;

public interface FlowDao {
	public String getDefXmlByDeployId(String deployId) ;
	public void wirteDefXml(final String deployId, String defXml);
}
