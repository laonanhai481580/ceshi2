<%@page import="com.ambition.chartdesign.entity.ChartDatabaseSetting"%>
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
		databaseTypeChange();
	});
	function databaseTypeChange(){
		var databaseType = $("#databaseType").val();
		if(databaseType == '<%=ChartDatabaseSetting.TYPE_ORACLE%>'){
			$("#databaseNameLabel").html("SID");
		}else{
			$("#databaseNameLabel").html("数据库名");
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
			<div class="opt-btn">
				<security:authorize ifAnyGranted="CHARTDESIGN-BASE-INFO-DATABASESETTING-SAVE">
				<button class='btn'
					onclick="submitForm('${chartdesignctx}/base-info/database-setting/save.htm')">
					<span><span><b class="btn-icons btn-icons-save"></b>保存</span></span>
				</button>
				</security:authorize>
				<button class='btn' onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
				<span id="message" style="color:red;">
					<s:actionmessage theme="mytheme" />
				</span>
			</div>
			<div id="opt-content">
				<form id="form" name="form"
					method="post" action="">
					<input type="hidden" name="id" id="id" value="${id}" />
					<table style="width: 90%;">
						<tr>
							<td align=right style="width:120px;"><font color="red">*</font>配置名称:</td>
							<td>
								<input name="showName" value="${showName}" class="{required:true,messages:{required:'必填!'}}"/>
							</td>
						</tr>
						<tr>
							<td align=right><font color="red">*</font>数据库类型:</td>
							<td>
								<s:select list="databaseTypes" 
										id="databaseType"
										onchange="databaseTypeChange()"
										name="type"
										listKey="value"
										listValue="name"
										theme="simple"
										cssStyle="width:150px;margin-left:2px;"
										cssClass="{required:true,messages:{required:'必填!'}}"
										value="type"></s:select>
							</td>
						</tr>
						<tr>
							<td align=right><font color="red">*</font>数据库IP:</td>
							<td>
								<input name="ip" value="${ip}" class="{required:true,messages:{required:'必填!'}}"/>
							</td>
						</tr>
						<tr>
							<td align=right><font color="red">*</font>数据库端口:</td>
							<td>
								<input name="port" value="${port}" class="{required:true,digits:true,messages:{required:'必填!',digits:'必须为整数!'}}"/>
							</td>
						</tr>
						<tr>
							<td align=right><font color="red">*</font><span id="databaseNameLabel">数据库名</span>:</td>
							<td>
								<input name="databaseName" value="${databaseName}" class="{required:true,messages:{required:'必填!'}}"/>
							</td>
						</tr>
						<tr>
							<td align=right><font color="red">*</font>用户名:</td>
							<td>
								<input name="userName" value="${userName}" class="{required:true,messages:{required:'必填!'}}"/>
							</td>
						</tr>
						<tr>
							<td align=right><font color="red">*</font>密码:</td>
							<td>
								<input type="password" name="password" value="${password}" class="{required:true,messages:{required:'必填!'}}"/>
							</td>
						</tr>
						<tr height=40>
							<td align=right></td>
							<td valign="middle">
								<button class='btn'
									onclick="testConnection()">
									<span><span><b class="btn-icons btn-icons-test"></b>测试数据库连接</span></span>
								</button>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>