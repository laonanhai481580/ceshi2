<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%
	String baseUrl = request.getContextPath();
%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript">
	$.extend($.jgrid.defaults,{
		loadComplete : function(){
			var p = $("#itemList")[0].p.postData;
			if(p&&p.searchParameters!=undefined){
				$("#path").html("查询结果");
				window.selParentId = null;
			}
		}
	});
		function operateFormatter(value,o,obj){
			if(obj.id){
				return "<a class=\"small-button-bg\" onclick=\"loadInspectingItemByStructureAndParent("+obj.id+");\" href=\"#\" style='float:left;margin-left:10px;' title='转到【"+obj.name+"】'><span class=\"ui-icon ui-icon-newwin\" style='cursor:pointer;'></span></a>";
			}else{
				return '';
			}
		}
		function nameFormatter(value,o,obj){
			if(obj.hasChild==true||obj.hasChild=='true'){
				return "<a class=\"small-button-bg\" href=\"#\" style='float:left;margin-right:2px;'><span class=\"ui-icon ui-icon-folder-collapsed\"></span></a>" + obj.name;
			}else if(obj.name){
				return obj.name;
			}else{
				return '';
			}
		}
		function $oneditfunc(rowId){
			var val = $("#" + rowId + "_name").val();
			if(val){
				val = val.replace(/<[^>]*>/g,'');
				$("#" + rowId + "_name").val(val);
			}
		}
		function $successfunc(response){
			var result = eval("(" + response.responseText + ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		
		function $processRowData(data){
			var postData = $("#itemList").jqGrid("getGridParam","postData");
			data.selParentId = postData?postData.selParentId:'';
			return data;
		}
		function updatePath(id){
			function createPath(result){
				result.unshift({
					id : '',
					name : '检验类型'
				});
				var html = '';
				for(var i=0;i<result.length;i++){
					var obj = result[i];
					if(html){
						html += " >> ";
					}
					if(i<result.length-1){
						html += "<a href='#' title='转到"+obj.name+"' onclick='loadInspectingItemByStructureAndParent("+obj.id+")'>"+obj.name+"</a>";
					}else{
						html += obj.name;
					}
				}
				$("#path").html(html);
			}
			if(!id){
				createPath([]);
			}else{
				$.post("${sictx}/base-info/item/get-path.htm",{selParentId:id},function(result){
					if(result.error){
						result = [];
					}
					createPath(result);
				},'json');
			}
		}
		//删除检验项目
		function deleteInspectintItem(){
			var ids = $("#itemList").jqGrid("getGridParam","selarrrow");
			if(ids.length==0){
				alert("请选择要删除的检验项目!");
				return;
			}
			if(confirm("确定要删除选择的检验项目吗?")){
				$.post('${sictx}/base-info/item/delete.htm',{deleteIds:ids.join(",")},function(result){
					if(result.error){
						alert(result.message);
					}else{
						$("#itemList").trigger("reloadGrid");
						refreshStrure();
					}
				},'json');
			}
		}
		//移动检验项目
		function moveInspectingItem(){
			var ids = $("#itemList").jqGrid("getGridParam","selarrrow");
			if(ids.length==0){
				alert("请选择要移动的检验项目!");
				return;
			}
			var html = '<div id="copyModelBody" class="opt-body" style="overflow-y:auto;"><div class="opt-btn" style="width:400px;line-height:30px;"><button class="btn" type="button" onclick="saveMoves(\''+ids.join(",")+'\');"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button><span id="copyMessage" style="color:red;padding-left:4px;"></span></div>';
			html += '<div style="padding:6px;padding-top:6px;overflow-y:auto;" id="moveStructure"></div>';
			html += '</div>';
			var height = $(window).height()<600?$(window).height()-50:600;
			$.colorbox({
				title : '移动检验项目',
				html : html,
				height : height,
				onComplete:function(){
					$("#copyModelBody").height(height-50);
					$("#moveStructure").height(height-105)
					.jstree({
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
								"url" : "${sictx}/base-info/item/list-move-structure.htm?deleteIds=" + ids.join(","),
								data : function(n){
									var id = n.attr("id");
									return {date:(new Date()).getTime(),selParentId:id=='root'?'':id}	
								}
							}
						},
						plugins : ["themes","json_data","ui","crrm",'types'],
						core : { "initially_open" : ["root"] },
						types : {
							valid_children:'drive',
							types:{
								drive:{
									icon:{
										image:'${sictx}/images/_drive.png'
									}
								}
							}
						},
						"ui" : {
//			 				"initially_select" : [ "root"]
						}
					});
				}
			});
		}
		
		function saveMoves(ids){
			var node = $("#moveStructure").jstree("get_selected");
			if(!node){
				alert("请选择移动目标!");
				return;
			}
			var parentId = node.attr("id");
			parentId = parentId=='root'?"":parentId;
			$("#copyModelBody :input").attr("disabled","disabled");
			$("#copyMessage").html("正在移动,请稍候.");
			var time = 1;
			var dd = setInterval(function(){
				time++;
				if(time>8){
					time = 1;
				}
				var html = '正在移动,请稍候';
				for(i=0;i<time;i++){
					html += ".";
				}
				$("#copyMessage").html(html);
			},500);
			$.post("${sictx}/base-info/item/move-items.htm",{deleteIds:ids,selParentId:parentId},function(result){
				clearInterval(dd);
				$("#copyModelBody :input").attr("disabled","");
				$("#copyMessage").html(result.message);
				if(result.error){
					alert(result.message);
					setTimeout(function(){
						$("#copyMessage").html("");
					},3000);
				}else{
					$.colorbox.close();
					$("#itemList").trigger("reloadGrid");
					refreshStrure();
				}
			},'json');
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="SI_INSPECTION_BASE_ITEM_SAVE">
			  isRight =  true;
			</security:authorize>
			return isRight;
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="inspectingItem";
		var treeMenu = "item";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/si-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/si-base-info-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name = "main">
				<div class="opt-btn">
				<security:authorize ifAnyGranted="SI_INSPECTION_BASE_ITEM_SAVE">
					<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="SI_INSPECTION_BASE_ITEM_DELETE">
					<button class='btn' onclick="deleteInspectintItem();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="SI_INSPECTION_BASE_ITEM_MOVE">
					<button  class='btn' onclick="moveInspectingItem();" type="button"><span><span><b class="btn-icons btn-icons-move"></b>移到</span></span></button>	
					</security:authorize>
					<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<div style="float:right;line-height:30px;margin-right:4px;" >
						<b>当前位置:</b>&nbsp;<span id="path">检验类型</span>
					</div>
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>	
				</div>
				<div style="margin-top:15px; margin-left: 10px;">
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="itemList"  url="${sictx}/base-info/item/list-datas.htm" code="SI_INSPECTING_ITEM" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>