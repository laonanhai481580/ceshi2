<%@page import="com.ambition.carmfg.entity.FormCodingRule"%>
<%@page import="com.ambition.iqc.entity.SampleCodeLetter"%>
<%@page import="com.ambition.iqc.entity.SampleTransitionRule"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<%
	Map<String,SampleTransitionRule> transtionRuleMap = (Map<String,SampleTransitionRule>)request.getAttribute("transitionRuleMap");
	if(transtionRuleMap == null){
		transtionRuleMap = new HashMap<String,SampleTransitionRule>();
	}
%>
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
	var topMenu ='';
	$(document).ready(function(){
		$("#formCodingRuleForm").validate({
		});
	});
	function submitForm(url){
		if($("#formCodingRuleForm").valid()){
			var formCodingRuleStrs = "";
			$("#formCodingRuleTable").find("tr").each(function(index,obj){
				if(formCodingRuleStrs){
					formCodingRuleStrs += ",";
				}
				var str = '';
				$(obj).find(":input").each(function(index,obj){
					if(obj.name){
						var $obj = $(obj);
						var name = obj.name;
						if($obj.attr("customName")){
							name = $obj.attr("customName");
						}
						if(str){
							str += ","; 
						}
						var val = $obj.val();
						str += "\"" + name + "\":\"" + val + "\"";
					}
				});
				formCodingRuleStrs += "{" + str + "}";
			});
			$("#message").html("数据保存中,请稍候... ...");
			$(".opt-btn button").attr("disabled",true);
			$("#formCodingRuleForm").attr("disabled",true);
			$.post(url,{formCodingRuleStrs:'[' + formCodingRuleStrs + ']'},function(result){
				$(".opt-btn button").attr("disabled","");
				$("#formCodingRuleForm").attr("disabled","");
				if(result.error){
					$("#message").html("<font color=red>"+result.message+"</font>");
					alert(result.message);
				}else{
					$("#message").html(result.message);
				}
				setTimeout(function(){
					$("#message").html("");
				},3000);
			},'json');
		}
	}
	function preview(obj){
		$("#message").html("正在生成编码,请稍候... ...");
		$(".opt-btn button").attr("disabled",true);
		$("#formCodingRuleForm").attr("disabled",true);
		$.post('${mfgctx}/base-info/form-coding-rule/preview.htm',{code:$(obj).parent().find("input[name=code]").val(),rule:$(obj).parent().find("input[customName=rule]").val()},function(result){
			$(".opt-btn button").attr("disabled","");
			$("#formCodingRuleForm").attr("disabled","");
			if(result.error){
				$("#message").html("<font color=red>"+result.message+"</font>");
				alert(result.message);
			}else{
				$("#message").html("");
				alert(result.message,"aa");
			}
			setTimeout(function(){
				$("#message").html("");
			},3000);
		},'json');
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
<input type="hidden" id="id" value="${id}"/>
	<script type="text/javascript">
	var secMenu="baseInfo";
	var thirdMenu="formCodingRule";
	var treeMenu="transition-rule";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-base-info-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" style="line-height:30px;">
			<security:authorize ifAnyGranted="MFG_BASE-INFO_FORM-CODING-RULE_SAVE">
			<button  class='btn' type="button" onclick="submitForm('${mfgctx}/base-info/form-coding-rule/save.htm');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
			</security:authorize>	
				<span style="margin-left:5px;color:red;" id="message"></span>
			</div>
			<div id="opt-content" style="text-align:center;">
				<form action="" method="post" id="formCodingRuleForm" name="formCodingRuleForm">
					<table class="form-table-border-left" style="width:100%;margin:2px auto;" id="formCodingRuleTable">
						<%
							List<FormCodingRule> formCodingRules = (List<FormCodingRule>)request.getAttribute("formCodingRules");
							if(formCodingRules==null){
								formCodingRules = new ArrayList<FormCodingRule>();
							}
							for(FormCodingRule formCodingRule : formCodingRules){
						%>
						<tr height=29>
							 <td style="text-align:center;width:180px;text-align:right;">
							 	<span style="font-size:16px;font-weight:bold;"><%=formCodingRule.getName()%></span>
							 </td>
							 <td style="padding-left:6px;">
							 	<input type="hidden" name="name" value="<%=formCodingRule.getName()%>"></input>
							 	<input type="hidden" name="code" value="<%=formCodingRule.getCode()%>"></input>
							 	<input type="hidden" name="entityName" value="<%=formCodingRule.getEntityName()%>"></input>
							 	<input type="hidden" name="targetField" value="<%=formCodingRule.getTargetField()%>"></input>
							 	<input type="hidden" name="additionalCondition" value="<%=formCodingRule.getAdditionalCondition()==null?"":formCodingRule.getAdditionalCondition()%>"></input>
							 	<input type="text" value="<%=formCodingRule.getRule() %>" customName="rule" name="rule<%=formCodingRule.getId()%>" style="width:50%;" class="{required:true,maxlength:90}"/>
							 	<security:authorize ifAnyGranted="MFG_BASE-INFO_FORM-CODING-RULE_PREVIEW">
							 	<button  class='btn' type="button" onclick="preview(this)"><span><span><b class="btn-icons btn-icons-play"></b>预览</span></span></button>
							 </security:authorize>
							 	</td>
						</tr>
						<%} %>
					</table>
				</form>
				<div style="margin:4px auto;width:100%;border:1px solid #8dc0e7;">
					<div style="height:30px;line-height:30px;padding-left:6px;font-weight:bold;text-align:left;border-bottom:1px solid #8dc0e7;background:#CAECF6;">
						编码规则
					</div>
					<div style="padding:6px;text-align:left;">
						1).可用代码:<b>$year(年),$month(月),$date(日),X(流水号),流水号至少为2位</b><br/>
						2).自定义的编码里面需要用到<b>超过两位连续的X时可以在X前加上\,如JCXXX-2012-06-18-00001可以表示为:JC\X\X\X-$year-$month-$date-XXXXX</b>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
</html>