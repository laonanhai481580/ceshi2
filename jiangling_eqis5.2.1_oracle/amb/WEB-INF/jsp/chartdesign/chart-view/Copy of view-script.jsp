<%@page import="com.ambition.chartdesign.entity.ChartSeries"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="${ctx}/widgets/highcharts4.0.3/highcharts.js"></script>
<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
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
		width:175px;
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
			var error = '${error}';
			if(error=='true'){
				return;
			}
			//查询条件
			var searchInputs = ${initSearchs};
			var selectListMap = ${selectListMap};
			for(var i=0;i<searchInputs.length;i++){
				var param = searchInputs[i];
				var editType = param.editType;
				var $li = $("<li></li>");
				$li.append("<span class='label'>"+param.label+"</span>");
				var name = "field_" + param.id + "_" + param.searchType;
				var inputHtml = "";
				if(editType == 'text'){
					if(param.isDate){
						inputHtml += '<input isDate=true type="text" readonly="readonly" style="width:72px;" class="line" name="field_'+param.id+'_ge" value="'+(param.defaultValue1?param.defaultValue1:'')+'"/>';
						inputHtml += '至<input isDate=true type="text" readonly="readonly" style="width:72px;" class="line" name="field_'+param.id+'_le" value="'+(param.defaultValue2?param.defaultValue2:'')+'"/>';
					}else{
						inputHtml += '<input class="input" type="text" name="'+name+'"/>';
					}
				}else if(editType=='select'){
					inputHtml = "<select name='"+name+"'>";
					var valueList = selectListMap[param.id];
					if(valueList){
						for(var j=0;j<valueList.length;j++){
							var o = valueList[j];
							var selected = o.value==param.defaultValue1?"selected":"";
							inputHtml += "<option value="+o.value+" "+selected+">"+o.name+"</option>";
						}
					}
					inputHtml += "</select>";
				}
				$li.append(inputHtml);
				$("#searchUl").append($li);
			}
			//初始化编辑类型
			initInputEditType();
			//统计类型
			var groupSets = ${groupSets};
			for(var i=0;i<groupSets.length;i++){
				var param = groupSets[i];
				var checked = i==0?"checked=checked":'';
				$("#groupParent").append('<input type="radio" id="groupType'+param.id+'" name="groupType" '+checked+' value="'+param.id+'" style="margin-left:20px;"/><label for="groupType'+param.id+'">'+param.name+'</label>');
			}
			//统计对象
			var totalSets = ${totalSets};
			for(var i=0;i<totalSets.length;i++){
				var param = totalSets[i];
				var checked = i==0?"checked=checked":'';
				$("#totalParent").append('<input type="radio" id="totalType'+param.id+'" name="totalType" '+checked+' value="'+param.id+'" style="margin-left:20px;"/><label for="totalType'+param.id+'">'+param.name+'</label>');
			}
			//是否显示查询条件
			var isShowSearchSet = '${isShowSearchSet}';
			if(isShowSearchSet!='true'){
				toggleSearchTable();	
			}
			//是否显示统计分组
			var isShowGroupType = '${isShowGroupType}';
			if(isShowGroupType!='true'){
				$("#groupTable").hide();
			}
			//是否显示统计对象
			var isShowTotalType = '${isShowTotalType}';
			if(isShowTotalType!='true'){
				$("#totalTable").hide();
			}
			//调用查询方法
			var isDefaultSearch = '${isDefaultSearch}';
			if(isDefaultSearch=='true'){
				search();				
			}
		});
		function initInputEditType(){
			$(":input[isDate]").datepicker({changeYear:true,changeMonth:true,showButtonPanel: true});
		}
		//显示方法
		function toggleSearchTable(){
			var display = $("#searchTable").css("display");
			if(display == 'block'){
				$("#searchTable").css("display","none");
				$("#showOrHideSearchBtn").html("<span><span><b class='btn-icons btn-icons-search'></b>显示查询 </span></span>");
			}else{
				$("#searchTable").css("display","block");
				$("#showOrHideSearchBtn").html("<span><span><b class='btn-icons btn-icons-search'></b>隐藏查询 </span></span>");
			}
		}
		function contentResize(){
			var scrollTop = $("#opt-content").scrollTop();
			$("#opt-content").scrollTop(scrollTop);
		}
		function setFormWidth(){
			var width = _getTableWidth()-16;
			var total = parseInt(width/260);
			var addWidth = parseInt(width/total);
			addWidth = addWidth < 260?260:addWidth;
			$("#searchUl li").width(addWidth);
		}
		
		function addMonth(dateTime,month){
			dateTime = new Date(dateTime.getTime());
			var m = dateTime.getMonth()+1+parseInt(month,10);
			dateTime.setMonth(m-1);
			return getYearAndMonth(dateTime);
		}
		/**
		 *  格式化时间格式字符串
		 *  dateStr : 要格式化的时间格式字符串
		 * */
		function parseDateStr(dateStr){
		   var isoExp = /^s*(\d{4})-(\d\d)-(\d\d)s*$/,
	       date = new Date(NaN), month,
	       parts = isoExp.exec(dateStr);
		   if(parts) {
		     month = +parts[2];
		     date.setFullYear(parts[1], month - 1, parts[3]);
		     if(month != date.getMonth() + 1) {
		       date.setTime(NaN);
		     }
		   }
		   return date;
		}
		function getYearAndMonth(dateTime){
			var str = dateTime.getFullYear() + "";
			var m = dateTime.getMonth()+1;
			if(m<10){
				str += "0";
			}
			str += m;
			return parseInt(str,10);
		}
		function getYearAndMonthFromStr(dateStr){
		   var strs = dateStr.split(".");
		   if(strs.length<2){
		   	  return null;
		   }
		   var year = strs[0],month=strs[1];
		   if(month.length<2){
		   	  month = "0" + month;
		   }
		   return parseInt(year+"" + month);
		}
		function search(){
			var paramArrs = getParams();
			var str = "{" + paramArrs.join(",") + "}";
			var url = '${chartdesignctx}/chart-view/chart-datas.htm';
			$.showMessage("正在查询,请稍候... ...",'custom');
			var params = {
				chartDefinitionCode:'${chartDefinitionCode}',
				searchParams:str
			};
			$.post(url,params,function(result){
				$.clearMessage();
				if(result.error){
					alert(result.message);
				}else{
					createChart(result);
					cacheResult = result;
				}
			},'json');
		}
		function getParams(){
			var paramArrs = [];
			$(":input[name]").each(function(index,obj){
				var isSel = false;
				if(obj.type=='radio'){
					if(obj.checked){
						isSel = true;
					}
				}else{
					if(obj.value != ''){
						isSel=true;
					}
				}
				if(isSel){
					paramArrs.push(obj.name + ":\"" + obj.value + "\"");
				}
			});
			return paramArrs;
		}
		//创建统计图
		var charts = [];
		var grids = [];
		var cacheResult = null;
		function createChart(result){
			//清除统计图
			for(var i=0;i<charts.length;i++){
				charts[i].destroy();
			}
			charts = [];
			//清除表格数据
			for(var i=0;i<grids.length;i++){
				grids[i].GridDestroy();
			}
			grids = [];
			
			var html = "";
			var chartId = "__chart";
			if(result.isShowDataTable){
				html += "<table style='width:100%;'>";
				result._gridId = "grid" + (new Date()).getTime();
				if(result.dataTablePosition == 'top'){
					html += "<tr><td><table id='"+result._gridId+"'></table></td></tr>";
					html += "<tr><td style='padding-top:6px;'><div id='"+chartId+"'></div></td></tr>";
				}else if(result.dataTablePosition == 'left'){
					html += "<tr><td width='50%' valign='top'><table id='"+result._gridId+"'></table></td>";
					html += "<td width='50%' valign='top'><div id='"+chartId+"'></div></td></tr>";
				}else if(result.dataTablePosition == 'right'){
					html += "<tr><td width='50%' valign='top'><div id='"+chartId+"'></div></td>";
					html += "<td width='50%' valign='top'><table id='"+result._gridId+"'></table></td></tr>";
				}else{
					html += "<tr><td><div id='"+chartId+"'></div></td></tr>";
					html += "<tr><td style='padding-top:6px;'><table id='"+result._gridId+"'></table></td></tr>";
				}
				html += "</table>";
			}else{
				html += "<div id='__chart'></div>";
			}
			result._chartId = chartId;
			$("#chartDiv").html(html);
			var width = $("#" + chartId).width()-15;
			var yAxiss = [];
			var ys = result.yAxiss;
			for(var i=0;i<ys.length;i++){
				var obj = {
					title: {
						text: ys[i].title
					},
					labels : {
						style: {
							color : 'black'
			            }
					}
				};
				var max=ys[i].max,min=ys[i].min;
				if(max!=undefined){
					obj.max=max;
				}
				if(min!=undefined){
					obj.min=min;
				}
				if(ys[i].position=='right'){
					obj.opposite=true;
				}
				var format = ys[i].showLabelFormatter;
				if(format&&ys[i].isFormatData){
					obj.labels.formatter=function(){
						if(this.value==null||this.value==undefined){
							return;
						}
						var val = this.value;
						if(obj.max!=undefined&&val>obj.max){
							return "";
						}
						return dataLabelFormatter(val,format);
					};
				}
				yAxiss.push(obj);
			}
			
			//链接事件
			var dataLabels = {
            	enabled:true,
            	formatter:function(){
            		var format = labelFormatMap[this.series.name];
            		if(format){
            			return dataLabelFormatter(this.y,format);
            		}
            		return "";
            	}
            };
			//报表初始化参数
			var reportParams = {
				exporting : {
					enabled : false
				},
				credits: {
			         enabled: false
				},
				chart: {
		            renderTo: chartId,
					width : width
		        },
		        title: {
		            text: result.title
		        },
				legend:{ //图例开关
					enabled:result.isShowLegend
				},
		        xAxis: {
		            categories: result.categories
		        },
		        yAxis: yAxiss,
				tooltip: {
					formatter: function() {
						var seriesId = this.series.options.seriesId;
						var str = '';
						var funcName = '_seriesTooltipFormatter' + seriesId;
						var func = window[funcName]
						if(func&&$.isFunction(func)){
							str = func.call(this);
						}
						if(linkSeries[seriesId]){
							if(str){
								str += "<br/>";	
							}
							str += "<span style='color:red;'>单击查看详细</span>";
						}
						if(str){
							return str;
						}else{
							return false;
						}
					}
				},
				plotOptions: {
		            column: {
		                pointPadding: 0,
		                borderWidth: 0,
		                dataLabels:$.extend(true,{},dataLabels)
		            },
		            line:{
		            	pointPadding:0,
		                borderWidth:0,
		                dataLabels:$.extend(true,{},dataLabels)
		            },
		            spline:{
		                dataLabels:$.extend(true,{},dataLabels)
		            },
		            pie:{
		                dataLabels:$.extend(true,{},dataLabels)
		            }
		        }
			};
			
			var seriesList = [];
			var labelFormatMap = {};
			var linkTypes = {},linkSeries={};
			for(var i=0;i<result.chartSeries.length;i++){
				var series = result.chartSeries[i];
				if(!series.isShow){
					continue;
				}
				if(series.isShowLabel){
					if(series.showLabelFormatter){
						labelFormatMap[series.name] = series.showLabelFormatter;						
					}else{
						labelFormatMap[series.name] = '_default';	
					}
				}else{
					if(series.type=='pie'){//饼图时不显示数据值
						reportParams.plotOptions.pie.dataLabels.enabled=false;
					}
				}
				seriesList.push($.extend(true,{},series));
				//判断是否有链接地址
				if(series.isLink&&series.linkUrl){
					linkTypes[series.type] = true;
					linkSeries[series.seriesId]={
						linkType:series.linkType,
						linkUrl:series.linkUrl
					};
				}
			};
			reportParams.series=seriesList;
			if(result.subtitle){
				reportParams.subtitle = {
		            text: result.subtitle
		        };
			};
			function pointClick(obj){
				var point = obj.point;
        		var options = point.series.options;
        		var seriesId = options.seriesId;
        		var value = point.category;
        		var linkObj = linkSeries[seriesId];
        		if(linkObj&&cacheResult){
        			var values=value;
        			var compareType="=";
        			//判断是否other
        			if(cacheResult.otherIndex==point.x){
        				values='';
        				var categories = cacheResult.categories;
        				for(var i=0;i<categories.length&&i<point.x;i++){
        					if(values){
        						values+=",";
        					}
        					values+=categories[i];
        				}
        				compareType="notin";
        			}
        			showDetailByArg(options.seriesId,linkObj.linkType,linkObj.linkUrl,compareType,values);
        		}
			}
			if(linkTypes['column']){
				reportParams.plotOptions.column.cursor = 'pointer';
				reportParams.plotOptions.column.events = {
	            	click : pointClick
	            };
			};
			if(linkTypes['line']){
				reportParams.plotOptions.line.cursor = 'pointer';
				reportParams.plotOptions.line.events = {
	            	click : pointClick
	            };
			};
			if(linkTypes['spline']){
				reportParams.plotOptions.spline.cursor = 'pointer';
				reportParams.plotOptions.spline.events = {
	            	click : pointClick
	            };
			};
			if(linkTypes['pie']){
				reportParams.plotOptions.pie.cursor = 'pointer';
				reportParams.plotOptions.pie.events = {
	            	click : pointClick
	            };
			};
			//图例位置
			var pos = result.legendPosition;
			if(pos=='top'){
				reportParams.legend.verticalAlign='top';
				reportParams.legend.x=width/2-(seriesList.length*60);
			}else if(pos=='left'){
				reportParams.legend.verticalAlign='middle';
				reportParams.legend.layout = 'vertical';
				reportParams.legend.align='left';
			}else if(pos=='right'){
				reportParams.legend.verticalAlign='middle';
				reportParams.legend.align='right';
				reportParams.legend.layout = 'vertical';
			}
			var chart = new Highcharts.Chart(reportParams);
			charts.push(chart);
			
			//创建表格
			createDetailTable(result);
		}
		//数据显示格式化
		function dataLabelFormatter(val,format){
			if(val==undefined){
				return '';
			}
			if(format=='_default'){
				return val;
			}else if(format=='*100+%'){
				return val*100+"%";
			}else if(format=='$'){
				return "$" + val;
			}else{
				return val + format;
			}
		}
		//tooltip显示格式化
		<s:iterator id="format" value="formatterStrs" status="f">
		window._seriesTooltipFormatter${format.seriesId}=function(){
			${format.function};
		}
		</s:iterator>
		//创建表格
		function createDetailTable(result){
			if(!result._gridId){
				return;
			}
			var colModels = [{name:'id',index:'id',hidden:true}];
			//标题
			colModels.push({
				name:'label',
				label:"名称",
				index:'label',
				width:100
			});
			var categories = result.categories;
			for(var i=0;i<categories.length;i++){
				colModels.push({
					name:'col' + i,
					label:categories[i],
					index:'col' + i,
					width:80
				});
			}
			//数据格式
			var width = $("#"+result._gridId).parent().width();
			var datas = [];
			for(var i=0;i<result.chartSeries.length;i++){
				var series = result.chartSeries[i];
				var data = series.data;
				var rowData = {
					label:series.name	
				};
				var format = series.showLabelFormatter;
				if(!format){
					format  = '_default';
				}
				for(var index in data){
					rowData['col' + index] = dataLabelFormatter(data[index], format);
				}
				datas.push(rowData);
			}
			var grid = $("#"+result._gridId).jqGrid({
				datatype: "local",
				data:datas,
				rownumbers : true,
				colModel:colModels,
			    multiselect: false,
			   	autowidth: true,
				forceFit : true,
			   	shrinkToFit: false,
				viewrecords: true, 
				sortorder: "desc",
				gridComplete : function(){}
			});
			$("#"+result._gridId).jqGrid("setGridWidth",width);
			var pos = result.dataTablePosition;
			if(pos=='left'||pos=='right'){
				$("#"+result._gridId).jqGrid("setGridHeight",$("#" + result._chartId).height()-20);
			}
		}
		//查看明细的方法
		//linkType:链接类型,list:自定义台帐,custom:其他地址
		//compareType:比较方式,equals:等于,notin:不包含
		//values:比较的值
		function showDetailByArg(seriesId,linkType,linkUrl,compareType,values){
			var url = linkUrl;
			if(linkType=='<%=ChartSeries.LINKTYPE_LIST%>'){
				url = "${chartdesignctx}/list-definition/view-grid.htm?"
						+"chartDefinitionCode=${chartDefinitionCode}&seriesId="+seriesId+"&listViewCode="+linkUrl;	
			}
			if(url&&url.indexOf("?")>-1){
				url += "&";
			}else{
				url += "?";
			}
			url += "_compareType="+compareType + "&_compareValues=" + values;
			$.colorbox({href:encodeURI(url),iframe:true, 
				innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
	 			overlayClose:false,
	 			title:"台帐明细"
	 		});
		}
	</script>