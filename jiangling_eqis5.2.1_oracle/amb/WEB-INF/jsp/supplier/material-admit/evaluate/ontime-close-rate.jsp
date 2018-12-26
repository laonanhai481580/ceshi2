<%@page import="com.ambition.product.base.WorkflowIdEntity"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
	<%@include file="/common/meta.jsp" %>
<%-- 	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script> --%>
<%-- 	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script> --%>
<%-- 	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script> --%>
<%-- 	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script> --%>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<c:set var="actionBaseCtx" value="${complainctx}/customer-complaint"/>
	<script type="text/javascript">
	var baseShowDetailUrl = '${actionBaseCtx}/ontime-close-detail.htm';
	function showDetailList(url){
		$.colorbox({href:encodeURI(url),iframe:true, 
			innerWidth:$(window).width()<600?$(window).width()-50:900, 
			innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
 			overlayClose:false,
 			title:"<s:text name='complain.message-7'/>"
 		});
	}
	//按时关闭
	function showOntimeClosedFormate(cellValue,option,rowObj){
		if(!cellValue || cellValue == '&nbsp;'){
			return "";
		}
		if(cellValue==0){
			return cellValue;
		}
		var url = baseShowDetailUrl + "?params.ontimeState=<%=WorkflowIdEntity.ONTIMESTATE_ONTIME_COMPLETE%>";
		url += "&params.name=" + rowObj.name;
		return "<a href='javascript:void(0);showDetailList(\""+url+"\")' style='color:black;'>"+cellValue+"</a>";
	}
	//超期关闭
	function showOvertimeClosedFormate(cellValue,option,rowObj){
		if(!cellValue || cellValue == '&nbsp;'){
			return "";
		}
		if(cellValue==0){
			return cellValue;
		}
		var url = baseShowDetailUrl + "?params.ontimeState=<%=WorkflowIdEntity.ONTIMESTATE_OVERDUE_COMPLETE%>";
		url += "&params.name=" + rowObj.name;
		return "<a href='javascript:void(0);showDetailList(\""+url+"\")' style='color:black;'>"+cellValue+"</a>";
	}
	//未关闭
	function showNotClosedFormate(cellValue,option,rowObj){
		if(!cellValue || cellValue == '&nbsp;'){
			return "";
		}
		if(cellValue==0){
			return cellValue;
		}
		var url = baseShowDetailUrl + "?params.name=" + rowObj.name;
		url += "&params.lastState=" + option.colModel.name;
		return "<a href='javascript:void(0);showDetailList(\""+url+"\")' style='color:black;'>"+cellValue+"</a>";
	}
	</script>
</head>

<body>
	<script type="text/javascript">
		var secMenu="complaint";
		var thirdMenu="complaintclose";
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
	
	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-material-admit-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post"  action=""></form>
			<div class="opt-btn">
				<button class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b><s:text name='common.search'/></span></span></button>
				<security:authorize ifAnyGranted="ONTIME_CLOSE_RATE">
				<button class='btn' onclick="iMatrix.export_Data('${actionBaseCtx}/export-ontime-close-rate.htm');"><span><span><b class="btn-icons btn-icons-export"></b><s:text name='common.export'/></span></span></button>
				</security:authorize>
			</div>
			<div id="message"><s:actionmessage theme="mytheme" /></div>	
			<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
			<div id="opt-content" >
				<form id="contentForm" name="contentForm" method="post"  action="">
					<grid:jqGrid gridId="list" url="${actionBaseCtx}/ontime-close-rate-datas.htm" submitForm="defaultForm" code="CUSTOMER_COMPLAINT_ONTIMECLOSERATE" pageName="ontimeClosePage"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
</html>