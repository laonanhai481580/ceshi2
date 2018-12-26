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
	function formateAttachmentFiles(value,o,obj){
		var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title='<s:text name='上传附件'/>'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
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
	//红警灯
	function createColorLight(cellvalue, options, rowObject){
		var inspectionPlanDate=rowObject.inspectionPlanDate;//约定送货日期
		var time = (new Date).getTime();//当前时间内
		var newDate=time-new Date(inspectionPlanDate).getTime();
		var  tempTime=7*24 * 60 * 60 *1000;
		if (newDate < 0) {//(24 * 60 * 60 * 1000)  一天时间
			return '<div style="text-align:center;"><img src="${ctx}/images/yellow.png"/></div>';
		} else {
			if (newDate > 0 && newDate < tempTime) {
				return '<div style="text-align:center;"><img src="${ctx}/images/red.png"/></div>';
			} else {
				return '';
			}
		}
	}
	//校验
	function test() {
		var id = jQuery("#dynamicMeasurementInspectionPlan").getGridParam('selarrrow');
		$.post("${gsmctx}/inspectionplan/test.htm", {
			ids : id + ""
		}, function(data) {
			$("#dynamicMeasurementInspectionPlan").trigger("reloadGrid");
		});
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="inspectionplan";
		var thirdMenu="_myInspectionPlan";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-inspection-plan-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
						<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>					
					<security:authorize ifAnyGranted="gsm_inspectionplan_export">
						<button class="btn" onclick="iMatrix.export_Data('${gsmctx}/inspectionplan/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="gsm_intransited-equipment_test">
						<button class="btn" onclick="test()"><span><span><b class="btn-icons btn-icons-paste"></b><s:text name="送校"/></span></span></button> 
					</security:authorize>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="dynamicMeasurementInspectionPlan" url="${gsmctx}/inspectionplan/list-datas.htm?inspectionState=${inspectionState}" code="MEASUREMENT_INSPECTION_PLAN" pageName="page" ></grid:jqGrid>
					</form>
			</div>
			</aa:zone>
		</div>
	</div> 
</body>
</html>