<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<script type="text/javascript">
$(function() {
	$( "#accordion1" ).accordion({
		animated:false,
		collapsible:false,
		event:'click',
		fillSpace:true 
	});
	
});
</script>
<div id="accordion1" class="basic">
	<security:authorize ifAnyGranted="aftersales_aftersales-pdi-list">
		<h3><a id="_aftersales_pdi_list" onclick="_change_menu('${aftersalesctx}/aftersales-pdi/pdi-list.htm');">接车PDI问题台账</a></h3>
		<div>
			<div id="aftersales_pdi_list" class="demo"></div>
		</div>
	</security:authorize>
	
	<security:authorize ifAnyGranted="aftersales_aftersales-chart-qualified">
		<h3><a id="_aftersales_chart" onclick="_change_menu('${aftersalesctx}/aftersales-pdi/chart-qualified.htm');">PDI统计分析</a></h3>
		<div>
			<div id="aftersales_chart" class="demo"></div>
		</div>
	</security:authorize>
</div>

		
<script type="text/javascript" class="source">
	$(function () {
		var aIndex = 0;
		var aId = thirdMenu;
		if(thirdMenu=="aftersales_pdi_list"){
			$("#aftersales_pdi_list").jstree({ 
				"html_data" : {
					"data" :  
			          "<ul>"+
					  "<li onclick='selectedNode(this)' id='myaftersales_pdi_list'><a href='${aftersalesctx}/aftersales-pdi/pdi-list.htm'>接车PDI问题台账</a></li>"+
					  "<li onclick='selectedNode(this)' id='myaftersales_pdi_nolist'><a href='${aftersalesctx}/aftersales-pdi/pdi-nolist.htm'>失效PDI问题台账</a></li>"+
					  "</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			aId = "_aftersales_pdi_list";
		}else if(thirdMenu=="aftersales_chart"){
			$("#aftersales_chart").jstree({ 
				"html_data" : {
					"data" :  
				          "<ul>"+
						  "<li onclick='selectedNode(this)' id='myaftersales_chart_qualified'><a href='${aftersalesctx}/aftersales-pdi/chart-qualified.htm'>PDI合格率运行图</a></li>"+
						  "<li onclick='selectedNode(this)' id='myaftersales_chart_defects'><a href='${aftersalesctx}/aftersales-pdi/chart-defects.htm'>接车千台缺陷数统计</a></li>"+
						  "<li onclick='selectedNode(this)' id='myaftersales_chart_bad'><a href='${aftersalesctx}/aftersales-pdi/chart-bad.htm'>不良柏拉图分析</a></li>"+
				          "</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			aId = "_aftersales_chart";
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