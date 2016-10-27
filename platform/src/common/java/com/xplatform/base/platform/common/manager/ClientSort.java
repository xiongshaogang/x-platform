package com.xplatform.base.platform.common.manager;

import java.util.Comparator;

import com.xplatform.base.platform.common.vo.Client;


public class ClientSort implements Comparator<Client> {

	
	public int compare(Client prev, Client now) {
		return (int) (now.getLogindatetime().getTime()-prev.getLogindatetime().getTime());
	}

}
