<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="gsm_borrowRecord_input">
	<div id="borrowRecordInput" class="west-notree" url="${gsmctx}/gsmUseRecord/borrowRecord/input.htm" onclick="changeMenu(this);"><span>仪器借调申请单</span></div>
</security:authorize>
<security:authorize ifAnyGranted="gsm_borrowRecord_list">
	<div id="borrowRecordList" class="west-notree" url="${gsmctx}/gsmUseRecord/borrowRecord/list.htm" onclick="changeMenu(this);"><span>仪器借调申请台账</span></div>
</security:authorize> 
 
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>

  