<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		var params = ${params};
		$(document).ready(function(){
			createDetailGrid();
		});
		function createDetailGrid(){
			var postData = {};
			for(var pro in params){
				postData['params.' + pro] = params[pro];
			}
			$("#detailTable").jqGrid({
				datatype: "json",
				url : '${mfgctx}/manu-analyse/general-plato-detail-datas.htm',
				height : $(window).height()-$("#titleDiv").height()-90,
				width : $(window).width()-10,
				postData : postData,
				pager : '#pager',
				rownumbers : true,
				colModel:${colModel},
			    multiselect: false,
			   	autowidth: true,
			   	forceFit : true,
			   	shrinkToFit: false,
				viewrecords: true, 
				sortorder: "desc",
				gridComplete : function(){}
			});
		}
		function click(cellvalue, options, rowObject){	
			return "<a href='#' onclick='javascript:goTo(\""+rowObject.inspectionRecordId+"\")'>"+cellvalue+"</a>";
		}
		function goTo(id){
			 if($.isFunction(window.parent.goToNewLocationById)){
				window.parent.goToNewLocationById(id);
			} 
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div style="margin:6px auto;text-align:center;font-size:16px;font-weight:bold;" id='titleDiv'>
				${title}
			</div>
			<div id="opt-content">
				<table style="width:100%;" id="detailTable"></table>
				<div id="pager"></div>
			</div>
		</div>
	</div>
</body>
</html>