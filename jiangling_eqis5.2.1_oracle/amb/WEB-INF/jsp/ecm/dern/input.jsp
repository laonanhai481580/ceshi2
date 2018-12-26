<%@page import="com.ambition.ecm.entity.DcrnReportDetail"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@page import="com.ambition.iqc.entity.IncomingInspectionActionsReport"%>
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
	<c:set var="actionBaseCtx" value="${ecmctx}/dern" />
	<style type="text/css">
		.tableTd{
			text-align: center !important;;src
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
	<script type="text/javascript">
	$(document).ready(function(){
		initForm();
	 });
	//导出
	function exportForm(){
		var id = '${id}';
		var current = 0;
		var dd = setInterval(function(){
			current++;
			var str = '';
			for(var i=0;i<(current%3);i++){
				str += "...";
			}
		}, 500);
		$("#iframe").bind("readystatechange",function(){
			clearInterval(dd);
			$("#message").html("");
			$("#iframe").unbind("readystatechange");
			printCertification();
		}).attr("src","${actionBaseCtx}/download-report.htm?id="+id);
	}
	</script>
	
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="_dern";
		var thirdMenu="_dern_input";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/ecm-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/ecm-drcn-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<s:if test="taskId>0">
					<wf:workflowButtonGroup taskId="${taskId}"></wf:workflowButtonGroup>
				</s:if>
				<s:else>
					<button class='btn' onclick="javascript:window.location='${ecmctx}/dern/input.htm';" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					<security:authorize ifAnyGranted="ecm-dern-save,ecm-dern-submit-process">
						<button class='btn' type="button" onclick="submitForm('${ecmctx}/dern/save.htm');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
						<button class='btn' type="button" onclick="submitForm('${ecmctx}/dern/submit-process.htm');"><span><span><b class="btn-icons btn-icons-submit"></b>提交</span></span></button>
					</security:authorize>
				</s:else>
				<s:if test="taskId>0">
 					 <button class="btn" onclick="viewFormInfo()"><span><span><b class="btn-icons btn-icons-info"></b><s:text name='详情' /></span></span></button>
				</s:if>
				<c:if test="${id>0}">
					<security:authorize ifAnyGranted="ecm_dcrn-export-form">
						<button class='btn' id="print" type="button" onclick="exportForm();"><span><span><b class="btn-icons btn-icons-print"></b>导出</span></span></button>
					</security:authorize>
				</c:if>
			</div>
			<div><iframe id="iframe" style="display:none;"></iframe></div>
			<div id="opt-content" style="text-align: center;">
				<form action="" method="post" id="inputForm" name="inputForm">
					<input type="hidden" id="id" name="id" value="${id}"/>
					<jsp:include page="input-form.jsp" />
				</form>
			</div>
		</div>
	</div>
</body>
</html>