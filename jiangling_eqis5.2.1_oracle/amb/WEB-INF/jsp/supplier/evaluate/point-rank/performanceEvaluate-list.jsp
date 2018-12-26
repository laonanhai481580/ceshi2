<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title><s:text name='main.title'/></title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
	var myLayout;
	$(document).ready(function(){
		myLayout = $('body').layout({
			north__paneSelector : '#header',
			north__size : 66,
			west__size : 250,
			north__spacing_open : 31,
			north__spacing_closed : 31,
			west__spacing_open : 6,
			west__spacing_closed : 6,
			center__minSize : 400,
			resizable : false,
			paneClass : 'ui-layout-panel',
			north__resizerClass : 'ui-layout-resizer',
			west__onresize : $.layout.callbacks.resizePaneAccordions,
			center__onresize : contentResize
		});
	});
	
	function realTotalPointFormater(cellValue,objmodel,obj){
		return "<div style='color:red'>"+cellValue+"</div>";
	}
	
	function changeLocation(){
		var supplierName= $("#gys").val();
		var year= $("#year").val();
		var month= $("#month").val();
		var parentModelId= $("#parentModelId").val();
		var str = "?supplierName="+supplierName;
		str += "&year="+year; 
		str += "&month="+month; 
		str += "&parentModelId="+parentModelId; 
		$(document).mask();
		window.location = "${supplierctx}/evaluate/point-rank/performanceEvaluate-list.htm" + str;
	}
	
	//加载二级
	function onchangeGsmCodeSecRules(){
		var parentModelId = $("#parentModelId").val();
		$.post("${supplierctx}/evaluate/point-rank/change-model.htm?parentModelId="+parentModelId,function(result){
			$("#month").empty();
			if(result.months.length == 0){
				$("#month").attr("disabled","disabled");
				$("#month").css({background: "#eeeeee"});
				return false;
			}
			$("#month").attr("disabled","");
			$("#month").css({background: ""});
			for (var i = 0; i < result.months.length; i++) {
				var option = result.months[i];
				$("#month").append("<option value='"+option.value+"'>"+option.name+"</option>");
			}
		},'json');
	}
</script>
<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="evaluate";
		var thirdMenu="performanceEvaluate_list";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west" id="west-ui">
		<%
			request.setAttribute("selLevel",1);
		%>
		<%@include file="../quarter/left.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post"  action=""></form>
			<aa:zone name="mainZone">
				<aa:zone name="main">
					<div class="opt-btn" style="text-align: right;">
<%-- 						<button class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b><s:text name="common.search"/></span></span></button> --%>
						供应商：<input type="text" name="supplierName" id="gys" value="${supplierName}"/>
						<s:select list="estimateModels"listKey="value" theme="simple" value="parentModelId" listValue="name" name="parentModelId" onchange="onchangeGsmCodeSecRules()"></s:select>
						<s:select list="years"listKey="value" theme="simple" value="year" listValue="name" name="year"></s:select>
						<s:select list="months"listKey="value" theme="simple" value="month" listValue="name" name="month"></s:select>
						<button class='btn' onclick="changeLocation();"><span><span><b class="btn-icons btn-icons-search"></b><s:text name="common.search"/></span></span></button>
					</div>
					<div id="message"><s:actionmessage theme="mytheme" /></div>	
					<div id="opt-content" >
						<form id="contentForm" name="contentForm" method="post"  action="">
							<grid:jqGrid gridId="performanceEvaluateGridId" url="${supplierctx}/evaluate/point-rank/performanceEvaluate-listDatas.htm?year=${year}&month=${month}&parentModelId=${parentModelId}&supplierName=${supplierName}" submitForm="defaultForm" code="SUPPLIER_PERFORMANCE_EVALUATE" ></grid:jqGrid>
						</form>
					</div>
				</aa:zone>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>