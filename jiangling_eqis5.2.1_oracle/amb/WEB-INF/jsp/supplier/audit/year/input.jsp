<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
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
	<c:set var="actionBaseCtx" value="${supplierctx}/audit/year"/>
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
				},
				beforeSaveCallback:function(){
				},
				inputformortaskform:'inputform',//表单类型,taskform:流程办理界面,inputform:普通表单页面
				fieldPermissionStr:'${fieldPermission}'//字符串格式的字段权限
			});
			$.clearMessage(3000);
	});
	/* $(document).ready(function(){
		if('${saveSucc}'=='true'){
			if(window.parent){
				window.parent.$.colorbox.close();
				window.parent.$("#supplier").trigger("reloadGrid");
				return;				
			}
		}
		$("input[name='firstCheckDate']").datepicker({
			changeMonth : true,
			changeYear : true
		});
		$("input[name='secondCheckDate']").datepicker({
			changeMonth : true,
			changeYear : true
		});
		$("input[name='problemCloseDate']").datepicker({
			changeMonth : true,
			changeYear : true
		});
		var attachments = [
		   				{showInputId:'checkFileShow',hiddenInputId:'checkFile'},
		   				{showInputId:'improveFileShow',hiddenInputId:'improveFile'}
		   			];
		parseDownloadFiles(attachments);
		$("input[class='time']").datepicker({
			changeMonth : true,
			changeYear : true,
			showButtonPanel: true
		});
		$("input[class='time']").datepicker({
			changeMonth : true,
			changeYear : true,
			showButtonPanel: true
		});
	});
	function parseDownloadFiles(arrs){
		for(var i=0;i<arrs.length;i++){
			var hiddenInputId = arrs[i].hiddenInputId;
			var showInputId = arrs[i].showInputId;
			var files = $("#"+hiddenInputId).val().length;
			if(files>0){
		 		$.parseDownloadPath({
					showInputId : showInputId,
					hiddenInputId : hiddenInputId
				});
			}
		}
	}
	//上传附件
	function uploadFiles(showId,hiddenId){
		$.upload({
			appendTo : '#opt-content',
			showInputId : showId,
			hiddenInputId : hiddenId,
			callback:function(files){
				if(showId=="showbad"){
					for(var i=0;i<files.length;i++){
						if(i==0){
							$("#pices0" ).attr("src",$.getDownloadPath(files[0].id));
							$("#pices0" ).attr("alt",$.getDownloadPath(files[0].fileName));
						}
						
					}
				}
				
			}
		});
	}
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
	function addRowHtml(totalObj,classObj,obj){
		
 		var tr = $(obj).closest("tr");
 		var clonetr = tr.clone(false);
//  		clonetr.children().last().remove();
 		tr.after(clonetr);
 		//document.getElementById('totalPrice').setAttribute('rowspan', p++);
 		var total = $("#"+totalObj);
		var num = total.val();
		clonetr.find(":input").each(function(index ,obj){
 			obj=$(obj);
 			var name=obj.attr("name").split("_")[0];
 			obj.attr("id",name+"_"+num).val("");
 			if(name=="planDate"||name=="designDate"){
 				$("#"+name+"_"+num).removeClass();
 				$("#"+name+"_"+num).unbind();
 				$("input[name='"+name+"'").datepicker({
 					changeMonth : true,
 					changeYear : true,
 					showButtonPanel: true
 				});
 			}
 			obj.attr("name",name);
 		});
 	  	total.val(parseInt(num)+1);
 	  	total.siblings("span").text(parseInt(num)+1);
  		$("."+classObj).each(function(index, obj){
  			$($(obj).children("td")[1]).text(parseInt(index)+1);
  		});
 	}
 	//减少维修项目
 	function removeRowHtml(totalObj,classObj,obj){
 		var total = $("#"+totalObj);
		var tr=$(obj).closest("tr");
		var pre=tr.prev("tr").attr("class");
		var next=tr.next("tr").attr("class");
		if(next==classObj){
		 	tr.remove();
	 	  	total.siblings("span").text(parseInt(total.val())-1);
		 	total.val(parseInt(total.val())-1);
		}else if(pre==classObj){
			tr.remove();
	 	  	total.siblings("span").text(parseInt(total.val())-1);
			total.val(parseInt(total.val())-1);
		}else{
			alert('至少要保留一行');
			total.val(1);
	 	  	total.siblings("span").text(1);
		}
  		$("."+classObj).each(function(index, obj){
  			$($(obj).children("td")[1]).text(parseInt(index)+1);
  		});
 	}
 	function getCheckItemStrs(){
		var checkItemStrs = "";
		$("#rejectTable tr[class=ingredientItemTr]").each(function(index,obj){
			if(checkItemStrs){
				checkItemStrs += ",";
			}
			var str = '';
			$(obj).find(":input").each(function(index,obj){
				if(obj.name){
					if(str){
						str += ","; 
					}
					str += "\"" + obj.name + "\":\"" + $(obj).val() + "\"";
					$(obj).attr("name","");
				}
			});
			if(str != ""){
				checkItemStrs += "{" + str + "}";
			}
		});
		return "[" + checkItemStrs + "]";
	}
	  function saveFormBoxClose(url){
		    var paramsChild = getCheckItemStrs();
			url = url + "?childParams="+paramsChild;
			$(".opt-btn").find("button.btn").attr("disabled",true);
			$.showMessage("保存中......");
			$("#inputForm").attr("action",url).submit();
	} */
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
			<div class="opt-btn" >
				<s:if test="taskId>0">
				  <c:if test="${isCurrent==true}">
					 <wf:workflowButtonGroup taskId="${taskId}"></wf:workflowButtonGroup>
					</c:if>
				</s:if>
				<s:else>
				<security:authorize ifAnyGranted="supplier-audit-save">
					<button class='btn' type="button" onclick="saveForm();"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
					<button class='btn' type="button" onclick="submitForm();"><span><span><b class="btn-icons btn-icons-ok"></b>提交</span></span></button>
				</security:authorize>
				</s:else>
				<s:if test="taskId>0">
				<button class="btn" onclick="viewFormInfo()"><span><span><b class="btn-icons btn-icons-alert"></b>流转历史</span></span></button>
				</s:if>
				<c:if test="${id>0}">
					<security:authorize ifAnyGranted="supplier-change-export-excel">
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
			     <button class='btn' onclick="history.back();">
						<span><span><b class="btn-icons btn-icons-undo"></b>返回</span></span>
					</button>
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
					<jsp:include page="input-form.jsp" />
					<s:if test="taskId>0">
						<%@ include file="process-form.jsp"%>
					</s:if>
				</s:form>
			</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>