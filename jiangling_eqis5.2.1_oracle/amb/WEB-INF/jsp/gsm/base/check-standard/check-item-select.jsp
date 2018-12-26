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
		var isUsingComonLayout=false;
		function initLayout(){
			$(".opt-body").height(34);
			var height = $(window).height()-16-$(".opt-body").height();
			$(".left").width($("#header").width()-230-24);
			$(".right").width(230);
			$(".split").width(24);
			$("#left-tabs").height(height);
			$("#right-tabs").height(height);
			//左边的宽度
			var leftWidth = 160;
			$(".left-left").width(leftWidth);
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
			$("#leftInspectingItemList").jqGrid("setGridWidth",width);
		}
		$(document).ready(function(){
			initLayout();
			$("#left-tabs").tabs();
			$("#right-tabs").tabs();
			createLeftBom();
			createRightBom();
			//createStructureTree();
		});
		
		//创建物料BOM
		function createRightBom(){
			var colModel = [
				{label:'id',name:'id',index:'id', width:1,hidden:true,key:true},
				{label:'项目名称',name:'itemName',index:'itemName'}
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
		//加载设备信息
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
		   		{label:'项目名称',name:'itemName',index:'itemName', width:120},
		   		{label:'标准值',name:'standardValue',index:'standardValue', width:120},
		   		{label:'允许误差	',name:'allowableError',index:'allowableError', width:120},	
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
				url:'${gsmctx}/base/check-item/list-datas.htm',
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
					<div id="left-tabs-1" style="padding:0px;">
						<table cellpadding="0" cellspacing="0" style="width:100%;height:100%;">
							<tr>
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