<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<%@include file="../../manu-analyse/qualityrate-top10-script.jsp" %>
	<script type="text/javascript">
	//查看不良率推移图
	function showUnqualityTrend(title,pointName){
		if(!cacheParams){
			alert("数据错误!");
			return;
		}
		var params = {};
		var group = cacheParams["params.group"];
		for(var pro in cacheParams){
			if(pro != 'params.group' && pro != 'params.groupValue' && pro != 'params.groupName'){
				params[pro] = cacheParams[pro];
			}
		}
		params["params." + group + "_equals"] = title;
		params["params.itemname_equals"] = pointName;
		window.location = "${mfgctx}/data-acquisition/data-acquisition-analyse/unquality-trend.htm?" + $.param(params);
	}
	
	
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="dataAcquisition";
		var thirdMenu="qualityrateTop10";
	</script>
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-data-acquisition-menu.jsp" %>
	</div>
	<%
request.setAttribute("inspectionPointType",InspectionPointTypeEnum.PATROLINSPECTION.ordinal());
%>
	<div class="ui-layout-center" style="overflow-y:auto;">
		<%@include file="../../manu-analyse/qualityrate-top10-center.jsp" %>
	</div>
</body>
</html>