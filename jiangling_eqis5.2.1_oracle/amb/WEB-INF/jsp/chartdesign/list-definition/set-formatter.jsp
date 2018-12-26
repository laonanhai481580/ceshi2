<%@page import="com.ambition.chartdesign.entity.ChartListViewColumn"%>
<%@page import="org.hibernate.Hibernate"%>
<%@page import="com.ambition.chartdesign.entity.ChartDefinition"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript">
	$(function(){
		initForm();
	});
	function initForm(){
		var rowData = window.parent.getRowData();
		$(":input[formatterValue]").attr("disabled","disabled");
		var obj = $(":input[name=formatterType][value="+rowData.formatterType+"]");
		obj.attr("checked","checked");
		obj.closest("tr").find(":input[formatterValue]").val(rowData.formatterValue);
		//格式化初始化
		$(":input[name=formatterType]").click(function(){
			formatterClick(this);
		});
		if(obj.length>0){
			formatterClick(obj);
		}
	}
	function formatterClick(obj){
		$(":input[formatterValue]").attr("disabled","disabled");
		$(obj).closest("tr").find(":input[formatterValue]").removeAttr("disabled");
	}
	function submitForm(url){
		if($("#form").valid()){
			var $obj = $(":input[name=formatterType]:checked");
			var params = {
				formatterType : $obj.val(),
				formatterValue:$obj.closest("tr").find(":input[formatterValue]").val()
			};
			if(params.formatterValue==undefined){
				params.formatterValue = '';
			};
			window.parent.setRowData(params);
			cancel();
		}
	}
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
	}
</script>
</head>
<body>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<button class='btn' type="button"
					onclick="submitForm()">
					<span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span>
				</button>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
				<span id="message" style="color:red;">
				</span>
			</div>
			<div id="opt-content">
				<form id="form" name="form"
					method="post" action="">
					<table style="width: 100%;height:100%;">
						<tr>
							<td>
								<input type="radio" name="formatterType" value=""  class="{required:true,messages:{required:'必填!'}}"
								id="formatterType"/><label for="formatterType">不格式化</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="radio" name="formatterType" class="{required:true,messages:{required:'必填!'}}" value="<%=ChartListViewColumn.FORMATTER_TYPE_NUMBER%>" 
								id="formatterType1"/><label for="formatterType1">数字  显示</label>
								<input type="text" formatterValue=true style="width:40px;" value="2" class="{digits:true,required:true,messages:{digits:'必须为整数!',required:'必填!'}}"/>
								<label>位.</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="radio" name="formatterType" class="{required:true,messages:{required:'必填!'}}" value="<%=ChartListViewColumn.FORMATTER_TYPE_ATTACHMENT%>" 
								id="formatterType2"/><label for="formatterType2">附件</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="radio" name="formatterType" class="{required:true,messages:{required:'必填!'}}" value="<%=ChartListViewColumn.FORMATTER_TYPE_GROUP%>" 
								id="formatterType3"/><label for="formatterType3">选项组</label>
								<input type="text" formatterValue=true value="" class="{required:true,messages:{required:'必填!'}}"/>
							</td>
						</tr>
						<tr>
							<td>
								<input type="radio" name="formatterType" class="{required:true,messages:{required:'必填!'}}" value="<%=ChartListViewColumn.FORMATTER_TYPE_CUSTOM%>" 
								id="formatterType4"/><label for="formatterType4">自定义</label>
								<div style="padding-left:20px;">
									<b>function formatter(value,o,rowObject){</b>
									<textarea rows="4" style="width:98%;height:60px;" formatterValue=true  class="{required:true,messages:{required:'必填!'}}">return value;</textarea>
									<b>}</b>
								</div>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>