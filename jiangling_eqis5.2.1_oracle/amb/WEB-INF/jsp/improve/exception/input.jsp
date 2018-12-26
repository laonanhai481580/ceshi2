<%@page import="com.ambition.improve.entity.ImproveReportTeam"%>
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
<c:set var="actionBaseCtx" value="${improvectx}/exception" />
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
			fieldPermissionStr:'${fieldPermission}'//字符串格式的字段权限
		});
		$.clearMessage(3000);
	 });

 	//选择部门
 	function _selectDept(showInputId,hiddenInputId,treeValue){
 		if(!_ambWorkflowFormObj.webBaseUrl){
 			alert("workflow-ofrm.js _selectDept方法提示:初始化选择部门时项目地址未指定!");
 			return;
 		}
 		var zTreeSetting={
 				leaf: {
 					enable: false,
 					multiLeafJson:  "[{'name':'部门树','type':'DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"code\":\"code\",\"shortTitle\":\"shortTitle\"}','showValue':'{\"name\":\"name\"}'}]"
 				},
 				type: {
 					treeType: "DEPARTMENT_TREE",
 					showContent:"[{'id':'id','code':'code','name':'name'}]",
 					noDeparmentUser:false,
 					onlineVisible:true
 				},
 				data: {
 					treeNodeData: "id,name,shortTitle",
// 						chkStyle:"checkbox",
 					chkboxType:"{'Y' : 'ps', 'N' : 'ps' }",
 					departmentShow:''
 				},
 				view: {
 					title: "部门树",
 					width: 300,
 					height:400,
 					url:webRoot
 				},
 				feedback:{
 					enable: true,
 			                showInput:"showInput", 
 			                showThing:"{'name':'name'}",
 			                hiddenInput:"hiddenInput",
 			                hiddenThing:"{'id':'id'}",
 			                append:false
 				},
 				callback: {
 					onClose:function(api){
 						$("#"+showInputId).val(ztree.getDepartmentName());
 						//$("#"+hiddenInputId).val(ztree.getDepartmentShortTitle());
 					}
 				}		
 				};
 			    popZtree(zTreeSetting);
 	}
 	//选择用户
 	function _selectUser(showInputId,hiddenInputId,treeValue,multiple){
 		if(!_ambWorkflowFormObj.webBaseUrl){
 			alert("workflow-ofrm.js _selectUser方法提示:初始化选择用户时项目地址未指定!");
 			return;
 		}
 		var data = {
 				treeNodeData: "loginName,name",
 				chkboxType:"{'Y' : 'ps', 'N' : 'ps' }",
 				departmentShow:''
 			};
 		if(multiple=="true"){
 			data = {
 					treeNodeData: "loginName,name",
 					chkStyle:"checkbox",
 					chkboxType:"{'Y' : 'ps', 'N' : 'ps' }",
 					departmentShow:''
 				}
 		}
 		var zTreeSetting={
 				leaf: {
 					enable: false,
 					multiLeafJson:  "[{'name':'用户树','type':'MAN_DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"loginName\":\"loginName\",\"code\":\"code\",\"cardNumber\":\"cardNumber\"}','showValue':'{\"name\":\"name\"}'}]"
 				},
 				type: {
 					treeType: "MAN_DEPARTMENT_TREE",
 					showContent:"[{'id':'id','code':'code','name':'name'}]",
 					noDeparmentUser:false,
 					onlineVisible:true
 				},
 				data: data,
 				view: {
 					title: "用户树",
 					width: 300,
 					height:400,
 					url:webRoot
 				},
 				feedback:{
 					enable: true,
 			                showInput:"showInput", 
 			                showThing:"{'name':'name'}",
 			                hiddenInput:"hiddenInput",
 			                hiddenThing:"{'loginName':'loginName'}",
 			                append:false
 				},
 				callback: {
 					onClose:function(api){
 						if(multiple=="true"){
 							$("#"+hiddenInputId).val(ztree.getLoginNames());
 							$("#"+showInputId).val(ztree.getNames());
 							
 						}else{
 							$("#"+hiddenInputId).val(ztree.getLoginName());
 							$("#"+showInputId).val(ztree.getName());
 							
 						}
 					}
 				}		
 				};
 			    popZtree(zTreeSetting);
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
		window.location.href = '${improvectx}/exception/export-report.htm?id=${id}';
	}
	
	</script>
</head>

<body onload="getContentHeight();">
	<script type="text/javascript">
		var secMenu="8d_report";
		var thirdMenu="exception_report_input";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/improve-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/improve-8d-report-third-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<s:if test="taskId>0">
						<wf:workflowButtonGroup taskId="${taskId}"></wf:workflowButtonGroup>
					</s:if>
					<s:else>
						<security:authorize ifAnyGranted="IMP_EXCEPTION_REPORT_SAVE">
						<%--  <button class='btn' type="button" onclick="saveForm();"><span><span><b class="btn-icons btn-icons-save"></b><s:text name='保存'/></span></span></button>  --%>
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
					<s:if test="task.active==0&&returnableTaskNames.size>0">
						<button class='btn' type="button" id="_task_button"
							onclick="showIdentifiersDiv();">
							<span><span><b class="btn-icons btn-icons-unbo"></b>驳回</span></span>
						</button>
					</s:if>
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
						 <security:authorize ifAnyGranted="IMP_EXCEPTION_REPORT_EXPORT_FORM">
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