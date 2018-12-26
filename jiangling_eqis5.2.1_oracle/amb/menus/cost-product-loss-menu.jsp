<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<div id="_product_loss" class="west-notree" url="${costctx}/innerproductloss/innerProductLoss-list.htm" onclick="changeMenu(this);"><span>内部成品损失维护</span></div>
<script type="text/javascript" class="source">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>