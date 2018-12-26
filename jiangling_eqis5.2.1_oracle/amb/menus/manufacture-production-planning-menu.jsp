<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
    <div style="display: block; height: 10px;"></div>
	<div id="_published" class="west-notree" url="${mfgctx}/production-planning/published/list.htm"  onclick="changeMenu(this);"><span>已发布计划</span></div>
	<div id="_make_planning" class="west-notree" url="${mfgctx}/production-planning/make-planning/list.htm"  onclick="changeMenu(this);"><span>制定生产计划</span></div>
	<div id="_auditing_planning" class="west-notree" url="${mfgctx}/production-planning/auditing-planning/list.htm"  onclick="changeMenu(this);"><span>审核生产计划</span></div>
	<div id="_ratify_planning" class="west-notree" url="${mfgctx}/production-planning/ratify-planning/list.htm"  onclick="changeMenu(this);"><span>批准生产计划</span></div>
	<div id="_report_complete_rate" class="west-notree" url="${mfgctx}/production-planning/report/complete-rate.htm"  onclick="changeMenu(this);"><span>计划完成率</span></div>
	<div id="_planning_monitor" class="west-notree" url="${mfgctx}/production-planning/report/planning-monitor.htm"  onclick="changeMenu(this);"><span>计划监控图</span></div>
	<script type="text/javascript" class="source">
		$().ready(function(){
			$('#'+thirdMenu).addClass('west-notree-selected');
		});
		function changeMenu(obj){
			window.location = $(obj).attr('url');
		}
	</script>