<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.DAY_OF_MONTH,1);
	String startDateStr = sdf.format(calendar.getTime());
	
	calendar.add(Calendar.MONTH,1);
	calendar.add(Calendar.DATE,-1);
	String endDateStr = sdf.format(calendar.getTime());
	
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
	calendar = Calendar.getInstance();
	calendar.set(Calendar.YEAR,2012);
	calendar.set(Calendar.MONTH,0);
	calendar.set(Calendar.DATE,1);
	calendar.set(Calendar.HOUR_OF_DAY,0);
	calendar.set(Calendar.MINUTE,0);
	calendar.set(Calendar.MILLISECOND,0);
	calendar.set(Calendar.SECOND,0);
	String startYearStr = sdf1.format(calendar.getTime());
	
	calendar = Calendar.getInstance();
	calendar.set(Calendar.YEAR,2012);
	calendar.set(Calendar.MONTH,12);
	calendar.set(Calendar.DATE,1);
	calendar.add(Calendar.DATE,-1);
	calendar.set(Calendar.HOUR_OF_DAY,23);
	calendar.set(Calendar.MINUTE,59);
	calendar.set(Calendar.MILLISECOND,59);
	calendar.set(Calendar.SECOND,59);
	String endYearStr = sdf1.format(calendar.getTime());
%>
<script type="text/javascript">
<!--
	$(document).ready(function(){
		if($("#workshopSelect").val()){
			$("#workshopSelect").attr("disabled","disabled");
		}
	});
//-->
</script>
<div class="opt-body" style="overflow-y:auto;">
	<form onsubmit="return false;">
	<div class="opt-btn" id="btnDiv">
	<span style="margin-left:4px;color:red;" id="message"></span>
	</div>
	 <div id="customerSearchDiv" style="display:block;padding:4px;">
			<input type="hidden" name="params.inspectionPointType_in" value="<%=request.getAttribute("inspectionPointType")==null?"":request.getAttribute("inspectionPointType")%>"></input>
			<input type="hidden" name="params.myType" value="date"/>
			<table class="form-table-outside-border" style="width:100%;display:block;" id="curstomerSearchTable">
				<tr>
					<td style="padding-left:6px;padding-bottom:4px;">
						<ul id="searchUl">
					 		<li>
				 				<span>检验日期</span>
								<input id="datepicker1" type="text" readonly="readonly" style="width:72px;" class="line" name="params.startDate_ge_date" value="<%=startDateStr%>"/>至
					    		<input id="datepicker2" type="text" readonly="readonly" style="width:72px;" class="line" name="params.endDate_le_date" value="<%=endDateStr%>"/>
					 		</li>
					 		<li>
					 			<span>产品类型</span>
					 			<s:select list="productModels" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="params.productModel_equals"
								  labelSeparator=""
								  emptyOption="true"></s:select>
					 		</li>
					 		<li>
					 			<span>产品型号</span>
					 			<s:select list="modelSpecifications" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="params.modelSpecification_equals"
								  labelSeparator=""
								  emptyOption="true"></s:select>
					 		</li>
					 		<li>
					 			<span>工厂</span>
					 			<s:select list="factorys" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="params.factory_equals"
								  labelSeparator=""
								  emptyOption="true"></s:select>
					 		</li>
					 		<li>
					 			<span>车间</span>
					 			<s:select list="workshops" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="params.workshop_equals"
								  labelSeparator=""
								  value="workshop"
								  id="workshopSelect"
								  emptyOption="true"></s:select>
					 		</li>
					 		<li>
					 			<span>生产线</span>
					 			<s:select list="productionLines" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="params.productionLine_equals"
								  labelSeparator=""
								  emptyOption="true"></s:select>
					 		</li>
					 		<li>
					 			<span>班组</span>
					 			<s:select list="workGroups" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="params.workGroup_equals"
								  labelSeparator=""
								  emptyOption="true"></s:select>
					 		</li>
					 		<li>
					 			<span>班别</span>
					 			<s:select list="workGroupTypes" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="params.workGroupType_equals"
								  labelSeparator=""
								  emptyOption="true"></s:select>
					 		</li>
					 	</ul>
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
				</tr>
				<tr>
				<td colspan="2" style="text-align:right;">
					<button  class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
					<button  class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					<button  class='btn' type="button" onclick="reset();formReset();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
					<button  class='btn' onclick="toggleSearchTable(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>隐藏查询</span></span></button> 
				</td>
				</tr>
			</table>
	</div>
	</form>	
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
