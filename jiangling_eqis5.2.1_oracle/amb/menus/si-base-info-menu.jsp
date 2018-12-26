<%@page import="com.ambition.carmfg1.ProductBomManager"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.ambition.util.common.CommonUtil1"%>
<%
	Map<String,Integer> structureKeyMap = IqcProductBomManager.getStructureKeyMap();
	Integer level = structureKeyMap.get("产品型号");
	level = (level == null?1:level);
%>
<script>
	$(function() {
		$( "#accordion1" ).accordion({
			animated:false,
			collapsible:false,
			event:'click',
			fillSpace:true 
		});
	});
	//刷新结构树
	function refreshStrure(){
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
			plugins : ["themes","json_data","ui","crrm",'types'],
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
// 				"initially_select" : [ "root"]
			}
		}).bind("select_node.jstree",function(e,data){
			var id = data.rslt.obj.attr("id");
			id = id == 'root'?'':id;
			loadInspectingItemByStructureAndParent(id);
		});
	}
	//刷新结构树
	function refreshProductStrure(){
		$("#product-structure-tree").jstree({
			json_data : {
				data : [
					{ 
						"data" : "所有物料", 
						"state" : "closed",
						attr:{
							id:'root',
							level : 0,
							rel:'drive'
						}
					}
				],
				ajax : { 
					"url" : "${sictx}/base-info/bom/list-structure.htm",
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
// 				"initially_select" : [ "root"]
			}
		}).bind("select_node.jstree",function(e,data){
			var modelSpecificationLevel = <%=level%>;
			var id = data.rslt.obj.attr("id");
			id = id == 'root'?'':id;
			loadProductBomByStructureAndParent(id);
		});
	}
	//编辑产品结构
	var hisId = '';
	function editProductStructure(id,parentId){
		var url='${sictx}/base-info/bom/input-structure.htm?1=1';
		var newId = '';
		if(id){
			url += '&id='+id;
			newId += id;
		}
		newId += ',';
		if(parentId){
			url += "&parentId=" + parentId;
			newId += parentId;
		}
		if(hisId == newId){
			return;
		}
		hisId = newId;
		setTimeout(function(){
			hisId = '';
		},100);
		$.colorbox({href:url,iframe:true, innerWidth:400, innerHeight:200,
			overlayClose:false,
			onClosed:function(){
				refreshProductStrure();
			},
			title:(id?"编辑":"添加") + "物料"
		});
	}
	//加载产品BOM 
	function loadProductBomByStructureAndParent(parentId){
		if(window.selParentId == parentId){
			return;
		}
		makeEditable(true);
		window.selParentId = parentId || '';
		$("#bomList")[0].p.postData = {selParentId:parentId,"_list_code":"MFG_PRODUCT_BOM"};
		$("#bomList").jqGrid("setGridParam",{page:1}).trigger("reloadGrid");
		updatePath(parentId);
	}
	//加载检验项目
	function loadInspectingItemByStructureAndParent(parentId){
		if(window.selParentId == parentId){
			return;
		}
		makeEditable(true);
		window.selParentId = parentId || '';
		$("#itemList")[0].p.postData = {selParentId:parentId};
		$("#itemList").jqGrid("setGridParam",{page:1}).trigger("reloadGrid");
		updatePath(parentId);
		setTimeout(function(){
			$("#itemList")[0].p.selarrrow = [];
		},100);
	}
</script>
<script type="text/javascript" class="source">
			$(function () {
				var aIndex = 0;
				var aId = thirdMenu;
				if(thirdMenu=="defectionCode"){
					$("#defectionCode").jstree({ 
						"html_data" : {
							"data" :
							  "<ul>"+<security:authorize ifAnyGranted="si-baseInfo-defection">
					          "<li onclick='selectedNode(this)' id='defection'><a href='${sictx}/base-info/defection-code/defection-type-list.htm'>不良类别维护</a></li>"
					          +</security:authorize>
					          "</ul>"
						},
						"ui" : {
							"initially_select" : [ treeMenu ]
						},
						"plugins" : [ "themes", "html_data","ui" ]
					});
					aId = "_defectionCode";
				}else if(thirdMenu=="inspectingIndicator"){
					$("#inspectingIndicator").jstree({ 
						"html_data" : {
							"data" :
					          "<ul>"+<security:authorize ifAnyGranted="si-baseInfo-indicator-list">
					          	"<li onclick='selectedNode(this)' id='indicator'><a href='${sictx}/base-info/indicator/list.htm'>检验标准</a></li>"
					          	+"<li onclick='selectedNode(this)' id='indicator-history'><a href='${sictx}/base-info/indicator/list-history.htm'>所有版本检验标准</a></li>"
					          	+</security:authorize>
						      "</ul>"
						},
						"ui" : {
							"initially_select" : [ treeMenu ]
						},
						"plugins" : [ "themes", "html_data","ui" ]
					});
					aId = "_inspectingIndicator";
				}else if(thirdMenu=="inspectingItem"){
					refreshStrure();
					aId = "_inspectingItem";
				}
				$("#accordion1 > h3 > a").each(function(index,obj){
					if(obj.id == aId){
						aIndex = index;
						return false;
					}
				});
				$("#accordion1").accordion({active:aIndex});
			});
			function selectedNode(obj) {
				window.location = $(obj).children('a').attr('href');
			}
		   function _change_menu(url){
				window.location=url;
			}
			</script>
			
	<div id="accordion1" class="basic">
		<security:authorize ifAnyGranted="si-baseInfo-defection">
		<h3><a id="_defectionCode" onclick="_change_menu('<grid:authorize code="si-baseInfo-defection" systemCode="si"></grid:authorize>');">不良项目</a></h3>
		<div>
			<div id="defectionCode" class="demo">菜单加载中,请稍候...</div>
		</div>
		</security:authorize>
	</div>
