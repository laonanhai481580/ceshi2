<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.ambition.util.common.CommonUtil1"%>
<%@page import="com.ambition.supplier.utils.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.iqc.entity.IncomingInspectionActionsReport" %>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<%
	Calendar calendar = Calendar.getInstance();
	String endDateStr1 = DateUtil.formateDateStr(calendar);
	calendar = Calendar.getInstance();
	calendar.add(Calendar.DATE,-10);
	String startDateStr1 = DateUtil.formateDateStr(calendar);
	calendar.set(Calendar.DAY_OF_MONTH, 1);
	
	calendar = Calendar.getInstance();
	String endDateStr = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM");
	calendar.add(Calendar.MONTH,-6);
	String startDateStr = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM");
	
    //物料大类
    ActionContext.getContext().put("materialTypes",ApiFactory.getSettingService().getOptionsByGroupCode("IQC_MATERIAL_TYPE"));
%>
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
	<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
	<link href="${ctx}/widgets/multiselect/jquery.multiselect.css" rel="stylesheet" type="text/css"/>
	<script src="${ctx}/widgets/multiselect/jquery.multiselect.js" type="text/javascript"></script>
	<script src="${ctx}/widgets/multiselect/jquery.multiselect.zh-cn.js" type="text/javascript"></script>
	<script type="text/javascript">
	var startTime = 0;
	function contentResize(){
		if(cacheResult != null){
			clearTable();
			createReport(cacheResult);
			createDetailTable(cacheResult);
		}
	}
	function formReset(){
		$("#checkBomMaterialType").multiselect("uncheckAll");
		$("#businessUnitCode").multiselect("uncheckAll");
	}
	var cacheResult = null;
		$(document).ready(function(){
			contentResize();
			$('#datepicker1').datepicker({"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
			$('#datepicker2').datepicker({"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
			$("#checkBomMaterialType").multiselect({selectedList:1});
			$("#checkBomMaterialType").multiselect("uncheckAll");
			$("#businessUnitCode").multiselect({selectedList:1});
			$("#businessUnitCode").multiselect("uncheckAll");
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
					x:0 //center
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
					 // opposite: true,
				        gridLineWidth : 0,
				        tickInterval : 10,
					labels : {
						style: {
							color : 'black'
			            },
						align : 'right',
						x : width - 60,
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
			            shadow : true,
// 			            pointInterval : 0,
			            borderWidth:0,
			            pointPadding:0,
			            groupPadding:0.0,
			    cursor : 'pointer',
	               events : {
		            	click : function(obj){
// 		            		alert(obj.point.date);
		            		showDetailByDate(obj.point.name,obj.point.date);
		            	}
		            }
			        },
			        spline: {
			            lineWidth : 1,
			            shadow : true,
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
							s = this.x + '的值' + ': ' + this.y.toFixed(2) + '%';
						}else{
							return "<b>" + this.series.name + ":</b>" + this.y + "<br/><span style='font-size:12px;color:blue;'>单击查看详细</span>";
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
			},function(){
				var endTime = (new Date()).getTime();
//                 alert ('rendered:' + (endTime - startTime));
			});
		}
		
		//确定的查询方法
		function search(){
			var date1 = $("#datepicker1").val();
			var date2 = $("#datepicker2").val();
			if(date1>date2){
				alert("检验时间选择有误，请重新设置！");
				$("#datepicker1").focus();
			}else{
				reportByParams(getParams());
			}
		}	
		
		//根据参数获取数据
		function reportByParams(params){
			$("#message").html("图表查询中,请稍候...");
			startTime = (new Date()).getTime();
			$.post("${iqcctx}/inspection-report/inspection-one-chart-datas.htm",params,function(result){
				$("#message").html("");
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
			$("#search :input[name]").each(function(index,obj){
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
						$(":input[name=multiselect_"+obj.id+"]:checked").each(function(index,obj){
							if(value){
								value += ",";
							}
							value += obj.value;
						});
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
		function dutySupplierClick(obj){
			var url='${supplierctx}';
			$.colorbox({href:url+"/archives/select-supplier.htm?multiselect=true",iframe:true, 
				innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"选择供应商"
			});
		}
		
		function setSupplierValue(objs){
			var dutySupplierName="";
			var dutySupplierCode="";
			for(var i=0;i<objs.length;i++){
				var obj = objs[i];
				dutySupplierName+=obj.name+',';
				dutySupplierCode+=obj.code+',';
			}
			dutySupplierName=dutySupplierName.substring(0, dutySupplierName.length-1);
			dutySupplierCode=dutySupplierCode.substring(0, dutySupplierCode.length-1);
			$("#params\\.dutySupplier").val(dutySupplierName);
			$("#params\\.dutySupplierCode").val(dutySupplierCode);
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
			var p = {id:1,custom_name:"检验批数"};
			for(var i=0;i<dataCheckList.length;i++){
				p['date' + i] = checks1[i];
			}
			datas.push(p);
			
			var dataRegularList = result.series2.data;
			var checks2 = [];
			for(var i=0;i<dataRegularList.length;i++){
				checks2.push(dataRegularList[i].y);
			}
			p = {id:1,custom_name:"合格批数"};
			for(var i=0;i<dataRegularList.length;i++){
				p['date' + i] = checks2[i];
			}
			datas.push(p);
			
			var dataRateList = result.series3.data;
			p = {id:1,custom_name:"合格率"};
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
				colModel: colModel,
			    multiselect: false,
			   	autowidth: true,
				forceFit : true,
				shrinkToFit: false,
				viewrecords: true, 
				sortorder: "desc",
				gridComplete : function(){}
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
		function showDetailByDate(inspectionPoint,inspectionDate){
			var params = getParams();
			if(inspectionPoint == '合格批数'){
				params['params.inspectionConclusion_equals'] = 'OK';			
			}
			params['params.startDate_ge_date'] = inspectionDate;
			params['params.endDate_le_date'] = inspectionDate;
			var url = '${iqcctx}/inspection-report/inspection-report-detail.htm?a=1';
			for(var pro in params){
				url += "&" + pro + "=" + params[pro];
			}
			$.colorbox({href:encodeURI(url),iframe:true, 
				innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
	 			overlayClose:false,
	 			title:"台帐明细"
	 		});
		}
		
		function goToNewLocationById(id){
			var url='${iqcctx}';
			$.colorbox({href:url+"/inspection-report/view-info.htm?id="+id,iframe:true, 
				innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"页面详情"
			});
		}
		function typeSelect(obj,selectId){
			$(".typeSelect").attr("disabled",true);
			$("#" + selectId).attr("disabled",false);
			if(obj.value=='month'){
				$('#datepicker1').datepicker('option',{"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
				$('#datepicker2').datepicker('option',{"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
				$('#datepicker1').val("<%=startDateStr%>");
				$('#datepicker2').val("<%=endDateStr%>");
			}else{
				$('#datepicker1').datepicker('option',{"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
				$('#datepicker2').datepicker('option',{"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
				$('#datepicker1').val("<%=startDateStr1%>");
				$('#datepicker2').val("<%=endDateStr1%>");
			}
		}
		
		function exportChart(){
			 $.exportChart({
	    		chart:chart,
	    		grid:$("#detail_table"),
	    		message:$("#message"),
	    		width:$("#reportDiv").width(),
	    		height:$("#reportDiv").height()
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
			$("#materialTypeInformation").html(html);
		}
		
		
	</script>
</head>