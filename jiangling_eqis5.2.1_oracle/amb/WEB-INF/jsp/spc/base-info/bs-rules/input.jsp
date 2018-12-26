<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="com.ambition.spc.entity.BsRules"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp"%>
	<script type="text/javascript" src="${ctx}/widgets/spin/jquery-spin.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" ></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/dynamic.validate.js" ></script>
	
	<script type="text/javascript">
		$(document).ready(function(){
			$.spin.imageBasePath='${ctx}/widgets/spin/img/spin1/';
			$("#number1").spin({
				max:100,
				min:1
			});
			$("#number2").spin({
				max:100,
				min:1
			});
			$("#number3").spin({
				max:100,
				min:1
			});
			$("#number4").spin({
				max:100,
				min:1
			});$("#number5").spin({
				max:100,
				min:1
			});
			$("#number6").spin({
				max:100,
				min:1
			});
			$("#contentForm").validate({});
		});
			
		function submitForm(url){
			var type1=$("#type1").attr("checked");
			var type2=$("#type2").attr("checked"); 
			if(!type1&&!type2){
				alert("请选择控制图类型");
				return;
			}
			var model1=$("#model1").attr("checked");
			var model2=$("#model2").attr("checked");
			if(!model1&&!model2){
				alert("请选择趋势图类型");
				return;
			}
			if(model1){
				var number1=$("#number1").val();
				if(!number1){
					alert("请为趋势图输入点数");
				}
			}
			if(model2){
				var number2=$("#number2").val();
				if(!number2){
					alert("请为运行图输入点数");
					return;
				}
				var number3=$("#number3").val();
				if(!number3){
					alert("请为运行图输入点数");
					return;
				}
			}
			$.showMessage("请选中需要编辑的记录！");
			$('#contentForm').attr('action',url);
			$('#contentForm').submit();
			$.showMessage("保存成功！");
		}
		
		var number1input=false;
		var number2input=false;
		var number3input=false;
		function setName(){
			var autoWriteName=$("#autoWriteName").attr("checked");
			if(autoWriteName){
				var name="";
				var type1=$("#type1").attr("checked");
				var type2=$("#type2").attr("checked");
				var model1=$("#model1").attr("checked");
				var model2=$("#model2").attr("checked");
				if(type1){
					name=name+"主控制图";
					$("#name").val(name);
				}
				if(type2){
					name=name+"副控制图";
					$("#name").val(name);
				}
				if(model1){
					name=name+"为趋势图";
					$("#name").val(name);
					var number1=$("#number1").val();
					if(number1){
						number1input=true;
					}else{
						number1input=false;
					}
					name=name+"连续"+number1+"点";
					$("#name").val(name);
					if(number1input){
						var range0=$("#range0").val();
						var rangename="";
						if(range0==1){
							rangename="上升";
						}else if(range0==2){
							rangename="下降";
						}else if(range0==3){
							rangename="不变";
						}else if(range0==4){
							rangename="上下交替";
						}
						name=name+rangename;
						$("#name").val(name);
					}
				}
				if(model2){
					name=name+"为运行图";
					$("#name").val(name);
					var number2=$("#number2").val();
					if(number2){
						number2input=true;
						name=name+"连续"+number2+"点";
						$("#name").val(name);
					}else{
						number2input=false;
					}
					var number3=$("#number3").val();
					if(number3){
						number3input=true;
						name=name+"中有"+number3+"点";
						$("#name").val(name);
					}else{
						number3input=false;
					}
					if(number3input||number2input){
						var range1=$("#range1").val();
						var rangename="";
						if(range1==5){
							rangename="位于A区";
						}else if(range1==6){
							rangename="位于B区";
						}else if(range1==7){
							rangename="位于C区";
						}else if(range1==8){
							rangename="在控制限以外";
						}else if(range1==9){
							rangename="在规格线以外";
						}else if(range1==10){
							rangename="在中心线同侧B区以外";
						}else if(range1==11){
							rangename="在中心线同侧C区以外";
						}else if(range1==12){
							rangename="在中心线同侧";
						}else if(range1==13){
							rangename="在中心线两侧B区以外";
						}else if(range1==14){
							rangename="在中心线两侧C区以外";
						}
						name=name+rangename;
						$("#name").val(name);
					}
				}
			}
		}
			
		var no1input=false;
		var no2input=false;
		var no3input=false;
		function setNo(){
			var no="";
			var type1=$("#type1").attr("checked");
			var type2=$("#type2").attr("checked");
			var model1=$("#model1").attr("checked");
			var model2=$("#model2").attr("checked");
			if(type1){
				no=no+"SN.0";
				$("#no").val(no);
			}
			if(type2){
				no=no+"SN.1";
				$("#no").val(no);
			}
			if(model1){
				no=no+".0";
				$("#no").val(no);
				var number1=$("#number1").val();
				if(number1){
					no1input=true;
				}else{
					no1input=false;
				}
				no=no+"."+number1;
				$("#no").val(no);
				if(no1input){
					var range0=$("#range0").val();
					no=no+"."+range0;
					$("#no").val(no);
				}
				
			}
			if(model2){
				no=no+".1";
				$("#no").val(no);
				var number2=$("#number2").val();
				if(number2){
					no2input=true;
					no=no+"."+number2;
					$("#no").val(no);
				}else{
					no2input=false;
				}
				var number3=$("#number3").val();
				if(number3){
					no3input=true;
					no=no+"."+number3;
					$("#no").val(no);
				}else{
					no3input=false;
				}
				if(no3input||no2input){
					var range1=$("#range1").val();
					no=no+"."+range1;
					$("#no").val(no);
				}
			}
		}		
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="base_info";
		var thirdMenu="_bsRulesInput";
	</script>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name = "main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="spc_base-info_bs-rules_save-detail">
						<button class='btn' onclick="submitForm('${spcctx}/base-info/bs-rules/save-detail.htm');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
					</security:authorize>
					<button class='btn' type="button" onclick="window.parent.$.colorbox.close();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
				</div>
				<div style="margin-top:15px; margin-left: 10px;">
					<form id="contentForm" name="contentForm" method="post" >
						<div><input type="hidden" value="${id}" name="id"></input></div>
						<table id="limit_table" style="width:100%;margin:0px;padding:0px;text-align: center;">
							<tr>
								<td valign="middle" style="width:100%;">
									<fieldSet> 
										<table>
										<tr>
											<td style="width:80px;text-align: right;">规则名称:</td>
											<td><input name="name" id="name" style="width:170px;" value="${bsRules.name}" /></td>
											<td style="width:80px;text-align: right;">规则编号:</td> 
											<td><input name="no" id="no" style="width:170px;" disabled="disabled" value="${bsRules.no}"/></td>
										</tr>
										<tr>
											<td colspan="2" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="autoWriteName" id="autoWriteName" value="是" type="checkbox" checked="checked"/>自动生成名称</td>
										</tr>
										</table>
									</fieldSet>			
								</td>
							</tr>
							<tr>
								<td>
									<fieldSet>
										<legend>控制图</legend>
										<table style="width: 100%;">
								    		<tr style="width:100%;">
								    			<td style="width:30%;"  align="left"><input type="radio" id="type1" onchange="setName();setNo();" name="type" value="0" <s:if test="%{type==\"0\"}">checked="checked"</s:if> />主控制图</td>
								    			<td style="width:70%;"  align="left"><input type="radio"  id="type2" onchange="setName();setNo();" name="type" value="1" <s:if test="%{type==\"1\"}">checked="checked"</s:if> />副控制图</td>
								    		</tr>
								    	</table>
									</fieldSet>
									<fieldSet>
										<legend>类型</legend>
										<table width="100%;">
								    		<tr>
								    			<td align="left" style="width:60%;">1、趋势图:
								    			<input type="radio" id="model1" name="model" onchange="setName();setNo();" value="0" <s:if test="%{model==\"0\"}">checked="checked"</s:if>/>连续&nbsp;&nbsp;&nbsp;&nbsp;
								    			<input type="text" name="number0" onchange="setName();setNo();" id="number1" style="width:50px;"/>&nbsp;&nbsp;点</td>
								    			<td align="left">
									    			<select name="range0" id="range0" style="width:170px;" onchange="setName();setNo();">
										    			<option value="1">上升</option>
										    			<option value="2">下降</option>
										    			<option value="3">不变</option>
										    			<option value="4">上下交替</option>
									    			</select>
								    			</td>
								    		</tr>
								    		<tr><td colspan="10" ><hr></hr></td></tr>
								    		<tr>
								    			<td align="left">2、运行图:
								    				<input type="radio" id="model2" onchange="setName();setNo();"  name="model" value="1" <s:if test="%{model==\"1\"}">checked="checked"</s:if>/>连续&nbsp;&nbsp;&nbsp;&nbsp;
								    				<input type="text" onchange="setName();setNo();" name="number11" id="number2" style="width:50px;"/>&nbsp;&nbsp;点中有
								    				<input type="text" onchange="setName();setNo();" name="number12" id="number3" style="width:50px;"/>&nbsp;&nbsp;点</td>
								    			<td align="left">
									    			<select name="range1" id="range1" style="width:170px;" onchange="setName();setNo();">
										    			<option value="5">位于A区</option>
										    			<option value="6">位于B区</option>
										    			<option value="7">位于C区</option>
										    			<option value="8">在控制限以外</option>
										    			<option value="9">在规格线以外</option>
										    			<option value="10">在中心线同侧B区以外</option>
										    			<option value="11">在中心线同侧C区以外</option>
										    			<option value="12">在中心线同侧</option>
										    			<option value="13">在中心线两侧B区以外</option>
										    			<option value="14">在中心线两侧C区以外</option>
									    			</select>
								    			</td>
								    		</tr>
									    	<%
											ValueStack valueStack=(ValueStack)request.getAttribute("struts.valueStack");
											BsRules bsRules=(BsRules)valueStack.findValue("bsRules");
											if(bsRules!=null){
											if(bsRules.getModel()!=null){
											if(bsRules.getModel().equals("0")){
												String expression=bsRules.getExpression();
												String[] expressions=expression.split("\\.");
												String number1=expressions[2];
												String range0=expressions[3];
											%>
											<script type="text/javascript">
												var number1=<%=number1%>;
												var range0=<%=range0%>;
												$("#number1").val(number1);
												$("#range0").val(range0);
											</script>
											<%}else if(bsRules.getModel().equals("1")){	
												String expression=bsRules.getExpression();
												String[] expressions=expression.split("\\.");
												String number2=expressions[2];
												String number3=expressions[3];
												String range1=expressions[4];
											%>
											<script type="text/javascript">
												var number2=<%=number2%>;
												var number3=<%=number3%>;
												var range1=<%=range1%>;
												$("#number2").val(number2);
												$("#number3").val(number3);
												$("#range1").val(range1);
											</script>
											<%}else if(bsRules.getModel().equals("2")){
												String expression=bsRules.getExpression();
												String[] expressions=expression.split("\\.");
												String number4=expressions[2];
											%>
											<script type="text/javascript">
												var number4=<%=number4%>;
												$("#number4").val(number4);
											</script>
											<%} else if(bsRules.getModel().equals("3")){
												String expression=bsRules.getExpression();
												String[] expressions=expression.split("\\.");
												String number5=expressions[2];
											%>
											<script type="text/javascript">
												var number5=<%=number5%>;
												$("#number5").val(number5);
											</script>
											<%}else if(bsRules.getModel().equals("3")){
												String expression=bsRules.getExpression();
												String[] expressions=expression.split("\\.");
												String number6=expressions[2];
											%>
											<script type="text/javascript">
												var number6=<%=number6%>;
												$("#number6").val(number6);
											</script>
											<%} %>
											<%} %>	
								    		<%} %>	
								    	</table>
									</fieldSet>
								</td>
							</tr>
						</table>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>