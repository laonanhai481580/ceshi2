<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title></title>
		<%@include file="/common/meta.jsp" %>	
			<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
		<script type="text/javascript">
 		function submitForm(url){
 			$("#assignForm").attr("action",url); 
 			var type= getType();
 			if(type =="user"){
				var name=getLoginName();
 				//name=name.split('|')[0].split('-')[1]; 
 				$("#assignee").attr("value",name); 
 				$("#assignForm").ajaxSubmit(function (str){
 					if(str){
 						alert(str);
 					}else{
 						window.parent.beforeCloseWindow(); 
	 					window.parent.$.colorbox.close(); 
						window.parent.parent.close();
 					}
 				}); 
 			} 
 		} 
 		</script> 
	</head>
	
	<body onload="getContentHeight();">
	<div class="ui-layout-center">
		<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn">
							<button class='btn' onclick="submitForm('${mfgctx}/defective-goods/ledger/assign.htm')"><span><span>提交</span></span></button>
					</div>
					<div style="display: none;" id="message"><s:actionmessage theme="mytheme" /></div>
					<div id="opt-content" class="form-bg">
						<form  id="assignForm" name="assignForm" method="post" action="">
							<input type="hidden" name="id" id="id" value="${id }"></input>
							<input type="hidden" name="taskId" id="taskId" value="${taskId }"></input>
							<input type="hidden"  name="assignee" id="assignee" ></input>
							<acsTags:tree defaultable="true" treeType="MAN_DEPARTMENT_TREE" multiple="false"	treeId="companyTree"></acsTags:tree>
						</form>
					</div>
				</aa:zone>
			</div>
		</div>
	</body>
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>