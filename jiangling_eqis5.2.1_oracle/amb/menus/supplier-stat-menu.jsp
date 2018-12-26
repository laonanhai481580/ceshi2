<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<div id="myInspectionOneChart" class="west-notree" url="${supplierctx }/stat/inspection-one-chart.htm" onclick="changeMenu(this);"><span>进货检验一次合格率</span></div>
<div id="myInspectionChart" class="west-notree" url="${supplierctx }/stat/inspection-chart.htm" onclick="changeMenu(this);"><span>原物料供应商合格率</span></div>
<div id="myDefectiveChart" class="west-notree" url="${supplierctx }/stat/defective-goods-chart.htm" onclick="changeMenu(this);"><span>进货检验不合格分析</span></div>
<div id="mySupplierRateChart" class="west-notree" url="${supplierctx }/stat/supplier-rate-chart.htm" onclick="changeMenu(this);"><span>供应商合格率对比</span></div>
<div id="myMaterialRateChart" class="west-notree" url="${supplierctx }/stat/material-rate-chart.htm" onclick="changeMenu(this);"><span>物料合格率对比</span></div>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>