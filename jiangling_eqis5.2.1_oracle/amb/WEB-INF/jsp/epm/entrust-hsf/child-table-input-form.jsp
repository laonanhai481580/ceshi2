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
						<td  style="text-align: center;">样品名称</td>
						<td  style="text-align: center;">规格型号</td>
						<td  style="text-align: center;">批号</td>
						<td  style="text-align: center;">数量</td>
						<td  style="text-align: center;">供应商</td>
						<td  style="text-align: center;">客户</td>
						<td  style="text-align: center;">测试项目</td>
						<td  style="text-align: center;">预计开始时间</td>
						<td  style="text-align: center;">预计结束时间</td>
						<td  style="text-align: center;">实际开始时间</td>
						<td  style="text-align: center;">实际结束时间</td>
						<td  style="text-align: center;">测试前结果</td>
						<td  style="text-align: center;">测试后结果</td>
						<td  style="text-align: center;">测试报告</td>
						<td  style="text-align: center;">异常处理单</td>
					</tr>
	                <s:iterator value="_entrustHsfSublists" var="entrustHsfSublist" id="entrustHsfSublist" status="status">
	                <tr class="entrustHsfSublists">
	                    <td style="text-align:center;" rn=true></td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="样品名称" fieldName="sampleName" id="sampleName" name="sampleName" value="${entrustHsfSublist.sampleName}" title="${entrustHsfSublist.sampleName}" />
	                    </td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="规格型号" fieldName="model" id="model" name="model" value="${entrustHsfSublist.model}" title="${entrustHsfSublist.model}" />
				        </td>
				        <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="批号" fieldName="lotNo" id="lotNo" name="lotNo" value="${entrustHsfSublist.lotNo}" title="${entrustHsfSublist.lotNo}" />
	                    </td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="数量" fieldName="amount" id="amount" name="amount" value="${entrustHsfSublist.amount}" title="${entrustHsfSublist.amount}" />
				        </td>
				        <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="供应商" fieldName="supplier" id="supplier" name="supplier" value="${entrustHsfSublist.supplier}" title="${entrustHsfSublist.supplier}" />
	                    </td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="客户" fieldName="client" id="client" name="client" value="${entrustHsfSublist.client}" title="${entrustHsfSublist.client}" />
				        </td>
				        <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="测试项目" fieldName="testItem" id="testItem" name="testItem" value="${entrustHsfSublist.testItem}" title="${entrustHsfSublist.testItem}" />
	                    </td>
	                     <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="预计开始时间" fieldName="startTime" id="startTime" name="startTime" value="${entrustHsfSublist.startTime}" title="${entrustHsfSublist.startTime}" />
	                    </td>
	                     <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="预计结束时间" fieldName="endTime" id="endTime" name="endTime" value="${entrustHsfSublist.endTime}" title="${entrustHsfSublist.endTime}" />
	                    </td>
	                     <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="实际开始时间" fieldName="factStartTime" id="factStartTime" name="factStartTime" value="${entrustHsfSublist.factStartTime}" title="${entrustHsfSublist.factStartTime}" />
	                    </td>
	                     <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="实际结束时间" fieldName="factEndTime" id="factEndTime" name="factEndTime" value="${entrustHsfSublist.factEndTime}" title="${entrustHsfSublist.factEndTime}" />
	                    </td>
	                     <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="测试前结果" fieldName="testBefore" id="testBefore" name="testBefore" value="${entrustHsfSublist.testBefore}" title="${entrustHsfSublist.testBefore}" />
	                    </td>
	                     <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="测试后结果" fieldName="testAfter" id="remark" name="testAfter" value="${entrustHsfSublist.testAfter}" title="${entrustHsfSublist.testAfter}" />
	                    </td>
	                    <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="测试报告" fieldName="testReport" id="testReport" name="testReport" value="${entrustHsfSublist.testReport}" title="${entrustHsfSublist.testReport}" />
	                    </td>
	                    <td>
	                     <input  style="width: 66%;" type="text" placeholder="异常处理单号" fieldName="exceptionNo" id="exceptionNo" name="exceptionNo" value="${entrustHsfSublist.exceptionNo}" title="${entrustHsfSublist.exceptionNo}" />
						</td>
	                </tr>
	                </s:iterator>
	            </table>
	        </td>
	    </tr>
	</table>
</div>