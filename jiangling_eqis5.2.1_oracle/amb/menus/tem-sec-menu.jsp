<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul>
	<security:authorize ifAnyGranted="tem-parts-inner,tem-year-plan-list,tem-year-plan-input">
		<li id="partsInner">
			<span><span>
			<a href='<grid:authorize code="tem-parts-inner,tem-year-plan-list,tem-year-plan-input" systemCode="tem"></grid:authorize>'>零部件内部实验管理</a>
			</span></span></li>
	</security:authorize>
</ul>
<div class="hid-header" onclick=headerChange(this); title="折叠/展开"></div>
<script>
    var topMenu='sysPlan';
	$('#'+secMenu).addClass('sec-selected');
</script>