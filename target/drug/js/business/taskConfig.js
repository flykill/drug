

var toolbar = [
		{
			id : 'button_beginBatchTask',
			text : '开始任务',
			iconCls : 'icon-test',
			handler : beginBatchTask
		}, {
			id : 'button_endBatchTask',
			text : '停止任务',
			iconCls : 'icon-undo',
			handler : endBatchTask
		}];
		
function handReflesh(){
	refreshData();
}
		
function beginBatchTask(){
	var rows = $('#contrastTable').datagrid('getChecked');
	if(rows.length == 0){
		$.messager.alert('info','未选中记录!','info');
		return;
	}
	var ids = "";
	for (var i = 0; i < rows.length; i++) {
		ids += rows[i].id + ",";
	}
	$.ajax({
			type : 'POST',
			url : 'beginBatchTask.do',
			data : {
				"ids" : ids.substring(0,ids.length-1)
			},
			success : function(data) {
				var json = $.parseJSON(data);
				if(json.success){
					$.tips.success({msg : json.message});
				}else{
					$.tips.error({msg : json.message});
				}
			}
		});
	setTimeout(refreshData,1000);
}

function endBatchTask(){
	var rows = $('#contrastTable').datagrid('getChecked');
	if(rows.length == 0){
		$.messager.alert('info','未选中记录!','info');
		return;
	}
	var ids = "";
	for (var i = 0; i < rows.length; i++) {
		ids += rows[i].id + ",";
	}
	$.ajax({
			type : 'POST',
			url : 'endBatchTask.do',
			data : {
				"ids" : ids.substring(0,ids.length-1)
			},
			success : function(data) {
				var json = $.parseJSON(data);
				if(json.success){
					$.tips.success({msg : json.message});
				}else{
					$.tips.error({msg : json.message});
				}
			}
		});
	setTimeout(refreshData,500);
}

function beginATask(id,_this){
	$.ajax({
				type : 'POST',
				url : 'beginATask.do',
				data : {
					"id" : id
				},
				success : function(data) {
					var json = $.parseJSON(data);
					if(json.success){
						$.tips.success({msg : json.message});
					}else{
						$.tips.error({msg : json.message});
					}
				}
			});
	setTimeout(refreshData,500);
}

function endATask(id,_this){
	$.ajax({
			type : 'POST',
			url : 'endATask.do',
			data : {
				"id" : id
			},
			success : function(data) {
				var json = $.parseJSON(data);
				if(json.success){
					$.tips.success({msg : json.message});
				}else{
					$.tips.error({msg : json.message});
				}
			}
		});
	setTimeout(refreshData,200);
}

function detailATask(id){
	//var row = $("#contrastTable").datagrid('getSelected');
	$.ajax({
				type : 'POST',
				url : 'detailATask.do',
				data : {
					"id" : id
				},
				success : function(data) {
					var json = $.parseJSON(data);
					if(json.success){
						showTaskState(json.taskState);
					}else{
						$.tips.error({msg : json.message});
					}
				}
			});
}

function showTaskState(taskState){
	$('#jobDetail').window('open');
	$('#jobDetail').empty();
	if(taskState == null){
		return;
	}
	
	var html="";
	html+="<span>"+ "id" +":" + taskState.id +"</span><br>";
	html+="<span>"+ "状态" +":" + getState(taskState.executeState) +"</span><br>";
	html+="<span>"+ "任务添加时间" +":" + myDate(taskState.jobAddTime) +"</span><br>";
	html+="<span>"+ "任务删除时间" +":" + myDate(taskState.jobDeleteTime) +"</span><br>";
	html+="<span>"+ "预测下次任务开始时间" +":" + myDate(taskState.startTime) +"</span><br>";
	/*html+="<span>"+ "最后一次开始时间" +":" + myDate(taskState.endTime) +"</span><br>";*/
	html+="<span>"+ "任务完成时间" +":" + myDate(taskState.finishTime) +"</span><br>";
	html+="<span>"+ "平均执行时间" +":" + convertTime(taskState.avgTimePay) +"</span><br>";
	html+="<span>"+ "任务完成次数" +":" + taskState.completeCount +"</span><br>";
	html+="<span>"+ "任务起火次数" +":" + taskState.firedCount +"</span><br>";
	html+="<br><br><span>" + "具体执行情况" + ":" + "</span><br>";
	
	//排序   为什么没有自动排序？
	var arr = new Array();
	$.each(taskState.taskDetailMap,function(i,n){
		arr.push(n);
	});
	var orderArr = arr.sort(
		function(a,b){
			if(a.count >= b.count) return -1;
			if(a.count < b.count) return 1;
		}
	);
	$.each(orderArr,function(i,n){
		html+="<span>"+ "第"+ n.count +"次" +"</span><br>";
		html+="<span>"+ "开始时间" +":" + myDate(n.fireTime) +"</span><br>";
		html+="<span>"+ "完成时间" +":" + myDate(n.completeTime) +"</span><br>";
		html+="<span>"+ "耗时" +":" + convertTime(n.jobRunTime) +"</span><br>";
		html+="<span>"+ "下次开始时间" +":" + myDate(n.nextFireTime) +"</span><br>";
		html+="<span>"+ "上次开始时间" +":" + myDate(n.previousFireTime) +"</span><br>";
		html+="<span>"+ "方法是否成功" +":" + (n.methodInvoke === true ? "成功" : "失败") +"</span><br>";
		html+="<span>"+ (n.methodInvoke === true ? "返回值" : "异常") +":" + n.methodInvokeResult +"</span><br>";
		html+="<br><br>";
	});
	$('#jobDetail').append(html);
}

function convertTime(milliseconds){
	if(typeof milliseconds == "undefined" || milliseconds == null || milliseconds == 0){
		return milliseconds;
	}
	var time ="";
	var second = milliseconds/1000;
	
	if((second/3600)/24 > 1 ){
		time += Math.floor((second/3600)/24) + "天";
	}
	if((second/3600)%24 > 1){
		time += Math.floor((second/3600)%24) + "时";
	}
	if((second/60)%60 > 1){
		time += Math.floor((second/60)%60) + "分";
	}
	//10秒以上不显示毫秒
	if(second%60 > 0){
		if(second%60 > 10){
			time += Math.floor(second%60) + "秒";
		}else{
			time += second%60 + "秒";
		}
	}
	return time;
}

var url;
var loseConnectTimes = 0;
var defaultInterval = 30;//S
var monitScheduler = setInterval(refreshData, defaultInterval*1000);
var queryConditions = {
	query_taskName : "",
	query_dataSetCode : "",
	query_dataSetCodeType : ""
};


var timeUnitObj = {};
$(document).ready(function() {
			$.ajax({
						type : 'POST',
						url : 'queryList.do',
						success : function(data) {
							var json = $.parseJSON(data);
							$('#contrastTable').datagrid("loadData", json);
						}
					});
			
			$('#operation').each(function(i, obj) {
						$(obj).panel('panel').css("padding-top", "1px");
					});
			$('#timeUnit option').each(function(i,n){
				timeUnitObj[$(this).val()] = $(this).text();
			});
			
			$('#add').click(function() {
						$('#addTask')[0].reset();
						$("#addTask #id").val("");
						$('#addTask .easyui-numberbox').numberbox('clear');
						//$("#addTask").form("clear");
						$('#addTask_Tabs').tabs("select",0);
						$('#addWin').window("open");
						comboSelectDefault();
						changeClassName();
						changeTriggerMode();
						url = "create.do";
					});
			$("#dataSetCode").next().find(".combo-arrow").click(function(){
				if($('#dataSetCode').combotree('getValue') != ""){
					var tree = $('#dataSetCode').combotree('tree');
					var node = tree.tree('getSelected');
					if (node != null){
						tree.tree('expandTo',node.target);
					}
				}
			});
			
			$(window).unload( function () {
				window.clearInterval(t);
			});
		});

/**
 * 定时器
 */
function refreshData() {
	$.ajax({
				type : 'POST',
				url : 'queryList.do',
				data : queryConditions,
				success : function(data) {
					var json = $.parseJSON(data);
					$('#contrastTable').datagrid("loadData", json);
				},
				error : function(XMLHttpRequest, textStatus, errorThrown){
					loseConnectTimes++;
					//连续3次查询失败则清除定时器
					if(loseConnectTimes == 3){
						clearInterval(monitScheduler);
						loseConnectTimes = 0;
						$.messager.alert('warning','失去连接!','warning');
					}
				}
			});
	var newInterval = $('#refreshFrequency').spinner('getValue');
	if(newInterval != defaultInterval){
		defaultInterval = newInterval;
		clearInterval(monitScheduler);
		monitScheduler = setInterval(refreshData, defaultInterval*1000);
	}
	
}

/**
 * 查找
 */
function queryByConditions() {
	queryConditions.query_taskName = $.trim($("#query_taskName").val());
	queryConditions.query_dataSetCode =  $.trim($("#query_dataSetCode").combo("getValue"));
	
	$.ajax({
				type : 'POST',
				url : 'queryList.do',
				data : queryConditions,
				success : function(data) {
					var json = $.parseJSON(data);
					$('#contrastTable').datagrid('loadData', json);
					// select row
					//$('#contrastTable').datagrid('selectRow', 0);
				}
			});
}

function ok() {
	
	$("#addTask .easyui-validatebox,.easyui-numberbox").validatebox('validate');
	$("#addTask .easyui-combobox").combo('validate');
	if($(".validatebox-invalid").length != 0 ){
		var index = $($(".validatebox-invalid")[0]).parents("div[index]").attr("index");
		$('#addTask_Tabs').tabs('select',Number(index));
	}
	
	if (!$("#addTask").form('validate')){
		return;
	}
	var dataSerialize = $("#addTask").serialize()
	$.each(queryConditions,function(k,v){
		dataSerialize += ("&" + k + "=" +v);
	});
	
	$.ajax({
				type : 'POST',
				url : url,
				data : dataSerialize,
				success : function(data) {
					var json = $.parseJSON(data);
					if (!json.success) {
						$.tips.error({
									msg : json.message
								});
						return;
					}
					// close win
					$('#addWin').window("close");
					// clear win
					//$('#addTask').form("clear");
					//$('#addTask')[0].reset();
					$('#contrastTable').datagrid('loadData', json);
					$.tips.success({
								msg : "保存成功"
							});
				}
			});
}

function cancel() {
	$('#addWin').window("close");
}

function comboSelectDefault(){
	$("#addTask option[default]").each(function(){
		$(this).parent().combobox("select",$(this).val());
	})
}

// general deleteButton
function generalDelete(id) {
	var html = "<a href='#'  onclick='alter("+id+")' class='l-btn l-btn-plain'><span class='l-btn-left'><span class='l-btn-text icon-doing' style='padding-left: 20px;'>修改</span></span></a>";
	html+="<a href='#'  onclick='deleteTask("+id+")' class='l-btn l-btn-plain'><span class='l-btn-left'><span class='l-btn-text icon-remove' style='padding-left: 20px;'>删除</span></span></a>";
	return html;
}
function generalTaskClassify(val){
	var result = "";
	$.each($('#targetClassName').combobox("getData"),function(i,o){
		if(val == o.value){
			result = o.text;
			return;
		}
	});
	return result;
}

function generalInterface(compressInterface,interfaceValid){
	//var arr = $('#query_compressInterface').combobox("getData");
	var text = compressInterface;
	/*$.each(arr,function(i,n){
		if(n.value == compressInterface){
			text = n.text;
		}
	});*/
	if(text.indexOf(".scheduler") > 0){
		text =text.substring(0,text.indexOf(".scheduler"));
	}
	if(interfaceValid !== true){
		text = "<font color='red'>" + text + "</font>";
	}
	return text;
}
function generalAdapter(adapterInterface,adapterValid){
	var text = adapterInterface;
	if(adapterValid !== true){
		text = "<font color='red'>" + text + "</font>";
	}
	return text;
}

function generalInterfaceState(interfaceValid){
	return interfaceValid === true ? "可用" : "无效";
}

function generalTaskState(row){
	if(row.taskState != null){
		return getState(row.taskState.executeState);
	}else{
		return "未添加";
	}
}

function generalCollFre(row){
	var triggerMode = row.triggerMode;
	if(triggerMode === 1){
		return "开始时间:" + myDate(row.triggerStartTime)
			+ " 间隔:" + row.timeInterval + timeUnitObj[row.timeUnit]
			+ " 次数:" + row.triggerRepeatCount;
		
	}else if(triggerMode == 2){
		return row.cronExpression;
	}else if(triggerMode == 3){
		return "时间段:" + row.startingDaily +"-" + row.endingDaily
			+ " 间隔:" + row.timeInterval + timeUnitObj[row.timeUnit];
	}
}

function generalTitle(data){
	$("td[field='collFre']:gt(0)").each(function(i,n){
		var title = $(this).children("div").text();
		if(title.length > 20){
			$(this).attr("title",title);
		}
	});
}

function getState(state){
	switch (state) {
		case 0:
			return "等待执行";
		case 1:
			return "已完成";
		case 2:
			return "正在执行";
		case 3:
			return "已停止";
		case 4:
			return "等待停止";
		default:
			return "未知";
		}
}

function generalScheduleOpera(row){
	var id = row.id;
	var html="";
	var begin_grayClass = "anotactive";
	var end_grayClass = "anotactive";
	var detail_grayClass = "anotactive";
	if(row.interfaceValid){
		detail_grayClass = "";
		//初始化-1 表示未添加
		var executeState = -1;
		if( row.taskState != null){
			executeState = row.taskState.executeState;
		}
		//未添加、已经停止的任务 + 已完成
		if(executeState==-1 || executeState==3 || executeState==1){
			begin_grayClass = "";
		}
		//等待、正在执行的任务
		if(executeState==0 || executeState==2){
			end_grayClass = "";
		}
	}
	
	html+= "<a href='#' id='button_beginATask' onclick='beginATask("+ id+",this)' class='" + begin_grayClass + " l-btn l-btn-plain'><span class='l-btn-left'><span class='l-btn-text icon-test' style='padding-left: 20px;'>开始</span></span></a>";
	html+= "<a href='#' id='button_endATask' onclick='endATask("+ id+",this)' class='" + end_grayClass + " l-btn l-btn-plain'><span class='l-btn-left'><span class='l-btn-text icon-undo' style='padding-left: 20px;'>停止</span></span></a>";
	html+="<a href='#' id='button_detailATask' onclick='detailATask("+ id+")' class='" + detail_grayClass + " l-btn l-btn-plain'><span class='l-btn-left'><span class='l-btn-text icon-todo' style='padding-left: 20px;'>详细</span></span></a>";
	return html;
}


function alter(id) {
	$.ajax({
				type : 'POST',
				url : 'queryById.do',
				data : {
					"id" : id
				},
				success : function(data) {
					var json = $.parseJSON(data);
					var task = json.task;
					if (task == null) {
						$.tips.error({
									msg : "记录已被删除"
								});
						return;
					}
					$('#addTask')[0].reset();
					$("#addTask #id").val("");
					$('#addTask .easyui-numberbox').numberbox('clear');
					//$("#addTask").form("clear");
					$('#addTask_Tabs').tabs("select",0);
					changeClassName(task.targetClassName);
					changeTriggerMode(task.triggerMode);
					task.triggerStartTime = myDate(task.triggerStartTime);
					$("#addTask").form("load", task);
					//$("#addWin").contents().find("tr[triggerMode][triggerMode!='"+task.triggerMode+"']").find("input").validatebox({required : false});
					$('#addWin').window("open");
					url = "modify.do";
				}
			});

}


// delete contrast
function deleteTask(id) {
	$.messager.confirm('提示信息', '确定要删除该条信息?', function(r) {
				if (r) {
					$.ajax({
								type : 'POST',
								url : 'remove.do',
								data : $.extend({},{"id" : id},queryConditions),
								success : function(data) {
									var json = $.parseJSON(data);
									if (!json.success) {
										$.tips.error({
													msg : json.message
												});
										return;
									}
									$('#contrastTable').datagrid('loadData',
											json);
									$.tips.success({
												msg : json.message
											});
								}
							});
				}
			});
}

// 分页
function pagerFilter(data) {
	if (typeof data.length == 'number' && typeof data.splice == 'function') { // is
		// array
		data = {
			total : data.length,
			rows : data
		}
	}
	var dg = $(this);
	var opts = dg.datagrid('options');
	var pager = dg.datagrid('getPager');
	pager.pagination({
				onSelectPage : function(pageNum, pageSize) {
					opts.pageNumber = pageNum;
					opts.pageSize = pageSize;
					pager.pagination('refresh', {
								pageNumber : pageNum,
								pageSize : pageSize
							});
					dg.datagrid('loadData', data);
				},
                showRefresh:false
			});
	if (!data.originalRows) {
		data.originalRows = (data.rows);
	}
	var start = (opts.pageNumber - 1) * parseInt(opts.pageSize);
	var end = start + parseInt(opts.pageSize);
	data.rows = (data.originalRows.slice(start, end));
	return data;
}

$(function() {
			$('#contrastTable').datagrid({
						loadFilter : pagerFilter
					});
		});

		
function changeClassName(val){
	var className = typeof val === "undefined" ? "com.bsoft.cdr.etl.compress.service.task.EtlService" : val;
	if(className == "com.bsoft.cdr.etl.compress.service.task.EtlService"){
		$("#targetMethodName").val("Etl");
		$("#cdcDataSourceId").combo({required : true});
	}else if(className == "com.bsoft.cdr.etl.compress.service.task.MqService"){
		$("#targetMethodName").val("start");
		$("#cdcDataSourceId").combo({required : false}).next().children().removeClass("validatebox-invalid");
	}
}
		
/*function changeTriggerMode(val) {
	var triggerMode = typeof val === "undefined" ? 1 : val;
	
	if(triggerMode == 1){
		$("#cronExpression").validatebox({required : false}).removeClass("validatebox-invalid");
		$("#cronExpression").parents("tr").hide();
		
		$('#triggerStartTime').datebox({required:true});
		$("#triggerRepeatCount,#timeInterval,#timeUnit").validatebox({required : true});
		$("#triggerStartTime,#triggerRepeatCount,#timeInterval,#timeUnit").parents("tr").show();
	}else if(triggerMode == 2){
		$('#triggerStartTime').datebox({required:false}).next().children().removeClass("validatebox-invalid");
		$("#triggerRepeatCount,#timeInterval,#timeUnit").validatebox({required : false}).removeClass("validatebox-invalid");
		$("#triggerStartTime,#triggerRepeatCount,#timeInterval,#timeUnit").parents("tr").hide();
		$("#cronExpression").parents("tr").show();
		$("#cronExpression").validatebox({required : true});
	}
}*/

function changeTriggerMode(val) {
	var triggerMode = typeof val === "undefined" ? 1 : val;
	
	if(triggerMode == 1){
		mode2Change(false);
		mode3Change(false);
		mode1Change(true);
	}else if(triggerMode == 2){
		mode1Change(false);
		mode3Change(false);
		mode2Change(true);
	}else if(triggerMode == 3){
		mode1Change(false);
		mode2Change(false);
		mode3Change(true);
	}
}		
		
function mode1Change(b){
	if(b){
		$('#triggerStartTime').datebox({required:true});
		$("#triggerRepeatCount,#timeInterval,#timeUnit").validatebox({required : true});
		$("#triggerStartTime,#triggerRepeatCount,#timeInterval,#timeUnit").parents("tr").show();
	}else{
		$('#triggerStartTime').datebox({required:false}).next().children().removeClass("validatebox-invalid");
		$("#triggerRepeatCount,#timeInterval,#timeUnit").validatebox({required : false}).removeClass("validatebox-invalid");
		$("#triggerStartTime,#triggerRepeatCount,#timeInterval,#timeUnit").parents("tr").hide();
	}
}
function mode2Change(b){
	if(b){
		$("#cronExpression").parents("tr").show();
		$("#cronExpression").validatebox({required : true});
	}else{
		$("#cronExpression").validatebox({required : false}).removeClass("validatebox-invalid");
		$("#cronExpression").parents("tr").hide();
	}
}
function mode3Change(b){
	if(b){
		$("#startingDaily,#endingDaily,#daysOfWeek,#timeInterval,#timeUnit").parents("tr").show();
		$("#startingDaily,#endingDaily,#daysOfWeek,#timeInterval,#timeUnit").validatebox({required : true});
	}else{
		$("#startingDaily,#endingDaily,#daysOfWeek,#timeInterval,#timeUnit").parents("tr").hide();
		$("#startingDaily,#endingDaily,#daysOfWeek,#timeInterval,#timeUnit").validatebox({required : false}).removeClass("validatebox-invalid");
	}
}


function myDate(milliseconds) {
	if (milliseconds == null) {
		return null;
	}
	var date = new Date(milliseconds);// 这里必须是整数，毫秒
	return date.format("yyyy-MM-dd hh:mm:ss");
}

Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
		// millisecond
	}
	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4
						- RegExp.$1.length));
	}
	for (var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1
							? o[k]
							: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}


function builderCronExpression(){
	var s = "0";
	var m = $.trim($("#Minute").val());
	var h = $.trim($("#Hour").val());
	var d = $.trim($("#Day").val());
	var M = $.trim($("#Month").val());
	var w = $.trim($("#Weekday").val());
	
	if(m =="" || h=="" || d=="" || M=="" || w==""){
		$.messager.alert('warning','some input is empty','warning');
		return;
	}
	var cronExpression = s + " " + m + " " + h + " " + d + " " + M + " " + w;
	$("#myCron").val(cronExpression);
	
}

function saveCronExpression(){
	$("#cronExpression").val($.trim($("#myCron").val()));
	closeCronBuilder();
}

function closeCronBuilder(){
	$('#cronBuilder_win').window("close");
}

function openCronBuilder(){
	$('#cronBuilder_win').window("open");
}

function dayOnBlur(val){
	if(val != "" && val != "?"){
		$("#Weekday").val("?");
	}
}
function weekdayOnBlur(val){
	if(val != "" && val != "?"){
		$("#Day").val("?");
	}
}

/**
 * beforeSend : ajaxLoading,
 * complete : ajaxLoadEnd,
 * 调用
 */
function ajaxLoading(){  
    $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");  
    $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});  
 }  
 function ajaxLoadEnd(){  
     $(".datagrid-mask").remove();  
     $(".datagrid-mask-msg").remove();        
}  
  

