<%@page import="com.ambition.util.common.DateUtil"%>
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
	<script src="${ctx}/widgets/multiselect/jquery.multiselect.zh-cn.js" type="text/javascript"></script>
	<script type="text/javascript">
     	var chart = null;
		$(document).ready(function(){
			$("#datepicker1").datepicker({"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
			$("#datepicker2").datepicker({"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
			totalReport();
		});
		function totalReport(){
			var startTime = $("#datepicker1").val();
			var endTime =  $("#datepicker2").val();
			if(startTime>endTime){
				alert("检验时间选择有误，请重新设置！");
				$("#datepicker1").focus();
			}else{
			var params = getParams();
			$("#container").html("数据加载中,请稍候... ...");
			var url = "${costctx}/statistical-analysis/cost-total-table.htm?a=1";
			for(var pro in params){
				url += "&" + pro + "=" + params[pro];
			}
			$("#container").load(encodeURI(url),function(){
				contentResize();
			});
			/**$.post(url,params,function(result){
	 	      if(result.error){
					alert(result.message);
				}else{
					$("#opt-content").load(url,{startDate:startDate,endDate:endDate});
				}
			},'json');*/
// 			$("#opt-content").load(url,{startDate:startDate,endDate:endDate});
		
		}
		}
		//导出汇总表
		function exportChart(){
			$.exportChart({
				chart:chart,
				grid:$("#container"),
				message:$("#message"),
				width:$("#opt-content").width(),
				height:$("#container").height()
			});
		}
		//发送邮件
		function sendChart(){
			$.sendChart({
				chart:chart,
				grid:$("#container"),
				message:$("#message"),
				width:$("#container").width(),
				height:$("#container").height()
			});
		}
		
		//获取表单的值
		function getParams(){
			var params = {};
			$("#searchBtnDiv :input[name]").each(function(index,obj){
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
						params['params.businessUnitName'] = names;
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
		function changePosition(){}
		
		//重置查询条件的方法
		function formReset() {
			$("#businessUnitCode").multiselect("uncheckAll");
		}
		function resetStata(){
			if(confirm("确定要重新计算吗?")){
				$("#message").html("正在重新计算,请稍候... ...");
				$("button").attr("disabled","disabled");
				$.post("${costctx}/statistical-analysis/update-cost-loss-count.htm",{},function(result){
					$("button").removeAttr("disabled");
					$("#message").html("");
					if(result.error){
						alert(result.message);
					}else{
						$("#updateMessage").html("上次计算时间:" + result.updateDate);
						totalReport();
					}
				},'json');
			}
		}
		
		function contentResize(){
			var top = $("#contentTable").position().top + $("#container").scrollTop();
			var width = $("#contentTable").width()+1;
// 			$("#blankDiv").width(width).css("top",top-10);
			$("#headerTable").width(width).css("top",top);
			var firstWidthMap = {},secWidthMap = {};
			$("#contentTable thead").find("tr").each(function(index,obj){
				if(index==0){
					$(obj).find("td").each(function(index,tdObj){
						var colspan = $(tdObj).attr("colspan");
						if(!colspan||colspan=='1'){
							firstWidthMap[index] = $(tdObj).width();						
						}
					});
				}else if(index==1){
					$(obj).find("td").each(function(index,tdObj){
						var colspan = $(tdObj).attr("colspan");
						if(!colspan||colspan=='1'){
							secWidthMap[index] = $(tdObj).width();						
						}
					});
				}
			});
			$("#headerTable thead").find("tr").each(function(index,obj){
				if(index==0){
					$(obj).find("td").each(function(index,tdObj){
						var colspan = $(tdObj).attr("colspan");
						if(!colspan||colspan=='1'){
							$(tdObj).width(firstWidthMap[index]);
						}
					});
				}else if(index==1){
					$(obj).find("td").each(function(index,tdObj){
						var colspan = $(tdObj).attr("colspan");
						if(!colspan||colspan=='1'){
							$(tdObj).width(secWidthMap[index]);
						}
					});
				}
			});
		}
		function showDetail(name,type){
			var params = getParams();
			if(type=='1'){
				params['params.levelTwoName']=name;
			}else{
				params['params.name']=name;
			}
			
			var url = "${costctx}/loadinfo/show-detail.htm?a=1";
			for(var pro in params){
				url += "&" + pro + "=" + params[pro];
			}
			$.colorbox({href:encodeURI(url),iframe:true,
				innerWidth:$(window).width()<1100?$(window).width()-50:1100, 
				innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"表单"
			});
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
	var secMenu="statistical_analysis";
	var thirdMenu="_composing_cost_total";
	</script>
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/cost-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/cost-statistical-analysis-menu.jsp" %>
	</div> 
    <div class="ui-layout-center">
		<div class="opt-body" style="overflow-y:auto;">
		<%
			Calendar calendar = Calendar.getInstance();
			String endDateStr = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM");
			calendar.set(Calendar.MONTH,0);
			String startDateStr = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM");
		%>
			<div  id="searchBtnDiv">
				<div id="opt-content" style="text-align: center;">
				    <form>
				<table class="form-table-border-left" style="width:100%;margin-right:4px;margin-top:4px;margin-bottom:4px;border:0px;">
				     <tr>
				        <td ><span class="label">统计月份</span></td>
				        <td><input id="datepicker1" type="text" readonly="readonly" style="width:72px;" class="line" name="params.startDate" value="<%=startDateStr%>"/>
					                至<input id="datepicker2" type="text" readonly="readonly" style="width:72px;" class="line" name="params.endDate" value="<%=endDateStr%>"/></td>
					     <td>事业部</td>
					     <td>
					        <s:select 
						      list="businessUnits" 
							  theme="simple"
							  listKey="value" 
							  listValue="name" 
							  name="params.businessUnitCode"
							  emptyOption="true"
							  labelSeparator=""
							  cssStyle="width:140px;">
							</s:select>
					     </td>
				       <td>
					       <button class='btn' type="button" style="float:right;" onclick="javascript:this.form.reset();formReset();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
					        <button class='btn' type="button" style="float:right;" onclick="totalReport();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
				       </td>
				     </tr>
				</table>
				</form>
				     <div id='content'>
						<div id="containerDiv_parent" style="margin-top:20px;">
								<div id="container" ></div>
						</div>
					</div>
				</div>
		 	</div>	
		 	
		</div>	
	</div>
</body>
<%-- <script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script> --%>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
</html>
























