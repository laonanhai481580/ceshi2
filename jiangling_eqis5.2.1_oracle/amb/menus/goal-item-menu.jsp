<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="GOAL-BASE-INFO-DATABASESETTING-LIST">
<div id="_base_info_databasesetting" class="west-notree" url="${goalctx}/base-info/database-setting/list.htm" onclick="changeMenu(this);"><span>数据库配置</span></div>
</security:authorize>
<security:authorize ifAnyGranted="GOAL-BASE-INFO-DATASOURCE-LIST">
<div id="_base_datasource" class="west-notree" url="${goalctx}/base-info/datasource/list.htm" onclick="changeMenu(this);"><span>数据集管理</span></div>
</security:authorize>
<security:authorize ifAnyGranted="GOAL-BASE-INFO-METADATA-LIST">
<div id="_meta_data" class="west-notree" url="${goalctx}/base-info/meta-data/list.htm" onclick="changeMenu(this);"><span>元数据维护</span></div>
</security:authorize>
<security:authorize ifAnyGranted="GOAL-BASE-INFO-INDEXDATA-LIST">
<div id="_index_data" class="west-notree" url="${goalctx}/base-info/index-data/list.htm" onclick="changeMenu(this);"><span>指标维护</span></div>
</security:authorize>
<security:authorize ifAnyGranted="GOAL_GOAL-ITEM_LIST_LIST">
	<div id="_item" class="west-notree" url="${goalctx}/goal-item/list.htm" onclick="changeMenu(this);"><span>目标维护</span></div>
</security:authorize>
<security:authorize ifAnyGranted="GOAL_GOAL-ITEM-SECURITY_LIST_LIST">
	<div id="_item_security" class="west-notree" url="${goalctx}/goal-item-security/list.htm" onclick="changeMenu(this);"><span>KPI查询授权</span></div>
</security:authorize>
<security:authorize ifAnyGranted="GOAL_GOAL-STATISTICS_MONITOR-LIST_LIST">
	<div id="_monitor_list" class="west-notree" url="${goalctx}/goal-statistics/monitor-list.htm" onclick="changeMenu(this);"><span>监控图配置</span></div>
</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>