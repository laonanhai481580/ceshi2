<%@page import="java.text.SimpleDateFormat"%>
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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>	
<link href="${ctx}/widgets/multiselect/jquery.multiselect.css" rel="stylesheet" type="text/css"/>
<script src="${ctx}/widgets/multiselect/jquery.multiselect.js" type="text/javascript"></script>
<script src="${ctx}/widgets/multiselect/jquery.multiselect.zh-cn.js" type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<%-- <script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script> --%>

<script type="text/javascript">
	$(document).ready(function(){
		$("#datepicker1").datepicker({changeMonth:true,changeYear:true});
		$("#datepicker2").datepicker({changeMonth:true,changeYear:true});
		//$("#_qualityFeature").multiselect({selectedList:2});
	});

	function search(){
		var url = '${spcctx }/statistics-analysis/cpk-appraisal-table.htm?nowtime=' + (new Date()).getTime();
		url += "&startDateStr=" + $("#datepicker1").val();
		url += "&endDateStr=" + $("#datepicker2").val();
		var values = $("#_qualityFeature").multiselect("getChecked").map(function(){
			   return this.value;	
			}).get();
		if(values.toString()){
			$("#message").html("");
			url += "&qualityFeatureIds=" + values.toString();
			$.showMessage("正在统计,请稍候... ...","custom");
			$("#cpkAppraisalTable").load(url,function(){
				$.clearMessage();
			});
		}else{
			$("#message").html("请选择质量特性");
		}
	}
	//选择质量特性
	function selectFeature(obj){
		$.colorbox({/* href:"${spcctx}/common/feature-bom-select.htm", */
			href:"${spcctx}/common/feature-bom-multi-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
			overlayClose:false,
			title:"选择质量特性"
		});
	}
	
	function setFeatureValue(datas){
		/* $("#featureName").val(datas[0].value);
		$("#_qualityFeature").val(datas[0].id); */
		var ids="",values="";
		for ( var data in datas) {
			if(datas.length > 1){
				values="当前已选择"+(parseInt(data)+1)+"项质量特性!";
			}else{
				values += datas[data].value;
			}
			if(data == (datas.length-1)){
				ids += datas[data].id;
				//values += datas[data].value;
				break;
			}
			ids += datas[data].id+",";
			//values += datas[data].value+",";
		}
		$("#featureName").val(values);
		$("#_qualityFeature").val(ids);
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="stat_analyse";
		var thirdMenu="_cpk_table";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/spc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/spc-statistics-analysis-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center" style="overflow: auto;">
		<div class="opt-body" style="overflow: auto;">
			<aa:zone name = "main">
				<form id="contentForm" name="contentForm" method="post" action="">
					<div class="opt-btn">
						<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
						<div id="message" style="color:red;"><s:actionmessage theme="mytheme" /></div>	
					</div>
					<div id="customerSearchDiv" style="display:block;padding:4px;">						
						<table class="form-table-outside-border"  style="width:100%;padding:4px;">
							<tr>
								<td style="width:70px;text-align:right;">日&nbsp;&nbsp;期</td>
								<td style="width:220px;">
									<input id="datepicker1" type="text" readonly="readonly" style="width:80px;border: none;" name="params.startDate_ge_date" value="<%=startDateStr%>"/>&nbsp;至&nbsp;<input id="datepicker2" type="text" readonly="readonly" style="width:80px;border: none;" name="params.endDate_le_date" value="<%=endDateStr%>"/>
								</td>
								<td>
									质量特性
									<%-- <s:select id="_qualityFeature" list="qualityFeatures"
										  theme="simple"
										  listKey="value" 
										  listValue="name" 
										  cssClass="targerSelect"
										  name="qualityFeatures">
									</s:select> --%>
									<input style="border: none;" name="featureName" id="featureName" value="${qualityFeatures[0].name}" onclick="selectFeature(this);" readonly="readonly"/>
									<input name="qualityFeatures" id="_qualityFeature" value="${qualityFeatures[0].value}" class="targerSelect" type="hidden"/>
								</td>
								<!-- <td style="text-align:right;">
									<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
								</td> -->
							</tr>
						</table>	
					</div>
				</form>
			</aa:zone>
			<div id="cpkAppraisalTable" style="overflow: auto;">
			<%@ include file="cpk-appraisal-table.jsp" %>
			</div>
		</div>
	</div>
</body>
</html>