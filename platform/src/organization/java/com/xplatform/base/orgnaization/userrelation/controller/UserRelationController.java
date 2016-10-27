package com.xplatform.base.orgnaization.userrelation.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.userrelation.entity.UserRelationEntity;
import com.xplatform.base.orgnaization.userrelation.service.UserRelationService;
import com.xplatform.base.platform.common.def.BusinessConst;
import com.xplatform.base.platform.common.utils.ClientUtil;

@Controller
@RequestMapping("/userRelationController")
public class UserRelationController extends BaseController {

	@Resource
	private UserRelationService userRelationService;

	/**
	 * 新增好友关系
	 * 
	 * @author lixt
	 * @createtime 2015年10月14日 下午2:19:16
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(HttpServletRequest request) throws Exception {
		AjaxJson result = new AjaxJson();

		String apply = request.getParameter("apply");
		String receive = request.getParameter("receive");
		UserRelationEntity userRelationEntity = this.userRelationService.getUserRelation(apply, receive);
		if (userRelationEntity != null) {
			result.setMsg("无法添加对方为好友,您与对方已是好友");
			result.setSuccess(false);
		} else {
			UserRelationEntity userRelation = new UserRelationEntity();
			userRelation.setApply(apply);
			userRelation.setReceive(receive);
			userRelation.setRelationCode(BusinessConst.RelationCode_CODE_friend);
			this.userRelationService.saveUserRelation(userRelation);
			result.setMsg("成功增加好友");
		}
		return result;
	}

	/**
	 * 删除好友
	 * 
	 * @author lixt
	 * @createtime 2015年10月14日 下午2:19:16
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		String receive = request.getParameter("receive");
		String userId = ClientUtil.getUserId();
		if (StringUtil.isEmpty(userId)) {
			j.setSuccess(false);
			j.setMsg("请求参数有误");
		}
		Integer i = this.userRelationService.deleteUserRelation(userId, receive);
		if (i > 0) {
			j.setMsg("好友删除成功");
		} else {
			j.setMsg("无对应好友,请刷新");
		}
		return j;
	}

	/**
	 * 查询好友
	 * 
	 * @author lixt
	 * @createtime 2015年10月14日 下午2:19:16
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "queryAllMyFirend")
	@ResponseBody
	public AjaxJson queryAllMyFirend(HttpServletRequest request) throws BusinessException {
		AjaxJson j = new AjaxJson();
		String userId = ClientUtil.getUserId();
		if (StringUtil.isEmpty(userId)) {
			j.setSuccess(false);
			j.setMsg("请求参数有误");
		}
		List<UserEntity> list = this.userRelationService.queryAllMyFirend(userId);
		j.setObj(list);
		j.setMsg("获取所有好友成功");
		return j;
	}

}
