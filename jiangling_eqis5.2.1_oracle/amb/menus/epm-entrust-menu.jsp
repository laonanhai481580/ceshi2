<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="epm_entrustHsf_input">
	<div id="entrustInput" class="west-notree" url="${epmctx}/entrust-hsf/input.htm" onclick="changeMenu(this);"><span>HSF实验委托单</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="epm_entrustHsf-Sub_list">
	<div id="entrustListSub" class="west-notree" url="${epmctx}/entrust-hsf/sub/list.htm" onclick="changeMenu(this);"><span>HSF实验委托子台帐</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="epm_entrustHsf_list">
	<div id="entrustList" class="west-notree" url="${epmctx}/entrust-hsf/list.htm" onclick="changeMenu(this);"><span>HSF实验委托总台帐</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="epm_entrustHsf_list-y">
	<div id="entrustListY" class="west-notree" url="${epmctx}/entrust-hsf/list-y.htm" onclick="changeMenu(this);"><span>HSF隐藏台帐</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="epm_entrustOrt_input">
	<div id="entrustOrtInput" class="west-notree" url="${epmctx}/entrust-ort/input.htm" onclick="changeMenu(this);"><span>可靠性试验委托单</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="epm_entrustOrt-Sub_list">
	<div id="entrustOrtListSub" class="west-notree" url="${epmctx}/entrust-ort/sub/list.htm" onclick="changeMenu(this);"><span>可靠性试验委托子台帐</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="epm_entrustOrt_list">
	<div id="entrustOrtList" class="west-notree" url="${epmctx}/entrust-ort/list.htm" onclick="changeMenu(this);"><span>可靠性试验委托总台帐</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="epm_entrustOrt_list-y">
	<div id="entrustOrtListY" class="west-notree" url="${epmctx}/entrust-ort/list-y.htm" onclick="changeMenu(this);"><span>可靠性隐藏台帐</span></div>
</security:authorize>


<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>
  