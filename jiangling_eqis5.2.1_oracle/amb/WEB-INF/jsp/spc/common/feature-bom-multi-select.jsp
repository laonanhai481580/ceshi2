<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.spc.processdefine.service.ProcessDefineManager"%>
<%@ include file="/common/taglibs.jsp"%>
<%Map<String,Integer> pointKeyMap = ProcessDefineManager.getPointKeyMap();%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>选择质量特性</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		function contentResize(){
			var searchDivHeight = 0;
			if($("#customerSearchDiv").css("display")=='block'){
				searchDivHeight = $("#customerSearchDiv").height();
			}
			$("#featureList").jqGrid("setGridWidth",$("#btnDiv").width());
			$("#featureList").jqGrid('setGridHeight',$(document).height() - $("#btnDiv").height() - 70 - searchDivHeight);
		}
		
		var secMenu="baseInfo";
		var thirdMenu="bom";
		var topMenu='aaa';
		var initially_select = '${processPoint.id}';
		$(document).ready(function(){
			createStructureTree();
			contentResize();
		});
		//创建树
		function createStructureTree(){
			$("#structure-tree").jstree({ 
				json_data : {
					data : [
						{ 
							"data" : "产品列表", 
							"state" : "closed",
							attr:{
								id:'root',
								level: 0,
								rel:'drive'
							}
						}
					],
					ajax : { 
						"url" : "${spcctx}/base-info/process-define/point-list.htm",
						data : function(n){
							return {date:(new Date()).getTime()};	
						}
					}
				},
				plugins : [ "themes", "json_data","ui","crrm",'types'],
				core : { "initially_open" : ["root"] },
				types : {
					valid_children:'drive',
					types:{
						drive:{
							icon:{
								image:'${mfgctx}/images/_drive.png'
							}
						}
					}
				},
				ui : {
					"initially_select" : [ "${processPoint.id}" ]
				}
			}).bind("select_node.jstree",function(e,data){
				var id = data.rslt.obj.attr("id");
				var name = data.rslt.obj.attr("name");
				id = id == 'root'?'':id;
				if(id != null){
					loadFeatureByStructure(id,name);
				}else{
					loadFeatureByStructure('');
				}
			});
		}
		//加载质量特性 
		function loadFeatureByStructure(Id,Name){
			if(initially_select == Id){
				return;
			}
			initially_select = Id || '';
			if(initially_select){
				$("#featureList")[0].p.postData = {processId : initially_select,"_list_code":"SPC_QUALITY_FEATURE"};
				$("#featureList").jqGrid("setGridParam",{page:1}).trigger("reloadGrid");
				$("#featureList").jqGrid("setCapition",Name + "的代码");
			}else{
				$("#featureList").clearGridData();
			}
		}
		//确定
		function realSelect(){
			var ids = jQuery("#featureList").getGridParam('selarrrow');;
			if(ids.length == 0){
				alert("请选择质量特性!");
				return;
			}
			if($.isFunction(window.parent.setFeatureValue)){
				var data = [];
				for(var i=0;i < ids.length;i++){
					var objs = $("#featureList").jqGrid('getRowData',ids[i]);					
					if(objs){
						data.push({key:objs.code,value:objs.name,id:objs.id,targetId:objs.targetId,sampleCapacity:objs.sampleCapacity,upperLimit:objs.upperLimit,lowerLimit:objs.lowerLimit});
					}
				}	
				if(data.length > 0){
					//选择质量特性后调用父窗口的setFeatureValue（data）方法，将质量特性信息返回到打开窗口的父页面
					window.parent.setFeatureValue(data);
				}else{
					alert("选择的值不存在!");
				}
				window.parent.$.colorbox.close();
			}else{
				alert("页面还没有 setFeatureValue()方法!");
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
			$("#featureList").clearGridData();
			$("#featureList").jqGrid("setGridParam",{postData:postData}).trigger("reloadGrid");
			//重置展开的节点为空
			$("#featureList").jqGrid("setGridParam",{postData:{searchParams:''}});
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-west">
		<div style="padding:4px;height: 98%;overflow: auto;">
			<div id="structure-tree"></div>
		</div>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" id="btnDiv">
				<button class='btn' type="button" onclick="realSelect();"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>	
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
				<button class='btn' onclick="javascript:$('#customerSearchDiv').toggle();contentResize();" type="button"><span><span><b class="btn-icons btn-icons-search"></b>显示查询</span></span></button>
			</div>
			<div id="customerSearchDiv" style="padding:6px 10px 0px 8px;display: none;'">
				<form id="searchForm" name="searchForm" onsubmit="return false;">
					<table class="form-table-outside-border" style="width:100%;">
						<tr>
							<td style="padding-left:6px;padding-bottom:4px;">
								<span class="field-label">特性名称</span>
					 			<input type="text" name="name" style="width:135px;"/>
					 			<span class="field-label">特性类型</span>
					 			<input type="text" name="processPoint_name" style="width:135px;"/>
					 			<button class='btn' onclick="javascript:search();$('#customerSearchDiv').toggle();contentResize();"><span><span><b class="btn-icons btn-icons-find"></b>查询</span></span></button>
							 	<button class='btn' type="reset"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
					 			<!-- <span class="field-label">代码</span>
					 			<input type="text" name="defectionCodeNo" style="width:135px;"/>
					 			<span class="field-label">名称</span>
					 			<input type="text" name="defectionCodeName" style="width:135px;"/>
					 			<button class='btn' onclick="javascript:search();$('#customerSearchDiv').toggle();contentResize();"><span><span><b class="btn-icons btn-icons-find"></b>查询</span></span></button>
							 	<button class='btn' type="reset"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button> -->
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div style="padding:6px;">
				<form id="contentForm" name="contentForm" method="post"  action="">
					<grid:jqGrid gridId="featureList" url="${spcctx}/common/feature-list-datas.htm" code="SPC_FEATURE_SELECT" pageName="page"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
</html>