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
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<caption style="font-size: 28px;padding-top: 10px;padding-bottom: 10px;">年度计划表</caption>
 	<tr>
		<th colspan="4" style="text-align:right;padding-bottom:4px;">
			计划号:${formNo}
		</th>
	</tr>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<tr>
			<td style="width:160px;text-align:center;">公司名称</td>
			<td style="width:200px;">
				<s:select list="companyNames"
					listKey="value" 
					listValue="name" 
					name="companyName" 
					id="companyName" 
					cssStyle="width:180px;"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td style="width:160px;text-align:center;">审核类型</td>
			<td style="width:200px;">
				<s:select list="auditTypes"
					listKey="value" 
					listValue="name" 
					name="auditType" 
					id="auditType" 
					cssStyle="width:160px;"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td style="width:160px;text-align:center;">审核日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="auditDate" name="auditDate" value="<s:date name='auditDate' format="yyyy-MM-dd"/>" />
			</td>
		</tr>
		<tr >
			<td style="width:160px;text-align:center;">体系类型</td>
			<td style="width:200px;" colspan="5">
				<s:checkboxlist
					theme="simple" list="systemNames" listKey="value" listValue="name"
					name="systemType" value="#request.systemTypeList">
				</s:checkboxlist>
			</td>
		</tr>		
		<tr>
			<td style="width:160px;text-align:center;">计划报告</td>
			<td style="width:200px;" colspan="5">
				<input type="hidden"  isFile="true" id="attachment" name="attachment" value="${attachment}"/>
			</td>
		</tr>
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="2"   id="remark" name="remark"  >${remark}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">制定人</td>
			<td style="width:200px;">
				${setMan}
			</td>
			<td style="width:160px;text-align:center;">审核</td>
			<td style="width:200px;">
				<input type="text" id="auditMan1" isTemp="true" isUser="true" hiddenInputId="auditMan1Login" style="float: left;"  name="auditMan1" value="${auditMan1}" />
				<input type="hidden" id="auditMan1Login" name="auditMan1Login" value="${auditMan1Login}" />
			</td>
			<td style="width:160px;text-align:center;">核准</td>
			<td style="width:200px;">
				<input type="text" id="auditMan2" isTemp="true" isUser="true" hiddenInputId="auditMan2Login" style="float: left;"  name="auditMan2" value="${auditMan2}" />
				<input type="hidden" id="auditMan2Login" name="auditMan2Login" value="${auditMan2Login}" />
			</td>
		</tr>
	</table>