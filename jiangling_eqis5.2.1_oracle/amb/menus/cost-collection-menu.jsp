<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="cost_reinspection_list">
<div id="_reinspection" class="west-notree" url="${costctx}/collection/reinspection-list.htm" onclick="changeMenu(this);"><span>重检、拆解费用统计</span></div>
</security:authorize>
<security:authorize ifAnyGranted="cost_material_loss_list">
<div id="_material_loss" class="west-notree" url="${costctx}/collection/material-loss-list.htm" onclick="changeMenu(this);"><span>材料损失金额统计</span></div>
</security:authorize>
<security:authorize ifAnyGranted="cost_customer_claim_list">
<div id="_customer_claim" class="west-notree" url="${costctx}/collection/customer-claim-list.htm" onclick="changeMenu(this);"><span>客户索赔统计</span></div>
</security:authorize>
<security:authorize ifAnyGranted="cost_quality_cost_list">
<div id="_quality_cost" class="west-notree" url="${costctx}/collection/quality-cost-list.htm" onclick="changeMenu(this);"><span>质量处理费用统计</span></div>
</security:authorize>
<security:authorize ifAnyGranted="cost_return_loss_list">
<div id="_return_loss" class="west-notree" url="${costctx}/collection/return-loss-list.htm" onclick="changeMenu(this);"><span>退货损失统计</span></div>
</security:authorize>
<security:authorize ifAnyGranted="cost_equipment_loss_list">
<div id="_equipment_loss" class="west-notree" url="${costctx}/collection/equipment-loss-list.htm" onclick="changeMenu(this);"><span>检测设备折旧金额统计</span></div>
</security:authorize>
<security:authorize ifAnyGranted="cost_quality_failure_list">
<div id="_quality_failure" class="west-notree" url="${costctx}/collection/quality-failure-list.htm" onclick="changeMenu(this);"><span>品质故障统计</span></div>
</security:authorize>
<security:authorize ifAnyGranted="cost_qa_pay_list">
<div id="_qa_pay" class="west-notree" url="${costctx}/collection/qa-pay-list.htm" onclick="changeMenu(this);"><span>品保部薪酬统计</span></div>
</security:authorize>
<security:authorize ifAnyGranted="cost_department_cost_list">
<div id="_department_cost" class="west-notree" url="${costctx}/collection/department-cost-list.htm" onclick="changeMenu(this);"><span>部门费用统计</span></div>
</security:authorize>
<script type="text/javascript" class="source">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>