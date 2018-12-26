<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.ambition.supplier.utils.DateUtil"%>
<%@page import="com.ambition.util.common.CommonUtil1"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
    <link href="${ctx}/widgets/multiselect/jquery.multiselect.css" rel="stylesheet" type="text/css"/>
	<script src="${ctx}/widgets/multiselect/jquery.multiselect.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script src="${ctx}/widgets/multiselect/jquery.multiselect.zh-cn.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<%
		Calendar calendar = Calendar.getInstance();
		String endDateStr = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM-dd");
		calendar.set(Calendar.MONTH,0);
		String startDateStr = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM-dd");
		//事业部
// 	    List<BusinessUnit>  businessUnits = CommonUtil.getCurrentBusinessUnits();
// 	    ActionContext.getContext().put("businessUnits", businessUnits);
	%>
	<script type="text/javascript">
     var chart = null;
	$(document).ready(function(){
		$("#datepicker1").datepicker({"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
		$("#datepicker2").datepicker({"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
		$("#businessUnitCode").multiselect({selectedList:1});
		$("#businessUnitCode").multiselect("uncheckAll");
		totalReport();
	});
	//重置查询条件的方法
	function formReset() {
		$("#businessUnitCode").multiselect("uncheckAll");
	}
	function totalReport(){
		var startTime = $("#datepicker1").val();
		var endTime =  $("#datepicker2").val();
		if(startTime>endTime){
			alert("检验时间选择有误，请重新设置！");
			$("#datepicker1").focus();
		}else{
		var params = getParams();
		$("#opt-content").html("数据加载中,请稍候... ...");
		var url = "${iqcctx}/inspection-report/statistical-finish-report-rate-datas.htm?a=1";
		for(var pro in params){
			url += "&" + pro + "=" + params[pro];
		}
		$("#opt-content").load(encodeURI(url));
		}
	}
	//获取表单的值
	function getParams(){
		var params = {};
		$(".form-table-outside-border :input[name]").each(function(index,obj){
			if(obj.name.indexOf("multiselect_")==0){
				return;
			}
			var jObj = $(obj);
			var value = jObj.val();
			if(obj.type=='radio'){
				if(obj.checked&&value){
					params[obj.name] = value;
				}
			}else if(obj.type == 'select-one'){
				var multiselect = jObj.attr("multiselect");
				//多选框
				if(multiselect == 'true'){
					value = "";
					var names = '';
					$(":input[name=multiselect_"+obj.id+"]:checked").each(function(index,obj){
						if(value){
							value += ",";
							names += ",";
						}
						names += $(obj).attr("title");
						value += obj.value;
					});
					params['params.businessUnitCode'] = value;
				}
				if(value){
					params[obj.name] = value;
				}
			}else if(value){
				params[obj.name] = value;
			}
		});
		return params;
	}
	function showAllMateriel(){
		var url="${iqcctx}/inspection-report/materiel-node-load.htm";
		$.colorbox({href:encodeURI(url),iframe:true, 
			innerWidth:$(window).width()<350?$(window).width()-50:350, 
			innerHeight:$(window).height()<400?$(window).height()-50:400,
 			overlayClose:false,
 			title:"物料级别"
 		});
	}
	
	function showCheckedMateriel(materielName,checkedId,information){
		var html='<span style="text-align: left;">物料级别信息:'+information+"</span>";
		var materielNameStr=materielName.substring(0,materielName.length-1);
		var checkedIdStr=checkedId.substring(0,checkedId.length-1);
		$("#materialLevels").val(materielNameStr);
		$("#materialLevels").next().val(checkedIdStr);
		$("#materialTypeInformation").html(information);
	}
	function checkBomClick(obj){
		$.colorbox({href:"${mfgctx}/common/product-bom-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择物料BOM"
 		});
 	}
	function setFullBomValue(datas){
		var his = $(":input[name=checkBomCode]").val();
		$(":input[name=params\.checkBomCode]").val(datas[0].code);
		$(":input[name=checkBomName]").val(datas[0].name);
// 		$(":input[name=checkBomName]").closest("td").find("span").html(datas[0].name);
// 		$(":input[name=checkBomModel]").val(datas[0].model);
// 		$(":input[name=checkBomModel]").closest("td").find("span").html(datas[0].model);
// 		if(datas[0].code != his){
// 			loadCheckItems();
// 		}
 	}
	</script>
</head>
<body>
	<script type="text/javascript">
		var secMenu="Chart";
		var thirdMenu="_finish_report";
	</script>
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	 <div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-chart-report-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
	<div class="opt-body">
	<div class="opt-btn">
 		<form onsubmit="return false;">
 		<table id="searchTable" style="margin:0px;border:0px;width:100%;" cellpadding="0" cellspacing="0" class="form-table-outside-border">
<!--  		</table> -->
<!--  		<table  id="searchTable"  class="form-table-outside-border" style="width:100%;"> -->
			<tr>
				<td>
					<span class="label">到货日期统计</span>
					<input id="datepicker1" type="text" readonly="readonly" style="width:72px;" class="line" name="params.startDate" value="<%=startDateStr%>"/>
					 至<input id="datepicker2" type="text" readonly="readonly" style="width:72px;" class="line" name="params.endDate" value="<%=endDateStr%>"/>
					<%-- <span style="margin-left:4px;">统计归属</span>
		 			<s:select list="businessUnits" 
					  theme="simple"
					  listKey="businessUnitCode" 
					  listValue="businessUnitName" 
					  name="params.businessUnitCode"
					  id="businessUnitCode"
					  multiselect="true"
					  ></s:select> --%>
					  <span style="margin-left:2px;">检验时间差:</span>
					  <select name="params.dutyDate">
					  	<%for(int i=1;i<10;i++){ %>
					  	<option value="<%=i%>" <%if(i==1) {%>selected="selected"<%} %>><%=i%></option>
					  	<%} %>
					  </select>
					  天
				</td>
				<td>
					<span style="float:left;">物料名称</span>
					<input type="hidden" name="params.checkBomCode" id="checkBomCode" />
					<input style="width:100px;float:left;" name="checkBomName" id="checkBomName" />
					<a class="small-button-bg" style="margin-left:2px;float:left" onclick="checkBomClick(this)" href="javascript:void(0);" title="选择产品型号"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
<!-- 					<input id=materialLevels name="materialLevels" onclick="showAllMateriel();"/> -->
<!-- 					<input type="hidden" id="params.checkBomMaterialLevels" name="params.checkBomMaterialLevels"/> -->
				</td>
				<td style="text-align: right;">
					<security:authorize ifAnyGranted="COST_STATISTICAL-ANALYSIS_COST-TOTAL_STATA">
						<button class='btn'  onclick="totalReport();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
					</security:authorize>
					<button class='btn' type="button" onclick="javascript:this.form.reset();formReset();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
				</td>
     		</tr>
		</table>
		</form>
		<div style="text-align: left;" id="materialTypeInformation" >
		</div>
		<div id="opt-content" style="text-align: center;"></div>
<%-- 			<jsp:include page="statistical-finish-report-rate-center.jsp" /> --%>
		</div>	
 	</div>
 	</div>
 	</div>
</body>
</html>