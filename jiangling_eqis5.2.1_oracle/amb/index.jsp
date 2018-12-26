<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@ include file="/common/taglibs.jsp"%>
<%@include file="/common/meta.jsp" %>
<%@page import="com.norteksoft.product.util.PropUtils"%>
<%
String url = PropUtils.getProp("host.imatrix") + "/portal/index/index.htm";
String queryStr = request.getQueryString();
if(queryStr != null){
	url += "?" + queryStr;
}
response.sendRedirect(url); 
%>
<%-- <% response.sendRedirect(ApiFactory.getMmsService().getAuthorizeUrl("iqc-statAnalyse-before-sales-ppm,iqc-statAnalyse-ppm-stat-chart,iqc-statAnalyse-ppm-rank-chart,iqc-statAnalyse-part-unquality-item,iqc-statAnalyse-iqc-ppm-rank,iqc-statAnalyse-check-type-chart,iqc-statAnalyse-defective-goods-chart")); %> --%>