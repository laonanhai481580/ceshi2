<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ambition.carmfg.entity.*,java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<%-- 	<%
  	List<Map<String,Object>> inputPoints = (List<Map<String,Object>>)ActionContext.getContext().getValueStack().findValue("inputPoints");
	if(inputPoints!=null){
		for(Map<String,Object> map : inputPoints){
	%>
		<div id="<%=map.get("id") %>" class="west-notree" url="${mfgctx}<%=map.get("thirdUrl") %>"  onclick="changeMenu(this);"><span><%=map.get("name")%></span></div>
	<%
		}
	}else{
	%>
		<div id="repair_record_report_list" class="west-notree" url="${mfgctx}/repair-record/list.jsp" onclick="changeMenu(this);"><span>提示页面</span></div>
	<%} %> --%>
	<security:authorize ifAnyGranted="inspection-made-inspection-input">
		<div id="_made_inspection_input" class="west-notree" url="${mfgctx}/inspection/made-inspection/input.htm" onclick="changeMenu(this);"><span>检验报告</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="made-inspection-recheck-list">
		<div id="_recheck_list" class="west-notree" url="${mfgctx}/inspection/made-inspection/recheck-list.htm" onclick="changeMenu(this);"><span>重检台帐</span></div>
	</security:authorize>	
	<security:authorize ifAnyGranted="made-inspection-wait-audit-list">
		<div id="_wait_audit_list" class="west-notree" url="${mfgctx}/inspection/made-inspection/wait-audit-list.htm" onclick="changeMenu(this);"><span>待审核台账</span></div>
	</security:authorize>		
	<security:authorize ifAnyGranted="made-inspection-first-list">
		<div id="_first_list" class="west-notree" url="${mfgctx}/inspection/made-inspection/first-list.htm" onclick="changeMenu(this);"><span>首检台帐</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="made-inspection-patrol-list">
		<div id="_patrol_list" class="west-notree" url="${mfgctx}/inspection/made-inspection/patrol-list.htm" onclick="changeMenu(this);"><span>巡检台帐</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="made-inspection-end-list">
		<div id="_end_list" class="west-notree" url="${mfgctx}/inspection/made-inspection/end-list.htm" onclick="changeMenu(this);"><span>末检台帐</span></div>
	</security:authorize>
	<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>