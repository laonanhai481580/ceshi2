<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<style>
<!--
table{
	border:1px solid black;
}
table tr{
	height:24px;
}
table td{
	border-right:1px solid black;
	border-bottom:1px solid black;
}
.lastRow td{
	border-bottom:0px;
}
.lastCell{
	border-right:0px;
}
-->
</style>
<table style="margin:4px auto;width:100%;" cellspacing=0>
	<caption style="text-align:center;padding-top:6px;padding-bottom:12px;font-weight:bold;font-size:16px;">${totalYear}年内部损失成本汇总表</caption>
	<thead>
		<tr style="height:24px;background:#99CCFF;font-weight:bold;height:29px;">
			<td>部门</td>
			<td>组别</td>
			<%
				for(int i=1;i<13;i++){
			%>
			<td style="width:50px;"><%=i%>月</td>					
			<%} %>
			<td>年度合计</td>
			<td class="lastCell">月平均</td>
		</tr>
	</thead>
	<tbody>
		${totalYearTBoday}
	</tbody>
</table>