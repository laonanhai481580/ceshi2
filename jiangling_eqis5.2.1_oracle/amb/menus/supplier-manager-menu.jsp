<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<div id="_state_query" class="west-notree" url="${supplierctx}/product-exploitation/list.htm"
	onclick="changeMenu(this);">
	<span>供应商开发状态查询</span>
</div>
<div id="_compensation_all" class="west-notree" url="${supplierctx }/manager/compensation-all.htm"
	onclick="changeMenu(this);">
	<span>供应商索赔管理</span>
</div>
<div id="_red_yellow" class="west-notree"
	url="${supplierctx}/manager/red-yellow.htm" onclick="changeMenu(this);"><span>供应商红黄牌</span>
</div>
 <div id="_improve_all" class="west-notree" url="${supplierctx }/manager/improve-all.htm"
	onclick="changeMenu(this);">
	<span>供应商改进管理</span>
</div>
<div id="_supplier_improve" class="west-notree" url="${supplierctx }/manager/supplier-improve.htm"
	onclick="changeMenu(this);">
	<span>供应商改进管理</span>
</div>
<script type="text/javascript" class="source">
		$(document).ready(function(){
			$('#'+thirdMenu).addClass('west-notree-selected');
		});
		function changeMenu(obj){
			window.location = $(obj).attr('url');
		}
	</script>