$.widget( "ui.CodeCombobox", {
	options : {
		listHeight : 300,
		listWidth : 600,
		listKey : 'defectionCodeName',//显示的属性
		listValue : 'defectionCodeName'//隐藏的属性
	},
	_create: function(config) {
		config = config||{};
		this.config = config;
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
			input.val("").focus();
			self.element.val("");
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
		
		this.view = $("<div style='overflow:auto;display:none;padding:0px;'>")
			.addClass("custom-combobox-view")
			.css("position","absolute")
			.width(this.options.listWidth)
			.height(this.options.listHeight)
			.css("border","1px solid #6BAECB")
			.css("z-index",99)
			.css("background","white")
			.css("left",offset.left+"px")
			.css("top",offset.top + content.height()+1 + "px")
			.insertAfter(content)
			.click(function(e){
				$(e.target).attr("customSelect","yes");
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
	},
	layout : function(gridId){
		var parentWidth = this.options.listWidth;
		var parentHeight = this.options.listHeight;
		var gridWidth = parentWidth - 140 - 6 - 22;
		var gridHeight = parentHeight - 33 - 61;
		$("#" + gridId).jqGrid("setGridWidth",gridWidth).jqGrid("setGridHeight",gridHeight);
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
					.load(webRoot + "/mfg/common/code-bom-custom-combobox.htm?elementId=" + this.element.attr("id"),function(){
						self.view.attr("loaded","loaded");
					});
			}else if(loaded == 'loaded'){
				var gridId = this.element.attr("id") + "_BomList";
				this.layout(gridId);
			}
		}
	},
	select : function(tableId,rowid){
		var data = $("#"+tableId).jqGrid("getRowData",rowid);
		if(data.id){
			this.element.val(data[this.options.listValue]);
			this.input.val(data[this.options.listKey]);
			this.view.hide();
			if($.isFunction(this.onSelect)){
				this.onSelect.call(data,this);
			}
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
			this.element.val(value[this.options.listValue]);
			this.input.val(value[this.options.listKey]);
		}
	},
	destroy : function(){
		this.content.remove();
		this.view.remove();
	}
});