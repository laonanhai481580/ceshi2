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
	function createReport(result){
		if(chart != null){
			chart.destroy();
			chart = null;
		}
		var reportDiv = "reportDiv";
		var width = $("#reportDiv").width();
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
				text: result.title,
				margin:20
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
					rotation: 45,
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
				labels : {
					format: '{value}',
					align : 'left'
				},
				maxPadding : 0,
				gridLineWidth : 1,
				gridLineDashStyle : 'ShortDashDot',
				max:result.amount
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
		        opposite:true,
				labels : {			
					format: '{value}%',
				},
				
					max:100
				
			}],
			legend: {
				 layout : 'vertical',
		         align: 'right',
		         verticalAlign : 'middle',
		         align: 'right',
		         floating: true,
		         backgroundColor: '#FFFFFF',
		         x : 6,
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
               		events : {
	            		 click : function(obj){
	            			 showDetail(obj.point.name,obj.point.xvalue);
	            		} 
	            	}
		        },
		        series: {
		        	pointPadding:0.2
		        	},
		        spline: {
		            lineWidth : 1,
		            shadow : true,
		            dataLabels: {
		               enabled: true,
		               formatter : function(){
		            	   return this.y ;
		               }
		            },
		        }
		    },
			tooltip: {
				formatter: function() {
					var s;
					if (this.series.type=="spline") { 
						s = 
						this.x+': '+ this.y;
					}else{
						return "<b>" + this.series.name + ":</b>" + this.y + "<br/><span style='font-size:12px;color:blue;'></span>";
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
				data: result.series2.data,				
			},{
				type: 'spline',
				name: result.series3.name,
				data: result.series3.data,
				yAxis: 1
			}]
		});
	}
	function showDetail(name,xvalue){
		var startDate=$("#datepicker1").val();
		var endDate=$("#datepicker2").val();
		var businessUnitName=$("#businessUnitName").val();
		var factory=$("#factory").val();
		var station=$("#station").val();
		var classGroup=$("#classGroup").val();
		var auditType=$("#auditType").val();
		var department=$("#department").val();
		var auditMan=$("#auditMan").val();
		var group="";
		$("#search input[type=radio]").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&obj.checked&&jObj.val()){
				group = jObj.val();
			}
		});
		
		var str=startDate+","+endDate+","+businessUnitName+","+factory+","+station+","+classGroup+","+auditType+","+department+","+auditMan+","+name+","+group+","+xvalue;
		var url = '${mfgctx}/ipqc/ipqc-audit/observe-rate-show-detail.htm?searchParams='+str+'&a=1';
		$.colorbox({href:encodeURI(url),iframe:true,
			innerWidth:$(window).width()<1100?$(window).width()-50:1100, 
			innerHeight:800,
			overlayClose:false,
			title:"详情"
		});
	}
	//确定的查询方法
	function search(str){
		var date1 = $("#datepicker1").val();
		var date2 = $("#datepicker2").val();
		if(date1>date2){
			alert("检验日期前后有误，请重新设置！");
			return;
		}
		reportByParams(getParams());		
	}	
	
	//根据参数获取数据
	function reportByParams(params){
		$.post("${mfgctx}/ipqc/ipqc-audit/observe-rate-datas.htm",params,function(result){
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
		$("#search input[type=radio]").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&obj.checked&&jObj.val()){
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
		var colModel=[{name:'custom_name',width:100,index:'custom_name',align:'center',label:result.firstColName}],datas = [];
		var tableHeaderList = result.tableHeaderList;
		for(var i=0;i<tableHeaderList.length;i++){
			colModel.push({name:'date'+i,index:'date' + i,width:100,align:'center',label : tableHeaderList[i]});
		}
		var dataCheckList = result.series1.data;
		var checks1 = [];
		for(var i=0;i<dataCheckList.length;i++){
			checks1.push(dataCheckList[i].y);
		}
		var p = {id:1,custom_name:"稽核件数"};
		for(var i=0;i<dataCheckList.length;i++){
			p['date' + i] = checks1[i];
		}
		datas.push(p);
		
		var dataRegularList = result.series2.data;
		var checks2 = [];
		for(var i=0;i<dataRegularList.length;i++){
			checks2.push(dataRegularList[i].y);
		}
		p = {id:1,custom_name:"问题件数"};
		for(var i=0;i<dataRegularList.length;i++){
			p['date' + i] = checks2[i];
		}
		datas.push(p);
		
/* 		var dataScoreList = result.series5.data;
		var checks5 = [];
		for(var i=0;i<dataScoreList.length;i++){
			checks5.push(dataScoreList[i].y);
		}
		p = {id:1,custom_name:"稽核分数"};
		for(var i=0;i<dataScoreList.length;i++){
			p['date' + i] = checks5[i];
		}
		datas.push(p); */
		
/* 		var dataProblemList = result.series4.data;
		var checks4 = [];
		for(var i=0;i<dataProblemList.length;i++){
			checks4.push(dataProblemList[i].y);
		}
		p = {id:1,custom_name:"问题分数"};
		for(var i=0;i<dataProblemList.length;i++){
			p['date' + i] = checks4[i];
		}
		datas.push(p);	 */
		
		var dataAvgList = result.series3.data;
		var checks3 = [];
		for(var i=0;i<dataAvgList.length;i++){
			checks3.push(dataAvgList[i].y+"%");
		}
		p = {id:1,custom_name:"遵守度"};
		for(var i=0;i<dataAvgList.length;i++){
			p['date' + i] = checks3[i];
		}
		datas.push(p);		

		
		var width = $("#reportDiv").width()-50;
		detailTable = $("#detail_table").jqGrid({
			datatype: "local",
			localReader: {
				id : 'custom_name'
			},
			data: datas,
			rownumbers: true,
			width: width,
			height: 140,
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
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="ipqc_list";
		var thirdMenu="_ipqc_observe";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-ipqc-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center" >
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
			<button class='btn' type="button" onclick="search(click);"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
			<button class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
			<button class='btn' type="button" onclick="clearAll();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
			<span style="margin-left:4px;color:red;" id="message"></span>
		</div>	
		<div id="search" style="display:block;"  >
			<table class="form-table-outside-border" style="width:100%;display:block;">
				<tr>
					<td style="text-align:right;width:70px;">稽核日期</td>
					<td colspan="2">
						<input id="datepicker1" type="text" name="params.startDate" readonly="readonly" style="width:72px" class="line" value="<%=startDateStr%>"/>&nbsp;至&nbsp;
						<input id="datepicker2" type="text" name="params.endDate" readonly="readonly" style="width:72px" class="line" value="<%=endDateStr%>"/>
					</td>
					<td style="text-align: right;width:60px;">厂区</td>
					<td style="width:150px;">
				    	<s:select list="businessUnits" 
						  theme="simple"
						  listKey="value" 
						  listValue="name" 
						  cssStyle="width:76%;"
						  name="params.businessUnitName"
						  id="businessUnitName"
						  emptyOption="true"></s:select> 
					</td>					
					<td style="text-align: right;width:60px;">制程区段</td>
					<td style="width:150px;">
						<s:select list="factorys"
							listKey="value" 
							listValue="name" 
							name="params.factory" 
							id="factory" 
							cssStyle="width:76%;"
							emptyOption="true"
							theme="simple"></s:select>
				    </td> 					
					<td style="text-align: right;width:60px;">站别</td>
					<td style="width:150px;">
						<s:select list="stations"
							listKey="value" 
							listValue="name" 
							name="params.station" 
							id="station" 
							cssStyle="width:76%;"
							emptyOption="true"
							theme="simple"
							onchange=""></s:select>
				    </td> 
				    <td style="text-align: right;width:60px;">班次</td>
					<td style="width:150px;">
						<s:select list="classGroups"
							listKey="value" 
							listValue="name" 
							name="params.classGroup" 
							id="classGroup" 
							cssStyle="width:76%;"
							emptyOption="true"
							theme="simple"
							onchange=""></s:select>
				    </td>	
				    <td style="text-align: right;width:70px;">稽核类别</td>
					<td style="width:150px;">
						<s:select list="auditTypes"
							listKey="value" 
							listValue="name" 
							name="params.auditType" 
							id="auditType" 
							cssStyle="width:76%;"
							emptyOption="true"
							theme="simple"
							onchange=""></s:select>
				    </td>				    
			</tr>
			<tr>	
				    <td style="text-align: right;width:60px;">责任单位</td>
					<td style="width:150px;">
						<input id="department" type="text" name="params.department" style="width:76%;" />
				    </td>			   
				    <td style="text-align: right;width:60px;">稽核人员</td>
					<td style="width:150px;">
						<input id="auditMan" type="text" name="params.auditMan" style="width:76%;" />
				    </td>					
				</tr>		
			</table>
			<table class="form-table-outside-border" style="width:100%;">
					<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;padding-left: 10px;">统计分组</caption>
					<tr>
						<td style="padding:5px;margin:5px;padding-bottom:2px;">
							<input type="radio" name="params.group" checked="checked" value="day" title="day" id="day"/>日
							<input type="radio" name="params.group" value="week" title="week" id="week"/>周
							<input type="radio" name="params.group" value="month" title="month" id="month"/>月
							<input type="radio" name="params.group" value="year" title="year"/>年	
							<input type="radio" name="params.group" value="station" title="station"/>站别
							<input type="radio" name="params.group" value="classGroup" title="classGroup"/>班别
							<input type="radio" name="params.group" value="auditType" title="auditType"/>稽核类别
							<input type="radio" name="params.group" value="problemDegree" title="problemDegree"/>问题严重度						
						</td>
					</tr>
			</table>			
		</div>
		<div id="chartDiv" style="padding-top:8px;">
			<table style="width:100%;padding-bottom: 20px;">
				<tr>
					<td id="reportDiv_parent">
						<div id='reportDiv'></div>
					</td>
				</tr>
			</table>
		</div>			
		<div id="detailTableDiv_parent" style="padding-top:8px;width: 98%;">
				<table id="detailTable"></table>
		</div>
		</div>
		</form>
	</div>
</body>
</html>