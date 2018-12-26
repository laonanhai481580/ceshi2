<%@page import="com.ambition.carmfg.entity.OrtTestItem"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.lang.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java"  pageEncoding="UTF-8"%>
<table class="form-table-border-left"  style="border:0px;table-layout:fixed;">
	<!-- 子表第一行 -->
	<tr>
		<td	style="text-align: center; width: 10%;">序号</td>
		<td	style="text-align: center;">ORT测试项目
 		<td	style="text-align: center;">测试条件</td> 
		<td	style="text-align: center;">判定标准</td>
		<td	style="text-align: center; ">结果</td>
	</tr>
<%-- <%
	List<OrtTestItem> testItems = (List<OrtTestItem>)request.getAttribute("testItems");
 	int max = 10,defaultColumns = 10;
	Map<Long,Map<String,Double>> resultMap = new HashMap<Long,Map<String,Double>>();
	long idFlag = 0;
	if(testItems.size()>=1){
%>   
	<!-- 子表内容 -->
		<%
		StringBuffer flagIds = new StringBuffer("");
		int i=1,flag = 0;
		boolean isLast = false;
		for(OrtTestItem testItem : testItems){
			flag++;
			flagIds.append(",a" + flag);
	%> 
	<tr  chemicalItems="items">
		<td style="text-align: center;"  target="true"><%=flag%></td>
		<td	style="text-align: center; padding:0px;">
			<input type="hidden"  id="items<%=flag %>_testItem"	readonly="readonly"	name="testItem"  	value="<%=testItem.getTestItem()==null?"":testItem.getTestItem()%>"	style="width:100px;"/>
			<%=testItem.getTestItem()==null?"":testItem.getTestItem()%>
		</td>
		<td	style="text-align: center; padding:0px;">
			<input type="hidden" id="items<%=flag %>_testCondition"	readonly="readonly"	name="testCondition"  	value="<%=testItem.getTestCondition()==null?"":testItem.getTestCondition()%>"	style="width:100px;"/>
			<%=testItem.getTestCondition()==null?"":testItem.getTestCondition()%>
		</td>	
		<td	style="text-align: center; padding:0px;">
			<input type="hidden" id="items<%=flag %>_judgeStandard"	  readonly="readonly" 	name="judgeStandard" 	value="<%=testItem.getJudgeStandard()==null?"":testItem.getJudgeStandard()%>"	style="width:70px;"/>
			<%=testItem.getJudgeStandard()==null?"":testItem.getJudgeStandard()%>
		</td>	
		<td	style="text-align: center; padding:0px;">
			<input  id="items<%=flag %>_testResult"	  	name="testResult" 	value="<%=testItem.getTestResult()==null?"":testItem.getTestResult()%>"	style="width:70px;"/>
			
		</td>
	</tr>
	<input type="hidden"  id="Idx"  name="Idx"  value="<%=flag%>"/>
	<%}} %>  --%>
	<%
		int count = 1;
	%> 
	<s:iterator value="_testItems" var="item" id="item" status="st">
		<tr class="ortTestItems" zbtr=true>
			<td style="border-bottom:0px;text-align: center;border-left: 0px;" ><%=count%> 
			</td>
			<td style="border-bottom:0px;text-align: center;">
				<input type="hidden" 		name="testItem"  	value="${testItem}"	/>
				${testItem}
			</td>
			<td style="border-bottom:0px;text-align: center;">
				<input type="hidden" 		name="testCondition"  	value="${testCondition}"	/>
				${testCondition}
			</td>
			<td style="border-bottom:0px;text-align: center;">
				<input type="hidden" 		name="judgeStandard"  	value="${judgeStandard}"	/>
				${judgeStandard}
			</td>
			<td style="border-bottom:0px;text-align: center;">
				<input type="text" 		name="testResult"  	value="${testResult}"	/>
				<%-- ${testResult} --%>
			</td>
		</tr>
		<%count++; %> 
	</s:iterator>
</table>
