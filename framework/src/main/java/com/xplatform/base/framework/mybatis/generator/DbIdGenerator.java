package com.xplatform.base.framework.mybatis.generator;

import org.springframework.beans.factory.annotation.Autowired;

import com.xplatform.base.framework.mybatis.entity.IdEntity;
import com.xplatform.base.framework.mybatis.service.IdEntityService;


public class DbIdGenerator implements IdGenerator {

	protected int idBlockSize = 100;

	protected long nextId = 0;

	protected long lastId = -1;

	protected String tmp = "00000000000000";
	
	protected IdEntityService idEntityService;
	
	public synchronized String getNextId() {
		if (lastId < nextId) {
			getNewBlock();
		}
		long _nextId = nextId++;
		
		return Long.toString(_nextId);
	}
	
	/*protected String buildNextId(long _nextId){
		
		StringBuilder idBuilder = new StringBuilder(DataUtils.getSystemCurrentDateTimeSimpleStr());
		
		String nextId = Long.toString(_nextId);
		
		int nextIdLength = nextId.length();
		
		String tmpStr = tmp.substring(0, (tmp.length() - nextIdLength));
		
		idBuilder.append(tmpStr).append(nextId);
		
		return idBuilder.toString();
	}*/
	
	protected synchronized void getNewBlock() {

		IdEntity idEntity = idEntityService.findPropertyById("next.dbid");

		long oldValue = Long.parseLong(idEntity.getValue());
		long newValue = oldValue + idBlockSize;
		idEntity.setValue(Long.toString(newValue));

		idEntityService.updateIdGenerator(idEntity);

		this.nextId = oldValue;
		this.lastId = newValue - 1;

	}

	public int getIdBlockSize() {
		return idBlockSize;
	}

	public void setIdBlockSize(int idBlockSize) {
		this.idBlockSize = idBlockSize;
	}

	

	public IdEntityService getIdEntityService() {
		return idEntityService;
	}

	@Autowired
	public void setIdEntityService(IdEntityService idEntityService) {
		this.idEntityService = idEntityService;
	}

}
