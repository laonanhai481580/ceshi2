<%@page import="sun.java2d.pipe.SpanShapeRenderer.Simple"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
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
		<caption style="font-size: 28px;padding-top: 10px;padding-bottom: 10px;">内审计划与实施</caption>
<%-- 	<tr>
		<th colspan="4" style="text-align:right;padding-bottom:4px;">
			编号:${formNo}
		</th>
	</tr> --%>
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
			<td style="width:160px;text-align:center;">计划号</td>
			<td style="width:200px;">${formNo}</td>
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
			<td style="width:160px;text-align:center;">年度计划表单</td>
			<td style="width:200px;">
				<input type="text"   id="yearAuditNo" name="yearAuditNo" value="${yearAuditNo}"/>
			</td>
			<td style="width:160px;text-align:center;">审核日期</td>
			<td style="width:200px;">
			<input  type="text" isDate="true" id="auditDate" name="auditDate" value="<s:date name='auditDate' format="yyyy-MM-dd"/>" />
			</td>
			<td style="width:160px;text-align:center;"></td>
			<td style="width:200px;">
			</td>			
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">内审计划</td>
		</tr>	
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="2"   id="remark1" name="remark1"  >${remark1}</textarea>
			</td>
		</tr>	
		<tr>
			<td style="width:160px;text-align:center;">附件上传</td>
			<td style="width:200px;" colspan="5">
				<input type="hidden"  isFile="true" id="attachment1" name="attachment1" value="${attachment1}"/>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">提交人</td>
			<td style="width:200px;">
				${submitter1}
			</td>
			<td style="width:160px;text-align:center;">审核</td>
			<td style="width:200px;">
				<input type="text" id="auditMan1" isTemp="true" isUser="true"  hiddenInputId="auditMan1Login" style="float: left;"  name="auditMan1" value="${auditMan1}" />
				<input type="hidden" id="auditMan1Login" name="auditMan1Login" value="${auditMan1Login}" />
			</td>
			<td style="width:160px;text-align:center;">核准</td>
			<td style="width:200px;">
				<input type="text" id="approvedMan1" isTemp="true" isUser="true" hiddenInputId="approvedMan1Login" style="float: left;"  name="approvedMan1" value="${approvedMan1}" />
				<input type="hidden" id="approvedMan1Login" name="approvedMan1Login" value="${approvedMan1Login}" />
			</td>
		</tr>
		
		
		
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">内审检查表</td>
		</tr>	
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="2"   id="remark2" name="remark2"  >${remark2}</textarea>
			</td>
		</tr>
		<tr>
			<td style="text-align: center;">附件上传
			</td>
			<td colspan="5" >
				<input type="hidden"  isFile="true" id="attachment2" name="attachment2" value="${attachment2}"/>
			</td>		
		</tr>					
		<tr>
			<td style="width:160px;text-align:center;">提交人</td>
			<td style="width:200px;">
				<input type="text" id="submitter2" isTemp="true" isUser="true" hiddenInputId="submitter2Login" style="float: left;"  name="submitter2" value="${submitter2}" />
				<input type="hidden" id="submitter2Login" name="submitter2Login" value="${submitter2Login}" />
			</td>
			<td style="width:160px;text-align:center;">审核</td>
			<td style="width:200px;">
				<input type="text" id="auditMan2" isTemp="true" isUser="true" hiddenInputId="auditMan2Login" style="float: left;"  name="auditMan2" value="${auditMan2}" />
				<input type="hidden" id="auditMan2Login" name="auditMan2Login" value="${auditMan2Login}" />
			</td>
			<td style="width:160px;text-align:center;">核准</td>
			<td style="width:200px;">
				<input type="text" id="approvedMan2" isTemp="true" isUser="true" hiddenInputId="approvedMan2Login" style="float: left;"  name="approvedMan2" value="${approvedMan2}" />
				<input type="hidden" id="approvedMan2Login" name="approvedMan2Login" value="${approvedMan2Login}" />
			</td>
		</tr>	
		
		
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">内审报告</td>
		</tr>	
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="2"   id="remark3" name="remark3"  >${remark3}</textarea>
			</td>
		</tr>	
		<tr>
			<td style="width:160px;text-align:center;">附件上传</td>
			<td style="width:200px;" colspan="5">
				<input type="hidden"  isFile="true" id="attachment3" name="attachment3" value="${attachment3}"/>
			</td>
		</tr>			
		<tr>
			<td style="width:160px;text-align:center;">发布部门</td>
			<td style="width:200px;text-align: center;" colspan="3" >
				<input type="text"   id="department" isDept=true name="department" value="${department}" style="width: 90%;border:none;text-align: center;background: none;"/> 
			</td>
			<td style="width:160px;text-align:center;">核准</td>
			<td style="width:200px;">
				<input type="text" id="approvedMan3" isTemp="true" isUser="true" hiddenInputId="approvedMan3Login" style="float: left;"  name="approvedMan3" value="${approvedMan3}" />
				<input type="hidden" id="approvedMan3Login" name="approvedMan3Login" value="${approvedMan3Login}" />
			</td>
		</tr>	
	</table>