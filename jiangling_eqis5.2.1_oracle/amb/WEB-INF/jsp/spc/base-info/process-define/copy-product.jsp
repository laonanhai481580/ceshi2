<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.DAY_OF_MONTH,1);
	String dateStr = sdf.format(calendar.getTime());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>过程节点属性</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" ></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/dynamic.validate.js" ></script>
	
<script type="text/javascript">
	isUsingComonLayout=false;
	$(document).ready(function(){
		$("#editDate").datepicker({changeMonth:true,changeYear:true});
	});
	
	function processFormValidate(){
		$("#processForm").validate({
			submitHandler: function() {
				$(".opt_btn").find("button.btn").attr("disabled","disabled");
				aa.submit('processForm','','main',showMsg);
			}
		});
	}
	
	function submitForm(url){
		if($("#processForm").valid()){
			$('#processForm').attr('action',url);
			$("#message").html("<b>数据保存中,请稍候... ...</b>");
			$('#processForm').submit();
		}
	}
	
	function callback(){
		addFormValidate('${fieldPermission}','processForm');
		processFormValidate();
		showMsg();
	}
	
	$(function(){
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
	//关闭
	function cancel(){
		window.parent.$.colorbox.close();
	}
</script>
</head>
<body>
	<div class="opt-body">
		<aa:zone name="main">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="spc_base-info_process-define_point-save">
					<button class='btn' onclick="submitForm('${spcctx}/base-info/process-define/copy-point-save.htm');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				</security:authorize>
				<button class='btn' onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
				<span id="message" style="color: red; padding-left: 4px; font-weight: bold;"><s:actionmessage theme="mytheme"/></span>
			</div>
			<div id="opt-content" class="form-bg">
				<form id="processForm" name="processForm" method="post">
					<input type="hidden" name="id" id="id" value="${id }"/>
					<input type="hidden" name="copyId" id="copyId" value="${copyId }"/>
					<input type="hidden" name="parentId" id="parentId" value="${parent.id }"/>
					<table cellpadding="0" cellspacing="0" width="100%" align="center">
						<tr>
							<td>父级结构:</td>
							<td>${parent.name }</td>
						</tr>
						<tr>
							<td style="width:90px"><span style="color:red;">*</span>产品编号:</td>
							<td><input id="code" name="code" value="${code }" class="{required:true}"/></td>
							<td style="width:90px"><span style="color:red;">*</span>产品名称:</td>
							<td><input id="name" name="name" value="${name }" class="{required:true}"/> </td>
						</tr>
						<tr>
							<td style="width:90px">编&nbsp;辑&nbsp;人:</td>
							<td><input id='editer' name='editer' value='${editer }' disabled="disabled"/></td>
							<td style="width:90px">编辑日期:</td>
							<td><input id='editDate' name='editDate' value='<%=dateStr %>' disabled="disabled"/></td>
						</tr>
						<tr>
							<td style="width:90px">备&nbsp;&nbsp;&nbsp;&nbsp;注:</td>
							<td colspan="3"><textarea id='remark' name='remark' rows="10" style="width:450px;">${remark }</textarea></td>
						</tr>
					</table>
				</form>
			</div>
		</aa:zone>
	</div>
</body>
</html>