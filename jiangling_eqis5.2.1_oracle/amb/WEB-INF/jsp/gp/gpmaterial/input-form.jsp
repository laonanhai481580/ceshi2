<%@page import="sun.java2d.pipe.SpanShapeRenderer.Simple"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page import="com.norteksoft.acs.service.AcsUtils"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar calendar = Calendar.getInstance();
	String dateStr = sdf.format(calendar.getTime());
	String user = ContextUtils.getUserName();
%> 
<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
	<tfoot>
		<caption><h2><s:text name="gp.gpmaterial.report" /></h2></caption>
		<input type="hidden" id="currentActivityName" name="currentActivityName" value="${currentActivityName }" />
 		<input  type="hidden" id="id" name="id" value="${id}" /> 
 		<input  type="hidden" id="approvalId" name="approvalId" value="${approvalId}" /> 
		<input name="taskId" id="taskId" value="${taskId}" type="hidden" /> 
		<input type="hidden" id=formNo name="formNo" value="${formNo}" />
		<caption style="text-align:left;padding-bottom:4px;">表单.NO:${formNo}</caption>
	</tfoot>
	<tbody>
	<tr>
		<td style="text-align:center;"><s:text name="gp.form.供应商编码"/></td>
		<td><input id="supplierCode" name="supplierCode" onclick="supplierClick();" value="${supplierCode}"/></td>
		<td style="text-align:center;"><s:text name="gp.form.供应商名称"/></td>
		<td>
			<input id="supplierName" name="supplierName" hiddenInputId="supplierLoginName" value="${supplierName}"/>
			<input type="hidden" name="supplierLoginName" id="supplierLoginName"  value="${supplierLoginName}" />
		</td>
		<td style="text-align:center;"><s:text name="gp.form.供应商邮箱"/></td>
		<td><input id="supplierEmail" name="supplierEmail" value="${supplierEmail}"/></td>
	</tr>
	<tr>
		<td style="text-align:center;"><s:text name="gp.form.物料名称"/></td>
		<td><input id="materialName" name="materialName" value="${materialName}"/></td>
		<td style="text-align:center;"><s:text name="gp.form.物料编码"/></td>
		<td><input id="materialCode" name="materialCode" value="${materialCode}"/>
			<a class="small-button-bg" onclick="testSelect(this)" ><span class="ui-icon ui-icon-search"></span></a>
		</td>
		<td style="text-align:center;"><s:text name="gp.form.物料类别"/></td>
		<td><input id="materialType" name="materialType" value="${materialType}"/></td>
	</tr>
		
	<tr>
		<td style="text-align:center;"><s:text name="gp.form.环保基准"/></td>
		<td>
			<s:select list="benchmarks" 
				listKey="value" 
				listValue="value" 
				name="benchmark" 
				id="benchmark"
				emptyOption="" 
				onchange=""
				theme="simple">
			</s:select>
		</td>
		<td style="text-align:center;"><s:text name="gp.form.环保属性"/></td>
		<td>
			<s:select list="attributes" 
				listKey="value" 
				listValue="value" 
				name="attribute" 
				id="attribute"
				emptyOption="" 
				onchange=""
				theme="simple">
			</s:select>
		</td>
		<td style="text-align:center;"><s:text name="gp.form.创建日期"/></td>
		<td><input id="supplierDate" name="supplierDate" isDate="true" value="<s:date name='supplierDate' format="yyyy-MM-dd"/>"/></td>
	</tr>
	<tr>
		<td style="text-align:center;"><s:text name="gp.form.回复交期"/></td>
		<td><input id="revertDate" name="revertDate" isDate="true" value="<s:date name='revertDate' format="yyyy-MM-dd"/>"/></td>
		<td style="text-align:center;"><s:text name="gp.form.备注"/></td>
		<td><textarea id="remark" name="remark" style="width:80%;">${remark}</textarea></td>
		<td style="text-align:center;"><s:text name="gp.form.审核日期"/></td>
		<td><input id="auditorDate" name="auditorDate" isDate="true" value="<s:date name='auditorDate' format="yyyy-MM-dd"/>"/></td>
	</tr>
	<tr>
		<td style="text-align:center;"><s:text name="gp.form.发起人"/></td>
		<td>
			<input id="initiator" name="initiator" hiddenInputId="initiatorLogin" style="float: left;" isUser="true" value="${initiator }"/>
			<input type="hidden" id="initiatorLogin" name="initiatorLogin" value="${initiatorLogin }"/>
		</td>
		
		<td style="text-align:center;"><s:text name="gp.form.确认人"/></td>
		<td >
			<input id="confirmDept" name="confirmDept" hiddenInputId="confirmDeptLoing" style="float: left;" isUser="true" value="${confirmDept }"/>
			<input type="hidden" id="confirmDeptLoing" name="confirmDeptLoing" value="${confirmDeptLoing }"/>
		</td>
		<td style="text-align:center;"><s:text name="gp.form.审核人"/></td>
		<td >
			<input id="auditor" name="auditor" hiddenInputId="auditorLogin" style="float: left;" isUser="true" value="${auditor }"/>
			<input type="hidden" id="auditorLogin" name="auditorLogin" value="${auditorLogin }"/>
			<input type="hidden" id="auditorDate" name="auditorDate" isDate="true" value="<s:date name='auditorDate' format="yyyy-MM-dd"/>"/>
		</td>
		
		
	</tr>
	<tr>
		<td style="text-align:center;"><s:text name="gp.form.产品重量"/>（<s:text name="gp.form.单位"/>：mg）</td>
		<td><input id="productWeight" name="productWeight" value="${productWeight}"/></td>
		<td style="text-align:center;"><s:text name="gp.form.供应商料号"/></td>
		<td><input id="supplierPart" name="supplierPart" value="${supplierPart}"/></td>
		<td style="text-align:center;"><s:text name="gp.form.授权宣告人"/></td>
		<td><input id="declaring" name="declaring"  value="${declaring}"/></td>
	</tr>
	<tr>
	<td colspan="6" style="padding:0px;" id="checkItemsParent" >
		<div style="width:100%; overflow-x:auto;overflow-y:hidden;">
			<%@ include file="material-sub.jsp" %>
		</div>
	</td>
	</tr>
	<tr>
	<td colspan="6" style="padding:0px;"  >
		<div style=" overflow-x:auto;overflow-y:hidden;">
		<table class="form-table-border-left" style="border:0px; table-layout: fixed;">
			<tr>
				<td style="text-align:center;"><s:text name="gp.form.产品拆解图"/></td>
				<td colspan="2"><input style="width:100%;" type="hidden" isFile="true" name="productFile" value="${productFile }" /></td>
				<td style="text-align:center;">BOM</td>
				<td colspan="2"><input style="width:100%;" type="hidden" isFile="true" name="bomFile" value="${bomFile }" /></td>
			</tr>
			<tr>
				<td style="text-align:center;">REACH SVHC调查表</td>
				<td colspan="2"><input style="width:100%;" type="hidden" isFile="true" name="surveyFile" value="${surveyFile }" /></td>
				<td style="text-align:center;">REACH SVHC申报表</td>
				<td colspan="2"><input style="width:100%;" type="hidden" isFile="true" name="declareFile" value="${declareFile }" /></td>
			</tr>
			<tr>
				<td style="text-align:center;"><s:text name="gp.form.检测报告压缩包"/></td>
				<td colspan="2"><input style="width:100%;" type="hidden" isFile="true" name="detectionReport" value="${detectionReport }" /></td>
				<td style="text-align:center;"><s:text name="gp.form.产品成分宣告"/></td>
				<td colspan="2"><input style="width:100%;" type="hidden" isFile="true" name="matterMaterials" value="${matterMaterials }" /></td>
			</tr>
			</table>
		</div>
	</td>
	</tr>
	
	</tbody>
	
</table>
