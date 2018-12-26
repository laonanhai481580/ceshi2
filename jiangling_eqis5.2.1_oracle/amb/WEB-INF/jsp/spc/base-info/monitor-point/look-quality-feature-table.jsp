<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>  
<%@page import="com.ambition.spc.entity.QualityFeature"%> 
<%@page import="com.ambition.spc.jlanalyse.entity.JLResult"%>
<%@page import="com.ambition.spc.entity.CPKMoudle"%> 
<%@page import="java.util.List"%>  
<%
    ValueStack valueStack1=(ValueStack)request.getAttribute("struts.valueStack");
    List<QualityFeature> qualityFeatures =(List<QualityFeature>) valueStack1.findValue("qualityFeatures");
    List<String> colors =(List<String>) valueStack1.findValue("colors");
    String isCpkModel =(String) valueStack1.findValue("isCpkModel");
    JLResult jlResult =(JLResult) valueStack1.findValue("jLResult");
    CPKMoudle cpkMoudle=(CPKMoudle)valueStack1.findValue("cpkMoudle");
%>
<table style="background:#efefef;" class="form-table-border-left" >
<%if(isCpkModel!=null){		
	if(isCpkModel.equals("N")){ %>
	<tr style="height:24px;background:#99CCFF;font-weight:bold;height:29px;"><td>监控状态</td><td>质量参数名称</td></tr>
	<%if(qualityFeatures!=null){
		for(int i=0;i<qualityFeatures.size();i++){ 
		QualityFeature q=qualityFeatures.get(i);
		String color=(String)colors.get(i);%>
	<tr>
	<td><div  class='qlight <%=color%>-qlight'  style='cursor:pointer;' ></div></td>
	<td onclick="dataAnalysis(<%=q.getId()%>);"><span style="color: blue;cursor:pointer;"><%=q.getName() %></span></td>
	</tr>
	<%}}}else{%>
	<tr><td>CPK</td><td><%= cpkMoudle==null?0:cpkMoudle.getCpk()%></td></tr>
	<tr><td>max</td><td><%= jlResult==null?0:jlResult.getMax()%></td></tr>
	<tr><td>min</td><td><%= jlResult==null?0:jlResult.getMin()%></td></tr>
	<tr><td>avg</td><td><%= jlResult==null?0:jlResult.getAverage()%></td></tr>
	<%}}%>
</table>