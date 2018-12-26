<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="carmfg-defective-goods">
	<div id="_defective_goods_list" class="west-notree" url="${carmfgctx}/defective-goods/ledger/list.htm" onclick="changeMenu(this);"><span>过程不良品处理台账</span></div>
</security:authorize>
<security:authorize ifAnyGranted="carmfg-defective-goods-packing">
	<div id="_defective_goods_packing_list" class="west-notree" url="${carmfgctx}/defective-goods/ledger/packing-list.htm" onclick="changeMenu(this);"><span>未装车零部件不良台账</span></div>
</security:authorize>
<security:authorize ifAnyGranted="carmfg-defective-vehicle-parts">
	<div id="_vehicle_parts_list" class="west-notree" url="${carmfgctx}/defective-goods/ledger/vehicle-parts-list.htm" onclick="changeMenu(this);"><span>整车零部件不良台账</span></div>
</security:authorize>
<security:authorize ifAnyGranted="carmfg-defective-goods-report">
	<div id="_defective_goods_report" class="west-notree" url="${carmfgctx}/defective-goods/ledger/defective-goods-report.htm" onclick="changeMenu(this);"><span>不合格品统计分析</span></div>
</security:authorize>
<security:authorize ifAnyGranted="carmfg-defective-goods-amount">
	<div id="_defective_goods_amount" class="west-notree" url="${carmfgctx}/defective-goods/ledger/amount.htm" onclick="changeMenu(this);"><span>内损金额推移图</span></div>
</security:authorize>
<security:authorize ifAnyGranted="carmfg-defective-goods-inf-coll">
	<div id="_defective_goods_inf_coll" class="west-notree" url="${carmfgctx}/defective-goods/information-collection/list.htm" onclick="changeMenu(this);"><span>不合格品处理信息采集</span></div>
</security:authorize>
<script type="text/javascript" class="source">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>