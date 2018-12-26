<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
			function evaluatingIndicatorFormValidate(){
				$("#evaluatingIndicatorForm").validate({
					submitHandler: function() {
						$(".opt_btn").find("button.btn").attr("disabled","disabled");
						aa.submit('evaluatingIndicatorForm','','main',showMsg);
					}
				});
			}
			function submitForm(url){
				$('#evaluatingIndicatorForm').attr('action',url);
				$('#evaluatingIndicatorForm').submit();
			}
			function callback(){
				addFormValidate('${fieldPermission}','evaluatingIndicatorForm');
				evaluatingIndicatorFormValidate();
				showMsg();
			}
			$(function(){
				//添加验证
				evaluatingIndicatorFormValidate();
			});
		</script>
</head>

<body>
	<div class="opt-body">
		<aa:zone name="main">
			<div class="opt-btn">
				<button class='btn' onclick="submitForm('${supplierctx}/estimate/indicator/quality/save.htm')">
					<span><span><b class="btn-icons btn-icons-save"></b>保存</span>
					</span>
				</button>
			</div>
			<div style="display: none;" id="message">
				<s:actionmessage theme="mytheme" />
			</div>
			<div id="opt-content">
				<form id="evaluatingIndicatorForm" name="evaluatingIndicatorForm" method="post" action="">
					<input type="hidden" name="id" id="id" value="${id }" />
					<input type="hidden" name="structureId" id="structureId" value="${structureId }" />
					<input type="hidden" name="parentId" id="parentId" value="${parent.id}" />
					<table style="width: 90%;">
						<tr>
							<td align=right width=20%>上级指标:</td>
							<td width=80%>${parent.name}</td>
						</tr>
						<tr>
							<td align=right>名称:</td>
							<td><input id="name" name="name" value="${name}"
								class="{required: true,messages:{required:'必填'}}" /></td>
						</tr>
						<tr>
							<td align=right valign="top">上级指标:</td>
							<td><textarea rows="5" id="remark" name="remark" style="width: 100%;">${remark}</textarea></td>
						</tr>
					</table>
				</form>
			</div>
		</aa:zone>
	</div>
</body>
</html>