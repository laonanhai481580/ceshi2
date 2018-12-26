<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/js/CustomCombobox.js"></script>
	<script type="text/javascript" src="${ctx}/js/CustomMultiCombobox.js"></script>
	<style>
		<!--
			#path a:HOVER {
				text-decoration:underline;
			}
		-->
	</style>
	<script type="text/javascript">
		$(function(){
			$("#customCombobox").CustomCombobox({
				value : {id:1,code:'asd',name:'add'}
			});
			$("#customCombobox1").CustomMultiCombobox({
				value : [{id:1,code:'asd',name:'add'}]
			});
		});
		$.extend($.jgrid.defaults,{
			loadComplete : function(){
				var p = $("#bomList")[0].p.postData;
				if(p&&p.searchParameters!=undefined){
					$("#path").html("查询结果");
					window.selParentId = null;
				}
			}
		});
		window.selParentId = '${parentId}';
		
		function operateFormatter(value,o,obj){
			if(value){
				return "<a class=\"small-button-bg\" onclick=\"loadProductBomByStructureAndParent("+value+");\" href=\"#\" style='float:left;margin-left:20px;' title='转到【"+obj.name+"】'><span class=\"ui-icon ui-icon-newwin\" style='cursor:pointer;'></span></a>";
			}else{
				return '';
			}
		}
		function nameFormatter(value,o,obj){
			if(obj.hasChild==true||obj.hasChild=='true'){
				return "<a class=\"small-button-bg\" href=\"#\" onclick=\"loadProductBomByStructureAndParent("+obj.id+");\" style='float:left;margin-right:2px;'><span class=\"ui-icon ui-icon-folder-collapsed\"></span></a>" + value;
			}else if(value){
				return value;
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
		//删除物料BOM
		function deleteProductBom(){
			var ids = $("#bomList").jqGrid("getGridParam","selarrrow");
			if(ids.length==0){
				alert("请选择要删除的物料BOM!");
				return;
			}
			if(confirm("确定要删除选择的物料BOM吗?")){
				$.post('${mfgctx}/base-info/bom/delete.htm',{deleteIds:ids.join(",")},function(result){
					if(result.error){
						alert(result.message);
					}else{
						$("#bomList").trigger("reloadGrid");
						refreshProductStrure();
					}
				},'json');
			}
		}
		
		//导入
		function imports(){
			var postData = $("#bomList").jqGrid("getGridParam","postData");
			var selParentId = postData&&postData.selParentId?postData.selParentId:'';
			var url = '${mfgctx}/base-info/bom/import-bom-form.htm?selParentId='+selParentId;
			$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
				overlayClose:false,
				title:"导入物料BOM",
				onClosed:function(){
					$("#bomList").trigger("reloadGrid");
					refreshProductStrure();
				}
			});
		}
		
		//导入
		function exports(){
			var postData = $("#bomList").jqGrid("getGridParam","postData");
			var selParentId = postData&&postData.selParentId?postData.selParentId:'';
			var url = '${mfgctx}/base-info/bom/exports.htm?selParentId=' + selParentId;
			window.location.href = url;
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
			var postData = $("#bomList").jqGrid("getGridParam","postData");
			data.selParentId = postData?postData.selParentId:'';
			return data;
		}
		function updatePath(id){
			function createPath(result){
				result.unshift({
					id : '',
					name : '产品结构'
				});
				var html = '';
				for(var i=0;i<result.length;i++){
					var obj = result[i];
					if(html){
						html += " >> ";
					}
					if(i<result.length-1){
						html += "<a href='#' title='转到"+obj.name+"' onclick='loadProductBomByStructureAndParent("+obj.id+")'>"+obj.name+"</a>";
					}else{
						html += obj.name;
					}
				}
				$("#path").html(html);
			}
			if(!id){
				createPath([]);
			}else{
				$.post("${mfgctx}/base-info/bom/get-path.htm",{selParentId:id},function(result){
					if(result.error){
						result = [];
					}
					createPath(result);
				},'json');
			}
		}
		//保存一个小时
		function setCookie(name,value){
			var exp = new Date();
			exp.setTime(exp.getTime() + 1*60*60*1000);
			document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
		}
		function getCookie(name){
			var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
			if(arr != null) return unescape(arr[2]); return null;
		}
		//移动物料BOM
		function moveProductBom(){
			alert("clc:" + getCookie("searchStrs"));
			setCookie("searchStrs","另哥斯达黎加");
			return;
			var ids = $("#bomList").jqGrid("getGridParam","selarrrow");
			if(ids.length==0){
				alert("请选择要移动的物料BOM!");
				return;
			}
			var html = '<div id="copyModelBody" class="opt-body" style="overflow-y:auto;"><div class="opt-btn" style="width:400px;line-height:30px;"><button class="btn" onclick="saveMoves(\''+ids.join(",")+'\');"><span><span>确定</span></span></button><span id="copyMessage" style="color:red;padding-left:4px;"></span></div>';
			html += '<div style="padding:6px;padding-top:6px;overflow-y:auto;" id="moveProductStructure"></div>';
			html += '</div>';
			var height = $(window).height()<600?$(window).height()-50:600;
			$.colorbox({
				title : '移动物料BOM',
				html : html,
				height : height,
				onComplete:function(){
					$("#copyModelBody").height(height-50);
					$("#moveProductStructure").height(height-105);
					$("#moveProductStructure").jstree({
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
								"url" : "${mfgctx}/base-info/bom/list-move-structure.htm?deleteIds=" + ids.join(","),
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
//			 				"initially_select" : [ "root"]
						}
					});
				}
			});
		}
		
		function saveMoves(ids){
			var node = $("#moveProductStructure").jstree("get_selected");
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
			$.post("${mfgctx}/base-info/bom/move-boms.htm",{deleteIds:ids,selParentId:parentId},function(result){
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
					$("#bomList").trigger("reloadGrid");
					refreshProductStrure();
				}
			},'json');
		}
		
		function testSelect(){
			var url = '${mfgctx}/common/product-bom-select.htm';
	 		$.colorbox({href:url,iframe:true,
	 			width:$(window).width()<800?$(window).width()-100:800, 
	 			height:$(window).height()<600?$(window).height()-100:600,
	 			overlayClose:false,
	 			title:"选择物料BOM"
	 		});
		}
		function multiSelect(){
			var url = '${mfgctx}/common/product-bom-multi-select.htm';
			if(window.datas){
				var str = "";
				var flag = 0;
				for(var i=0;i<window.datas.length;i++){
					flag++;
					if(str){
						str += ",";
					}
					var j = "";
					var d = window.datas[i];
					for(var pro in d){
						if(j){
							j += ",";
						}
						j += "\"" + pro + "\":\"" + d[pro] + "\""; 
					}
					str += "{" + j + "}";
					if(flag>20){
						break;
					}
				}
				str = "[" + str + "]";
				url += "?hisDatas="+str;
			}
	 		$.colorbox({href:url,iframe:true,
	 			width:$(window).width()<900?$(window).width()-100:900, 
	 			height:$(window).height()<600?$(window).height()-100:600,
	 			overlayClose:false,
	 			title:"选择物料BOM"
	 		});
		}
		function setFullBomValue(datas){
			window.datas = datas;
		}
		function refreshParentIds(){
			$("button").attr("disabled","disabled");
			if(confirm("确定要执行操作吗?")){
				$.post("${mfgctx}/base-info/bom/upate-all-parent-ids-and-level.htm",{},function(result){
					$("button").attr("disabled","");
					alert(result.message);
				},'json');
			}
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="MFG_BASE-INFO_BOM_SAVE">
			  isRight =  true;
			</security:authorize>
			return isRight;
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="bom";
		var treeMenu="bom";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-base-info-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name = "main">
				<div class="opt-btn">
				<security:authorize ifAnyGranted="carmfg-baseInfo-bom-add">
				<button class='btn' onclick="iMatrix.addRow();" type="button" style="float:left;margin-left:4px;"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="carmfg-baseInfo-bom-del">
				<button class='btn' onclick="deleteProductBom();" type="button" style="float:left;margin-left:4px;"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button" style="float:left;margin-left:4px;"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<security:authorize ifAnyGranted="carmfg-baseInfo-bom-import">
				<button class='btn' type="button" onclick="imports();" style="float:left;margin-left:4px;"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="MFG_BASE-INFO_BOM_EXPORTS">
				<button class='btn' type="button" onclick="exports();" style="float:left;margin-left:4px;"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="MFG_BASE-INFO_BOM_MOVE-BOMS">
				<button class='btn' type="button" onclick="moveProductBom();" style="float:left;margin-left:4px;"><span><span><b class="btn-icons btn-icons-move"></b>移到</span></span></button>
				</security:authorize>
					<!-- 
					<button class='btn' onclick="testSelect();" style="float:left;margin-left:4px;"><span><span>单选</span></span></button>
					<button class='btn' onclick="multiSelect();" style="float:left;margin-left:4px;"><span><span>多选</span></span></button>
					<button class='btn' onclick="refreshParentIds();" style="float:left;margin-left:4px;"><span><span>更新物料结构</span></span></button>
					
					<div style="float:left;margin-left:4px;line-height:30px;">
						单选框:<input type="text" id="customCombobox"/>
					</div>
					<div style="float:left;margin-left:4px;line-height:30px;">
						多选框:<input type="text" id="customCombobox1"/>
					</div>
					 -->
					<div style="float:right;line-height:30px;" >
						<b>当前位置:</b>&nbsp;<span id="path">产品结构</span>
					</div>					
				</div>
				<div style="margin-top:15px; margin-left: 10px;">
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="bomList"  url="${mfgctx}/base-info/bom/list-datas.htm?parentId=${parentId}" code="MFG_PRODUCT_BOM" pageName="page"></grid:jqGrid>
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