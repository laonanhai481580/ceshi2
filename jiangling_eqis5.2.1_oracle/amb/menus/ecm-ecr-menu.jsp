<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="ecm-ecr-input">
	<div id="_ecr_input" class="west-notree" ><a href="${ecmctx}/ecr/input.htm">ECR变更单</a></div>
</security:authorize>
<security:authorize ifAnyGranted="ecm-ecr-list">
	<div id="_ecr_list" class="west-notree" ><a href="${ecmctx}/ecr/list.htm">ECR变更台帐</a></div>
</security:authorize>
<security:authorize ifAnyGranted="ecm-ecr-history-list">
	<div id="_ecr_history" class="west-notree" ><a href="${ecmctx}/ecr/history-list.htm">ECR变更台帐</a></div>
</security:authorize>
<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>