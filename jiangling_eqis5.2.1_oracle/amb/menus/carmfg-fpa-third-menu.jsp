<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="carmfg-fpa-input">
	<div id="fpa_input" class="west-notree" url="${carmfgctx}/fpa/input.htm" onclick="changeMenu(this);">
		<span>FPA评价表单</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="carmfg-fpa-list">
	<div id="fpa_list" class="west-notree" url="${carmfgctx}/fpa/list.htm" onclick="changeMenu(this);">
		<span>FPA评价台账</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="carmfg-fpa-analysis">
	<div id="fpa_analysis" class="west-notree" url="${carmfgctx}/fpa/analysis-list.htm" onclick="changeMenu(this);">
		<span>FPA分析</span>
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