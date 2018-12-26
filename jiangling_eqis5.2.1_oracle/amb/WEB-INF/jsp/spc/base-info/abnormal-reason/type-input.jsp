<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>层级类别</title>
<%@include file="/common/meta.jsp" %>
<script type="text/javascript">
	isUsingComonLayout=false;
	function reasonTypeFormValidate(){
		$("#reasonTypeForm").validate({
			submitHandler: function() {
				$(".opt_btn").find("button.btn").attr("disabled","disabled");
				aa.submit('reasonTypeForm','','main',showMsg);
			}
		});
	}
	function submitForm(url){
		$('#reasonTypeForm').attr('action',url);
		$("#message").html("<b>数据保存中,请稍候... ...</b>");
		$('#reasonTypeForm').submit();
	}
	function callback(){
		addFormValidate('${fieldPermission}','reasonTypeForm');
		reasonTypeFormValidate();
		showMsg();
	}
	//添加验证
	$(function(){
		reasonTypeFormValidate();
	});
	function cancel(){
		window.parent.$.colorbox.close();
	}
</script>
</head>
<body>
	<div class="opt-body">
		<aa:zone name="main">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="spc_base-info_abnormal-reason_type-save">
					<button class='btn' onclick="submitForm('${spcctx}/base-info/abnormal-reason/type-save.htm');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				</security:authorize>
				<button class='btn' onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
				<span id="message" style="color: red; padding-left: 4px; font-weight: bold;"><s:actionmessage theme="mytheme"/></span>
			</div>
			<div id="opt-content" class="form-bg">
				<form id="reasonTypeForm" name="reasonTypeForm" method="post">
					<input type="hidden" name="id" id="id" value="${id }"/>
					<input type="hidden" name="parentId" id="parentId" value="${parent.id }"/>
					<table cellpadding="0" cellspacing="0" width="100%" align="center">
						<tr>
							<td>父级结构:</td>
							<td>${parent.typeName }</td>
						</tr>
						<tr>
							<td style="width:90px"><span style="color:red;">*</span>类别简码:</td>
							<td><input id="typeNo" name="typeNo" value="${typeNo }" class="{required:true}"/></td>
							<td style="width:90px"><span style="color:red;">*</span>类别名称:</td>
							<td><input id="typeName" name="typeName" value="${typeName}" class="{required:true}"/> </td>
						</tr>
					</table>
				</form>
			</div>
		</aa:zone>
	</div>
</body>
</html>