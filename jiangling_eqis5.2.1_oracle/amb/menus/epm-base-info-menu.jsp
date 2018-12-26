<%@page import="com.ambition.carmfg.bom.service.ProductBomManager"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.ambition.util.common.CommonUtil"%>
<script>
	$(function() {
		$( "#accordion1" ).accordion({
			animated:false,
			collapsible:false,
			event:'click',
			fillSpace:true 
		});
	});
</script>
<script type="text/javascript" class="source">
			$(function () {
				var aIndex = 0;
				var aId = thirdMenu;
				if(thirdMenu=="ortItem"){
					$("#ortItem").jstree({ 
						"html_data" : {
							"data" :
							  "<ul>"+<security:authorize ifAnyGranted="epm-baseInfo-ortIndicator,epm-baseInfo-ortItem">
					          "<li onclick='selectedNode(this)' id='ortItem'><a href='${epmctx}/base-info/ort-item/ort-indicator-list.htm'>可靠性测试项目维护</a></li>"
					          +</security:authorize>
					          "</ul>"
						},
						"ui" : {
							"initially_select" : [ treeMenu ]
						},
						"plugins" : [ "themes", "html_data","ui" ]
					});
					aId = "_ortItem";
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
		<security:authorize ifAnyGranted="epm-baseInfo-ortItem">
		<h3><a id="_ortItem" onclick="_change_menu('<grid:authorize code="epm-baseInfo-ortIndicator,epm-baseInfo-ortItem" systemCode="epm"></grid:authorize>');">可靠性测试项目</a></h3>
		<div>
			<div id="ortItem" class="demo">菜单加载中,请稍候...</div>
		</div>
		</security:authorize>
	</div>
