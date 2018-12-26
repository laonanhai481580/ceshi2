<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="java.util.List"%>
<%@page import="com.ambition.supplier.utils.DateUtil"%>
<%@page import="java.util.Calendar"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.Date"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="ui-layout-center" style="overflow:auto;">
	<%
		Calendar calendar = Calendar.getInstance();
		Integer currentYear = calendar.get(Calendar.YEAR);
		
		List<Option> options = new ArrayList<Option>();
		for(int i=currentYear;i>=2013;i--){
			Option option = new Option();
			option.setName(String.valueOf(i));
			option.setValue(String.valueOf(i));
			options.add(option);
		}
		ActionContext.getContext().put("totalYears",options);
		
		String endDateStr = DateUtil.formateDateStr(calendar);

		calendar.add(Calendar.DATE, -15);
		String startDateStr = DateUtil.formateDateStr(calendar);
		
		calendar = Calendar.getInstance();
		String endDateStr1 = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM");
		calendar.add(Calendar.MONTH,-1);
		String startDateStr1 = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM");
		
		
		List<Option> classifications = ApiFactory.getSettingService().getOptionsByGroupCode("iqc_Classification");
		ActionContext.getContext().put("classifications",classifications);
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
	<form onsubmit="return false;">
	<div class="opt-body" >
		<div class="opt-btn" id="btn" style="line-height:33px;">
		<span style="margin-left:4px;color:red;" id="message"></span>
		</div>	
		<div id="search" style="display:block;padding:4px;">
<!-- 			<input type="hidden" name="params.myType" value="date"/> -->
			<table class="form-table-outside-border" style="width:100%">
				<tr>
					<td style="text-align:right;width:70px;">检验</td>
					<td>
<%-- 						<input id="datepicker1" type="text" name="params.startDate_ge_date" readonly="readonly" style="width:70px" class="line" value="<%=startDateStr%>"/>至 --%>
<%-- 						<input id="datepicker2" type="text" name="params.endDate_le_date" readonly="readonly" style="width:70px" class="line" value="<%=endDateStr%>"/> --%>
						<input type="radio" name="params.myType" id="month" onclick="typeSelect(this,'month');" value="month" checked="checked" /><label for="month">月份</label>
						<input type="radio" id="date" name="params.myType" onclick="typeSelect(this,'date');" value="date"/><label for="date">日期</label>
						<input id="datepicker1" type="text" name="params.startDate_ge_date" readonly="readonly" style="width:72px" class="line" value="<%=startDateStr1%>"/>至
					    <input id="datepicker2" type="text" name="params.endDate_le_date" readonly="readonly" style="width:72px" class="line" value="<%=endDateStr1%>"/>
					</td>
					<%-- <td style="text-align:right;width:60px;">缺陷等级</td>
					<td>
						<s:select list="classifications"  
								  listValue="value" 
								  listKey="name" 
								  name="params.classification"
								  theme="simple"
								  emptyOption="true" 
								  cssStyle="width:160px;">
						</s:select>
					</td> --%>
					<td>
						<input type="radio" name="params.type" value="materiel" checked="checked"></input>不合格物料
						<input type="radio" name="params.type" value="supplier"></input>供应商
						<input type="radio" name="params.type" value="inspector"></input>检验员
						<input type="radio" name="params.type" value="result"></input>处理结果
					</td>
				</tr>
				<tr>
					<td colspan="4">&nbsp;</td>
					<td style="text-align:right;">
						<security:authorize ifAnyGranted="IQC_DEFECTIVE-GOODS_DEFECTIVE-GOODS-CHART_STATA,SUPPLIER_STAT_DEFECTIVE-GOODS-CHART_STATA">
						<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
						</security:authorize>
						<button class='btn' type="reset"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button> 
						<security:authorize ifAnyGranted="IQC_DEFECTIVE-GOODS_DEFECTIVE-GOODS-CHART_EXPORT,SUPPLIER_STAT_DEFECTIVE-GOODS-CHART_EXPORT">
						<button class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					 	</security:authorize>
					 	<security:authorize ifAnyGranted="IQC_DEFECTIVE-GOODS_DEFECTIVE-GOODS-CHART_EMAIL,SUPPLIER_STAT_DEFECTIVE-GOODS-CHART_EMAIL">
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
					<td id="detailTableDiv_parent">
						<table id="detailTable"></table>
					</td>
				</tr>
			</table>
		</div>
	</div>
	</form>
</div>
