<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.DAY_OF_MONTH,1);
	String startDateStr = sdf.format(calendar.getTime());
	
	calendar.add(Calendar.MONTH,1);
	calendar.add(Calendar.DATE,-1);
	String endDateStr = sdf.format(calendar.getTime());
%>
<div class="opt-body">
	<form onsubmit="return false;">
	<div class="opt-btn" id="btnDiv">
		<button type="button"  class='btn' onclick="reset();formReset();"><span><span>重置查询</span></span></button>
		<button type="button"  class='btn' onclick="goToBack()"><span><span>返回</span></span></button>
	</div>
	<div id="searchDiv" style="display:block;padding:4px;">
		<input type="hidden" name="params.workshop_equals" name="workshop" value="${workshop}"/>
		<table class="form-table-outside-border" style="width:100%;display:block;" id="searchTable">
			<tr>
				<td>
					<ul id="searchUl">
				 		<li>
				 			<span>日期</span>
				 			<input id="datepicker1" type="text" readonly="readonly" style="width:70px;" class="line" name="params.startDate_ge_date" value="<%=startDateStr%>"/>至
				   	 		<input id="datepicker2"type="text" readonly="readonly" style="width:70px;" class="line" name="params.endDate_le_date" value="<%=endDateStr%>"/>
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
				 			<span>部位</span>
				 			<input type="text" name="params.部位_equals" disabled="disabled"/>
				 		</li>
				 		<li>
				 			<span>方位</span>
				 			<input type="text"  name="params.方位_equals" disabled="disabled"/>
				 		</li>
				 		<li>
				 			<span>不良</span>
				 			<input type="text" name="params.itemname_equals" value="" id="itemname"/>
				 		</li>
				 	</ul>
				</td>
			</tr>
		</table>
		<table class="form-table-outside-border" style="width:100%">
			<caption style="font-weight: bold;text-align: left;margin:4px;">统计对象</caption>
			<tr>
				<td><input type="radio" name="params.type" checked="checked" value="inspection" onclick="typeSelect(this,'inspection_select')" id="inspection_select_radio"/>检验&nbsp;&nbsp;&nbsp;&nbsp;
				检验采集点
				<s:select id="inspection_select" cssClass="typeSelect" 
							  list="inspectionSpectionPoints" 
							  theme="simple"
							  listKey="inspectionPointName" 
							  listValue="inspectionPointName" 
							  emptyOption = "true"
							  name="params.inspectionPoint_equals"></s:select>
				</td>
				<td><input type="radio" name="params.type" value="check" onclick="typeSelect(this,'check_select')"/>检查&nbsp;&nbsp;&nbsp;&nbsp;
				检查采集点
				<s:select id="check_select" cssClass="typeSelect"
							  disabled="true" 
							  list="checkSpectionPoints" 
							  theme="simple"
							  listKey="inspectionPointName" 
							  listValue="inspectionPointName"
							  emptyOption = "true" 
							  name="params.inspectionPoint_equals"></s:select>
				</td>
			</tr>
		</table>
		<table class="form-table-outside-border" style="width:100%;">
			<caption style="font-weight: bold;text-align: left;margin:4px;">统计分组</caption>
			<tr>
				<td >
					<div id="statType">
						<input type="radio" name="params.group" checked="checked" value="rate" title="不良发生率"/>不良发生率
						&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="params.group" value="number" title="不良数"/>不良数
					</div>
				</td>
				<td align="right">
					<button  class='btn' onclick="search()"><span><span>统计</span></span></button>&nbsp;&nbsp;
					<button class='btn' onclick="toggleSearchTable(this);">
						<span><span>隐藏查询</span></span>
					</button>&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	</div>
	</form>	
	<div>
		<table style="width:98%;">
			<tr>
				<td id="reportDiv_parent" width="100%">
					<div id='reportDiv'></div>
				</td>
			</tr>
			<tr>
				<td id="detailTableDiv_parent" width="100%" style="display:none;">
					<table id="detail_table"></table>
				</td>
			</tr>
		</table>
	</div>
</div>
