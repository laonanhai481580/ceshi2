<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="mfg-manu-analyse-regular-right">
<div id="_regular_right" class="west-notree" url="${mfgctx}/manu-analyse/regular-right.htm" onclick="changeMenu(this);"><span>合格率/良品率推移图</span></div>
</security:authorize>
<security:authorize ifAnyGranted="mfg-manu-analyse-qualityrate-contrast">
<div id="_qualityrate_contrast" class="west-notree" url="${mfgctx}/manu-analyse/qualityrate-contrast.htm"  onclick="changeMenu(this);"><span>合格率对比分析</span></div>
</security:authorize>
<security:authorize ifAnyGranted="mfg-manu-analyse-general-plato">
<div id="_general_plato" class="west-notree" url="${mfgctx}/manu-analyse/general-plato.htm"  onclick="changeMenu(this);"><span>质量综合分析柏拉图</span></div>
</security:authorize>
<security:authorize ifAnyGranted="mfg-manu-analyse-unquality-trend">
<div id="_unquality_trend" class="west-notree" url="${mfgctx}/manu-analyse/unquality-trend.htm" onclick="changeMenu(this);"><span>单项不良率/数推移图</span></div>
</security:authorize>
<security:authorize ifAnyGranted="mfg-manu-analyse-qualityrate-top">
<div id="_qualityrate_top10" class="west-notree" url="${mfgctx}/manu-analyse/qualityrate-top10.htm" onclick="changeMenu(this);"><span>不合格TOP10对比分析</span></div>
</security:authorize>
<security:authorize ifAnyGranted="mfg-spc-control-list">
<%-- <div id="_spc_control" class="west-notree" url="${mfgctx}/spc-control/list.htm" onclick="changeMenu(this);"><span>过程检验工艺控制点</span></div> --%>
</security:authorize>
<!-- <div id="_defective_goods_total" class="west-notree" url="${mfgctx}/defective-goods/ledger/total.htm"  onclick="changeMenu(this);"><span>制程质量成本汇总</span></div>
<div id="_quality_trend" class="west-notree" url="${mfgctx}/manu-analyse/quality-trend.htm" onclick="changeMenu(this);"><span>新合格率推移图</span></div>
<div id="_qualityrate_analysis" class="west-notree" url="${mfgctx}/manu-analyse/qualityrate-analysis.htm" onclick="changeMenu(this);"><span>新合格率对比分析</span></div> -->
<script type="text/javascript" class="source">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>