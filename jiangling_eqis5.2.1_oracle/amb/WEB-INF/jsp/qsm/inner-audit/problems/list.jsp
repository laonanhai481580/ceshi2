<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<c:set var="actionBaseCtx" value="${qsmctx}/inner-audit/problems" />
<script type="text/javascript">
	function createqualiy() {
		window.location = "${actionBaseCtx}/input.htm";
	}
	
	function click(cellvalue, options, rowObject) {
		return "<a href='${actionBaseCtx}/input.htm?id=" + rowObject.id + "'>"
				+ cellvalue + "</a>";
	}
	//节点
	function stageFormatter(value,options, rowObject){
		var launchState = rowObject.launchState; 
		var changeWorkFlowColor = rowObject.changeWorkFlowColor;
		if(changeWorkFlowColor=='red'){
			return "<div style='text-align:center;margin-left:-10px;'><img src='"+webRoot+"/images/red.gif'/></div>";
		}else{
			if(launchState){
				var colName = options.colModel.name;
				if(launchState.indexOf(colName)>-1){
					var reg = new RegExp(colName + "$");
					if(reg.test(launchState)){
						return "<div style='text-align:center;margin-left:-10px;color:green;'>办理中...</div>";
					}else{
						return "<div style='text-align:center;margin-left:-10px;'><img src='"+webRoot+"/images/green.gif'/></div>";					
					}
				}else{
					return '';
				}
			}else{
				return "";
			}
		}
	}
	//流转历史
	function viewProcessInfo(value,o,obj){
		console.log(value,o,obj);
		var strs = "";
		strs = "<div style='width:100%;text-align:center;' title='查看流转历史' ><a class=\"small-button-bg\"  onclick=\"_viewProcessInfo("+obj.id+");\" href=\"#\"><span class='ui-icon ui-icon-info' style='cursor:pointer;text-align:right;'></span></a><div>";
		return strs;
	}
	function _viewProcessInfo(formId){
		$.colorbox({href:'${actionBaseCtx}/view-info.htm?'+formId,iframe:true,
			innerWidth:$(window).width()<1100?$(window).width()-50:1100, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"表单"
		});
	}
	/*
	//自定义保存方法
	function customSave(gridId) {
			console.log(gridId);
		if (lastsel == undefined || lastsel == null) {
			alert("当前没有可编辑的行!");
			return;
		}
		var $grid = $("#" + gridId);
		var o = getGridSaveParams(gridId);
		if ($.isFunction(gridBeforeSaveFunc)) {
			gridBeforeSaveFunc.call($grid);
		}
		$grid.jqGrid("saveRow", lastsel, o);
	}
	 */
	function $oneditfunc(rowid) {
		params = {};
		myId = rowid;
		// 	 		更改回车事件为下一单元格
		enterKeyToNext("customerGrid", rowid, function() {

		});
	}
	//导入台账数据
		function importDatas(){
			var url = encodeURI('${actionBaseCtx}/import.htm');
			$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
				overlayClose:false,
				title:"导入台账数据",
				onClosed:function(){
					$("#list").trigger("reloadGrid");
				}
			});
		}
		function downloadTemplate(){
			window.location = '${actionBaseCtx}/download-template.htm';
		}	
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu = "inner_audit";
		var thirdMenu = "_problems_list";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/qsm-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/qsm-inner-audit-third-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="QSM_INNERAUDIT_PROBLEMS_SAVE">
						<button class="btn" onclick="createqualiy();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="QSM_INNERAUDIT_PROBLEMS_DELETE">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button id="searchBtn" class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="QSM_INNERAUDIT_PROBLEMS_EXPORT">
						<button class="btn" onclick="iMatrix.export_Data('${qsmctx}/inner-audit/problems/export-excel-report.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="QSM_INNERAUDIT_PROBLEMS_IMPORT">
						<button class='btn' onclick="importDatas();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>
						<button class="btn" onclick="downloadTemplate();"><span><span><b class="btn-icons btn-icons-download"></b>下载导入模板</span></span></button>
					</security:authorize>	
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="">
						<grid:jqGrid gridId="list" url="${qsmctx}/inner-audit/problems/list-datas.htm"
							code="QSM_INNERAUDIT_PROBLEMS" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>