<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
	function $oneditfunc(rowId){
		var val = $("#" +rowId + "_measurementNo").val();
		val = val.replace(/<[^<>]*>/g,'');
		if(val=='undefined'){
			val="";
		}
		$("#" +rowId + "_measurementNo").val(val);
		enterKeyToNext("dynamicMeasurementEquipment",rowId,function(){},true);
	}
	function callView(id){
		$.colorbox({
			href:'${gsmctx}/equipment/view.htm?id='+id,
			iframe:true, 
			innerWidth:900, 
			innerHeight:600,
			overlayClose:false,
			title:"<s:text name='详细信息'/>",
			onClosed:function(){
				$("#dynamicMeasurementEquipment").trigger("reloadGrid");
			}
		});
	}
	function click(cellvalue, options, rowObject){	
		if(cellvalue != null && cellvalue != ""){
			return "<a onclick='callView("+rowObject.id+");' href='#'>"+cellvalue+"</a>";
		}
		return "";
	}
	function importSomeInspectionPlan() {
		var ids = jQuery("#dynamicMeasurementEquipment").getGridParam('selarrrow');
		if(ids.length<1){
			alert("<s:text name='请选中需要导入的记录！'/>");
			return;
		}
		$.post("${gsmctx}/equipment/import-some-inspection-plan.htm", {
			deleteIds : ids.join(',')
		}, function(data) {});
	}
	function $successfunc(response){
		if(response.responseText=="false"){
			alert("<s:text name='生产日期应小于购物日期！'/>");
			return false;
		}else if(response.responseText=="false1"){
			alert("<s:text name='购入日期应小于入库日期！'/>");
			return false;
		}else{
			return true;
		}
	}
	//选择人员
	function storagerClick(obj){
		var acsSystemUrl = "${ctx}";
		popTree({ title :"<s:text name='编辑人'/>",
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:obj.currentInputId,
			showInputId:obj.currentInputId,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}});
	}
	//选择部门
	function useDeptClick(obj){
		var acsSystemUrl = "${ctx}";
		popTree({ title :"<s:text name='选择部门'/>",
			innerWidth:'400',
			treeType:'DEPARTMENT_TREE',
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:obj.currentInputId,
			showInputId:obj.currentInputId,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}
		});
	}
	function $addGridOption(jqGridOption){
		jqGridOption.postData.dept=$("#useDept").val();
	}
	function $processRowData(data){
		data.useDept = $("#useDept").val();
		return data;
	}
	function exports(){
		iMatrix.export_Data('${gsmctx}/equipment/export.htm');
	}
	//盘点
	function makeInventory(){
			$.colorbox({
				href:'${gsmctx}/equipment/inventory-input.htm',
				iframe:true, 
				innerWidth:900, 
				innerHeight:600,
				overlayClose:false,
				title:"盘点",
				onClosed:function(){
					$("#dynamicMeasurementEquipment").trigger("reloadGrid");
				}
			});
	} 
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div id="hidden"><input type="hidden" id="useDept" value="${useDept}"/></div>
	<script type="text/javascript">
		var secMenu="equipment";
		var thirdMenu="deptEquipment";
		var treeMenu=$("#useDept").val();
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-equipment-report-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
			<div class="opt-btn">
				<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
				<button class="btn" onclick="exports();"><span><span><b class="btn-icons btn-icons-export"></b> 导出</span></span></button>
				<security:authorize ifAnyGranted="equipment-inventory-input">
					<button class="btn" onclick="makeInventory();"><span><span><b class="btn-icons btn-icons-report"></b>盘点</span></span></button>
				</security:authorize>
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post"  action="">
				<grid:jqGrid gridId="dynamicMeasurementEquipment" url="${gsmctx}/equipment/dept-list-datas.htm?useDept=${useDept}"code="GSM_EQUIPMENT" ></grid:jqGrid>
<%-- 					<grid:jqGrid gridId="dynamicMeasurementEquipment" url="${gsmctx}/equipment/dept-list-datas.htm" code="GSM_EQUIPMENT"  pageName="page" dynamicColumn="${dynamicColumn}"></grid:jqGrid> --%>
				</form>
			</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>