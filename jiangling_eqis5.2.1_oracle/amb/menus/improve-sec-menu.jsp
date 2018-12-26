<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<a class="scroll-left-btn" onclick="_scrollLeft();">&lt;&lt;</a>
<div class="fix-menu">
<ul>
	<security:authorize ifAnyGranted="IMP_8DREPORT_LIST,IMP_8DREPORT_INPUT">
		<li id="8d_report"><span><span><a href="<grid:authorize code="IMP_8DREPORT_LIST,IMP_8DREPORT_INPUT" systemCode="improve"></grid:authorize>">8D改进管理</a></span></span></li>
	</security:authorize>	
	<security:authorize ifAnyGranted="IMP_BAD_ITEM_LIST,IMP_CLOSE_LIST,IMP_ANALYSIS">
		<li id="analysis"><span><span><a href="<grid:authorize code="IMP_BAD_ITEM_LIST,IMP_CLOSE_LIST,IMP_ANALYSIS" systemCode="improve"></grid:authorize>">统计分析</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="IMP_PROBLRM_DESCRIBLE_LIST">
		<li id="base_info"><span><span><a href="<grid:authorize code="IMP_PROBLRM_DESCRIBLE_LIST" systemCode="improve"></grid:authorize>">基础维护</a></span></span></li>
	</security:authorize>
</ul>
</div>
<a class="scroll-right-btn" onclick="_scrollRight();">&gt;&gt;</a>
<div class="hid-header" onclick="headerChange(this);" title="隐藏"></div>
<script>
    var topMenu;
	$('#'+secMenu).addClass('sec-selected');
</script>