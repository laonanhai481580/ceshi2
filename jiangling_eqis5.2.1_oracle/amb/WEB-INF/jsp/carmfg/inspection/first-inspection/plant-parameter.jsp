<%@page import="com.ambition.carmfg.entity.MfgInspectingItem"%>
<%@page import="com.ambition.carmfg.entity.MfgCheckItem"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.ambition.iqc.entity.SampleScheme"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.ambition.iqc.entity.InspectingItem"%>
<%@page import="com.ambition.iqc.entity.SampleCodeLetter"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="java.util.List"%>
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String sampleSchemeType = SampleScheme.ORDINARY_TYPE;
	List<MfgCheckItem> checkItems = (List<MfgCheckItem>)request.getAttribute("checkItems");
	int max = 10,defaultColumns = 10;
	Map<Long,Map<String,Double>> resultMap = new HashMap<Long,Map<String,Double>>();
	long idFlag = 0;
	StringBuffer flagIds = new StringBuffer("");
	for(MfgCheckItem checkItem : checkItems){
		if(checkItem.getId()==null){
			idFlag++;
			checkItem.setId(idFlag);
		}
		if(!MfgInspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
			Map<String,Double> tempMap = new HashMap<String,Double>();
			int tempMax = defaultColumns;
			checkItem.getResult3();
			for(int i=1;i<=80;i++){
				Double value = (Double)PropertyUtils.getProperty(checkItem,"result"+i);
				if(value != null){
					tempMap.put("result" + i, value);
					if(i>tempMax){
						tempMax=i;
					}
				}
			}
			if(tempMax>max){
				max = tempMax;
			}
			for(int i=1; i<=tempMax; i++){
				if(!tempMap.containsKey("result" + i)){
					tempMap.put("result" + i,null);
				}
			}
			resultMap.put(checkItem.getId(), tempMap);
		}
	}
%>
<table class="form-table-border-left" style="border:0px;table-layout:fixed;">
	<thead>
		<tr>
			<td style="width:20px;text-align:center;border-top:0px;border-left:0px;">NO</td>
			<td style="width:20px;text-align:center;border-top:0px;">设备名称</td>
			<td style="width:220px;text-align:center;border-top:0px;">参数名称</td>
			<td style="width:70px;text-align:center;border-top:0px;">参数规格</td>
			<td style="width:60px;text-align:center;border-top:0px;">规格上限</td>
			<td style="width:45px;text-align:center;border-top:0px;">规格下限</td>
			<td style="width:<%=max*37 + 70%>px;padding:0px;border-top:0px;text-align: center;" id="checkItemsHeader">
				检验记录
			</td>
			<td style="width:40px;text-align: center;border-top:0px;border-right:0px;">判定</td>
			<td style="width:40px;text-align: center;border-top:0px;border-right:0px;">描述</td>
			<td style="width:40px;text-align: center;border-top:0px;border-right:0px;">检验员</td>
		</tr>
	</thead>
	<tbody>
		<%
			List<Option> inspectionLevels = (List<Option>)request.getAttribute("inspectionLevels");
			if(inspectionLevels == null){
				inspectionLevels = new ArrayList<Option>();
			}
			Map<String,String> levelMap = new HashMap<String,String>();
			for(Option option : inspectionLevels){
				levelMap.put(option.getValue(),option.getName());
			}
			int i=1,flag = 0;
			boolean isLast = false;
			for(MfgCheckItem checkItem : checkItems){
				flag++;
				if(flag==checkItems.size()){
					isLast = true;
				}
				flagIds.append(",a" + flag);
		%>
				<tr>
				<td style="text-align: center;border-left:0px;"><%=i++ %></td>
 				<%  
 					if(checkItem.getParentItemName() != null && !checkItem.getParentRowSpan().equals(1)){ 
 				%>  
 				<td rowspan="<%=checkItem.getParentRowSpan() %>" style="text-align: center;"><%=checkItem.getParentItemName()==null?"": checkItem.getParentItemName()%></td> 
 				<%
 					} else if(checkItem.getParentItemName() != null && checkItem.getParentRowSpan().equals(1)){
 				%>
				<td style="text-align: center;"><%=checkItem.getParentItemName()==null?"": checkItem.getParentItemName()%></td>
				<%} %>
				<td style="text-align: center;">
					<%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%>
				</td>
				<td style="text-align:center;">
					<input type="hidden" fieldName="checkMethod" name="a<%=flag %>_checkMethod" value="<%=checkItem.getCheckMethod()==null?"":checkItem.getCheckMethod()%>"/>
					<%=checkItem.getCheckMethod()==null?"":checkItem.getCheckMethod() %>
				</td>
				<td style="text-align:center;">
					<input type="hidden" fieldName="inspectionLevel" name="a<%=flag %>_inspectionLevel" value="<%=checkItem.getInspectionLevel()%>"/>
					<%=checkItem.getInspectionLevel()==null?"":(levelMap.containsKey(checkItem.getInspectionLevel())?levelMap.get(checkItem.getInspectionLevel()):checkItem.getInspectionLevel()) %>
				</td>
				<td style="text-align: center;">
					<input type="hidden" fieldName="inspectionAmount" name="a<%=flag %>_inspectionAmount" value="<%=checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount()%>"/>
					<%=checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount() %>
				</td>
				<td style="text-align: center;">
					<input type="hidden" fieldName="specifications" name="a<%=flag %>_specifications" value="<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications()%>"/>
					<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications() %>
				</td>
				<td style="text-align: center;display: none;">
					<input type="hidden" fieldName="featureId" name="a<%=flag %>_featureId" value="<%=checkItem.getFeatureId()==null?"":checkItem.getFeatureId()%>"/>
					<%=checkItem.getFeatureId()==null?"":checkItem.getFeatureId() %>
				</td>
				<td style="padding:0px;" class="checkItemsClass">
					<%
						if(InspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
					%>
							<textarea style="width:98%;height:100%;" rows=3 fieldName="results" name="a<%=flag %>_results" class="{maxlength:1000}"><%=checkItem.getResults()==null?"":checkItem.getResults() %></textarea>	
					<%}else{ 
							Map<String,Double> tempMap = resultMap.get(checkItem.getId());
							int count = tempMap.keySet().size();
							Double maxlimit = checkItem.getMaxlimit(),minlimit = checkItem.getMinlimit();
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
								<input results=true color="<%=color %>" style="width:33px;float:left;margin-left:2px;color:<%=color %>" title="<%=checkItem.getCheckItemName() + "样品" + (j+1) %>" fieldName="<%=name%>" name="a<%=flag %>_<%=name%>" value="<%=value==null?"":value%>" class="{number:true,min:0}"></input>
						<%
							}
							if(count<30){
						%>
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="addResultInput(this,'<%=checkItem.getCheckItemName() %>')" href="#" title="添加检验项"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="removeResultInput(this)" href="#" title="删除最后的检验项"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
						<%} %>
					<%} %>
				</td>
				<td style="text-align: center;border-right:0px;">
					<input type="hidden" fieldName="conclusion" name="a<%=flag %>_conclusion" value="<%=checkItem.getConclusion()%>"/>
					<span fieldName="conclusionSpan" name="conclusionSpan"><%=checkItem.getConclusion() %></span>
				</td>
			</tr>
		<%} %>
	</tbody>
</table>
<input type="hidden" name="flagIds" value="<%=flagIds%>">
