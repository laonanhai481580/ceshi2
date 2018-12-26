<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="new-project-info-list">
	<div id="_project_info_list" class="west-notree" url="${newproductctx}/project-info/list.htm" onclick="changeMenu(this);">
		<span>项目管理</span>
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