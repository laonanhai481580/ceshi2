<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<style type="text/css">
	#flagExport{
		position:absolute; 
		display: none;
		z-index:999;
		background: #eee;
		border:3px solid #e1e1e1;
		box-shadow:0px 0px 4px #888;border-radius: 3px;margin: 0px;
	}
	#flagExport ul{ padding: 0px;margin: 0px;left: auto;width:160px;}
	#flagExport ul li{ padding: 2px 6px; list-style-type: none; width:150px;height: 20px;}
	#flagExport ul li:hover{background:#6CA412;}
	#flagExport ul li a {padding-left: 4px;color:#555;cursor:pointer}
	#flagExport ul li:hover a {padding-left: 5px;color:#fff;}
</style>
<script type="text/javascript">
//格式化编号
	var pagegsmSize=0,pageGsmNum=0;
	jQuery.extend($.jgrid.defaults,{
		loadComplete : function(){
			var rowDatas = $("#dynamicMeasurementEquipment").jqGrid('getRowData');
			pagegsmSize=rowDatas.length;
			pageGsmNum=0;
			 for(var obj in rowDatas){
				var isWarm=rowDatas[obj].isWarm;
				if(isWarm==1){
					pageGsmNum++;
				}
			}
			$("#pagegsmSize").html(pagegsmSize);
			$("#pageGsmNum").html(pageGsmNum);
		}
	});
	//格式化编号
	function click(cellValue, options, rowObject) {
		if(cellValue != null && cellValue != ""){
			return "<a href='${gsmctx}/equipment/input.htm?id=" + rowObject.id + "'>" + cellValue + "</a>";
		}
		return "";
	}
	//新建
	function creategsmEquipment() {
		window.location = "${gsmctx}/equipment/input.htm";
	}
	
	
	
	//生成校验计划
	function createEquipmentPlan() {
		var rowIds = $("#dynamicMeasurementEquipment").getGridParam("selarrrow");
		if (rowIds.length == 0) {
			alert("<s:text name='请选择数据！！'/>");
			return;
		}
		var flag = false;
		for (var i = 0; i < rowIds.length; i++) {
			var rowData = $("#dynamicMeasurementEquipment").jqGrid("getRowData",rowIds[i]);
			if(rowData.checkMethod==""||rowData.checkMethod=="&nbsp;"){
				alert("校验方式不能为空");
				return;
			}
			if(!("在用" == rowData.measurementState || "在库" == rowData.measurementState)){
				flag = true;
			}
		}
		if(flag){
			alert("<s:text name='状态不是【在用】或【在库】不能生成校验计划'/>");
			return false;
		}
		$.post("${gsmctx}/equipment/create-equipment-plan.htm?ids="+ rowIds, null,function(data) {
			if (data.error) {
				alert(data.message);
				return false;
			}
			alert(data.message);
			$("#dynamicMeasurementEquipment").trigger("reloadGrid");
		}, "json");
	}
	
	//选择人员
	function storagerClick(obj) {
		var acsSystemUrl = "${ctx}";
		popTree({
			title : "<s:text name='编辑人'/>",
			innerWidth : '400',
			treeType : 'MAN_DEPARTMENT_TREE',
			defaultTreeValue : 'id',
			leafPage : 'false',
			multiple : 'false',
			hiddenInputId : obj.currentInputId,
			showInputId : obj.currentInputId,
			acsSystemUrl : acsSystemUrl,
			callBack : function() {
			}
		});
	}
	
	//红警灯
/* 	function createColorLight(cellvalue, options, rowObject){
		var nextProofTime=rowObject.nextProofTime;//计划校验日期
		var time = (new Date).getTime();//当前时间内
		var newDate=new Date(nextProofTime).getTime()-time;
		var  tenDayTime=10*24 * 60 * 60 *1000;
		var  monthTime=30*24 * 60 * 60 *1000;
		var checkMethod=rowObject.checkMethod;//计划校验日期
		var str="";
		if(!nextProofTime){
			str= '<div style="text-align:center;"><img src="${ctx}/images/green.gif"/></div>';
		}
		if(nextProofTime&&checkMethod =="内校"){
			if(newDate>tenDayTime){
				str= '<div style="text-align:center;"><img src="${ctx}/images/green.gif"/></div>';
			}else{
				pageGsmNum++;
				str= '<div style="text-align:center;"><img src="${ctx}/images/red.gif"/></div>';				
			};
		}
		if(nextProofTime&&checkMethod=="外校"){
			if(newDate>monthTime){
				str= '<div style="text-align:center;"><img src="${ctx}/images/green.gif"/></div>';
			}else{
				pageGsmNum++;
				str= '<div style="text-align:center;"><img src="${ctx}/images/red.gif"/></div>';				
			};
		}	
		$("#pageGsmNum").html(pageGsmNum);
		return str;
	} */	
	function createColorLight(cellvalue, options, rowObject){
		var isWarm=rowObject.isWarm;
		if(isWarm&&isWarm==1){
			str= '<div style="text-align:center;"><img src="${ctx}/images/red.gif"/></div>';				
		}else{
			str= '<div style="text-align:center;"><img src="${ctx}/images/green.gif"/></div>';
		}
		return str;
	}	
	
	//选择部门
	function useDeptClick(obj) {
		var acsSystemUrl = "${ctx}";
		popTree({
			title : "<s:text name='选择部门'/>",
			innerWidth : '400',
			treeType : 'DEPARTMENT_TREE',
			defaultTreeValue : 'id',
			leafPage : 'false',
			multiple : 'false',
			hiddenInputId : obj.currentInputId,
			showInputId : obj.currentInputId,
			acsSystemUrl : acsSystemUrl,
			callBack : function() {
			}
		});
	}
		/*---------------------------------------------------------
	函数名称:showIdentifiersDiv
	参          数:
	功          能:标识为（下拉选）
	------------------------------------------------------------*/
	function showIdentifiersDiv(idName,buttonId){
		if($("#"+idName).css("display")=='none'){
			removeSearchBox();
			$("#"+idName).show();
			var position = $("#"+buttonId).position();
			$("#"+idName).css("left",position.left+15);
			$("#"+idName).css("top",position.top+28);
		}else{
			$("#"+idName).hide();
		}
	}
	function show_moveiIdentifiersDiv(idName){
		clearTimeout(identifiersDiv);
	}
	var identifiersDiv;
	function hideIdentifiersDiv(idName){
		identifiersDiv = setTimeout('$("#"+idName+")".hide()',300);
	}
 	function backToTask(obj){
 		hideIdentifiersDiv();
 		var title = $(obj).attr("title");
 		if(title=="导入量检数据"){
 			imports();
 		}else{
 			downloadExcelFormat();
 		}
 	}
 	/*
	下载导入模板
	*/
 	function downloadExcelFormat(){
		window.location = '${gsmctx}/equipment/download-excel-format.htm';
	}
	//导入
	function imports(){
		var url = '${gsmctx}/equipment/list-import-form.htm';
		$.colorbox({href:url,iframe:true, innerWidth:380, innerHeight:200,
			overlayClose:false,
			title:"<s:text name='导入量检具信息'/>",
			onClosed:function(){
				$("#dynamicMeasurementEquipment").trigger("reloadGrid");
			}
		});
	}
	//生成MSA计划
	function createEquipmentMsaPlan() {
		var rowIds = $("#dynamicMeasurementEquipment").getGridParam("selarrrow");
		if (rowIds.length == 0) {
			alert("<s:text name='请选择数据！！'/>");
			return;
		}
		var flag = false;
		for (var i = 0; i < rowIds.length; i++) {
			var rowData = $("#dynamicMeasurementEquipment").jqGrid("getRowData",rowIds[i]);
			if(!("在用" == rowData.measurementState || "在库" == rowData.measurementState)){
				flag = true;
			}
		}
		if(flag){
			alert("<s:text name='状态不是【在用】或【在库】不能生成MSA计划'/>");
			return false;
		}
		$.post("${gsmctx}/equipment/create-equipment-msaplan.htm?ids="+ rowIds, null, function(data) {
			if (data.error) {
				alert(data.message);
				return false;
			}
			alert(data.message);
			$("#dynamicMeasurementEquipment").trigger("reloadGrid");
		}, "json");
	}
	//报修
	function sendMaintenance() {
		var rowIds = $("#dynamicMeasurementEquipment").getGridParam("selarrrow");
		if (rowIds.length == 0) {
			alert("<s:text name='请选择数据！！'/>");
			return false;
		}
		var flag = false;
		for (var i = 0; i < rowIds.length; i++) {
			var rowData = $("#dynamicMeasurementEquipment").jqGrid("getRowData",rowIds[i]);
			if(!("在用" == rowData.measurementState || "在库" == rowData.measurementState)){
				flag = true;
			}
		}
		if(flag){
			alert("<s:text name='状态不是【在用】或【在库】不能报修'/>");
			return false;
		}
		$.post("${gsmctx}/equipment/createEquipmentMaintenance.htm?ids="+ rowIds, null, function(data) {
			if (data.error) {
				alert(data.message);
				return false;
			}
			$("#dynamicMeasurementEquipment").trigger("reloadGrid");
			window.location = "${gsmctx}/equipment-maintenance/list.htm?ids=" + rowIds;
		}, "json");
	}
	function hideState(obj){
		var id = $("#dynamicMeasurementEquipment").jqGrid("getGridParam","selarrrow");
		if(id.length==0){
			alert("请选择一项！");
			return ;
		}
		
		$.post('${gsmctx}/equipment/hideState.htm?id='+id+"&&type=N",
		function(data){
			  window.location.reload(href='${gsmctx}/equipment/list.htm');
// 		  alert("修改成功");
		  alert(data);
		});
}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu = "equipment";
		var thirdMenu = "allEquipment";
		var treeMenu = "myEquipmentReport";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-equipment-report-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="gsm_equipment_save">
						<button class="btn" onclick="creategsmEquipment();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="gsm_equipment_delete">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
 					<security:authorize ifAnyGranted="gsm_equipment_create-equipment-plan"> 
 						<button class="btn" onclick="createEquipmentPlan()"><span><span><b class="btn-icons btn-icons-paste"></b><s:text name="生成校验计划" /></span></span></button> 
 					</security:authorize> 
<%-- 					<security:authorize ifAnyGranted="gsm_equipment_sendMaintenance">
						<button class="btn" onclick="sendMaintenance()"><span><span><b class="btn-icons btn-icons-wrench"></b><s:text name="报修" /> </span></span></button>
					</security:authorize> --%>
						<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询 </span></span></button>
					<security:authorize ifAnyGranted="gsm_equipment_import">
						<button class='btn' type="button" id="_export_button" onclick='showIdentifiersDiv("flagExport","_export_button");'><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>
					</security:authorize> 
					<security:authorize ifAnyGranted="gsm_equipment_create-equipment-msaplan">
						<button class="btn" onclick="createEquipmentMsaPlan()"><span><span><b class="btn-icons btn-icons-paste"></b><s:text name="生成MSA计划" /></span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="gsm_equipment_export">
						<button class="btn" onclick="iMatrix.export_Data('${gsmctx}/equipment/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="gsm_equipment_hide">
						<button class='btn' onclick="hideState(this)" type="button">
							<span><span><b class="btn-icons btn-icons-undo"></b>隐藏</span></span>
						</button>
					</security:authorize>
					
					<div style="float:right;position:absolute; right:50px;top:6px;" >
					<table style="float:right ">
						<tr>
							<td style="font-family:SimHei;font-weight:bold;">仪器总项数:</td>
							<td id="pagegsmSize" style="font-family:STHeiti"></td>
							<td>&nbsp;</td>
							<td style="font-family:SimHei;font-weight:bold;">校准报警项:</td>
							<td id="pageGsmNum" style="font-family:STHeiti;color:red"></td>
						</tr>
					</table> 
					</div>
				</div>
				<div id="opt-content">
<!-- 					<table>
						<tr>
							<td>仪器总项数</td>
							<td id="pagegsmSize"></td>
						</tr>
						<tr>
							<td>校准报警项</td>
							<td id="pageGsmNum"></td>
						</tr>
					</table> -->		
					<input type="hidden" id="colCode"  name="colCode" value="${colCode}"/>
					<div id="flagExport" onmouseover='show_moveiIdentifiersDiv("flagExport");' onmouseout='hideIdentifiersDiv("flagExport");'>
						<ul style="width:160px;">
							 <li onclick="backToTask(this);" style="cursor:pointer;" title="导入量检数据">导入量检数据</li>
							 <li onclick="backToTask(this);" style="cursor:pointer;" title="下载导入模板">下载导入模板</li>
						</ul>
					</div>
					<form id="contentForm" method="post" action="">
						<grid:jqGrid gridId="dynamicMeasurementEquipment" url="${gsmctx}/equipment/list-state.htm?type=N" code="GSM_EQUIPMENT" pageName="page" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>