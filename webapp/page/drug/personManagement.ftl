<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>药店管理系统-用户管理</title> 
<#-- meta --> 
<#include "/widgets/meta.ftl"/>
<script type="text/javascript" src="../js/autoComplete/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="../css/autoComplete/jquery.autocomplete.css" />
<script type="text/javascript">
	jQuery().ready(function() {
		//新增
		$('#btnAdd').click(function() {
			$('#id').val("");
			$('#addWin').window('open');
			$('#addPersonForm')[0].reset();
		});
		//查询 
		$('#btnQuery').click(function() {
			query();
		});
		//init
		query();
	});
	
	function query(){
		var pageNumber= $('#personPage').pagination('options').pageNumber;
		var pageSize=$('#personPage').pagination('options').pageSize;
		$.ajax({
			  type:'POST',
			  url: 'queryPerson.do',
			  data: {
				  pageNumber:pageNumber,
				  pageSize:pageSize,
				  userName:$('#personName_query').val()
			  },
			  success: function(data){
				  var json = $.parseJSON(data);
				  if(!json.success){
						$.tips.error({msg : json.message});
						  return; 
				  }
				$('#personTable').datagrid("loadData", json.roleList);
				$('#personPage').pagination({"total" : json.total});
	  			}
		});	
	}
	//addWin ok
	function ok(){
		var roleIds = $('#roleId').combobox('getValues');
		if(!$("#addPersonForm").form('validate') || ''==roleIds){
			$.tips.error({msg :"请填写必填项!"});
			return;
		}
		$('#roleIds').val(roleIds);
		$('#roleNames').val($('#roleId').combobox('getText'));
		$.ajax({
			  type:'POST',
			  url: 'savePerson.do',
			  data:$('#addPersonForm').serialize(),
			  success: function(data){
				  var json = $.parseJSON(data);
				  if(!json.success){
					   $.tips.error({msg:json.message});
					   return;
				  }
				  $.tips.success({msg:json.message});
                  $('#addWin').window("close");
                  query();
			  }
		});
	}
	
	//addWin canel
    function cancel(){
    	$('#addWin').window("close");
	}
	
	//general operation
	function generalOperation(id){
		var html = "<a href='#'  onclick='updatePerson("+id+")' class='l-btn l-btn-plain'><span class='l-btn-left'><span class='l-btn-text icon-doing' style='padding-left: 20px;'>修改</span></span></a>";
		html+="<a href='#'  onclick='deletePerson("+id+")' class='l-btn l-btn-plain'><span class='l-btn-left'><span class='l-btn-text icon-remove' style='padding-left: 20px;'>删除</span></span></a>";
		return html;
	}
	//update drug
	function updatePerson(id){
		$.ajax({
			  type:'POST',
			  url: 'queryPersonById.do',
			  data: {"id":id},
			  success: function(data){
				  var json = $.parseJSON(data);
				  if(!json.success){
					   $.tips.error({msg:json.message});
					   return;
				  }
				$('#addPersonForm').form('load',json.entity);
				$('#id').val(id);
				$('#roleId').combobox('setValues',json.entity.roleList);
				$('#addWin').window('open');
			  }
		});
	}
	function deletePerson(id){
		$.messager.confirm('提示信息', '确定要删除该条信息?', function(r) {
			   if (r) {
					$.ajax({
						  type:'POST',
						  url: 'deletePerson.do',
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
	
</script>
</head>
<body id="B-page" style="font-family:'Microsoft YaHei', 'Trebuchet MS', 'Lucida Grande', Arial, Sans-serif;">
<#-- header --> 
<#include "/widgets/header.ftl"/>
	<div id="B-body" class="clearfix">
		<#escape x as x?html>
		<div id="B-content">
			<div id="B-content-wrap" class="radius">
				<!-- 主要内容 开始 -->
				<div class="title-area sp">
					<h1 class="f-yh">用户管理</h1>
				</div>
				<div class="form-area">
					<table class="form-mod" style="margin-bottom:4px;">
						<col width="10%" />
						<tr>
							<th>用户名称 :</th>
							<td> <input type="text" id="personName_query" name="personName_query" class="in-text" size="30" />
								 <input id="btnQuery" type="button" style="margin-left:20px;" class="in-button s2 sp" value="查询" /> 
								 <input id="btnAdd" type="button" class="in-button s2 sp" value="新增" /> 
							</td>
						</tr>
					</table>
					<table id="personTable" class="easyui-datagrid" title="用户列表" style="height:380px;padding:5px;"
						data-options="rownumbers:true,singleSelect:true,nowrap:true,striped:true,fitColumns:true">
						<thead>
							<tr>
						        <th data-options="field:'ID',hidden:true"></th>
								<th data-options="field:'USERCODE',width:100">用户账号</th>
								<th data-options="field:'USERNAME',width:100">用户名称</th>
								<th data-options="field:'ROLENAMES',width:300">角色列表</th>
								<th data-options="field:'EFFECTIVE',width:80,
								formatter:function(value,row,index){
								   return value=='1'?'有效':'无效';
								  }">是否有效</th>
								<th data-options="field:'delete',width:100,align:'center',
								  formatter:function(value,row,index){
								   return generalOperation(row.ID);
								  }">操作</th>
							</tr>
						</thead>
					 </table>
					 <div id="personPage" class="easyui-pagination" style="background:#efefef; border: 1px solid #ccc;margin-top:2px;"
							data-options="pageSize:12,showRefresh:false,pageList:[12],
								onSelectPage:function(pageNumber,pageSize){
			  						query();
				    			}"></div>
					 </div>
				   <!-- add window -->
					<div id="addWin" class="easyui-window"
					data-options="closed:true,modal:true,iconCls:'icon-tip',collapsible:false,minimizable:false,title:'用户信息'"
					style="width:400px; height:270px; padding: 2px;">
					<div class="easyui-layout" data-options="fit:true">
						<div data-options="region:'center',border:false"
							style="padding: 10px; background: #fff; border: 1px solid #ccc;">
							<form id="addPersonForm">
							<input type="hidden" id="id" name="id"/>
							<input type="hidden" id="roleIds" name="roleIds"/>
							<input type="hidden" id="roleNames" name="roleNames"/>
							<table class="form-mod">
								<col width="60px"/>
								<col width="180px"/>
								<tr>
									<th>用户账号 <span style="color: red">*</span> :</th>
									<td><input type="text" id="userCode" name="userCode" class="in-text easyui-validatebox"
									data-options="required:true" size="30" /></td>
								</tr>
								<tr>
									<th>用户名称 <span style="color: red">*</span> :</th>
									<td><input type="text" id="userName" name="userName" class="in-text easyui-validatebox"
									data-options="required:true" size="30"/></td>
								</tr>
								<tr>
									<th>用户密码 <span style="color: red">*</span> :</th>
									<td><input type="password" id="pwd" name="pwd" class="in-text easyui-validatebox"
									data-options="required:true" size="30"/></td>
								</tr>
								<tr>
									<th>角色列表 <span style="color: red">*</span> :</th>
									<td><input class="easyui-combobox" id="roleId" name="roleId" data-options="
												url:'queryRole.do',
												valueField:'id',
												textField:'text',
												multiple:true,
												panelHeight:'auto'
										"></td>
								</tr>
								<tr>
									<th>是否有效 :</th>
									<td><label><input type="radio" value="1" name="effective" checked="checked" />有效&nbsp;&nbsp;</label>
									    <label><input type="radio" value="2" name="effective"/>无效</label>
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
