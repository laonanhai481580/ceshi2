<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul>
	<security:authorize ifAnyGranted="spc_data-acquisition_list,spc_data-acquisition_off-line-list,spc_data-acquisition_maintenance-list">
		<li id="data_acq"><span><span><a href="<grid:authorize code="spc_data-acquisition_list,spc_data-acquisition_off-line-list,spc_data-acquisition_maintenance-list" systemCode="spc"></grid:authorize>">数据采集</a></span></span></li>
	</security:authorize>
<%-- 	<security:authorize ifAnyGranted="base-info-monitor-program-monitor-list,spc_process-monitor_exception-message">
		<li id="process_monitor"><span><span><a href="<grid:authorize code="base-info-monitor-program-monitor-list,spc_process-monitor_exception-message" systemCode="spc"></grid:authorize>">过程监控</a></span></span></li>
	</security:authorize> --%>
	<security:authorize ifAnyGranted="base-info-monitor-program-monitor-list,spc_process-monitor_exception-message">
		<li id="process_monitor"><span><span><a href="<grid:authorize code="base-info-monitor-program-monitor-list,spc_process-monitor_exception-message" systemCode="spc"></grid:authorize>">过程监控</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="spc_statistics-analysis_cpk-trend,spc_statistics-analysis_sign-cpk-trend,spc_statistics-analysis_cpk-appraisal,spc_statistics-analysis_spc-application,spc_statistics-analysis_reason-measure,spc_statistics-analysis_data-analysis,spc_statistics-analysis_data-about-analysis">
		<li id="stat_analyse"><span><span><a href="<grid:authorize code="spc_statistics-analysis_cpk-trend,spc_statistics-analysis_sign-cpk-trend,spc_statistics-analysis_cpk-appraisal,spc_statistics-analysis_spc-application,spc_statistics-analysis_reason-measure,spc_statistics-analysis_data-analysis,spc_statistics-analysis_data-about-analysis" systemCode="spc"></grid:authorize>">统计分析</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="spc_base-info_process-define_list,spc_base-info_layer-type_list,spc_base-info_bs-rules_list,spc_base-info_monitor-program_list,spc_base-info_abnormal-reason_list,spc_base-info_improve-measure_list">
		<li id="base_info"><span><span><a href="<grid:authorize code="spc_base-info_process-define_list,spc_base-info_layer-type_list,spc_base-info_bs-rules_list,spc_base-info_monitor-program_list,spc_base-info_abnormal-reason_list,spc_base-info_improve-measure_list" systemCode="spc"></grid:authorize>">基础维护</a></span></span></li>
	</security:authorize>
</ul>
<div class="hid-header" onclick=headerChange(this); title="折叠/展开"></div>
<script>
	var topMenu='spc';
	$('#'+secMenu).addClass('sec-selected');
</script>