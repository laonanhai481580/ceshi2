<%@page import="java.text.DecimalFormat"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.ambition.cost.entity.Composing"%>
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
<!-- <div style="line-height:20px;height:20px;background:#ECF7FB;position:absolute;" id="blankDiv"> -->
<!-- </div> -->
<table style="margin:4px auto;width:100%;border-right:0px;border-bottom:0px;" cellspacing=0 id="contentTable">
	<caption style="text-align:center;padding-top:6px;padding-bottom:12px;font-weight:bold;font-size:16px;">
		南昌欧菲光技术有限公司质量成本汇总表
	</caption>
	<thead>
		<tr style='background:#99CCFF;font-weight:bold;height:29px;'>
			<td >二级成本</td>
			<td >二级成本金额</td>
			<td >三级成本</td>
			<td >三级成本金额</td>
			<% 	Map<Integer, Integer> yearMap=(Map<Integer, Integer>)ActionContext.getContext().get("map");
			   for (int i = 0; i < yearMap.size(); i++) {
			%>
			<td ><%=yearMap.get(i)%></td>
			<% 					
		        }
			%>			
		</tr>
	</thead> 
	<tbody>
		<%
			List<Map<String,Object>> composings = (List<Map<String,Object>>)ActionContext.getContext().get("composings");
			Double lastTwoValue = 0.0,lastThreeValue = 0.0,totalBudgetValue = 0.0;
			DecimalFormat df = new DecimalFormat("0.00%");
			DecimalFormat valDf = new DecimalFormat("0.0#");
			for(Map<String,Object> map : composings){
				%>
			<tr>
				<%
					if(map.containsKey("level_two_name")){
						lastTwoValue = (Double)map.get("level_two_total");
						Double total = (Double)map.get("total");
						String rate = "0.00%";
						if(total != null){
							if(total>0){
								rate = df.format(lastTwoValue/total);
							}
						}
				%>
				<td rowspan="<%=map.get("level_two_rowspan")%>">
					<%=map.get("level_two_name") %>
				</td>
				<td rowspan="<%=map.get("level_two_rowspan")%>"><%=valDf.format(lastTwoValue) %></td>
				<%} %>
				<%
					if(map.containsKey("level_three_name")){
						lastThreeValue = (Double)map.get("level_three_total");
						String rate = "0.00%";
						if(lastTwoValue != null){
							if(lastTwoValue>0){
								rate = df.format(lastThreeValue/lastTwoValue);
							}
						}
				%>
					<td rowspan="<%=map.get("level_three_rowspan")%>">
						<%=map.get("level_three_name") %>
					</td>
					<td rowspan="<%=map.get("level_three_rowspan")%>"><%=valDf.format(lastThreeValue) %></td>
					<% 	
					   for (int i = 0; i < yearMap.size(); i++) {
						   int a=yearMap.get(i);
					%>
					<td ><%=map.get(yearMap.get(i).toString())%></td>
					<% 					
				        }
					%>	
				<%} %>
			</tr>
				<%
			}
		%>
	</tbody>
</table>