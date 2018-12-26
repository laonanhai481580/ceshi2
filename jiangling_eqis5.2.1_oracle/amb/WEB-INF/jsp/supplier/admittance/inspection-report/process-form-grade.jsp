<%@page import="com.ambition.util.common.DateUtil"%>
<%@page import="com.norteksoft.product.api.entity.Opinion"%>
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
		<span style="color:red;" id="message"><s:actionmessage theme="mytheme" /></span>
	 </div>
	 <div style="float:right;padding-right:4px;line-height:30px;">
	 	<s:if test="task.active==0 && task.code == 'grade'">
			<a href="#opinion-table">填写意见</a>
		</s:if>
	 </div>
</div>	
<div class="opt-content" id="opt-content" style="overflow-y:auto;">
	<form action="${supplierctx}/admittance/inspection-report/complete-task-grade.htm" method="post" id="inspectionReportForm" name="inspectionReportForm">
		<input type="hidden" name="id" value="${id}"></input>
		<input type="hidden" name="operateName" id="operateName" value=""></input>
		<input type="hidden" name="paramsStr" id="paramsStr"></input>
		<input type="hidden" name="taskId" id="taskId" value="${taskId }" />
		<input type="hidden" name="taskTransact" id="taskTransact" />
		<table class="form-table-border-left" id="appraisal-table"	style="width:100%;border:0px;">
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
			<tr height=32>
				<td colspan="6" style="padding:0px;border-bottom:0px;padding-left:4px;">
					考察评分表<span id="totalInfo"></span>
					<s:if test="task.active==0">
					<button class='btn' onclick="loadItemsByUser(true)">
						<span><span>更新考察项目</span></span>
					</button>
					</s:if>
				</td>
			</tr>
		</table>
		<div>
		<table class="form-table-border-left" id="item-table" style="width:100%;">
			<tr height=29>
				<td style="border-top:0px;" level=1 type="info">评分项目加载中,请稍候... </td>
			</tr>
		</table>
		</div>	
		<s:if test="task.active==0 && task.code == 'grade'">
			<table class="form-table-border-left" id="opinion-table" style="width:100%;margin-top:4px;margin-bottom:4px;">
				<tr height=29>
					<td style="background:#99CCFF;font-weight: bold;font-size:14px;">
						我的意见
					</td>
				</tr>
				<tr height=29>
					<td>
						<textarea rows="6" style="width:100%;" name="opinion" id="opinion"></textarea>
					</td>
				</tr>
			</table>
		</s:if>
	</form>
</div>
				