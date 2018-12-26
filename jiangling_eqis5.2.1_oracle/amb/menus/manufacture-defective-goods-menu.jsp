<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="MFG_DEFECTIVE-GOODS_FORM_FORM">
<div id="_defective_goods_form" class="west-notree" url="${mfgctx}/defective-goods/ledger/input.htm"  onclick="changeMenu(this);"><span>不合格品处理单</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_DEFECTIVE-GOODS_LIST_LIST">
<div id="_defective_goods_input" class="west-notree" url="${mfgctx}/defective-goods/ledger/list.htm"  onclick="changeMenu(this);"><span>不合格品处理台帐</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_DEFECTIVE-GOODS_MONITOR-LIST_LIST">
<div id="_defective_goods_monitor" class="west-notree" url="${mfgctx}/defective-goods/ledger/monitor-list.htm" onclick="changeMenu(this);"><span>不合格品流程跟踪</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_MANU-ANALYSE_DEFECTIVE-GOODS-REPORT_PAGE">
<div id="_defective_goods_report" class="west-notree" url="${mfgctx}/manu-analyse/defective-goods-report.htm"  onclick="changeMenu(this);"><span>不合格品统计分析</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_DEFECTIVE-GOODS_AMOUNT_PAGE">
<div id="_defective_goods_amount" class="west-notree" url="${mfgctx}/defective-goods/ledger/amount.htm" onclick="changeMenu(this);"><span>内部损失推移图</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_DEFECTIVE-GOODS_INTERNAL-TOTAL_PAGE">
<div id="_defective_goods_total" class="west-notree" url="${mfgctx}/defective-goods/ledger/internal-total.htm" onclick="changeMenu(this);"><span>内部损失成本汇总</span></div>
</security:authorize>
<script type="text/javascript" class="source">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>