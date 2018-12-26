<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="gsm_inspectionplan_list">
	<div id="_myInspectionPlan" class="west-notree" url="${gsmctx}/inspectionplan/list.htm" onclick="changeMenu(this);"><span>校验计划台账</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="gsm_inspectionplan_list-over">
	<div id="_myInspectionPlan1" class="west-notree" url="${gsmctx}/inspectionplan/list-over.htm" onclick="changeMenu(this);"><span>已校验台账</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="GSM_INNER_CHECK_REPORT_INPUT1">
	<div id="_report_input" class="west-notree" url="${gsmctx}/inspectionplan/inner-report/input.htm" onclick="changeMenu(this);"><span>內校报告</span></div>
</security:authorize>
<security:authorize ifAnyGranted="GSM_INNER_CHECK_REPORT_LIST">
	<div id="_report_list" class="west-notree" url="${gsmctx}/inspectionplan/inner-report/list.htm" onclick="changeMenu(this);"><span>內校报告台账</span></div>
</security:authorize>
<security:authorize ifAnyGranted="GSM_FOREIGN_REPORT_LIST">
	<div id="foreing_report_list" class="west-notree" url="${gsmctx}/inspectionplan/external/list.htm" onclick="changeMenu(this);"><span>外校报告台账</span></div>
</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>

  