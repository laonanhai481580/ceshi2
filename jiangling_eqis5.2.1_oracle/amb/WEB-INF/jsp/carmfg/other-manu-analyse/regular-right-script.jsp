<%@ page import="com.ambition.carmfg.entity.InspectionPointTypeEnum"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%
	//目标线
	String goalLine = "98";
	try{
		goalLine = com.norteksoft.product.util.PropUtils.getProp("regular-right-rate-goal-line");
	}catch(Exception e){}
	
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
<%-- <script type="text/javascript" src="${ctx}/js/common-layout.js"></script> --%>
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
	#searchUl li input{
		width:170px;
	}
	#searchUl li span{
		float:left;
		width:70px;
		text-align:right;
		padding-right:2px;
	}
-->
</style>
<script type="text/javascript">
		function setFormWidth(){
			var width = _getTableWidth()-16;
			var total = parseInt(width/260);
			var addWidth = parseInt(width/total);
			addWidth = addWidth < 260?260:addWidth;
			$("#searchUl li").width(addWidth);
		}
		function contentResize(){
			if(cacheResult != null){
				clearTable();
				createReport(cacheResult);
				createDetailTable(cacheResult);
			}
			setFormWidth();
		}
		var goalLine = '<%=goalLine%>';//目标线
		var chart = null,cacheResult = null, model = 'date';
		
		$(document).ready(function(){
			$('#datepicker1').datepicker({changeMonth:true,changeYear:true});
			$('#datepicker2').datepicker({changeMonth:true,changeYear:true});
			$("#check_select").each(function(index,obj){
				$($(obj).find("option")[0]).html("全选");
				if($(obj).find("option").length == 1){
					$(obj).width(100);
				}
			});
			$("#inspection_select").each(function(index,obj){
				$($(obj).find("option")[0]).html("全选");
				if($(obj).find("option").length == 1){
					$(obj).width(100);
				}
			});
// 			var val = $('input:checkbox[name="params.myType"]:checked').val();
// 			if(val == model){
<%-- 				$('#datepicker1').val("<%=startDateStr%>"); --%>
<%-- 				$('#datepicker2').val("<%=endDateStr%>"); --%>
// 			}else{
<%-- 				$('#datepicker1').val("<%=startYearStr%>"); --%>
<%-- 				$('#datepicker2').val("<%=endYearStr%>"); --%>
// 			}
			search();
			setFormWidth();
		});
		function createReport(result){
			if(chart != null){
				chart.destroy();
				chart = null;
			}
			var goalLines = [];
			var datas = result.seriesRate.data;
			for(var i=0;i<datas.length;i++){
				goalLines.push(parseFloat(goalLine));
			}
			var width = $("#btnDiv").width()-10;
			var size = result.tableHeaderList.length;
			var hasShowGoalLine = false;
			$("#reportDiv").html("图表生成中,请稍候... ...");
			setTimeout(function(){
				chart = new Highcharts.Chart({
					exporting : {
						enabled : false
					},
					colors : ['#95B3D7','#B7DEE8','#89A54E'],
					chart: {
						renderTo: "reportDiv",
						width : width
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
						x:0//center
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
							style : {
								color : 'black',
				               fontSize:15
							},
							rotation : 0,
							y : -40
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
							   color : 'black',
				               fontSize:18
				            },
							rotation : 0,
							y : -50
						},
						plotLines: [{
							width: 1,
							color: 'black'
						}],
				        opposite: true,
				        gridLineWidth : 0,
						labels : {
							style: {
								color : 'black'
				            },
							formatter : function(){
								if(this.value>100){
									return "";
								}else{
									return this.value + '%';
								}
							}
						},
						min : 0,
				        max : 99
					}],
					legend: {
				         align: 'right',
				         verticalAlign : 'top',
				         floating: true,
				         backgroundColor: '#FFFFFF',
				         x : -55,
				         y : 16
				    },
					plotOptions: {
						column: {
				            dataLabels: {
				               enabled: false,
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
//	 			            pointInterval : 0,
				            borderWidth:0,
				            pointPadding:0,
//	 			            groupPadding:0.0,
				            pointWidth : 10,
				            cursor : 'pointer',
				            events : {
				            	click : function(obj){
				            		showDetailByDate(obj.point.date,obj.point.name);
				            	}
				            }
				        },
				        line: {
//	 			            shadow : false,
				            lineWidth : 1,
				            dataLabels: {
				               enabled: true,
				               formatter : function(){
				            	   return '<font style="color:black;">' + this.y + '%</font>';
				               }
				            }
				        },
				        spline : {
				            dashStyle : 'ShortDot',
				            shadow : false,
				        	dataLabels : {
				        	   enabled: true,
				        	   x:40,
				               formatter : function(){
				            	   if(!hasShowGoalLine){
				            		   hasShowGoalLine = true;
				            		   return "<b>目标线" + this.y + "%</b>";
				            	   }else{
				            		   return '';
				            	   }
				               }
				        	},
				        	color : 'green'
				        }
				    },
					tooltip: {
						formatter: function() {
							if(this.point.name){
								if(this.point.name == 'rate'){
									return "<b>" + this.series.name + ":</b>" + this.y + "%";
								}else{
									return "<b>" + this.series.name + ":</b>" + this.y + "<br/><span style='font-size:12px;color:blue;'></span>";
								}
							}else{
								return "<b>目标线:</b>" + this.y + "%";
							}
						}
					},
					series: [{
						type: 'column',
						flag : 'check',
						name: result.seriesCheck.name,
						data: result.seriesCheck.data,
						yAxis : 0
					},{
						type: 'column',
						flag : 'regular',
						name: result.seriesRegular.name,
						data: result.seriesRegular.data,
						yAxis : 0
					},{
						type: 'line',
						flag : 'rate',
						name: result.seriesRate.name,
						data: result.seriesRate.data,
						yAxis : 1
					},{
						type : 'spline',
						name: '目标线',
						marker: {
			                symbol: 'url(rl.png)'
			            },
						data: goalLines,
						yAxis : 1
					}]
				});
			},100);
		}
		//确定的查询方法
		function search(){
// 			var val = $('input:checkbox[name="params.myType"]:checked').val();
			var date1 = $("#datepicker1").val();
			var date2 = $("#datepicker2").val();
			if(date1>date2){
				alert("检验时间前后有误,请重新设置!");
			}else{
				reportByParams(getParams());
			}
		}
		//根据参数获取数据
		function reportByParams(params){
			$.post("${mfgctx}/other-manu-analyse/regular-right-chart-datas.htm",params,function(result){
				if(result.error){
					alert(result.message);
				}else{
					createDetailTable(result);
					createReport(result);
					cacheResult = result;
					window.location = "#typeTable";
				}
			},'json');
		}
		
		//显示方法
		function toggleSearchTable(obj){
			var display = $("#curstomerSearchTable").css("display");
			if(display == 'block'){
				$("#curstomerSearchTable").css("display","none");
				$(obj).html("<span><span><b class='btn-icons btn-icons-search'></b>显示查询 </span></span>");
			}else{
				$("#curstomerSearchTable").css("display","block");
				$(obj).html("<span><span><b class='btn-icons btn-icons-search'></b>隐藏查询 </span></span>");
				window.location = "#btnDiv";
			}
		}
		//取消方法
		function hiddenSearchTable(){
			$("#curstomerSearchTable").css("display","none");
		}
		//获取表单的值
		function getParams(){
			var params = {};
			$("#customerSearchDiv :input").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name&&jObj.val()&&jObj.val()!=""){
					if(obj.type=="radio"){
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
			clearTable();
			setTimeout(function(){
				var colModel=[{name:'custom_name',width:70,index:'custom_name',label:'日期'}],datas = [];
				var tableHeaderList = result.tableHeaderList;
				for(var i=0;i<tableHeaderList.length;i++){
					colModel.push({name:'date'+i,index:'date' + i,width:45,align:'center',label : tableHeaderList[i]});
				}
				var dataCheckList = result.seriesCheck.data;
				var p = {id:1,custom_name:result.seriesCheck.name};
				for(var i=0;i<dataCheckList.length;i++){
					p['date' + i] = dataCheckList[i].y;
				}
				datas.push(p);
				
				var dataRegularList = result.seriesRegular.data;
				p = {id:1,custom_name:result.seriesRegular.name};
				for(var i=0;i<dataRegularList.length;i++){
					p['date' + i] = dataRegularList[i].y;
				}
				datas.push(p);
				
				var dataRateList = result.seriesRate.data;
				p = {id:1,custom_name:result.seriesRate.name};
				for(var i=0;i<dataRateList.length;i++){
					p['date' + i] = parseFloat(dataRateList[i].y).toFixed(2)+"%";
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
			},10);
		}
		
		//清除表格
		function clearTable(){
			if(detailTable != null){
				detailTable.GridDestroy();
				detailTable = null;
			}
			$("#detailTableDiv_parent").html("<table id=\"detail_table\"></table>");
		}
		//统计对象改变的方法
		function typeSelect(obj,selectId){
			$(".typeSelect").attr("disabled",true);
			$("#" + selectId).attr("disabled",false);
		}
		//重置查询条件的方法
		function formReset(){
			typeSelect($("#inspection_select_radio")[0],"inspection_select");
		}
		//查看明细的方法
		function showDetailByDate(inspectionDate,name){
			var params = getParams();
			params['params.inspectionDate'] = inspectionDate;
			params['params.name'] = name;
			var url = '${mfgctx}/other-manu-analyse/regular-right-detail.htm?1=1';
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
		
		function exportChart(){
			$.exportChart({
	    		chart:chart,
	    		grid:$("#detail_table"),
	    		message:$("#message")
	    	});
		}
// 		function checkme(e){ 
// 			var t = document.getElementsByName(e.name);//取得鼠标点中的控件数组。 
// 			var est = e.checked; 
// 			for(var i=0;i<t.length;i++){
// 				if(t[i].checked){
// 					t[i].checked = false;
// 				}else{
// 					t[i].checked = true;
// 				}
// 			}//排除同组控件选中 
// 			e.checked = est; 
// 			model = e.value;
// 		}
	</script>
	
