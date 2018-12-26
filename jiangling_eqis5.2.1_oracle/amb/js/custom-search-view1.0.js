$.extend($.validator.messages, {
	required: "这是必填字段",
	remote: "请修正此字段",
	email: "请输入有效的电子邮件地址",
	url: "请输入有效的网址",
	date: "请输入有效的日期",
	dateISO: "请输入有效的日期 (YYYY-MM-DD)",
	number: "请输入有效的数字",
	digits: "只能输入数字",
	creditcard: "请输入有效的信用卡号码",
	equalTo: "你的输入不相同",
	extension: "请输入有效的后缀",
	maxlength: $.validator.format("最多可以输入 {0} 个字符"),
	minlength: $.validator.format("最少要输入 {0} 个字符"),
	rangelength: $.validator.format("请输入长度在 {0} 到 {1} 之间的字符串"),
	range: $.validator.format("请输入范围在 {0} 到 {1} 之间的数值"),
	max: $.validator.format("请输入不大于 {0} 的数值"),
	min: $.validator.format("请输入不小于 {0} 的数值")
});
window.customSearchView = {
	customSearchCode:'',//编码
	webRoot:'',//上下文地址
	searchInputs:[],
	selectListMap:{},
	colModels:[],//列模型
	isShowSearchSet:'true',//是否显示查询条件
	isDefaultSearch:'true',//初始化时调用查询方法
	grids:[]
};
//格式化方法
window.customSearchView.formatters = {
	'attachment':function(value,o,rowObject){
		return value;
	}
};
window.customSearchView.init=function(initParams){
	$.extend(window.customSearchView,initParams);
	var searchInputs = window.customSearchView.searchInputs;
	var selectListMap = window.customSearchView.selectListMap;
	for(var i=0;i<searchInputs.length;i++){
		var param = searchInputs[i];
		var editType = param.editType;
		var isMulti = param.isMulti?true:false;
		var $li = $("<li class='searchli'></li>");
		$li.append("<span class='label'>"+param.label+"</span>");
		var name = param.paramName;
		var inputHtml = "";
		var inputId = param.paramName;
		if(param.className){
			param.className = "{" + param.className + "}";
		}
		if(editType == 'text'){
			if(param.inputAmount>1){
				inputHtml += '<input labelName="'+param.label+'" searchInput=true '+(param.dateFlag?("dateFlag="+param.dateFlag):"") + ' ' + (param.formatStr?("formatStr=" + param.formatStr):"") + ' type="text" style="width:72px;" class="'+(param.className?param.className:'')+'" id="'+param.paramName+'_1" name="'+param.paramName+'_1" value="'+(param.defaultValue1?param.defaultValue1:'')+'"/>';
				inputHtml += '至<input labelName="'+param.label+'" searchInput=true '+(param.dateFlag?("dateFlag="+param.dateFlag):"") + ' ' + (param.formatStr?("formatStr=" + param.formatStr):"") + ' type="text" style="width:72px;" class="'+(param.className?param.className:'')+'" id="'+param.paramName+'_2" name="'+param.paramName+'_2" value="'+(param.defaultValue2?param.defaultValue2:'')+'"/>';
			}else{
				inputHtml += '<input labelName="'+param.label+'" id="'+inputId+'" searchInput=true class="'+(param.className?param.className:'')+'"' + (param.dateFlag?("dateFlag="+param.dateFlag):"") + " " + (param.formatStr?("formatStr=" + param.formatStr):"") + ' type="text" name="'+name+'" value="'+(param.defaultValue1?param.defaultValue1:'')+'"/>';
			}
		}else if(editType=='select'){
			inputHtml = "<select id='"+inputId+"' labelName="+param.label+" searchInput=true name='"+name+"' " +(isMulti?"isMulti=true":"")+ ">";
			var valueList = selectListMap[param.paramName];
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
		//多选的情况
		if(editType=='select'&&isMulti){
			var $select = $li.find("select");
			//多选框移除空值的对象
			var $firstOption = $select.find("option").first();
			if($firstOption.val()==''){
				$firstOption.remove();
			}
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
	//没有查询条件时移除掉查询表格和查询按钮
	if(searchInputs.length==0){
		$("#searchTable").remove();
		$("#showOrHideSearchBtn").remove();
		$("#resetSearchBtn").remove();
	}
	//初始化编辑类型
	window.customSearchView.initInputEditType();
	//是否显示查询条件
	var isShowSearchSet = window.customSearchView.isShowSearchSet;
	if(isShowSearchSet!='true'){
		window.customSearchView.toggleSearchTable();	
	}
	//创建表格
	window.customSearchView.createGrid(window.customSearchView);
	//调用查询方法
	var isDefaultSearch = window.customSearchView.isDefaultSearch;
	if(isDefaultSearch=='true'){
		window.customSearchView.search();				
	}
	contentResize();
	//调用初始化完成后的初始化方法
	window.customSearchView.initComplete(window.customSearchView._gridId);
};
//初始化查询框
window.customSearchView.initInputEditType = function(){
	$(":input[dateFlag]").each(function(index,obj){
		var $obj = $(obj);
		var dateFlag = $obj.attr('dateFlag');
		if(dateFlag == 'date'){
			var formatStr = $obj.attr("formatStr");
			if(!formatStr){
				formatStr = 'yy-mm-dd';
			}
			$obj.datepicker({changeYear:true,changeMonth:true,showButtonPanel: true,dateFormat:formatStr});
		}else if(dateFlag == 'datetime'){
			$obj.datetimepicker({changeYear:true,changeMonth:true,showButtonPanel: true});
		}
	});
	//$(":input[isDate]").datepicker({changeYear:true,changeMonth:true,showButtonPanel: true});
};
//控制查询条件
window.customSearchView.toggleSearchTable=function(){
	var display = $("#searchTable").css("display");
	if(display == 'block'){
		$("#searchTable").css("display","none");
		$("#showOrHideSearchBtn").html("<span><span><b class='btn-icons btn-icons-search'></b>显示查询 </span></span>");
	}else{
		$("#searchTable").css("display","block");
		$("#showOrHideSearchBtn").html("<span><span><b class='btn-icons btn-icons-search'></b>隐藏查询 </span></span>");
	}
	contentResize();
};
/**
 *  格式化时间格式字符串
 *  dateStr : 要格式化的时间格式字符串
 * */
window.customSearchView.parseDateStr=function(dateStr){
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
window.customSearchView.getYearAndMonth=function(dateTime){
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
window.customSearchView.getYearAndMonthFromStr=function(dateStr){
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
 * 创建表格
 */
window.customSearchView.createGrid=function(result){
	//清除表格数据
	var grids = window.customSearchView.grids;
	if(grids){
		for(var i=0;i<grids.length;i++){
			grids[i].GridDestroy();
		}
	}
	window.customSearchView.grids = [];
	
	var html = '<table id="table"></table><div id="page"></div>';
	result._gridId = "table";
	$("#tableDiv").html(html);
	
	var colModels = result.colModels;
	var gridParams = {
		datatype: "json",
		postData:{
			customSearchCode : result.customSearchCode
		},
		rownumbers : true,
		colModel:colModels,
	    multiselect: false,
	   	autowidth: true,
		forceFit : true,
	   	shrinkToFit: false,
		viewrecords: true, 
		rownumbers:true,
		beforeRequest : function(){
			var url = $("#table").jqGrid("getGridParam","url");
			if(!url){
				return false;
			}else{
				$.showMessage("正在查询,请稍候... ...","custom");
				return true;
			}
		},
		gridComplete : function(){},
		loadComplete : function(){
			$.clearMessage();
			//延时300毫秒执行加载完成后的处理事件,避免执行时间过长导致反应很慢
			setTimeout(function(){
				//配置的加载完成后的事件
				window.customSearchView.loadComplete(result._gridId);
				//处理合并列
				window.customSearchView.totalGridValue();
				//处理单元格合并
				window.customSearchView.rowspanGridValue();
			},300);
		}
	};
	//检查是否有合计列
	var totalColumnNames = [];
	//合并列
	var rowspanColumnNames = [];
	var totalNameColumn = "";
	for(var i=0;i<colModels.length;i++){
		var model = colModels[i];
		if(model.total){
			totalColumnNames.push(model.name);
		}else if(!model.hidden&&!totalNameColumn){
			totalNameColumn = model.name;
		}
		if(model.rowspan){
			rowspanColumnNames.push(model.name);
		}
	}
	if(totalColumnNames.length>0){
		gridParams.footerrow = true;
	}
	window.customSearchView.totalColumnNames = totalColumnNames;
	
	window.customSearchView.rowspanColumnNames = rowspanColumnNames;
	
	if(result.sortBy){
		gridParams.sortname = result.sortBy;
		gridParams.sortorder = result.sortByType;
	}
	if(result.postData){
		gridParams.postData = result.postData;
	}
	if(result.rowList&&result.rowList.length>0){
		gridParams.rowList = result.rowList;
		gridParams.pager = "#page";
		gridParams.rowNum = result.rowList[0];
	}else{
		gridParams.rowNum = 2000;//最多1万条
	}
	var grid = $("#"+result._gridId).jqGrid(gridParams);
	if(result.groupHeaders&&result.groupHeaders.length>0){
		$("#"+result._gridId).jqGrid('setGroupHeaders', {
		  useColSpanStyle: true, 
		  groupHeaders:result.groupHeaders
		});
	}
	//合计列
	if(totalColumnNames.length>0&&totalNameColumn){
		var fotterJson = {};
		fotterJson[totalNameColumn] = '<div style="text-align:center;">合计</div>';
		grid.footerData("set",fotterJson);
	}
	window.customSearchView.grids.push(grid);
};
/**
 * 合计单元格的值
 */
window.customSearchView.totalGridValue=function(){
	if(window.customSearchView.totalColumnNames.length>0){
		var totalJson = {};
		var totalColumnNames = window.customSearchView.totalColumnNames;
		for(var i=0;i<totalColumnNames.length;i++){
			totalJson[totalColumnNames[i]] = '';
		}
		var datas = $("#" + window.customSearchView._gridId).jqGrid("getRowData");
		for(var i=0;i<datas.length;i++){
			var d = datas[i];
			for(var columnName in totalJson){
				var val = d[columnName];
				if(val == undefined || val == ''){
					continue;
				}
				//处理有格式化内容的情况
				val = val.replace(/<[^<|>]*>/g,'');
				if(isNaN(val)){
					continue;
				}
				val = parseFloat(val);
				if(totalJson[columnName] == ''){
					totalJson[columnName] = val;
				}else{
					totalJson[columnName] = totalJson[columnName] + val;
				}
			}
		}
		//用户自定义配置处理
		window.customSearchView.totalFormatter(totalJson);
		
		$("#" + window.customSearchView._gridId).footerData("set",totalJson);
	}
};
/**
 * 合并单元格的值
 */
window.customSearchView.rowspanGridValue=function(){
	if(window.customSearchView.rowspanColumnNames.length>0){
		setTimeout(function(){
			$.showMessage("正在合并单元格","custom");
			var rowspanColumns = window.customSearchView.rowspanColumnNames;
			var gridId = window.customSearchView._gridId;
			var ids = $("#"+gridId).jqGrid('getDataIDs');
			var lastFlagJson = {},rowSpanIdJson = {};
			for(var i=0;i<ids.length;i++){
				var d = $("#"+gridId).jqGrid('getRowData',ids[i]);
				for(var j=0;j<rowspanColumns.length;j++){
					var rowSpanField = rowspanColumns[j];
					if(d[rowSpanField]){
						if(lastFlagJson[rowSpanField]!=undefined&&d[rowSpanField] != lastFlagJson[rowSpanField]){
							window.customSearchView._customRowspan(gridId,rowSpanIdJson[rowSpanField],rowSpanField);
							rowSpanIdJson[rowSpanField] = [];
						}
						if(!rowSpanIdJson[rowSpanField]){
							rowSpanIdJson[rowSpanField] = [];
						}
						rowSpanIdJson[rowSpanField].push(ids[i]);
						lastFlagJson[rowSpanField] = d[rowSpanField];
					}
				}
			}
			for(var rowSpanField in lastFlagJson){
				window.customSearchView._customRowspan(gridId,rowSpanIdJson[rowSpanField],rowSpanField);
			}
			$.clearMessage();
		},100);
	}
};
window.customSearchView._customRowspan=function(gridId,rowSpanIds,rowSpanField){
	if(rowSpanIds.length<2){
		return;
	}
	var firstRow = $("#" + rowSpanIds[0],"#"+gridId);
	firstRow.find("td[aria-describedby="+gridId+"_"+rowSpanField+"]").attr("rowspan",rowSpanIds.length).attr("valign","middle");
	
	for(var i=1;i<rowSpanIds.length;i++){
		var row = $("#" + rowSpanIds[i],"#"+gridId);
		row.find("td[aria-describedby="+gridId+"_"+rowSpanField+"]").remove();
	}
};
/**
 * 查询方法
 */
window.customSearchView.search=function(){
	if(!$("#customSearchForm").valid()){
		return;
	}
	var params = window.customSearchView.getParams();
	//需要分页
	if(window.customSearchView.rowList.length>0){
		params._needPage = true;
	}
	var url = window.customSearchView.webRoot + '/chartdesign/custom-search-view/chart-datas.htm';
	$("#" + window.customSearchView._gridId).jqGrid("setGridParam",{
		page:1,
		postData:{
			searchParams:JSON.stringify(params)
		},
		url:url
	}).trigger("reloadGrid");
};
/**
 * 获取查询参数
 */
window.customSearchView.getParams=function(){
	//var paramArrs = [];
	var params = {};
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
			params[obj.name] = value;
			//paramArrs.push(obj.name + ":\"" + value + "\"");
		}
	});
	return params;
};
//数据显示格式化
window.customSearchView.dataLabelFormatter=function(val,format){
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
//查看明细的方法
//linkType:链接类型,list:自定义台帐,custom:其他地址
//compareType:比较方式,equals:等于,notin:不包含
//values:比较的值
window.customSearchView.showDetailByArg=function(seriesId,linkType,linkUrl,compareType,values){
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
window.customSearchView.exportGrid = function(){
	if(window.customSearchView.grids.length==0){
		alert("台帐为空,不能导出!");
		return;
	}
	var postData = $("#" + window.customSearchView._gridId).jqGrid("getGridParam","postData");
	var url = window.customSearchView.webRoot + '/chartdesign/custom-search-view/exports.htm';
	$("button").attr("disabled","disabled");
	$.showMessage("正在导出,请稍候... ...","custom");
	$.post(url,postData,function(result){
		$.clearMessage();
		$("button").removeAttr("disabled");
		if(result.error){
			alert(result.message);
		}else{
			var fileUrl = window.customSearchView.webRoot + '/portal/export-data.action?fileName='+ result.exportFileFlag;
			fileUrl = encodeURI(fileUrl);
			$("#exportForm").attr("action",fileUrl);
			$("#exportForm").submit();
		}
	},'json');
};
//保存列宽
window.customSearchView.saveGridColumnWidth = function(){
	var grid = $("#" + window.customSearchView._gridId);
	var colModel = grid.jqGrid("getGridParam","colModel");
	var widthMap = {};
	for(var i=0;i<colModel.length;i++){
		var model = colModel[i];
		widthMap[model.name] = model.width;
	}
	var params = {
		customSearchCode : window.customSearchView.customSearchCode,
		widthStrs : JSON.stringify(widthMap)
	};
	var url = window.customSearchView.webRoot + '/chartdesign/custom-search/save-width.htm';
	$("button").attr("disabled","disabled");
	$.showMessage("正在保存,请稍候... ...","custom");
	$.post(url,params,function(result){
		$.clearMessage();
		$("button").removeAttr("disabled");
		if(result.error){
			alert(result.message);
		}else{
			$.showMessage("保存成功");
		}
	},'json');
};
//打开新页面
window.customSearchView.openColorbox = function(url,title,params){
	var newUrl = window.customSearchView.webRoot + url;
	if(params){
		newUrl += "?" + $.param(params);
	}
	$.colorbox({
		href:newUrl,
		iframe:true, 
		width:$(window).width()-100, 
		height:$(window).height()-40,
		overlayClose:false,
		title:title?title:'详细信息',
		onClosed:function(){
			
		}
	});
};
//图表配置方法
window.customSearchView.editGridSetting = function(){
	var url = window.customSearchView.webRoot + '/chartdesign/custom-search/input.htm?customSearchCode=';
	url += window.customSearchView.customSearchCode;
	$.colorbox({
		href:url,
		iframe:true, 
		width:$(window).width()-100, 
		height:$(window).height()-40,
		overlayClose:false,
		title:"修改配置",
		onClosed:function(){
			$.showMessage("正在刷新配置,请稍候... ...","custom");
			window.location.reload(true);
			/**window.location.href = window.chartView.webRoot 
				+ '/chartdesign/chart-view/view.htm?chartDefinitionCode='
				+ window.chartView.chartDefinitionCode;*/
		}
	});
};
