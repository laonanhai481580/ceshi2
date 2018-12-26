<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%Date date = new Date(System.currentTimeMillis());%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title><s:text name='main.title'/></title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
	function contentResize(){
	}
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
			paneClass : 'ui-layout-pane',
			north__resizerClass : 'ui-layout-resizer',
			west__onresize : $.layout.callbacks.resizePaneAccordions,
			center__onresize : contentResize
		});
	});
</script>
<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
<style type="text/css">
table tr {
	height: 29px;
}
</style>
<script type="text/javascript">
	var postData = {};
	var params = {
		supplierId : '${supplierId}',
		evaluateYear : '${evaluateYear}'
	};
	$(document).ready(function(){
		contentResize(); 
	});
	//改变路径
	function changeLocation(p){
		p = p || {};
		for(var pro in p){
			params[pro] = p[pro];
		}
		$(document).mask();
		window.location = "${supplierctx}/evaluate/quarter/evaluate-total-table.htm?" + $.param(params);
	}
	//评价年改变
	function yearChange(){
		var evaluateYear = document.getElementsByName("evaluateYear")[0].value;
		var materialType = document.getElementsByName("materialType")[0].value;
		changeLocation({evaluateYear:evaluateYear,materialType:materialType});
	}
</script>
<script type="text/javascript">
    function evaluateDetail(key,myType){
    	var keys = key.split("_");
    	var params = {};
		params['params.myType'] = myType;
		params['params.type'] = keys[0];
		params['params.'+myType] = keys[1];
		
		params['params.evaluateYear'] = '${evaluateYear}';
		params['params.supplierId'] = '${supplierId}';

		var url = "${supplierctx}/evaluate/quarter/evaluate-detail.htm?_source="+keys[0];
		for(var pro in params){
			url += "&" + pro + "=" + params[pro];
		}
    	$.colorbox({href:encodeURI(url),iframe:true,
			innerWidth:$(window).width()<1366?$(window).width()-124:$(window).width()-366, 
			innerHeight:$(window).height()<768?$(window).height()-68:$(window).height()-68,
			overlayClose:false,
			title:"不良数与抽检数不正确!",
		});
    }
    function exportToExcel(){
    	document.getElementById("tableInfo").value=document.getElementById("table").innerHTML;
    	$("#applicationForm").submit();
    	/* var url = "${supplierctx}/evaluate/quarter/export-excel.htm";
    	$.post(url, tableInfo,function(data) {
			if (data.error) {
				alert(data.message);
				return false;
			}
		}, "json"); */
    }
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="evaluate";
		var thirdMenu="company-info";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west" id="west-ui">
		<%@include file="left.jsp"%>
	</div>
	<div class="ui-layout-center">
		<%
			String error = (String)ActionContext.getContext().get("error");
		%>
		<%
			if("".equals(error)){
		%>
			<div class="opt-body">
				<div class="opt-btn">
					<table cellpadding="0" cellspacing="0" width="100%">
						<tr>
							<td style="padding-left: 6px;" valign="middle">
								年份:&nbsp;<s:select list="evaluateYears" listKey="value" name="evaluateYear"
									theme="simple" value="evaluateYear"	onchange="yearChange()" listValue="name"></s:select>
									物料类别:&nbsp;
							   <s:select list="materialTypes" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="materialType"
								  id="materialType"
								  emptyOption="false"
								  labelSeparator=""
								  onchange="yearChange()"
								  cssStyle="width:150px;"
							  ></s:select>
							  <input type="submit" name="Excel" value="导出表格" onclick="exportToExcel()"/>
<!-- 							  <button  class='btn' onclick="exportToExcel();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button> -->
							</td>
						</tr>
					</table>
				</div>
				<div id="opt-content" style="overflow-y:auto;" >
					<div style="height: 30px; text-align: center">
						<h2>${supplier.name}评价得分总表</h2>
					</div>
					<form id="applicationForm" action="${supplierctx}/evaluate/quarter/export-excel.htm" method="post">
									 <input type="hidden" id="tableInfo" name="tableInfo" value=""/>					
					 <span id="table">
					<table class="form-table-border" id="estimate-service1-table-1" 
						style="width: 100%">
						<thead>
							<tr>
								<th  style="width: 9%;">评价指标</th>
								<th style="width: 18%;" colspan="2">评价指标明细</th>
								<th style="width: 9%;">总分</th>
<!-- 								<th style="width: 4%;">1</th> -->
<!-- 								<th style="width: 4%;">2</th> -->
<!-- 								<th style="width: 4%;">3</th> -->
								<th style="width: 4%;">一季度</th>
<!-- 								<th style="width: 4%;">4</th> -->
<!-- 								<th style="width: 4%;">5</th> -->
<!-- 								<th style="width: 4%;">6</th> -->
								<th style="width: 4%;">二季度</th>
<!-- 								<th style="width: 4%;">7</th> -->
<!-- 								<th style="width: 4%;">8</th> -->
<!-- 								<th style="width: 4%;">9</th> -->
								<th style="width: 4%;">三季度</th>
<!-- 								<th style="width: 4%;">10</th> -->
<!-- 								<th style="width: 4%;">11</th> -->
<!-- 								<th style="width: 4%;">12</th> -->
								<th style="width: 4%;">四季度</th>
							</tr>
						</thead>
						<tbody id="estimate-service1-table-body">
							${modelObj}
						</tbody>
					</table>
					</span>
					</form>
				</div>
			</div>
		<%}else{ %>
			<div style="font-size: 24px;font-weight: bold;"><s:actionmessage /></div>
			<%-- <table width="100%" style="background: white;">
				<tr>
					<td valign="top">
						<h2>
							<s:actionmessage />
						</h2>
					</td>
				</tr>
			</table> --%>
		<%} %>
	</div>
</body>
</html>