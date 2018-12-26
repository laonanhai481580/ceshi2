<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='gsm.title-1'/></title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		function contentResize(){
			var searchDivHeight = 0;
			if($("#customerSearchDiv").css("display")=='block'){
				searchDivHeight = $("#customerSearchDiv").height();
			}
			$("#gsmList").jqGrid("setGridWidth",$("#btnDiv").width());
			$("#gsmList").jqGrid('setGridHeight',$(document).height() - $("#btnDiv").height() - 70 - searchDivHeight);
		}
		var secMenu="baseInfo";
		var thirdMenu="bom";
		var topMenu='aaa';
		var initially_select = '${gsmCodeRules.id}';
		$(document).ready(function(){
			createStructureTree();
			createBom();
			contentResize();
		});
		//创建 分类树
		function createStructureTree(){
			var children = ${gsmTypeMaps};
			$("#structure-tree").jstree({ 
				"json_data" : {
					"data" : [
						{ 
							"data" : "<s:text name='器具分类'/>", 
							"state" : "open",
							attr:{
								id:'root',
								level : 0,
								rel:'drive',
							},
							children : children,
						}]
				},
				"plugins" : [ "themes", "json_data", "ui" ],
			}).bind("select_node.jstree",function(e,data){
				var id = data.rslt.obj.attr("id");
				var name = data.rslt.obj.attr("typeName");
				if(id != null){
					loadCodeBomByTypeAndParent(id,name);
				}else{
					loadCodeBomByTypeAndParent('');
				};
			});
		}
		//加载计量器具 
		function loadCodeBomByTypeAndParent(id,Name){
			if(initially_select == id){
				return;
			}
			initially_select = id || '';
			if(initially_select){
				$("#gsmList").jqGrid("setGridParam",{postData:{id : initially_select}}).trigger("reloadGrid");
				$("#gsmList").jqGrid("setCapition",Name);
			}else{
				$("#gsmList").clearGridData();
			}
		}
		//创建计量器具
		function createBom(){
			$("#gsmList").jqGrid({
				url:'${gsmctx}/common/gsm-bom-datas.htm',
				postData : {id : initially_select},
				datatype: 'json',
				mtype: "POST",
				rowNum: 20,
				rownumbers: true,
				multiselect: true,
				pager: '#pager',
				colNames:['',
				          "<s:text name='器具编号'/>",
				          "<s:text name='器具名称'/>",
				          "<s:text name='型号/规格'/>"], 
				colModel:[{name:'id',index:'id',width:1,hidden:true,editable:false},
				          {name:'measurementSerialNo',index:'measurementSerialNo',align:'left',width:200,editable:false},
						  {name:'measurementName',index:'measurementName',align:'left',width:200,editable:false},
						  {name:'measurementSpecification',index:'measurementSpecification',align:'left',width:200,editable:false},
						  ], 
		        gridComplete:function(){}
			});
		}
		//确定
		function realSelect(){
			var ids = jQuery("#gsmList").getGridParam('selarrrow');
			if(ids.length == 0){
				alert("<s:text name='gsm.message-18'/>");
				return;
			}
			if($.isFunction(window.parent.setGsmInput)){
				var data = [];
				for(var i=0;i < ids.length;i++){
					var objs = $("#gsmList").jqGrid('getRowData',ids[i]);					
					if(objs){
						data.push({id:ids[i],measurementNo:objs.measurementNo,measurementSerialNo:objs.measurementSerialNo,measurementName:objs.measurementName,measurementSpecification:objs.measurementSpecification});
					}
				}	
				if(data.length > 0){
					//调用父窗口的setGsmInput（data）方法
					window.parent.setGsmInput(data);
				}else{
					alert("选择的值不存在!");
				}
				window.parent.$.colorbox.close();
			}else{
				alert("页面还没有 setGsmInput 方法!");
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
				searchParams : paramsStr?("{" + paramsStr + "}"):"",
			};
			scroolTop = 0;
			$("#gsmList").clearGridData();
			$("#gsmList").jqGrid("setGridParam",{postData:postData,}).trigger("reloadGrid");
			//重置展开的节点为空
			$("#gsmList").jqGrid("setGridParam",{postData:{searchParams:'',}});
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
				<button class='btn' onclick="realSelect();"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>
				<button class='btn' onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
				<button class='btn' onclick="javascript:$('#customerSearchDiv').toggle();contentResize();"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
			</div>
			<div id="customerSearchDiv" style="padding:6px 10px 0px 8px;display: none;'">
				<form id="searchForm" name="searchForm" onsubmit="return false;">
					<table class="form-table-outside-border" style="width:100%;">
						<tr>
							<td style="padding-left:6px;padding-bottom:4px;">
					 			<span class="field-label">器具编号</span>
					 			<input type="text" name="measurementNo" style="width:135px;"/>
					 			<span class="field-label">新建器具编号</span>
					 			<input type="text" name="measurementSerialNo" style="width:135px;"/>
					 			<span class="field-label">器具名称</span>
					 			<input type="text" name="measurementName" style="width:135px;"/>
								<button class='btn' onclick="javascript:search();$('#customerSearchDiv').toggle();contentResize();"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
							 	<button class='btn' type="reset"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div style="padding:6px;">
				<table id="gsmList"></table>
				<div id="pager"></div>
			</div>
		</div>
	</div>
</body>
</html>