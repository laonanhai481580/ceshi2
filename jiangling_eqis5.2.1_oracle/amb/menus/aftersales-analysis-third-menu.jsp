<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="AFS_LAR_PASS_LIST">
	<div id="lar_pass_rate" class="west-notree" ><a href="${aftersalesctx}/data-acquisition/lar-pass-rate.htm">Lar批次合格率推移图</a></div>
</security:authorize>
<security:authorize ifAnyGranted="AFS_VLRR_BAD_LIST">
	<div id="vlrr_bad_rate" class="west-notree" ><a href="${aftersalesctx}/data-acquisition/vlrr-bad-rate.htm">VLRR不良趋势图</a></div>
</security:authorize>
<security:authorize ifAnyGranted="AFS_VLRR_BAD_ITEM_LIST">
	<div id="vlrr_bad_item_rate" class="west-notree" ><a href="${aftersalesctx}/data-acquisition/vlrr-bad-item-rate.htm">VLRR不良项目柏拉图</a></div>
</security:authorize>
<security:authorize ifAnyGranted="AFS_OBA_BAD_LIST">
	<div id="oba_bad_rate" class="west-notree" ><a href="${aftersalesctx}/data-acquisition/oba-bad-rate.htm">OBA不良趋势图</a></div>
</security:authorize>
<security:authorize ifAnyGranted="AFS_OBA_BAD_ITEM_LIST">
	<div id="oba_bad_item_rate" class="west-notree" ><a href="${aftersalesctx}/data-acquisition/oba-bad-item-rate.htm">OBA不良项目柏拉图</a></div>
</security:authorize>
<security:authorize ifAnyGranted="AFS_FAR_CLOSE_LIST">
	<div id="far_close_rate" class="west-notree" ><a href="${aftersalesctx}/data-acquisition/far-close-rate.htm">FAR结案率趋势图</a></div>
</security:authorize>
<security:authorize ifAnyGranted="AFS_FAR_BAD_ITEM_LIST">
	<div id="far_bad_item_rate" class="west-notree" ><a href="${aftersalesctx}/data-acquisition/far-bad-item-rate.htm">FAR不良项目柏拉图</a></div>
</security:authorize>
<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>