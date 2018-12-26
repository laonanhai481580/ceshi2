<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${ctx}/widgets/validation/jquery.validate.js"
	type="text/javascript"></script>
<link rel="stylesheet" type="text/css"
	href="${ctx}/widgets/validation/cmxform.css" />
<script src="${ctx}/widgets/validation/jquery.metadata.js"
	type="text/javascript"></script>
<script src="${ctx}/widgets/validation/dynamic.validate.js"
	type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
<script type="text/javascript">
	var mydata = [ 
	               {id:"",supervisionDate:"",supervisionReportNO:"",supplierName:"",partName:"",partNO:"",supervisionGoal:"",remark:""}, 
	               {id:"",supervisionDate:"",supervisionReportNO:"",supplierName:"",partName:"",partNO:"",supervisionGoal:"",remark:""}, 
	               {id:"",supervisionDate:"",supervisionReportNO:"",supplierName:"",partName:"",partNO:"",supervisionGoal:"",remark:""}, 
	               {id:"",supervisionDate:"",supervisionReportNO:"",supplierName:"",partName:"",partNO:"",supervisionGoal:"",remark:""}, 
	               {id:"",supervisionDate:"",supervisionReportNO:"",supplierName:"",partName:"",partNO:"",supervisionGoal:"",remark:""}, 
	               {id:"",supervisionDate:"",supervisionReportNO:"",supplierName:"",partName:"",partNO:"",supervisionGoal:"",remark:""}, 
	               {id:"",supervisionDate:"",supervisionReportNO:"",supplierName:"",partName:"",partNO:"",supervisionGoal:"",remark:""}, 
	               {id:"",supervisionDate:"",supervisionReportNO:"",supplierName:"",partName:"",partNO:"",supervisionGoal:"",remark:""}, 
	             ];
	$(document).ready(function(){
		$("#list1").jqGrid({ 
			datatype: "local",
			data:mydata,
			shrinkToFit:false,
			pager:"pager1",
			colNames:['序号','监察日期', '监察报告编号', '供应商名称','零件名','零件号','监察得分','备注'], 
			colModel:[ 
			           {name:'id',index:'id',width:50,align:"center"}, 
		               {name:'supervisionDate',index:'supervisionDate',width:100}, 
		               {name:'supervisionReportNO',index:'supervisionReportNO',width:120}, 
		               {name:'supplierName',index:'supplierName',width:180}, 
		               {name:'partName',index:'partName',width:150}, 
		               {name:'partNO',index:'partNO',width:150}, 
		               {name:'supervisionGoal',index:'supervisionGoal',width:100}, 
		               {name:'remark',index:'remark',width:150}, 
		               ], 
		               multiselect: true,
		   gridComplete: function(){ 
		   		var ids = jQuery("#list1").jqGrid('getDataIDs');
		   		for(var i=0;i < ids.length;i++){ 
		   		var cl = ids[i]; 
		   		var operations="<button title='显示详细信息' class='small-button-bg' onclick='showInfo()'><span class='ui-icon ui-icon-document'></span></button>"
		   			jQuery("#list1").jqGrid('setRowData',ids[i],{attachment:operations}); 
		   			}
		   		contentResize();
		   		}, 
		   }); 
		$("#datepicker1").datepicker();
		$("#datepicker2").datepicker();
	})
	
	function showInfo(){
		$.colorbox({href:'supplier-archives-contract-info.html',iframe:true, innerWidth:800, innerHeight:600,overlayClose:false,title:"供应商信息"});
	}
	
	function _create_report(){
		
		top.location="supplier-supervision-report-input.html";
	}
	</script>



</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="supervision";
		var thirdMenu="_report_all";
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
					<button class='btn' onclick="_create_report()"><span><span>新建</span></span></button>
					<button class='btn' onclick="_create_report()"><span><span>修改</span></span></button>
					<button class='btn' onclick=""><span><span>删除</span></span></button>
					<button class='btn' onclick="toggleSearchDiv();"><span><span>查询</span></span></button>
					<button class='btn' onclick=""><span><span>导出</span></span></button>
				 </div>
				 <div id="searchDiv" style="display:block">
						<form onsubmit="return false;">
							<table class="form-table-without-border">
								<tr>
									<td>监察日期</td>
									<td><input id="datepicker1"  readonly="readonly" style="width:65px;" class="line"/>至
									    <input id="datepicker2" readonly="readonly" style="width:65px;" class="line"/></td>
									<td>供应商</td>
									<td><input type="text"/></td>
									<td>零件名称</td>
									<td><input type="text"/></td>	
								</tr>
								<tr>
									<td>零件号</td>
									<td><input type="text" style="width:152px;"/></td>
									<td>监察报告编号</td>
									<td><input type="text"/></td>
									<td colspan="2" align="right"><button  class='btn' onclick="toggleSearchDiv();"><span><span>确定</span></span></button></td>
									<td colspan="2"><button  class='btn' onclick="toggleSearchDiv();"><span><span>取消</span></span></button></td>
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