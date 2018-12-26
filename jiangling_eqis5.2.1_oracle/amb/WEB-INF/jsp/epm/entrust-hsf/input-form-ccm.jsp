<%@page import="sun.java2d.pipe.SpanShapeRenderer.Simple"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar calendar = Calendar.getInstance();
	String dateStr = sdf.format(calendar.getTime());
	String user = ContextUtils.getUserName();
	
%> 
	<table class="form-table-without-border" style="width:100%;margin: auto;">
		<caption><h2>可靠性试验委托单</h2></caption>
		<input type="hidden" id="currentActivityName" name="currentActivityName" value="${currentActivityName }" />
<%-- 		<input  type="hidden" id="id" name="id" value="${id}" /> --%>
 		<input name="taskId" id="taskId" value="${taskId}" type="hidden" /> 
<%--		<input type="hidden" id=formNo name="formNo" value="${formNo}" /> --%>
		<caption style="text-align:left;padding-bottom:4px;">表单.NO:${formNo}</caption>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<tr>
			<td style="text-align:center;">申请人</td>
 			<td style="width:10%;"><input id="consignor" name="consignor" readonly="ture" value="${consignor }"/> </td>
			<td style="text-align:center;">联系方式</td>
			<td ><input id="contact" name="contact" value="${contact}"/></td>
			<td style="text-align:center;">申请部门</td>
			<td ><input id="consignorDept" name="consignorDept" readonly="ture" value="${consignorDept}"/></td>
			<td style="text-align:center;">申请日期</td>
			<td style="width:10%;"><input id="consignableDate" name="consignableDate" isDate="true" value="<s:date name='consignableDate' format="yyyy-MM-dd"/>"/></td>
		</tr>
		
		<tr>
			<td style="text-align:center;">客户编号</td>
			<td ><input id="customerNo" name="customerNo" value="${customerNo}" style="float:left;" />
			<a class="small-button-bg" onclick="Epm(this)" ><span class="ui-icon ui-icon-search"></span></a>
			</td>
			<td style="text-align:center;">机种</td>
			<td >
<%-- 				<s:select list="productNos"	listKey="value" listValue="value" name="productNo" id="productNo" emptyOption="true" onchange="" theme="simple"></s:select>  --%>
 				<input id="productNo" name="productNo" value="${productNo}" />
			</td>
			<td style="text-align:center;">样品类别</td>
			<td >
<%-- 				<s:select list="sampleTypes" listKey="value" listValue="value" name="sampleType" id="sampleType" emptyOption="true" onchange="" theme="simple"></s:select>  --%>
			<input id="sampleType" name="sampleType" value="${sampleType}" />
			</td>
			<td style="text-align:center;">批号</td>
			<td ><input id="lotNo" name="lotNo" value="${lotNo}"/></td>
			
		</tr>
		<tr>
			<td style="text-align:center;">样品数量</td>
			<td ><input id="quantity" name="quantity" value="${quantity}" class="{number:true,messages:{number:'请输入有效数字!'}}" /></td>
			<td style="text-align:center;">希望完成时间</td>
			<td style="width:10%;"><input id="deadline" name="deadline" isDate="true" value="<s:date name='deadline' format="yyyy-MM-dd"/>"/></td>
			<td style="width:15%;text-align: center;">是否留样:</td>
			<td>
				<input  type="radio" id="d1" name="sampleHandling" value="是"  <s:if test='%{sampleHandling=="是"}'>checked="true"</s:if> title="是"/><label for="d1">是</label>
				<input  type="radio" id="d2" name="sampleHandling" value="否"  <s:if test='%{sampleHandling=="否"}'>checked="true"</s:if> title="否"/><label for="d2">否</label>
			</td>
			<td style="text-align:center;">管理编号</td>
			<td ><input id="managerAssets" name="managerAssets" value="${managerAssets}" /></td>
		</tr>
	
		<tr>
			<td style="text-align:center;">产品阶段</td>
			<td colspan="5">
			<s:radio cssStyle="margin-left:8px;" name="category" id="category" value="#request.category" theme="simple" list="categorys" onclick="showInput(this)" listKey="value" listValue="name"/>
			</td>
			<td style="text-align:center;">异常申请编号</td>
			<td ><input type="hidden" id="abnormalOdd" name="abnormalOdd" value="${abnormalOdd}"/></td>
		</tr>
		<tr>
			<td colspan="8" style="text-align:center;">试验明细</td>
		</tr>
		<tr>
			<td rowspan="2" style="text-align:center;">目的测试后要求:</td>
			<td colspan="7"><textarea rows="3" id="purpose" name="purpose">${purpose}</textarea></td>
		</tr>
		<tr>
			<td colspan="7"><input type="hidden" isFile="true" name="aimFile" id="aimFile" value="${aimFile }"/></td>
		</tr>
		<tr>
			<td rowspan="2" style="text-align:center;">样品描述</td>
			<td colspan="7"><textarea rows="3" id="sampleDiscription" name="sampleDiscription">${sampleDiscription }</textarea></td>
		</tr>
		<tr>
			<td colspan="7"><input type="hidden" isFile="true" name="describeFile" id="describeFile" value="${describeFile }"/></td>
		</tr>
		<tr>
			<td colspan="8" style="text-align:center;">测试项目</td>
		</tr>
		<tr>
			<td colspan="8" style="padding:0px;" id="checkItemsParent" >
			<div style="overflow-x:auto;overflow-y:hidden;">
				<table class="form-table-border-left" style="border:0px;table-layout:fixed;">
				<tbody>
					<tr >	
	               		<td  style="width:60px;text-align:center;border-top:0px;border-left:0px;">序号</td>
						<td  style="width:150px;text-align:center;border-top:0px;">试验项目</td>
						<td  style="width:200px;text-align:center;border-top:0px;">测试条件</td>
						<td  style="width:50px;text-align:center;border-top:0px;">数量</td>
						<td style="width:200px;text-align:center;border-top:0px;">判定标准</td>
						<td style="width:100px;text-align:center;border-top:0px;">预计开始时间</td>
						<td style="width:100px;text-align:center;border-top:0px;">预计结束时间</td>
						<td style="width:100px;text-align:center;border-top:0px;">实际开始时间</td>
						<td style="width:100px;text-align:center;border-top:0px;">实际结束时间</td>
						<td style="width:100px;text-align:center;border-top:0px;">测试前结果</td>
						<td style="width:100px;text-align:center;border-top:0px;">测试后结果</td>
						<td style="width:100px;text-align:center;border-top:0px;">备注</td>
						<td style="width:100px;text-align:center;border-top:0px;">异常处理</td>
						<td style="width:160px;text-align:center;border-top:0px;">异常处理单号</td>
					</tr>
					<s:iterator value="_entrustOrtSublists" id="item" var="item">
						<tr class="entrustOrtSublists" zbtr1=true>
							<td style="text-align:center;">
								<a class="small-button-bg" addBtn="true"  onclick="addRowHtml(this)" href="#" title="添加">
								<span class="ui-icon ui-icon-plus" style='cursor: pointer;'></span></a> 
								<a class="small-button-bg" delBtn="true"  onclick="removeRowHtml(this)" href="#" title="删除">
								<span class="ui-icon ui-icon-minus" style='cursor: pointer;'></span></a>
							</td>					
							<td style="text-align: center;"><input stage="one" style="width:80%;" name="properties" value="${properties }"/>
								<a class="small-button-bg" onclick="selectProject(this);"  style="float:right;"><span class="ui-icon ui-icon-search"></span></a>
							</td>
							<td style="text-align: center;">
							<input  style="width:90%;" name="ortId" type="hidden" value="${ortId }" />
							<textarea stage="one" style="width:95%;" name="testCondition" >${testCondition }</textarea></td>
							<td style="text-align: center;"><input stage="one" style="width:90%;" name="testNumber" value="${testNumber }" onchange="setweigh(this)" class="{number:true,messages:{number:'请输入有效数字!'}}" /></td>
							<td style="text-align: center;"><textarea stage="one" style="width:95%;" name="criterionG" value="" >${criterionG }</textarea></td>
							<td style="text-align: center;"><input stage="two" style="width:100%;" name="startTime" isDate="true" value="<s:date name='startTime' format="yyyy-MM-dd"/>" /></td>
							<td style="text-align: center;"><input stage="two" style="width:100%;" name="endTime" isDate="true" value="<s:date name='endTime' format="yyyy-MM-dd"/>" /></td>
							<td style="text-align: center;"><input stage="three" style="width:100%;" name="factStartTime" isDate="true" value="<s:date name='factStartTime' format="yyyy-MM-dd"/>" /></td>
							<td style="text-align: center;"><input stage="three" style="width:100%;" name="factEndTime" isDate="true" value="<s:date name='factEndTime' format="yyyy-MM-dd"/>" /></td>
							<td style="text-align: center;">
									<s:select list="testResults" style="width:100px;"
										listKey="value" 
										listValue="value" 
										name="testBefore" 
										id="testBefore"
										emptyOption="true" 
										onchange=""
										stage="three"
										theme="simple">
									</s:select>
								</td>
								<td style="text-align: center;">
									<s:select list="testResults" style="width:100px;"
										listKey="value" 
										listValue="value" 
										name="testAfter" 
										id="testAfter"
										emptyOption="true" 
										onchange="isTestResult()"
										stage="three"
										theme="simple">
									</s:select>
								</td>
								<td><textarea stage="three" style="width:95%;" name="remark" >${remark }</textarea></td>
						     	<td style="text-align: center;" >
									<button class="btn" type="button" isButton=true onclick="addExceptionSingle(this)">
										<span><span>生成</span></span>
									</button>
								</td>								    
							<td>
								<input type="text"  value="${exceptionNo}" name="exceptionNo" style="width: 90%;border:none;text-align: center;background: none;" readonly="readonly"></input>
							</td>
						</tr>
					</s:iterator>
			</tbody>
			</table>
			</div>
			</td>
		</tr>
		<tr>
			<td >确认部门</td>
			<td colspan="3">
				<input id="confirmDept" name="confirmDept" hiddenInputId="confirmDeptLoing" style="float: left;" isUser="true" value="${confirmDept }"/>
				<input type="hidden" id="confirmDeptLoing" name="confirmDeptLoing" value="${confirmDeptLoing }"/>
			</td>
			<td >实验室排程</td>
			<td colspan="3">
				<input id="schedule" name="schedule" hiddenInputId="scheduleLoing" style="float: left;" isUser="true" value="${schedule }"/>
				<input type="hidden" id="scheduleLoing" name="scheduleLoing" value="${scheduleLoing }"/>
			</td>
		</tr>
		<tr>
			<td >实验室测试员</td>
			<td colspan="3">
				<input id="tester" name="tester" hiddenInputId="testerLoing" style="float: left;" isUser="true" value="${tester }"/>
				<input type="hidden" id="testerLoing" name="testerLoing" value="${testerLoing }"/>
			</td>
			<td >报告审核</td>
			<td colspan="7">
				<input id="reportAudit" name="reportAudit" hiddenInputId="reportAuditLoing" style="float: left;" isUser="true" value="${reportAudit }"/>
				<input type="hidden" id="reportAuditLoing" name="reportAuditLoing" value="${reportAuditLoing }"/>
			</td>
		</tr>
		<tr>
			<td>测试报告</td>
			<td colspan="3"><input style="width:100%;" type="hidden" isFile="true" name="testReport" value="${testReport }" />
			</td>
			<td colspan="4"></td>
		</tr>
		<tr type="hidden">
		<td>测试结果</td>
			<td >
				<input id="textResult" name="textResult" value="${textResult }" style="width: 90%;border:none;background: none;" readonly="readonly"/>
				<input type="hidden" id="epmState" name="epmState" value="${epmState }"/>
			</td>
			<td style="text-align:center;">进货检验单号</td>
			<td ><input id="inspectionNo" name="inspectionNo" value="${inspectionNo}" style="width: 90%;border:none;background: none;" readonly="readonly"/>
			<input id="inspectionId" name="inspectionId" value="${inspectionId}" type="hidden"/></td>
			<td colspan="4"></td>
		</tr>
	</table>