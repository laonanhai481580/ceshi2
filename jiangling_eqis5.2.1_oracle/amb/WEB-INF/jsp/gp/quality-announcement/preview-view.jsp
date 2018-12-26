<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>全面品质管理系统</title>
<%@include file="/common/meta.jsp"%>

<script type="text/javascript" src="${ctx}/widgets/calendar/WdatePicker.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<link rel="stylesheet" href="${ctx}/widgets/kindeditor-4.1.7/themes/default/default.css" />
	<script charset="utf-8" src="${ctx}/widgets/kindeditor-4.1.7/kindeditor-min.js"></script>
	<script charset="utf-8" src="${ctx}/widgets/kindeditor-4.1.7/lang/zh_CN.js"></script>

<script type="text/javascript">

	$(document).ready(function(){
		var file = "${attachFile}";
		if(file==""){
			$("#file").html("");
		}else{
			$.parseDownloadPath({
				showInputId : 'showAttachFile',
				hiddenInputId : 'attachFile'
			});
		}
		var time = "${releaseTime}";
		$("#time").html(time.substr(0,19));
	});

	
	var editor;
	KindEditor.ready(function(K) {
		editor = K.create('textarea[name="content"]', {
			resizeType : 1,
			allowPreviewEmoticons : false,
			allowImageUpload : false,
			items : []
		});
	});

	//关闭
	function cancel(){
		<%
			String _from = request.getParameter("_from");
			if("portal".equals(_from)){
		%>
		window.close();
		<%}else{%>
		window.parent.$.colorbox.close();
		<%}%>
	}
	
	
	
	
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="projectAnnouncement";
		var thirdMenu="projectAnnouncementInput";
	</script>

	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<button class='btn' onclick="cancel();">
				<span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span>
				</button>
			</div>
			<div id="opt-content" style="clear:both;" align="center">
				<form id="basicForm" name="basicForm" method="post" action="">
				<input type="hidden" name="id" id="id" value="${id}"></input>
				<input type="hidden" name="projectPlanId" id="projectPlanId"></input>
						<span style="margin-left:6px;line-height:30px;color: red;" id="message"></span>
								<table style="width: 800px;">
									<tr><td colspan="10" style="width:100%;text-align: center;"><span style="text-align: center;line-height:30px;font-size: 25px;">${title}</span></td></tr>
									<tr align="center">
										<td align="center"><span style="text-align: center;line-height:80px;">【发布时间】:<span id="time"></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;【来源】:${publishOrganization}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;【作者】:${publisher}</span></td>
							  		</tr>
									<tr>
									<td  colspan="8">${contentHtml}</td>
									</tr>
									<tr align="right">
										<td align="right">${publishOrganization}</br><s:date name='releaseTime' format='yyyy-MM-dd HH:mm:ss'/></span></td>
							  		</tr>
							  		<tr align="left">
									<td class="content-title" style="width:200px" colspan="3">
									<input type="hidden" name="hisAttachFile" value='${attachFile}'></input>
									<input type="hidden" name="attachFile" id="attachFile" value='${attachFile}'></input>
									<span id="file">附件:</span><span id="showAttachFile"></span>
									</td>
							  		</tr>
								</table>
					</form>
				</div>
			</div>
			</div>
</body>

</html>