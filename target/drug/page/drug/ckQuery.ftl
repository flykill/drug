<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>药店管理系统-销售查询</title> <#-- meta --> <#include
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
			  url: 'queryCk.do',
			  data: {
				  "pageNumber":pageNumber,
				  "pageSize":pageSize,
				  "drugName":$('#drugName_query').val(),
			      "factoryName":$('#factoryName_query').val(),
			      "drugCode":$('#drugCode_query').val(),
			      "drugType":$('#drugType').val(),
			      "operationDate":$('#operationDate').datebox('getValue')
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
					<h1 class="f-yh">销售查询</h1>
				</div>
				<div class="form-area">
				  <form id="drugForm">
					<table class="form-mod" style="margin-bottom:4px;">
						<col width="10%" />
						<col width="20%" />
						<col width="10%" />
						<col width="20%" />
						<col width="10%" />
						<tr>
							<th>企业名称 :</th>
							<td><input type="text" id="factoryName_query" name="factoryName_query"
								class="in-text" size="20" /></td>
							<th>药品名称 :</th>
							<td><input type="text" id="drugName_query" name="drugName_query"
								class="in-text" size="20" /></td>
							<th>药品条形码 :</th>
							<td><input type="text" id="drugCode_query" name="drugName_query"
								class="in-text" size="20" />
							</td>
						</tr>
						<tr>
							<th>药品分类 :</th>
							<td><select id="drugType" name="drugType" class="in-select">
							                <option value="">所有</option>
								         	<#list drugTypes as entity>
											<option value="${entity.key}">${entity.value}</option>
											</#list>
									</select>
							</td>
							<th>销售时间:</th>
							<td colspan="3"><input type="text" id="operationDate" name="operationDate" class="in-text easyui-datebox" size="30" /></td>
						</tr>
 					 </table>
 					 <div class="form-action" style="text-align:center;margin-bottom:4px;">
					  	 <input  type="reset" class="in-button s2 sp" value="重置" />
						 <input id="btnQuery" type="button" class="in-button s2 sp" value="查询" /> 
					</div>
					</form>
					<table id="drugTable" class="easyui-datagrid" title="信息列表" style="height:370px;padding:5px;"
						data-options="rownumbers:true,singleSelect:true,nowrap:true,striped:true,fitColumns:true">
						<thead>
							<tr>
						        <th data-options="field:'ID',hidden:true"></th>
								<th data-options="field:'DRUGNAME',width:100">药品名称</th>
								<th data-options="field:'FACTORYNAME',width:100">企业名称</th>
								<th data-options="field:'CF',width:30,
								 formatter:function(value,row,index){
					   				return value=='1'?'是':'否';
								 }">OTC</th>
								<th data-options="field:'DRUGTYPENAME',width:40">药品分类</th>
								<th data-options="field:'MEMBERPRICE',width:40,align:'right'">会员价格</th>
								<th data-options="field:'LSPRICE',width:40,align:'right'">零售价格</th>
								<th data-options="field:'XSPRICE',width:40,align:'right'">销售价格</th>
								<th data-options="field:'XSNUMBER',width:40,align:'right'">销售数量</th>
								<th data-options="field:'OPERATIONDATE',width:40">销售时间</th>
								<th data-options="field:'CKTYPE',width:60,
								formatter:function(value,row,index){
								    if(value=='1'){
							       return '销售-现金销售 ';
								   }else if(value=='2'){
								   return '销售-医保刷卡';
								   }else if(value=='3'){
								   return '销售-居民刷卡';
								   }else if(value=='4'){
								   return '销毁-过期';
								   }else{
								   return '其他';
								   }
								}
								">销售方式</th>
							</tr>
						</thead>
					 </table>
					 <div id="drugPage" class="easyui-pagination" style="background:#efefef; border: 1px solid #ccc;margin-top:2px;"
							data-options="pageSize:12,showRefresh:false,pageList:[12],displayMsg:'显示{from}到{to},共{total}记录     销售数量合计:{numberTotal},销售总价合计:{priceTotal}',
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
