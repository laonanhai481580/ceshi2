<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>企业管理效率促进专家</title>
	<%@ include file="/common/meta.jsp" %>
<%-- 	<%@ include file="/common/common-js.jsp" %> --%>
	<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
	<!-- 表单和流程常用的方法封装 -->
	<script type="text/javascript" src="${ctx}/js/workflow-form-0.9.js"></script>
<!-- 	流程驳回 -->
	<script type="text/javascript" src="${ctx}/js/lcbh.js"></script>
	<c:set var="actionBaseCtx" value="${supplierctx}/audit/year"/>
	<script type="text/javascript">
		function update(){
			var email = $("#email").val();
			var url = "${mfgctx}/common/save-user-email.htm?email="+email;
			$("#message").html("正在保存,请稍候... ...");
			$.post(url,{},function(result){
				$("#btnDiv").find("button.btn").attr("disabled","disabled");
				if(result.error&&result.error!=""){
					alert(result.message);
				}else{
					$("#message").html(result.message);
				}
				setTimeout(function() {
					$("#message").html('');
				}, 1000);
			},"json");
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
			<div class="opt-btn" >
				<button class='btn' onclick="update();"><span><span><b class="btn-icons btn-icons-edit"></b>修改</span></span></button>		
			     <span style="color:red;" id="message1"></span>
			   
			</div>
			<div><iframe id="iframe" style="display:none;"></iframe></div>
			<div id="opt-content" class="form-bg">
				<div style="color:red;" id="message"><s:actionmessage theme="mytheme"/></div>
				<s:form  id="form" name="form" method="post" action="">
					<table class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
					   <tr>
					        <td>工号</td><td><input  name="userLogin" id="userLogin" value="${user.loginName}" disabled="disabled" /></td>
					   </tr>
					  <tr>
					        <td>新邮箱</td><td><input style="width:250px;" name="email" id="email" value="${user.email}" /></td>
					  </tr>
					   
					</table>
				</s:form>
			</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>