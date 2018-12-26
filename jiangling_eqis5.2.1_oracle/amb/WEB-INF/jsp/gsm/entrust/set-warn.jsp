<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
	</script>
</head>

<body>
	
	<script type="text/javascript">
 		var secMenu="gsm"; 
		var thirdMenu="gsmLists"; 
 	</script> 
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>	
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>	
	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-entrust-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
			<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn">
						<button class='btn' onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b><s:text name="common.add"/></span></span></button>
						<button class='btn' onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b><s:text name="common.delete"/></span></span></button>
						<span id="message" style="color:red"><s:actionmessage theme="mytheme" /></span>	
					</div>
					
					<div id="opt-content">
					<form id="sForm" name="sForm" method="post"  action=""></form>
					<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
						<form id="contentForm"  method="post"  action="">
							<grid:jqGrid gridId="timewarn" url="${gsmctx}/entrust/time-warn-list-datas.htm" code="GSM_ENTRUST_TIMEWARN" submitForm="sForm" pageName="page" ></grid:jqGrid>
						</form>
					</div>
				</aa:zone>
			</div>
	</div>
</body>
</html>



