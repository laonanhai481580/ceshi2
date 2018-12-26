<%@page import="com.ambition.chartdesign.entity.ChartDatasource"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		initForm();
	});
	function initForm(){
		$(":input[name=databaseSettingType]").click(databaseSettingTypeChange);
		databaseSettingTypeChange();
	}
	function databaseSettingTypeChange(){
		var selObj = $(":input[name=databaseSettingType]:checked");
		var databaseSettingType = selObj.val();
		if(databaseSettingType == '<%=ChartDatasource.DATABASE_SETTING_TYPE_CUSTOM%>'){
			$("select[name=databaseName]").removeAttr("disabled");
		}else{
			$("select[name=databaseName]").attr("disabled","disabled");
		}
	}
	function submitForm(url){
		if($("#form").valid()){
			$('#form').attr('action',url);
			$("button").attr("disabled","disabled");
			$("#message").html("正在执行操作,请稍候... ...");
			$('#form').submit();
		}
	}
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
	}
	/**
	* 测试数据库连接
	*/
	function testConnection(){
		if($("#form").valid()){
			var params = {};
			$("#form :input[name]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("button").attr("disabled","disabled");
			$("#message").html("正在执行操作,请稍候... ...");
			$.post('${chartdesignctx}/base-info/database-setting/test-connection.htm',params,function(result){
				$("button").removeAttr("disabled");
				$("#message").html("");
				if(result.error){
					alert(result.message);
				}else{
					alert("恭喜你,连接成功!");
				}
			},'json');
		}
	}
</script>
</head>
<body>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" style="text-align:right;">
				<span id="message" style="color:red;position:absolute;left:4px;top:8px;">
					<s:actionmessage theme="mytheme" />
				</span>
				<button class='btn' type="button"
					onclick="submitForm('${chartdesignctx}/base-info/datasource/step2.htm')">
					<span><span><b class="btn-icons btn-icons-next"></b>下一步</span></span>
				</button>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
			</div>
			<div id="opt-content">
				<div id="info" style="background:white;padding:4px;margin:0px 0px 8px 0px;font-weight:bold;font-size:14px;line-height:30px;">
					1.选择数据库
				</div>
				<form id="form" name="form"
					method="post" action="">
					<input type="hidden" name="id" id="id" value="${id}" />
					<table style="width: 90%;">
						<tr>
							<td align=right style="width:120px;"><font color="red">*</font>数据源名称:</td>
							<td>
								<input name="name" value="${name}" style="width:80%;" class="{required:true,messages:{required:'必填!'}}"/>
							</td>
						</tr>
					</table>
					<fieldset>
						<legend>数据库</legend>
						<table style="width:100%;">
							<tr>
								<%
									String databaseSettingType = ActionContext.getContext().getValueStack().findString("databaseSettingType");
								%>
								<td style="padding-left:40px;">
									<input type="radio" name="databaseSettingType" id="local" 
										value="<%=ChartDatasource.DATABASE_SETTING_TYPE_LOCAL%>"
										<%=ChartDatasource.DATABASE_SETTING_TYPE_CUSTOM.equals(databaseSettingType)?"":"checked=checked"%>/>
									<label for="local">QIS数据库</label>
								</td>
							</tr>
							<tr>
								<td style="padding-left:40px;">
									<input type="radio" name="databaseSettingType" id="custom" 
										value="<%=ChartDatasource.DATABASE_SETTING_TYPE_CUSTOM%>"
										<%=ChartDatasource.DATABASE_SETTING_TYPE_CUSTOM.equals(databaseSettingType)?"checked=checked":""%>/>
									<label for="custom">自定义数据库</label>
									<s:select list="databaseSettings" 
										name="databaseName"
										listKey="showName"
										listValue="showName"
										theme="simple"
										cssStyle="margin-left:4px;"
										cssClass="{required:true,messages:{required:'必填!'}}"
										value="databaseName"></s:select>
								</td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</body>
</html>