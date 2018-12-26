<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
    <div style="display: block; height: 10px;"></div>

     <security:authorize ifAnyGranted="MFG_YIELD_LIST">
		<div id="_yield" class="west-notree" url="${mfgctx}/data-acquisition/analyse/yield.htm" onclick="changeMenu(this);"><span>制程良率推移图</span></div>
	 </security:authorize>	
	 <div id="_yieldInfo" class="west-notree" url="${mfgctx}/data-acquisition/analyse/yield-info.htm" onclick="changeMenu(this);"><span>制程良率明细推移图</span></div>
	 <security:authorize ifAnyGranted="MFG_IPQC_SAMPLING_BAD_LIST">
		<div id="_ipqc_sampling_bad" class="west-notree" url="${mfgctx}/data-acquisition/analyse/ipqc-sampling-bad.htm" onclick="changeMenu(this);"><span>不良项目柏拉图</span></div>
	 </security:authorize>	
	 <security:authorize ifAnyGranted="MFG_IPQC_BATCH_PASS_LIST">	
		<div id="_ipqc_batch_pass_rate" class="west-notree" url="${mfgctx}/data-acquisition/analyse/ipqc-batch-pass-rate.htm" onclick="changeMenu(this);"><span>批次合格率推移图</span></div>
	 </security:authorize>	
	 <security:authorize ifAnyGranted="MFG_OQC_SAMPLING_PASS_LIST">	
		<div id="_oqc_sampling_rate" class="west-notree" url="${mfgctx}/data-acquisition/analyse/oqc-sampling-rate.htm" onclick="changeMenu(this);"><span>OQC批次合格率推移图</span></div>
	 </security:authorize>	
	 <security:authorize ifAnyGranted="MFG_OQC_SAMPLING_BAD_LIST">	
		<div id="_oqc_sampling_bad" class="west-notree" url="${mfgctx}/data-acquisition/analyse/oqc-sampling-bad.htm" onclick="changeMenu(this);"><span>OQC不良项目柏拉图</span></div>
	 </security:authorize>	
	 <security:authorize ifAnyGranted="MFG_OQC_PASS_LIST">	
		<div id="_oqc_pass_rate" class="west-notree" url="${mfgctx}/data-acquisition/analyse/oqc-pass-rate.htm" onclick="changeMenu(this);"><span>OQC良率推移图</span></div>
	 </security:authorize>
	 <security:authorize ifAnyGranted="MFG_OQC_DELIVER_SAMPLING_LIST">	
		<div id="_oqc_deliver_sampling_rate" class="west-notree" url="${mfgctx}/data-acquisition/analyse/oqc-deliver-sampling-rate.htm" onclick="changeMenu(this);"><span>OQC出货批次合格率</span></div>
	 </security:authorize>
	<script type="text/javascript" class="source">
		$().ready(function(){
			$('#'+thirdMenu).addClass('west-notree-selected');
		});
		function changeMenu(obj){
			window.location = $(obj).attr('url');
		}
	</script>
