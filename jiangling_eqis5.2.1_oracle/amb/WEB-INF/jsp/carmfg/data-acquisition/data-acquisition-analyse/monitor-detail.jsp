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
				url : '${mfgctx}/data-acquisition/data-acquisition-analyse/monitor-detail-datas.htm',
				height : $(window).height()-$("#titleDiv").height()-110,
				width : $(window).width()-20,
				postData : postData,
				pager : '#pager',
				rowNum : 20,
				rownumbers : true,
				colNames:[ 'VIN号','工艺线名称','订单号','批次号','原始条码','发动机','3c号码','车型配置','颜色', '采集开始时间', '采集结束时间','状态','工厂','车间',
					          '生产线','班组','班别', '工序'], 
				colModel:[   
				       {name:"vinNo",index:"vinNo",editable:true,width:230}, 
				       {name:"processBusNode",index:"processBusNode",editable:true},
				       {name:"orderNo",index:"orderNo",editable:true}, 
				       {name:"batchNo",index:"batchNo",editable:true}, 
				       {name:"barcode",index:"barcode",editable:true}, 
				       {name:"engine",index:"engine",editable:true},        
		               {name:"threecNo",index:"threecNo",editable:true}, 
		               {name:"modelConfiguration",index:"modelConfiguration",editable:true}, 
		               {name:"color",index:"color",editable:true}, 
		               {name:"startTime",index:"startTime",editable:true,formatter:timeFormatter}, 
		               {name:"endTime",index:"endTime",editable:true}, 
		               {name:"status",index:"status",editable:true}, 
		               {name:"factory",index:"factory",editable:true}, 
		               {name:"workshop",index:"workshop",editable:true}, 
		               {name:"productionLine",index:"productionLine",editable:true}, 
		               {name:"workGroup",index:"workGroup",editable:true},
		               {name:"workGroupType",index:"workGroupType",editable:true},
		               {name:"workProcedure",index:"workProcedure",editable:true}      
		             ], 
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
			<div style="margin:20px auto;text-align:center;font-size:16px;font-weight:bold;" id='titleDiv'>
			</div>
			<div id="opt-content">
				<table style="width:100%;" id="detailTable"></table>
				<div id="pager"></div>
			</div>
		</div>
	</div>
</body>
</html>