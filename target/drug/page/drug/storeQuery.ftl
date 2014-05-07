<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>药店管理系统-库存查询</title> <#-- meta --> <#include
"/widgets/meta.ftl"/>
<script type="text/javascript">
	jQuery().ready(function() {
		//查询 
		$('#btnQuery').click(function() {
			query();
		});
		//init
		query();
	});
	//query
	function query(){
		var pageNumber= $('#drugPage').pagination('options').pageNumber;
		var pageSize=$('#drugPage').pagination('options').pageSize;
		$.ajax({
			  type:'POST',
			  url: 'queryStore.do',
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
					$('#drugPage').pagination({"total" : json.total,"numberTotal":json.numberTotal,"priceTotal":json.priceTotal});
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
					<h1 class="f-yh">库存查询</h1>
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
			  	   	</div>
					<table id="drugTable" class="easyui-datagrid" title="信息列表" style="height:380px;padding:5px;"
						data-options="rownumbers:true,singleSelect:true,nowrap:true,striped:true,fitColumns:true">
						<thead>
							<tr>
						        <th data-options="field:'ID',hidden:true"></th>
								<th data-options="field:'DRUGNAME',width:120">药品名称</th>
								<th data-options="field:'FACTORYNAME',width:140">企业名称</th>
								<th data-options="field:'DRUGNUMBER',width:40">库存数量</th>
								<th data-options="field:'DRUGWH',width:60">批准文号</th>
								<th data-options="field:'CF',width:40,
								 formatter:function(value,row,index){
					   				return value=='1'?'是':'否';
								 }">OTC</th>
								<th data-options="field:'MEMBERPRICE',width:40">会员价格</th>
								<th data-options="field:'LSPRICE',width:40">零售价格</th>
								<th data-options="field:'DRUGTYPENAME',width:40">药品分类</th>
							</tr>
						</thead>
					 </table>
					 <div id="drugPage" class="easyui-pagination" style="background:#efefef; border: 1px solid #ccc;margin-top:2px;"
							data-options="pageSize:12,showRefresh:false,pageList:[12],displayMsg:'显示{from}到{to},共{total}记录     库存数量合计:{numberTotal},库存总价合计:{priceTotal}',
								onSelectPage:function(pageNumber,pageSize){
			  						query();
				    			}"></div>
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
