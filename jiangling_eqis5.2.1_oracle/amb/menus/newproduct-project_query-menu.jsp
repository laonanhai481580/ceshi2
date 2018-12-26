<!-- 页面类型 与 字符编码 -->
<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 访问路径 与 标签库集 -->
<%@ include file="/common/taglibs.jsp"%>
<!-- 权限控制 与 资源名称 -->
<div style="display: block; height: 10px;"></div>

<security:authorize ifAnyGranted="projectReport-list">
	<div id="_project_resume_list" class="west-notree"
		url="${newproductctx}/project-query/list.htm" onclick="changeMenu(this);">
		<span>项目台账</span>
	</div>
</security:authorize>


<!-- 三级菜单 与 选中样式 -->
<script type="text/javascript" class="source">
	$(document).ready(function() {
		$('#' + thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj) {
		window.location = $(obj).attr('url');
	}
</script>