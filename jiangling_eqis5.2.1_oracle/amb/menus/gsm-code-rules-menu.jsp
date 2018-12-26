<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<%-- <security:authorize ifAnyGranted="gsm_equipment_code-rules_list"> --%>
<%-- 	<div id="MyCodeRules" class="west-notree" url="${gsmctx}/code-rules/list.htm" onclick="changeMenu(this);"><span>计量编号规则</span></div> --%>
<%-- </security:authorize>   --%>
<security:authorize ifAnyGranted="GSM_CHECK_STANDARD_LIST">
	<div id="checkStandard" class="west-notree" url="${gsmctx}/base/check-standard/list.htm" onclick="changeMenu(this);"><span>检验标准</span></div>
</security:authorize>  
<security:authorize ifAnyGranted="GSM_CHECK_ITEM_LIST">
	<div id="checkItem" class="west-notree" url="${gsmctx}/base/check-item/list.htm" onclick="changeMenu(this);"><span>检验项目</span></div>
</security:authorize>  
<security:authorize ifAnyGranted="GSM_EQUIPMENT_STANDARD_LIST">
	<div id="standard" class="west-notree" url="${gsmctx}/base/equipment-standard/standard-list.htm" onclick="changeMenu(this);"><span>仪器标准件维护</span></div>
</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>

  