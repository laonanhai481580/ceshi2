<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="${ctx}/widgets/highcharts-4.0.4/highcharts.js"></script>
<script type="text/javascript" src="${ctx}/widgets/highcharts-4.0.4/highcharts-more.js"></script>
<script type="text/javascript" src="${ctx}/widgets/highcharts-4.0.4/highcharts-3d.js"></script>
<script type="text/javascript" src="${ctx}/widgets/highcharts-4.0.4/modules/exporting.js"></script>
<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript">
function showHint(name,data){
$(function () {
    $('#container').highcharts({
        chart: {
            polar: true,
            type: 'line'
        },
        title: {
            text: '质量管理体系',
            x: -50
        },
        pane: {
            size: '80%'
        },
        credits:{
        	enabled:false
        },
        exporting:{
        	enabled:false
        },
        xAxis: {
            categories: name,
            tickmarkPlacement: 'on',
            lineWidth: 0
        },
        yAxis: {
            gridLineInterpolation: 'polygon',
            lineWidth: 0,
            min: 0
        },
        tooltip: {
            shared: false,
            pointFormat: '<span style="color:{point.color}"></span> {series.name}: <b>{point.y}</b><br/>'
        },
        legend: {
            align: 'right',
            verticalAlign: 'top',
            y: 70,
            layout: 'vertical'
        },
        series: [{
            name: '数据',
            data: data,
            pointPlacement: 'on'
        }]
    });
});
}
</script>
<div id="container" style="min-width: 400px; max-width: 800px; height: 400px; margin: 0 auto"></div>