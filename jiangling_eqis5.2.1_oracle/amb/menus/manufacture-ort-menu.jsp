<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="MFG_ORT_PLAN_LIST">
	<div id="_ORT_PLAN_LIST" class="west-notree" url="${mfgctx}/ort/ort-plan/list.htm"  onclick="changeMenu(this);"><span>ORT计划</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_ORT_TEST_INPUT">
	<div id="_ORT_TEST_INPUT" class="west-notree" url="${mfgctx}/ort/ort-entrust/input.htm"  onclick="changeMenu(this);"><span>实验委托单</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_ORT_TEST_LIST">
	<div id="_ORT_TEST_LIST" class="west-notree" url="${mfgctx}/ort/ort-entrust/list.htm"  onclick="changeMenu(this);"><span>实验委托台账</span></div>
</security:authorize>
<script type="text/javascript" class="source">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>