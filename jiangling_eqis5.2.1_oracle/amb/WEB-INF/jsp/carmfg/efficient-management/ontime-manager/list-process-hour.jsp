<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
	<title>选择检验员</title>
	<%@include file="/common/meta.jsp" %>	
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript" src="${ctx}/js/search.js"></script>
	
	<script type="text/javascript">
	var editing=false;
	var rowId,originalIndex,newIndex;
	var lastSelection;
	function makeEditable(editable){
		if(editable){
			editing=false;
			jQuery("#processHourList tbody").sortable('enable');
		}else{
			editing=true;
			jQuery("#processHourList tbody").sortable('disable');
		}
		
	}
	function editNextRow(rowId){
		var ids=jQuery("#processHourList").jqGrid("getDataIDs");
		var index=jQuery("#processHourList").jqGrid("getInd",rowId);
		index++;
		if(index>ids.length){//当前编辑行是最后一行
			addRow(true);
		}else{
			editRow(ids[index-1]);
		}
	}
	function afterSaveRow(rowId,data){
		//必须加括号才能转换为对象
		var jsonData = eval("(" + data.responseText
				+ ")");
		if (rowId == 0) {//新纪录删除了再增加
			jQuery("#processHourList").jqGrid(
					'delRowData', rowId);
			jQuery("#processHourList").jqGrid(
					'addRowData', jsonData.id,
					jsonData, "last");
		} else {//更新已有记录
			jQuery("#processHourList").jqGrid(
					'setRowData', jsonData.id,
					jsonData);
		}
		alert(jsonData.id);
		editNextRow(jsonData.id);
	}
	function addRow(byEnter) {
		if(byEnter==undefined){
			byEnter=false;
		}
		if ((!editing&&!byEnter)||byEnter) {
			jQuery("#processHourList").jqGrid(
					'addRow',
					{
						rowID : "0",
						position : "last",
						addRowParams : {
							keys:true,
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
			jQuery("#processHourList").jqGrid("saveRow",lastSelection);
		}
		lastSelection=rowId;
		jQuery("#processHourList").jqGrid("editRow",rowId,{
			keys:true,
			aftersavefunc : function(rowId, data) {
				afterSaveRow(rowId,data);
			},
			afterrestorefunc :function(rowId){
				makeEditable(true);
			}
		});
		jQuery("#processHourList").focus();
		makeEditable(false);
	}
	
	function delRow(rowId) {
		var ids = jQuery("#processHourList").getGridParam('selarrrow');
		if(ids.length<1){
			alert("请选中需要删除的记录！");
			return;
		}
		$.post("${mfgctx}/efficient-management/ontime-manager/delete-ontime-process-hour.htm", {
			deleteIds : ids.join(',')
		}, function(data) {
			//ids数组的长度是会自动变小的(实际是jqgrid内部的一个数组)
			while (ids.length>0) {
				jQuery("#processHourList").jqGrid('delRowData', ids[0]);
			}
		});

	}
	function closeBtn(){
		window.parent.$.colorbox.close();
	}
	
	
	
	
	</script>
	</head>
	
	<body>
		<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn">
							<button class='btn' onclick="addRow()"><span><span>新建</span></span></button>
							<button  class='btn' onclick="delRow();"><span><span>删除</span></span></button>
							<button  class='btn' onclick="closeBtn();"><span><span>关闭</span></span></button>
					</div>
					<div id="opt-content" class="form-bg">
						<div style="display: none;" id="message"><font class=onSuccess><nobr>保存成功！</nobr></font></div>
						<form id="contentForm" name="contentForm" method="post"  action="" onsubmit="return false;">
							<input type="hidden"  value="${ontimeManagerId}" name="ontimeManagerId" id="ontimeManagerId" />
							
							<table id="processHourList"></table>
							<div id="pager"></div> 
							<script type="text/javascript">
								$(document).ready(function(){
									var ontimeManagerId = $("#ontimeManagerId").val();
									jQuery("#processHourList").jqGrid({
										url:'${mfgctx}/efficient-management/ontime-manager/list-process-hour-datas.htm',
										postData:{"ontimeManagerId":ontimeManagerId},
										rownumbers:true,
										height:390,
										colNames:['工序','工时'],
										colModel:[
										          {name:'processName', index:'processName',width:240,editable:true,edittype:'select',formatter:'select',editoptions:{${processNameStr}}},
										          {name:'hour', index:'hour',width:240,editable:true}
										          ],
								      	editurl: "${mfgctx}/efficient-management/ontime-manager/save-process-hour.htm",
										gridComplete: function(){ 
											contentResize();
										},
										ondblClickRow: function(rowId){
											editRow(rowId);
										},
										serializeRowData:function(data){
											//不要把id=0传回去，避免后台判断id=0或null
											if(data.id==0){
												data.id="";
											}
											data.ontimeManagerId = ontimeManagerId;
											return data;
										}
									});
									
						       	});
								
								
							</script>
						</form>
					</div>
					
				</aa:zone>
			</div>
	</body>
</html>