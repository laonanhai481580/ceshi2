<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
	function $oneditfunc(rowid){
		params = {
			hisAttachmentFiles : $("#" + rowid + "_hiddenAttachmentFiles").val()
		};
		$("#" + rowid + " .upload").show();
	};
	function $processRowData(data){
		for(var pro in params){
			data[pro] = params[pro];
		}
		return data;
	}
	//上传附件
	function formateAttachmentFiles(value,o,obj){
		var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title=\"<s:text name='gsm.message-33'/>\"><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
		return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
	}
	function beginUpload(rowId){
		$.upload({
			showInputId : rowId + "_showAttachmentFiles",
			hiddenInputId : rowId + "_hiddenAttachmentFiles",
			callback : function(files){
				params.attachment = $("#" + rowId + "_hiddenAttachmentFiles").val();
			}
		});
	} 
	//送修
		function sendMaintenance(){	 
		var ids = $("#_myeauipmentmaintenance").jqGrid("getGridParam","selrow");
		if(ids ==null){
			alert("<s:text name='请选择数据！！'/>");	
		}else{
			$.post("${gsmctx}/equipment-maintenance/sendMaintenance.htm?ids="+ids,null,function(data){ 
				if(data.error){
					alert(data.message);
					return false;
				}
				$("#_myeauipmentmaintenance").trigger("reloadGrid");
			},"json"); 
		}
	}
		</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="maintenance";
		var thirdMenu="_mymaintenance1"; 
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-equipment-maintenance-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="gsm_equipment_delete"> 
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
						<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
					<security:authorize ifAnyGranted="gsm_equipment-maintenance_export">
						<button class="btn" onclick="iMatrix.export_Data('${gsmctx}/equipment-maintenance/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="_myeauipmentmaintenance" url="${gsmctx}/equipment-maintenance/list-over-datas.htm" code="MEASUREMENT_EQUIPMENT_MAINTENANCE" pageName="page" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body> 
</html>