<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>江淮汽车QIS系统</title>
	<%@include file="/common/meta.jsp" %>
		
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript">
	function paramTypeClick(obj){
		$.colorbox({href:"${spcctx}/common/feature-bom-multi-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
			overlayClose:false,
			title:"选择质量特性"
		});
	}
	//设置质量特性
	function setFeatureValue(objs){
		var obj = objs[0];
		$(":input[name='paramType']").val(obj.value);
			 
				$("#jqgrids").jqGrid("setRowData",rowId,{
			 
				});
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="data_acq";
		var thirdMenu="spc-other-datas";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/spc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/spc-data-acquisition-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="save-spc-other-datas">
						<button class='btn' onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="delete-spc-other-datas">
						<button class='btn' onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="export-spc-other-datas">
						<button class="btn" onclick="iMatrix.export_Data('${spcctx}/data-acquisition/export-spc-other-datas.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="">
						<grid:jqGrid gridId="list" url="${spcctx}/data-acquisition/spc-other-datas-list-datas.htm" code="SPC_OTHER_DATAS"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>