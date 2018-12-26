<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
	function exportProduct(){
		aa.submit('contentForm', '${gsmctx}/equipment/export.htm'); 
	}
	function borrowGsm(){
		$.colorbox({
			href:'${gsmctx}/common/gsm-bom-select.htm',
			iframe:true, 
			innerWidth:900, 
			innerHeight:600,
			overlayClose:false,
			title:"<s:text name='领用登记'/>",
			onClosed : function(){
				$("#dynamicMeasurementEquipment").trigger("reloadGrid");
			}
		});
	}
	function $oneditfunc(rowId){
		var val = $("#" +rowId + "_measurementNo").val();
		val = val.replace(/<[^<>]*>/g,'');
		if(val=='undefined'){
			val="";
		}
		$("#" +rowId + "_measurementNo").val(val);
		enterKeyToNext("dynamicMeasurementEquipment",rowId,function(){
		},true);
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
		return "<a onclick='callView("+rowObject.id+");' href='#'>"+cellvalue+"</a>";
	}
	//选择人员
	function selectStaff(obj){
		var acsSystemUrl = "${ctx}";
		popTree({ title :"<s:text name='选择人员'/>",
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:obj.currentInputId,
			showInputId:obj.currentInputId,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}
		});
	}
	function storagerClick(obj){
		selectStaff(obj);
	}
	function buyerClick(obj){
		selectStaff(obj);
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
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
	var secMenu="equipmentReport";
	var thirdMenu="allEquipment";
	var treeMenu="borrow";
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
				<a class='btn' onclick="borrowGsm();"><span><span><b class="btn-icons btn-icons-add"></b><s:text name="领用"/></span></span></a>
				<a class='btn' onclick="showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b><s:text name="查询"/></span></span></a>
			</div>
			<div id="opt-content">
				<form id="contentForm" method="post"  action="">
					<grid:jqGrid gridId="dynamicMeasurementEquipment" url="${gsmctx}/equipment/borrow-list-datas.htm" code="MEASUREMENT_EQUIPMENT_BORROW" pageName="page" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
				</form>
			</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>