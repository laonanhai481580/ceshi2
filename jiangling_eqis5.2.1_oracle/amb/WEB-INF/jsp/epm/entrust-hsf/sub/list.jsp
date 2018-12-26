<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<c:set var="actionBaseCtx" value="${epmctx}/entrust-hsf/sub" />
<script type="text/javascript">
	function createqualiy() {
		window.location = "${epmctx}/entrust-hsf/input.htm";
	}
	
	function click(cellvalue, options, rowObject) {
		return "<a href='${epmctx}/entrust-hsf/input.htm?sunId=" + rowObject.id + "'>"
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
		var strs = "";
		strs = "<div style='width:100%;text-align:center;' title='查看流转历史' ><a class=\"small-button-bg\"  onclick=\"_viewProcessInfo("+obj.id+");\" href=\"#\"><span class='ui-icon ui-icon-info' style='cursor:pointer;text-align:right;'></span></a><div>";
		return strs;
	}
	function _viewProcessInfo(formId){
		$.colorbox({href:'${epmctx}/entrust-hsf/view-info.htm?sunId='+formId,iframe:true,
			innerWidth:$(window).width()<1100?$(window).width()-50:1100, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"表单"
		});
	}
	
	
	function $oneditfunc(rowid) {
		params = {};
		myId = rowid;
		// 	 		更改回车事件为下一单元格
		enterKeyToNext("customerGrid", rowid, function() {

		});
	}
	 function viewChildTableInputForm(value,o,obj){
			var strs = "";
			strs = "<div style='width:100%;text-align:center;' title='查看子表详情' ><a class=\"small-button-bg\"  onclick=\"_viewChildTableInputForm("+obj.id+");\" href=\"#\"><span class='ui-icon ui-icon-info' style='cursor:pointer;text-align:right;'></span></a><div>";
			return strs;
		}

		function addExceptionSingle(){
	 		var id = $("#list").jqGrid("getGridParam","selarrrow");
	 		if(id.length>1){
	 			alert("只能选择一项！");
	 			return ;
	 		}else if(id.length==0){
	 			alert("请选择一项！");
	 			return ;
	 		}
			window.open('${epmctx}/exception-single/input.htm?formId='+id+"&&type=hsf");
		}
		function alterException(obj){
			var id = $("#list").jqGrid("getGridParam","selarrrow");
			console.log(id);
			if(id.length>1){
	 			alert("只能选择一项！");
	 			return ;
	 		}else if(id.length==0){
	 			alert("请选择一项！");
	 			return ;
	 		}
			$.post('${epmctx}/exception-single/alter-exception.htm?entrustId='+id+"&&type=hsf", null,
					   function(data){
					     window.location.reload(href='${epmctx}/entrust-ort/sub/list.htm');
					     alert("修改成功");
					   });
// 			window.location.href='${epmctx}/exception-single/alter-exception.htm?entrustId='+id+"&&type=hsf";
// 			window.location.reload(href='${epmctx}/entrust-ort/sub/list.htm');
		}
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu = "entrust";
		var thirdMenu = "entrustListSub";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>
	<div id="secNav">
		<%@ include file="/menus/epm-sec-menu.jsp"%>
	</div>
	<div class="ui-layout-west">
		<%@ include file="/menus/epm-entrust-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="epm_entrustHsf_save">
						<button class="btn" onclick="createqualiy();">
							<span><span><b class="btn-icons btn-icons-add"></b>新建</span></span>
						</button>
					</security:authorize>
					
					<button class='btn' onclick="iMatrix.showSearchDIV(this);"
						type="button">
						<span><span><b class="btn-icons btn-icons-search"></b>查询</span></span>
					</button>
					<security:authorize ifAnyGranted="epm_entrustHsf_export">
						<button class='btn' onclick="iMatrix.export_Data('${actionBaseCtx}/export.htm');"
							type="button">
							<span><span><b class="btn-icons btn-icons-export"></b>导出</span></span>
						</button>
					</security:authorize>
				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="">
						<grid:jqGrid gridId="list" url="${actionBaseCtx}/list-datas.htm"
							code="EPM_ENTRUST_HSF_SUBLIST" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>