<%@page import="com.ambition.ecm.entity.EcrReportDetail"%>
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
	<c:set var="actionBaseCtx" value="${ecmctx}/ecr" />
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
				ecrReportDetails:{
					entityClass:"<%=EcrReportDetail.class.getName()%>"//子表1实体全路径
				}
			}
		});
		$(":input[isLogin]").bind("click",function(){
			selectObj("选择人",$("#"+this.id).prev().attr("name"),this.id,"loginName");
		});
/* 		$("input[name^='operationTime']").each(function(index,obj){
			$(obj).datepicker({changeMonth:true,changeYear:true});
		}); */
		$("input[name^='marketSignChange']").each(function(index,obj){
			$(obj).datepicker({changeMonth:true,changeYear:true});
		});
		$("tr[lanuch]").each(function(index,obj){
			if($(obj).attr("lanuch")=='${workCode}'){
				addFormValidate('${fieldPermission}', 'defectiveGoodsForm');
			}else{
				$(obj).find(":input").attr("disabled","disabled");
			}
		});
		$("a").each(function(index,obj){
			if($(obj).attr("lanuch")!='${workCode}'){
				$(obj).removeAttr("onclick");
			}
		});
		checkboxChange();
		$.clearMessage(3000);
	 });
	//选择检验人员
 	function selectObj(title,hiddenInputId,showInputId,treeValue){
		var acsSystemUrl = "${ctx}";
		popTree({ title : title,
			innerWidth:'400',
			treeType:"MAN_DEPARTMENT_TREE",
			defaultTreeValue:treeValue?treeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId :hiddenInputId,
			showInputId : showInputId,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}});
	}
 	//物料清单弹出框选择
	function checkBomClick(){
		$.colorbox({href:"${mfgctx}/common/product-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择物料"
 		});
 	}
	function setProductValue(datas){
		$("#machineNo").val(datas[0].materielCode);
 	}
	//物料清单弹出框选择
	var objIndex = "";
	function selectBomClick(obj){
		var objId = obj.id;
		objIndex = objId.split("_")[0];
		$.colorbox({href:"${mfgctx}/common/product-bom-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择物料"
 		});
 	}
	function setFullBomValue(datas){
		$("#"+objIndex+"_beforeCode").val(datas[0].materielCode);
		$("#"+objIndex+"_beforeName").val(datas[0].materielName);
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
		window.location.href = '${actionBaseCtx}/export-report.htm?id=${id}';
	}
	function checkboxChange(){
		$("input[label=true]").each(function(index,obj){
			var check=$("#"+obj.id).attr("checked");
			if(check){
				$("#"+obj.id+"_label").attr("style","background-color:yellow");
			}else{
				$("#"+obj.id+"_label").removeAttr("style","background-color:yellow");
			}
		});		
	};	
	var rowId=null;
	function itemNumberClick(obj){
		rowId=obj.id.split("_")[0];
		$.colorbox({href:"${iqcctx}/inspection-base/inspection-bom/bom-inspection-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择物料"
 		});
	}
	function setInspectionBomValue(datas){
		$("#"+rowId+"_beforeCode").val(datas[0].materielCode);
		$("#"+rowId+"_beforeName").val(datas[0].materielName);
 	}	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="_ecr";
		var thirdMenu="_ecr_input";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/ecm-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/ecm-ecr-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<s:if test="taskId>0">
					<wf:workflowButtonGroup taskId="${taskId}"></wf:workflowButtonGroup>
				</s:if>
				<s:else>
					<button class='btn' onclick="javascript:window.location='${ecmctx}/ecr/input.htm';" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					<security:authorize ifAnyGranted="ecm-ecr-save,ecm-ecr-submit-process">
					<button class='btn' type="button" onclick="saveForm();"><span><span><b class="btn-icons btn-icons-save"></b><s:text name='保存'/></span></span></button>  
					<button class='btn' type="button" onclick="submitForm();"><span><span><b class="btn-icons btn-icons-ok"></b><s:text name='提交' /></span></span></button>
					</security:authorize>
				</s:else>
				<s:if test="taskId>0">
					<button class="btn" onclick="viewFormInfo()"><span><span><b class="btn-icons btn-icons-info"></b><s:text name='详情' /></span></span></button>
				</s:if>
				<c:if test="${id>0}">
					<security:authorize ifAnyGranted="MFG_IPQC_IMPROVE_EXPORT_FORM">
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