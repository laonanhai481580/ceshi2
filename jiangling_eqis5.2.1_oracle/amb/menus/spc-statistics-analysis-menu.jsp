<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>

<security:authorize ifAnyGranted="spc_statistics-analysis_cpk-trend">
	<div id="_cpk_trend" class="west-notree" url="${spcctx }/statistics-analysis/cpk-trend.htm" onclick="changeMenu(this);"><span>质量特性对比分析</span></div>
</security:authorize>
<security:authorize ifAnyGranted="spc_statistics-analysis_sign-cpk-trend">
	<div id="_sign_cpk_trend" class="west-notree" url="${spcctx }/statistics-analysis/sign-cpk-trend.htm" onclick="changeMenu(this);"><span>层别信息CPK对比分析</span></div>
</security:authorize>
<security:authorize ifAnyGranted="spc_statistics-analysis_cpk-appraisal">
	<div id="_cpk_table" class="west-notree" url="${spcctx }/statistics-analysis/cpk-appraisal.htm" onclick="changeMenu(this);"><span>CPK过程能力考评表</span></div>
</security:authorize>
<security:authorize ifAnyGranted="spc_statistics-analysis_spc-application">
	<div id="_spc_process" class="west-notree" url="${spcctx }/statistics-analysis/spc-application.htm" onclick="changeMenu(this);"><span>SPC应用状况报表</span></div>
</security:authorize>
<security:authorize ifAnyGranted="spc_statistics-analysis_reason-measure">
	<div id="_cpk_reason" class="west-notree" url="${spcctx }/statistics-analysis/reason-measure.htm" onclick="changeMenu(this);"><span>异常原因及改善措施表</span></div>
</security:authorize>
<security:authorize ifAnyGranted="spc_statistics-analysis_data-analysis">
	<div id="_data_analysis" class="west-notree" url="${spcctx }/statistics-analysis/data-analysis.htm" onclick="changeMenu(this);"><span>数据分析</span></div>
</security:authorize>
<security:authorize ifAnyGranted="spc_statistics-analysis_data-about-analysis">
	<div id="_aboutAnalysis" class="west-notree" url="${spcctx }/statistics-analysis/data-about-analysis.htm" onclick="changeMenu(this);"><span>相关分析</span></div>
</security:authorize>

<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>