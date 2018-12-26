<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>



<security:authorize ifAnyGranted="MFG_IPQC_INSPECTION_LIST">
	<div id="_IPQC_INSPECTION_LIST" class="west-notree" url="${mfgctx}/ipqc/ipqc-inspection/list.htm"  onclick="changeMenu(this);"><span>IPQC检验报告建立库</span></div>
</security:authorize>
 <security:authorize ifAnyGranted="MFG_IPQC_INSPECTION_LIST_PENDING">
	<div id="_IPQC_INSPECTION_LIST_PENDING" class="west-notree" url="${mfgctx}/ipqc/ipqc-inspection/list-pending.htm"  onclick="changeMenu(this);"><span>待审核IPQC检验报告</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_IPQC_INSPECTION_LIST_OK">
	<div id="_IPQC_INSPECTION_LIST_OK" class="west-notree" url="${mfgctx}/ipqc/ipqc-inspection/list-ok.htm"  onclick="changeMenu(this);"><span>IPQC检验报告完成台账</span></div>
</security:authorize> 
 


<script type="text/javascript" class="source">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>