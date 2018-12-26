<%@page import="com.ambition.util.common.CommonUtil1"%>
<%@page import="com.ambition.supplier.utils.DateUtil"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
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
	//选择不良代码
	var selectInputObj = '';
	function selectDefectionCode(obj){
		selectInputObj = obj;
		var url = '${mfgctx}/common/code-bom-select.htm';
		$.colorbox({href:url,iframe:true, 
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"选择不良代码"
		});
	}

	//选择之后的方法 data格式{key:'a',value:'a'}
	function setDefectionValue(data){
		$(selectInputObj).val(data[0].value).focus();
	}
	
	$(document).ready(function(){
		if($("#workshopSelect").val()){
			$("#workshopSelect").attr("disabled","disabled");
			$("#workshopRadio").attr("disabled","disabled");
			$("#factoryRadio").attr("disabled","disabled");
		}
	});
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
	<div class="opt-btn" id="btnDiv">
		<span id="message" style="color:red;"></span>
	 </div>
	 <div id="searchDiv" style="display:block;padding:4px;">
		<form onsubmit="return false;">
			<table class="form-table-outside-border"  style="width:100%;padding:4px;">
				<caption style="font-weight: bold;text-align: left;margin:4px;">查询条件</caption>
				<tr>
					<td style="padding:2px 0px 8px 0px;">
						<ul id="searchUl">
<!-- 					 		<li> -->
<!-- 					 			<span class="label">检验日期</span> -->
<%-- 					 			<input id="datepicker1" type="text" readonly="readonly" style="width:72px" name="params.startDate_ge_date" value="<%=startDateStr%>"/>至 --%>
<%-- 						    	<input id="datepicker2" type="text" readonly="readonly" style="width:72px" name="params.endDate_le_date" value="<%=endDateStr%>"/> --%>
<!-- 					 		</li> -->
							<li>
					 			<span style="margin:0px;padding:0px;float: left;">
					 			<select style="width:70px;height:21px;line-height:21px;margin:0px;padding:0px;" name="params.myType" onchange='typeTimeSelect(this.value);'>
					 				<option value="date" selected="selected">检验日期</option>
					 				<option value="month">检验月份</option>
					 			</select>&nbsp;
								</span>
								<input id="datepicker1" type="text" readonly="readonly" style="width:70px;" class="line" name="params.startDate_ge_date" value="<%=startDateStr%>"/>至
					    		<input id="datepicker2" type="text" readonly="readonly" style="width:70px;" class="line" name="params.endDate_le_date" value="<%=endDateStr%>"/>
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
					 			<span class="label">物料级别</span>
								<input id=materialLevels name="materialLevels" onclick="showAllMateriel();"/>
								<input type="hidden" id="params.checkBomMaterialLevels" name="params.checkBomMaterialLevels"/>
					 		</li>
				 		</ul>
					</td>
				</tr>
			</table>
			<table class="form-table-outside-border"  style="width:100%;padding:4px;">
				<caption style="font-weight: bold;text-align: left;margin:4px;">分析对象</caption>
				<tr>
					<td>
						<input name="params.group" id="group_section" type="radio" value="section" onclick="targetSelect(this,'_section')" title="工段"/>
						<label for="group_section">工段</label>
						<s:select id="_section" list="sections"
							  theme="simple"
							  listKey="value" 
							  disabled="true"
							  listValue="name" 
							  cssClass="targerSelect"
							  name="params.groupValue">
						</s:select>
						<input name="params.group" id="group_productionLine" type="radio" value="productionLine" onclick="targetSelect(this,'_productionLine')" title="生产线"/>
						<label for="group_productionLine">生产线</label>
						<s:select id="_productionLine" list="productionLines"
							  theme="simple"
							  listKey="value" 
							  disabled="true"
							  listValue="name" 
							  cssClass="targerSelect"
							  name="params.groupValue">
						</s:select>
						<input name="params.group" id="group_workGroupType" type="radio" value="workGroupType" onclick="targetSelect(this,'_workgroup')" title="生产班组"/>
						<label for="group_workGroupType">班别</label>
						<s:select id="_workgroup" list="workGroupTypes" 
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
						<td><input type="radio" id="type_inspection" name="params.type" checked="checked" value="inspection" onclick="typeSelect(this,'inspection_select')" id="inspection_select_radio"/>
						<label for="type_inspection">检验&nbsp;&nbsp;&nbsp;&nbsp;
						检验采集点</label>
						<s:select id="inspection_select" cssClass="typeSelect" 
									  list="inspectionSpectionPoints" 
									  theme="simple"
									  listKey="inspectionPointName" 
									  listValue="inspectionPointName"
									  emptyOption="true"
									  name="params.inspectionPoint_equals"></s:select>
						</td>
						<td><input type="radio" id="type_check" name="params.type" value="check" onclick="typeSelect(this,'check_select')"/>
						<label for="type_check">检查&nbsp;&nbsp;&nbsp;&nbsp;
						检查采集点</label><s:select id="check_select" cssClass="typeSelect"
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
