<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.ambition.util.common.DateUtil"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
	<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<style>
	<!--
		#searchUl{
			margin:0px;
			padding:0px;
		}
		.searchli{
			float:left;
			width:260px;
			height:24px;
			line-height:24px;
			list-style:none;
		}
		.searchli select{
			width:182px;
		}
		.input{
			width:178px;
		}
		.label{
			float:left;
			width:70px;
			text-align:right;
			padding-right:2px;
		}
	-->
	</style>
	<script type="text/javascript">
	function exportChart(){
		$.exportChart({
    		chart:chart,
    		grid:$("#detail_table"),
    		message:$("#message"),
    		width:$("#reportDiv").width(),
       		height:$("#reportDiv").height()
    	});
	}
	function contentResize(){
		if(cacheResult != null){
			clearTable();
			createReport(cacheResult);
			createDetailTable(cacheResult);
		}
	}
	var cacheResult = null;
	$(document).ready(function(){
		contentResize();
		$('#datepicker1').datepicker({"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
		$('#datepicker2').datepicker({"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
		search();
	});
	
	
	var chart = null;
	 //创建报告
	function createReport(result){
		if(chart != null){
			chart.destroy();
			chart = null;
		}
		var width = $("#btnDiv").width()-10;
		//var size = result.tableHeaderList.length;
		chart = new Highcharts.Chart({
			/**colors: ['#7cb5ec', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
			         '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'],*/
			exporting : {
				enabled : false
			},
			chart: {
				renderTo: "reportDiv",
				width : width,
				type : 'column'
			},
			credits: {
		         enabled: false
			},
			title: {
				style : {
					"color": '#3E576F',
					"font-size": '16px'
				},
				text: result.title
			},
			/**subtitle: {
				text: result.subtitle,
				y:33,
				x:0//center
			},*/
			xAxis: {
				categories: result.categories,
				gridLineDashStyle : 'ShortDashDot',
				gridLineWidth: 1,
				labels: { 
					formatter : function(){
						var str = (this.value + "").substr(5);
						return str.replace(/ /g,'<br/>');
					} 
	            }
			},
			yAxis: [{
				title: {
					text: ''
				},
		        gridLineWidth : 0,
				labels : {
					formatter : function(){
						if(this.value<0){
							return "";
						}else{
							return this.value;;
						}
					}
				}
			}],
			legend: {
				 //enabled: false,
				 layout : 'vertical',
		         align: 'right',
		         verticalAlign : 'middle',
		         backgroundColor: '#FFFFFF',
		         x : 6,
		         y : 0
		    },
			plotOptions: {
				column: {
	                stacking: 'normal',
	                borderWidth:0,
	                shadow : false
	            },
		        line : {
		            shadow : false,
		            color : '#8085e8',
		            dataLabels: {
		               enabled: true,
		               formatter : function(){
		            	   return this.y;
		               }
		            }
		        }
		    },
			tooltip: {
		   		 formatter: function() {
					if (this.series.type=="column") { 
						return '<b>'+ this.x +'</b><br/>'+
								//'零件: ' + this.point.partCode + '<br/>' + 
	                    		this.series.name +': '+ this.y +'<br/>'+
	                    		'合计: '+ this.point.stackTotal;
					}else{
						return "<b>" + this.series.name + ":</b>" + this.y;	
					}
				}
			},
			series:result.series
		});
	} 
	
	//确定的查询方法
	function search(){
		var date1 = $("#datepicker1").val();
		var date2 = $("#datepicker2").val();
		if( date2!=""&&date1!=""&&date1>date2){
			alert("检验日期前后有误，请重新设置！");
		}else{
			reportByParams(getParams());
		}
	}	
	//根据参数获取数据
	function reportByParams(params){
		$.post("${costctx}/statistical-analysis/cost-analysis-datas.htm",params,function(result){
			if(result.error){
				alert(result.message);
			}else{
				createReport(result);
				createDetailTable(result);
				cacheResult = result;
			}
		},'json');
	}
	function levelThreeChange(){
		var levelTwo=$("#search select[id=levelTwoName]").val();
		var url = "${costctx}/common/select-by-level-two.htm?levelTwo="+levelTwo;
		$.post(encodeURI(url),{},function(result){
 			if(result.error){
 				alert(result.message);
 			}else{
 				$(".label").each(function(index,obj2){
 					var labelName2 = obj2.innerHTML;
 					var leavlThrees = result.leavlThrees;
 					var leavlThreeArr = leavlThrees.split(",");
 					if("三级科目"==labelName2){
 						var leavlThree = obj2.nextElementSibling;
 						leavlThree.options.length=0;
 						var opp1 = new Option("","");
 						leavlThree.add(opp1);
 		 				for(var i=0;i<leavlThreeArr.length;i++){
 		 					var opp = new Option(leavlThreeArr[i],leavlThreeArr[i]);
 		 					leavlThree.add(opp);
 		 				}
 					}   
 				});
 			}
 		},'json');
	}

	//获取表单的值
	function getParams(){
		var params = {};
		$("#search input[type=text]").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});	
		$("#search select").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()&&jObj.val()!=""){
				params[obj.name] = jObj.val();
			}
		});
		$("#search input[type=radio]").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&obj.checked&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		return params;
	}
	//创建表格
	var detailTable = null;
	function createDetailTable(result){
		clearTable();
		var colModel=[{name:'custom_name',width:70,index:'custom_name',align:'center',label:'名称'}],datas = [];
		var tableHeaderList = result.tableHeaderList;
		for(var i=0;i<tableHeaderList.length;i++){
			colModel.push({name:'date'+i,index:'date' + i,width:90,align:'center',label : tableHeaderList[i]});
		}
		colModel.push({name:'total_',index:'total_',width:90,align:'center',label : '均值'});
		for(var i=0;i<result.series.length;i++){
			var serie = result.series[i];
			var p = {id:i+1,custom_name:serie.name,name:result.groupName};
			var total = 0;
			for(var j=0;j<serie.data.length;j++){
				var val=parseFloat(serie.data[j].y).toFixed(0);
				if(val>0){
					p['date' + j] = val;
					total += parseInt(val,10);
				}else{
					p['date' + j]=0;
				}
			}
			if(serie.data.length>0){
				p['total_'] = (total/serie.data.length).toFixed(2);
			}else{
				p['total_'] = 0;
			}
			datas.push(p);
		}
	
		var width = $("#btnDiv").width()-30;
		detailTable = $("#detail_table").jqGrid({
			datatype: "local",
			localReader: {
				id : 'custom_name'
			},
			data: datas,
			rownumbers: true,
			width: width,
			height: 180,
			colModel: colModel,
		    multiselect: false,
		   	autowidth: true,
			forceFit: true,
			shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete: function(){}
		});
		$("#detail_table").jqGrid("setGridWidth",width+10);
	}
	//清除表格
	function clearTable(){
		if(detailTable != null){
			detailTable.GridDestroy();
			detailTable = null;
		}
		$("#detailTableDiv_parent").html("<table id=\"detail_table\"></table>");
	}
	function clearAll(){
		$("#datepicker1").val("");
		$("#datepicker2").val("");
		document.myform.reset();
	}
	//选择部门(新方法)
	function departmentTree(obj){
		popZtree({
			leaf: {
				enable: false,
				multiLeafJson:"[{'name':'部门树','type':'DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"code\":\"code\"}','showValue':'{\"name\":\"name\"}'}]"
			},
			type: {
				treeType: "DEPARTMENT_TREE",
				showContent:"[{'id':'id','code':'code','name':'name'}]",
				noDeparmentUser:true,
				onlineVisible:true
			},
			data: {
				treeNodeData:"id,code,name",
				chkStyle:"",
				chkboxType:"{'Y':'ps','N':'ps'}",
				departmentShow:""
			},
			view: {
				title: "选择部门",
				width: 300,
				height:400,
				url:"${ctx}"
			},
			feedback:{
				enable: true,
	            showInput:"departmentName",
	            showThing:"{'name':'name'}",
	            hiddenInput:"departmentCode",
	            hiddenThing:"{'id':'id'}",
	            append:false
			},
			callback: {
				onClose:addValue
			}
		});
	}
	function addValue(api){
		var currentClickNodeData = api.single.getCurrentClickNodeData();
		var department = $.parseJSON(currentClickNodeData);
		$("#departmentCode").val(department.code);
		$("#departmentTo").val(department.name);
    }
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="statistical_analysis";
		var thirdMenu="_cost_analysis";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/cost-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/cost-statistical-analysis-menu.jsp" %>
	</div> 
	
	<div class="ui-layout-center" style="overflow:auto;">
		<%
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String startDateStr = sdf.format(calendar.getTime());

		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DATE, -1);
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
		endDateStr = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM-dd");
		calendar.set(Calendar.MONTH,0);
		startDateStr = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM-dd");
		%>
		<form onsubmit="return false;" name="myform">
		<div class="opt-body" style="overflow-y:auto;">
			<div class="opt-btn" id="btnDiv">
				<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
				<button class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				<button class='btn' type="button" onclick="clearAll();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
				<span style="margin-left:4px;color:red;" id="message"></span>
			</div>
			<div id="opt-content">
				<div id="search" style="display:block;"  >
					<table class="form-table-outside-border" style="width:100%;display:block;">
						<tr>
							<td>
								<ul id="searchUl">
									<li class='searchli'>
										<span class='label'>日期</span>
										<input id="datepicker1" isDate=true type="text" readonly="readonly" style="width:72px;" class="line" name="params.startDate_date" value="<%=startDateStr%>"/>
										至
										<input id="datepicker2" isDate=true type="text" readonly="readonly" style="width:72px;" class="line" name="params.endDate_date" value="<%=endDateStr%>"/>
									</li>
									<li class='searchli'>
										<span class='label'>事业部</span>
										 <s:select list="businessUnits" 
											  theme="simple"
											  listKey="value" 
											  listValue="name" 
											  name="params.businessUnitCode"
											  emptyOption="false"
											  labelSeparator=""
											  cssStyle="width:140px;">
										 </s:select>
										<%-- <input type="text" id="businessUnitCode" class="input" name="params.businessUnitCode" value="${businessUnitCode}"/> --%>
									</li>
									<li class='searchli'>
										<span class='label'>二级科目</span>
										 <s:select list="cost_topComposings" 
											  theme="simple"
											  listKey="value" 
											  listValue="name" 
											  id="levelTwoName"
											  name="params.levelTwoName"
											  emptyOption="true"
											  labelSeparator=""
											  onchange="levelThreeChange();"
											  cssStyle="width:140px;">
										 </s:select>										
										<%-- <input type="text" class="input" id="levelTwoName" name="params.levelTwoName" value="${levelTwoName}" /> --%>
									</li>
									<li class='searchli'>
										<span class='label'>三级科目</span>
										<s:select list="threeLevels" 
											  theme="simple"
											  listKey="value" 
											  listValue="name" 
											  id="leavlThree"
											  name="params.levelThreeName"
											  emptyOption="true"
											  cssStyle="width:140px;">
										 </s:select>
										<%-- <input type="text" class="input" id="levelThreeName" name="params.levelThreeName" value="${levelThreeName}" /> --%>
									</li>
								</ul>
							</td>
						</tr>		
					</table>
					<table class="form-table-outside-border" style="width:100%;">
							<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;padding-left: 10px;">统计分组</caption>
							<tr>
								<td style="padding:0px;margin:0px;padding-bottom:2px;">
									<input type="radio" name="params.group"  value="day" title="day"/>日
									<input type="radio" name="params.group" value="month" checked="checked" title="month" id="month"/>月
								</td>
							</tr>
					</table>
				</div>
				<div>
					<table style="width:100%;">
						<tr>
							<td id="reportDiv_parent">
								<div id='reportDiv'></div>
							</td>
						</tr>
						<tr>
							<td id="detailTableDiv_parent">
								<table id="detailTable"></table>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		</form>
	</div>
</body>
</html>