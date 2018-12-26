<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul>
	<security:authorize ifAnyGranted="monitor-iqc-warnRules-list,monitor-mfg-warnRules-list,monitor-aftersales-warnRules-list">
		<li id="_rule"><span><span><a href="<grid:authorize code="monitor-iqc-warnRules-list,monitor-mfg-warnRules-list,monitor-aftersales-warnRules-list" systemCode="monitor"></grid:authorize>">监控规则维护</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="monitor-iqc-warnNotices-list,monitor-mfg-warnNotices-list,monitor-aftersales-warnNotices-list"> 
		<li id="_notice"><span><span><a href="<grid:authorize code="monitor-iqc-warnNotices-list,monitor-mfg-warnNotices-list,monitor-aftersales-warnNotices-list" systemCode="monitor"></grid:authorize>">监控消息台账</a></span></span></li>
	</security:authorize>
</ul>
<div class="hid-header" onclick=headerChange(this); title="折叠/展开"></div>
<script>
    var topMenu='sysPlan';
	$('#'+secMenu).addClass('sec-selected');
</script>

