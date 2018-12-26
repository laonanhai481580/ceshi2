<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="lessonLibrary-list">
	<div id="_lesson_list" 
			class="west-notree" 
			url="${newproductctx}/lesson-library/lesson-list.htm"
			onclick="changeMenu(this);">
		<span>经验教训提交</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="lessonLibrary-list-audit">
	<div id="_lesson_list_audit" 
			class="west-notree" 
			url="${newproductctx}/lesson-library/lesson-list-audit.htm"
			onclick="changeMenu(this);">
		<span>经验教训审核</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="lessonLibrary-list-complete">
	<div id="_lesson_list_complete" 
			class="west-notree" 
			url="${newproductctx}/lesson-library/lesson-list-complete.htm"
			onclick="changeMenu(this);">
		<span>经验教训库查询</span>
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