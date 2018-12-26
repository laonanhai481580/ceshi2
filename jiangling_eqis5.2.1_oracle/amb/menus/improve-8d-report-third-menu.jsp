<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="IMP_8DREPORT_LIST">
	<div id="8d_report_list" class="west-notree" ><a href="${improvectx}/report/list.htm">8D改进报告台账</a></div>
</security:authorize>
<security:authorize ifAnyGranted="IMP_8DREPORT_INPUT">
	<div id="8d_report_input" class="west-notree" ><a href="${improvectx}/report/input.htm">8D改进报告</a></div>
</security:authorize>
<security:authorize ifAnyGranted="IMP_EXCEPTION_REPORT_LIST">
	<div id="exception_report_list" class="west-notree" ><a href="${improvectx}/exception/list.htm">品质异常联络单台账</a></div>
</security:authorize>
<security:authorize ifAnyGranted="IMP_EXCEPTION_REPORT_INPUT">
	<div id="exception_report_input" class="west-notree" ><a href="${improvectx}/exception/input.htm">品质异常联络单</a></div>
</security:authorize>
<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>