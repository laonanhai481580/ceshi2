<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String baseUrl = request.getContextPath();
	String hisDatas = request.getParameter("hisDatas");
	if(hisDatas==null||!(hisDatas.startsWith("[")&&hisDatas.endsWith("]"))){
		hisDatas = "[]";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
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
// 		var url = '${mfgctx}/common/product-bom-select.htm';
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
			$(".left").width($("#header").width()-300-24);
			$(".right").width(300);
			$(".split").width(24);
			$("#left-tabs").height(height);
			$("#right-tabs").height(height);
			//左边的宽度
			$(".left-right .main").width($(".left").width());
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
			$("#leftBomList").jqGrid("setGridWidth",width);
		}
		$(document).ready(function(){
			initLayout();
			$("#left-tabs").tabs();
			$("#right-tabs").tabs();
			createLeftBom();
			createRightBom();
// 			createStructureTree();
		});
		//创建树
		function createStructureTree(){
			$("#product-structure-tree").jstree({
				json_data : {
					data : [
						{ 
							"data" : "产品结构", 
							"state" : "closed",
							attr:{
								id:'root',
								level : 0,
								rel:'drive'
							}
						}
					],
					ajax : { 
						"url" : "${mfgctx}/base-info/bom/list-structure.htm",
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
								image:'${mfgctx}/images/_drive.png'
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
				loadProductBomByStructureAndParent(id);
			});
		}
		//加载产品BOM 
		function loadProductBomByStructureAndParent(parentId){
			if(window.selParentId == parentId){
				return;
			}
			window.selParentId = parentId || '';
			$("#leftBomList")[0].p.postData = {selParentId:parentId};
			$("#leftBomList").jqGrid("setGridParam",{page:1}).trigger("reloadGrid");
		}
		function search(){
			var val = $("#searchInput").val();
			window.selParentId = null;
			$("#leftBomList")[0].p.postData = {customSearch:val};
			$("#leftBomList").jqGrid("setGridParam",{page:1}).trigger("reloadGrid");
		}
		//创建物料BOM
		function createLeftBom(){
			var colModel = [
		   		{label:'id',name:'id',index:'id', width:1,hidden:true,key:true},
		   		{label:'modelSpecification',name:'modelSpecification',index:'modelSpecification', width:1,hidden:true},
		   		{label:'名称',name:'name',index:'name', width:160,formatter:nameFormatter},
		   		{label:'编码',name:'code',index:'code', width:80},
		   		{label:'型号',name:'model',index:'model', width:180},
		   		{label:'物料类别',name:'materialType',index:'materialType', width:70,hidden:true},	
		   		{label:'追溯类型',name:'ascendType',index:'ascendType', width:70,hidden:true},			
		   		{label:'重要程度',name:'importance',index:'importance', width:65,hidden:true},		
		   		{label:'标配',name:'assemblyNum',index:'assemblyNum', width:65,hidden:true},	
		   		{label:'hasChild',name:'hasChild',index:'hasChild', width:65,hidden:true},		
		   		{label:'备注',name:'remark',index:'remark', width:180,hidden:true},	
		   		{label:'操作',name:'aa',index:'aa', width:40,formatter:operateFormatter,hidden:true}		
		   	];
			$("#leftBomList").jqGrid({
				rownumbers:true,
				gridComplete:function(){},
				loadComplete:function(){
					$("#leftBomList")[0].p.selarrrow = [];
					var ids = $("#rightBomList").jqGrid("getDataIDs");
					for(var i=0;i<ids.length;i++){
						$("#leftBomList").jqGrid("setSelection",ids[i]);						
					}
				},
				postData : {},
				rowNum:15,
				datatype: "json",
				height : $(".left").height()-125,
				url:'${mfgctx}/base-info/bom/list-datas.htm',
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
		   		{label:'modelSpecification',name:'modelSpecification',index:'modelSpecification', width:1,hidden:true},
		   		{label:'名称',name:'name',index:'name', width:130},
		   		{label:'编码',name:'code',index:'code', width:120},
		   		{label:'物料类别',name:'materialType',index:'materialType', width:70,hidden:true},	
		   		{label:'追溯类型',name:'ascendType',index:'ascendType', width:70,hidden:true},			
		   		{label:'重要程度',name:'importance',index:'importance', width:65,hidden:true},
		   		{label:'标配',name:'assemblyNum',index:'assemblyNum', width:65,hidden:true},
		   		{label:'备注',name:'remark',index:'remark', width:180,hidden:true}	
		   	];
			$("#rightBomList").jqGrid({
				rownumbers:true,
				multiselect: true,
				data : <%=hisDatas%>,
				gridComplete:function(){},
				datatype: "local",
				height : $(".right").height()-65,
			   	colModel : colModel
			});
		}
		
		function operateFormatter(value,o,obj){
			if(obj.hasChild==true||obj.hasChild=='true'){
				return "<a class=\"small-button-bg\" onclick=\"loadProductBomByStructureAndParent("+obj.id+");\" href=\"#\" style='float:left;margin-left:10px;' title='转到【"+obj.name+"】'><span class=\"ui-icon ui-icon-newwin\" style='cursor:pointer;'></span></a>";
			}else{
				return '';
			}
		}
		
		function nameFormatter(value,o,obj){
			if(obj.hasChild==true||obj.hasChild=='true'){
				return "<a class=\"small-button-bg\" href=\"#\" onclick=\"loadProductBomByStructureAndParent("+obj.id+");\" style='float:left;margin-right:2px;' title='转到【"+obj.name+"】'><span class=\"ui-icon ui-icon-folder-collapsed\"></span></a>" + value;
			}else if(value){
				return value;
			}else{
				return '';
			}
		}
		function add(){
			var ids = $("#leftBomList").jqGrid("getGridParam","selarrrow");
			var rightIds = "," + $("#rightBomList").jqGrid("getDataIDs").join(",") + ",";
			var datas = [];
			for(var i=0;i<ids.length;i++){
				var d = $("#leftBomList").jqGrid("getRowData",ids[i]);
				if(rightIds.indexOf("," + d.id + ",")==-1){
					var obj = {};
					for(var pro in d){
						var val = d[pro];
						if(val&&val.indexOf("<")>-1){
							val = val.replace(/<[^<]*>/g,'');
						}
						obj[pro] = val;
					}
					$("#rightBomList").jqGrid("addRowData",obj.id,obj);
				}
			}
		}
		function remove(){
			var ids = $("#rightBomList").jqGrid("getGridParam","selarrrow");
			var ids = ids.join(",").split(",");
			for(var i=0;i<ids.length;i++){
				$("#leftBomList").jqGrid("resetSelection",ids[i]);
				$("#rightBomList").jqGrid("delRowData",ids[i]);
			}
			$("#rightBomList")[0].p.selarrrow = [];
		}
		//确定
		function realSelect(id){
			var ids = $("#rightBomList").jqGrid("getDataIDs");
			var datas = [];
			for(var i=0;i<ids.length;i++){
				var d = $("#rightBomList").jqGrid("getRowData",ids[i]);
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
			if($.isFunction(window.parent.setMultiBomValue)){//获取全部的值
				window.parent.setMultiBomValue(datas);
				window.parent.$.colorbox.close();
			}else if($.isFunction(window.parent.setBomValue)){//获取简洁的值
				var objs = [];
				for(var i=0;i<datas.length;i++){
					var data = datas[i];
					if(data){
						objs.push({key:data.code,value:data.name});
					}
				}
				window.parent.setBomValue(objs);
				window.parent.$.colorbox.close();
			}else{
				alert("页面还没有 setBomValue 方法!");
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
						<li><a href="#left-tabs-1">所有物料</a>
						</li>
					</ul>
					<div id="left-tabs-1" style="padding:0px;">
						<table cellpadding="0" cellspacing="0" style="width:100%;height:100%;">
							<tr>
								<td class="left-right">
									<div class="opt-body">
										<div class="opt-btn" style="line-height:30px;padding-left:4px;">
											物料编码/名称:&nbsp;<input style="width:120px;" id="searchInput"></input>
											<button  class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
											<button  class='btn' type="button" onclick="$('#searchInput').val('').focus();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
										</div>
									</div>
									<div class="main" style="padding:2px;padding-left:5px;">
										<div id="left-page"></div>
										<table id="leftBomList"></table>
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
						<li><a href="#right-tabs-1">已选物料</a>
						</li>
					</ul>
					<div id="right-tabs-1" style="padding:0px;">
						<div class="main" style="padding:2px;padding-left:10px;">
							<table id="rightBomList"></table>
						</div>
					</div>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>