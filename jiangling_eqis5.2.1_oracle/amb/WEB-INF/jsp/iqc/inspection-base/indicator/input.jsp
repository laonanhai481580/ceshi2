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
			//选择检验物料
			function selectMaterielName(){
				$.colorbox({href:"${mfgctx}/common/product-bom-select.htm",iframe:true, innerWidth:750, innerHeight:500, overlayClose:false, title:"选择物料"});
		 	}
			function setBomValue(datas){
				$("#materielCode").val(datas[0].key);
				$("#materielName").val(datas[0].value);
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
				<button class='sexybutton' onclick="submitForm('${iqcctx}/inspection-base/indicator/save.htm')">
					<span class='save'>保存</span>
				</button>
				<button class='sexybutton' onclick="cancel();"><span class='cancel'>取消</span></button>
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
						<tr style=" display: none">
							<td align=right width=20%>上级:</td>
							<td width=80%>${parent.name}</td>
						</tr>
						<tr>
							<td align=right>检验物料:</td>
							<td><input id="materielName" name="materielName" value="${materielName}" readonly="readonly" onclick="selectMaterielName()"
								class="{required: true,messages:{required:'请输入检验物料'}}" /></td>
							<td style=" display: none"><input id="materielCode" name="materielCode" value="${materielCode}"/></td>
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