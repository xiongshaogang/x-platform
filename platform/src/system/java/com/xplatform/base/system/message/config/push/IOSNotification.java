package com.xplatform.base.system.message.config.push;

import java.util.Arrays;
import java.util.HashSet;

import com.xplatform.base.framework.core.common.exception.BusinessException;

import net.sf.json.JSONObject;

public abstract class IOSNotification extends UmengNotification {

	// Keys can be set in the aps level
	protected static final HashSet<String> APS_KEYS = new HashSet<String>(Arrays.asList(new String[]{
			"alert", "badge", "sound", "content-available"
	}));
	
	@Override
	public boolean setPredefinedKeyValue(String key, Object value) throws BusinessException {
		if (ROOT_KEYS.contains(key)) {
			// This key should be in the root level
			rootJson.put(key, value);
		} else if (APS_KEYS.contains(key)) {
			// This key should be in the aps level
			JSONObject apsJson = null;
			JSONObject payloadJson = null;
			if (rootJson.has("payload")) {
				payloadJson = rootJson.getJSONObject("payload");
			} else {
				payloadJson = new JSONObject();
				rootJson.put("payload", payloadJson);
			}
			if (payloadJson.has("aps")) {
				apsJson = payloadJson.getJSONObject("aps");
			} else {
				apsJson = new JSONObject();
//				payloadJson.put("aps", apsJson);
			}
			apsJson.put(key, value);
			payloadJson.put("aps", apsJson);
			rootJson.put("payload", payloadJson);
		} else if (POLICY_KEYS.contains(key)) {
			// This key should be in the body level
			JSONObject policyJson = null;
			if (rootJson.has("policy")) {
				policyJson = rootJson.getJSONObject("policy");
			} else {
				policyJson = new JSONObject();
				rootJson.put("policy", policyJson);
			}
			policyJson.put(key, value);
		} else {
			if (key == "payload" || key == "aps" || key == "policy") {
				throw new BusinessException("You don't need to set value for " + key + " , just set values for the sub keys in it.");
			} else {
				throw new BusinessException("Unknownd key: " + key);
			}
		}
		
		return true;
	}
	// Set customized key/value for IOS notification
	public boolean setCustomizedField(String key, String value) throws BusinessException {
		//rootJson.put(key, value);
		JSONObject payloadJson = null;
		if (rootJson.has("payload")) {
			payloadJson = rootJson.getJSONObject("payload");
		} else {
			payloadJson = new JSONObject();
			rootJson.put("payload", payloadJson);
		}
		payloadJson.put(key, value);
		return true;
	}

}