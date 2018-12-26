<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ambition.carmfg.entity.*,java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
	<security:authorize ifAnyGranted="si-analysis-okAndNg-list">
		<div id="_si_okAndNg" class="west-notree" url="${sictx}/data-acquisition/ok-ng-rate.htm" onclick="changeMenu(this);"><span>SI合格率推移图</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="si-analysis-bad-list">
		<div id="_si_bad" class="west-notree" url="${sictx}/data-acquisition/bad-item-rate.htm" onclick="changeMenu(this);"><span>SI不良分布柏拉图</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="si-analysis-lrr-list">
		<div id="_si_lrr" class="west-notree" url="${sictx}/data-acquisition/lrr-rate.htm" onclick="changeMenu(this);"><span>SI LRR推移图</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="si-analysis-total-list">
		<div id="_si_total" class="west-notree" url="${sictx}/data-acquisition/total.htm" onclick="changeMenu(this);"><span>SI报告统计表</span></div>
	</security:authorize>
	<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>