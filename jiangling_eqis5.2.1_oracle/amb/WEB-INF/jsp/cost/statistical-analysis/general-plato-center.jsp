<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="com.norteksoft.product.util.PropUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	boolean isFetchAll = true;
	try{
		isFetchAll = Boolean.valueOf(com.norteksoft.product.util.PropUtils.getProp("isFetchAll"));
	}catch(Exception e){}
%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();
	int currentYear = calendar.get(Calendar.YEAR);
	int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
	calendar.set(Calendar.DAY_OF_MONTH,1);
	String startDateStr = sdf.format(calendar.getTime());
	calendar.add(Calendar.MONTH,1);
	calendar.add(Calendar.DATE,-1);
	String endDateStr = sdf.format(calendar.getTime());
	
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
	//选择品名
	var materialNameId = null;//选择品名
	function SelectMaterialName(obj){
			materialNameId = obj.currentInputId;
			$.colorbox({href:"${mfgctx}/common/product-bom-select.htm",
				iframe:true, 
				innerWidth:750, 
				innerHeight:500,	
				overlayClose:false,	
				title:"选择品名"
			});
		}
	//选择之后的方法 data格式{key:'a',value:'a'}
	function setDefectionValue(data){
		$(selectInputObj).val(data[0].value).focus();
	}
	
	//选择之后的方法 data格式{key:'a',value:'a'}
	function setFullBomValue(data){
		var d = data[0];
		$('#materialName').val(d.name); 
		$('#materialCode').val(d.code);
		$('#materialModel').val(d.model);
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
	 	<%-- <input type="hidden" name="params.inspectionPointType_in" value="<%=request.getAttribute("inspectionPointType")==null?"":request.getAttribute("inspectionPointType")%>"></input> --%>
		<table class="form-table-outside-border" style="width:100%;display:block;" id="searchTable">
			<tr>
				<td style="padding-left:6px;padding-bottom:4px;">
					<ul id="searchUl">
						<li style="display: none;">
				 			<span class="label">车间</span>
				 			<input class="input" type="text"  name="params.workshop" value="${workshop}" id="itemdirectionCodeName"  readonly="readonly"/>
				 		</li>
				 		<li>
				 			<span class="label"><input type="radio" name="scope" value="date" checked="checked"></input>日期</span>
				 			<input id="datepicker1" type="text" readonly="readonly" style="width:72px;" class="line" name="startDate_ge_date" value="<%=startDateStr%>"/>
				 		        至<input id="datepicker2"type="text" readonly="readonly" style="width:72px;" class="line" name="endDate_le_date" value="<%=endDateStr%>"/>
				 		</li>
				 		
				 		<li>
				 			<span class="label"><input type="radio" name="scope" value="month"></input>月</span>
				 			<input id="datepicker3" type="text" readonly="readonly" style="width:72px;" class="line" name="yearAndMonth_ge_int" value="<%=startDateStr1 %>"/>
				 		        至<input id="datepicker4"type="text" readonly="readonly" style="width:72px;" class="line" name="yearAndMonth_le_int" value="<%=endDateStr1 %>"/>
				 		</li>
						 <c:choose>
							<c:when test="${workshop=='注塑车间'}">
								<li>
						 			<span>料号</span>
						 			<input class="input" type="text" name="params.materialCode_like"/>
						 		</li>
						 		<li>
						 			<span class="label">班组</span>
						 			<s:select list="workGroups" 
										  theme="simple"
										  listKey="name" 
										  listValue="name" 
										  name="params.workGroup_equals"
										  labelSeparator=""
									  emptyOption="true"></s:select>
						 		</li>
								<li>
									<span class="label">成型员</span>
									<input class="input" type="text" name="moldingMan" id="moldingMan" value="${moldingMan}"  readonly="readonly" onclick="selectObj('选择检验人员','moldingMan','moldingMan')"  class="{required:true,messages:{required:'检验员不能为空'}}"></input>
								</li>
								<li>
									<span class="label">机台号</span>
						 			<s:select list="machineNos" 
										  theme="simple"
										  listKey="name" 
										  listValue="name" 
										  name="params.machineNo_equals"
										  labelSeparator=""
										  emptyOption="true"></s:select>
								</li>
							</c:when>
						 	<c:when test="${workshop=='机加车间'}">
								<li>
						 			<span>料号</span>
						 			<input id="materialCode" class="input" type="text" onclick="SelectMaterialName(this)" name="params.materialCode_like"/>
						 		</li>
						 		<li>
									<span class="label">部门</span>
									<input name="params.section_equals" id="section" value="${section}"  readonly="readonly" onclick="selectDept('选择部门','section','section')"  class="{required:true,messages:{required:'部门不能为空'}}"></input>
								</li>
						 		<li>
						 			<span class="label">班组</span>
						 			<s:select list="workGroups" 
										  theme="simple"
										  listKey="name" 
										  listValue="name" 
										  name="params.workGroup_equals"
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
							 </c:when> 
							 <c:when test="${workshop=='抛光车间'}">
								<li>
						 			<span>料号</span>
						 			<input id="materialCode"  class="input" type="text" onclick="SelectMaterialName(this)" name="params.materialCode_like"/>
						 		</li>
						 		<li>
									<span class="label">抛光人员</span>
									<input name="params.polishingMan_equals" id="polishingMan" value="${polishingMan}"  readonly="readonly" onclick="selectObj('选择抛光人员','polishingMan','polishingMan')"  class="{required:true,messages:{required:'抛光人员不能为空'}}"></input>
								</li>
							</c:when>
							<c:when test="${workshop=='电镀车间'}">
								<li>
						 			<span>品名</span>
						 			<input id="materialName"  class="input" type="text" onclick="SelectMaterialName(this)" name="params.materialName_like"/>
						 		</li>
								<li>
						 			<span>料号</span>
						 			<input id="materialCode"  class="input" type="text" onclick="SelectMaterialName(this)" name="params.materialCode_like"/>
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
						 			<span class="label">班组</span>
						 			<s:select list="workGroups" 
										  theme="simple"
										  listKey="name" 
										  listValue="name" 
										  name="params.workGroup_equals"
										  labelSeparator=""
										  emptyOption="true"></s:select>
						 		</li>
						 		<li>
						 			<span class="label">表面处理</span>
						 			<s:select list="surfaceTreatments" 
										  theme="simple"
										  listKey="name" 
										  listValue="name" 
										  name="params.surfaceTreatment_equals"
										  labelSeparator=""
										  emptyOption="true"></s:select>
						 		</li>
						 		<li>
						 			<span class="label">成型方式</span>
						 			<s:select list="formingWays" 
										  theme="simple"
										  listKey="name" 
										  listValue="name" 
										  name="params.formingWay_equals"
										  labelSeparator=""
										  emptyOption="true"></s:select>
						 		</li>
							</c:when>
							<c:when test="${workshop=='PVD车间'}">
								<li>
						 			<span>品名</span>
						 			<input id="materialName"  class="input" type="text" onclick="SelectMaterialName(this)" name="params.materialName_like"/>
						 		</li>
								<li>
						 			<span>料号</span>
						 			<input id="materialCode"  class="input" type="text" onclick="SelectMaterialName(this)" name="params.materialCode_like"/>
						 		</li>
						 		<li>
						 			<span>规格型号</span>
						 			<input id="materialModel"  class="input" type="text" onclick="SelectMaterialName(this)" name="params.materialModel_like"/>
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
						 			<span class="label">班组</span>
						 			<s:select list="workGroups" 
										  theme="simple"
										  listKey="name" 
										  listValue="name" 
										  name="params.workGroup_equals"
										  labelSeparator=""
										  emptyOption="true"></s:select>
						 		</li>
						 		<li>
						 			<span class="label">成型方式</span>
						 			<s:select list="formingWays" 
										  theme="simple"
										  listKey="name" 
										  listValue="name" 
										  name="params.formingWay_equals"
										  labelSeparator=""
										  emptyOption="true"></s:select>
						 		</li>
							</c:when>
							<c:when test="${workshop=='组立'}">
								<li>
						 			<span>客户</span>
						 			<input class="input" type="text" name="params.customerName_like"/>
						 		</li>
						 		<li>
						 			<span>订单号</span>
						 			<input class="input" type="text" name="params.orderNo_like"/>
						 		</li>
						 		<li>
						 			<span>路达品名</span>
						 			<input class="input" type="text" name="params.lodaMaterialName_like"/>
						 		</li>
						 		<li>
						 			<span>客户品名</span>
						 			<input class="input" type="text" name="params.materialName_like"/>
						 		</li>
						 		<li>
						 			<span>成品料号</span>
						 			<input class="input" type="text" name="params.materialCode_like"/>
						 		</li>
						 		<li>
									<span class="label">部门</span>
									<input name="params.section_equals" id="section" value="${section}"  readonly="readonly" onclick="selectDept('选择部门','section','section')"  class="{required:true,messages:{required:'部门不能为空'}}"></input>
								</li>
						 		<li>
						 			<span class="label">班组</span>
						 			<s:select list="workGroups" 
										  theme="simple"
										  listKey="name" 
										  listValue="name" 
										  name="params.workGroup_equals"
										  labelSeparator=""
										  emptyOption="true"></s:select>
						 		</li>
							</c:when>
						 	<c:otherwise>
							 	<li>
						 			<span class="label">车间</span>
						 			<s:select list="workshops" 
										  theme="simple"
										  listKey="name" 
										  listValue="name" 
										  name="params.workshop_equals"
										  labelSeparator=""
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
						 			<span class="label">班组</span>
						 			<s:select list="workGroups" 
										  theme="simple"
										  listKey="name" 
										  listValue="name" 
										  name="params.workGroup_equals"
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
							</c:otherwise> 
						</c:choose>
				 	 </ul>
				</td>
			</tr>
		</table>
		<table class="form-table-outside-border" style="width:100%;">
			<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;">统计类型</caption>
			<tr>
				<c:choose>
				<c:when test="${workshop=='注塑车间'||workshop=='机加车间'}">
					<td>
						<input type="radio" name="params.type" checked="checked" value="inspectionQualifiedAmount" onclick="groupTargetClick('unqualifiedAmount')"/>检验不合格数量 
					</td>
				</c:when>
				<c:when test="${workshop=='抛光车间'||workshop=='电镀车间'||workshop=='PVD车间'||workshop=='组立'}">
					<td>
						<input type="radio" name="params.type" checked="checked" value="checkQualifiedAmount" onclick="groupTargetClick('unqualifiedAmount')"/>检查不良数量
					</td>
				</c:when>
				<c:otherwise>
					<td>
						<input type="radio" name="params.type" checked="checked" value="inspectionQualifiedAmount" onclick="groupTargetClick('unqualifiedAmount')"/>检验不合格数量
					</td>
					<td>
						<input type="radio" name="params.type" value="checkQualifiedAmount" onclick="groupTargetClick('unqualifiedAmount')"/>检查不良数量&nbsp;&nbsp; 
					</td>
				</c:otherwise>
			</c:choose>
				<td style="text-align:right">显示前&nbsp;<input type="text" size="5" value="10" style="width:40px;text-align: center;" name="params.pageSize" id="pageSize" readonly="readonly"/>&nbsp;项</td>
			</tr>
		</table>
		<table class="form-table-outside-border" style="width:100%;">
			<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;">统计分组</caption>
			<tr>
				<td style="padding:0px;margin:0px;padding-bottom:2px;">
					<ul id="groupUl">
						<li>
							<input type="radio" name="params.group" value="itemname"  checked="checked" title="不良项目"/>不良项目
						</li>
						<c:choose>
							<c:when test="${workshop=='注塑车间'}">
								<li>
									<input type="radio" name="params.group" value="produceTaskCode" title="生产任务单"/>生产任务单
								</li>
								<li>
									<input type="radio" name="params.group" value="workGroup" title="班组"/>班组
								</li>
								<li>
									<input type="radio" name="params.group" value="materialCode" title="料号"/>料号
								</li>
								<li>
									<input type="radio" name="params.group" value="itemdefectionTypeName" title="不良类型"/>不良类型
								</li>
								<li>
									<input type="radio" name="params.group" value="moldingMan" title="成型员"/>成型员
								</li>
								<li>
									<input type="radio" name="params.group" value="machineNo" title="机台号"/>机台号
								</li>
							</c:when>
							<c:when test="${workshop=='机加车间'}">
								<li>
									<input type="radio" name="params.group" value="materialName" title="品名"/>品名
								</li>
								<li>
									<input type="radio" name="params.group" value="section" title="部门"/>部门
								</li>
								<li>
									<input type="radio" name="params.group" value="workGroup" title="班组"/>班组
								</li>
								<li>
									<input type="radio" name="params.group" value="workGroupType" title="班别"/>班别
								</li>
							</c:when>
							<c:when test="${workshop=='抛光车间'}">
								<li>
									<input type="radio" name="params.group" value="materialName" title="品名"/>品名
								</li>
								<li>
									<input type="radio" name="params.group" value="workGroup" title="班组"/>班组
								</li>
								<li>
									<input type="radio" name="params.group" value="polishingMan" title="抛光人员"/>抛光人员
								</li>
							</c:when>
							<c:when test="${workshop=='电镀车间'}">
								<li>
									<input type="radio" name="params.group" value="materialName" title="品名"/>品名
								</li>
								<li>
									<input type="radio" name="params.group" value="platingLine" title="电镀线"/>电镀线
								</li>
								<li>
									<input type="radio" name="params.group" value="workGroup" title="班组"/>班组
								</li>
								<li>
									<input type="radio" name="params.group" value="surfaceTreatment" title="表面处理"/>表面处理
								</li>
								<li>
									<input type="radio" name="params.group" value="formingWay" title="成型方式"/>成型方式
								</li>
							</c:when>
							<c:when test="${workshop=='PVD车间'}">
								<li>
									<input type="radio" name="params.group" value="materialName" title="品名"/>品名
								</li>
								<li>
									<input type="radio" name="params.group" value="productionLine" title="生产线"/>生产线
								</li>
								<li>
									<input type="radio" name="params.group" value="workGroup" title="班组"/>班组
								</li>
								<li>
									<input type="radio" name="params.group" value="formingWay" title="成型方式"/>成型方式
								</li>
							</c:when>
							<c:when test="${workshop=='组立'}">
								<li>
									<input type="radio" name="params.group" value="materialName"  title="客户品名"/>客户品名
								</li>
								<li>
									<input type="radio" name="params.group" value="section" title="部门"/>部门
								</li>
								<li>
									<input type="radio" name="params.group" value="workGroup" title="班组"/>班组
								</li>
							</c:when>
							<c:otherwise>
								<li>
									<input type="radio" name="params.group" value="workshop" title="车间"/>车间
								</li>
								<li>
									<input type="radio" name="params.group" value="productionLine" title="生产线"/>生产线
								</li>
								<li>
									<input type="radio" name="params.group" value="workGroup" title="班组"/>班组
								</li>
								<li>
									<input type="radio" name="params.group" value="workGroupType" title="班别"/>班别
								</li>
							</c:otherwise>
						</c:choose>
					</ul>
				</td>
			</tr>
			<tr>
				<td style="text-align:right;">
					<span>
					<security:authorize ifAnyGranted="manu-analyse-general-plato-count">
						<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
						</security:authorize>
						<security:authorize ifAnyGranted="manu-analyse-general-plato-export">
						<button class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
						</security:authorize>
						<button class='btn' type="button" onclick="reset();scopeClick();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
						<button class='btn' onclick="toggleSearchTable(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>隐藏查询</span></span></button> 
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
