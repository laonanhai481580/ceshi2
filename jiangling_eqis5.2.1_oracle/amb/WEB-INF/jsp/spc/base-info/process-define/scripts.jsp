<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${ctx}/widgets/spin/jquery-spin.js"></script>
<script type="text/javascript">
	var initiated = {
		'0' : true,
		'1' : false,
		'2' : false,
		'3' : false,
		'4' : false
	};
	var id='${id}', idIndex = 0, processId = '', featureId='', processName = '', gridHeight = 0, gridWidth=0;
	$(document).ready(function(){
		$("#tabs").tabs({
			show: function(event, ui) {
				if(!initiated[ui.index]){
					initiated[ui.index] = true;
					if(ui.index==2){
						createJudgeGrid();
					}else if(ui.index==3){
						createLevelGrid();
					}else if(ui.index==4){
						createPersonGrid();
					}
				}
			}
		}).height($(document).height()-30);
		createFeatureTree(processId,processName);
		$("#qualityFeature").height($(".ui-layout-center").height()-48);
		$.spin.imageBasePath='${ctx}/widgets/spin/img/spin1/';
		$("#rangeInterval").spin({
			max:100,
			min:1
		});
		$("#precs").spin({
			max:100,
			min:1
		});
		gridHeight = $(document).height()-230;
		gridWidth = $(document).width()-document.getElementById("top").rows[0].cells[0].offsetWidth-document.getElementById("top").rows[0].cells[1].offsetWidth-200;
		//绑定规格类型改变事件 
		$(":input[name=specificationType]").change(function(){
			specificationTypeChange();
		});
	});
	//规格类型变化
	function specificationTypeChange(){
		var type = $(":input[name=specificationType]:checked").val();
		$("#add").removeAttr("disabled");//规格上限
		$("#del").removeAttr("disabled");///规格下限
		$("#upperLimit").removeAttr("disabled");//合理上限
		$("#lowerLimit").removeAttr("disabled");//合理下限
		if(type=='single-u'){//单侧上公差
			$("#del").attr("disabled","disabled");
			$("#lowerLimit").attr("disabled","disabled");
		}else if(type=='single-l'){//单侧下公差
			$("#add").attr("disabled","disabled");
			$("#upperLimit").attr("disabled","disabled");
		}
	}
	function createFeatureTree(processId,processName){
		window.processId = processId;
		window.processName = processName;
		$("#qualityFeature").jstree({ 
			"json_data" : {
				"data" : [
					{ 
						"attr" : { "id" : "-1"},
						"state" : "closed",
						"data" : { 
							"title" : processName==""?"产品特性":processName
						}
					}
				],
				"ajax" : {
					"url" : "${spcctx}/base-info/process-define/quality-feature-datas.htm?processId="+processId,
					data : function(node){
						return {id:node.attr("id")};
					}
				}
			},
			"contextmenu":{
				items : {
					create:{
						label : '添加',
						action : function(obj){
							if(obj.attr("orderNum")=='0'){
								alert("只能添加1级!");
								return false;
							}else{
								$("#qualityFeature").jstree("create",obj);
								return true;
							}
						}
					},
					rename : {
						label : '编辑',
						action : function(obj){
							if(obj.attr("id")=='-1'){
								alert("不能编辑根节点!");
								return false;
							}else{
								$("#qualityFeature").jstree("rename",obj);  
								return true;
							}
						}
					},
					remove : {
						label : '删除',
						action : function(obj){
							if(obj.attr("id")=='-1'){
								alert("不能删除根节点!");
								return false;
							}else{
								$("#qualityFeature").jstree("remove",obj);  
								return true;
							}
						}
					},
					ccp : null
				}
			},
			core : { "initially_open" : ["-1"] },
			"plugins" : [ "themes", "json_data","ui","contextmenu","crrm"]
		}).bind("create.jstree",function(event,data){
			$("#qualityFeature").attr("disabled","disabled");
			var pId = processId;
			var params = {
				processId : pId<0?'':pId,
				name : data.rslt.name
			};
			$.post("${spcctx}/base-info/process-define/save-quality-feature.htm",params,function(result){
				$("#qualityFeature").attr("disabled","");
				if(result.error){
					alert(result.message);
					$.jstree.rollback(data.rlbk);
				}else{
					data.rslt.obj.attr("id",result.obj.id);
					data.rslt.obj.attr("orderNum",result.obj.orderNum);
				}
			},'json');
		}).bind("rename.jstree",function(event,data){
			if(data.rslt.old_name == data.rslt.new_name){
				return;
			}
			$("#qualityFeature").attr("disabled","disabled");
			var params = {
				id : data.rslt.obj.attr("id"),
				name : data.rslt.new_name
			};
			$.post("${spcctx}/base-info/process-define/save-quality-feature.htm",params,function(result){
				$("#qualityFeature").attr("disabled","");
				if(result.error){
					alert(result.message);
					$.jstree.rollback(data.rlbk);
				}
			},'json');
		}).bind("remove.jstree",function(event,data){
			var id = data.rslt.obj.attr("id");
			if(id<0){
				$.jstree.rollback(data.rlbk);
				return;
			}
			$.vakata.context.hide();
			if(confirm("确定要删除吗?")){
				$("#qualityFeature").attr("disabled","disabled");
				var params = {
					deleteIds : id
				};
				$.post("${spcctx}/base-info/process-define/delete-quality-feature.htm",params,function(result){
					$("#qualityFeature").attr("disabled","");
					if(result.error){
						alert(result.message);
						$.jstree.rollback(data.rlbk);
					}
				},'json');
			}else{
				$.jstree.rollback(data.rlbk);
			}
		}).bind("copy.jstree",function(event,data){
			var id = data.rslt.obj.attr("id");
			if(id<0){
				$.jstree.rollback(data.rlbk);
				return;
			}
			$.vakata.context.hide();
		}).bind("select_node.jstree",function(e,data){
			if($(data.rslt.obj).hasClass("jstree-closed")){
				$.jstree._reference("#qualityFeature").open_node($(data.rslt.obj),null,false);
			}else if($(data.rslt.obj).hasClass("jstree-open")){
				$.jstree._reference("#qualityFeature").close_node($(data.rslt.obj),null,false);
			}else{
				setQualityFeature(data.rslt.obj.attr("id"),data.rslt.obj.attr("name"));
				featureId = data.rslt.obj.attr("id");
				jQuery("#judge_table").jqGrid('setGridParam',{postData:{id:featureId}}).trigger("reloadGrid"); 
				jQuery("#level_table").jqGrid('setGridParam',{postData:{id:featureId}}).trigger("reloadGrid");
				jQuery("#person_table").jqGrid('setGridParam',{postData:{id:featureId}}).trigger("reloadGrid");
			}
		});
	}
	
	function createJudgeGrid(){
		$("#judge_table").jqGrid({
			url : '${spcctx}/base-info/process-define/rule-datas.htm',
			postData : {id:featureId},
			height : gridHeight,
			width : gridWidth,
			datatype : "json",
			rownumbers : true,
			colNames : ${judgeGridColumnInfo.colNames},
			colModel : ${judgeGridColumnInfo.colModel},
			forceFit : true,
		   	shrinkToFit : true,
		   	autowidth : false,
			viewrecords : true, 
			sortorder : "desc",
			gridComplete : function(){}
		});
	}
	
	function createLevelGrid(){
		$("#level_table").jqGrid({
			url : '${spcctx}/base-info/process-define/level-datas.htm',
			postData : {id:featureId},
			height : gridHeight,
			width : gridWidth,
			datatype : "json",
			rownumbers : true,
			colNames : ${levelGridColumnInfo.colNames},
			colModel : ${levelGridColumnInfo.colModel},
			forceFit : true,
		   	shrinkToFit : true,
		   	autowidth : false,
			viewrecords : true, 
			sortorder : "desc",
			gridComplete : function(){}
		});
	}
	
	function createPersonGrid(){
		$("#person_table").jqGrid({
			url : '${spcctx}/base-info/process-define/person-datas.htm',
			postData : {id:featureId},
			height : gridHeight,
			width : gridWidth,
			datatype : "json",
			rownumbers : true,
			colNames : ${personGridColumnInfo.colNames},
			colModel : ${personGridColumnInfo.colModel},
			forceFit : true,
		   	shrinkToFit : true,
		   	autowidth : false,
			viewrecords : true, 
			sortorder : "desc",
			gridComplete : function(){}
		});
	}
	
	function setQualityFeature(id,name){
		$.post('${spcctx}/base-info/process-define/list-datas.htm',{featureId:id},function(data){
			if(data.error){
				alert(data.message);
			}else{
				$(":input[name=id]").val(data.id);
				$(":input[name=name]").val(data.name);
				$(":input[name=code]").val(data.code);
				$(":input[name=cpkGoal]").val(data.cpkGoal);
				$(":input[name=orderNum]").val(data.orderNum);
				var paramType = data.paramType;
				var paramlen = document.contentForm.paramType.length;
				if(paramType){
					for(var i=0;i<paramlen;i++){
					    if(paramType==document.contentForm.paramType[i].value){
					    	document.contentForm.paramType[i].checked = true;
					    }else{
					    	document.contentForm.paramType[i].checked = false;
					    }
					}
				}
				var type = data.specificationType;
				var radiolen = document.contentForm.specificationType.length;
				if(type){
					for(var i=0;i<radiolen;i++){
					    if(type==document.contentForm.specificationType[i].value){
					  		document.contentForm.specificationType[i].checked = true;
					    }else{
					    	document.contentForm.specificationType[i].checked = false;
					    }
					}
				}
				var precs = data.precs;
				if(precs&&!isNaN(precs)){
					precs = parseInt(precs);
				}else{
					precs = 3;
				}
				if(!isNaN(data.targeValue)){
					$(":input[name=targeValue]").val(data.targeValue);
				}
				if(data.upperTarge){
					$(":input[name=add]").val((data.upperTarge - data.targeValue).toFixed(precs));
				}else{
					$(":input[name=add]").val('');
				}
				if(data.lowerTarge||data.lowerTarge==0){
					$(":input[name=del]").val((data.targeValue - data.lowerTarge).toFixed(precs));
				}else{
					$(":input[name=del]").val('');
				}
				$(":input[name=sampleCapacity]").val(data.sampleCapacity);
				$(":input[name=effectiveCapacity]").val(data.effectiveCapacity);
				$(":input[name=controlChart]").val(data.controlChart);
				$(":input[name=rangeInterval]").val(data.rangeInterval);
				if(data.precs){
					$(":input[name=precs]").val(data.precs);
				}
				$(":input[name=unit]").val(data.unit);
				$(":input[name=upperLimit]").val(data.upperLimit);
				$(":input[name=lowerLimit]").val(data.lowerLimit);
				if(data.isNoAccept){
					$("#isNoAccept-1").attr("checked", "checked");
				}else{
					$("#isNoAccept-1").attr("checked", "");
				}
			    if(data.multiple){
			    	$(":input[name=multiple]").val(data.multiple);
			    }
				var isAutoCl = data.isAutoCl;
				var statelen = document.contentForm.isAutoCl.length;
				if(isAutoCl){
					for(var i=0;i<statelen;i++){
					    if(isAutoCl == document.contentForm.isAutoCl[i].value){
					    	document.contentForm.isAutoCl[i].checked = true;
					    }else{
					    	document.contentForm.isAutoCl[i].checked = false;
					    }
					}
				}
				$(":input[name=ucl1]").val(data.ucl1);
 				$(":input[name=ucl2]").val(data.ucl2);
 				$(":input[name=cl1]").val(data.cl1);
 				$(":input[name=cl2]").val(data.cl2);
 				$(":input[name=lcl1]").val(data.lcl1);
 				$(":input[name=lcl2]").val(data.lcl2);
				var method = data.method;
				var methodlen = document.contentForm.method.length;
				if(method){
					for(var i=0;i<methodlen;i++){
					    if(method==document.contentForm.method[i].value){
					  		document.contentForm.method[i].checked = true;
					    }else{
					    	document.contentForm.method[i].checked = false;
					    }
					}
				}
				$(":input[name=u]").val(data.u);
				$(":input[name=cpk]").val(data.cpk);
				$(":input[name=uclMin]").val(data.uclMin);
				$(":input[name=uclMax]").val(data.uclMax);
				$(":input[name=lclMin]").val(data.lclMin);
				$(":input[name=lclMax]").val(data.lclMax);
				$(":input[name=uclCurrent1]").val(data.uclCurrent1);
				$(":input[name=uclCurrent2]").val(data.uclCurrent2);
				$(":input[name=clCurrent1]").val(data.clCurrent1);
				$(":input[name=clCurrent2]").val(data.clCurrent2);
				$(":input[name=lclCurrent1]").val(data.lclCurrent1);
				$(":input[name=lclCurrent2]").val(data.lclCurrent2);
				//格式化规格限
				specificationTypeChange();
			}
		},'json');
	}
	//获取表单的值
	function getParams(){
		var params = {};
		$(":input","#contentForm").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				if(obj.type=='radio'){
					if(obj.checked){
						params[obj.name] = jObj.val();
					}
				}else if(obj.type=='checkbox'){
					if(obj.checked){
						params[obj.name] = jObj.val();
					}
				}else{
					params[obj.name] = jObj.val();
				}
			}
		});
		return params;
	}
	
	function submitForm(url){
		if($('#contentForm').valid()){
			if($(":input[name=name]").val()==""){
				alert("保存错误，请先选择某个质量特性！");
				return;
			}
			if($("#targeValue").val()==""){
				alert("目标值不能为空！");
				$("#targeValue").focus();
				return;
			}
			if($("#sampleCapacity").val()==""){
				alert("样本容量不能为空！");
				$("#sampleCapacity").focus();
				return;
			}
			if($("#effectiveCapacity").val()==""){
				alert("有效容量不能为空！");
				$("#effectiveCapacity").focus();
				return;
			}
			if(!$("#upperLimit").is(":disabled")&&$("#upperLimit").val()==""){
				alert("合理上限不能为空！");
				$("#upperLimit").focus();
				return;
			}
			if(!$("#lowerLimit").is(":disabled")&&$("#lowerLimit").val()==""){
				alert("合理下限不能为空！");
				$("#lowerLimit").focus();
				return;
			}
			var isAutoCl = document.contentForm.isAutoCl;
			for(var i=0;i<isAutoCl.length;i++){
				var val = isAutoCl[i].value;
			    if(isAutoCl[i].checked){
			    	if(val=="N"){
			    		if($(":input[name=ucl1]").val()==""){
			    			alert("X/XBar的UCL值不为空！");
			    			$(":input[name=ucl1]").focus();
			    			return;
			    		}
			    		if($(":input[name=ucl2]").val()==""){
			    			alert("R/S/MR的UCL值不为空！");
			    			$(":input[name=ucl2]").focus();
			    			return;
			    		}
			    		if($(":input[name=cl1]").val()==""){
			    			alert("X/XBar的CL值不为空！");
			    			$(":input[name=cl1]").focus();
			    			return;
			    		}
			    		if($(":input[name=cl2]").val()==""){
			    			alert("R/S/MR的CL值不为空！");
			    			$(":input[name=cl2]").focus();
			    			return;
			    		}
			    		if($(":input[name=lcl1]").val()==""){
			    			alert("X/XBar的LCL值不为空！");
			    			$(":input[name=lcl1]").focus();
			    			return;
			    		}
			    		if($(":input[name=lcl2]").val()==""){
			    			alert("R/S/MR的LCL值不为空！");
			    			$(":input[name=lcl2]").focus();
			    			return;
			    		}
			    	}
			    }
			}
			if(parseFloat($("#uclMin").val())>parseFloat($("#uclMax").val())){
				alert("控制限UCL最小值不能大于最大值！");
				return;
			}
			if(parseFloat($("#lclMin").val())>parseFloat($("#lclMax").val())){
				alert("控制限LCL最小值不能大于最大值！");
				return;
			}
			var params = getParams();
			var rows1 = jQuery("#judge_table").jqGrid('getRowData'); 
			var rows2 = jQuery("#level_table").jqGrid('getRowData');
			var rows3 = jQuery("#person_table").jqGrid('getRowData');
			var ids = [];
			for(var i=0;i<rows1.length;i++){
				ids.push(rows1[i].targetId);
			}
			params.judgeIds = ids.join(",");
			ids = [];
			for(var i=0;i<rows2.length;i++){
				ids.push(rows2[i].targetId);
			}
			params.levelIds = ids.join(",");
			var paras = new Array();
			for(var i=0;i<rows3.length;i++){
			    var row = rows3[i];
			    paras.push('{"name":"'+row.name+'","id":"'+row.code+'"}');
			}
			params.personStrs = "[" + paras.toString() + "]";
// 			alert($.param(params));
			$.post(url,params,function(result){
				if(result.error){
					alert(result.message);
				}else{
					$("#message").html("保存成功！");
				}
			},'json');
		}
	}
	
	function processFormValidate(){
		$("#contentForm").validate({
			submitHandler: function() {
				$(".opt_btn").find("button.btn").attr("disabled","disabled");
			}
		});
	}
	//删除方法
	function delBules() {
		var rows = jQuery("#judge_table").getGridParam('selarrrow');
		rows = rows.join(",").split(",");
		for(var i=0;i<rows.length;i++){
			jQuery("#judge_table").jqGrid("delRowData",rows[i]);
		}
	}
	function delType() {
		var rows = jQuery("#level_table").getGridParam('selarrrow');
		rows = rows.join(",").split(",");
		for(var i=0;i<rows.length;i++){
			jQuery("#level_table").jqGrid("delRowData",rows[i]);
		}
	}
	//选择判断准则
	var productSelectId = "";
	function selectBsRules(){
		productSelectId = "";
		var url='${spcctx}/base-info/bs-rules/bs-rules-select.htm?multiselect=true';
		$.colorbox({href:url,iframe:true, innerWidth:900, innerHeight:600,
			overlayClose:false,
			title:"选择判断准则"
		});
	}
 	//选择之后的方法 data格式{key:'a',value:'a'}
	function setBsRules(data){
		var rows = jQuery("#judge_table").jqGrid('getRowData'); 
		var mydata = [];
		var isExit = false;
		for(var i=0;i<data.length;i++){
			for(var y=0;y<rows.length;y++){
				if(data[i].id==rows[y].targetId){
					isExit = true;
					break;
				}
			}
			if(!isExit){
				data[i].targetId = data[i].id;
				mydata.push(data[i]);
			}
			isExit = false;
		}
		for(var i=0;i<mydata.length;i++){
			jQuery("#judge_table").jqGrid('addRowData',mydata[i].id + "_new",mydata[i]);
		}
	}
	//选择判断准则--批量设置
	function selectRules(productId){
		productSelectId = productId;
		var url='${spcctx}/base-info/bs-rules/bs-rules-batch-select.htm?multiselect=true';
		$.colorbox({href:url,iframe:true, innerWidth:900, innerHeight:600,
			overlayClose:false,
			title:"选择判断准则"
		});
	}
 	//选择之后的方法 data格式{key:'a',value:'a'}
	function setBsBatchRules(data){
		var rows = jQuery("#judge_table").jqGrid('getRowData'); 
		if(productSelectId.legnth==0){
			var mydata = [];
			var isExit = false;
			for(var i=0;i<data.length;i++){
				for(var y=0;y<rows.length;y++){
					if(data[i].id==rows[y].targetId){
						isExit = true;
						break;
					}
				}
				if(!isExit){
					data[i].targetId = data[i].id;
					mydata.push(data[i]);
				}
				isExit = false;
			}
			for(var i=0;i<mydata.length;i++){
				jQuery("#judge_table").jqGrid('addRowData',mydata[i].id + "_new",mydata[i]);
			}
		}else{
			var ruleIds = "";
			for(i=0;i<data.length;i++){
				if(ruleIds.length==0){
					ruleIds = data[i].id;
				}else{
					ruleIds += "," + data[i].id;
				}
			}
			if(ruleIds.length!=0){
				$.post('${spcctx}/base-info/process-define/bath-set-rules.htm',{parentId:productSelectId,ruleIds:ruleIds},function(data){
					if(data.error){
						alert(data.message);
					}else{
						_self.remove(obj);
					}
				},'json');
			}
		}
		
	}
	var layerProductId = "";
	//选择层别信息
	function selectLayerType(){
		layerProductId = "";
		var url='${spcctx}/base-info/layer-type/layer-type-select.htm';
		$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:400,
			overlayClose:false,
			title:"选择层别信息"
		});
	}
	//选择层别信息-批量
	function selectBathLayerType(parentId){
		layerProductId = parentId;
		var url='${spcctx}/base-info/layer-type/layer-type-select.htm';
		$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:400,
			overlayClose:false,
			title:"选择层别信息"
		});
	}
 	//选择之后的方法 data格式{id:'a',name:'a',code:'a'}
	function setLayerType(data){
		var rows = jQuery("#level_table").jqGrid('getRowData'); 
		var mydata = [];
		var isExit = false;
		if(layerProductId.length==0){
			for(var i=0;i<data.length;i++){
				for(var y=0;y<rows.length;y++){
					if(data[i].id==rows[y].targetId){
						isExit = true;
						break;
					}
				}
				if(!isExit){
					data[i].targetId = data[i].id;
					mydata.push(data[i]);
				}
				isExit = false;
			}
			for(var i=0;i<mydata.length;i++){
				jQuery("#level_table").jqGrid('addRowData',mydata[i].id + "_new",mydata[i]);
			}
		}else{
			var layerIds = "";
			for(i=0;i<data.length;i++){
				if(layerIds.length==0){
					layerIds = data[i].id;
				}else{
					layerIds += "," + data[i].id;
				}
			}
			$.post('${spcctx}/base-info/process-define/bath-set-layers.htm',{parentId:layerProductId,layerIds:layerIds},function(data){
				if(data.error){
					alert(data.message);
				}else{
					_self.remove(obj);
				}
			},'json');
		}
		
	}
	function copyProduct(parentId){
		layerProductId = parentId;
		var url='${spcctx}/base-info/process-define/copy-product.htm?copyId='+layerProductId;
		$.colorbox({href:url,iframe:true,innerWidth:620, innerHeight:450,
			overlayClose:false,
			title:"复制机种"
		});
	}
	//选择异常通知人员
	function selectPerson(){
		/**if(!_ambWorkflowFormObj.webBaseUrl){
		alert("workflow-ofrm.js _selectUser方法提示:初始化选择用户时项目地址未指定!");
		return;
		}*/
		var acsSystemUrl = '${ctx}';
		popZtree({
	        leaf: {
	            enable: false,
	            multiLeafJson:"[{'name':'用户树','type':'MAN_DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"loginName\":\"loginName\"}','showValue':'{\"name\":\"name\"}'}]"
	        },
	        type: {
	            treeType: "MAN_DEPARTMENT_TREE",
	            showContent:"[{'id':'id','loginName':'loginName','name':'name'}]",
	            noDeparmentUser:true,
	            onlineVisible:false
	        },
	        data: {
	            treeNodeData:"id,loginName,name",
	            chkStyle:"checkbox",
	            chkboxType:"{'Y':'ps','N':'ps'}",
	            departmentShow:""
	        },
	        view: {
	            title: title,
	            width: 400,
	            height:400,
	            url:acsSystemUrl
	        },
	        feedback:{
	            enable: true,
	            showInput:'',
	            showThing:"{'name':'name'}",
	            hiddenInput:'',
	            hiddenThing:"{'id':'id'}",
	            append:false
	        },
	        callback: {
	            onClose:function(api){
	            	$("#personId").val(ztree.getIds());
	            	$("#personName").val(ztree.getNames());
	            	setPerson();
	            }
	        }
	    });
	}
	
	function setPerson(){
		var personIds = $("#personId").val().split(",");
		var personName = $("#personName").val().split(",");
		var rows = jQuery("#person_table").jqGrid('getRowData'); 
		var mydata = [];
		var isExit;
		for(var i=0;i<personIds.length;i++){
			for(var y=0;y<rows.length;y++){
				if(personIds[i]==rows[y].code){
					isExit = true;
					break;
				}
			}
			if(!isExit){
				mydata.push({name:personName[i],code:personIds[i],targetId:personIds[i]});
				
			}
			isExit = false;
		}
		for(var i=0;i<=mydata.length;i++){
			jQuery("#person_table").jqGrid('addRowData',i+1,mydata[i]);
		}
	}
	//删除异常通知人员
	function delPerson() {
		var rows = jQuery("#person_table").getGridParam('selarrrow');
		rows = rows.join(",").split(",");
		for(var i=0;i<rows.length;i++){
			jQuery("#person_table").jqGrid("delRowData",rows[i]);
		}
	}
	//段别统一选择异常通知人员
	function selectPersonAll(productId){
		/**if(!_ambWorkflowFormObj.webBaseUrl){
		alert("workflow-ofrm.js _selectUser方法提示:初始化选择用户时项目地址未指定!");
		return;
		}*/
		var acsSystemUrl = '${ctx}';
		popZtree({
	        leaf: {
	            enable: false,
	            multiLeafJson:"[{'name':'用户树','type':'MAN_DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"loginName\":\"loginName\"}','showValue':'{\"name\":\"name\"}'}]"
	        },
	        type: {
	            treeType: "MAN_DEPARTMENT_TREE",
	            showContent:"[{'id':'id','loginName':'loginName','name':'name'}]",
	            noDeparmentUser:true,
	            onlineVisible:false
	        },
	        data: {
	            treeNodeData:"id,loginName,name",
	            chkStyle:"checkbox",
	            chkboxType:"{'Y':'ps','N':'ps'}",
	            departmentShow:""
	        },
	        view: {
	            title: title,
	            width: 400,
	            height:400,
	            url:acsSystemUrl
	        },
	        feedback:{
	            enable: true,
	            showInput:'',
	            showThing:"{'name':'name'}",
	            hiddenInput:'',
	            hiddenThing:"{'id':'id'}",
	            append:false
	        },
	        callback: {
	            onClose:function(api){
	            	$("#personId").val(ztree.getIds());
	            	$("#personName").val(ztree.getNames());
	            	setAllPerson(productId,ztree.getIds(),ztree.getNames());
	            }
	        }
	    });
	}
	function setAllPerson(productId,ids,names){
		$.post('${spcctx}/base-info/process-define/set-person.htm',{parentId:productId,userIds:ids,userNames:names},function(data){
			if(data.error){
				alert(data.message);
			}else{
				_self.remove(obj);
			}
		},'json');
	}
	function $successfunc(response){
		var result = eval("(" + response.responseText + ")");
		if(result.error){
			alert(result.message);
			return false;
		}else{
			return true;
		}
	}
	
	function callback(){
		addFormValidate('${fieldPermission}','processForm');
		processFormValidate();
		showMsg();
	}
	
	$(function(){
		//添加验证
		processFormValidate();
	});
	
	function onchangeChart(){
		var controlChart=$("#controlChart").val();
		if(controlChart==4){
			$("#sampleCapacity").val(1);
			$("#effectiveCapacity").val(1);
			$("#sampleCapacity").attr('disabled',true);
			$("#effectiveCapacity").attr('disabled',true);
		}else{
			/* $("#sampleCapacity").val("");
			$("#effectiveCapacity").val(""); */
			$("#sampleCapacity").attr('disabled',false);
			$("#effectiveCapacity").attr('disabled',false);
		}
	}
	
	function copyFeature(){
		var featureId = $(":input[name=id]").val();
		if(!featureId){
			$.showMessage("请先选择质量特性!");
			return;
		}
		window._isFresh=false;
		$.colorbox({href:"${spcctx}/base-info/process-define/copy-form.htm?featureId=" + featureId,iframe:true, 
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
 			overlayClose:false,
 			onClosed:function(){
 				if(window._isFresh&&window.processId){
 					createFeatureTree(window.processId,window.processName);
 				}
 			},
 			title:"复制质量特性"
 		});
	}
</script>
</head>
