<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
	function $successfunc(response){
		var result = eval("(" + response.responseText + ")");
		if(result.error){
			alert(result.message);
			return false;
		}else{
			return true;
		}
	}
	function $beforeEditRow(rowId,iRow,iCol,e){
		var isRight = false;
		<security:authorize ifAnyGranted="gsm_equipment-msaplan_save">
			isRight =  true;
		</security:authorize>
		return isRight;
	}	
	
	function showPicture(value,options,obj){
		var strs = "";
		strs = "<div style='width:100%;' title='上传附件' ><a class=\"small-button-bg\" onclick=\"attachmentFilesClick('"+obj.id+"');\" href=\"#\"><input type='hidden' id='"+obj.id+"_hiddenFiles' value='"+value+"'/><span id='"+obj.id+"_uploadBtn' class='ui-icon ui-icon-image uploadBtn' style='cursor:pointer;display:none;'></span></a><span style='text-align:left;' id='"+obj.id+"_showFiles'>"+$.getDownloadHtml(value)+"</span><div>";
		return strs;
	}
	function attachmentFilesClick(rowId){
		//上传附件 
		$.upload({   
			showInputId : rowId + "_showFiles",
			hiddenInputId : rowId + "_hiddenFiles",
			title:"上传附件",
			callback:function(files){
				params.attachmentFiles = $("#" + rowId + "_hiddenFiles").val();
			}
		}); 
	}
	function addNew(){
		iMatrix.addRow();
		$("#undefined_uploadBtn").show();
	}
	var params = {};
	function $oneditfunc(rowId){
		selRowId = rowId;
		params.attachmentFiles = $("#" + rowId + "_hiddenFiles").val();
		params.hisAttachmentFiles = params.attachmentFiles;
		$(".uploadBtn").hide();
		$("#undefined_uploadBtn").show();
		$("#" + rowId + "_uploadBtn").show();
		
	}
	function $afterrestorefunc(rowId){
		$("#" + rowId + "_uploadBtn").hide();
	}
	function $processRowData(data){
		for(var pro in params){
			data[pro] = params[pro];
		}
		return data;
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu = "inspectionmsaplan";
		var thirdMenu = "_myInspectionmsaplan";
	</script>
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp"%>
	</div>
	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-inspection-msaplan-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="gsm_equipment-msaplan_delete">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>  
<%-- 					<security:authorize ifAnyGranted="gsm_equipment-msaplan_sendMsa">
						<button class="btn" onclick="sendMsa()"><span><span><b class="btn-icons btn-icons-paste"></b><s:text name="送MSA"/></span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="gsm_inspectionMsaplan-mail">
						<button class="btn" onclick="mailSettings('inspectionMsaplan-mail');"><span><span><b class="btn-icons btn-icons-settings"></b><s:text name="提前邮件提醒设置"/></span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="gsm_inspectionMsaplan-mail">
						<button class="btn" onclick="mailSettingsOver('inspectionMsaplan-mail-over');"><span><span><b class="btn-icons btn-icons-settings"></b><s:text name="超期邮件提醒设置"/></span></span></button>
					</security:authorize> --%>
						<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="gsm_equipment-msaplan_export">
						<button class="btn" onclick="iMatrix.export_Data('${gsmctx}/equipment-msaplan/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="">
						<grid:jqGrid gridId="dynamicInspectionMsaplan" url="${gsmctx}/equipment-msaplan/list-datas.htm" code="MEASUREMENT_MSAINSPECTION_PLAN" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>