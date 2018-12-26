<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.ambition.spc.jlanalyse.entity.JLResult"%>
<%@page import="com.ambition.spc.entity.CPKMoudle"%>
<%@page import="java.util.List"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%
	ValueStack valueStack=(ValueStack)request.getAttribute("struts.valueStack");
	String[] qualityFeatureNames=(String[])valueStack.findValue("qualityFeatureName");
	List results=(List)valueStack.findValue("result");
%> 
<h1 style="text-align:center;padding-top:6px;padding-bottom:12px;font-weight:bold;font-size:24px;">过程能力报表</h1>
<h1 style="text-align:left;padding-top:6px;padding-bottom:12px;font-weight:bold;font-size:15px;">日期范围:${subtitle }</h1>
<table style="width: <%=qualityFeatureNames==null?100:10*qualityFeatureNames.length>100?100:10*qualityFeatureNames.length%>%;" class="form-table-border" align="center" >
	<tr style="height:24px;background:#99CCFF;font-weight:bold;height:29px;">
	<%DecimalFormat df = new DecimalFormat("0.00000");
		if(qualityFeatureNames!=null){
			for(int i=0;i<qualityFeatureNames.length;i++){
				String qualityFeatureName=qualityFeatureNames[i];%>
				<td><%=qualityFeatureName %></td>
			<%}}%>
	</tr>
	<%if(results!=null){
	for(int j=0;j<results.size();j++){
		String[] result=(String[])results.get(j);%>
	<tr style="background-color: <%=j%2!=0?"#EAF2D3":""%>">
		<%for(int k=0;k<result.length;k++){
			String number=result[k];%>
			<td><%=k==0?number:df.format(Double.parseDouble(number))%></td>
		<%}%>
	</tr>
	<%}}%>
</table>