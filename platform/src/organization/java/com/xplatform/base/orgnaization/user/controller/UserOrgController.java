package com.xplatform.base.orgnaization.user.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.util.PasswordUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.orgnaization.orggroup.entity.GroupShareEntity;
import com.xplatform.base.orgnaization.orggroup.entity.OrgGroupEntity;
import com.xplatform.base.orgnaization.orggroup.service.GroupShareService;
import com.xplatform.base.orgnaization.orgnaization.entity.OrgnaizationEntity;
import com.xplatform.base.orgnaization.orgnaization.service.OrgnaizationService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.entity.UserOrgEntity;
import com.xplatform.base.orgnaization.user.service.UserOrgService;
import com.xplatform.base.orgnaization.user.service.UserService;
import com.xplatform.base.platform.common.utils.ClientUtil;

/**
 * 
 * description :  员工岗位管理
 *
 * @version 1.0
 * @author hexj
 * @createtime : 2014年12月10日 上午9:35:06
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * hexj        2014年12月10日 上午9:35:06
 *
 */
@Controller
@RequestMapping("/userOrgController")
public class UserOrgController extends BaseController {
	@Resource
	private UserOrgService userOrgService;
	@Resource
	private OrgnaizationService orgnaizationService;
	@Resource
	private UserService userService;
	@Resource
	private GroupShareService groupShareService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月20日 下午10:23:10
	 * @Decription  岗位分配员工
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "jobUserSel")
	public ModelAndView jobUserSel(HttpServletRequest request){
		String jobId = request.getParameter("jobId");
		List<UserOrgEntity> empJobList = userOrgService.findByJobId(jobId);
		request.setAttribute("jobId", jobId);
		request.setAttribute("empJobList", empJobList);
		return new ModelAndView("common/jobEmpList");
	}

	/**
	 * 
	 * @author hexj
	 * @createtime 2014年12月20日 下午10:10:46
	 * @Decription  保存岗位分配员工
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "saveUsersOrg")
	@ResponseBody
	public AjaxJson saveUsersOrg(HttpServletRequest request){
		AjaxJson result=new AjaxJson();
		String orgId = request.getParameter("orgId");
		String userIds = request.getParameter("ids");
		if(StringUtil.isEmpty(userIds)){
			message = "用户不存在";
			result.setSuccess(false);
			return result;
		}
		try{
			userOrgService.updateJobUsers(orgId, userIds);
			message = "分配员工成功";
		}catch(Exception e){
			message = "分配员工失败";
		}
		result.setMsg(message);
		return result;
	}
	
	@RequestMapping(params = "addUser")
	@ResponseBody
	public AjaxJson addUser(HttpServletRequest request){
		AjaxJson result=new AjaxJson();
		String orgId = request.getParameter("orgId");
		String userIds= request.getParameter("userIds");
		if(StringUtil.isEmpty(userIds)){
			message = "没有选择用户";
			result.setSuccess(false);
			return result;
		}
		if(StringUtil.isEmpty(orgId)){
			message = "没有选择组织";
			result.setSuccess(false);
			return result;
		}
		try{
			this.userOrgService.addJobUsers(userIds, orgId);
			message = "加入员工成功";
		}catch(Exception e){
			message = "加入员工失败";
		}
		result.setMsg(message);
		return result;
	}
	
	@RequestMapping(params = "saveUserOrgs")
	@ResponseBody
	public AjaxJson saveUserOrgs(HttpServletRequest request){
		AjaxJson result=new AjaxJson();
		String orgId = request.getParameter("orgId");
		String orgIds = request.getParameter("orgIds");
		String userId = request.getParameter("userId");
		String phone = request.getParameter("phone");
		String type = request.getParameter("type");
		if(StringUtil.isEmpty(userId) && StringUtil.isNotEmpty(phone)){
			List<UserEntity> userList=this.userService.findUserByPhone(phone);
			if(userList!=null && userList.size()>0){
				UserEntity user=userList.get(0);
				userId=user.getId();
			}
		}
		if(StringUtil.isEmpty(userId)){
			userId = ClientUtil.getUserId();
		}
		if(StringUtil.equals("orgUser", type)){
			boolean flag=this.userOrgService.isExistCompany(userId,orgId);
			if(flag){
				result.setSuccess(false);
				result.setMsg("用户已经在团队中");
				return result;
			}
		}
		
		try{
			userOrgService.updateUserJobs(userId, orgIds,orgId);
			message = "分配部门成功";
		}catch(Exception e){
			message = "分配部门失败";
		}
		result.setMsg(message);
		return result;
	}
	
	@RequestMapping(params = "addUserOrg")
	@ResponseBody
	public AjaxJson addUserOrg(HttpServletRequest request) {
		AjaxJson result=new AjaxJson();
		String orgId = request.getParameter("orgId");
		String phone = request.getParameter("phone");
		String name = request.getParameter("name");
		String type = request.getParameter("type");
		try {
			userOrgService.addUserToOrg(orgId, phone, name, type);
			message = "添加员工成功";
		} catch (Exception e) {
			message = "添加员工失败";
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 通过jobId获得岗位下的用户
	 * @author xiaqiang
	 * @createtime 2015年4月22日 下午11:04:56
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "getUserIds4Orgs")
	@ResponseBody
	public String getUserIds4Orgs(HttpServletRequest request){
		String orgId = request.getParameter("orgId");
		String userIds = "";
		if (StringUtil.isNotEmpty(orgId)) {
			StringBuffer sb = new StringBuffer();
			List<UserOrgEntity> userJobList = userOrgService.findByJobId(orgId);
			for (UserOrgEntity uj : userJobList) {
				String userId = uj.getUser().getId();
				sb.append(userId).append(",");
			}
			userIds = StringUtil.removeDot(sb.toString());
		}
		return userIds;
	}
	
	/**
	 * 查询机构分享二维码数据
	 * @author lixt
	 * @createtime 2016年01月20日 下午02:04:56
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "queryOrgShare")
	@ResponseBody
	public AjaxJson queryGroupShare(GroupShareEntity groupShare, HttpServletRequest request,HttpServletResponse response) throws Exception {
		AjaxJson j = new AjaxJson();
		String serverHost = request.getHeader("host");
		String orgId = request.getParameter("orgId");
		if (jodd.util.StringUtil.isNotEmpty(orgId)) {
			j = this.groupShareService.doQueryGroupShare(orgId,serverHost); 
		} else {
			j.setSuccess(false);
			j.setMsg("请传递orgId参数");
		}
		return j;
	}
	/**
	 * 查询机构分享二维码（直接返回二维码输出流）
	 * @author lixt
	 * @createtime 2016年01月20日 下午02:04:56
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "getRQCode")
	@ResponseBody
	public AjaxJson getRQCode(GroupShareEntity groupShare, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String serverHost = request.getHeader("host");
		AjaxJson j = new AjaxJson();
		String orgId = request.getParameter("orgId");
		if (jodd.util.StringUtil.isNotEmpty(orgId)) {
			OrgnaizationEntity org = this.orgnaizationService.get(orgId);
			if(org == null){
				j.setSuccess(false);
				j.setMsg("orgId参数有误");
				return j;
			}
			j = this.groupShareService.doQueryGroupShare(orgId,serverHost);
			  BitMatrix bitMatrix = null;  
		        try { 
		        	String url = j.getStatus();
		        	QRCodeWriter writer = new QRCodeWriter();
		            bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, 300, 300);  
		            MatrixToImageWriter.writeToStream(bitMatrix, "jpg", response.getOutputStream());  
		            response.getOutputStream().flush();  
		            response.getOutputStream().close();  
		        } catch (WriterException e) {  
		            e.printStackTrace();  
		        } catch (IOException e) {  
		            e.printStackTrace();  
		        }  
		} else {
			j.setSuccess(false);
			j.setMsg("请传递orgId参数");
		}
		return j;
	}
	
	/**
	 * 分享页
	 * 
	 * @return
	 */
	@RequestMapping(params = "groupShare")
	public ModelAndView groupShare(HttpServletRequest request) {
		String orgId = request.getParameter("orgId");
		OrgnaizationEntity org = this.orgnaizationService.get(orgId);
		if (org != null) {
			request.setAttribute("name", org.getName());
			request.setAttribute("orgId", org.getId());
			request.setAttribute("orgIds", org.getId());
			request.setAttribute("type", "orgUser");
			UserEntity user = this.userService.get(ClientUtil.getUserId());
			if (user != null) {
				request.setAttribute("portrait", user.getPortrait());
				request.setAttribute("ownerName", user.getName());
			}
		}
		return new ModelAndView("main/group/orgShare");
	}
	
	/**
	 * 为群组添加用户(单个),web页面接口调
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "addUserFromWeb")
	@ResponseBody
	public AjaxJson addUserFromWeb(HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		// 判断是否移动端请求

		if (StringUtil.isNotEmpty(userName)) {
			// 登录名登陆方式
			UserEntity user = userService.checkUserExits(userName);
			if (user != null) {
				// 密码校验正确
				String pcyPassword = PasswordUtil.encrypt(user.getId(), password, PasswordUtil.getStaticSalt());
				if (StringUtil.equals(pcyPassword, user.getPassword())) {
					j = this.saveUserOrgs(request);
				} else {
					j.setStatus("2");
					j.setMsg("密码输入不正确");
					j.setSuccess(false);
				}
			} else {
				j.setStatus("3");
				j.setMsg("用户不存在");
				j.setSuccess(false);
			}
		}

		return j;
	}
	
}
