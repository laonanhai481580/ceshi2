<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="gsm_equipmentmsaplan_list">
	<div id="_myInspectionmsaplan" class="west-notree" url="${gsmctx}/equipment-msaplan/list.htm" onclick="changeMenu(this);"><span>测量系统分析计划与实施记录表</span></div>
</security:authorize> 
<%-- <security:authorize ifAnyGranted="gsm_equipmentmsaplan_list-over">
	<div id="_myInspectionmsaplan1" class="west-notree" url="${gsmctx}/equipment-msaplan/list-over.htm" onclick="changeMenu(this);"><span>MSA已完台账</span></div>
</security:authorize>  --%>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>

  