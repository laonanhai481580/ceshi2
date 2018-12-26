<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@page import="com.ambition.util.common.CommonUtil1"%>
<%@page import="com.ambition.supplier.utils.DateUtil"%>
<%@ include file="/common/taglibs.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%
	Calendar calendar = Calendar.getInstance();
	String endDateStr = DateUtil.formateDateStr(calendar);
	calendar.add(Calendar.DATE,-15);
	String startDateStr = DateUtil.formateDateStr(calendar);
	
	SimpleDateFormat ymf = new SimpleDateFormat("yyyy-MM");
	calendar = Calendar.getInstance();
	calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
	calendar.set(Calendar.MONTH,0);
	calendar.set(Calendar.DATE,1);
	String startDateStr1 = ymf.format(calendar.getTime());
	calendar = Calendar.getInstance();
	calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
	calendar.set(Calendar.MONTH,12);
	calendar.set(Calendar.DATE,1);
	calendar.add(Calendar.DATE,-1);
	String endDateStr1 = ymf.format(calendar.getTime());
%>
<script type="text/javascript">
<!--
	function typeTimeSelect(objVal){
		if(objVal=='month'){
			$('#datepicker1').datepicker('option',{"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
			$('#datepicker2').datepicker('option',{"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
			$('#datepicker1').val("<%=startDateStr1%>");
			$('#datepicker2').val("<%=endDateStr1%>");
			$('#datepicker1').attr("name","params\.yearAndMonth_ge_int");
			$('#datepicker2').attr("name","params\.yearAndMonth_le_int");
		}else{
			$('#datepicker1').datepicker('option',{"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
			$('#datepicker2').datepicker('option',{"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
			$('#datepicker1').val("<%=startDateStr%>");
			$('#datepicker2').val("<%=endDateStr%>");
			$('#datepicker1').attr("name","params\.startDate_ge_date");
			$('#datepicker2').attr("name","params\.endDate_le_date");
			
		}
	}
//-->
</script>
<div class="opt-body" style="overflow-y:auto;">
	<form onsubmit="return false;">
	<div class="opt-btn" id="btnDiv">
		<span id="message" style="color:red;"></span>
	</div>
	<div id="searchDiv" style="display:block;padding:4px;">
		<input type="hidden" name="params.workshop_equals" name="workshop" value="${workshop}"/>
		<table class="form-table-outside-border" style="width:100%;display:block;" id="searchTable">
			<tr>
				<td>
					<ul id="searchUl">
						<li>
								<span style="margin:0px;padding:0px;float: left;">
					 			<select style="width:70px;height:21px;line-height:21px;margin:0px;padding:0px;" name="params.myType" onchange='typeTimeSelect(this.value);'>
					 				<option value="date" selected="selected">检验日期</option>
					 				<option value="month">检验月份</option>
					 			</select>&nbsp;
								</span>
					 			<input id="datepicker1" type="text" readonly="readonly" style="width:72px" name="params.startDate_ge_date" value="<%=startDateStr%>"/>至
						    	<input id="datepicker2" type="text" readonly="readonly" style="width:72px" name="params.endDate_le_date" value="<%=endDateStr%>"/>
					 		</li>
				 		<li>
				 			<span class="label">车间</span>
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
				 			<span class="label">生产线</span>
				 			<s:select list="productionLines" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="params.productionLine_equals"
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
				 		<li>
				 			<span>产品型号</span>
				 			<input type="text" name="params.modelSpecification_equals"/>
				 		</li>
				 		<li>
				 			<span>产品名称</span>
				 			<input type="text" name="params.itemdutyPart_equals" value="" id="itemdutyPart"/>
				 		</li>
				 		<li>
				 			<span>不良</span>
				 			<input type="text" name="params.itemname_equals" value="" id="itemname"/>
				 		</li>
				 		<li>
				 			<span>物料级别</span>
							<input id=materialLevels name="materialLevels" onclick="showAllMateriel();"/>
							<input type="hidden" id="params.checkBomMaterialLevels" name="params.checkBomMaterialLevels"/>
				 		</li>
				 	</ul>
				</td>
			</tr>
		</table>
		<table class="form-table-outside-border" style="width:100%">
			<caption style="font-weight: bold;text-align: left;margin:4px;">统计对象</caption>
			<tr>
				<td><input type="radio" id="type_inspection" name="params.type" checked="checked" value="inspection" onclick="typeSelect(this,'inspection_select')" id="inspection_select_radio"/>
				<label for="type_inspection">检验&nbsp;&nbsp;&nbsp;&nbsp;
				检验采集点</label>
				<s:select id="inspection_select" cssClass="typeSelect" 
							  list="inspectionSpectionPoints" 
							  theme="simple"
							  listKey="inspectionPointName" 
							  listValue="inspectionPointName" 
							  emptyOption = "true"
							  name="params.inspectionPoint_equals"></s:select>
				</td>
				<td><input id="type_check" type="radio" name="params.type" value="check" onclick="typeSelect(this,'check_select')"/>
					<label for="type_check">检查&nbsp;&nbsp;&nbsp;&nbsp;
					检查采集点</label>
					<s:select id="check_select" cssClass="typeSelect"
						disabled="true" 
						list="checkSpectionPoints" 
						theme="simple"
						listKey="inspectionPointName" 
						listValue="inspectionPointName"
						emptyOption = "true" 
						name="params.inspectionPoint_equals">
					</s:select>
				</td>
			</tr>
		</table>
		<table class="form-table-outside-border" style="width:100%;">
			<caption style="font-weight: bold;text-align: left;margin:4px;">统计分组</caption>
			<tr>
				<td >
					<div id="statType">
						<input type="radio" id="group_rate" name="params.group" checked="checked" value="rate" title="不良发生率"/>
						<label for="group_rate">不良发生率&nbsp;&nbsp;&nbsp;&nbsp;</label>
						<input type="radio" id="group_number" name="params.group" value="number" title="不良数"/>
						<label for="group_number">不良数</label>
					</div>
				</td>
				<td style="text-align:right;">
				<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
				<button class='btn' type="button" onclick="reset();formReset();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
				<button class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				<button  class='btn' type="button" onclick="sendChart();"><span><span><b class="btn-icons btn-icons-email"></b>发送邮件</span></span></button>
				<button class='btn' onclick="toggleSearchTable(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>隐藏查询</span></span></button>&nbsp; 
				</td>
			</tr>
		</table>
	</div>
	</form>	
	<div>
		<table style="width:100%;">
			<tr>
				<td id="reportDiv_parent" width="100%">
					<div id='reportDiv'></div>
				</td>
			</tr>
			<tr>
				<td id="detailTableDiv_parent" width="100%">
					<table id="detail_table"></table>
				</td>
			</tr>
		</table>
	</div>
</div>
