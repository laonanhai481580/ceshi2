<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.ambition.gp.entity.GpSubstance"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@ include file="/common/meta.jsp" %>
<%-- 	<%@ include file="/common/common-js.jsp" %> --%>
<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"
	type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
	<!-- 表单和流程常用的方法封装 -->
	<script type="text/javascript" src="${ctx}/js/workflow-form-0.9.js"></script>
<!-- 	流程驳回 -->
	<script type="text/javascript" src="${ctx}/js/lcbh.js"></script>
	<c:set var="actionBaseCtx" value="${gpctx}/averageMaterial"/>
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
					gpSubstances:{
						entityClass:"<%=GpSubstance.class.getName()%>"//子表1实体全路径
					}
				},
				inputformortaskform:'inputform',//表单类型,taskform:流程办理界面,inputform:普通表单页面
				fieldPermissionStr:'${fieldPermission}',//字符串格式的字段权限
			});
			setPercentage();
		});
		
		function saveForm() {
			var rate=setPercentage();
			if(rate!=100){
				alert("总比率需要等于100%");
				return;
			}
			if($("#workflowForm").valid()){
				var item=getScrapItems(1);
				$("#zibiao").val(item);
				$("#workflowForm").attr("action","${actionBaseCtx}/save-sub.htm");
				$("#workflowForm").submit();
				$("#message").html("正在保存，请稍候... ...");

// 	 			window.location.href='${actionBaseCtx}/input-s.htm?id='+${id};
			}else{
				var error = $("#workflowForm").validate().errorList[0];
				$(error.element).focus();
			}
		}
		function getScrapItems(n){
			var infovalue="";
			if(n==1){
				$("tr[zbtr1=true]").each(function(index,obj){
					infovalue += getTdItem(obj);
				});
			}
			var item ="["+infovalue.substring(1)+"]";
			return item;
		}
		function getTdItem(obj){
			var value="";
			$(obj).find(":input").each(function(index,obj){
				iobj = $(obj);
			    value += ",\""+iobj.attr("name").split("_")[1]+"\":\""+iobj.val()+"\"";
			});
			return ",{"+value.substring(1)+"}";
	   	}
		var ret=0;
		function setPercentage(){
			var m=0;
		    var n=0;
			var controls = $("tr[zbtr1=true]").find('input[fieldname=percentage]');
			for(var i=0; i<controls.length; i++){
			       m=parseFloat(controls[i].value);
			       if(isNaN(m)){
			    	   m=0;
			       }
				   m=m.toFixed(2);
			       n=parseFloat(n)+parseFloat(m);
			    }
					ret=n;
				$("#ratio").val(ret+"%");
				return ret;
		}  
	</script>
</head>

<body onload="getContentHeight();">
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
			<div class="opt-btn">
				<s:if test="taskId>0">
						<wf:workflowButtonGroup taskId="${taskId}"></wf:workflowButtonGroup>
					</s:if>
						 <button class='btn' type="button" onclick="saveForm();"><span><span><b class="btn-icons btn-icons-save"></b><s:text name='保存'/></span></span></button> 
				<span style="float:right;">总比率:
					<input style="float:right;" name="ratio" id="ratio" value="${ratio }"/></span>
			</div>
			<div id="opt-content" class="form-bg">
				<div style="color:red;" id="message"><s:actionmessage theme="mytheme"/></div>
				<s:form  id="workflowForm" name="workflowForm" method="post" action="">
				<div style="overflow-x:auto;overflow-y:hidden;">
					<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
					<input  type="hidden" id="id" name="id" value="${id}" />
					<input type="hidden" name= "zibiao" id="zibiao" value=""/>
						<tr>
						<td  style="padding:0px;" id="checkItemsParent" >
							<div style="overflow-x:auto;overflow-y:hidden;">
								<table class="form-table-border-left" style="border:0px;table-layout:fixed;">
									<tr >	
					               		<td  style="width:60px;text-align:center;border-top:0px;border-left:0px;">序号</td>
										<td  style="width:150px;text-align:center;border-top:0px;">Substance</td>
										<td  style="width:200px;text-align:center;border-top:0px;">CAS</td>
										<td  style="width:50px;text-align:center;border-top:0px;">重量</td>
										<td style="width:200px;text-align:center;border-top:0px;">百分比</td>
									</tr>
									<s:iterator value="_gpSubstances" id="item" var="item">
										<tr class="gpSubstances" zbtr1=true>
											<td style="text-align:center;">
												<a class="small-button-bg" addBtn="true"  onclick="addRowHtml(this)" href="#" title="添加">
												<span class="ui-icon ui-icon-plus" style='cursor: pointer;'></span></a> 
												<a class="small-button-bg" delBtn="true"  onclick="removeRowHtml(this)" href="#" title="删除">
												<span class="ui-icon ui-icon-minus" style='cursor: pointer;'></span></a>
											</td>					
											<td style="text-align: center;"><input stage="one" style="width:90%;" name="substanceName" value="${substanceName }"  /></td>
											<td style="text-align: center;"><input stage="one" style="width:90%;" name="substanceCode" value="${substanceCode }" /></td>
											<td style="text-align: center;"><input stage="one" style="width:90%;" name="substanceweight" value="${substanceweight }" class="{number:true,messages:{number:'请输入有效数字!'}}" /></td>
											<td style="text-align: center;"><input stage="one" style="width:90%;" name="percentage" onchange="setPercentage()" value="${percentage }" class="{number:true,messages:{number:'请输入有效数字!'}}" /></td>
										</tr>
									</s:iterator>
								</table>
							</div>
						</td>
						</tr>
					</table>
				</div>
				</s:form>
			</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>
