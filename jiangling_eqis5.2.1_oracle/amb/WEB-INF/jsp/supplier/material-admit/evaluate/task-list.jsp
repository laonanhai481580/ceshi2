<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<c:set var="actionBaseCtx" value="${complainctx}/customer-complaint/claim"/>
	<script type="text/javascript">
		function formNoFormate(value,o,obj){
			return "<a title=\"<s:text name='complain.message-9'/>\"  href='javascript:_viewProcessInfo("+obj.formId+");'>"+value+"</a>";
		}
		function _viewProcessInfo(formId){
			$.colorbox({href:'${actionBaseCtx}/view-info.htm?id='+formId,iframe:true,
				innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"<s:text name='complain.message-10'/>"
			});
		}
		function duedateFormate(value,o,obj){
			if(value=='0'){
				return '无限制';
			}else{
				return value;
			}
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	 <script type="text/javascript">
		var secMenu="claim";
		var thirdMenu="claimflow";
 	</script>
 	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-material-admit-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post"  action=""></form>
			<div class="opt-btn">
				<button class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b><s:text name='common.search'/></span></span></button>
				<security:authorize ifAnyGranted="IMPROVE_QRQC_EXPORT_TASK_LIST">
				<button class='btn' onclick="iMatrix.export_Data('${actionBaseCtx}/export-task-datas.htm');"><span><span><b class="btn-icons btn-icons-export"></b><s:text name='common.export'/></span></span></button>
				</security:authorize>
			</div>
			<div id="message"><s:actionmessage theme="mytheme" /></div>	
			<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
			<div id="opt-content" >
				<form id="contentForm" name="contentForm" method="post"  action="">
					<grid:jqGrid gridId="list" url="${actionBaseCtx}/task-list-datas.htm" submitForm="defaultForm" code="TASK_TEMP_FOR_DISPLAY" pageName="taskPage"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
</html>