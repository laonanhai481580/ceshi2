<%@page import="com.ambition.gsm.entity.BorrowRecordSublist"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/widgets/workflowEditor/swfobject.js"></script>
	<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
	<script type="text/javascript" src="${ctx}/js/workflow-form-0.9.js"></script>
	<c:set var="actionBaseCtx" value="${improvectx}/project-put"/>
	<script type="text/javascript">
		var hasInitHistory = false;
		isUsingComonLayout=false;
		$(document).ready(function() {
			//初始化表单元素,格式如
			$initForm({
				actionBaseUrl : '${actionBaseCtx}',//后台执行的Action的前缀名,如:http://localhost:8080/amb/application
				webBaseUrl:'${ctx}',//项目的前缀地址,如:http://localhost:8080/amb
				objId:'${id}',//数据库对象的ID
				taskId:'${taskId}',//任务ID
				formId:'listForm',//表单ID
				children:{
					borrowRecordSublists:{
						entityClass:"<%=BorrowRecordSublist.class.getName()%>"//
					}
				},
				isView:true,//仅显示
				fieldPermissionStr:'${fieldPermission}',//字符串格式的字段权限
				hideDisalbedInput:true,//默认隐藏禁用的文本框,把值显示的标签中
			});
			$( "#tabs" ).tabs({
				show: function(event, ui) {
				}
			});
			setTimeout(function(){
				$("#opt-content").height($(window).height()-70);
			},500);
		});
		function closeBtn(){
			window.parent.$.colorbox.close();
		}

	</script>
</head>

<body >
	<div class="ui-layout-center">
		<div class="opt-body">
<!-- 			<div class="opt-btn"> -->
<!-- 				<button class="btn" onclick="closeBtn();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button> -->
<!-- 			</div> -->
			<div id="opt-content" style="overflow-y:auto;">
				<div id="tabs" >
					<ul>
						<li><a href="#tabs-1">设备信息</a></li>
					</ul>
					<form  method="post" id="listForm" name="listForm" action="" >
						<jsp:include page="child-table-input-form.jsp" />
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>