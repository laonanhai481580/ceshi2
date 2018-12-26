<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="gsm_inspectionplan-chart_check-chart">
<div id="mychartReport" class="west-notree" url="${gsmctx }/inspection-chart/inspectionPlan-check-chart.htm" onclick="changeMenu(this);"><span>校验计划完成率统计</span></div>
</security:authorize>
<security:authorize ifAnyGranted="gsm_inspection-qualified-chart_check-chart">
<div id="inspectionQualifiedReport" class="west-notree" url="${gsmctx }/inspection-chart/inspection-qualified-check-chart.htm" onclick="changeMenu(this);">校验合格率统计</span></div>
</security:authorize>
<security:authorize ifAnyGranted="gsm_equipment-msaplan-chart_check-chart">
<div id="inspectionMsaplanReport" class="west-notree" url="${gsmctx }/inspectionMsa-chart/inspectionMsaplan-check-chart.htm" onclick="changeMenu(this);"><span>MSA计划完成率统计</span></div>
</security:authorize>
<security:authorize ifAnyGranted="gsm_equipment-msaqualified-chart_check-chart">
<div id="inspectionMsaQualifiedReport" class="west-notree" url="${gsmctx }/inspectionMsa-chart/inspectionMsaQualified-check-chart.htm " onclick="changeMenu(this);"><span>MSA合格率统计</span></div>
</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>