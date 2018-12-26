<%@page import="com.norteksoft.wf.engine.entity.InstanceHistory"%>
<%@page import="com.ambition.supplier.utils.DateUtil"%>
<%@page import="com.norteksoft.product.api.entity.Opinion"%>
<%@page import="com.norteksoft.product.util.PropUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/widgets/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/swfupload/handlers.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/workflowEditor/swfobject.js"></script>
	<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/dynamic.validate.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<jsp:include page="form-script.jsp" />
	<script type="text/javascript">
		isUsingComonLayout = false;
		var hasInitHistory = false;
		$(document).ready(function() {
			$( "#tabs" ).tabs({
				show: function(event, ui) {
					if(ui.index==1&&!hasInitHistory){
						changeViewSet("history");
						hasInitHistory = true;
					}
				}
			});
			//意见
			initForm();
			setTimeout(function(){
				$("#opt-content").height($(window).height()-70);
			},500);
		});
	</script>
</head>

<body
	onclick="$('#sysTableDiv').hide();
        $('#styleList').hide();">
	<div class="opt-body">
		<aa:zone name="main">
			<aa:zone name="btnZone">
				<div class="opt-btn">
					<wf:workflowButtonGroup taskId="${taskId}"></wf:workflowButtonGroup>
					<span id="message" style="color:red;"><s:actionmessage theme="mytheme" /></span>
				</div>
			</aa:zone>
			<div id="opt-content" class="form-bg">
				<div id="tabs" >
					<ul>
						<li><a href="#tabs-1">表单信息</a>
						</li>
						<li><a href="#tabs-2">流转历史</a>
						</li>
					</ul>
					<div id="tabs-1" style="background:#ECF7FB;">
						<form  method="post" id="inputForm" action="" name="inputForm">
							<input type="hidden" name="id" id="id" value="${id}" />
							<input name="taskId" id="taskId" value="${taskId}" type="hidden" />
							<input type="hidden" id="jointlySignStrs" name="jointlySignStrs" value="${jointlySignStrs}"/>
							<jsp:include page="input-form.jsp" />
							<jsp:include page="process-form.jsp" />
						</form>
					</div>
					<div id="tabs-2">
					</div>
				</div>
			</div>
		</aa:zone>
	</div>
</body>
</html>