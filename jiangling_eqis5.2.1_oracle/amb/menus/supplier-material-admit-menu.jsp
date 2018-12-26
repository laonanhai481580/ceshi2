<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="supplier-material-evaluate-list">
	<div id="evaluatelist" class="west-notree" url="${supplierctx }/material-admit/evaluate/list.htm"
		onclick="changeMenu(this);">
		<span>样件评估台帐</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-material-evaluate-input">
	<div id="evaluateInput" class="west-notree" url="${supplierctx }/material-admit/evaluate/input.htm"
		onclick="changeMenu(this);">
		<span>样件评估表</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-material-admit-list1">
	<div id="admitlist" class="west-notree" url="${supplierctx }/material-admit/admit/list.htm"
		onclick="changeMenu(this);">
		<span>材料承认台帐</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-material-admit-input1">
	<div id="admitInput" class="west-notree" url="${supplierctx }/material-admit/admit/input.htm"
		onclick="changeMenu(this);">
		<span>材料承认申请表</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-approval-list">
	<div id="approvalList" class="west-notree" url="${supplierctx }/material-admit/approval/list.htm"
		onclick="changeMenu(this);">
		<span>材料承认台帐</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-approval-input">
	<div id="approvalInput" class="west-notree" url="${supplierctx }/material-admit/approval/input.htm"
		onclick="changeMenu(this);">
		<span>材料承认申请表</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-material-data-supply-list1">
	<div id="dataSupplylist" class="west-notree" url="${supplierctx }/material-admit/data-supply/list.htm"
		onclick="changeMenu(this);">
		<span>GP资料提供台帐</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-material-data-supply-input1">
	<div id="dataSupplyInput" class="west-notree" url="${supplierctx }/material-admit/data-supply/input.htm"
		onclick="changeMenu(this);">
		<span>GP资料提供申请表</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-materials-file">
<div id="_supplier_materials" class="west-notree"
	url="${supplierctx}/material-admit/supplier-materials-file/list.htm" onclick="changeMenu(this);">
	<span>供应商附件</span>
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