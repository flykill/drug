<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>药店管理系统-基本信息维护</title> 
<#-- meta --> 
<#include "/widgets/meta.ftl"/>
<script type="text/javascript" src="../js/autoComplete/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="../css/autoComplete/jquery.autocomplete.css" />
<script type="text/javascript">
	jQuery().ready(function() {
		//新增
		$('#btnAdd').click(function() {
			$('#addWin').window('open');
			$('#addDrugForm')[0].reset();
			$('#id').val("");
			$('#addDrugForm .easyui-numberbox').numberbox('clear');
		});
		//查询 
		$('#btnQuery').click(function() {
			query();
		});
		//init
		query();
		//init autoComplete
		intiFactoryName();
		intiDrugName();
	});
	//query
	function query(){
		var pageNumber= $('#drugPage').pagination('options').pageNumber;
		var pageSize=$('#drugPage').pagination('options').pageSize;
		$.ajax({
			  type:'POST',
			  url: 'queryDrug.do',
			  data: {
				  pageNumber:pageNumber,
				  pageSize:pageSize,
				  drugName:$('#drugName_query').val(),
			      factoryName:$('#factoryName_query').val(),
			      drugCode:$('#drugCode_query').val()
			  },
			  success: function(data){
				  var json = $.parseJSON(data);
				  if(!json.success){
						$.tips.error({msg : json.message});
						  return; 
				  }
					$('#drugTable').datagrid("loadData", json.drugList);
					$('#drugPage').pagination({"total" : json.total});
				//	$.tips.success({msg : json.message});
	  			}
		});	
	}
	//addWin ok
	function ok(){
		if(!$("#addDrugForm").form('validate')){
			$.tips.error({msg :"请填写必填项!"});
			return;
		}
		$.ajax({
			  type:'POST',
			  url: 'saveDrug.do',
			  data: $('#addDrugForm').serialize(),
			  success: function(data){
				  var json = $.parseJSON(data);
				  if(!json.success){
					   $.tips.error({msg:json.message});
					   return;
				  }
				  $.tips.success({msg:json.message});
                  $('#addWin').window("close");
                  query();
                  intiFactoryName();
                  intiDrugName();
			  }
		});
	}
	
	//addWin canel
    function cancel(){
    	$('#addWin').window("close");
	}
	
	//general operation
	function generalOperation(id){
		var html = "<a href='#'  onclick='updateDrug("+id+")' class='l-btn l-btn-plain'><span class='l-btn-left'><span class='l-btn-text icon-doing' style='padding-left: 20px;'>修改</span></span></a>";
		html+="<a href='#'  onclick='deleteDrug("+id+")' class='l-btn l-btn-plain'><span class='l-btn-left'><span class='l-btn-text icon-remove' style='padding-left: 20px;'>删除</span></span></a>";
		return html;
	}
	//update drug
	function updateDrug(id){
		$.ajax({
			  type:'POST',
			  url: 'queryDrugById.do',
			  data: {"id":id},
			  success: function(data){
				  var json = $.parseJSON(data);
				  if(!json.success){
					   $.tips.error({msg:json.message});
					   return;
				  }
				$('#addDrugForm').form('load',json.entity);
                $('#id').val(id);
				$('#addWin').window('open');
				intiFactoryName();
	            intiDrugName();
			  }
		});
	}
	function deleteDrug(id){
		$.messager.confirm('提示信息', '确定要删除该条信息?', function(r) {
			   if (r) {
					$.ajax({
						  type:'POST',
						  url: 'deleteDrug.do',
						  data: {"id":id},
						  success: function(data){
							  var json = $.parseJSON(data);
							  if(!json.success){
								   $.tips.error({msg:json.message});
								   return;
							  }
							  $.tips.success({msg:json.message});
							  query();
						  }
					});
			   }
			});
	}
	function intiFactoryName(){
		$.ajax({
			  type:'POST',
			  url: 'queryFactoryCode.do',
			  success: function(data){
				  var json = $.parseJSON(data);
				  if(json.success){
						$("#factoryName").autocomplete(json.list, {
							minChars:0,
							width:520,
							matchContains:true,
							formatItem: function(row, i, max) {
								return row.value;
							},
							formatMatch: function(row, i, max) {
								return row.key;
							},
							formatResult: function(row) {
								return row.value;
							}
						});
				  }
			  }
		});
	}
	function intiDrugName(){
		$.ajax({
			  type:'POST',
			  url: 'queryDrugCode.do',
			  success: function(data){
				  var json = $.parseJSON(data);
				  if(json.success){
						$("#drugName").autocomplete(json.list, {
							minChars:0,
							width:520,
							matchContains:true,
							formatItem: function(row, i, max) {
								return row.value;
							},
							formatMatch: function(row, i, max) {
								return row.key;
							},
							formatResult: function(row) {
								return row.value;
							}
						});
				  }
			  }
		});
	}
</script>
</head>
<body id="B-page" style="font-family:'Microsoft YaHei', 'Trebuchet MS', 'Lucida Grande', Arial, Sans-serif;">
	<div id="B-body" class="clearfix">
		<#escape x as x?html>
		<div id="B-content">
			<div id="B-content-wrap" class="radius">
				<!-- 主要内容 开始 -->
				<div class="title-area sp">
					<h1 class="f-yh">基本信息管理</h1>
				</div>
				<div class="form-area">
					<table class="form-mod" style="margin-bottom:4px;">
						<col width="10%" />
						<col width="20%" />
						<col width="10%" />
						<col width="20%" />
						<col width="10%" />
						<tr>
							<th>企业名称 :</th>
							<td><input type="text" id="factoryName_query" name="factoryName_query"
								class="in-text" size="30" /></td>
							<th>药品名称 :</th>
							<td><input type="text" id="drugName_query" name="drugName_query"
								class="in-text" size="30" /></td>
						</tr>
						<tr>
							<th>药品条形码 :</th>
							<td colspan="3"><input type="text" id="drugCode_query" name="drugName_query"
								class="in-text" size="30" />
							</td>
						</tr>
					</table>
					<div class="form-action" style="text-align:center">
			  	        <input id="btnQuery" type="button" style="margin-left:20px;" class="in-button s2 sp" value="查询" /> 
						 <input id="btnAdd" type="button" class="in-button s2 sp" value="新增" /> 
					</div>
					<table id="drugTable" class="easyui-datagrid" title="信息列表" style="height:380px;padding:5px;"
						data-options="rownumbers:true,singleSelect:true,nowrap:true,striped:true,fitColumns:true">
						<thead>
							<tr>
						        <th data-options="field:'ID',hidden:true"></th>
								<th data-options="field:'DRUGNAME',width:120">药品名称</th>
								<th data-options="field:'FACTORYNAME',width:160">企业名称</th>
								<th data-options="field:'DRUGWH',width:60">批准文号</th>
								<th data-options="field:'CF',width:40,
								 formatter:function(value,row,index){
					   				return value=='1'?'是':'否';
								 }">OTC</th>
								<th data-options="field:'MEMBERPRICE',width:40">会员价格</th>
								<th data-options="field:'LSPRICE',width:40">零售价格</th>
								<th data-options="field:'DRUGTYPENAME',width:50">药品分类</th>
								<th data-options="field:'delete',width:100,align:'center',
								  formatter:function(value,row,index){
								   return generalOperation(row.ID);
								  }">操作</th>
							</tr>
						</thead>
					 </table>
					 <div id="drugPage" class="easyui-pagination" style="background:#efefef; border: 1px solid #ccc;margin-top:2px;"
							data-options="pageSize:12,showRefresh:false,pageList:[12],
								onSelectPage:function(pageNumber,pageSize){
			  						query();
				    			}"></div>
					 </div>
				   <!-- add window -->
					<div id="addWin" class="easyui-window"
					data-options="closed:true,modal:true,iconCls:'icon-tip',collapsible:false,minimizable:false,title:'基本信息'"
					style="width:660px; height:350px; padding: 2px;">
					<div class="easyui-layout" data-options="fit:true">
						<div data-options="region:'center',border:false"
							style="padding: 10px; background: #fff; border: 1px solid #ccc;">
							<form id="addDrugForm">
							<input type="hidden" id="id" name="id"/>
							<table class="form-mod">
								<col width="120px"/>
								<col width="180px"/>
								<col width="120px"/>
								<tr>
									<th>企业名称 <span style="color: red">*</span> :
									</th>
									<td colspan="3" ><input type="text" id="factoryName" name="factoryName" class="in-text easyui-validatebox"
									data-options="required:true"  size="85" /></td>
								</tr>
								<tr>
									<th>药品名称 <span style="color: red">*</span> :
									</th>
									<td colspan="3" ><input type="text" id="drugName" name="drugName" class="in-text easyui-validatebox" 
									data-options="required:true" size="85"/></td>
								</tr>
								<tr>
									<th>药品条形码 <span style="color: red">*</span> :</th>
									<td><input type="text" id="drugCode" name="drugCode" class="in-text" size="30" /></td>
									<th>批准文号 <span style="color: red">*</span> :</th>
									<td><input type="text" id="drugWH" name="drugWH" class="in-text easyui-validatebox" data-options="required:true" size="30" /></td>
								</tr>
								<tr>
									<th>药品规格  :</th>
									<td><input type="text" id="drugRule" name="drugRule" class="in-text" size="30" /></td>
									<th>药品剂型  :</th>
									<td><input type="text" id="drugJX" name="drugJX" class="in-text" size="30" /></td>
								</tr>
								<tr>
									<th>药品单位 :</th>
									<td colspan="3"><input type="text" id="drugUnit" name="drugUnit" class="in-text" size="30" /></td>
								</tr>
								<tr>
								    <th>会员价格 :</th>
									<td><input type="text" id="memberPrice" name="memberPrice" class="in-text easyui-numberbox" data-options="precision:2,groupSeparator:','"  size="30" /></td>
									<th>零售价格 :</th>
									<td><input type="text" id="lsPrice" name="lsPrice" class="in-text easyui-numberbox" data-options="precision:2,groupSeparator:','" size="30" /></td>
								</tr>
								<tr>
	                            <th>OTC <span style="color: red">*</span> :</th>
								<td><select id="isCF" name="isCF" class="in-select">
											<option value="2">否</option>
											<option value="1">是</option>
									</select>
								</td>
								<th>药品分类  <span style="color: red">*</span> :</th>
								<td><select id="drugType" name="drugType" class="in-select">
								         	<#list drugTypes as entity>
											<option value="${entity.key}">${entity.value}</option>
											</#list>
									</select>
								</td> 
								</tr>
							</table>
						   </form>
						</div>
						<div data-options="region:'south',border:false"
							style="text-align: right; padding: 5px 0;height:40px">
							<a class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
								href="javascript:void(0)" onclick="ok()">确定</a> <a
								class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
								href="javascript:void(0)" onclick="cancel()">取消</a>
						</div>
					</div>
				  </div>
		<!-- 主要内容 结束 -->
			</div>
		</div>
		<!-- content -->
		</#escape> <#-- sidebar --> <#include "/widgets/sidebar.ftl"/>
	</div>
	<!-- body -->
</body>
</html>
