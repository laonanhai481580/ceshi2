<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="supplier-change-list">
	<div id="changelist" class="west-notree" url="${supplierctx }/change/list.htm"
		onclick="changeMenu(this);">
		<span>PCN台帐</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-change-input">
	<div id="changeInput" class="west-notree" url="${supplierctx }/change/input.htm"
		onclick="changeMenu(this);">
		<span>PCN 申请单</span>
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