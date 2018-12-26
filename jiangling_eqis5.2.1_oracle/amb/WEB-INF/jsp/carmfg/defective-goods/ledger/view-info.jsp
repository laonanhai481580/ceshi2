<%@page import="com.norteksoft.product.util.PropUtils"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/widgets/workflowEditor/swfobject.js"></script>
	<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<jsp:include page="form-script.jsp" />
	<script type="text/javascript">
		var hasInitHistory = false;
		$(document).ready(function() {
			$( "#tabs" ).tabs({
				show: function(event, ui) {
					if(ui.index==1&&!hasInitHistory){
						hasInitHistory = true;
						$("#tabs-2").load("${mfgctx}/defective-goods/ledger/history.htm?taskId=${workflowInfo.firstTaskId}",function(){
							$("#tabs-2").height($(window).height()-115);
						});
					}
				}
			});
			$("#tabs-1").height($(window).height()-115);
			initForm();
		});
		isUsingComonLayout=false;
		function closeBtn(){
			window.parent.$.colorbox.close();
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
		<div class="opt-body">
			<div class="opt-btn">
				<button class="btn" onclick="closeBtn();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
			</div>
			<div id="opt-content" style="text-align: center;overflow-y:auto;">
				<div id="tabs" >
					<ul>
						<li><a href="#tabs-1">表单信息</a>
						</li>
						<s:if test="workflowInfo.firstTaskId>0">
						<li><a href="#tabs-2">流转历史</a>
						</li>
						</s:if>
					</ul>
					<div id="tabs-1" style="background:#ECF7FB;overflow-y:auto;">
						<form  method="post" id="defectiveGoodsForm" action="" >
							<div>
								<input type="hidden" name="id" id="id" value="${id }" />
								<input type="hidden" name="taskId" id="taskId" value="${taskId }" />
								<input type="hidden" name="assignee" id="assignee" />
								<input type="hidden" name="taskTransact" id="taskTransact" />
								<input type="hidden" name="launchState" id="launchState" value="${launchState}"/>
							</div>
							<jsp:include page="form.jsp" />
						</form>
					</div>
					<div id="tabs-2">
					</div>
				</div>
			</div>
		</div>
</body>
</html>