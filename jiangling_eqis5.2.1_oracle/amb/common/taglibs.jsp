<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="aa" uri="http://ajaxanywhere.sourceforge.net/"%> 
<%@ taglib prefix="wf" uri="http://www.norteksoft.com/workflow/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ds" uri="http://www.norteksoft.com/search/tags"%>
<%@ taglib prefix="menu" uri="http://www.norteksoft.com/menu/tags"%>
<%@ taglib prefix="grid" uri="http://www.norteksoft.com/view/tags"%>
<%@ taglib prefix="acsTags" uri="http://www.norteksoft.com/acs/tags"%>
<%@ taglib prefix="view" uri="http://www.norteksoft.com/view/tags"%>
<%@ taglib prefix="chart" uri="http://www.ambition-soft.com/tags/chart-view"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="mfgctx" value="${pageContext.request.contextPath}/carmfg"/>
<c:set var="iqcctx" value="${pageContext.request.contextPath}/iqc"/>
<c:set var="aftersalesctx" value="${pageContext.request.contextPath}/aftersales"/>
<c:set var="improvectx" value="${pageContext.request.contextPath}/improve"/>
<c:set var="monitorctx" value="${pageContext.request.contextPath}/monitor"/>
<c:set var="gsmctx" value="${pageContext.request.contextPath}/gsm"/>
<c:set var="supplierctx" value="${pageContext.request.contextPath}/supplier"/>
<c:set var="integrationctx" value="${pageContext.request.contextPath}/integration"/>
<c:set var="goalctx" value="${pageContext.request.contextPath}/goal"/>
<c:set var="costctx" value="${pageContext.request.contextPath}/cost"/>
<c:set var="spcctx" value="${pageContext.request.contextPath}/spc"/>
<c:set var="sictx" value="${pageContext.request.contextPath}/si"/>

<c:set var="qsmctx" value="${pageContext.request.contextPath}/qsm"/>
<c:set var="temctx" value="${pageContext.request.contextPath}/tem"/>
<c:set var="chartdesignctx" value="${pageContext.request.contextPath}/chartdesign"/>
<c:set var="sbglctx" value="${pageContext.request.contextPath}/sbgl"/>
<c:set var="costctx" value="${pageContext.request.contextPath}/cost"/>
<c:set var="newproductctx" value="${pageContext.request.contextPath}/newproduct" />
<c:set var="utilctx" value="${pageContext.request.contextPath}/util"/>
<c:set var="ecmctx" value="${pageContext.request.contextPath}/ecm"/>
<c:set var="helpctx" value="${pageContext.request.contextPath}/help"/>
<c:set var="epmctx" value="${pageContext.request.contextPath}/epm"/>

<c:url var="resourcesCtx" value='<%=com.norteksoft.product.util.PropUtils.getProp("host.resources")%>'></c:url>
<c:url var="imatrixCtx" value='<%=com.norteksoft.product.util.PropUtils.getProp("host.imatrix")%>'></c:url>
<c:url var="tempVersionType" value='<%=com.norteksoft.product.util.PropUtils.getOnlineProp("product.version.type")%>'></c:url>
<s:set id="versionType">${tempVersionType}</s:set>