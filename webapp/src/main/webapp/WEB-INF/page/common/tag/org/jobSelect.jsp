<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	var jobArray = [];
	var job;
	var jobMultipleSelect = ${multiples};
	var afterSaveClose=${afterSaveClose};
	<c:forEach var="job" items="${jobList}">
	job = {
		id : '${job.id}',
		name : '${job.name}'
	};
	jobArray.push(job);
	</c:forEach>
	$(function(){
		//加载已选项
		$.each(jobArray, function(i, ele) {
			addSelectedHtml(ele.id, ele.name);
		});
	});
	
	//添加已选Html
	function addSelectedHtml(id, name) {
		var spanContent = '<span class="viewdelete_span" id="'+ id +'"><label style="width:72%;cursor: default;" title="'
				+ name + '">' + name + '</label><a title="删除" href="#1" style="float: right;" onclick="removeJob(\''
				+ id + '\');"><i class="awsm-icon-remove red"></i></a></span>';
		$("#empJobListSel").append(spanContent);
	}
	
	//添加已选数据(至数组里)
	function addSelectedData(id, name){
		var adds = {};
		adds.id = id;
		adds.name = name;
		jobArray.push(adds);
	}
	
	function addJob() {
		var rows = $("#jobListSel").datagrid('getSelections');
		if ((!jobMultipleSelect) && (rows.length > 1 || jobArray.length > 0)) {
			tip("只能选择一个岗位");
		} else {
			for (var i = 0; i < rows.length; i++) {
				var jobId = rows[i].id;
				var jobName = rows[i].name;
				var isContained = false;
				//如果还没有被选中,则加入到右侧已选
				$.each(jobArray, function(i, ele) {
					if (ele.id == jobId) {
						isContained = true;
					}
				});

				if (!isContained) {
					addSelectedHtml(jobId,jobName);
					addSelectedData(jobId,jobName);
				}
			}
		}
	}

	function finishSelectJob(ele) {
		if ((!jobMultipleSelect) && jobArray.length > 1) {
			tip("只能选择一个岗位");
		} else {
			var ids = "";
			var names = "";
			$.each(jobArray, function(i, ele) {
				ids += ele.id + ",";
				names += ele.name + ",";
			});
			ids = ids.removeDot();
			names = names.removeDot();
			$("#${hiddenId}").val(ids);
			$("#${displayId}").val(names);
			$("#${hiddenId}").blur();
			closeD(getD($(ele)));
		}
	}

	//删除触发方法
	function removeJob(jobId) {
		jobArray = $.grep(jobArray, function(ele, i) {
			return ele.id != jobId;
		});
		$("#empJobListSel span").filter("#" + jobId).remove();
	}
	
	//直接保存方法
	function saveJob(ele){
		if ((!jobMultipleSelect) && jobArray.length > 1) {
			tip("只能选择一项");
		} else {
			var ids = "";
			var names = "";
			$.each(jobArray, function(i, ele) {
				ids += ele.id + ",";
				names += ele.name + ",";
			});
			ids = ids.removeDot();
			names = names.removeDot();
			var subUrl="${saveUrl}"+"&ids="+ids+"&names="+names;
			doSubmit(subUrl);
			if(afterSaveClose){
				closeD(getD($(ele)));
			}
		}
	}
</script>
<div class="easyui-layout" style="width: 98%; height: 100%; margin: 0px auto 0px auto">
	<div region="center" style="padding: 0px; border: 0px">
		<t:datagrid name="jobListSel" checkbox="true" fit="true" fitColumns="true" title="岗位列表"
			actionUrl="jobController.do?datagrid">
			<t:dgCol title="主键" field="id" hidden="false"></t:dgCol>
			<t:dgCol title="岗位编码" field="code" query="true" width="200"></t:dgCol>
			<t:dgCol title="岗位名称" field="name" query="true" width="200"></t:dgCol>
			<t:dgCol title="岗位英文名" field="enName" width="200"></t:dgCol>
			<t:dgCol title="岗位简称" field="shortName" width="200"></t:dgCol>
			<t:dgToolBar id="job_gridAdd" title="加入已选" icon="awsm-icon-plus blue" onclick="addJob()"></t:dgToolBar>
			<c:if test="${needBtnSelected}">
			<t:dgToolBar id="finishSelectJob" title="确定" icon="awsm-icon-ok-sign green" onclick="finishSelectJob(this)"></t:dgToolBar>
			</c:if>
			<c:if test="${needBtnSave}">
			<t:dgToolBar id="saveJob" title="保存" icon="awsm-icon-save green" onclick="saveJob(this)"></t:dgToolBar>
			</c:if>
		</t:datagrid>
	</div>
	<div id="empJobListSel" title="已选岗位" data-options="split:true" region="east" style="width: 150px; text-align: center;">
	</div>
</div>
