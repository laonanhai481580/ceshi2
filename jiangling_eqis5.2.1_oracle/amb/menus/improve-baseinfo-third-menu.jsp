<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="IMP_PROBLRM_DESCRIBLE_LIST">
	<div id="problem_describle" class="west-notree" ><a href="${improvectx}/base-info/problem-describle/problem-describle-list.htm">问题描述维护</a></div>
</security:authorize>
<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>