<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>选择不良代码</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		function contentResize(){
			var searchDivHeight = 0;
			if($("#customerSearchDiv").css("display")=='block'){
				searchDivHeight = $("#customerSearchDiv").height();
			}
			$("#defectionCodeList").jqGrid("setGridWidth",$("#btnDiv").width());
			$("#defectionCodeList").jqGrid('setGridHeight',$(document).height() - $("#btnDiv").height() - 70 - searchDivHeight);
		}
		var secMenu="baseInfo";
		var thirdMenu="bom";
		var topMenu='aaa';
		var defaultBomGridCaption = "${productStructure.name}的BOM";
		var initially_select = '${defectionType.id}';
		$(document).ready(function(){
			createStructureTree();
			createBom();
			contentResize();
		});
		//创建树
		function createStructureTree(){
			var children = ${defectionTypeMaps};
			$("#structure-tree").jstree({ 
				"json_data" : {
					"data" : [
						{ 
							"data" : "不良分类", 
							"state" : "open",
							attr:{
								id:'root',
								level : 0,
								rel:'drive'
							},
							children : children
						}
					]
				},
				"plugins" : [ "themes", "json_data", "ui"]
			}).bind("select_node.jstree",function(e,data){
				var id = data.rslt.obj.attr("id");
				var name = data.rslt.obj.attr("defectionTypeName");
				if(id != null){
					loadCodeBomByTypeAndParent(id,name);
				}else{
					loadCodeBomByTypeAndParent('');
				}
			});
		}
		//加载不良代码 
		function loadCodeBomByTypeAndParent(Id,Name){
			if(initially_select == Id){
				return;
			}
			initially_select = Id || '';
			if(initially_select){
				$("#defectionCodeList").jqGrid("setGridParam",{postData:{defectionTypeId : initially_select}}).trigger("reloadGrid");
				$("#defectionCodeList").jqGrid("setCapition",Name + "的代码");
			}else{
				$("#defectionCodeList").clearGridData();
			}
		}
		//创建不良代码
		function createBom(){
			$("#defectionCodeList").jqGrid({
				url:'${mfgctx}/common/code-list-datas.htm',
				postData : {defectionTypeId : initially_select},
				datatype: 'json',
				mtype: "POST",
				rowNum: 15,
				rownumbers: true,
				multiselect: true,
				pager: '#pager',
				colNames:['','不良代码', '不良名称', '不良分类','不良'],
				colModel:[
					{name:'id',index:'id', width:1,hidden:true},		            
		            {name:'defectionCodeNo', index:'defectionCodeNo', width: 100,align:'left',editable:false},
		            {name:'defectionCodeName', index:'defectionCodeName', width: 150,align:'left',editable:false},
		            {name:'defectionTypeName', index:'defectionTypeName', width: 150,align:'left',editable:false},
		            {name:'defectionTypeNo', index:'defectionTypeNo', width: 150,align:'left',editable:false,hidden:true}
		        ],
		        gridComplete:function(){}
			});
		}
		//确定
		function realSelect(id){
			var ids = $("#defectionCodeList").jqGrid("getGridParam","selarrrow");
			if(ids.length == 0){
				alert("请选择不良代码!");
				return;
			}
			if($.isFunction(window.parent.setDefectionValue)){
				var data = [];
				for(var i=0;i < ids.length;i++){
					var objs = $("#defectionCodeList").jqGrid('getRowData',ids[i]);					
					if(objs){
						data.push({key:objs.defectionCodeNo,value:objs.defectionCodeName,id:objs.id,defectionTypeNo:objs.defectionTypeNo,defectionTypeName:objs.defectionTypeName});
					}
				}	
				if(data.length > 0){
					//选择不良后调用父窗口的setDefectionValue（data）方法，将不良信息返回到打开窗口的父页面
					window.parent.setDefectionValue(data);
				}else{
					alert("选择的值不存在!");
				}
				window.parent.$.colorbox.close();
			}else{
				alert("页面还没有 setDefectionValue 方法!");
			}
		}
		//取消
		function cancel(){
			window.parent.$.colorbox.close();
		}
		//查询方法
		function search(){
			var paramsStr = "";
			$("#searchForm :input").each(function(index,obj){
				if(obj.name){
					var $obj = $(obj);
					if($obj.val()){
						if(paramsStr){
							paramsStr += ",";
						}
						paramsStr += "\"" + obj.name + "\":\"" + $obj.val() + "\""; 
					}
				}
			});
			var postData = {
				searchParams : paramsStr?("{" + paramsStr + "}"):""
			};
			scroolTop = 0;
			$("#defectionCodeList").clearGridData();
			$("#defectionCodeList").jqGrid("setGridParam",{postData:postData}).trigger("reloadGrid");
			//重置展开的节点为空
			$("#defectionCodeList").jqGrid("setGridParam",{postData:{searchParams:''}});
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-west">
		<div style="padding:4px;">
			<div id="structure-tree"></div>
		</div>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" id="btnDiv">
				<button  class='btn' type="button" onclick="realSelect();"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>	
				<button  class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
				<button  class='btn' onclick="javascript:$('#customerSearchDiv').toggle();contentResize();" type="button"><span><span><b class="btn-icons btn-icons-search"></b>显示查询</span></span></button>
			</div>
			<div id="customerSearchDiv" style="padding:6px 10px 0px 8px;display: none;'">
				<form id="searchForm" name="searchForm" onsubmit="return false;">
					<table class="form-table-outside-border" style="width:100%;">
						<tr>
							<td style="padding-left:6px;padding-bottom:4px;">
					 			<span class="field-label">代码</span>
					 			<input type="text" name="defectionCodeNo" style="width:135px;"/>
					 			<span class="field-label">名称</span>
					 			<input type="text" name="defectionCodeName" style="width:135px;"/>
					 			<button class='btn' onclick="javascript:search();$('#customerSearchDiv').toggle();contentResize();"><span><span><b class="btn-icons btn-icons-find"></b>查询</span></span></button>
							 	<button class='btn' type="reset"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div style="padding:6px;">
				<table id="defectionCodeList"></table>
				<div id="pager"></div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>