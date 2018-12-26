<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<div id="_plan_all" class="west-notree" url="${supplierctx }/supervision/check-plan/list.htm"
	onclick="changeMenu(this);">
	<span>监察计划管理台帐</span>
</div>
<div id="_check_report_input" class="west-notree" url="${supplierctx }/supervision/check-report/input.htm"
	onclick="changeMenu(this);">
	<span>供应商监察评分表</span>
</div>
<div id="_check_report" class="west-notree" url="${supplierctx }/supervision/check-report/list.htm"
	onclick="changeMenu(this);">
	<span>供应商监察报告</span>
</div>
<script type="text/javascript" class="source">
		$(document).ready(function(){
			$('#'+thirdMenu).addClass('west-notree-selected');
		});
		function changeMenu(obj){
			window.location = $(obj).attr('url');
		}
	</script>