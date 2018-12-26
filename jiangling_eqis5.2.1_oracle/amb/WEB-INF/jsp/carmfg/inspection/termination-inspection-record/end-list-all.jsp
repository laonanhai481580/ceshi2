<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
	function click(cellvalue, options, rowObject){
		if(cellvalue){
			return "<a href='javascript:void(0);' onclick='callList("+rowObject.id+")'> "+cellvalue+"</a>";
		}else{
			return "";
		}			
	}
	function callList(id){
		myId = id;
		$.colorbox({href:'${mfgctx}/inspection/made-inspection/view-info.htm?id='+id,iframe:true,
			innerWidth:$(window).width()-100, 
			innerHeight:$(window).height()<800?$(window).height()-50:800,
			overlayClose:false,
			title:"报告详情"
		});
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu='data_list';
		var thirdMenu="storageInspectionRecord";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-inspection-list-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
			<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn" id="btnDiv">
					<security:authorize ifAnyGranted="termination-inspection-end-record-input">
					<button class='btn' onclick="window.location='${mfgctx}/inspection/made-inspection/input.htm'" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="termination-inspection-end-record-delete">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="MFG_INSPECTION_AUDIT">
					<button class='btn' onclick="reCheckList();" type="button"><span><span><b class="btn-icons btn-icons-cancel"></b>重新检验</span></span></button>
					</security:authorize>
					<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="termination-inspection-end-record-export">
					<button  class='btn' onclick="iMatrix.export_Data('${mfgctx}/inspection/termination-inspection-record/export-end-list-all.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					</div>
					<div id="opt-content">
						<form id="contentForm" name="contentForm" method="post" action="">
							<grid:jqGrid gridId="dynamicInspection"  url="${mfgctx}/inspection/termination-inspection-record/end-list-all-datas.htm" code="MFG_FIRST_INSPECTION_ALL" pageName="page"></grid:jqGrid>
						</form>
					</div>
				</aa:zone>
			</div>
	</div>
	
</body>
</html>