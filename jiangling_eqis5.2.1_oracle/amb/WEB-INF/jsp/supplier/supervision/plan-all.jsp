<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<link rel="stylesheet" type="text/css"	href="${ctx}/widgets/validation/cmxform.css" />
<script src="${ctx}/widgets/validation/jquery.validate.js"	type="text/javascript"></script>
<script src="${ctx}/widgets/validation/jquery.metadata.js"	type="text/javascript"></script>
<script src="${ctx}/widgets/validation/dynamic.validate.js"	type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
<script type="text/javascript">
	var mydata = [ {id:"",supplierName:"",mainPorducts:"",degree:"",inspectionPart:"",planInspectionDate:"",inspectionLeader:"",recommendReason:"",actualInspctionDate:"",inspctionReportNO:"",inspctionGoal:"",attachment:""}, 
	               {id:"",supplierName:"",mainPorducts:"",degree:"",inspectionPart:"",planInspectionDate:"",inspectionLeader:"",recommendReason:"",actualInspctionDate:"",inspctionReportNO:"",inspctionGoal:"",attachment:""}, 
	               {id:"",supplierName:"",mainPorducts:"",degree:"",inspectionPart:"",planInspectionDate:"",inspectionLeader:"",recommendReason:"",actualInspctionDate:"",inspctionReportNO:"",inspctionGoal:"",attachment:""}, 
	               {id:"",supplierName:"",mainPorducts:"",degree:"",inspectionPart:"",planInspectionDate:"",inspectionLeader:"",recommendReason:"",actualInspctionDate:"",inspctionReportNO:"",inspctionGoal:"",attachment:""}, 
	               {id:"",supplierName:"",mainPorducts:"",degree:"",inspectionPart:"",planInspectionDate:"",inspectionLeader:"",recommendReason:"",actualInspctionDate:"",inspctionReportNO:"",inspctionGoal:"",attachment:""}, 
	               {id:"",supplierName:"",mainPorducts:"",degree:"",inspectionPart:"",planInspectionDate:"",inspectionLeader:"",recommendReason:"",actualInspctionDate:"",inspctionReportNO:"",inspctionGoal:"",attachment:""}, 
	               {id:"",supplierName:"",mainPorducts:"",degree:"",inspectionPart:"",planInspectionDate:"",inspectionLeader:"",recommendReason:"",actualInspctionDate:"",inspctionReportNO:"",inspctionGoal:"",attachment:""}, 
	               {id:"",supplierName:"",mainPorducts:"",degree:"",inspectionPart:"",planInspectionDate:"",inspectionLeader:"",recommendReason:"",actualInspctionDate:"",inspctionReportNO:"",inspctionGoal:"",attachment:""}, 
	             ];
	$(document).ready(function(){
		$("#list1").jqGrid({ 
			datatype: "local",
			data:mydata,
			shrinkToFit:false,
			pager:"pager1",
			colNames:['序号','供应商名称', '主要产品', '供应商等级','重点检查零部件/物料','计划检查日期','审核组长','推荐理由','实际稽查日期','稽查报告编号','稽查得分','附件'], 
			colModel:[ 
			           {name:'id',index:'id',width:50,align:"center"}, 
		               {name:'supplierName',index:'supplierName',width:180}, 
		               {name:'mainPorducts',index:'mainPorducts',width:180}, 
		               {name:'degree',index:'degree',width:100}, 
		               {name:'inspectionPart',index:'inspectionPart',width:180}, 
		               {name:'planInspectionDate',index:'planInspectionDate',width:120}, 
		               {name:'inspectionLeader',index:'inspectionLeader',width:100}, 
		               {name:'recommendReason',index:'recommendReason',width:150}, 
		               {name:'actualInspctionDate',index:'actualInspctionDate',width:160,align:"center"}, 
		               {name:'inspctionReportNO',index:'inspctionReportNO',width:160,align:"center"}, 
		               {name:'inspctionGoal',index:'inspctionGoal',width:100,align:"center"}, 
		               {name:'attachment',index:'attachment',width:80,align:"center"}, 
		               ], 
		               multiselect: true,
		   gridComplete: function(){ 
		   		var ids = jQuery("#list1").jqGrid('getDataIDs');
		   		for(var i=0;i < ids.length;i++){ 
		   		var cl = ids[i]; 
		   		var operations="<button title='显示详细信息' class='small-button-bg' ><span class='ui-icon ui-icon-document'></span></button>"
		   			jQuery("#list1").jqGrid('setRowData',ids[i],{attachment:operations}); 
		   			}
		   		contentResize();
		   		}, 
		   }); 
		 
	})
	
	</script>


</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="supervision";
		var thirdMenu="_plan_all";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-supervision-menu.jsp"%>
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
									<td style="width:80px;">供应商编号</td>
									<td><input type="text" style="width:125px;"/></td>
									<td style="width:80px;">供应商名称</td>
									<td><input type="text" style="width:125px;"/></td>
									<td style="width:65px;">重要度</td>
									<td><input type="text" style="width:125px;"/></td>
								</tr>
								<tr>
									<td>物料类别</td>
									<td><input type="text" style="width:125px;"/></td>
									<td>物料编号</td>
									<td><input type="text" style="width:125px;"/></td>
									<td>物料名称</td>
									<td><input type="text" style="width:125px;"/></td>
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
</html>