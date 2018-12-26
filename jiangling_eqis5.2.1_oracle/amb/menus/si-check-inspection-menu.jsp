<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ambition.carmfg.entity.*,java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
	<security:authorize ifAnyGranted="si-report-input">
		<div id="_si_input" class="west-notree" url="${sictx}/check-inspection/input.htm" onclick="changeMenu(this);"><span>SI检验报告</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="si-report-list">
		<div id="_si_list" class="west-notree" url="${sictx}/check-inspection/list.htm" onclick="changeMenu(this);"><span>SI检验报告台帐</span></div>
	</security:authorize>
	<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>