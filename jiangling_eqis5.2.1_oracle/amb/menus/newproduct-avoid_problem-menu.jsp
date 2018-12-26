<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="avoidProblem_list">
	<div id="_avoidProblem_list" 
			class="west-notree" 
			url="${newproductctx}/avoid-problem/list.htm"
			onclick="changeMenu(this);">
		<span>应规避问题台账</span>
	</div>
</security:authorize>

<script type="text/javascript" class="source">
	$(document).ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>