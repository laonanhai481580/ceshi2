<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${imatrixCtx}/wf/js/workflowTag.js"></script>
	<script type="text/javascript">
		//新建
		function createChnlIncomingInspection(url){
			ajaxSubmit("defaultForm",url,"main",createChnlIncomingInspectionCallback);
		}
		function createChnlIncomingInspectionCallback(){
			validateChnlIncomingInspection();
		}
		//验证
		function validateChnlIncomingInspection(){
			$("#inputForm").validate({
				submitHandler: function() {
					$("#inputForm").ajaxSubmit(function (id){
						$("#id").attr("value",id);
						$("#message").show("show");
						setTimeout('$("#message").hide("show");',3000);
					});
				},
				rules: {
					
				},
				messages: {
					
				}
			});
		}
		
		//修改
		function updateChnlIncomingInspection(url){
			var ids = jQuery("#chnlIncomingInspectionGridId").getGridParam('selarrrow');
			if(ids==""){
				alert("请选择一条数据");
			}else if(ids.length > 1){
				alert("只能选择一条数据");
			}else if(ids.length == 1){
				ajaxSubmit("defaultForm",url+"?id="+ids[0],"main",createChnlIncomingInspectionCallback);
			}
		}
		
		//删除
		function deleteChnlIncomingInspection(url){
			var ids = jQuery("#chnlIncomingInspectionGridId").getGridParam('selarrrow');
			if(ids.length<=0){
				alert("请选择数据");
			}else {
				aa.submit('defaultForm', url+'?ids='+ids.join(','), 'main');
			}
		}

		function saveChnlIncomingInspection(url){
			$("#inputForm").attr("action",url);
			$("#inputForm").submit();
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="inspectionReport";
		var thirdMenu="chnlIncomingInspection";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-inspection-report-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post"  action=""></form>
			<aa:zone name="main">
				<div class="opt-btn">
					<button  class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
<%-- 					<button class="btn" onclick="createChnlIncomingInspection('${ctx}/iqc/chnlIncomingInspection-input.htm');"><span><span>新建</span></span></button> --%>
<%-- 					<button class="btn" onclick="updateChnlIncomingInspection('${ctx}/iqc/chnlIncomingInspection-input.htm');"><span><span>修改</span></span></button> --%>
<%-- 					<button class="btn" onclick="deleteChnlIncomingInspection('${ctx}/iqc/chnlIncomingInspection-delete.htm');"><span><span >删除</span></span></button> --%>
				</div>
				<div id="message"><s:actionmessage theme="mytheme" /></div>	
				<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
				<div id="opt-content" >
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="chnlIncomingInspectionGridId" url="${iqcctx}/inspection-report/chnlIncomingInspection-listDatas.htm" submitForm="defaultForm" code="IQC_CHNL_INCOMING_INSPECTION" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
</html>