<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
	<security:authorize ifAnyGranted="iqc_batch_pass_rate">
		<div id="_batch_pass_rate" class="west-notree" url="${iqcctx}/statistical-analysis/iqc-batch-pass-rate.htm" onclick="changeMenu(this);"><span>IQC批次合格率推移图</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc_sampling_bad">
		<div id="_sampling_bad" class="west-notree" url="${iqcctx}/statistical-analysis/iqc-sampling-bad.htm" onclick="changeMenu(this);"><span>不良项目柏拉图</span></div>
	</security:authorize>
<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>