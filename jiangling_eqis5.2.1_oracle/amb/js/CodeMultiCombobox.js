$.widget( "ui.CodeMultiCombobox", {
	options : {
		listHeight : 300,
		listWidth : 600,
		listKey : 'defectionCodeName',//显示的属性
		listValue : 'defectionCodeName'//隐藏的属性
	},
	_create: function(config) {
		var self = this;
		var offset = this.element.position();
		var content = this.content = $("<div>").css("position","absolute")
		.css("clear","both")
		.css("background","white")
		.css("z-index",100)
		.css("border","1px solid #6BAECB")
		.css("left",offset.left+"px")
		.css("top",offset.top + "px")
		.css("line-height",(this.element.height()+5) + "px")
		.width(this.element.width()+5)
		.height(this.element.height()+5)
		.insertAfter(this.element)
		.click(function(){
			return false;
		});
		
		var input = this.input = $("<input type='text' readonly=readonly style='border:0px;float:left;'></input>")
		.width(content.width()-40)
		.appendTo(content)
		.click(function(){
			self.expand();
		})
		.keyup(function(e){
			if(e.keyCode==38){
				self.view.hide();
			}else{
				self.expand();
			}
		});
		
		var clearBtn = this.clearBtn = $("<a><span class=\"ui-icon ui-icon-closethick\" style='margin-top:1px;'></span></a>")
		.css("float","left")
		.attr("href","#")
		.attr("title","清除")
		.insertAfter(input)
		.click(function(){
			self.showCache();
		});
		
		var searchBtn = this.searchBtn = $("<a class='small-button-bg'><span class=\"ui-icon ui-icon-triangle-1-s\" style=''></span></a>")
		.css("float","left")
		.attr("href","#")
		.attr("title","展开")
		.insertAfter(clearBtn)
		.click(function(){
			self.expand();
		});
		if(content.find(".small-button-bg").length>1){
			content.find("a").last().remove();
			content.find("a").last().remove();
		}
		//下拉的内容
		this.view = $("<div style='overflow:auto;display:none;position:absolute;' class='custom-combobox-view'>")
			.width(this.options.listWidth)
			.height(this.options.listHeight)
			.css("border","1px solid #6BAECB")
			.css("z-index",98)
			.css("background","white")
			.css("left",offset.left+"px")
			.css("top",offset.top + content.height()+1 + "px")
			.insertAfter(content)
			.click(function(e){
				$(e.target).attr("customSelect","yes");
			});
		//缓存的多选
		this.cacheView = $("<div style='position:absolute;display:none;text-align:center;' class='custom-combobox-view'>")
			.width(content.width())
			.css("border","1px solid #6BAECB")
			.css("z-index",99)
			.css("background","white")
			.css("left",offset.left+"px")
			.css("top",offset.top + content.height()+1 + "px")
			.html("<div class='opt-btn' style='height:24px;line-height:24px;text-align:left;'><span title='清除全部'style='cursor:pointer;font-size:12px;float:left;' class='clearAllCache'>清除全部(<span class='number'></span>)</span><span style='float:right;margin-right:2px;cursor:pointer;font-size:12px;' title='关闭' class='closeCache'>关闭</span></div>")
			.append("<div style='overflow:auto;height:20px;' class='cacheContent'><table style='margin:0px auto;width:99%;' cellpadding=0 cellspacing=0></table><div>")
			.insertAfter(content)
			.click(function(){
				return false;
			});
		this.cacheView.find(".closeCache").click(function(){
			self.cacheView.hide();
		});
		this.cacheView.find(".clearAllCache").click(function(){
			var gridId = self.element.attr("id") + "_BomList";
			for(var i=0;i<self.value.length;i++){
				$("#"+gridId).jqGrid("resetSelection",self.value[i].id);
			}
			self.value = [];
			self.cacheView.find("tr").remove();
			self.addCacheView([]);
			input.val("").attr("title","").focus();
			self.element.val("");
			self.cacheView.hide();
			if($.isFunction(self.onSelect)){
				self.onSelect.call(self.value,self);
			}
		});
		if(!window.hasBindCustomComboboxEvent){//选择框的事件
			window.hasBindCustomComboboxEvent = true;
			$("body").click(function(e){
				if($(e.target).attr("customSelect")!='yes'){
					$('.custom-combobox-view').hide();
				}else{
					$(e.target).removeAttr("customSelect");
				}
			});
		}
		//初始化
		this.value = this.options.value||[];
		this.val(this.value);
	},
	layout : function(gridId){
		var parentWidth = this.view.width();
		var parentHeight = this.view.height();
		var gridWidth = parentWidth - 140 - 6 - 22;
		var gridHeight = parentHeight - 33 - 61;
		$("#" + gridId).jqGrid("setGridWidth",gridWidth).jqGrid("setGridHeight",gridHeight);
	},
	showCache : function(){
		if(this.cacheView.css("display")=='block'){
			this.cacheView.hide();
		}else{
			$(".custom-combobox-view").hide();
			this.cacheView.show();
			var height = this.cacheView.find("table").height();
			if(height>this.options.listHeight){
				height = this.options.listHeight-24;
			}
			this.cacheView.find(".cacheContent").height(height+2);
		}
	},
	expand : function(){
		if(this.view.css("display")=='block'){
			this.view.hide();
		}else{
			$(".custom-combobox-view").hide();
			this.view.show();
			var loaded = this.view.attr("loaded");
			if(!loaded){
				var self = this;
				this.view.attr("loaded","isLoading");
				this.view.html("数据加载中,请稍候... ...")
					.load(webRoot + "/mfg/common/code-bom-custom-multicombobox.htm?elementId=" + this.element.attr("id"),function(){
						self.view.attr("loaded","loaded");
					});
			}else if(loaded == 'loaded'){
				var gridId = this.element.attr("id") + "_BomList";
				this.layout(gridId);
			}
		}
	},
	addCacheView : function(values){
		var self = this;
		this.cacheView.find("tr[class=info]").remove();
		for(var i=0;i<values.length;i++){
			var data = values[i];
			$("<tr height=23 target='"+data.id+"'></tr>")
			 .append("<td style='border-bottom:1px dashed blue;text-align:left;' title='"+data[this.options.listKey]+"'>"+data[this.options.listKey]+"</td>")
			 .append("<td style='border-bottom:1px dashed blue;width:30px;font-size:12px;'><span class='clearCache' style='cursor:pointer;' target='"+data.id+"' title='清除'>清除</span></td>")
			 .appendTo(this.cacheView.find("table"))
			 .mouseover(function(){
				$(this).css("background","yellow"); 
			 }).mouseout(function(){
				 $(this).css("background","");
			 }).find(".clearCache").click(function(){
				 var rowid = $(this).attr("target");
				 var gridId = self.element.attr("id") + "_BomList";
				 $("#"+gridId).jqGrid("resetSelection",rowid);
				 self.removeCacheView([{id:rowid}]);
				 var index = null,showStrs = [],hideStrs = [];
				for(var i=0;i<self.value.length;i++){
					var data = self.value[i];
					if(data.id == rowid){
						index = i;
					}else{
						showStrs.push(data[self.options.listKey]);
						hideStrs.push(data[self.options.listValue]);
					}
				}
				if(index != null){
					self.value.splice(index,1);
				}
				self.element.val(hideStrs.join(","));
				self.input.val(showStrs.join(",")).attr("title",showStrs.join(","));
				if($.isFunction(self.onSelect)){
					self.onSelect.call(self.value,self);
				}
			 });
		}
		var number = this.cacheView.find("tr").length; 
		this.cacheView.find(".number").html(number);
		if(number==0){
			this.cacheView.find("table").append("<tr height=23 class='info'><td style='text-align:left;'>没有选择..</td></tr>");
		}
		if(this.cacheView.css("display")=='block'){
			var height = this.cacheView.find("table").height();
			if(height>this.options.listHeight){
				height = this.options.listHeight-24;
			}
			this.cacheView.find(".cacheContent").height(height);
		}
	},
	removeCacheView : function(values){
		this.cacheView.find("tr[class=info]").remove();
		for(var i=0;i<values.length;i++){
			this.cacheView.find("tr[target="+values[i].id+"]").remove();
		}
		var number = this.cacheView.find("tr").length; 
		this.cacheView.find(".number").html(number);
		if(number==0){
			this.cacheView.find("table").append("<tr height=23 class='info'><td style='text-align:left;'>没有选择..</td></tr>");
		}
		if(this.cacheView.css("display")=='block'){
			var height = this.cacheView.find("table").height();
			if(height>this.options.listHeight){
				height = this.options.listHeight-24;
			}
			this.cacheView.find(".cacheContent").height(height+2);
		}
	},
	select : function(gridId,rowid){
		var change = false;
		var showStrs = [],hideStrs = [];
		var selIds = $("#" + gridId).jqGrid("getGridParam","selarrrow");
		if((","+selIds.join(",")+",").indexOf(","+rowid+",")==-1){//删除
			for(var i=0;i<this.value.length;i++){
				var data = this.value[i];
				if(data.id == rowid){
					this.value.splice(i,1);
					this.removeCacheView([{id:rowid}]);
					change = true;
					break;
				}
			}
		}else{
			var data = $("#"+gridId).jqGrid("getRowData",rowid);
			if(data.id){
				this.value.push(data);
				this.addCacheView([data]);
				change = true;
			}
		}
		if(change){
			for(var i=0;i<this.value.length;i++){
				var data = this.value[i];
				showStrs.push(data[this.options.listKey]);
				hideStrs.push(data[this.options.listValue]);
			}
			this.element.val(hideStrs.join(","));
			this.input.val(showStrs.join(",")).attr("title",showStrs.join(","));
			if($.isFunction(this.onSelect)){
				this.onSelect.call(this.value,this);
			}
		}
	},
	selectAll : function(gridId,rowids){
		if(rowids.length==0){
			return;
		}
		var ids = [];
		for(var i=0;i<this.value.length;i++){
			ids.push(this.value[i].id);
		}
		var selIds = $("#" + gridId).jqGrid("getGridParam","selarrrow");
		if(selIds.length==0){//删除
			var deletes = [];
			for(var i=0;i<rowids.length;i++){
				var index = ("," + ids.join(",") + ",").indexOf(","+rowids[i] + ",");
				if(index>-1){
					deletes.push(this.value[index]);
					this.value.splice(index,1);
					ids.splice(index,1);
				}
			}
			if(deletes.length>0){
				this.removeCacheView(deletes);
			}
		}else{
			var adds = [];
			for(var i=0;i<rowids.length;i++){
				var index = ("," + ids.join(",") + ",").indexOf(","+rowids[i] + ",");
				if(index==-1){
					var data = $("#"+gridId).jqGrid("getRowData",rowids[i]);
					if(data.id){
						ids.push(data.id);
						this.value.push(data);
						adds.push(data);
					}
				}
			}
			if(adds.length>0){
				this.addCacheView(adds);
			}
		}
		var showStrs = [],hideStrs = [];
		for(var i=0;i<this.value.length;i++){
			var data = this.value[i];
			showStrs.push(data[this.options.listKey]);
			hideStrs.push(data[this.options.listValue]);
		}
		this.element.val(hideStrs.join(","));
		this.input.val(showStrs.join(",")).attr("title",showStrs.join(","));
		if($.isFunction(this.onSelect)){
			this.onSelect.call(this.value,this);
		}
	},
	search : function(tableId,val){
		this.selParentId = null;
		$("#"+tableId)[0].p.postData = {customSearch:val};
		$("#"+tableId).jqGrid("setGridParam",{page:1}).trigger("reloadGrid");
	},
	searchByParent : function(tableId,parentId){
		if(this.selParentId == parentId){
			return;
		}
		this.selParentId = parentId || '';
		$("#"+tableId)[0].p.postData = {selParentId:parentId};
		$("#"+tableId).jqGrid("setGridParam",{page:1}).trigger("reloadGrid");
	},
	val : function(value){
		if(value==undefined){
			return this.value;
		}else{
			if(!$.isArray(value)){
				value = [value];
			}
			this.value = value;
			this.cacheView.find("tr").remove();
			this.addCacheView(this.value);
			
			var showStrs = [],hideStrs = [];
			for(var i=0;i<this.value.length;i++){
				var data = this.value[i];
				showStrs.push(data[this.options.listKey]);
				hideStrs.push(data[this.options.listValue]);
			}
			this.element.val(hideStrs.join(","));
			this.input.val(showStrs.join(",")).attr("title",showStrs.join(","));
			if($.isFunction(this.onSelect)){
				this.onSelect.call(this.value,this);
			}
		}
	},
	destroy : function(){
		this.content.remove();
		this.view.remove();
		this.cacheView.remove();
	}
});