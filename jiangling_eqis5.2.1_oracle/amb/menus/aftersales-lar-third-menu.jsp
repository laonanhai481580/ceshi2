<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
	<security:authorize ifAnyGranted="AFS_LAR_DATA_LIST">
		<div id="lar_data" class="west-notree"  ><a href="${aftersalesctx}/lar/list.htm">LAR批次合格率台账</a></div>
	</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
</script>