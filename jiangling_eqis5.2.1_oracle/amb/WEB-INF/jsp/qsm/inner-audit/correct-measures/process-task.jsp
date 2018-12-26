<%@page import="com.ambition.qsm.entity.SignMeasureItem"%>
<%@page import="com.ambition.qsm.entity.SignCompleteItem"%>
<%@page import="com.ambition.qsm.entity.SignReasonItem"%>
<%@page import="com.ambition.qsm.entity.CorrectMeasuresItem"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>客诉抱怨通知单</title>
<%@include file="/common/meta.jsp"%>
<!--上传js-->
<script type="text/javascript"
	src="${ctx}/widgets/swfupload/swfupload.js"></script>
<script type="text/javascript"
	src="${ctx}/widgets/swfupload/handlers.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/custom.tree.js"> </script>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"
	type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="${ctx}/widgets/workflowEditor/swfobject.js"></script>
<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
<!-- 表单和流程常用的方法封装 -->
<script type="text/javascript" src="${ctx}/js/workflow-form-0.9.js"></script>
<c:set var="actionBaseCtx" value="${qsmctx}/inner-audit/correct-measures" />
<script type="text/javascript">
	isUsingComonLayout=false;
	var hasInitHistory = false;
	$(document).ready(function() {
		//初始化表单元素,格式如
		var initParams = {
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
			inputformortaskform:'taskform',//表单类型,taskform:流程办理界面,inputform:普通表单页面
			fieldPermissionStr:'${fieldPermission}'//字符串格式的字段权限
		};
		editControl();
		$initForm(initParams);		
		$( "#tabs" ).tabs({
			show: function(event, ui) {
				if(ui.index==1&&!hasInitHistory){
					changeViewSet("history","${id}");
					hasInitHistory = true;
				}
			}
		});
		setTimeout(function(){
			$("#opt-content").height($(window).height()-70);
		},500);
 });
	function editControl(){
		var loginName=$("#loginName").val();
		$("tr[zbtr1=true]").find(":input[item=true]").each(function(index,obj){
			var login=$(obj).attr("login");
			var id=obj.id;
			if(loginName!=login){								
				var a=id.split("_")[0];
				$(obj).attr("disabled","disabled");
				var button = $("#"+a+"_attachment").parent().children()[1];
				$(button).attr("disabled","disabled");
			}	
		});
		
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
	</script>
</head>

<body>
	<div class="opt-body">
		<aa:zone name="main">
			<aa:zone name="btnZone">
				<div class="opt-btn">
					<wf:workflowButtonGroup taskId="${taskId}"></wf:workflowButtonGroup>
					<s:if test="task.active==0&&returnableTaskMaps.size>0">
						<button class='btn' type="button" id="_task_button"
							onclick="showIdentifiersDiv();">
							<span><span><b class="btn-icons btn-icons-unbo"></b>驳回到</span></span>
						</button>
					</s:if>
					<span id="message1"
						style="color: red; padding-left: 4px; font-weight: bold;">${message1}</span>
				</div>
			</aa:zone>
			<div style="display: none; color: red;" id="message">
				<s:actionmessage theme="mytheme" />
			</div>
			<div id="opt-content" class="form-bg">
				<form id="defaultForm1" name="defaultForm1" action="">
					<input type="hidden" name="id" id="id" value="${id}" />
					<input name="taskId" id="taskId" value="${taskId}" type="hidden" />
					<input id="selecttacheFlag" type="hidden" value="true" />
				</form>
				<div id="tabs">
					<ul>
						<li><a href="#tabs-1">表单信息</a></li>
						<li><a href="#tabs-2"
							onclick="changeViewSet('history',${id});">流转历史</a></li>
					</ul>
					<div id="tabs-1" style="background: #ECF7FB;">
						<aa:zone name="viewZone">
							<form id="inputForm" name="inputForm" method="post">
								<jsp:include page="input-form.jsp" />
								<%@ include file="process-form.jsp"%>
							</form>
						</aa:zone>
					</div>
					<div id="tabs-2"></div>
				</div>
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
		</aa:zone>
	</div>
</body>
</html>