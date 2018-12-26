<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<a class="scroll-left-btn" onclick="_scrollRight();">&lt;&lt;</a>
<div class="fix-menu">
	<ul class="scroll-menu">
	    <security:authorize ifAnyGranted="gsm_new_equipment_list,gsm_new_equipment_list"> 
			<li id="newequipment"><span><span><a href="<grid:authorize code="gsm_new_equipment_input,gsm_new_equipment_list" systemCode="gsm"></grid:authorize>">新设备登记</a></span></span></li>
		</security:authorize>
		<security:authorize ifAnyGranted="gsm_equipment_list,gsm_equipment_input,gsm_equipment-dept_list,gsm_equipment_transfer_list,gsm_equipment_list-y">
			<li id="equipment"><span><span><a href="<grid:authorize code="gsm_equipment_list,gsm_equipment_input,gsm_equipment-dept_list,gsm_equipment_transfer_list,gsm_equipment_list-y" systemCode="gsm"></grid:authorize>">量检具管理</a></span></span></li>
		</security:authorize> 
		<security:authorize ifAnyGranted="gsm_inspectionplan_list">
			<li id="inspectionplan"><span><span><a href="<grid:authorize code="gsm_inspectionplan_list" systemCode="gsm"></grid:authorize>">校验计划</a></span></span></li>
		</security:authorize>
		<security:authorize ifAnyGranted="gsm_equipmentmsaplan_list">
			<li id="inspectionmsaplan"><span><span><a href="<grid:authorize code="gsm_equipmentmsaplan_list" systemCode="gsm"></grid:authorize>">MSA计划</a></span></span></li>
		</security:authorize>
		<security:authorize ifAnyGranted="gsm_gsmUseRecord,gsm_borrowRecord_input,gsm_borrowRecord_list">
			<li id="myUseRecord"><span><span><a href="<grid:authorize code="gsm_gsmUseRecord,gsm_borrowRecord_input,gsm_borrowRecord_list" systemCode="gsm"></grid:authorize>">量检具借还管理</a></span></span></li>
		</security:authorize>
		<security:authorize ifAnyGranted="gsm_nonconformity_list">
			<li id="nonconformity"><span><span><a href="${gsmctx }/nonconformity/list.htm">不合格品处理</a></span></span></li>
		</security:authorize>
		<security:authorize ifAnyGranted="gsm_entrust_list,gsm_entrust_input">
			<li id="entrust"><span><span><a href="<grid:authorize code="gsm_entrust_list,gsm_entrust_input" systemCode="gsm"></grid:authorize>">外校委托</a></span></span></li>
		</security:authorize>
		<security:authorize ifAnyGranted="GSM_ANALYSIS,GSM_INSPECTION_COMPLETE_LIST,GSM_INSPECTION_PASS_LIST,GSM_INSPECTION_INTIME_LIST,GSM_MSA_PASS_LIST">
			<li id="analysis"><span><span><a href="<grid:authorize code="GSM_ANALYSIS,GSM_INSPECTION_COMPLETE_LIST,GSM_INSPECTION_PASS_LIST,GSM_INSPECTION_INTIME_LIST,GSM_MSA_PASS_LIST" systemCode="gsm"></grid:authorize>">统计分析</a></span></span></li>
		</security:authorize>
		<security:authorize ifAnyGranted="gsm_base-inspectionbase,GSM_CHECK_STANDARD_LIST,GSM_CHECK_ITEM_LIST">
			<li id="baseInfo"><span><span><a href="<grid:authorize code="gsm_base-inspectionbase,GSM_CHECK_STANDARD_LIST,GSM_CHECK_ITEM_LIST" systemCode="gsm"></grid:authorize>">基础设置</a></span></span></li>
		</security:authorize>
	</ul>
</div>
<a class="scroll-right-btn" onclick="_scrollLeft();">&gt;&gt;</a>

<div class="hid-header" onclick=headerChange(this); title="折叠/展开"></div>
<script>
    var topMenu='sysPlan';
	$('#'+secMenu).addClass('sec-selected');
</script>

