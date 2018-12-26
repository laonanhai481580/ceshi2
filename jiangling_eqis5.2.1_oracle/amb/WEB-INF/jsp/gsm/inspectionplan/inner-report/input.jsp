<%@page import="com.ambition.gsm.entity.CheckReportItem"%>
<%@page import="com.ambition.gsm.entity.CheckReportDetail"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@ include file="/common/meta.jsp"%>
<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"
	type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js"
	type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/js/lcbh.js"></script>
<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
<!-- 表单和流程常用的方法封装 -->
<script type="text/javascript" src="${ctx}/js/workflow-form-0.9.js"></script>
<c:set var="actionBaseCtx" value="${gsmctx}/inspectionplan/inner-report" />
<%@ include file="input-script.jsp" %>
<script>
$(document).ready(function(){
	//初始化表单元素
	$initForm({
		webBaseUrl:'${ctx}',//后台执行的Action的前缀名,如:http://localhost:8080/amb/qrqc
		actionBaseUrl : '${actionBaseCtx}',//项目的前缀地址,如:http://localhost:8080/amb
		formId:'inputForm',//表单ID
		objId:'${id}',//数据库对象的ID
		taskId:'${taskId}',//任务ID
// 		children:{
// 			checkReportDetails:{
<%-- 				entityClass:"<%=CheckReportDetail.class.getName()%>"//子表1实体全路径 --%>
// 			},
// 			checkReportItems:{
<%-- 				entityClass:"<%=CheckReportItem.class.getName()%> "//子表2实体全路径 --%>
// 			}
// 		},
		inputformortaskform:'inputform',//表单类型,taskform:流程办理界面,inputform:普通表单页面
		fieldPermissionStr:'${fieldPermission}'//字符串格式的字段权限
	});
	$.clearMessage(3000);
});	

function goback(){
	window.location="${actionBaseCtx}/list.htm";
}
function checkDateChange(obj){
	var checkDate=obj.value;
	var frequency=$("#frequency").val();
		$.post("${actionBaseCtx}/check-date-change.htm?checkDate="+checkDate+"&frequency="+frequency,function(result){
			if(result[0]){
				var date=result[0].date;
				$("#nextCheckDate").val(date);
			}
		},'json');
}

//选择部门
function selectDep(obj){
	var acsSystemUrl = "${ctx}";
	popTree({ title :'选择部门',
		innerWidth:'400',
		treeType:'DEPARTMENT_TREE',
		defaultTreeValue:'id',
		leafPage:'false',
		multiple:'false',
		hiddenInputId:obj,
		showInputId:obj,
		acsSystemUrl:acsSystemUrl,
		callBack:function(){}
	});
}
function checkManClick(title,showInputId,hiddenInputId,multiple,defaultTreeValue){
	var acsSystemUrl = '${ctx}';
	popZtree({
        leaf: {
            enable: false,
            multiLeafJson:"[{'name':'用户树','type':'MAN_DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"loginName\":\"loginName\"}','showValue':'{\"name\":\"name\"}'}]"
        },
        type: {
            treeType: "MAN_DEPARTMENT_TREE",
            showContent:"[{'id':'id','loginName':'loginName','name':'name'}]",
            noDeparmentUser:true,
            onlineVisible:true
        },
        data: {
            treeNodeData:"id,loginName,name",
            chkStyle:"",
            chkboxType:"{'Y':'ps','N':'ps'}",
            departmentShow:""
        },
        view: {
            title: title,
            width: 400,
            height:400,
            url:acsSystemUrl
        },
        feedback:{
            enable: true,
            showInput:showInputId,
            showThing:"{'name':'name'}",
            hiddenInput:hiddenInputId,
            hiddenThing:"{'id':'id'}",
            append:false
        },
        callback: {
            onClose:function(api){
                if(hiddenInputId){
                    var currentClickNodeData = api.single.getCurrentClickNodeData();
                    var user = $.parseJSON(currentClickNodeData);
                    $("#"+hiddenInputId).val(user.loginName);
                }
            }
        }
    });
}
//选择标准件
var selectIndex="";
var selectIndex1="";
function standardItemClick(obj){
	selectIndex = obj.id;
	selectIndex1=obj.parentNode.parentNode;
	var measurementName=$("#measurementName").val();
	if(!measurementName){
		alert("请先填写仪器名称！");
		return;
	}
	var url = '${gsmctx}/base/equipment-standard/standard-item-multi-select.htm?measurementName='+measurementName;
		$.colorbox({href:url,iframe:true, 
		innerWidth:$(window).width()<900?$(window).width()-50:900, 
		innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"选择标准件"
		});
}

//选择校准项目
function checkDetailClick(obj){
	selectIndex = obj.id;
	selectIndex1=obj.parentNode.parentNode;
	var measurementName=$("#measurementName").val();
	if(!measurementName){
		alert("请先填写仪器名称！");
		return;
	}
	var measurementSpecification=$("#measurementSpecification").val();
	if(!measurementSpecification){
		alert("请先填写规格型号！");
		return;
	}
	var manufacturer=$("#manufacturer").val();
	if(!manufacturer){
		alert("请先填写制造厂商！");
		return;
	}
	var url = '${gsmctx}/base/check-standard/check-detail-select.htm?measurementName='+measurementName+'&measurementSpecification='+measurementSpecification+'&manufacturer='+manufacturer;
		$.colorbox({href:url,iframe:true, 
		innerWidth:$(window).width()<900?$(window).width()-50:900, 
		innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"选择校验项目"
		});
}
function setDetailValue(datas){
	var idFirst = selectIndex.split("_")[0];
	var a=idFirst.slice(0,2);
	var b=idFirst.slice(2);
	for(var i=0;i<datas.length;i++){
		$("#" + a + b + "_itemName").val(datas[i].itemName);
		$("#" + a + b + "_standardValue").val(datas[i].standardValue);
		$("#" + a + b + "_allowableError").val(datas[i].allowableError);
		b++;
		if(i!=datas.length-1){
			addRowHtml(selectIndex1);
		}
	}
}

function setItemValue(datas){
	var idFirst = selectIndex.split("_")[0];
	var a=idFirst.slice(0,2);
	var b=idFirst.slice(2);
	for(var i=0;i<datas.length;i++){
		$("#" + a + b + "_standardNo").val(datas[i].standardNo);
		$("#" + a + b + "_standardName").val(datas[i].standardName);
		$("#" + a + b + "_validityDate").val(datas[i].validityDate);
		$("#" + a + b + "_certificateNo").val(datas[i].certificateNo);
		b++;
		if(i!=datas.length-1){
			addRowHtml(selectIndex1);
		}
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
	window.location.href = '${actionBaseCtx}/download-report.htm?id=${id}';
}
	
</script>
</head>

<body onload="getContentHeight();">
	<script type="text/javascript">
		var secMenu="inspectionplan";
		var thirdMenu="_report_input";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>	
	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-inspection-plan-menu.jsp" %>
	</div>
<%-- 
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<button id="saveBtn" class="btn" type="button" onclick="saveForm();"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				<button class='btn' type="button" onclick="goback();"><span><span><b class="btn-icons btn-icons-undo"></b>返回</span></span></button>
				<span style="color:red;" id="message"><s:actionmessage theme="mytheme"/></span>
				<security:authorize ifAnyGranted="gsm_inspectionplan_inner-report-export-form">
						<button class='btn' onclick="exportForm();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>
			</div>
			<div><iframe id="iframe" style="display:none;"></iframe></div>
			<div id="opt-content" style="text-align: center;">
				<form id="inputform" name="inputform" method="post" action="">
					 <%@ include file="input-form.jsp"%>
				</form>		
			</div>
		</div>
	</div> --%>
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<s:if test="taskId>0">
						<wf:workflowButtonGroup taskId="${taskId}"></wf:workflowButtonGroup>
					</s:if>
					<s:else>
						<security:authorize ifAnyGranted="GSM_INNER_CHECK_REPORT_SAVE">
							 <button class='btn' type="button" onclick="saveForm();">
								<span><span><b class="btn-icons btn-icons-save"></b>
									<s:text name='暂存' /></span></span>
							</button> 
							<button class='btn' type="button" onclick="submitForm();">
								<span><span><b class="btn-icons btn-icons-ok"></b>
									<s:text name='提交' /></span></span>
							</button>
						</security:authorize>
					</s:else>
					<s:if test="taskId>0">
						<button class="btn" onclick="viewFormInfo()">
							<span><span><b class="btn-icons btn-icons-info"></b>
								<s:text name='详情' /></span></span>
						</button>
					</s:if>
<%-- 					<s:if test="task.active==0&&returnableTaskNames.size>0">
						<button class='btn' type="button" id="_task_button"
							onclick="showIdentifiersDiv();">
							<span><span><b class="btn-icons btn-icons-unbo"></b>驳回</span></span>
						</button>
					</s:if> --%>
					<div id="flag" style="display: none;" onmouseover='show_moveiIdentifiersDiv();' onmouseout='hideIdentifiersDiv();'>
						<ul style="width: 240px;">
							 <s:iterator var="returnTaskName" value="returnableTaskNames">
								 <li onclick="backToTask(this,'${actionBaseCtx}','${taskId}');" style="cursor:pointer;" title="驳回到 ${returnTaskName}" taskName="${returnTaskName}">
								  ${returnTaskName}
								 </li>
							 </s:iterator>
						</ul>
					</div>
					<c:if test="${id>0}">
						 <security:authorize ifAnyGranted="gsm_inspectionplan_inner-report-export-form">
							<button class='btn' id="print" type="button"
								onclick="exportForm();">
								<span><span><b class="btn-icons btn-icons-print"></b>导出</span></span>
							</button>
						</security:authorize> 
					</c:if>
				</div>
				<div>
					<iframe id="iframe" style="display:none;"></iframe>
				</div>
				<div id="opt-content" class="form-bg">
					<div style="color: red;" id="message">
						<s:actionmessage theme="mytheme" />
					</div>
					<div>
						<form id="defaultForm1" name="defaultForm1" action="">
							<input type="hidden" name="id" id="id" value="${id}" />
							<input name="taskId" id="taskId" value="${taskId}" type="hidden" />
							<input id="selecttacheFlag" type="hidden" value="true" />
						</form>
					</div>
					<div>
						<s:form id="inputForm" name="inputForm" method="post" action="">
							<jsp:include page="input-form.jsp" />
							<s:if test="taskId>0">
								<%@ include file="process-form.jsp"%>
							</s:if>
						</s:form>
					</div>
				</div>
			</aa:zone>
		</div>
	</div>	
</body>
</html>