<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
	<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
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
	/*
	function createReport(result){
		if(chart != null){
			chart.destroy();
			chart = null;
		}
		var reportDiv = "reportDiv";
		var width = $("#" + reportDiv).width();
		chart = new Highcharts.Chart({
			exporting : {
				enabled : false
			},
			colors : ['#95B3D7','#B7DEE8','#89A54E'],
			chart: {//图表配置
				renderTo: reportDiv,
				spacingRight : 100 ,
				type: 'column'
			},
			credits: {//版权
		         enabled: false
			},
			title: {//标题
				style : {
					"font-weight":'bold',
					"color": '#3E576F',
					"font-size": '20px'
				},
				text: result.title
			},
			subtitle: {//副标题
				text: result.subtitle,
				y:33,
				x: 0 //center
			},
			xAxis: {//横
				categories: result.categories,
// 				crosshair: true
			},
			yAxis: [{//纵
				min:0,
				max: 1000,
// 				endOnTick: false,
				title: {
					text:"数量"
				}
			}],
			legend: {
// 		         align: 'right',
// 		         verticalAlign : 'top',
// 		         floating: true,
// 		         backgroundColor: '#FFFFFF',
// 		         x : 45,
// 		         y : 0
		    },
		    plotOptions: {//数据列配置
				column: {
		            dataLabels: {
	            		enabled: true,
		               	formatter : function(){
		            	    if(this.y<=0){
		            		    return '';
		            	    }else{
		            			return '<font style="color:black;">' + this.y + '</font>';
							}
						}      
		            },
		            lineWidth : 0,
		            shadow : true,
		            borderWidth:0,
		            pointPadding:0,
		    		cursor : 'pointer',
               		events : {
	            		 click : function(obj){
	            			showDetailByDate(obj.point.name,obj.point.id);
	            		} 
	            	}
		        },
		        series: {
	                allowPointSelect: true
	            }
		    },
			tooltip: { //提示框
				formatter: function() {
					var s;
					if (this.series.type=="column") { 
						s = 
						this.x+': '+ this.y;
					}else{
						return "<b>" + this.series.name + ":</b>" + this.y + "<br/><span style='font-size:12px;color:blue;'></span>";
					}
					return s;
				}
			},
			series: [{
				name: result.series1.name,
				data: result.series1.data
			} ,
			{
				name: result.series2.name,
				data: result.series2.data
			},{
				name: result.series3.name,
				data: result.series3.data,
			}]
		});
	}*/
	function createReport(result){
		$(function () {
		    chart = new Highcharts.Chart({
		        chart: {
		        	renderTo: reportDiv,
		            type: 'column'
		        },
		        credits: {//版权
			         enabled: false
				},
		        title: {
		        	style : {
						"font-weight":'bold',
						"color": '#3E576F',
						"font-size": '20px'
					},
					text: result.title
		        },
// 		        subtitle: {
// 		            text: '请点击按钮查看坐标轴变化'
// 		        },
		        xAxis: {
		        	categories: result.categories,
//	 				crosshair: true
		        },
		        yAxis: {
		        	tickPositioner: function () {
		                var positions = [],
		                    tick = Math.floor(this.dataMin),
		                    increment = Math.ceil((this.dataMax - this.dataMin) / 6);
		                for (tick; tick - increment <= this.dataMax; tick += increment) {
		                    positions.push(tick);
		                }
		                return positions;
		            },
		            title: {
		                text: '项目'
		            }
		        },
		        plotOptions: {//数据列配置
					column: {
			            dataLabels: {
		            		enabled: true,
			               	formatter : function(){
			            	    if(this.y<=0){
			            		    return '';
			            	    }else{
			            			return '<font style="color:black;">' + this.y + '</font>';
								}
							}      
			            },
			            lineWidth : 0,
			            shadow : true,
			            borderWidth:0,
			            pointPadding:0,
			    		cursor : 'pointer',
	               		events : {
		            		 click : function(obj){
		            			showDetailByDate(obj.point.name,obj.point.id);
		            		} 
		            	}
			        },
			        series: {
		                allowPointSelect: true
		            }
			    },
		        series: [{
					name: result.series1.name,
					data: result.series1.data
				} ,
				{
					name: result.series2.name,
					data: result.series2.data
				},{
					name: result.series3.name,
					data: result.series3.data,
				}],
		    });
		});

	}
	function showDetail(name,id){
		$.colorbox({href:'${iqcctx}/statistical-analysis/show-detail.htm?id='+id,iframe:true,
			innerWidth:$(window).width()<1100?$(window).width()-50:1100, 
			innerHeight:500,
			overlayClose:false,
			title:"详情"
		});
	}
	//确定的查询方法
	function search(){
		var date1 = $("#datepicker1").val();
		var date2 = $("#datepicker2").val();
		if(date1>date2){
			alert("检验日期前后有误，请重新设置！");
			return;
		}
		var checkBomCode=$("#checkBomCode").val();
		var checkItem=$("#checkItem").val();
// 		if(checkBomCode!=null&&checkItem!=null&&checkBomCode!=""&&checkItem!=""){
			reportByParams(getParams());
// 		}				
	}	
	
	//根据参数获取数据
	function reportByParams(params){
		$.post("${epmctx}/data-acquisition/size-spread-chart-datas.htm",params,function(result){
			if(result.error){
				alert(result.message);
			}else{
				createReport(result);
				createDetailTable(result);
				cacheResult = result;
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
		return params;
	}
	//创建表格
	var detailTable = null;
	function createDetailTable(result){
		clearTable();
		var colModel=[{name:'custom_name',width:70,index:'custom_name',align:'center',label:result.firstColName}],datas = [];
		var tableHeaderList = result.tableHeaderList;
		for(var i=0;i<tableHeaderList.length;i++){
			colModel.push({name:'date'+i,index:'date' + i,width:60,align:'center',label : tableHeaderList[i]});
		}
		var dataCheckList = result.series1.data;
		var checks1 = [];
		for(var i=0;i<dataCheckList.length;i++){
			checks1.push(dataCheckList[i].y);
		}
		var p = {id:1,custom_name:"最大值"};
		for(var i=0;i<dataCheckList.length;i++){
			p['date' + i] = checks1[i];
		}
		datas.push(p);
		
		var dataRegularList = result.series2.data;
		var checks2 = [];
		for(var i=0;i<dataRegularList.length;i++){
			checks2.push(dataRegularList[i].y);
		}
		p = {id:1,custom_name:"最小值"};
		for(var i=0;i<dataRegularList.length;i++){
			p['date' + i] = checks2[i];
		}
		datas.push(p);
		
		var dataAvgList = result.series3.data;
		var checks3 = [];
		for(var i=0;i<dataAvgList.length;i++){
			checks3.push(dataAvgList[i].y);
		}
		p = {id:1,custom_name:"平均值"};
		for(var i=0;i<dataAvgList.length;i++){
			p['date' + i] = checks3[i];
		}
		datas.push(p);		

/* 		var dataStandradList = result.series4.data;
		var checks4 = [];
		for(var i=0;i<dataStandradList.length;i++){
			checks4.push(dataStandradList[i].y);
		}
		p = {id:1,custom_name:"规格值"};
		for(var i=0;i<dataStandradList.length;i++){
			p['date' + i] = checks4[i];
		}
		datas.push(p); */
		
		var width = $("#btnDiv").width()-30;
		detailTable = $("#detail_table").jqGrid({
			datatype: "local",
			localReader: {
				id : 'custom_name'
			},
			data: datas,
			rownumbers: true,
			width: width,
			height: 80,
			colModel: colModel,
		    multiselect: false,
		   	autowidth: true,
			forceFit: true,
			shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete: function(){}
		});
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
 	function checkBomCodeClick(){
 		var url = '${iqcctx}/statistical-analysis/bom-code-select.htm';
 		$.colorbox({href:url,iframe:true, 
 			innerWidth:700, 
			innerHeight:500,
 			overlayClose:false,
 			title:"选择物料"
 		});
 	} 	
 	function setBomValue(datas){
 		$("#checkBomCode").val(datas[0].value);
 		var url = "${iqcctx}/statistical-analysis/size-select.htm?checkBomCode="+datas[0].value;
		$.post(encodeURI(url),{},function(result){
 			if(result.error){
 				alert(result.message);
 			}else{
				var checkItems = result.checkItems;
				var checkItemArr = checkItems.split(",");
				var checkItem = document.getElementById("checkItem");
				checkItem.options.length=0;
				var opp1 = new Option("","");
				checkItem.add(opp1);
 				for(var i=0;i<checkItemArr.length;i++){
 					var opp = new Option(checkItemArr[i],checkItemArr[i]);
 					checkItem.add(opp);
 				}
 			}
 		},'json');
 	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="analyse";
		var thirdMenu="_size_spread";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/epm-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/epm-analysis-third-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center" style="overflow:auto;">
		<%
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			String startDateStr = sdf.format(calendar.getTime());

			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.DATE, -1);
			String endDateStr = sdf.format(calendar.getTime());
		%>
		<form onsubmit="return false;" name="myform">
		<div class="opt-body">
		<div class="opt-btn">
			<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
			<button class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
			<button class='btn' type="button" onclick="clearAll();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
			<span style="margin-left:4px;color:red;" id="message"></span>
		</div>	
		<div id="search" style="display:block;"  >
			<table class="form-table-outside-border" style="width:100%;display:block;">
				<tr>
					<td style="text-align:right;width:70px;">检验日期</td>
					<td>
						<input id="datepicker1" type="text" name="params.startDate" readonly="readonly" style="width:72px" class="line" value="<%=startDateStr%>"/>至
						<input id="datepicker2" type="text" name="params.endDate" readonly="readonly" style="width:72px" class="line" value="<%=endDateStr%>"/>
					</td>
				</tr>		
			</table>
		</div>
			<div id="search" style="display:block;"  >
			<table class="form-table-outside-border" style="width:100%;display:block;">
				<tr>
					<td>
						<input searchinput="true" type="radio" id="groupType1" name="groupType" checked="checked" value="1" style="margin-left:20px;"/>
						<label for="groupType1">周</label>
						<input searchinput="true" type="radio" id="groupType2" name="groupType"  value="1" style="margin-left:20px;"/>
						<label for="groupType2">月</label>
						<input searchinput="true" type="radio" id="groupType3" name="groupType"  value="1" style="margin-left:20px;"/>
						<label for="groupType3">年</label>
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
		</form>
	</div>
</body>
</html>