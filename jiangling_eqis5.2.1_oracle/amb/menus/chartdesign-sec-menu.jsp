<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul>
	<security:authorize ifAnyGranted="CHARTDESIGN-BASE-INFO-DATABASESETTING-LIST">
		<li id="basic"><span><span><a href="<grid:authorize code="CHARTDESIGN-BASE-INFO-DATABASESETTING-LIST" systemCode="chartdesign"></grid:authorize>">数据库</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="CHARTDESIGN-BASE-INFO-DATASOURCE-LIST">
		<li id="_basic_datasource"><span><span><a href="<grid:authorize code="CHARTDESIGN-BASE-INFO-DATASOURCE-LIST" systemCode="chartdesign"></grid:authorize>">数据集</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="CHARTDESIGN-CHARTDEFINITION-LIST">
		<li id="_basic_chartdefinition"><span><span><a href="<grid:authorize code="CHARTDESIGN-CHARTDEFINITION-LIST" systemCode="chartdesign"></grid:authorize>">统计图</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="CHARTDESIGN-CUSTOMSEARCH-LIST">
		<li id="_custom_search"><span><span><a href="<grid:authorize code="CHARTDESIGN-CUSTOMSEARCH-LIST" systemCode="chartdesign"></grid:authorize>">自定义查询</a></span></span></li>
	</security:authorize>
</ul>
<div class="hid-header" onclick=headerChange(this); title="折叠/展开"></div>
<script>
    var topMenu='sysPlan';
	$('#'+secMenu).addClass('sec-selected');
</script>