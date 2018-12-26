<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>

<security:authorize ifAnyGranted="qualityProblem-input">
	<div    id="_qualityProblem_input" 
			class="west-notree" 
			url="${newproductctx}/improve-management/input.htm"
			onclick="changeMenu(this);">
		<span>质量问题录入</span>
	</div>
</security:authorize>

<security:authorize ifAnyGranted="qualityProblem-list">
	<div    id="_improve_management_list" 
			class="west-notree" 
			url="${newproductctx}/improve-management/list.htm"
			onclick="changeMenu(this);">
		<span>质量问题台账</span>
	</div>
</security:authorize>

<security:authorize ifAnyGranted="new-8d-input">
	<div id="_8d_input" class="west-notree" url="${newproductctx}/newproduct-g8d/input.htm"
		onclick="changeMenu(this);">
		<span>8D报告单</span>
	</div>
</security:authorize>

<security:authorize ifAnyGranted="new-8d-list">
	<div id="_8d_list" class="west-notree" url="${newproductctx}/newproduct-g8d/list.htm"
		onclick="changeMenu(this);">
		<span>8D报告单台账</span>
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