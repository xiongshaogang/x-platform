<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	var commonArray = [];
	var obj;
	var backObj;
	var hiddenMapperField;
	var displayMapperField;
	var singleSelectBackup; //如果是单选,并且要返回除id,name外另外的字段,需要判断是否改变了进页面时的值
	var empExpandAll = ${expandAll};
	var commonMultipleSelect = ${multiples};
	var gridId="${gridId}";
	var index="${index}";
	var backMap=new Map();
	<c:forEach var="item" items="${idNameMap}">
	obj = {
		${hiddenField} : '${item.key}',
		${displayField} : '${item.value}'
	};
	commonArray.push(obj);
	</c:forEach>
	
	<c:forEach var="json" items="${gridFieldJsonList}">
		<c:if test="${json.backField!=null}" >
		backMap.put("${json.field}","${json.backField}");
		</c:if>
		<c:if test="${json.backField==hiddenField}" >
		hiddenMapperField='${json.field}';
		</c:if>
		<c:if test="${json.backField==displayField}" >
		displayMapperField='${json.field}';
		</c:if>
	</c:forEach>
	singleSelectBackup=commonArray[0];
	function addItem() {
		var rows = $("#${displayField}ListSel").datagrid('getSelections');
		if ((!commonMultipleSelect) && (rows.length > 1 || commonArray.length > 0)) {
			tip("只能选择一项");
		} else {
			for (var i = 0; i < rows.length; i++) {
				var itemId = rows[i][hiddenMapperField];
				var itemName = rows[i][displayMapperField];
				var isContained = false;
				//如果还没有被选中,则加入到右侧已选
				$.each(commonArray, function(i, ele) {
					if (ele.${hiddenField} == itemId) {
						isContained = true;
					}
				});
				if (!isContained) {
					var spanContent = '<span class="viewdelete_span" id="'+ itemId +'">' + itemName
							+ '<a href="#1" style="float: right;" onclick="removeItem(\'' + itemId
							+ '\');"><i class="awsm-icon-remove red"></i></a></span>';
					$("#${displayField}SelectedList").append(spanContent);
					var addsItem = {};
					for(var j=0;j<backMap.size();j++){
						var ele=backMap.element(j);
						addsItem[ele.value]=rows[i][ele.key];
					}
					commonArray.push(addsItem);
				}
			}
		}
	}

	//根据列名获取其单选或多选值
	function getMulValue(field){
		var mulValue="";
		$.each(commonArray,function(i, ele) {
			mulValue += ele[field] + ",";
		});
		return mulValue.removeDot();
	}
	
	function finishSelectItem(button) {
		if ((!commonMultipleSelect) && commonArray.length > 1) {
			tip("只能选择一项");
		} else {
			if(commonArray.length>0){
				if(singleSelectBackup==undefined||singleSelectBackup.${hiddenField}!=commonArray[0].${hiddenField}){
					for(var j=0;j<backMap.size();j++){
						var ele=backMap.element(j);
						var backValue=getMulValue(ele.value);
						$("#"+gridId).datagrid("setEditorFieldValue",{index:index,field:ele.value,value:backValue});
					}
				}
			}else{
				for(var j=0;j<backMap.size();j++){
					var ele=backMap.element(j);
					$("#"+gridId).datagrid("setEditorFieldValue",{index:index,field:ele.value,value:""});
				}
			}
			${callback};
			closeD(getD($(button)));
		}
	}

	//删除触发方法
	function removeItem(itemId) {
		commonArray = $.grep(commonArray, function(ele, i) {
			return ele.${hiddenField} != itemId;
		});
		$("#${displayField}SelectedList span").filter("#" + itemId).remove();
	}
	
	//打开页面时是否异步加载出所有节点
	function treeOnLoadSuccess(){
		if(empExpandAll){
			$("#${displayField}TreeSel").tree("expandAll");
		}
	}
	
	//返回无树id过滤时的数据
	function backToAll(){
		var queryParams=$("#${displayField}ListSel").datagrid("options").queryParams;
		delete queryParams.${gridTreeFilter};
		$("#${displayField}ListSel").datagrid("load");
	}
</script>
<div class="easyui-layout" style="width: 98%; height: 100%; margin: 0px auto 0px auto">
	<c:if test="${hasTree}">
		<div region="west" style="width: 150px" title="树型过滤">
			<span onclick="backToAll()" style="cursor: pointer; color: #69aa46; margin-left: 5px;"><i
				class="awsm-icon-home bigger-110">全部</i></span>
			<t:tree id="${displayField}TreeSel" url="${treeUrl}" gridId="${displayField}ListSel"
				gridTreeFilter="${gridTreeFilter}" onLoadSuccess="treeOnLoadSuccess()"></t:tree>
		</div>
	</c:if>
	<div region="center" style="padding: 0px; border: 0px">
		<t:datagrid name="${displayField}ListSel" checkbox="true" fit="true" fitColumns="true" title="选择列表" actionUrl="${url}">
			<c:forEach var="json" items="${gridFieldJsonList}">
				<t:dgCol title="${json.title}" width="${json.width}" field="${json.field}" hidden="${json.hidden}"
					query="${json.query}" queryMode="${json.queryMode}" queryInputType="${json.queryInputType}"
					queryExParams="${json.queryExParams}"></t:dgCol>
			</c:forEach>
			<t:dgToolBar title="加入已选" icon="awsm-icon-plus blue"
				onclick="addItem()"></t:dgToolBar>
			<t:dgToolBar title="确定" icon="awsm-icon-ok-sign green"
				onclick="finishSelectItem(this)"></t:dgToolBar>
		</t:datagrid>
	</div>
	<div id="${displayField}SelectedList" title="已选" region="east" style="width: 170px; text-align: center;">
		<c:forEach var="item" items="${idNameMap}">
			<span id="${item.key}" class="viewdelete_span">${item.value}<a style="float: right;" href="#1"
				onclick="removeItem('${item.key}');"><i class="awsm-icon-remove red"></i></a></span>
		</c:forEach>
	</div>
</div>
