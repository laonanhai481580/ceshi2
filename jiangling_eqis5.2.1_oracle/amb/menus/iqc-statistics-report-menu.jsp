<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
	<security:authorize ifAnyGranted="iqc-statAnalyse-before-sales-ppm">
		<div id="myBeforeSalesChart" class="west-notree" url="${iqcctx }/inspection-statistics/before-sales-ppm.htm" onclick="changeMenu(this);"><span>售前PPM统计</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc-statAnalyse-ppm-stat-chart">
		<div id="myPpmStatChart" class="west-notree" url="${iqcctx }/inspection-statistics/ppm-stat-chart.htm" onclick="changeMenu(this);"><span>供应商/零部件PPM统计</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc-statAnalyse-ppm-rank-chart">
		<div id="myPpmRankChart" class="west-notree" url="${iqcctx }/inspection-statistics/ppm-rank-chart.htm" onclick="changeMenu(this);"><span>供应商/零部件PPM排名</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc-statAnalyse-part-unquality-item">
		<div id="myPartUnqualityItem" class="west-notree" url="${iqcctx }/inspection-statistics/part-unquality-item.htm" onclick="changeMenu(this);"><span>零部件责任不良明细</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc-statAnalyse-iqc-ppm-rank">
		<div id="myIqcPpmRank" class="west-notree" url="${iqcctx }/inspection-statistics/iqc-ppm-rank.htm" onclick="changeMenu(this);"><span>进货检验PPM排名</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc-statAnalyse-check-type-chart">
		<div id="myCheckItemChart" class="west-notree" url="${iqcctx }/inspection-statistics/check-type-chart.htm" onclick="changeMenu(this);"><span>外购检验项目不良分析</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc-statAnalyse-defective-goods-chart">
		<div id="myDefectiveChart" class="west-notree" url="${iqcctx }/defective-goods/defective-goods-chart.htm" onclick="changeMenu(this);"><span>不合格品处理统计分析</span></div>
	</security:authorize>
<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>