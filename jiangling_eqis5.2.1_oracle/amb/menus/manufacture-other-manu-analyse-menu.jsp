<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
    <div style="display: block; height: 10px;"></div>
	<div id="_regular_right" class="west-notree" url="${mfgctx}/other-manu-analyse/regular-right.htm" onclick="changeMenu(this);"><span>合格率/良品率推移图</span></div>
	<div id="_qualityrate_contrast" class="west-notree" url="${mfgctx}/other-manu-analyse/qualityrate-contrast.htm"  onclick="changeMenu(this);"><span>合格率对比分析</span></div>
	<div id="_general_plato" class="west-notree" url="${mfgctx}/other-manu-analyse/general-plato.htm"  onclick="changeMenu(this);"><span>质量综合分析柏拉图</span></div>
	<div id="_unquality_trend" class="west-notree" url="${mfgctx}/other-manu-analyse/unquality-trend.htm" onclick="changeMenu(this);"><span>单项不良率/数推移图</span></div>
	<div id="_qualityrate_top10" class="west-notree" url="${mfgctx}/other-manu-analyse/qualityrate-top10.htm" onclick="changeMenu(this);"><span>不合格TOP10对比分析</span></div>
	<script type="text/javascript" class="source">
		$().ready(function(){
			$('#'+thirdMenu).addClass('west-notree-selected');
		});
		function changeMenu(obj){
			window.location = $(obj).attr('url');
		}
	</script>