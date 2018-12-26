<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.ambition.util.common.CommonUtil1"%>
<%
	Map<String,Integer> structureKeyMap = null;
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
					"url" : "${mfgctx}/inspection-base/item/list-structure.htm",
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
					"url" : "${mfgctx}/base-info/bom/list-structure.htm",
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
							image:'${mfgctx}/images/_drive.png'
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
				if(thirdMenu=="bom"){
					$("#product-structure-tree").jstree({ 
						"html_data" : {
							"data" :
					          "<ul>"+<security:authorize ifAnyGranted="mfg_base_info_bom_list">"<li onclick='selectedNode(this)' id='bom'><a href='${mfgctx}/base-info/bom/list.htm'>所有物料</a></li>"+</security:authorize>
					         //<security:authorize ifAnyGranted="MFG_INSPECTION_BOM_LIST"> "<li onclick='selectedNode(this)' id='inspection-bom'><a href='${mfgctx}/base-info/inspection-bom/list.htm'>报检物料</a></li>"+</security:authorize>
						      "</ul>"
						},
						"ui" : {
							"initially_select" : [ treeMenu ]
						},
						"plugins" : [ "themes", "html_data","ui" ]
					});
					/* refreshProductStrure(); */
					aId = "_bom";
				}else if(thirdMenu=="bomView"){
					$("#bomView").jstree({ 
						"html_data" : {
							"data" :
					          "<ul>"+<security:authorize ifAnyGranted="MFG_BASE-INFO_BOM-VIEW_LIST">"<li onclick='selectedNode(this)' id='bomViewView'><a href='${mfgctx}/base-info/bom-view/list.htm'>产品BOM</a></li>"+</security:authorize>
						      "</ul>"
						},
						"ui" : {
							"initially_select" : [ 'bomViewView' ]
						},
						"plugins" : [ "themes", "html_data","ui" ]
					});
					aId = "_bomView";
				}else if(thirdMenu=="inspectionPoint"){
					$("#inspectionPoint").jstree({ 
						"html_data" : {
							"data" :  
					          "<ul>"+<security:authorize ifAnyGranted="carmfg-baseInfo-inspection-point">"<li onclick='selectedNode(this)' id='point'><a href='${mfgctx}/base-info/inspection-point/list.htm'>采集点维护</a></li>"+</security:authorize>
					          <security:authorize ifAnyGranted="carmfg-baseInfo-procedure-defection-list">"<li onclick='selectedNode(this)' id='code'><a href='${mfgctx}/base-info/procedure-defection/list.htm'>工序不良维护</a></li>"+</security:authorize>"</ul>"
					         
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
					          //"<li onclick='selectedNode(this)' id='position'><a href='${mfgctx}/base-info/position-code/component-list.htm'>部位维护</a></li>"+
					          //"<li onclick='selectedNode(this)' id='direction'><a href='${mfgctx}/base-info/direction-code/list.htm'>方位维护</a></li>"+
							  <security:authorize ifAnyGranted="carmfg-baseInfo-defection">
					          "<li onclick='selectedNode(this)' id='defection'><a href='${mfgctx}/base-info/defection-code/defection-type-list.htm'>不良类别代码维护</a></li>"+</security:authorize>
					         "</ul>"
						},
						"ui" : {
							"initially_select" : [ treeMenu ]
						},
						"plugins" : [ "themes", "html_data","ui" ]
					});
					aId = "_defectionCode";
				}else if(thirdMenu=="linkAddress"){
					$("#linkAddress").jstree({ 
						"html_data" : {
							"data" :
					          "<ul>"+<security:authorize ifAnyGranted="">"<li onclick='selectedNode(this)' id='address'><a href='${mfgctx}/base-info/my-function/list.htm'>链接地址维护</a></li>"+</security:authorize>
						      "</ul>"
						},
						"ui" : {
							"initially_select" : [ treeMenu ]
						},
						"plugins" : [ "themes", "html_data","ui" ]
					});
					aId = "linkAddress";
				}else if(thirdMenu=="formCodingRule"){
					$("#formCodingRule").jstree({ 
						"html_data" : {
							"data" :
					          "<ul>"+<security:authorize ifAnyGranted="MFG_BASE-INFO_FORM-CODING-RULE_INPUT">"<li onclick='selectedNode(this)' id='address'><a href='${mfgctx}/base-info/form-coding-rule/input.htm'>编码规则维护</a></li>"+</security:authorize>
						      "</ul>"
						},
						"ui" : {
							"initially_select" : [ treeMenu ]
						},
						"plugins" : [ "themes", "html_data","ui" ]
					});
					aId = "_formCodingRule";
				}else if(thirdMenu=="plant_parameter"){
					$("#plant_parameter").jstree({ 
						"html_data" : {
							"data" :
					          "<ul>"+<security:authorize ifAnyGranted="carmfg-plant-parameter-list">
					          	"<li onclick='selectedNode(this)' id='_plant_parameter'><a href='${mfgctx}/plant-parameter/list.htm'>设备参数维护</a></li>"
					          	+"<li onclick='selectedNode(this)' id='_plant_item'><a href='${mfgctx}/plant-item/list.htm'>设备信息维护</a></li>"
					          	+</security:authorize>
						      "</ul>"
						},
						"ui" : {
							"initially_select" : [ treeMenu ]
						},
						"plugins" : [ "themes", "html_data","ui" ]
					});
					aId = "_plant_parameter";
				}else if(thirdMenu=="ort_inspection"){
					$("#ort_inspection").jstree({ 
						"html_data" : {
							"data" :
					          "<ul>"+<security:authorize ifAnyGranted="carmfg-ort-inspection-list">
					          	"<li onclick='selectedNode(this)' id='_ort_inspection'><a href='${mfgctx}/ort/ort-base/customer-list.htm'>客户信息维护</a></li>"
					          	+</security:authorize>
						      "</ul>"
						},
						"ui" : {
							"initially_select" : [ treeMenu ]
						},
						"plugins" : [ "themes", "html_data","ui" ]
					});
					aId = "_ort_inspection";
				}else if(thirdMenu=="ipqc_warming"){
					$("#ipqc_warming").jstree({ 
						"html_data" : {
							"data" :
					          "<ul>"+<security:authorize ifAnyGranted="carmfg-ipqc-warming-list">
					          	"<li onclick='selectedNode(this)' id='_ipqc_warming'><a href='${mfgctx}/ipqc/ipqc-warming/list.htm'>IPQC稽核问题严重度+重复性维护</a></li>"
					          	+</security:authorize>
						      "</ul>"
						},
						"ui" : {
							"initially_select" : [ treeMenu ]
						},
						"plugins" : [ "themes", "html_data","ui" ]
					});
					aId = "_ipqc_warming";
				}else if(thirdMenu=="sampling_number"){
					$("#sampling_number").jstree({ 
						"html_data" : {
							"data" :
					          "<ul>"+<security:authorize ifAnyGranted="carmfg-sampling-number-list">
					          	"<li onclick='selectedNode(this)' id='_sampling_number'><a href='${mfgctx}/sampling-number/list.htm'>制程检验抽检数量维护</a></li>"
					          	+</security:authorize>
						      "</ul>"
						},
						"ui" : {
							"initially_select" : [ treeMenu ]
						},
						"plugins" : [ "themes", "html_data","ui" ]
					});
					aId = "_sampling_number";
				}else if(thirdMenu=="inspectingIndicator"){
					$("#inspectingIndicator").jstree({ 
						"html_data" : {
							"data" :
					          "<ul>"+<security:authorize ifAnyGranted="carmfg-baseInfo-inspection-base-indicator-list">
					          	"<li onclick='selectedNode(this)' id='indicator'><a href='${mfgctx}/inspection-base/indicator/list.htm'>最新版本标准维护</a></li>"
					          	+"<li onclick='selectedNode(this)' id='indicator-history'><a href='${mfgctx}/inspection-base/indicator/list-history.htm'>所有版本检验标准</a></li>"
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
// 				else if(thirdMenu=="qualifiedRate"){
// 					$("#qualifiedRate").jstree({ 
// 						"html_data" : {
// 							"data" :  
// 					          "<ul><li onclick='selectedNode(this)' id='rate'><a href='${mfgctx}/base-info/qualified-rate/list.htm'>一次合格率规则设置</a></li></ul>"
// 						},
// 						"ui" : {
// 							"initially_select" : [ treeMenu ]
// 						},
// 						"plugins" : [ "themes", "html_data","ui" ]
// 					});
// 					$("#accordion1").accordion({active:7});
// 				}
			});
			function selectedNode(obj) {
				window.location = $(obj).children('a').attr('href');
			}
		   function _change_menu(url){
				window.location=url;
			}
			</script>
			
	<div id="accordion1" class="basic">
		<security:authorize ifAnyGranted="mfg_base_info_bom_list">
		<h3><a id="_bom" onclick="_change_menu('<grid:authorize code="mfg_base_info_bom_list" systemCode="carmfg"></grid:authorize>');">物料清单</a></h3>
		<div>
			<div id="product-structure-tree" class="demo">菜单加载中,请稍候...</div>
		</div>
		</security:authorize>
		<security:authorize ifAnyGranted="MFG_BASE-INFO_BOM-VIEW_LIST">
		<h3><a id="_bomView"  onclick="_change_menu('<grid:authorize code="MFG_BASE-INFO_BOM-VIEW_LIST" systemCode="carmfg"></grid:authorize>');">产品BOM</a></h3>
		<div>
			<div id="bomView" class="demo">菜单加载中,请稍候...</div>
		</div>
		</security:authorize>
		<security:authorize ifAnyGranted="carmfg-baseInfo-defection,carmfg-baseInfo-defection-attribute">
		<h3><a id="_defectionCode" onclick="_change_menu('<grid:authorize code="carmfg-baseInfo-defection,carmfg-baseInfo-defection-attribute" systemCode="carmfg"></grid:authorize>');">不良代码定义</a></h3>
		<div>
			<div id="defectionCode" class="demo">菜单加载中,请稍候...</div>
		</div>
		</security:authorize>
		<security:authorize ifAnyGranted="MFG_BASE-INFO_FORM-CODING-RULE_INPUT">
		<h3><a id="_formCodingRule" onclick="_change_menu('<grid:authorize code="MFG_BASE-INFO_FORM-CODING-RULE_INPUT" systemCode="carmfg"></grid:authorize>');">表单编号编码规则</a></h3>
		<div>
			<div id="formCodingRule" class="demo">菜单加载中,请稍候...</div>
		</div>
		</security:authorize>
		<security:authorize ifAnyGranted="carmfg-plant-parameter-list">
		<h3><a id="_plant_parameter" onclick="_change_menu('<grid:authorize code="carmfg-plant-parameter-list" systemCode="carmfg"></grid:authorize>');">设备参数维护</a></h3>
		<div>
			<div id="plant_parameter" class="demo">菜单加载中,请稍候...</div>
		</div>
		</security:authorize>
		<%-- 
		<security:authorize ifAnyGranted="carmfg-ort-customer-list,carmfg-ort-inspection-list">
		<h3><a id="_ort_inspection" onclick="_change_menu('<grid:authorize code="carmfg-ort-customer-list,carmfg-ort-inspection-list" systemCode="carmfg"></grid:authorize>');">ORT检验项目维护</a></h3>
		<div>
			<div id="ort_inspection" class="demo">菜单加载中,请稍候...</div>
		</div>
		</security:authorize>
		--%>
		<security:authorize ifAnyGranted="carmfg-ipqc-warming-list">
		<h3><a id="_ipqc_warming" onclick="_change_menu('<grid:authorize code="carmfg-ipqc-warming-list" systemCode="carmfg"></grid:authorize>');">IPQC稽核预警</a></h3>
		<div>
			<div id="ipqc_warming" class="demo">菜单加载中,请稍候...</div>
		</div>
		</security:authorize>
		<security:authorize ifAnyGranted="carmfg-sampling-number-list">
		<h3><a id="_sampling_number" onclick="_change_menu('<grid:authorize code="carmfg-sampling-number-list" systemCode="carmfg"></grid:authorize>');">制程检验抽检数量维护</a></h3>
		<div>
			<div id="sampling_number" class="demo">菜单加载中,请稍候...</div>
		</div>
		</security:authorize>
		<security:authorize ifAnyGranted="carmfg-baseInfo-inspection-base-indicator-list">
		<h3><a id="_inspectingIndicator" onclick="_change_menu('<grid:authorize code="carmfg-baseInfo-inspection-base-indicator-list" systemCode="carmfg"></grid:authorize>');">检验标准维护</a></h3>
		<div>
			<div id="inspectingIndicator" class="demo">菜单加载中,请稍候...</div>
		</div>
		</security:authorize>
		<security:authorize ifAnyGranted="carmfg-baseInfo-inspection-base-item-list">
		<h3><a id="_inspectingItem" onclick="_change_menu('<grid:authorize code="carmfg-baseInfo-inspection-base-item-list" systemCode="carmfg"></grid:authorize>');">检验项目维护</a></h3>
		<div>
			<div id="structure-tree" class="demo">菜单加载中,请稍候...</div>
		</div>
		</security:authorize>
		<security:authorize ifAnyGranted="MFG_BASE-INFO_BUSINESS-UNIT_LIST">
		<h3><a id="_businessUnit" onclick="_change_menu('<grid:authorize code="MFG_BASE-INFO_BUSINESS-UNIT_LIST" systemCode="carmfg"></grid:authorize>');">事业部人员配置</a></h3>
		<div></div>
		</security:authorize>
		<%-- h3><a id="_qualifiedRate" onclick="_change_menu('${mfgctx}/base-info/qualified-rate/list.htm');">一次合格率规则</a></h3>
		<div>		
			<div id="qualifiedRate" class="demo">菜单加载中,请稍候...</div>
		</div> --%>
	</div>
