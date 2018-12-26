<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script language="javascript" src="${ctx}/widgets/lodop/LodopFuncs.js"></script>
<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
<script type="text/javascript" src="${ctx}/js/workflow-form-0.9.js"></script>
<c:set var="actionBaseCtx" value="${iqcctx}/experimental-delegation" />
<style type="text/css">
	.tableTd{
		text-align: center !important;;
	}
	ul{
		margin:0px;
		padding:0px;
	}
	li{
		margin:0px;
		padding:0px;
		text-decoration: none;
		list-style: none;
	}
</style>
<script type="text/javascript">
		var hasInitHistory = false;
		isUsingComonLayout=false;
		$(document).ready(function() {
			addFormValidate('${fieldPermission}', 'inputForm');
			//初始化表单元素,格式如
			setTimeout(function(){
				$("#opt-content").height($(window).height()-70);
			},500);
		});
		function closeBtn(){
			window.parent.$.colorbox.close();
		}
		
		function submit(){
			if($("#inputForm").valid()){
				var url="${iqcctx}/experimental-delegation/submit.htm";
				$('#inputForm').attr('action',url);
				$("#message").html("<b>数据保存中,请稍候... ...</b>");
				$(".opt-btn .btn").attr("disabled",true);
				$('#inputForm').submit();
			}else{
				var error = $("#inputForm").validate().errorList[0];
				$(error.element).focus();
			}
		}
		function updateDisabledStauts(obj){
			var objVal=$(obj).val();
			if(objVal=="hsf"){
				$("td[name=ort]").next().children().find(":input").attr("disabled","disabled");
				$("td[name=hsf]").next().children().find(":input").removeAttr("disabled");
				$("a[name]").hide();
			}else if(objVal=="ort"){
				$("td[name=hsf]").next().children().find(":input").attr("disabled","disabled");
				$("td[name=ort]").next().children().find(":input").removeAttr("disabled");
				$("a").show();
			}
		}
	</script>
</head>
<body>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<button class="btn" onclick="submit();">
					<span><span><b class="btn-icons btn-icons-submit"></b>提交</span></span>
				</button>
				<button class="btn" onclick="closeBtn();">
					<span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span>
				</button>
			</div>
			<div id="opt-content" style="overflow-y: auto;">
				<form method="post" id="inputForm" name="inputForm" action="">
					<h3 style="text-align: center;font-size: 25px;font-weight: bold;">实验委托单<br/>Relaibility Evaluation Request Form</h3>
					<table  class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
							<caption style="text-align: right;font-weight: bold;">编码:${experimentalNo}</caption>
							<tr>
								<td class="tableTd" style="text-align: center;width: 15%;"><b>样品名称</b><br/>sample</td>
								<td style="width: 15%;">
									<input id="sampleName" name="sampleName" value="${sampleName}"/>
								</td>
								<td style="text-align: center;width: 15%;"><b>规格型号</b><br/>specification</td>
								<td style="width: 20%;">
									<input id="specificationModel" name="specificationModel" value="${specificationModel}"/>
								</td>
								<td style="text-align: center;width: 15%;"><b>供应商</b><br/>supplier</td>
								<td style="width: 20%;">
									<input type="hidden" id="supplierCode" name="supplierCode" value="${supplierCode}"/>
									<input id="supplierName" name="supplierName" value="${supplierName}"/>
								</td>
							</tr>
							<tr>
								<td class="tableTd"><b>机种编号</b><br/>Product NO.</td>
								<td>
									<input id="meachineNo" name="meachineNo" value="${meachineNo}"/>
								</td>
								<td class="tableTd"><b>客户编号</b><br/>sample amount</td>
								<td>
									<input id="coustomerCode" name="coustomerCode" value="${coustomerCode}"/>
									<input type="hidden" id="coustomerName" name="coustomerName" value="${coustomerName}"/>
								</td>
								<td class="tableTd"><b>批号</b><br/>Consignable date</td>
								<td>
									<input id="batchNo" name="batchNo" value="${batchNo}"/>
								</td>
							</tr>
							<tr>
								<td class="tableTd"><b>紧急度</b><br/>Emergency degree</td>
								<td>
									<s:iterator value="emergency_degree" var="option">
										<input type="radio" id="${option.id}" name="emergencyDegree" value="${option.name}" <s:if test="%{#option.value.equals(emergencyDegree)}">checked="checked"</s:if>/>
										<label for="${option.id}">${option.name}</label>
									</s:iterator>
								</td>
								<td class="tableTd"><b>样品数量</b><br/>sample amount</td>
								<td>
									<input id="sampleAmount" name="sampleAmount" value="${sampleAmount}"/>
								</td>
								<td class="tableTd"><b>申请日期</b><br/>Consignable date</td>
								<td>
									<input id="consignableDate" name="consignableDate" value='<s:date name="consignableDate" format="yyyy-MM-dd HH:mm"/>'/>
								</td>
							</tr>
							<tr>
								<td class="tableTd"><b>申请人</b><br/> Consignor</td>
								<td>
									<input id="consignor" name="consignor" value="${consignor}" readonly="readonly"/>
								</td>
								<td class="tableTd"><b>申请部门</b><br/> Department</td>
								<td >
									<input id="consignDev" name="consignDev" value="${consignDev}" readonly="readonly"/>
								</td>
								<td colspan="2"></td>
							</tr>
							<tr>
								<td class="tableTd"><b>样品描述</b><br/> Sample Discription</td>
								<td colspan="5">
									<textarea rows="3" cols="2" name="sampleDiscription">${sampleDiscription}</textarea>
								</td>
							</tr>
							<tr>
								<td class="tableTd" rowspan="2"><b>测试项目</b><br/>Test Items</td>
								<td class="tableTd" name="hsf"><b><input type="radio" id="hsf" name="hsfOrt" onclick="updateDisabledStauts(this);" value="hsf" <s:if test="%{hsfOrt.equals(hsf)}"></s:if>/>HSF</b></td>
								<td colspan="4">
									<ul>
									<%int a=1; %>
									<s:iterator value="hsfDetailItem" var="option">
										<li>
											<%
												String temp=(String)request.getAttribute("String"+a); 
											%>
											<c:set var="setA" value="<%=a %>" scope="page"></c:set>
											<c:set var="S" value="<%="String"+a%>"></c:set>
											<input type="checkbox" id="${option.id}"  name="String<%=a %>" value="${option.name}" <s:if test="%{#option.value.equals([s])}">checked="checked"</s:if>/>
											<label for="${option.id}" style="font-size: 14px;">${option.name}</label>&nbsp;&nbsp;
											<input type="hidden" name="file<%=a %>" 
											uploadBtnText="${option.name}上传附件" id="file<%=a %>" isFile="true" value="${file[setA]}"/>
											<hr style="width:100%;" color="#8dc0e7"/>
										</li>
										<%a++; %>
									</s:iterator>
									</ul>
								</td>
							</tr>
							<tr>
								<td class="tableTd" name="ort"><b><input type="radio" id="ort" name="hsfOrt" onclick="updateDisabledStauts(this);" value="ort" <s:if test="%{ort.equals(是)}"></s:if>/>ORT</b></td>
								<td colspan="4">
									<table  class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
										<tr>
											<td class="tableTd"><b>操作</b></td>
											<td class="tableTd"><b>ORT测试项目</b></td>
											<td class="tableTd"><b>测试结果</b></td>
										</tr>
										<s:iterator value="ortItems">
										<tr tableName="ortItems" class="ortItems" zbtr=true>
											<td class="tableTd">
												<div style="margin:0 auto;width: 42px;">
									      			<a class="small-button-bg" style="float:left;" name="add" onclick="addRowHtml(this)" title="添加项目"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
													<a class="small-button-bg" style="margin-left:2px;float:left;" name="remove" onclick="removeRowHtml(this)" title="删除项目"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
												</div>
											</td>
											<td class="tableTd">
												<input id="ortItemName" fieldName="ortItemName" name="ortItemName" value="${ortItemName}"/>
											</td>
											<td class="tableTd">
												<s:select list="iqc_okorng" 
													  theme="simple"
													  listKey="name" 
													  listValue="name" 
													  labelSeparator=""
													  name="ortResult"
													  emptyOption="false"></s:select>
											</td>
										</tr>
										</s:iterator>
									</table>
								</td>
							</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>