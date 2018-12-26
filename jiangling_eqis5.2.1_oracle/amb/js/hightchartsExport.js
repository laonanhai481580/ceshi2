$.extend({
	//导出单个图片
	exportChart:function(options){
		options = options || {};
		if(!options.chart){
			alert("图表不存在");
			return;
		}
		if(!options.grid){
			alert("表格不存在");
			return;
		}
		if(!this.chartIframe){
			var parents = $("form");
			if(parents.length==0){
				parents = options.grid;
			}
			this.chartIframe = $('<iframe name="chart_iframe_custom" style="display: none;"></iframe>').insertAfter(parents);
			this.chartForm = $("<form style='display:none;'></form>").attr("action",webRoot + "/common/hight-chart/chart-export.htm")
			.attr("method","post")
			.attr("target",this.chartIframe.attr("name"))
			.insertAfter(this.chartIframe)
			.append("<input name='svg'></input>")
			.append("<input name='filename' value='aa'></input>")
			.append("<input name='label'></input>")
			.append("<input name='data'></input>")
			.append("<input name='width'></input>")
			.append("<input name='height'></input>");
		}
		
		var svg=options.chart.getSVG();
		this.chartForm.find(":input[name=svg]").val(svg);
		//宽度
		if(options.width){
		this.chartForm.find(":input[name=width]").val(options.width);
		}
		//高度
		if(options.height){
		this.chartForm.find(":input[name=height]").val(options.height);
		}
		
		var labels=[],names=[];
		var colModel=options.grid.jqGrid("getGridParam","colModel");
		var colNames=options.grid.jqGrid("getGridParam","colNames");
		var rownumbers = false;
		if(colModel!=undefined){
			for(var i=0;i<colModel.length;i++){
				var col = colModel[i];
				labels.push(col.label);
				names.push(col.name);
				if(col.name=='rn'){
					rownumbers = true;
				}
			}
			if(colNames){
				labels = [];
				for(var i=0;i<colModel.length;i++){
					if(colModel[i].name=='rn'){
						labels.push("rn");
					}else{
						labels.push(colNames[i]);
					}
				}
			}
		}
	//alert("names:" + names.join(",") + ":labels:" + labels.join(","));
//		if(colModel[1].label==undefined){
//			for(var i=1;i<colNames.length;i++){
//				var col = colNames[i];
//				labels.push(col);
//			}
//		}
		var labelStr=labels.join(",");
		this.chartForm.find(":input[name=label]").val(labelStr);
		var ids=options.grid.jqGrid("getDataIDs");
		var dataStr = "";
//		var idName = options.grid.jqGrid("getGridParam","localReader");
//		if(idName&&idName.id){
//			idName = idName.id;
//		}else{
//			idName = "id";
//		}
		for(var i=0;i<ids.length;i++){
			var d = options.grid.jqGrid('getRowData', ids[i]);
			//alert(d.custom_name);
			/*if(d[idName] != undefined){
				
			}*/
			if(dataStr){
				dataStr +="|";
			}
			var str = "";
			for(var j=0;j<names.length;j++){
				if(str){
					str += ",";
				}
				var val = d[names[j]];
				if(rownumbers&&names[j]=='rn'){
					val = (i+1);
				}
				str += val;
			}
			dataStr += str;
		}
		this.chartForm.find(":input[name=data]").val(dataStr);
		if(!options.message){
			alert("提示信息不存在");
		}else{
			var current = 0;
			var dd = setInterval(function(){
				current++;
				var str = '';
				for(var i=0;i<(current%3);i++){
					str += "...";
				}
				if(current/2>1){
					options.message.html("");
				}else{
					options.message.html("正在下载,请稍候..." + str);
				}
			}, 500); 
			options.message.html("正在下载,请稍候...");
			this.chartIframe.bind("readystatechange",function(){
				clearInterval(dd);
				options.message.html("");
				$.chartIframe.unbind("readystatechange");
			});
		}
		this.chartForm.submit();
	},
	//导出多个图片
	exportCharts:function(options){
		options = options || {};
		if(options.chart.length==0){
			alert("图表不存在");
			return;
		}
		if(!options.grid){
			alert("表格不存在");
			return;
		}
		if(!this.chartIframe){
			var parents = $("form");
			if(parents.length==0){
				parents = options.grid;
			}
			this.chartIframe = $('<iframe name="chart_iframe_custom" style="display: none;"></iframe>').insertAfter(parents);
			this.chartForm = $("<form style='display:none;'></form>").attr("action",webRoot + "/common/hight-chart/chart-exports.htm")
			.attr("method","post")
			.attr("target",this.chartIframe.attr("name"))
			.insertAfter(this.chartIframe)
			.append("<input name='filename' value='aa'></input>")
			.append("<input name='label'></input>")
			.append("<input name='data'></input>")
			.append("<input name='chartSize'></input>");
		}
		
		//报表数量
		this.chartForm.find(":input[name=chartSize]").val(options.chart.length);
		
		for(var i=0;i<options.chart.length;i++){
			this.chartForm
			.append("<input name='svg"+i+"'></input>")
			.append("<input name='width"+i+"'></input>")
			.append("<input name='height"+i+"'></input>");
		}
		
		for(var j=0;j<options.chart.length;j++){
			var svg=options.chart[j].getSVG();
			this.chartForm.find(":input[name=svg"+j+"]").val(svg);
			//宽度
			if(options.width){
				this.chartForm.find(":input[name=width"+j+"]").val(options.width);
			}
			//高度
			if(options.height){
				this.chartForm.find(":input[name=height"+j+"]").val(options.height);
			}
		}
		
		var labels=[],names=[];
		var colModel=options.grid.jqGrid("getGridParam","colModel");
		var colNames=options.grid.jqGrid("getGridParam","colNames");
		var rownumbers = false;
		if(colModel!=undefined){
			for(var i=0;i<colModel.length;i++){
				var col = colModel[i];
				labels.push(col.label);
				names.push(col.name);
				if(col.name=='rn'){
					rownumbers = true;
				}
			}
			if(colNames){
				labels = [];
				for(var i=0;i<colModel.length;i++){
					if(colModel[i].name=='rn'){
						labels.push("rn");
					}else{
						labels.push(colNames[i]);
					}
				}
			}
		}
	//alert("names:" + names.join(",") + ":labels:" + labels.join(","));
//		if(colModel[1].label==undefined){
//			for(var i=1;i<colNames.length;i++){
//				var col = colNames[i];
//				labels.push(col);
//			}
//		}
		var labelStr=labels.join(",");
		this.chartForm.find(":input[name=label]").val(labelStr);
		var ids=options.grid.jqGrid("getDataIDs");
		var dataStr = "";
//		var idName = options.grid.jqGrid("getGridParam","localReader");
//		if(idName&&idName.id){
//			idName = idName.id;
//		}else{
//			idName = "id";
//		}
		for(var i=0;i<ids.length;i++){
			var d = options.grid.jqGrid('getRowData', ids[i]);
			//alert(d.custom_name);
			/*if(d[idName] != undefined){
				
			}*/
			if(dataStr){
				dataStr +="|";
			}
			var str = "";
			for(var j=0;j<names.length;j++){
				if(str){
					str += ",";
				}
				var val = d[names[j]];
				if(rownumbers&&names[j]=='rn'){
					val = (i+1);
				}
				str += val;
			}
			dataStr += str;
		}
		this.chartForm.find(":input[name=data]").val(dataStr);
		if(!options.message){
			alert("提示信息不存在");
		}else{
			var current = 0;
			var dd = setInterval(function(){
				current++;
				var str = '';
				for(var i=0;i<(current%3);i++){
					str += "...";
				}
				options.message.html("正在下载,请稍候..." + str);
			}, 500); 
			options.message.html("正在下载,请稍候...");
			this.chartIframe.bind("readystatechange",function(){
				clearInterval(dd);
				options.message.html("");
				$.chartIframe.unbind("readystatechange");
			});
		}
		this.chartForm.submit();
	},
	exportGrid:function(options){//导出表格
		options = options || {};
		if(!options.grid){
			alert("表格不存在");
			return;
		}
		if(!this.chartIframe){
			var parents = $("form");
			if(parents.length==0){
				parents = options.grid;
			}
			this.chartIframe = $('<iframe name="chart_iframe_custom" style="display: none;"></iframe>').insertAfter(parents);
			this.chartForm = $("<form style='display:none;'></form>").attr("action",webRoot + "/common/hight-chart/grid-export.htm")
			.attr("method","post")
			.attr("target",this.chartIframe.attr("name"))
			.insertAfter(this.chartIframe)
			.append("<input name='svg'></input>")
			.append("<input name='filename' value='aa'></input>")
			.append("<input name='label'></input>")
			.append("<input name='data'></input>")
			.append("<input name='width'></input>")
			.append("<input name='height'></input>");
		}
		
//		var svg=options.chart.getSVG();
//		this.chartForm.find(":input[name=svg]").val(svg);
		//宽度
		if(options.width){
		this.chartForm.find(":input[name=width]").val(options.width);
		}
		//高度
		if(options.height){
		this.chartForm.find(":input[name=height]").val(options.height);
		}
		
		var labels=[],names=[];
		var colModel=options.grid.jqGrid("getGridParam","colModel");
		var colNames=options.grid.jqGrid("getGridParam","colNames");
		var rownumbers = false;
		if(colModel!=undefined){
			for(var i=0;i<colModel.length;i++){
				var col = colModel[i];
				labels.push(col.label);
				names.push(col.name);
				if(col.name=='rn'){
					rownumbers = true;
				}
			}
			if(colNames){
				labels = [];
				for(var i=0;i<colModel.length;i++){
					if(colModel[i].name=='rn'){
						labels.push("rn");
					}else{
						labels.push(colNames[i]);
					}
				}
			}
		}
	//alert("names:" + names.join(",") + ":labels:" + labels.join(","));
//		if(colModel[1].label==undefined){
//			for(var i=1;i<colNames.length;i++){
//				var col = colNames[i];
//				labels.push(col);
//			}
//		}
		var labelStr=labels.join(",");
		this.chartForm.find(":input[name=label]").val(labelStr);
		var ids=options.grid.jqGrid("getDataIDs");
		var dataStr = "";
//		var idName = options.grid.jqGrid("getGridParam","localReader");
//		if(idName&&idName.id){
//			idName = idName.id;
//		}else{
//			idName = "id";
//		}
		for(var i=0;i<ids.length;i++){
			var d = options.grid.jqGrid('getRowData', ids[i]);
			//alert(d.custom_name);
			/*if(d[idName] != undefined){
				
			}*/
			if(dataStr){
				dataStr +="|";
			}
			var str = "";
			for(var j=0;j<names.length;j++){
				if(str){
					str += ",";
				}
				var val = d[names[j]];
				if(rownumbers&&names[j]=='rn'){
					val = (i+1);
				}
				str += val;
			}
			dataStr += str;
		}
		this.chartForm.find(":input[name=data]").val(dataStr);
		if(!options.message){
			alert("提示信息不存在");
		}else{
			var current = 0;
			var dd = setInterval(function(){
				current++;
				var str = '';
				for(var i=0;i<(current%3);i++){
					str += "...";
				}
				options.message.html("正在下载,请稍候..." + str);
			}, 500); 
			options.message.html("正在下载,请稍候...");
			this.chartIframe.bind("readystatechange",function(){
				clearInterval(dd);
				options.message.html("");
				$.chartIframe.unbind("readystatechange");
			});
		}
		this.chartForm.submit();
	},
	chartprint: function (options) {
		options = options || {};
		var container = options.chart.container,
			origDisplay = [],
			origParent = container.parentNode,
			body = document.body,
			childNodes = body.childNodes;

		if (options.chart.isPrinting) { // block the button while in printing mode
			return;
		}

		options.chart.isPrinting = true;

		/**
		 * Utility for iterating over an array. Parameters are reversed compared to jQuery.
		 * @param {Array} arr
		 * @param {Function} fn
		 */
		each = function (arr, fn) {
			var i = 0,
				len = arr.length;
			for (; i < len; i++) {
				if (fn.call(arr[i], arr[i], i, arr) === false) {
					return i;
				}
			}
		};
		
		// hide all body content
		each(childNodes, function (node, i) {
			if (node.nodeType === 1) {
				origDisplay[i] = node.style.display;
				node.style.display = 'none';
			}
		});

		// pull out the chart
		body.appendChild(container);
		body.appendChild($("#detailTableDiv_parent")[0]);

		// print
		window.print();

		// allow the browser to prepare before reverting
		setTimeout(function () {
			// put the chart back in
			origParent.appendChild(container);
			// restore all body content
			each(childNodes, function (node, i) {
				if (node.nodeType === 1) {
					node.style.display = origDisplay[i];
				}
			});
			options.chart.isPrinting = false;

		}, 1000);
	},
	//发送图片
	sendChart:function(options){
		options = options || {};
		if(!options.chart){
			alert("图表不存在");
			return;
		}
		if(!options.grid){
			alert("表格不存在");
			return;
		}
		$._chart_options = options;
		var url = webRoot + "/common/send-chart.htm";
		$.colorbox({href:encodeURI(url),iframe:true, 
			innerWidth:$(window).width()<600?$(window).width()-50:600, 
			innerHeight:$(window).height()<500?$(window).height()-50:500,
 			overlayClose:false,
 			title:"发送邮件"
 		});
	}
});