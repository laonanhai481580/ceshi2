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
	<title>进货检验报告</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
	var topMenu ='';
	$(document).ready(function(){
		$("#transitionRuleForm").validate({
		});
		$(":input[type=checkbox]").bind("click",function(){
			checkboxChange(this);
		});
		//设置状态
		$(":input[type=checkbox]").each(function(index,obj){
			checkboxChange(obj);
		});
	});
	function checkboxChange(obj){
		$(obj).parent().parent().find(":input").attr("disabled",obj.checked?"":"disabled");
		if(!obj.checked){
			$(obj).attr("disabled","");
			$("#transitionRuleForm").valid();
		}
	}
	function submitForm(url){
		if($("#transitionRuleForm").valid()){
			var transtionRuleStrs = "";
			$("#transitionRuleTable").find("tr").each(function(index,obj){
				if(transtionRuleStrs){
					transtionRuleStrs += ",";
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
						if(obj.type=='checkbox'){
							if(!obj.checked){
								val = '<%=SampleTransitionRule.STATE_DISABLE%>';
							}
						}
						str += "\"" + name + "\":\"" + val + "\"";
					}
				});
				transtionRuleStrs += "{" + str + "}";
			});
			var useBaseType = $("#useBaseTypeTable").find(":input[name=useBaseType][checked]").val();
			$("#message").html("数据保存中,请稍候... ...");
			$(".opt-btn .btn").attr("disabled",true);
			$("#transitionRuleForm").attr("disabled",true);
			$.post(url,{transtionRuleStrs:'[' + transtionRuleStrs + ']',baseType:useBaseType},function(result){
				$(".opt-btn .btn").attr("disabled","");
				$("#transitionRuleForm").attr("disabled","");
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
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
<input type="hidden" id="id" value="${id}"/>
	<script type="text/javascript">
	var secMenu="standard";
	var thirdMenu="_transition_sample";
	var treeMenu="transition-rule";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-sample-standard-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" style="line-height:30px;">
			<security:authorize ifAnyGranted="iqc-sampleTransitionRule-save">
				<button  class='btn' type="button" onclick="submitForm('${iqcctx}/sample-standard/transition-rule/save.htm');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
			</security:authorize>
				<span style="margin-left:5px;color:red;" id="message"></span>
			</div>
			<div id="opt-content" style="text-align:center;">
				<form action="" method="post" id="transitionRuleForm" name="transitionRuleForm">
					<table class="form-table-border-left" style="width:100%;margin: auto;border-bottom:0px;" id="useBaseTypeTable">
						<tr height=29>
							 <td style="text-align:center;width:180px;border-bottom:0px;">
							 	<b>抽样标准</b>
							 </td>
							 <%
							 	String useBaseType = (String)request.getAttribute("useBaseType");
							 	if(useBaseType == null){
							 		useBaseType = SampleCodeLetter.MIL_TYPE;
							 	}
							 %>
							 <td style="padding-left:6px;border-bottom:0px;">
							 	<input name="useBaseType" type="radio" value="<%=SampleCodeLetter.GB_TYPE%>" <%=SampleCodeLetter.GB_TYPE.equals(useBaseType)?"checked=checked":"" %>></input><%=SampleCodeLetter.GB_TYPE%>
<%-- 							 	<input name="useBaseType" type="radio" value="<%=SampleCodeLetter.MIL_TYPE%>" <%=SampleCodeLetter.MIL_TYPE.equals(useBaseType)?"checked=checked":"" %>></input><%=SampleCodeLetter.MIL_TYPE%> --%>
							 </td>
						</tr>
					</table>
					<table class="form-table-border-left" style="width:100%;margin: auto;" id="transitionRuleTable">
						<%
							//正常 至 加严
							String key = SampleTransitionRule.RULE_ORDINARY + "_" + SampleTransitionRule.RULE_TIGHTEN;
							SampleTransitionRule sampleTransitionRule = transtionRuleMap.get(key);
							if(sampleTransitionRule==null){
								sampleTransitionRule = new SampleTransitionRule();
								sampleTransitionRule.setState(SampleTransitionRule.STATE_USE);
							}
						%>
						<tr height=29>
							 <td style="text-align:center;width:180px;">
							 	<span style="font-size:16px;font-weight:bold;"><%=SampleTransitionRule.RULE_ORDINARY%></span> 至
							 	<span style="font-size:16px;font-weight:bold;"><%=SampleTransitionRule.RULE_TIGHTEN%></span>
							 </td>
							 <td style="padding-left:6px;">
							 	<input type="hidden" name="sourceRule" value="<%=SampleTransitionRule.RULE_ORDINARY%>"></input>
							 	<input type="hidden" name="targetRule" value="<%=SampleTransitionRule.RULE_TIGHTEN%>"></input>
							 	<input type="hidden" name="flowWay" value="up"></input>
							 	在
							 	<input type="hidden" name="statisticalMethod" value="<%=SampleTransitionRule.METHOD_ACCUMULATIVE%>"></input>
							 	<input type="text" value="<%=sampleTransitionRule.getTotalRange()==null?"":sampleTransitionRule.getTotalRange() %>" customName="totalRange" name="totalRange1" style="width:50px;" class="{required:true,min:0}"/>
							 	批检验中有
							 	<input type="hidden" value=">=" name="comparisonOperators"></input>
							 	<input type="text" value="<%=sampleTransitionRule.getAmount()==null?"":sampleTransitionRule.getAmount() %>" customName="amount" name="amount1" style="width:50px;" class="{required:true,min:0}"/>
							 	批不接收时启动转移规则
							 	<input type="hidden" value="NG" name="conclusion"></input>
							 </td>
							 <td style="width:180px;">
							 	<input type="checkbox" name="state" value="<%=SampleTransitionRule.STATE_USE%>" <%=SampleTransitionRule.STATE_USE.equals(sampleTransitionRule.getState())?"checked=\"checked\"":"" %>/>启用
							 </td>
						</tr>
						<%
							//加严 至 正常
							key = SampleTransitionRule.RULE_TIGHTEN + "_" + SampleTransitionRule.RULE_ORDINARY;
							sampleTransitionRule = transtionRuleMap.get(key);
							if(sampleTransitionRule==null){
								sampleTransitionRule = new SampleTransitionRule();
								sampleTransitionRule.setState(SampleTransitionRule.STATE_USE);
							}
						%>
						<tr height=29>
							 <td style="text-align:center;width:180px;">
							 	<span style="font-size:16px;font-weight:bold;"><%=SampleTransitionRule.RULE_TIGHTEN%></span> 至
							 	<span style="font-size:16px;font-weight:bold;"><%=SampleTransitionRule.RULE_ORDINARY%></span>
							 </td>
							 <td style="padding-left:6px;">
							 	<input type="hidden" name="sourceRule" value="<%=SampleTransitionRule.RULE_TIGHTEN%>"></input>
							 	<input type="hidden" name="targetRule" value="<%=SampleTransitionRule.RULE_ORDINARY%>"></input>
							 	<input type="hidden" name="flowWay" value="down"></input>
							 	接连
							 	<input type="hidden" name="statisticalMethod" value="<%=SampleTransitionRule.METHOD_SUCCESSION%>"></input>
							 	<input type="hidden" value=">=" name="comparisonOperators"></input>
							 	<input type="text" value="<%=sampleTransitionRule.getAmount()==null?"":sampleTransitionRule.getAmount() %>" customName="amount" name="amount2" style="width:50px;" class="{required:true,min:0}"/>
							 	批检验项目被接收时启动转移规则
							 	<input type="hidden" value="OK" name="conclusion"></input>
							 </td>
							 <td style="width:180px;">
							 	<input type="checkbox" name="state" value="<%=SampleTransitionRule.STATE_USE%>" <%=SampleTransitionRule.STATE_USE.equals(sampleTransitionRule.getState())?"checked=\"checked\"":"" %>/>启用
							 </td>
						</tr>
						<%
							//正常 至 放宽
							key = SampleTransitionRule.RULE_ORDINARY + "_" + SampleTransitionRule.RULE_RELAX;
							sampleTransitionRule = transtionRuleMap.get(key);
							if(sampleTransitionRule==null){
								sampleTransitionRule = new SampleTransitionRule();
								sampleTransitionRule.setState(SampleTransitionRule.STATE_USE);
							}
						%>
						<tr height=29>
							 <td style="text-align:center;width:180px;">
							 	<span style="font-size:16px;font-weight:bold;"><%=SampleTransitionRule.RULE_ORDINARY%></span> 至
							 	<span style="font-size:16px;font-weight:bold;"><%=SampleTransitionRule.RULE_RELAX%></span>
							 </td>
							 <td style="padding-left:6px;">
							 	<input type="hidden" name="sourceRule" value="<%=SampleTransitionRule.RULE_ORDINARY%>"></input>
							 	<input type="hidden" name="targetRule" value="<%=SampleTransitionRule.RULE_RELAX%>"></input>
							 	<input type="hidden" name="flowWay" value="down"></input>
							 	接连
							 	<input type="hidden" name="statisticalMethod" value="<%=SampleTransitionRule.METHOD_SUCCESSION%>"></input>
							 	<input type="hidden" value=">=" name="comparisonOperators"></input>
							 	<input type="text" value="<%=sampleTransitionRule.getAmount()==null?"":sampleTransitionRule.getAmount() %>" customName="amount" name="amount3" style="width:50px;" class="{required:true,min:0}"/>
							 	批检验项目被接收时启动转移规则
							 	<input type="hidden" value="OK" name="conclusion"></input>
							 </td>
							 <td style="width:180px;">
							 	<input type="checkbox" name="state" value="<%=SampleTransitionRule.STATE_USE%>" <%=SampleTransitionRule.STATE_USE.equals(sampleTransitionRule.getState())?"checked=\"checked\"":"" %>/>启用
							 </td>
						</tr>
						<%
							//放宽 至 正常
							key = SampleTransitionRule.RULE_RELAX + "_" + SampleTransitionRule.RULE_ORDINARY;
							sampleTransitionRule = transtionRuleMap.get(key);
							if(sampleTransitionRule==null){
								sampleTransitionRule = new SampleTransitionRule();
								sampleTransitionRule.setState(SampleTransitionRule.STATE_USE);
							}
						%>
						<tr height=29>
							 <td style="text-align:center;width:180px;">
							 	<span style="font-size:16px;font-weight:bold;"><%=SampleTransitionRule.RULE_RELAX%></span> 至
							 	<span style="font-size:16px;font-weight:bold;"><%=SampleTransitionRule.RULE_ORDINARY%></span>
							 </td>
							 <td style="padding-left:6px;">
							 	<input type="hidden" name="sourceRule" value="<%=SampleTransitionRule.RULE_RELAX%>"></input>
							 	<input type="hidden" name="targetRule" value="<%=SampleTransitionRule.RULE_ORDINARY%>"></input>
							 	<input type="hidden" name="flowWay" value="up"></input>
							 	在
							 	<input type="hidden" name="statisticalMethod" value="<%=SampleTransitionRule.METHOD_ACCUMULATIVE%>"></input>
							 	<input type="text" value="<%=sampleTransitionRule.getTotalRange()==null?"":sampleTransitionRule.getTotalRange() %>" customName="totalRange" name="totalRange2" style="width:50px;" class="{required:true,min:0}"/>
							 	批检验中有
							 	<input type="hidden" value=">=" name="comparisonOperators"></input>
							 	<input type="text" value="<%=sampleTransitionRule.getAmount()==null?"":sampleTransitionRule.getAmount() %>" customName="amount" name="amount4" style="width:50px;" class="{required:true,min:0}"/>
							 	批不接收时启动转移规则
							 	<input type="hidden" value="NG" name="conclusion"></input>
							 </td>
							 <td style="width:180px;">
							 	<input type="checkbox" name="state" value="<%=SampleTransitionRule.STATE_USE%>" <%=SampleTransitionRule.STATE_USE.equals(sampleTransitionRule.getState())?"checked=\"checked\"":"" %>/>启用
							 </td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
</html>