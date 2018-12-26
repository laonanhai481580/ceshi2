<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
	$(function(){
		var modelSpecification = window.parent.$(":input[name=modelSpecification]").val();
		$(":input[name=modelSpecification]").val(modelSpecification);
		var bringAmount = window.parent.$(":input[name=bringAmount]").val();
		$(":input[name=bringAmount]").val(bringAmount);
		//
		var isPPMCount = '';
		window.parent.$(":input[name=isPPMCount]").each(function(index,obj){
			if($(obj).is(":checked")){
				isPPMCount = $(obj).val();
				return false;
			}
		});
		$(":input[name=isPPMCount][value="+isPPMCount+"]").attr("checked","checked");
		if(isPPMCount==0){
			isPPMCountNoFunc();
		}else{
			isPPMCountYesFunc();
		}
	});
	function isPPMCountNoFunc(){
		$("#bringAmount").val(0);
		$("#bringAmount").attr("disabled","disabled");
	}
	function isPPMCountYesFunc(){
		$("#bringAmount").removeAttr("disabled");
	}
	//获取表单的值
	function getParams(){
		var params = {};
		$(":input[name]").each(function(index,obj){
			var jObj = $(obj);
			if(obj.type=='radio'){
				if(jObj.is(":checked")){
					params[obj.name] = jObj.val();
				}
			}else{
				params[obj.name] = jObj.val();			
			}
		});
		return params;
	}
	function submitForm(url){
		if(!confirm("确定要修改吗?")){
			return;
		}
		var params = getParams();
		$("#message").html("正在保存,请稍候... ...").show();
		$.post(url,params,function(result){
			$("#message").hide();
			if(result.error){
				alert(result.message);
			}else{
				afterUpdateInfo(params);
				closeInput();
			}
		},'json');
	}
	function closeInput(){
		window.parent.$.colorbox.close();
	}
	function afterUpdateInfo(params){
		window.parent.$(":input[name=modelSpecification]").val(params.modelSpecification);
		window.parent.$(":input[name=bringAmount]").val(params.bringAmount);
		window.parent.$(":input[name=isPPMCount][value="+params.isPPMCount+"]").attr("checked","checked");
		if(params.isPPMCount==0){
			window.parent.isPPMCountNoFunc();
		}else{
			window.parent.isPPMCountYesFunc();
		}
	}
	</script>
</head>
<body>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" id="btmDiv">
				<button class='btn' type="button" onclick="submitForm('${iqcctx}/inspection-report/saveaudit.htm?id='+${id});"><span><span><b class="btn-icons btn-icons-submit"></b>提交</span></span></button>
				<button class='btn' onclick="closeInput();"><span><span><b class="btn-icons btn-icons-cancel"></b><s:text name="关闭"/></span></span></button>				
			</div>
			<div style="display:block;" id="message"><s:actionmessage theme="mytheme" cssStyle="color:red;" /></div>
			<div id="opt-content" style="text-align: center;">
				<form action="" method="post" id="measurementForm" name="measurementForm" >
					<input type="hidden" id="id" value="${id}"/>
					<table class="form-table-border-left" style="width:100%;margin: auto;">
						<tr>
							<td style="width:30%;">产品车型</td>
							<td>
								<s:select list="modelSpecifications" id="modelSpecification"
										  theme="simple"
										  listKey="value" 
										  listValue="value" 
										  name="modelSpecification"
										  labelSeparator=""
										  emptyOption="true">
								</s:select>
							</td>
						</tr>
						<tr>
							<td>是否PPM统计</td>
							<td>
								<input type="radio" onclick="isPPMCountYesFunc();" id="isPPMCountYes" name="isPPMCount" value="1" <s:if test="%{isPPMCount==\"1\"}">checked="checked"</s:if>/>是
								<input type="radio" onclick="isPPMCountNoFunc();"  id="isPPMCountNo" name="isPPMCount" value="0" <s:if test="%{isPPMCount==\"0\"}">checked="checked"</s:if>/>否
							</td>
						</tr>
						<tr>
							<td>纳入统计数</td>
							<td>
									<input type="text" id="bringAmount" name="bringAmount" value="${bringAmount}"/>
							</td>
						</tr>
					</table> 
				</form>
			</div>
		</div>
	</div>
</body>
</html>