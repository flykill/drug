

function triggerConfig(sqlId){
	sql_id = sqlId;
	clearTriggerWin();
	getLogTable();
	$("#triggerWin").window('open');
	
}

function clearTriggerWin(){
//	$("#trigger_tableName").val("");
//	try{
//		$('#trigger_tableName').combobox('clear');
//	}catch(err){
//		console.info("combobox clear error");
//	}
	
	$(".tr_logAndSource:gt(0)").remove();
	$("#trigger_logTable_0,#trigger_sourceId_0").val("");
	
	$("#trigger_name").val("");
	$("#trigger_SQL").val("").attr({style:"width: 420px; height: 160px;display: inline-block;"});
	
	getTables();
	getSourceId();
}

function logTableSpanOperator(isExist){
	$("#trigger_logTable_" + isExist).show();
	$("#trigger_logTable_" + !isExist).hide();
}

function getSourceId(){
	var where = $.trim($("#"+ sql_id + "_areaWhere").val());
	if(where.indexOf("#SOURCEID#") > -1){
		where = where.replace(/\s*[=]\s*/g,"=").replace(/[\s]|[.]+/g,"-");
		var e_index = where.indexOf("=#SOURCEID#");
		var s_index = 0;
		for (var i = e_index; i >= 0; i--) {
			if(where.charAt(i) == "-"){
				s_index = i;
				break;
			}
		}
		var sourceId = where.substring(s_index+1,e_index);
		$("#trigger_sourceId_0").val(sourceId);
	}
}

function getTables(){
	var from = $.trim($("#"+ sql_id + "_areaFrom").val()).replace(/\s+/g," ");
	var tableArr = new Array();
	var arr1 = from.match(/\s*\S+/);
	var arr2 = from.match(/JOIN\s+\S+/gi);
	var arr3 = from.match(/,\s*\S+/g);
	tableArr = tableArr.concat(arr1);
	for(var x in arr2){
		tableArr.push(arr2[x].replace(/JOIN\s*/gi,""))
	}
	for(var y in arr3){
		tableArr.push(arr3[y].replace(/,\s*/gi,""))
	}
	var s_data = new Array();
	for(var z in tableArr){
		s_data.push({"text":tableArr[z],"value":tableArr[z]});
	}
	$("#trigger_tableName").combobox({
	    data:s_data,
	    onChange:function(newValue, oldValue){
	    	$("#trigger_name").val("TR_CDR_" + newValue);
	    	$("#triggerExist_msg").hide();
	    }
	});
}

function getLogTable(){
	//var tableNames = "";	
	$.ajax({
	  type:'POST',
	  url: '../sqltrigger/getLogTable.do',
	  data: {
  		  "sqlId":sql_id
	  },
	  success : function(data) {
			var json = $.parseJSON(data);
			if (!json.success) {
				$.tips.error({msg : json.message});
				return;
			}
			$("#trigger_logTable_0").val(json.logTableName);
		}
	});
}

function createLogTable(){
	$.ajax({
	  type:'POST',
	  url: '../sqltrigger/createLogTable.do',
	  data: {
  		  "sqlId":sql_id
	  },
	  success : function(data) {
			var json = $.parseJSON(data);
			if (!json.success) {
				$.tips.error({msg : json.message});
				return;
			}
			logTableSpanOperator(json.isExist);
			$("#trigger_logTable").val(json.logTableName);
		}
	});
}

function getTriggerSQL(){
	var tableName = $.trim($('#trigger_tableName').combobox('getValue'));
	if(tableName == ""){
		return;
	}
	var datasourceId = $("#"+sql_id+"_database").val();
	$.ajax({
	  type:'POST',
	  url: '../sqltrigger/getTriggerSQL.do',
	  data: {
	  	 "sqlId":sql_id,
	  	 "datasourceId":datasourceId,
	  	 "tableName":tableName
	  },
	  success : function(data) {
			var json = $.parseJSON(data);
			if (!json.success) {
				$.tips.error({msg : json.message});
				return;
			}
			if(json.triggerExist){
				$("#trigger_SQL").val(json.triggerSQL);
				$("#triggerExist_msg").hide();
			}else{
				$("#trigger_SQL").val("");
				$("#triggerExist_msg").text(json.message);
				$("#triggerExist_msg").show();
			}
			
			
			/*var triggerJson = json.triggers;
			var html = "";
			$.each(triggerJson, function(i){
				html += "<option value=" + i + ">" + i + "</option>";
			})
			if(html == ""){
				html += "<option value=''>无</option>";
			}
			$("#trigger_name").append(html);
			$("#trigger_name").off().on("change",function(){
				var trigName = $("#trigger_name").val();
				$("#trigger_SQL").val(triggerJson[trigName]);
			});
			$("#trigger_name").trigger("change");*/
		}
	});
}

function checkBeforeCreate(triggerData){
	var sourceTable = $.trim($('#trigger_tableName').combobox('getValue'));
	if(sourceTable == ""){
		$.messager.alert('提示','表名不能为空!','info');
		return false;
	}
	
	var valid = true;
	var arr = new Array();
	$(".tr_logAndSource").each(function(i,n){
		var logTable = $.trim($(this).find("input[name='trigger_logTable']").val());
		var sourceId = $.trim($(this).find("input[name='trigger_sourceId']").val());
		var isMaster = $.trim($(this).find("select[name='trigger_isMaster']").val());
		if(logTable=="" || sourceId==""){
			$.messager.alert('提示','日志表及来源标识不能为空!','info');
			valid=false;
			return false;
		}
		
		arr.push({"logTable":logTable,"sourceId":sourceId,"isMaster":isMaster});
	});
	
	if(!valid){
		return false;
	}
	
	triggerData.sqlId = sql_id;
	triggerData.datasourceId = $("#"+sql_id+"_database").val();
	triggerData.sourceTable = sourceTable;
	triggerData.logAndSourceId = arr;
	return true;
}

function generalStandTrigger(){
	var triggerData = {
		"sqlId" : "",
		"datasourceId" :"",
		"sourceTable" : "",
		"logAndSourceId" : null
	};
	if(!checkBeforeCreate(triggerData)){
		return;
	}
	
	$.ajax({
	  type:'POST',
	  url: '../sqltrigger/generalStandTrigger.do',
	  data: {"triggerData":JSON.stringify(triggerData)},
	  success : function(data) {
			var json = $.parseJSON(data);
			if (!json.success) {
				$.tips.error({msg : json.message});
				return;
			}
			$("#trigger_SQL").val(json.triggerSQL);
			$("#getTriggerSQL_msg").hide();
		}
	});
}

function createTrigger(){
	var triggerData = {
		"sqlId" : "",
		"datasourceId" :"",
		"sourceTable" : "",
		"logAndSourceId" : null
	};
	if(!checkBeforeCreate(triggerData)){
		return;
	}
	var triggerSQL = $("#trigger_SQL").val();
	if($.trim(triggerSQL).length == 0){
		$.messager.alert('提示','请先生成预览SQL!','info');
		return;
	}
	triggerData["triggerSQL"] = triggerSQL;
	
	$.ajax({
	  type:'POST',
	  url: '../sqltrigger/createTrigger.do',
	  data: {"triggerData":JSON.stringify(triggerData)},
	  success : function(data) {
			var json = $.parseJSON(data);
			if (!json.success) {
				$.tips.error({msg : json.message});
				return;
			}
			$.tips.success({msg : json.message});
		}
	});
}

$(document).ready(function() {
	
	bindHover($('.tr_logAndSource'));
	
	$("._add").live("click",function(){
		var jqTr = $(this).closest("tr");
		bindHover($("#tr_logAndSource").clone().removeAttr("id").addClass("tr_logAndSource").insertAfter(jqTr[0]));
	});
	
	$("._del").live("click",function(){
		$(this).closest("tr").remove();
	});
})

function bindHover(jqObj){
	jqObj.hover(
		function(){
			$(this).find("._add,._del").show();
		},
		function(){
			$(this).find("._add,._del").hide();
		}
	);
}

