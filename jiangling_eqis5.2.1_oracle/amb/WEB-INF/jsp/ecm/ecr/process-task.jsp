<%@page import="com.ambition.ecm.entity.EcrReportDetail"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>客诉抱怨通知单</title>
<%@include file="/common/meta.jsp"%>
<!--上传js-->
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/widgets/swfupload/swfupload.js"></script>
<script type="text/javascript" src="${ctx}/widgets/swfupload/handlers.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/custom.tree.js"> </script>
<script type="text/javascript" src="${ctx}/widgets/workflowEditor/swfobject.js"></script>
<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<!-- 表单和流程常用的方法封装 -->
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
	isUsingComonLayout=false;
	var hasInitHistory = false;
	$(document).ready(function() {
		//初始化表单元素
		$initForm({
			webBaseUrl:'${ctx}',//后台执行的Action的前缀名,如:http://localhost:8080/amb/qrqc
			actionBaseUrl : '${actionBaseCtx}',//项目的前缀地址,如:http://localhost:8080/amb
			formId:'inputForm',//表单ID
			objId:'${id}',//数据库对象的ID
			taskId:'${taskId}',//任务ID
			inputformortaskform:'taskform',//表单类型,taskform:流程办理界面,inputform:普通表单页面
			fieldPermissionStr:'${fieldPermission}',//字符串格式的字段权限
			children:{
				ecrReportDetails:{
					entityClass:"<%=EcrReportDetail.class.getName()%>"//子表1实体全路径
				}
			}
		});
		$( "#tabs" ).tabs({
			show: function(event, ui) {
				if(ui.index==1&&!hasInitHistory){
					changeViewSet("history","${id}");
					hasInitHistory = true;
				}
			}
		});
		$("tr[lanuch]").each(function(index,obj){
			if($(obj).attr("lanuch")=='${workCode}'){
				addFormValidate('${fieldPermission}', 'defectiveGoodsForm');
			}else{
				$(obj).find(":input").attr("disabled","disabled");
				$("a").removeAttr("onclick");
			}
		});
		setTimeout(function(){
			$("#opt-content").height($(window).height()-70);
		},500);
 });
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
<!-- 					<input id="selecttacheFlag" type="hidden" value="true" /> -->
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
							<input type="hidden" id="id" name="id" value="${id}"/>
							<input name="taskId" id="taskId" value="${taskId}" type="hidden" />
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