<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>药店管理系统-入库查询</title> <#-- meta --> <#include
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
			  url: 'queryRk.do',
			  data: {
				  "pageNumber":pageNumber,
				  "pageSize":pageSize,
				  "drugName":$('#drugName').val(),
			      "drugPH":$('#drugPH').val(),
			      "operationDate":$('#operationDate').datebox('getValue'),
			      "scName":$('#scName').val(),
			      "gjName":$('#gjName').val()
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
<#-- header --> 
<#include "/widgets/header.ftl"/>	
	<div id="B-body" class="clearfix">
		<#escape x as x?html>
		<div id="B-content">
			<div id="B-content-wrap" class="radius">
				<!-- 主要内容 开始 -->
				<div class="title-area sp">
					<h1 class="f-yh">入库查询</h1>
				</div>
				<div class="form-area">
				 <form id="drugForm">
					<table class="form-mod">
						<col width="13%" />
						<col width="20%" />
						<col width="10%" />
						<col width="20%" />
						<col width="10%" />
						<tr>
							<th>药品名称 :</th>
							<td><input type="text" id="drugName" name="drugName" class="in-text" size="20" /></td>
							<th>药品批号 :</th>
							<td><input type="text" id="drugPH" name="drugPH" class="in-text" size="20" /></td>
							<th>入库时间:</th>
							<td><input type="text" id="operationDate" name="operationDate" class="in-text easyui-datebox" size="20" /></td>
						</tr>
						<tr>
							<th>生产企业名称 :</th>
							<td colspan="5"><input type="text" id="scName" name="scName" class="in-text" size="100" /></td>
						</tr>
						<tr>
							<th>购进企业名称 :</th>
							<td colspan="5"><input type="text" id="gjName" name="gjName" class="in-text" size="100" /></td>
						</tr>
					</table>
					<div class="form-action" style="text-align:center;margin-bottom:4px;"">
					  	 <input  type="reset" class="in-button s2 sp" value="重置" />
						 <input id="btnQuery" type="button" class="in-button s2 sp" value="查询" /> 
					</div>
				  </form>
					<table id="drugTable" class="easyui-datagrid" title="记录列表" style="height:360px;"
						data-options="rownumbers:true,singleSelect:true,nowrap:true,striped:true,fitColumns:true">
						<thead>
							<tr>
						        <th data-options="field:'ID',hidden:true"></th>
								<th data-options="field:'DRUGNAME',width:100">药品名称</th>
								<th data-options="field:'FACTORYNAME',width:100">生产企业名称</th>
								<th data-options="field:'GJFACTORYNAME',width:100">购进企业名称</th>
								<th data-options="field:'JHPRICE',width:40,align:'right'">进货价格</th>
								<th data-options="field:'DRUGPH',width:40,align:'right'">药品批号</th>
								<th data-options="field:'DRUGVALIDITY',width:60,align:'right'">药品效期</th>
								<th data-options="field:'DRUGNUMBER',width:40,align:'right'">进货数量</th>
								<th data-options="field:'OPERATIONDATE',width:40,align:'right'">进货时间</th>
								<th data-options="field:'RKTYPE',width:40,
								 formatter:function(value,row,index){
								   if(value=='1'){
							       return '正常入库';
								   }else if(value=='2'){
								   return '代销入库';
								   }else if(value=='3'){
								   return '退药入库';
								   }else{
								   return '其他';
								   }
								 }">入库方式</th>
							</tr>
						</thead>
					 </table>
					 <div id="drugPage" class="easyui-pagination" style="background:#efefef; border: 1px solid #ccc;margin-top:2px;"
							data-options="pageSize:12,showRefresh:false,pageList:[12],displayMsg:'显示{from}到{to},共{total}记录     入库数量合计:{numberTotal},入库总价合计:{priceTotal}',
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
