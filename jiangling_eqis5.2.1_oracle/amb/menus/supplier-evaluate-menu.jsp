<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="supplier-develop-survey-list">
	<div id="surveylist" class="west-notree" url="${supplierctx }/develop/survey/list.htm"
		onclick="changeMenu(this);">
		<span>供应商调查表台帐</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-develop-list">
	<div id="evaluatelist" class="west-notree" url="${supplierctx }/develop/list.htm"
		onclick="changeMenu(this);">
		<span>供应商评价台帐</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-develop-input">
	<div id="evaluateInput" class="west-notree" url="${supplierctx }/develop/input.htm"
		onclick="changeMenu(this);">
		<span>供应商评价表</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-develop-monitor-list">
	<div id="evaluateMonitorList" class="west-notree" url="${supplierctx }/develop/monitor-list.htm"
		onclick="changeMenu(this);">
		<span>供应商评价跟踪</span>
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