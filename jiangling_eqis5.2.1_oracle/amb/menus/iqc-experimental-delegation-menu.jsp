<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
	<security:authorize ifAnyGranted="iqc-experimental-delegation-input">
		<div id="_experimental_delegation_input" class="west-notree" url="${iqcctx}/experimental-delegation/input.htm" onclick="changeMenu(this);"><span>实验委托单</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc-experimental-delegation-list">
		<div id="_experimental_delegation_list" class="west-notree" url="${iqcctx }/experimental-delegation/list.htm" onclick="changeMenu(this);"><span>实验委托台帐</span></div>
	</security:authorize>
<%-- 	<security:authorize ifAnyGranted="iqc-experimental-delegation-history-list">
		<div id="_experimental_delegation_history" class="west-notree" url="${iqcctx }/experimental-delegation/history-list.htm" onclick="changeMenu(this);"><span>委托流转历史</span></div>
	</security:authorize> --%>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>