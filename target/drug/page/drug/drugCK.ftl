<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>药店管理系统-药品销售</title> 
<#-- meta --> 
<#include "/widgets/meta.ftl"/>
<script type="text/javascript" src="../js/autoComplete/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="../css/autoComplete/jquery.autocomplete.css" /> 
<script type="text/javascript">
var dg_entity,dg_index,dg_row;
	jQuery().ready(function() {
		$('#drugCode').keydown(function(e){
			if(e.keyCode==13){
				$.ajax({
					  type:'POST',
					  url: '../drugcommon/queryDrugByCode.do',
					  data: {"drugCode":$('#drugCode').val()},
					  success: function(data){
						  var json = $.parseJSON(data);
						  if(!json.success){
							   $.tips.error({msg:json.message});
							   return;
						  }
						  $('#xsPrice').val(json.entityList[0].lsPrice);
						  $('#xsNumber').numberspinner('setValue','1');
						  $('#drugForm').form('load',json.entityList[0]);	 
						  dg_entity = json.entityList[0];
					  }
				});
			}
		});
	 //添加药品
	 $('#addDrug').click(function(){
		 if(!$("#drugForm").form('validate')){
				$.tips.error({msg :"请填写必填项!"});
				return;
		  }
		 var entity = copyObject();
		 entity.xsPrice = $('#xsPrice').val();
		 entity.xsNumber =$('#xsNumber').numberspinner('getValue');
	      $('#drugTable').datagrid('appendRow',entity);
	      $('#drugCode').val('');
	      $('#drugId').val('');
	      var totalPrice = $('#totalPrice').html();
	      if(totalPrice==''){
	    	  totalPrice='0';
	      }
	      totalPrice=(parseFloat(totalPrice)+entity.xsPrice*entity.xsNumber).toFixed(2);
	      $('#totalPrice').html(totalPrice);
	 });
		//提交
	 $('#saveDrug').click(function(){
		   var rowTotal = $('#drugTable').datagrid('getData').rows.length;
		   if(rowTotal==0){
			   $.tips.error({msg:"请添加药品!"});
			   return;
		   }
		   $.messager.confirm('提示信息', '确定提交?', function(r) {
				   if (r) {
						$.ajax({
							  type:'POST',
							  url: 'ckOperation.do',
							  data: {"ckType":$('#ckType').val(),
								     "totalPrice":$('#totalPrice').html(),
								     "ckList":$('#drugTable').datagrid('getData').rows,
								     "rows":rowTotal
							        },
							  success: function(data){
								  var json = $.parseJSON(data);
							  if(!json.success){
								   $.tips.error({msg:json.message});
								   return;
							  }
							  //clear
							   $('#drugForm')[0].reset();
							   $('#xsNumber').numberspinner('setValue','1');
							   $('#drugTable').datagrid("loadData",[]);
							   $('#totalPrice').html("0");
							  //tip
							   $.tips.success({msg:json.message});
							  }
						});
				     }	   
				});
	 });
		//init autoComplete
	 	initDrugId();
	});
	//general operation
	function generalOperation(drugCode){
	  return "<a href='#'  onclick='deleteDrug("+drugCode+")' class='l-btn l-btn-plain'><span class='l-btn-left'><span class='l-btn-text icon-remove' style='padding-left: 20px;'>删除</span></span></a>";
	}
	//delete drug
	function deleteDrug(drugCode){
		$.messager.confirm('提示信息', '确定要删除该条信息?', function(r) {
		   if (r) {
			 $('#drugTable').datagrid('deleteRow',dg_index);
			  var totalPrice = $('#totalPrice').html();
		      if(totalPrice==''){
		    	  totalPrice='0';
		      }
		      totalPrice=(parseFloat(totalPrice)-dg_row.xsPrice*dg_row.xsNumber).toFixed(2);
		      $('#totalPrice').html(totalPrice);
		     }	   
		});
	}
	//datagrid click
	function drugClick(index,rowData){
		dg_index = index;
		dg_row=rowData;
	}
	function copyObject(){
		var obj = {"drugCode":dg_entity.drugCode,"drugName":dg_entity.drugName,"factoryName":dg_entity.factoryName,
				   "isCF":dg_entity.isCF,"drugTypeName":dg_entity.drugTypeName,"lsPrice":dg_entity.lsPrice};
		return obj
	}
	function initDrugId(){
		$("#drugId").autocomplete('../drugcommon/queryDrug.do', {
			minChars:0,
			width:200,
			matchSubset:false,
			matchCase:false,
			dataType:"json",
			max:20,
			extraParams:{limit:"20"},
		    parse: function(data) {
                return $.map(eval(data), function(row) {
                    return {
                     data: row
                   }
                 });
     		 },
			formatItem: function(row, i, max) {
				return row.drugName+"【"+row.factoryName+"】";
			},
			formatMatch: function(row, i, max) {
				return row.drugzjm;
			}
			}).result(function(event, data, formatted) {
				  $('#xsPrice').val(data.lsPrice);
				  $('#xsNumber').numberspinner('setValue','1');
				  $('#drugForm').form('load',data);	 
				  dg_entity = data;
				  $('#drugId').val(data.drugName+"【"+data.factoryName+"】");
			});
}
</script>
</head>
<body id="B-page" style="font-family:'Microsoft YaHei', 'Trebuchet MS', 'Lucida Grande', Arial, Sans-serif;">
	<div id="B-body" class="clearfix">
		<#escape x as x?html>
		<div id="B-content" >
			<div id="B-content-wrap" class="radius">
				<!-- 主要内容 开始 -->
				<div class="title-area sp">
					<h1 class="f-yh">药品销售</h1>
				</div>
				<!-- title-area -->
				<div class="form-area">
					<form id="drugForm">
						<table class="form-mod" style="margin-bottom:4px;">
							<col width="120px"/>
							<col width="180px"/>
							<col width="120px"/>
							<col width="180px"/>
							<col width="120px"/>
							<tr>
								<th>药品条形码  :</th>
								<td colspan="2" >
								<input type="text" id="drugCode" name="drugCode" class="in-text" size="30" />
								</td>
								<th>药品助记码  :</th>
								<td colspan="2" >
								<input type="text" id="drugId" name="drugId" class="in-text" size="30" />
								</td>
							</tr>
	                        <tr>
								<th>药品名称  :
								</th>
								<td colspan="5" ><input type="text" id="drugName" name="drugName" disabled="disabled" class="in-text" size="100" /></td>
							</tr>
							<tr>
								<th>企业名称  :
								</th>
								<td colspan="5" ><input type="text" id="factoryName" name="factoryName" disabled="disabled" class="in-text" size="100"/></td>
							</tr>
							<tr>
								<th>批准文号 :</th>
								<td><input type="text" id="drugWH" name="drugWH" disabled="disabled" class="in-text"  size="20" /></td>
								<th>会员价格 :</th>
								<td><input type="text" id="memberPrice" name="memberPrice" disabled="disabled" class="in-text" size="20" /></td>
						        <th>零售价格 :</th>
								<td><input type="text" id="lsPrice" name="lsPrice" disabled="disabled" class="in-text" size="20" /></td>
							</tr>
							<tr>
								<th>药品规格  :</th>
								<td><input type="text" id="drugRule" name="drugRule" disabled="disabled" class="in-text" size="20" /></td>
								<th>药品剂型  :</th>
								<td><input type="text" id="drugJX" name="drugJX" disabled="disabled" class="in-text" size="20" /></td>
								<th>药品单位 :</th>
								<td><input type="text" id="drugUnit" name="drugUnit" disabled="disabled" class="in-text" size="20" /></td>
							</tr>
							<tr>
	                            <th>OTC  :</th>
								<td><select id="isCF" name="isCF" class="in-select" disabled="disabled">
											<option value="2">否</option>
											<option value="1">是</option>
									</select>
								</td>
								<th>药品分类   :</th>
								<td><select id="drugType" name="drugType" class="in-select" disabled="disabled">
								         	<#list drugTypes as entity>
											<option value="${entity.key}">${entity.value}</option>
											</#list>
									</select>
								</td> 
								<th>库存数量 :</th>
								<td><input type="text" id="drugNumber" name="drugNumber" disabled="disabled" class="in-text" size="20" /></td>
							</tr> 					
							<tr>
								<th>销售价格 <span style="color: red">*</span> :</th>
								<td><input type="text" id="xsPrice" name="xsPrice" class="in-text easyui-validatebox"  data-options="required:true" size="20" /></td>
								<th>销售数量  <span style="color: red">*</span> :</th>
								<td colspan="3">
								 <input type="text" id="xsNumber" name="xsNumber" class="easyui-numberspinner" style="width:80px;" data-options="required:true,min:1,max:1000,editable:true" value="1" />
								 <input id="addDrug" type="button" style="margin-left:40px;" class="in-button s2 sp" value="添加" /> 
								</td>
							</tr>
						</table>
					</form>
						<table id="drugTable" class="easyui-datagrid" title="销售列表" style="height:220px;padding:5px;"
						data-options="rownumbers:true,singleSelect:true,nowrap:true,striped:true,fitColumns:true,
						onClickRow:function(rowIndex,rowData){
			  				 drugClick(rowIndex,rowData);
							}">
						<thead>
							<tr>
						        <th data-options="field:'drugCode',hidden:true"></th>
								<th data-options="field:'drugName',width:80">药品名称</th>
								<th data-options="field:'factoryName',width:120">企业名称</th>
								<th data-options="field:'isCF',width:40,
								 formatter:function(value,row,index){
					   				return value=='1'?'是':'否';
								 }">是否处方</th>
								<th data-options="field:'drugTypeName',width:80">药品分类</th>
								<th data-options="field:'lsPrice',width:60,align:'right'">零售价格</th>
								<th data-options="field:'xsPrice',width:80,align:'right'">销售价格</th>
								<th data-options="field:'xsNumber',width:40,align:'right'">销售数量</th>
								<th data-options="field:'delete',width:80,align:'center',
								  formatter:function(value,row,index){
								   return generalOperation(row.drugCode);
								  }">操作</th>
							</tr>
						</thead>
					 </table>
					 	<div class="form-action" style="text-align:right;">
					 		 <p style="padding-bottom:4px;color:red;font-size:14px;">合计:<span id="totalPrice">0</span> 元</p>
							 <select id="ckType" name="ckType" class="in-select sp">
								<option value="1">销售-现金销售</option>
								<option value="2">销售-医保刷卡</option>
								<option value="3">销售-居民刷卡</option>
								<option value="4">销毁-过期</option>
								<option value="9">其他</option>
							</select>
							<input id="saveDrug" type="button" class="in-button s2" value="提交" /> 
						</div>
				</div>
				<!-- form-area end -->
				<!-- 主要内容 结束 -->
			</div>
		</div>
		<!-- content -->
		</#escape> <#-- sidebar --> 
		 <#if sidebar> 
		 <#include "/widgets/sidebar.ftl"/> 
		 </#if>
	</div>
	<!-- body -->
</body>
</html>
