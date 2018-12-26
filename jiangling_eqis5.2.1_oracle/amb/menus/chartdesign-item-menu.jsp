<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="CHARTDESIGN-BASE-INFO-DATABASESETTING-LIST">
<div id="_base_info_databasesetting" class="west-notree" url="${chartdesignctx}/base-info/database-setting/list.htm" onclick="changeMenu(this);"><span>数据库配置</span></div>
</security:authorize>
<security:authorize ifAnyGranted="CHARTDESIGN-BASE-INFO-DATASOURCE-LIST">
<div id="_base_datasource" class="west-notree" url="${chartdesignctx}/base-info/datasource/list.htm" onclick="changeMenu(this);"><span>数据集管理</span></div>
</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>