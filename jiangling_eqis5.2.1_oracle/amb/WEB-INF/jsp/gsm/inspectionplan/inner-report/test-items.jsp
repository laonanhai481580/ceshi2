<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java"  pageEncoding="UTF-8"%>
<table class="form-table-border-left"  style="border:0px;table-layout:fixed;">
	<!-- 子表第一行 -->
	<tr>
		<td	style="text-align: center; width: 10%;">序号</td>
		<td	style="text-align: center;">校准项目
 		<td	style="text-align: center;">标准值</td> 
		<td	style="text-align: center;">指示值</td>
		<td	style="text-align: center;">误差值</td>
		<td	style="text-align: center;">允许误差</td>
		<td	style="text-align: center;">Pass Or Fail</td>
		<td	style="text-align: center;">备注</td>
	</tr>
	<%
		int count = 1;
	%> 
	<s:iterator value="_checkReportDetails" var="item" id="item" status="st" >
		<tr class="checkReportDetails" zbtr2=true trprefix="a_<%=count%>" >
			<td style="border-bottom:0px;text-align: center;border-left: 0px;" ><%=count%> 
			</td>
			<td style="border-bottom:0px;text-align: center;">
				<input type="hidden" 		name="itemName"  	value="${itemName}"	/>
				${itemName}
			</td>
			<td style="border-bottom:0px;text-align: center;">
				<input type="hidden" 		name="standardValue"  	value="${standardValue}"	/>
				${standardValue}
			</td>
			<td style="border-bottom:0px;text-align: center;">
				<input type="text" 		name="indicatedValue"  	value="${indicatedValue}"	/>
			</td>
			<td style="border-bottom:0px;text-align: center;">
				<input type="text" 		name="errorValue"  	value="${errorValue}"	/>
			</td>
			<td style="border-bottom:0px;text-align: center;">
				<input type="hidden" 		name="allowableError"  	value="${allowableError}"	/>
				${allowableError}
			</td>
			<td style="border-bottom:0px;text-align: center;">
		    	<s:select list="passOrFails"
					listKey="value" 
					listValue="value" 
					name="passOrFail" 
					id="passOrFail" 
					cssStyle="width:180px;"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td style="border-bottom:0px;text-align: center;">
				<input type="text" 		name="remark"  	value="${remark}"	/>
			</td>
		</tr>
		<%count++; %> 
	</s:iterator>
</table>
