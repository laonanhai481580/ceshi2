if(!window.chartView){
	window.chartView = {};
};
window.chartView.searchFormatMap = {};
/**
 * 添加查询输入框格式化方法
 * searchFormat的正确格式如:
 * {
 * 	  code:'customer',
 *    name:'客户查询格式化',
 *    initFormat:function($input){
 *    	   //初始化内容
 *    }
 * }
 */
window.chartView.addSearchFormat = function(searchFormat){
	if(!searchFormat){
		alert("无效的格式化对象");
		return;
	};
	if(!searchFormat.code){
		alert("格式化对象的编码不能为空!");
		return;
	};
	if(!searchFormat.name){
		searchFormat.name = searchFormat.code;
	};
	if(!searchFormat.initFormat){
		alert("查询框格式化的名称为["+searchFormat.name+"]的初始化方法initFormat不能为空!");
		return;
	};
	if(!$.isFunction(searchFormat.initFormat)){
		alert("查询框格式化的名称为["+searchFormat.name+"]的初始化方法initFormat不是有效方法!");
		return;
	};
	if(window.chartView.searchFormatMap[searchFormat.code]){
		alert("编码["+searchFormat.code+"]的查询格式化方法已经存在!");
		return;
	};
	window.chartView.searchFormatMap[searchFormat.code]=searchFormat;
};
//通用的模糊查询方法
window.chartView.inputVagueSearchFormat = function($input,url){
	//模糊查询
	$input.attr("title","输入关键字模糊查询").bind("blur",function(){
		if(!$(this).data("autocomplete").menu.element.is(":visible")){
			var hisSearch = $(this).attr("hisSearch");
			if(this.value !== hisSearch){
				$(this).attr("hisSearch",this.value); 
			}
		}
	}).autocomplete({
		minLength: 1,
		delay: 100,
		autoFocus: true,  
		matchSubset: true,
		source: function( request, response ) {
			response([{label:'数据加载中...',value:''}]);
			$.post(url,{searchParams:'{"code":"'+request.term+'"}'},function(result){
				response(result);
			},'json');
		},
		focus: function() {
			return false;
		},
		select: function( event, ui ) {
			if(ui.item.value){
				this.value = ui.item.label; 
				$input.val(ui.item.value);
				$input.attr("hisValue",ui.item.label);
			}
			return true;
		},
		close : function(event,ui){
			var val = $input.val();
			if(val){
				var hisValue = $input.attr("hisValue");
				if(!hisValue||hisValue==null){
					$input.val(val);
				}else{
					$input.val(hisValue);
				}
			}else{ 
				$input.val('').attr("hisValue",'').attr("hisSearch",'');
			}
		}
	});
};
//添加客户的格式化方法
window.chartView.addSearchFormat({
	code:'customer',
	name:'客户编码',
	initFormat:function($input){
		//弹出框
		var id = $input.attr("id");
		var $a = $('<a class="small-button-bg" autoAppend=true id="'+id+'SelectA" style="float:left;margin-left:2px;margin-top:1px;" onclick="window.chartView._selectCustomer(\''+id+'\')" href="javascript:void(0);" title="选择客户"><span class="ui-icon ui-icon-search" style="cursor:pointer;"></span></a>');
		$input.css({
			"float":"left",
			"margin-top":"2px"
		}).width($input.width()-22)
				.after($a);
		//模糊查询
		var webRoot = window.chartView.webRoot;
		if(!webRoot){
			webRoot = window.customSearchView.webRoot;
		}
		window.chartView.inputVagueSearchFormat($input,webRoot + "/complain/customer-list/select-customer-ByKey.htm");
	}
});
window.chartView._selectCustomer = function(inputId){
	window.chartView._selectObjId = inputId;
	var webRoot = window.chartView.webRoot;
	if(!webRoot){
		webRoot = window.customSearchView.webRoot;
	}
	var selectCustomerUrl = webRoot + "/complain/customer-list/select-customer.htm";
	$.colorbox({href:selectCustomerUrl,iframe:true,
		innerWidth:$(window).width()<900?$(window).width()-50:900, 
		innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
		overlayClose:false,
		title:"选择客户"
	});
};
//选择客户后的回写
function setCustomerValue(objs){
	$("#" + window.chartView._selectObjId).val(objs[0].customerCode);
};
//添加物料的格式化方法
window.chartView.addSearchFormat({
	code:'materialCode',
	name:'物料编码',
	initFormat:function($input){
		//弹出框
		var id = $input.attr("id");
		var $a = $('<a class="small-button-bg" autoAppend=true id="'+id+'SelectA" style="float:left;margin-left:2px;margin-top:1px;" onclick="window.chartView._selectMaterial(\''+id+'\')" href="javascript:void(0);" title="选择物料代码"><span class="ui-icon ui-icon-search" style="cursor:pointer;"></span></a>');
		$input.css({
			"float":"left",
			"margin-top":"2px"
		}).width($input.width()-22)
				.after($a);
		//模糊查询
		var webRoot = window.chartView.webRoot;
		if(!webRoot){
			webRoot = window.customSearchView.webRoot;
		}
		window.chartView.inputVagueSearchFormat($input,webRoot + "/carmfg/base-info/bom/select-material-ByKey.htm");
	}
});
window.chartView._selectMaterial = function(inputId){
	window.chartView._selectObjId = inputId;
	window.chartView._fieldType="code";
	var webRoot = window.chartView.webRoot;
	if(!webRoot){
		webRoot = window.customSearchView.webRoot;
	}
	var selectCustomerUrl = webRoot + "/carmfg/common/product-bom-select.htm?multiselect=true";
	$.colorbox({href:selectCustomerUrl,iframe:true,
		innerWidth:$(window).width()<900?$(window).width()-50:900, 
		innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
		overlayClose:false,
		title:"选择物料编码"
	});
};
//选择物料编号后的回写
//function setFullBomValue(objs){
//	
//};
//添加物料名称的格式化方法
window.chartView.addSearchFormat({
	code:'bomName',
	name:'物料名称',
	initFormat:function($input){
		//弹出框
		var id = $input.attr("id");
		var $a = $('<a class="small-button-bg" autoAppend=true id="'+id+'SelectA" style="float:left;margin-left:2px;margin-top:1px;" onclick="window.chartView._selectBomName(\''+id+'\')" href="javascript:void(0);" title="选择物料名称"><span class="ui-icon ui-icon-search" style="cursor:pointer;"></span></a>');
		$input.css({
			"float":"left",
			"margin-top":"2px"
		}).width($input.width()-22)
				.after($a);
	}
});
window.chartView._selectBomName = function(inputId){
	window.chartView._selectObjId = inputId;
	window.chartView._fieldType="name";
	var webRoot = window.chartView.webRoot;
	if(!webRoot){
		webRoot = window.customSearchView.webRoot;
	}
	var selectProjectUrl = webRoot + "/carmfg/common/product-bom-select.htm";
	$.colorbox({href:selectProjectUrl,
		iframe:true, 
		width:$(window).width()<700?$(window).width()-100:900,
		height:$(window).height()<400?$(window).height()-100:600,
		overlayClose:false,
		title:"选择物料名称"
	});
};
//选择物料后的回写
function setBomValue(objs){
	if(window.chartView._fieldType=="code"){
		$("#" + window.chartView._selectObjId).val(objs[0].key);
	}
	if(window.chartView._fieldType=="name"){
		$("#" + window.chartView._selectObjId).val(objs[0].value);
	}
};

//添加供应商的格式化方法
window.chartView.addSearchFormat({
	code:'supplierName',
	name:'供方',
	initFormat:function($input){
		//弹出框
		var id = $input.attr("id");
		var $a = $('<a class="small-button-bg" autoAppend=true id="'+id+'SelectA" style="float:left;margin-left:2px;margin-top:1px;" onclick="window.chartView._selectSupplier(\''+id+'\')" href="javascript:void(0);" title="选择供应商"><span class="ui-icon ui-icon-search" style="cursor:pointer;"></span></a>');
		$input.css({
			"float":"left",
			"margin-top":"2px"
		}).width($input.width()-22)
				.after($a);
	}
});
window.chartView._selectSupplier = function(inputId){
	window.chartView._selectObjId = inputId;
	var webRoot = window.chartView.webRoot;
	if(!webRoot){
		webRoot = window.customSearchView.webRoot;
	}
	var selectProjectUrl = webRoot + "/supplier/archives/select-supplier.htm";
	$.colorbox({href:selectProjectUrl,iframe:true,
		width:$(window).width()<1000?$(window).width()-100:1000,
		height:$(window).height()<600?$(window).height()-100:600,
		overlayClose:false,
		title:"选择供应商"
	});
};
//选择供应商后的回写
function setSupplierValue(objs){
	$("#" + window.chartView._selectObjId).val(objs[0].name);
};
//添加物料类别的格式化方法
window.chartView.addSearchFormat({
	code:'checkBomMaterialType',
	name:'物料类别',
	initFormat:function($input){
		//弹出框
		var id = $input.attr("id");
		var $a = $('<a class="small-button-bg" autoAppend=true id="'+id+'SelectA" style="float:left;margin-left:2px;margin-top:1px;" onclick="window.chartView._selectcheckBomMaterialType(\''+id+'\')" href="javascript:void(0);" title="选择物料类别"><span class="ui-icon ui-icon-search" style="cursor:pointer;"></span></a>');
		$input.css({
			"float":"left",
			"margin-top":"2px"
		}).width($input.width()-22)
				.after($a);
	}
});
//选择物料类别
window.chartView._selectcheckBomMaterialType = function(inputId){
	window.chartView._selectObjId = inputId;
	var webRoot = window.chartView.webRoot;
	if(!webRoot){
		webRoot = window.customSearchView.webRoot;
	}
	var selectProduct = webRoot + "/supplier/base-info/material-type-goal/select-material-type-list.htm";
	$.colorbox({href:selectProduct,iframe:true,
		width:$(window).width()<1000?$(window).width()-100:1000,
		height:$(window).height()<600?$(window).height()-100:600,
		overlayClose:false,
		title:"选择物料类别"
	});
};
//选择物料类别后的回写
function setMaterialTypeValue(objs){
	var names = "";
	for(var i=0;i<objs.length;i++){
		if(names.length==0){
			names = objs[i].materialType;
		}else{
			names += ","+objs[i].materialType;
		}
	}
	$("#" + window.chartView._selectObjId).val(names);
};
//添加机种的格式化方法
window.chartView.addSearchFormat({
	code:'productName',
	name:'机种',
	initFormat:function($input){
		//弹出框
		var id = $input.attr("id");
		var $a = $('<a class="small-button-bg" autoAppend=true id="'+id+'SelectA" style="float:left;margin-left:2px;margin-top:1px;" onclick="window.chartView._selectProduct(\''+id+'\')" href="javascript:void(0);" title="选择机种"><span class="ui-icon ui-icon-search" style="cursor:pointer;"></span></a>');
		$input.css({
			"float":"left",
			"margin-top":"2px"
		}).width($input.width()-22)
				.after($a);
	}
});
//选择机种
window.chartView._selectProduct = function(inputId){
	window.chartView._selectObjId = inputId;
	var webRoot = window.chartView.webRoot;
	if(!webRoot){
		webRoot = window.customSearchView.webRoot;
	}
	var selectProduct = webRoot + "/carmfg/inspection/termination-inspection-record/select-product.htm";
	$.colorbox({href:selectProduct,iframe:true,
		width:$(window).width()<1000?$(window).width()-100:1000,
		height:$(window).height()<600?$(window).height()-100:600,
		overlayClose:false,
		title:"选择机种"
	});
};
//选择机种后的回写
function setProductValues(objs){
	var names = "";
	for(var i=0;i<objs.length;i++){
		if(names.length==0){
			names = objs[i].machineNo;
		}else{
			names += ","+objs[i].machineNo;
		}
	}
	$("#" + window.chartView._selectObjId).val(names);
};


//添加机种的格式化方法
window.chartView.addSearchFormat({
	code:'oqcProductName',
	name:'OQC机种',
	initFormat:function($input){
		//弹出框
		var id = $input.attr("id");
		var $a = $('<a class="small-button-bg" autoAppend=true id="'+id+'SelectA" style="float:left;margin-left:2px;margin-top:1px;" onclick="window.chartView._selectOqcProduct(\''+id+'\')" href="javascript:void(0);" title="选择机种"><span class="ui-icon ui-icon-search" style="cursor:pointer;"></span></a>');
		$input.css({
			"float":"left",
			"margin-top":"2px"
		}).width($input.width()-22)
				.after($a);
	}
});
//选择机种
window.chartView._selectOqcProduct = function(inputId){
	window.chartView._selectObjId = inputId;
	var webRoot = window.chartView.webRoot;
	if(!webRoot){
		webRoot = window.customSearchView.webRoot;
	}
	var selectOqcProduct = webRoot + "/carmfg/oqc/select-product-list.htm";
	$.colorbox({href:selectOqcProduct,iframe:true,
		width:$(window).width()<1000?$(window).width()-100:1000,
		height:$(window).height()<600?$(window).height()-100:600,
		overlayClose:false,
		title:"选择机种"
	});
};
//选择机种后的回写
function setOqcProductValues(objs){
	var names = "";
	for(var i=0;i<objs.length;i++){
		if(names.length==0){
			names = objs[i].model;
		}else{
			names += ","+objs[i].model;
		}
	}
	$("#" + window.chartView._selectObjId).val(names);
};


//添加售后-客户方法
window.chartView.addSearchFormat({
	code:'afsCustomerClick',
	name:'售后-客户',
	initFormat:function($input){
		//弹出框
		var id = $input.attr("id");
		var $a = $('<a class="small-button-bg" autoAppend=true id="'+id+'SelectA" style="float:left;margin-left:2px;margin-top:1px;" onclick="window.chartView._selectAfsCustomerClick(\''+id+'\')" href="javascript:void(0);" title="选择客户"><span class="ui-icon ui-icon-search" style="cursor:pointer;"></span></a>');
		$input.css({
			"float":"left",
			"margin-top":"2px"
		}).width($input.width()-22)
				.after($a);
	}
});
//选择售后-客户
window.chartView._selectAfsCustomerClick = function(inputId){
	window.chartView._selectObjId = inputId;
	
	var webRoot = window.chartView.webRoot;
	if(!webRoot){
		webRoot = window.customSearchView.webRoot;
	}
//	var customerName=$("#"+selRowId+"_customerName").val();
	var selectAfsCustomerClick = webRoot + "/aftersales/base-info/customer/customer-select.htm";
	$.colorbox({href:selectAfsCustomerClick,iframe:true,
		width:$(window).width()<1000?$(window).width()-100:1000,
		height:$(window).height()<600?$(window).height()-100:600,
		overlayClose:false,
		title:"选择客户"
	});
};
//选择售后-客户后的回写
function setcustomerValues(objs){
	var names = "";
	for(var i=0;i<objs.length;i++){
		if(names.length==0){
			names = objs[i].customerName;
		}else{
			names += ","+objs[i].customerName;
		}
	}
	$("#" + window.chartView._selectObjId).val(names);
};
//添加机型格式化方法
window.chartView.addSearchFormat({
	code:'modelClick',
	name:'客户机型',
	initFormat:function($input){
		//弹出框
		var id = $input.attr("id");
		var $a = $('<a class="small-button-bg" autoAppend=true id="'+id+'SelectA" style="float:left;margin-left:2px;margin-top:1px;" onclick="window.chartView._selectModelClick(\''+id+'\')" href="javascript:void(0);" title="选择机型"><span class="ui-icon ui-icon-search" style="cursor:pointer;"></span></a>');
		$input.css({
			"float":"left",
			"margin-top":"2px"
		}).width($input.width()-22)
				.after($a);
	}
});
//选择客户机型
window.chartView._selectModelClick = function(inputId){
	window.chartView._selectObjId = inputId;
	var webRoot = window.chartView.webRoot;
	if(!webRoot){
		webRoot = window.customSearchView.webRoot;
	}
	var selectModelClick = webRoot + "/aftersales/base-info/customer/model-select.htm";
	$.colorbox({href:selectModelClick,iframe:true,
		width:$(window).width()<1000?$(window).width()-100:1000,
		height:$(window).height()<600?$(window).height()-100:600,
		overlayClose:false,
		title:"选择机型"
	});
};

//添加欧菲机型格式化方法
window.chartView.addSearchFormat({
	code:'ofgClick',
	name:'欧菲机型',
	initFormat:function($input){
		//弹出框
		var id = $input.attr("id");
		var $a = $('<a class="small-button-bg" autoAppend=true id="'+id+'SelectA" style="float:left;margin-left:2px;margin-top:1px;" onclick="window.chartView._selectOfgClick(\''+id+'\')" href="javascript:void(0);" title="选择机型"><span class="ui-icon ui-icon-search" style="cursor:pointer;"></span></a>');
		$input.css({
			"float":"left",
			"margin-top":"2px"
		}).width($input.width()-22)
				.after($a);
	}
});
//选择欧菲机型
window.chartView._selectOfgClick = function(inputId){
	window.chartView._selectObjId = inputId;
	var webRoot = window.chartView.webRoot;
	if(!webRoot){
		webRoot = window.customSearchView.webRoot;
	}
	var selectOfgClick = webRoot + "/aftersales/base-info/customer/model-select-ofg.htm";
	$.colorbox({href:selectOfgClick,iframe:true,
		width:$(window).width()<1000?$(window).width()-100:1000,
		height:$(window).height()<600?$(window).height()-100:600,
		overlayClose:false,
		title:"选择机型"
	});
};
//选择客户机型后的回写
function setProblemValue(objs){
	$("#" + window.chartView._selectObjId).val(objs[0].value);		
};
//选择欧菲机型后的回写
function setOfgModelValue(objs){
	$("#" + window.chartView._selectObjId).val(objs[0].key);
};
//添加组织结构的格式化方法
window.chartView.addSearchFormat({
	code:'userName',
	name:'用户名',
	initFormat:function($input){
		//弹出框
		var id = $input.attr("id");
		var $a = $('<a class="small-button-bg" autoAppend=true id="'+id+'SelectA" style="float:left;margin-left:2px;margin-top:1px;" onclick="window.chartView._selectPerson(\''+id+'\')" href="javascript:void(0);" title="选择用户"><span class="ui-icon ui-icon-search" style="cursor:pointer;"></span></a>');
		$input.css({
			"float":"left",
			"margin-top":"2px"
		}).width($input.width()-22)
				.after($a);
	}
});
window.chartView._selectPerson = function(inputId){
	var webRoot = window.chartView.webRoot;
	if(!webRoot){
		webRoot = window.customSearchView.webRoot;
	}
	popTree({
		innerWidth:'400',
		treeType:'MAN_DEPARTMENT_TREE',
		defaultTreeValue:'loginName',
		leafPage:'false',
		multiple:'false',
		hiddenInputId:inputId,
		showInputId:inputId,
		acsSystemUrl:webRoot,
		callBack : function() {
		}
	});
};
//添加部门的格式化方法
window.chartView.addSearchFormat({
	code:'departName',
	name:'部门',
	initFormat:function($input){
		//弹出框
		var id = $input.attr("id");
		var $a = $('<a class="small-button-bg" autoAppend=true id="'+id+'SelectA" style="float:left;margin-left:2px;margin-top:1px;" onclick="window.chartView._selectDepart(\''+id+'\')" href="javascript:void(0);" title="选择用户"><span class="ui-icon ui-icon-search" style="cursor:pointer;"></span></a>');
		$input.css({
			"float":"left",
			"margin-top":"2px"
		}).width($input.width()-22)
				.after($a);
	}
});
window.chartView._selectDepart = function(inputId){
	var webRoot = window.chartView.webRoot;
	if(!webRoot){
		webRoot = window.customSearchView.webRoot;
	}
	var selectProjectUrl = webRoot;
	popTree({
		innerWidth:'400',
		treeType:'DEPARTMENT_TREE',
		defaultTreeValue:'loginName',
		leafPage:'false',
		multiple:'false',
		hiddenInputId:inputId,
		showInputId:inputId,
		acsSystemUrl:selectProjectUrl,
		callBack : function() {
		}
	});
};
//获取所有的输入框格式化插件
window.chartView.getAllSearchFormats=function(){
	var arrs = [];
	for(var code in window.chartView.searchFormatMap){
		arrs.push({
			code : code,
			name : window.chartView.searchFormatMap[code].name
		});
	};
	return arrs;
};
