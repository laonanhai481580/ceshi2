<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="bom-traceability-list">
	<div id="_bom_traceability_list" class="west-notree" url="${mfgctx}/inspection/made-inspection/bom-traceability-list.htm"  onclick="changeMenu(this);"><span>物料信息</span></div>
</security:authorize>
<security:authorize ifAnyGranted="product-traceability-list">
	<div id="_product_traceability_list" class="west-notree" url="${mfgctx}/inspection/made-inspection/product-traceability-list.htm"  onclick="changeMenu(this);"><span>生产信息</span></div>
</security:authorize>
<script type="text/javascript" class="source">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>