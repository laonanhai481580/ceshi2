<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
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
					"url" : "${carmfgctx}/inspection-base/item/list-structure.htm",
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
						"data" : "产品结构", 
						"state" : "closed",
						attr:{
							id:'root',
							level : 0,
							rel:'drive'
						}
					}
				]
// 				,
// 				ajax : { 
// 					"url" : "${carmfgctx}/base-info/bom/list-structure.htm",
// 					data : function(n){
// 						var id = n.attr("id");
// 						return {date:(new Date()).getTime(),selParentId:id=='root'?'':id}	
// 					}
// 				}
			},
			plugins : ["themes","json_data","ui","crrm",'types'],
			core : { "initially_open" : ["root"] },
			types : {
				valid_children:'drive',
				types:{
					drive:{
						icon:{
							image:'${carmfgctx}/images/_drive.png'
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
			loadProductBomByStructureAndParent(id);
		});
	}
	//编辑产品结构
	var hisId = '';
	function editProductStructure(id,parentId){
		var url='${carmfgctx}/base-info/bom/input-structure.htm?1=1';
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
			title:(id?"编辑":"添加") + "产品结构"
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
		if(thirdMenu=="bom"){
			refreshProductStrure();
			aId = "_bom";
		}else if(thirdMenu=="inspectionPoint"){
			$("#inspectionPoint").jstree({ 
				"html_data" : {
					"data" :  
			          "<ul>"+
			          <security:authorize ifAnyGranted="carmfg-baseInfo-inspection-point">
			          "<li onclick='selectedNode(this)' id='point'><a href='${carmfgctx}/base-info/inspection-point/list.htm'>采集点维护</a></li>"+
			          </security:authorize>
			          <security:authorize ifAnyGranted="carmfg-baseInfo-procedure-defection">
				      "<li onclick='selectedNode(this)' id='code'><a href='${carmfgctx}/base-info/procedure-defection/list.htm'>工序不良维护</a></li>"+
				      </security:authorize>+
				      "</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			aId = "_inspectionPoint";
		}else if(thirdMenu=="defectionCode"){
			$("#defectionCode").jstree({ 
				"html_data" : {
					"data" :
			          "<ul>"+
			          <security:authorize ifAnyGranted="carmfg-baseInfo-direction">
					  "<li onclick='selectedNode(this)' id='direction'><a href='${carmfgctx}/base-info/direction-code/list.htm'>方位维护</a></li>"+
					  </security:authorize>
			          <security:authorize ifAnyGranted="carmfg-baseInfo-position">
					  "<li onclick='selectedNode(this)' id='position'><a href='${carmfgctx}/base-info/position-code/component-list.htm'>部位维护</a></li>"+
					  </security:authorize>
			          <security:authorize ifAnyGranted="carmfg-baseInfo-defection">
					  "<li onclick='selectedNode(this)' id='defection'><a href='${carmfgctx}/base-info/defection-code/defection-type-list.htm'>不良代码维护</a></li>"+
					  </security:authorize>
			          <security:authorize ifAnyGranted="carmfg-baseInfo-defection-attribute">
					  "<li onclick='selectedNode(this)' id='attribute'><a href='${carmfgctx}/base-info/defection-code-attribute/list.htm'>不良代码属性维护</a></li>"+
					  </security:authorize>
					  <security:authorize ifAnyGranted="mfg-fpa-type-list">
					  "<li onclick='selectedNode(this)' id='type_list'><a href='${carmfgctx}/fpa/defect/type-list.htm'>FPA缺陷类型维护</a></li>"+
					  </security:authorize>
					  <security:authorize ifAnyGranted="mfg-fpa-level-list">
					  "<li onclick='selectedNode(this)' id='level_list'><a href='${carmfgctx}/fpa/defect/level-list.htm'>FPA缺陷等级维护</a></li>"+
					  </security:authorize>
					  "</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			aId = "_defectionCode";
		}else if(thirdMenu=="formCodingRule"){
			$("#formCodingRule").jstree({ 
				"html_data" : {
					"data" :
			          "<ul>"+
			          <security:authorize ifAnyGranted="carmfg-baseInfo-code-rule">
			          "<li onclick='selectedNode(this)' id='address'><a href='${carmfgctx}/base-info/form-coding-rule/input.htm'>编码规则维护</a></li>"+
			          </security:authorize>+
			          "</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			aId = "_formCodingRule";
		}else if(thirdMenu=="qualifiedRate"){
			$("#qualifiedRate").jstree({ 
				"html_data" : {
					"data" :  
			          "<ul>"+
			          <security:authorize ifAnyGranted="carmfg-baseInfo-qualified-rate">
			          "<li onclick='selectedNode(this)' id='rate'><a href='${carmfgctx}/base-info/qualified-rate/list.htm'>一次合格率规则定义</a></li>"+
			          </security:authorize>
			          "</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			aId = "_qualifiedRate";
		}else if(thirdMenu=="averageGoalLine"){
			$("#averageGoalLine").jstree({ 
				"html_data" : {
					"data" :  
			          "<ul>"+
			          <security:authorize ifAnyGranted="mfg-base-targetline">
			          "<li onclick='selectedNode(this)' id='line'><a href='${carmfgctx}/base-info/target-line/targetLine-listEditable.htm'>目标线规则</a></li>"+
			          </security:authorize>
			          <security:authorize ifAnyGranted="mfg-base-fpa-goalline">
			          "<li onclick='selectedNode(this)' id='tar'><a href='${carmfgctx}/base-info/targer/list.htm'>FPA目标值配置</a></li>"+
			          </security:authorize>
			          "</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			aId = "_averageGoalLine";
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
	<security:authorize ifAnyGranted="carmfg-baseInfo-bom">
		<h3><a id="_bom" onclick="_change_menu('<grid:authorize code="carmfg-baseInfo-bom" systemCode="carmfg"></grid:authorize>');">产品BOM</a></h3>
		<div>
			<div id="product-structure-tree" class="demo">菜单加载中,请稍候...</div>
		</div>
	</security:authorize>
	<security:authorize ifAnyGranted="carmfg-baseInfo-inspection-point,carmfg-baseInfo-procedure-defection">
		<h3><a id="_inspectionPoint" onclick="_change_menu('<grid:authorize code="carmfg-baseInfo-inspection-point,carmfg-baseInfo-procedure-defection" systemCode="carmfg"></grid:authorize>');">采集点设置</a></h3>
		<div>
			<div id="inspectionPoint" class="demo">菜单加载中,请稍候...</div>
		</div>
	</security:authorize>
	<security:authorize ifAnyGranted="carmfg-baseInfo-direction,carmfg-baseInfo-position,carmfg-baseInfo-defection,carmfg-baseInfo-defection-attribute,mfg-fpa-type-list,mfg-fpa-level-list">
		<h3><a id="_defectionCode" onclick="_change_menu('<grid:authorize code="carmfg-baseInfo-direction,carmfg-baseInfo-position,carmfg-baseInfo-defection,carmfg-baseInfo-defection-attribute,mfg-fpa-type-list,mfg-fpa-level-list" systemCode="carmfg"></grid:authorize>');">不良代码定义</a></h3>
		<div>
			<div id="defectionCode" class="demo">菜单加载中,请稍候...</div>
		</div>
	</security:authorize>
	<security:authorize ifAnyGranted="carmfg-baseInfo-code-rule">
		<h3><a id="_formCodingRule" onclick="_change_menu('<grid:authorize code="carmfg-baseInfo-code-rule" systemCode="carmfg"></grid:authorize>');">表单编号编码规则</a></h3>
		<div>
			<div id="formCodingRule" class="demo">菜单加载中,请稍候...</div>
		</div>
	</security:authorize>
<!-- 	<security:authorize ifAnyGranted="carmfg-baseInfo-qualified-rate"> -->
<%-- 		<h3><a id="_qualifiedRate" onclick="_change_menu('<grid:authorize code="carmfg-baseInfo-qualified-rate" systemCode="carmfg"></grid:authorize>');">一次合格率规则</a></h3> --%>
<!-- 		<div>		 -->
<!-- 			<div id="qualifiedRate" class="demo">菜单加载中,请稍候...</div> -->
<!-- 		</div> -->
<!-- 	</security:authorize> -->

	<security:authorize ifAnyGranted="mfg-base-targetline,mfg-base-fpa-goalline">
		<h3><a id="_averageGoalLine" onclick="_change_menu('<grid:authorize code="mfg-base-targetline,mfg-base-fpa-goalline" systemCode="carmfg"></grid:authorize>');">目标线规则</a></h3>
		<div>		
			<div id="averageGoalLine" class="demo">菜单加载中,请稍候...</div>
		</div>
	</security:authorize>
</div>
