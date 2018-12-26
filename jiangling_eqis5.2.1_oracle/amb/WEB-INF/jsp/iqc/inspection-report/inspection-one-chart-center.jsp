<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.ambition.util.common.CommonUtil1"%>
<%@page import="com.ambition.supplier.utils.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.iqc.entity.IncomingInspectionActionsReport" %>
<%@ page import="com.ambition.iqc.inspectionreport.service.IncomingInspectionActionsReportManager" %>
<%@ page import="com.ambition.iqc.inspectionreport.dao.IncomingInspectionActionsReportDao" %>
<%@ include file="/common/taglibs.jsp"%>
<%
	Calendar calendar = Calendar.getInstance();
	String endDateStr1 = DateUtil.formateDateStr(calendar);
	calendar = Calendar.getInstance();
	calendar.add(Calendar.DATE,-10);
	String startDateStr1 = DateUtil.formateDateStr(calendar);
	calendar.set(Calendar.DAY_OF_MONTH, 1);
	
	calendar = Calendar.getInstance();
	String endDateStr = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM");
	calendar.add(Calendar.MONTH,-1);
	String startDateStr = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM");
	
    //物料大类
    ActionContext.getContext().put("materialTypes",ApiFactory.getSettingService().getOptionsByGroupCode("IQC_MATERIAL_TYPE"));
%>
<div class="ui-layout-center">
	<div class="opt-body" style="overflow:auto;">
	 	<div class="opt-btn"><span style="margin-left:4px;color:red;float:left;" id="message"></span></div>	
		<div id="search" style="display:block">
			<form action="" onsubmit="return false;">
			<table class="form-table-outside-border" style="width:100%">
				<tr>
					<td style="width:300px;">
						<input type="radio" name="params.myType" id="month" onclick="typeSelect(this,'month');" value="month" checked="checked" /><label for="month">月份</label>
						<input type="radio" id="date" name="params.myType" onclick="typeSelect(this,'date');" value="date"/><label for="date">日期</label>
						<input id="datepicker1" type="text" name="params.startDate_ge_date" readonly="readonly" style="width:72px" class="line" value="<%=startDateStr%>"/>至
					    <input id="datepicker2" type="text" name="params.endDate_le_date" readonly="readonly" style="width:72px" class="line" value="<%=endDateStr%>"/>
					</td>
					<td>
						<span style="margin-left:4px;float:left;">供应商</span>
						<input type="text" onclick="dutySupplierClick();" id="params.dutySupplier" name="params.supplierName_like" style="width:170px;margin-left:4px;float:left;"/>
						<input type="hidden" id="params.dutySupplierCode" name="params.supplierCode_equals"/>
						<a class="small-button-bg" style="float:left;margin-left:2px;margin-top:2px;" onclick="dutySupplierClick(this)" href="javascript:void(0);" title="选择供应商"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
					</td>
				<%-- 	<td>
					    <span style="margin-left:4px;">事业部</span>
					    <s:select list="businessUnits" 
							  theme="simple"
							  listKey="businessUnitCode" 
							  listValue="businessUnitName" 
							  id="businessUnitCode"
							  multiselect="true"
							  name="params.businessUnitCode_in"
							  ></s:select>
					 </td> --%>
					<!--  <td>				
						<span style="margin-left:4px;">单据类型</span>
					    <select name="params.formType_equals">
								<option value=""></option>
								<option value="入库单">入库单</option>
								<option value="退货单">退货单</option>
							</select>
					</td>
					<td>							
						<span style="margin-left:4px;">物料级别</span>
						<input id=materialLevels name="materialLevels" onclick="showAllMateriel();"/>
						<input type="hidden" id="params.checkBomMaterialLevels" name="params.checkBomMaterialLevels"/>
					</td> -->
				</tr>
				<tr>
				<%-- 	<td>
						<span style="margin-left:40px;">物料类别</span>
						<s:select list="materialTypes" 
							  theme="simple"
							  listKey="value" 
							  listValue="name"
							  id="checkBomMaterialType"
							  multiselect="true"
							  name="params.checkBomMaterialType_in"></s:select>
					</td> --%>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td colspan="4" style="text-align: right;" >
						<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_INSPECTION-ONE-CHART_STATA,SUPPLIER_STAT_INSPECTION-ONE-CHART_STATA">
							<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
						</security:authorize>
						<button class='btn' type="button" onclick="javascript:this.form.reset();formReset()"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
						<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_INSPECTION-ONE-CHART_EXPORT,SUPPLIER_STAT_INSPECTION-ONE-CHART_EXPORT">
							<button class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
						</security:authorize>
						<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_INSPECTION-ONE-CHART_EMAIL,SUPPLIER_STAT_INSPECTION-ONE-CHART_EMAIL">
							<button class='btn' type="button" onclick="sendChart();"><span><span><b class="btn-icons btn-icons-email"></b>发送邮件</span></span></button>&nbsp;
						</security:authorize>
					</td>
				</tr>
			</table>
			</form>
		</div>
		<div>
			<table style="width:98.5%;">
				<tr>
					<td id="reportDiv_parent">
						<div id='reportDiv'></div>
					</td>
					
				</tr>
				<tr>
					<td style="text-align: left;" id="materialTypeInformation" >
						
					</td>
				</tr>
				<tr>
					<td id="detailTableDiv_parent">
						<table id="detailTable"></table>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>
