<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="epm_exceptionSingle_input1">
	<div id="exceptionSingleInput" class="west-notree" url="${epmctx}/exception-single/input.htm" onclick="changeMenu(this);"><span>异常处理单</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="epm_exceptionSingle_list">
	<div id="exceptionSingleList" class="west-notree" url="${epmctx}/exception-single/list.htm" onclick="changeMenu(this);"><span>HSF异常台帐</span></div>
	<div id="exceptionSingleList-REL" class="west-notree" url="${epmctx}/exception-single/list-rel.htm" onclick="changeMenu(this);"><span>可靠性异常台帐</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="epm_exceptionSingle_y">
	<div id="exceptionSingleList-y" class="west-notree" url="${epmctx}/exception-single/list-y.htm" onclick="changeMenu(this);"><span>异常隐藏台帐</span></div>
</security:authorize> 



<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>
  