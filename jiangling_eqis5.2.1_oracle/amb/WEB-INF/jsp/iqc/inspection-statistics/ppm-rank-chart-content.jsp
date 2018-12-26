<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.util.common.DateUtil"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	Calendar calendar = Calendar.getInstance();
	String endDateStr = DateUtil.formateDateStr(calendar);
	calendar = Calendar.getInstance();
	calendar.set(Calendar.DAY_OF_MONTH, 1);
	String startDateStr = DateUtil.formateDateStr(calendar);
%>
<div class="opt-body" style="overflow:auto;">
<!-- 		 	<div class="opt-btn"></div>	 -->
	<div id="search" style="display:block;padding: 2px;">
		<form action="" onsubmit="return false;">
			<table class="form-table-outside-border" style="width:100%;">
				<tr>
					<td>
						<span class="spanLabel">检验日期</span>
						<input id="datepicker1" type="text" name="params.startDate_ge_date" readonly="readonly" style="width:72px" class="line" value="<%=startDateStr%>"/>
					        至<input id="datepicker2" type="text" name="params.endDate_le_date" readonly="readonly" style="width:72px" class="line" value="<%=endDateStr%>"/>
				    </td>
				    <td colspan="2" id="supplierTd">
				    	<span class="spanLabel"><input type="radio" name="params.statType" value="0" checked="checked"/>供应商</span>
				    	<input type="text" style="width: 554px" name="supplierName" id="supplierName" readonly="readonly" onclick="selectSupplier();"/>
				    	<input type="hidden" name="params.supplierCode" id="supplierCode"/>
				    </td>
				</tr>
				<tr>
					<td>
						<span class="spanLabel">车型</span>
				    	<s:select id="modelSpecification"
							  list="modelSpecifications" 
							  cssStyle="width:170px;"
							  emptyOption="true"
							  name="params.modelSpecification"
							  theme="simple"
							  listKey="value" 
							  listValue="value">
						</s:select>
					</td>
					<td>
				    	<span class="spanLabel"><input type="radio" id="statType1" name="params.statType" value="1"/>零件</span>
				    	<input type="text" style="width: 554px" name="materialName" id="materialName" readonly="readonly" onclick="selectBom();"/>
				    	<input type="hidden" name="params.materialCode" id="materialCode"/>
				    </td>
					<td colspan="1" style="text-align: right;">
						<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
<!-- 								<button class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button> -->
					 	<button class='btn' type="button" onclick="reset();formReset();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
					 	<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_CHECK-TYPE-CHART_EMAIL">
					 		<button class='btn' id="emailBtn" type="button" onclick="sendChart();"><span><span><b class="btn-icons btn-icons-email"></b>发送邮件</span></span></button>&nbsp;
						</security:authorize>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div id="opt-content" style="display:block;padding: 2px;"></div>
</div>
