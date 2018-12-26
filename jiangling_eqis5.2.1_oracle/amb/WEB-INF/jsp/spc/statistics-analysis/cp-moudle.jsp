<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.ambition.spc.jlanalyse.entity.JLResult"%>
<%@page import="com.ambition.spc.entity.CPKMoudle"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<table style="width: 98%; margin-left: 2px; margin-top: 0px;" class="form-table-border-left">
<%
	ValueStack valueStack=ActionContext.getContext().getValueStack();
	JLResult jlResult =(JLResult) valueStack.findValue("jLResult");
	CPKMoudle cpkMoudle=(CPKMoudle)valueStack.findValue("cpkMoudle");
	DecimalFormat  df=(DecimalFormat)valueStack.findValue("decimalFormat");
	if(df==null){
		df=new DecimalFormat("0.00000");
	}
%>
	<tr ><td colspan="2" style="text-align: center"><strong>统计量</strong></td></tr>
	<tr  align="left">
	<td width="40%" ><strong>Mean</strong></td>
	<td width="*%" align="right"><%=df.format(jlResult==null?0:jlResult.getAverage()) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Max</strong></td>
	<td width="*%" align="right"><%=df.format(jlResult==null?0:jlResult.getMax()) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Min</strong></td>
	<td width="*%" align="right"><%=df.format(jlResult==null?0:jlResult.getMin()) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Range</strong></td>
	<td width="*%" align="right"><%=df.format(jlResult==null?0:jlResult.getR()) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>StdDev</strong></td>
	<td width="*%" align="right"><%=df.format(jlResult==null?0:jlResult.getS()) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Sigma</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getSigma()) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Skewness</strong></td>
	<td width="*%" align="right"><%=df.format(jlResult==null?0:jlResult.getSkewness()) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Kurtosis</strong></td>
	<td width="*%" align="right"><%=df.format(jlResult==null?0:jlResult.getKurtosis()) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Cp</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getCp())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Cr</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getCr())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>K</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getK())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Cpu</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getCpu())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Cpl</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getCpl())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Cpk</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getCpk())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Cpm</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getCpm())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Zu_Cap</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getZu_cap())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Zl_Cap</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getZl_cap())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Fpu_Cap</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getFpu_cap())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Fpl_Cap</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getFpl_cap())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Fp_Cap</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getFp_cap())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Pp</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getPp())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Pr</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getPr())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Ppu</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getPpu())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Ppl</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getPpl())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Ppk</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getPpk())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Ppm</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getPpm())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Zu_Perf</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getZu_pref())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Zl_Perf</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getZl_pref())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Fpu_Perf</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getFpu_pref())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Fpl_Perf</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getFpl_pref())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Fp_Perf</strong></td>
	<td width="*%" align="right"><%=df.format(cpkMoudle==null?0:cpkMoudle.getFp_pref())%></td>
	</tr>
</table>