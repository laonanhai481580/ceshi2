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
	<jsp:include page="form-script.jsp" />
	<style type="text/css">
		.tableTd{
			text-align: center !important;;
		}
		#ulLi{
			margin:0px;
			padding:0px;
		}
		#ulLi li{
			margin:0px;
			padding:2px;
			text-decoration: none;
			list-style: none;
			float:left;
		}
	</style>
<!-- 表单和流程常用的方法封装 -->
<script type="text/javascript">
		var hasInitHistory = false;
		isUsingComonLayout=false;
		$(document).ready(function() {
			initForm();
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
							<div>
								<input type="hidden" name="id" id="id" value="${id }" />
								<input type="hidden" name="taskId" id="taskId" value="${taskId}" />
								<input type="hidden" name="assignee" id="assignee" />
								<input type="hidden" name="taskTransact" id="taskTransact" />
								<input type="hidden" name="launchState" id="launchState" value="${launchState}"/>
							</div>
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