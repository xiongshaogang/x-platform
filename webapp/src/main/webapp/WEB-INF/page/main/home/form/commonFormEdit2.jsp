<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />

<script type="text/javascript">
window.jQuery || document.write(
	'<link href="basic/css/bootstrap.min.css" rel="stylesheet" \/>'
	+'<link href="basic/css/font-awesome.min.css" rel="stylesheet" \/>'
	+'<link href="basic/css/weather-icons.min.css" rel="stylesheet" \/>'
	+'<link href="basic/css/bootstrap-switch.min.css" rel="stylesheet" \/>'
	//+'<link href="basic/css/mobiscroll.custom-2.17.0.min.css" rel="stylesheet" \/>'
	+'<link href="plug-in/jquery-fileupload/css/jquery.fileupload-ui.css" rel="stylesheet" \/>'
	+'<link href="plug-in/jquery-fileupload/css/jquery.fileupload.css" rel="stylesheet" \/>'
	+'<link href="basic/css/common.css" rel="stylesheet" \/>'
	+'<link href="basic/css/index.css" rel="stylesheet" \/>'
	+'<link href="basic/css/home.css" rel="stylesheet" \/>'
	+'<link rel="stylesheet" type="text/css" href="plug-in/uploadifive-v1.2.2-standard/uploadifive.css" \/>'
	//+'<link href="basic/css/mobiscroll.custom-2.17.0.min.css" rel="stylesheet" \/>'
);
</script>

<link href="basic/css/mobiscroll.custom-2.17.0.min.css" rel="stylesheet" />

<div class="common-form">
	
	<button id="selectPerson" onclick="selectPerson.select()">选人</button>
	<button id="selectAttachment" onclick="attachment.select()">附件</button>
	<button id="phoneCall" onclick="phone.call(13428951050)">拨通电话</button>
	<button id="getAddress" onclick="gps.getInfo()">获取地址</button>
	<button id="getAddress" onclick="simpleCMD.back()">回退</button>

	
	<div class="common-form-container">
		<div class="common-form-content">
			<input type="text" name="id" value="m1"/>
			<input type="text" name="mainTableName" value="t_auto_m"/>
			<div class="form-field-box form-radio clearfix" data-cid="c101" data-index="1">
				<label>省份：</label>
				<select id="province">
				</select>
				<i class="fa fa-angle-right"></i>
			</div>
			<div class="form-field-box form-radio clearfix" data-cid="c102" data-index="2">
				<label>市：</label>
				<select id="city">
				</select>
				<i class="fa fa-angle-right"></i>
			</div>
			<div class="form-field-box form-radio clearfix" data-cid="c103" data-index="3">
				<label>区：</label>
				<select id="country">
				</select>
				<i class="fa fa-angle-right"></i>
			</div>
			<div class="form-field-box form-text clearfix">
				<label>附件：</label>
				<i class="fa fa-plus btn-add" flag="addAttachBtn"></i>
				<input id="contract" type="file" name="contract" flag="aaa"/>
			</div>
			<div class="form-field-box form-text clearfix">
				<label>单行输入：</label>
				<input type="text" placeholder="请输入内容"/>
				<a id="uploadBtn" style="display:inline">123</a>
			</div>
			
			<div class="form-field-box form-paragraph clearfix">
				<label>多行输入：</label>
				<textarea placeholder="请输入内容"></textarea>
			</div>
			<div class="form-field-box form-number clearfix">
				<label>数字：</label>
				<input type="text" placeholder="请输入数字"/>
			</div>
			<div class="form-field-box form-website clearfix">
				<label>网址：</label>
				<input type="text" placeholder="http://"/>
			</div>
			<div class="form-field-box form-email clearfix">
				<label>邮箱：</label>
				<input type="text" placeholder="example@mail.com"/>
			</div>
			<div class="form-field-box form-idcard clearfix">
				<label>身份证号：</label>
				<input type="text" placeholder="请输入身份证号"/>
			</div>
			<div class="form-field-box form-date clearfix">
				<label>日期：</label>
				<input type="text"  />
				<i class="fa fa-angle-right"></i>
			</div>
			<div class="form-field-box form-time clearfix">
				<label>时间：</label>
				<input type="text"  />
				<i class="fa fa-angle-right"></i>
			</div>
			<div class="form-field-box form-radio clearfix">
				<label>单选框：</label>
				<select>
					<option value="1">11111</option>
					<option value="2">222222</option>
					<option value="3" selected="selected">33</option>
					<option value="4">4444444444</option>
				</select>
				<i class="fa fa-angle-right"></i>
			</div>
			<div class="form-field-box form-checkboxes clearfix">
				<label>多选框：</label>
				<select multiple>
					<option value="1" >11111</option>
					<option value="2" selected="selected">222222</option>
					<option value="3" selected="selected">33</option>
					<option value="4">4444444444</option>
				</select>
				<i class="fa fa-angle-right"></i>
			</div>
			<div class="form-field-box form-selectuser clearfix">
				<label>选择用户：</label>
				<input type="text"  />
				<i class="fa fa-angle-right"></i>
			</div>
			<div class="form-field-box form-detail clearfix" data-cid="c5" data-tablename="t_auto_a">
				<input id="a[0].id" type="text" name="a[0].id" flag="subId" value="a1"/>
				<div class="detail-top">
					<label>报销<span class="detailIndex">(1)</span>：</label>
					<i class="fa fa-trash-o delete hidden"></i>
				</div>
				<div class="detail-content">
					<div class="form-field-box form-email clearfix">
						<label>邮箱：</label>
						<input name="a[0].1" type="text" placeholder="example@mail.com"/>
					</div>
					<div class="form-field-box form-idcard clearfix">
						<label>身份证号：</label>
						<input name="a[0].2" type="text" placeholder="请输入身份证号"/>
					</div>
					<div class="form-field-box form-date clearfix">
						<label>日期：</label>
						<input type="text" id="date1" class="mobiEle"/>
						<i class="fa fa-angle-right"></i>
					</div>
					<div class="form-field-box form-text clearfix">
						<label>附件：</label>
						<i class="fa fa-plus btn-add" flag="addAttachBtn"></i>
						<input type="file" />
					</div>
				</div>
			</div>
			<div class="form-field-box form-detail clearfix" data-cid="c5" data-tableName="t_auto_a">
				<input id="a[1].id" type="text" name="a[1].id" flag="subId" value="a2"/>
				<div class="detail-top">
					<label>报销<span class="detailIndex">(2)</span>：</label>
					<i class="fa fa-trash-o delete"></i>
				</div>
				<div class="detail-content">
					<div class="form-field-box form-email clearfix">
						<label>邮箱：</label>
						<input name="a[1].1" type="text" placeholder="example@mail.com"/>
					</div>
					<div class="form-field-box form-idcard clearfix">
						<label>身份证号：</label>
						<input name="a[1].2" type="text" placeholder="请输入身份证号"/>
					</div>
					<div class="form-field-box form-date clearfix">
						<label>日期：</label>
						<input type="text" id="date2" class="mobiEle"/>
						<i class="fa fa-angle-right"></i>
					</div>
					<div class="form-field-box form-text clearfix" >
						<label>附件：</label>
						<i class="fa fa-plus btn-add" flag="addAttachBtn"></i>
						<input id="c" type="file" />
					</div>
				</div>
				<div class="detail-bottom">
					<a class="clone"><i class="fa fa-plus"></i>增加明细</a>
				</div>
			</div>
			<div class="form-field-box form-detail clearfix" data-cid="c10" data-tableName="t_auto_b">
				<input id="b[0].id" type="text" name="b[0].id" flag="subId" value="b1"/>
				<div class="detail-top">
					<label>请假<span class="detailIndex">(1)</span>：</label>
					<i class="fa fa-trash-o delete hidden"></i>
				</div>
				<div class="detail-content">
					<div class="form-field-box form-time clearfix">
						<label>时间：</label>
						<input name="b[1].2" type="text" id="time1" class="mobiEle"/>
						<i class="fa fa-angle-right"></i>
					</div>
					<div class="form-field-box form-radio clearfix">
						<label>单选框：</label>
						<select id="radio1" class="mobiEle">
							<option value="1">11111</option>
							<option value="2">222222</option>
							<option value="3">33</option>
							<option value="4">4444444444</option>
						</select>
						<i class="fa fa-angle-right"></i>
					</div>
					<div class="form-field-box form-paragraph clearfix">
						<label>多行输入：</label>
						<textarea placeholder="请输入内容"></textarea>
					</div>
					<div class="form-field-box form-text clearfix">
						<label>附件：</label>
						<i class="fa fa-plus btn-add" flag="addAttachBtn"></i>
						<input id="b[0].file" type="file" />
					</div>
				</div>
				<div class="detail-bottom">
					<a class="clone"><i class="fa fa-plus"></i>增加明细</a>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 按钮 -->
	<div>
		<c:out value="${buttonType}"></c:out>
		<c:out value="${taskId}"></c:out>
		<c:if test="${buttonType=='startFlow'}">
			<input type="button" onclick="execute('startFlow')" value="提交"/>
		</c:if>
		<c:if test="${buttonType=='nextProcess'}">
			<form id="approveForm" action="">
				<textarea id="voteContent" name="voteContent">${opinion}</textarea>
				<input type="hidden" id="tranPerson"/>
				<input type="button" onclick="execute('nextProcess','1')" value="同意"/>
				<input type="button" onclick="execute('nextProcess','2')" value="反对"/>
				<input type="button" onclick="commonFormEdit.selectTran()" value="转交"/>
			</form>
		</c:if>
		<c:if test="${buttonType=='save'}">
			<input type="button" onclick="execute('save')" value="保存"/>
		</c:if>
	</div>
</div>

<script type="text/javascript">
var miboContaxt = "body";
if(window.jQuery){
	miboContaxt = "#commonFormEdit";
}

window.jQuery || document.write(
	'<script type="text/javascript" src="basic/js/html5.js"><\/script>'
	+ '<script type="text/javascript" src="basic/js/jquery-2.0.3.min.js"><\/script>'
	+ '<script type="text/javascript" src="basic/js/jquery-migrate-1.1.0.js"><\/script>'
	+ '<script type="text/javascript" src="basic/js/bootstrap.min.js"><\/script>'
	+ '<script type="text/javascript" src="basic/js/toastr/toastr.js"><\/script>'
	+ '<script type="text/javascript" src="basic/js/jquery.slimscroll.min.js"><\/script>'
	//+ '<script type="text/javascript" src="basic/js/mobiscroll-2.14.4-crack.js"><\/script>'
	+ '<script type="text/javascript" src="basic/js/bootstrap-switch.min.js"><\/script>'
	+ '<script type="text/javascript" src="plug-in/tools/syUtil.js"><\/script>'
	+ '<script type="text/javascript" src="plug-in/tools/curdtools.js"><\/script>'
	+ '<script type="text/javascript" src="basic/js/home.js"><\/script>'
	+ '<script type="text/javascript" src="plug-in/workflow/js/approveButton.js"><\/script>'
	+ '<script type="text/javascript" src="plug-in/uploadifive-v1.2.2-standard/jquery.uploadifive.js" ><\/script>'
	//+ '<script type="text/javascript" src="basic/js/mobiscroll.custom-2.17.0.min.js"><\/script>'
);
</script>

<script type="text/javascript" src="basic/js/mobiscroll.custom-2.17.0.min.js"></script>

<script type="text/javascript">
var commonFormEdit = {
	loadMobi: function(detailGroup){
		if(!detailGroup) detailGroup = $("body");
		
		detailGroup.find(".form-detail .mobiEle").prev("input").remove();
		
		detailGroup.find(".form-date input").mobiscroll().date({
			lang: 'zh',
	        theme: 'mobiscroll',
	        dateFormat: 'yyyy-mm-dd',
	        display: 'bottom',
	        context: miboContaxt
	    });
		detailGroup.find(".form-time input").mobiscroll().time({
			lang: 'zh',
	        theme: 'mobiscroll',
	        display: 'bottom',
	        context: miboContaxt
	    });
		detailGroup.find(".form-radio select").mobiscroll().select({
			lang: 'zh',
	        theme: 'mobiscroll',
	        display: 'bottom',
	        minWidth: 200,
	        context: miboContaxt
	    });
		detailGroup.find(".form-checkboxes select").mobiscroll().select({
			lang: 'zh',
	        theme: 'mobiscroll',
	        display: 'bottom',
	        minWidth: 200,
	        context: miboContaxt
	    });
	},
	controllerCenter:function(cmd, params) {
		if (navigator.userAgent.toLowerCase().indexOf('iphone') > 0) {
			document.location = "http://WEBVIEW_ENGINE:" + cmd + ":" + params;
		} else if (navigator.userAgent.indexOf('Android') > 0) {
			eval('javascript:WEBVIEW_ENGINE'+'.' + cmd + '("' + params + '")');
		} else {
			alert("Can't use this feature");
		}
	},
	updateDetailIndex: function(detailGroud){
		detailGroud.find(".detailIndex").each(function(i){
			$(this).text("("+(i+1)+")")
		});
		
		detailGroud.each(function(i){
			$(':input, select', this).each(function(){
				var $this = $(this), name = $this.attr('name');
				if(name!=null){
					var s = name.indexOf("[");
					var e = name.indexOf("]");
					var new_name = name.substring(s+1,e);
					$this.attr("name",name.replace(new_name,i));
					$this.attr("id",$this.attr("name"));
				}
			});
		});
	},
	selectTran:function(){
		var voteContent=$("#voteContent").val();
		if(!voteContent||voteContent==""){
			if(!confirm("您还未输入意见,是否继续提交?")){
				return false;
			}	
		}
		selectPerson.select('{callbackKey:"tran",multiple:false}');
	}
};

var attachment={
	"select":function(businessKey,businessType,businessExtra){
		var params='{businessKey:"'+businessKey+'",businessType:"'+businessType+'",businessExtra:"'+businessExtra+'"}';
		commonFormEdit.controllerCenter('selectAttachment',params);
	},
	"download":function(aId){
		var ext='txt';
		var filename='文本文件(1)';
		var params='{aId:"'+aId+'",ext:"'+ext+'",filename:"'+filename+'"}';
		commonFormEdit.controllerCenter('downloadAttachment',params);
	},
	"select_callback":function(params){
	}
};

var gps={
	"getInfo":function(params){
		commonFormEdit.controllerCenter('getGPSInfo',params);
	},
	"getInfo_callback":function(params){
		console.info(params);
		alert(params);
	}
};

var selectPerson={
	"select":function(params){
		commonFormEdit.controllerCenter('selectPerson',params);
	},
	"select_callback":function(params){
		var data=parseJSON(params);
		if(data.callbackKey){
			if(data.callbackKey=="tran"){
				var ids="";
				$(data.selected).each(function(i){
					ids+=this.id+",";
				});
				$("#tranPerson").val(ids.removeDot());
				execute("tran");
			}
		}
	}
};

var phone={
	"call":function(params){
		commonFormEdit.controllerCenter('callPhone',params);
	}
};

var simpleCMD={
	"back":function(params){
		commonFormEdit.controllerCenter('back',params);
	}	
}


$(function(){
	if(miboContaxt == "body"){
		$(".common-form .common-form-container").css("height", "auto");
	}else{
		home.loadSlimScroll([ {
			obj : $('.common-form-content')
		}]);
	}
	
	$("#uploadBtn").on("click",function(){
		var data = {
			"businessExtra" : "c12",
			"businessKey" : "1",
			"businessType" : "t_auto_abc"
		};
		$('#contract').data('uploadifive').settings.formData = data;
		$('#contract').uploadifive("upload");
	});
	$('[flag=addAttachBtn]').on("click",function(){
		var $thisDetail = $(this).closest(".form-detail");
		var id,tableName,cid;
		if($thisDetail[0]){
			//说明是从表附件
			id=$("[flag=subId]",$thisDetail).val();
			tableName=$thisDetail.data("tablename");
			cid=$thisDetail.data("cid");
		}else{
			//说明是从表附件
			id=$("[name=id]").val();
			tableName=$("[name=mainTableName]").val();
			cid="";
		}
		debugger;
		attachment.select(id,cid,tableName);
	});
	
	$('[type=file]').each(function(i,e){
		var $thisDetail = $(this).closest(".form-detail");
		var id,tableName,cid;
		if($thisDetail[0]){
			//说明是从表附件
			id=$("[flag=subId]",$thisDetail).val();
			tableName=$thisDetail.data("tablename");
			cid=$thisDetail.data("cid");
		}else{
			//说明是从表附件
			id=$("[name=id]").val();
			tableName=$("[name=mainTableName]").val();
			cid="";
		}
		var data = {
				"businessExtra" : cid,
				"businessKey" : id,
				"businessType" : tableName
		};
		$(this).uploadifive({
			'fileObjName' : id+cid,
			'onUploadComplete' : function(file, data) {
				var data = parseJSON(data);
		        if(data.success){
		        	
		        }
		    },
		    'formData':data,
			'auto': true,  
			'buttonText': "选择文件",  
			'onUpload' : function(filesToUpload) {
				if(filesToUpload == 0){
					alert("onUpload");
				}
		    },
			'uploadScript' : 'attachController.do?uploadFiles'
		});
	});
	
	$("#province").mobiscroll().select({
		lang: 'zh',
        theme: 'mobiscroll',
        display: 'bottom',
        minWidth: 200,
        context: miboContaxt
    });
	var areaLi="<option value='-1'>请选择</option>";
	$("#province").empty();
	$("#province").append(areaLi);
// 	ajax("areasController.do?queryAreas",function(data){
// 		$.each(data.obj,function(i,item){
// 			areaLi="<option value='"+item.areaName+"'>请选择</option>";
// 			$("#province").append(areaLi);
// 		});
// 	});
	
	$("#province").on("changed",function(){
		var eleIndex;
		//清空子孙值
		if(eleIndex>ownIndex){
			$(ele).combobox("clear"); //清空后续输入框的值
		}
		//启用子要素,刷新子要素
		if(eleIndex==ownIndex+1){
			$(ele).combobox("enable"); //清空后续输入框的值
			$(ele).combobox("reload");
		}
		//禁用孙要素
		if(eleIndex>ownIndex+1){
			$(ele).combobox("disable"); //将下一个节点之后的所有节点设置成不可选择
		}  
	});
	
	
	$(".form-detail i.delete").on("click", function(){
		var $thisDetail = $(this).closest(".form-detail"),
			$thisDetailCid = $thisDetail.attr("data-cid"),
			$detailBottomClone = $(".detail-bottom:last").clone(true);
		
		$thisDetail.remove();
		
		var $thisDetailGroup = $(".common-form-content > .form-detail[data-cid='"+$thisDetailCid+"']");
		$thisDetailGroup.find(".detail-bottom").remove();
		$detailBottomClone.appendTo($thisDetailGroup.last());
		
		commonFormEdit.updateDetailIndex($thisDetailGroup);
	});
	$(".form-detail a.clone").on("click", function(){
		var $thisDetail = $(this).closest(".form-detail"),
			$thisDetailCid = $thisDetail.attr("data-cid"),
			$thisDetailClone = $thisDetail.clone(true), 
			$detailBottomClone = $thisDetail.children(".detail-bottom").clone(true),
			$thisDetailGroup = $(".common-form-content > .form-detail[data-cid='"+$thisDetailCid+"']");
		
		$thisDetailClone.find("i.delete").removeClass("hidden");
		$thisDetailClone.find("input, textarea").val("");
		$thisDetailClone.insertAfter($thisDetail);
		$thisDetailGroup = $(".common-form-content > .form-detail[data-cid='"+$thisDetailCid+"']");
		$thisDetailGroup.find(".detail-bottom").remove();
		$detailBottomClone.appendTo($thisDetailGroup.last());
		
		commonFormEdit.updateDetailIndex($thisDetailGroup);
		commonFormEdit.loadMobi($thisDetailGroup);
		
	});
	
// 	commonFormEdit.loadMobi();
});
function appendAttachment(data){
}
function execute(type,voteAgree){
	//1.先保存数据
	//2.处理流程
	var taskId="${taskId}";
	var voteContent=$("#voteContent").val();
	if(type=="nextProcess"){
		if(!voteContent||voteContent==""){
			if(!confirm("您还未输入意见,是否继续提交?")){
				return false;
			}	
		}
		$.ajax({
			cache : false,
			type : 'POST',
			url : "taskController.do?complete&taskId=${taskId}&voteAgree=" + voteAgree,
			data : $("#approveForm").serialize(),
			async : false,
			success : function(data) {
				var d = parseJSON(data);
				if (d.success) {
					simpleCMD.back();
				}
			}
		});
	}else if(type=="tran"){
		var tranPerson=$("#tranPerson").val();
		var description=$("#voteContent").val();
		$.ajax({
			cache : false,
			type : 'POST',
			url : "taskExeController.do?assignSave&taskId=${taskId}&exeUserId="+tranPerson+"&description="+description,
			async : false,
			success : function(data) {
				var d = parseJSON(data);
				if (d.success) {
					simpleCMD.back();
				}
			}
		});
	}else if(type=="startFlow"){
		if(!voteContent||voteContent==""){
			if(!confirm("您还未输入意见,是否继续提交?")){
				return false;
			}	
		}
		$.ajax({
 			cache : false,
 			type : 'POST',
 			url : "taskController.do?startFlow&actDefId=${actDefId}&businessKey=${businessKey}&businessName=${businessName}",
 			async : false,
 			success : function(data) {
 				var d = parseJSON(data);
 				if (d.success) {
 					var msg = d.msg;
 					tip(msg);
 				}
 			}
 		});
	}
}

</script>