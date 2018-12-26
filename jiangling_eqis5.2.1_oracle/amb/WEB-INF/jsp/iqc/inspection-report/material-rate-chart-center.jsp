<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@page import="com.ambition.util.common.CommonUtil1"%>
<%@page import="com.ambition.supplier.utils.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="ui-layout-center" style="overflow:auto;">
	<%
		Calendar calendar = Calendar.getInstance();
		String endDateStr = DateUtil.formateDateStr(calendar);
		calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE,-10);
		String startDateStr = DateUtil.formateDateStr(calendar);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		calendar = Calendar.getInstance();
		String endDateStr1 = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM");
		calendar.add(Calendar.MONTH,-1);
		String startDateStr1 = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM");
	    //物料大类
	    ActionContext.getContext().put("materialTypes",ApiFactory.getSettingService().getOptionsByGroupCode("IQC_MATERIAL_TYPE"));
	%>
	<script type="text/javascript">
		function typeSelect(obj,selectId){
			$(".typeSelect").attr("disabled",true);
			$("#" + selectId).attr("disabled",false);
			if(obj.value=='month'){
				$('#datepicker1').datepicker('option',{"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
				$('#datepicker2').datepicker('option',{"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
				$('#datepicker1').val("<%=startDateStr1%>");
				$('#datepicker2').val("<%=endDateStr1%>");
			}else{
				$('#datepicker1').datepicker('option',{"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
				$('#datepicker2').datepicker('option',{"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
				$('#datepicker1').val("<%=startDateStr%>");
				$('#datepicker2').val("<%=endDateStr%>");
			}
		}
	</script>
	<form onsubmit="return false;" name="myform">
	<div class="opt-body">
	<div class="opt-btn">
	<span style="margin-left:4px;color:red;" id="message"></span>
	</div>	
	<div id="search" style="display:block;padding-right:2px;"  >
		<table class="form-table-outside-border" style="width:100%;">
			<tr>
				<td>
					<span style="margin-left:4px;">检验</span>
<%-- 					<input id="datepicker1" type="text" name="params.startDate_ge_date" readonly="readonly" style="width:72px" class="line" value="<%=startDateStr%>"/>至 --%>
<%-- 					<input id="datepicker2" type="text" name="params.endDate_le_date" readonly="readonly" style="width:72px" class="line" value="<%=endDateStr%>"/> --%>
					<input type="radio" id="date" name="params.myType" onclick="typeSelect(this,'date');" value="date" checked="checked"/><label for="date">日期</label>
					<input type="radio" name="params.myType" id="month" onclick="typeSelect(this,'month');" value="month"  /><label for="month">月份</label>
					<input id="datepicker1" type="text" name="params.startDate_ge_date" readonly="readonly" style="width:72px" class="line" value="<%=startDateStr%>"/>至
				    <input id="datepicker2" type="text" name="params.endDate_le_date" readonly="readonly" style="width:72px" class="line" value="<%=endDateStr%>"/>
					
				</td>
				<td>
					<span style="margin-left:4px;float:left;">同一供应商</span>
					<input type="text" onclick="dutySupplierClick();" id="params.dutySupplier" name="params.dutySupplier" style="width:178px;float:left;"/>
			  		<input type="hidden" id="params.dutySupplierCode" name="params.dutySupplierCode"/>
					<a class="small-button-bg" style="float:left;margin-left:2px;margin-top:2px;" onclick="dutySupplierClick(this)" href="javascript:void(0);" title="选择供应商"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
				</td>
				<%-- <td>
					<span style="margin-left:4px;">事业部</span>
				    <s:select list="businessUnits" 
					  theme="simple"
					  listKey="businessUnitCode" 
					  listValue="businessUnitName" 
					  name="params.businessUnitCode_equals"
					  id="businessUnitCode"
					  multiselect="true"></s:select>
				</td> --%>
				<td colspan="2">
					<span style="margin-left:4px;float:left;">物料</span>
					<input type="text" onclick="materialNameClick();" id="params.materiel" style="width:265px;margin-left:2px;float:left;" readonly="readonly"/>
				    <input type="hidden" id="params.materielCode" name="params.checkBomCode_in"/>
				    <a class="small-button-bg" style="float:left;margin-left:2px;margin-top:2px;" onclick="materialNameClick(this)" href="javascript:void(0);" title="选择物料"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
				</td>
			</tr>
			<tr>
				<%-- <td>
					<span style="margin-left:4px;">物料级别</span>
					<input id="materialLevels" name="materialLevels" onclick="showAllMateriel();"/>
					<input type="hidden" id="params.checkBomMaterialLevels_in" name="params.checkBomMaterialLevels_in"/>
				</td>
				<td>
					<span style="margin-left:4px;">单据类型</span>
				    <select name="params.formType_equals">
						<option value=""></option>
						<option value="入库单">入库单</option>
						<option value="退货单">退货单</option>
					</select>
				</td>
				<td>
					<span style="margin-left:4px;">物料类别</span>
					<s:select list="materialTypes" 
					  theme="simple"
					  listKey="value" 
					  listValue="name" 
					  name="params.checkBomMaterialType_in"
					  id="checkBomMaterialType"
					  multiselect="true"></s:select>
				</td> --%>
			</tr>
			<tr>
				<td style="text-align: right;" colspan="4">
				<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_MATERIAL-RATE-CHART_STATA">
					<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
				</security:authorize>
				 	<button class='btn' type="button" onclick="clearAll();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
				<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_MATERIAL-RATE-CHART_EXPORT">
				 	<button class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_MATERIAL-RATE-CHART_EMAIL">
				 	<button  class='btn' type="button" onclick="sendChart();"><span><span><b class="btn-icons btn-icons-email"></b>发送邮件</span></span></button>&nbsp;
				</security:authorize>
				</td>
			</tr>
		</table>
	</div>
	<div>
		<table style="width:100%;">
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
	</form>
</div>
