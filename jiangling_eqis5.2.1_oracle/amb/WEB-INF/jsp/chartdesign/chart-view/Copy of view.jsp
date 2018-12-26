<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<jsp:include page="view-script.jsp" />
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<form action="post" onsubmit="return false" id="form">
			<div class="opt-body">
				<div class="opt-btn" id="btnDiv">
				 	<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
					<button class='btn' type="reset"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
					<button class='btn' onclick="toggleSearchTable();" type="button" id="showOrHideSearchBtn"><span><span><b class="btn-icons btn-icons-search"></b>隐藏查询</span></span></button>
				 	<span style="margin-left:4px;color:red;" id="message">
				 		<s:actionmessage theme="mytheme" />
				 	</span>
				 </div>
				<div id="opt-content">
					<table class="form-table-outside-border" style="width:100%;display:block;" id="searchTable">
						<tr>
							<td style="padding-left:6px;padding-bottom:4px;">
								<ul id="searchUl">
							 	</ul>
							 </td>
						</tr>
					</table>
					<table id="totalTable" class="form-table-outside-border" style="width:100%;">
						<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;">统计对象</caption>
						<tr>
							<td id="totalParent">
							</td>
						</tr>
					</table>
					<table id="groupTable" class="form-table-outside-border" style="width:100%;">
						<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;">统计分组</caption>
						<tr>
							<td id="groupParent">
							</td>
						</tr>
					</table>
					<div id="chartDiv" style="padding-top:8px;">
					</div>
				</div>
			</div>
		</form>
	</div>
</body>
</html>