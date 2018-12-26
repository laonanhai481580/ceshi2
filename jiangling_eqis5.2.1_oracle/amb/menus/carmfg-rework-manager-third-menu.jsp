<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="carmfg-rework-list">
	<div id="reworkLeft" class="west-notree" url="${carmfgctx}/data-acquisition/rework.htm" onclick="changeMenu(this);">
		<span>返修管理</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="carmfg-final-inspection">
	<div id="inspectionLeft" class="west-notree" url="${carmfgctx}/data-acquisition/final-inspection.htm" onclick="changeMenu(this);">
		<span>终检管理</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="carmfg-base-reason-measure">
	<div id="baseReasonAndMearsure" class="west-notree" url="${carmfgctx}/data-acquisition/base-reason-measure/list.htm" onclick="changeMenu(this);">
		<span>返工经验库</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="carmfg-reason-list">
	<div id=reasonList class="west-notree" url="${carmfgctx}/data-acquisition/base-reason-measure/list-reason.htm" onclick="changeMenu(this);">
		<span>基础原因库</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="carmfg-measure-list">
<div id="measureList" class="west-notree" url="${carmfgctx}/data-acquisition/base-reason-measure/list-measure.htm" onclick="changeMenu(this);">
	<span>基础措施库</span>
</div>
</security:authorize>
<script type="text/javascript" class="source">
	$().ready(function() {
		$('#' + thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj) {
		window.location = $(obj).attr('url');
	}
</script>