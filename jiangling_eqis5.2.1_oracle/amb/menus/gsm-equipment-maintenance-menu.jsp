<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="gsm_equipment-maintenance_list">
	<div id="_mymaintenance" class="west-notree" url="${gsmctx}/equipment-maintenance/list.htm" onclick="changeMenu(this);"><span>量检具维修台账</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="gsm_equipment-maintenance_list-over">
	<div id="_mymaintenance1" class="west-notree" url="${gsmctx}/equipment-maintenance/list-over.htm" onclick="changeMenu(this);"><span>量检具维修完成台账</span></div>
</security:authorize> 
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>

  