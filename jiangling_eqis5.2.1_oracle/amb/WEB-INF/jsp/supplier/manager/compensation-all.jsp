<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript">
	var mydata = [ 
	               {id:"",batchNO:"",submissionDate:"",serviceNO:"",customerName:"",productModel:"",frameNO:"",engineNO:"",partName:"",accessoriesService:"",state:""}, 
	               {id:"",batchNO:"",submissionDate:"",serviceNO:"",customerName:"",productModel:"",frameNO:"",engineNO:"",partName:"",accessoriesService:"",state:""}, 
	               {id:"",batchNO:"",submissionDate:"",serviceNO:"",customerName:"",productModel:"",frameNO:"",engineNO:"",partName:"",accessoriesService:"",state:""}, 
	               {id:"",batchNO:"",submissionDate:"",serviceNO:"",customerName:"",productModel:"",frameNO:"",engineNO:"",partName:"",accessoriesService:"",state:""}, 
	               {id:"",batchNO:"",submissionDate:"",serviceNO:"",customerName:"",productModel:"",frameNO:"",engineNO:"",partName:"",accessoriesService:"",state:""}, 
	               {id:"",batchNO:"",submissionDate:"",serviceNO:"",customerName:"",productModel:"",frameNO:"",engineNO:"",partName:"",accessoriesService:"",state:""}, 
	               {id:"",batchNO:"",submissionDate:"",serviceNO:"",customerName:"",productModel:"",frameNO:"",engineNO:"",partName:"",accessoriesService:"",state:""}, 
	               {id:"",batchNO:"",submissionDate:"",serviceNO:"",customerName:"",productModel:"",frameNO:"",engineNO:"",partName:"",accessoriesService:"",state:""}, 
	             ];
	$(document).ready(function(){
		$("#list1").jqGrid({ 
			datatype: "local",
			data:mydata,
			shrinkToFit: false,
			pager:"pager1",
			colNames:['序号','批次号', '提交日期', '三包卡号','用户姓名','车型','车架号','发动机号','零部件名称','配件三包','状态'], 
			colModel:[ 
			           {name:'id',index:'id',width:50,align:"center"}, 
		               {name:'batchNO',index:'batchNO',width:120}, 
		               {name:'submissionDate',index:'submissionDate',width:180}, 
		               {name:'serviceNO ',index:'serviceNO',width:120}, 
		               {name:'customerName',index:'customerName',width:120}, 
		               {name:'productModel',index:'productModel',width:100}, 
		               {name:'frameNO',index:'frameNO',width:120}, 
		               {name:'engineNO',index:'engineNO',width:120}, 
		               {name:'partName',index:'partName',width:120}, 
		               {name:'accessoriesService',index:'accessoriesService',width:120}, 
		               {name:'state',index:'state',width:120}, 
		               ], 
		    multiselect: true,
		    gridComplete: function(){ 
       			contentResize();
			}
		   }); 
		$("#datepicker1").datepicker();
		$("#datepicker2").datepicker();
	})
	
	</script>


</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="manager";
		var thirdMenu="_compensation_all";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-manager-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
			<div class="opt-body">
				<div class="opt-btn">
					<button class='btn' onclick="toggleSearchDiv();"><span><span>查询</span></span></button>
					<button class='btn' onclick=""><span><span>导出</span></span></button>
				 </div>
				 <div id="searchDiv" style="display:block">
						<form onsubmit="return false;">
							<table class="form-table-without-border">
								<tr>
									<td >三包卡号</td>
									<td ><input type="text" style="width:125px;"/></td>
									<td >车型</td>
									<td ><input type="text" style="width:125px;"/></td>
									<td >车架号</td>
									<td ><input type="text" style="width:125px;"/></td>
								</tr>
								<tr>
									<td >购车日期</td>
									<td ><input type="text" style="width:125px;"/></td>
									<td >维修日期</td>
									<td ><input type="text" style="width:125px;"/></td>
									<td >零部件名称</td>
									<td ><input type="text" style="width:125px;"/></td>
								</tr>
								<tr>
									<td >图号</td>
									<td ><input type="text" style="width:125px;"/></td>
									<td >零件分类</td>
									<td ><input type="text" style="width:125px;"/></td>
									<td >零部件组件</td>
									<td ><input type="text" style="width:125px;"/></td>
								</tr>
								<tr>
									<td >零部件</td>
									<td ><input type="text" style="width:125px;"/></td>
									<td >故障模式</td>
									<td ><input type="text" style="width:125px;"/></td>
									<td >故障部位</td>
									<td ><input type="text" style="width:125px;"/></td>
								</tr>
								<tr>
									<td >单位</td>
									<td ><input type="text" style="width:125px;"/></td>
									<td >返件方式</td>
									<td ><input type="text" style="width:125px;"/></td>
									<td >配件三包</td>
									<td ><input type="text" style="width:125px;"/></td>
									<td colspan="0" align="right"><button  class='btn' onclick="toggleSearchDiv();"><span><span>确定</span></span></button></td>
									<td colspan="0"><button  class='btn' onclick="toggleSearchDiv();"><span><span>取消</span></span></button></td>
								</tr>
							</table>
						</form>

					</div>
				<div id="opt-content">
					<table id="list1"></table>
					<div id="pager1"></div>
				</div>
			</div>
		</div>

</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>