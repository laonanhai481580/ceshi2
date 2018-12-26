<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="QSM_LOSS_ITEM_LIST">
	<div id="loss_item_rate" class="west-notree" ><a href="${qsmctx}/data-acquisition/loss-item-rate.htm">缺失条款分析</a></div>
</security:authorize>
<security:authorize ifAnyGranted="QSM_AUDIT_PASS_LIST">
	<div id="audt_pass_rate" class="west-notree" ><a href="${qsmctx}/data-acquisition/audit-pass-rate.htm">客户审核通过率推移图</a></div>
</security:authorize>
<security:authorize ifAnyGranted="QSM_AUDIT_BING_LIST">
	<div id="audt_bing_rate" class="west-notree" ><a href="${qsmctx}/data-acquisition/audit-bing-rate.htm">客户审核柏拉图</a></div>
</security:authorize>
<security:authorize ifAnyGranted="QSM_AUDIT_CLOSE_LIST">
	<div id="audt_close_rate" class="west-notree" ><a href="${qsmctx}/data-acquisition/audit-close-rate.htm">客户审核结案率推移图</a></div>
</security:authorize>
<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>