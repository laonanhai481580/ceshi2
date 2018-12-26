<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>

<security:authorize ifAnyGranted="supplier-improve-list">
	<div id="improveList" class="west-notree" url="${supplierctx }/improve/list.htm"
		onclick="changeMenu(this);">
		<span>进料异常纠正措施台账</span>
	</div>
</security:authorize>

<security:authorize ifAnyGranted="supplier-improve-oklist">
	<div id="improveokList" class="west-notree" url="${supplierctx }/improve/oklist.htm"
		onclick="changeMenu(this);">
		<span>进料异常纠正措施完成台账</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-improve-input">
	<div id="improveInput" class="west-notree" url="${supplierctx }/improve/input.htm"
		onclick="changeMenu(this);">
		<span>进料异常纠正措施单</span>
	</div>
</security:authorize>


<script type="text/javascript" class="source">
		$(document).ready(function(){
			$('#'+thirdMenu).addClass('west-notree-selected');
		});
		function changeMenu(obj){
			window.location = $(obj).attr('url');
		}
	</script>