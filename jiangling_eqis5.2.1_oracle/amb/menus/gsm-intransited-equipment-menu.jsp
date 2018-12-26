<!-- 在途量检具菜单  张顺志 -->
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="gsm_intransited-equipment_list">
	<div id="_myIntransitedEquipment" class="west-notree" url="${gsmctx}/intransited-equipment/list.htm" onclick="changeMenu(this);"><span>在途量检具台账</span></div>
</security:authorize> 
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>