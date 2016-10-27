package com.xplatform.base.orgnaization.orggroup.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.util.FileUtils;
import com.xplatform.base.framework.core.util.MapKit;
import com.xplatform.base.framework.core.util.PasswordUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.orgnaization.orggroup.entity.GroupShareEntity;
import com.xplatform.base.orgnaization.orggroup.entity.OrgGroupEntity;
import com.xplatform.base.orgnaization.orggroup.service.GroupShareService;
import com.xplatform.base.orgnaization.orggroup.service.OrgGroupMemberService;
import com.xplatform.base.orgnaization.orggroup.service.OrgGroupService;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.orgnaization.user.service.UserService;
import com.xplatform.base.platform.common.utils.ClientUtil;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;




@Controller
@RequestMapping("/orgGroupController")
public class OrgGroupController extends BaseController {

	@Resource
	private OrgGroupService orgGroupservice;
	@Resource
	private OrgGroupMemberService orgGroupMemberService;
	@Resource
	private UserService userService;
	@Resource
	private GroupShareService groupShareService;

	/**
	 * Web端群设置
	 * 
	 * @return
	 */
	@RequestMapping(params = "groupSettingsEdit")
	public ModelAndView groupSettingsEdit(HttpServletRequest request) {
		String userId = ClientUtil.getUserId();
		String userName = ClientUtil.getName();
		String groupId = request.getParameter("groupId");
		if(StringUtil.isNotEmpty(groupId)){
			OrgGroupEntity group = orgGroupservice.get(groupId);
			String ownername= userService.queryName(group.getOwner());
			List<Map<String, Object>> groupUsersList = this.orgGroupMemberService.queryGroupUsers(groupId);
			request.setAttribute("groupUsers", groupUsersList);
			request.setAttribute("ownername",ownername);
			request.setAttribute("loginName",ClientUtil.getName());
			request.setAttribute("group", group);
			return new ModelAndView("main/group/groupSettingsEdit");
		}else{
			return new ModelAndView("main/group/workGroupAdd");
		}
	}

	/**
	 * 分享页
	 * 
	 * @return
	 */
	@RequestMapping(params = "groupShare")
	public ModelAndView groupShare(HttpServletRequest request) {
		String groupId = request.getParameter("orgId");
		OrgGroupEntity orgGroup = this.orgGroupservice.get(groupId);
		if (orgGroup != null) {
			request.setAttribute("name", orgGroup.getName());
			request.setAttribute("groupId", orgGroup.getId());
			UserEntity user = this.userService.get(orgGroup.getOwner());
			if (user != null) {
				request.setAttribute("portrait", user.getPortrait());
				request.setAttribute("ownerName", user.getName());
			}
		}
		return new ModelAndView("main/group/groupShare");
	}

	/**
	 * 新增群组
	 * 
	 * @author lixt
	 * @createtime 2015年10月13日 下午2:19:16
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(OrgGroupEntity orgGroup, HttpServletRequest request) throws Exception {
		AjaxJson result = new AjaxJson();
		orgGroup.setType("chat");
		String phones = request.getParameter("phones");
		String ids = request.getParameter("ids");
		String status = request.getParameter("workGroup");
		int workGroup = 0;
		if(StringUtil.isNotEmpty(status)){
			workGroup = Integer.parseInt(status);
		}
/*		if(workGroup == 2){
			if(StringUtil.isEmpty(orgGroup.getName())){
				result.setMsg("群组名称不能为空");
				result.setSuccess(false);
				return result;
			}
		}*/
		if (StringUtil.isEmpty(phones) && StringUtil.isEmpty(ids)) {
			result = this.orgGroupservice.saveAndProcessHX(orgGroup,workGroup);
		} else {
			result = this.orgGroupservice.saveAndProcessHX(orgGroup, phones, ids,workGroup);
		}

		return result;
	}

	/**
	 * 修改群组自身信息
	 * 
	 * @author lixt
	 * @createtime 2015年10月13日 下午2:19:16
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "update")
	@ResponseBody
	public AjaxJson update(OrgGroupEntity orgGroup, HttpServletRequest request) throws Exception {
		AjaxJson result = new AjaxJson();
		this.orgGroupservice.updateAndProcessHX(orgGroup);
		result.setMsg("群组信息已更新");
		return result;
	}

	/**
	 * 为群组添加用户(单个/多个均可)
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "addUser")
	@ResponseBody
	public AjaxJson addUser(HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		String groupId = request.getParameter("groupId");
		String userIds = request.getParameter("userIds");
		// 传的是id,则进else方法，传的是phones，则进if方法
		if (StringUtil.isEmpty(userIds)) {
			String phones = request.getParameter("phones");
			j = this.orgGroupservice.addUser(groupId, phones, true);
		} else {
			j = this.orgGroupservice.addUser(groupId, userIds);
		}
		//j.setMsg("组用户添加成功");
		return j;
	}

	@RequestMapping(params = "deleteUser")
	@ResponseBody
	public AjaxJson deleteUser(HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		String groupId = request.getParameter("groupId");
		String userIds = request.getParameter("userIds");
		if(StringUtil.isNotEmpty(userIds) && StringUtil.isNotEmpty(groupId)){
			j = this.orgGroupservice.deleteUser(groupId, userIds);
			j.setSuccess(true);
		}else{
			j.setMsg("删除失败，参数有误");
			j.setSuccess(false);
		}
		return j;
	}

	/**
	 * 退出群组
	 * 
	 * @author lixt
	 * @createtime 2015年10月13日 下午2:19:16
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "exitGroup")
	@ResponseBody
	public AjaxJson exitGroup(HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		String groupId = request.getParameter("groupId");
		String userId = request.getParameter("userId");
		if (userId == null) {
			userId = ClientUtil.getUserId();
		}
		j = this.orgGroupservice.deleteUser(groupId, userId);
		return j;
	}

	/**
	 * 解散群组
	 * 
	 * @author lixt
	 * @createtime 2015年10月13日 下午2:19:16
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(HttpServletRequest request) throws Exception {
		AjaxJson result = new AjaxJson();
		String id = request.getParameter("groupId");
		this.orgGroupservice.deleteAndProcessHX(id);
		result.setMsg("工作组已解散！");
		result.setSuccess(true);
		return result;
	}

	/**
	 * 查询一个用户所有群组
	 * 
	 * @author lixt
	 * @createtime 2015年10月13日 下午2:19:16
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "queryUserGroups")
	@ResponseBody
	public AjaxJson queryUserGroups(HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		String pageStr = request.getParameter("page");
		int page = 1;
		if(StringUtil.isNotEmpty(pageStr)){
			page = Integer.parseInt(pageStr);
		}
		String rowsStr = request.getParameter("rows");
		String userId = ClientUtil.getUserId();
		if (StringUtil.isEmpty(userId)) {
			j.setSuccess(false);
			j.setMsg("请求参数有误,userId为null");
		}else{
			if("-1".equals(rowsStr)){
				List<Map<String, Object>> list = this.orgGroupservice.queryUserGroups(userId);
				j.setObj(list);
			}else{
				int rows = 10;
				if(StringUtil.isNotEmpty(rowsStr)){
					rows = Integer.parseInt(rowsStr);
				}
				List<Map<String, Object>> list = this.orgGroupservice.queryUserGroupsByPage(userId,rows,page);
				j.setObj(list);
			}

		}


		return j;
	}

	
	/**
	 * 查询一个用户所有群组
	 * 
	 * @author lixt
	 * @createtime 2015年10月13日 下午2:19:16
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "getUserGroup")
	@ResponseBody
	public AjaxJson getUserGroup(HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		String groupId = request.getParameter("groupId");
		j.setObj(this.orgGroupservice.getUserOrg(groupId));
		j.setSuccess(true);
		return j;
	}
	/**
	 * 查询用户的所有群好友
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryUsers")
	@ResponseBody
	public AjaxJson queryUsers(HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		String userId = ClientUtil.getUserId();
		if (StringUtil.isEmpty(userId)) {
			j.setSuccess(false);
			j.setMsg("请求参数有误");
		}
		List<Map<String, Object>> list = this.orgGroupservice.queryUsers(userId);
		j.setObj(list);
		return j;
	}
	
	/**
	 * 查询用户的所有工作组群群友
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryUsersByWork")
	@ResponseBody
	public AjaxJson queryUsersByWork(HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		String userId = ClientUtil.getUserId();
		if (StringUtil.isEmpty(userId)) {
			j.setSuccess(false);
			j.setMsg("请求参数有误");
		}else{
				List<Map<String, Object>> list = this.orgGroupservice.queryUsersByWork(userId);
				j.setObj(list);
				j.setSuccess(true);
				j.setMsg("获取成功");
			}
		
		return j;
	}

	/**
	 * 获得群头像(最多4个,附带群名称)
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(params = "queryGroupPortrait")
	@ResponseBody
	public AjaxJson queryGroupPortrait(HttpServletRequest request, HttpServletResponse response) throws IOException {
		AjaxJson j = new AjaxJson();
		String groupId = request.getParameter("groupId");
		OrgGroupEntity group = this.orgGroupservice.get(groupId);
		List<Map<String, Object>> list = this.orgGroupMemberService.queryGroupPortrait(groupId);
		j.setObj(MapKit.create("id", group.getId()).put("name", this.orgGroupservice.get(groupId).getName()).put("portraits", list).getMap());
		return j;
	}
	
	/**
	 * 获得群头像(最多4个,附带群名称)
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(params = "queryGroupsPortrait")
	@ResponseBody
	public AjaxJson queryGroupsPortrait(HttpServletRequest request, HttpServletResponse response) throws IOException {
		AjaxJson j = new AjaxJson();
		String groupIds = request.getParameter("groupIds");

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (String groupId : groupIds.split(",")) {
			List<Map<String, Object>> portraitList = this.orgGroupMemberService.queryGroupPortrait(groupId);
			OrgGroupEntity group = this.orgGroupservice.get(groupId);
			if(group != null){
				Map<String, Object> item = MapKit.create("id", group.getId()).put("name", group.getName()).put("portraits", portraitList).getMap();
				list.add(item);
			}	
		}
		j.setObj(list);
		return j;
	}

	/**
	 * 通过搜索关键词来查询某个群里边的人员
	 * 
	 * @author lixt
	 * @createtime 2015年10月13日 下午2:19:16
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(params = "queryUserFromGroup")
	@ResponseBody
	public AjaxJson queryUserFromGroup(HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		String searchKey = request.getParameter("searchKey");
		String groupId = request.getParameter("groupId");

		if (StringUtil.isEmpty(searchKey) || StringUtil.isEmpty(groupId)) {
			j.setSuccess(false);
			j.setMsg("请求参数有误");
		}

		List<Map<String, Object>> list = this.orgGroupservice.queryUserFromGroup(searchKey, groupId);
		j.setObj(list);
		return j;
	}

	@RequestMapping(params = "saveGroupShare")
	@ResponseBody
	public AjaxJson saveGroupShare(GroupShareEntity groupShare, HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		j = this.groupShareService.saveGroupShare(groupShare);
		return j;
	}

	@RequestMapping(params = "updateGroupShare")
	@ResponseBody
	public AjaxJson updateGroupShare(GroupShareEntity groupShare, HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		j = this.groupShareService.updateGroupShare(groupShare);
		return j;
	}

/*	@RequestMapping(params = "queryGroupShare")
	@ResponseBody
	public AjaxJson queryGroupShare(GroupShareEntity groupShare, HttpServletRequest request,HttpServletResponse response) throws Exception {
		AjaxJson j = new AjaxJson();
		String groupId = request.getParameter("groupId");
		if (jodd.util.StringUtil.isNotEmpty(groupId)) {
			j = this.groupShareService.doQueryGroupShare(groupId); 
			j.setStatus("");
		} else {
			j.setSuccess(false);
			j.setMsg("请传递groupId参数");
		}
		return j;
	}*/

/*	@RequestMapping(params = "getRQCode")
	@ResponseBody
	public AjaxJson getRQCode(GroupShareEntity groupShare, HttpServletRequest request,HttpServletResponse response) throws Exception {
		AjaxJson j = new AjaxJson();
		String groupId = request.getParameter("groupId");
		if (jodd.util.StringUtil.isNotEmpty(groupId)) {
			j = this.groupShareService.doQueryGroupShare(groupId);
			//FileUtils.outviewImg(request, response, fis);
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
			j.setMsg("请传递groupId参数");
		}
		return j;
	}*/
	
	
	@RequestMapping(params = "deleteGroupShare")
	@ResponseBody
	public AjaxJson deleteGroupShare(GroupShareEntity groupShare, HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		String groupId = request.getParameter("groupId");
		j = this.groupShareService.deleteGroupShare(groupId);
		return j;
	}

	/**
	 * 为群组添加用户(单个),扫二维吗接口调
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "addUserFromQRCode")
	@ResponseBody
	public AjaxJson addUserFromQRCode(HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		String groupId = request.getParameter("groupId");
		String randomCode = request.getParameter("randomCode");
		String userId = ClientUtil.getUserId();
		if (userId != null) {
			GroupShareEntity groupShare = this.groupShareService.queryGroupShare(groupId, randomCode);
			if (groupShare != null) {
				j = this.orgGroupservice.addUser(groupId, userId);
				j.setObj(groupId);
			} else {
				j.setMsg("无法加入该群,参数有误");
				j.setSuccess(false);
			}
		} else {
			j.setMsg("无法加入该群,请重新登录再试");
			j.setSuccess(false);
		}
		return j;
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
		String groupId = request.getParameter("groupId");
		String phone = request.getParameter("phone");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		//判断是否移动端请求
		String userId = ClientUtil.getUserId();
		if (StringUtil.isNotEmpty(userId)) {
			j = addUserFromQRCode(request);
		} else {
			if (StringUtil.isNotEmpty(userName)) {
				// 登录名登陆方式
				UserEntity user = userService.checkUserExits(userName);
				if (user != null) {
					// 密码校验正确
					String pcyPassword = PasswordUtil.encrypt(user.getId(), password, PasswordUtil.getStaticSalt());
					if (StringUtil.equals(pcyPassword, user.getPassword())) {

						j = this.orgGroupservice.addUser(groupId, phone, true);
					} else {
						j.setMsg("密码输入不正确");
						j.setSuccess(false);
					}
				} else {
					j.setMsg("用户不存在");
					j.setSuccess(false);
				}
			}
		}

		return j;
	}
	
	@RequestMapping(params = "workGroupList")
	public ModelAndView workGroupList(HttpServletRequest request) {
		return new ModelAndView("main/group/workGroupList");
	}
	
	/**
	 * 根据key模糊查询用户
	 * 
	 * @author lixt
	 * @throws UnsupportedEncodingException 
	 * @createtime 2015年11月11日 上午10:16:55
	 *
	 */
	@RequestMapping(params = "queryUsersByLikeKey")
	@ResponseBody
	public AjaxJson queryUsersByLikeKey(HttpServletRequest request) throws UnsupportedEncodingException {
		AjaxJson result = new AjaxJson();
		String message = "获取成功";
		String key = request.getParameter("key");
		String userId = ClientUtil.getUserId();
		if(StringUtil.isNotEmpty(userId)){
			result.setObj(this.orgGroupservice.queryUsersByLikeKey(userId,key));
			result.setMsg(message);
			result.setSuccess(true);
		}else{
			result.setMsg("获取数据失败");
			result.setSuccess(false);
		}
		return result;
	}
}
