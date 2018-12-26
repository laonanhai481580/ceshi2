<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div id="tabs-1">
	<table style="width:100%;margin: auto;" class="form-table-border-left">
<!-- 		<tr style="background-color: CornflowerBlue;color: white;font-size: 16px;font-weight: bold;">
	        <td></td>
	    </tr> -->
	    <tr>
	        <td class="form-table-border-left" style="border:0px;table-layout:fixed;">
	        	<table class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
					<tr >	
	               		<td  style="text-align: center;">序号</td>
						<td  style="text-align: center;">试验项目</td>
						<td  style="text-align: center;">测试条件</td>
						<td  style="text-align: center;">测试数量</td>
						<td  style="text-align: center;">判定标准</td>
						<td  style="text-align: center;">预计投入时间</td>
						<td  style="text-align: center;">预计完成时间</td>
						<td  style="text-align: center;">实际开始时间</td>
						<td  style="text-align: center;">实际结束时间</td>
						<td  style="text-align: center;">测试前结果</td>
						<td  style="text-align: center;">测试后结果</td>
						<td  style="text-align: center;">测试报告</td>
						<td  style="text-align: center;">测试结果</td>
						<td  style="text-align: center;">异常处理单号</td>
					
					</tr>
	                <s:iterator value="_entrustOrtSublists" var="entrustOrtSublist" id="entrustOrtSublist" status="status">
	                <tr class="entrustOrtSublists">
	                    <td style="text-align:center;" rn=true></td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="试验项目" fieldName="properties" id="properties" name="testCondition" value="${entrustOrtSublist.properties}" title="${entrustOrtSublist.properties}" />
	                    </td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="测试条件" fieldName="testCondition" id="testCondition" name="testCondition" value="${entrustOrtSublist.testCondition}" title="${entrustOrtSublist.testCondition}" />
				        </td>
				        <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="测试数量" fieldName="testNumber" id="testNumber" name="testNumber" value="${entrustOrtSublist.testNumber}" title="${entrustOrtSublist.testNumber}" />
	                    </td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="判定标准" fieldName="criterionG" id="criterionG" name="criterionG" value="${entrustOrtSublist.criterionG}" title="${entrustOrtSublist.criterionG}" />
				        </td>
				        <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="判定标准" fieldName="criterionY" id="criterionY" name="criterionY" value="${entrustOrtSublist.criterionY}" title="${entrustOrtSublist.criterionY}" />
				        </td>
				        <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="判定标准" fieldName="criterionR" id="criterionR" name="criterionR" value="${entrustOrtSublist.criterionR}" title="${entrustOrtSublist.criterionR}" />
				        </td>
				        <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="预计投入时间" fieldName="startTime" id="startTime" name="startTime" value="${entrustOrtSublist.startTime}" title="${entrustOrtSublist.startTime}" />
	                    </td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="预计结束时间" fieldName="endTime" id="endTime" name="endTime" value="${entrustOrtSublist.endTime}" title="${entrustOrtSublist.endTime}" />
				        </td>
				         <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="实际开始时间" fieldName="factStartTime" id="factStartTime" name="factStartTime" value="${entrustOrtSublist.factStartTime}" title="${entrustOrtSublist.factStartTime}" />
	                    </td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="实际结束时间" fieldName="factEndTime" id="factEndTime" name="factEndTime" value="${entrustOrtSublist.factEndTime}" title="${entrustOrtSublist.factEndTime}" />
				        </td>
				        <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="测试前结果" fieldName="testBefore" id="testBefore" name="testBefore" value="${entrustOrtSublist.testBefore}" title="${entrustOrtSublist.testBefore}" />
	                    </td>
	                     <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="测试后结果" fieldName="testAfter" id="testAfter" name="testAfter" value="${entrustOrtSublist.testAfter}" title="${entrustOrtSublist.testAfter}" />
	                    </td>
	                     <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="测试报告" fieldName="testReport" id="testReport" name="testReport" value="${entrustOrtSublist.testReport}" title="${entrustOrtSublist.testReport}" />
	                    </td>
	                     <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="最终测试结果" fieldName="testResult" id="testResult" name="testResult" value="${entrustOrtSublist.testResult}" title="${entrustOrtSublist.testResult}" />
	                    </td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="异常处理单号" fieldName="exceptionNo" id="exceptionNo" name="invalidNumber" value="${exceptionNo.exceptionNo}" title="${entrustOrtSublist.exceptionNo}" />
				        </td>
	                </tr>
	                </s:iterator>
	            </table>
	        </td>
	    </tr>
	</table>
</div>