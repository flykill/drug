<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>药店管理系统-药品入库</title> 
<#-- meta --> 
<#include "/widgets/meta.ftl"/> 
<script type="text/javascript" src="../js/autoComplete/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="../css/autoComplete/jquery.autocomplete.css" />
<script type="text/javascript">
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
							  json.entityList[0].drugNumber ="";
							  json.entityList[0].factoryName_hidden = json.entityList[0].factoryName;
							  $('#drugForm').form('load',json.entityList[0]);	
						  }
				});
			}
		});
		//add click
		$('#addDrug').click(function() {
			if(!$("#drugForm").form('validate') ||$('#drugValidity').datebox('getValue')==''){
				$.tips.error({msg :"请填写必填项!"});
				return;
			}
			$.ajax({
				  type:'POST',
				  url: 'rkOperation.do',
				  data: $('#drugForm').serialize(),
				  success: function(data){
					  var json = $.parseJSON(data);
					  if(!json.success){
						   $.tips.error({msg:json.message});
						   return;
					  }
					  $('#drugForm')[0].reset();
					  $('#drugForm .easyui-numberbox').numberbox('clear');
					  $.tips.success({msg:json.message});
					  initGjFactoryName();
				  }
			});
		});
		//init autoComplete
		initGjFactoryName();
		//init drug
		initDrugId();
	});
	
	function initGjFactoryName(){
		$.ajax({
			  type:'POST',
			  url: 'queryGjFactoryCode.do',
			  success: function(data){
				  var json = $.parseJSON(data);
				  if(json.success){
						$("#gjFactoryName").autocomplete(json.list, {
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
					  data.drugNumber ="";
					  data.factoryName_hidden = data.factoryName;
					  $('#drugForm').form('load',data);	
					  $('#drugId').val(data.drugName+"【"+data.factoryName+"】");
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
					<h1 class="f-yh">药品入库</h1>
				</div>
				<!-- title-area -->
				<div class="form-area">
					<form id="drugForm">
						<table class="form-mod">
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
								<th>药品名称  :</th>
								<td colspan="5" >
								    <input type="text" id="drugName" name="drugName" disabled="disabled" class="in-text" size="100" />
								</td>
							</tr>
							<tr>
								<th>企业名称  :</th>
								<td colspan="5" >
								      <input type="text" id="factoryName" name="factoryName" disabled="disabled" class="in-text" size="100"/>
								      <input type="hidden" id="factoryName_hidden" name="factoryName_hidden"/>              
								</td>
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
							</tr>
							<tr>
								<th>购进企业名称  :</th>
								<td colspan="5" ><input type="text" id="gjFactoryName" name="gjFactoryName" class="in-text" size="100" /></td>
							</tr>
							<tr>
								<th>进货价格  :</th>
								<td><input type="text" id="JHPrice" name="JHPrice"  class="in-text" size="20" /></td>
								<th>药品批号 <span style="color: red">*</span> :</th>
								<td><input type="text" id="drugPH" name="drugPH"  class="in-text easyui-validatebox"  data-options="required:true" size="20" /></td>
								<th>药品效期 <span style="color: red">*</span> :</th>
								<td><input type="text" id="drugValidity" name="drugValidity" class="in-text easyui-datebox" size="20" /></td>
							</tr>
							<tr>
								<th>进货数量 <span style="color: red">*</span> :</th> 
								<td><input type="text" id="drugNumber" name="drugNumber" class="in-text easyui-numberbox easyui-validatebox"  data-options="required:true,groupSeparator:','" size="20" /></td>
								<th>入库方式 <span style="color: red">*</span> :</th>
								<td><select id="rkType" name="rkType" class="in-select">
										<option value="1">正常入库</option>
										<option value="2">代销入库</option>
										<option value="3">退药入库</option>
										<option value="9">其他</option>
								</select></td>
							</tr>
						</table>
						<div class="form-action" style="padding-left: 400px">
						  	 <input id="resetDrug" type="reset" class="in-button s2 sp" value="重置" />
							 <input id="addDrug" type="button" class="in-button s2 sp" value="录入" /> 
						</div>
					</form>
				</div>
				<!-- form-area end -->
				<!-- 主要内容 结束 -->
			</div>
		</div>
		<!-- content -->
		</#escape> <#-- sidebar -->
		<#include "/widgets/sidebar.ftl"/>
	</div>
	<!-- body -->
</body>
</html>
