<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="ecm-dern-input">
	<div id="_dern_input" class="west-notree" ><a href="${ecmctx}/dern/input.htm">DCR/N变更单</a></div>
</security:authorize>
<security:authorize ifAnyGranted="ecm-dern-list">
	<div id="_dern_list" class="west-notree" ><a href="${ecmctx}/dern/list.htm">DCR/N变更台帐</a></div>
</security:authorize>
<security:authorize ifAnyGranted="ecm-dern-history-list">
	<div id="_dern_history" class="west-notree" ><a href="${ecmctx}/dern/history-list.htm">DCR/N流转历史</a></div>
</security:authorize>
<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>