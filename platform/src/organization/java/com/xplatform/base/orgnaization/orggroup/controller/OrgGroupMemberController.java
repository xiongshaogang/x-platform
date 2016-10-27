package com.xplatform.base.orgnaization.orggroup.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.orgnaization.orggroup.dao.OrgGroupMemberDao;
import com.xplatform.base.orgnaization.orggroup.entity.OrgGroupEntity;
import com.xplatform.base.orgnaization.orggroup.entity.OrgGroupMemberEntity;
import com.xplatform.base.orgnaization.orggroup.service.OrgGroupMemberService;
import com.xplatform.base.orgnaization.orggroup.service.OrgGroupService;
import com.xplatform.base.system.dict.dao.DictTypeDao;

@Controller
@RequestMapping("/orgGroupMemberController")
public class OrgGroupMemberController extends BaseController {

	@Resource
	private OrgGroupMemberService orgGroupMemberService;

	/**
	 * 新增群组人员
	 * 
	 * @author lixt
	 * @createtime 2015年10月13日 下午2:19:16
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(OrgGroupMemberEntity orgGroupMember, HttpServletRequest request) throws BusinessException {
		AjaxJson result = new AjaxJson();
		String phones = request.getParameter("phones");
		String message = "";
		// orgGroupMember.setId(UUIDGenerator.generate());

		message = this.orgGroupMemberService.saveOrgGroMem(orgGroupMember, phones);
		result.setMsg(message);
		result.setSuccess(true);

		return result;
	}

	/**
	 * 修改群组人员表
	 * 
	 * @author lixt
	 * @createtime 2015年10月13日 下午2:19:16
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "update")
	@ResponseBody
	public AjaxJson update(OrgGroupMemberEntity orgGroupMember, HttpServletRequest request) throws Exception {
		AjaxJson result = new AjaxJson();
		String message = "";

		this.orgGroupMemberService.updateOrgGroMem(orgGroupMember);
		result.setMsg("群组人员更新成功");
		result.setSuccess(true);

		return result;
	}

	/**
	 * 删除群组人员
	 * 
	 * @author lixt
	 * @createtime 2015年10月13日 下午2:19:16
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(OrgGroupMemberEntity orgGroupMember, HttpServletRequest request) throws BusinessException {
		AjaxJson result = new AjaxJson();
		String message = "";
		String id = request.getParameter("id");

		this.orgGroupMemberService.deleteOrgGroMem(id);

		result.setMsg("删除成功");
		result.setSuccess(true);

		return result;
	}

	/**
	 * 查询一个群里的群组人员
	 * 
	 * @author lixt
	 * @createtime 2015年10月13日 下午2:19:16
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "queryList")
	@ResponseBody
	public AjaxJson queryList(OrgGroupMemberEntity orgGroupMember, HttpServletRequest request) throws BusinessException {
		AjaxJson result = new AjaxJson();
		String groupId = request.getParameter("groupId");
		List<Map<String, Object>> list = this.orgGroupMemberService.queryGroupUsers(groupId);
		result.setObj(list);
		result.setMsg("查询成功");
		result.setSuccess(true);

		return result;
	}
}
