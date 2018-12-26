<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>VIN明细列表</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
	<script type="text/javascript">
	
	$(document).ready(function(){
		jQuery("#vindetail").jqGrid({
			datatype: "json",
			rownumbers: true,
			pager: '#vindetailPager',
			multiselect:false,
			height:510,
			url:'${mfgctx}/data-acquisition/vin-detail-datas.htm',
			colNames:[ 'VIN','车型配置','采集时间','零件数','换件数','不良数','未返修','返修通过','返修未过','未终检', '终检未通过', '终检通过' ], 
			colModel:[   
			          {name:"barcode",index:"barcode",editable:false,width:110}, 
			          {name:"modelSpecification",index:"modelSpecification",editable:false,width:110}, 
			          {name:"inspectionDate",index:"inspectionDate",editable:false,width:160}, 
			          {name:"partAmount",index:"partAmount",editable:false,width:80}, 
			          {name:"dutySupplier",index:"dutySupplier",editable:false,width:80},        
		               {name:"unqualityAmount",index:"unqualityAmount",editable:false,width:70}, 
		               {name:"notreWorkAmount",index:"notreWorkAmount",editable:false,width:70}, 
		               {name:"notpassReWorkAmount",index:"notpassReWorkAmount",editable:false,width:70}, 
		               {name:"passReWorkAmount",index:"passReWorkAmount",editable:false,width:70}, 
		               {name:"notfinalWorkAmount",index:"notfinalWorkAmount",editable:false,width:70}, 
		               {name:"notpassFinalWorkAmount",index:"notpassFinalWorkAmount",editable:false,width:80}, 
		               {name:"passFinalWorkAmount",index:"passFinalWorkAmount",editable:false,width:70}, 
		              ], 
					gridComplete: function(){ 
						
					}
		});
   	});
	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
	var secMenu="dataAcquisition";
	var thirdMenu="vinDetail";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-data-acquisition-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
			<div class="opt-body">
				<div class="opt-btn">
					</div>
					<div id="opt-content">
						<div>
						<table id="vindetail"></table>
						<div id="vindetailPager"></div>
						</div>
					</div>
			</div>
	</div>
	
</body>
</html>