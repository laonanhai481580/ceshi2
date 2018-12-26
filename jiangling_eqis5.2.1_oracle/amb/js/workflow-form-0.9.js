var _ambWorkflowFormObj = {
	actionBaseUrl:'',//后台执行的Action的前缀名,如:http://localhost:8080/amb/qrqc
	webBaseUrl:'',//项目的前缀地址,如:http://localhost:8080/amb
	objId:'',//数据库对象的ID
	taskId:'',//任务ID
	formId:'',//表单ID
	isView:false,//是否仅显示
	refuseValid:false,//不同意时不验证
	saveValid:false,//保存时是否验证
	inputformortaskform:'',//表单类型,taskform:流程办理界面,inputform:普通表单页面
	fieldPermissionStr : '',//字符串格式的字段权限
	uploadBtnText:'上传附件',//上传文件按钮显示的文字
	hideDisalbedInput:true,//默认隐藏禁用的文本框,把值显示的标签中
	childrenInitParams:{
		addRowToClearValue:true,//默认添加行后清除内容
		afterAddRow:function(tableName,addTr){},//添加行后的回调方法,tableName:子表名称,addTr:添加的行
		afterRemoveRow:function(tableName,delTr){}//删除行后的回调方法:tableName:子表名称,delTr:删除的行
	},//执行子表操作时的一些参数
	children:{
		/**qrqcReportItems:{
			entityClass:"com.ambition.improve.entity.QrqcReportItem"//子表实体全路径
		}*/
	},//绑定的子表
	beforeSaveCallback:function(){}//保存之前的回调方法
};
function $initForm(initParams){
	initParams = initParams||{};
	$.extend(_ambWorkflowFormObj,initParams);
	if(!_ambWorkflowFormObj.formId){
		alert("workflow-ofrm.js $initForm方法提示:表单ID不能为空!");
		return;
	}
	//如果有objId,判断是否存在
	if(_ambWorkflowFormObj.objId&&$("#"+_ambWorkflowFormObj.formId+" #id").length==0){
		$("#" + _ambWorkflowFormObj.formId).append('<input type="hidden" name="id" id="id" value="'+_ambWorkflowFormObj.objId+'" />');
	}
	//如果有taskId,判断是否存在
	if(_ambWorkflowFormObj.taskId&&$("#"+_ambWorkflowFormObj.formId+" #taskId").length==0){
		$("#" + _ambWorkflowFormObj.formId).append('<input type="hidden" name="taskId" id="taskId" value="'+_ambWorkflowFormObj.taskId+'" />');
	}
	//如果有inputformortaskform,判断是否存在
	if(_ambWorkflowFormObj.inputformortaskform&&$("#"+_ambWorkflowFormObj.formId+" #inputformortaskform").length==0){
		$("#" + _ambWorkflowFormObj.formId).append('<input type="hidden" name="inputformortaskform" id="inputformortaskform" value="'+_ambWorkflowFormObj.inputformortaskform+'"/>');
	}
	//初始化子表信息
	if(_ambWorkflowFormObj.children){
		var children = _ambWorkflowFormObj.children;
		var tableIndex = 0;
		for(var pro in children){
			var tableParams = children[pro];
			tableParams['inputNamePrefix'] = "a" + tableIndex++;
			_initChildTableInfo(pro,children[pro]);
		}
	}
	if(!_ambWorkflowFormObj.isView){
		//初始化表单中有元素
		_initInputForScope($("#"+_ambWorkflowFormObj.formId));
		//添加验证
		_validateForm();
	}else{
		$("#" + _ambWorkflowFormObj.formId + " :input[name]").attr("disabled","disabled");
	}
	//把禁用的字段隐藏并在标签中显示内容
	if(_ambWorkflowFormObj.hideDisalbedInput){
		$("#" + _ambWorkflowFormObj.formId + " :input[disabled]").each(function(index,obj){
			var $obj = $(obj);
			var type = $obj.attr("type");
			if(type == 'hidden'){
				return;
			}
			if(type=='radio'||type=='checkbox'){//单选框和复选框不处理
				/**if($obj.is(":checked")){
					var val = obj.value;
					$obj.after("<label>" + val + "</label>");
				}*/
			}else{
				if(type!="select-multiple"){
					$obj.hide();
					var val = obj.value;
					$obj.after("<label>" + val + "</label>");
				}
				
			}
			$obj.closest("td").css("background","white").find("a.small-button-bg").hide();
		});
	}
}
//初始化范围内的元素
function _initInputForScope(scopeObj){
	//初始化日期输入框
	$(scopeObj).find(":input[isDate]").datepicker({changeYear:true,changeMonth:true,showButtonPanel: true});
	//初始化时间输入框
	$(scopeObj).find(":input[isDateTime]").datetimepicker({changeYear:true,changeMonth:true,showButtonPanel: true});
	//初始化部门输入框
	_initDeptSelect(scopeObj);
	//初始化用户输入框
	_initUserSelect(scopeObj);
	//初始化客户选择框
	_initCustomerSelect(scopeObj);
	//初始化物料选择框
	_initMaterialSelect(scopeObj);
	//初始化文件选择框
	_initAttachmentFiles(scopeObj);
}
//初始化子表信息
function _initChildTableInfo(tableName,tableParams){
	
	var maxIndex = 0;
	var flags = ',';
	$("tr." + tableName).each(function(index,obj){
		maxIndex = index+1;
		var flag = tableParams['inputNamePrefix'] + maxIndex;
		flags +=flag+",";
		$(obj)
			.attr("tableName",tableName)
			.attr("trPrefix",flag)
			.find("td[rn]").html(maxIndex);
		$(obj).find(":input[name]").each(function(inputIndex,input){
			$input = $(input);
			var id = flag + "_" + input.name;
			$input.attr("fieldName",input.name)
				  .attr("name",id)
				  .attr("id",id);
			var hiddenInputName = $input.attr("hiddenInputName");
			if(hiddenInputName){
				$input.attr("hiddenInputId",flag + "_" + hiddenInputName);
			}
			var showInputName = $input.attr("showInputName");
			if(showInputName){
				$input.attr("showInputId",flag + "_" + showInputName);
			}
		});
		tableParams['flags'] = flags;
		tableParams['maxIndex'] = maxIndex;
	});
}
//添加行
function addRowHtml(aObj){
	var $tr = $(aObj).closest("tr");
	var clonetr = $tr.clone(false); 
	$tr.after(clonetr);
	var tableName = $tr.attr("tableName");
	var tableParams = _ambWorkflowFormObj.children[tableName];
	var maxIndex = tableParams['maxIndex']+1;
	tableParams['maxIndex'] = maxIndex;
	var flag = tableParams['inputNamePrefix'] + maxIndex;
	clonetr.attr("trPrefix",flag);
	//重置对象
	clonetr
		.find(":input[fieldName]")
		.unbind()
		.removeClass("hasDatepicker")
		.each(function(index ,input){
			$input = $(input);
			//清除值
			if(_ambWorkflowFormObj.childrenInitParams.addRowToClearValue){
				$input.val("");
				if($input.attr("title")!=""){
					$input.attr("title","");
				}
			}
			var id = flag + "_" + $input.attr("fieldName");
			$input.attr("name",id)
				  .attr("id",id);
			var hiddenInputName = $input.attr("hiddenInputName");
			if(hiddenInputName){
				$input.attr("hiddenInputId",flag + "_" + hiddenInputName);
			}
			var showInputName = $input.attr("showInputName");
			if(showInputName){
				$input.attr("showInputId",flag + "_" + showInputName);
			}
	});
	//移除自动添加的对象
	clonetr.find("[autoAppend]").remove();
	clonetr.find(":input[isFile]").removeAttr("showInputId");
	//初始化新增行的输入元素
	_initInputForScope(clonetr);
	//更新序号
	_updateRowNum(tableName);
	//检查是否有回调事件
	if($.isFunction(_ambWorkflowFormObj.childrenInitParams.afterAddRow)){
		_ambWorkflowFormObj.childrenInitParams.afterAddRow(tableName,clonetr);
	}
}
//删除行
function removeRowHtml(aObj){
	var $tr = $(aObj).closest("tr");
	var tableName = $tr.attr("tableName");
	if($("tr." + tableName).length<=1){
		alert("至少保留一行!");
		return;
	}
	$tr.remove();
	//更新序号
	_updateRowNum(tableName);
	//检查是否有回调事件
	if($.isFunction(_ambWorkflowFormObj.childrenInitParams.afterRemoveRow)){
		_ambWorkflowFormObj.childrenInitParams.afterRemoveRow(tableName,$tr);
	}
}
//更新行号
function _updateRowNum(tableName){
	var flags = [];
	$("tr." + tableName).each(function(index,obj){
		$(obj).find("td[rn]").html(index+1);
		flags.push($(obj).attr("trPrefix"));
	});
	var str = flags.join(",");
	if(_ambWorkflowFormObj.children&&_ambWorkflowFormObj.children[tableName]){
		_ambWorkflowFormObj.children[tableName]["flags"] = str?("," + str + ","):",";
	}
}
//初始化上传文件的文本框
function _initAttachmentFiles(scopeObj){
	$(scopeObj).find(":input[name][isFile]").each(function(index,obj){
		_initAttachmentFilesForObj(obj);
	});
}
function _initAttachmentFilesForObj(obj){
	var $obj = $(obj);
	var hiddenInputId = obj.id;
	if(!hiddenInputId){
		hiddenInputId = obj.name;
		$obj.attr("id",hiddenInputId);
	}
	var showInputId = $obj.attr("showInputId");
	if(!showInputId){
		showInputId = "show" + obj.name;
		$obj.attr("showInputId",showInputId)
			.after("<span autoAppend=true id='"+showInputId+"'></span>");
	};
	$.parseDownloadPath({
		showInputId : showInputId,
		hiddenInputId : hiddenInputId
	});
	//如果仅显示时不执行后续操作
	if(_ambWorkflowFormObj.isView){
		return;
	}
	//历史文件隐藏域,后台保存时比对
	$obj.after('<input autoAppend=true type="hidden" name="his'+obj.name+'" value="'+obj.value+'"></input>');
	//上传按钮
	var uploadBtnText = $obj.attr("uploadBtnText");
	if(!uploadBtnText){
		uploadBtnText = _ambWorkflowFormObj.uploadBtnText;
	}
	$obj.after('<button autoAppend=true class="btn" type="button" onclick="_uploadFiles(\''+showInputId+'\',\''+hiddenInputId+'\');"><span><span><b class="btn-icons btn-icons-upload"></b>'+uploadBtnText+'</span></span></button>');
}
function _uploadFiles(showInputId,hiddenInputId){
	$.upload({
		appendTo : "#" + _ambWorkflowFormObj.formId,
		showInputId : showInputId,
		hiddenInputId : hiddenInputId
	});
}
//初始化部门选择
function _initDeptSelect(scopeObj){
	$(scopeObj).find(":input[name][isDept]").each(function(index,obj){
		_initDeptSelectForObj(obj);
	});
}
function _initDeptSelectForObj(obj){
	var $obj = $(obj);
	var showInputId = obj.id;
	if(!showInputId){
		$obj.attr("id",obj.name);
		showInputId = obj.name;
	}
	//判断如果有指定隐藏域,则
	var hiddenInputId = $obj.attr("hiddenInputId");
	if(hiddenInputId){
		if($("#" + hiddenInputId).length==0){
			alert("workflow-ofrm.js _initDeptSelect方法提示:初始化部门时name="+obj.name+"指定的hiddenInputId不存在!");
			return;
		}
	}else{
		hiddenInputId = showInputId;
	}
	var treeValue = $obj.attr("treeValue");
	if(!treeValue){
		treeValue = "id";
	}
	var multiple = $obj.attr("multiple");
	if(!multiple){
		multiple = 'false';
	}
	$obj.attr("readonly","readonly").click(function(){
		_selectDept(showInputId,hiddenInputId,treeValue);
    }).after('<a class="small-button-bg" autoAppend=true id="'+obj.name+'a" style="margin-left:2px;float:left;" onclick="_selectDept(\''+showInputId+'\',\''+hiddenInputId+'\',\''+treeValue+'\',\''+multiple+'\')" href="javascript:void(0);" title="选择部门"><span class="ui-icon ui-icon-search" style="cursor:pointer;"></span></a>');
}
//选择部门
function _selectDept(showInputId,hiddenInputId,treeValue,multiple){
	if(!_ambWorkflowFormObj.webBaseUrl){
		alert("workflow-ofrm.js _selectDept方法提示:初始化选择部门时项目地址未指定!");
		return;
	}
	var acsSystemUrl = _ambWorkflowFormObj.webBaseUrl;
//	popTree({ title :'选择部门',
//		innerWidth:'400',
//		treeType:'DEPARTMENT_TREE',
//		defaultTreeValue:treeValue,
//		leafPage:'false',
//		multiple:'false',
//		hiddenInputId:hiddenInputId,
//		showInputId:showInputId,
//		acsSystemUrl:acsSystemUrl,
//		callBack:function(){}
//	});
	var data ={
			treeNodeData: "id,name,shortTitle",
//			chkStyle:"checkbox",
		chkboxType:"{'Y' : 'ps', 'N' : 'ps' }",
		departmentShow:''
	};
	if(multiple){
		data ={
			treeNodeData: "id,name,shortTitle",
			chkStyle:"checkbox",
			chkboxType:"{'Y' : 'ps', 'N' : 'ps' }",
			departmentShow:''
		};
	}
	var zTreeSetting={
			leaf: {
				enable: false,
				multiLeafJson:  "[{'name':'部门树','type':'DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"code\":\"code\",\"shortTitle\":\"shortTitle\"}','showValue':'{\"name\":\"name\"}'}]"
			},
			type: {
				treeType: "DEPARTMENT_TREE",
				showContent:"[{'id':'id','code':'code','name':'name'}]",
				noDeparmentUser:false,
				onlineVisible:true
			},
			
			data: data,
			view: {
				title: "部门树",
				width: 300,
				height:400,
				url:webRoot
			},
			feedback:{
				enable: true,
		                showInput:"showInput", 
		                showThing:"{'name':'name'}",
		                hiddenInput:"hiddenInput",
		                hiddenThing:"{'id':'id'}",
		                append:false
			},
			callback: {
				onClose:function(api){
					if(multiple){
						$("#"+showInputId).val(ztree.getDepartmentNames());
					}else{
						$("#"+showInputId).val(ztree.getDepartmentName());
					}
//					$("#"+hiddenInputId).val(ztree.getDepartmentShortTitle().split("-")[1]);
				}
			}		
			};
		    popZtree(zTreeSetting);
}
//初始化用户选择
function _initUserSelect(scopeObj){
	$(scopeObj).find(":input[name][isUser]").each(function(index,obj){
		_initUserSelectForObj(obj);
	});
}
function _initUserSelectForObj(obj){
	var $obj = $(obj);
	var showInputId = obj.id;
	if(!showInputId){
		$obj.attr("id",obj.name);
		showInputId = obj.name;
	}
	//判断如果有指定隐藏域,则
	var hiddenInputId = $obj.attr("hiddenInputId");
	if(hiddenInputId){
		if($("#" + hiddenInputId).length==0){
			alert("workflow-ofrm.js _initDeptSelect方法提示:初始化用户时name="+obj.name+"指定的hiddenInputId不存在!");
			return;
		}
	}else{
		hiddenInputId = showInputId;
	}
	var treeValue = $obj.attr("treeValue");
	if(!treeValue){
		treeValue = "loginName";
	}
	var multiple = $obj.attr("multiple");
	if(!multiple){
		multiple = 'false';
	}
	$obj.attr("readonly","readonly").click(function(){
		_selectUser(showInputId,hiddenInputId,treeValue,multiple);
    }).after('<a class="small-button-bg" autoAppend="true" id="'+obj.name+'a" style="margin-left:2px;float:left;" onclick="_selectUser(\''+showInputId+'\',\''+hiddenInputId+'\',\''+treeValue+'\',\''+multiple+'\')" href="javascript:void(0);" title="选择人员"><span class="ui-icon ui-icon-search" style="cursor:pointer;"></span></a>');
}
//选择用户
function _selectUser(showInputId,hiddenInputId,treeValue,multiple){
	if(!_ambWorkflowFormObj.webBaseUrl){
		alert("workflow-ofrm.js _selectUser方法提示:初始化选择用户时项目地址未指定!");
		return;
	}
//	var acsSystemUrl = _ambWorkflowFormObj.webBaseUrl;
//	popTree({ title :'选择人员',
//		innerWidth:'400',
//		treeType:'MAN_DEPARTMENT_TREE',
//		defaultTreeValue:treeValue,
//		leafPage:'false',
//		multiple:multiple=='true'?'true':'false',
//		hiddenInputId:hiddenInputId,
//		showInputId:showInputId,
//		acsSystemUrl:acsSystemUrl,
//		callBack:function(){}});
	var data = {
			treeNodeData: "loginName,name",
			chkboxType:"{'Y' : 'ps', 'N' : 'ps' }",
			departmentShow:''
		};
	if(multiple=="true"){
		data = {
				treeNodeData: "loginName,name",
				chkStyle:"checkbox",
				chkboxType:"{'Y' : 'ps', 'N' : 'ps' }",
				departmentShow:''
			}
	}
	var zTreeSetting={
			leaf: {
				enable: false,
				multiLeafJson:  "[{'name':'用户树','type':'MAN_DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"loginName\":\"loginName\",\"code\":\"code\",\"cardNumber\":\"cardNumber\"}','showValue':'{\"name\":\"name\"}'}]"
			},
			type: {
				treeType: "MAN_DEPARTMENT_TREE",
				showContent:"[{'id':'id','code':'code','name':'name'}]",
				noDeparmentUser:false,
				onlineVisible:true
			},
			data: data,
			view: {
				title: "用户树",
				width: 300,
				height:400,
				url:webRoot
			},
			feedback:{
				enable: true,
		                showInput:"showInput", 
		                showThing:"{'name':'name'}",
		                hiddenInput:"hiddenInput",
		                hiddenThing:"{'loginName':'loginName'}",
		                append:false
			},
			callback: {
				onClose:function(api){
					if(multiple=="true"){
						$("#"+showInputId).val(ztree.getNames());
						$("#"+hiddenInputId).val(ztree.getLoginNames());
					}else{
						$("#"+showInputId).val(ztree.getName());
						$("#"+hiddenInputId).val(ztree.getLoginName());
					}
				}
			}		
			};
		    popZtree(zTreeSetting);
}
//初始化客户选择
function _initCustomerSelect(scopeObj){
	$(scopeObj).find(":input[name][isCustomer]").each(function(index,obj){
		_initCustomerSelectForObj(obj);
	});
}
function _initCustomerSelectForObj(obj){
	var $obj = $(obj);
	var showInputId = obj.id;
	if(!showInputId){
		$obj.attr("id",obj.name);
		showInputId = obj.name;
	}
	//判断如果有指定隐藏域,则
	var hiddenInputId = $obj.attr("hiddenInputId");
	if(hiddenInputId){
		if($("#" + hiddenInputId).length==0){
			alert("workflow-ofrm.js _initCustomerSelect方法提示:初始化选择客户时name="+obj.name+"指定的hiddenInputId不存在!");
			return;
		}
	}else{
		hiddenInputId = showInputId;
	}
	$obj.attr("readonly","readonly")./**click(function(){
		_selectCustomer(showInputId,hiddenInputId);
    }).*/after('<a class="small-button-bg" autoAppend=true id="'+obj.name+'a" style="margin-left:2px;float:left;" onclick="_selectCustomer(\''+showInputId+'\',\''+hiddenInputId+'\')" href="javascript:void(0);" title="选择客户"><span class="ui-icon ui-icon-search" style="cursor:pointer;"></span></a>');
}
//选择客户
function _selectCustomer(showInputId,hiddenInputId){
	if(!_ambWorkflowFormObj.webBaseUrl){
		alert("workflow-ofrm.js _selectCustomer方法提示:初始化选择客户时项目地址未指定!");
		return;
	}
	_ambWorkflowFormObj.lastCustomerShowInputId=showInputId;
	_ambWorkflowFormObj.lastCustomerHiddenInputId=hiddenInputId;
	var selectCustomerUrl = _ambWorkflowFormObj.webBaseUrl + "/market/customer/select-customer.htm";
	$.colorbox({href:selectCustomerUrl,iframe:true,
		innerWidth:$(window).width()<900?$(window).width()-50:900, 
		innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
		overlayClose:false,
		title:"选择客户"
	});
}
function setCustmerValue(objs){
	var obj = objs[0];
	$("#"+_ambWorkflowFormObj.lastCustomerHiddenInputId).val(obj.code);
	$("#"+_ambWorkflowFormObj.lastCustomerShowInputId).val(obj.name);
}
//初始化物料选择
function _initMaterialSelect(scopeObj){
	$(scopeObj).find(":input[name][isMaterial]").each(function(index,obj){
		_initMaterialSelectForObj(obj);
	});
}
function _initMaterialSelectForObj(obj){
	var $obj = $(obj);
	var showInputId = $obj.attr("showInputId");
	if(!showInputId){
		showInputId = obj.id;
		if(!showInputId){
			$obj.attr("id",obj.name);
			showInputId = obj.name;
		}
	}else{
		if($("#" + showInputId).length==0){
			alert("workflow-ofrm.js _initMaterialSelect方法提示:初始化选择物料时name="+obj.name+"指定的showInputId不存在!");
			return;
		}
	}
	//判断如果有指定隐藏域,则
	var hiddenInputId = $obj.attr("hiddenInputId");
	if(hiddenInputId){
		if($("#" + hiddenInputId).length==0){
			alert("workflow-ofrm.js _initMaterialSelect方法提示:初始化选择物料时name="+obj.name+"指定的hiddenInputId不存在!");
			return;
		}
	}else{
		hiddenInputId = showInputId;
	}
	$obj./**attr("readonly","readonly")./**click(function(){
		_selectCustomer(showInputId,hiddenInputId);
    }).*/after('<a class="small-button-bg" autoAppend=true id="'+obj.name+'a" style="margin-left:2px;float:left;" onclick="_selectMaterial(\''+showInputId+'\',\''+hiddenInputId+'\')" href="javascript:void(0);" title="选择物料"><span class="ui-icon ui-icon-search" style="cursor:pointer;"></span></a>');
}
//选择物料
function _selectMaterial(showInputId,hiddenInputId){
	if(!_ambWorkflowFormObj.webBaseUrl){
		alert("workflow-ofrm.js _selectMaterial方法提示:初始化选择物料时项目地址未指定!");
		return;
	}
	_ambWorkflowFormObj.lastMaterialShowInputId=showInputId;
	_ambWorkflowFormObj.lastMaterialHiddenInputId=hiddenInputId;
	var selectMaterialUrl = _ambWorkflowFormObj.webBaseUrl + "/carmfg/common/product-bom-select.htm";
	$.colorbox({href:selectMaterialUrl,iframe:true,
		innerWidth:$(window).width()<900?$(window).width()-50:900, 
		innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
		overlayClose:false,
		title:"选择物料"
	});
}
function setFullBomValue(objs){
	var obj = objs[0];
	$("#" + _ambWorkflowFormObj.lastMaterialHiddenInputId).val(obj.materielName);
	$("#" + _ambWorkflowFormObj.lastMaterialShowInputId).val(obj.materielCode);
}
function callback(){
	if(_ambWorkflowFormObj.fieldPermissionStr){
		addFormValidate(_ambWorkflowFormObj.fieldPermissionStr,_ambWorkflowFormObj.formId);
	}
	showMsg();
}
function _validateForm(){
	if(_ambWorkflowFormObj.fieldPermissionStr){
		addFormValidate(_ambWorkflowFormObj.fieldPermissionStr,_ambWorkflowFormObj.formId);
		var fieldPermission =eval(_ambWorkflowFormObj.fieldPermissionStr);
		for(var i=0;i<fieldPermission.length;i++){
			var obj=fieldPermission[i];
			if(obj.readonly=='true'){
				//判断是否子表
				if(_ambWorkflowFormObj.children
						&&_ambWorkflowFormObj.children[obj.name]){
					$("tr." + obj.name).find(":input[name]").attr("disabled","disabled");
					$("tr." + obj.name).find("button").removeAttr("onclick");
					$("tr." + obj.name).find(".small-button-bg").removeAttr("onclick");
					//添加只读的标签
					_ambWorkflowFormObj.children[obj.name]['readonly'] = true;
				}else{
					$(":input[name="+obj.name+"]").attr("disabled","disabled");
					$("#"+obj.name+"a").removeAttr("onclick");
				}
			};
			if(obj.controlType=="allReadolny"){
				$(":input[name]","#" + _ambWorkflowFormObj.formId).each(function(index,obj){
					$(obj).attr("disabled","disabled");
				});
				$(".small-button-bg","#" + _ambWorkflowFormObj.formId).each(function(index,obj){
					$(obj).removeAttr("onclick");
				});
				$("button","#"+_ambWorkflowFormObj.formId).each(function(index,obj){
					$(obj).removeAttr("onclick");
				});
			};
		};
	}
}

//保存form
function _beforeSaveFormInfo(taskTransact){
	if(_ambWorkflowFormObj.beforeSaveCallback&&$.isFunction(_ambWorkflowFormObj.beforeSaveCallback)){
		var res = _ambWorkflowFormObj.beforeSaveCallback();
		if(res==false){
			return false;
		}
	}
	//检查是否需要验证,默认不同意不验证
	var needValid = true;
	if(taskTransact=='REFUSE'&&!_ambWorkflowFormObj.refuseValid){
		needValid = false;
	}
	if(!taskTransact&&!_ambWorkflowFormObj.saveValid){
		needValid = false;
	}
	if(needValid){
		if(!$("#"+_ambWorkflowFormObj.formId).valid()){
			var error = $("#"+_ambWorkflowFormObj.formId).validate().errorList[0];
			$.showMessage(error.message);
			$(error.element).focus();
			return false;
		}
	}
	//检查保存子表的数据
	if(_ambWorkflowFormObj.children){
		var tables = [];
		for(var pro in _ambWorkflowFormObj.children){
			var tableParams = _ambWorkflowFormObj.children[pro];
			//只读时跳过不处理参数
			if(tableParams['readonly']==true){
				continue;
			}
			var str = '{"fieldName":"' + pro + '","flags":"' + tableParams["flags"] + '","entityClass":"' + tableParams["entityClass"] + '"}';
			tables.push(str);
		}
		var str = encodeURI("[" + tables.join(",") + "]");
		if($("#"+_ambWorkflowFormObj.formId+" #_childrenInfos").length==0){
			$("#" + _ambWorkflowFormObj.formId).append('<input type="hidden" name="_childrenInfos" id="_childrenInfos" value="'+str+'" />');
		}else{
			$("#"+_ambWorkflowFormObj.formId+" #_childrenInfos").val(str);
		}
	}
}
//完成任务
function _completeTask(taskTransact) {
	var res = _beforeSaveFormInfo(taskTransact);
	if(res==false){
		return;
	}
	$.showMessage("正在执行操作,请稍候... ...","custom");
	$("#opt-content").scrollTop(0);
	//检查元素是否存在
	if($("#"+_ambWorkflowFormObj.formId + " #taskTransact").length==0){
		$("#"+_ambWorkflowFormObj.formId).append('<input type="hidden" name="taskTransact" id="taskTransact" />');
	}
	$("#"+_ambWorkflowFormObj.formId + " #taskTransact").val(taskTransact);
	$("#"+_ambWorkflowFormObj.formId+" :input[name]").removeAttr("disabled");
	$("#"+_ambWorkflowFormObj.formId).attr("action",_ambWorkflowFormObj.actionBaseUrl + "/complete-task.htm");
	$(".opt-btn").find("button.btn").attr("disabled",true);
	var validator = $("#"+_ambWorkflowFormObj.formId).data('validator');
	if(validator){
		validator.cancelSubmit = true;
	}
	$('#'+_ambWorkflowFormObj.formId).submit();
//	window.location.href=_ambWorkflowFormObj.actionBaseUrl + "/input.htm?id="+$("#id").val();
}
//保存
function saveForm() {
	var res = _beforeSaveFormInfo();
	if(res==false){
		return;
	}
	$.showMessage("正在保存,请稍候... ...","custom");
	$("#opt-content").scrollTop(0);
	$("#"+_ambWorkflowFormObj.formId + " :input[name]").removeAttr("disabled");
	$("#"+_ambWorkflowFormObj.formId).attr("action",_ambWorkflowFormObj.actionBaseUrl + "/save.htm");
	//检查元素是否存在
	if($("#"+_ambWorkflowFormObj.formId+" #saveTaskFlag").length==0){
		$("#"+_ambWorkflowFormObj.formId).append('<input type="hidden" name="saveTaskFlag" id="saveTaskFlag" />');
	}
	$("#"+_ambWorkflowFormObj.formId+" #saveTaskFlag").attr("value","true");
	$(".opt-btn").find("button.btn").attr("disabled",true);
	var validator = $("#"+_ambWorkflowFormObj.formId).data('validator');
	if(validator){
		validator.cancelSubmit = true;
	}
	$('#'+_ambWorkflowFormObj.formId).submit();
}
///保存弹出窗口，关闭窗口
function saveFormBox() {
	var res = _beforeSaveFormInfo();
	if(res==false){
		return;
	}
	$.showMessage("正在保存,请稍候... ...","custom");
	$("#opt-content").scrollTop(0);
	$("#"+_ambWorkflowFormObj.formId + " :input[name]").removeAttr("disabled");
	$("#"+_ambWorkflowFormObj.formId).attr("action",_ambWorkflowFormObj.actionBaseUrl + "/save.htm");
	//检查元素是否存在
	if($("#"+_ambWorkflowFormObj.formId+" #saveTaskFlag").length==0){
		$("#"+_ambWorkflowFormObj.formId).append('<input type="hidden" name="saveTaskFlag" id="saveTaskFlag" />');
	}
	$("#"+_ambWorkflowFormObj.formId+" #saveTaskFlag").attr("value","true");
	$(".opt-btn").find("button.btn").attr("disabled",true);
	$('#'+_ambWorkflowFormObj.formId).submit();
	window.parent.$.colorbox.close();
	window.parent.$("#list").trigger("reloadGrid");
}
//流转历史和表单信息切换
function changeViewSet(opt,id){
	if (opt == "history") {
		$("#tabs-2").load(_ambWorkflowFormObj.actionBaseUrl + "/showhistory.htm?id="+id,function(){
			$("#tabs-2").height($(window).height()-115);
			
		});
	};
}

//办理完任务关闭窗口前执行
function beforeCloseWindow(opt){
	//aa.submit("defaultForm1", "${improvectx}/correction-precaution/correctionprecaution-task.htm", 'btnZone,viewZone');
}

//选择加签人员
function _addTask(){
	//检查元素是否存在
	if($('#'+_ambWorkflowFormObj.formId+' #addSignPerson').length==0){
		$("#"+_ambWorkflowFormObj.formId).append('<input type="hidden" name="addSignPerson" id="addSignPerson" />');
	}
	popTree({ title :'选择加签人员',
		innerWidth:'400',
		treeType:'MAN_DEPARTMENT_TREE',
		defaultTreeValue:'id',
		leafPage:'false',
		multiple:'true',
		hiddenInputId:"addSignPerson",
		acsSystemUrl:_ambWorkflowFormObj.webBaseUrl,
		callBack:function(){_addSignCallBack();}});
}
function _addSignCallBack(){
	if(jstree.getNames().indexOf("全公司")>=0){
		$('#'+_ambWorkflowFormObj.formId+' #addSignPerson').attr("value","all_user");
	}else{
		$('#'+_ambWorkflowFormObj.formId+' #addSignPerson').attr("value",jstree.getLoginNames());
	}
	$("#" + _ambWorkflowFormObj.formId).attr("action",_ambWorkflowFormObj.actionBaseUrl + "/addsigner.htm");
	$("#" + _ambWorkflowFormObj.formId).ajaxSubmit(function (id){
		alert(id);
	});
	//validateForm();
}

//选择减签人员
function _cutTask(){
	custom_tree({url:_ambWorkflowFormObj.actionBaseUrl + '/cutsigntree.htm',
		onsuccess:function(){_removeSignerCallBack();},
		width:500,
		height:400,
		title:'选择环节',
		postData:{taskId:$("#taskId").attr("value")},
		nodeInfo:['id'],
		multiple:true,
		webRoot:_ambWorkflowFormObj.webBaseUrl
	});
}
function _removeSignerCallBack(){
	//检查元素是否存在
	if($('#'+_ambWorkflowFormObj.formId+' #removeSignPerson').length==0){
		$("#"+_ambWorkflowFormObj.formId).append('<input type="hidden" name="removeSignPerson" id="removeSignPerson" />');
	}
	$("#"+_ambWorkflowFormObj.formId+" #removeSignPerson").attr("value",getSelectValue('id'));
	$("#" + _ambWorkflowFormObj.formId).attr("action",_ambWorkflowFormObj.actionBaseUrl + "/removesigner.htm");
	$("#" + _ambWorkflowFormObj.formId).ajaxSubmit(function (id){
		alert(id);
	});
	//validateForm();
}
//保存
workflowButtonGroup.btnStartWorkflow.click = function(taskId){
	saveForm(_ambWorkflowFormObj.actionBaseUrl + '/save.htm?taskId=' + taskId);
};
//第一次提交
workflowButtonGroup.btnSubmitWorkflow.click = function(taskId){
	submitForm();
};
//第一次提交
function submitForm(taskId){
	var res = _beforeSaveFormInfo("SUBMIT");
	if(res==false){
		return;
	}
	//检查元素是否存在
	if($('#'+_ambWorkflowFormObj.formId+' #taskTransact').length==0){
		$("#"+_ambWorkflowFormObj.formId).append('<input type="hidden" name="taskTransact" id="taskTransact" />');
	}
	$('#'+_ambWorkflowFormObj.formId+' #taskTransact').val("SUBMIT");
	$("#" + _ambWorkflowFormObj.formId).attr("action",_ambWorkflowFormObj.actionBaseUrl + '/submit-process.htm');
	$("#opt-content").scrollTop(0);
	$.showMessage("正在提交,请稍候... ...","custom");
	$(".opt-btn").find("button.btn").attr("disabled",true);
	$("#" + _ambWorkflowFormObj.formId + " :input[name]").removeAttr("disabled");
	$("#" + _ambWorkflowFormObj.formId).submit();
}
//提交
workflowButtonGroup.btnSubmitTask.click = function(taskId){
	_completeTask('SUBMIT');
};
//同意
workflowButtonGroup.btnApproveTask.click = function(taskId){
	_completeTask('APPROVE');
};
//不同意
workflowButtonGroup.btnRefuseTask.click = function(taskId){
	_completeTask('REFUSE');
};
//加签
workflowButtonGroup.btnAddCountersign.click = function(taskId){
	_addTask();
};
//减签
workflowButtonGroup.btnDeleteCountersign.click = function(taskId){
	_cutTask();
};

//保存表单元素
workflowButtonGroup.btnSaveForm.click = function(taskId){
	saveForm();
};

//取回
workflowButtonGroup.btnGetBackTask.click = function(taskId){
	if(!confirm("确定要取回吗?")){
		return;
	}
	$("#" + _ambWorkflowFormObj.formId).attr("action",_ambWorkflowFormObj.actionBaseUrl + "/retrievetask.htm?taskId="+taskId);
	$.showMessage("正在取回,请稍候... ...","custom");
	$("#" + _ambWorkflowFormObj.formId).ajaxSubmit(function (id){
		$.showMessage(id);
		if(id=="任务已取回"){
			$.showMessage("正在加载,请稍候... ...",'custom');
			if(_ambWorkflowFormObj.inputformortaskform=='taskform'){
				window.location = _ambWorkflowFormObj.actionBaseUrl + '/process-task.htm?taskId=' + taskId;
			}else{
				window.location = _ambWorkflowFormObj.actionBaseUrl + '/input.htm?taskId=' + taskId;
			}
		}else{
			alert(id);
		}
	});
};

//领取
workflowButtonGroup.btnDrawTask.click = function(taskId){
	if(!confirm("确定要领取吗?")){
		return;
	}
	$.showMessage("正在领取,请稍候... ...","custom");
	aa.submit("defaultForm1", _ambWorkflowFormObj.actionBaseUrl + "/drawtask.htm", 'btnZone',function(){
		window.location.reload();//刷新当前页面
	});
};
//领取回调
function receiveback(){
	$("#message").show("show");
	$.showMessage("正在加载,请稍候... ...",'custom');
	if(_ambWorkflowFormObj.inputformortaskform=='taskform'){
		window.location.href = _ambWorkflowFormObj.actionBaseUrl + "/process-task.htm?id=" + _ambWorkflowFormObj.taskId;
	}else{
		window.location = _ambWorkflowFormObj.actionBaseUrl + '/input.htm?taskId=' + _ambWorkflowFormObj.taskId;
	}
}
//放弃领取
workflowButtonGroup.btnAbandonTask.click = function(taskId){
	if(!confirm("确定要放弃领取吗?")){
		return;
	}
	$("#opt-content").scrollTop(0);
	$.showMessage("正在放弃领取,请稍候... ...","custom");
	aa.submit(_ambWorkflowFormObj.formId, _ambWorkflowFormObj.actionBaseUrl + "/abandonreceive.htm", 'btnZone',function(){
		window.location.reload();
	});
};
//已阅
workflowButtonGroup.btnReadTask.click = function(taskId){
	//检查元素是否存在
	if($('#'+_ambWorkflowFormObj.formId+' #taskTransact').length==0){
		$("#"+_ambWorkflowFormObj.formId).append('<input type="hidden" name="taskTransact" id="taskTransact" />');
	}
	$('#'+_ambWorkflowFormObj.formId+' #taskTransact').val('READED');
	$("#opt-content").scrollTop(0);
	$.showMessage("正在操作,请稍候... ...","custom");
	aa.submit(_ambWorkflowFormObj.formId, _ambWorkflowFormObj.actionBaseUrl + "/complete-task.htm", 'main', _readTaskCallback);
};
//选择环节
workflowButtonGroup.btnChoiceTache.click = function(){
	_completeTask('READED');
};

function _readTaskCallback(){
	$("#message").show("show");
	setTimeout('$("#message").hide("show");',3000);
	window.parent.close();
}

//抄送
workflowButtonGroup.btnCopyTache.click = function(taskId){
	//检查元素是否存在
	if($('#'+_ambWorkflowFormObj.formId+' #copyPerson').length==0){
		$("#"+_ambWorkflowFormObj.formId).append('<input type="hidden" name="copyPerson" id="copyPerson" />');
	}
	/*popTree({ title :'抄送人员',
		innerWidth:'400',
		treeType:'MAN_DEPARTMENT_TREE',
		defaultTreeValue:'id',
		leafPage:'false',
		multiple:'true',
		hiddenInputId:"copyPerson",
		showInputId:"copyPerson",
		acsSystemUrl:_ambWorkflowFormObj.webBaseUrl,
		callBack:function(){_copyPersonCallBack();}});*/
	var data = {
			treeNodeData: "loginName,name",
			chkboxType:"{'Y' : 'ps', 'N' : 'ps' }",
			departmentShow:''
		};
	var multiple = "true";
	if(multiple=="true"){
		data = {
				treeNodeData: "loginName,name",
				chkStyle:"checkbox",
				chkboxType:"{'Y' : 'ps', 'N' : 'ps' }",
				departmentShow:''
			}
	}
	var zTreeSetting={
			leaf: {
				enable: false,
				multiLeafJson:  "[{'name':'用户树','type':'MAN_DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"loginName\":\"loginName\",\"code\":\"code\",\"cardNumber\":\"cardNumber\"}','showValue':'{\"name\":\"name\"}'}]"
			},
			type: {
				treeType: "MAN_DEPARTMENT_TREE",
				showContent:"[{'id':'id','code':'code','name':'name'}]",
				noDeparmentUser:false,
				onlineVisible:true
			},
			data: data,
			view: {
				title: "用户树",
				width: 300,
				height:400,
				url:webRoot
			},
			feedback:{
				enable: true,
		                showInput:"showInput", 
		                showThing:"{'name':'name'}",
		                hiddenInput:"hiddenInput",
		                hiddenThing:"{'loginName':'loginName'}",
		                append:false
			},
			callback: {
				onClose:function(api){
					/*if(multiple=="true"){
						$("#"+showInputId).val(ztree.getNames());
						$("#"+hiddenInputId).val(ztree.getLoginNames());
					}else{
						$("#"+showInputId).val(ztree.getName());
						$("#"+hiddenInputId).val(ztree.getLoginName());
					}*/
					$('#'+_ambWorkflowFormObj.formId+' #copyPerson').attr("value",ztree.getLoginName());
					$("#" + _ambWorkflowFormObj.formId).attr("action",_ambWorkflowFormObj.actionBaseUrl + "/copytask.htm");
					$("#opt-content").scrollTop(0);
					$.showMessage("正在抄送,请稍候... ...",'custom');
					$("#" + _ambWorkflowFormObj.formId).ajaxSubmit(function (id){
						$.clearMessage();
						alert(id);
					}); 
				}
			}		
			};
		    popZtree(zTreeSetting);
};

function _copyPersonCallBack(){
	if(jstree.getNames().indexOf("全公司")>=0){
		$('#'+_ambWorkflowFormObj.formId+' #copyPerson').attr("value","all_user");
		if(!confirm("您选择抄送给了全公司,确定要继续吗?")){
			return;
		}
	}else{
		$('#'+_ambWorkflowFormObj.formId+' #copyPerson').attr("value",jstree.getLoginNames());
	}
	$("#" + _ambWorkflowFormObj.formId).attr("action",_ambWorkflowFormObj.actionBaseUrl + "/copytask.htm");
	$("#opt-content").scrollTop(0);
	$.showMessage("正在抄送,请稍候... ...",'custom');
	$("#" + _ambWorkflowFormObj.formId).ajaxSubmit(function (id){
		$.clearMessage();
		alert(id);
	}); 
}
function gobackTask(){
	$.showMessage("正在操作,请稍候... ...","custom");
	$("#" + _ambWorkflowFormObj.formId).attr("action",_ambWorkflowFormObj.actionBaseUrl + "/goback.htm?");
	$("#" + _ambWorkflowFormObj.formId).ajaxSubmit(function (id){
		alert(id);
		//_changeViewSet("history","${id}");
		window.parent.close();
	});
};
//指派
workflowButtonGroup.btnAssign.click = function(taskId){
	//检查元素是否存在
	if($('#'+_ambWorkflowFormObj.formId+' #assignee').length==0){
		$("#"+_ambWorkflowFormObj.formId).append('<input type="hidden" name="assignee" id="assignee" />');
	}
	/*popTree({ title :'指派人员',
		innerWidth:'400',
		treeType:'MAN_DEPARTMENT_TREE',
		defaultTreeValue:'loginName',
		leafPage:'false',
		multiple:'true',
		hiddenInputId:"assignee",
		showInputId:"assignee",
		acsSystemUrl:_ambWorkflowFormObj.webBaseUrl,
		callBack:function(){_assignPersonCallBack();}});*/
	var data = {
			treeNodeData: "loginName,name",
			chkboxType:"{'Y' : 'ps', 'N' : 'ps' }",
			departmentShow:''
		};
	var multiple = "true";
	if(multiple=="true"){
		data = {
				treeNodeData: "loginName,name",
				chkStyle:"checkbox",
				chkboxType:"{'Y' : 'ps', 'N' : 'ps' }",
				departmentShow:''
			}
	}
	var zTreeSetting={
			leaf: {
				enable: false,
				multiLeafJson:  "[{'name':'用户树','type':'MAN_DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"loginName\":\"loginName\",\"code\":\"code\",\"cardNumber\":\"cardNumber\"}','showValue':'{\"name\":\"name\"}'}]"
			},
			type: {
				treeType: "MAN_DEPARTMENT_TREE",
				showContent:"[{'id':'id','code':'code','name':'name'}]",
				noDeparmentUser:false,
				onlineVisible:true
			},
			data: data,
			view: {
				title: "用户树",
				width: 300,
				height:400,
				url:webRoot
			},
			feedback:{
				enable: true,
		                showInput:"showInput", 
		                showThing:"{'name':'name'}",
		                hiddenInput:"hiddenInput",
		                hiddenThing:"{'loginName':'loginName'}",
		                append:false
			},
			callback: {
				onClose:function(api){
					$('#'+_ambWorkflowFormObj.formId+' #assignee').attr("value",ztree.getLoginNames());
					$("#" + _ambWorkflowFormObj.formId).attr("action",_ambWorkflowFormObj.actionBaseUrl + "/assign.htm");
					$("#opt-content").scrollTop(0);
					$.showMessage("正在执行操作,请稍候... ...",'custom');
					$("#" + _ambWorkflowFormObj.formId).ajaxSubmit(function (id){
						$.clearMessage();
						alert(id);
						if(_ambWorkflowFormObj.inputformortaskform=='taskform'){
							window.parent.close();
						}else{
							$.showMessage("正在加载,请稍候... ...",'custom');
							window.location = _ambWorkflowFormObj.actionBaseUrl + '/input.htm?id=' + _ambWorkflowFormObj.objId;
						}
					});
				}
			}		
			};
		    popZtree(zTreeSetting);
};
//指派回调
function _assignPersonCallBack(){
	$('#'+_ambWorkflowFormObj.formId+' #assignee').attr("value",jstree.getLoginNames());
	$("#" + _ambWorkflowFormObj.formId).attr("action",_ambWorkflowFormObj.actionBaseUrl + "/assign.htm");
	$("#opt-content").scrollTop(0);
	$.showMessage("正在执行操作,请稍候... ...",'custom');
	$("#" + _ambWorkflowFormObj.formId).ajaxSubmit(function (id){
		$.clearMessage();
		alert(id);
		if(_ambWorkflowFormObj.inputformortaskform=='taskform'){
			window.parent.close();
		}else{
			$.showMessage("正在加载,请稍候... ...",'custom');
			window.location = _ambWorkflowFormObj.actionBaseUrl + '/input.htm?id=' + _ambWorkflowFormObj.objId;
		}
	});
}
//不确定使用方法
function dealResult(id){
	if(id!=""&&typeof id!='undefined'&&id!=null){
		var ids=id.split(";");
		$("#id").attr("value",ids[0]);
		if(ids[1]!=undefined&&ids[1]!=null&&ids[1]!=''){
// 		根据后台返回id判断执行操作，这里代码根据需求写
		}else{
			$("#message").show("show");
			setTimeout('$("#message").hide("show");',3000);
		}
	}
}
function viewFormInfo(){
	if(!_ambWorkflowFormObj.objId){
		alert("初始化时未指定对象ID!");
		return;
	}
	$.colorbox({href:_ambWorkflowFormObj.actionBaseUrl + '/view-info.htm?id='+_ambWorkflowFormObj.objId+"#tabs-2",iframe:true,
		innerWidth:$(window).width()<1000?$(window).width()-50:$(window).width()*0.8, 
		innerHeight:$(window).height()<600?$(window).height()-50:$(window).height()*0.8,
		overlayClose:false,
		title:"流转历史"
	});
}
//导出
function exportForm(){
	if(!_ambWorkflowFormObj.objId){
		alert("workflow-ofrm.js input_export方法提示:对象ID不能为空!");
		return;
	}
	window.location = _ambWorkflowFormObj.actionBaseUrl + '/export-excel-report.htm?id=' + _ambWorkflowFormObj.objId;
}

function exportForm1(){
	if(!_ambWorkflowFormObj.objId){
		alert("workflow-ofrm.js input_export方法提示:对象ID不能为空!");
		return;
	}
	window.location = _ambWorkflowFormObj.actionBaseUrl + '/export-excel-report-en.htm?id=' + _ambWorkflowFormObj.objId;
}
