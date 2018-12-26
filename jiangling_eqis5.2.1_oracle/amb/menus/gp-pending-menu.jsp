<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="gp-average-list">
	<div id="averageList" class="west-notree" url="${gpctx }/averageMaterial/list.htm"
		onclick="changeMenu(this);">
		<span>均质材料建立库</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="gp-average-list_pending">
	<div id="averageList_pending" class="west-notree" url="${gpctx }/averageMaterial/list-pending.htm"
		onclick="changeMenu(this);">
		<span>待审核均质材料</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="gp-gpmaterial_pending">
	<div id="gpmaterialList_pending" class="west-notree" url="${gpctx }/gpmaterial/list-pending.htm"
		onclick="changeMenu(this);">
		<span>待处理产品宣告</span>
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