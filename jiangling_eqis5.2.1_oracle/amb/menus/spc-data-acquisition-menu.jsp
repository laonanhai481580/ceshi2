<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>

<security:authorize ifAnyGranted="spc_data-acquisition_list">
	<div id="_acq" class="west-notree" url="${spcctx}/data-acquisition/list.htm" onclick="changeMenu(this);"><span>在线采集</span></div>
</security:authorize>
<security:authorize ifAnyGranted="spc_data-acquisition_off-line-list">
	<div id="_off_line" class="west-notree" url="${spcctx}/data-acquisition/off-line-list.htm" onclick="changeMenu(this);"><span>离线采集</span></div>
</security:authorize>
<%-- <security:authorize ifAnyGranted="spc_data-acquisition_list">
	<div id="_analysis" class="west-notree" url="${spcctx }/statistics-analysis/data-analysis.htm" onclick="changeMenu(this);"><span>数据分析</span></div>
</security:authorize> --%>
<security:authorize ifAnyGranted="spc_data-acquisition_maintenance-list">
	<div id="_maintenance" class="west-notree" url="${spcctx}/data-acquisition/maintenance-list.htm" onclick="changeMenu(this);"><span>数据维护</span></div>
</security:authorize>
<%-- <security:authorize ifAnyGranted="spc_data-acquisition_list">
	<div id="_aboutAnalysis" class="west-notree" url="${spcctx }/statistics-analysis/data-about-analysis.htm" onclick="changeMenu(this);"><span>相关分析</span></div>
</security:authorize> --%>

<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>