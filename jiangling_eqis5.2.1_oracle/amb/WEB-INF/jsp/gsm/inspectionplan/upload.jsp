<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<title><s:text name='gsm.title-4'/></title>
	<%@include file="/common/meta.jsp" %>
	<link href="${ctx}/widgets/swfupload/css/default.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="${ctx}/widgets/swfupload/css/button.css" type="text/css" />
	<script type="text/javascript" src="${ctx}/widgets/calendar/WdatePicker.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/swfupload/handlers.js"></script>
	<script type="text/javascript">
	var isUsingComonLayout=false;
		var swfu;
		window.onload = function () {
			swfu = new SWFUpload({
				upload_url: "${gsmctx}/inspectionplan/upload-datas.htm",
				post_params: {"id" : "<%=request.getParameter("id")%>"},
				file_post_name: "file",
				
				// File Upload Settings
				file_size_limit : "10 MB",	// 1000MB
				file_types : "*.*",
				file_types_description : "<s:text name='所有文件'/>",
				file_upload_limit : "0",
								
				file_queue_error_handler : fileQueueError,
				file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
				file_queued_handler : fileQueued,
				upload_progress_handler : uploadProgress,
				upload_error_handler : uploadError,
				upload_success_handler : uploadSuccess,
				upload_complete_handler : uploadComplete,

				// Button Settings
				button_image_url : "${ctx}/widgets/swfupload/css/images/SmallSpyGlassWithTransperancy_17x18.png",
				button_placeholder_id : "spanButtonPlaceholder",
				button_width: 80,
				button_height: 18,
				button_text : '<span class="button"><s:text name=\'选择文件\'/><span class="buttonSmall"></span></span>',
				button_text_style : '.button { font-family: Helvetica, Arial, sans-serif; font-size: 12pt; } .buttonSmall { font-size: 10pt; }',
				button_text_top_padding: 0,
				button_text_left_padding: 20,
				button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
				button_cursor: SWFUpload.CURSOR.HAND,
				
				// Flash Settings
				flash_url : "${ctx}/widgets/swfupload/swfupload.swf",

				custom_settings : {
					upload_target : "divFileProgressContainer"
				},
				debug: false  //是否显示调试窗口
			});	
		};
		function uploadComplete(){
			alert("<s:text name='gsm.message-63'/>");
		}
		function startUploadFile(){
			swfu.startUpload();
		}
	</script>
</head>
<body style="background-color: #C0D1E3; padding: 2px;">
	<div id="content">
		<form style="text-align: center;">
			<div style="display: inline; border: solid 1px #7FAAFF; background-color: #C5D9FF; padding: 2px;">
				<span id="spanButtonPlaceholder"></span>
				<input id="btnUpload" type="button" value="<s:text name='上  传'/>"
					onclick="startUploadFile();" class="btn3_mouseout" onMouseUp="this.className='btn3_mouseup'"
					onmousedown="this.className='btn3_mousedown'"
					onMouseOver="this.className='btn3_mouseover'"
					onmouseout="this.className='btn3_mouseout'"/>
				<input id="btnCancel" type="button" value="<s:text name='取  消'/>"
					onclick="cancelUpload();"  class="btn3_mouseout" onMouseUp="this.className='btn3_mouseup'"
					onmousedown="this.className='btn3_mousedown'"
					onMouseOver="this.className='btn3_mouseover'"
					onmouseout="this.className='btn3_mouseout'"/>
			</div>
		</form>
		<div id="divFileProgressContainer" ></div>
		<div id="thumbnails">
			<table id="infoTable" border="0" width="300" style="display: inline; border: solid 1px #7FAAFF; background-color: #C5D9FF; padding: 2px;margin-top:8px;">
			</table>
		</div>
	</div>
</body>
</html>