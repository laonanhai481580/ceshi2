<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
	function info(cellValue,options,rowObj){
		return "<div style='text-align:center;'><button title='详细信息' type='button' class='small-button-bg' onclick='showReportInfo("+rowObj.id+")'><span class='ui-icon ui-icon-info' style='cursor:pointer;'></span></button></div>";
	}
	function showReportInfo(id){
		$.colorbox({href:'${mfgctx}/inspection/daliy-report/info-list.htm?reportId='+id,iframe:true, innerWidth:800, innerHeight:600,overlayClose:false,title:"生产日报表详细信息"});
	}
	function $addGridOption(jqGridOption){
		jqGridOption.postData.workshop=$("#workshop").val();
		
	}
	function showInfo(cellvalue, options, rowObject){
		return "<a href='#' onclick='showReportInfo("+rowObject.id+")'>"+cellvalue+"</a>";
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<input type="hidden" id="workshop"  value="${workshop}"/>
	<c:set var="accordionMenu" value="input"/>
	<script type="text/javascript">
		var secMenu=$("#workshop").val();
		var thirdMenu="daliyPoduceReportList";
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
			<div class="opt-body" >
			
				<aa:zone name="main">
					<div class="opt-btn">
					<security:authorize ifAnyGranted="inspection-daliy-report-delete">
							<button class='btn' onclick="delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
							</security:authorize>
					<button  class='btn' onclick="showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					</div>
					<div style="display: none;" id="message"><s:actionmessage theme="mytheme" /></div>
					<div id="opt-content">
						
						<form id="contentForm" name="contentForm" method="post"  action="">
							<grid:jqGrid gridId="daliyReportList" url="${mfgctx}/inspection/daliy-report-list/list-datas.htm" code="MFG_DALIY_REPORT"></grid:jqGrid>
						</form>
					</div>
				</aa:zone>
			</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>