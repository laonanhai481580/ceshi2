<%@page import="java.text.DecimalFormat"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<% 
	Map<String,List<String>>  mapBus=(Map<String,List<String>>)ActionContext.getContext().get("mapBus");
	Map<String,Integer> mapInspectorRate=(Map<String,Integer>)ActionContext.getContext().get("mapInspectorRate");
	Map<String,Integer> mapInspectorFinishRate= (Map<String,Integer>) ActionContext.getContext().get("mapInspectorFinishRate");
%>
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
<div style="width:100%;text-align:left; ">
	<span style="margin-left:4px;width:100%;color:red;" id="message"></span>
</div>
<div style="width:100%;">
	<span style="font-size: 20px;font-weight: bold;text-align: center;">杭州鸿雁电器有限公司<br/>${businessUnitName}质量成本汇总表</span>	
</div>
<table style="margin:4px auto;padding:0px;width:100%;border-right:0px;border-bottom:0px;" cellspacing=0>
	<thead>
		<tr style='background:#99CCFF;font-weight:bold;height:29px;'>
			<td style="width:20%;height: 40px;">事业部</td>
			<td style="width:80%;margin: 0px;padding: 0px;height: 40px;">
				<table style="margin:0px auto;padding:0px;width:100%;height:50px;border:0px;" cellspacing=0>
					<tr>
						<td style="margin:0px;width:25%;padding:0px;height:50px;border-bottom:0px;border-left:0px;">检验员</td>
						<td style="margin:0px;width:25%;padding:0px;height:50px;border-bottom:0px;">检验总批数</td>
						<td style="margin:0px;width:25%;padding:0px;height:50px;border-bottom:0px;">检验及时批数</td>
						<td style="margin:0px;width:25%;padding:0px;height:50px;border-bottom:0px;border-right:0px;">检验及时率</td>
<!-- 						<td style="margin:0px;width:20%;padding:0px;height:50px;border-bottom:0px;border-right:0px;">详情</td>	 -->
					</tr>
				</table>
			</td>
		</tr>
	</thead>
	<tbody>
		<%
		if(!mapBus.isEmpty()){
		for(Map.Entry<String, List<String>> entry : mapBus.entrySet()){
		%>
		<tr>
			<td style="width:20%;"><%=entry.getKey()%></td>
			<td style="width:80%;margin: 0px;padding: 0px;">
				<table style="margin:0px auto;padding:0px;width:100%;border:0px;" cellspacing=0>
					<%for(String inspector:entry.getValue()){//检验员 
						int all=0;
						int finish=0;
					%>
					<tr>
						<td style="margin:0px;width:20%;padding:0px;height:30px;border-left:0px;"><%=inspector%></td>
						<%
							String key=entry.getKey()+"-"+inspector;
						%>
						
						<%if(mapInspectorRate.containsKey(key)){ 
							all=mapInspectorRate.get(key);
						%>
						<td style=margin:0px;width:20%;padding:0px;height:30px;"><%=mapInspectorRate.get(key)%></td>
						<%}else{%>
						<td style=margin:0px;width:20%;padding:0px;height:30px;"></td>
						<%} %>
						<%if(mapInspectorFinishRate.containsKey(key)){ 
							finish=mapInspectorFinishRate.get(key);
						%>
						<td style="margin:0px;width:20%;padding:0px;height:30px;"><%=mapInspectorFinishRate.get(key) %></td>
						<%}else{ %>
						<td style="margin:0px;width:20%;padding:0px;height:30px;"></td>
						<%} %>
						<td style="margin:0px;width:20%;padding:0px;height:30px;">
							<%
								DecimalFormat sd=new DecimalFormat("##.##%");
								double rate=0.00;
								if(all>0){
									rate= finish/all*1.00;
								}
							%>
							<%= sd.format(rate)%>
						</td>
<!-- 						<td style="margin:0px;width:20%;padding:0px;height:30px;border-right:0px;"></td> -->
					</tr>
					<%} %>
				</table>
			</td>
		</tr>
		<%}}%>
	</tbody>
</table>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>