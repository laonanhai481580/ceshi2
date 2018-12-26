<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
	<security:authorize ifAnyGranted="QSM_SYSTEM_CERTIFICATE_LIST">
		<div id="_system_certificate" class="west-notree"  ><a href="${qsmctx}/system-certificate/list.htm">认证审核履历</a></div>
	</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
</script>