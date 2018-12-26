<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<jsp:include page="input-script.jsp" />
	<c:set var="actionBaseCtx" value="${sictx}/check-inspection" />
	<script type="text/javascript">
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu='report';
		var thirdMenu="_si_input";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/si-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/si-check-inspection-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
			<div class="opt-body">
					<div class="opt-btn">
						<security:authorize ifAnyGranted="si-report-input">
							<button class='btn' onclick="javascript:window.location='${sictx}/check-inspection/input.htm';" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
						</security:authorize>
						<c:if test="${canEdit==true}">
						<security:authorize ifAnyGranted="si-check-inspection-save">
							<button class='btn' type="button" onclick="saveForm();"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
						</security:authorize>
						</c:if>
						<security:authorize ifAnyGranted="si-report-list">
							<button class='btn' type="button" onclick="javascript:window.location='${sictx}/check-inspection/list.htm';"><span><span><b class="btn-icons btn-icons-undo"></b>返回</span></span></button>	
						</security:authorize>
							<button class='btn' onclick="exportForm();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
						<span style="margin-left:6px;line-height:30px;" id="message"><s:actionmessage theme="mytheme" cssStyle="color:red;" /></span>				
					</div>
					<div><iframe id="iframe" style="display:none;"></iframe></div>
					<div id="opt-content" style="text-align: center;">
						<form id="inputform" name="inputform" method="post" action="">
							 <%@ include file="input-form.jsp"%>
						</form>		
					</div>
				</div>
			</div>
</body>
</html>