<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="newproduct-stagefile-list">
	<div id="stageexportfile_list" class="west-notree" url="${newproductctx}/base-set/stageexportfile/list.htm" onclick="changeMenu(this);">
		<span>转段需提交材料</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="newproduct-projectstage-list">
	<div id="projecttypestage_list" class="west-notree" url="${newproductctx}/base-set/projecttypestage/list.htm" onclick="changeMenu(this);">
		<span>项目级别对应阶段维护</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="newproduct-goalitem-list">
	<div id="goalitem_list" class="west-notree" url="${newproductctx}/base-set/goal-item/list.htm" onclick="changeMenu(this);">
		<span>目标维护</span>
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