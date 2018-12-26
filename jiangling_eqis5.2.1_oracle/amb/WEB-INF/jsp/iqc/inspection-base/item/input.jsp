<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${ctx}/widgets/validation/jquery.validate.js"
	type="text/javascript"></script>
<link rel="stylesheet" type="text/css"
	href="${ctx}/widgets/validation/cmxform.css" />
<script src="${ctx}/widgets/validation/jquery.metadata.js"
	type="text/javascript"></script>
<script src="${ctx}/widgets/validation/dynamic.validate.js"
	type="text/javascript"></script>
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
			//取消
			function cancel(){
				window.parent.$.colorbox.close();
			}
		</script>
</head>

<body>
	<div class="opt-body">
		<aa:zone name="main">
			<div class="opt-btn">
				<button class='btn'
					onclick="submitForm('${iqcctx}/inspection-base/item/save.htm')">
					<span><span>保存</span>
					</span>
				</button>
				<button class='btn' onclick="cancel();"><span><span>取消</span></span></button>
			</div>
			<div style="display: none;" id="message">
				<s:actionmessage theme="mytheme" />
			</div>
			<div id="opt-content">
				<form id="evaluatingIndicatorForm" name="evaluatingIndicatorForm"
					method="post" action="">
					<input type="hidden" name="id" id="id" value="${id }" />
					<input type="hidden" name="structureId" id="structureId"
						value="${structureId }" />
					<input type="hidden" name="parentId" id="parentId"
						value="${parent.id}" />
					<table style="width: 90%;">
						<tr>
							<td align=right width=20%>上级项目:</td>
							<td width=80%>${parent.name}</td>
						</tr>
						<tr>
							<td align=right>检验项目:</td>
							<td><textarea rows="5" id="name" name="name"
									style="width: 100%;" class="{required: true,messages:{required:'请输入检验项目'}}">${name}</textarea></td>
						</tr>
						<tr>
							<td align=right>统计类型:</td>
							<td>
								<s:select list="countTypeOptions" 
										name="countType"
										emptyOption="true"
										listKey="name"
										listValue="name"
										theme="simple"
										cssStyle="width:150px;"
										value="countType"></s:select>
							</td>
						</tr>
						<tr>
							<td align=right>检验方法:</td>
							<td><input id="method" name="method" value="${method}"/></td>
						</tr>
						<%-- <tr>
							<td align=right>规格:</td>
							<td><input id="standards" name="standards" value="${standards}"/></td>
						</tr> --%>
						<tr>
							<td align=right>单位:</td>
							<td><input id="unit" name="unit" value="${unit}"/></td>
						</tr>
						<tr>
							<td align=right valign="top">备注:</td>
							<td><textarea rows="5" id="remark" name="remark"
									style="width: 100%;">${remark}</textarea></td>
						</tr>
					</table>
				</form>
			</div>
		</aa:zone>
	</div>
</body>
</html>