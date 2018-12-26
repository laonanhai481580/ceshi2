<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.ambition.spc.jlanalyse.entity.JLResult"%>
<%@page import="com.ambition.spc.entity.CPKMoudle"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<table style="width: 98%; margin-left: 10px; margin-top: 10px;" class="form-table-border-left">
<%
	ValueStack a=(ValueStack)request.getAttribute("struts.valueStack");
	JLResult jlResult1 =(JLResult) a.findValue("jLResult");
	CPKMoudle cpkMoudle11=(CPKMoudle)a.findValue("cpkMoudle");
	DecimalFormat df1=(DecimalFormat)a.findValue("decimalFormat");
	if(df1==null){
		df1=new DecimalFormat("0.00000");
	}
%>
<tr><td colspan="2" align="left"><strong>统计量</strong></td></tr>
	<tr  align="left">
	<td width="40%" >Mean</td>
	<td width="*%" align="right"><%=df1.format(jlResult1==null?0:jlResult1.getAverage()) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Max</td>
	<td width="*%" align="right"><%=df1.format(jlResult1==null?0:jlResult1.getMax()) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Min</td>
	<td width="*%" align="right"><%=df1.format(jlResult1==null?0:jlResult1.getMin()) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Range</td>
	<td width="*%" align="right"><%=df1.format(jlResult1==null?0:jlResult1.getR()) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" >StdDev</td>
	<td width="*%" align="right"><%=df1.format(jlResult1==null?0:jlResult1.getS()) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Skewness</td>
	<td width="*%" align="right"><%=df1.format(jlResult1==null?0:jlResult1.getSkewness()) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Kurtosis</td>
	<td width="*%" align="right"><%=df1.format(jlResult1==null?0:jlResult1.getKurtosis()) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Cp</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getCp())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Cr</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getCr())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >K</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getK())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Cpu</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getCpu())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Cpl</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getCpl())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Cpk</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getCpk())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Cpm</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getCpm())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Zu_Cap</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getZu_cap())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Zl_Cap</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getZl_cap())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Fpu_Cap</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getFpu_cap())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Fpl_Cap</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getFpl_cap())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Fp_Cap</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getFp_cap())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Pp</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getPp())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Pr</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getPr())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Ppu</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getPpu())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Ppl</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getPpl())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Ppk</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getPpk())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Ppm</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getPpm())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Zu_Perf</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getZu_pref())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Zl_Perf</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getZl_pref())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Fpu_Perf</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getFpu_pref())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Fpl_Perf</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getFpl_pref())%></td>
	</tr>
	<tr  align="left">
	<td width="40%" >Fp_Perf</td>
	<td width="*%" align="right"><%=df1.format(cpkMoudle11==null?0:cpkMoudle11.getFp_pref())%></td>
	</tr>
</table>