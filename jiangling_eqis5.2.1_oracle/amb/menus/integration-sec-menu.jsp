<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul>
	<security:authorize ifAnyGranted="integration-input">
		<li id="integration_sec"><span><span><a href="<grid:authorize code='integration-input' systemCode='integration'></grid:authorize>">QIS与ERP集成</a></span></span></li>
	</security:authorize>
</ul>
<div class="hid-header" onclick=headerChange(this); title="折叠/展开"></div>
<script>
    var topMenu='sysPlan';
	$('#'+secMenu).addClass('sec-selected');
</script>

