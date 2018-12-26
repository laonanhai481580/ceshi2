<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
	var isUsingComonLayout=false;
	$(function(){
		//添加验证
		evaluatingIndicatorFormValidate();
	});
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
	function checkedClick(obj){
		$("#readonly").val(obj.checked);
	}
</script>
</head>

<body>
	<div class="opt-body">
		<aa:zone name="main">
			<div class="opt-btn">
				<button class='btn'
					onclick="submitForm('${supplierctx}/estimate/indicator/quarter/save.htm')">
					<span><span><b class="btn-icons btn-icons-save"></b>保存
					</span>
					</span>
				</button>
				<span style="display: none;color:red;" id="message">
					<s:actionmessage theme="mytheme" />
				</span>
			</div>
			<div id="opt-content">
				<form id="evaluatingIndicatorForm" name="evaluatingIndicatorForm" method="post" action="">
					<div id="hiddenDiv">
						<input type="hidden" name="id" id="id" value="${id }" />
						<input type="hidden" name="structureId" id="structureId" value="${structureId }" />
						<input type="hidden" name="parentId" id="parentId" value="${parent.id}" />
					</div>
					<table style="width: 90%;">
						<tr>
							<td align=right width=20%>上级指标:</td>
							<td width=80%>${parent.name}</td>
						</tr>
						<tr>
							<td align=right>名称:</td>
							<td><input style="width: 320px;height: 20px;" id="name" name="name" value="${name}" class="{required: true,messages:{required:'必填'}}" /></td>
						</tr>
						<tr>
							<td align=right>指标单位:</td>
							<td>
								<s:select list="units"
										  listKey="value" 
										  listValue="value" 
							              labelSeparator=""
							  			  emptyOption="true"
										  theme="simple"
										  cssStyle="width:324px;height: 24px;"
										  name="unit"
										  value="unit">
								</s:select>
							</td>
						</tr>
							<tr>
							<td align=right>排序:</td>
							<td><input  name="orderByNum" id="orderByNum" readonly="readonly" value="${orderByNum}" /></td>
							<td>
							</td>
						</tr>
						<tr>
							<td align=right>数据来源:</td>
							<td>
								<s:select list="dataSources" 
										name="dataSourceCode"
										listKey="code"
										listValue="name"
										theme="simple"
										headerKey=""
										headerValue="<手动计算>"
										cssStyle="width:324px;height: 24px;"
										value="dataSourceCode"></s:select>
							</td>
						</tr>
						<tr>
							<td align=right valign="top">备注:</td>
							<td><textarea style="width: 320px;" rows="5" id="remark" name="remark">${remark}</textarea></td>
						</tr>
						<tr>
							<td align=right valign="top">
								<input type="checkbox" onclick="checkedClick(this);" ${readonly==true?"checked='checked'":""}/>
								<input type="hidden" id="readonly" name="readonly" value="${readonly}" />
							</td>
							<td>readonly=true</td>
						</tr>
					</table>
				</form>
			</div>
		</aa:zone>
	</div>
</body>
</html>