<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul>
	<security:authorize
		ifAnyGranted="new-project-info-list">
		<li id="project-info"><span><span><a
					href="<grid:authorize code="new-project-info-list" systemCode="newproduct"></grid:authorize>">项目管理</a></span></span></li>
	</security:authorize>
	<security:authorize
		ifAnyGranted="turnphaseCheck-input,turnphase-check">
		<li id="turnphase_check_sec"><span><span><a
					href="<grid:authorize code="turnphase-check,turnphaseCheck-input" systemCode="newproduct"></grid:authorize>">转阶段检查</a></span></span></li>
	</security:authorize>
	<security:authorize
		ifAnyGranted="qualityProblem-list,qualityProblem-input,new-8d-list,new-8d-input">
		<li id="improve_management"><span><span><a
					href="<grid:authorize code="qualityProblem-list,qualityProblem-input,new-8d-list,new-8d-input" systemCode="newproduct"></grid:authorize>">质量问题改进管理</a></span></span></li>
	</security:authorize>
	<security:authorize
		ifAnyGranted="avoidProblem_list">
		<li id="avoid_problem"><span><span><a
					href="<grid:authorize code="avoidProblem_list" systemCode="newproduct"></grid:authorize>">应规避问题排查 </a></span></span></li>
	</security:authorize>
	<security:authorize
		ifAnyGranted="lessonLibrary-list">
		<li id="lesson_library"><span><span><a
					href="<grid:authorize code="lessonLibrary-list" systemCode="newproduct"></grid:authorize>">经验教训库</a></span></span></li>
	</security:authorize>
	<!--<security:authorize
		ifAnyGranted="project-query">
		<li id="project_resume"><span><span><a
					href="<grid:authorize code="project-query" systemCode="newproduct"></grid:authorize>">项目信息查询</a></span></span></li>
	</security:authorize>-->
	<security:authorize ifAnyGranted="new_statistical_analysis,new_statistical_analysis_weekOverdue,new_statistical_analysis_nextExpected,new_statistical_analysis_problemCompletion,new_statistical_analysis_problemType,new_statistical_analysis_problemLevel,new_statistical_analysis_problemState,new_statistical_analysis_carType,new_statistical_analysis_close_rate">
		<li id="statistical_analysis">
			<span><span>
				<a href='<grid:authorize code="new_statistical_analysis,new_statistical_analysis_weekOverdue,new_statistical_analysis_nextExpected,new_statistical_analysis_problemCompletion,new_statistical_analysis_problemType,new_statistical_analysis_problemLevel,new_statistical_analysis_problemState,new_statistical_analysis_carType,new_statistical_analysis_close_rate" systemCode="newproduct"></grid:authorize>'>统计分析</a>
			</span></span>
		</li>
	</security:authorize>
	<li id="base_set"><span><span><a
					href="${newproductctx}/base-set/stageexportfile/list.htm">基础设置</a></span></span></li>
</ul>
<div class="hid-header" onclick=headerChange(this); title="折叠/展开"></div>
<script>
	var topMenu='newproduct';
	$('#'+secMenu).addClass('sec-selected');
</script>


