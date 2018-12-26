 <%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
	//送Msa
	function sendMsa(){	
		var id = jQuery("#dynamicInspectionMsaplan").getGridParam('selarrrow');
		$.post("${gsmctx}/equipment-msaplan/sendMsa.htm",{ids:id+""}, function(data){$("#dynamicInspectionMsaplan").trigger("reloadGrid")});
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="inspectionmsaplan";
		var thirdMenu="_myInspectionmsaplan";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>	
	
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>
		
	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-inspection-msaplan-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="gsm_equipment-msaplan_save">
						<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
						<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				  	<security:authorize ifAnyGranted="gsm_equipment-msaplan_sendMsa">
						<button class="btn" onclick="sendMsa()"><span><span><b class="btn-icons btn-icons-paste"></b><s:text name="送MSA"/></span></span></button> 
				 	</security:authorize>	
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="dynamicInspectionMsaplan" url="${gsmctx}/equipment-msaplan/list-datas.htm" code="MEASUREMENT_MSAINSPECTION_PLAN" pageName="page" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div> 
</body>
</html>