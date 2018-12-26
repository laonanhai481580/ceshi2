<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript">
	//重定义内容大小
	function contentResize(){
		$("#customerDataDiv").width($("#btnDiv").width());
		$("#customerDataDiv").height($(document).height() - $("#btnDiv").height() - 300);
	}
	var capcity='',featureId='',layerId='',lastSelect={},upperValue='',lowerValue='',isNoAccept='';
	//选择质量特性
	function selectFeature(obj){
		$.colorbox({href:"${spcctx}/common/feature-bom-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
			overlayClose:false,
			title:"选择质量特性"
		});
	}
	//设置质量特性
	function setFeatureValue(datas){
		$("#featureName").val(datas[0].value);
		$("#sgValue").focus();
		capcity = datas[0].sampleCapacity;
		if(!capcity){
			capcity = 5;
		}
		featureId = datas[0].id; 
		layerId = datas[0].targetId;
		upperValue = datas[0].upperLimit;
		lowerValue = datas[0].lowerLimit;
		isNoAccept = datas[0].isNoAccept;
		$("#sgValue").val("");
		$("#sigma").val("");
		$("#rangeDiff").val("");
		$("#minValue").val("");
		$("#maxValue").val("");
		jQuery("#sgSample_table").jqGrid("clearGridData");
		loadLayerItems();
		setImgUrl();
		$(".ui-layout-center").attr("disabled","disabled");
		var url = '${spcctx}/data-acquisition/find-last-spc-sub-group.htm';
		$.post(url,{featureId:featureId,capcity:capcity},function(result){
			$(".ui-layout-center").attr("disabled","");
			if(result.error){
				alert(result.message);
			}else{
				if(result.id>-1){
					mainId = result.id;
					$("#sigma").val(result.sigma);
					$("#rangeDiff").val(result.rangeDiff);
					$("#maxValue").val(result.maxValue);
					$("#minValue").val(result.minValue);
					//采样值
					var samples = result.samples;
					for(var i=0;i<samples.length;i++){
						var s = samples[i];
						jQuery("#sgSample_table").jqGrid('addRowData',s.id,s);
					}
					//层别信息
					var levelMap = result.levelMap;
					$("#layerItemsParent").find(":input[name=tagName]").each(function(obj,index){
						var val = $(obj).val();
						if(levelMap[val]){
							$(obj).closest("tr").find(":input[name=tagValue]").val(levelMap[val]);
						}
					});	
				}
				$("#sgValue").focus();
			}
		},'json');
	}
	//设置图片的路径
	function setImgUrl(){
		var inspectionType = $("#inspectionType").val();
		var url = '${spcctx }/jl-analyse/draw-control.htm?featureId='+featureId + "&nowtime=" + (new Date()).getTime()+"&inspectionType="+inspectionType;
		$("#imgID").attr("src",url);
	}
	//加载层别信息
	function loadLayerItems(){
		var params = {featureId : featureId};
		var url = "${spcctx }/data-acquisition/layer-items.htm";
		$("#layerItemsParent").find("div").load(url,params,function(){});
	}
	
	var index=0,mainId=null;
	$(document).ready(function(){
		$("#featureName").focus();
		$("#sgValue").bind("keyup",function(e){
			if(e.keyCode==13){
				addSgSample(this);
				$("#sgValue").val("");
				$("#sgValue").focus();
			}
		});
		loadSampleItems();
// 		loadLevelItems();
		loadLayerItems();
		$("#tabs").tabs({});
		contentResize();
	});
	//加载样本信息
	function loadSampleItems(){
		$("#sgSample_table").jqGrid({
	 		url:'${spcctx}/data-acquisition/sg-sample-datas.htm',
	 		postData : {featureId:featureId},
			datatype: 'json',
			rowNum: 15,
			height: 150,
		   	autowidth:true,
		   	shrinkToFit:false,
			rownumbers: true,
			multiselect: false,
			colNames:['','序号', '采样值'],
			colModel:[
				{name:'id', index:'id', width:1, hidden:true,align:'center'},
	            {name:'sampleNo', index:'sampleNo', width: 80,align:'center',editable:false},
	            {name:'samValue', index:'samValue', width: 120,align:'center',editable:false}
	        ],
			gridComplete: function(){}
		});
	}
	//加载横向信息
	function loadLevelItems(){
		$("#level_table").jqGrid({
	 		url:'${spcctx}/data-acquisition/sg-tag-datas.htm',
	 		postData : {featureId:featureId},
			datatype: 'json',
			rowNum: 15,
			height: 130,
		   	autowidth:true,
		   	shrinkToFit:false,
			rownumbers: true,
			multiselect: false,
			gridEdit: true,
			colNames:['','','层别','','取值'],
			colModel:[
				{name:'id', index:'id', width:1, hidden:true,align:'center',key:true},
				{name:'sampleMethod', index:'sampleMethod', width:1, hidden:true,align:'center'},
	            {name:'tagName', index:'tagName',width: 80,align:'center',editable:false},
	            {name:'tagCode', index:'tagCode', width:1, hidden:true,align:'center'},
	            {name:'tagValue', index:'tagValue', width: 120,align:'center',editable:true}	        
	        ],
			gridComplete: function(){},
			editurl: "${spcctx}/data-acquisition/save-tag.htm",
			ondblClickRow: function(rowId){
				editRow("level_table",rowId);
			},
			serializeRowData:function(data){
				var rowData = $("#level_table").jqGrid("getRowData",data.id);
				if(mainId){
					data.mainId = mainId;
				}
				data.tagName = rowData.tagName;
				data.tagCode = rowData.tagCode;
				data.tagValue = $("#" + data.id + "_tagValue").val();
				data.sampleMethod = rowData.sampleMethod;
				if(data.id.indexOf("layer_")==0){
					data.id="";
				}
				return data;
			}
		});
	}
	
	var isAdd = false;
	function addSgSample(obj){
		var value = $("#sgValue").val();
		if(!value){
			return;
		}
		var rows = jQuery("#sgSample_table").jqGrid('getRowData'); 
		if(featureId==""){
			alert("请先选择某一质量特性!");
			return;
		}
		if(parseFloat(value)>parseFloat(upperValue)){
			if(isNoAccept=='true'){
				alert("采样值不能大于上限："+upperValue+"!");
				return;
			}
		}
		if(parseFloat(value)<parseFloat(lowerValue)){
			if(isNoAccept=='true'){
				alert("采样值不能小于下限："+lowerValue+"!");
				return;
			}
		}
		//设置默认样本数
		if(capcity==""){
			capcity = 5;
		}
		if(rows.length>=capcity){
			alert("最多只可添加"+capcity+"个取样值!");
			return;
		}else{
			index = rows.length+1;
		}
		var obj = {sampleNo:"X"+index,samValue:value};
		if(mainId != null){
			obj.mainId = mainId;
		}
		obj.qualityFeatureId = featureId;
		if(index >= capcity){
			obj.layerItemStrs = getLayerItemStrs();
// 			$("input[name=layerItemStrs]").val(getLayerItemStrs1());
		}
		if(isAdd){
			return;
		}
		isAdd = true;
		var url = '${spcctx}/data-acquisition/save.htm';
		$.post(url,obj,function(result){
			isAdd = false;
			if(result.error){
				alert(result.message);
			}else{
				mainId = result.mainId;
				$("#sigma").val(result.sigma);
				$("#rangeDiff").val(result.rangeDiff);
				$("#maxValue").val(result.maxValue);
				$("#minValue").val(result.minValue);
				jQuery("#sgSample_table").jqGrid('addRowData',result.id,result);
				if(rows.length+1>=capcity){
					mainId = null;
					$("#sigma").val("");
					$("#rangeDiff").val("");
					$("#minValue").val("");
					$("#maxValue").val("");
					$("#sgValue").val("");
					$("#sgValue").focus();
					jQuery("#sgSample_table").jqGrid("clearGridData");
					setImgUrl();
					//判断是否发起异常
					lanchAmbormal(result.num);	
				}
			}
		},'json');
	}
	
	function lanchAmbormal(num){
		$.post('${spcctx}/process-monitor/lanch-abnormal.htm',{featureId:featureId,num:num},function(result){},'json');
	}	
	//获取层别信息
	function getLayerItemStrs(){
		var layerItemStrs = "";
		$("#layerItemsParent").find("table>tbody>tr").each(function(index,obj){
			if(layerItemStrs){
				layerItemStrs += ",";
			}
			var str = '';
			$(obj).find(":input").each(function(index,obj){
				if(obj.name){
					if(str){
						str += ","; 
					}
					str += "\"" + obj.name + "\":\"" + $(obj).val() + "\"";
				}
			});
			layerItemStrs += "{" + str + "}";
		});
		return "[" + layerItemStrs + "]";
	}
	//获取表单的值
	function getParams(){
		var params = {};
		$(":input","#contentForm").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		return params;
	}
	
	var editFumObj = {
		'level_table' : function(id){
			var obj = jQuery("#level_table").jqGrid("getRowData",id);
			var url = '${spcctx}/common/set-layer-datas.htm';
			var params = {};
			if(obj.sampleMethod=="2"){
				params = {
					layerName : obj.tagName
				};
				$("#" + id + "_tagValue").attr("readonly","readonly").val("");
				$.post(url,params,function(result){
					var list = result;
					$("#" + id + "_tagValue").attr("readonly","");
					var html = "<select id='"+id + "_tagValue' style='width:"+$("#" + id + "_tagValue").width()+"px;'>";
					for(var i=0;i<list.length;i++){
						html += "<option value='"+list[i].value+"'>"+list[i].value+"</option>";
					}
					html += "</select>";
					$("#" + id + "_tagValue").replaceWith(html);
				},'json');
			}
			/* $("#" + id + "_tagValue").click(function(){
				selectLayInfo(id);
			}); */
		}
	};
	/**
	 * 编辑时需要的参数
	 */
	var editParams={
		keys : true,
		oneditfunc : function(rowId){
			editFun(rowId);
		},
		successfunc: function( response ) {
	        return $successfunc(response);
	    },
		aftersavefunc : function(rowId, data) {
			afterSaveRow(rowId,data);
		},
		afterrestorefunc : function(rowId) {
			makeEditable(true);
		},
		restoreAfterError:false
	};
	
	function editFun(rowId){
		if(editFumObj[window.tableId]){
			editFumObj[window.tableId](rowId);
		}
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

	var editing=false;
	function makeEditable(tableId,editable){
		if(editable){
			editing=false;
			jQuery("#"+tableId+" tbody").sortable('enable');
		}else{
			editing=true;
			jQuery("#"+tableId+" tbody").sortable('disable');
		}
	}
	
	function editNextRow(tableId,rowId){
		var grid = jQuery("#" + tableId);
		var ids=grid.jqGrid("getDataIDs");
		var index=grid.jqGrid("getInd",rowId);
		index++;
		if(index>ids.length){//当前编辑行是最后一行
			return;
		}else{
			editRow(tableId,ids[index-1]);
		}
	}
	
	/**
	 * 保存后走的方法
	 * @param rowId
	 * @param data
	 * @return
	 */
	function afterSaveRow(rowId, data) {
		//必须加括号才能转换为对象
		var jsonData = eval("(" + data.responseText + ")");
		if (rowId == 0||rowId.indexOf("layer_")==0) {//新记录删除了再增加
			$("#" + window.tableId).jqGrid('delRowData', rowId);
			$("#" + window.tableId).jqGrid('addRowData', jsonData.id, jsonData, "last");
		} else {//更新已有记录
			$("#" + window.tableId).jqGrid('setRowData', jsonData.id, jsonData);
		}
		editNextRow(window.tableId,jsonData.id);
	}
	//编辑表格
	function editRow(tableId,rowId){
		if(rowId){
			window.tableId = tableId;
			if(lastSelect[tableId]){
				$("#" + tableId).jqGrid("restoreRow", lastSelect[tableId]);
			}
			lastSelect[tableId] = rowId;
			$("#" + tableId).jqGrid("editRow", rowId, editParams);
			makeEditable(false);
		}
	}
	
	function updateCl(){
		if(featureId==""){
			alert("请先选择某一质量特性！");
			return;
		}else{
		$.colorbox({href:"${spcctx}/jl-analyse/update-cl.htm?featureId="+featureId+"&date="+$("#dateValue").val(),
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
			overlayClose:false,
			title:"修改控制线",
			onClosed:function(){
				setImgUrl();
			}
		});
		}
	}
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="data_acq";
		var thirdMenu="_acq";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/spc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/spc-data-acquisition-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow: auto">
			<form id="contentForm" name="contentForm" method="post" action="">
				<div><input type="hidden" name="id" value="${spcSubGroup.id }"/></div>
				<div class="opt-btn" id="btnDiv">
					<table cellpadding="0" cellspacing="0" width="100%">
						<tr>
							<td style="padding: 0px;width:50%; margin: 0px;">
								<span>
									<security:authorize ifAnyGranted="spc_common_feature-bom-select">
									   <strong style="vertical-align: middle;">检验类型</strong>
									   <select id="inspectionType"  name="inspectionType">   
										  <option   value=""></option>   
										  <option   value="首检">首检</option>   
										  <option   value="巡检">巡检</option>  
										  <option   value="末检">末检</option>   
										</select>
										<strong style="vertical-align: middle;">质量特性</strong>
										<input name="featureName" id="featureName" style="width:150px;vertical-align: middle;border: none;" readonly="readonly"/>
										<a class="small-button-bg" style="vertical-align: middle;" onclick="selectFeature(this);" href="javascript:void(0);" title="选择质量特性"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
										<span style="vertical-align: middle;">（请选择质量特性）</span>
									</security:authorize>
								</span>
							</td>
							<td style="margin-right:10px;" align="right">
								<span>
									<security:authorize ifAnyGranted="spc_jl-analyse_update-cl">
										<button class='btn' type="button" onclick="updateCl(this);"><span><span><b class="btn-icons btn-icons-settings"></b>修改控制线</span></span></button>
									</security:authorize>
								</span>
							</td>
						</tr>
					</table>
				</div>
				<div style="margin-top:8px; margin-left: 8px;height:480px;" id="customerDataDiv">
					<table style="height:100%;">
						<tr>
							<td style="width:25%;height:480px;" valign="top">
								<fieldset style="height: 60%;">
			    					<legend>数据</legend>
			    					<table>
										<tr>
											<td>均值</td>
											<td><input style="border: none;" name="sigma" id="sigma" disabled="disabled"/></td>
										</tr>
										<tr>
											<td>极差</td>
											<td><input style="border: none;" name="rangeDiff" id="rangeDiff" disabled="disabled"/></td>
										</tr>
										<tr style="display: none;">
											<td>最大值</td>
											<td><input style="border: none;" name="maxValue" id="maxValue"/></td>
										</tr>
										<tr style="display: none;">
											<td>最小值</td>
											<td><input style="border: none;" name="minValue" id="minValue"/></td>
										</tr>
										<tr>
											<td>数值</td>
											<td>
												<input style="border: none;" name="sgValue" id="sgValue" onkeypress="if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value" onkeyup="if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value" onblur="if(!this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?|\.\d*?)?$/))this.value=this.o_value;else{if(this.value.match(/^\.\d+$/))this.value=0+this.value;if(this.value.match(/^\.$/))this.value=0;this.o_value=this.value}"/> 
											</td>
										</tr>
									</table><br/>
									<table id="sgSample_table"></table>
								</fieldset>
								<fieldset style="height: 39%;">
									<legend>层别信息</legend>
									<table id="level_table">
										<input type="hidden" name="layerItemStrs" value=""/>
										<tr>
											<td id="layerItemsParent">
												<div style="overflow-x:auto;overflow-y:auto;height: 200px">
													<%@ include file="layer-items.jsp"%>
												</div>
											</td>
										</tr>
									</table>
								</fieldset>
							</td>
							<td style="width:75%;height:480px;" valign="top">
								<fieldset style="width: 97%;height: 100%;">
									<legend>控制图</legend>
									<table>
										<tr>
											<td>
												<img id="imgID" src="${ctx}/images/stat/controlpic.png" width="100%" height="450PX;"/>
											</td>
										</tr>
									</table>
								</fieldset>	
							</td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div>
</body>
</html>