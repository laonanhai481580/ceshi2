<!-- 在途量检具菜单  张顺志 -->
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="gsm_new_equipment_input">
	<div id="NewEquipmentInput" class="west-notree" url="${gsmctx}/new-equipment/input.htm" onclick="changeMenu(this);"><span>新设备申请登记</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="gsm_new_equipment_list">
	<div id="NewEquipmentList" class="west-notree" url="${gsmctx}/new-equipment/list.htm" onclick="changeMenu(this);"><span>新设备申请登记台帐</span></div>
</security:authorize>

<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>