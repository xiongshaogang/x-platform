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
	var callback="${callback}";
	var idOrName="${idOrName}";
	var backMap=new Map(); //key为弹出页grid中的要返回的列,value为返回到页面上的input控件
	
	<c:forEach var="item" items="${idNameMap}">
	<c:choose>
		<c:when test="${idOrName=='id'}">
			obj = {
				${hiddenId} : '${item.key}',
				${displayId} : '${item.value}'
			};
		</c:when>
		<c:otherwise>
			obj = {
				${hiddenName} : '${item.key}',
				${displayName} : '${item.value}'
			};
		</c:otherwise>
	</c:choose>
	
	commonArray.push(obj);
	</c:forEach>
	
	<c:forEach var="json" items="${gridFieldJsonList}">
		<c:if test="${json.backField!=null}" >
		backMap.put("${json.field}","${json.backField}");
		</c:if>
		
		<c:choose>
			<c:when test="${idOrName=='id'}">
				<c:if test="${json.backField==hiddenId}" >
				hiddenMapperField='${json.field}';
				</c:if>
			</c:when>
			<c:otherwise>
				<c:if test="${json.backField==hiddenName}" >
				hiddenMapperField='${json.field}';
				</c:if>
			</c:otherwise>
		</c:choose>
		
		<c:choose>
		<c:when test="${idOrName=='id'}">
			<c:if test="${json.backField==displayId}" >
			displayMapperField='${json.field}';
			</c:if>
		</c:when>
		<c:otherwise>
			<c:if test="${json.backField==displayName}" >
			displayMapperField='${json.field}';
			</c:if>
		</c:otherwise>
		</c:choose>
	</c:forEach>
	singleSelectBackup=commonArray[0];
	function addItem() {
		var rows = $("#${displayName}ListSel").datagrid('getSelections');
		if ((!commonMultipleSelect) && (rows.length > 1 || commonArray.length > 0)) {
			tip("只能选择一项");
		} else {
			for (var i = 0; i < rows.length; i++) {
				var itemId = rows[i][hiddenMapperField];
				var itemName = rows[i][displayMapperField];
				var isContained = false;
				//如果还没有被选中,则加入到右侧已选
				$.each(commonArray, function(i, ele) {
					if (ele.${hiddenName} == itemId) {
						isContained = true;
					}
				});
				if (!isContained) {
					var spanContent = '<span class="viewdelete_span" id="'+ itemId +'">' + itemName
							+ '<a href="#1" style="float: right;" onclick="removeItem(\'' + itemId
							+ '\');"><i class="awsm-icon-remove red"></i></a></span>';
					$("#${displayName}SelectedList").append(spanContent);
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
				var oldSel;
				var nowSel;
				if(singleSelectBackup!=undefined){
					if(idOrName=="id"){
						oldSel=singleSelectBackup.${hiddenId};
						nowSel=commonArray[0].${hiddenId};
					}else{
						oldSel=singleSelectBackup.${hiddenName};
						nowSel=commonArray[0].${hiddenName};
					}
				}
				if(singleSelectBackup==undefined||oldSel!=nowSel){
					for(var j=0;j<backMap.size();j++){
						var ele=backMap.element(j);
						var backInput;
						if(idOrName=="id"){
							backInput=$("#"+ele.value);
						}else{
							backInput=$("[name="+ele.value+"]");
							if(backInput.length>1){
								backInput=$("[id][name="+ele.value+"]");
							}
						}
						var backValue=getMulValue(ele.value);
						backInput.val(backValue);
					}
				}
			}else{
				for(var j=0;j<backMap.size();j++){
					var ele=backMap.element(j);
					var backInput;
					if(idOrName=="id"){
						backInput=$("#"+ele.value);
					}else{
						backInput=$("[name="+ele.value+"]");
						if(backInput.length>1){
							backInput=$("[id][name="+ele.value+"]");
						}
					}
					backInput.val("");
				}
			}
			$("#${displayId}").blur();
			${callback};
			closeD(getD($(button)));
		}
	}

	//删除触发方法
	function removeItem(itemId) {
		commonArray = $.grep(commonArray, function(ele, i) {
			if(idOrName=="id"){
				return ele.${hiddenId} != itemId;
			}else{
				return ele.${hiddenName} != itemId;
			}
			
		});
		$("#${displayName}SelectedList span").filter("#" + itemId).remove();
	}
	
	//打开页面时是否异步加载出所有节点
	function treeOnLoadSuccess(){
		if(empExpandAll){
			$("#${displayName}TreeSel").tree("expandAll");
		}
	}
	
	//返回无树id过滤时的数据
	function backToAll(){
		var queryParams=$("#${displayName}ListSel").datagrid("options").queryParams;
		delete queryParams.${gridTreeFilter};
		$("#${displayName}ListSel").datagrid("load");
	}
</script>
<div class="easyui-layout" style="width: 100%; height: 100%; margin: 0px auto 0px auto">
	<c:if test="${hasTree}">
		<div region="west" style="width: 150px" title="树型过滤">
			<span onclick="backToAll()" style="cursor: pointer; color: #69aa46; margin-left: 5px;"><i
				class="awsm-icon-home bigger-110">全部</i></span>
			<t:tree id="${displayName}TreeSel" url="${treeUrl}" gridId="${displayName}ListSel" gridTreeFilter="${gridTreeFilter}"
				onLoadSuccess="treeOnLoadSuccess()"></t:tree>
		</div>
	</c:if>
	<div region="center" style="padding: 0px; border: 0px">
		<t:datagrid name="${displayName}ListSel" checkbox="true" fit="true" fitColumns="true" actionUrl="${url}">
			<c:forEach var="json" items="${gridFieldJsonList}">
				<t:dgCol title="${json.title}" width="${json.width}" field="${json.field}" hidden="${json.hidden}"
					query="${json.query}" queryMode="${json.queryMode}" queryInputType="${json.queryInputType}"
					queryExParams="${json.queryExParams}"></t:dgCol>
			</c:forEach>
			<t:dgToolBar title="加入已选" icon="glyphicon glyphicon-plus icon-color"
				onclick="addItem()"></t:dgToolBar>
			<t:dgToolBar title="确定" icon="glyphicon glyphicon-check icon-color"
				onclick="finishSelectItem(this)"></t:dgToolBar>
		</t:datagrid>
	</div>
	<div id="${displayName}SelectedList" title="已选" region="east" style="width: 170px; text-align: center;">
		<c:forEach var="item" items="${idNameMap}">
			<span id="${item.key}" class="viewdelete_span">${item.value}<a style="float: right;" href="#1"
				onclick="removeItem('${item.key}');"><i class="awsm-icon-remove red"></i></a></span>
		</c:forEach>
	</div>
</div>
