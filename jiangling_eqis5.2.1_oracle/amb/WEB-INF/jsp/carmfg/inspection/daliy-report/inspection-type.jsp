<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
	$(document).ready(function(){
		var url;
		var recordModel = ${recordModel};
		if(recordModel=='0'){
			url="${mfgctx}/inspection/inprocess-inspection/list.htm?workshop=${workshop}";
		}else if(recordModel=='1'){
			url="${mfgctx}/inspection/daliy-report/list.htm?workshop=${workshop}";
		}else if(recordModel=='2'){
			url="${mfgctx}/check-inspection/list.htm?workshop=${workshop}&inspectionPointId=${inspectionPointId}";
		}
		window.location = encodeURI(url);
		
	});
	</script>
	
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<input type="hidden" id="workshop"  value="${workshop}"/>
	
	
	<c:set var="accordionMenu" value="input"/>
	<script type="text/javascript">
		var secMenu=$("#workshop").val();
		var thirdMenu="";
		var accordionMenu="input";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-inprocess-inspection-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
			<div class="opt-body">
			
			
			
					<div id="opt-content">
					<form id="contentForm"  name="contentForm"  method="post"   action="">
							
						
					</form>
							
							
					
					</div>
			</div>
	</div>
</body>
</html>