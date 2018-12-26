<%@page import="com.ambition.carmfg.bom.service.ProductBomManager"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript">
		$(function(){
			var width = $(":input[name=subject]").width();
			$("#toNames").width(width-20);
		});
		function realSend(){
			if($("#contentForm").valid()){
				var params = {};
				$("#contentForm :input[name]").each(function(index,obj){
					params[obj.name] = $(obj).val();
				});
				params.svg = window.parent.$._chart_options.chart.getSVG();
				$("#message").html("正在发送邮件,请稍候...");
				$(":input[name]").attr("disabled","disabled");
				$.post("${mfgctx}/common/send-message.htm",params,function(result){
					$(":input[name]").removeAttr("disabled");
					$("#message").html("");
					if(result.error){
						alert(result.message);
					}else{
						window.parent.$.colorbox.close();
					}
				},'json');
			}
		}
		
		function cancel(){
			window.parent.$.colorbox.close();
		}
		
		//选择检验人员
	 	function selectObj(){
			var acsSystemUrl = "${ctx}";
			popTree({ 
				title : "选择收件人",
				innerWidth:'400',
				treeType:"MAN_DEPARTMENT_TREE",
				defaultTreeValue:"loginName",
				leafPage:'false',
				multiple:'true',
				hiddenInputId :'tos',
				showInputId : 'toNames',
				acsSystemUrl:acsSystemUrl,
				callBack:function(){}});
		}
	</script>
</head>
<body>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" id="btnDiv">
				<button class='btn' type="button" onclick="realSend();"><span><span><b class="btn-icons btn-icons-ok"></b>发送</span></span></button>	
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
				<span id="message" style="color:red;"></span>
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post">
					<table style="width:100%;margin-top:6px;" cellpadding="0" cellspacing="0">
						<tr height=29>
							<td style="width:70px;text-align:right;">
								<font color=red>*</font>收件人:
							</td>
							<td>
								<input id="toNames" style="float:left;" readonly="readonly" name="toNames" class="{required:true,messages:{required:'收信人必填!'}}"/>
								<input id="tos" name="tos" type="hidden"/>
								<a class="small-button-bg" style="margin-left:2px;float:left;margin-top:3px;" onclick="selectObj()" href="javascript:void(0);" title="选择收信人"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
							</td>
						</tr>
						<tr height=29>
							<td style="text-align:right;">
								<font color=red>*</font>主题:
							</td>
							<td>
								<input name="subject" style="width:98%;" class="{required:true,messages:{required:'标题必填!'}}"/>
							</td>
						</tr>
						<tr>
							<td style="text-align:right;" valign="top">
								内容:
							</td>
							<td colspan="2" style="padding:1px;padding-top:4px;">
								<textarea name="content" rows="22" style="width:98%;"></textarea>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>