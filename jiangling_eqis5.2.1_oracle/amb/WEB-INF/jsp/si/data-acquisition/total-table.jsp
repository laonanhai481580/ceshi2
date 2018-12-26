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
	border-bottom:1px;
}
.lastCell{
	border-right:1px;
}
-->
</style>
<table style="width:100%;" id="contentTable">
	<caption style="text-align:center;padding-top:6px;padding-bottom:12px;font-weight:bold;font-size:16px;">
		南昌欧菲光技术有限公司SI报告汇总表
	</caption>
	<thead>
		<tr style='background:#99CCFF;font-weight:bold;height:29px;border:1px solid black'>
			<td colspan="2" style="width: 200px;">统计项</td>
			<td style="width: 100px;">Total</td>
			<% 	
			Map<String,Double> totalMap=(Map<String,Double>)ActionContext.getContext().get("totalMap");
			if(totalMap.size()==0){
				totalMap=new HashMap<String,Double>();
			}
			Map<String,Map<String,Double>> Map=(Map<String,Map<String,Double>>)ActionContext.getContext().get("dateValueMap");
			if(Map.size()==0){
				Map=new HashMap<String,Map<String,Double>>();
			}
			Map<String,Set<String>> itemMap=(Map<String,Set<String>>)ActionContext.getContext().get("itemMap");
			if(itemMap.size()==0){
				itemMap=new HashMap<String,Set<String>>();
			}			
			Map<String,Object> itemValueMap=(Map<String,Object>)ActionContext.getContext().get("itemValueMap");
			if(itemValueMap.size()==0){
				itemValueMap=new HashMap<String,Object>();
			}	
			   for (String str:Map.keySet()) {
			%>
			<td ><%=str%></td>
			<% 					
		        }
			%>			
		</tr>
	</thead> 
	<tbody>
			<tr>
				<td colspan="2">投入数</td>
				<td ><%=Math.floor(totalMap.get("stockAmountTotal"))%></td>
				<% 	
				   for (String str:Map.keySet()) {
				%>
				<td ><%=Integer.valueOf(Map.get(str).get("stockAmount").intValue())%></td>
				<% 					
			        }
				%>
			</tr>
			<tr>
				<td rowspan="3" style="width: 100px;">检验数</td>
				<td  style="width: 100px;">外观</td>
				<td ><%=totalMap.get("appearanceAmountTotal")%></td>
				<% 	
				   for (String str:Map.keySet()) {
				%>
				<td ><%=Integer.valueOf(Map.get(str).get("appearanceAmount").intValue())%></td>
				<% 					
			        }
				%>
			</tr>	
			<tr>
				<td >尺寸</td>
				<td ><%=totalMap.get("sizeAmountTotal")%></td>
				<% 	
				   for (String str:Map.keySet()) {
				%>
				<td ><%=Integer.valueOf(Map.get(str).get("sizeAmount").intValue())%></td>
				<% 					
			        }
				%>
			</tr>	
			<tr>
				<td >功能</td>
				<td ><%=totalMap.get("functionAmountTotal")%></td>
				<% 	
				   for (String str:Map.keySet()) {
				%>
				<td ><%=Integer.valueOf(Map.get(str).get("functionAmount").intValue())%></td>
				<% 					
			        }
				%>
			</tr>			
			<tr>
				<td rowspan="3" style="width: 100px;">不良数量</td>
				<td  style="width: 100px;">外观</td>
				<td ><%=totalMap.get("appearanceUnAmountTotal")%></td>
				<% 	
				   for (String str:Map.keySet()) {
				%>
				<td ><%=Integer.valueOf(Map.get(str).get("appearanceUnAmount").intValue())%></td>
				<% 					
			        }
				%>
			</tr>	
			<tr>
				<td >尺寸</td>
				<td ><%=totalMap.get("sizeUnAmountTotal")%></td>
				<% 	
				   for (String str:Map.keySet()) {
				%>
				<td ><%=Integer.valueOf(Map.get(str).get("sizeUnAmount").intValue())%></td>
				<% 					
			        }
				%>
			</tr>	
			<tr>
				<td >功能</td>
				<td ><%=totalMap.get("functionUnAmountTotal")%></td>
				<% 	
				   for (String str:Map.keySet()) {
				%>
				<td ><%=Integer.valueOf(Map.get(str).get("functionUnAmount").intValue())%></td>
				<% 					
			        }
				%>
			</tr>			
			<tr>
				<td rowspan="3" style="width: 100px;">不良率</td>
				<td  style="width: 100px;">外观</td>
				<td ><%=totalMap.get("appearanceUnRateTotal")%>%</td>
				<% 	
				   for (String str:Map.keySet()) {
				%>
				<td ><%=Map.get(str).get("appearanceUnRate")%>%</td>
				<% 					
			        }
				%>
			</tr>			
			<tr>
				<td >尺寸</td>
				<td ><%=totalMap.get("sizeUnRateTotal")%>%</td>
				<% 	
				   for (String str:Map.keySet()) {
				%>
				<td ><%=Map.get(str).get("sizeUnRate")%>%</td>
				<% 					
			        }
				%>
			</tr>	
			<tr>
				<td >功能</td>
				<td ><%=totalMap.get("functionUnRateTotal")%>%</td>
				<% 	
				   for (String str:Map.keySet()) {
				%>
				<td ><%=Map.get(str).get("functionUnRate")%>%</td>
				<% 					
			        }
				%>
			</tr>	
			<tr>
				<td colspan="2">检验Lot数</td>
				<td ><%=totalMap.get("inspectionLotAmountTotal")%></td>
				<% 	
				   for (String str:Map.keySet()) {
				%>
				<td ><%=Integer.valueOf(Map.get(str).get("inspectionLotAmount").intValue())%></td>
				<% 					
			        }
				%>
			</tr>
			<tr>
				<td colspan="2">Pass Lot数</td>
				<td ><%=totalMap.get("passLotAmountTotal")%></td>
				<% 	
				   for (String str:Map.keySet()) {
				%>
				<td ><%=Integer.valueOf(Map.get(str).get("passLotAmount").intValue())%></td>
				<% 					
			        }
				%>
			</tr>
			<tr>
				<td colspan="2">Reject Lot数	</td>
				<td ><%=totalMap.get("rejectLotAmountTotal")%></td>
				<% 	
				   for (String str:Map.keySet()) {
				%>
				<td ><%=Integer.valueOf(Map.get(str).get("rejectLotAmount").intValue())%></td>
				<% 					
			        }
				%>
			</tr>
			<tr>
				<td colspan="2">LRR	</td>
				<td ><%=totalMap.get("lrrRateTotal")%>%</td>
				<% 	
				   for (String str:Map.keySet()) {
				%>
				<td ><%=Map.get(str).get("lrrRate")%>%</td>
				<% 					
			        }
				%>
			</tr>							
			<tr style='background:#99CCFF;font-weight:bold;height:29px;border:1px solid black'>
				<td colspan="2">不良明细</td>
				<td >Total</td>
				<% 	
				   for (String str:Map.keySet()) {
				%>
				<td ><%=str%></td>
				<% 					
			        }
				%>				
			</tr>
				<% 	
				   for (String str:itemMap.keySet()) {
					Set<String> set=itemMap.get(str);
					int i=1;
					for(String name : set){		
				%>
				<tr>
				<% 					
			        if(i==1){			        
				%>	
				<td rowspan="<%=itemValueMap.get(str+"_rows")%>"><%=str%></td>
				<% 					
			        i++;		        
				%>
				<td><%=name%></td>
				<% 					
			        }else{	        
				%>
				<td><%=name%></td>
				<% 					
					}	        
				%>
				<td ><%=itemValueMap.get(str+"_"+name)==null?0:itemValueMap.get(str+"_"+name)%></td>
				<% 	
				   for (String date:Map.keySet()) {
				%>
				<td ><%=itemValueMap.get(str+"_"+name+"_"+date)==null?0:itemValueMap.get(str+"_"+name+"_"+date)%></td>
				<% 					
			        }
				}
				%>
				</tr>
				<% 					
			        }
				%>
	</tbody>
</table>