<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul>
<security:authorize ifAnyGranted="supplier-improve,supplier-improve-oklist,supplier-improve-list,supplier-imrpve-input">
		<li id="improve"><span><span><a href="<grid:authorize code="supplier-improve,supplier-improve-oklist,supplier-improve-list,supplier-imrpve-input" systemCode="supplier"></grid:authorize>">供应商改进</a></span></span></li>
</security:authorize>
<security:authorize ifAnyGranted="supplier_evaluate_add,supplier_evaluate_total-table,supplier_evaluate_all,supplier_evaluate_ledger,supplier_evaluate_manager_estimate-stat-distribution,supplier_evaluate_manager_qcds,supplier_evaluate_point-rank">
		<li id="evaluate"><span><span><a href="<grid:authorize code="supplier_evaluate_add,supplier_evaluate_total-table,supplier_evaluate_all,supplier_evaluate_ledger,supplier_evaluate_manager_warning-sign,supplier_evaluate_manager_estimate-stat-distribution,supplier_evaluate_manager_qcds,supplier_evaluate_point-rank" systemCode="supplier"></grid:authorize>">供应商考评</a></span></span></li>
</security:authorize>
<security:authorize ifAnyGranted="supplier-audit-year-list,supplier-audit-year-history-list,supplier-audit-month-list,supplier-audit-improve-list,supplier-audit-improve-input">
	<li id="audit"><span><span><a
				href="<grid:authorize code="supplier-audit-year-list,supplier-audit-year-history-list,supplier-audit-month-list,supplier-audit-improve-list,supplier-audit-improve-input" systemCode="supplier"></grid:authorize>">供应商审核</a>
		</span>
	</span>
	</li>
</security:authorize>
<security:authorize ifAnyGranted="archives-qualified,archives-qualified-cancle-list,archives-qualified-cancle-input,archives-eliminated,archives-message">
	<li id="archives"><span><span><a
				href="<grid:authorize code="archives-qualified,archives-qualified-cancle-list,archives-qualified-cancle-input,archives-eliminated,archives-message" systemCode="supplier"></grid:authorize>">供应商档案</a>
		</span>
	</span>
	</li>
</security:authorize>
<security:authorize ifAnyGranted="supplier-develop-survey-list,supplier-develop-list,supplier-develop-input,supplier-develop-monitor-list">
	<li id="develop">
	     <span>
	          <span><a href="<grid:authorize code="supplier-develop-survey-list,supplier-develop-list,supplier-develop-input,supplier-develop-monitor-list" systemCode="supplier"></grid:authorize>">供应商开发</a>
		</span></span>
	</li>
</security:authorize>
<security:authorize ifAnyGranted="supplier-change-list,supplier-change-input">
	<li id="change">
	     <span>
	          <span><a href="<grid:authorize code="supplier-change-list,supplier-change-input" systemCode="supplier"></grid:authorize>">PCN申请</a>
		</span></span>
	</li>
</security:authorize>
<security:authorize ifAnyGranted="supplier-material-evaluate-list,supplier-material-evaluate-input,supplier-material-admit-list,supplier-admit-evaluate-input,supplier-material-data-supply-list,supplier-admit-data-supply-input">
	<li id="admit">
	     <span>
	          <span><a href="<grid:authorize code="supplier-material-evaluate-list,supplier-material-evaluate-input,supplier-material-admit-list,supplier-admit-evaluate-input,supplier-material-data-supply-list,supplier-admit-data-supply-input" systemCode="supplier"></grid:authorize>">材料承认管理</a>
		</span></span>
	</li>
</security:authorize>
<security:authorize ifAnyGranted="supplier-abnormal-list">
		<li id="abnormal"><span><span><a href="<grid:authorize code="supplier-abnormal-list" systemCode="supplier"></grid:authorize>">上线异常数据</a></span></span></li>
</security:authorize>
<security:authorize ifAnyGranted="supplier-base-info-level-score-list,supplier-base-info-level-change-list,supplier-base-info-material-type-goal-list,estimate-indicator-list,estimate-model-list,data-source-list">
	<li id="baseInfo"><span><span><a href="<grid:authorize code="supplier-base-info-level-score-list,supplier-base-info-level-change-list,supplier-base-info-material-type-goal-list,estimate-indicator-list,estimate-model-list,data-source-list" systemCode="supplier"></grid:authorize>">基础维护</a>
		</span>
	</span>
	</li>
</security:authorize>
<security:authorize ifAnyGranted="SUPPLIER_ANALYSIS,SUPPLIER_INCOMING_CLOSE_LIST">
		<li  id="analysis" class="last"><span><span><a href="<grid:authorize code="SUPPLIER_ANALYSIS,SUPPLIER_INCOMING_CLOSE_LIST" systemCode="supplier"></grid:authorize>">统计分析</a></span></span></li>
</security:authorize>	
</ul>
<div class="hid-header" onclick=headerChange(this); title="折叠/展开"></div>
<script>
	var topMenu='supplier';
	$('#'+secMenu).addClass('sec-selected');
</script>


