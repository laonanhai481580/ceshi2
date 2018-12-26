<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.List"%>
<%@page import="com.ambition.spc.jlanalyse.entity.JLResult"%>
<%@page import="com.ambition.spc.entity.CPKMoudle"%>
<%@page import="com.ambition.spc.jlanalyse.entity.JLSampleData"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<table style="width: 98%; margin-left: 10px; margin-top: 10px;" class="form-table-border-left">
	<caption style="text-align:center;padding-top:6px;padding-bottom:12px;font-weight:bold;font-size:16px;">过程统计量</caption>
	<%
		ValueStack valueStack=(ValueStack)request.getAttribute("struts.valueStack");
		int effectiveCapacity=(Integer)valueStack.findValue("effectiveCapacity");
		List jldatalist=(List)valueStack.findValue("jldatalist");
		int k=1;
	%>
	<tr style="height:24px;background:#99CCFF;font-weight:bold;height:29px;">
		<td>子组号</td>
		<%int a=0;
		for(int i=0;i<effectiveCapacity;i++){
			a++;%>
		<td><%="x"+a %></td>
		<%}%>
		<td>平均值</td>
		<td>最大值</td>
		<td>最小值</td>
	</tr>
	<%for(int i=0;i<jldatalist.size();i++){ 
		JLSampleData jl=(JLSampleData)jldatalist.get(i);
		double[] datas=jl.getData();%>
	<tr>
		<td><%=k%></td>
		<%for(int h=0;h<datas.length;h++){
			double data=datas[h];%>
			<td><%=data%></td>
		<%}%>
		<td><%=jl.getAverage()%></td>
		<td><%=jl.getMax()%></td>
		<td><%=jl.getMin()%></td>
	</tr>
	<%k++;}%>
</table>