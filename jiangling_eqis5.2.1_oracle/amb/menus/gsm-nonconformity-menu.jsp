<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="gsm_nonconformity_input">
	<div id="nonconformityInput" class="west-notree" url="${gsmctx}/nonconformity/input.htm" onclick="changeMenu(this);"><span>不合格品处理单</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="gsm_nonconformity_list">
	<div id="nonconformityList" class="west-notree" url="${gsmctx}/nonconformity/list.htm" onclick="changeMenu(this);"><span>不合格品处理台帐</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="gsm_scrap_input">
	<div id="scrapInput" class="west-notree" url="${gsmctx}/scrap/input.htm" onclick="changeMenu(this);"><span>报废单</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="gsm_scrap_list">
	<div id="scrapList" class="west-notree" url="${gsmctx}/scrap/list.htm" onclick="changeMenu(this);"><span>报废台帐</span></div>
</security:authorize> 
<security:authorize ifAnyGranted="gsm_maintain_list">
	<div id="maintainList" class="west-notree" url="${gsmctx}/maintain/list.htm" onclick="changeMenu(this);"><span>维修管理台帐</span></div>
</security:authorize> 

<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>
  