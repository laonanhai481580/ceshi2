<%@page import="java.io.File"%>
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
	<script src="${ctx}/widgets/validation/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctx}/widgets/validation/jquery.metadata.js" type="text/javascript"></script>
	<script src="${ctx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/workflowEditor/swfobject.js"></script>
	<script type="text/javascript">
		var isUsingComonLayout=false;
		var code = '${task.code}', active = '${task.active}';
		$(function(){
			$("#tabs").tabs({
			});
			$("#tabs-1 .opt-content").width($(window).width()-5);
			setTimeout(function(){
				$("#message").html("");
			},3000);
			if(code=='grade'){
				loadItemsByUser();
			}else{
				createGrid();
				setTimeout(function(){
					$("#tabs-1 .opt-content").height($(window).height()-90);
				},1000);
			}
		});
		function loadItemsByUser(isRefresh){
			var current = 6;
			var dd = setInterval(function(){
				current --;
				var str = '';
				if(current<0){
					for(var i=0;i<-(current%2);i++){
						str += " ...";
					}
				}else{
					str = current;
				}
				$("#item-table").find("td").html("评分项目加载中,请稍候... " + str);
			}, 1000);
			$("#item-table").find("td").html("评分项目加载中,请稍候... ");
			$("button.btn").attr("disabled",true);
			var url = "${supplierctx}/admittance/inspection-report/inspection-grades.htm?id=${id}";
			if(isRefresh){
				url += "&isRefresh=true";
			}
			$("#item-table").parent().load(url,{},function(){
				clearInterval(dd);
				$("button.btn").attr("disabled","");
				if(active == '0'){//可编辑
					$(":input[type=radio]","#item-table").each(function(index,obj){
	 					$(obj).bind("click",function(){
	 						caculateRealFees();
	 					});
	 				});
					$("#inspectionReportForm").validate({});
				}else{
					$(":input[type=radio]","#item-table").attr("disabled","disabled");
				}
				$("#tabs-1 .opt-content").height($(window).height()-90);
				//计算总分和总项
				var items = {};
				$(":input[type=radio]","#item-table").each(function(index,obj){
 					if(obj.name&&!isNaN(obj.title)&&!isNaN(parseFloat(obj.title))){
 						if(!items[obj.name]){
 							items[obj.name] = parseFloat(obj.title);
 						}else{
 							if(parseFloat(obj.title)>items[obj.name]){
 								items[obj.name] = parseFloat(obj.title);
 							}
 						}
					}
 				});
				var total=0,totalFee = 0;
				for(var pro in items){
					total++;
					totalFee += items[pro];
				}
				window.totalFee = totalFee;
				$("#totalInfo").html("(总分为<b>"+totalFee+"</b>分,共<b>"+total+"</b>项)");
				caculateRealFees();
			});
		}
		function createGrid(){
			<%
				InspectionReport inspectionReport = (InspectionReport)ActionContext.getContext().getValueStack().findValue("inspectionReport");
				List<InspectionGradeType> inspectionGradeTypes = inspectionReport.getInspectionGradeTypes();
				JSONArray jsonArray = new JSONArray();
				DecimalFormat df = new DecimalFormat("0.0%");
				Double totalRate = 0.0;
				int greenTotal = 0,total=0;
				for(InspectionGradeType inspectionGradeType : inspectionGradeTypes){
					JSONObject json = new JSONObject();
					json.put("id",inspectionGradeType.getId());
					json.put("weight",inspectionGradeType.getWeight());
					json.put("name",inspectionGradeType.getName());
					json.put("totalFee",inspectionGradeType.getTotalFee());
					int t = inspectionGradeType.getTotalFee()==null?0:inspectionGradeType.getTotalFee().intValue()/10;
					json.put("total",t);
					total+=t;
					json.put("reviewers",inspectionGradeType.getReviewer());
					Double realFee = inspectionGradeType.getRealFee();
					json.put("realFee",realFee);
					json.put("greenTotal",realFee==null?0:realFee.intValue()/10);
					greenTotal+= realFee==null?0:realFee.intValue()/10;
					if(realFee != null && inspectionGradeType.getTotalFee() != null && inspectionGradeType.getTotalFee()>0&&inspectionGradeType.getWeight()!=null){
						Double rate = realFee/inspectionGradeType.getTotalFee();
						totalRate += rate * inspectionGradeType.getWeight()/100;
						json.put("rate", df.format(rate));
					}
					jsonArray.add(json);
				}
				JSONObject json = new JSONObject();
				json.put("name","合计");
				json.put("weight",100.0);
				json.put("total",total);
				json.put("totalFee",100.0);
				json.put("realFee",df.format(totalRate).split("%")[0]);
				json.put("greenTotal",greenTotal);
				json.put("rate",df.format(totalRate));
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
				height : 200,
				rownumbers:true,
			   	colModel:[
			   		{name:'id',index:'id', width:1,hidden:true,key:true},
			   		{label:'　',name:'name',index:'name', width:200},
			   		//{label:'权重',name:'weight',index:'weight', width:80,formatter:function(val){if(val){return val + "%";}else{return "";}}},
			   		{label:'评价项目数量(项)',name:'total',index:'total', width:80},
			   		//{label:'实际分数',name:'realFee',index:'realFee', width:70},
			   		{label:'绿灯(项)',name:'greenTotal',index:'greenTotal', width:70},
			   		//{label:'实际%',name:'rate',index:'rate', width:70},
			   		{label:'评审人员',name:'reviewers',index:'reviewers',width:160},
			   		{label:'查看',name:'id',width:50,formatter:function(val){
			   			if(val){
			   				return "<a href='#' title='查看详情' onclick='showGradeDetail("+val+")'>查看</a>";
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
		
		function loadItems(obj){
			$(obj).parent().html("考察评分表").css("border-bottom","0px");
			var current = 6;
			var dd = setInterval(function(){
				current --;
				var str = '';
				if(current<0){
					for(var i=0;i<-(current%2);i++){
						str += " ...";
					}
				}else{
					str = current;
				}
				$("#item-table").find("td").html("评分项目加载中,请稍候... " + str);
			}, 1000);
			$("#item-table").find("td").html("评分项目加载中,请稍候... ");
			$("button.btn").attr("disabled",true);
			$("#item-table").show().load("${supplierctx}/admittance/inspection-report/inspection-grades.htm?id=${id}",{},function(){
				clearInterval(dd);
				$("button.btn").attr("disabled","");
// 				$(":input[type=radio]","#item-table").each(function(index,obj){
// 					$(obj).bind("click",function(){
// 						caculateRealFees();
// 					});
// 				});
				$(":input[type=radio]","#item-table").attr("disabled","disabled");
				$("#tabs-1 .opt-content").height($(window).height()-90);
			});
		}
		
		function caculateRealFees(){
			var val = 0;
			var redTotal =0,greenTotal=0,yellowTotal=0;
			$(":input[type=radio]:checked","#item-table").each(function(index,obj){
				var fee = 0;
				if(obj.title != '' && !isNaN(obj.title)){
					fee = parseInt(obj.title);
				}
				val += fee;
				var max = $(obj).closest("tr").find(":input[maxWeight]").val();
				var min = $(obj).closest("tr").find(":input[minWeight]").val();
				if(fee==max){
					greenTotal++;
				}else if(fee==min){
					redTotal++;
				}else{
					yellowTotal++;
				}
			});
			$("#" + "realFee").val(val);
			//var totalFee = window.totalFee?window.totalFee:0.0;
			//var greenTotal = val/10;
			//var redTotal = (totalFee-val)/10;
			$("#greenTotal").val(greenTotal);
			$("#redTotal").val(redTotal);
			$("#yellowTotal").val(yellowTotal);
		}
		
	 	function completeTask(taskTransact,operateName) {
			$('#taskTransact').val(taskTransact);
			$("#operateName").val(operateName);
			var paramsStr = "";
			$("#paramsStr").val("[" + paramsStr + "]");
			$("button.btn").attr("disabled", "disabled");
			var current = 8;
			var dd = setInterval(function(){
				current--;
				var str = '';
				if(current<0){
					for(var i=0;i<-(current%2);i++){
						str += " ...";
					}
				}else{
					str = current;
				}
				$("#message").html("正在保存,请稍候... ... " + str);
			}, 1000);
			$('#inspectionReportForm').submit();
		}
	 	
	 	function completeGrade(){
			if($("#inspectionReportForm").valid()){
				var paramsStr = "";
				$("tr[id]","#item-table").each(function(index,obj){
					if(paramsStr){
						paramsStr += ",";
					}
					var $radio = $(obj).find(":checked");
					var $remark = $(obj).find(":input[name=remark]");
					var select = $radio.val();
					var remark = $remark.val();
					paramsStr += "{\"id\":\"" + obj.id + "\",\"selectId\":\"" + (select?select:"") + "\",\"remark\":\""+(remark?remark:"")+"\"}";
				});
				$("tr[id]","#item-table").find(":input").attr("name","");
				$("#paramsStr").val("[" + paramsStr + "]");
				$("button.btn").attr("disabled", "disabled");
				var current = 8;
				var dd = setInterval(function(){
					current--;
					var str = '';
					if(current<0){
						for(var i=0;i<-(current%2);i++){
							str += " ...";
						}
					}else{
						str = current;
					}
					$("#message").html("正在保存,请稍候... ... " + str);
				}, 1000);
				$('#inspectionReportForm').submit();
			}
	 	}
	 	function retriveTask() {
			$(".opt_btn").find("button.btn").attr("disabled", "disabled");
			aa.submit("inspectionReportForm", "", 'main', showMsg);
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
	 			changeViewSet('${supplierctx}/admittance/inspection-report/grade-detail.htm?inspectionGradeTypeId='+id,obj);
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
				<li><a href="#tabs-0" onclick="changeViewSet('',this)">表单信息</a></li>
				<s:if test="task.code != 'grade'">
				<%
					for(InspectionGradeType inspectionGradeType : inspectionGradeTypes){
						%>
				<li><a href="#tabs-<%=inspectionGradeType.getId() %>" onclick="changeViewSet('${supplierctx}/admittance/inspection-report/grade-detail.htm?inspectionGradeTypeId=<%=inspectionGradeType.getId()%>',this)"><%=inspectionGradeType.getName() %></a></li>		
						<%
					}
				%>
				</s:if>
				<li><a href="#tabs-2" onclick="changeViewSet('${supplierctx}/admittance/inspection-report/history.htm?taskId=${task.id}',this)">流程进度</a></li>
			</ul>
			<div id="tabs-0" style="padding:0px;" loaded='true'>
				<s:if test="task.code=='grade'">
					<%@ include file="process-form-grade.jsp"%>
				</s:if>
				<s:else>
					<%@ include file="process-form.jsp"%>
				</s:else>
			</div>
			<%
					int index = 1;
					for(InspectionGradeType inspectionGradeType : inspectionGradeTypes){
						%>
				<div href="#tabs-<%=inspectionGradeType.getId() %>" index=<%=index++ %> id="tabs-<%=inspectionGradeType.getId() %>" style="padding:0px;overflow-y:auto;text-align:center;">
				</div>
			<%
					}
			%>
			<div id="tabs-2" style="padding:0px;overflow-y:auto;text-align:center;">
			</div>
		</div>
	</div>
</body>
</html>