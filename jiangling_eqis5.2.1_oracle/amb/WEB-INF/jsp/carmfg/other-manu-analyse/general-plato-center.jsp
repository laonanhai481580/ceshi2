<%@ page import="com.norteksoft.product.util.PropUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
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
//-->
</script>
<div class="opt-body" style="overflow-y:auto;">
	 <form onsubmit="return false;">
	 <div class="opt-btn" id="btnDiv">
	 <span style="margin-left:4px;color:red;" id="message"></span>
	 </div>
	 <div style="padding:4px;" id="customerSearchDiv">
	 	<input type="hidden" name="params.inspectionPointType_in" value="<%=request.getAttribute("inspectionPointType")==null?"":request.getAttribute("inspectionPointType")%>"></input>
		<table class="form-table-outside-border" style="width:100%;display:block;" id="searchTable">
			<tr>
				<td style="padding-left:6px;padding-bottom:4px;">
					<ul id="searchUl">
				 		<li>
				 			<span class="label">检验日期</span>
				 			<input id="datepicker1" type="text" readonly="readonly" style="width:72px;" class="line" name="params.startDate_ge_date"/>
				 		        至<input id="datepicker2"type="text" readonly="readonly" style="width:72px;" class="line" name="params.endDate_le_datetime"/>
				 		</li>
				 		<li>
				 			<span class="label">工厂</span>
				 			<s:select list="factorys" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="params.factory_equals"
								  labelSeparator=""
								  emptyOption="true"></s:select>
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
				 			<span class="label">采集点</span>
				 			<s:select list="inspectionPoints" 
								  theme="simple"
								  listKey="inspectionPointName" 
								  listValue="inspectionPointName" 
								  name="params.inspectionPoint_equals"
								  label="采集点"
								  labelSeparator=""
								  emptyOption="true"></s:select>
				 		</li>
				 		<!-- <li>
				 			<span class="label">计划号</span>
				 			<input class="input" type="text"  name="params.planNo_equals"/>
				 		</li> -->
				 		<li>
				 			<span class="label">产品类型</span>
				 			<s:select list="productModels" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="params.productModel_equals"
								  labelSeparator=""
								  emptyOption="true"></s:select>
				 		</li>
				 		<li>
				 			<span class="label">产品型号</span>
				 			<s:select list="modelSpecifications" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="params.modelSpecification_equals"
								  labelSeparator=""
								  emptyOption="true"></s:select>
				 		</li>
				 		<li>
				 			<span class="label">批次</span>
				 			<input class="input" type="text"  name="params.batchNo_equals"/>
				 		</li>
				 		<%-- <%
				 			if(isFetchAll){
				 		%>
				 		<li>
				 			<span class="label">方位</span>
				 			<input class="input" type="text"  name="params.itemdirectionCodeName_equals" value="" id="itemdirectionCodeName"  readonly="readonly" onclick="selectDirectionCode(this)"/>
				 		</li>
				 		<li>
				 			<span class="label">部位</span>
				 			<input class="input" type="text" name="params.itempositionCodeName_equals" value="" id="itempositionCodeName"  readonly="readonly" onclick="selectComponentCode(this)"/>
				 		</li>
				 		<%} %>
				 		<li>
				 			<span class="label">零部件</span>
				 			<input class="input" type="text" name="params.itemdutyPart_equals" value="" id="itemdutyPart" readonly="readonly" onclick="selectBomValue(this)"/>
				 		</li>
				 		<li>
				 			<span class="label">不良类别</span>
				 			<s:select list="defectionTypes" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="params.itemdefectionTypeName_equals"
								  labelSeparator=""
								  emptyOption="true"></s:select>
				 		</li> --%>
				 		<li>
				 			<span class="label">不良</span>
				 			<input class="input" type="text" name="params.itemname_equals" onclick="selectDefectionCode(this);"/>
				 		</li>
				 		<%-- <li>
				 			<span class="label">严重度</span>
				 			<s:select list="unqualifiedGrades" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="params.itemdefectionCodeAttributeLevel_equals"
								  labelSeparator=""
								  emptyOption="true"></s:select>
				 		</li> --%>
				 	 </ul>
				</td>
			</tr>
		</table>
		<table class="form-table-outside-border" style="width:100%;">
			<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;">统计对象</caption>
			<tr>
				<td><input type="radio" name="params.type" checked="checked" value="inspection"/>检验&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="params.type" value="check"/>检查&nbsp;&nbsp;&nbsp;&nbsp;<!-- <input type="radio" name="params.type" value="result"/>不良扣分值 --></td>
				<td style="text-align:right">显示前&nbsp;<input type="text" size="5" value="10" style="width:40px;text-align: center;" name="params.pageSize" id="pageSize" readonly="readonly"/>&nbsp;项</td>
			</tr>
		</table>
		<table class="form-table-outside-border" style="width:100%;">
			<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;">统计分组</caption>
			<tr>
				<td style="padding:0px;margin:0px;padding-bottom:2px;">
					<ul id="groupUl">
						<li>
							<input type="radio" name="params.group" checked="checked" value="itemname" title="不良项目"/>不良项目
						</li>
						<li>
							<input type="radio" name="params.group" value="factory" title="工厂" id="factoryRadio"/>工厂
						</li>
						<li>
							<input type="radio" name="params.group" value="workshop" title="车间" id="workshopRadio"/>车间
						</li>
						<li>
							<input type="radio" name="params.group" value="productionLine" title="生产线"/>生产线
						</li>
						<li>
							<input type="radio" name="params.group" value="workGroupType" title="班别"/>班别
						</li>
						<li>
							<input type="radio" name="params.group" value="workProcedure" title="工序"/>工序
						</li>
						<li>
							<input type="radio" name="params.group" value="inspectionPoint" title="采集点"/>采集点
						</li>
						<!-- <li>
							<input type="radio" name="params.group" value="planNo" title="计划号"/>计划号
						</li> -->
						<li>
							<input type="radio" name="params.group" value="productModel" title="产品类型"/>产品类型
						</li>
						<li>
							<input type="radio" name="params.group" value="modelSpecification" title="产品型号"/>产品型号
						</li>
						<li>
							<input type="radio" name="params.group" value="batchNo" title="批次"/>批次
						</li>
						<%-- <li>
							<input type="radio" name="params.group" value="产品阶段" disabled="disabled" title="产品阶段"/>产品阶段
						</li>
						<li>
							<input type="radio" name="params.group" value="不合格类别" disabled="disabled" title="不合格类别"/>不合格类别
						</li>
						<%
				 			if(isFetchAll){
				 		%>
				 		<li>
							<input type="radio" name="params.group" value="itemdirectionCodeName" title="方位"/>方位
						</li>
						<li>
							<input type="radio" name="params.group" value="itempositionCodeName" title="部位"/>部位
						</li>
				 		<%
				 			}
				 		%>
						<li>
							<input type="radio" name="params.group" value="itemdutyPart" title="零部件"/>零部件
						</li>
						<li>
							<input type="radio" name="params.group" value="不合格来源" disabled="disabled" title="不合格来源"/>不合格来源
						</li>
						<li>
							<input type="radio" name="params.group" value="itemdefectionTypeName" title="不良类别"/>不良类别
						</li>
						<li>
							<input type="radio" name="params.group" value="itemliabilityDepartment" title="责任单位"/>责任单位
						</li>
						<li>
							<input type="radio" name="params.group" value="itemdefectionCodeAttributeLevel" title="严重度"/>严重度
						</li> --%>
					</ul>
				</td>
			</tr>
			<tr>
				<td style="text-align:right;">
					<span>
					<button  class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
					<button  class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					<button  class='btn' type="button" onclick="reset();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
					<button  class='btn' onclick="toggleSearchTable(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>隐藏查询</span></span></button> 
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
