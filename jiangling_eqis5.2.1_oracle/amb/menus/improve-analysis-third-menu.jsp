<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="IMP_BAD_ITEM_LIST">
	<div id="bad_item" class="west-notree" ><a href="${improvectx}/data-acquisition/bad-item-rate.htm">不良项目柏拉图</a></div>
</security:authorize>
<security:authorize ifAnyGranted="IMP_CLOSE_LIST">
	<div id="close_rate" class="west-notree" ><a href="${improvectx}/data-acquisition/close-rate.htm">8D结案率推移图</a></div>
</security:authorize>
<security:authorize ifAnyGranted="IMP_EXCEPTION_CLOSE_LIST">
	<div id="exception_close_rate" class="west-notree" ><a href="${improvectx}/data-acquisition/exception-close-rate.htm">品质异常结案率推移图</a></div>
</security:authorize>
<security:authorize ifAnyGranted="IMP_EXCEPTION_BAD_ITEM_LIST">
	<div id="exception_bad_item" class="west-notree" ><a href="${improvectx}/data-acquisition/exception-bad-item-rate.htm">品质异常项目柏拉图</a></div>
</security:authorize>
<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>