<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>药店管理系统-角色管理</title> 
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
			$('#addRuleForm')[0].reset();
			$('#ruleTree').tree("reload");
		});
		//查询 
		$('#btnQuery').click(function() {
			query();
		});
		//init
		query();
	});
	
	function query(){
		var pageNumber= $('#rulePage').pagination('options').pageNumber;
		var pageSize=$('#rulePage').pagination('options').pageSize;
		$.ajax({
			  type:'POST',
			  url: 'queryRole.do',
			  data: {
				  pageNumber:pageNumber,
				  pageSize:pageSize,
				  roleName:$('#roleName_query').val()
			  },
			  success: function(data){
				  var json = $.parseJSON(data);
				  if(!json.success){
						$.tips.error({msg : json.message});
						  return; 
				  }
				$('#ruleTable').datagrid("loadData", json.roleList);
				$('#rulePage').pagination({"total" : json.total});
	  			}
		});	
	}
	//addWin ok
	function ok(){
		var nodes = $('#ruleTree').tree('getChecked');
		if(!$("#addRuleForm").form('validate') || null==nodes){
			$.tips.error({msg :"请填写必填项!"});
			return;
		}
		var menuIds="",menuNames="";
		for(var i=0; i<nodes.length; i++){
			var children = $('#ruleTree').tree('getChildren', nodes[i].target);
		    if(children ==''){
		    	if (menuIds != '') menuIds += ',';
				if (menuNames != '') menuNames += ',';
		    	menuIds += nodes[i].id;
		    	menuNames+=nodes[i].text;
		    }
		}
		$.ajax({
			  type:'POST',
			  url: 'saveRule.do',
			  data: {
				  roleName:$('#ROLENAME').val(),
			      roleDescription:$('#ROLEDESCRIPTION').val(),
			      menuIds:menuIds,
			      menuNames:menuNames,
			      id:$('#id').val()
			  },
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
		var html = "<a href='#'  onclick='updateRole("+id+")' class='l-btn l-btn-plain'><span class='l-btn-left'><span class='l-btn-text icon-doing' style='padding-left: 20px;'>修改</span></span></a>";
		html+="<a href='#'  onclick='deleteRole("+id+")' class='l-btn l-btn-plain'><span class='l-btn-left'><span class='l-btn-text icon-remove' style='padding-left: 20px;'>删除</span></span></a>";
		return html;
	}
	//update drug
	function updateRole(id){
		$.ajax({
			  type:'POST',
			  url: 'queryRoleById.do',
			  data: {"id":id},
			  success: function(data){
				  var json = $.parseJSON(data);
				  if(!json.success){
					   $.tips.error({msg:json.message});
					   return;
				  }
				$('#addRuleForm').form('load',json.entity);
				$('#id').val(id);
				$('#ruleTree').tree("reload");
				$('#addWin').window('open');
			  }
		});
	}
	function deleteRole(id){
		$.messager.confirm('提示信息', '确定要删除该条信息?', function(r) {
			   if (r) {
					$.ajax({
						  type:'POST',
						  url: 'deleteRole.do',
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
					<h1 class="f-yh">角色管理</h1>
				</div>
				<div class="form-area">
					<table class="form-mod" style="margin-bottom:4px;">
						<col width="10%" />
						<tr>
							<th>角色名称 :</th>
							<td> <input type="text" id="roleName_query" name="roleName_query" class="in-text" size="30" />
								 <input id="btnQuery" type="button" style="margin-left:20px;" class="in-button s2 sp" value="查询" /> 
								 <input id="btnAdd" type="button" class="in-button s2 sp" value="新增" /> 
							</td>
						</tr>
					</table>
					<table id="ruleTable" class="easyui-datagrid" title="角色列表" style="height:380px;padding:5px;"
						data-options="rownumbers:true,singleSelect:true,nowrap:true,striped:true,fitColumns:true">
						<thead>
							<tr>
						        <th data-options="field:'ID',hidden:true"></th>
								<th data-options="field:'ROLENAME',width:100">角色名称</th>
								<th data-options="field:'ROLEDESCRIPTION',width:100">角色描述</th>
								<th data-options="field:'MENUNAMES',width:300">权限列表</th>
								<th data-options="field:'delete',width:100,align:'center',
								  formatter:function(value,row,index){
								   return generalOperation(row.ID);
								  }">操作</th>
							</tr>
						</thead>
					 </table>
					 <div id="rulePage" class="easyui-pagination" style="background:#efefef; border: 1px solid #ccc;margin-top:2px;"
							data-options="pageSize:12,showRefresh:false,pageList:[12],
								onSelectPage:function(pageNumber,pageSize){
			  						query();
				    			}"></div>
					 </div>
				   <!-- add window -->
					<div id="addWin" class="easyui-window"
					data-options="closed:true,modal:true,iconCls:'icon-tip',collapsible:false,minimizable:false,title:'角色信息'"
					style="width:450px; height:450px; padding: 2px;">
					<div class="easyui-layout" data-options="fit:true">
						<div data-options="region:'center',border:false"
							style="padding: 10px; background: #fff; border: 1px solid #ccc;">
							<form id="addRuleForm">
							<input type="hidden" id="id" name="id"/>
							<table class="form-mod">
								<col width="60px"/>
								<col width="180px"/>
								<tr>
									<th>角色名称 <span style="color: red">*</span> :</th>
									<td><input type="text" id="ROLENAME" name="ROLENAME" class="in-text easyui-validatebox"
									data-options="required:true" size="30" /></td>
								</tr>
								<tr>
									<th>角色描述 :</th>
									<td><textarea id="ROLEDESCRIPTION" name="ROLEDESCRIPTION" class="in-textarea"
										style="width:260px; height:40px;"></textarea></td>
								</tr>
								<tr>
								    <th>权限列表 <span style="color: red">*</span> :</th>
								    <td><ul id="ruleTree" class="easyui-tree" data-options="url:'queryTree.do',checkbox:true,
											onClick: function(node){
												$(this).tree('toggle', node.target);
											}"></ul>
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
