<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<link rel="stylesheet" href='plug-in/fullcalendar-2.3.1/fullcalendar.css' />
<link rel="stylesheet" href='plug-in/fullcalendar-2.3.1/fullcalendar.print.css' media='print' />
<link rel="stylesheet" href="plug-in/fullcalendar-2.3.1/fullcalendar.extend.css" />
<script src='plug-in/fullcalendar-2.3.1/lib/moment.min.js'></script>
<script src='plug-in/fullcalendar-2.3.1/fullcalendar.min.js'></script>
<script src='plug-in/fullcalendar-2.3.1/lang/zh-cn.js'></script>

<script>
	$(function() {
		var date = new Date();
		var d = date.getDate();
		var m = date.getMonth();
		var y = date.getFullYear();
		var calendar = $('#calendar').fullCalendar({
			header : {
				left : 'prev,next today',
				center : 'title',
				right : 'month,agendaWeek,agendaDay'
			},
			events : function(start, end, timezone, callback) {
				$.ajax({
					url : 'scheduleController.do?calendarEvents',
					type : 'post',
					cache : false,
					success : function(doc) {
						var events = [];
						var info = doc.eventinfo;
						for (var i = 0; i < info.length; i++) {
							var ev = info[i];
							var title = ev.title;
							var evtstart = ev.start;
							var evtend = ev.end;
							if (ev.allDay == 'false') {
								evtstart = strToDate(ev.start);
								evtend = strToDate(ev.end);
							}
							events.push({
								title : title,
								start : evtstart,
								end : evtend,
								allDay : ev.allDay,
								className : ev.className,
								id : ev.id
							});
						}
						callback(events);
					}
				});
			},
			editable : true,
			droppable : true, // this allows things to be dropped onto the calendar !!!
			timeFormat : {
				agenda : 'h:mm - {h:mm}'
			},
			'' : 'h(:mm)',
			drop : function(date, jsEvent, ui) { // this function is called when something is dropped
				var currentDate = new Date();
				//				if(moment(date).format('YYYY-MM-DD') < moment(currentDate).format('YYYY-MM-DD'))
				//					return;
				var originalEventObject = $(this).data('eventObject');
				var copiedEventObject = $.extend({}, originalEventObject);
				$('#calendar').fullCalendar('renderEvent', copiedEventObject, true);
				// is the "remove after drop" checkbox checked?
				var id = this.id;
				if ($('#drop-remove').is(':checked')) {
					// if so, remove the element from the "Draggable Events" list
					$(this).remove();
					$.ajax({
						url : 'oftenScheduleController.do?delete&ids=' + this.id,
						type : 'post',
						data : {
							id : id
						},
						cache : false,
						success : function(data) {
							//$('#calendar').fullCalendar('refetchEvents');
						}
					});
				}
				var startDate = moment(date).format('YYYY-MM-DD HH:mm:ss');
				var endDate = moment(date).format('YYYY-MM-DD HH:mm:ss');
				$.ajax({
					url : 'scheduleController.do?drop&id=' + id + '&startDate=' + startDate + '&endDate=' + endDate + "&allDay=" + allDay,
					type : 'post',
					data : {
						id : id
					},
					cache : false,
					success : function(data) {
						scheduleMainTabs();
						$('#calendar').fullCalendar('refetchEvents');
						//$('#calendar').fullCalendar('renderEvent', true);
					}
				});
			},
			selectable : true,
			selectHelper : true,
			//点击日程触发
			select : function(start, end, allDay) {
				createwindow('新增', "scheduleController.do?scheduleEdit", null, 480, 1, {
					optFlag : 'add'
				});
			},
			//拖拽
			eventDrop : function(event, delta, revertFunc, jsEvent, ui, view) {
				allDay = false;
				var startDate = moment(event.start).format('YYYY-MM-DD HH:mm:ss');
				if (event.end != null)
					endDate = moment(event.end).format('YYYY-MM-DD HH:mm:ss');
				else
					endDate = startDate;
				//				if(endDate < moment(date).format('YYYY-MM-DD')){
				//					revertFunc();
				//					return;
				//				}
				$.ajax({
					url : 'scheduleController.do?eventResize&id=' + event.id + '&startDate=' + startDate + '&endDate=' + endDate + "&allDay=" + allDay,
					type : 'post',
					data : {
						id : event.id
					},
					cache : false,
					success : function(data) {
						scheduleMainTabs();
						//$('#calendar').fullCalendar('refetchEvents');
						//$('#calendar').fullCalendar('renderEvent', true);  //刷新
					}
				});
			},
			//事件改变大小触发
			eventResize : function(event, dayDelta, revertFunc) {
				var endDate;
				var startDate = moment(event.start).format('YYYY-MM-DD HH:mm:ss');
				if (event.end != null)
					endDate = moment(event.end).format('YYYY-MM-DD HH:mm:ss');
				else
					endDate = startDate;
				if (endDate < moment(date).format('YYYY-MM-DD')) {
					revertFunc();
					return;
				}
				$.ajax({
					url : 'scheduleController.do?eventResize&id=' + event.id + '&startDate=' + startDate + '&endDate=' + endDate,
					type : 'post',
					data : {
						id : event.id
					},
					cache : false,
					success : function(data) {
						scheduleMainTabs();
						//$('#calendar').fullCalendar('refetchEvents');
						//$('#calendar').fullCalendar('renderEvent', true);  //刷新
					}
				});

			},
			//点击日程事件触发
			eventClick : function(calEvent, jsEvent, view) {
				createwindow('新增', "scheduleController.do?scheduleEdit&id=" + calEvent.id, null, 480, 1, {
					optFlag : 'add'
				});
			}
		});

	});

	function strToDate(str) {
		var tempStrs = str.split(" ");
		var dateStrs = tempStrs[0].split("-");
		var year = parseInt(dateStrs[0], 10);
		var month = parseInt(dateStrs[1], 10) - 1;
		var day = parseInt(dateStrs[2], 10);
		var timeStrs = tempStrs[1].split(":");
		var hour = parseInt(timeStrs[0], 10);
		var minute = parseInt(timeStrs[1], 10) - 1;
		var second = parseInt(timeStrs[2], 10);
		var date = new Date(year, month, day, hour, minute, second);
		return date;
	}
</script>
<style>
#calendar {
	max-width: 930px;
	margin: 0 auto;
}
</style>
<!-- <div style="width: 100%; height: 800px; background: #fff; padding-top: 17px;">
	
</div> -->
<div class="row">
	<div class="col-lg-12 col-sm-12 col-xs-12">
		<div class="widget">
			<div class="widget-header bordered-bottom bordered-themeprimary">
				<i class="widget-icon fa fa-calendar themeprimary"></i>
				<span class="widget-caption themeprimary">日程管理</span>
			</div>
			<div class="widget-body" style="padding-top:17px;padding-bottom:17px">
				<div id='calendar'></div>
			</div>
		</div>
	</div>
</div>



<!-- var date = new Date();
		var d = date.getDate();
		var m = date.getMonth();
		var y = date.getFullYear();
		var calendar = $('#calendar').fullCalendar({
			buttonText : {
				prev : '<i class="awsm-icon-chevron-left"></i>',
				next : '<i class="awsm-icon-chevron-right"></i>'
			},

			header : {
				left : 'prev,next today',
				center : 'title',
				right : 'month,agendaWeek,agendaDay'
			},
			events:function(start, end, callback) {
				$.ajax({
					url : 'scheduleController.do?calendarEvents',
					type : 'post',
					cache : false,
					success : function(doc) {
						var events = [];
						var info = doc.eventinfo;
						for (var i = 0; i < info.length; i++) {
							var ev = info[i];
							var title = ev.title;
							var evtstart = ev.start;
							var evtend = ev.end;
							if(ev.allDay == 'false'){
								evtstart = strToDate(ev.start);
								evtend = strToDate(ev.end);
							}
							events.push({
								title:title,
								start:evtstart,
								end:evtend,
								allDay:ev.allDay,
								className:ev.className,
								id:ev.id
							});
						}
						callback(events);
					}
				});
            },
			editable : true,
			droppable : true, // this allows things to be dropped onto the calendar !!!
			timeFormat : {agenda: 'h:mm - {h:mm}'},
			'': 'h(:mm)',
			drop : function(date, allDay) { // this function is called when something is dropped
				var currentDate = new Date();
// 				if(moment(date).format('YYYY-MM-DD') < moment(currentDate).format('YYYY-MM-DD'))
// 					return;
				var originalEventObject = $(this).data('eventObject');
				var copiedEventObject = $.extend({}, originalEventObject);
				$('#calendar').fullCalendar('renderEvent', copiedEventObject, true);
				// is the "remove after drop" checkbox checked?
				var id = this.id;
				if ($('#drop-remove').is(':checked')) {
					// if so, remove the element from the "Draggable Events" list
					$(this).remove();
					$.ajax({
						url : 'oftenScheduleController.do?delete&ids='+this.id,
						type : 'post',
						data : {
							id : id
						},
						cache : false,
						success : function(data) {
							//$('#calendar').fullCalendar('refetchEvents');
						}
					});
				}
				var startDate = moment(date).format('YYYY-MM-DD HH:mm:ss');
				var endDate = moment(date).format('YYYY-MM-DD HH:mm:ss');
				$.ajax({
					url : 'scheduleController.do?drop&id='+id+'&startDate='+startDate+'&endDate='+endDate+"&allDay="+allDay,
					type : 'post',
					data : {
						id : id
					},
					cache : false,
					success : function(data) {
						scheduleMainTabs();
 						$('#calendar').fullCalendar('refetchEvents');
						//$('#calendar').fullCalendar('renderEvent', true);
					}
				});
			},
			selectable : true,
			selectHelper : true,
			//点击日程触发
			select : function(start, end, allDay) {
				//$('#scheduleDialogForm').reset();
				document.scheduleDialogForm.reset();
				if(moment(start).format('YYYY-MM-DD') < moment(date).format('YYYY-MM-DD'))
					return;
				var startDate = moment(start).format('YYYY-MM-DD HH:mm');//$.fullCalendar.formatDate(start,'yyyy-MM-dd HH:mm:ss'); 
				var endDate = moment(end).format('YYYY-MM-DD HH:mm');//$.fullCalendar.formatDate(end,'yyyy-MM-dd HH:mm:ss');
				var scheduledate = startDate + " - " + endDate;
				$("#startDate").val(scheduledate);
				$("#scheduleEdit").dialog({
				    bgiframe: true,
				    resizable: false,
				    width:400,
				    height:420,
				    position: ["auto",40],
				    modal: true,
				    dialogClass: "ucg-ui-dialog",
				    overlay: {
				        backgroundColor: '#000',
				        opacity: 0.5
				    },
				    buttons: [
				    	{
							html: "<i class='awsm-icon-save bigger-110'></i>&nbsp;保存",
							"class" : "btn btn-info btn-xs",
							click: function() {
								var title = $("#title").val();
								var scheduledate1 = $("#startDate").val();
								var startDate = scheduledate1.substring(0, scheduledate1.indexOf(" - "));
								var endDate = scheduledate1.substring(scheduledate1.indexOf(" - ") + 3, scheduledate1.length);
								var context = $("#context").val();
								var classStyle = excgColor("", $("#className").val());
								if(title == ''){
									alert("请填写标题");
									return;
								}
								if(startDate == ''){
									alert("请填开始时间");
									return;
								}
								if(endDate == ''){
									alert("请填结束时间");
									return;
								}
								if(context == ''){
									alert("请填内容");
									return;
								}
								var scheduleType = '';
								var scheduleTypes = document.getElementsByName("scheduleType");
								for(var i = 0; i < scheduleTypes.length; i++){
							       if(scheduleTypes[i].checked){
							    	   scheduleType += scheduleTypes[i].value + ",";
							       }
							    }
								if(scheduleType.indexOf(",") != -1){
									scheduleType = scheduleType.substring(0, scheduleType.length -1);
								}
								if(scheduleType == ''){
									alert("请选择提醒方式");
									return;
								}
								var url = "scheduleController.do?saveOrUpdate&title="+title+"&context="+context+"&className="+classStyle+"&allDay="+allDay+"&scheduleType="+scheduleType;
								$.ajax({
									url : encodeURI(encodeURI(url))+"&startDate="+startDate+"&endDate="+endDate,
									type : 'post',
									data : {
										id : ''
									},
									cache : false,
									success : function(data) {
										scheduleMainTabs();
										$('#calendar').fullCalendar('refetchEvents');
										//$('#calendar').fullCalendar('renderEvent', true);  //刷新
									}
								});
					            $(this).dialog('close');
					            $(this).dialog('destroy');
							}
						},
						{
							html: "<i class='awsm-icon-remove bigger-110'></i>&nbsp;取消",
							"class" : "btn btn-info btn-xs",
							click: function() {
								 $(this).dialog('close');
						         $(this).dialog('destroy');
							}
						}
				    ]
				});
			},
			//拖拽
			eventDrop: function(event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view) {
				allDay = false;
				var startDate = moment(event.start).format('YYYY-MM-DD HH:mm:ss');
				if(event.end != null)
					endDate = moment(event.end).format('YYYY-MM-DD HH:mm:ss');
				else 
					endDate = startDate;
// 				if(endDate < moment(date).format('YYYY-MM-DD')){
// 					revertFunc();
// 					return;
// 				}
				$.ajax({
					url : 'scheduleController.do?eventResize&id='+event.id+'&startDate='+startDate+'&endDate='+endDate+"&allDay="+allDay,
					type : 'post',
					data : {
						id : event.id
					},
					cache : false,
					success : function(data) {
						scheduleMainTabs();
 						//$('#calendar').fullCalendar('refetchEvents');
						//$('#calendar').fullCalendar('renderEvent', true);  //刷新
					}
				});
			},
			//事件改变大小触发
			eventResize: function(event,dayDelta,minuteDelta,revertFunc){
				var endDate;
				var startDate = moment(event.start).format('YYYY-MM-DD HH:mm:ss');
				if(event.end != null)
					endDate = moment(event.end).format('YYYY-MM-DD HH:mm:ss');
				else 
					endDate = startDate;
				if(endDate < moment(date).format('YYYY-MM-DD')){
					revertFunc();
					return;
				}
				$.ajax({
					url : 'scheduleController.do?eventResize&id='+event.id+'&startDate='+startDate+'&endDate='+endDate,
					type : 'post',
					data : {
						id : event.id
					},
					cache : false,
					success : function(data) {
						scheduleMainTabs();
 						//$('#calendar').fullCalendar('refetchEvents');
						//$('#calendar').fullCalendar('renderEvent', true);  //刷新
					}
				});
				
			},
			//点击日程事件触发
			eventClick : function(calEvent, jsEvent, view) {
				$.ajax({
					url : 'scheduleController.do?findById&id='+calEvent.id,
					type : 'post',
					data : {
						id : calEvent.id
					},
					cache : false,
					success : function(data) {
						document.scheduleDialogForm.reset();
						var d = $.parseJSON(data);
						$("#title").val(calEvent.title);
						var scheduledate = data.startDate + " - " + data.endDate;
						$("#startDate").val(scheduledate);
						//$("#endDate").val(data.endDate);
						$("#context").val(data.context);
						var scheduleType1 = data.scheduleType;
						var scheduleTypes1 = scheduleType1.split(",");
						for(var i = 0; i < scheduleTypes1.length; i++){
							if(scheduleTypes1[i] != '' && scheduleTypes1[i] != 'null'){
								document.getElementById(scheduleTypes1[i]).checked = true;
							}
						}
						var className = excgColor(data.className, "");
						$("#className").val(className);
						$("#scheduleEdit").dialog({
						    bgiframe: true,
						    resizable: false,
						    width:400,
						    height:420,
						    position: ["auto",40],
						    modal: true,
						    dialogClass: "ucg-ui-dialog",
						    overlay: {
						        backgroundColor: '#000',
						        opacity: 0.5
						    },
						    buttons: [{
						    		html: "<i class='awsm-icon-save bigger-110'></i>&nbsp;保存",
									"class" : "btn btn-info btn-xs",
							        click: function() {
							        	var title = $("#title").val();
							        	var scheduledate1 = $("#startDate").val();
										var startDate = scheduledate1.substring(0, scheduledate1.indexOf(" - "));
										var endDate = scheduledate1.substring(scheduledate1.indexOf(" - ") + 3, scheduledate1.length);
										var context = $("#context").val();
										var classStyle = excgColor("", $("#colorStyle").val());
										if(title == ''){
											alert("请填写标题");
											return;
										}
										if(startDate == ''){
											alert("请填开始时间");
											return;
										}
										if(endDate == ''){
											alert("请填结束时间");
											return;
										}
										if(context == ''){
											alert("请填内容");
											return;
										}
										var scheduleType = '';
										var scheduleTypes = document.getElementsByName("scheduleType");
										for(var i = 0; i < scheduleTypes.length; i++){
									       if(scheduleTypes[i].checked){
									    	   scheduleType += scheduleTypes[i].value + ",";
									       }
									    }
										if(scheduleType.indexOf(",") != -1){
											scheduleType = scheduleType.substring(0, scheduleType.length -1);
										}
										if(scheduleType == ''){
											alert("请选择提醒方式");
											return;
										}
										var url = "scheduleController.do?saveOrUpdate&title="+title+"&context="+context+"&className="+classStyle+"&scheduleType="+scheduleType;
										$.ajax({
											url : encodeURI(encodeURI(url))+"&startDate="+startDate+"&endDate="+endDate,
											type : 'post',
											data : {
												id : calEvent.id
											},
											cache : false,
											success : function(data) {
												scheduleMainTabs();
												$('#calendar').fullCalendar('refetchEvents');
												//$('#calendar').fullCalendar('renderEvent', true);  //刷新
											}
										});
							            $(this).dialog('close');
							            $(this).dialog('destroy');
							        }
						    	},{
						    		html: "<i class='awsm-icon-trash bigger-110'></i>&nbsp;删除",
									"class" : "btn btn-info btn-xs",
						    		click: function() {
							        	$.ajax({
											url : "scheduleController.do?delete&ids="+calEvent.id,
											type : 'post',
											data : {
												id : calEvent.id
											},
											cache : false,
											success : function(data) {
												scheduleMainTabs();
												calendar.fullCalendar('removeEvents',function(ev) {
													return (ev._id == calEvent._id);
												})
											}
										});
							        	$(this).dialog('close');
							            $(this).dialog('destroy');
							        }
						    	 },{
						    		html: "<i class='awsm-icon-remove bigger-110'></i>&nbsp;取消",
									"class" : "btn btn-info btn-xs",
							        click: function() {
							            $(this).dialog('close');
							            $(this).dialog('destroy');
							        }
						    	 }
						    ]
						});
					}
				});
			}
		});

	});
	
	function strToDate(str) {
		 var tempStrs = str.split(" ");
		 var dateStrs = tempStrs[0].split("-");
		 var year = parseInt(dateStrs[0], 10);
		 var month = parseInt(dateStrs[1], 10) - 1;
		 var day = parseInt(dateStrs[2], 10);
		 var timeStrs = tempStrs[1].split(":");
		 var hour = parseInt(timeStrs [0], 10);
		 var minute = parseInt(timeStrs[1], 10) - 1;
		 var second = parseInt(timeStrs[2], 10);
		 var date = new Date(year, month, day, hour, minute, second);
		 return date;
	} -->

