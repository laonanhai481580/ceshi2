<%@page import="com.ambition.iqc.entity.OrtExperimentalItem"%>
<%@page import="com.ambition.iqc.entity.ExperimentalDelegation"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@page import="com.ambition.iqc.entity.IncomingInspectionActionsReport"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>	
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script language="javascript" src="${ctx}/widgets/lodop/LodopFuncs.js"></script> 
	<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
	<script type="text/javascript" src="${ctx}/js/workflow-form-0.9.js"></script>
	<c:set var="actionBaseCtx" value="${iqcctx}/experimental-delegation" />
	<style type="text/css">
		.tableTd{
			text-align: center !important;;
		}
		ul{
			margin:0px;
			padding:0px;
		}
		li{
			margin:0px;
			padding:0px;
			text-decoration: none;
			list-style: none;
		}
	</style>
	<script type="text/javascript">
	$(document).ready(function(){
		//初始化表单元素
		$initForm({
			webBaseUrl:'${ctx}',//后台执行的Action的前缀名,如:http://localhost:8080/amb/qrqc
			actionBaseUrl : '${actionBaseCtx}',//项目的前缀地址,如:http://localhost:8080/amb
			formId:'inputForm',//表单ID
			objId:'${id}',//数据库对象的ID
			taskId:'${taskId}',//任务ID
			inputformortaskform:'inputform',//表单类型,taskform:流程办理界面,inputform:普通表单页面
			fieldPermissionStr:'${fieldPermission}',//字符串格式的字段权限
			children:{
				ortItems:{
					entityClass:"<%=OrtExperimentalItem.class.getName()%>"//子表1实体全路径
				}
			},
		});
		//方法多选
		var testHsfs = '${testHsf}'.split(",");
		$("input[name='testHsf']").removeAttr("disabled");
		$(":input[name=testHsf]").each(function(index,obj){
			var checked = false;
			for(var i=0;i<testHsfs.length;i++){
				if($.trim(obj.value)==$.trim(testHsfs[i])){
					checked = true;
					break;
				}
			}
			if(checked){
				$(obj).attr("checked","checked");
			}else{
				$(obj).removeAttr("checked");
			}
		});
		radioChange();
		$("input[name='testHsf']").attr("disabled","disabled");
		$.clearMessage(3000);
	 });
	function updateDisabledStauts(obj){
		var objVal=$(obj).val();
		if(objVal=="hsf"){
			$("td[name=ort]").next().children().find(":input").attr("disabled","disabled");
			$("td[name=hsf]").next().children().find(":input").removeAttr("disabled");
			$("a[name]").hide();
		}else if(objVal=="ort"){
			$("td[name=hsf]").next().children().find(":input").attr("disabled","disabled");
			$("td[name=ort]").next().children().find(":input").removeAttr("disabled");
			$("a").show();
		}
	}
	function radioChange(){
		var str = $("#currentActivityName").val();
		var val=$("input[name='testProject']:checked").val();
		if(!val){
			$("#testOrt").val("");
			$("#testOrt").attr("disabled","disabled");
			$("#testOther").val("");
			$("#testOther").attr("disabled","disabled");
			$("#testHsf").val("");
			$("input[name='testHsf']").attr("disabled","disabled");			
		}
		if(val=="HSF"){
			$("#testOrt").val("");
			$("#testOrt").attr("disabled","disabled");
			$("#testOther").val("");
			$("#testOther").attr("disabled","disabled");
			$("input[name='testHsf']").removeAttr("disabled");
		}
		if(val=="ORT"){
			$("input[name='testHsf']").each(function(index,obj){
				iobj = $(obj);
				iobj.attr("checked",false);
			});
			$("input[name='testHsf']").attr("disabled","disabled");
			$("#testOther").attr("disabled","disabled");
			$("#testOther").val("");
			$("#testOrt").removeAttr("disabled");
			
		}
		if(val=="其他"){
			$("input[name='testHsf']").each(function(index,obj){
				iobj = $(obj);
				iobj.attr("checked",false);
			});
			$("input[name='testHsf']").attr("disabled","disabled");
			$("#testOrt").attr("disabled","disabled");
			$("#testOrt").val("");
			$("#testOther").removeAttr("disabled");
		}
	}
	/**
	导出表单
	*	
	*/
	function exportForm(){
		var id = '${id}';
		if(!id){
			alert("请先保存!");
			return;
		}
		window.location.href = '${actionBaseCtx}/export-form.htm?id=${id}';
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="_experimental_delegation";
		var thirdMenu="_experimental_delegation_input";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-experimental-delegation-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<s:if test="taskId>0">
					<wf:workflowButtonGroup taskId="${taskId}"></wf:workflowButtonGroup>
				</s:if>
				<s:else>
					<button class='btn' onclick="javascript:window.location='${iqcctx}/experimental-delegation/input.htm';" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					<security:authorize ifAnyGranted="iqc-experimental-delegation-save,iqc-experimental-delegation-submit-process">
					<button class='btn' type="button" onclick="saveForm();"><span><span><b class="btn-icons btn-icons-save"></b><s:text name='保存'/></span></span></button>  
					<button class='btn' type="button" onclick="submitForm();"><span><span><b class="btn-icons btn-icons-ok"></b><s:text name='提交' /></span></span></button>
					</security:authorize>
				</s:else>
				<s:if test="taskId>0">
					<button class="btn" onclick="viewFormInfo()"><span><span><b class="btn-icons btn-icons-info"></b><s:text name='详情' /></span></span></button>
				</s:if>
				<c:if test="${id>0}">
				<security:authorize ifAnyGranted="iqc-experimental-delegation-export-form">
						<button class='btn' id="print" type="button" onclick="exportForm();"><span><span><b class="btn-icons btn-icons-print"></b>导出</span></span></button>
				</security:authorize> 
				</c:if>
			</div>
			<div id="opt-content" style="text-align: center;">
				<form action="" method="post" id="inputForm" name="inputForm">
					<jsp:include page="input-form.jsp" />
				</form>
			</div>
		</div>
	</div>
</body>
</html>