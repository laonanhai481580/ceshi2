<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="epm_sample_input1">
	<div id="sampleInput" class="west-notree" url="${epmctx}/sample/input.htm" onclick="changeMenu(this);"><span>样品管理单</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="epm_sample_list">
	<div id="sampleList" class="west-notree" url="${epmctx}/sample/list.htm" onclick="changeMenu(this);"><span>REL样品台帐</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="epm_sample_list-hsf">
	<div id="sampleList-hsf" class="west-notree" url="${epmctx}/sample/list-hsf.htm" onclick="changeMenu(this);"><span>HSF样品台帐</span></div>
</security:authorize>
<security:authorize ifAnyGranted="epm_sample_sub_list">
	<div id="sampleSubList" class="west-notree" url="${epmctx}/sample/sub/list.htm" onclick="changeMenu(this);"><span>样品归还台帐</span></div>
</security:authorize>


<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>
  