<%@page import="com.ambition.util.common.CommonUtil1"%>
<%@page import="com.ambition.carmfg.entity.BusinessUnit"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="com.ambition.supplier.utils.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	// 工序
    List<Option> workProcedures = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_work_procedure");
    ActionContext.getContext().put("workProcedureOptions", workProcedures);

    // 产品阶段
    List<Option> productPhases = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_product_phase");
    ActionContext.getContext().put("productPhaseOptions", productPhases);

    // 不合格类别
    List<Option> unqualifiedTypes = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_unqualified_type");
    ActionContext.getContext().put("unqualifiedTypeOptions", unqualifiedTypes);
    
    //有权限的事业部
    List<BusinessUnit> businessUnits = CommonUtil1.getCurrentBusinessUnits();
    ActionContext.getContext().put("businessUnits", businessUnits);
    
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.DATE,1);
	String startDateStr = DateUtil.formateDateStr(calendar);
	
	calendar.add(Calendar.MONTH,1);
	calendar.add(Calendar.DATE,-1);
	String endDateStr = DateUtil.formateDateStr(calendar);
%>
<div class="opt-body" style="overflow-y: auto;">
	<form onsubmit="return false;">
		<div class="opt-btn" id="btnDiv">
			<span style="margin-left: 4px; color: red;" id="message"></span>
		</div>
		<div id="searchDiv" style="display: block;">
			<table class="form-table-outside-border"
				style="width: 100%; display: block;" id="searchTable">
				<tr>
					<td style="padding-left: 6px; padding-bottom: 4px;">
						<ul id="searchUl">
							<li><span class="label">检验日期</span> <input id="datepicker1" target=all
								type="text" readonly="readonly" style="width: 72px;"
								class="line" name="params.reportDate_ge_date" value="<%=startDateStr%>" /> 至<input  target=all
								id="datepicker2" type="text" readonly="readonly"
								style="width: 72px;" class="line" value="<%=endDateStr%>" name="params.reportDate_le_date" />
							</li>
							<li><span>所属产业</span> 
								<s:select list="businessUnits" 
								  listKey="businessUnitCode" 
								  listValue="businessUnitName"
								  theme="simple"
								  emptyOption=""
								  headerKey=""
								  headerValue="请选择"
								  name="params.businessUnitCode_equals"></s:select>
					  		</li>
							<li><span>产品型号</span> <input
								id="searchModel"
								target=defectNum
								name="params.itemmodelSpecification_equals" />
								<!--<a id="selectModel" class="small-button-bg" style="vertical-align: middle;" onclick="selectComponentCode();" href="javascript:void(0);" title="选择产品型号"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a> --></li>
							<li><span>责任部门</span> <input type="text" 
								target=lossAmount
								name="params.itemdepartmentName_equals" id="searchDepartmentName"
								onclick="selectObj('选择责任部门', this, 'DEPARTMENT_TREE')" /></li>
						</ul>
					</td>
				</tr>
			</table>
			<table class="form-table-outside-border" style="width: 100%;">
				<caption
					style="font-weight: bold; text-align: left; padding-top: 4px; padding-bottom: 4px;">统计对象</caption>
				<tr>
					<td><input type="radio" name="params.type" checked="checked"
						value="defectNum" id="defectNum" /><label for="defectNum"
						style="padding-right: 20px;">不合格数量</label> <input type="radio"
						name="params.type" value="lossAmount" id="lossAmount" /><label
						for="lossAmount">损失金额</label></td>
					<td style="text-align: right">显示前&nbsp;<input type="text"
						size="5" value="10" style="width: 40px; text-align: center;"
						name="params.pageSize" id="pageSize" readonly="readonly" />&nbsp;项
					</td>
				</tr>
			</table>
			<table class="form-table-outside-border" style="width: 100%;">
				<caption
					style="font-weight: bold; text-align: left; padding-top: 4px; padding-bottom: 4px;">统计分组</caption>
				<tr>
					<td style="padding: 0px; margin: 0px; padding-bottom: 4px;">
						<ul id="groupUl">
							<li><input type="radio" name="params.group" target="all"
								checked="checked" value="businessUnitName" title="归属产业"
								id="businessUnitName" /><label for="businessUnitName">归属产业</label></li>
							<li><input type="radio" name="params.group"
								value="itemmodelSpecification" target="defectNum" title="产品型号" id="itemmodelSpecification" /><label
								for="modelSpecification">产品型号</label></li>
							<li><input type="radio" target="lossAmount"  name="params.group" for="defectNum"
								value="itemdepartmentName" title="责任部门" id="department" /><label
								for="department">责任部门</label></li>
							<li><input type="radio" target="lossAmount" name="params.group"
								value="itemname" title="损失质量成本科目" id="composingName" /><label
								for="composingName">损失质量成本科目</label></li>
						</ul>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">
						<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
						<button class='btn' type="button" onclick="reset();"><span id="redoBtn"><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
						<button class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
						<button class='btn' type="button" onclick="sendChart();"><span><span><b class="btn-icons btn-icons-email"></b>发送邮件</span></span></button>
						<button class='btn' onclick="toggleSearchTable(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>隐藏查询</span></span></button>&nbsp;
					</td>
				</tr>
			</table>
		</div>
	</form>
	<div>
		<table>
			<tr>
				<td width="90%" style="height: 30%;" valign="top"
					id="detail_table_parent">
					<table id="detail_table"></table>
				</td>
				<td width="90%" valign="top">
					<div id="reportDiv_parent">
						<div id='reportDiv'></div>
					</div>
				</td>
			</tr>
		</table>
	</div>
</div>