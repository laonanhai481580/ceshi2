<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="com.ambition.carmfg.bom.web.BomAction"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>数据来源维护</title>
		<%@include file="/common/meta.jsp" %>	
		<script src="${ctx}/widgets/validation/jquery.validate.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="${ctx}/widgets/validation/cmxform.css"/>
	<script src="${ctx}/widgets/validation/jquery.metadata.js" type="text/javascript"></script>
	<script src="${ctx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
		<script type="text/javascript">
			isUsingComonLayout = false;
			$(function(){
				$("#dataSourceForm").validate({
				});
				$("#opt-content").height($(window).height()-60);
			});
			function save(){
				if($("#dataSourceForm").valid()){
					var params = getParams();
					$(".opt_btn").find("button.btn").attr("disabled","disabled");
					$("#message").show().html("正在保存,请稍候... ...");
					$.post("${supplierctx}/datasource/save.htm",params,function(result){
						if(result.error){
							$("#message").html("<font color=red>保存失败!</font>");
							alert(result.message);
						}else{
							$("#message").html("保存成功!");
							$("#id").val(result.id);
							$(".code","#dataSourceForm").html(result.code +
									"<input type='hidden' name='code' id='code' value='"+result.code+"'/> ");
						}
						showMsg();
						$(".opt_btn").find("button.btn").attr("disabled","false");
					},'json');
				}
			}
			//获取参数
			function getParams(){
				var params = {};
				$(":input","#dataSourceForm").each(function(index,obj){
					var jObj = $(obj);
					if(obj.name){
						params[obj.name] = jObj.val();
					}
				});
				return params;
			}
		</script>
	</head>
	
	<body>
		<div class="opt-body">
			<div class="opt-btn">
				<button class='btn' type="button" onclick="save()"><span><span>保存</span></span></button>
				<span style="padding-left:4px;padding-top:4px;" id="message"></span>
			</div>
			<div id="opt-content">
				<form  id="dataSourceForm" name="dataSourceForm" method="post" action="">
					<input type="hidden" name="id" id="id" value="${id }"/>
					<fieldset>
						<legend>基本信息</legend>
						<table style="width:100%;">
						<tr>
							<td align=right style="width:100px;"><font color=red>*</font>编码:</td>
							<td class='code'>
								<s:if test="id==null">
									<input id="code" name="code" value="${code}" class="{required:true,messages:{required:'编码不能为空!'}}"/> 
								</s:if>
								<s:else>
									${code}
									<input type="hidden" name="code" id="code" value="${code }"/> 
								</s:else>
							</td>
							<td align=right><font color=red>*</font>名称:</td>
							<td>
								<input id="name" name="name" value="${name }" style="width:100%;"  class="{required:true,messages:{required:'请输入规则名称!'}}"/>
							</td>
						</tr>
					</table>
					</fieldset>
					<fieldset style="margin-top:10px;">
						<legend><font color=red>*</font>JAVA执行代码&nbsp;<!-- <button type="button" onclick="javascript:alert('help')">帮助</button> --></legend>
						<table style="width:100%;">
							<tr>
								<td>
									<textarea rows="18" id="executeCode" name="executeCode" class="{required:true,messages:{required:'请输入执行代码!'}}">${executeCode}</textarea> 
								</td>
							</tr>
						</table>
					</fieldset>
					<fieldset style="margin-top:10px;">
						<legend>描述</legend>
						<table style="width:100%;">
							<tr>
								<td>
									<textarea rows="5" id="description" name="description">${description}</textarea> 
								</td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>
			</div>
	</body>
</html>