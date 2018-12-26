<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
		isUsingComonLayout=false;
		var topMenu = '',editing=false;
		function isEstimateFormat(cellvalue,options,rowObject){
			if(rowObject.isLeaf == true){
				var checked = cellvalue=='yes'?"checked":"";
				var disabled = rowObject.canUse == 'yes'?"":"disabled=true";
				return '<div style="text-align:center;"><input type="checkbox" ' + checked + ' ' + disabled + ' onclick="isEstimateChange(this,'+rowObject.id+')"></input></div>';
			}else{
				return '';
			}
		}
		function isEstimateChange(obj,id){
			var $t = $("#estimateModelList")[0];
			if($t.p.savedRow.length>0){
				$($t).jqGrid("restoreCell",$t.p.iRow,$t.p.iCol);
			}
			var rowData = $("#estimateModelList").jqGrid("getRowData",id);
			if(rowData){
				var val = obj.checked?"yes":"no";
				var params = {};
				for(var pro in rowData){
					params[pro] = rowData[pro];
				}
				params.estimateModelId = '${estimateModelId}';
				params.isEstimate = val;
				$.post("${supplierctx}/estimate/model/quality/save-indicator.htm",params,function(result){
					if(result.error){
						alert(result.message);
						obj.checked = !obj.checked;
					}else{
						$("#estimateModelList").jqGrid('setRowData',id,{isEstimate:val,isLeaf:true,canUse:'yes',id : id});
					}
				},'json');
			}
		}
		
		function levelFormatter(value,options,rowObj){
			var fieldName = options.colModel.index;
			var min = rowObj[fieldName + "Min"],max = rowObj[fieldName + "Max"];
			if(min != undefined && max != undefined){
				if(min==max){
					return min;
				}else{
					return min + "-" + max;
				}				
			}else{
				return '';
			}
		}
		var postData = {
			estimateModelId:'${estimateModelId}'
		};
		$(document).ready(function(){
			jQuery("#estimateModelList").jqGrid({
				url:'${supplierctx}/estimate/model/quality/edit-indicator-datas.htm',
				postData : postData,
				treedatatype: "json",
				height : $(document).height()-70,
				mtype: "POST",
			   	colModel:[
			   		{name:'id',index:'id', width:1,hidden:true,key:true},
			   		{name:'isLeaf',index:'isLeaf', width:1,hidden:true},
			   		{label:"评价指标",name:'name',index:'name', width:260},
			   		{label:"是否评价'/>",name:'isEstimate',index:'isEstimate', width:80,formatter:isEstimateFormat},
			   		{label:"分值",name:'params.totalPoints',index:'totalPoints', width:70,editable:true,editrules:{min:0,number:true}},
			   		{label:"A档分",name:'params.levela',index:'levela', width:70,editable:true},
			   		{label:"B档分",name:'params.levelb',index:'levelb', width:70,editable:true},
			   		{label:"C档分",name:'params.levelc',index:'levelc', width:70,editable:true},
			   		{label:"D档分",name:'params.leveld',index:'leveld', width:70,editable:true},
			   		{label:"E档分",name:'params.levele',index:'levele', width:70,editable:true},
			   		{label:"描述",name:'params.remark',index:'remark', width:180,editable:true,edittype:"textarea", editoptions:{rows:"5",style:"width:100%;"}}
			   	],
				treeGridModel: 'adjacency',
			    treeGrid: true,
			    viewrecords: true,
				ExpandColumn : 'name',
// 				ExpandColClick : true,
				shrinkToFit : true,
				gridComplete : function(){
				},
				loadComplete : function(){
					if(scroolTop != null){
						$($("#estimateModelList").closest(".ui-jqgrid-bdiv")[0]).scrollTop(scroolTop);
						scroolTop = null;
					}
				},
				cellEdit: true, 
				cellsubmit : 'remote',
				cellurl : "${supplierctx}/estimate/model/quality/save-indicator.htm?estimateModelId=${estimateModelId}",
				afterSubmitCell : function(serverresponse, rowid, cellname, value, iRow, iCol){
					var result = eval("(" + serverresponse.responseText
							+ ")");
					if(result.error){
						return [false,result.message];
					}else{
						return [true,""];
					}
				},
				beforeEditCell : function(){
					return true;
				}
			});
		});
		/**
		* 刷新评价模型
		*/
		var scroolTop = null;
		function refreshEstimateModel(){
			var data = $("#estimateModelList")[0].p.data;
			var expandIds = [];
			for(var i=0;i<data.length;i++){
				var d = data[i];
				if(d.expanded){
					expandIds.push(d.id);
				}
			}
			var postData = {
				estimateModelId:'${estimateModelId}',
				nodeid:'',
				expandIds : expandIds.join(",")
			};
			scroolTop = $($("#estimateModelList").closest(".ui-jqgrid-bdiv")[0]).scrollTop();
			$("#estimateModelList").clearGridData();
			$("#estimateModelList").jqGrid("setGridParam",{postData:postData}).trigger("reloadGrid");
			//重置展开的节点为空
			$("#estimateModelList").jqGrid("setGridParam",{postData:{expandIds:''}});
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<div style="line-height: 22px;">
		&nbsp;<span id="message"
			style="color: red; font-size: 14px; padding-left: 6px;"></span>
	</div>
	<div id="opt-content" style="padding-left:6px;">
		<form id="contentForm" name="contentForm" method="post" action="">
			<table id="estimateModelList"></table>
		</form>
	</div>
</body>
</html>