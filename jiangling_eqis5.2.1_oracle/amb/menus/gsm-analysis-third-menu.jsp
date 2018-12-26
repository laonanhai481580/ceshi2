<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="GSM_INSPECTION_COMPLETE_LIST">
	<div id="complete_rate" class="west-notree" ><a href="${gsmctx}/data-acquisition/inspection-complete-rate.htm">校验计划完成率</a></div>
</security:authorize>
<security:authorize ifAnyGranted="GSM_INSPECTION_PASS_LIST">
	<div id="pass_rate" class="west-notree" ><a href="${gsmctx}/data-acquisition/inspection-pass-rate.htm">校验合格率</a></div>
</security:authorize>
<security:authorize ifAnyGranted="GSM_INSPECTION_INTIME_LIST">
	<div id="intime_rate" class="west-notree" ><a href="${gsmctx}/data-acquisition/inspection-intime-rate.htm">校验及时率</a></div>
</security:authorize>
<security:authorize ifAnyGranted="GSM_MSA_PASS_LIST">
	<div id="msa_pass_rate" class="west-notree" ><a href="${gsmctx}/data-acquisition/msa-pass-rate.htm">MSA合格率</a></div>
</security:authorize>
<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>