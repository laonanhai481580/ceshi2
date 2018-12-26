<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp"%>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" ></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/dynamic.validate.js" ></script>
<%-- 	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script> --%>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	

<script type="text/javascript">
	$(document).ready(function() {
		var attachments = [
			   				{showInputId:'fileHidden',hiddenInputId:'processFile'}
			   			];
		parseDownloadFiles(attachments);
		$("#analyzeStartTime").datetimepicker({
			changeMonth : true,
			changeYear : true,
			"timeFormat" : 'hh:mm'
		});
		$("#analyzeEndTime").datetimepicker({
			changeMonth : true,
			changeYear : true,
			"timeFormat" : 'hh:mm'
		});
		$("#improveTime").datetimepicker({
			changeMonth : true,
			changeYear : true,
			"timeFormat" : 'hh:mm'
		});
		$("#measureTime").datetimepicker({
			changeMonth : true,
			changeYear : true,
			"timeFormat" : 'hh:mm'
		});
	});
	function parseDownloadFiles(arrs){
		for(var i=0;i<arrs.length;i++){
			var hiddenInputId = arrs[i].hiddenInputId;
			var showInputId = arrs[i].showInputId;
			var files = $("#"+hiddenInputId).val().length;
			if(files>0){
		 		$.parseDownloadPath({
					showInputId : showInputId,
					hiddenInputId : hiddenInputId
				});
			}
		}
	}
	//上传附件
	function uploadFiles(showId,hiddenId){
		$.upload({
			appendTo : '#opt-content',
			showInputId : showId,
			hiddenInputId : hiddenId,
			callback:function(files){
			}
		});
	}
	//选择责任人
	function selectPerson1(obj) {
		var acsSystemUrl = "${ctx}";
		popTree({
			title : '选择责任人',
			innerWidth : '400',
			treeType : 'MAN_DEPARTMENT_TREE',
			defaultTreeValue : 'id',
			leafPage : 'false',
			multiple : 'false',
			hiddenInputId : obj.id,
			showInputId : obj.id,
			acsSystemUrl : acsSystemUrl,
			callBack : function() {
			}
		});
	}
	//选择责任人
	function selectPerson2(obj) {
		var acsSystemUrl = "${ctx}";
		popTree({
			title : '选择责任人',
			innerWidth : '400',
			treeType : 'MAN_DEPARTMENT_TREE',
			defaultTreeValue : 'id',
			leafPage : 'false',
			multiple : 'false',
			hiddenInputId : obj.id,
			showInputId : obj.id,
			acsSystemUrl : acsSystemUrl,
			callBack : function() {
			}
		});
	}
	//选择原因
	function selectReasons() {
		$.colorbox({
			href : "${spcctx}/base-info/abnormal-reason/reasons-select.htm",
			iframe : true,
			innerWidth:$(window).width()<1366?$(window).width()-124:$(window).width()-366, 
			innerHeight:$(window).height()<768?$(window).height()-68:$(window).height()-68,
			overlayClose : false,
			title : "选择原因"
		});
	}
	function setReasonValue(datas) {
		var reason = "";
		var measure = "";
		for (var i = 0; i < datas.length; i++) {
			var obj = datas[i];
			reason += obj.reason;
			measure += obj.measures;
		}
		$('#reason').val(reason);
		$('#measure').val(measure);
	}
	function cancel() {
		window.parent.$.colorbox.close();
	}
	function submitForm(url) {
		if ($('#contentForm').valid()) {
			$('#contentForm').attr('action', url);
			$("#message").html("<b style=\"color: red\">数据保存中,请稍候... ...</b>");
			$('#contentForm').submit();
		}
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<%
					String messageId = request.getParameter("messageId");
				%>
				<button class="btn"
					onclick="submitForm('${spcctx}/process-monitor/save.htm?messageId=<%=messageId%>')">
					<span><span><b class="btn-icons btn-icons-save"></b>保存</span></span>
				</button>
				<button class="btn" onclick="cancel();">
					<span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span>
				</button>
			</div>
			<div style="display: block;" id="message">
				<s:actionmessage theme="mytheme" cssStyle="color:red;" />
			</div>
			<div id="opt-content" style="text-align: center;">
				<form action="" method="post" id="contentForm">
					<div id="hiddenDiv"><input type="hidden" name="id" id="id" value="${reasonMeasure.id}" /></div>
					<table class="form-table-without-border" style="width:100%;margin: auto;">
						<tr>
							<td style="width: 100%;">
								<fieldset>
									<legend style="font-size: 20px;">异常原因</legend>
									<table style="width:100%;margin: auto;" class="form-table-border-left">
										<tr>
											<td style="text-align: right;">分析开始时间</td>
											<td><input name="analyzeStartTime" id="analyzeStartTime"
												value="${reasonMeasure.analyzeStartTime }" /></td>
											<td>分析结束时间</td>
											<td><input name="analyzeEndTime" id="analyzeEndTime"
												value="${reasonMeasure.analyzeEndTime }" /></td>
											<td>分析人</td>
											<td><input name="reasonPersonLiable"
												id="reasonPersonLiable"
												value="${reasonMeasure.reasonPersonLiable }"
												onclick="selectPerson1(this);" /></td>
										</tr>
										<tr>
											<td style="text-align: right;vertical-align: top;"><span style="color: red;">*</span>异常分析原因<br></td>
											<td colspan="5" align="left">
												<textarea name="reason" id="reason" rows="10" class="{required:true,messages:{required:'原因必填!'}}">${reasonMeasure.reason }</textarea>
											</td>
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>
						<tr>
							<td style="width: 100%;" align="center">
								<a class="small-button-bg" style="float: right;" onclick="selectReasons(this);" href="javascript:void(0);" title="添加原因"><span class="ui-icon ui-icon-search" style='cursor: pointer;'></span></a><span style="color:red;font-size:18px;float: right;">点击此处选择原因--></span>
							</td>
						</tr>
						<tr>
							<td style="width: 100%;" align="center">
								<fieldset>
									<legend style="font-size: 20px;">改善措施</legend>
									<table style="width:100%;margin: auto;" class="form-table-border-left">
										<tr>
											<td style="text-align: right;">计划改善时间</td>
											<td><input name="improveTime" id="improveTime"
												value="${reasonMeasure.improveTime }" /></td>
											<td>采取措施时间</td>
											<td><input name="measureTime" id="measureTime"
												value="${reasonMeasure.measureTime }" /></td>
											<td>改善人</td>
											<td><input name="measurePersonLiable"
												id="measurePersonLiable"
												value="${reasonMeasure.measurePersonLiable }"
												onclick="selectPerson2(this);" /></td>
										</tr>
										<tr>
											<td style="text-align: right;vertical-align: top;"><span style="color: red;">*</span>异常改善措施</td>
											<td colspan="5" align="left"><textarea name="measure"
													id="measure" rows="10"
													class="{required:true,messages:{required:'改善措施必填!'}}">${reasonMeasure.measure }</textarea>
											</td>
										</tr>
										
										<tr>
										   <td  colspan="6" >
										    <button class='btn' type="button" onclick="uploadFiles('fileHidden','processFile');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
								            <input type="hidden" name="hisAttachmentFilesAll" value='${reasonMeasure.processFile}'></input>
								            <input type="hidden" name="processFile" id="processFile" value='${reasonMeasure.processFile}'></input>
								           <span id='fileHidden'></span>
								           </td>
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>