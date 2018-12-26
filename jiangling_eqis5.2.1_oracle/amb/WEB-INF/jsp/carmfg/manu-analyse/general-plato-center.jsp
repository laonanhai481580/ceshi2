<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.ambition.supplier.utils.DateUtil"%>
<%@page import="com.ambition.util.common.CommonUtil1"%>
<%@page import="com.ambition.carmfg.entity.BusinessUnit"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@ page import="com.norteksoft.product.util.PropUtils"%>
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
	
	boolean isFetchAll = true;
	try{
		isFetchAll = Boolean.valueOf(com.norteksoft.product.util.PropUtils.getProp("isFetchAll"));
	}catch(Exception e){}
    
    
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
	 <form onsubmit="return false;">
	 <div class="opt-btn" id="btnDiv">
	 <span style="margin-left:4px;color:red;" id="message"></span>
	 </div>
	 <div style="padding:4px;" id="customerSearchDiv">
		<table class="form-table-outside-border" style="width:100%;display:block;" id="searchTable">
			<tr>
				<td style="padding-left:6px;padding-bottom:4px;">
					<ul id="searchUl">
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
				 			<span class="label">班别</span>
				 			<s:select list="workGroupTypes" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="params.workGroupType_equals"
								  labelSeparator=""
								  emptyOption="true"></s:select>
				 		</li>
				 		<li>
				 			<span class="label">工段</span>
				 			<s:select list="sections" 
							  theme="simple"
							  listKey="value" 
							  listValue="name" 
							  name="params.section_equals"
							  labelSeparator=""
							  emptyOption="true"></s:select>
				 		</li>
				 		<li>
				 			<span class="label">工序</span>
				 			<s:select list="workProcedures" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="params.workProcedure_equals"
								  labelSeparator=""
								  emptyOption="true"></s:select>
				 		</li>
				 		<li>
				 			<span class="label">产品型号</span>
				 			<input class="input" type="text" name="params.modelSpecification_equals"/>
				 		</li>
				 		<!-- <li>
				 			<span class="label">零部件</span>
				 			<input class="input" type="text" name="params.itemdutyPart_equals" value="" id="itemdutyPart" readonly="readonly" onclick="selectBomValue(this)"/>
				 		</li> -->
				 		<li>
				 			<span class="label">不良类别</span>
				 			<s:select list="defectionTypes" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="params.itemdefectionTypeName_equals"
								  labelSeparator=""
								  emptyOption="true"></s:select>
				 		</li>
				 		<li>
				 			<span class="label">不良</span>
				 			<input class="input" type="text" name="params.itemname_equals" onclick="selectDefectionCode(this);"/>
				 		</li>
				 		<li>
				 			<span class="label">严重度</span>
				 			<s:select list="unqualifiedGrades" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="params.itemdefectionCodeAttributeLevel_equals"
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
		<table class="form-table-outside-border" style="width:100%;">
			<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;">统计对象</caption>
			<tr>
				<td><input type="radio" id="type_inspection" name="params.type" checked="checked" value="inspection"/>
					<label for="type_inspection">检验&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
				<input type="radio" id="type_check" name="params.type" value="check"/>
					<label for="type_check">检查<!-- &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="params.type" value="result"/>不良扣分值</td> --></label>
				<td style="text-align:right">显示前&nbsp;<input type="text" size="5" value="10" style="width:40px;text-align: center;" name="params.pageSize" id="pageSize" readonly="readonly"/>&nbsp;项</td>
			</tr>
		</table>
		<table class="form-table-outside-border" style="width:100%;">
			<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;">统计分组</caption>
			<tr>
				<td style="padding:0px;margin:0px;padding-bottom:2px;">
					<ul id="groupUl">
						<li>
							<input type="radio" id="group_itemname" name="params.group" checked="checked" value="itemname" title="不良项目"/>
							<label for="group_itemname">不良项目</label>
						</li>
						<li>
							<input type="radio" id="group_workshop" name="params.group" value="workshop" title="车间" id="workshopRadio"/>
							<label for="group_workshop">车间</label>
						</li>
						<li>
							<input type="radio" id="group_productionLine" name="params.group" value="productionLine" title="生产线"/>
							<label for="group_productionLine">生产线</label>
						</li>
						<li>
							<input type="radio" id="group_section" name="params.group" value="section" title="工段"/>
							<label for="group_section">工段</label>
						</li>
						<li>
							<input type="radio" id="group_workProcedure" name="params.group" value="workProcedure" title="工序"/>
							<label for="group_workProcedure">工序</label>
						</li>
						<li>
							<input type="radio" id="group_workGroupType" name="params.group" value="workGroupType" title="班别"/>
							<label for="group_workGroupType">班别</label>
						</li>
						<li>
							<input type="radio" id="group_inspectionPoint" name="params.group" value="inspectionPoint" title="采集点"/>
							<label for="group_inspectionPoint">采集点</label>
						</li>
						<li>
							<input type="radio" id="group_modelSpecification" name="params.group" value="modelSpecification" title="产品型号"/>
							<label for="group_modelSpecification">产品型号</label>
						</li>
						<!-- <li>
							<input type="radio" name="params.group" value="itemdutyPart" title="零部件"/>零部件
						</li> -->
						<li>
							<input type="radio" id="group_itemdefectionTypeName" name="params.group" value="itemdefectionTypeName" title="不良类别"/>
							<label for="group_itemdefectionTypeName">不良类别</label>
						</li>
						<li>
							<input type="radio" id="group_itemdefectionCodeAttributeLevel" name="params.group" value="itemdefectionCodeAttributeLevel" title="严重度"/>
							<label for="group_itemdefectionCodeAttributeLevel">严重度</label>
						</li>
					</ul>
				</td>
			</tr>
			<tr>
				<td style="text-align:right;">
					<span>
					<button  class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
					<button  class='btn' type="button" onclick="reset();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
					<button  class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					<button  class='btn' type="button" onclick="sendChart();"><span><span><b class="btn-icons btn-icons-email"></b>发送邮件</span></span></button>
					<button  class='btn' onclick="toggleSearchTable(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>隐藏查询</span></span></button>&nbsp;
					</span>
				</td>
			</tr>
		</table>
	 </div>
	</form>
	<div>
		<table>
			<tr>
				<td width="50%" valign="top" id="detail_table_parent">
					<table id="detail_table"></table>
				</td>
				<td width="50%" valign="top">
					<div>
						<input type="radio" name="photo" checked="checked" value="plato" onclick="showMe(this);"/>柏拉图
						<input type="radio" name="photo" value="pie" onclick="showMe(this);">饼图
					</div>
					<div id="reportDiv_parent">
						<div id='reportDiv'></div>
					</div>
				</td>
			</tr>
		</table>
	</div>
</div>
