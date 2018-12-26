<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
	<%@include file="/common/meta.jsp" %>
	<%
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH,0);
		calendar.set(Calendar.DATE,1);
		String startDateStr = sdf.format(calendar.getTime());
		calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH,12);
		calendar.set(Calendar.DATE,1);
		calendar.add(Calendar.DATE,-1);
		String endDateStr = sdf.format(calendar.getTime());
	%>
	<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
	<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
	<script type="text/javascript">
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
		$('#datepicker1').datepicker({
			"dateFormat":'yy-mm',
		     changeMonth:true,
		     changeYear:true
		});
		$('#datepicker2').datepicker({
			"dateFormat":'yy-mm',
		     changeMonth:true,
		     changeYear:true
		});
		search();
	});
	var chart = null;
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
			chart: {
				renderTo: reportDiv,
				spacingRight : 63 
			},
			credits: {
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
			subtitle: {
				text: result.subtitle,
				y:33,
				x: 0 //center
			},
			xAxis: {
				categories: result.categories,
				gridLineDashStyle : 'ShortDashDot',
				gridLineWidth: 1,
				labels : {
					style : {
						"color": 'black'
					}
				}
			},
			yAxis: [{
				title: {
					text: result.yAxisTitle1,
					  stackLabels: {
				            enabled: true,
				            style: {
				               fontWeight: 'bold',
				               color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
				            }
				         },
					style : {
			               'font-weight':'bold',
			               fontSize:15,
			               color : 'black'
						},
					rotation : 0
				},
				plotLines: [{
					value: 12,
					color: 'black'
				}],
				labels : {
					align : 'right'
				},
				maxPadding : 0,
				gridLineWidth : 1,
				gridLineDashStyle : 'ShortDashDot',
				min: 0
			},{
				title: {
					text: result.yAxisTitle2,
					style: {
			               color: '#000000',
			               'font-weight':'bold',
			               fontSize:18
			            },
					x : 52,
					rotation : 0
				},
				plotLines: [{
					width: 1,
					color: 'black'
				}],
			        gridLineWidth : 0,
			        tickInterval : 10,
				labels : {
					style: {
						color : 'black'
		            },
					align : 'right',
					x : width - 45,
					y : 1,
					formatter : function(){
						if(result.max==0){
							return '';
						}else if(this.value == result.max){
							return '100%';
						}else if(this.value < result.max){
							return ((this.value / result.max *  100).toFixed(0)) + '%';
						}else{
							return '';
						}
					}
				},
				min: 0
				
			}],
			legend: {
		         align: 'right',
		         verticalAlign : 'top',
		         floating: true,
		         backgroundColor: '#FFFFFF',
		         x : 45,
		         y : 0
		    },
		    plotOptions: {
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
		            shadow : false,
		            borderWidth:0,
		            pointPadding:0,
		            groupPadding:0.0,
		       cursor : 'pointer',
               events : {
	            	click : function(obj){
		            	showDetailByDate(obj.point.name,obj.point.startdate,obj.point.enddate);
	            	}
	            }
		        },
		        spline: {
		            lineWidth : 1,
		            shadow : false,
		            dataLabels: {
		               enabled: true,
		               formatter : function(){
		            	   return ((this.y / result.max *  100).toFixed(2)) + '%';
		               }
		            }
		        }
		    },
			tooltip: {
				formatter: function() {
					var s;
					if (this.series.type=="spline") { 
						s = "<s:text name='第'/>"+this.x+"<s:text name='月'/>"+': '+ (this.y).toFixed(2)+'%';
					}else{
						return "<b>" + this.series.name + ":</b>" + this.y + "<br/><span style='font-size:12px;color:blue;'>"+"<s:text name='查看详情'/>"+"</span>";
					}
					return s;
				}
			},
			series: [{
				type: 'column',
				name: result.series1.name,
				data: result.series1.data
			} ,
			{
				type: 'column',
				name: result.series2.name,
				data: result.series2.data
			},{
				type: 'spline',
				name: result.series3.name,
				data: result.series3.data,
				yAxis : 1
			}]
		});
	}
		
	//确定的查询方法
	function search(){
		var date1 = $("#datepicker1").val(); 
		var date2 = $("#datepicker2").val();
		if(date1>date2){
			alert("<s:text name='统计年月前后有误，请重新设置！'/>");
		}else{
			reportByParams(getParams());
		}
	}	
	
	//根据参数获取数据
	function reportByParams(params){
		$.post("${gsmctx}/inspectionMsa-chart/check-chart-datas.htm",params,function(result){
			if(result.error){
				alert(result.message);
			}else{
				createDetailTable(result);
				createReport(result);
				cacheResult = result;
			}
		},'json');
	}
	
	//获取表单的值
	function getParams(){
		var params = {};
		$("#search :input").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				if(obj.type=="radio"){
					if(obj.checked){
						params[obj.name] = jObj.val();
						if(obj.name=='params.group'&&jObj.attr("title")){
							params['params.groupName'] = jObj.attr("title");
						}
					}
				}else{
					params[obj.name] = jObj.val();
				}
			}
		});
		return params;
	}
	
	//创建表格
	var detailTable = null;
	function createDetailTable(result){
		clearTable();
		var colModel=[{name:'custom_name',width:70,index:'custom_name',align:'center',label:"<s:text name='月份'/>"}],datas = [];
		var tableHeaderList = result.tableHeaderList;
		for(var i=0;i<tableHeaderList.length;i++){
			colModel.push({name:'date'+i,index:'date' + i,width:45,align:'center',label : tableHeaderList[i]});
		}
		var dataCheckList = result.series1.data;
		var checks1 = [];
		for(var i=0;i<dataCheckList.length;i++){
			checks1.push(dataCheckList[i].y);
		}
		var p = {id:1,custom_name:"<s:text name='应送校验数'/>"};
		for(var i=0;i<dataCheckList.length;i++){
			p['date' + i] = checks1[i];
		}
		datas.push(p);
		
		var dataRegularList = result.series2.data;
		var checks2 = [];
		for(var i=0;i<dataRegularList.length;i++){
			checks2.push(dataRegularList[i].y);
		}
		p = {id:1,custom_name:"<s:text name='实际送校数'/>"};
		for(var i=0;i<dataRegularList.length;i++){
			p['date' + i] = checks2[i];
		}
		datas.push(p);
		
		var dataRateList = result.series3.data;
		p = {id:1,custom_name:"<s:text name='完成率'/>"};
		for(var i=0;i<dataRateList.length;i++){
			p['date' + i] = parseFloat(dataRateList[i]).toFixed(2)+"%";
		}
		datas.push(p);
	
		var width = $("#btnDiv").width()-30;
		detailTable = $("#detail_table").jqGrid({
			datatype: "local",
			localReader : {
				id : 'custom_name'
			},
			data: datas,
			rownumbers : true,
			width : width,
			height: 85,
			colModel:colModel,
		    multiselect: false,
		   	autowidth: true,
			forceFit : true,
			shrinkToFit: true,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete : function(){
				
			}
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
	
	//查看明细的方法
	function showDetailByDate(name,StartDate,EndDate){
		var params = getParams();
		params['params.name'] = name;
		params['params.StartDate'] = StartDate;
		params['params.EndDate'] = EndDate;
		var url = '${gsmctx}/inspectionMsa-chart/check-chart-detail.htm?1=1';
		for(var pro in params){
			url += "&" + pro + "=" + params[pro];
		}
		$.colorbox({href:encodeURI(url),iframe:true, 
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<700?$(window).height()-50:600,
 			overlayClose:false,
 			title:"<s:text name='统计年月前后有误，请重新设置！'/>"
 		});
	}
	function exportChart(){
		$.exportChart({
    		chart:chart,
    		grid:$("#detail_table"),
    		message:$("#message")
    	});
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="chartReport";
		var thirdMenu="inspectionMsaplanReport";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-chart-report-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<form onsubmit="return false;">
			<div class="opt-body" style="overflow-y:auto;">
				<div class="opt-btn"></div>
				<div id="search" style="display:block;padding:4px;">
					<table class="form-table-outside-border" style="width:100%" >
						<tr>
							<td style="text-align:right;width:70px;"><s:text name="统计年月"/></td>
							<td>
								<input id="datepicker1" type="text" readonly="readonly" style="width:72px;" class="line" name="params.startDate_ge_date" value="<%=startDateStr%>"/>&nbsp;<s:text name="至"/>
					    		<input id="datepicker2" type="text" readonly="readonly" style="width:72px;" class="line" name="params.endDate_le_date" value="<%=endDateStr%>"/>
							</td>
							<td style="text-align:right;">
								<button class='btn' onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b><s:text name="统计"/></span></span></button>
								<button class="btn" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b><s:text name="导出"/></span></span></button>
								<button class='btn' onclick="reset();"><span><span><b class="btn-icons btn-icons-redo"></b><s:text name="重置"/></span></span></button>&nbsp;
							</td>
						</tr>
					</table>
				</div>	
				<div>
					<table style="width:99%;">
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