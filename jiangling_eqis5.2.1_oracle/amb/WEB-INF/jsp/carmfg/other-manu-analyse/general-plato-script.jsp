<%@page import="com.ambition.carmfg.entity.InspectionPointTypeEnum"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%
	StringBuffer sb = new StringBuffer("");
    InspectionPointTypeEnum[] enums = InspectionPointTypeEnum.values();
	for(int i=0;i<enums.length;i++){
		InspectionPointTypeEnum e = enums[i];
		if(e != InspectionPointTypeEnum.PATROLINSPECTION){
			if(sb.length()>0){
				sb.append(",");
			}
			sb.append(e.ordinal());
		}
	}
	request.setAttribute("inspectionPointType",sb.toString());
%>
<script type="text/javascript" src="${ctx}/widgets/spin/jquery-spin.js"></script>
<script type="text/javascript" src="${ctx}/js/CodeCombobox.js"></script>
<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
<style>
<!--
	#searchUl{
		margin:0px;
		padding:0px;
	}
	#searchUl li{
		float:left;
		width:260px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#searchUl li select{
		width:178px;
	}
	.input{
		width:170px;
	}
	.label{
		float:left;
		width:70px;
		text-align:right;
		padding-right:2px;
	}
	#groupUl{
		margin:0px;
		padding:0px;
	}
	#groupUl li{
		float:left;
		width:95px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#groupUl li.last{
		padding:0px;
		width:280px;
		margin-bottom:2px;
		text-align:right;
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

		$(function(){
			$("#customCombobox").CodeCombobox({
				value : {id:1,code:'asd',name:'add'}
			});
		});
		function contentResize(){
			if(cacheResult != null){
				createReport(cacheResult);
				createDetailTable(cacheResult);
			}
			setFormWidth();
		}
		function setFormWidth(){
			var width = _getTableWidth()-16;
			var total = parseInt(width/260);
			var addWidth = parseInt(width/total);
			addWidth = addWidth < 260?260:addWidth;
			$("#searchUl li").width(addWidth);
		}
		var chart = null,searchParams = null,cacheResult = null;
		$(document).ready(function(){
			$.spin.imageBasePath = '${ctx}/widgets/spin/img/spin1/';
			$('#pageSize').spin({
				max: 100,
				min: 1
			});
			$("#datepicker1").datepicker({changeMonth:true,changeYear:true});
			$("#datepicker2").datepicker({changeMonth:true,changeYear:true});
			searchParams = getParams();
			reportByParams(searchParams);
			setFormWidth();
		});
		function createReport(result){
			if(chart != null){
				try {
					chart.destroy();
					chart = null;
				} catch (e) {
					chart = null;
				}
			}
			var chartType = 'plato';
			$(":input[name=photo]").each(function(index,obj){
				if(obj.checked){
					chartType = obj.value;
					return false;
				}
			});
			var width = $("#btnDiv").width()/2;
			$("#reportDiv_parent").html("<div style=\"width:"+width+"px;\" id='reportDiv'>图表生成中,请稍候... ...</div>");
			setTimeout(function(){
				if(chartType=='plato'){
					var isShowMax = 0;
					var size = result.categories.length;
					chart = new Highcharts.Chart({
						exporting : {
							enabled : false
						},
						colors: ["#4BB2C5", "#E0B56C", "#DF5353", "#7798BF", "#aaeeee", "#ff0066", "#eeaaee",
						 		"#55BF3B", "#DF5353", "#7798BF", "#aaeeee"],
						chart: {
							renderTo: "reportDiv",
							marginTop : 50
						},
						credits: {
					         enabled: false
						},
						title: {
							style : {
								"font-weight":'bold',
								"font-size": '20px',
								color: 'black'
							},
							text: result.title,
							y:13,
							x:-20
						},
						subtitle: {
							text: null
						},
						xAxis: {
							categories: result.categories,
							gridLineDashStyle : 'ShortDashDot',
//		 					gridLineWidth: 1,
							labels : {
								style : {
					               fontSize:'13px',
								   color: 'black'
								},
								y : 25,
								rotation:-45
							},
							title: {
								style: {
									fontWeight: 'bold',
									fontSize: '12px'
								}
							}
						},
						yAxis: [{
							title: {
								text: result.yAxisTitle1,
								style : {
									color: 'black',
					               fontWeight:'bold',
					               fontSize:15
								},
								rotation : 0,
								y : -70
							},
							plotLines: [{
								value: 12
							}],
							labels : {
								align : 'right'
							},
							gridLineWidth : 1,
//		 					tickInterval : parseInt(result.max/10),
							gridLineDashStyle : 'ShortDashDot'
						},{
							title: {
								text: result.yAxisTitle2,
								style: {
									color: 'black',
					               'font-weight':'bold',
					               fontSize:15
					            },
								rotation : 0,
								y : -70
							},
							plotLines: [{
								width: 1
							}],
					        opposite: true,
					        gridLineWidth : 0,
//		 			        tickInterval : result.max/10,
							labels : {
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
							}
						}],
						legend: {
					         align: 'right',
					         verticalAlign : 'top',
					         floating: true,
					         x : -20,
					         y : 12
					    },
					    style: {
							color: 'black'
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
					            borderWidth : 0,
					            pointPadding : 0,
					            pointWidth : size == 0 ? 1 : (width-160)/size,
					            cursor : 'pointer',
					            events : {
					            	click : function(obj){
					            		showDetailByArg(obj.point.arg);
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
					               },
					               color:'black'
					            }
					        }
					    },
						tooltip: {
							formatter: function() {
								var s;
								if(this.point.name == 'spline'){
									s = ''+
									'累计不良百分比 ' + ((this.y / result.max *  100).toFixed(2)) + '%';
								}else{
									if("result"==result.type){
										s = ''+
										'不良扣分: '+ this.y + "<br/><span style='font-size:12px;color:blue;'></span>";
									}else{
										s = ''+
										'不良数量: '+ this.y + "<br/><span style='font-size:12px;color:blue;'></span>";
									}
								}
								return s;
							}
						},
						series: [{
							type: 'column',
							name: result.series1.name,
							data: result.series1.data
						},{
							type: 'spline',
							name: result.series2.name,
							data: result.series2.data,
							yAxis : 1
						}]
					});
				}else if(chartType=='pie'){
					var myData = [];
					if(result.categories){
						for(var i=0;i<result.tableData.length;i++){
							var d = {};
							d.name = result.categories[i];
							d.y = result.tableData[i].total;
							d.arg = result.series1.data[i].arg;
							if(i == 0){
								d.sliced = 'true';
								d.selected = 'true';
							}
							myData.push(d);
						}
					}
					chart = new Highcharts.Chart({
						chart: {
							renderTo: "reportDiv",
							plotBackgroundColor: null,
							plotBorderWidth: null,
							plotShadow: false
						},
						title: {
							text: '不合格分析饼图'
						},
						tooltip: {
							formatter: function() {
								return '<b>'+ this.point.name +'</b>: '+ this.percentage.toFixed(2) +' %';
							}
						},
						credits: {
					         enabled: false
						},
						plotOptions: {
							pie: 
							{
								allowPointSelect: true,
								cursor: 'pointer',
								dataLabels: {
									enabled: true,
									color: '#000000',
									connectorColor: '#000000',
									formatter: function() {
										return '<b>'+ this.point.name +'</b>: '+ this.percentage.toFixed(2) +' %';
									}
								},
								showInLegend: true,
								events : {
					            	click : function(obj){
					            		showDetailByArg(obj.point.arg);
					            	}
					            }
							}
							
						},
						series: [{
							type: 'pie',
							name: 'Browser share',
							data: myData
						}]
					});
				}
				$("#detail_table").jqGrid("setGridHeight",$("#reportDiv").height()-1);
			},10);
		}
		//确定的查询方法
		function search(){
			searchParams = getParams();
			var date1 = $("#datepicker1").val();
			var date2 = $("#datepicker2").val();
			if(date1&&date2&&date1>date2){
				alert("检验日期前后选择有误,请重新设置!");
			}else{
				reportByParams(searchParams);
			}
		}
		//根据参数获取数据
		function reportByParams(params){
			params = params || searchParams;
			$.post("${mfgctx}/other-manu-analyse/general-plato-datas.htm",params,function(result){
				if(result.error){
					alert(result.message);
				}else{
					createReport(result);
					createDetailTable(result);
					cacheResult = result;
// 					window.location = "#reportDiv";
				}
			},'json');
		}


		//显示方法
		function toggleSearchTable(obj){
			var display = $("#searchTable").css("display");
			if(display == 'block'){
				$("#searchTable").css("display","none");
				$(obj).html("<span><span><b class='btn-icons btn-icons-search'></b>显示查询 </span></span>");
			}else{
				$("#searchTable").css("display","block");
				$(obj).html("<span><span><b class='btn-icons btn-icons-search'></b>隐藏查询 </span></span>");
			}
		}
		//获取表单的值
		function getParams(){
			var params = {};
			$("#customerSearchDiv :input").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name&&jObj.val()&&jObj.val()!=""){
					if(obj.type == 'radio'){
						if(obj.checked){
							params[obj.name] = jObj.val();
							if(obj.name=='params.group'&&jObj.attr("title")){
								params['params.groupName'] = jObj.attr("title");
							}
						}
					}else{
						if(obj.name=='params.endDate_le_datetime'){
							params[obj.name] = jObj.val()+' 23:59:59';
						}else{
							params[obj.name] = jObj.val();
						}
					}
				}
			});
			return params;
		}
		
		//创建表格
		var detailTable = null;
		function createDetailTable(result){
			if(detailTable != null){
				detailTable.GridDestroy();
				detailTable = null;
			}
			$("#detail_table_parent").html("<table id=\"detail_table\"></table>");
			//数据格式
			detailTable = $("#detail_table").jqGrid({
				datatype: "local",
				data: result.tableData,
				rownumbers : true,
				colNames:[result.series1.name,(result.type=='result'?'不良扣分':'不良数量'),'百分比','累计','累计百分比'], 
				colModel:[ 
	               {name:'name',index:'name',width:80}, 
	               {name:'total',index:'total',width:60,align:'center'}, 
	               {name:'bi1',index:'bi1',width:80,align:'center'}, 
	               {name:'allTotal',index:'allTotal',width:70,align:'center'}, 
	               {name:'bi2',index:'bi2',width:70,align:'center'}
		        ],
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
		//查看明细的方法
		function showDetailByArg(arg){
			var params = getParams();
			params['params.arg'] = arg;
			var url = '${mfgctx}/other-manu-analyse/general-plato-detail.htm?1=1';
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
		//明细详情
		function goToNewLocationById(id){
			var url='${mfgctx}';
			$.colorbox({href:url+"/check-inspection/analyse-view-info.htm?id="+id,iframe:true, 
				innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"页面详情"
			});
		}
		//选择物料BOM
	 	function selectBomValue(){
	 		var url = '${mfgctx}/common/product-bom-select.htm';
	 		$.colorbox({href:url,iframe:true, 
	 			innerWidth:$(window).width()<900?$(window).width()-50:1000, 
				innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
	 			overlayClose:false,
	 			title:"选择物料BOM"
	 		});
	 	}
		
		//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setBomValue(data){
	 		$("#itemdutyPart").val(data[0].value).focus();
	 	}
	 	//选择方位
	 	function selectDirectionCode(obj){
	 		var url = '${mfgctx}/common/direction-bom-select.htm';
	 		$.colorbox({href:url,iframe:true, 
	 			innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
	 			overlayClose:false,
	 			title:"选择方位"
	 		});
	 	}
		
	 	//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setDirectionValue(datas){
	 		$("#itemdirectionCodeName").val(datas[0].value).focus();
	 	}
	 	//选择部件
	 	function selectComponentCode(obj){
	 		var url = '${mfgctx}/common/component-bom-select.htm';
	 		$.colorbox({href:url,iframe:true, 
	 			innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
	 			overlayClose:false,
	 			title:"选择部位"
	 		});
	 	}
	 	//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setComponentValue(datas){
	 		$("#itempositionCodeName").val(datas[0].value).focus();
	 	}
	 	function showMe(obj){
	 		contentResize();
	 	}
	</script>