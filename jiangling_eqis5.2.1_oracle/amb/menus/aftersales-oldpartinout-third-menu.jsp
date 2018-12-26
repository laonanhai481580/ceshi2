<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
	<security:authorize ifAnyGranted="aftersales-oldpart-inout-detail">
	<div id="aftersales-out" class="west-notree"  ><a href="${aftersalesctx}/oldpart-out/oldpartOutView-listEditable.htm">库存明细</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="aftersales-oldpart-inout-in">
	<div id="aftersales-in-info" class="west-notree"  ><a href="${aftersalesctx}/aftersales-oldpart-in-info/oldpartInInfo-listEditable.htm">入库记录</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="aftersales-oldpart-inout-out">
	<div id="aftersales-out-detail" class="west-notree"  ><a href="${aftersalesctx}/oldpart-out-detail/oldpartInOutDetail-listEditable.htm">台账查询</a></div>
	</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
</script>