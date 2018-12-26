<%@page import="com.ambition.qsm.entity.SignReasonItem"%>
<%@page import="com.ambition.qsm.entity.SignMeasureItem"%>
<%@page import="com.ambition.qsm.entity.SignCompleteItem"%>
<%@page import="com.ambition.qsm.entity.CorrectMeasuresItem"%>
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
<script type="text/javascript"
	src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript"
	src="${ctx}/widgets/workflowEditor/swfobject.js"></script>
<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
<!-- 表单和流程常用的方法封装 -->
<script type="text/javascript" src="${ctx}/js/workflow-form-0.9.js"></script>
<c:set var="actionBaseCtx" value="${qsmctx}/inner-audit/correct-measures" />
<script type="text/javascript">

	$(document).ready(function(){
		//初始化表单元素
		$initForm({
			webBaseUrl:'${ctx}',//后台执行的Action的前缀名,如:http://localhost:8080/amb/qrqc
			actionBaseUrl : '${actionBaseCtx}',//项目的前缀地址,如:http://localhost:8080/amb
			formId:'inputForm',//表单ID
			objId:'${id}',//数据库对象的ID
			taskId:'${taskId}',//任务ID
			children:{
				correctMeasuresItems:{
					entityClass:"<%=CorrectMeasuresItem.class.getName()%>"//子表1实体全路径
				},
				signReasonItems:{
					entityClass:"<%=SignReasonItem.class.getName()%>"//子表2实体全路径
				}, 
				signCompleteItems:{
					entityClass:"<%=SignCompleteItem.class.getName()%>"//子表3实体全路径
				}, 
				signMeasureItems:{
					entityClass:"<%=SignMeasureItem.class.getName()%>"//子表4实体全路径
				}, 
			},
			inputformortaskform:'inputform',//表单类型,taskform:流程办理界面,inputform:普通表单页面
			fieldPermissionStr:'${fieldPermission}'//字符串格式的字段权限
		});	
		$.clearMessage(3000);
		editControl();
	 });
	function editControl(){
		var loginName=$("#loginName").val();
		$("tr[zbtr1=true]").find(":input[item=true]").each(function(index,obj){
			var login=$(obj).attr("login");
			if(loginName!=login){
				$(obj).attr("disabled","disabled");
				var id=obj.id;
				var a=id.split("_")[0];
				var button = $("#"+a+"_attachment").parent().children()[1];
				$(button).attr("disabled","disabled");
			}	
		});
		
	}
	//上传附件
	function uploadFiles(showId,hiddenId){
		$.upload({
			appendTo : '#opt-content',
			showInputId : showId,
			hiddenInputId : hiddenId
		});
	}
	//加载附件
	function parseDownloadFiles(arrs){
		for(var i=0;i<arrs.length;i++){
			var hiddenInputId = arrs[i].hiddenInputId;
			var showInputId = arrs[i].showInputId;
			var files = $("#"+hiddenInputId).val();
			if(files&&files.length>0){
		 		$.parseDownloadPath({
					showInputId : showInputId,
					hiddenInputId : hiddenInputId
				});
			}
		}
	}	
	//模糊查询不符合条款
	function searchSet(obj){
		var myId=obj.id;
		var a=myId.split("_")[0];
		$('#'+myId)
		.bind("blur",function(){
			if(!$(this).data("autocomplete").menu.element.is(":visible")){
				var hisSearch = $(this).attr("hisSearch");
				if(this.value !== hisSearch){
					$(this).attr("hisSearch",this.value); 
				}
			}
		})
		.autocomplete({
			minLength: 2,
			delay: 100,
			autoFocus: true, 
			source: function( request, response ) {
					response([{label:'数据加载中...',value:''}]);
					$.post("${qsmctx}/inner-audit/correct-measures/search-clause-name.htm",{searchParams:'{"clauseName":"'+request.term+'"}',label:'name'},function(result){
						response(result);
					},'json');
			},
			focus: function() {
				return false;
			},
			select: function( event, ui ) {
				if(ui.item.label){
					$('#'+myId).attr("hisValue",ui.item.label);
					$('#'+myId).val(ui.item.label);
					$('#'+a+"_systemName").val(ui.item.value);
					this.value = ui.item.label; 
				}else{
					$('#'+myId).attr("hisValue","");
					$('#'+myId).val("");
					$('#'+a+"_systemName").val("");
					this.value = ""; 
				}
				return true;
			},
			close : function(event,ui){
				var cust = $('#'+myId);
				var val=cust.val();
				if(val){//如果当前料号值非空
					var hisValue = cust.attr("hisValue");
					if(!hisValue||hisValue==null){//如果料号没有查询出值，且val不为空，则val为原始输入值，保留原始输入数据
						cust.val(val);
					}else{//如果料号查询出有值，且val非空，则val为查询出的值，保留查询出的数据
						cust.val(hisValue);
					}
				}else{ //如果当前料号值为空
					var hisValue = cust.attr("hisValue");
					if(!hisValue||hisValue==null){//如果料号没有查询出值，且val为空，则val为原始输入值就是空
						cust.val('');
					}else{//如果料号查询出有值，且val空，则val为查询出的值带出的空，保留查询出的数据
						cust.val(hisValue);
					}
				}
				cust.attr("hisValue",'').attr("hisSearch",'');
				$('#'+myId).attr("hisValue",'').attr("hisSearch",'');
			}
		});
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
	</script>
</head>

<body onload="getContentHeight();">
	<script type="text/javascript">
		var secMenu="inner_audit";
		var thirdMenu="_correct_measures_input";
	</script>


	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/qsm-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/qsm-inner-audit-third-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<s:if test="taskId>0">
						<wf:workflowButtonGroup taskId="${taskId}"></wf:workflowButtonGroup>
					</s:if>
					<s:else>
						<security:authorize ifAnyGranted="QSM_CORRECT_MEASURES_SAVE">
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
					<s:if test="task.active==0&&returnableTaskMaps.size>0">
						<button class='btn' type="button" id="_task_button"
							onclick="showIdentifiersDiv();">
							<span><span><b class="btn-icons btn-icons-unbo"></b>驳回到</span></span>
						</button>
					</s:if>
					<c:if test="${id>0}">
						<security:authorize ifAnyGranted="QSM_CORRECT_MEASURES_EXPORT_FORM">
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
							<c:if test="taskId>0">
								<%@ include file="process-form.jsp"%>
							</c:if>
						</s:form>
					</div>
				</div>
			</aa:zone>
			<div id="flag" style="display: none;"
				onmouseover='show_moveiIdentifiersDiv();'
				onmouseout='hideIdentifiersDiv();'>
				<ul style="width: 240px;">
					<s:iterator var="returnableTaskMap" value="returnableTaskMaps">
						<li
							onclick="backToTask('${taskId}','${returnableTaskMap.taskName}','${returnableTaskMap.loginName}');"
							style="cursor: pointer; width: 232px; overflow: hidden;"
							title="驳回到 ${returnableTaskMap.taskName}(${returnableTaskMap.userName})">
							${returnableTaskMap.taskName}(${returnableTaskMap.userName})</li>
					</s:iterator>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>