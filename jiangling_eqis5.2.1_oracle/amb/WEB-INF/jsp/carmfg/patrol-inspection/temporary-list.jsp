<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
	});
	function click(cellvalue, options, rowObject){	
		return "<a href='${mfgctx}/patrol-inspection/input.htm?workshop=${workshop}&id="+rowObject.id+"'>"+cellvalue+"</a>";
	}
	function formateAttachmentFiles(value,o,obj){
		var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title='上传附件'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
		return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
	}
	//添加巡检告
	function addRow(){
		window.location.href = "${mfgctx}/patrol-inspection/input.htm?workshop=${workshop}";
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu='data_acquisition';
		var thirdMenu="patrolTemporary";
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
			<div class="opt-btn">
				<security:authorize ifAnyGranted="mfg-patrol-inspection-list-input">
				<button class="btn" onclick="addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="MFG_PATROL-INSPECTION_LIST_DELETE">
				<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<security:authorize ifAnyGranted="mfg-patrol-inspection-list-export">
				<button class="btn" onclick="iMatrix.export_Data('${mfgctx}/patrol-inspection/exports.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>
			</div> 
			<div id="opt-content" style="clear:both;">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="list"
									url="${mfgctx}/patrol-inspection/temporary-list-datas.htm" code="MFG_PATROL_INSPECTION_REPORT"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
</html>