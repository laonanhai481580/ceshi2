<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
	<div id="fault_info_trend_graph" class="west-notree"  ><a href="${aftersalesctx}/aftersales-info/faultInfo-ronethousand-trend-graph.htm">R1000故障率走势</a></div>
	<div id="fault_info_graph" class="west-notree"  ><a href="${aftersalesctx}/aftersales-info/faultInfo-ronethousand-graph.htm">R1000故障率分析</a></div>
	<div id="ronethousand_graph_report" class="west-notree"  ><a href="${aftersalesctx}/aftersales-info/faultInfo-ronethousand-report-graph.htm">R1000故障率报表</a></div>
	<div class="linee"></div>
	
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
</script>