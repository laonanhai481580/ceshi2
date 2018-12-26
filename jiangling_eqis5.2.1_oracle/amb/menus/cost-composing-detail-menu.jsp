<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
    <div id="accordion1" class="basic">
    	<security:authorize ifAnyGranted="cost_composing_list">
		<h3><a id="cost_composing_list" onclick="_change_menu('${costctx}/composing-detail/list.htm');">质量成本构成表</a></h3>
		<div>
			<div id="_cost_composing_list" class="demo"></div>
		</div>
		</security:authorize>
<%-- 		<security:authorize ifAnyGranted="cost-parts-loss-list,inner-productLoss-list"> --%>
<%-- 		<h3><a id="partsloss" onclick="_change_menu('${costctx}/partsloss/partsLoss-list.htm');">内部损失档次分类表</a></h3> --%>
<!-- 		<div> -->
<!-- 			<div id="_partsloss" class="demo"></div> -->
<!-- 		</div> -->
<%-- 		</security:authorize> --%>
<%-- 		<security:authorize ifAnyGranted="cost_cost_convert_list"> --%>
<%-- 		<h3><a id="cost_convert_list" onclick="_change_menu('${costctx}/cost-convert/list.htm');">集成成本转换规则</a></h3> --%>
<!-- 		<div> -->
<!-- 			<div id="_cost_convert_list" class="demo"></div> -->
<!-- 		</div> -->
<%-- 		</security:authorize> --%>
	</div>
	<script type="text/javascript" class="source">
		$(function () {
			var aIndex = 0;
			var aId = thirdMenu;
			if(thirdMenu=="cost_composing_list"){
				$("#_cost_composing_list").jstree({ 
					"html_data" : {
						"data" :  
				          "<ul>"+
	 					  <security:authorize ifAnyGranted="cost_composing_list">
	 					  "<li onclick='selectedNode(this)' id='_cost_composing_list_list'><a href='${costctx}/composing-detail/list.htm'>质量成本构成表</a></li>"+
	 					  </security:authorize>
				          "</ul>"
					},
					"ui" : {
						"initially_select" : [ treeMenu ]
					},
					"plugins" : [ "themes", "html_data","ui" ]
				});
			}else if(thirdMenu=="partsloss"){
				$("#_partsloss").jstree({ 
					"html_data" : {
						"data" :  
				          "<ul>"+
				          <security:authorize ifAnyGranted="cost-parts-loss-list">
	 					  "<li onclick='selectedNode(this)' id='_partsloss-partsLoss-list'><a href='${costctx}/partsloss/partsLoss-list.htm'>内部零件损失维护</a></li>"+
	 					  </security:authorize>
	 					  <security:authorize ifAnyGranted="inner-productLoss-list">
	 					  "<li onclick='selectedNode(this)' id='_inner-productLoss-list'><a href='${costctx}/innerproductloss/innerProductLoss-list.htm'>内部成品损失维护</a></li>"+
	 					  </security:authorize>
				          "</ul>"
					},
					"ui" : {
						"initially_select" : [ treeMenu ]
					},
					"plugins" : [ "themes", "html_data","ui" ]
				});
			}else if(thirdMenu=="cost_convert_list"){
				$("#_cost_convert_list").jstree({ 
					"html_data" : {
						"data" :  
				          "<ul>"+
	 					  <security:authorize ifAnyGranted="cost_composing_list">
	 					  "<li onclick='selectedNode(this)' id='cost_convert_list_list'><a href='${costctx}/cost-convert/list.htm'>转换规则</a></li>"+
	 					  </security:authorize>
				          "</ul>"
					},
					"ui" : {
						"initially_select" : [ treeMenu ]
					},
					"plugins" : [ "themes", "html_data","ui" ]
				});
			}
			$("#accordion1 > h3 > a").each(function(index,obj){
				if(obj.id == aId){
					aIndex = index;
					return false;
				}
			});
			$("#accordion1").accordion({
				active:aIndex,
				animated:false,
				collapsible:false,
				event:'click',
				fillSpace:true
			});
		});
		function selectedNode(obj) {
			window.location = $(obj).children('a').attr('href');
		}
		  function _change_menu(url){
			window.location=url;
		}
	</script>