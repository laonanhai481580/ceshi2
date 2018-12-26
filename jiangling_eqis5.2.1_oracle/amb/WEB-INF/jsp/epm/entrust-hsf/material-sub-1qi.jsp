<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<table class="form-table-border-left"
	style="border: 0px; table-layout: fixed;">
	<tbody>
		<tr>
			<td
				style="width: 60px; text-align: center; border-top: 0px; border-left: 0px;">序号</td>
			<td style="width: 150px; text-align: center; border-top: 0px;">样品名称</td>
			<td style="width: 100px; text-align: center; border-top: 0px;">规格型号</td>
			<td style="width: 100px; text-align: center; border-top: 0px;">批号</td>
			<td style="width: 70px; text-align: center; border-top: 0px;">数量</td>
			<td style="width: 200px; text-align: center; border-top: 0px;">供应商</td>
			<td style="width: 120px; text-align: center; border-top: 0px;">客户</td>
			<td style="width: 350px; text-align: center; border-top: 0px;">测试项目</td>
			<td style="width: 100px; text-align: center; border-top: 0px;">预计开始时间</td>
			<td style="width: 100px; text-align: center; border-top: 0px;">预计结束时间</td>
			<td style="width: 100px; text-align: center; border-top: 0px;">实际开始时间</td>
			<td style="width: 100px; text-align: center; border-top: 0px;">实际结束时间</td>
			<td style="width: 100px; text-align: center; border-top: 0px;">测试前结果</td>
			<td style="width: 100px; text-align: center; border-top: 0px;">测试后结果</td>
			<td style="width: 100px; text-align: center; border-top: 0px;">测试报告</td>
			<td style="width: 100px; text-align: center; border-top: 0px;">备注</td>
			<td style="width: 100px; text-align: center; border-top: 0px;">异常处理</td>
			<td style="width: 160px; text-align: center; border-top: 0px;">异常处理单号</td>
		</tr>
		<s:iterator value="_entrustHsfSublists" id="item" var="item">
			<tr class="entrustHsfSublists" zbtr1=true lazy=false>
				<td style="text-align: center;"><a class="small-button-bg"
					addBtn="true" onclick="addRowHtml(this)" href="#" title="添加"> <span
						class="ui-icon ui-icon-plus" style='cursor: pointer;'></span></a> <a
					class="small-button-bg" delBtn="true" onclick="removeRowHtml(this)"
					href="#" title="删除"> <span class="ui-icon ui-icon-minus"
						style='cursor: pointer;'></span></a></td>
				<td style="text-align: center;"><input stage="one"
					style="width: 90%;" name="sampleName" value="${sampleName }" /></td>
				<td style="text-align: center;"><input stage="one"
					style="width: 100%;" name="model" value="${model }" /></td>
				<td style="text-align: center;"><input stage="one"
					style="width: 100%;" name="lotNo" onclick="setExValue(this)"
					value="${lotNo }" /></td>
				<td style="text-align: center;"><input stage="one"
					style="width: 100%;" name="amount" value="${amount }"
					onchange="setweigh(this)"
					class="{number:true,messages:{number:'请输入有效数字!'}}" /></td>
				<td style="text-align: center;"><input stage="one"
					style="width: 86%;" name="supplier" value="${supplier }"
					style="float:left;" /> <a class="small-button-bg"
					onclick="supplierClick(this);" style="float: right;"><span
						class="ui-icon ui-icon-search"></span></a></td>
				<td style="text-align: center;"><input stage="one"
					style="width: 100%;" name="client" value="${client }" /></td>
				<td>
					<%-- 									<s:checkboxlist cssStyle="margin-left:8px;" name="testItem" id="testItem" value="#request.sendItemList" theme="simple" list="testItems" listKey="value" listValue="name"/> --%>
					<s:iterator value="testItems" id="option">
						<input id="testItemValueStr${option.id}"
							onclick="setTestItemValues(this);" control="true" type="checkbox"
							<s:if test="testItem.contains(#option.value)" > checked="checked" </s:if>
							value="${option.value}" />
						<span for="testItemValueStr${option.id}">${option.name}</span>
					</s:iterator> <%-- 	    							<input id="testItem" name="testItem" type="hidden" value="${testItem }" /> --%>
					<!-- 									<input type="checkbox" onclick="showInput(this)" id="p" name="p">其它 -->
					<input name="testItem" class="checkboxLabel" value="${testItem }" />
				</td>
				<td style="text-align: center;"><input stage="two"
					style="width: 100%;" name="startTime" isDate="true"
					value="<s:date name='startTime' format="yyyy-MM-dd"/>" /></td>
				<td style="text-align: center;"><input stage="two"
					style="width: 100%;" name="endTime" isDate="true"
					value="<s:date name='endTime' format="yyyy-MM-dd"/>" /></td>
				<td style="text-align: center;"><input stage="three"
					style="width: 100%;" name="factStartTime" isDate="true"
					value="<s:date name='factStartTime' format="yyyy-MM-dd"/>" /></td>
				<td style="text-align: center;"><input stage="three"
					style="width: 100%;" name="factEndTime" isDate="true"
					value="<s:date name='factEndTime' format="yyyy-MM-dd"/>" /></td>
				<td style="text-align: center;"><s:select list="testResults"
						style="width:100px;" listKey="value" listValue="value"
						name="testBefore" id="testBefore" emptyOption="true"
						onchange="isTestResult()" stage="three" theme="simple">
					</s:select></td>
				<td style="text-align: center;"><s:select list="testResults"
						style="width:100px;" listKey="value" listValue="value"
						name="testAfter" id="testAfter" emptyOption="true"
						onchange="isTestResult()" stage="three" theme="simple">
					</s:select></td>
				<td><input style="width: 100%;" type="hidden" isFile="true"
					name="testReport" value="${testReport}" /> <input
					style="width: 100%;" type="hidden" name="id" value="${item.id}" />
					<input style="width: 100%;" type="hidden" name="hsfId"
					value="${hsfId}" /></td>
				<td><textarea stage="three" style="width: 95%;" name="remark">${remark }</textarea></td>
				<td style="text-align: center;">
					<button class="btn" type="button" isButton=true
						onclick="addExceptionSingle(this)">
						<span><span>生成</span></span>
					</button>
				</td>
				<td><input type="text" value="${exceptionNo}"
					name="exceptionNo"
					style="width: 90%; border: none; text-align: center; background: none;"
					readonly="readonly"></input> <input type="hidden"
					value="${factoryClassify}" name="factoryClassify"></input></td>
			</tr>
		</s:iterator>
	</tbody>
</table>