package com.xplatform.base.orgnaization.member.controller;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.cache.UcgCache;
import com.xplatform.base.framework.core.cache.manager.UcgCacheManager;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.common.model.json.DataGridReturn;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.PasswordUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.orgnaization.member.entity.MemberEntity;
import com.xplatform.base.orgnaization.member.entity.TOrgUser;
import com.xplatform.base.orgnaization.member.service.MemberService;
import com.xplatform.base.platform.common.utils.ClientUtil;

/**   
 * @Title: Controller
 * @Description: 会员信息
 * @author onlineGenerator
 * @date 2014-05-21 14:29:30
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/memberController")
public class MemberController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MemberController.class);

	@Resource
	private MemberService memberService;
	@Resource
	private UcgCacheManager ucgCacheManager;
	
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}

	
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * 会员信息列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "member")
	public ModelAndView member(HttpServletRequest request) {
		return new ModelAndView("platform/organization/member/memberList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(MemberEntity member,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(MemberEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, member, request.getParameterMap());
		DataGridReturn dgReturn=this.memberService.getDataGridReturn(cq, false);
		
		setExcelExportData(request,  dgReturn.getRows(), ClientUtil.getSessionId());
		this.memberService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除会员信息
	 * 
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(MemberEntity member, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		member = memberService.getEntity(MemberEntity.class, member.getId());
		message = "会员信息删除成功";
		try{
			memberService.delete(member);
		}catch(Exception e){
			e.printStackTrace();
			message = "会员信息删除失败";
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除会员信息
	 * 
	 * @return
	 */
	 @RequestMapping(params = "deleteBatch")
	@ResponseBody
	public AjaxJson deleteBatch(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		message = "会员信息删除成功";
		try{
			for(String id:ids.split(",")){
				MemberEntity member = memberService.getEntity(MemberEntity.class, 
				id
				);
				memberService.delete(member);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "会员信息删除失败";
		}
		j.setMsg(message);
		return j;
	}
	 
	@RequestMapping(params = "saveOrUpdate")
	@ResponseBody
	public AjaxJson saveOrUpdate(MemberEntity member, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if(member.getId() != null && !member.getId().equals("")){
			j = this.update(member, request);
		}else{
			j = this.save(member, request);
		}
		return j;
	}


	/**
	 * 添加会员信息
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(MemberEntity member, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "会员信息添加成功";
		try{
			memberService.save(member);
		}catch(Exception e){
			e.printStackTrace();
			message = "会员信息添加失败";
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新会员信息
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "update")
	@ResponseBody
	public AjaxJson update(MemberEntity member, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "会员信息更新成功";
		MemberEntity t = memberService.get(MemberEntity.class, member.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(member, t);
			memberService.saveOrUpdate(t);
		} catch (Exception e) {
			e.printStackTrace();
			message = "会员信息更新失败";
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 会员信息新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addPage")
	public ModelAndView addPage(MemberEntity member, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(member.getId())) {
			member = memberService.getEntity(MemberEntity.class, member.getId());
			req.setAttribute("memberPage", member);
		}
		return new ModelAndView("platform/organization/member/memberEdit");
	}
	/**
	 * 会员信息编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "editPage")
	public ModelAndView editPage(MemberEntity member, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(member.getId())) {
			member = memberService.getEntity(MemberEntity.class, member.getId());
			req.setAttribute("memberPage", member);
		}
		return new ModelAndView("platform/organization/member/memberEdit");
	}
	
	/**
	 * 会员注册
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "registered")
	@ResponseBody
	public AjaxJson registered(HttpServletRequest request, HttpServletResponse response){
		AjaxJson j = new AjaxJson();
		String email = request.getParameter("email");
		String loginName = request.getParameter("loginName");
		String loginPassword = request.getParameter("loginPassword");
		String password = PasswordUtil.encrypt(loginName, loginPassword, PasswordUtil.getStaticSalt());
		//存入会员信息表
		MemberEntity member = new MemberEntity();
		member.setLoginName(loginName);
		member.setLoginPassword(password);
		member.setEmail(email);;
		member.setFlag("N");
		member.setAvailable("N");
		member.setIsActived("N");
		memberService.save(member);
		//存入用户表
		TOrgUser tou = new TOrgUser();
		tou.setUserName(member.getLoginName());
		tou.setPassword(member.getLoginPassword());
		tou.setEmail(member.getEmail());
		tou.setUserType("0");
        tou.setUserTypeId(member.getId());
        tou.setFlag("N");//是否删除
        tou.setActive("N");//是否激活
        tou.setLocked("N");//是否锁定
        tou.setCode(member.getCode());
        memberService.save(tou);
        //发送邮件处理
		memberService.processregister(tou,request.getRequestURL().toString());
		j.setMsg("注册成功");
		return j;
	}
	
	/**
	 * 注册后页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "afterRegistered")
	public ModelAndView afterRegistered(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("platform/organization/member/register");
	}
	
	/**
	 * 激活
	 * 
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(params = "activate")
	public ModelAndView activate(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		String sid = StringUtil.decodeFromDes3(request.getParameter("sid"));
		String msg ="激活成功";
		boolean flag = true;
		String[] str = sid.split(",");
		TOrgUser tou = memberService.getTOrgUserEntity(TOrgUser.class, str[0]);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(str[2]);
		long time =(System.currentTimeMillis() -  date.getTime())/3600000;
		if(str.length != 3){
			msg = "链接地址错误";
			flag = false;
		}else if(tou == null){
			msg = "激活地址错误，该账号不存在";
			flag = false;
		}else if(time  > 2){
			msg = "已超时";
			flag = false;
		}else if(StringUtil.equals(tou.getActive(),"1")){
			msg="账号已激活";
			flag= false;
		}
		if(flag){
		tou.setActive("1");
		memberService.saveOrUpdate(tou);
		}
		request.setAttribute("msg", msg);
		return new ModelAndView("platform/organization/member/register");
	}
}
