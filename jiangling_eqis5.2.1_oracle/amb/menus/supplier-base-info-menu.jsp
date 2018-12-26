<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="supplier-base-info-level-score-list">
	<div id="levelScorelist" class="west-notree" url="${supplierctx }/base-info/level-score/list.htm"
		onclick="changeMenu(this);">
		<span>供应商等级与得分关系</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-base-info-level-change-list">
	<div id="levelChangelist" class="west-notree" url="${supplierctx }/base-info/level-change/list.htm"
		onclick="changeMenu(this);">
		<span>供应商等级变更关系</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-base-info-material-type-goal-list">
	<div id="materialTypeGoallist" class="west-notree" url="${supplierctx }/base-info/material-type-goal/list.htm"
		onclick="changeMenu(this);">
		<span>物料类别目标</span>
	</div>
</security:authorize>
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
<security:authorize ifAnyGranted="data-source-list">
<div id="_data_source" class="west-notree"
	url="${supplierctx}/datasource/list.htm" onclick="changeMenu(this);">
	<span>数据来源</span>
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