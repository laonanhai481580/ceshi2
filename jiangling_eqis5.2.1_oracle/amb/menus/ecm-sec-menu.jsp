<%@page import="com.ambition.carmfg.entity.InspectionPoint"%>
<%@page import="com.ambition.carmfg.entity.InspectionPointTypeEnum"%>
<%@page import="java.util.*"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul>
	<security:authorize ifAnyGranted="ecm-dern-input,ecm-dern-list">
		<li  id="_dern">
			<span><span><a href='<grid:authorize code="ecm-dern-input,ecm-dern-list" systemCode="ecm"></grid:authorize>'>DCR/N变更单</a>
			</span></span>
		</li>
	</security:authorize>
	<security:authorize ifAnyGranted="ecm-ecr-input,ecm-ecr-list">
		<li  id="_ecr">
			<span><span><a href='<grid:authorize code="ecm-ecr-input,ecm-ecr-list" systemCode="ecm"></grid:authorize>'>ECR变更单</a>
			</span></span>
		</li>
	</security:authorize>
	<security:authorize ifAnyGranted="ecm-ecn-input,ecm-ecn-list">
		<li  id="_ecn">
			<span><span>
				<a href='<grid:authorize code="ecm-ecn-input,ecm-ecn-list" systemCode="ecm"></grid:authorize>'>ECN变更</a>
			</span></span>
		</li>
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


