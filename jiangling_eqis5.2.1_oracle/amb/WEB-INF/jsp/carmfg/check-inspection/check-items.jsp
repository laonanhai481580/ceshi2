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
	for(MfgCheckItem checkItem : checkItems){
		if(checkItem.getId()==null){
			idFlag++;
			checkItem.setId(idFlag);
		}
		if(!MfgInspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
			Map<String,Double> tempMap = new HashMap<String,Double>();
			int tempMax = defaultColumns;
			checkItem.getResult3();
			for(int i=1;i<=30;i++){
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
			<td style="width:20px;text-align:center;border-top:0px;">分类</td>
			<td style="width:220px;text-align:center;border-top:0px;">检查项目</td>
			<td style="width:70px;text-align:center;border-top:0px;">检查方法</td>
			<td style="width:60px;text-align:center;border-top:0px;">检验类别</td>
			<td style="width:45px;text-align:center;border-top:0px;">抽检数量</td>
			<td style="width:90px;text-align:center;border-top:0px;">规格</td>
			<td style="width:70px;text-align:center;border-top:0px;display: none;">质量参数</td>
			<td style="width:<%=max*37 + 70%>px;padding:0px;border-top:0px;text-align: center;" id="checkItemsHeader">
				检验记录
			</td>
			<td style="width:40px;text-align: center;border-top:0px;">合格数</td>
			<td style="width:40px;text-align: center;border-top:0px;">不良数</td>
			<td style="width:40px;text-align: center;border-top:0px;border-right:0px;">结论</td>
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
					<input type="hidden" name="unit" value="<%=checkItem.getUnit()==null?"":checkItem.getUnit()%>"/>
					<input type="hidden" name="minlimit" value="<%=checkItem.getMinlimit()==null?"":checkItem.getMinlimit()%>"/>
					<input type="hidden" name="maxlimit" value="<%=checkItem.getMaxlimit()==null?"":checkItem.getMaxlimit()%>"/>
					<input type="hidden" name="parentRowSpan" value="<%=checkItem.getParentRowSpan()==null?"":checkItem.getParentRowSpan()%>"/>
					<input type="hidden" name="parentItemName" value="<%=checkItem.getParentItemName()==null?"":checkItem.getParentItemName()%>"/>
					<input type="hidden" name="checkItemName" value="<%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%>"/>
					<input type="hidden" name="codeLetter" value="<%=checkItem.getCodeLetter()==null?"":checkItem.getCodeLetter()%>"/>
					<input type="hidden" name="inspectionType" value="<%=checkItem.getInspectionType()==null?"":checkItem.getInspectionType()%>"/>
					<input type="hidden" name="countType" value="<%=checkItem.getCountType()==null?"":checkItem.getCountType()%>"/>
					<input type="hidden" name="aql" value="<%=checkItem.getAql()==null?"":checkItem.getAql()%>"/>
					<input type="hidden" name="aqlAc" value="<%=checkItem.getAqlAc()==null?"":checkItem.getAqlAc()%>"/>
					<input type="hidden" name="aqlRe" value="<%=checkItem.getAqlRe()==null?"":checkItem.getAqlRe()%>"/>
					<%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%>
				</td>
				<td style="text-align:center;">
					<input type="hidden" name="checkMethod" value="<%=checkItem.getCheckMethod()==null?"":checkItem.getCheckMethod()%>"/>
					<%=checkItem.getCheckMethod()==null?"":checkItem.getCheckMethod() %>
				</td>
				<td style="text-align:center;">
					<input type="hidden" name="inspectionLevel" value="<%=checkItem.getInspectionLevel()%>"/>
					<%=checkItem.getInspectionLevel()==null?"":(levelMap.containsKey(checkItem.getInspectionLevel())?levelMap.get(checkItem.getInspectionLevel()):checkItem.getInspectionLevel()) %>
				</td>
				<td style="text-align: center;">
					<input type="hidden" name="inspectionAmount" value="<%=checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount()%>"/>
					<%=checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount() %>
				</td>
				<td style="text-align: center;">
					<input type="hidden" name="specifications" value="<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications()%>"/>
					<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications() %>
				</td>
				<td style="text-align: center;display: none;">
					<input type="hidden" name="featureId" value="<%=checkItem.getFeatureId()==null?"":checkItem.getFeatureId()%>"/>
					<%=checkItem.getFeatureId()==null?"":checkItem.getFeatureId() %>
				</td>
				<td style="padding:0px;" class="checkItemsClass">
					<%
						if(InspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
					%>
							<textarea style="width:98%;height:100%;" rows=3 name="results" class="{maxlength:1000}"><%=checkItem.getResults()==null?"":checkItem.getResults() %></textarea>	
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
								<input color="<%=color %>" style="width:33px;float:left;margin-left:2px;color:<%=color %>" title="<%=checkItem.getCheckItemName() + "样品" + (j+1) %>" name="<%=name%>" value="<%=value==null?"":value%>" class="{number:true,min:0}"></input>
						<%
							}
							if(count<30){
						%>
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="addResultInput(this,'<%=checkItem.getCheckItemName() %>')" href="#" title="添加检验项"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="removeResultInput(this)" href="#" title="删除最后的检验项"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
						<%} %>
					<%} %>
				</td>
				<td style="text-align: center;">
					<input style="width:95%;padding:0px;text-align:center;" name="qualifiedAmount" value="<%=checkItem.getQualifiedAmount() %>" class="{required:true,digits:true,min:0}"></input>
				</td>
				<td style="text-align: center;">
					<input style="width:95%;padding:0px;text-align:center;" name="unqualifiedAmount" value="<%=checkItem.getUnqualifiedAmount() %>" class="{required:true,digits:true}"></input>
				</td>
				<td style="text-align: center;border-right:0px;">
					<input type="hidden" name="conclusion" value="<%=checkItem.getConclusion()%>"/>
					<span name="conclusionSpan"><%=checkItem.getConclusion() %></span>
				</td>
			</tr>
		<%} %>
	</tbody>
</table>