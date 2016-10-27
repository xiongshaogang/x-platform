package com.xplatform.base.orgnaization.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xplatform.base.framework.core.common.service.impl.BaseServiceImpl;
import com.xplatform.base.framework.core.util.sms.JXTSmsClient;
import com.xplatform.base.framework.core.util.sms.SmsUtil;
import com.xplatform.base.orgnaization.user.dao.UserVerifycodeDao;
import com.xplatform.base.orgnaization.user.entity.UserVerifycodeEntity;
import com.xplatform.base.orgnaization.user.service.UserVerifycodeService;

/**
 * 
 * description :字典类型service实现
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 下午12:30:12
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 下午12:30:12
 *
 */
@Service("userVerifycodeService")
public class UserVerifycodeServiceImpl extends BaseServiceImpl<UserVerifycodeEntity> implements UserVerifycodeService {
	private static final Logger logger = Logger.getLogger(UserVerifycodeServiceImpl.class);
	@Resource
	private UserVerifycodeDao userVerifycodeDao;
	
	@Resource
	public void setBaseDao(UserVerifycodeDao userVerifycodeDao) {
		super.setBaseDao(userVerifycodeDao);
	}
	
	public void sendVerifyCode(UserVerifycodeEntity verifycode) {
		Random random = new Random();
		int k = random.nextInt(9000) + 1000;
		String num = String.valueOf(k);
		verifycode.setVerifyCode(num);
		try {

			// Member m = getByPhone(phone);
			List<UserVerifycodeEntity> userVerifycodeList= this.userVerifycodeDao.findByProperty("phone", verifycode.getPhone());
			if (userVerifycodeList != null && userVerifycodeList.size()>0) {
				UserVerifycodeEntity userVerifycode=userVerifycodeList.get(0);
				verifycode.setId(userVerifycode.getId());
				this.userVerifycodeDao.merge(verifycode);
			} else {
				this.userVerifycodeDao.save(verifycode);
			}
			SmsUtil.send(verifycode.getPhone(), "您的验证码是" + num);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public boolean compareVerifyCode(String phone,String verifyCode,String moduleFlag){
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("phone", phone);
		param.put("verifyCode", verifyCode);
		param.put("moduleFlag", moduleFlag);
		List<UserVerifycodeEntity> userVerifycode= this.userVerifycodeDao.findByObjectPropertys(UserVerifycodeEntity.class, param);
		if (userVerifycode != null && userVerifycode.size()>0) {
			Date updateTime =userVerifycode.get(0).getUpdateTime();
			Date now = new Date();
			long num = now.getTime() - updateTime.getTime();
			if (num / (5 * 60 * 1000) > 1) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
}
