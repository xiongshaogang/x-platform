package com.xplatform.base.workflow.node.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.orgnaization.orgnaization.service.OrgnaizationService;
import com.xplatform.base.workflow.node.entity.NodeSignEntity;
import com.xplatform.base.workflow.node.entity.NodeSignPrivilegeEntity;
import com.xplatform.base.workflow.node.service.NodeSignService;
import com.xplatform.base.workflow.node.vo.NodeSignPrivilegeVo;

/**
 * 
 * description : 会签规则设置controller
 *
 * @version 1.0
 * @author binyong
 * @createtime : 2014年8月1日
 * 
 *
 */
@Controller
@RequestMapping("/nodeSignController")
public class NodeSignController extends BaseController {

	@Resource
	private NodeSignService nodeSignService;
	
	@Resource
	private OrgnaizationService orgnaizationService;

	private AjaxJson result = new AjaxJson();

	private String message;

	/**
	 * 会签设置页面跳转
	 * 
	 * @author binyong
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "nodeSign")
	public ModelAndView NodeSign(HttpServletRequest request) {
		String actDefId = request.getParameter("actDefId");
		String nodeId = request.getParameter("nodeId");
		List<NodeSignPrivilegeEntity> nodeSignPri = null;
		//投票规则信息
		NodeSignEntity nodeSign = this.nodeSignService
				.getNodeSignByNodeIdAndActDefId(nodeId, actDefId);
		if (nodeSign != null) {
			//特权功能信息
			nodeSignPri = this.nodeSignService.getNodeSignPrivilege(nodeSign);
		}
		return new ModelAndView("workflow/node/nodeSign").addObject("nodeSign",
				nodeSign).addObject("nodeSignPri", JSONArray.fromObject(nodeSignPri));
	}

	/**
	 * 新增或修改节点时间脚本
	 * 
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param NodeSign
	 * @return
	 */
	@RequestMapping(params = "saveNodeSign")
	@ResponseBody
	public AjaxJson saveNodeSign(NodeSignEntity nodeSignEntity,
 NodeSignPrivilegeVo priVo, HttpServletRequest request) {
		try {
			if (StringUtil.isNotEmpty(nodeSignEntity.getId())) {
				nodeSignService.update(nodeSignEntity);
			} else {
				nodeSignService.save(nodeSignEntity);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		//保存特权信息前删除该会签节点所有的特权信息（先删除后保存）
		nodeSignService.deleteNodeSignPrivilegeById(nodeSignEntity.getId());

		if (StringUtils.isNotEmpty(priVo.getPrivilegeUserType0())) {
			NodeSignPrivilegeEntity nspe = new NodeSignPrivilegeEntity();
			nspe.setNodeSignId(nodeSignEntity.getId());
			nspe.setPrivilegeType(priVo.getPrivilegeType0());
			nspe.setPrivilegeUserType(priVo.getPrivilegeUserType0());
			if ("org".equals(priVo.getPrivilegeUserType0())) {
				nspe.setPrivilegeUserIds(priVo.getOrgPrivilegeUserIds0());
				String[] deptIds = priVo.getOrgPrivilegeUserIds0().split(",");
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < deptIds.length; i++) {
					sb.append(this.orgnaizationService.get(deptIds[i]).getName()).append(",");
				}
				nspe.setPrivilegeUserNames(sb.toString().substring(0, sb.toString().length() - 1));
			} else if ("emp".equals(priVo.getPrivilegeUserType0())) {
				nspe.setPrivilegeUserIds(priVo.getEmpPrivilegeUserIds0());
				nspe.setPrivilegeUserNames(priVo.getEmpPrivilegeUserNames0());
			} else if ("job".equals(priVo.getPrivilegeUserType0())) {
				nspe.setPrivilegeUserIds(priVo.getJobPrivilegeUserIds0());
				nspe.setPrivilegeUserNames(priVo.getJobPrivilegeUserNames0());
			} else if ("role".equals(priVo.getPrivilegeUserType0())) {
				nspe.setPrivilegeUserIds(priVo.getRolePrivilegeUserIds0());
				nspe.setPrivilegeUserNames(priVo.getRolePrivilegeUserNames0());
			} else if ("script".equals(priVo.getPrivilegeUserType0())) {
				nspe.setPrivilegeUserIds(priVo.getScriptPrivilegeUserIds0());
				nspe.setPrivilegeUserNames("自定义脚本");
			}
			this.nodeSignService.savePrivilege(nspe);
		}

		if (StringUtils.isNotEmpty(priVo.getPrivilegeUserType1())) {
			NodeSignPrivilegeEntity nspe = new NodeSignPrivilegeEntity();
			nspe.setNodeSignId(nodeSignEntity.getId());
			nspe.setPrivilegeType(priVo.getPrivilegeType1());
			nspe.setPrivilegeUserType(priVo.getPrivilegeUserType1());
			if ("org".equals(priVo.getPrivilegeUserType1())) {
				nspe.setPrivilegeUserIds(priVo.getOrgPrivilegeUserIds1());
				String[] deptIds = priVo.getOrgPrivilegeUserIds1().split(",");
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < deptIds.length; i++) {
					sb.append(this.orgnaizationService.get(deptIds[i]).getName()).append(",");
				}
				nspe.setPrivilegeUserNames(sb.toString().substring(0, sb.toString().length() - 1));
			} else if ("emp".equals(priVo.getPrivilegeUserType1())) {
				nspe.setPrivilegeUserIds(priVo.getEmpPrivilegeUserIds1());
				nspe.setPrivilegeUserNames(priVo.getEmpPrivilegeUserNames1());
			} else if ("job".equals(priVo.getPrivilegeUserType1())) {
				nspe.setPrivilegeUserIds(priVo.getJobPrivilegeUserIds1());
				nspe.setPrivilegeUserNames(priVo.getJobPrivilegeUserNames1());
			} else if ("role".equals(priVo.getPrivilegeUserType1())) {
				nspe.setPrivilegeUserIds(priVo.getRolePrivilegeUserIds1());
				nspe.setPrivilegeUserNames(priVo.getRolePrivilegeUserNames1());
			} else if ("script".equals(priVo.getPrivilegeUserType1())) {
				nspe.setPrivilegeUserIds(priVo.getScriptPrivilegeUserIds1());
				nspe.setPrivilegeUserNames("自定义脚本");
			}
			this.nodeSignService.savePrivilege(nspe);
		}

		if (StringUtils.isNotEmpty(priVo.getPrivilegeUserType2())) {
			NodeSignPrivilegeEntity nspe = new NodeSignPrivilegeEntity();
			nspe.setNodeSignId(nodeSignEntity.getId());
			nspe.setPrivilegeType(priVo.getPrivilegeType2());
			nspe.setPrivilegeUserType(priVo.getPrivilegeUserType2());
			if ("org".equals(priVo.getPrivilegeUserType2())) {
				nspe.setPrivilegeUserIds(priVo.getOrgPrivilegeUserIds2());
				String[] deptIds = priVo.getOrgPrivilegeUserIds2().split(",");
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < deptIds.length; i++) {
					sb.append(this.orgnaizationService.get(deptIds[i]).getName()).append(",");
				}
				nspe.setPrivilegeUserNames(sb.toString().substring(0, sb.toString().length() - 1));
			} else if ("emp".equals(priVo.getPrivilegeUserType2())) {
				nspe.setPrivilegeUserIds(priVo.getEmpPrivilegeUserIds2());
				nspe.setPrivilegeUserNames(priVo.getEmpPrivilegeUserNames2());
			} else if ("job".equals(priVo.getPrivilegeUserType2())) {
				nspe.setPrivilegeUserIds(priVo.getJobPrivilegeUserIds2());
				nspe.setPrivilegeUserNames(priVo.getJobPrivilegeUserNames2());
			} else if ("role".equals(priVo.getPrivilegeUserType2())) {
				nspe.setPrivilegeUserIds(priVo.getRolePrivilegeUserIds2());
				nspe.setPrivilegeUserNames(priVo.getRolePrivilegeUserNames2());
			} else if ("script".equals(priVo.getPrivilegeUserType2())) {
				nspe.setPrivilegeUserIds(priVo.getScriptPrivilegeUserIds2());
				nspe.setPrivilegeUserNames("自定义脚本");
			}
			this.nodeSignService.savePrivilege(nspe);
		}

		if (StringUtils.isNotEmpty(priVo.getPrivilegeUserType3())) {
			NodeSignPrivilegeEntity nspe = new NodeSignPrivilegeEntity();
			nspe.setNodeSignId(nodeSignEntity.getId());
			nspe.setPrivilegeType(priVo.getPrivilegeType3());
			nspe.setPrivilegeUserType(priVo.getPrivilegeUserType3());
			if ("org".equals(priVo.getPrivilegeUserType3())) {
				nspe.setPrivilegeUserIds(priVo.getOrgPrivilegeUserIds3());
				String[] deptIds = priVo.getOrgPrivilegeUserIds3().split(",");
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < deptIds.length; i++) {
					sb.append(this.orgnaizationService.get(deptIds[i]).getName()).append(",");
				}
				nspe.setPrivilegeUserNames(sb.toString().substring(0, sb.toString().length() - 1));
			} else if ("emp".equals(priVo.getPrivilegeUserType3())) {
				nspe.setPrivilegeUserIds(priVo.getEmpPrivilegeUserIds3());
				nspe.setPrivilegeUserNames(priVo.getEmpPrivilegeUserNames3());
			} else if ("job".equals(priVo.getPrivilegeUserType3())) {
				nspe.setPrivilegeUserIds(priVo.getJobPrivilegeUserIds3());
				nspe.setPrivilegeUserNames(priVo.getJobPrivilegeUserNames3());
			} else if ("role".equals(priVo.getPrivilegeUserType3())) {
				nspe.setPrivilegeUserIds(priVo.getRolePrivilegeUserIds3());
				nspe.setPrivilegeUserNames(priVo.getRolePrivilegeUserNames3());
			} else if ("script".equals(priVo.getPrivilegeUserType3())) {
				nspe.setPrivilegeUserIds(priVo.getScriptPrivilegeUserIds3());
				nspe.setPrivilegeUserNames("自定义脚本");
			}
			this.nodeSignService.savePrivilege(nspe);
		}

		message = "保存成功";
		return result;
	}

	public void setNodeSignService(NodeSignService nodeSignService) {
		this.nodeSignService = nodeSignService;
	}

	public void setOrgnaizationService(OrgnaizationService orgnaizationService) {
		this.orgnaizationService = orgnaizationService;
	}

}
