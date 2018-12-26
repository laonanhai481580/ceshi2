<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<input id="companyId" type="hidden" value="${inspectionReport.companyId}" />
<input id="instanceId" type="hidden" value="${inspectionReport.workflowInfo.workflowId}" />
<input id="localeLang" type="hidden" value="<%= request.getLocale().getLanguage() %>" />
<div id="flashcontent" style="width: 97%; height: 700px;"></div>
<script type="text/javascript">
$().ready(function() {
	_add_SWf();
	});
</script>