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
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
	function click(cellvalue, options, rowObject){	
		return "<a href='${sictx}/check-inspection/input.htm?id="+rowObject.id+"'>"+cellvalue+"</a>";
	}
	function $successfunc(response){
		var result = eval("(" + response.responseText + ")");
		if(result.error){
			alert(result.message);
			return false;
		}else{
			return true;
		}
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu='report';
		var thirdMenu="_si_list";
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
				<aa:zone name="main">
					<div class="opt-btn" id="btnDiv">
					<security:authorize ifAnyGranted="si-check-inspection-save">
					<button class='btn' onclick="window.location='${sictx}/check-inspection/input.htm';" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="si-check-inspection-delete">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>					
					<security:authorize ifAnyGranted="si-check-inspection-export">
					<button  class='btn' onclick="iMatrix.export_Data('${sictx}/check-inspection/export.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					</div>
					<div style="float:right;position:absolute; right:50px;top:6px;" >
						<span style="color:red;" id="message"></span>
					</div>	
					<div id="message"><s:actionmessage theme="mytheme" /></div>	
					<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
					<div id="opt-content">
						<form id="contentForm" name="contentForm" method="post" action="">
							<grid:jqGrid gridId="dynamicInspection"  url="${sictx}/check-inspection/list-datas.htm" code="SI_CHECK_INSPECTION_REPORT" pageName="page"></grid:jqGrid>
						</form>
					</div>
				</aa:zone>
			</div>
	</div>
	
</body>
</html>