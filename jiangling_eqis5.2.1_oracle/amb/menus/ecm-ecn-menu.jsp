<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="ecm-ecn-input">
	<div id="_ecn_input" class="west-notree" ><a href="${ecmctx}/ecn/input.htm">ECN变更单</a></div>
</security:authorize>
<security:authorize ifAnyGranted="ecm-ecn-list">
	<div id="_ecn_list" class="west-notree" ><a href="${ecmctx}/ecn/list.htm">ECN变更台帐</a></div>
</security:authorize>
<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>