<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<c:set var="actionBaseCtx" value="${improvectx}/exception" />
<script type="text/javascript">
		function formNoFormate(value,o,obj){
			return "<a title='<s:text name='查看详情'/>'  href='javascript:_viewProcessInfo("+obj.formId+");'>"+value+"</a>";
		}
		function _viewProcessInfo(formId){
			$.colorbox({href:'${actionBaseCtx}/view-info.htm?id='+formId,iframe:true,
				innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"品质异常联络单"
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

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="8d_report";
		var thirdMenu="exception_report_input";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/improve-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/improve-8d-report-third-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<div class="opt-btn">
				<button class='btn' onclick="iMatrix.showSearchDIV(this);">
					<span><span><b class="btn-icons btn-icons-search"></b>
						<s:text name='common.search' /></span></span>
				</button>
				<security:authorize ifAnyGranted="improve_impreport_task_list_exp">
					<button class='btn'
						onclick="iMatrix.export_Data('${actionBaseCtx}/export-task-datas.htm');">
						<span><span><b class="btn-icons btn-icons-export"></b>
							<s:text name='common.export' /></span></span>
					</button>
				</security:authorize>
			</div>
			<div id="message">
				<s:actionmessage theme="mytheme" />
			</div>
			<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="list"
						url="${actionBaseCtx}/task-list-datas.htm"
						submitForm="defaultForm" code="TASK_TEMP_FOR_DISPLAY"
						pageName="taskPage"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
</html>