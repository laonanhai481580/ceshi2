<!-- 页面类型 与 字符编码 -->
<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 访问路径 与 标签库集 -->
<%@ include file="/common/taglibs.jsp"%>
<!-- 权限控制 与 资源名称 -->
<div style="display: block; height: 10px;"></div>

<security:authorize ifAnyGranted="turnphaseCheck-input">
	<div    id="_check_report_input" 
			class="west-notree" 
			url="${newproductctx}/turnphase-check/input.htm"
			onclick="changeMenu(this);">
		<span>转阶段检查报告</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="turnphase-check">
	<div id="_check_report_list" 
		 class="west-notree"
		 url="${newproductctx}/turnphase-check/list.htm" 
		 onclick="changeMenu(this);">
	   <span>转阶段检查报告台账</span>
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