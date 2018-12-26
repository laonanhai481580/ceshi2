<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="gsm_entrust_input">
	<div id="entrustInput" class="west-notree" url="${gsmctx}/entrust/input.htm" onclick="changeMenu(this);"><span>外校委托单</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="gsm_entrust_list">
	<div id="entrustList" class="west-notree" url="${gsmctx}/entrust/list.htm" onclick="changeMenu(this);"><span>外校委托台账</span></div>
</security:authorize>


<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>
  