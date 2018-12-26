<%@page import="java.util.*"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul>
	<security:authorize ifAnyGranted="si-report-input,si-report-list">
		<li  id="report">
			<span><span><a href='<grid:authorize code="si-report-input,si-report-list" systemCode="si"></grid:authorize>'>SI报告</a>
			</span></span>
		</li>
	</security:authorize>
		<security:authorize ifAnyGranted="SI_ANALYSE,si-analysis-okAndNg-list,si-analysis-bad-list,si-analysis-lrr-list,si-analysis-total-list">
		<li  id="analyse">
			<span><span>
				<a href='<grid:authorize code="SI_ANALYSE,si-analysis-okAndNg-list,si-analysis-bad-list,si-analysis-lrr-list,si-analysis-total-list" systemCode="si"></grid:authorize>'>统计分析</a>
			</span></span>
		</li>
	</security:authorize>
	<security:authorize ifAnyGranted="si-baseInfo-defection,si-baseInfo-indicator-list,si-baseInfo-item-list">
	<li  id="baseInfo" class="last"><span><span><a href="<grid:authorize code="si-baseInfo-defection,si-baseInfo-indicator-list,si-baseInfo-item-list" systemCode="si"></grid:authorize>">基础设置</a></span></span></li>
	</security:authorize>
</ul>
<div class="hid-header" onclick=headerChange(this); title="折叠/展开"></div>
<script>
	var topMenu='sysInprocess';
	$('#'+secMenu).addClass('sec-selected');
	function changeSecMenu(url){
		window.location = encodeURI(url);
	}
</script>


