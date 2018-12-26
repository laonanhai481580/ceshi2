<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="estimate-indicator-list">
<div id="_estimate_indicator" class="west-notree"
	url="${supplierctx}/estimate/indicator/quarter/list.htm" onclick="changeMenu(this);">
	<span>考评指标维护</span></div>
</security:authorize>
<security:authorize ifAnyGranted="estimate-model-list">
<div id="_estimate_model" class="west-notree"
	url="${supplierctx}/estimate/model/quarter/list.htm" onclick="changeMenu(this);">
	<span>考评模型维护</span></div>
</security:authorize>
<%-- <security:authorize ifAnyGranted="supervision-grade-list">
<div id="_check_grade" class="west-notree" url="${supplierctx }/supervision/check-grade/list.htm"
	onclick="changeMenu(this);">
	<span><s:text name='supplier.supervision.grade-list'/></span>
</div>
</security:authorize>
<security:authorize ifAnyGranted="admittance-survey-list">
<div id="_survey_grade" class="west-notree" url="${supplierctx }/admittance/survey-grade/list.htm"
	onclick="changeMenu(this);">
	<span><s:text name='supplier.admittance.survey-list'/></span>
</div>
</security:authorize>
<security:authorize ifAnyGranted="manager-degree-list">
<div id="_estimate_degree" class="west-notree"
	url="${supplierctx}/manager/degree/degree-list.htm" onclick="changeMenu(this);">
	<span><s:text name='supplier.manager.degree-list'/></span>
</div>
</security:authorize> --%>
<security:authorize ifAnyGranted="data-source-list">
<div id="_data_source" class="west-notree"
	url="${supplierctx}/datasource/list.htm" onclick="changeMenu(this);">
	<span><s:text name='supplier.data-source.list'/></span>
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