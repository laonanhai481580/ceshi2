<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
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
			if(chart != null){
				chart.destroy();
				chart = null;
			}
			createReport(cacheResult);
			createDetailTable(cacheResult);
		}
	} 
	var cacheResult = null;
	$(document).ready(function(){
		contentResize();
		$("#datepicker1").datepicker({"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
		$("#datepicker2").datepicker({"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
		searchParams = getParams();
		reportByParams(searchParams);
	});		
	//查看明细的方法
	function showDetailByDate(qualityPoint,yearAndMonth){
		var params = getParams();
		delete params['params.startDate_ge_date'];
		delete params['params.endDate_le_date'];
		params['params.group'] = "reportYearAndMonth";
		params['params.type'] = "lossAmount";
		params['params.arg'] = yearAndMonth + "_equals";
		var url = '${mfgctx}/manu-analyse/defective-goods-report-detail.htm?a=1';
		for(var pro in params){
			url += "&" + pro + "=" + params[pro];
		}
		$.colorbox({href:encodeURI(url),iframe:true, 
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
 			overlayClose:false,
 			title:"台帐明细"
 		});
	}	
	
	function goToNewLocationById1(id){
		var url='${mfgctx}';
		$.colorbox({href:url+"/defective-goods/ledger/view-info.htm?id="+id,iframe:true, 
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"页面详情"
		});
	}
	
	var chart=null;
	function createReport(result){
// 		clearTable();
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
			credits: {
		         enabled: false
			},
			colors :['#AA4643'],
			chart: {
				renderTo: reportDiv,
				spacingRight:65 ,
				marginLeft:65,
				height:280
			},
			title: {
				text: "内部损失推移图"
			},
			subtitle: {
				text: result.subtitle,
				y: 33,
				x: 0
			},
			xAxis: {
				categories: result.categories,
				labels : {
					style : {
		               fontSize:'13px',
					   color: 'black'
					},
					y : 25
				}
				
			},
			yAxis: [{
				title: {
					text: result.yAxisTitle,
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
			               color:"black"
						},
					rotation : 0,
					y : -30
				},
				plotLines: [{
					value: 12,
					color: '#808080'
				}],
				labels : {
					align : 'right'
				}
			},{
				title: {
					text: "",
					style: {
			               color: '#89A54E',
			               'font-weight':'bold',
			               fontSize:15
			            },
					x : 1100,
					rotation : 0
				},
				plotLines: [{
					width: 1,
					color: '#808080'
				}],
				labels : {
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
		         x : -120,
		         y : 15
		    },
		    plotOptions: {
				column: {
		            dataLabels: {
		            	enabled: true,
		            	 formatter : function(){
								return '<font style="color:black;">' + this.y + '</font>';
							}
		            },
		            shadow : false,
		            cursor : 'pointer',
		               events : {
			            	click : function(obj){
			            		showDetailByDate(obj.point.qualityionPoint,obj.point.yearAndMonth);
			            	}
		 				} 
	        		},
		        spline: {
		            dataLabels: {
		               enabled: false,
		               formatter : function(){
		            	   return ((this.y / result.max *  100).toFixed(0)) + '%';
		               }
		            }
		        }
		    },
			tooltip: {
				formatter: function() {
					var s;
					if (this.series.type=="spline") { 
						s = '第'+
						this.x+'天'  +': '+ this.y+'%';
					} else {
						s = this.x+'月份'  +': '+ this.y;
					}
					return s;
				}
			},
			series: [
			{
				type: 'column',
				name: result.series.name,
				data: result.series.data
			}]
		});
	}
	//根据参数获取数据
	function reportByParams(params){
		 $.post("${mfgctx}/defective-goods/ledger/amount-datas.htm",params,function(result){
			if(result.error){
				alert(result.message);
			}else{
				createReport(result);
				createDetailTable(result);
				cacheResult=result;
			}
		},'json');
	} 
	
	//获取表单的值
	function getParams(){
		var params = {};
		$("#search select").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		$("#search input").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		return params;
	}
	function search(){
		var date1 = $("#datepicker1").val();
		var date2 = $("#datepicker2").val();
		if(date1>date2){
			alert("统计年月前后有误，请重新设置！");
		}else{
			searchParams = getParams();
			reportByParams(searchParams);
		}
	}	
	
	//创建表格
	var detailTable = null;
	function createDetailTable(result){
// 		alert(result.series.data);
		clearTable();
		var colModel=[{name:'custom_name',width:70,index:'custom_name',align:'center',label:"月份"}],datas = [];
		var tableHeaderList = result.tableHeaderList;
		for(var i=0;i<tableHeaderList.length;i++){
			colModel.push({name:'date'+i,index:'date' + i,width:45,align:'center',label : tableHeaderList[i]});
		}
		
		var dataRegularList = result.series.data;
		var checks = [];
		for(var i=0;i<dataRegularList.length;i++){
			checks.push(dataRegularList[i].y);
		}
		p = {id:1,custom_name:"内部损失金额(元)"};
		for(var i=0;i<dataRegularList.length;i++){
			p['date' + i] = checks[i];
		}
		datas.push(p);	
		var width = $("#btnDiv").width();
// 		var height = $("#btnDiv").height();
		detailTable = $("#detail_table").jqGrid({
			datatype: "local",
			localReader : {
				id : 'custom_name'
			},
			data: datas,
			rownumbers : true,
			width : width,
			height: 140,
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
		
// 	清除表格
	function clearTable(){
		if(detailTable != null){
			detailTable.GridDestroy();
			detailTable = null;
		}
		$("#detailTableDiv_parent").html("<table id=\"detail_table\"></table>");
	}

	function exportChart(){
		$.exportChart({
    		chart:chart,
    		grid:$("#detail_table"),
    		message:$("#message")
    	});
	}
	//发送邮件
	function sendChart(){
		$.sendChart({
			chart:chart,
			grid:$("#detail_table"),
			message:$("#message"),
			width:$("#reportDiv").width(),
			height:$("#reportDiv").height()
		});
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="regectManager";
		var thirdMenu="_defective_goods_amount";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-defective-goods-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center" style="overflow-y:auto;">
		<div class="opt-body">
			<div class="opt-btn"></div>
			<form onsubmit="return false;">
	 		<div id="search" style="display:block">
				<table class="form-table-outside-border" style="width:100%;">
					<tr>
						<td style="width:70px;text-align:right;">统计年月</td>
						<td>
							<input id="datepicker1" type="text" readonly="readonly" style="width:72px;" class="line" name="params.startDate_ge_date" value="<%=startDateStr%>"/>&nbsp;至
				    		<input id="datepicker2" type="text" readonly="readonly" style="width:72px;" class="line" name="params.endDate_le_date" value="<%=endDateStr%>"/>	
						</td>
						<td style="text-align: right;">
							<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
							<button class='btn' type="reset" ><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
							<button class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
							<button  class='btn' type="button" onclick="sendChart();"><span><span><b class="btn-icons btn-icons-email"></b>发送邮件</span></span></button>&nbsp;
						</td>
					</tr>
				  </table>
				</div>
				</form>
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
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
</html>