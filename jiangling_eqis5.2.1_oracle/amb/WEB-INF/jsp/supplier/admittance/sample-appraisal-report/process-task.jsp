<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/widgets/workflowEditor/swfobject.js"></script>
	<script type="text/javascript">
		var isUsingComonLayout=false;
		$(function(){
			$("#tabs").tabs({
			});
			setTimeout(function(){
				$("#tabs-1 .opt-content").width($(window).width()-4);
				$("#tabs-1 .opt-content").height($(window).height()-100);
			}, 100);
			$.parseDownloadPath({
				showInputId : 'showAttachmentFiles',
				hiddenInputId : 'attachmentFiles'
			});
			//添加验证
// 			$("#appraisalReportForm").validate({
// 			});
			$("#appraisalReportForm :input").attr("readonly","readonly");
			$("#appraisalReportForm :checkbox").attr("disabled",true);
			$("#appraisalReportForm :radio").attr("disabled",true);
			$("#appraisalReportForm :input[name=opinion]").attr("readonly","");
		});
		function completeTask(taskTransact,operateName) {
			$('#taskTransact').val(taskTransact);
			$('#operateName').val(operateName);
			$(".btn").attr("disabled", "disabled");
			$("#message").html("正在保存,请稍候... ...");
			$('#appraisalReportForm').submit();
		}
	 	function retriveTask() {
			$(".opt_btn").find("button.btn").attr("disabled", "disabled");
			aa.submit("appraisalReportForm", "", 'main', showMsg);
	 	}
	 	function submitImprove(taskTransact){
	 		var url='${improvectx}/correction-precaution/called-input.htm';
	 		$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
	 			overlayClose:false,
	 			title:"改进页面",
	 			onClosed:function(){
	 				$('#taskTransact').val(taskTransact);
	 				$('#appraisalReportForm').submit();
	 			}
	 		});
	 	}
	 	function changeViewSet(url,obj){
	 		var target = $(obj).attr("href");
	 		var loaded = $(target).attr("loaded");
	 		if(!loaded){
	 			$(obj).attr("loaded","loading");
	 			var id = $("#opt-content :input[name=id]").val();
	 			$(target).html("<div style='paddint-top:4px;'>数据加载中,请稍候... ...</div>")
	 				.height($(window).height()-40)
	 				.load(url,{id:id},function(result){
		 				$(target).attr("loaded",true);
		 			});
	 		}
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="opt-body">
		<div id="tabs">
			<ul>
				<li><a href="#tabs-1" onclick="changeViewSet('',this)">表单信息</a></li>
				<li><a href="#tabs-2" onclick="changeViewSet('${supplierctx}/admittance/sample-appraisal-report/history.htm',this)">流程进度</a></li>
			</ul>
			<div id="tabs-1" style="padding:0px;" loaded='true'>
				<%@ include file="process-form.jsp"%>
			</div>
			<div id="tabs-2" style="padding:0px;overflow-y:auto;text-align:center;">
			</div>
		</div>
	</div>
</body>
</html>