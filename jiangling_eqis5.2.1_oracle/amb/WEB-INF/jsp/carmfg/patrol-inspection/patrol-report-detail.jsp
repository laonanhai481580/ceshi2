<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="java.net.URLEncoder"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		function click(cellvalue, options, rowObject){	
			return "<a href='#' onclick='javascript:goTo(\""+rowObject.id+"\")'>"+cellvalue+"</a>";
		}
		function goTo(id){
			window.location.href = "${mfgctx}/patrol-inspection/view-info.htm?id="+id;
		}
		function formateAttachmentFiles(value,o,obj){
			return "<div><span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span></div>";
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn" id="btnDiv">
					<button class='btn' onclick="javascript:window.parent.$.colorbox.close();" type="button"><span><span><b class="btn-icons btn-icons-close"></b>关闭</span></span></button>
				</div>
				<div id="opt-content" style="overflow:auto;visibility:visible;">
					<form id="contentForm" name="contentForm" method="post" action="">
						<%
							String queryString = "";
							Set<String> set = request.getParameterMap().keySet();
							for(String pro : set){
								if(pro.startsWith("params.")){
									if(!"params.itemcheckItemName_like".equals(pro)){//自动过滤检验项目查询项目
										String val = request.getParameter(pro);
										queryString += "&" + pro + "=" + request.getParameter(pro);
									}
								}
							}
							ActionContext.getContext().put("queryString",queryString);
						%>
						<grid:jqGrid gridId="dynamicInspection"  url="${mfgctx}/patrol-inspection/list-datas-by-item.htm?a=1${queryString}" code="MFG_PATROL_INSPECTION_REPORT" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>