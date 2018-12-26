window.chartView = {
	chartDefinitionCode:'',//图表定义的编码
	webRoot:'',//上下文地址
	searchInputs:[],
	selectListMap:{},
	groupSets:[],
	totalSets:[],
	isShowSearchSet:'true',//是否显示查询条件
	isShowTotalType:'true',//是否显示统计对象
	isShowGroupType:'true',//是否显示分组条件
	isDefaultSearch:'true',//初始化时调用查询方法
	is3D:'false',//是否3D显示
	cacheResult:null,
	charts:[],
	grids:[]
};
window.chartView.init=function(initParams){
	$.extend(window.chartView,initParams);
	var searchInputs = window.chartView.searchInputs;
	var selectListMap = window.chartView.selectListMap;
	for(var i=0;i<searchInputs.length;i++){
		var param = searchInputs[i];
		var editType = param.editType;
		var isMulti = param.isMulti?true:false;
		var $li = $("<li class='searchli'></li>");
		$li.append("<span class='label'>"+param.label+"</span>");
		var name = "field_" + param.id + "_";
		if(editType == 'select' && isMulti){//多选框时采用in
			name += "in";
		}else{
			name += param.searchType;
		};
		var inputHtml = "";
		var inputId = "input" + param.id;
		if(editType == 'text'){
			if(param.isDate){
				inputHtml += '<input searchInput=true dateFormat="'+(param.format?param.format:"")+'"  isDate=true type="text" readonly="readonly" style="width:80px;" class="line" id="'+inputId+'_1" name="field_'+param.id+'_ge" value="'+(param.defaultValue1?param.defaultValue1:'')+'"/>';
				inputHtml += '至<input searchInput=true dateFormat="'+(param.format?param.format:"")+'" isDate=true type="text" readonly="readonly" style="width:80px;" class="line" id="input'+inputId+'_2" name="field_'+param.id+'_le" value="'+(param.defaultValue2?param.defaultValue2:'')+'"/>';
			}else if(param.searchType=='between'){
				inputHtml += '<input searchInput=true type="text" style="width:72px;" class="line" id="'+inputId+'_1" name="field_'+param.id+'_ge" value="'+(param.defaultValue1?param.defaultValue1:'')+'"/>';
				inputHtml += '至<input searchInput=true type="text" style="width:72px;" class="line" id="input'+inputId+'_2" name="field_'+param.id+'_le" value="'+(param.defaultValue2?param.defaultValue2:'')+'"/>';
			}else{
				inputHtml += '<input id="'+inputId+'" searchInput=true class="input" type="text" name="'+name+'"/>';
			}
		}else if(editType=='select'){
			inputHtml = "<select id='"+inputId+"' searchInput=true name='"+name+"' " +(isMulti?"isMulti=true":"")+ ">";
			var valueList = selectListMap[param.id];
			if(valueList){
				for(var j=0;j<valueList.length;j++){
					var o = valueList[j];
					var selected = o.value==param.defaultValue1?"selected":"";
					inputHtml += "<option value=\""+o.value+"\" "+selected+">"+o.name+'</option>';
				}
			}
			inputHtml += "</select>";
		}
		$li.append(inputHtml);
		$("#searchUl").append($li);
		//多选的情况
		if(editType=='select'&&isMulti){
			var $select = $li.find("select");
			$select.multiselect({
				noneSelectedText: "请选择...",
				checkAllText: "全选",
		        uncheckAllText: '全不选',
				selectedList:2
			});
			$select.multiselect("uncheckAll");
			//处理多选框变形的问题
			var width = $li.find("button.ui-multiselect").width();
			$li.find("ul.ui-multiselect-checkboxes").width(width);
		};
		if(param.valueSetType=='plugin'){
			var searchFormatMap = window.chartView.searchFormatMap;
			if(searchFormatMap&&searchFormatMap[param.valueSetCode]){
				//调用初始化方法
				searchFormatMap[param.valueSetCode].initFormat($li.find(":input[searchInput]"));
			}else{
				alert("没有定义编码为["+param.valueSetCode+"]的查询框插件!");
			};
		};
	}
	//初始化编辑类型
	window.chartView.initInputEditType();
	//统计类型
	var groupSets = window.chartView.groupSets;
	for(var i=0;i<groupSets.length;i++){
		var param = groupSets[i];
		var checked = i==0?"checked=checked":'';
		$("#groupParent").append('<input searchInput=true type="radio" id="groupType'+param.id+'" name="groupType" '+checked+' value="'+param.id+'" style="margin-left:20px;"/><label for="groupType'+param.id+'">'+param.name+'</label>');
	}
	//统计对象
	var totalSets = window.chartView.totalSets;
	for(var i=0;i<totalSets.length;i++){
		var param = totalSets[i];
		var checked = i==0?"checked=checked":'';
		$("#totalParent").append('<input searchInput=true type="radio" id="totalType'+param.id+'" name="totalType" '+checked+' value="'+param.id+'" style="margin-left:20px;"/><label for="totalType'+param.id+'">'+param.name+'</label>');
	}
	//是否显示查询条件
	var isShowSearchSet = window.chartView.isShowSearchSet;
	if(isShowSearchSet!='true'){
		window.chartView.toggleSearchTable();	
	}
	//是否显示统计分组
	var isShowGroupType = window.chartView.isShowGroupType;
	if(isShowGroupType!='true'){
		$("#groupTable").hide();
	}
	//是否显示统计对象
	var isShowTotalType = window.chartView.isShowTotalType;
	if(isShowTotalType!='true'){
		$("#totalTable").hide();
	}
	//调用查询方法
	var isDefaultSearch = window.chartView.isDefaultSearch;
	if(isDefaultSearch=='true'){
		window.chartView.search();				
	}
};
//初始化查询框
window.chartView.initInputEditType = function(){
	$(":input[isDate]").each(function(index,obj){
		var formatStr = $(obj).attr("dateFormat");
		if(!formatStr){
			formatStr = "yy-mm-dd";
		}
		$(obj).datepicker({changeYear:true,changeMonth:true,showButtonPanel: true,dateFormat:formatStr});
//		if(formatStr=='yy-MM-dd HH:mm'){
//			$(obj).datetimepicker({changeYear:true,changeMonth:true});
//		}else{
//			$(obj).datepicker({changeYear:true,changeMonth:true,showButtonPanel: true,dateFormat:formatStr});
//		}
	});
};
//控制查询条件
window.chartView.toggleSearchTable=function(){
	var display = $("#searchTable").css("display");
	if(display == 'block'){
		$("#searchTable").css("display","none");
		$("#showOrHideSearchBtn").html("<span><span><b class='btn-icons btn-icons-search'></b>显示查询 </span></span>");
	}else{
		$("#searchTable").css("display","block");
		$("#showOrHideSearchBtn").html("<span><span><b class='btn-icons btn-icons-search'></b>隐藏查询 </span></span>");
	}
};
/**
 *  格式化时间格式字符串
 *  dateStr : 要格式化的时间格式字符串
 * */
window.chartView.parseDateStr=function(dateStr){
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
};
/**
 * 获取年月
 */
window.chartView.getYearAndMonth=function(dateTime){
	var str = dateTime.getFullYear() + "";
	var m = dateTime.getMonth()+1;
	if(m<10){
		str += "0";
	}
	str += m;
	return parseInt(str,10);
};
/**
 * 获取年月
 */
window.chartView.getYearAndMonthFromStr=function(dateStr){
   var strs = dateStr.split(".");
   if(strs.length<2){
   	  return null;
   }
   var year = strs[0],month=strs[1];
   if(month.length<2){
   	  month = "0" + month;
   }
   return parseInt(year+"" + month);
};
/**
 * 查询方法
 */
window.chartView.search=function(){
	var paramArrs = window.chartView.getParams();
	var str = "{" + paramArrs.join(",") + "}";
	var url = window.chartView.webRoot + '/chartdesign/chart-view/chart-datas.htm';
	$.showMessage("正在查询,请稍候... ...",'custom');
	var params = {
		chartDefinitionCode:window.chartView.chartDefinitionCode,
		searchParams:str
	};
	$.post(url,params,function(result){
		$.clearMessage();
		if(result.error){
			alert(result.message);
		}else{
			window.chartView.createChart(result);
			window.chartView.cacheResult = result;
		}
	},'json');
};
/**
 * 获取查询参数
 */
window.chartView.getParams=function(){
	var paramArrs = [];
	$(":input[name][searchInput]").each(function(index,obj){
		var isSel = false;
		var value = '';
		if(obj.type=='radio'){
			if(obj.checked){
				isSel = true;
				value = obj.value;
			}
		}else{
			if(obj.type=='select-one'&&'true'==$(obj).attr("isMulti")){//多选的情况
				var values = $(obj).multiselect("getChecked").map(function(){
				   	return this.value;	
				}).get();
				if(values&&values.length>0){
					value = values.join(",");
					isSel=true;					
				}
			}else if(obj.value != ''){
				isSel=true;
				value = obj.value;
			}
		}
		if(isSel){
			paramArrs.push(obj.name + ":\"" + value + "\"");
		}
	});
	return paramArrs;
};
/**
 * 创建图表
 */
window.chartView.createChart=function(result){
	//清除统计图
	var charts = window.chartView.charts;
	for(var i=0;i<charts.length;i++){
		charts[i].destroy();
	}
	window.chartView.charts = [];
	//清除表格数据
	var grids = window.chartView.grids;
	for(var i=0;i<grids.length;i++){
		grids[i].GridDestroy();
	}
	window.chartView.grids = [];
	
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
				text: ys[i].title,
				style:{
					"fontSize":"12px"
				}
			},
			labels : {
				style: {
					color : 'black',
					"fontSize":"12px"
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
				return window.chartView.dataLabelFormatter(val,format);
			};
		}
		yAxiss.push(obj);
	}
	
	//链接事件
	var dataLabels = {
    	enabled:true,
    	formatter:function(){
    		var format = labelFormatMap[this.series.options.seriesId];
    		if(format){
    			return window.chartView.dataLabelFormatter(this.y,format);
    		}
    		return "";
    	}
    };
	var scrollbarAble = false;
	var xAxisObj = {};
	xAxisObj['categories'] =  result.categories;
	xAxisObj['min'] =  0;
	if(result.categories.length>25){
		xAxisObj['max'] =  25;
		scrollbarAble = true;
	}
	//报表初始化参数
	var reportParams = {
		exporting : {
			enabled : true
		},
		credits: {
	         enabled: false
		},
		chart: {
            renderTo: chartId,
			width : width
        },
        colors: ['#7cb5ec','#f7a35c', 
				    '#8085e9', '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#90ed7d','#91e8e1'],
        title: {
            text: result.title,
            style:{
				"fontSize":"14px"
			}
        },
		legend:{ //图例开关
			enabled:result.isShowLegend
		},
        xAxis:xAxisObj,
        scrollbar: {
            enabled: scrollbarAble
        },
        yAxis: yAxiss,
		tooltip: {
			formatter: function() {
				var seriesId = this.series.options.seriesId;
				var str = '';
				var funcName = '_seriesTooltipFormatter' + seriesId;
				var func = window[funcName];
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
		panning : true,
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
            	allowPointSelect: true,
                dataLabels:$.extend(true,{},dataLabels),
                depth: 50
            }
        }
	};
	//判断是否有设置横坐旋转显示
	if(result.rotation){
		reportParams.xAxis.labels = {
				rotation:result.rotation
			};
	}
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
				labelFormatMap[series.seriesId] = series.showLabelFormatter;						
			}else{
				labelFormatMap[series.seriesId] = '_default';	
			}
		}else{
			if(series.type=='pie'){//饼图时不显示数据值
				reportParams.plotOptions.pie.dataLabels.enabled=false;
			}
		}
		var seriesParam = $.extend(true,{},series);
		if(series.showName){
			seriesParam.name = series.showName;
		}
		//颜色
		if(series.color){
			seriesParam.color = "#" + series.color;
		}
		seriesList.push(seriesParam);
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
	//是否设置为3D
	if(window.chartView.is3D=='true'){
		var chartType = '';
		for(var i=0;i<seriesList.length;i++){
			if(seriesList[i].type=='column'){
				chartType = 'column';
				break;
			}else if(seriesList[i].type=='pie'){
				chartType = 'pie';
				break;
			}
		};
		if(chartType == 'column'){
			reportParams.chart
			.options3d =  {
	            enabled: true,
                alpha:15,
                beta: 15,
                depth: 50
	        };
		}else if(chartType == 'pie'){
			reportParams.chart
			.options3d =  {
				enabled: true,
                alpha: 50,
                beta: 0
	        };
		}
	};
	function pointClick(obj){
		var point = obj.point;
		var options = point.series.options;
		var seriesId = options.seriesId;
		var linkObj = linkSeries[seriesId];
		var cacheResult = window.chartView.cacheResult;
		if(linkObj&&cacheResult){
			var categories = cacheResult.categories;
			var values = '';
			if(point.x<categories.length){
				values = categories[point.x];
			}
			var compareType="=";
			//判断是否other
			if(cacheResult.otherIndex==point.x){
				values='';
				
				for(var i=0;i<categories.length&&i<point.x;i++){
					if(values){
						values+=",";
					}
					values+=categories[i];
				}
				compareType="notin";
			}
			window.chartView.showDetailByArg(options.seriesId,linkObj.linkType,linkObj.linkUrl,compareType,values);
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
	//缓存起来
	window.chartView.charts.push(chart);
	
	//创建表格
	window.chartView.createDetailTable(result);
};
//数据显示格式化
window.chartView.dataLabelFormatter=function(val,format){
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
};
/**
 * 创建表格
 */
window.chartView.createDetailTable=function(result){
	if(!result._gridId){
		return;
	}
	var width = $("#"+result._gridId).parent().width();
	var grid = null;
	if(result.dataTableFormatType == 'series'){
		grid = _createDataTableForSeries(result);
	}else{
		grid = _createDataTableForCategory(result);
	}
	$("#"+result._gridId).jqGrid("setGridWidth",width);
	var pos = result.dataTablePosition;
	if(pos=='left'||pos=='right'){
		$("#"+result._gridId).jqGrid("setGridHeight",$("#" + result._chartId).height()-20);
	}
	window.chartView.grids=[grid];
};
/**使用横坐标的值做表头的方式创建表格*/
function _createDataTableForCategory(result){
//	var colModels = [{name:'id',index:'id',hidden:true}];
	var colModels = [];
	//标题
	var tempLabel = "名称";
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
			label:categories[i]?categories[i]:"&nbsp;",
			index:'col' + i,
			width:_caculateTableColWidth(categories[i])
		});
	}
	//数据格式
	var width = $("#"+result._gridId).parent().width();
	var datas = [];
	for(var i=0;i<result.chartSeries.length;i++){
		var series = result.chartSeries[i];
		var data = series.data;
		var label = series.showName?series.showName:series.name;
		var rowData = {
			label:label
		};
		if(label.length>tempLabel.length){
			tempLabel = label;
		}
		var format = series.showLabelFormatter;
		if(!format){
			format  = '_default';
		}
		for(var index in data){
			rowData['col' + index] = window.chartView.dataLabelFormatter(data[index]==null?"0":data[index], format);
		}
		datas.push(rowData);
	}
	//根据最长的名称设置列的宽度
	colModels[0].width = _caculateTableColWidth(tempLabel);
	
	var grid = $("#"+result._gridId).jqGrid({
		datatype: "local",
		data:datas,
		rownumbers : true,
		rowNum:100,
		colModel:colModels,
	    multiselect: false,
	   	autowidth: true,
		forceFit : true,
	   	shrinkToFit: false,
		viewrecords: true, 
		sortorder: "desc",
		gridComplete : function(){}
	});
	return grid;
};
/**根据字符串计算列的宽度*/
function _caculateTableColWidth(str){
	if(!str){
		return 100;
	}else{
		return str.length * 12 + 30;
	}
}
/**使用系列名称的值做表头的方式创建表格*/
function _createDataTableForSeries(result){
//	var colModels = [{name:'id',index:'id',hidden:true}];
	var colModels = [];
	//标题
	var groupTypeName = $(":input[name=groupType]:checked").next().html();
	groupTypeName = groupTypeName?groupTypeName:"名称";
	var tempLabel = groupTypeName;
	colModels.push({
		name:'label',
		label:groupTypeName,
		index:'label'
	});
	for(var i=0;i<result.chartSeries.length;i++){
		var series = result.chartSeries[i];
		var label = series.showName?series.showName:(series.name?series.name:"&nbsp;");
		colModels.push({
			name:'col' + i,
			label:label,
			index:'col' + i,
			width:_caculateTableColWidth(label)
		});
	};
	//组装数据
	var datas = [];
	var categories = result.categories;
	for(var i=0;i<categories.length;i++){
		var label = categories[i];
		datas.push({
			id:i+1,
			label:label
		});
		if(label.length>tempLabel.length){
			tempLabel = label;
		}
	};
	console.log(tempLabel);
	colModels[0].width = _caculateTableColWidth(tempLabel);
	
	for(var i=0;i<result.chartSeries.length;i++){
		var series = result.chartSeries[i];
		var data = series.data;
		var format = series.showLabelFormatter;
		if(!format){
			format  = '_default';
		}
		for(var index in data){
			var value = window.chartView.dataLabelFormatter(data[index]==null?"0":data[index], format);
			if(index<datas.length){
				datas[index]['col' + i] = value;
			}
		}
	}
	var width = $("#"+result._gridId).parent().width();
	var grid = $("#"+result._gridId).jqGrid({
		datatype: "local",
		data:datas,
		rowNum:100,
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
	return grid;
};
//查看明细的方法
//linkType:链接类型,list:自定义台帐,custom:其他地址
//compareType:比较方式,equals:等于,notin:不包含
//values:比较的值
window.chartView.showDetailByArg=function(seriesId,linkType,linkUrl,compareType,values){
	var url = linkUrl;
	if(linkType=='list'){
		url = window.chartView.webRoot + "/chartdesign/list-definition/view-grid.htm?"
				+"chartDefinitionCode="+window.chartView.chartDefinitionCode+"&seriesId="+seriesId+"&listViewCode="+linkUrl;	
	}
	if(url&&url.indexOf("?")>-1){
		url += "&";
	}else{
		url += "?";
	}
	url += "_compareType="+compareType + "&_compareValues=" + values;
	$.colorbox({href:encodeURI(url),iframe:true, 
		innerWidth:$(window).width()<1366?$(window).width()-124:$(window).width()-366, 
        innerHeight:$(window).height()<768?$(window).height()-68:$(window).height()-68,
		overlayClose:false,
		title:"台帐明细"
	});
};
//导出方法
window.chartView.exportChart = function(){
	if(window.chartView.charts.length==0){
		alert("请等图表显示后再导出!");
		return;
	}
	var chartId = window.chartView.cacheResult?window.chartView.cacheResult._chartId:null;
	if(!chartId){
		alert("请等图表显示后再导出!");
		return;
	}
	$.exportChart({
		title:window.chartView.cacheResult.title,
		chart:window.chartView.charts[0],
		grid:window.chartView.grids.length>0?window.chartView.grids[0]:null,
		message:$("#message"),
		width:$("#" + chartId).width(),
		height:$("#" + chartId).height()
	});
};
//图表配置方法
window.chartView.editChartSetting = function(){
	var url = window.chartView.webRoot + '/chartdesign/chart-definition/input.htm?chartDefinitionCode=';
	url += window.chartView.chartDefinitionCode;
	$.colorbox({
		href:url,
		iframe:true, 
		width:$(window).width()<800?$(window).width()-100:800, 
		height:$(window).height()-40,
		overlayClose:false,
		title:"统计图配置",
		onClosed:function(){
			$.showMessage("正在刷新配置,请稍候... ...","custom");
			window.location.reload(true);
			/**window.location.href = window.chartView.webRoot 
				+ '/chartdesign/chart-view/view.htm?chartDefinitionCode='
				+ window.chartView.chartDefinitionCode;*/
		}
	});
};
