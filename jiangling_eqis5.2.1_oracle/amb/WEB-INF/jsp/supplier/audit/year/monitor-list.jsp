<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/common.js"></script>
	<c:set var="actionBaseCtx" value="${supplierctx}/audit/year"/>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
	function createComplain(){
		window.location="${actionBaseCtx}/input.htm";	
	}
	function click(cellvalue, options, rowObject){	
		return "<a href='${actionBaseCtx}/input.htm?id="+rowObject.id+"'>"+cellvalue+"</a>";
	}
	
	function send8d(){
		var ids = [];
		ids = $("#dynamicComplain").jqGrid("getGridParam","selarrrow");
		if(ids.length==0){
			alert("<s:text name='complain.message-3'/>");
			return;
		}else if(ids.length>1){
			alert("<s:text name='complain.message-33'/>");
			return;
		}
		if(confirm("<s:text name='complain.message-32'/>")){
			$.colorbox.close();
			$("#message").html("<s:text name='complain.message-31'/>");
			$.post("${impctx}/impreport/check-comId.htm?complainId="+ids[0],null,function(result){
				if(result.error){
					alert(result.message);				
				}else{
					window.location = "${impctx}/impreport/input.htm?complainId="+ids[0];
				}
			},'json');
		}
	}
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
	

	function viewProcessInfo(value,o,obj){
		var strs = "";
		strs = "<div style='width:100%;text-align:center;' title='查看流转历史' ><a class=\"small-button-bg\"  onclick=\"_viewProcessInfo("+obj.id+");\" href=\"#\"><span class='ui-icon ui-icon-info' style='cursor:pointer;text-align:right;'></span></a><div>";
		return strs;
	}
	function _viewProcessInfo(formId){
		$.colorbox({href:'${actionBaseCtx}/view-info.htm?id='+formId,iframe:true,
			innerWidth:$(window).width()<1100?$(window).width()-50:1100, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"表单"
		});
	}
	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="evaluate";
		var thirdMenu="evaluateMonitorList";
	</script>


	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-evaluate-menu.jsp" %>
	</div>
	
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<span style="color:red;" id="message"></span>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post" action="">
						<grid:jqGrid gridId="dynamicDefectiveGood" url="${actionBaseCtx}/monitor-list-datas.htm" code="SUPPLIER_CHANGE_MONITOR" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
</html>