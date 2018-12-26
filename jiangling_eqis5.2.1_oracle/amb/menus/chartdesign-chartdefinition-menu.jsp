<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="CHARTDESIGN-CHARTDEFINITION-LIST">
<div id="_chart_definition" class="west-notree" url="${chartdesignctx}/chart-definition/list.htm" onclick="changeMenu(this);"><span>统计图配置</span></div>
</security:authorize>
<security:authorize ifAnyGranted="CHARTDESIGN-LISTDEFINITION-LIST">
<div id="_list_definition" class="west-notree" url="${chartdesignctx}/list-definition/list.htm" onclick="changeMenu(this);"><span>台帐配置</span></div>
</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>