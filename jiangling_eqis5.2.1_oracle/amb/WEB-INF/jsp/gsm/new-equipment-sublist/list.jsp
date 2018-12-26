<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %> 
	<c:set var="actionBaseCtx" value="${gsmctx}/new-equipment" />
	<script type="text/javascript">
	
	function addForm(){
		window.location="${actionBaseCtx}/input.htm";
	}
	
	function confirmDelivery(){	
		var ids = $("#dynamicNewEquipment").getGridParam('selarrrow');
		if(ids.length == 0){
			alert("<s:text name='请先选择数据'/>");
			return;
		}
 		$.post("${gsmctx}/new-equipment/confirm-delivery.htm?ids="+ids,null, function(data){
			if(data.error){
				alert(data.message);
				return false;
			}
			$("#dynamicNewEquipment").trigger("reloadGrid");
		},"json"); 
	} 
	//新建
	function createIntransitedEquipment(){
		window.location="${gsmctx}/new-equipment/input.htm";	
	}
	function click(cellvalue, options, rowObject){
		if(cellvalue != null && cellvalue != ""){
			var p = rowObject.newEquipmentRegister;
			return "<a href='${gsmctx}/new-equipment/input.htm?sunId="+rowObject.id+"'>"+cellvalue+"</a>";
		}
		return "";
	}
	// 邮件
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

	function viewProcessInfo(value,o,obj){
		var strs = "";
		strs = "<div style='width:100%;text-align:center;' title='查看流转历史' ><a class=\"small-button-bg\"  onclick=\"_viewProcessInfo("+obj.id+");\" href=\"#\"><span class='ui-icon ui-icon-info' style='cursor:pointer;text-align:right;'></span></a><div>";
		return strs;
	}
	
	function _viewProcessInfo(formId){
		$.colorbox({href:'${actionBaseCtx}/view-info.htm?id='+formId,iframe:true,
			innerWidth:$(window).width()<1100?$(window).width()-50:1100, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"新设备申请登记详情"
		});
	}
	
	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="newequipment";
		var thirdMenu="_myNewEquipmentsublistlist";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-new-equipment-register-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="gsm_new_equipment_input1">
					<button class='btn' onclick="addForm();">
						<span><span><b class="btn-icons btn-icons-add"></b>新建</span></span>
					</button>
				</security:authorize>
<%-- 				<security:authorize ifAnyGranted="gsm_new_equipment_delete"> --%>
<!-- 					<button class="btn" onclick="iMatrix.delRow();"> -->
<!-- 						<span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span> -->
<!-- 					</button> -->
<%-- 				</security:authorize> --%>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);">
					<span><span><b class="btn-icons btn-icons-search"></b>查询</span></span>
				</button>
				<security:authorize ifAnyGranted="gsm_new_equipment_export">
					<button class='btn'
						onclick="iMatrix.export_Data('${actionBaseCtx}/export.htm');">
						<span><span><b class="btn-icons btn-icons-export"></b>导出</span></span>
					</button>
				</security:authorize>
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post"  action="">
					<grid:jqGrid gridId="dynamicNewEquipment" url="${gsmctx}/new-equipment-sublist/list-datas.htm" code="GSM_EQUIPMENT_SUBLIST" pageName="page" ></grid:jqGrid>
				</form>
			</div>
			</aa:zone>
		</div>
	</div> 
</body>  
</html>