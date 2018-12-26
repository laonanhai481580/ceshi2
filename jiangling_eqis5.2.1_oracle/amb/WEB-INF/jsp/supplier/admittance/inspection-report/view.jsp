<%@page import="java.text.DecimalFormat"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.ambition.supplier.entity.InspectionGradeType"%>
<%@page import="com.norteksoft.task.base.enumeration.TaskProcessingResult"%>
<%@page import="com.ambition.supplier.entity.InspectionReport"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/js/FusionCharts.js"></script>	
	<script type="text/javascript">
		var isUsingComonLayout=false;
		$(function(){
// 			FusionCharts.setCurrentRenderer("Flash");
			$("#tabs").tabs({
			});
			$("#tabs-1 .opt-content").width($(window).width()-22);
			setTimeout(function(){
				$("#message").html("");
			},3000);
			createGrid();
			setTimeout(function(){
				$("#tabs-1 .opt-content").height($(window).height()-60);
			},1000);
		});
		function createGrid(){
			<%
				InspectionReport inspectionReport = (InspectionReport)ActionContext.getContext().getValueStack().findValue("inspectionReport");
				List<InspectionGradeType> inspectionGradeTypes = inspectionReport.getInspectionGradeTypes();
				JSONArray jsonArray = new JSONArray();
				DecimalFormat df1 = new DecimalFormat("0.0%");
				Double totalRate = 0.0;
				for(InspectionGradeType inspectionGradeType : inspectionGradeTypes){
					JSONObject json = new JSONObject();
					json.put("id",inspectionGradeType.getId());
					json.put("weight",inspectionGradeType.getWeight());
					json.put("name",inspectionGradeType.getName());
					json.put("totalFee",inspectionGradeType.getTotalFee());
					json.put("reviewers",inspectionGradeType.getReviewer());
					Double realFee = inspectionGradeType.getRealFee();
					json.put("realFee",realFee);
					if(realFee != null && inspectionGradeType.getTotalFee() != null && inspectionGradeType.getTotalFee()>0){
						Double rate = realFee/inspectionGradeType.getTotalFee();
						totalRate += rate * inspectionGradeType.getWeight()/100;
						json.put("rate", df1.format(rate));
					}
					jsonArray.add(json);
				}
				JSONObject json = new JSONObject();
				json.put("name","合计");
				json.put("weight",100.0);
				json.put("totalFee",100.0);
				json.put("realFee",df1.format(totalRate).split("%")[0]);
				json.put("rate",df1.format(totalRate));
				jsonArray.add(json);
				if(inspectionReport.getInspectionResult()==null){
					if(totalRate<0.70){
						inspectionReport.setInspectionResult(InspectionReport.RESULT_FAIL);
					}else{
						inspectionReport.setInspectionResult(InspectionReport.RESULT_PASS);
					}
				}
			%>
			jQuery("#inspectionGradeTypes").jqGrid({
				postData : {},
				data : <%=jsonArray.toString()%>,
				datatype: "local",
				height : 120,
				rownumbers:true,
			   	colModel:[
			   		{name:'id',index:'id', width:1,hidden:true,key:true},
			   		{label:'　',name:'name',index:'name', width:200},
			   		{label:'权重',name:'weight',index:'weight', width:80,formatter:function(val){if(val){return val + "%";}else{return "";}}},
			   		{label:'标准分数',name:'totalFee',index:'totalFee', width:80},
			   		{label:'实际分数',name:'realFee',index:'realFee', width:70},
			   		{label:'实际%',name:'rate',index:'rate', width:70},
			   		{label:'评审人员',name:'reviewers',index:'reviewers',width:160},
			   		{label:'查看',name:'id',width:50,formatter:function(val){
			   			if(val){
			   				return "<a href='#' title='查看雷达图' onclick='showGradeDetail("+val+")'>查看</a>";
			   			}else{
			   				return "";
			   			}
			   		}}
			   	],
			   	multiselect:false,
			    viewrecords: true,
				shrinkToFit : true,
				gridComplete : function(){
					
				}
			});
			jQuery("#inspectionGradeTypes").jqGrid("setGridWidth",$("#tabs").width()-20);
		}
		
	 	function changeViewSet(url,obj){
	 		var target = $(obj).attr("href");
	 		var loaded = $(target).attr("loaded");
	 		if(!loaded){
	 			$(obj).attr("loaded","loading");
	 			var id = $("#opt-content :input[name=id]").val();
	 			$(target).html("<div style='paddint-top:4px;'>数据加载中,请稍候... ...</div>")
	 				.height($(window).height()-40)
	 				.load(url,{id:id},function(result){
		 				$(target).attr("loaded",true);
		 			});
	 		}
		}
	 	function showGradeDetail(id){
	 		var obj = $("#tabs-"+id);
	 		if(obj.length>0){
	 			var index = obj.attr("index");
	 			obj = obj[0];
	 			$("#tabs").tabs("select",parseInt(index));
	 			changeViewSet('${supplierctx}/admittance/inspection-report/view-detail.htm?inspectionGradeTypeId='+id,obj);
	 		}
	 	}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var topMenu = '';
		var secMenu="admittance";
		var thirdMenu="_admittance_inspection";
	</script>
	<div class="opt-body">
		<div id="tabs">
			<ul>
				<li><a href="#tabs-1" onclick="changeViewSet('',this)">表单摘要</a></li>
				<%
					for(InspectionGradeType inspectionGradeType : inspectionGradeTypes){
						%>
				<li><a href="#tabs-<%=inspectionGradeType.getId() %>" onclick="changeViewSet('${supplierctx}/admittance/inspection-report/view-detail.htm?inspectionGradeTypeId=<%=inspectionGradeType.getId()%>',this)"><%=inspectionGradeType.getName() %></a></li>		
						<%
					}
				%>
			</ul>
			<div id="tabs-1" style="padding:0px;" loaded='true'>
				<%@ include file="view-form.jsp"%>
			</div>
			<%
					int indexx = 1;
					for(InspectionGradeType inspectionGradeType1 : inspectionGradeTypes){
						%>
				<div href="#tabs-<%=inspectionGradeType1.getId() %>" index=<%=indexx++ %> id="tabs-<%=inspectionGradeType1.getId() %>" style="padding:0px;overflow-y:auto;text-align:center;">
				</div>
			<%
					}
			%>
		</div>
	</div>
</body>
</html>