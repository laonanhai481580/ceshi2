<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String baseUrl = request.getContextPath();
	String hisDatas = "[]";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<style type="text/css">
		.collapse{
			height:49px;
			width:6px;
			cursor:pointer;
			background-image:url('<%=baseUrl%>/images/collapse.jpg');
		}
		.expand{
			width:6px;
			height:49px;
			cursor:pointer;
			background-image:url('<%=baseUrl%>/images/expand.jpg');
		}
	</style>
	<script type="text/javascript">
///////////////页面使用方法////////////////////////////////////
//	//选择物料BOM
// 	function selectBomValue(){
// 		var url = '${sictx}/common/product-bom-select.htm';
// 		$.colorbox({href:url,iframe:true, innerWidth:700, innerHeight:400,
// 			overlayClose:false,
// 			title:"选择物料BOM"
// 		});
// 	}
	
//  //选择之后的方法 data格式{key:'a',value:'a'}
// 	function setBomValue(data){
// 		alert("select is key:" + data.key + ",value:" + data.value);
// 	}
///////////////页面使用方法结束////////////////////////////////////	
// 		function contentResize(){
// 			var searchDivHeight = 0;
// 			if($("#customerSearchDiv").css("display")=='block'){
// 				searchDivHeight = $("#customerSearchDiv").height();
// 			}
// 			$("#bomList").jqGrid("setGridWidth",$("#btnDiv").width());
// 			$("#bomList").jqGrid('setGridHeight',$(document).height() - $("#btnDiv").height() - 45 - searchDivHeight);
// 		}
		var isUsingComonLayout=false;
		function initLayout(){
			$(".opt-body").height(34);
			var height = $(window).height()-16-$(".opt-body").height();
			$(".left").width($("#header").width()-230-24);
			$(".right").width(230);
			$(".split").width(24);
			$("#left-tabs").height(height);
			$("#right-tabs").height(height);
			$("#structure-tree").height(height-65);
			//左边的宽度
			var leftWidth = 160;
			$(".left-left").width(leftWidth);
			$("#structure-tree").width(leftWidth - 10);
			$(".left-right .main").width($(".left").width()-166);
			$("#right-tabs-1 .main").width($(".right").width());
		}
		function contentResize(obj){
			$(obj).removeClass();
			var display = $(".left-left").css("display");
			var width = 0;
			if(display=='none'){
				$(".left-left").css("display","table-cell");
				width = $(".left").width() - 166 - 10;
				$(obj).addClass("collapse").attr("title","Close");	
			}else{
				$(".left-left").css("display","none");
				width = $(".left").width() - 6 - 15;	
				$(obj).addClass("expand").attr("title","Open");		
			}
			$("#leftInspectingItemList").jqGrid("setGridWidth",width);
		}
		$(document).ready(function(){
			initLayout();
			$("#left-tabs").tabs();
			$("#right-tabs").tabs();
			createLeftBom();
			createRightBom();
			createStructureTree();
		});
		//创建树
		function createStructureTree(){
			$("#structure-tree").jstree({
				json_data : {
					data : [
						{ 
							"data" : "检验类型", 
							"state" : "closed",
							attr:{
								id:'root',
								level : 0,
								rel:'drive'
							}
						}
					],
					ajax : { 
						"url" : "${sictx}/base-info/item/list-structure.htm",
						data : function(n){
							var id = n.attr("id");
							return {date:(new Date()).getTime(),selParentId:id=='root'?'':id}	
						}
					}
				},
				plugins : ["themes","json_data","ui","crrm","contextmenu",'types'],
				core : { "initially_open" : ["root"] },
				types : {
					valid_children:'drive',
					types:{
						drive:{
							icon:{
							}
						}
					}
				},
				"ui" : {
//	 				"initially_select" : [ "root"]
				}
			}).bind("select_node.jstree",function(e,data){
				var id = data.rslt.obj.attr("id");
				id = id == 'root'?'':id;
				loadInspectingItemByStructureAndParent(id);
			});
		}
		//加载检验项目
		function loadInspectingItemByStructureAndParent(parentId){
			if(window.selParentId == parentId){
				return;
			}
			window.selParentId = parentId || '';
			$("#leftInspectingItemList")[0].p.postData = {selParentId:parentId};
			$("#leftInspectingItemList").jqGrid("setGridParam",{page:1}).trigger("reloadGrid");
		}
		function search(){
			var val = $("#searchInput").val();
			window.selParentId = null;
			$("#leftInspectingItemList")[0].p.postData = {customSearch:val};
			$("#leftInspectingItemList").jqGrid("setGridParam",{page:1}).trigger("reloadGrid");
		}
		//创建物料BOM
		function createLeftBom(){
			var colModel = [
		   		{label:'id',name:'id',index:'id', width:1,hidden:true,key:true},
		   		{label:'项目名称',name:'name',index:'name', width:130,formatter:nameFormatter},
		   		{label:'检验方法',name:'method',index:'method', width:120},
		   		{label:'规格',name:'standards',index:'standards', width:70,hidden:true},	
		   		{label:'单位',name:'unit',index:'unit', width:70,hidden:true},			
		   		{label:'统计类型',name:'countType',index:'countType', width:65,hidden:true},		
		   		{label:'hasChild',name:'hasChild',index:'hasChild', width:65,hidden:true},		
		   		{label:'备注',name:'remark',index:'remark', width:180,hidden:true},	
		   		{label:'操作',name:'aa',index:'aa', width:40,formatter:operateFormatter}		
		   	];
			$("#leftInspectingItemList").jqGrid({
				rownumbers:true,
				gridComplete:function(){},
				loadComplete:function(){
					$("#leftInspectingItemList")[0].p.selarrrow = [];
					var ids = $("#rightInspectingItemList").jqGrid("getDataIDs");
					for(var i=0;i<ids.length;i++){
						$("#leftInspectingItemList").jqGrid("setSelection",ids[i]);						
					}
				},
				postData : {},
				rowNum:15,
				datatype: "json",
				height : $(".left").height()-125,
				url:'${sictx}/base-info/item/list-datas.htm?',
				prmNames:{
					rows:'page.pageSize',
					page:'page.pageNo',
					sort:'page.orderBy',
					order:'page.order'
				},
			   	colModel : colModel,
			   	pager:'#left-page'
			});
		}
		//创建物料BOM
		function createRightBom(){
			var colModel = [
				{label:'id',name:'id',index:'id', width:1,hidden:true,key:true},
				{label:'项目名称',name:'name',index:'name'}
		   	];
			$("#rightInspectingItemList").jqGrid({
				rownumbers:true,
				multiselect: true,
				data : [],
				gridComplete:function(){},
				datatype: "local",
				height : $(".right").height()-65,
			   	colModel : colModel
			});
		}
		
		function operateFormatter(value,o,obj){
			if(obj.hasChild==true||obj.hasChild=='true'){
				return "<a class=\"small-button-bg\" onclick=\"loadInspectingItemByStructureAndParent("+obj.id+");\" href=\"#\" style='float:left;margin-left:10px;' title='转到【"+obj.name+"】'><span class=\"ui-icon ui-icon-newwin\" style='cursor:pointer;'></span></a>";
			}else{
				return '';
			}
		}
		
		function nameFormatter(value,o,obj){
			if(obj.hasChild==true||obj.hasChild=='true'){
				return "<a class=\"small-button-bg\" href=\"#\" style='float:left;margin-right:2px;'><span class=\"ui-icon ui-icon-folder-collapsed\"></span></a>" + value;
			}else if(value){
				return value;
			}else{
				return '';
			}
		}
		function add(){
			var ids = $("#leftInspectingItemList").jqGrid("getGridParam","selarrrow");
			var rightIds = "," + $("#rightInspectingItemList").jqGrid("getDataIDs").join(",") + ",";
			var datas = [];
			for(var i=0;i<ids.length;i++){
				var d = $("#leftInspectingItemList").jqGrid("getRowData",ids[i]);
				if(rightIds.indexOf("," + d.id + ",")==-1){
					var obj = {};
					for(var pro in d){
						var val = d[pro];
						if(val&&val.indexOf("<")>-1){
							val = val.replace(/<[^<]*>/g,'');
						}
						obj[pro] = val;
					}
					$("#rightInspectingItemList").jqGrid("addRowData",obj.id,obj);
				}
			}
		}
		function remove(){
			var ids = $("#rightInspectingItemList").jqGrid("getGridParam","selarrrow");
			var ids = ids.join(",").split(",");
			for(var i=0;i<ids.length;i++){
				$("#leftInspectingItemList").jqGrid("resetSelection",ids[i]);
				$("#rightInspectingItemList").jqGrid("delRowData",ids[i]);
			}
			$("#rightInspectingItemList")[0].p.selarrrow = [];
			$("#leftInspectingItemList")[0].p.selarrrow = [];
		}
		//确定
		function realSelect(id){
			var ids = $("#rightInspectingItemList").jqGrid("getDataIDs");
			var datas = [];
			for(var i=0;i<ids.length;i++){
				var d = $("#rightInspectingItemList").jqGrid("getRowData",ids[i]);
				var obj = {};
				for(var pro in d){
					var val = d[pro];
					if(val&&val.indexOf("<")>-1){
						val = val.replace(/<[^<]*>/g,'');
					}
					obj[pro] = val;
				}
				datas.push(obj);
			}
			if($.isFunction(window.parent.setInspectingItemValue)){//获取全部的值
				window.parent.setInspectingItemValue(datas);
				window.parent.$.colorbox.close();
			}else{
				alert("页面还没有 setInspectingItemValue 方法!");
			}
		}
		
		//取消
		function cancel(){
			window.parent.$.colorbox.close();
		}
	</script>
</head>

<body>
	<div class="opt-body" style="margin-bottom:2px;" id="header">
		<div class="opt-btn">
		<button  class='btn' type="button" onclick="realSelect();"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>	
		<button  class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
		</div>
	</div>
	<table>
		<tr>
			<td class="left" valign="top">
				<div id="left-tabs">
					<ul>
						<li><a href="#left-tabs-1">所有检验项目</a>
						</li>
					</ul>
					<div id="left-tabs-1" style="padding:0px;">
						<table cellpadding="0" cellspacing="0" style="width:100%;height:100%;">
							<tr>
								<td class="left-left" style="width:100px;"valign="top">
									<div class="opt-body">
										<div class="opt-btn" style="line-height:30px;padding-left:4px;">
											检验类型
										</div>
									</div>
									<div style="padding:4px;overflow:auto;" id="structure-tree">
									</div>
								</td>
								<td class="left-split" style="width:6px;background:#AED5F9;" valign="middle">
									<div class="collapse" onclick="contentResize(this)" title="Close">&nbsp;</div>
								</td>
								<td class="left-right">
									<div class="opt-body">
										<div class="opt-btn" style="line-height:30px;padding-left:4px;">
											项目名称:&nbsp;<input style="width:120px;" id="searchInput"></input>
											<button  class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
											<button  class='btn' type="button" onclick="$('#searchInput').val('').focus();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
										</div>
									</div>
									<div class="main" style="padding:2px;padding-left:5px;">
										<div id="left-page"></div>
										<table id="leftInspectingItemList"></table>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</td>
			<td class="split" valign="top" align="center" style="padding-top:80px;">
				<a class="small-button-bg" href="#" onclick="add()" style='' title='添加所选'><span class="ui-icon ui-icon-carat-1-e"></span></a><br/>
				<a class="small-button-bg" href="#" onclick="remove()" style='' title='移除所选'><span class="ui-icon ui-icon-carat-1-w"></span></a>
			</td>
			<td class="right" valign="top">
				<div id="right-tabs">
					<ul>
						<li><a href="#right-tabs-1">已选项目</a>
						</li>
					</ul>
					<div id="right-tabs-1" style="padding:0px;">
						<div class="main" style="padding:2px;padding-left:10px;">
							<table id="rightInspectingItemList"></table>
						</div>
					</div>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>