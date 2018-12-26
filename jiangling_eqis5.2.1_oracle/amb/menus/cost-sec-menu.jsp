<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul>
	<security:authorize ifAnyGranted="cost_statistical_cost_total,cost_cost_record_list,cost_cost_record_collection_list,composing_cost_info">
	<li id="statistical_analysis"><span><span><a href="<grid:authorize code="cost_statistical_cost_total,cost_cost_record_list,cost_cost_record_collection_list,composing_cost_info" systemCode="cost"></grid:authorize>">质量成本报表</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="cost_reinspection_list">
	<li id="collection"><span><span><a href="<grid:authorize code="cost_reinspection_list" systemCode="cost"></grid:authorize>">成本数据采集</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="cost_composing_base">
	<li id="composing_detail"><span><span><a href="${costctx}/composing-detail/list.htm">基础设置</a></span></span></li>
	</security:authorize>
</ul>
<div class="hid-header" onclick=headerChange(this); title="折叠/展开"></div>
<script>
	var topMenu='cost';
	$('#'+secMenu).addClass('sec-selected');
</script>


