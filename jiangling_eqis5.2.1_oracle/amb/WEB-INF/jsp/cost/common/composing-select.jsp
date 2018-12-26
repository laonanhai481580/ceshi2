<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
///////////////页面使用方法////////////////////////////////////
//	//选择质量成本
// 	function selectComposing(){
//  	var url = '${costctx}/common/composing-select.htm?multiselect=true';
//  	$.colorbox({href:url,iframe:true, innerWidth:800, innerHeight:400,
//  		overlayClose:false,
//  		title:"选择质量成本"
// 	 	});
// 	 }
	
//  //选择之后的方法 data格式{key:'a',value:'a'}
// 	function setComposingValue(datas){
// 		alert("select is key:" + datas[0].key + ",value:" + datas[0].value);
// 	}
//  //选择之后的方法 data格式全部
// 	function setFullComposingValue(datas){
// 		alert("select is key:" + datas);
// 	}
///////////////页面使用方法结束////////////////////////////////////
		isUsingComonLayout=false;
		function contentResize(){
			$("#composingList").jqGrid("setGridWidth",$("#btnDiv").width()-10);
			$("#composingList").jqGrid('setGridHeight',$(document).height()-95);
		}
		var multiselect = ${multiselect};
		$(document).ready(function(){
			createGrid();
			contentResize(); 
		});
		//创建表格
		function createGrid(){
			var colModel = [
				{name:'id',index:'id', width:1,hidden:true,key:true},
				{label:'科目',name:'name',index:'name', width:160},
				{label:'编码',name:'code',index:'code', width:90},
				{label:'二级科目',name:'levelTwoName',index:'levelTwoName', width:160,hidden:true},
				{label:'二级编码',name:'levelTwoCode',index:'levelTwoCode', width:90,hidden:true},
				//{label:'核算部门',name:'checkDepartment',index:'checkDepartment', width:70},		
				//{label:'配合部门',name:'cooperateDepartment',index:'cooperateDepartment', width:70},		
				{label:'备注',name:'remark',index:'remark',width:200}	
		   	];
			if(multiselect){
				var ckObj = {label:'<input type="checkbox" class="cbox" onclick=\"selectAll(event,this)\"/>',name:'act',index:'act', width:30,align:'center'};
				colModel.splice(1,0,ckObj);
			}
			$("#composingList").jqGrid({
				url:'${costctx}/common/composing-list-datas.htm',
				postData : {},
				treedatatype: "json",
				mtype: "POST",
			   	colModel : colModel,
			   	autoWidth : true,
				treeGridModel: 'adjacency',
			    treeGrid: true,
			    multiselect: multiselect,
			    viewrecords: true,
				ExpandColumn : 'name',
				ExpandColClick : true,
				shrinkToFit : true,
				gridComplete: function(){
					if(multiselect){
						var ids = jQuery("#composingList").jqGrid('getDataIDs');
						for(var i=0;i < ids.length;i++){
							var cl = ids[i];
							ck = "<input type=\"checkbox\" id=\"ck_"+cl+"\" class=\"cbox\"/>"; 
							jQuery("#composingList").jqGrid('setRowData',ids[i],{act:ck});
						}	
					}
				}
			});
		}
		function selectAll(e,obj){
			//如果提供了事件对象，则这是一个非IE浏览器
	        if ( e && e.stopPropagation ){
	        	e.stopPropagation();
	        } else{
	        	window.event.cancelBubble = true;
	        }
	        $("#composingList input[type='checkbox']").attr("checked",obj.checked);
	        if(!obj.checked){
	        	var id = $("#composingList").jqGrid("getGridParam","selrow");
				if(id){
					$("#composingList").jqGrid("resetSelection",id);
				}
	        }
		}
		//加载产品BOM 
		function loadProductBomByStructureAndParent(structureId,structureName){
			if(initially_select == structureId){
				return;
			}
			initially_select = structureId || '';
			$("#composingList").clearGridData();
			if(initially_select){
				$("#composingList").jqGrid("setGridParam",{postData:{structureId:initially_select,nodeid:''}}).trigger("reloadGrid");
			}
		}
		//确定
		function realSelect(){
			var ids = [];
			if(multiselect){
				$("#composingList input[type='checkbox']").each(function(index,obj){
		        		if(obj.checked&&obj.id&&obj.id.indexOf("_")>0){
		        			ids.push(obj.id.split('_')[1]);
		        		}
		        });
			}else{
				var id = $("#composingList").jqGrid("getGridParam","selrow");
				if(id){
					ids.push(id);
				}
			}
			if(ids.length == 0){
				alert("请选择质量成本!");
				return;
			}
			
			if($.isFunction(window.parent.setFullComposingValue)){//获取全部的值
				var objs = [];
				for(var i=0;i<ids.length;i++){
					var data = $("#composingList").jqGrid('getRowData',ids[i]);
					if(data&&data.isLeaf == 'true'){
						objs.push(data);
					}
				}
				if(objs.length>0){
					window.parent.setFullComposingValue(objs);
					window.parent.$.colorbox.close();
				}else{
					alert("选择的值不存在!");
				}
			}else if($.isFunction(window.parent.setComposingValue)){//获取简洁的值
				var objs = [];
				for(var i=0;i<ids.length;i++){
					var data = $("#composingList").jqGrid('getRowData',ids[i]);
					if(data&&data.isLeaf == 'true'){
						objs.push({key:data.code,value:data.name});
					}
				}
				if(objs.length>0){
					window.parent.setComposingValue(objs);
					window.parent.$.colorbox.close();
				}else{
					alert("选择的值不存在!");
				}
			}else{
				alert("页面还没有 setComposingValue 方法!");
			}
		}
		//取消
		function cancel(){
			window.parent.$.colorbox.close();
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="opt-body">
		<div class="opt-btn" id="btnDiv">
			<button  class='btn' onclick="realSelect();"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>
			<button  class='btn' onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
		</div>
		<div style="padding:6px;">
			<table id="composingList"></table>
		</div>
	</div>
</body>
</html>