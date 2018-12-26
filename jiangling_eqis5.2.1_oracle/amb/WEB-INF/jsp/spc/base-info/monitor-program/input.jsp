<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>过程节点属性</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" ></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/dynamic.validate.js" ></script>
<%-- 	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script> --%>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	
	<script type="text/javascript">
		isUsingComonLayout=false;
		$(document).ready(function(){
			$("#editDate").datepicker({changeMonth:true,changeYear:true});
		});
		
		function processFormValidate(){
			$("#monitForm").validate({
				submitHandler: function() {
					$(".opt_btn").find("button.btn").attr("disabled","disabled");
					aa.submit('monitForm','','main',showMsg);
				}
			});
		}
		
		function submitForm(url){
			$('#monitForm').attr('action',url);
			$('#monitForm').submit();
		}
		
		function callback(){
			addFormValidate('${fieldPermission}','monitForm');
			processFormValidate();
			showMsg();
		}
		
		$(function(){
			//添加验证
			processFormValidate();
		});
		//选择人员
		function selectPerson(obj){
			var acsSystemUrl = "${ctx}";
			popTree({ title :'选择人员',
				innerWidth:'400',
				treeType:'MAN_DEPARTMENT_TREE',
				defaultTreeValue:'id',
				leafPage:'false',
				multiple:'false',
				hiddenInputId:obj.id,
				showInputId:obj.id,
				acsSystemUrl:acsSystemUrl,
				callBack:function(){}});
		}
	</script>
</head>
<body>
	<div class="opt-body">
		<aa:zone name="main">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="spc_base-info_monitor-program_program-save">
					<button class='btn' onclick="submitForm('${spcctx}/base-info/monitor-program/program-save.htm');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				</security:authorize>
				<button  class='btn' type="button" onclick="javascript:window.parent.$.colorbox.close();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
				<span style="color: red;border: 2px;" id="message"><s:actionmessage theme="mytheme" /></span>
			</div>
			<div id="opt-content" class="form-bg">
				<form id="monitForm" name="monitForm" method="post">
					<input type="hidden" name="id" id="id" value="${id }"/>
					<input type="hidden" name="parentId" id="parentId" value="${parent.id }"/>
					<table cellpadding="0" cellspacing="0" width="100%" align="center">
						<tr>
							<td>父级结构:</td>
							<td>${parent.name }</td>
						</tr>
						<tr>
							<td style="width:90px"><span style="color:red;">*</span>方案编号:</td>
							<td><input id="code" name="code" value="${code }" class="{required:true}"/></td>
							<td style="width:90px"><span style="color:red;">*</span>方案名称:</td>
							<td><input id="name" name="name" value="${name }" class="{required:true}"/> </td>
						</tr>
						<tr>
							<td style="width:90px"><span style="color:red;">*</span>编&nbsp;辑&nbsp;人:</td>
							<td><input id='editer' name='editer' value='${editer }' onclick="selectPerson(this)" readonly="readonly" class="{required:true}"/></td>
							<td style="width:90px"><span style="color:red;">*</span>编辑日期:</td>
							<td><input id='editDate' name='editDate' value='${editDate }' readonly="readonly" class="line"/></td>
						</tr>
						<tr>
							<td style="width:90px"><span style="color:red;">*</span>类别描述:</td>
							<td colspan="3"><textarea id='categoryDescription' name='categoryDescription' rows="10" style="width:450px;" class="{required:true}">${categoryDescription }</textarea></td>
						</tr>
					</table>
				</form>
			</div>
		</aa:zone>
	</div>
</body>
</html>