<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>


<security:authorize ifAnyGranted="MFG_OQC_INSPECTION_REPORT_LIST">
	<div id="_OQC_INSPECTION_REPORT_LIST" class="west-notree" url="${mfgctx}/oqc/oqc-inspection/list.htm"  onclick="changeMenu(this);"><span>OQC检验报告台账建立库</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_OQC_INSPECTION_LIST_PENDING">
	<div id="_OQC_INSPECTION_LIST_PENDING" class="west-notree" url="${mfgctx}/oqc/oqc-inspection/list-pending.htm"  onclick="changeMenu(this);"><span>待审核OQC检验报告台账</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_OQC_INSPECTION_LIST_OK">
	<div id="_OQC_INSPECTION_LIST_OK" class="west-notree" url="${mfgctx}/oqc/oqc-inspection/list-ok.htm"  onclick="changeMenu(this);"><span>OQC检验报告台账</span></div>
</security:authorize>

<security:authorize ifAnyGranted="MFG_OQC_VISUAL_INSPECTION_REPORT_LIST">  
	<div id="_OQC_VISUAL_INSPECTION_REPORT_LIST" class="west-notree" url="${mfgctx}/oqc/oqcvisual-inspection/list.htm"  onclick="changeMenu(this);"><span>OQC外观检验台账建立库</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_OQC_VISUAL_INSPECTION_LIST_PENDING">  
	<div id="_OQC_VISUAL_INSPECTION_LIST_PENDING" class="west-notree" url="${mfgctx}/oqc/oqcvisual-inspection/list-pending.htm"  onclick="changeMenu(this);"><span>待审核OQC外观检验台账</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_OQC_VISUAL_INSPECTION_LIST_OK">
	<div id="_OQC_VISUAL_INSPECTION_LIST_OK" class="west-notree" url="${mfgctx}/oqc/oqcvisual-inspection/list-ok.htm"  onclick="changeMenu(this);"><span>完成OQC外观检验台账</span></div>
</security:authorize>


<security:authorize ifAnyGranted="MFG_OQC_OTHER_INSPECTION_REPORT_LIST">  
	<div id="_OQC_OTHER_INSPECTION_REPORT_LIST" class="west-notree" url="${mfgctx}/oqc/oqcother-inspection/list.htm"  onclick="changeMenu(this);"><span>OQC其他检验台账建立库</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_OQC_OTHER_INSPECTION_LIST_PENDING">  
	<div id="_OQC_OTHER_INSPECTION_LIST_PENDING" class="west-notree" url="${mfgctx}/oqc/oqcother-inspection/list-pending.htm"  onclick="changeMenu(this);"><span>待审核OQC其他检验台账</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_OQC_OTHER_INSPECTION_LIST_OK">
	<div id="_OQC_OTHER_INSPECTION_LIST_OK" class="west-notree" url="${mfgctx}/oqc/oqcother-inspection/list-ok.htm"  onclick="changeMenu(this);"><span>完成OQC其他检验台账</span></div>
</security:authorize>




<script type="text/javascript" class="source">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>