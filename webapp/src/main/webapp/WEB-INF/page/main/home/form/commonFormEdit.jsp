<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />

<script type="text/javascript">
window.jQuery || document.write(
	'<link href="basic/css/bootstrap.min.css" rel="stylesheet" \/>'
	+'<link href="basic/css/font-awesome.min.css" rel="stylesheet" \/>'
	+'<link href="basic/css/weather-icons.min.css" rel="stylesheet" \/>'
	+'<link href="basic/css/bootstrap-switch.min.css" rel="stylesheet" \/>'
	+'<link href="basic/css/beyond.min.css" rel="stylesheet" \/>'
	+'<link href="basic/css/beyond-rewrite.css" rel="stylesheet" \/>'
	//+'<link href="basic/css/mobiscroll.custom-2.17.0.min.css" rel="stylesheet" \/>'
	+'<link href="plug-in/jquery-fileupload/css/jquery.fileupload-ui.css" rel="stylesheet" \/>'
	+'<link href="plug-in/jquery-fileupload/css/jquery.fileupload.css" rel="stylesheet" \/>'
	//+'<link href="basic/css/common.css" rel="stylesheet" \/>'
	//+'<link href="basic/css/index.css" rel="stylesheet" \/>'
	+'<link href="basic/css/home.common.css" rel="stylesheet" \/>'
	+'<link href="basic/css/commonForm.css" rel="stylesheet" \/>'
	//+'<link href="basic/css/mobiscroll.custom-2.17.0.min.css" rel="stylesheet" \/>'
);
</script>

<link href="basic/css/mobiscroll.custom-2.17.0.min.css" rel="stylesheet" />
<link href="basic/css/colorbox.css" rel="stylesheet" />
<form name="form1" id="form1">
<div class="common-form">
	<button type="button" id="selectPerson" onclick="selectPerson.select()">选人</button>
	<button type="button"  id="selectPerson" onclick="showSelect()">pc选人</button>
	<button type="button"  id="selectAttachment" onclick="attachment.select()">附件</button>
	<button type="button"  id="phoneCall" onclick="phone.call(13428951050)">拨通电话</button>
	<button type="button"  id="getAddress" onclick="gps.getInfo()">获取地址</button>
	<button type="button"  id="back" type="button" onclick="simpleCMD.back()">回退</button>
	<button type="button"  id="assignSubmit" type="button" onclick="assignSubmitFun()">回退</button>

<!-- 选人弹框 -->
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
						<span>选择成员：</span>
						<div class="search">
							<input type="text" id="searchUser" place-holder="搜索成员"  > <i class="glyphicon glyphicon-search"></i>
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
						<span>新添成员：</span> <span class="pull-right">已选<span class="selected-count">0</span>人
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
	<div class="common-form-container">
		<div class="common-form-content">
			<div class="form-field-box form-selectuser clearfix" flag="candidateDiv">
				<div class="top clearfix">
					<label>审批人<span class="light-grey font-size-12">（点击头像可删除）</span>：</label>
				</div>
				<div class="content">
					<ul class="list-style-none-h user-group">
						<li class="read">
							<img class="avatar" src="basic/img/background_home.jpg">
							<span class="name text-overflow">李四四</span>
							<span class="read"><i class="fa fa-check"></i></span>
						</li>
						<li>
							<img class="avatar" src="basic/img/background_home.jpg">
							<span class="name text-overflow">李四四</span>
							<span class="read"><i class="fa fa-check"></i></span>
						</li>
						<li>
							<img class="avatar" src="basic/img/background_home.jpg">
							<span class="name text-overflow">李四四</span>
							<span class="read"><i class="fa fa-check"></i></span>
						</li>
						<li>
							<img class="avatar" src="basic/img/background_home.jpg">
							<span class="name text-overflow">李四四</span>
							<span class="read"><i class="fa fa-check"></i></span>
						</li>
						<li class="read">
							<img class="avatar" src="basic/img/background_home.jpg">
							<span class="name text-overflow">李四四</span>
							<span class="read"><i class="fa fa-check"></i></span>
						</li>
						<li>
							<img class="avatar" src="basic/img/background_home.jpg">
							<span class="name text-overflow">李四四</span>
							<span class="read"><i class="fa fa-check"></i></span>
						</li>
						<li onclick="selectCandidate(this)" flag="addAssign">
							<div class="btn-add">
								<!-- 
								<i class="fa fa-plus"></i>
								 -->
								<img src="basic/img/group_member_add.png" alt="增加">
							</div>
						</li>
					</ul>
					<div class="toggle-btn">展开</div>
				</div>
			</div>
			<div class="form-field-box form-history clearfix">
				<div class="top clearfix">
					<label>审批历史：</label>
				</div>
				<div class="content">
					<br><br>
				</div>
			</div>
			<div class="form-field-box seperator"></div>
			<div class="form-field-box form-option-edit clearfix">
				<div class="top clearfix">
					<label>编辑选项</label>
				</div>
				<div class="content">
					<div class="form-field-box form-option clearfix">
						<span class="my-checkbox">
							<input class="option-check" type="checkbox">
							<span class="text"></span>
						</span>
						<input class="option-text" type="text">
						<div class="option-btn">
							<button><img src="basic/img/iconfont-plus.png" alt="加"></button>
							<button><img src="basic/img/iconfont-minus.png" alt="减"></button>
						</div>
					</div>
					<div class="form-field-box form-option clearfix">
						<span class="my-checkbox">
							<input class="option-check" type="checkbox">
							<span class="text"></span>
						</span>
						<input class="option-text" type="text">
						<div class="option-btn">
							<button><img src="basic/img/iconfont-plus.png" alt="加"></button>
							<button><img src="basic/img/iconfont-minus.png" alt="减"></button>
						</div>
					</div>
					<div class="form-field-box form-option clearfix">
						<span class="my-checkbox">
							<input class="option-check" type="checkbox">
							<span class="text"></span>
						</span>
						<input class="option-text" type="text">
						<div class="option-btn">
							<button><img src="basic/img/iconfont-plus.png" alt="加"></button>
							<button><img src="basic/img/iconfont-minus.png" alt="减"></button>
						</div>
					</div>
				</div>
			</div>
			<div class="form-field-box seperator"></div>
			<div class="form-field-box form-text clearfix disabled">
				<label>单行输入：</label>
				<input type="text" placeholder="请输入内容" required disabled/>
			</div>
			<div class="form-field-box form-paragraph clearfix">
				<label>多行输入：</label>
				<textarea autoHeight="true" placeholder="请输入内容">asdqweqweqwe</textarea>
			</div>
			<div class="form-field-box form-number clearfix">
				<label for="a[0].a">数字：</label>
				<input id="a[0].a" type="text" placeholder="请输入数字" required onkeyup="return noNumbers(this,0)"/>
			</div>
			<div class="form-field-box form-number clearfix">
				<label for="test_t">数字千分位：</label>
				<input id="test_t" type="text" placeholder="请输入数字" required/>
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
				<input type="text" placeholder="请输入身份证号" pattern="^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$" >
			</div>
			<div class="form-field-box form-date clearfix">
				<label>日期：</label>
				<input type="text" required/>
				<i class="fa fa-angle-right"></i>
			</div>
			<div class="form-field-box form-time clearfix">
				<label>时间：</label>
				<input type="text"  />
				<i class="fa fa-angle-right"></i>
			</div>
			<div class="form-field-box form-phone clearfix">
				<label>电话：</label>
				<input placeholder="请输入号码" type="text" />
				<i class="fa fa-phone phoneIcon"></i>
			</div>
			<div class="form-field-box form-position clearfix">
				<label>位置：</label>
				<p></p>
				<i class="fa fa-map-marker"></i>
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
			<div class="form-field-box form-check clearfix">
				<label>单个复选框：</label>
				<span class="my-checkbox">
					<input class="field-checkbox" type="checkbox">
					<span class="text"></span>
				</span>
			</div>
			<div class="form-field-box form-file clearfix">
				<div class="top clearfix">
					<label>附件：</label>
					<span class="btn-add">
						<img src="basic/img/icon_add_orange2.png" alt="增加">
					</span>
				</div>
				<div class="content">
					<ul class="list-style-none">
						<li class="file-item type-img" data-url="">
							<div class="file-left">
								<a class="colorbox-ele" href="basic/img/background_home.jpg" title="图片1">
									<img src="basic/img/background_home.jpg" />
								</a>
							</div>
							<p class="file-name">135465743213216576897.jpg</p>
							<p class="file-size">235.33KB</p>
							<p class="file-time">2015-07-02 15:32</p>
							<button class="btn-del"><i class="fa fa-close"></i></button>
						</li>
						<li class="file-item type-img" data-url="">
							<div class="file-left">
								<a class="colorbox-ele" href="http://120.24.160.161/attachController.do?downloadFile&aId=f898202150caf28e0150cb134e0f0027" title="图片2">
									<img src="http://120.24.160.161/attachController.do?downloadFile&aId=f898202150caf28e0150cb134e0f0027" />
								</a>
							</div>
							<p class="file-name">135465743213216576897.jpg</p>
							<p class="file-size">235.33KB</p>
							<p class="file-time">2015-07-02 15:32</p>
							<button class="btn-del"><i class="fa fa-close"></i></button>
						</li>
						<li class="file-item file type-audio" data-url="">
							<div class="file-left">
								<a class="colorbox-ele media" href="http://www.w3school.com.cn/i/song.mp3" title="音频1">
									<audio class="media-ele" controls="controls">
										<source src="http://www.w3school.com.cn/i/song.ogg" type="audio/ogg">
										<source src="http://www.w3school.com.cn/i/song.mp3" type="audio/mpeg">
										<span>您的浏览器不支持HTML5的音频功能。</span>
									</audio>
									<div class="file-type-icon">
										<!-- 
										<i class="fa fa-file-sound-o file-icon"></i>
										 -->
										<img class="file-icon" src="basic/img/attachment/audio.png" alt="音频">
									</div>
								</a>
							</div>
							<p class="file-name">135465743213216576897.jpg</p>
							<p class="file-size">235.33KB</p>
							<p class="file-time">2015-07-02 15:32</p>
							<button class="btn-del"><i class="fa fa-close"></i></button>
						</li>
						<li class="file-item type-video" data-url="">
							<div class="file-left">
								<a class="colorbox-ele media" href="http://localhost/attachController.do?downloadFile&aId=402881b5518983ac01518990b1780000" title="视频1">
									<video class="media-ele">
									    <source src="http://localhost/attachController.do?downloadFile&aId=402881b5518983ac01518990b1780000" type="video/mp4" />
										<span>您的浏览器不支持HTML5的视频功能。</span>
								    </video>
								</a>
							</div>
							<p class="file-name">135465743213216576897.jpg</p>
							<p class="file-size">235.33KB</p>
							<p class="file-time">2015-07-02 15:32</p>
							<button class="btn-del"><i class="fa fa-close"></i></button>
						</li>
						<li class="file-item file type-word" data-url="">
							<div class="file-left">
								<div class="file-type-icon">
									<img class="file-icon" src="basic/img/attachment/word.png" alt="word">
								</div>
							</div>
							<p class="file-name">135465743213216576897.jpg</p>
							<p class="file-size">235.33KB</p>
							<p class="file-time">2015-07-02 15:32</p>
							<button class="btn-del"><i class="fa fa-close"></i></button>
						</li>
						<li class="file-item file type-excel" data-url="">
							<div class="file-left">
								<div class="file-type-icon">
									<img class="file-icon" src="basic/img/attachment/excel.png" alt="excel">
								</div>
							</div>
							<p class="file-name">135465743213216576897.jpg</p>
							<p class="file-size">235.33KB</p>
							<p class="file-time">2015-07-02 15:32</p>
							<button class="btn-del"><i class="fa fa-close"></i></button>
						</li>
						<li class="file-item file type-ppt" data-url="">
							<div class="file-left">
								<div class="file-type-icon">
									<img class="file-icon" src="basic/img/attachment/powerpoint.png" alt="ppt">
								</div>
							</div>
							<p class="file-name">135465743213216576897.jpg</p>
							<p class="file-size">235.33KB</p>
							<p class="file-time">2015-07-02 15:32</p>
							<button class="btn-del"><i class="fa fa-close"></i></button>
						</li>
						<li class="file-item file type-pdf" data-url="">
							<div class="file-left">
								<div class="file-type-icon">
									<img class="file-icon" src="basic/img/attachment/pdf.png" alt="pdf">
								</div>
							</div>
							<p class="file-name">135465743213216576897.jpg</p>
							<p class="file-size">235.33KB</p>
							<p class="file-time">2015-07-02 15:32</p>
							<button class="btn-del"><i class="fa fa-close"></i></button>
						</li>
						<li class="file-item file type-other" data-url="">
							<div class="file-left">
								<div class="file-type-icon">
									<img class="file-icon" src="basic/img/attachment/other.png" alt="other">
								</div>
							</div>
							<p class="file-name">135465743213216576897.abc</p>
							<p class="file-size">235.33KB</p>
							<p class="file-time">2015-07-02 15:32</p>
							<button class="btn-del"><i class="fa fa-close"></i></button>
						</li>
					</ul>
				</div>
			</div>
			<!-- 
			<div class="form-field-box form-selectuser clearfix">
				<label>选择用户：</label>
				<input type="text"  />
				<i class="fa fa-angle-right"></i>
			</div>
			<div class="form-field-box seperator clearfix"></div>
			 -->
			
			<div class="form-field-box form-detail clearfix hidden" data-cid="c5" data-flag="empty">
				<div class="detail-top">
					<label>报销<span class="detailIndex">(0)</span>：</label>
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
				</div>
				<div class="detail-bottom">
					<a class="clone">增加明细</a>
				</div>
			</div>
			<div class="form-field-box form-detail clearfix" data-cid="c5">
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
				</div>
			</div>
			<div class="form-field-box form-detail clearfix" data-cid="c5">
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
				</div>
				<div class="detail-bottom">
					<a class="clone">增加明细</a>
				</div>
			</div>
			<div class="form-field-box form-detail clearfix hidden" data-cid="c10" data-flag="empty">
				<div class="detail-top">
					<label>请假<span class="detailIndex">(1)</span>：</label>
					<i class="fa fa-trash-o delete hidden"></i>
				</div>
				<div class="detail-content">
					<div class="form-field-box form-time clearfix">
						<label>时间：</label>
						<input name="a[1].2" type="text" id="time1" class="mobiEle"/>
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
				</div>
				<div class="detail-bottom">
					<a class="clone">增加明细</a>
				</div>
			</div>
			<div class="form-field-box form-detail clearfix" data-cid="c10">
				<div class="detail-top">
					<label>请假<span class="detailIndex">(1)</span>：</label>
					<i class="fa fa-trash-o delete hidden"></i>
				</div>
				<div class="detail-content">
					<div class="form-field-box form-time clearfix">
						<label>时间：</label>
						<input name="a[1].2" type="text" id="time1" class="mobiEle"/>
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
				</div>
				<div class="detail-bottom">
					<a class="clone">增加明细</a>
				</div>
			</div>
			<div class="form-field-box form-btn-area clearfix">
				<div class="form-field-box form-btn clearfix" data-url="appFormTableController.do?commonFormEdit2">
					<label>立即申请</label>
					<i class="fa fa-angle-right"></i>
				</div>
				<div class="form-field-box form-btn clearfix" data-url="appFormTableController.do?commonFormEdit2">
					<label>尽职调查</label>
					<i class="fa fa-angle-right"></i>
				</div>
			</div>
			<!-- 底部按钮区域 -->
			<div class="form-field-box form-bottom-bar clearfix">
				<c:out value="${buttonType}"></c:out>
				<c:out value="${taskId}"></c:out>
				<c:if test="${buttonType=='startFlow'}">
					<input class="btn-bottom" type="button" onclick="execute('startFlow')" value="提交"/>
				</c:if>
				<c:if test="${buttonType=='nextProcess'}">
					<form class="form-bottom" id="approveForm" action="">
						<div class="person-box">
							<ul class="list-style-none-h person-list">
								<li><img src="basic/img/avatars/avatar_80.png" alt="头像"></li>
								<li><img src="basic/img/avatars/avatar_80.png" alt="头像"></li>
								<li><img src="basic/img/avatars/avatar_80.png" alt="头像"></li>
								<li><img src="basic/img/avatars/avatar_80.png" alt="头像"></li>
							</ul>
						</div>
						<textarea id="voteContent" name="voteContent">${opinion}</textarea>
						<ul class="list-style-table btn-bottom-3">
							<li>
								<input type="button" onclick="execute('nextProcess','1')" value="同意"/>
							</li>
							<li>
								<input type="button" onclick="execute('nextProcess','2')" value="反对"/>
							</li>
							<li>
								<input type="button" onclick="commonFormEdit.selectTran()" value="转交"/>
							</li>
						</ul>
					</form>
					<input type="hidden" id="tranPerson"/>
				</c:if>
					<input class="btn-bottom" type="button" onclick="execute('save')" value="保存"/>
				<c:if test="${buttonType=='save'}">
				</c:if>
			</div>
		</div>
		
	</div>
	<ul class="timeline">
		<li class="timeline-node"><a class="btn" style="border: 0px">2015年12月23日</a></li>
		<li><div class="timeline-datetime"></div>
			<div class="timeline-badge">
				<img style="border-radius: 50px" width="50" height="50"
					src="basic/img/avatars/avatar_80.png">
			</div>
			<div class="timeline-panel bordered-right-3 ">
				<div>
					<div class="timeline-title"
						style="float: left; padding: 5px 0 0 10px">隔壁老王</div>
					<div class="timeline-executeTime"
						style="float: right; padding: 5px 10px 0 0">18:28</div>
				</div>
				<div>
					<div class="timeline-approve"
						style="clear: both; padding: 5px 10px 0 10px">审核中</div>
				</div>
				<div class="timeline-body">
					<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>
				</div>
			</div>
		</li>
		<li><div class="timeline-datetime"></div>
			<div class="timeline-badge">
				<img style="border-radius: 50px" width="50" height="50"
					src="basic/img/avatars/avatar_80.png">
			</div>
			<div class="timeline-panel bordered-right-3 ">
				<div>
					<div class="timeline-title"
						style="float: left; padding: 5px 0 0 10px">隔壁老王</div>
					<div class="timeline-executeTime"
						style="float: right; padding: 5px 10px 0 0">18:28</div>
				</div>
				<div>
					<div class="timeline-approve"
						style="clear: both; padding: 5px 10px 0 10px">审核中</div>
				</div>
				<div class="timeline-body">
					<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>
				</div>
			</div>
		</li>
	</ul>
</div>
</form>
<script type="text/javascript">
var mobileContext = "body",
	mobileContext_m = mobileContext;
if(window.jQuery){
	mobileContext = ".sys-right-panel";
	mobileContext_m = $(mobileContext).closest(".content-pannel");
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
	+ '<script type="text/javascript" src="plug-in/tools/Map.js"><\/script>'
	+ '<script type="text/javascript" src="plug-in/tools/syUtil.js"><\/script>'
	+ '<script type="text/javascript" src="plug-in/tools/curdtools.js"><\/script>'
	+ '<script type="text/javascript" src="basic/js/home.js"><\/script>'
	+ '<script type="text/javascript" src="plug-in/workflow/js/approveButton.js"><\/script>'
	//+ '<script type="text/javascript" src="basic/js/mobiscroll.custom-2.17.0.min.js"><\/script>'
);
</script>

<script type="text/javascript" src="basic/js/mobiscroll.custom-2.17.0.min.js"></script>
<script type="text/javascript" src="basic/js/jquery.colorbox-min.js"></script>
<script type="text/javascript" src="basic/js/html5Validate/jquery-html5Validate.js"></script>

<script type="text/javascript">
var commonFormEdit = {
	loadMobi: function(detailGroup){
		//debugger;
		if(!detailGroup) detailGroup = $("body");
		
		detailGroup.find(".mobiEle").prev("input").remove();
		
		detailGroup.find(".form-date input").mobiscroll().date({
			lang: 'zh',
	        theme: 'mobiscroll',
	        dateFormat: 'yyyy-mm-dd',
	        display: 'bottom',
	        showLabel: true,
	        //yearText: '年',
	        context: mobileContext
	    });
		detailGroup.find(".form-time input").mobiscroll().time({
			lang: 'zh',
	        theme: 'mobiscroll',
	        display: 'bottom',
	        showLabel: true,
	        context: mobileContext
	    });
		detailGroup.find(".form-radio select").mobiscroll().select({
			lang: 'zh',
	        theme: 'mobiscroll',
	        display: 'bottom',
	        minWidth: 200,
	        context: mobileContext
	    });
		detailGroup.find(".form-checkboxes select").mobiscroll().select({
			lang: 'zh',
	        theme: 'mobiscroll',
	        display: 'bottom',
	        minWidth: 200,
	        context: mobileContext
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
	},
	
	/* 
	将数值四舍五入后格式化. 
	@param num 数值(Number或者String) 
	@param cent 要保留的小数位(Number) 
	@param isThousand 是否需要千分位 0:不需要,1:需要(数值类型); 
	@return 格式的字符串,如'1,234,567.45' 
	@type String 
	*/ 
	toThousands : function(number) {
		/*
		//方法1
		//params: n
		n += "";
		var re=/\d{1,3}(?=(\d{3})+$)/g;
		var n1=n.replace(/^(\d+)((\.\d+)?)$/,function(s,s1,s2){return s1.replace(re,"$&,")+s2;});

		return n1;
		*/
		
		/*
		//方法2
		//params: num,cent,isThousand
		num = num.toString().replace(/\$|\,/g,'');
		
		if(isNaN(num)){		//检查传入数值为数值类型.
			num = "0"; 
		} 
		if(isNaN(cent)){	//确保传入小数位为数值型数值.
			cent = 0; 
		} 
		
		cent = parseInt(cent); 
		cent = Math.abs(cent);		//求出小数位数,确保为正整数. 
		
		if(isNaN(isThousand)){		//确保传入是否需要千分位为数值类型.
			isThousand = 0; 
		} 
		
		isThousand = parseInt(isThousand); 
		
		if(isThousand < 0){
			isThousand = 0; 
		}
		
		if(isThousand >=1){		//确保传入的数值只为0或1
			isThousand = 1; 
		} 
		
		sign = (num == (num = Math.abs(num)));//获取符号(正/负数) 
		
		//Math.floor:返回小于等于其数值参数的最大整数 
		num = Math.floor(num*Math.pow(10,cent)+0.50000000001);//把指定的小数位先转换成整数.多余的小数位四舍五入. 
		cents = num%Math.pow(10,cent); //求出小数位数值. 
		num = Math.floor(num/Math.pow(10,cent)).toString();//求出整数位数值. 
		cents = cents.toString();//把小数位转换成字符串,以便求小数位长度. 
		
		while(cents.length < cent){//补足小数位到指定的位数. 
			cents = "0" + cents; 
		} 
		
		if(isThousand == 0){		//不需要千分位符.
			return (((sign)?'':'-') + num + '.' + cents); 
		}
		
		//对整数部分进行千分位格式化. 
		for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++){
			num = num.substring(0,num.length-(4*i+3))+','+num.substring(num.length-(4*i+3));
		} 
		
		return (((sign)?'':'-') + num + '.' + cents); 
		*/
		
		/* 
		//方法3
		params: number
		*/
		number += "";
		var num = number.replace(new RegExp(",","g"),""); 

		// 正负号处理 
		var symble = ""; 
		if(/^([-+]).*$/.test(num)) { 
		symble = num.replace(/^([-+]).*$/,"$1"); 
		num = num.replace(/^([-+])(.*)$/,"$2"); 
		} 

		if(/^[0-9]+(\.[0-9]+)?$/.test(num)) { 
		var num = num.replace(new RegExp("^[0]+","g"),""); 
		if(/^\./.test(num)) { 
		num = "0" + num; 
		} 

		var decimal = num.replace(/^[0-9]+(\.[0-9]+)?$/,"$1"); 
		var integer= num.replace(/^([0-9]+)(\.[0-9]+)?$/,"$1"); 

		var re=/(\d+)(\d{3})/ 

		while(re.test(integer)){ 
		integer　=integer.replace(re,"$1,$2") 
		} 
		return symble + integer　+ decimal; 

		} else { 
			return number; 
		} 
	}
	
};

var attachment={
	"select":function(params){
		commonFormEdit.controllerCenter('selectAttachment',params);
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
		if($.html5Validate.isAllpass($("#form1"))){
			alert("通过");
		}
	}	
}

var selectUserModal=$("#selectUserModal");
var allUserUL=$("#userList",selectUserModal);
var selectedUserUL=$("#selectedUser ul",selectUserModal);
var selected_count = $(".selected-count",selectUserModal);
var multiplePersonSelect=false;
var timer;

var showSelect=function(){
	//选人页面显示
	selectUserModal.modal({
		show : true
	});
	$("li",selectedUserUL).remove();
	//查询待选人
	ajax("userController.do?queryUserList",function(result){
		if(result.success){
			allUserUL.empty();
			initPersonUl(result.obj);
		}
	});
	//选人确定完成选择事件
	$("#selectTranConfirm",selectUserModal).off().on("click", function() {
		var selectingUserLis=$("li",selectedUserUL);
		//请求服务器维护成员
		if(selectingUserLis.length>0){
			var userId="";
			$.each(selectingUserLis,function(){
				userId=$(this).attr("id");
			});
			console.info(userId);
		}
	});
	
};

//根据文件分类，返回相应file class
var getFileTypeInfo = function(fileType){
	var rtn;
	switch(fileType){
	case "word":
	case "excel":
	case "pdf":
		rtn = fileType;
		break;
	case "ppt":
		rtn = "powerpoint";
		break;
	default:
		rtn = ""
	}
	
	return rtn;
}
//加载colorbox
var loadColorbox = function(ele){
	if($(ele).hasClass("media")){
		//说明是音、视频
		$(ele).colorbox({
			innerWidth:"50%", 
			innerHeight:"50%",
			inline: true,
			href: $(".media-ele", $(ele)),
			onComplete: function(){
				$("#cboxLoadedContent > video").attr("controls", "controls");
			},
			onClosed: function(){
				$(this).find("video").removeAttr("controls");
			}
		});
	}else {
		//说明是图片
		$(ele).colorbox({
			iframe: false,
			maxWidth: "70%",
			maxHeight: "50%",
			photo: true
		});
	}
}

//附件删除事件
var fileDel = function(){
	$(".form-file .btn-del").on("click", function(){
		$(this).closest(".file-item").remove();
	});
}

$(function(){
	ajax("userController.do?queryUserList",function(result){
		if(result.success){
			allUserUL.empty();
			initPersonUl(result.obj);
		}
	});
	if(mobileContext == "body"){
		$(".common-form .common-form-container").css("height", "auto");
		$("body").css("background", "#f2f2f2");
	}else{
		home.loadSlimScroll([ {
			obj : $('.common-form-content')
		}]);
	}
	$("form").html5Validate($.noop, {
	    novalidate: true
	});
	
	$(".form-detail i.delete").on("click", function(){
		var $thisDetail = $(this).closest(".form-detail"),
			$thisDetailCid = $thisDetail.attr("data-cid"),
			$detailBottomClone = $(".detail-bottom:last").clone(true);
		
		$thisDetail.remove();
		
		var $thisDetailGroup = $(".common-form-content > .form-detail[data-flag!='empty'][data-cid='"+$thisDetailCid+"']");
		$thisDetailGroup.find(".detail-bottom").remove();
		$detailBottomClone.appendTo($thisDetailGroup.last());
		
		commonFormEdit.updateDetailIndex($thisDetailGroup);
	});
	$(".form-detail a.clone").on("click", function(){
		var $thisDetail = $(this).closest(".form-detail"),
			$thisDetailCid = $thisDetail.attr("data-cid"),
			$thisDetailClone = $thisDetail.siblings(".form-detail[data-flag='empty'][data-cid='"+$thisDetailCid+"']").clone(true).removeClass("hidden").removeAttr("data-flag"), 
			$detailBottomClone = $thisDetail.children(".detail-bottom").clone(true),
			$thisDetailGroup = $(".common-form-content > .form-detail[data-flag!='empty'][data-cid='"+$thisDetailCid+"']");
		
		$thisDetailClone.find("i.delete").removeClass("hidden");
		$thisDetailClone.find("input, textarea").val("");
		$thisDetailClone.insertAfter($thisDetail); 
		$thisDetailGroup = $(".common-form-content > .form-detail[data-flag!='empty'][data-cid='"+$thisDetailCid+"']");
		$thisDetailGroup.find(".detail-bottom").remove();
		$detailBottomClone.appendTo($thisDetailGroup.last());
		
		commonFormEdit.updateDetailIndex($thisDetailGroup);
		commonFormEdit.loadMobi($thisDetailGroup);
		
	});
	//debugger;
	commonFormEdit.loadMobi();
	
	//绑定附件的添加方法
	$(".form-file .btn-add").on("click", function(){
		var jsonTemp = [
			{"id":"40288117513730df015139bdca5a0036", "sizeStr":"100KB", "name":"文件1", "iconType":"word", "url": "test/test.test"},
			{"id":"40288117513730df015139bdca5a0036", "sizeStr":"100KB", "name":"文件2", "iconType":"excel", "url": "test/test.test"},
			{"id":"40288117513730df015139bdca5a0036", "sizeStr":"100KB", "name":"文件3", "iconType":"ppt", "url": "test/test.test"},
			{"id":"40288117513730df015139bdca5a0036", "sizeStr":"100KB", "name":"文件4", "iconType":"pdf", "url": "test/test.test"},
			{"id":"40288117513730df015139bdca5a0036", "sizeStr":"100KB", "name":"文件5", "iconType":"img", "url": "basic/img/background_home.jpg"},
			{"id":"40288117513730df015139bdca5a0036", "sizeStr":"100KB", "name":"文件6", "iconType":"audio", "url": "test/test.test"},
			{"id":"40288117513730df015139bdca5a0036", "sizeStr":"100KB", "name":"文件7", "iconType":"video", "url": "test/test.test"},
			{"id":"40288117513730df015139bdca5a0036", "sizeStr":"100KB", "name":"文件8", "iconType":"other", "url": "test/test.test"}
		];
		var _this = this,fileLeftHtml = "";
		
		$.each(jsonTemp, function(i, n){
			var iconName = n.iconType=="other"?"":n.iconType+"-";
			var url='${attachForeRequest}'+n.id;
			var ifFile = (n.iconType == "img" || n.iconType == "video")?"":"file";
			switch(n.iconType){
				case "img":
					fileLeftHtml = "<a class='colorbox-ele' href='"+url+"' title='"+n.name+"'>"
						+ "<img src='"+url+"' />"
						+ "</a>";
					break;
				case "audio":
					fileLeftHtml = "<a class='colorbox-ele media' href='"+url+"' title='"+n.name+"'>"
						+ "<audio>"
						+ "<source src='"+url+"' type='audio/mpeg'>"
						+ "<span>您的浏览器不支持HTML5的音频功能。<span>"
						+ "</audio>"
						+ "<div class='file-type-icon'><i class='fa fa-file-sound-o file-icon'></i></div>"
						+ "</a>";
					break;
				case "video":
					fileLeftHtml = "<a class='colorbox-ele media' href='"+url+"' title='"+n.name+"'>"
						+ "<video>"
						+ "<source src='"+url+"' type='video/mp4' />"
						+ "<span>您的浏览器不支持HTML5的视频功能。</span>"
						+ "</video>"
						+ "</a>";
					break;
				default:
					fileLeftHtml = "<div class='file-type-icon'><i class='fa fa-file-"+iconName+"o file-icon'></i></div>";
			}
			
			var liEle=$(
					"<li id='"+n.id+"' class='file-item " + ifFile + " type-" + n.iconType + "' data-url='"+url+"'>"
					+ "<div class='file-left'>"
					+ fileLeftHtml
					+ "</div>"
					+ "<p class='file-name' data-url='"+url+"' data-name='"+n.name+"' data-id='"+n.id+"'>"+n.name+"</p>"
					+ "<p class='file-size'>"+n.sizeStr+"</p>"
					+ "<button class='btn-del'><i class='fa fa-close'></i></button>"
					+ "</li>"
				);
			//绑定文件名下载事件
			$(".file-name",liEle).on("click",function(){
				if (getUseragent()=='iphone') {
					attachment.download($(this).data("id"),$(this).data("name"));
				} else if (getUseragent()=='android') {
					attachment.download($(this).data("id"),$(this).data("name"));
				} else if (getUseragent()=='pcweb'){
					common_downloadFileByUrl($(this).data("url"));
				}
			});
			$(_this).closest(".form-file").children(".content").children("ul").append(liEle);
			if(n.iconType=="img"||n.iconType=="audio"||n.iconType=="video"){
				//初始化colorbox
				loadColorbox($(".colorbox-ele",liEle));
			}
			
			//绑定附件删除
			fileDel();
		});
	});
	//加载colorbox
	$(".colorbox-ele").each(function(){
		loadColorbox(this);
	});
	
	//绑定附件删除
	fileDel();
	
	//绑定表单按钮点击事件
	$(".form-btn-area > .form-btn").on("click", function(){
		$(this).closest(".common-form").load($(this).attr("data-url"));
	});
	
	//输入0.5秒后才进行查询
	$("#searchUser").on("input propertychange",function(){
		clearTimeout(timer);
		timer=setTimeout(function(){
			var key = encodeURI(encodeURI($("#searchUser").val()));
			ajax('userController.do?queryUsersByLikeKey&key='+key,function(result){
				allUserUL.empty();
				initPersonUl(result.obj);
			});
		},500);
    });
	
	//伸缩显示/隐藏用户列表
	$(".form-selectuser .toggle-btn").on("click", function(){
		var $this = $(this),
			$thisParent = $this.parent(".content");
		
		$thisParent.toggleClass("open");
		
		$this.text($thisParent.hasClass("open") ? "隐藏" : "展开");
	});
	
	//千分位格式化
	$("#test_t").on("keyup", function(){
		$(this).val(commonFormEdit.toThousands($(this).val()));
	});
});

//textarea高度自适应 
$.fn.autoHeight = function(){
	function autoHeight(elem){
		elem.style.height = 'auto';
		elem.scrollTop = 0; //防抖动
		elem.style.height = elem.scrollHeight + 'px';
	}
	
	this.each(function(){
		autoHeight(this);
		$(this).on('keyup', function(){
			autoHeight(this);
		});
	});
}
$('textarea[autoHeight]').autoHeight();

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

function noNumbers(obj,num) {
	$this=$(obj);
	
	//先把非数字的都替换掉，除了数字和.
    obj.value = obj.value.replace(/[^\d.]/g,"");
    //必须保证第一个为数字而不是.
    obj.value = obj.value.replace(/^\./g,"");
    //保证只有出现一个.而没有多个.
    obj.value = obj.value.replace(/\.{2,}/g,".");
    //保证.只出现一次，而不能出现两次以上
    obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
    
    var index=obj.value.indexOf(".");
    if(index >= 0 && (obj.value.length-1)-index >num){
    	obj.value = obj.value.substring(0,obj.value.length-1);
    }
}
function initPersonUl(result){
	$.each(result, function(i, item) {
		var userInfo={
			id : item.id,
			name : item.name,
			portrait : item.portrait,
			sortKey : item.sortKey,
			searchKey : item.searchKey
    	};
		var portrait=item.portraits ? '${attachForeRequest}' + item.portrait : 'basic/img/avatars/avatar_80.png';
		var templi = $("<li id='"+item.id+"'>"
					+"<div class='avatar'><img src='"+portrait+"'></div>"
					+"<div class='name'><p class='text-overflow' title='"+item.name+"'>"+item.name+"</p></div>"
					+"</li>");
		templi.data(userInfo);
		allUserUL.append(templi);
	});
	
    //选人弹框中，左侧所有人列表项点击添加到右侧
    $(".person-list.all li").on("click",function(){
   		if(!multiplePersonSelect&&$("li",selectedUserUL).length>=1){
   			alert("只能选择一人");
   		}else{
   			var $thisLi = $("<li id='"+this.id+"'>"+
	    			"<div class='item-person'>"+
	    			$(this).html()+
	    			"</div>"+
	    			"<i class='glyphicon glyphicon-remove'></i>"+
	    			"<i class='glyphicon glyphicon-arrow-right'></i>"+
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
function initSelectedPersonUl(result){
	var userId="",userName="",portrait=null;
	$.each(result,function(i,obj){
		userId=obj.id;
		userName=obj.name;
		portrait=nulls(obj.portrait);
		portrait= portrait ? '${attachForeRequest}' + portrait : 'basic/img/avatars/avatar_80.png';
		
		var tempLi=$('<li id="'+userId+'"><div class="item-person">'+
		    			'<div class="avatar"><img src="'+portrait+'"></div><div class="name"><p class="text-overflow" title="'+userName+'">'+userName+'</p></div>'+
		    			'</div><i class="glyphicon glyphicon-remove"></i><i class="glyphicon glyphicon-arrow-right"></i></li>').click(function(){
		    				selectedUserMap.remove(userId);
		    				selected_count.html(selectedUserMap.size());
		    				$(this).closest("li").remove();
		    			 });
		selectedUserUL.append(tempLi);
	});
	
	selected_count.html(selectedUserMap.size());
}
function selectCandidate(ele){
	multiplePersonSelect=true;
	home.loadSlimScroll([{
		obj : $('.person-box .person-list',mobileContext),
		width: "100%"
	}]);
	$this=$(ele);
	//选人页面显示
	selectUserModal.modal({
		show : true
	});
	selectedUserUL.empty();
	selected_count.html(0);
	//选人确定完成选择事件
	$("#selectTranConfirm",selectUserModal).off().on("click", function() {
		//清空页面已选择的
		
		var selectedUserLis=$("[flag=candidateDiv] ul li[flag!=addAssign]");
		var selectingUserLis=$("li",selectedUserUL);
		//请求服务器维护成员
		if(selectingUserLis.length>0){
			var userId="",userName="",portrait=null;
			$.each(selectingUserLis,function(){
				userId=$(this).attr("id");
				userName=$(".name p",$(this)).html();
				portrait=$("img",$(this)).attr("src");
				
				var tempLi=$('<li onclick="removeCandidate(this)" ><img class="avatar" src="'+portrait+'"><span class="name text-overflow">'+userName+'</span></li>');
				tempLi.data("id",userId);
				$this.before(tempLi);
			});
		}
	});
}
function assignSubmitFun(){
	var selectedUserLis=$("[flag=candidateDiv] ul li[flag!=addAssign]");
	var userIds="",userNames="";
	$.each(selectedUserLis,function(i,item){
		console.info(item);
		userIds+=$(item).data("id")+",";
		userNames+=$(".name",item).html()+",";
	});
	console.info(userIds.removeDot());
	console.info(userNames.removeDot());
}
//点击移除候选人
function removeCandidate(ele){
	$(ele).remove();
}
</script>