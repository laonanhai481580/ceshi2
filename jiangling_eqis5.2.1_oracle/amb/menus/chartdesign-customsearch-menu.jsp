<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="CHARTDESIGN-CUSTOMSEARCH-LIST">
<div id="_list" class="west-notree" url="${chartdesignctx}/custom-search/list.htm" onclick="changeMenu(this);"><span>查询配置</span></div>
</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>