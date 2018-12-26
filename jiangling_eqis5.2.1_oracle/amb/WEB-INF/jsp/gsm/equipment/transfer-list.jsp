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
	//复制
	function from() {
		var ids = $("#equipmentGrid").getGridParam('selarrrow');
		if (ids.length == 0) {
			alert("<s:text name='请选择数据！！'/>");
			return false;
		}
		$.post("${gsmctx}/equipment/frow.htm?ids=" + ids, null, function(data) {
			if (data.error) {
				alert(data.message);
				return false;
			}
			$("#equipmentGrid").trigger("reloadGrid");
		});
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
			hiddenInputId : 'hiddenInputId',
			showInputId : 'showInputId',
			acsSystemUrl : acsSystemUrl,
			callBack : function() {
			}
		});
	}
	//转移确认
	function makeTransfer(){
		$.colorbox({
			href:'${gsmctx}/equipment/transfer-input.htm',
			iframe:true, 
			innerWidth:900, 
			innerHeight:600,
			overlayClose:false,
			title:"转移确认",
			onClosed:function(){
				$("#equipmentGrid").trigger("reloadGrid");
			}
		});
	}
	//责任转移
	function transfer(){
		$.colorbox({
			href:'${gsmctx}/equipment/transfer.htm',
			iframe:true, 
			innerWidth:900, 
			innerHeight:600,
			overlayClose:false,
			title:"责任转移",
			onClosed:function(){
				$("#equipmentGrid").trigger("reloadGrid");
			}			
		});
	}
	//责任转移
/* 	function transfer(){
		var rowIds = $("#equipmentGrid").jqGrid("getGridParam","selarrrow");
		if(rowIds.length==0){
			alert("请选择需要转移责任人的设备!");
			return false;
		}else{
			var acsSystemUrl = "${ctx}";
			popTree({
				title : "<s:text name='目标责任人'/>",
				innerWidth : '400',
				treeType : 'MAN_DEPARTMENT_TREE',
				defaultTreeValue : 'id',
				leafPage : 'false',
				multiple : 'false',
				hiddenInputId : 'hiddenInputId',
				showInputId : 'showInputId',
				acsSystemUrl : acsSystemUrl,
				callBack : toTransfer(rowIds)
			});
			$("#equipmentGrid").trigger("reloadGrid");
		}
		
	}
	function toTransfer(rowIds) {
		var loginName=$("#hiddenInputId").val();
		$.post("${gsmctx}/equipment/transfer.htm?ids=" + rowIds+"&loginName="+loginName, null, function(data) {
			if (data.error) {
				alert(data.message);
				return false;
			}
		});
	} */
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu = "equipment";
		var thirdMenu = "transfer";
		var treeMenu = "transfer_list";
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
					<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
					<security:authorize ifAnyGranted="gsm_transfer_input">
						<button class="btn" onclick="transfer();"><span><span><b class="btn-icons btn-icons-move"></b><s:text name='责任转移'/></span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="gsm_transfer_input">
						<button class="btn" onclick="makeTransfer();"><span><span><b class="btn-icons btn-icons-move"></b><s:text name='转移确认'/></span></span></button>
					</security:authorize>
				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="">
						<grid:jqGrid gridId="equipmentGrid" url="${gsmctx}/equipment/transfer-list-datas.htm" code="GSM_EQUIPMENT_TRANSFER" pageName="page" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>