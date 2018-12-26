<%@page import="com.norteksoft.product.api.entity.Opinion"%>
<%@page import="com.ambition.util.common.DateUtil"%>
<%@page import="com.norteksoft.task.base.enumeration.TaskProcessingResult"%>
<%@page import="com.ambition.supplier.entity.InspectionReport"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="opt-btn">
	<div style="float: left;" id="btnDiv">
		<s:if test="task.active==0 && task.code == 'grade'">
			<button class='btn' onclick="completeGrade('<%=TaskProcessingResult.AGREEMENT.name()%>','${task.addSignerButton}')">
				<span><span>提交</span>
				</span>
			</button>
		</s:if>
		<s:if test="task.active==0 && task.processingMode.condition == '审批式'">
			<button class='btn' onclick="completeTask('<%=TaskProcessingResult.APPROVE.name()%>','同意')">
				<span><span>同意</span></span>
			</button>
			<button class='btn' onclick="completeTask('<%=TaskProcessingResult.REFUSE.name()%>','不同意')">
				<span><span>不同意</span></span>
			</button>
		</s:if>
		<s:if test="task.active==0 && task.processingMode.condition == '会签式'">
			<button class='btn' onclick="completeTask('<%=TaskProcessingResult.AGREEMENT.name()%>','${task.addSignerButton}')">
				<span><span>${task.addSignerButton}</span>
				</span>
			</button>
			<button class='btn' onclick="completeTask('<%=TaskProcessingResult.OPPOSE.name()%>','${task.removeSignerButton}')">
				<span><span>${task.removeSignerButton}</span>
				</span>
			</button>
		</s:if>
		<s:if
			test="task.active==0 && task.processingMode.condition == '编辑式'">
			<button class='btn' onclick="completeTask('<%=TaskProcessingResult.SUBMIT.name()%>','提交')">
				<span><span>提交</span>
				</span>
			</button>
		</s:if>
		<span style="color:red;" id="message"><s:actionmessage theme="mytheme" /></span>
	 </div>
	 <div style="float:right;padding-right:4px;line-height:30px;">
	 	<s:if test="task.active==0 && task.processingMode.condition != '编辑式'">
			<a href="#opinion-table">填写意见</a>
		</s:if>
	 	<a href="#history-table">查看所有处理意见</a>
	 </div>
	 </div>	
	<div class="opt-content" id="opt-content" style="overflow-y:auto;">
	<form action="${supplierctx}/admittance/inspection-report/complete-task.htm" method="post" id="inspectionReportForm" name="inspectionReportForm">
		<input type="hidden" name="id" value="${id}"></input>
		<input type="hidden" name="operateName" id="operateName" value=""></input>
		<input type="hidden" name="paramsStr" id="paramsStr"></input>
		<input type="hidden" name="taskId" id="taskId" value="${taskId }" />
		<input type="hidden" name="taskTransact" id="taskTransact" />
		<table class="form-table-border-left" id="appraisal-table"	style="width:100%;margin-bottom:4px;">
			<caption style="height: 35px;text-align: center"><h2>潜在供应商考察报告</h2></caption>
			<caption style="margin-bottom:4px;"><div style="float:right;padding-right:8px;padding-bottom:4px;">编号:${inspectionReport.code}</div></caption>
			<tr>
				<td style="width:90px;">供应商<font color="red">*</font></td>
				<td>
					<input id="supplierName" value="${inspectionReport.supplier.name}" style="width:70%;" readonly="readonly"/>
					<input type="hidden" name="supplierId" id="supplierId" value="${inspectionReport.supplier.id}"></input>
<!-- 								<a href="javascript:void(0);viewSupplierInfo();">查看详情</a> -->
				</td>
				<td style="width:90px;">生产的产品</td>
				<td colspan="3">
					<input name="matingProducts" value="${inspectionReport.matingProducts}" style="width:90%;"  readonly="readonly"/>
				</td>
			</tr>
			<tr>
				<td style="width:90px;">稽核日期<font color="red">*</font></td>
				<td>
					<input name="inspectionDate" id="inspectionDate"  readonly="readonly" value="${inspectionReport.inspectionDateStr}" style="width:120px;"/>
				</td>
				<td style="width:90px;">稽核人员<font color="red">*</font></td>
				<td>
					<input id="inspectionManLoginName" value="${inspectionReport.inspectionManLoginName}" type="hidden"/>
					<input id="inspectionMan" readonly="readonly" value="${inspectionReport.inspectionMan}" style="width:120px;"/>
				</td>
				<td style="width:90px;">版本<font color="red">*</font></td>
				<td>
					<input name="reportVersion" value="${inspectionReport.reportVersion}" readonly="readonly" style="width:90px;"/>
				</td>
			</tr>
			</table>
			<table id="inspectionGradeTypes"></table>
			<table class="form-table-border-left" style="width:100%;border-top:0px;margin-top:4px;">
				<tr>
					<td colspan="6" style="font-weight:bold;">考察综合结果</td>
				</tr>
				<tr>
					<td colspan="6">综合评价</td>
				</tr>
				<tr>
					<td colspan="6">
						<s:if
							test="task.active==0 && task.processingMode.condition == '编辑式'">
							<textarea rows=4 style="100%" name="comprehensiveEvaluation" id="comprehensiveEvaluation">${comprehensiveEvaluation}</textarea></td>
						</s:if>
						<s:else>
							<textarea rows=4 readonly="readonly" style="100%" name="comprehensiveEvaluation" id="comprehensiveEvaluation">${comprehensiveEvaluation}</textarea></td>
						</s:else>
				</tr>
				<tr>
					<td style="width:90px;">
						考察结果
					</td>
					<td colspan="5">
						<s:if
							test="task.active==0 && task.processingMode.condition == '编辑式'">
							<input type="radio" id="inspectionResultPass" name="inspectionResult" <s:if test="%{inspectionResult=='通过'}"> checked="checked" </s:if> value="<%=InspectionReport.RESULT_PASS %>"/><%=InspectionReport.RESULT_PASS %>
							<input type="radio" id="inspectionResultFail" name="inspectionResult" <s:if test="%{inspectionResult!='通过'}"> checked="checked" </s:if> value="<%=InspectionReport.RESULT_FAIL %>"/><%=InspectionReport.RESULT_FAIL %>
						</s:if>
						<s:else>
							<input type="radio" disabled=disabled id="inspectionResultPass" name="inspectionResult" <s:if test="%{inspectionResult=='通过'}"> checked="checked" </s:if> value="<%=InspectionReport.RESULT_PASS %>"/><%=InspectionReport.RESULT_PASS %>
							<input type="radio" disabled=disabled id="inspectionResultFail" name="inspectionResult" <s:if test="%{inspectionResult!='通过'}"> checked="checked" </s:if> value="<%=InspectionReport.RESULT_FAIL %>"/><%=InspectionReport.RESULT_FAIL %>
						</s:else>
					</td>
				</tr>
			</table>
		<s:if test="task.active==0 && task.processingMode.condition != '编辑式'">
			<table class="form-table-border-left" id="opinion-table"	style="width:100%;margin-top:4px;">
				<tr height=29>
					<td style="background:#99CCFF;font-weight: bold;font-size:14px;">
						我的意见
					</td>
				</tr>
				<tr height=29>
					<td>
						<textarea rows="6" style="width:100%;" name="opinion"></textarea>
					</td>
				</tr>
			</table>
		</s:if>
		<%
			List<Opinion> opinionParameters = (List<Opinion>)request.getAttribute("opinionParameters");
			if(opinionParameters==null){
				opinionParameters = new ArrayList<Opinion>();
			}
		%>
		<table class="form-table-border-left" id="history-table" style="width:100%;margin-top:4px;margin-bottom:4px;">
			<tr height=29>
				<td style="background:#99CCFF;font-weight: bold;font-size:14px;width:30px;">
					序
				</td>
				<td style="background:#99CCFF;font-weight: bold;font-size:14px;width:140px;">
					时间
				</td>
				<td style="background:#99CCFF;font-weight: bold;font-size:14px;width:100px;">
					处理人员
				</td>
				<td style="background:#99CCFF;font-weight: bold;font-size:14px;width:90px;">
					操作类型
				</td>
				<td style="background:#99CCFF;font-weight: bold;font-size:14px;">
					处理意见
				</td>
				<td style="background:#99CCFF;font-weight: bold;font-size:14px;width:100px;">
					结果
				</td>
			</tr>
			<%
				int index = 1;
				for(Opinion param : opinionParameters){
			%>
				<tr height=24>
					<td>
						<%=index++ %>
					</td>
					<td>
						<%=DateUtil.formateTimeStr(param.getAddOpinionDate()) %>
					</td>
					<td>
						<%=param.getTransactorName() %>
					</td>
					<td>
						<%=param.getTaskName() %>
					</td>
					<td>
						<%=param.getOpinion()==null?"":param.getOpinion() %>
					</td>
					<td >
						<%=param.getCustomField()==null?"":param.getCustomField() %>
					</td>
				</tr>
			<%} %>
		</table>
	</form>
</div>
				