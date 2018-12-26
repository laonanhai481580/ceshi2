<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="EPM_ORT_QUALIFIED">
	<div id="epm_ort_qualified" class="west-notree" ><a href="${epmctx}/data-acquisition/ort-qualified-rate.htm">可靠性合格率</a></div>
</security:authorize>
<security:authorize ifAnyGranted="EPM_ORT_ABNORMAL">
	<div id="epm_ort_abnormal" class="west-notree" ><a href="${epmctx}/data-acquisition/ort-abnormal-rate.htm">可靠性异常回复率</a></div>
</security:authorize>
<security:authorize ifAnyGranted="EPM_HSF_QUALIFIED">
	<div id="epm_hsf_qualified" class="west-notree" ><a href="${epmctx}/data-acquisition/hsf-qualified-rate.htm">HSF批次合格率</a></div>
</security:authorize>
<security:authorize ifAnyGranted="EPM_HSF_ACCOMPLISH">
	<div id="epm_hsf_accomplish" class="west-notree" ><a href="${epmctx}/data-acquisition/hsf-accomplish-rate.htm">HSF批次完成率</a></div>
<%-- 	<div id="epm_hsf_accomplish" class="west-notree" ><a href="${epmctx}/data-acquisition/hsf-11-rate.htm">HSF111率</a></div> --%>
</security:authorize>

<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>