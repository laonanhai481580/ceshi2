<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="iqc-modelPurchasedPart-list">
	<div id="purchasedPart" class="west-notree" url="${iqcctx }/purchased-part/modelPurchasedPart-list.htm" onclick="changeMenu(this);"><span>车型外购件数量维护</span></div>
</security:authorize>
<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>