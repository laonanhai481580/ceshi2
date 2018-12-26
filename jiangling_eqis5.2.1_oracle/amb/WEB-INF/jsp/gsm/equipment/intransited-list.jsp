<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
	//复制
	function from(){	
		var id = jQuery("#dynamicIntransitedEquipment").getGridParam('selarrrow');
		$.post("${gsmctx}/intransited-equipment/frow.htm",{ids:id+""}, function(data){$("#dynamicIntransitedEquipment").trigger("reloadGrid")});
	}
	//重写方法（用于处理服务器返回的值）
	function $successfunc(response){
		var result = eval("(" + response.responseText + ")");
		if(result.error){
			alert(result.message);
			return false;
		}else{
			return true;
		}
	}
	//红警灯
	function createColorLight(cellvalue, options, rowObject){
		var agreedDeliveryDate=rowObject.agreedDeliveryDate;//约定送货日期
		var time = (new Date).getTime();//当前时间内
		var newDate=time-new Date(agreedDeliveryDate).getTime();
		var  tempTime=7*24 * 60 * 60 *1000;
 		if(newDate<0){//(24 * 60 * 60 * 1000)  一天时间
 			return '<div style="text-align:center;"><img src="${ctx}/images/yellow.png"/></div>';
 		}else {
 			if(newDate>0&&newDate<tempTime){
 				return '<div style="text-align:center;"><img src="${ctx}/images/red.png"/></div>';
	 		}else{
	 			return '';
	 		}
		}
	}
	function confirmDelivery(){	
		var id = jQuery("#dynamicIntransitedEquipment").getGridParam('selarrrow');
		$.post("${gsmctx}/intransited-equipment/confirm-delivery.htm",{ids:id+""}, function(data){$("#dynamicIntransitedEquipment").trigger("reloadGrid")});
	}
	function createIntransitedEquipment(){
		window.location="${gsmctx}/intransited-equipment/input.htm";	
	}
	function click(cellvalue, options, rowObject){	
		return "<a href='${gsmctx}/intransited-equipment/input.htm?id="+rowObject.id+"'>"+cellvalue+"</a>";
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="intransited";
		var thirdMenu="_myIntransitedEquipment";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>	
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>	
	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-intransited-equipment-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="gsm_intransited-equipment_save">
						<button class="btn" onclick="createIntransitedEquipment();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="gsm_intransited-equipment_frow">
						<button class="btn" onclick="from()"><span><span><b class="btn-icons btn-icons-paste"></b>复制 </span></span></button> 
					</security:authorize>
						<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
					<security:authorize ifAnyGranted="gsm_intransited-equipment_export">
						<button class="btn" onclick="iMatrix.export_Data('${gsmctx}/intransited-equipment/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="gsm_intransited-equipment_delete">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize> 
					<security:authorize ifAnyGranted="gsm_confirm-delivery_input">
						<button class="btn" onclick="confirmDelivery();"><span><span><b class="btn-icons btn-icons-wrench"></b><s:text name="确认收货"/></span></span></button>
					</security:authorize>
						<button class="btn" onclick="sendEMail();" ><span><span><b class="btn-icons btn-icons-email"></b><s:text name="发送邮件"/></span></span></button>
				</div>
				<div id="opt-content">
					<form id="contentForm" method="post"  action="">
						<grid:jqGrid gridId="dynamicIntransitedEquipment" url="${gsmctx}/equipment/intransited-list-datas.htm" code="MEASUREMENT_EQUIPMENT" pageName="page" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div> 
</body>
</html>