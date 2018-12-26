<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/widgets/workflowEditor/swfobject.js"></script>
	<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<%-- 	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script> --%>
	<!-- 表单和流程常用的方法封装 -->
	<script type="text/javascript" src="${ctx}/js/workflow-form-0.9.js"></script>
	<c:set var="actionBaseCtx" value="${supplierctx}/material-admit/evaluate"/>
	<script type="text/javascript">
	isUsingComonLayout=false;
	var hasInitHistory = false;
	$(document).ready(function() {
		//初始化表单元素,格式如
		$initForm({
			webBaseUrl:'${ctx}',//后台执行的Action的前缀名,如:http://localhost:8080/amb/qrqc
			actionBaseUrl : '${actionBaseCtx}',//项目的前缀地址,如:http://localhost:8080/amb
			formId:'workflowForm',//表单ID
			objId:'${id}',//数据库对象的ID
			taskId:'${taskId}',//任务ID
			children:{
			},
			beforeSaveCallback:function(){
			},
// 			inputformortaskform:'taskform',//表单类型,taskform:流程办理界面,inputform:普通表单页面
// 			fieldPermissionStr:'${fieldPermission}'//字符串格式的字段权限
		});

		$( "#tabs" ).tabs({
			show: function(event, ui) {
				if(ui.index==1&&!hasInitHistory){
					changeViewSet("history","${id}");
					hasInitHistory = true;
				}
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

<body>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<button class="btn" onclick="closeBtn();"><span><span><b class="btn-icons btn-icons-cancel"></b><s:text name='取消'/></span></span></button>
			</div>
			<div id="opt-content" style="overflow-y:auto;">
				<div id="tabs" >
					<ul>
						<li><a href="#tabs-1">表单信息</a>
						</li>
						<s:if test="workflowInfo.firstTaskId>0">
						<li><a href="#tabs-2">流转历史</a>
						</li>
						</s:if>
					</ul>
					<div id="opt-content" class="form-bg">
						<div id="scroll" style="position:absolute;top:0px;left:15px;overflow-y:hidden;overflow-x:auto;height:35px;line-height:35px;display:block;z-index:2;">
							<div style="">&nbsp;</div>
						</div>
					<div id="tabs-1" style="background:#ECF7FB;overflow-x:hidden;overflow-y:auto;">
						<form  method="post" id="qrqcForm" name="qrqcForm" action="" >
							<jsp:include page="input-form.jsp" />
							<%@ include file="process-form.jsp"%>
						</form>
					</div>
					<div id="tabs-2">
					</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>