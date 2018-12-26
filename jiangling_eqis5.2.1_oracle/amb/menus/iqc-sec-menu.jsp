<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul>
	<security:authorize ifAnyGranted="iqc-incominginspectionactionsreport-uncheck,iqc-check-group,iqc-wait-audit-list,iqc-inspection-report-complete-list,iqc-inspection-report-unlist,iqc-incominginspectionactionsreport-list,iqc-sample-list,iqc-inspection-no-list,iqc-incominginspectionactionsreport-checked">
		<li id="inspectionReport"><span><span><a href='<grid:authorize code="iqc-incominginspectionactionsreport-uncheck,iqc-check-group,iqc-wait-audit-list,iqc-inspection-report-complete-list,iqc-inspection-report-unlist,iqc-incominginspectionactionsreport-list,iqc-sample-list,iqc-inspection-no-list" systemCode="iqc"></grid:authorize>'>检验报告</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="taskmonitor-overdue-list,taskmonitor-task-list">
		<li id="taskMontior"><span><span><a href='<grid:authorize code="taskmonitor-overdue-list,taskmonitor-task-list" systemCode="iqc"> </grid:authorize>'>任务监控</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc-experimental-delegation-input,iqc-experimental-delegation-list,iqc-experimental-delegation-history-list">
		<%-- <li id="_experimental_delegation"><span><span><a href='<grid:authorize code="iqc-experimental-delegation-input,iqc-experimental-delegation-list,iqc-experimental-delegation-history-list" systemCode="iqc"> </grid:authorize>'>实验委托管理</a></span></span></li> --%>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc_batch_pass_rate,iqc_sampling_bad,iqc_statistical_analysis">
		<li id="analyse"><span><span><a href='<grid:authorize code="iqc_batch_pass_rate,iqc_sampling_bad,iqc_statistical_analysis" systemCode="iqc"></grid:authorize>'>统计分析</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc-inspecting-indicator-list,iqc-inspecting-item-list,iqc-inspection-interval-list">
		<li id="base"><span><span><a href='<grid:authorize code="iqc-inspecting-indicator-list,iqc-inspecting-item-list,iqc-inspection-interval-list" systemCode="iqc"></grid:authorize>'>基础维护</a></span></span></li>
	</security:authorize>	
	<security:authorize ifAnyGranted="iqc-sample-standard-code-letter-list,iqc-sample-standard-ordinary-sample-list,iqc-sample-standard-tighten-sample-list,iqc-sample-standard-relax-sample-list,iqc-sample-standard-code-letter-cl-list,iqc-sample-standard-count-sample-list,iqc-sample-standard-measure-sample-list,iqc-sample-standard-transition-rule-input">
		<li id="standard"><span><span><a href='<grid:authorize code="iqc-sample-standard-code-letter-list,iqc-sample-standard-ordinary-sample-list,iqc-sample-standard-tighten-sample-list,iqc-sample-standard-relax-sample-list,iqc-sample-standard-code-letter-cl-list,iqc-sample-standard-count-sample-list,iqc-sample-standard-measure-sample-list,iqc-sample-standard-transition-rule-input" systemCode="iqc"></grid:authorize>'>抽样方案维护</a></span></span></li>
	</security:authorize>
</ul>
<div class="hid-header" onclick=headerChange(this); title="折叠/展开"></div>
<script>
    var topMenu='sysPlan';
	$('#'+secMenu).addClass('sec-selected');
</script>