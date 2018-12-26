<%@page import="com.ambition.supplier.entity.InspectionReport"%>
<%@page import="com.norteksoft.product.util.PropUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp" %>
<script type="text/javascript" src="${ctx}/widgets/swfupload/swfupload.js"></script>
<script type="text/javascript" src="${ctx}/widgets/tablednd/jquery.tablednd.js"></script>
	<script type="text/javascript">
		//重写保存后的方法
		function $successfunc(response){
			var result = eval("(" + response.responseText + ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		//重写编辑后的方法
		function $oneditfunc(rowId){
			
		}
		//导出
		function exports(){
			$("#contentForm").attr("action","${supplierctx}/admittance/inspection-report/export.htm");
			$("#contentForm").submit();
		}
		//生成问题点报告
		function createQuestionReport(){
			var ids = $("#inspectionReportList").jqGrid("getGridParam","selarrrow");
			if(ids.length == 0){
				alert("请选择考察报告");
			}else if(ids.length>1){
				alert("只能选择一条考察报告!");
			}else{
				var current = 0;
				var dd = setInterval(function(){
					current++;
					var str = '';
					for(var i=0;i<(current%3);i++){
						str += "...";
					}
					$("#message").html("正在下载供应商问题点报告,请稍候..." + str);
				}, 500);
				$("#message").html("正在下载供应商问题点报告,请稍候...");
				$("#iframe").bind("readystatechange",function(){
					clearInterval(dd);
					$("#message").html("");
					$("#iframe").unbind("readystatechange");
				}).attr("src","${supplierctx}/admittance/inspection-report/create-question-report.htm?id=" + ids[0]);
			}
		}
		function editInspectionReport(id){
			$("button").attr("disabled",true);
			$(".small-button-bg").attr("disabled",true);
			$("#opt-content").attr("disabled",true);
	 		$("#message").html("正在转到表单编辑页面,请稍候... ...");
			window.location = '${supplierctx}/admittance/inspection-report/input.htm?id=' + id;
		}
		function operateFormatter(value,options,rowObj){
			var operations = "<div style='text-align:center;'>";
<%-- 			if(rowObj.inspectionState == '<%=InspectionReport.STATE_DEFAULT%>'){ --%>
				operations += "<a class=\"small-button-bg\" href=\"javascript:editInspectionReport("+value+")\" title='编辑考察报告'><span class=\"ui-icon ui-icon-note\" style='cursor:pointer;'></span></a>&nbsp;"; 
// 			}
			operations += "<a class=\"small-button-bg\" href=\"javascript:void(0);viewDetailInfo("+value+");\" title='查看详细信息'><span class=\"ui-icon ui-icon-info\" style='cursor:pointer;'></span></a>";
			if(rowObj.inspectionState == '<%=InspectionReport.STATE_PASS%>'&&rowObj.inspectionResult=='<%=InspectionReport.RESULT_PASS%>'){
				operations += "&nbsp;<a class=\"small-button-bg\" href=\"javascript:void(0);startSampleAppraisal("+value+");\" title='启动样件鉴定'><span class=\"ui-icon ui-icon-triangle-1-e\" style='cursor:pointer;'></span></a>";
			}
			operations += "</div>";
			return operations;
		}
		
		function improveOperateFormatter(value){
			return "<div style='text-align:center;' title='发起改进'><a class=\"small-button-bg\" onclick='submitImprove("+value+");'><span class=\"ui-icon ui-icon-info\" style='cursor:pointer;'></span></a></div>";
		}
		function submitImprove(id){
	 		var url='${improvectx}/correction-precaution/called-input.htm?inspectionReportId='+id;
	 		$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
	 			overlayClose:false,
	 			title:"改进页面",
	 			onClosed:function(){
	 			}
	 		});
	 	}
		
		function viewDetailInfo(value){
			var url='${supplierctx}/admittance/inspection-report/view.htm?id='+value;
	 		$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
	 			overlayClose:false,
	 			title:"查看考察报告详情",
	 			onClosed:function(){
	 			}
	 		});
		}
		function startSampleAppraisal(id){
			$("button").attr("disabled",true);
			$(".small-button-bg").attr("disabled",true);
			$("#opt-content").attr("disabled",true);
	 		$("#message").html("正在生成样件鉴定报告,请稍候... ...");
			window.location = "${supplierctx}/admittance/sample-appraisal-report/input.htm?inspectionReportId=" + id;
		}
		function addNew(){
			$("button").attr("disabled",true);
			$(".small-button-bg").attr("disabled",true);
			$("#opt-content").attr("disabled",true);
			$("#message").html("正在转到供应商考察报告编辑页面,请稍候...");
			window.location='${supplierctx}/admittance/inspection-report/input.htm';
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="admittance";
		var thirdMenu="_admittance_inspection";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-admittance-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" style="line-height:30px;">
				<security:authorize ifAnyGranted="admittance-input">
				<button class="btn" onclick="addNew();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>				
				<security:authorize ifAnyGranted="admittance-delete">
				<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button> 
				</security:authorize>				
				<security:authorize ifAnyGranted="admittance-list-datas">
				<button class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				</security:authorize>				
				<security:authorize ifAnyGranted="admittance-export">
				<button class="btn" onclick="exports();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>				
				<security:authorize ifAnyGranted="admittance-create-report">
				<button class="btn" onclick="createQuestionReport();"><span><span><b class="btn-icons btn-icons-report"></b>生成报告</span></span></button>
				</security:authorize>
				<span style="margin-left:4px;color:red;" id="message"></span>
			</div>
			<div><iframe id="iframe" style="display:none;"></iframe></div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post"  action="">
					<grid:jqGrid gridId="inspectionReportList" url="${supplierctx}/admittance/inspection-report/list-datas.htm" code="SUPPLIER_INSPECTION_REPORT"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js">
</script><script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>