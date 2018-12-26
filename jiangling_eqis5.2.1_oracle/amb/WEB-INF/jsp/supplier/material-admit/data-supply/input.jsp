<%@page import="com.ambition.supplier.entity.ProductPartReport"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.ambition.supplier.entity.ProductPartReport"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>企业管理效率促进专家</title>
	<%@ include file="/common/meta.jsp" %>
<%-- 	<%@ include file="/common/common-js.jsp" %> --%>
	<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
	<!-- 表单和流程常用的方法封装 -->
	<script type="text/javascript" src="${ctx}/js/workflow-form-0.9.js"></script>
<!-- 	流程驳回 -->
	<script type="text/javascript" src="${ctx}/js/lcbh.js"></script>
	<c:set var="actionBaseCtx" value="${supplierctx}/material-admit/data-supply"/>
	 <%@ include file="input-script.jsp" %>
	<script type="text/javascript">
		$(document).ready(function(){
			//初始化表单元素
			$initForm({
				webBaseUrl:'${ctx}',//后台执行的Action的前缀名,如:http://localhost:8080/amb/qrqc
				actionBaseUrl : '${actionBaseCtx}',//项目的前缀地址,如:http://localhost:8080/amb
				formId:'workflowForm',//表单ID
				objId:'${id}',//数据库对象的ID
				taskId:'${taskId}',//任务ID
				children:{
					productPartReports:{
						entityClass:"<%=ProductPartReport.class.getName()%>"//子表3实体全路径
					}
				},
				beforeSaveCallback:function(){
					var value = checkRate();
					return value;
				},
				inputformortaskform:'inputform',//表单类型,taskform:流程办理界面,inputform:普通表单页面
				fieldPermissionStr:'${fieldPermission}'//字符串格式的字段权限
			});
			$("#scroll").bind("scroll",function(){
				$("#checkItemsParent").find("div").scrollLeft($("#scroll").scrollLeft());
			});
			$("#checkItemsParent").find("div").bind("scroll",function(){
				$("#scroll").scrollLeft($("#checkItemsParent").find("div").scrollLeft());
			});
			$("#opt-content").bind("scroll",function(){
				checkScrollDiv();
			});
			contentResize();
			function getScrollTop(){
				 return $("#opt-content").height()+23;
			}
			function checkScrollDiv(){
				var scrollTop = getScrollTop();
				var tableTop = $("#checkItemsParent").position().top + $("#checkItemsParent").height()-18;
				if(tableTop<scrollTop){
					$("#scroll").hide();
				}else{
					$("#scroll").show();
				}
			}
			function contentResize(){
				initScrollDiv();
				checkScrollDiv();
			}
			function initScrollDiv(){
				var width = $(".ui-layout-center").width()-30;
				var offset = $("#checkItemsParent").find("div").width(width).offset();
				var contentWidth =  $("#checkItemsParent").find("table").width();
				$("#scroll").width(width).css("top",getScrollTop() + "px").find("div").width(contentWidth);
			}
			$.clearMessage(3000);
			setweigh();
	});
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
		function doUpload() {  
		     var formData = new FormData($( "#uploadForm" )[0]);  
		     $.ajax({  
		          url: 'http://localhost:8080/amb/carmfg/common/upload.htm' ,  
		          type: 'POST',  
		          data: formData,  
		          async: false,  
		          cache: false,  
		          contentType: false,  
		          processData: false,  
		          success: function (returndata) {  
		              alert(returndata);  
		          },  
		          error: function (returndata) {  
		              alert(returndata);  
		          }  
		     });  
		}  
	</script>
	
 
</head>

<body onload="getContentHeight();">
	<script type="text/javascript">
		var secMenu="admit";
		var thirdMenu="dataSupplyInput";
 	</script>
 	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-material-admit-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
			<div class="opt-btn">
				<s:if test="taskId>0">
				  <c:if test="${isCurrent==true}">
					 <wf:workflowButtonGroup taskId="${taskId}"></wf:workflowButtonGroup>
					</c:if>
				</s:if>
				<s:else>
				<security:authorize ifAnyGranted="supplier-material-data-supply-save">
					<button class='btn' type="button" onclick="saveForm();"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
					<button class='btn' type="button" onclick="submitForm();"><span><span><b class="btn-icons btn-icons-ok"></b>提交</span></span></button>
				</security:authorize>
				</s:else>
				<s:if test="taskId>0">
				<button class="btn" onclick="viewFormInfo()"><span><span><b class="btn-icons btn-icons-alert"></b>流转历史</span></span></button>
				</s:if>
				<c:if test="${id>0}">
					<security:authorize ifAnyGranted="supplier-material-data-supply-export-excel">
					<button class='btn' id="print" type="button" onclick="exportForm();"><span><span><b class="btn-icons btn-icons-export"></b><s:text name='导出'/></span></span></button>
					</security:authorize>
<!-- 					驳回按钮 -->
					 <c:if test="${isCurrent==true}">
					 <button  class='btn' type="button" id="_task_button" onclick="showIdentifiersDiv();"><span><span><b class="btn-icons btn-icons-back"></b>驳回</span></span></button>
					</c:if>
					<div id="flag" onmouseover='show_moveiIdentifiersDiv();' onmouseout='hideIdentifiersDiv();'>
						<ul style="width:300px;">
							 <s:iterator var="returnTaskName" value="returnableTaskNames">
								 <li onclick="backToTask(this,'${actionBaseCtx}','${taskId}');" style="cursor:pointer;" title="驳回到 ${returnTaskName}" taskName="${returnTaskName}">
								  ${returnTaskName}
								 </li>
							 </s:iterator>
						</ul>
				    </div>
<!-- 				    驳回按钮 -->
				</c:if>
<%-- 				<button class='btn' onclick="history.back();"><span><span><b class="btn-icons btn-icons-undo"></b><s:text name="common.goBack"/></span></span></button>		 --%>
			     <span style="color:red;" id="message1"></span>
			</div>
			<div><iframe id="iframe" style="display:none;"></iframe></div>
			<div id="opt-content" class="form-bg">
			 <div id="scroll" style="position:absolute;top:0px;left:15px;overflow-y:hidden;overflow-x:auto;height:35px;line-height:35px;display:block;z-index:2;">
					<div style="">&nbsp;</div>
				</div>
				<div style="color:red;" id="message"><s:actionmessage theme="mytheme"/></div>
				<div>
					<form id="defaultForm1" name="defaultForm1"action="">
						<input type="hidden" name="id" id="id" value="${id}" />
						<input name="taskId" id="taskId" value="${taskId}" type="hidden"/>
						<input id="selecttacheFlag" type="hidden" value="true"/>
					</form>
				</div>
				<s:form  id="workflowForm" name="workflowForm" method="post" action="">
					<table class="form-table-without-border" style="width:100%;margin: auto;">
						<caption><h2>GP资料提供</h2></caption>
						<tr>
							<td style="text-left;padding-bottom:4px;">编号:${formNo}</td>
						</tr>
					</table>
					<jsp:include page="input-form.jsp" />
					<c:if test="taskId>0">
						<%@ include file="process-form.jsp"%>
					</c:if>
				</s:form>
			</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>