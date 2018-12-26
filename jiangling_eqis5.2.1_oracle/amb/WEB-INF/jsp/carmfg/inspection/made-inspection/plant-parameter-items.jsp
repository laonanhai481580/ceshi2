<%@page import="com.ambition.iqc.entity.InspectingItem"%>
<%@page import="com.ambition.carmfg.entity.MfgPlantParameterItem"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.ambition.iqc.entity.SampleScheme"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="java.util.List"%>
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	int maxPlant = 5,defaultPlantColumns = 5;
	long plantIdFlag = 0;
	List<MfgPlantParameterItem> plantDetails= (List<MfgPlantParameterItem>)request.getAttribute("plantDetails");
	Map<Long,Map<String,Double>> plantResultMap = new HashMap<Long,Map<String,Double>>();
	for(MfgPlantParameterItem detail:plantDetails){
		if(detail.getId()==null){
			plantIdFlag++;
			detail.setId(plantIdFlag);
		}
		Map<String,Double> tempMap = new HashMap<String,Double>();
			int tempMax = defaultPlantColumns;
			for(int k=1;k<=5;k++){
				Double value = (Double)PropertyUtils.getProperty(detail,"result"+k);
				if(value != null){
					tempMap.put("result" + k, value);
					if(k>tempMax){
						tempMax=k;
					}
				}
			}
			if(tempMax>maxPlant){
				maxPlant = tempMax;
			}
			for(int l=1; l<=tempMax; l++){
				if(!tempMap.containsKey("result" + l)){
					tempMap.put("result" + l,null);
				}
			}
			plantResultMap.put(detail.getId(), tempMap);
	}
%>
 <table class="form-table-border-left" style="border:0px;table-layout:fixed;">
	<thead>
		<tr>
			<td style="width:20px;text-align:center;border-top:0px;border-left:0px;">序号</td>
			<td style="width:80px;">设备名称</td>
			<td style="width:80px;text-align:center;border-top:0px;">参数名称</td>
			<td style="width:80px;text-align:center;border-top:0px;">参数规格</td>
			<td style="width:45px;text-align:center;border-top:0px;">上限</td>
			<td style="width:45px;text-align:center;border-top:0px;">下限</td>
			<td style="width:<%=maxPlant*37 + 70%>px;padding:0px;border-top:0px;text-align: center;" id="checkItemsHeader">检验记录</td>
			<td style="width:40px;text-align: center;border-top:0px;">判定</td>
			<td style="width:60px;text-align: center;border-top:0px;">检验员</td>
			<td style="width:200px;text-align: center;border-top:0px;border-right:0px;">描述</td>
		</tr>
	</thead>
 	<tbody>
		<%
			
			int plantI=1,flagPlant = 0;
			boolean isPlantLast = false;
			StringBuffer plantFlagIds = new StringBuffer("");
			for(MfgPlantParameterItem detail:plantDetails){ 
				flagPlant++;
				if(flagPlant==plantDetails.size()){
					isPlantLast = true;
				}
				plantFlagIds.append(",a" + flagPlant);
		%>
		<tr>
			<td><%=plantI++%></td>
			<td>
				<input type="hidden" fieldName="itemName" name="_a<%=flagPlant %>_itemName" value="<%=detail.getItemName()==null?"":detail.getItemName()%>"/>
				<input type="hidden" fieldName="parameterName" name="_a<%=flagPlant %>_parameterName" value="<%=detail.getParameterName()==null?"":detail.getParameterName()%>"/>
				<input type="hidden" fieldName="parameterSpc" name="_a<%=flagPlant %>_parameterSpc" value="<%=detail.getParameterSpc()==null?"":detail.getParameterSpc()%>"/>
				<input type="hidden" fieldName="parameterMin" name="_a<%=flagPlant %>_parameterMin" value="<%=detail.getParameterMin()==null?"":detail.getParameterMin()%>"/>
				<input type="hidden" fieldName="parameterMax" name="_a<%=flagPlant %>_parameterMax" value="<%=detail.getParameterMax()==null?"":detail.getParameterMax()%>"/>
				<%=detail.getItemName()==null?"":detail.getItemName()%>
			</td>
			<td><%=detail.getParameterName() ==null?"":detail.getParameterName()%></td>
			<td><%=detail.getParameterSpc() ==null?"":detail.getParameterSpc()%></td>
			<td><%=detail.getParameterMax()==null?"":detail.getParameterMax() %></td>
			<td><%=detail.getParameterMin()==null?"":detail.getParameterMin()%></td>
			<td style="padding:0px;"  class="plantItemsClass">
				<%
					if(InspectingItem.COUNTTYPE_COUNT.equals(detail.getCountType())){
				%>
					<textarea style="width:98%;height:100%;" rows=3 fieldName="results" name="_a<%=flagPlant %>_results" class="{maxlength:1000}"><%=detail.getResults()==null?"":detail.getResults() %></textarea>	
				<%}else{ %>
					<%
					Map<String,Double> tempMap = plantResultMap.get(detail.getId());
					int count = tempMap.keySet().size();
					Double maxlimit = detail.getParameterMax(),minlimit = detail.getParameterMin();
					for(int j=0;j<count;j++){
						String name = "result"+(j+1);
						Double value = tempMap.get(name);
						String color = "black";
						if(maxlimit!=null&&minlimit!=null&&value!=null){
							if(value<minlimit||value>maxlimit){
								color="red";
							}
						}
					%>		
						<input results=true color="<%=color %>" style="width:33px;float:left;margin-left:2px;color:<%=color %>" title="<%=detail.getItemName() + "样品" + (j+1) %>" fieldName="<%=name%>" name="_a<%=flagPlant %>_<%=name%>" value="<%=value==null?"":value%>" class="{number:true,min:0}"></input>
					<%
						}
						if(count<30){
					%>
							<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="addResultInput(this,'<%=detail.getItemName()%>')" href="#" title="添加检验项"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
							<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="removeResultInput(this)" href="#" title="删除最后的检验项"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
					<%} %>	
				<%} %>
			</td>
			<td>
				<input type="hidden" fieldName="conclusion" name="_a<%=flagPlant %>_conclusion" value="<%=detail.getConclusion()%>"/>
				<span fieldName="conclusionSpan" name="conclusionSpan"><%=detail.getConclusion()==null?"合格": detail.getConclusion()%></span>
			</td>
			<td>
				<%=detail.getInspector()==null?"":detail.getInspector()%>
			</td>
			<td style="text-align: center;">
				<textarea rows="2" cols="2" fieldName="remark" name="_a<%=flagPlant %>_remark" ><%=detail.getRemark()==null?"":detail.getRemark()%></textarea>
			</td>
		</tr>
		<%} %>
	</tbody> 
</table>
<input type="hidden" name="planFlagIds" value="<%=plantFlagIds%>">
