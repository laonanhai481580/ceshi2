<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
	<security:authorize ifAnyGranted="AFS_FAR_ANALYSIS_INPUT">
		<div id="far_analysis_input" class="west-notree"  ><a href="${aftersalesctx}/far/input.htm">FAR解析单</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="AFS_FAR_ANALYSIS_LIST">
		<div id="far_analysis_list" class="west-notree"  ><a href="${aftersalesctx}/far/list.htm">FAR解析台账</a></div>
	</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
</script>