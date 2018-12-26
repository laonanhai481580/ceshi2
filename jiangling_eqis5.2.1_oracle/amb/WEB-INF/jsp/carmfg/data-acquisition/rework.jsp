<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
	<script type="text/javascript">
	var editing=false;
	var rowId,originalIndex,newIndex;
	var lastSelection;
	function makeEditable(editable){
		if(editable){
			editing=false;
			jQuery("#reportDiv tbody").sortable('enable');
		}else{
			editing=true;
			jQuery("#reportDiv tbody").sortable('disable');
		}
		
	}
	function editNextRow(rowId){
		var ids=jQuery("#reportDiv").jqGrid("getDataIDs");
		var index=jQuery("#reportDiv").jqGrid("getInd","398,"+rowId);
		index++;
		if(index>ids.length){//当前编辑行是最后一行
		}else{
			editRow(ids[index-1]);
		}
	}
	function afterSaveRow(rowId,data){
		//必须加括号才能转换为对象
		var jsonData = eval("(" + data.responseText
				+ ")");
		if (rowId == 0) {//新纪录删除了再增加
			jQuery("#reportDiv").jqGrid(
					'delRowData', rowId);
			jQuery("#reportDiv").jqGrid(
					'addRowData', jsonData.id,
					jsonData, "last");
		} else {//更新已有记录
			jQuery("#reportDiv").jqGrid(
					'setRowData', jsonData.id,
					jsonData);
		}
		var rowData = $('#reportDiv').jqGrid("getRowData",rowId);
		if(rowData.reState=='通过'){
			var rowId = rowData.id.replace(",","\\,");
			$('#reportDiv').find('#'+rowId).css("background","#FFFF00");
		}else{
			var rowId = rowData.id.replace(",","\\,");
			$('#reportDiv').find('#'+rowId).css("background","#FF0000");
		}
		editNextRow(jsonData.id);
	}
	function addRow(byEnter) {
		if(byEnter==undefined){
			byEnter=false;
		}
		if ((!editing&&!byEnter)||byEnter) {
			jQuery("#reportDiv").jqGrid(
					'addRow',
					{
						rowID : "0",
						position : "last",
						addRowParams : {
							keys:true,
							oneditfunc : function(rowId){
								$("#reportDiv").setGridParam({postData:getParams()});
							},
							aftersavefunc : function(rowId, data) {
								afterSaveRow(rowId,data);
							},
							afterrestorefunc :function(rowId){
								makeEditable(true);
							}
						}
					});
			makeEditable(false);
		}

	}
	function editRow(rowId){
		if(rowId&&rowId!=lastSelection){
			jQuery("#reportDiv").jqGrid("saveRow",lastSelection);
		}
		lastSelection=rowId;
		jQuery("#reportDiv").jqGrid("editRow",rowId,{
			keys:true,
			oneditfunc : function(rowId){
				$("#reportDiv").setGridParam({postData:getParams()});
			},
			aftersavefunc : function(rowId, data) {
				afterSaveRow(rowId,data);
			},
			afterrestorefunc :function(rowId){
				makeEditable(true);
			}
		});
		var rowId = rowId.replace(",","\\,");
		jQuery('#'+rowId+'_reDate').click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		jQuery('#'+rowId+'_reWorker').val('${name}');
		var nowtime="<%=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())%>";
		jQuery('#'+rowId+'_reDate').val(nowtime);
		$("#" + rowId + "_reWorker").focus();
		makeEditable(false);
	}
	var uiIndex = 0;
	function contentResize(){
		if(uiIndex==1){
			$("#all_part_table").jqGrid("setGridWidth",$("#btnDiv").width()-80);
			$("#current_part_table").jqGrid("setGridWidth",$("#btnDiv").width()-80);
		}
	}
	
	$(document).ready(function(){
		
		var initiated = {
				'1':false,
				'2' : false
			};
		$( "#tabs" ).tabs({
			show: function(event, ui) {
				uiIndex = ui.index;
				if(!initiated[ui.index]){
					if(ui.index==1){
					}else if(ui.index == 2){
					}
					initiated[ui.index] = true;
				}
				if(ui.index==1){
					$("#all_part_table").jqGrid("setGridWidth",$("#btnDiv").width()-80);
					$("#current_part_table").jqGrid("setGridWidth",$("#btnDiv").width()-80);
				}
			}
		});
		
		$("#detail_table").jqGrid({
			url:'${mfgctx}/data-acquisition/current-defective-data.htm',
			datatype: 'json',
			rowNum: 15,
			pager: '#detail_tablePager',
			rownumbers: true,
			multiselect:false,
			colNames:['','方位', '部位','不良'],
			colModel:[
				{name:'id', index:'id', width:1, hidden:true,align:'center'},
	            {name:'direction', index:'direction', width: 180,align:'center',editable:false},
	            {name:'position', index:'position', width: 180,align:'center',editable:false},
	            {name:'defection', index:'defection', width: 180,align:'center',editable:false}
	        ],
			 gridComplete: function(){ 
        		 
 			},
 			ondblClickRow: function(rowId){
				var id=rowId.split(",");
				$.post("${mfgctx}/data-acquisition/delete-current-defective-data.htm?currentId="+id[1],"",function(){
					$("#detail_table").trigger("reloadGrid");
				},'json');
				
			}
		});
	
		$("#reportDiv").jqGrid({
			url:'${mfgctx}/data-acquisition/current-all-defective-data.htm',
			datatype: 'json',
			rowNum: 15,
			pager: '#reportDivPager',
			rownumbers: true,
			multiselect:true,
			colNames:['','方位', '部位','不良','返修人','返修日期','结果'],
			colModel:[
				{name:'id', index:'id', width:1, hidden:true,align:'center'},
			    {name:'direction', index:'direction', width: 60,align:'center',editable:false},
			    {name:'position', index:'position', width: 100,align:'center',editable:false},
			    {name:'defection', index:'defection', width: 70,align:'center',editable:false},
			    {name:'reWorker', index:'reWorker', width: 80,align:'center',editable:true},
			    {name:'reDate', index:'reDate', width: 80,align:'center',editable:true},
			    {name:'reState', index:'reState', width: 80,align:'center',edittype:'checkbox',editoptions:{value:"通过:未过"},editable:true}
	        ],
	        editurl: "${mfgctx}/data-acquisition/save.htm?saveType=reSave",
			gridComplete: function(){ 
				var ids = $('#reportDiv').jqGrid("getDataIDs");
				for(var i=0;i<ids.length;i++){
					var rowData = $('#reportDiv').jqGrid("getRowData",ids[i]);
					if(rowData.reState=='通过'){
						var rowId = rowData.id.replace(",","\\,");
						$('#reportDiv').find('#'+rowId).css("background","#FFFF00");
					}else{
						var rowId = rowData.id.replace(",","\\,");
						$('#reportDiv').find('#'+rowId).css("background","#FF0000");
					}
				}

			},
			ondblClickRow: function(rowId){
				editRow(rowId);
				var rowId = rowId.replace(",","\\,");
				$("#" + rowId + "_reWorker").focus();
			},
			serializeRowData:function(data){
				$.extend(data,getParams());
				return data;
			},
		});

		
		$("#detail_table1").jqGrid({
			url:'${mfgctx}/base-info/direction-code/list-datas.htm',
			datatype: 'json',
			rowNum: 15,
			width:100,
			height:230,
			pager: '#directionPager',
			multiselect:false,
			pgtext:"",
			recordtext:"",
			colNames : [ 'id', '方位代码', '方位名称'],
					colModel : [ {
						name : 'id',
						index : 'id',
						hidden : true
					}, {
						name : 'directionCodeNo',
						index : 'directionCodeNo',
						width:65,
						hidden : false
					}, {
						name : 'directionCodeName',
						index : 'directionCodeName',
						width:65,
						hidden : false
					}],
					gridComplete : function() {

					},
					ondblClickRow: function(rowId){
						setDirection(rowId);
						jQuery("#direction").focus();
					}
				});

		$("#detail_table2").jqGrid({
			url : '${mfgctx}/base-info/position-code/list-datas.htm',
			datatype : 'json',
			rowNum : 15,
			height:230,
			pgtext:"",
			recordtext:"",
			pager: '#positionPager',
			multiselect : false,
			colNames:['','部位代码', '部位名称'],
			colModel:[
			          {name:'id', index:'id', width:1, hidden:true,align:'center'},
			          {name:'positionCodeNo', index:'positionCodeNo',width: 65, editable:true},
			          {name:'positionCodeName', index:'positionCodeName', width: 65, editable:true},
			          ],
			gridComplete : function() {
	
			},
			ondblClickRow: function(rowId){
				setPosition(rowId);
				jQuery("#position").focus();
			}
		});

		$("#detail_table3").jqGrid({
			url:'${mfgctx}/base-info/defection-code/list-datas.htm',
			datatype: 'json',
			rowNum: 15,
			height:230,
			pgtext:"",
			recordtext:"",
			multiselect : false,
			pager: '#defectionPager',
			colNames:['','不良代码', '不良名称'],
			colModel:[
			          {name:'id', index:'id', width:1, hidden:true,align:'center'},
			          {name:'defectionCodeNo', index:'defectionCodeNo', width: 65, editable:true},
			          {name:'defectionCodeName', index:'defectionCodeName', width: 65, editable:true},
			          ],
			gridComplete : function() {

			},
			ondblClickRow: function(rowId){
				setDefection(rowId);
				jQuery("#defection").focus();
			}
		});

		

		$("#detail_table4").jqGrid({
			url : '${mfgctx}/common/code-list-datas.htm',
			datatype : 'json',
			rowNum : 15,
			height:230,
			recordtext:"",
			rownumbers : true,
			pager: '#detail_table4Pager',
			multiselect : false,
			colNames : [ '', '不良代码', '不良名称', '不良分类' ],
			colModel : [ {
				name : 'id',
				index : 'id',
				width : 1,
				hidden : true,
				align : 'center'
			}, {
				name : 'defectionCodeNo',
				index : 'defectionCodeNo',
				width : 100,
				align : 'left',
				editable : false
			}, {
				name : 'defectionCodeName',
				index : 'defectionCodeName',
				width : 180,
				align : 'center',
				editable : false
			}, {
				name : 'defectionTypeName',
				index : 'defectionTypeName',
				width : 180,
				align : 'center',
				editable : false
			} ],
			multiselect : true,
			gridComplete : function() {

			}
		});

		$("#current_part_table").jqGrid({
			url : '${mfgctx}/data-acquisition/current-part-data.htm',
			datatype : 'json',
			rowNum : 15,
			height:230,
			width:1000,
			recordtext:"",
			rownumbers : true,
			pager: '#current_part_tablePager',
			multiselect : false,
			colNames : [ '', '条码', '零件', '供应商' ],
			colModel : [ {
				name : 'id',
				index : 'id',
				width : 1,
				hidden : true,
				align : 'center'
			}, {
				name : 'partVinNo',
				index : 'partVinNo',
				width : 100,
				align : 'left',
				editable : false
			}, {
				name : 'partName',
				index : 'partName',
				width : 180,
				align : 'center',
				editable : false
			}, {
				name : 'supplierName',
				index : 'supplierName',
				width : 180,
				align : 'center',
				editable : false
			} ],
			multiselect : false,
			gridComplete : function() {

			},
			ondblClickRow: function(rowId){
				var id=rowId.split(",");
				alert(id);
				$.post("${mfgctx}/data-acquisition/delete-current-part-data.htm?currentId="+id[1],"",function(){
					$("#current_part_table").trigger("reloadGrid");
					$("#all_part_table").trigger("reloadGrid");
				},'json');
				
			}
		});
		
		
		$("#all_part_table").jqGrid({
			url : '${mfgctx}/data-acquisition/current-all-part-data.htm',
			datatype : 'json',
			rowNum : 15,
			height:230,
			recordtext:"",
			rownumbers : true,
			pager: '#all_part_tablePager',
			colNames : [ '', '条码', '零件', '供应商'  ],
			colModel : [ {
				name : 'id',
				index : 'id',
				width : 1,
				hidden : true,
				align : 'center'
			}, {
				name : 'partVinNo',
				index : 'partVinNo',
				width : 100,
				align : 'left',
				editable : false
			}, {
				name : 'partName',
				index : 'partName',
				width : 180,
				align : 'center',
				editable : false
			}, {
				name : 'supplierName',
				index : 'supplierName',
				width : 180,
				align : 'center',
				editable : false
			} ],
			multiselect : false,
			gridComplete : function() {

			}
		});
		
	});
	
	
	
	//设置值
	function setDirection(rowId){
		var data = $("#detail_table1").jqGrid("getRowData",rowId);
		var value=data.directionCodeNo+'/'+data.directionCodeName;
	$("#direction").val(value);
	}
	function setPosition(rowId){
		var data = $("#detail_table2").jqGrid("getRowData",rowId);
		var value=data.positionCodeNo+'/'+data.positionCodeName;
	$("#position").val(value);
	}
	function setDefection(rowId){
		var data = $("#detail_table3").jqGrid("getRowData",rowId);
		var value=data.defectionCodeNo+'/'+data.defectionCodeName;
	$("#defection").val(value);
	}
	
	var directionTimeout = null;
	function findDirectionValue(obj){
		if(directionTimeout != null){
			clearTimeout(directionTimeout);
		}
		directionTimeout = setTimeout(function(){
			$("#detail_table1").setGridParam({postData:{"directionCode":obj.value}});
			$("#detail_table1").trigger("reloadGrid");
			$.post("${mfgctx}/data-acquisition/set-code-value.htm?directionCode="+obj.value,"",function(result){
				if(result.value==""){
					return false;
				}else{
					$("#direction").val(result.value);
					jQuery("#position").focus();
				}
			},'json');
			directionTimeout = null;
		},500);
	}
	function findPositionValue(obj){
		if(directionTimeout != null){
			clearTimeout(directionTimeout);
		}
		directionTimeout=setTimeout(function(){
		$("#detail_table2").setGridParam({postData:{"positionCode":obj.value}});
		$("#detail_table2").trigger("reloadGrid");
		$.post("${mfgctx}/data-acquisition/set-code-value.htm?positionCode="+obj.value,"",function(result){
			if(result.value==""){
				return false;
			}else{
				$("#position").val(result.value);
				jQuery("#defection").focus();
			}
		},'json');
		directionTimeout = null;
		},500);
	}
	function findDefectionValue(obj){
		if(window.event.keyCode==13){
			if($("#inspectionRecordBarcode").val()==''){
				alert("请扫描VIN码!!");
				return false;
			}
			submitForm('${mfgctx}/data-acquisition/save.htm');
			$("#direction").val('');
			$("#position").val('');
			$("#defection").val('');
			$("#direction").focus();
		}else{
		if(directionTimeout != null){
			clearTimeout(directionTimeout);
		}
		directionTimeout=setTimeout(function(){
		$("#detail_table3").setGridParam({postData:{"defectionCode":obj.value}});
		$("#detail_table3").trigger("reloadGrid");
		$.post("${mfgctx}/data-acquisition/set-code-value.htm?defectionCode="+obj.value,"",function(result){
			if(result.value==""){
				return false;
			}else{
				$("#defection").val(result.value);	
			}
		},'json');
		directionTimeout = null;
		},500);
		}
	}
	
	function findCarDetailByKeyCode(){
		if(window.event.keyCode==13){
			findCarDetail();
		}
	}
	
	function findCarDetail(){
		var inputbarcode=$("#inputbarcode").val();
		params=getParams();
		$.post("${mfgctx}/data-acquisition/find-car-detail.htm?inputbarcode="+inputbarcode,params,function(result){
			if(result.error){
				alert(result.message);
			}else{	
				$("#barcode").val(result.barcode);
				$("#vinNo").val(result.vinNo);
				$("#engine").val(result.engine);
				$("#output").val(result.output);
				$("#threecNo").val(result.threecNo);
				$("#modelConfiguration").val(result.modelConfiguration);
				$("#color").val(result.color);
				$("#orderNo").val(result.orderNo);
				$("#batchNo").val(result.batchNo);
				var workProcedure=$("#workprocedure").val();
				var vinNo=$("#vinNo").val();
				$("#detail_table").setGridParam({postData:{"vinNo":vinNo,"workProcedure":workProcedure}});
				$("#detail_table").trigger("reloadGrid");
				$("#reportDiv").setGridParam({postData:{"vinNo":vinNo}});
				$("#reportDiv").trigger("reloadGrid");
				$("#current_part_table").setGridParam({postData:{"vinNo":vinNo,"workProcedure":workProcedure}});
				$("#current_part_table").trigger("reloadGrid");
				$("#all_part_table").setGridParam({postData:{"vinNo":vinNo}});
				$("#all_part_table").trigger("reloadGrid");
				$("#direction").focus();
				$("#inspectionRecordBarcode").val(result.vinNo);
			}
		},'json');
		
	}
	function findPartDetailByKeyCode(){
		if(window.event.keyCode==13){
			findPartDetail();
		}
	}
	
	function findPartDetail(){
		if($("#inspectionRecordBarcode").val()==''){
			alert("请扫描VIN码!!");
			return false;
		}
		if($("#partVinNo").val()==''){
			alert("请扫描零件VIN码!!");
			return false;
		}
		params=getParams();
		$.post("${mfgctx}/data-acquisition/find-part-detail.htm",params,function(result){
			if(result.error){
				alert(result.message);
			}else{
				var vinNo=$("#vinNo").val();
				var workprocedure=$("#workprocedure").val();
				$("#current_part_table").setGridParam({postData:{"vinNo":vinNo,"workProcedure":workprocedure}});
				$("#current_part_table").trigger("reloadGrid");
				$("#all_part_table").setGridParam({postData:{"vinNo":vinNo}});
				$("#all_part_table").trigger("reloadGrid");
			}
		},'json');
		$("#direction").focus();
	}
	function submitForm(url){
		var params=getParams();
		$.post(url,params,function(){
			var vinNo=$("#vinNo").val();
			$("#detail_table").setGridParam({postData:{"vinNo":vinNo,"workProcedure":"${workProcedure}"}});
			$("#detail_table").trigger("reloadGrid");
			$("#reportDiv").setGridParam({postData:{"vinNo":vinNo}});
			$("#reportDiv").trigger("reloadGrid");
		});
		$("#direction").focus();
	}
	
	//获取表单的值
	function getParams(){
		var params = {};
		$("#productDetailForm :input[type=text]").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		$("#productDetailForm :input[type=hidden]").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		$("#detailDiv :input").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		return params;
	}
	
	
	function detail(){
		var ids = jQuery("#reportDiv").getGridParam('selarrrow');
		if(ids.length<1){
			alert("请选中需要编辑的记录！");
			return;
		}
		if(ids.length>1){
			alert("请选中一条记录！");
			return;
		}
		var id=ids[0].split(",");
		var url="${mfgctx}/data-acquisition/detail.htm?id="+id[1];
		$.colorbox({href:encodeURI(url),iframe:true, innerWidth:400, innerHeight:420,
 			overlayClose:false,
 			title:"详细记录"
 		});
	}
	function reason(){
		var ids = jQuery("#reportDiv").getGridParam('selarrrow');
		if(ids.length<1){
			alert("请选中需要编辑的记录！");
			return;
		}
		if(ids.length>1){
			alert("请选中一条记录！");
			return;
		}
		var id=ids[0].split(",");
		var url="${mfgctx}/data-acquisition/reason.htm?id="+id[1];
		$.colorbox({href:encodeURI(url),iframe:true, innerWidth:450, innerHeight:530,
 			overlayClose:false,
 			title:"原因措施"
 		});
	}
	
	function changeInpsectionPoint(){
		var params=getParams();
		$.post("${mfgctx}/data-acquisition/inspection-point-change.htm",params,function(result){
			$("#workprocedure").val(result.workprocedure);
			workprocedure=result.workprocedure;
			var vinNo=$("#vinNo").val();
			$("#detail_table").setGridParam({postData:{"vinNo":vinNo,"workProcedure":workprocedure}});
			$("#detail_table").trigger("reloadGrid");
			$("#reportDiv").setGridParam({postData:{"vinNo":vinNo}});
			$("#reportDiv").trigger("reloadGrid");
			$("#current_part_table").setGridParam({postData:{"vinNo":result.vinNo,"workProcedure":workprocedure}});
			$("#current_part_table").trigger("reloadGrid");
			$("#all_part_table").setGridParam({postData:{"vinNo":vinNo}});
			$("#all_part_table").trigger("reloadGrid");
		},'json');
	}	
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="dataAcquisition";
		var thirdMenu="reworkLeft";
	</script>
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-data-acquisition-menu.jsp" %>
	</div>
	<div class="ui-layout-center" style="overflow:auto;">
		
<div class="opt-body" >
	<form id="productDetailForm" name="productDetailForm" method="post" action="">
	<input type="hidden" id="inspectionRecordBarcode" name="inspectionRecordBarcode"></input>
	<input type="hidden" id="inspectionDate" name="inspectionDate" value="<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>"></input>
	<s:if test="hasRight=='yes'">
	<div class="opt-btn" id="btnDiv">
	 </div>
	 </s:if>
	 <s:else>
			<div class="opt-btn"><span><span style="font-size: 14px;line-height:35PX">请确认${workshop}检查点检验台帐和检验权限是否已正确配置，如有问题请联系系统管理员！</span></span></div>
	</s:else>
	<div id="opt-content" class="form-bg">
	 <div style="padding:4px;" id="detailDiv">
	 <s:if test="hasRight=='yes'">
	<table class="form-table-outside-border" style="width:100%;">
			<tr>
			<td><font>条码</font></td>
			<td><input type="text" id="inputbarcode" name="inputbarcode" onkeyup="findCarDetailByKeyCode();"/></td>
			<td>
			<a  class='btn' onclick="findCarDetail();"><span><span>查询</span></span></a>&nbsp;&nbsp;
			</td>
			<td>请扫描VIN条码</td>
			<td>请选择采集点</td>
			<td><s:select list="inspectionPointList" 
				theme="simple"
				listKey="id" 
				listValue="inspectionPointName" 
				name="inspectionPointId"
				label="采集点"
				labelSeparator=""
				onchange="changeInpsectionPoint();"></s:select></td>
			</tr>
		</table>
		<table class="form-table-outside-border" style="width:100%;">
		<tr>
			<td>原始条码</td>
			<td><input type="text" id="barcode" name="barcode" disabled="disabled"/></td>
			<td>VIN号</td>
			<td><input type="text" id="vinNo" name="vinNo" disabled="disabled"/></td>
			<td>发动机</td>
			<td><input type="text" id="engine" name="engine" disabled="disabled"/></td>
			</tr>
			<tr>
			<td>用户</td>
			<td>${name}</td>
			<td>工位</td>
			<td><input type="text" id="workprocedure" name="workprocedure" disabled="disabled" value="${workProcedure}"/></td>
			<td>当前产量</td>
			<td><input type="text" id="output" name="output" disabled="disabled"/></td>
			</tr>
			<tr>
			<td>当前日期</td>
			<td><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%></td>
			<td>车型配置</td>
			<td><input type="text" id="modelConfiguration" name="modelConfiguration" disabled="disabled"/></td>
			<td >3C号码</td>
			<td><input type="text" id="threecNo" name="threecNo" disabled="disabled"/></td>
			<td align="right"><a  class='btn' onclick="detail();"><span><span>详情</span></span></a></td>
			<td><a   class='btn' onclick="reason();"><span><span>原因</span></span></a></td>
			</tr>
		</table>
		</s:if>
	 </div>
	<div id="opt-content">
	<s:if test="hasRight=='yes'">
		<div id="tabs">
			<ul>
				<li><span><a href="#tabs1">不良采集</a></span>
				</li>
				<li><span><a href="#tabs2">零部件装配</a></span>
				</li>
			</ul>
			<div id="tabs1" >
				<table style="width:100%;height:200px;" >
					<tr>
						<td style="width:50%;">
						<div style="height:20px;">当前车当前不良</div>
							<table id="detail_table"></table>
							<div id="detail_tablePager"></div>
						</td>
						<td style="width:1%;"></td>
						<td style="width:50%;">
						<div style="height:20px;">当前车历史不良</div>
							<table id="reportDiv"></table>
							<div id="reportDivPager"></div>
						</td>
					</tr>
				</table>
				<table style="width:100%;height:200px;margin-top:4px;" cellpadding="0" cellspacing="0">
				<tr>
						<td style="width:15%;">
						<div style="height:30px;">
						方位:<input type="text" id="direction" name="direction" style="width:60%" onkeyup="findDirectionValue(this);"/>
						</div>
						<table id="detail_table1"></table>
						<div id="directionPager"></div>
						</td>
						<td style="width:1%;"></td>
						<td style="width:15%;">
						<div style="height:30px;">
						部位:<input type="text" id="position" name="position" style="width:60%" onkeyup="findPositionValue(this);"/>
						</div>
							<table id="detail_table2"></table>
							<div id="positionPager"></div>
						</td>
						<td style="width:1%;"></td>
						<td style="width:15%;">
						<div style="height:30px;">
						不良:<input type="text" id="defection" name="defection" style="width:60%" onkeyup="findDefectionValue(this);"/>
						</div>
							<table id="detail_table3"></table>
							<div id="defectionPager"></div>
						</td>
						<td style="width:1%;"></td>
						<td style="width:45%;">
						<div style="height:30px;">
						<span>组合=方位+部位+不良</span>
						</div>
							<table id="detail_table4"></table>
							<div id="detail_table4Pager"></div>
						</td>
					</tr>
				</table>
			</div>
		
		
		
			<div id="tabs2" >
			<table cellpadding="0" cellspacing="0">
				<tr>
				<td>零部件定位:</td>
				<td align="right"><input type="text" id="partVinNo" name="partVinNo" onkeyup="findPartDetailByKeyCode();"/></td>
			
			<td align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a  class='btn' onclick="findPartDetail();"><span><span>查询</span></span></a>&nbsp;&nbsp;
			</td>
			</tr>
			</table>
			<table style="width:100%;height:200px;padding-top: 10px;" cellpadding="0" cellspacing="0" >
				<tr>
					<td style="width:100%;">
					<div style="height:20px;">当前装配零部件</div>
						<table id="current_part_table"></table>
						<div id="current_part_tablePager"></div>
					</td>
				</tr>
				<tr><td>&nbsp;</td></tr>
			<tr>
					<td style="width:100%;">
					<div style="height:20px;">已装配零部件</div>
						<table id="all_part_table"></table>
						<div id="all_part_tablePager"></div>
					</td>
				</tr>
			</table>				
		</div>
		</div>
		</s:if>
		</div>
		</div>
		</form>
	</div>
	</div>
</body>
</html>