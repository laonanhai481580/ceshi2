<%@page import="com.ambition.iqc.entity.OrtExperimentalItem"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript"
	src="${ctx}/widgets/workflowEditor/swfobject.js"></script>
<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"
	type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js"
	type="text/javascript"></script>
<!-- 表单和流程常用的方法封装 -->
<script type="text/javascript" src="${ctx}/js/workflow-form-0.9.js"></script>
<c:set var="actionBaseCtx" value="${iqcctx}/experimental-delegation"/>
<style type="text/css">
		.tableTd{
			text-align: center !important;;
		}
		ul{
			margin:0px;
			padding:0px;
		}
		li{
			margin:0px;
			padding:0px;
			text-decoration: none;
			list-style: none;
		}
	</style>
<script type="text/javascript">
		var hasInitHistory = false;
		isUsingComonLayout=false;
		$(document).ready(function() {
			//初始化表单元素,格式如
			$initForm({
			webBaseUrl:'${ctx}',//后台执行的Action的前缀名,如:http://localhost:8080/amb/qrqc
			actionBaseUrl : '${actionBaseCtx}',//项目的前缀地址,如:http://localhost:8080/amb
			formId:'inputForm',//表单ID
			objId:'${id}',//数据库对象的ID
			taskId:'${taskId}',//任务ID
			inputformortaskform:'inputform',//表单类型,taskform:流程办理界面,inputform:普通表单页面
			fieldPermissionStr:'${fieldPermission}',//字符串格式的字段权限
			children:{
				ortItems:{
					entityClass:"<%=OrtExperimentalItem.class.getName()%>"//子表1实体全路径
				}
			},
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
				<button class="btn" onclick="closeBtn();">
					<span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span>
				</button>
			</div>
			<div id="opt-content" style="overflow-y: auto;">
				<div id="tabs">
					<ul>
						<li><a href="#tabs-1">表单详情</a></li>
						<s:if test="workflowInfo.firstTaskId>0">
							<li><a href="#tabs-2">流转历史</a></li>
						</s:if>
					</ul>
					<div id="tabs-1"
						style="background: #ECF7FB; overflow-x: hidden; overflow-y: auto;">
						<form method="post" id="inputForm" name="inputForm" action="">
							<jsp:include page="input-form.jsp" />
							<%@ include file="process-form.jsp"%>
						</form>
					</div>
					<div id="tabs-2"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>