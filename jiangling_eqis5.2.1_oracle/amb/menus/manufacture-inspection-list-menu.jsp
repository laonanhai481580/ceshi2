<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ambition.carmfg.entity.*,com.norteksoft.acs.base.web.struts2.Struts2Utils,java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="termination-inspection-first-record-list">
<div id="firstInspectionRecord" class="west-notree" url="${mfgctx}/inspection/termination-inspection-record/first-list-all.htm" onclick="changeMenu(this);"><span>首检数据台帐</span></div>
</security:authorize>
<security:authorize ifAnyGranted="termination-inspection-patrol-record-list">
<div id="patrolList" class="west-notree" url="${mfgctx}/inspection/termination-inspection-record/patrol-all-list.htm" onclick="changeMenu(this);"><span>巡检检查台帐</span></div>
</security:authorize>
<%-- <security:authorize ifAnyGranted="inspection-daliy-report-list-all">
<div id="daliyPoduceReportList" class="west-notree" url="${mfgctx}/inspection/daliy-report/list-all.htm" onclick="changeMenu(this);"><span>生产日报表台帐</span></div>
</security:authorize> --%>
<security:authorize ifAnyGranted="termination-inspection-end-record-list">
<div id="storageInspectionRecord" class="west-notree" url="${mfgctx}/inspection/termination-inspection-record/end-list-all.htm" onclick="changeMenu(this);"><span>末检检验台帐</span></div>
</security:authorize>
<security:authorize ifAnyGranted="termination-inspection-un-record-list">
<div id="unquafiedInspectionRecord" class="west-notree" url="${mfgctx}/inspection/termination-inspection-record/un-list.htm" onclick="changeMenu(this);"><span>过程检验不合格台帐</span></div>
</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>