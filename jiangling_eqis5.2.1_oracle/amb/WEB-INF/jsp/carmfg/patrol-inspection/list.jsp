<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
	function click(cellvalue, options, rowObject){	
		return "<a href='${mfgctx}/patrol-inspection/input.htm?workshop=${workshop}&inspectionPointId=${inspectionPointId}&id="+rowObject.id+"'>"+cellvalue+"</a>";
	}
	function formatRate(cellvalue, options, obj){
		if(cellvalue){
			var rate = cellvalue*100;
			if(obj.inspectionAmount != undefined && obj.qualifiedAmount != undefined){
				rate = obj.qualifiedAmount*100.0/obj.inspectionAmount;
			}
			if(rate.toFixed){
				rate = rate.toFixed(2);
			}
			var operations = "<div style='text-align:left;' class='rate'>"+rate+"%</div>";
			return operations;
		}else{
			return "<div style='text-align:left;' class='rate'></div>";
		}
	}
	function formateAttachmentFiles(value,o,obj){
		var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title='上传附件'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
		return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu='data_list';
		var thirdMenu="patrolList";
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
					<security:authorize ifAnyGranted="MFG_PATROL-INSPECTION_LIST_INPUT">
					<button class='btn' onclick="window.location='${mfgctx}/patrol-inspection/input.htm?workshop=${workshop}&inspectionPointId=${inspectionPointId}';" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="MFG_PATROL-INSPECTION_LIST_DELETE">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="MFG_PATROL-INSPECTION_LIST_EXPORT">
					<button  class='btn' onclick="iMatrix.export_Data('${mfgctx}/patrol-inspection/exports.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					</div>
					<div id="opt-content">
						<form id="contentForm" name="contentForm" method="post" action="">
							<grid:jqGrid gridId="dynamicInspection"  url="${mfgctx}/patrol-inspection/list-datas.htm" code="MFG_PATROL_INSPECTION_REPORT" pageName="page"></grid:jqGrid>
						</form>
					</div>
				</aa:zone>
			</div>
	</div>
	
</body>
</html>