<%@page import="com.ambition.chartdesign.entity.ChartSearch"%>
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
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/js/chart-view-search-format1.0.js"></script>	
<script type="text/javascript">
	$(function(){
		initForm();
	});
	function initForm(){
		//初始化插件
		if(window.chartView&&window.chartView.getAllSearchFormats){
			var formats = window.chartView.getAllSearchFormats();
			for(var i=0;i<formats.length;i++){
				$("#valueSetCode2").append("<option value='"+formats[i].code+"'>"+formats[i].name+"</option>");
			}
		}
		$("select[name]").attr("disabled","disabled");
		$(":input[changeInput]").attr("disabled","disabled");
		$("#isMulti").attr("disabled","disabled");
		//类型
		var valueSetType = '<%=request.getParameter("valueSetType")%>';
		if(valueSetType=='null'||!valueSetType){
			valueSetType = "";
		};
		var valueSetCode = "<%=request.getParameter("valueSetCode")==null?"":request.getParameter("valueSetCode")%>";
		$(":input[name=valueSetType]")
		 .bind("click",function(){
		 	$("label.error").remove();
		 	$("select[name]").attr("disabled","disabled");
			$("#isMulti").attr("disabled","disabled");
			$(":input[changeInput]").attr("disabled","disabled");
			valueSetTypeClick(this);
		 }).each(function(index,obj){
		 	if(obj.value == valueSetType){
				valueSetTypeClick(obj);	 
				$("#valueSetCode" + (index+1)).val(valueSetCode);
		 	}
		 });
		 var isMulti = "<%=request.getParameter("isMulti")==null?"":request.getParameter("isMulti")%>";
		 if(isMulti){
		 	$("#isMulti").attr("checked","checked");
		 }else{
		 	$("#isMulti").removeAttr("checked");
		 }
	}
	function valueSetTypeClick(obj){
		var bindObjName = $(obj).attr("checked","checked").attr("bindObj");
		if(bindObjName){
			var names = bindObjName.split(",");
			for(var i=0;i<names.length;i++){
				$(":input[name="+names[i]+"]").removeAttr("disabled");	
			}
		}
	}
	function submitForm(url){
		if($("#form").valid()){
			var rowId = '<%=request.getParameter("editId")%>';
			var valueSetType = $(":input[name=valueSetType]:checked").val();
			var valueSetCode = '',valueSetName = '',isMulti = '';
			if(valueSetType == '<%=ChartSearch.VALUESETTYPE_OPTIONGROUP%>'){
				valueSetCode = $("#valueSetCode1").val();
				var multiObj = $("#isMulti:checked");
				if(multiObj.length>0){
					isMulti = multiObj.val();
				}
			}else if(valueSetType == '<%=ChartSearch.VALUESETTYPE_PLUGIN%>'){
				valueSetCode = $("#valueSetCode2").val();
				valueSetName = $("#valueSetCode2").find("option:checked").html();
			}else if(valueSetType == '<%=ChartSearch.VALUESETTYPE_YEARMONTH%>'){
				valueSetCode = $("#valueSetCode3").val();
				valueSetName = $("#valueSetCode3").find("option:checked").html();
			}
			window.parent.afterSetValueSetParam(rowId,valueSetType,valueSetCode,valueSetName,isMulti);
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
								<input id="valueSetType1" bindObj="valueSetCode1,isMulti" type="radio" name="valueSetType" value="<%=ChartSearch.VALUESETTYPE_OPTIONGROUP%>"/>
								<label for="valueSetType1">选项组</label>
							</td>
							<td style="">
								<input type="text" id="valueSetCode1" name="valueSetCode1" changeInput=true style="width:70%;" class="{required:true,messages:{required:'必填!'}}"/>
								<input type="checkbox" value="1" name="isMulti" 
									id="isMulti"/><label for="isMulti">多选</label>
							</td>
						</tr>
						<tr>
							<td>
								<input id="valueSetType2" bindObj="valueSetCode2" type="radio" name="valueSetType" value="<%=ChartSearch.VALUESETTYPE_PLUGIN%>"/>
								<label for="valueSetType2">自定义插件</label>
							</td>
							<td style="">
								<select name="valueSetCode2" id="valueSetCode2"  
									class="{required:true,messages:{required:'必填!'}}">
									<option value="">请选择</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>
								<input id="valueSetType3" bindObj="valueSetCode3" type="radio" name="valueSetType" value="<%=ChartSearch.VALUESETTYPE_YEARMONTH%>"/>
								<label for="valueSetType3">月份</label>
							</td>
							<td style="">
								<select name="valueSetCode3" id="valueSetCode3"  
									class="{required:true,messages:{required:'必填!'}}">
									<option value="">请选择</option>
									<option value="yy-mm">年-月</option>
									<option value="yymm">年月</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>
								<input id="valueSetType4" type="radio" name="valueSetType" value=""/>
								<label for="valueSetType4">不设置</label>
							</td>
							<td style="">
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>