<%@page import="sun.java2d.pipe.SpanShapeRenderer.Simple"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar calendar = Calendar.getInstance();
	String dateStr = sdf.format(calendar.getTime());
	String user = ContextUtils.getUserName();
	
%> 
	<table class="form-table-without-border" style="width:100%;"> 
		<caption><h2>HSF试验委托单</h2></caption>
		<input type="hidden" id="currentActivityName" name="currentActivityName" value="${currentActivityName }" />
 	<input  type="hidden" id="id" name="id" value="${id}" /> 
		<input type="hidden" id="factoryClassify" name="factoryClassify" value="${factoryClassify}" />
	<input name="taskId" id="taskId" value="${taskId}" type="hidden" />
		<caption style="text-align:left;padding-bottom:4px;">表单.NO:${formNo}</caption>
	</table>
	<table class="form-table-border-left" style="border:0px;width:100%;" >
		<tr>
			<td style="width:10%;text-align:center;">申请人</td>
			<td style="width:10%"><input id="consignor" name="consignor" readonly="ture" value="${consignor }"/></td>
			<td style="text-align:center;">联系方式</td>
			<td ><input id="contact" name="contact" value="${contact}"/></td>
			<td style="width:10%;text-align:center;">申请部门</td>
			<td><input id="consignorDept" name="consignorDept" readonly="ture" value="${consignorDept}"/></td>
			<td style="width:10%;text-align:center;">申请日期</td>
			<td ><input id="consignableDate" name="consignableDate" isDate="true" value="<s:date name='consignableDate' format="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td style="text-align:center;">产品阶段</td>
			<td colspan="5">
			<s:radio cssStyle="margin-left:8px;" name="category" id="category" value="#request.category" theme="simple" list="categorys" listKey="name" listValue="value"/>
			</td>
			<td style="text-align:center;">样品类型</td>
			<td>
				<s:select list="categoryTexts" style="width:100px;"
					listKey="value" 
					listValue="value" 
					name="categoryText" 
					id="categoryText"
					emptyOption="true" 
					onchange=""
					theme="simple">
				</s:select>
			</td>
		</tr>
		<tr>
			<td style="text-align:center;">客户</td>
			<td ><input id="clientName" name="clientName" value="${clientName}"/></td>
			<td style="text-align:center;">供应商</td>
			<td ><input id="supplierName" name="supplierName" value="${supplierName}"/></td>
			<td style="text-align:center;">紧急程度</td>
			<td>
				<s:select list="severitys" style="width:100px;"
					listKey="value" 
					listValue="value" 
					name="degree" 
					id="degree"
					emptyOption="true" 
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td></td><td></td>
		</tr>
		<tr>
			<td style="width:10%;text-align:center;">实验目的:</td>
			<td colspan="7"><textarea id="purpose" name="purpose">${purpose}</textarea></td>
		</tr>
		<tr>
			<td style="width:10%;text-align:center;">样品数量</td>
			<td><input id="quantity" name="quantity" value="${quantity }" class="{number:true,messages:{number:'请输入有效数字!'}}" /></td>
			<td style="width:10%;text-align:center;">附件</td>
			<td ><input type="hidden" isFile="true" name="aimFile" id="aimFile" value="${aimFile }"></td>
			<td style="width:15%;text-align: center;">实验室保留样品:</td>
			<td>
				<input  type="radio" id="d1" name="sampleHandling" value="是"  <s:if test='%{sampleHandling=="是"}'>checked="true"</s:if> title="是"/><label for="d1">是</label>
				<input  type="radio" id="d2" name="sampleHandling" value="否"  <s:if test='%{sampleHandling=="否"}'>checked="true"</s:if> title="否"/><label for="d2">否</label>
			</td>
			<td style="text-align:center;">管理编号</td>
			<td ><input id="managerAssets" name="managerAssets" value="${managerAssets}" />
			<input type="hidden" id="epmState" name="epmState" value="${epmState }"/></td>
		</tr>
		<tr>
			<td colspan="8" style="text-align:center;">样品描述 </td>
		</tr>
		<tr>
			<td colspan="8" style="padding:0px;" id="checkSub">
			<div style="width:100%; overflow-x:auto;overflow-y:hidden;">
				<%@ include file="material-sub.jsp" %>
			</div>
				
			</td>
		</tr>
		<tr>
			<td style="text-align:center;">确认部门</td>
			<td colspan="3">
				<input id="confirmDept" name="confirmDept" hiddenInputId="confirmDeptLoing" style="float: left;" isUser="true" value="${confirmDept }"/>
				<input type="hidden" id="confirmDeptLoing" name="confirmDeptLoing" value="${confirmDeptLoing }">
			</td>
			<td style="text-align:center;">实验室排程</td>
			<td colspan="3">
				<input id="schedule" name="schedule" hiddenInputId="scheduleLoing" style="float: left;" isUser="true" value="${schedule }"/>
				<input type="hidden" id="scheduleLoing" name="scheduleLoing" value="${scheduleLoing }">
			</td>
		</tr>
		<tr>
			<td style="text-align:center;">实验室测试员</td>
			<td colspan="3">
				<input id="tester" name="tester" hiddenInputId="testerLoing" style="float: left;" isUser="true" value="${tester }"/>
				<input type="hidden" id="testerLoing" name="testerLoing" value="${testerLoing }">
			</td>
			<td style="text-align:center;">报告审核</td>
			<td colspan="3">
				<input id="reportAudit" name="reportAudit" hiddenInputId="reportAuditLoing" style="float: left;" isUser="true" value="${reportAudit }"/>
				<input type="hidden" id="reportAuditLoing" name="reportAuditLoing" value="${reportAuditLoing }">
			</td>
			
		</tr>
		
		<tr>
			<td style="text-align:center;">测试结果</td>
			<td >
				<input id="textResult" name="textResult" value="${textResult }" style="width: 90%;border:none;background: none;" readonly="readonly"/>
<%-- 				<input type="hidden" id="textResult" name="textResult" value="${textResult }"/> --%>
			</td>
			<td style="text-align:center;">进货检验单号</td>
			<td ><input id="inspectionNo" name="inspectionNo" value="${inspectionNo}" style="width: 90%;border:none;background: none;" readonly="readonly"/>
				 <input id="inspectionId" name="inspectionId" value="${inspectionId}" type="hidden"/></td>
			<td colspan="4"></td>
		</tr>
	</table>
