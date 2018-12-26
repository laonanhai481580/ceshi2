<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
	<security:authorize ifAnyGranted="AFS_OBA_DATA_LIST">
		<div id="oba_data" class="west-notree"  ><a href="${aftersalesctx}/oba/list.htm">OBA明细表</a></div>
	</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
</script>