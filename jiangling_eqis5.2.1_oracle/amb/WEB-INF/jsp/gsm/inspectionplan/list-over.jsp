  <%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
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
		var inspectionPlanDate=rowObject.inspectionPlanDate;//计划校验日期
		var time = (new Date).getTime();//当前时间内
		var newDate=time-new Date(inspectionPlanDate).getTime();
		var  tempTime=7*24 * 60 * 60 *1000;
 		if(newDate<0){//(24 * 60 * 60 * 1000)  一天时间
 			return '<div style="text-align:center;"><img src="${ctx}/images/yellow.png"/></div>';
 		}
 		if(newDate>0&&newDate<tempTime){
 			return '<div style="text-align:center;"><img src="${ctx}/images/red.png"/></div>';
 		}else{
 			return '';
 		}	
	}
	//校验
	function test(){
		var ids = $("#dynamicMeasurementInspectionPlan").jqGrid("getGridParam","selrow"); 
		if(ids == null){
		alert("<s:text name='请选择数据！！'/>");	
		}else{
			$.post("${gsmctx}/inspectionplan/test.htm?ids="+ids,null,function(data){ 
				if(data.error){
					alert(data.message);
					return false;
				}
				$("#dynamicMeasurementInspectionPlan").trigger("reloadGrid");
			},"json"); 
		}
	}
	function $gridComplete(){
		if(!window._initGroupHeader){
			window._initGroupHeader = true;
			$("#dynamicMeasurementInspectionPlan").jqGrid('setGroupHeaders',{
				  useColSpanStyle: true, 
				  groupHeaders:${groupNames}
			});
		};
	}
	function $beforeEditRow(rowId,iRow,iCol,e){
		var isRight = false;
		return isRight;
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="inspectionplan";
		var thirdMenu="_myInspectionPlan1";
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
					<security:authorize ifAnyGranted="gsm_inspectionplan_delete">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b><s:text name="删除"/> </span></span></button>
					</security:authorize>  
					<%-- <security:authorize ifAnyGranted="gsm_intransited-equipment_test">
						<button class="btn" onclick="test()"><span><span><b class="btn-icons btn-icons-paste"></b><s:text name='gsm.inspectionplan.list-over.testbtn'/> </span></span></button> 
					</security:authorize> --%>
						<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b><s:text name="查询"/> </span></span></button>	
					<security:authorize ifAnyGranted="gsm_inspectionplan_export">
						<button class="btn" onclick="iMatrix.export_Data('${gsmctx}/inspectionplan/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b><s:text name="导出"/></span></span></button>
					</security:authorize>
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="dynamicMeasurementInspectionPlan" url="${gsmctx}/inspectionplan/list-over-datas.htm" code="MEASUREMENT_INSPECTION_PLAN" pageName="page" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div> 
</body>
</html>