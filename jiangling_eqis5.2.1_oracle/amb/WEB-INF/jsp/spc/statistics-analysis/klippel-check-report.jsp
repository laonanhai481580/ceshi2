<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
	Calendar calendar = Calendar.getInstance();
	calendar.setFirstDayOfWeek(Calendar.MONDAY);
	calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	int currentYear = calendar.get(Calendar.YEAR);
	int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
	calendar.set(Calendar.DAY_OF_MONTH, 1);
	String startDateStr = sdf.format(calendar.getTime());
	calendar.add(Calendar.MONTH, 1);
	calendar.add(Calendar.DATE, -1);
	String endDateStr = sdf.format(calendar.getTime());
	
	calendar = Calendar.getInstance();
	calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
	calendar.set(Calendar.MONTH,0);
	calendar.set(Calendar.DATE,1);
	String startDateStr1 = sdf1.format(calendar.getTime());
	calendar = Calendar.getInstance();
	calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
	calendar.set(Calendar.MONTH,12);
	calendar.set(Calendar.DATE,1);
	calendar.add(Calendar.DATE,-1);
	String endDateStr1 = sdf1.format(calendar.getTime());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
	<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
	<link href="${ctx}/widgets/multiselect/jquery.multiselect.css" rel="stylesheet" type="text/css"/>
	<script src="${ctx}/widgets/multiselect/jquery.multiselect.js" type="text/javascript"></script>
	<script src="${ctx}/widgets/multiselect/jquery.multiselect.zh-cn.js" type="text/javascript"></script>
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
		var startTime = 0;
		function contentResize(){
			if(cacheResult != null){
				clearTable();
				createReport(cacheResult);
				createDetailTable(cacheResult);
			}
		}
		
		var cacheResult = null;
		function setFormWidth(){
			var width = _getTableWidth()-16;
			var total = parseInt(width/260);
			var addWidth = parseInt(width/total);
			addWidth = addWidth < 260?260:addWidth;
			$("#searchUl li").width(addWidth);
		}
		
		function scopeClick(){
			$(":input[name=scope]").each(function(index,obj){
				if(!obj.checked){
					$(obj).closest("li").find(":input").each(function(index,obj){
						if(obj.name != 'scope'){
							$(obj).attr("disabled","disabled");
						}
					});
				}else{
					$(obj).closest("li").find(":input").attr("disabled","");
				}
			});
		}
		
		$(document).ready(function(){
			contentResize();
			$("#datepicker1").datepicker({changeMonth:true,changeYear:true});
			$("#datepicker2").datepicker({changeMonth:true,changeYear:true});
			$("#datepicker3").datepicker({changeMonth:true,changeYear:true,dateFormat:'yy-mm'});
			$("#datepicker4").datepicker({changeMonth:true,changeYear:true,dateFormat:'yy-mm'});
			$("#_machineNo").multiselect({selectedList:2,value:''});
			scopeClick();
			$(":input[name=scope]").click(scopeClick);
			setFormWidth();
			$(".ui-helper-reset li").css("float","left");
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
				colors : ['#95B3D7','#B7DEE8','Green'],
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
	            			//showDetailByDate(obj.point.name,obj.point.date);
		            	}
		            }
			        },
			        spline: {
			            lineWidth : 1,
			            shadow : true,
			            dataLabels: {
			               enabled: true,
			               formatter : function(){
			            	   return ((this.y / result.max * 100).toFixed(2)) + '%';
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
							return "<b>" + this.series.name + ":</b>" + this.y + "<br/>";
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
			reportByParams(getParams());
			return;
			var statType = $('input[name="params.myType"]:checked').val();
			var date1 = $("#datepicker1").val(),date2 = $("#datepicker2").val();
			var year1 = $("#year1").val(),year2 = $("#year2").val();
			var week1 = $("#week1").val(),week2 = $("#week2").val();
			if(statType!="week"){
				if(date1>date2){
					alert("检验时间选择有误，请重新设置！");
					$("#datepicker1").focus();
				}else{
					
				}
			}else{
				if(year1>year2){
					alert("周期年份选择有误，请重新设置！");
					$("#year1").focus();
				}else if(year1<=year2 && week1>week2){
					alert("周期选择有误，请重新设置！");
					$("#week1").focus();
				}else if(week1==''||week2==''){
					alert("周期不能为空，请重新设置！");
					$("#week1").focus();
				}else{
					reportByParams(getParams());
				}
			}
		}	
		//根据参数获取数据
		function reportByParams(params){
			startTime = (new Date()).getTime();
			$.post("${spcctx}/statistics-analysis/klippel-check-report-datas.htm",params,function(result){
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
				if(obj.type=="select-one"){
					if(obj.name&&!obj.disabled){
						if(obj.id){
							var values = $("#" + obj.id).multiselect("getChecked").map(function(){
							   return this.value;	
							}).get();
							if(values){
								var val = values.toString();
								if(val){
									params[obj.name] = val;
								}
							}else{
								if(jObj.val()){
									params[obj.name] = jObj.val();
								}
							}
						}
					}
				}else{
					var disabled = jObj.attr("disabled");
					if(!disabled&&obj.name&&jObj.val()&&jObj.val()!=""){
						if(obj.type == 'radio'){
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
				}
			});
			var scope = $(":input[name=scope]:checked").val();
			if(scope != 'week'){
				$(":input[name=scope]:checked").closest("li").find(":input").each(function(index,obj){
					if(obj.name&&obj.name != 'scope'){
						if(obj.value){
							if(scope=='month'){
								params['params.' + obj.name] = obj.value.replace(/-/g,"");
							}else{
								params['params.' + obj.name] = obj.value;
							}
						}
					}
				});
			}else{
				var $li = $(":input[name=scope]:checked").closest("li");
				var startWeek = $li.find(":input[name=week_ge]").val();
				if(startWeek){
					var yearAndWeek = $li.find(":input[name=year_ge]").val();
					if(startWeek.length==1){
						yearAndWeek +="0"+startWeek;
					}else{
						yearAndWeek +=startWeek;
					}
					params['params.yearAndWeek_ge_int']=yearAndWeek;
				}
				var endWeek = $li.find(":input[name=week_le]").val();
				if(endWeek){
					var yearAndWeek = $li.find(":input[name=year_le]").val();
					if(endWeek.length==1){
						yearAndWeek +="0"+endWeek;
					}else{
						yearAndWeek +=endWeek;
					}
					params['params.yearAndWeek_le_int']=yearAndWeek;
				}
			}
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
			colModel.push({name:'_total',index:'_total' + i,width:60,align:'center',label : '合计'});
			var dataCheckList = result.series1.data;
			var checks1 = [];
			var _totalAll = 0,_totalHege = 0;
			for(var i=0;i<dataCheckList.length;i++){
				checks1.push(dataCheckList[i].y);
				_totalAll += dataCheckList[i].y;
			}
			var p = {id:1,custom_name:"检验数量"};
			for(var i=0;i<dataCheckList.length;i++){
				p['date' + i] = checks1[i];
			}
			p['_total'] = _totalAll;
			datas.push(p);
			
			var dataRegularList = result.series2.data;
			var checks2 = [];
			for(var i=0;i<dataRegularList.length;i++){
				checks2.push(dataRegularList[i].y);
				_totalHege += dataRegularList[i].y;
			}
			p = {id:1,custom_name:"合格数量"};
			for(var i=0;i<dataRegularList.length;i++){
				p['date' + i] = checks2[i];
			}
			p['_total'] = _totalHege;
			datas.push(p);
			
			var dataRateList = result.series3.data;
			p = {id:1,custom_name:"合格率"};
			for(var i=0;i<dataRateList.length;i++){
				p['date' + i] = parseFloat(dataRateList[i]).toFixed(2)+"%";
			}
			if(_totalAll>0){
				p['_total'] =  (_totalHege/_totalAll*100).toFixed(2)+"%";
			}else{
				p['_total'] = '100.0%';
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
			params['params.inspectionPoint'] = inspectionPoint;
			params['params.inspectionDate'] = inspectionDate;
			params['params.state'] = 'one';
			var url = '${iqcctx}/inspection-report/inspection-report-detail.htm?1=1';
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
			if(obj.value=='month'){
				$('#datepicker1').attr("disabled","");
				$('#datepicker2').attr("disabled","");
				$('#year1').attr("disabled","disabled");
				$('#year2').attr("disabled","disabled");
				$('#week1').attr("disabled","disabled");
				$('#week2').attr("disabled","disabled");
				$('#datepicker1').datepicker('option',{"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
				$('#datepicker2').datepicker('option',{"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
				$('#datepicker1').val("<%=startDateStr1%>");
				$('#datepicker2').val("<%=endDateStr1%>");
			}else if(obj.value=='date'){
				$('#datepicker1').attr("disabled","");
				$('#datepicker2').attr("disabled","");
				$('#year1').attr("disabled","disabled");
				$('#year2').attr("disabled","disabled");
				$('#week1').attr("disabled","disabled");
				$('#week2').attr("disabled","disabled");
				$('#datepicker1').datepicker('option',{"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
				$('#datepicker2').datepicker('option',{"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
				$('#datepicker1').val("<%=startDateStr%>");
				$('#datepicker2').val("<%=endDateStr%>");
			}else{
				$('#datepicker1').attr("disabled","disabled");
				$('#datepicker2').attr("disabled","disabled");
				$('#year1').attr("disabled","");
				$('#year2').attr("disabled","");
				$('#week1').attr("disabled","");
				$('#week2').attr("disabled","");
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
		
		function bomClick(){
			$.colorbox({href:"${mfgctx}/common/product-bom-select.htm",
				iframe:true, 
				width:$(window).width()<700?$(window).width()-100:900,
				height:$(window).height()<400?$(window).height()-100:600,
	 			overlayClose:false,
	 			title:"选择规格型号"
	 		});
	 	}
		
		function setBomValue(datas){
			$("#productNo").val(datas[0].model);
	 	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="stat_analyse";
		var thirdMenu="_klippel_check";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/spc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/spc-statistics-analysis-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow:auto;">
		 	<div class="opt-btn"><span style="margin-left:4px;color:red;" id="message"></span></div>	
			<div id="search" style="display:block">
				<form action="" onsubmit="return false;">
				<table class="form-table-outside-border" style="width:100%;padding:0px;">
					<tr>
						<td>
							<ul id="searchUl">
						 		<li>
						 			<span class="label"><input type="radio" name="scope" value="date" checked="checked"></input>日期</span>
						 			<input id="datepicker1" type="text" readonly="readonly" style="width:72px;" class="line" name="startDate_ge_date" value="<%=startDateStr%>"/>
						 		        至<input id="datepicker2" type="text" readonly="readonly" style="width:72px;" class="line" name="endDate_le_date" value="<%=endDateStr%>"/>
						 		</li>
						 		<li>
						 			<span class="label"><input type="radio" name="scope" value="week"></input>从</span>
						 			<select name="year_ge" style="width:53px;">
						 				<%for(int i=2012;i<=currentYear;i++){%>
						 				<option value="<%=i%>" <%=i==currentYear?"selected='selected'":"" %>><%=i%></option>
						 				<%}%>
						 			</select><input class="input" style="width:15px;height:13px;" type="text" name="week_ge" value="<%=currentWeek%>"/>周至<select name="year_le" style="width:53px;">
						 				<%for(int i=2012;i<=currentYear;i++){%>
						 				<option value="<%=i%>" <%=i==currentYear?"selected='selected'":"" %>><%=i%></option>
						 				<%}%>
						 			</select><input class="input" style="width:15px;height:13px;" type="text" name="week_le" value="<%=currentWeek%>"/>周
						 		</li>
						 		<li>
						 			<span class="label"><input type="radio" name="scope" value="month"></input>月</span>
						 			<input id="datepicker3" type="text" readonly="readonly" style="width:72px;" class="line" name="yearAndMonth_ge_int" value="<%=startDateStr1 %>"/>
						 		        至<input id="datepicker4" type="text" readonly="readonly" style="width:72px;" class="line" name="yearAndMonth_le_int" value="<%=endDateStr1 %>"/>
						 		</li>
						 		<!-- <li>
						 			<span class="label"><input type="radio" name="scope" value="year"></input>年</span>
						 			<select name="year_ge_int" style="width:70px;">
						 				<%for(int i=2012;i<=currentYear;i++){%>
						 				<option value="<%=i%>" <%=i==currentYear?"selected='selected'":"" %>><%=i%>年</option>
						 				<%}%>
						 			</select>
						 		        至
						 		   <select name="year_le_int" style="width:70px;">
						 				<%for(int i=2012;i<=currentYear;i++){%>
						 				<option value="<%=i%>" <%=i==currentYear?"selected='selected'":"" %>><%=i%>年</option>
						 				<%}%>
						 			</select>
						 		</li> -->
					 		</ul>
					    </td>
					</tr>
				</table>
				<table class="form-table-outside-border" style="width:100%">
					<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;">查询条件</caption>
					<tr>
				 		<td>
				 			<span>&nbsp;规格型号</span>
						 	<input type="text" style="width:170px;" name="params.productNo_like" id="productNo" readonly="readonly" onclick="bomClick();"/>
				 			<span style="margin-left:60px;">机台号</span>
				 			<s:select id="_machineNo" list="machineNos" 
							  theme="simple"
							  listKey="value" 
							  listValue="name" 
							  cssClass="targerSelect"
							  cssStyle="width:200px;"
							  name="params.machineNo_in"></s:select>
				 		</td>
						<td style="text-align: right;">
							<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
							<button class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
						 	<button class='btn' type="button" onclick="this.form.reset();scopeClick();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>&nbsp;
						</td>
					</tr>
				</table>
				</form>
			</div>
			<div>
				<table style="width:98.5%;">
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
</html>