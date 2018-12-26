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
	 </div>
	 <div id="searchDiv" style="display:block;padding:4px;">
		<form onsubmit="return false;">
			<input type="hidden" name="params.inspectionPointType_in" value="<%=request.getAttribute("inspectionPointType")==null?"":request.getAttribute("inspectionPointType")%>"></input>
			<table class="form-table-outside-border" style="width:100%;display:block;" id="searchTable">
			<tr>
				<td style="padding-left:6px;">
					<ul id="searchUl">
				 		<li class="li">
				 			<span class="liSpan">日&nbsp;&nbsp;&nbsp;&nbsp;期</span>
				 			<input id="datepicker1" type="text" readonly="readonly" style="width:71px" name="params.startDate_ge_date" value="<%=startDateStr%>"/>至<input id="datepicker2" type="text" readonly="readonly" style="width:71px" name="params.endDate_le_datetime" value="<%=endDateStr%>"/>
				 		</li>
				 		<li class="li">
				 			<span class="liSpan"><input name="params.group" type="radio" value="productModel" checked="checked" onclick="targetSelect(this,'_productModel')" title="产品类型"/>产品类型</span>
				 			<s:select id="_productModel" list="productModels" 
									  theme="simple"
									  listKey="value" 
									  listValue="value" 
									  cssClass="targerSelect"
									  cssStyle="width:170px;"
									  name="params.groupValue"></s:select>
				 		</li>
				 		<li class="li">
				 			<span class="liSpan"><input name="params.group" type="radio" value="modelSpecification" onclick="targetSelect(this,'_modelSpecification')" title="产品型号"/>产品型号</span>
				 			<s:select id="_modelSpecification" list="modelSpecifications" 
									  theme="simple"
									  listKey="value" 
									  disabled="true"
									  listValue="value" 
									  cssClass="targerSelect"
									  cssStyle="width:170px;"
									  name="params.groupValue"></s:select>
				 		</li>
				 		<li class="li">
				 			<span class="liSpan"><input name="params.group" type="radio" value="workGroupType" onclick="targetSelect(this,'_workGroupType')" title="生产班组"/>生产班组</span>
				 			<s:select id="_workGroupType" list="workGroups" 
									  theme="simple"
									  listKey="name" 
									  disabled="true"
									  listValue="value" 
									  cssClass="targerSelect"
									  cssStyle="width:170px;"
									  name="params.groupValue"></s:select>
				 		</li>
				 	</ul>
				 </td>
			</tr>
			<tr>
				<td>
					
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
						<button  class='btn' type="button" onclick="reset();formReset();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
						</td>
					</tr>
				</table>
		</form>
	</div>
	<div id="reportDivParent">
	</div>
</div>
