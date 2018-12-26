<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>层级类别</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" ></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/dynamic.validate.js" ></script>
	
	<script type="text/javascript">
		isUsingComonLayout=false;
		function processFormValidate(){
			$("#layerTypeForm").validate({
				submitHandler: function() {
					$(".opt_btn").find("button.btn").attr("disabled","disabled");
					aa.submit('layerTypeForm','','main',showMsg);
				}
			});
		}
		
		function submitForm(url){
			$.showMessage("数据保存中,请稍候... ...");
			$('#layerTypeForm').attr('action',url);
			$('#layerTypeForm').submit();
			$.showMessage("保存成功！");
		}
		
		function callback(){
			addFormValidate('${fieldPermission}','layerTypeForm');
			processFormValidate();
			showMsg();
		}
		
		$(function(){
			//添加验证
			processFormValidate();
		});
	</script>
</head>
<body>
	<div class="opt-body">
		<aa:zone name="main">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="spc_base-info_layer-type_save">
					<button class='btn' onclick="submitForm('${spcctx}/base-info/layer-type/save.htm');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				</security:authorize>
				<button  class='btn' type="button" onclick="javascript:window.parent.$.colorbox.close();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
				<span id="message" style="color: red; padding-left: 4px; font-weight: bold;"><s:actionmessage theme="mytheme"/></span>
			</div>
			<div id="opt-content" class="form-bg">
				<form id="layerTypeForm" name="layerTypeForm" method="post">
					<input type="hidden" name="id" id="id" value="${id }"/>
					<input type="hidden" name="parentId" id="parentId" value="${parent.id }"/>
					<table cellpadding="0" cellspacing="0" width="100%" align="center">
						<tr>
							<td>父级结构:</td>
							<td>${parent.typeName }</td>
						</tr>
						<tr>
						<td style="width:90px"><span style="color:red;">*</span>编码:</td>
						<td><input id="typeCode" name="typeCode" value="${typeCode}" disabled="disabled"/> </td>
						<td style="width:90px"><span style="color:red;">*</span>名称:</td>
							<td><input id="typeName" name="typeName" value="${typeName}" class="{required:true}"/> </td>
						</tr>
						<tr>
							<td style="width:120px"><span style="color:red;"></span>必须录入取值:</td>
							<td><input type="checkbox" id="isInputValue" name="isInputValue" value="是" <s:if test="%{isInputValue==\"是\"}">checked="checked"</s:if>/></td>
							<td style="width:120px"><span style="color:red;"></span>自动复制:</td>
							<td><input type="checkbox" id="isAutoCopy" name="isAutoCopy" value="是" <s:if test="%{isAutoCopy==\"是\"}">checked="checked"</s:if>/> </td>
						</tr>
						<tr>
						<td style="width:90px">取值方式:</td>
							<td>
								<s:select list="sampleMethods"
									listKey="value" 
								  	listValue="name" 
						            labelSeparator=""
						  			cssStyle="width:160px;"
									theme="simple"
									name="sampleMethod">
								</s:select>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</aa:zone>
	</div>
</body>
</html>