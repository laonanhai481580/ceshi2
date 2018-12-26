<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
	<div style="display: block; height: 10px;"></div>

	<security:authorize ifAnyGranted="aftersales-pdiproblem-list">
		<div id="pdiproblem-list" class="west-notree"  ><a href="${aftersalesctx}/pdiproblem/list.htm">PDI问题台账</a></div>
	</security:authorize>
	
	<security:authorize ifAnyGranted="aftersales-pdiproblem-regular-right">
		<div id="pdiproblem-regular-right" class="west-notree"  ><a href="${aftersalesctx}/pdiproblem/regular-right.htm">PDI合格率运行图</a></div>
	</security:authorize>
	
	<security:authorize ifAnyGranted="aftersales-pdiproblem-defects-in-a-histogram">
		<div id="pdiproblem-defects-in-a-histogram" class="west-notree"  ><a href="${aftersalesctx}/pdiproblem/defects-in-a-histogram.htm">接车千台缺陷数统计</a></div>
	</security:authorize>
	
	<security:authorize ifAnyGranted="aftersales-pdiproblem-afs-pdi-plato">
		<div id="pdiproblem-afs-pdi-plato" class="west-notree"  ><a href="${aftersalesctx}/pdiproblem/afs-pdi-plato.htm">不良柏拉图分析</a></div>
	</security:authorize>
	
	<security:authorize ifAnyGranted="aftersales-pdiproblem-afs-pdi-pie">
		<div id="pdiproblem-afs-pdi-pie" class="west-notree"  ><a href="${aftersalesctx}/pdiproblem/afs-pdi-pie.htm">不良饼图分析</a></div>
	</security:authorize>
	
	<security:authorize ifAnyGranted="aftersales-pdiproblem-delete-list">
		<div id="pdiproblem-delete-list" class="west-notree"  ><a href="${aftersalesctx}/pdiproblem/delete-list.htm">PDI问题删除台账</a></div>
	</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
</script>