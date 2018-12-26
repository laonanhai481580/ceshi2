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
<script type="text/javascript">
	$(function(){
		initForm();
	});
	function initForm(){
		$("select[name]").attr("disabled","disabled");
		$(":input[changeInput]").attr("disabled","disabled");
		var type = "<%=request.getParameter("type")%>";
		if(type=='null'||!type){
			type = "<%=ChartDefinition.TOTALTYPE_COUNT%>";
		}
		var value = "<%=request.getParameter("value")==null?"":request.getParameter("value")%>";
		 $(":input[name=type]")
		 .bind("click",function(){
		 	$("label.error").remove();
		 	$("select[name]").attr("disabled","disabled");
			$(":input[changeInput]").attr("disabled","disabled");
			var bindObjName = $(this).attr("checked","checked").attr("bindObj");
			$(":input[name="+bindObjName+"]").removeAttr("disabled");	
		 }).each(function(index,obj){
		 	if(obj.value == type){
				var bindObjName = $(obj).attr("checked","checked").attr("bindObj");
				$(":input[name="+bindObjName+"]").val(value).removeAttr("disabled");		 		
		 	}
		 });
	}
	function submitForm(url){
		var obj = $(":input[name=type]:checked");
		var type = obj.val();
		var value = $(":input[name="+obj.attr("bindObj")+"]").val();
		if($("#form").valid()){
			window.parent.afterSetCountParam(type,value,"<%=request.getParameter("datasourceId")%>");
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
							<td style="width:120px;">
								<input id="totalTypeCount" bindObj="valueCount" type="radio" name="type" value="<%=ChartDefinition.TOTALTYPE_COUNT %>"/>
								<label for="totalTypeCount">计数(count)</label>
							</td>
							<td style="">
								<s:select list="dataColumns" name="valueCount" 
									listKey="name" 
									listValue="alias" 
									cssClass="{required:true,messages:{required:'必填!'}}"
									cssStyle="width:95%;"
									theme="simple"></s:select>
							</td>
						</tr>
						<tr>
							<td>
								<input id="totalTypeSum" bindObj="valueSum" type="radio" name="type" value="<%=ChartDefinition.TOTALTYPE_SUM %>"/>
								<label for="totalTypeSum">求和(sum)</label>
							</td>
							<td>
								<s:select list="sumDataColumns" name="valueSum" 
									listKey="name" 
									listValue="alias" 
									cssClass="{required:true,messages:{required:'必填!'}}"
									cssStyle="width:95%;"
									theme="simple"></s:select>
							</td>
						</tr>
						<tr>
							<td>
								<input id="totalTypeCustom" bindObj="valueCustom" type="radio" name="type" value="<%=ChartDefinition.TOTALTYPE_CUSTOM %>"/>
								<label for="totalTypeCustom">自定义</label>
							</td>
							<td style="">
								<input type="text" name="valueCustom" changeInput=true style="width:94.5%;" class="{required:true,messages:{required:'必填!'}}"/>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>