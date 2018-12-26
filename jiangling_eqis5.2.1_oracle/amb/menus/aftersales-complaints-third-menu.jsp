<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
	<security:authorize ifAnyGranted="aftersales-complaints-complaintsInfo">
		<div id="aftersales-complaints-info" class="west-notree"  ><a href="${aftersalesctx}/aftersales-complaints/complaintsInfo-list.htm">售后投诉信息</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="aftersales-complaints-complaintsAnalysis">
		<div id="aftersales-complaints-plato" class="west-notree"  ><a href="${aftersalesctx}/aftersales-complaints/complaintsInfo-plato.htm">售后投诉分析</a></div>
	</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
</script>