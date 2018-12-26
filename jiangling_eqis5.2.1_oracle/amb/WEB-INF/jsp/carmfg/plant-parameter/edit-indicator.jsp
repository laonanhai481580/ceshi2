<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript">
		var topMenu = '',editing=false,isUsingComonLayout=false;
		function isSetFormat(cellvalue,options,rowObject){
			if(rowObject.isLeaf == true){
				var checked = cellvalue=='yes'?"checked":"";
				var disabled = rowObject.canUse == 'yes'?"":"disabled=true";
				return '<div style="text-align:center;"><input type="checkbox" ' + checked + ' ' + disabled + ' onclick="isEstimateChange(this,'+rowObject.id+')"></input></div>';
			}else{
				return '';
			}
		}
		function isEstimateChange(obj,id){
			var $t = $("#itemIndicatorList")[0];
			if($t.p.savedRow.length>0){
				$($t).jqGrid("restoreCell",$t.p.iRow,$t.p.iCol);
			}
			var rowData = $("#itemIndicatorList").jqGrid("getRowData",id);
			if(rowData){
				var val = obj.checked?"yes":"no";
				var params = {};
				for(var pro in rowData){
					params[pro] = rowData[pro];
				}
				params.indicatorId = '${indicatorId}';
				params.isSet = val;
				$.post("${mfgctx}/inspection-base/indicator/save-item.htm",params,function(result){
					if(result.error){
						alert(result.message);
						obj.checked = !obj.checked;
					}else{
						$("#itemIndicatorList").jqGrid('setRowData',id,{isSet:val,isLeaf:true,canUse:'yes',id : id});
					}
				},'json');
			}
		}
		var postData = {
				indicatorId:'${indicatorId}',
				materialCode:'<%=request.getParameter("model")%>',
				workingProcedure:'<%=request.getParameter("workingProcedure")%>'
		};
		$(document).ready(function(){
			jQuery("#itemIndicatorList").jqGrid({
				url:'${mfgctx}/plant-parameter/edit-indicator-datas.htm',
// 				rownumbers:true,
				postData : postData,
				treedatatype: "json",
				height : $(document).height()-75,
				mtype: "POST",
			   	colModel:[
			   		{name:'id',index:'id', width:1,hidden:true,key:true},
			   		{name:'isLeaf',index:'isLeaf', width:1,hidden:true},
			   		{name:'itemIndicatorId',index:'itemIndicatorId', width:1,hidden:true},
			   		{label:'操作',name:'',index:'',width:80,editable:false,formatter:function(val,o,obj){
			   			if(obj.itemIndicatorId){
			   				return '<button class="btn" type="button" onclick="deleteItemIndicator('+obj.itemIndicatorId+');" style="margin-left:6px;" title="删除【'+obj.name+'】"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button> ';
			   			}else{
			   				return "";
			   			}
			   		}},
			   		{label:'设备名称',name:'params.plantName',index:'plantName', width:160,editable:false},
			   		{label:'参数名称',name:'params.parameterName',index:'parameterName', width:120,editable:true},
			   		{label:'参数规格',name:'params.parameterStandard',index:'parameterStandard', width:80,editable:true},
			   		{label:'上限',name:'params.levela',index:'levela', width:80,editable:true,editrules:{min:0,number:true}},
			   		{label:'下限',name:'params.levelb',index:'levelb', width:80,editable:true,editrules:{min:0,number:true}},
			   		{label:'单位',name:'params.unit',index:'unit', width:80,editable:true},
			   		{label:'备注',name:'params.remark',index:'remark', width:180,editable:true,edittype:"textarea", editoptions:{rows:"5",style:"width:100%;"}}
			   	],
				treeGridModel: 'adjacency',
			    treeGrid: true,
			    viewrecords: true,
				ExpandColumn : 'name',
// 				ExpandColClick : true,
// 				shrinkToFit : true,
				gridComplete : function(){
				},
				loadComplete : function(){
					if(scroolTop != null){
						$($("#itemIndicatorList").closest(".ui-jqgrid-bdiv")[0]).scrollTop(scroolTop);
						scroolTop = null;
					}
				},
				cellEdit: true, 
				cellsubmit : 'remote',
				cellurl : "${mfgctx}/plant-parameter/save-item.htm?indicatorId=${indicatorId}",
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
		* 刷新检验项目
		*/
		var scroolTop = null;
		function refreshItemIndicator(){
			var data = $("#itemIndicatorList")[0].p.data;
			var expandIds = [];
			for(var i=0;i<data.length;i++){
				var d = data[i];
				if(d.expanded){
					expandIds.push(d.id);
				}
			}
			var postData = {
				indicatorId:'${indicatorId}',
				materialCode:'<%=request.getParameter("materialCode")%>',
				workingProcedure:'<%=request.getParameter("workingProcedure")%>',
				nodeid:'',
				expandIds : expandIds.join(",")
			};
			scroolTop = $($("#itemIndicatorList").closest(".ui-jqgrid-bdiv")[0]).scrollTop();
			$("#itemIndicatorList").clearGridData();
			$("#itemIndicatorList").jqGrid("setGridParam",{postData:postData}).trigger("reloadGrid");
			//重置展开的节点为空
			$("#itemIndicatorList").jqGrid("setGridParam",{postData:{expandIds:''}});
		}
		function deleteItemIndicator(id){
			if(confirm("确定要删除吗?")){
				$("button").attr("disabled","disabled");
				$("#opt-content").attr("disabled","disabled");
				$("#message").html("正在删除项目,请稍候... ...");
				$.post("${mfgctx}/plant-parameter/delete-item.htm",{indicatorId:id},function(result){
					$("button").attr("disabled","");
					$("#opt-content").attr("disabled","");
					$("#message").html(result.message);
					if(result.error){
						alert(result.message);
					}else{
						refreshItemIndicator();
					}
					setTimeout(function(){
						$("#message").html("");
					},3000);
				},'json');
			}
		}
		function addInspectingItems(){
			var url='${mfgctx}/plant-parameter/plant-item-select.htm?indicatorId=${indicatorId}';
			$.colorbox({href:url,
				iframe:true,
				width:850,
				height:$(window).height()-100,
				overlayClose:false,
				onClosed:function(){
// 					refreshItemIndicator();
				},
				title:"添加设备"
			});
		}
		
		function setInspectingItemValue(datas){
			var ids = [];
			for(var i=0;i<datas.length;i++){
				ids.push(datas[i].id);
			}
			if(ids.length>0){
				$("button").attr("disabled","disabled");
				$("#opt-content").attr("disabled","disabled");
				$("#message").html("正在添加设备,请稍候... ...");
				$.post("${mfgctx}/plant-parameter/add-item.htm",{indicatorId:${indicatorId},deleteIds:ids.join(",")},function(result){
					$("button").attr("disabled","");
					$("#opt-content").attr("disabled","");
					$("#message").html(result.message);
					if(result.error){
						alert(result.message);
					}else{
						refreshItemIndicator();
					}
					setTimeout(function(){
						$("#message").html("");
					},3000);
				},'json');
			}
		}
		
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<div class="opt-body" style="height:30px;">
		<div class="opt-btn" style="height:30px;line-height:30px;">
		<button class='btn' onclick="addInspectingItems();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>添加检验项目</span></span></button>
		<button class='btn' type="button" onclick="window.parent.$.colorbox.close();;"><span><span ><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>	
			<span style="color:red;" id="message">
			</span>
		</div>
	</div>
	<div id="opt-content" style="padding-left:6px;">
		<form id="contentForm" name="contentForm" method="post" action="">
			<table id="itemIndicatorList"></table>
		</form>
	</div>
</body>
</html>