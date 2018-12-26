<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<%@include file="/common/meta.jsp" %>
 	<title><s:text name='main.title'/></title>
 	<script type="text/javascript">
 	//加载内容
 	$(document).ready(function(){
 		onchangeGsmCodeSecRules();
		$('#datepicker1').datepicker({changeMonth:true,changeYear:true});
		$('#datepicker2').datepicker({changeMonth:true,changeYear:true});
		$('#datepicker3').datepicker({changeMonth:true,changeYear:true});
		$("#measurementForm").validate({rules: {}});
	});
 	//提交保存
 	function submitForm(url){
 		var dateOfPurchase=new Date($("#datepicker1").val());//采购日期
 		var agreedDeliveryDate=new Date($("#datepicker2").val());//约定送货日期
 		var confirmDeliveryDate=new Date($("#datepicker3").val());//确认收货货日期	
 		if(Date.parse(dateOfPurchase)-Date.parse(agreedDeliveryDate)>0){
			alert("<s:text name='采购日期大于约定送货日期！！'/>");
			return false;
		}
		if(Date.parse(dateOfPurchase)-Date.parse(confirmDeliveryDate)>0){
			alert("<s:text name='采购日期大于确认收货货日期！！'/>");
			return false;
		}
 		$('#measurementForm').attr('action',url);
 		$('#measurementForm').submit(); 
	}
 	//加载二级
 	function onchangeGsmCodeSecRules(){
 		var id = $("#measurementType").val();
		var params = {id:id};
		$.showMessage("<s:text name='正在加载请稍候... ...'/>","custom");
		$.post("${gsmctx}/intransited-equipment/top-bot-options-gsmCodeSecRules.htm",params,function(result){
			$.clearMessage();
			if(result.error){
				$.showMessage(result.message);
			}else{
				$("#secondaryClassification").empty();
				if(result.botOptions.length == 0){
					$("#secondaryClassification").attr("disabled","disabled");
					$("#secondaryClassification").css({background: "#eeeeee"});
					return false;
				}
				$("#secondaryClassification").attr("disabled","");
				$("#secondaryClassification").css({background: ""});
				var multiWidth = 240;
				for (var i = 0; i < result.botOptions.length; i++) {
					var option = result.botOptions[i];
					$("#secondaryClassification").append("<option value="+option.name+">"+option.value+"</option>");
					if(option.value.length > multiWidth){multiWidth = option.value.length;}
				}
				//$("#secondaryClassification").multiselect("refresh");
				//$("#secondaryClassification").width(multiWidth+"px").multiselect({selectedList:1});
			}
		},'json');
 	}
 	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="intransited";
		var thirdMenu="_myIntransitedEquipment";
 	</script>
 	
 	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>
	
 	<div class="ui-layout-west">
	<%@ include file="/menus/gsm-intransited-equipment-menu.jsp" %>
	</div>
	
 	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<button class='btn' onclick="javascript:window.location='${gsmctx}/intransited-equipment/input.htm';"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
 				<button class='btn' onclick="submitForm('${gsmctx}/intransited-equipment/save.htm?saveType=input');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				<button class='btn' onclick="history.back();"><span><span><b class="btn-icons btn-icons-undo"></b><s:text name="返回"/></span></span></button>				
				<span style="color: red;" id="message"><s:actionmessage theme="mytheme" cssStyle="color:red;" /></span>
			</div>
			<div id="opt-content" style="text-align: center;">
				<form action="" method="post" id="measurementForm">
					<div id="hidden">
						<input type="hidden" name="params.savetype" value="input" />
						<input type="hidden" id="id" name="id" value="${id}" />
						<input type="hidden" id="gsmCodeRules" name="gsmCodeRules" value="${gsmCodeRules}" />
						<input type="hidden" id="serialNo" name="serialNo" value="${serialNo}" />
					</div>
					<table class="form-table-border-left" style="width:100%;margin: auto;">
						<caption style="font-size: 24px;font-weight: bold;height: 50px;"><s:text name="量检具卡片"/></caption>
						<tr>
							<td style="width: 160px;"><span style="color:red">*</span><s:text name="量检具编号"/></td>
							<td><input style="background-color:#eeeeee;" type="text" id="measurementSerialNo" name="measurementSerialNo" value="${measurementSerialNo}" readonly="readonly" /></td>
							<td style="width: 160px;"><span style="color:red">*</span><s:text name="量检具名称"/></td>
							<td><input type="text" id="measurementName" name="measurementName" value="${measurementName}" class="{required:true,messages:{required:必填}}" /></td>
							<td style="width: 160px;"><span style="color:red">*</span><s:text name="型号/规格"/></td>
							<td><input type="text" id="measurementSpecification" name="measurementSpecification" value="${measurementSpecification}" class="{required:true,messages:{required:必填}}" /></td>
						</tr>
						<tr>
							<td><span style="color:red">*</span><s:text name="生产商"/></td>
							<td><input type="text" id="manufacturer" name="manufacturer" value="${manufacturer}" class="{required:true,messages:{required:必填}}"  /></td>
							<td><span style="color:red">*</span><s:text name="类别"/></td>
						 	<td><s:select theme="simple" list="topOptions" emptyOption="true" listKey="name" listValue="value" id="measurementType" name="measurementType" value="measurementType" onchange="onchangeGsmCodeSecRules();" cssClass="{required:true,messages:{required:必填}}"></s:select></td>
							<td><span style="color:red">*</span><s:text name="二级分类"/></td>
							<td><s:select theme="simple" list="botOptions" listKey="name" listValue="value" id="secondaryClassification" name="secondaryClassification" value="secondaryClassification" cssClass="{required:true,messages:{required:必填}}"></s:select></td>							
						</tr>
						<tr>
							<td ><span style="color:red">*</span><s:text name="采购日期"/></td>
							<td><input type="text" id="datepicker1" name="dateOfPurchase" value="<s:date name="dateOfPurchase" format="yyyy-MM-dd" />" class="{required:true,messages:{required:必填}}" readonly="readonly"/></td>
							<td><span style="color:red">*</span><s:text name="约定送货日期" /></td>
							<td><input type="text" id="datepicker2" name="agreedDeliveryDate" value="<s:date name="agreedDeliveryDate" format="yyyy-MM-dd" />" class="{required:true,messages:{required:必填}}" readonly="readonly"/></td> 
							<td><span style="color:red">*</span><s:text name="出厂编号"/></td>
							<td><input type="text" id="factoryNumber" name="factoryNumber" value="${factoryNumber}" class="{required:true,messages:{required:必填}}" /></td>
						</tr>
						<tr>
							<td><span style="color:red">*</span><s:text name="品牌"/></td>
							<td><input type="text" id="brand" name="brand" value="${brand}" class="{required:true,messages:{required:必填}}" /></td>
							<td><s:text name="确认收货日期"/></td>
							<td><input type="text" id="datepicker3" name="confirmDeliveryDate" value="<s:date  name="confirmDeliveryDate" format="yyyy-MM-dd" />"  disabled="disabled" readonly="readonly"/></td> 
							<td><s:text name="确认人"/></td>
							<td>
								<input type="text" id="confirmPeople" name="confirmPeople" value="${confirmPeople}" readonly="readonly" disabled="disabled"/>
								<input type="hidden" id="confirmPeopleLoginName" name="confirmPeopleLoginName" value="${confirmPeople}"/>
							</td>
						</tr>
						<tr>
							<td><s:text name="备注"/></td>
							<td colspan="5"><textarea rows="10" id="remark" name="remark">${remark}</textarea></td>
						</tr> 
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>