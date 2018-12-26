<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.ambition.spc.jlanalyse.entity.JLResult"%>
<%@page import="com.ambition.spc.entity.CPKMoudle"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<table style="width: 98%; margin-left: 10px; margin-top: 10px;" class="form-table-border-left">
	<caption style="text-align:center;padding-top:6px;padding-bottom:12px;font-weight:bold;font-size:16px;">过程统计量</caption>
	<tr style="height:24px;background:#99CCFF;font-weight:bold;height:29px;">
		<td>Mean</td>
		<td>Max</td>
		<td>Min</td>
		<td>Range</td>
		<td>StdDev</td>
		<td>Skewness</td>
		<td>Kurtosis</td>
		<td>Cp</td>
		<td>Cr</td>
		<td>K</td>
	</tr>
	<%
		ValueStack valueStack=(ValueStack)request.getAttribute("struts.valueStack");
		JLResult jlResult =(JLResult) valueStack.findValue("jLResult");
		CPKMoudle cpkMoudle=(CPKMoudle)valueStack.findValue("cpkMoudle");
		DecimalFormat df = new DecimalFormat("0.00000");
	%>
	<tr>
		<td><%=df.format(jlResult==null?0:jlResult.getAverage()) %></td>
		<td><%=df.format(jlResult==null?0:jlResult.getMax()) %></td>
		<td><%=df.format(jlResult==null?0:jlResult.getMin()) %></td>
		<td><%=df.format(jlResult==null?0:jlResult.getR()) %></td>
		<td><%=df.format(jlResult==null?0:jlResult.getS()) %></td>
		<td><%=df.format(jlResult==null?0:jlResult.getSkewness()) %></td>
		<td><%=df.format(jlResult==null?0:jlResult.getKurtosis()) %></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getCp())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getCr())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getK())%></td>
	</tr>
	<tr style="height:24px;background:#99CCFF;font-weight:bold;height:29px;">
		<td>Cpu</td>
		<td>Cpl</td>
		<td>Cpk</td>
		<td>Cpm</td>
		<td>Zu_Cap</td>
		<td>Zl_Cap</td>
		<td>Fpu_Cap</td>
		<td>Fpl_Cap</td>
		<td>Fp_Cap</td>
		<td>Pp</td>
	</tr>
	<tr>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getCpu())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getCpl())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getCpk())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getCpm())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getZu_cap())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getZl_cap())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getFpu_cap())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getFpl_cap())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getFp_cap())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getPp())%></td>
	</tr>
	<tr style="height:24px;background:#99CCFF;font-weight:bold;height:29px;">
		<td>Pr</td>
		<td>Ppu</td>
		<td>Ppl</td>
		<td>Ppk</td>
		<td>Ppm</td>
		<td>Zu_Perf</td>
		<td>Zl_Perf</td>
		<td>Fpu_Perf</td>
		<td>Fpl_Perf</td>
		<td>Fp_Perf</td>
	</tr>
	<tr>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getPr())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getPpu())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getPpl())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getPpk())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getPpm())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getZu_pref())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getZl_pref())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getFpu_pref())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getFpl_pref())%></td>
		<td><%=df.format(cpkMoudle==null?0:cpkMoudle.getFp_pref())%></td>
	</tr>
</table>