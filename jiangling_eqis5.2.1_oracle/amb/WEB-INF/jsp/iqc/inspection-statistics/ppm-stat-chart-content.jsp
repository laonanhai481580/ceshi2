<%@page import="com.ambition.util.common.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	Calendar calendar = Calendar.getInstance();
	String endDateStr = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM");
	
	calendar.add(Calendar.MONTH,-12);
	String startDateStr = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM");
%>
<div class="opt-body" style="overflow:auto;">
 	<div class="opt-btn">
 		<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
		<button class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
	 	<button class='btn' type="button" onclick="reset();formReset();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
	 	<button class='btn' id="emailBtn" type="button" onclick="sendChart();"><span><span><b class="btn-icons btn-icons-email"></b>发送邮件</span></span></button>&nbsp;
	 	<span style="margin-left:4px;color:red;" id="message"></span>
	 </div>	
	<div id="search" style="display:block;padding: 2px;">
		<form action="" onsubmit="return false;">
			<table class="form-table-outside-border" style="width:100%;">
				<tr>
					<td>
						<span style="float:left;width:70px;text-align:right;padding-right:2px;">统计年月</span>
						<input id="datepicker1" type="text" name="params.startDate_ge_date" readonly="readonly" style="width:72px" class="line" value="<%=startDateStr%>"/>
					        至<input id="datepicker2" type="text" name="params.endDate_le_date" readonly="readonly" style="width:72px" class="line" value="<%=endDateStr%>"/>
				    </td>
				    <td id="supplierTd">
				    	<span style="float:left;width:70px;text-align:right;padding-right:2px;">供应商</span>
						<input type="text" name="params.supplierName_equals" id="params.supplierName_equals" style="width:170px;" readonly="readonly" onclick="selectSupplier();"/>
				 		<input type="hidden" name="params.supplierCode_equals" id="params.supplierCode_equals"/>
					</td>
					<td>
						<span style="float:left;width:70px;text-align:right;padding-right:2px;">零部件</span>
						<input type="text" name="params.partName_equals" id="params.partName_equals" style="width:170px;" readonly="readonly" onclick="selectBom();"/>
				 		<input type="hidden" name="params.partCode_equals" id="params.partCode_equals"/>
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
				<td id="detailTableDiv_parent">
					<table id="detailTable"></table>
				</td>
			</tr>
		</table>
	</div>
	<div style="font-size:18px;font-weight:bold;margin-bottom:12px;">
		<div>
			说明:
		</div>
		<div style="padding-left:8px;">
			&nbsp;月份范围:上月26日到本月25日
		</div>
		<div style="padding-left:8px;">
			&nbsp;&nbsp;不良数:来料不良统计数+过程不良+未装车不良+整车零部件不良
		</div>
		<div style="padding-left:8px;">
			&nbsp;不良PPM:不良数/(检验数+免检数)*1000000
		</div>
	</div>
</div>
