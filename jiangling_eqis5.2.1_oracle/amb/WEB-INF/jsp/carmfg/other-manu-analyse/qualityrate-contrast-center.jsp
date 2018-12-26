<%@ page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.DAY_OF_MONTH,1);
	String startDateStr = sdf.format(calendar.getTime());
	
	calendar.add(Calendar.MONTH,1);
	calendar.add(Calendar.DATE,-1);
	String endDateStr = sdf.format(calendar.getTime());
%>
<div class="opt-body" style="overflow-y:auto;">
	<div class="opt-btn" id="btnDiv">
	<span style="margin-left:4px;color:red;" id="message"></span>
	 </div>
	 <div id="customerSearchDiv" style="display:block;padding:4px;">
		<form onsubmit="return false;">
			<input type="hidden" name="params.inspectionPointType_in" value="<%=request.getAttribute("inspectionPointType")==null?"":request.getAttribute("inspectionPointType")%>"></input>
			<input type="hidden" name="params.workshop_equals" name="workshop" value="${workshop}"/>
			<table class="form-table-outside-border"  style="width:100%;padding:4px;">
				<tr>
					<td style="width:70px;text-align:right;">日&nbsp;&nbsp;&nbsp;&nbsp;期</td>
					<td colspan="4">
						<input id="datepicker1" type="text" readonly="readonly" style="width:72px" name="params.startDate_ge_date" value="<%=startDateStr%>"/>至
					    <input id="datepicker2" type="text" readonly="readonly" style="width:72px" name="params.endDate_le_datetime" value="<%=endDateStr%>"/>
					</td>
					<td>
						<input name="params.group" type="radio" value="modelSpecification" checked="checked" onclick="targetSelect(this,'_product')" title="产品型号"/>产品型号
						<s:select id="_product" list="modelSpecifications"
							  theme="simple"
							  listKey="value" 
							  listValue="value" 
							  cssClass="targerSelect"
							  name="params.groupValue">
						</s:select>
					</td>
					<td>
						<input name="params.group" type="radio" value="workGroupType" onclick="targetSelect(this,'_workgroup')" title="生产班组"/>生产班组
						<s:select id="_workgroup" list="workGroups" 
							  theme="simple"
							  listKey="name" 
							  disabled="true"
							  listValue="value" 
							  cssClass="targerSelect"
							  name="params.groupValue">
						</s:select>	
					</td>
				</tr>
			</table>
			<table class="form-table-outside-border" style="width:100%" id="typeTable">
					<caption style="font-weight: bold;text-align: left;margin:4px;">统计对象</caption>
					<tr>
						<td><input type="radio" name="params.type" checked="checked" value="inspection" onclick="typeSelect(this,'inspection_select')" id="inspection_select_radio"/>检验&nbsp;&nbsp;&nbsp;&nbsp;
						检验采集点
						<s:select id="inspection_select" cssClass="typeSelect" 
									  list="inspectionSpectionPoints" 
									  theme="simple"
									  listKey="inspectionPointName" 
									  listValue="inspectionPointName"
								  	  emptyOption="true"
									  name="params.inspectionPoint_equals"></s:select>
						</td>
						<td><input type="radio" name="params.type" value="check" onclick="typeSelect(this,'check_select')"/>检查&nbsp;&nbsp;&nbsp;&nbsp;
						检查采集点<s:select id="check_select" cssClass="typeSelect"
									  disabled="true" 
									  list="checkSpectionPoints" 
									  theme="simple"
									  listKey="inspectionPointName" 
									  listValue="inspectionPointName"
								  	  emptyOption="true"
									  name="params.inspectionPoint_equals"></s:select>
						</td>
						<td style="text-align:right;">
						<button  class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
						<button  class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
						<button  class='btn' type="button" onclick="reset();formReset();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
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
</div>
