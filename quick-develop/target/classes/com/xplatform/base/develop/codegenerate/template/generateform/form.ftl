<#setting number_format="#.##########">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />

<script type="text/javascript">
window.jQuery || document.write(
	'<link href="basic/css/bootstrap.min.css" rel="stylesheet" \/>'
	+'<link href="basic/css/font-awesome.min.css" rel="stylesheet" \/>'
	+'<link href="basic/css/home.common.css" rel="stylesheet" \/>'
	+'<link href="basic/css/commonForm.css" rel="stylesheet" \/>'
	+'<link href="plug-in/uploadifive-v1.2.2-standard/uploadifive.css" rel="stylesheet" \/>'
);
</script>
<#if !isMobile>
<link rel="stylesheet" href="plug-in/zTree/css/metroStyle/metroStyle.css" type="text/css">
</#if>
<link href="basic/css/mobiscroll.custom-2.17.0.min.css" rel="stylesheet" />
<link href="basic/css/colorbox.css" rel="stylesheet" />
<style>
.uploadifive-queue {
    display: none;
}
</style>

<form id="mainForm" name="mainForm" action="appFormTableController.do?saveOrUpdate" method="post">
<input type="hidden" name="id" value="${businessKey}"/>
<input type="hidden" name="parentBusinessKey" value="${parentBusinessKey!''}"/>
<input type="hidden" name="formCode" value="${formCode}"/>
<input type="hidden" name="mainFormCode" value="${mainFormCode}"/>
<input type="hidden" name="isApp" value="${isApp}"/>
<input type="hidden" name="mainFormTableName" value="${mainFormTableName}"/>
<input type="hidden" name="mainTableName" value="${mainTableName}"/>
<input type="hidden" name="mainTableType" value="${mainTableType}"/>
<input type="hidden" name="viewType" value="${viewType}"/>
<div class="common-form">
	<div class="common-form-container">
		<div class="common-form-content">
<#list configJsonMap["fields"] as field>
	<#if field.field_type!="detail">
	<div class="form-field-box form-${field.field_type} clearfix" data-cid="${field.cid}" data-ctype="${field.field_type!''}">
	</#if>
	<#if field.field_type=="text">
		<label>${fieldConfigMap[field.cid].name}：</label>
		<input id="${fieldConfigMap[field.cid].code}" name="${fieldConfigMap[field.cid].code}" 
		type="text" placeholder="请输入内容" 
		<#if fieldConfigMap[field.cid].isConnectionField==1>
        value="${formData[mainFormTableName][fieldConfigMap[field.cid].code]!''}"
        <#else>
        value="${formData[mainTableName][fieldConfigMap[field.cid].code]!''}"
        </#if>
		<#if fieldConfigMap[field.cid].notNull==1>required</#if> />
	</#if>
	<#if field.field_type=="paragraph">
		<label>${fieldConfigMap[field.cid].name}：</label>
		<textarea autoHeight="true" id="${fieldConfigMap[field.cid].code}" name="${fieldConfigMap[field.cid].code}" 
		  placeholder="请输入内容" <#if fieldConfigMap[field.cid].notNull==1>required</#if> ><#if fieldConfigMap[field.cid].isConnectionField==1>${formData[mainFormTableName][fieldConfigMap[field.cid].code]!''}<#else>${formData[mainTableName][fieldConfigMap[field.cid].code]!''}</#if></textarea>
	</#if>
	<#if field.field_type=="number">
		<label>${fieldConfigMap[field.cid].name}：</label>
		<input id="${fieldConfigMap[field.cid].code}" name="${fieldConfigMap[field.cid].code}" onkeyup="checkNumbers(this,${fieldConfigMap[field.cid].scale!0})"
		type="text" placeholder="请输入数字" 
		<#if fieldConfigMap[field.cid].isConnectionField==1>
        value="${formData[mainFormTableName][fieldConfigMap[field.cid].code]!''}"
        <#else>
        value="${formData[mainTableName][fieldConfigMap[field.cid].code]!''}"
        </#if>
		<#if fieldConfigMap[field.cid].notNull==1>required</#if>/>
	</#if>
	<#if field.field_type=="website">
		<label>${fieldConfigMap[field.cid].name}：</label>
		<input id="${fieldConfigMap[field.cid].code}" name="${fieldConfigMap[field.cid].code}" 
		type="text" placeholder="http://"
		<#if fieldConfigMap[field.cid].isConnectionField==1>
        value="${formData[mainFormTableName][fieldConfigMap[field.cid].code]!''}"
        <#else>
        value="${formData[mainTableName][fieldConfigMap[field.cid].code]!''}"
        </#if>
		<#if fieldConfigMap[field.cid].notNull==1>required</#if>/>
	</#if>
	<#if field.field_type=="email">
		<label>${fieldConfigMap[field.cid].name}：</label>
		<input type="text" name="${fieldConfigMap[field.cid].code}" placeholder="example@mail.com"
		<#if fieldConfigMap[field.cid].isConnectionField==1>
        value="${formData[mainFormTableName][fieldConfigMap[field.cid].code]!''}"
        <#else>
        value="${formData[mainTableName][fieldConfigMap[field.cid].code]!''}"
        </#if>
		<#if fieldConfigMap[field.cid].notNull==1>required</#if>/>
	</#if>
	<#if field.field_type=="idcard">
		<label>${fieldConfigMap[field.cid].name}：</label>
		<input id="${fieldConfigMap[field.cid].code}" name="${fieldConfigMap[field.cid].code}" 
		type="text" placeholder="请输入身份证号"
		<#if fieldConfigMap[field.cid].isConnectionField==1>
        value="${formData[mainFormTableName][fieldConfigMap[field.cid].code]!''}"
        <#else>
        value="${formData[mainTableName][fieldConfigMap[field.cid].code]!''}"
        </#if>
		pattern="^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$" <#if fieldConfigMap[field.cid].notNull==1>required</#if>/>
	</#if>
	<#if field.field_type=="date">
		<label>${fieldConfigMap[field.cid].name}：</label>
		<input id="${fieldConfigMap[field.cid].code}" name="${fieldConfigMap[field.cid].code}" dateType="${fieldConfigMap[field.cid].dateType}"
		type="text" placeholder="请选择日期" <#if fieldConfigMap[field.cid].notNull==1>required</#if>
		<#if fieldConfigMap[field.cid].isConnectionField==1>
        value="<#if formData[mainFormTableName][fieldConfigMap[field.cid].code]??><#if fieldConfigMap[field.cid].dateType=0>${formData[mainFormTableName][fieldConfigMap[field.cid].code]?string('yyyy-MM-dd')}<#else>${formData[mainFormTableName][fieldConfigMap[field.cid].code]?string('yyyy-MM-dd HH:mm')}</#if></#if>"
        <#else>
        value="<#if formData[mainTableName][fieldConfigMap[field.cid].code]??><#if fieldConfigMap[field.cid].dateType=0>${formData[mainTableName][fieldConfigMap[field.cid].code]?string('yyyy-MM-dd')}<#else>${formData[mainTableName][fieldConfigMap[field.cid].code]?string('yyyy-MM-dd HH:mm')}</#if></#if>"
        </#if>
		/>
		<i class="fa fa-angle-right"></i>
	</#if>
	<#if field.field_type=="time">
		<label>${fieldConfigMap[field.cid].name}：</label>
		<input id="${fieldConfigMap[field.cid].code}" name="${fieldConfigMap[field.cid].code}" 
		type="text" placeholder="请选择时间"
		<#if fieldConfigMap[field.cid].isConnectionField==1>
        value="${formData[mainFormTableName][fieldConfigMap[field.cid].code]!''}"
        <#else>
        value="${formData[mainTableName][fieldConfigMap[field.cid].code]!''}"
        </#if>
		<#if fieldConfigMap[field.cid].notNull==1>required</#if>/>
		<i class="fa fa-angle-right"></i>
	</#if>
	<#if field.field_type=="radio">
		<label>${fieldConfigMap[field.cid].name}：</label>
		<select id="${fieldConfigMap[field.cid].code}" name="${fieldConfigMap[field.cid].code}" <#if fieldConfigMap[field.cid].notNull==1>required</#if>
		 class="mobiEle" data-target="${fieldConfigMap[field.cid].code}_dummy">
			<#list field.field_options.options as option >
			<option value="${option.label}" 
				<#if fieldConfigMap[field.cid].isConnectionField==1>
		        	<#if (formData[mainFormTableName][fieldConfigMap[field.cid].code]??)>
						<#if formData[mainFormTableName][fieldConfigMap[field.cid].code]==option.label>
							selected
						</#if>
					</#if>
		        <#else>
			        <#if viewType!="add"&&(formData[mainTableName][fieldConfigMap[field.cid].code]??)>
						<#if formData[mainTableName][fieldConfigMap[field.cid].code]==option.label>
							selected
						</#if>
					</#if>
		        </#if>
				
				<#if viewType=="add"&&option.checked>selected</#if>
			>${option.label}</option>
			</#list>
		</select>
		<i class="fa fa-angle-right"></i>
	</#if>
	<#if field.field_type=="checkboxes">
		<label>${fieldConfigMap[field.cid].name}：</label>
		<select id="${fieldConfigMap[field.cid].code}" name="${fieldConfigMap[field.cid].code}" <#if fieldConfigMap[field.cid].notNull==1>required</#if>
		 multiple class="mobiEle" data-target="${fieldConfigMap[field.cid].code}_dummy">
			<#list field.field_options.options as option >
			<option value="${option.label}" 
				<#if fieldConfigMap[field.cid].isConnectionField==1>
		        	<#if formData[mainFormTableName][fieldConfigMap[field.cid].code]??>
						<#list formData[mainFormTableName][fieldConfigMap[field.cid].code]?split(",") as item>
							<#if item==option.label>
								selected
							</#if>
						</#list>
					<#else>
						<#if option.checked>selected</#if>
					</#if>
		        <#else>
			        <#if viewType!="add"&&(formData[mainTableName][fieldConfigMap[field.cid].code]??)>
						<#list formData[mainTableName][fieldConfigMap[field.cid].code]?split(",") as item>
							<#if item==option.label>
								selected
							</#if>
						</#list>
					</#if>
					<#if viewType=="add"&&option.checked>selected</#if>
		        </#if>
			>${option.label}</option>
			</#list>
		</select>
		<i class="fa fa-angle-right"></i>
	</#if>
	<#if field.field_type=="phone">
		<label>${fieldConfigMap[field.cid].name}：</label>
		<input id="${fieldConfigMap[field.cid].code}" name="${fieldConfigMap[field.cid].code}"  pattern="^(0|86|17951)?(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$"
		type="text" placeholder="请输入号码"
		<#if fieldConfigMap[field.cid].isConnectionField==1>
        value="${formData[mainFormTableName][fieldConfigMap[field.cid].code]!''}"
        <#else>
        value="${formData[mainTableName][fieldConfigMap[field.cid].code]!''}"
        </#if>
		<#if fieldConfigMap[field.cid].notNull==1>required</#if>/>
		<#if isMobile>
		<i class="fa fa-phone phoneIcon"></i>
		</#if>
	</#if>
	<#if field.field_type=="position">
		<label>${fieldConfigMap[field.cid].name}：</label>
		<p>
			<#if fieldConfigMap[field.cid].isConnectionField==1>
	        ${formData[mainFormTableName][fieldConfigMap[field.cid].code+'mapaddress']!''}
	        <#else>
	        ${formData[mainTableName][fieldConfigMap[field.cid].code+'mapaddress']!''}
	        </#if>
		</p>
		<input id="${fieldConfigMap[field.cid].code}mapaddress" name="${fieldConfigMap[field.cid].code}mapaddress" type="hidden" flag="mapaddress"
		<#if fieldConfigMap[field.cid].isConnectionField==1>
        value="${formData[mainFormTableName][fieldConfigMap[field.cid].code+'mapaddress']!''}"
        <#else>
        value="${formData[mainTableName][fieldConfigMap[field.cid].code+'mapaddress']!''}"
        </#if>
		 />
		<input id="${fieldConfigMap[field.cid].code}lonandlat" name="${fieldConfigMap[field.cid].code}lonandlat" type="hidden" flag="lonandlat"
		<#if fieldConfigMap[field.cid].isConnectionField==1>
        value="${formData[mainFormTableName][fieldConfigMap[field.cid].code+'lonandlat']!''}"
        <#else>
        value="${formData[mainTableName][fieldConfigMap[field.cid].code+'lonandlat']!''}"
        </#if> 
		/>
		<i class="fa fa-map-marker mapIcon" source="${fieldConfigMap[field.cid].code}" onclick="commonFormEdit.initOpenMapEvent(this)"></i>
	</#if>
	<#if field.field_type=="selectuser">
		<label>${fieldConfigMap[field.cid].name}：</label>
		<input id="${fieldConfigMap[field.cid].code}" name="${fieldConfigMap[field.cid].code}" 
		type="text" placeholder="请选择时间" value="${formData[mainTableName][fieldConfigMap[field.cid].code]!''}"/>
		<i class="fa fa-angle-right"></i>
	</#if>
	<#if field.field_type=="file">
		<div class="top clearfix">
			<label>${fieldConfigMap[field.cid].name}：</label>
			<span class="btn-add" flag="addAttachBtn"
				<#if isMobile>
					onclick="commonFormEdit.initUploadBtnEvent(this)"
				</#if> 
			>
				<img src="basic/img/icon_add_orange2.png" alt="增加">
				<#if !isMobile>
				<input id="${fieldConfigMap[field.cid].code}" name="${fieldConfigMap[field.cid].code}" 
				type="file" class="hiddenfile"/>
				</#if>
			</span>
		</div>
		<div class="content">
			<ul class="list-style-none" fileul="${fieldConfigMap[field.cid].code}">
			<#if formData[mainTableName][fieldConfigMap[field.cid].code]??>
				<#list formData[mainTableName][fieldConfigMap[field.cid].code] as mainFileData >
				<#if mainFileData.iconType=="word"||mainFileData.iconType=="excel"||mainFileData.iconType=="powerpoint"||mainFileData.iconType=="pdf"||mainFileData.iconType=="other">
				<li class="file-item file type-${mainFileData.iconType}">
					<div class="file-left">
						<div class="file-type-icon"><img class="file-icon" src="basic/img/attachment/${mainFileData.iconType}.png" alt="${mainFileData.iconType}"></i></div>
					</div>
				</#if>
				<#if mainFileData.iconType=="img">
					<li class="file-item type-img">
						<div class="file-left">
							<a class="colorbox-ele" href="${attachImgRequest!""}${mainFileData.id}" title="图片1">
								<img src="${attachImgRequest!""}${mainFileData.id}" />
							</a>
						</div>
				</#if>
				<#if mainFileData.iconType=="audio">
					<li class="file-item file type-audio">
						<div class="file-left">
							<a class="colorbox-ele media" href="${attachForeRequest!""}${mainFileData.id}" title="音频1">
								<audio class="media-ele" >
									<source src="${attachForeRequest!""}${mainFileData.id}" type="audio/mpeg">
									<span>您的浏览器不支持HTML5的音频功能。</span>
								</audio>
								<div class="file-type-icon"><i class="fa fa-file-sound-o file-icon"></i></div>
							</a>
						</div>
				</#if>
				<#if mainFileData.iconType=="video">
					<li class="file-item type-video">
						<div class="file-left">
							<a class="colorbox-ele media" href="${attachForeRequest!""}${mainFileData.id}" title="视频1">
								<video class="media-ele" >
								    <source src="${attachForeRequest!""}${mainFileData.id}" type="video/mp4" />
									<span>您的浏览器不支持HTML5的视频功能。</span>
							    </video>
							</a>
						</div>
				</#if>
						<p class="file-name" data-edittype="edit" onclick="commonFormEdit.bindFileNameEvent(this)" data-id="${mainFileData.id}" data-name="${mainFileData.name}">${mainFileData.name}</p>
						<p class="file-size">${mainFileData.attachSizeStr}</p>
						<button type="button" class="btn-del" onclick="commonFormEdit.deleteAttachment(this)"><i class="fa fa-close"></i></button>
					</li>
				</#list>
			</#if>
			</ul>
		</div>
	</#if>
	
	
	<#if field.field_type=="detail">
			<div class="form-field-box form-${field.field_type} clearfix hidden" data-tablename="${subTableNameMap[field.cid]}" data-cid="${field.cid}" data-flag="empty">
				<div class="detail-top">
					<label>${field.label}(<span class="detailIndex">0</span>)<input flag="subId" type="hidden" name="${subTableNameMap[field.cid]}[0].id" value=""/><input flag="editType" type="hidden" name="${subTableNameMap[field.cid]}[0].editType" value="add"/>：</label>
					<i class="fa fa-trash-o delete hidden"></i>
				</div>
				<div class="detail-content">
				<#list field["fields"] as subField>
					<div class="form-field-box form-${subField.field_type} clearfix" data-cid="${subField.cid}" data-ctype="${subField.field_type!''}">
					<#if subField.field_type=="text">
						<label>${fieldConfigMap[subField.cid].name}：</label>
						<input id="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}"
						type="text" placeholder="请输入内容" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>/>
					</#if>
					<#if subField.field_type=="paragraph">
						<label>${fieldConfigMap[subField.cid].name}：</label>
						<textarea autoHeight="true" id="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}"
						  placeholder="请输入内容" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>></textarea>
					</#if>
					<#if subField.field_type=="number">
						<label>${fieldConfigMap[subField.cid].name}：</label>
						<input id="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" onkeyup="checkNumbers(this,${fieldConfigMap[subField.cid].scale!0})"
						type="text" placeholder="请输入数字" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>/>
					</#if>
					<#if subField.field_type=="website">
						<label>${fieldConfigMap[subField.cid].name}：</label>
						<input id="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}"
						type="text" placeholder="http://" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>/>
					</#if>
					<#if subField.field_type=="email">
						<label>${fieldConfigMap[subField.cid].name}：</label>
						<input id="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" 
						type="text" placeholder="example@mail.com" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>/>
					</#if>
					<#if subField.field_type=="idcard">
						<label>${fieldConfigMap[subField.cid].name}：</label>
						<input id="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" pattern="^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$"
						type="text" placeholder="请输入身份证号" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>/>
					</#if>
					<#if subField.field_type=="date">
						<label>${fieldConfigMap[subField.cid].name}：</label>
						<input id="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" dateType="${fieldConfigMap[subField.cid].dateType}"
						type="text" placeholder="请选择日期" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>/>
						<i class="fa fa-angle-right"></i>
					</#if>
					<#if subField.field_type=="time">
						<label>${fieldConfigMap[subField.cid].name}：</label>
						<input id="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" 
						type="text" placeholder="请选择时间" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>/>
						<i class="fa fa-angle-right"></i>
					</#if>
					<#if subField.field_type=="radio">
						<label>${fieldConfigMap[subField.cid].name}：</label>
						<select id="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}"  
						class="mobiEle" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>>
							<#list subField.field_options.options as option >
							<option value="${option.label}" 
								<#if viewType=="add"&&option.checked>selected</#if>
							>${option.label}</option>
							</#list>
						</select>
						<i class="fa fa-angle-right"></i>
					</#if>
					<#if subField.field_type=="checkboxes">
						<label>${fieldConfigMap[subField.cid].name}：</label>
						<select id="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" 
						 multiple class="mobiEle" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>>
							<#list subField.field_options.options as option >
							<option value="${option.label}" 
								<#if viewType=="add"&&option.checked>selected</#if>
							>${option.label}</option>
							</#list>
						</select>
						<i class="fa fa-angle-right"></i>
					</#if>
					<#if subField.field_type=="phone">
						<label>${fieldConfigMap[subField.cid].name}：</label>
						<input id="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" 
						type="text" placeholder="请输入号码" pattern="^(0|86|17951)?(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>/>
						<#if isMobile>
						<i class="fa fa-phone phoneIcon"></i>
						</#if>
					</#if>
					<#if subField.field_type=="position">
						<label>${fieldConfigMap[subField.cid].name}：</label>
						<p></p>
						<input id="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}mapaddress" name="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}mapaddress" type="hidden" flag="mapaddress"/>
						<input id="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}lonandlat" name="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}lonandlat" type="hidden" flag="lonandlat"/>
						<i class="fa fa-map-marker mapIcon" source="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" onclick="commonFormEdit.initOpenMapEvent(this)"></i>
					</#if>
					<#if subField.field_type=="selectuser">
						<label>${fieldConfigMap[subField.cid].name}：</label>
						<input id="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" 
						 type="text" placeholder="请选择时间" />
						<i class="fa fa-angle-right"></i>
					</#if>
					<#if subField.field_type=="file">
						<div class="top clearfix">
							<label>${fieldConfigMap[subField.cid].name}：</label>
							<span class="btn-add" flag="addAttachBtn" name="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}"
								<#if isMobile>
									onclick="commonFormEdit.initUploadBtnEvent(this)"
								</#if> 
							>
								<img src="basic/img/icon_add_orange2.png" alt="增加">
								<#if !isMobile>
								<input id="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}" 
								type="file" class="hiddenfile"/>
								</#if>
							</span>
						</div>
						<div class="content">
							<ul class="list-style-none" >
							</ul>
						</div>
					</#if>
					</div>
				</#list>
				</div>
				<div class="detail-bottom">
					<a class="clone">增加明细</a>
				</div>
			</div>
			<#if (formData[subTableNameMap[field.cid]]?size>0)>
			<#list formData[subTableNameMap[field.cid]] as subTableData >
				<div class="form-field-box form-${field.field_type} clearfix" data-cid="${field.cid}" data-tablename="${subTableNameMap[field.cid]}" >
					<div class="detail-top">
						<label>${field.label}(<span class="detailIndex">${subTableData_index+1}</span>)<input flag="subId" type="hidden" name="${subTableNameMap[field.cid]}[${subTableData_index}].id" value="${subTableData["id"]}"/><input flag="editType" type="hidden" name="${subTableNameMap[field.cid]}[${subTableData_index}].editType" value="edit"/>：</label>
						<i class="fa fa-trash-o delete"></i>
					</div>
					<div class="detail-content">
					<#list field["fields"] as subField>
						<div class="form-field-box form-${subField.field_type} clearfix" data-cid="${subField.cid}" data-ctype="${subField.field_type!''}">
						<#if subField.field_type=="text">
							<label>${fieldConfigMap[subField.cid].name}：</label>
							<input id="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}"
							type="text" placeholder="请输入内容" value="${subTableData[fieldConfigMap[subField.cid].code]!''}" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>/>
						</#if>
						<#if subField.field_type=="paragraph">
							<label>${fieldConfigMap[subField.cid].name}：</label>
							<textarea autoHeight="true" id="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}"
							  placeholder="请输入内容" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>>${subTableData[fieldConfigMap[subField.cid].code]!''}</textarea>
						</#if>
						<#if subField.field_type=="number">
							<label>${fieldConfigMap[subField.cid].name}：</label>
							<input id="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}"
							type="text" placeholder="请输入数字" value="${subTableData[fieldConfigMap[subField.cid].code]!''}" onkeyup="checkNumbers(this,${fieldConfigMap[subField.cid].scale!0})" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>/>
						</#if>
						<#if subField.field_type=="website">
							<label>${fieldConfigMap[subField.cid].name}：</label>
							<input id="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}"
							type="text" placeholder="http://" value="${subTableData[fieldConfigMap[subField.cid].code]!''}" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>/>
						</#if>
						<#if subField.field_type=="email">
							<label>${fieldConfigMap[subField.cid].name}：</label>
							<input id="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" 
							type="text" placeholder="example@mail.com" value="${subTableData[fieldConfigMap[subField.cid].code]!''}" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>/>
						</#if>
						<#if subField.field_type=="idcard">
							<label>${fieldConfigMap[subField.cid].name}：</label>
							<input id="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" pattern="^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$"
							type="text" placeholder="请输入身份证号" value="${subTableData[fieldConfigMap[subField.cid].code]!''}" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>/>
						</#if>
						<#if subField.field_type=="date">
							<label>${fieldConfigMap[subField.cid].name}：</label>
							<input id="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}"  dateType="${fieldConfigMap[subField.cid].dateType}"
							type="text" placeholder="请选择日期" value="<#if subTableData[fieldConfigMap[subField.cid].code]??>${(subTableData[fieldConfigMap[subField.cid].code])?string('yyyy-MM-dd')}</#if>" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>/>
							<i class="fa fa-angle-right"></i>
						</#if>
						<#if subField.field_type=="time">
							<label>${fieldConfigMap[subField.cid].name}：</label>
							<input id="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" 
							type="text" placeholder="请选择时间" value="${subTableData[fieldConfigMap[subField.cid].code]!''}" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>/>
							<i class="fa fa-angle-right"></i>
						</#if>
						<#if subField.field_type=="radio">
							<label>${fieldConfigMap[subField.cid].name}：</label>
							<select id="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}"  
							class="mobiEle" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>>
								<#list subField.field_options.options as option >
								<option value="${option.label}" 
									<#if viewType!="add"&&(subTableData[fieldConfigMap[subField.cid].code]??)>
										<#if subTableData[fieldConfigMap[subField.cid].code]==option.label>
											selected
										</#if>
									</#if>
									<#if viewType=="add"&&option.checked>selected</#if>
								>${option.label}</option>
								</#list>
							</select>
							<i class="fa fa-angle-right"></i>
						</#if>
						<#if subField.field_type=="checkboxes">
							<label>${fieldConfigMap[subField.cid].name}：</label>
							<select id="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" 
							 multiple class="mobiEle" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>>
								<#list subField.field_options.options as option >
								<option value="${option.label}" 
									<#if viewType!="add"&&(subTableData[fieldConfigMap[subField.cid].code]??)>
										<#list subTableData[fieldConfigMap[subField.cid].code]?split(",") as item>
											<#if item==option.label>
												selected
											</#if>
										</#list>
									</#if>
									<#if viewType=="add"&&option.checked>selected</#if>
								>${option.label}</option>
								</#list>
							</select>
							<i class="fa fa-angle-right"></i>
						</#if>
						<#if subField.field_type=="phone">
							<label>${fieldConfigMap[subField.cid].name}：</label>
							<input id="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" 
							type="text" placeholder="请输入号码" pattern="^(0|86|17951)?(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$" value="${subTableData[fieldConfigMap[subField.cid].code]!''}" <#if fieldConfigMap[subField.cid].notNull==1>required</#if>/>
							<#if isMobile>
							<i class="fa fa-phone phoneIcon"></i>
							</#if>
						</#if>
						<#if subField.field_type=="position">
							<label>${fieldConfigMap[subField.cid].name}：</label>
							<p>${subTableData[fieldConfigMap[subField.cid].code+'mapaddress']!''}</p>
							<input id="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}mapaddress" name="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}mapaddress" type="hidden" flag="mapaddress"
							value="${subTableData[fieldConfigMap[subField.cid].code+'mapaddress']!''}" />
							<input id="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}lonandlat" name="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}lonandlat" type="hidden" flag="lonandlat"
							value="${subTableData[fieldConfigMap[subField.cid].code+'lonandlat']!''}" />
							<i class="fa fa-map-marker mapIcon" source="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" onclick="commonFormEdit.initOpenMapEvent(this)"></i>
						</#if>
						<#if subField.field_type=="selectuser">
							<label>${fieldConfigMap[subField.cid].name}：</label>
							<input id="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" 
							 type="text" placeholder="请选择时间" value="${subTableData[fieldConfigMap[subField.cid].code]!''}"/>
							<i class="fa fa-angle-right"></i>
						</#if>
						<#if subField.field_type=="file">
							<div class="top clearfix">
								<label>${fieldConfigMap[subField.cid].name}：</label>
								<span class="btn-add" flag="addAttachBtn" name="${subTableNameMap[field.cid]}[0].${fieldConfigMap[subField.cid].code}"
									<#if isMobile>
										onclick="commonFormEdit.initUploadBtnEvent(this)"
									</#if> 
								>
									<img src="basic/img/icon_add_orange2.png" alt="增加">
									<#if !isMobile>
									<input id="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" name="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}" 
									type="file" class="hiddenfile"/>
									</#if>
								</span>
							</div>
							<div class="content">
								<ul class="list-style-none" fileul="${subTableNameMap[field.cid]}[${subTableData_index}].${fieldConfigMap[subField.cid].code}">
									<#if subTableData[fieldConfigMap[subField.cid].code]??>
									<#list subTableData[fieldConfigMap[subField.cid].code] as subFileData >			
									<#if subFileData.iconType=="word"||subFileData.iconType=="excel"||subFileData.iconType=="powerpoint"||subFileData.iconType=="pdf"||subFileData.iconType=="other">
									<li class="file-item file type-${subFileData.iconType}">
										<div class="file-left">
											<div class="file-type-icon"><img class="file-icon" src="basic/img/attachment/${subFileData.iconType}.png" alt="${subFileData.iconType}"></i></div>
										</div>
									</#if>
									<#if subFileData.iconType=="img">
										<li class="file-item type-img">
											<div class="file-left">
												<a class="colorbox-ele" href="${attachImgRequest!""}${subFileData.id}" title="图片1">
													<img src="${attachImgRequest!""}${subFileData.id}" />
												</a>
											</div>
									</#if>
									<#if subFileData.iconType=="audio">
										<li class="file-item file type-audio">
											<div class="file-left">
												<a class="colorbox-ele media" href="${attachForeRequest!""}${subFileData.id}" title="音频1">
													<audio class="media-ele" >
														<source src="${attachForeRequest!""}${subFileData.id}" type="audio/mpeg">
														<span>您的浏览器不支持HTML5的音频功能。</span>
													</audio>
													<div class="file-type-icon"><i class="fa fa-file-sound-o file-icon"></i></div>
												</a>
											</div>
									</#if>
									<#if subFileData.iconType=="video">
										<li class="file-item type-video">
											<div class="file-left">
												<a class="colorbox-ele media" href="${attachForeRequest!""}${subFileData.id}" title="视频1">
													<video class="media-ele" >
													    <source src="${attachForeRequest!""}${subFileData.id}" type="video/mp4" />
														<span>您的浏览器不支持HTML5的视频功能。</span>
												    </video>
												</a>
											</div>
									</#if>
											<p class="file-name" onclick="commonFormEdit.bindFileNameEvent(this)" data-id="${subFileData.id}" data-name="${subFileData.name}">${subFileData.name}</p>
											<p class="file-size">${subFileData.attachSizeStr}</p>
											<button type="button" class="btn-del" onclick="commonFormEdit.deleteAttachment(this)"><i class="fa fa-close"></i></button>
										</li>
									</#list>
								</#if>
								</ul>
							</div>
						</#if>
						
						</div>
					</#list>
					</div>
					<#if !subTableData_has_next>
					<div class="detail-bottom">
						<a class="clone">增加明细</a>
					</div>
					</#if>
				</div>
			</#list>
		</#if>
		
	</#if>
	<#if field.field_type!="detail">
	</div>
	</#if>
</#list>

	<!-- 审批人查看 -->
	<#if isStartAssign==1&&buttonType=='startFlow'>
	<div class="form-field-box form-selectuser clearfix" flag="candidateDiv">
		<input id="assignResult" type="hidden" name="assignResult"/>
		<div class="top clearfix">
			<label >审批人<span class="light-grey font-size-12">（点击头像可删除）</span></label>
		</div>
		<div class="content">
			<ul class="list-style-none-h user-group">
				<li onclick="selectCandidate(this)" flag="addAssign">
					<div class="btn-add">
						<img src="basic/img/group_member_add.png" alt="增加">
					</div>
				</li>
			</ul>
		</div>
	</div>
	</#if>

	<!-- 出传阅人的条件  1.传阅表单+有传阅人-->
	<#if notifyType==1>
		<#if (circulateList?size>0)>
			<div class="form-field-box form-selectuser clearfix" >
				<div class="top clearfix">
					<label >传阅人</label>
				</div>
				<div class="content">
					<ul class="list-style-none-h user-group">
					<#list circulateList as circulatePeron >
						<li >
							<img class="avatar" src="${((circulatePeron['portrait']!'')!='')?string(attachForeRequest+circulatePeron['portrait']!'','basic/img/avatars/avatar_80.png')}">
							<span class="name text-overflow">${circulatePeron['userName']}</span>
							<span class="read"><i class="fa fa-check"></i></span>
						</li>
					</#list>
					</ul>
					<!-- 超过一行才需要展开 -->
					<#if (circulateList?size>=6)>
					<div class="toggle-btn">展开</div>
					</#if>
				</div>
			</div>
		</#if>
	</#if>
		
	<#if viewType=='detail'>
		<!-- 关联模板查看区域 -->
		<#if childrenProcessInstanceList??>
			<#list childrenProcessInstanceList as childrenProcessInstance >
				<div class="form-field-box form-btn-area clearfix">
					<div class="form-field-box form-btn clearfix" flag="childrenProcessInstance" onclick="commonFormEdit.loadUrl(this)" data-url="appFormTableController.do?commonFormEdit&viewType=detail&parentBusinessKey=${businessKey}&businessKey=${childrenProcessInstance['businessKey']}&formCode=${childrenProcessInstance['formCode']}" data-title="${childrenProcessInstance['defName']}">
						<label>${childrenProcessInstance['defName']}</label>
						<i class="fa fa-angle-right"></i>
					</div>
				</div>
			</#list>
		</#if>
	</#if>
	
		<!-- 按钮 -->
		<#if buttonType??>
		<div class="form-field-box form-bottom-bar clearfix" >
			<#if buttonType=='startFlow'>
				<input class="btn-bottom" type="button" onclick="saveAndProcess('startFlow')" value="提交"/>
			<#elseif buttonType=='nextProcess'>
				<form class="form-bottom" id="approveForm" action="" >
					<div style="border:1px solid #efefef">
						<textarea id="voteContent" name="voteContent" style="height:100px"></textarea>
						<input type="hidden" id="tranPerson"/>
						<ul class="list-style-table btn-bottom-3">
							<li>
								<input type="button" onclick="saveAndProcess('nextProcess','1')" value="同意"/>
							</li>
							<li>
								<input type="button" onclick="saveAndProcess('nextProcess','2')" value="反对"/>
							</li>
							<li>
								<input type="button" onclick="commonFormEdit.selectTran()" value="转交"/>
							</li>
						</ul
					</div>
				</form>
				
			<#elseif buttonType=='save'>
				<input class="btn-bottom" type="button" onclick="saveAndProcess()" value="保存"/>
			<#elseif buttonType=='saveAndSend'>
				<input class="btn-bottom" type="button" onclick="saveAndProcess('saveAndSend')" value="发送"/>
			<#elseif buttonType=='view'>
				<!-- 出传阅按钮的条件  1.传阅表单+流程状态为正常结束   -->
				<#if notifyType==1&&flowStatus==2>
					<#if (circulateList?size>0)>
					<#else>
						<#if isSharefolder==1>
							<!-- 先发布到云盘,再传阅   -->
							<input class="btn-bottom" type="button" onclick="commonFormEdit.selectFolder()" value="发布后传阅"/>
						<#else>
							<!-- 普通传阅   -->
							<input class="btn-bottom" type="button" onclick="commonFormEdit.selectNotify()" value="传阅"/>
						</#if>
					</#if>
				<#elseif flowStatus==1>
					<div style="border:1px solid #efefef">
						<textarea id="revokeOpinion" name="revokeOpinion" style="height:100px"></textarea>
						<ul class="list-style-table btn-bottom-3">
							<li>
								<input type="button" onclick="saveAndProcess('revoke','1')" value="撤回"/>
							</li>
						</ul
					</div>
				</#if>
			</#if>
		</div>
		</#if>
		
		<#if isFlow==1&&buttonType!="startFlow">
			<iframe id="taskOpinionsIframe" src="definitionController.do?showTaskOpinions&businessKey=${businessKey}" width="100%" onload="setIframeLoadedHeight(this)">
			</iframe>
		</div>
		</#if>
	</div>
</div>
	
	
</div>

<!-- 选人弹框 -->
<div class="modal fade in-iframe" id="selectMulTypeModal" role="dialog">
	<div class="modal-dialog with-select-all" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">审批权限设置</h4>
			</div>
			<div class="modal-body clearfix">
			</div>
			<div class="modal-footer">
				<button id="selectMulTypeConfirm" type="button" class="btn btn-orange" data-dismiss="modal">确定</button>
			</div>
		</div>
	</div>
</div>


<!-- 选人弹框 -->
<#if buttonType=='nextProcess'||buttonType=='saveAndSend'||(buttonType=='startFlow'&&isStartAssign==1)||(viewType=='detail'&&notifyType==1)>
<div class="modal fade" id="selectUserModal" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">选择转办人</h4>
			</div>
			<div class="modal-body clearfix">
				<div class="pull-left all-person-box">
					<div class="top">
						<span class="top-left-span">选择成员：</span>
						<span class="top-left-btn">
							<a class="select-all">全选</a>
						</span>
						<div class="search">
							<input type="text" id="searchUser" place-holder="搜索成员"> <i class="glyphicon glyphicon-search"></i>
						</div>
					</div>
					<div class="person-box">
						<div class="person-list all">
							<ul class="list-style-none" id="userList">
							</ul>
						</div>
					</div>
				</div>
				<div class="pull-right selected-box">
					<div class="top">
						<span class="top-left-span">新添成员：</span>
						<span class="top-left-btn">
							<a class="remove-all">全部移除</a>
						</span>
						<span class="pull-right">已选<span class="selected-count">0</span>人
						</span>
					</div>
					<div class="person-box">
						<div class="person-list approval-flow" id="selectedUser">
							<ul class="list-style-none-h">
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button id="selectTranConfirm" type="button" class="btn btn-orange" data-dismiss="modal">确定</button>
			</div>
		</div>
	</div>
</div>
</#if >

<#if !isMobile>
<!-- 位置 -->
<div class="modal fade in-iframe" id="mapViewModal" role="dialog">
	<div class="modal-dialog with-select-all" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">位置</h4>
			</div>
			<div class="modal-body clearfix" style="height:550px">
				<iframe id="mapViewIframe" width="100%" height="100%" />
			</div>
			<div class="modal-footer">
				<button id="mapViewConfirm" type="button" class="btn btn-orange" data-dismiss="modal">确定</button> 
			</div>
		</div>
	</div>
</div>
</#if>

</form>
<script type="text/javascript">
var mobileContext = "body",
	mobileContext_m = "body";
if(window.jQuery){
	mobileContext = ".sys-right-panel";
	mobileContext_m = $(mobileContext).closest(".content-pannel");
}

window.jQuery || document.write(
	'<script type="text/javascript" src="basic/js/html5.js"><\/script>'
	+ '<script type="text/javascript" src="basic/js/jquery-2.0.3.min.js"><\/script>'
	+ '<script type="text/javascript" src="basic/js/jquery-migrate-1.1.0.js"><\/script>'
	+ '<script type="text/javascript" src="basic/js/bootstrap.min.js"><\/script>'
	+ '<script type="text/javascript" src="basic/js/jquery.slimscroll.min.js"><\/script>'
	+ '<script type="text/javascript" src="plug-in/tools/syUtil.js"><\/script>'
	+ '<script type="text/javascript" src="plug-in/tools/curdtools.js"><\/script>'
	+ '<script type="text/javascript" src="basic/js/home.js"><\/script>'
	+ '<script type="text/javascript" src="plug-in/tools/uuid.js"><\/script>'
	+ '<script type="text/javascript" src="plug-in/uploadifive-v1.2.2-standard/jquery.uploadifive.js" ><\/script>'
	+ '<script type="text/javascript" src="plug-in/tools/Map.js" ><\/script>'
	+ '<script type="text/javascript" src="basic/js/html5Validate/jquery-html5Validate.js" ><\/script>'
	+ '<script type="text/javascript" src="plug-in/tools/native-support.js" ><\/script>'
);
</script>
<script type="text/javascript" src="basic/js/mobiscroll.custom-2.17.0.min.js"></script>
<script type="text/javascript" src="basic/js/jquery.colorbox-min.js"></script>
<#if !isMobile>
<script type="text/javascript" src="plug-in/zTree/jquery.ztree.core-3.5.js"></script>
</#if>
<script>
var commonFormEdit = {
	loadMobi: function(detailGroup){
		if(!detailGroup) detailGroup = $("body");
		
		detailGroup.find(".mobiEle").prev("input").remove();
		
		detailGroup.find(".form-date input[dateType=0]").mobiscroll().date({
			lang: 'zh',
	        theme: 'mobiscroll',
	        dateFormat: 'yyyy-mm-dd',
	        display: 'bottom',
	        showLabel: true,
	        context: mobileContext_m
	    });
	    detailGroup.find(".form-date input[dateType=1]").mobiscroll().datetime({
			lang: 'zh',
	        theme: 'mobiscroll',
	        dateFormat: 'yyyy-mm-dd',
	        display: 'bottom',
	        showLabel: true,
	        context: mobileContext_m
	    });
		detailGroup.find(".form-time input").mobiscroll().time({
			lang: 'zh',
	        theme: 'mobiscroll',
	        display: 'bottom',
	        context: mobileContext_m
	    });
		detailGroup.find(".form-radio select").mobiscroll().select({
			lang: 'zh',
	        theme: 'mobiscroll',
	        display: 'bottom',
	        minWidth: 200,
	        context: mobileContext_m
	    });
		detailGroup.find(".form-checkboxes select").mobiscroll().select({
			lang: 'zh',
	        theme: 'mobiscroll',
	        display: 'bottom',
	        minWidth: 200,
	        context: mobileContext_m
	    });
	},
	updateDetailIndex: function(detailGroud){
		detailGroud.find(".detailIndex").each(function(i){
			$(this).text(i+1);
		});
		detailGroud.each(function(i){
			$(':input, select, i, span', this).each(function(){
				var $this = $(this), name = $this.attr('name');
				//位置特殊处理
				if($this.is("i")&&$this.closest(".form-field-box").data("ctype")=="position"){
					var source=$this.attr("source");
					var s = source.indexOf("[");
					var e = source.indexOf("]");
					var new_name = source.substring(s,e+1);
					$this.attr("source",source.replace(new_name,"["+i+"]"));
				}
				if(name!=null){
					var s = name.indexOf("[");
					var e = name.indexOf("]");
					var new_name = name.substring(s,e+1);
					$this.attr("name",name.replace(new_name,"["+i+"]"));
					//把重整后的name给id
					$this.attr("id",$this.attr("name"));
					//把file重整后的name给fileul
					if($this.attr("type")=="file"||$this.is("span")){
						var ul=$this.closest(".form-file").children(".content").children("ul");
						ul.attr("fileul",$this.attr("name"));
					}
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
		
		//选人与部门
		var config='{"callbackKey":"commonFormEdit.tran","multiple":false}';
		if (getUseragent()=='iphone') {
			selectPerson.select(config);
		} else if (getUseragent()=='android') {
			selectPerson.select(config);
		} else if (getUseragent()=='pcweb'){
			$this=$(ele);
			var pcConfig={
				title:"选择转办人",
				containsSelf:false
			};
			pcConfig=$.extend({},parseJSON(config),pcConfig);
			selectMulType.goSelectPage(pcConfig);
		}
	},
	//初始化上传组件
	initUploadifive:function(ele){
		var $this=$(ele);
		var $thisDetail = $this.closest(".form-detail");
		var id,tableName,cid;
		if($thisDetail[0]){
			//说明是从表附件
			id=$("[flag=subId]",$thisDetail).val();
			tableName=$thisDetail.data("tablename");
			cid=$this.closest(".form-field-box").data("cid");
		}else{
			//说明是从表附件
			id=$("[name=id]").val();
			tableName=$("[name=mainTableName]").val();
			cid=$this.closest(".form-field-box").data("cid");
		}
		var data = {
			"businessExtra" : cid,
			"businessKey" : id,
			"businessType" : tableName,
			"otherKey" : "${((parentBusinessKey!'')!='')?string(parentBusinessKey!'',businessKey)}",
			"otherKeyType" : "${mainFormCode!''}"
		};
		$this.uploadifive({
			fileObjName : id+cid,
			onUploadComplete : function(file, data) {
				var result = parseJSON(data);
		        if(result.success){
		        	if(result.obj[0]){
		        		var fileULId=$this.closest(".form-field-box").find(".content ul").attr("fileul");
		        		commonFormEdit.appendAttachItem(result.obj,$("[fileul="+escapeJquery(fileULId)+"]"));
		        	}
		        }
		    },
		    buttonClass:'hiddenfile',
		    formData:data,
			auto: true,  
			buttonText: "选择文件",  
			onUpload : function(filesToUpload) {
				if(filesToUpload == 0){
					alert("onUpload");
				}
		    },
			uploadScript : 'attachController.do?uploadFiles'
		});
	},
	//绑定上传交互的事件
	initUploadBtnEvent:function(ele){
		var $this=$(ele);
		var $thisDetail = $this.closest(".form-detail");
		var id,tableName,cid;
		var otherKey="${((parentBusinessKey!'')!='')?string(parentBusinessKey!'',businessKey)}";
		var otherKeyType="${mainFormCode!''}";
		if($thisDetail[0]){
			//说明是从表附件
			id=$("[flag=subId]",$thisDetail).val();
			tableName=$thisDetail.data("tablename");
			cid=$this.closest(".form-field-box").data("cid");
		}else{
			//说明是主表附件
			id=$("[name=id]").val();
			tableName=$("[name=mainTableName]").val();
			cid=$this.closest(".form-field-box").data("cid");;
		}
		var fileULId=$this.closest(".form-field-box").find(".content ul").attr("fileul");
		attachment.select(id,tableName,cid,otherKey,otherKeyType,fileULId);
	},
	//绑定打开地图view的事件
	initOpenMapEvent:function(ele){
		var mapAddress=$(ele).closest(".form-field-box").find("input[flag=mapaddress]").val();
		var lonAndLat=$(ele).closest(".form-field-box").find("input[flag=lonandlat]").val();
		var source=$(ele).closest(".form-field-box").find("i[source]").attr("source");
		var disabled=$(ele).closest(".form-field-box").hasClass("disabled");
		var viewType;
		if(disabled){
			viewType=2;
		}else{
			viewType=1;
		}
		
		if(viewType==2){
			if(!lonAndLat){
				simpleCMD.tip("无位置信息无法查看");
				return;
			}
		}
		var params='{"mapAddress":"'+mapAddress+'","lonAndLat":"'+lonAndLat+'","viewType":'+viewType+',"source":"'+source+'"}';
	
		if (getUseragent()=='iphone') {
			map.goMapView(params);
		} else if (getUseragent()=='android') {
			map.goMapView(params);
		} else if (getUseragent()=='pcweb'){
			mapViewIframe.attr("src", "appFormTableController.do?goMapView&params="+params);
			mapViewModal.modal("show");
		}
	},
	loadColorbox:function(ele){
		if($(ele).hasClass("media")){
			//说明是音、视频
			$(ele).colorbox({
				innerWidth:"85%", 
				innerHeight:"85%",
				inline: true,
				href: $(".media-ele", $(ele)),
				onComplete: function(){
					$("#cboxLoadedContent > video,#cboxLoadedContent > audio").attr("controls", "controls");
				},
				onClosed: function(){
					$(this).find("video,audio").removeAttr("controls");
				}
			});
		}else {
			//说明是图片
			$(ele).colorbox({
				iframe: false,
				photo:true,
				maxWidth: "85%",
				maxHeight: "85%"
			});
		}
	},
	bindFileNameEvent:function(ele){
		$this=$(ele);
		if (getUseragent()=='iphone') {
			attachment.download($this.data("id"),$this.data("name"));
		} else if (getUseragent()=='android') {
			attachment.download($this.data("id"),$this.data("name"));
		} else if (getUseragent()=='pcweb'){
			common_downloadFileByUrl('${attachForeRequest!""}'+$this.data("id"));
		}
	},
	appendAttachItem:function(result,ele){
		var _this = ele,fileLeftHtml = "";
		$.each(result, function(i, n){
			var iconName = n.iconType;
			var url;
			var ifFile = (n.iconType == "img" || n.iconType == "video")?"":"file";
			switch(n.iconType){
				case "img":
					url='${attachImgRequest!""}'+n.id;
					fileLeftHtml = "<a class='colorbox-ele' href='"+url+"' title='"+n.name+"'>"
						+ "<img src='"+url+"' />"
						+ "</a>";
					break;
				case "audio":
					url='${attachForeRequest!""}'+n.id;
					fileLeftHtml = "<a class='colorbox-ele media' href='"+url+"' title='"+n.name+"'>"
						+ "<audio class='media-ele' >"
						+ "<source src='"+url+"' type='audio/mpeg'>"
						+ "<span>您的浏览器不支持HTML5的音频功能。<span>"
						+ "</audio>"
						+ "<div class='file-type-icon'><i class='fa fa-file-sound-o file-icon'></i></div>"
						+ "</a>";
					break;
				case "video":
					url='${attachForeRequest!""}'+n.id;
					fileLeftHtml = "<a class='colorbox-ele media' href='"+url+"' title='"+n.name+"'>"
						+ "<video class='media-ele' >"
						+ "<source src='"+url+"' type='video/mp4' />"
						+ "<span>您的浏览器不支持HTML5的视频功能。</span>"
						+ "</video>"
						+ "</a>";
					break;
				default:
					fileLeftHtml = "<div class='file-type-icon'><img class='file-icon' src='basic/img/attachment/"+iconName+".png'/></div>";
			}
			var liEle=$(
					"<li id='"+n.id+"' class='file-item " + ifFile + " type-" + n.iconType + "' data-url='"+url+"'>"
					+ "<div class='file-left'>"
					+ fileLeftHtml
					+ "</div>"
					+ "<p class='file-name' data-url='"+url+"' data-edittype='add' data-name='"+n.name+"' data-id='"+n.id+"' onclick='commonFormEdit.bindFileNameEvent(this)'>"+n.name+"</p>"
					+ "<p class='file-size'>"+n.sizeStr+"</p>"
					+ "<button class='btn-del' type='button' onclick='commonFormEdit.deleteAttachment(this)'><i class='fa fa-close'></i></button>"
					+ "</li>"
				);
				
			//绑定文件名下载事件
			//$(_this).closest(".form-file").children(".content").children("ul").append(liEle);
			$(_this).append(liEle);
			if(n.iconType=="img"||n.iconType=="audio"||n.iconType=="video"){
				//初始化colorbox
				commonFormEdit.loadColorbox($(".colorbox-ele",liEle));
			}
			
			//绑定附件删除
			//fileDel();
		});
	},
	deleteAttachment:function(ele){
		var $this=$(ele);
		var fileNameEle=$this.closest(".file-item").find(".file-name");
		var attachId=fileNameEle.data("id");
		var editType=fileNameEle.data("edittype");
		if(editType=="add"){
			if(confirm("确定删除该附件?")){
				ajax("attachController.do?deleteFile&aId="+attachId,function(){
					$this.closest(".file-item").remove();
				});
			}
		}else{
			alert("只能删除本次上传的文件");
		}
	},
	loadUrl:function(ele){
		$this=$(ele);
		var params='{"url":"'+ $this.data("url") +'","title":"'+ $this.data("title") +'"}';
		if (getUseragent()=='iphone') {
			simpleCMD.loadUrl(params);
		} else if (getUseragent()=='android') {
			simpleCMD.loadUrl(params);
		} else if (getUseragent()=='pcweb'){
			$this.closest(".sys-right-panel").load($this.data("url"));
		}
	},
	//选择传阅人
	selectNotify:function(){
		//选人、部门、角色
		var config='{"callbackKey":"commonFormEdit.circulate","multiple":true,"showType":"contact,org,role","selectOrg":true}';
		if (getUseragent()=='iphone') {
			selectPerson.select(config);
		} else if (getUseragent()=='android') {
			selectPerson.select(config);
		} else if (getUseragent()=='pcweb'){
			var pcConfig={
				title:"选择传阅人",
				containsSelf:true
			};
			pcConfig=$.extend({},parseJSON(config),pcConfig);
			selectMulType.goSelectPage(pcConfig);
		}
	},
	//发布选择文件夹
	selectFolder:function(){
		var config='{"callbackKey":"commonFormEdit.publishToDisk"}';
		if (getUseragent()=='iphone') {
			cloudDisk.selectFolder(config);
		} else if (getUseragent()=='android') {
			cloudDisk.selectFolder(config);
		} else if (getUseragent()=='pcweb'){
		}
	},
	//动态审批人回调
	assign:function(params){
		var id="",name="",portrait=null;
		$.each(params,function(i,obj){
			id=obj.id;
			name=obj.name; 
			type=obj.type;
			if(type=="org"||type=="role"){
				portrait= selectMulType.getKindIcon(type);
			}else if(type=="user"){
				portrait = defaultImg('${attachForeRequest}',obj.portrait);
			}
			
			var tempLi=$('<li onclick="removeCandidate(this)" ><img class="avatar" src="'+portrait+'"><span class="name text-overflow">'+name+'</span></li>');
			tempLi.data("id",id);
			tempLi.data("name",name);
			tempLi.data("type",type);
			$("[flag=addAssign]").before(tempLi);
		});
	},
	//转办回调
	tran:function(params){
		var ids="";
		$(params).each(function(i){
			if(this.type=="user"){
				ids+=this.id+",";
			}
		});
		
		$("#tranPerson").val(ids.removeDot());
		saveAndProcess("tran");
	},
	//传阅回调
	circulate:function(params){
		//请求发送传阅
		simpleCMD.showProcessing();	
		debugger;
		ajax("processInstanceController.do?sendNotifyMessage&businessKey=${businessKey!''}&formCode=${formCode!''}&finalValue="+encodeURIComponent(JSON.stringify(params)),function(data){
			simpleCMD.hideProcessing();
			simpleCMD.tip(data.msg);
			if (getUseragent()=='iphone') {
				location.href="appFormTableController.do?commonFormEdit&viewType=detail&businessKey=${businessKey!''}&formCode=${formCode!''}";
				simpleCMD.listRefresh();
			} else if (getUseragent()=='android') { 
				location.href="appFormTableController.do?commonFormEdit&viewType=detail&businessKey=${businessKey!''}&formCode=${formCode!''}";
				simpleCMD.listRefresh();
			} else if (getUseragent()=='pcweb') { 
				$(".sys-right-panel").load("appFormTableController.do?commonFormEdit&viewType=detail&businessKey=${businessKey!''}&formCode=${formCode!''}");
			}
		});
	},
	//发布到云盘回调
	publishToDisk:function(params){
		if(params[0]&&params[0].id){
			var typeId=params[0].id;
			var otherKey="${((parentBusinessKey!'')!='')?string(parentBusinessKey!'',businessKey)}";
			var otherKeyType="${mainFormCode!''}";
			ajax("attachController.do?doPublishToDisk",function(data){
				//调用传阅逻辑
				commonFormEdit.selectNotify();
			},{
				data:{
					typeId:typeId,
					otherKey:otherKey,
					otherKeyType:otherKeyType
				}
			});
		}else{
			simpleCMD.tip("未选择发布文件夹");
		}
	}
};

var mapViewIframe=$("#mapViewIframe");
var selectUserModal=$("#selectUserModal",".sys-right-panel");
var mapViewModal=$("#mapViewModal",".sys-right-panel");
var modalTitle=$(".modal-title",selectUserModal);
var allUserUL=$("#userList",selectUserModal);
var selectedUserUL=$("#selectedUser ul",selectUserModal);
var selected_count = $(".selected-count",selectUserModal);
var multiplePersonSelect=false;//是否可多选
var containsSelf=true;//是否包含自己
var repeatPersonSelect=false;//是否可重复选人
var needArrow=true;//选人是否需要箭头(流程的感觉)
var timer;
$(function(){
	if(mobileContext == "body"){
		$(".common-form .common-form-container").css("height", "auto");
		$("body").css("background", "#f2f2f2");
	}else{
		home.loadSlimScroll([ {
			obj : $('.common-form-content')
		}]);
	}
	

	//处理手机和pc端独特的需求
	<#if isMobile>
	//拨打电话交互
	$(".phoneIcon",$(mobileContext)).off().on("click",function(){
		var phoneNumber=$(this).closest(".form-field-box").find("input[name]").val();
		if(phoneNumber&&phoneNumber.length>0){
			phone.call(phoneNumber);
		}else{
			simpleCMD.tip("请输入号码再拨打");
		}
	});
	
	//多行输入框查看更多交互
	$(".viewMoreIcon",$(mobileContext)).off().on("click",function(){
		var title=$(this).closest(".form-field-box").find("label").html();
		var content=$(this).closest(".form-field-box").find("textarea[name]").val();
		if(content&&content.length>0){
			var params='{"title":"'+title+'","content":"'+content+'"}';
			inputControl.viewMore(params);
		}else{
			simpleCMD.tip("请输入内容");
		}
	});
	<#else>
	$(".form-detail[data-flag!='empty'] [type=file],.common-form-content>.form-file [type=file]").each(function(i,e){
		commonFormEdit.initUploadifive(this);
	});
	
	//输入0.5秒后才进行查询
	$("#searchUser").off().on("input propertychange",function(){
		clearTimeout(timer);
		timer=setTimeout(function(){
			var key = $("#searchUser").val();
			ajax('orgGroupController.do?queryUsersByLikeKey&key='+key,function(result){
				allUserUL.empty();
				initPersonUl(result.obj);
			});
		},500);
    });
    
    $('textarea[autoHeight]').autoHeight();
    
    //位置确定完成选择事件
	$("#mapViewConfirm",mapViewModal).off().on("click", function() {
		var mapAddress=mapViewIframe[0].contentWindow.document.getElementById("mapAddress").value;
		var lonAndLat=mapViewIframe[0].contentWindow.document.getElementById("lonAndLat").value;
		mapViewIframe[0].contentWindow.jsonObj.mapAddress=mapAddress;
		mapViewIframe[0].contentWindow.jsonObj.lonAndLat=lonAndLat;
		map.select_callback(mapViewIframe[0].contentWindow.jsonObj);
	});
	
		<#if buttonType=="startFlow"&&isStartAssign==1>
		//加载待选人
		ajax("orgGroupController.do?queryUsersByWork",function(result){
			if(result.success){
				allUserUL.empty();
				initPersonUl(result.obj);
			}
		});
		</#if>
		
		<#if buttonType=="startFlow"&&isApp==0>
		//关联模板启动时,要修改按钮事件与显示
		$("#leftName").off().on("click",function(){
			//先跳转回待办页
			$(".sys-right-panel").load("appFormTableController.do?commonFormEdit&businessKey=${parentBusinessKey!''}&formCode=${mainFormCode!''}");
			//再修改左按钮事件
			$("#leftName").off().on("click",function(){
				//$("#rightName").text("");
				//$("#rightName").html("");
				//$("#appList").attr("data-type",0);
				//$("#relaFlFoUl").empty();
				$(".sys-right-panel").load("appFormTableController.do?fieldData&formCode=${mainFormCode!''}");
			});
		});
		</#if>
	</#if>
	
	//有无记录的明细类时,给明细默认Id
	$(".form-detail[data-flag!='empty']").find("[flag=subId]").each(function(){
		if($(this).val().length==0){
			$(this).val(Math.uuid());
		}
	});
	$(".form-detail i.delete").off().on("click", function(){
		var $thisDetail = $(this).closest(".form-detail"),
			$thisDetailCid = $thisDetail.attr("data-cid"),
			$detailBottomClone = $(".detail-bottom:last").clone(true);
		
		$thisDetail.remove();
		
		var $thisDetailGroup = $(".common-form-content > .form-detail[data-flag!='empty'][data-cid='"+$thisDetailCid+"']");
		$thisDetailGroup.find(".detail-bottom").remove();
		$detailBottomClone.appendTo($thisDetailGroup.last());
		
		commonFormEdit.updateDetailIndex($thisDetailGroup);
		commonFormEdit.loadMobi($thisDetailGroup);
	});
	$(".form-detail a.clone").off().on("click", function(){
		var $thisDetail = $(this).closest(".form-detail"),
			$thisDetailCid = $thisDetail.attr("data-cid"),
			thisDetailClone;
		if($thisDetail.attr("data-flag")&&$thisDetail.attr("data-flag")=="empty"){
			//主表
			$thisDetailClone=$thisDetail.clone(true).removeClass("hidden").removeAttr("data-flag");
		}else{
			//从表
			$thisDetailClone = $thisDetail.siblings(".form-detail[data-flag='empty'][data-cid='"+$thisDetailCid+"']").clone(true).removeClass("hidden").removeAttr("data-flag");
			$thisDetailClone.find("i.delete").removeClass("hidden");
		}
		var	$detailBottomClone = $thisDetail.children(".detail-bottom").clone(true),
			$thisDetailGroup = $(".common-form-content > .form-detail[data-flag!='empty'][data-cid='"+$thisDetailCid+"']");
			$fileEle=$("[type=file]",$thisDetailClone);
		$thisDetailClone.find("[flag=subId]").val(Math.uuid());
		$thisDetailClone.find("[flag=editType]").val("add");
		$thisDetailClone.insertAfter($thisDetail);
		$thisDetailGroup = $(".common-form-content > .form-detail[data-flag!='empty'][data-cid='"+$thisDetailCid+"']");
		$thisDetailGroup.find(".detail-bottom").remove();
		$detailBottomClone.appendTo($thisDetailGroup.last());
		
		commonFormEdit.updateDetailIndex($thisDetailGroup);
		commonFormEdit.loadMobi($thisDetailGroup);
		<#if !isMobile>
		$fileEle.each(function(){
			commonFormEdit.initUploadifive(this);
		});
		
		</#if>
	});
	
	//伸缩显示/隐藏用户列表
	$(".toggle-btn",mobileContext).on("click", function(){
		var $this = $(this),
		$thisParent = $this.parent(".content");
		
		$thisParent.toggleClass("open");
		
		$this.text($thisParent.hasClass("open") ? "隐藏" : "展开");
	});
	
	//渲染单选框、多选框、时间选择控件(1.先渲染mobiscroll)
	commonFormEdit.loadMobi();
	
	//新增页 没有记录的明细需要初始化第一条(2.再出现明细)
	<#list needInitTables?split(",") as needInitTable>
		var detailItem=$("[data-tablename=${needInitTable}]");
		$("a.clone",detailItem).click();
	</#list>
	
	//执行页面禁用逻辑(3.再禁用页面逻辑)
	<#if buttonType=='nextProcess'>
		<#list disabledFieldsList as disabledField>
			var fieldBox = $("[data-cid=${disabledField['cid']}]",mobileContext);
			var cType="${disabledField['cType']!""}";
			formPageReadonly(fieldBox,cType);
		</#list>
	<#elseif viewType=='detail'||viewType=='viewProcess'>
		$("[data-cid]:visible",mobileContext).each(function(){
			var cid=$(this).data("cid");
			var ctype=$(this).data("ctype");
			formPageReadonly(this,ctype);
		});
	</#if >
	
	//流程类型表单结束执行的逻辑
	<#if buttonType=='view'>
		<#if flowStatus==2||flowStatus==3>
			//标识完成的章
			$(".common-form",mobileContext).addClass("finished");
		</#if>
	</#if>
	
	//加载colorbox
	$(".colorbox-ele").each(function(){
		commonFormEdit.loadColorbox(this);
	});
	
	//页面校验逻辑
	$("#mainForm").html5Validate($.noop, {
	    novalidate: true
	});
});
function initPersonUl(result){
	$.each(result, function(i, item) {
		var userInfo={
			id : item.id,
			name : item.name,
			portrait : item.portrait,
			sortKey : item.sortKey,
			searchKey : item.searchKey
    	};
		var portrait=item.portrait ? '${attachForeRequest}' + item.portrait : 'basic/img/avatars/avatar_80.png';
		if(containsSelf){
			var templi = $("<li id='"+item.id+"'>"
					+"<div class='avatar'><img src='"+portrait+"'></div>"
					+"<div class='name'><p class='text-overflow' title='"+item.name+"'>"+item.name+"</p></div>"
					+"</li>");
			templi.data(userInfo);		
			allUserUL.append(templi);
		}else{
			if('${user_id}'!=item.id){
				var templi = $("<li id='"+item.id+"'>"
					+"<div class='avatar'><img src='"+portrait+"'></div>"
					+"<div class='name'><p class='text-overflow' title='"+item.name+"'>"+item.name+"</p></div>"
					+"</li>");
				templi.data(userInfo);		
				allUserUL.append(templi);
			}
		}
	});
	
    //选人弹框中，左侧所有人列表项点击添加到右侧
    $(".person-list.all li").off().on("click",function(){
   		if(!multiplePersonSelect&&$("li",selectedUserUL).length>=1){
   			alert("只能选择一人");
   		}else{
   			if(!repeatPersonSelect&&$("li#"+this.id,selectedUserUL)[0]){
   				alert("已选择["+$(this).data("name")+"]");
   				return;
   			}
   			var $thisLi = $("<li id='"+this.id+"'>"+
	    			"<div class='item-person'>"+
	    			$(this).html()+
	    			"</div>"+
	    			"<i class='glyphicon glyphicon-remove'></i>"+
	    			(needArrow?"<i class='glyphicon glyphicon-arrow-right'></i>":"")+
	    			"</li>").click(function(){
	    				selected_count.html($("li",selectedUserUL).length);
	    				$(this).closest("li").remove();
	    			});
    		selectedUserUL.append($thisLi);
    		selected_count.html($("li",selectedUserUL).length);
   		}
    });
    $("li .glyphicon-remove",selectedUserUL).on("click", function(){
    	$(this).closest("li").remove();
    	selected_count.html($("li",selectedUserUL).length);
    });
}

//选择动态提交人的候选人
function selectCandidate(ele){
	//选人与部门
	var config='{"callbackKey":"commonFormEdit.assign","multiple":true,"needArrow":true,"showType":"contact,org,role"}';
	if (getUseragent()=='iphone') {
		selectPerson.select(config);
	} else if (getUseragent()=='android') {
		selectPerson.select(config);
	} else if (getUseragent()=='pcweb'){
		$this=$(ele);
		var pcConfig={
			title:"选择动态审批人",
			containsSelf:true,
			repeatSelect:true,
			needArrow:true
		};
		pcConfig=$.extend({},parseJSON(config),pcConfig);
		selectMulType.goSelectPage(pcConfig);
	}
}

//点击移除候选人
function removeCandidate(ele){
	$(ele).remove();
}

//保存数据方法
function saveAndProcess(type,voteAgree){
	if(!$.html5Validate.isAllpass($("#mainForm"))){
		return;
	};
	<#if isStartAssign==1&&buttonType=='startFlow'>
	//先保存动态审批人
	var selectedUserLis=$("[flag=candidateDiv] ul li[flag!=addAssign]");
	var finalValue=[];
	if(selectedUserLis.length>0){
		$.each(selectedUserLis,function(i,item){
			var mulResult=new Object();
			mulResult.id=$(item).data("id");
			mulResult.name=$(item).data("name");
			mulResult.type=$(item).data("type");
			finalValue[i]=mulResult;
		});
	}else{
		simpleCMD.tip("请先设置动态审批人");
		return;
	}
	$("#assignResult").val(JSON.stringify(finalValue));
	</#if>
	
	$(".common-form-content > .form-detail[data-flag='empty']").remove();
	home.handleLoading(true);
	$.ajax({
		cache : false,
		type : 'POST',
		dataType : 'json',
		url : "appFormTableController.do?saveOrUpdate",
		data : $("#mainForm").serialize(),
		async : false,
		success : function(data) {
			var d = parseJSON(data);
			if (d.success) {
				//保存成功才处理流程
				if(type){
					if(type=='saveAndSend'){
						commonFormEdit.selectNotify();
					}else if(type=='startFlow'||type=='tran'||type=='nextProcess'||type=='revoke'){
						execute(type,voteAgree);
					}
				}else{
					if (getUseragent()=='iphone') {
						simpleCMD.tip("保存成功");
						simpleCMD.back();
					} else if (getUseragent()=='android') {
						simpleCMD.tip("保存成功");
						simpleCMD.back();
					} else if (getUseragent()=='pcweb'){
						alert("保存成功");
						//跳转到数据列表
						$(".sys-right-panel").load("appFormTableController.do?fieldData&formCode=${formCode!''}");
					}
				}
			}else{
				simpleCMD.tip(d.msg);
			}
			home.handleLoading(false);
		}
	});
}

//流程处理方法
function execute(type,voteAgree){
	if(type!="tran"){
		if(!confirm("确认继续提交?")){
			return;
		}
	}
	var taskId="${taskId!''}";
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
			dataType : 'json',
			url : "taskController.do?complete&taskId=${taskId!''}&businessKey=${businessKey!''}&isStartAssign=${isStartAssign!''}&voteAgree=" + voteAgree,
			data : "voteContent="+voteContent,
			async : false,
			success : function(data) {
				var d = parseJSON(data);
				if (d.success) {
					if (getUseragent()=='iphone') {
						simpleCMD.tip("提交成功");
						simpleCMD.back();
					} else if (getUseragent()=='android') {
						simpleCMD.tip("提交成功");
						simpleCMD.back();
					} else if (getUseragent()=='pcweb'){
						alert("提交成功");
						//主模板则跳到待办页
						$(".sys-right-panel").load("flowFormController.do?commission");
					}
				}else{
					simpleCMD.tip(d.msg);
				}
				home.handleLoading(false);
			}
		});
	}else if(type=="tran"){
		var tranPerson=$("#tranPerson").val();
		if(tranPerson&&tranPerson.length==0){
			simpleCMD.tip("请先选择审批人");
			return;
		}
		var description=$("#voteContent").val();
		$.ajax({
			cache : false,
			type : 'POST',
			dataType : 'json',
			url : "taskExeController.do?assignSave&taskId=${taskId!''}&businessKey=${businessKey!''}&isStartAssign=${isStartAssign!''}&exeUserId="+tranPerson+"&description="+description,
			async : false,
			success : function(data) {
				var d = parseJSON(data);
				if (d.success) {
					if (getUseragent()=='iphone') {
						simpleCMD.tip("提交成功");
						simpleCMD.back();
					} else if (getUseragent()=='android') {
						simpleCMD.tip("提交成功");
						simpleCMD.back();
					} else if (getUseragent()=='pcweb'){
						alert("提交成功");
						selectUserModal.modal("hide");
						//网页则跳到待办页
						$(".sys-right-panel").load("flowFormController.do?commission");
					}
				}else{
					simpleCMD.tip(d.msg);
				}
				home.handleLoading(false);
			}
		});
	}else if(type=="startFlow"){
		$.ajax({
 			cache : false,
 			type : 'POST',
 			dataType : 'json',
 			url : "taskController.do?startFlow&actDefId=${actDefId!''}&businessKey=${businessKey!''}&formCode=${formCode!''}&parentBusinessKey=${parentBusinessKey!''}&businessName=${businessName!''}&isStartAssign=${isStartAssign!''}",
 			async : false,
 			success : function(data) {
 				var d = parseJSON(data);
 				if (d.success) {
 					if (getUseragent()=='iphone') {
 						simpleCMD.tip("提交成功");
 						if(${isApp}==0){
							simpleCMD.lastPageRefresh();
							simpleCMD.relationTemplateRefresh();
							simpleCMD.back();
						}else{
							simpleCMD.goDataHistory();
						}
					} else if (getUseragent()=='android') {
						simpleCMD.tip("提交成功");
						if(${isApp}==0){
							simpleCMD.lastPageRefresh();
							simpleCMD.relationTemplateRefresh();
							simpleCMD.back();
						}else{
							simpleCMD.goDataHistory();
						}
					} else if (getUseragent()=='pcweb'){
						alert("提交成功");
						if(${isApp}==0){
							//关联模板启动要回到主模板查看页
							$(".sys-right-panel").load("appFormTableController.do?commonFormEdit&businessKey=${parentBusinessKey!''}&formCode=${mainFormCode!''}");
						}else{
							//主模板则跳到待办页
							$(".sys-right-panel").load("appFormTableController.do?fieldData&formCode=${formCode!''}");
						}
					}
 					
 				}else{
					simpleCMD.tip(d.msg);
				}
 				home.handleLoading(false);
 			}
 		});
	}else if(type=="revoke"){
		var revokeOpinion=$("#revokeOpinion").val();
		if(!revokeOpinion||revokeOpinion==""){
			if(!confirm("您还未输入撤回原因,是否继续?")){
				return false;
			}	
		}
		ajax("taskController.do?endProcess&businessKey=${businessKey!''}&result="+revokeOpinion,function(data){
			var d = parseJSON(data);
			if (d.success) {
				if (getUseragent()=='iphone') {
					simpleCMD.tip("撤回成功");
					simpleCMD.back();
				} else if (getUseragent()=='android') {
					simpleCMD.tip("撤回成功");
					simpleCMD.back();
				} else if (getUseragent()=='pcweb'){
					alert("撤回成功");
					//主模板则跳到待办页
					$(".sys-right-panel").load("flowFormController.do?commission");
				}
			}else{
				simpleCMD.tip(d.msg);
			}
		});
	}
}
</script>
