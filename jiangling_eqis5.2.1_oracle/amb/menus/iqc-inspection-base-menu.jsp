<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
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
					"url" : "${iqcctx}/inspection-base/item/list-structure.htm",
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
	//编辑产品结构
	var hisId = '';
	function editProductStructure(id,parentId){
		var url='${mfgctx}/base-info/bom/input-structure.htm?1=1';
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
				refreshStrure();
			},
			title:(id?"编辑":"添加") + "产品结构"
		});
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
		if(thirdMenu=="item"){
			refreshStrure();
			$("#accordion1").accordion({active:1});
		}else if(thirdMenu=="indicator"){
			$("#indicator").jstree({ 
				"html_data" : {
					"data" :  
			          "<ul>"
			          <security:authorize ifAnyGranted="iqc-inspecting-indicator-list">
			          +"<li onclick='selectedNode(this)' id='indicator'><a href='${iqcctx}/inspection-base/indicator/list.htm'>最新版本标准维护</a></li>"
			          +"<li onclick='selectedNode(this)' id='indicator-history'><a href='${iqcctx}/inspection-base/indicator/list-history.htm'>所有版本检验标准</a></li>"
			          </security:authorize>
			          +"</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			$("#accordion1").accordion({active:0});
		}else if(thirdMenu=="_inspection_bom"){
			$("#_inspection_bom").jstree({ 
				"html_data" : {
					"data" :  
			          "<ul>"
			          <security:authorize ifAnyGranted="iqc-inspection-bom-list">
			          +"<li onclick='selectedNode(this)' id='_inspection_bom'><a href='${iqcctx}/inspection-base/inspection-bom/list.htm'>报检物料</a></li>"
			          </security:authorize>
			          +"</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			$("#accordion1").accordion({active:2});
		}else if(thirdMenu=="_inspection_interval"){
			$("#_inspection_interval").jstree({ 
				"html_data" : {
					"data" :  
			          "<ul>"
			          <security:authorize ifAnyGranted="iqc-inspection-interval-list">
			          +"<li onclick='selectedNode(this)' id='inspection_interval'><a href='${iqcctx}/inspection-interval/list.htm'>检验周期维护</a></li>"
			          </security:authorize>
			          +"</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			$("#accordion1").accordion({active:3});
		}
		
	});
	function selectedNode(obj) {
		window.location = $(obj).children('a').attr('href');
	}
	  function _change_menu(url){
		window.location=url;
	}
</script>
<div id="accordion1" class="basic">
	<security:authorize ifAnyGranted="iqc-inspecting-indicator-list">
		<h3><a id="_inspectionPoint"  onclick="_change_menu('${iqcctx}/inspection-base/indicator/list.htm');">检验标准</a></h3>
		<div>
			<div id="indicator"><span style="padding:4px;font-size:12px;">菜单加载中...</span></div>
		</div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc-inspecting-item-list">
		<h3><a id="_bom" onclick="_change_menu('${iqcctx}/inspection-base/item/list.htm');">检验项目</a></h3>
		<div>
			<div id="structure-tree" class="demo"><span style="padding:4px;font-size:12px;">检验项目加载中...</span></div>
		</div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc-inspection-bom-list">
		<h3><a id="inspection_bom" onclick="_change_menu('${iqcctx}/inspection-base/inspection-bom/list.htm');">报检物料维护</a></h3>
		<div>
			<div id="_inspection_bom" class="demo"><span style="padding:4px;font-size:12px;">报检物料维护加载中...</span></div>
		</div>
	</security:authorize>		
	<security:authorize ifAnyGranted="iqc-inspection-interval-list">
		<h3><a id="interval" onclick="_change_menu('${iqcctx}/inspection-interval/list.htm');">检验周期维护</a></h3>
		<div>
			<div id="_inspection_interval" class="demo"><span style="padding:4px;font-size:12px;">周期维护加载中...</span></div>
		</div>
	</security:authorize>
	</div>
</div>