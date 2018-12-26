<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
String userName=ContextUtils.getUserName();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<jsp:include page="form-script.jsp" />
	<script type="text/javascript">
		$(document).ready(function(){
			initForm();
			if('${workflowInfo.firstTaskId}'){
				$("#message").html("不合格品已提交,不能修改!");
// 				$(":input").attr("disabled","disabled");
			}
		});
	 	function submitForm(url,type){
			if($("#defectiveGoodsForm").valid()){
				getDetailItems();
				getComposingItems();
				$('#defectiveGoodsForm').attr('action',url);
				$(".opt-btn .btn").attr("disabled",true);
				$("#message").html("<b>数据保存中,请稍候... ...</b>");
				$('#defectiveGoodsForm').submit();
			}else{
				var error = $("#defectiveGoodsForm").validate().errorList[0];
				$(error.element).focus();
			}
		}
		function addNew(){
			window.location='${mfgctx}/defective-goods/ledger/input.htm';
		}
		function returnToPage(){
			this.history.back();
			//var htm = "";
		}
		function returnToIqcPage(){
			window.location='${iqcctx}/inspection-report/wait-audit.htm';
		}
		function SaveAsFile(){
			var id = '${id}';
			if(!id){
				alert("请先保存!");
				return;
			}
			window.location.href = '${mfgctx}/defective-goods/ledger/export-report.htm?id=${id}';
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="regectManager";
		var thirdMenu="_defective_goods_form";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-defective-goods-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow-y:auto;">
			<div class="opt-btn">
				<div  id="btnDiv">
					<security:authorize ifAnyGranted="MFG_DEFECTIVE-GOODS_FORM_INPUT">
					<button class='btn' onclick="javascript:window.location='${mfgctx}/defective-goods/ledger/input.htm';" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<s:if test="taskId<1">
						<security:authorize ifAnyGranted="MFG_DEFECTIVE-GOODS_FORM_SAVE">
						<button  class='btn' type="button" onclick="submitForm('${mfgctx}/defective-goods/ledger/save.htm','0')"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
						</security:authorize>
						<security:authorize ifAnyGranted="MFG_DEFECTIVE-GOODS_FORM_SUBMIT">
						<button  class='btn' type="button" onclick="submitForm('${mfgctx}/defective-goods/ledger/submit-process.htm','1')"><span><span><b class="btn-icons btn-icons-ok"></b>提交</span></span></button>
						</security:authorize>
					</s:if>
					<button class='btn' onclick="SaveAsFile();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					<button  class='btn' type="button" onclick="returnToPage();"><span><span><b class="btn-icons btn-icons-undo"></b>返回</span></span></button>
					<button  class='btn' type="button" onclick="returnToIqcPage();"><span><span><b class="btn-icons btn-icons-undo"></b>返回进货检验待审核台账</span></span></button>
					<span style="color:red;" id="message"><s:actionmessage theme="mytheme" /></span>
				</div>
			</div>
			<div id="opt-content" style="text-align: center;">
				<form action="" method="post" id="defectiveGoodsForm">
					<div>
						<input type="hidden" name="id" id="id" value="${id}"/>
						<input type="hidden" name="taskId" id="taskId" value="${taskId}" />
					</div>
					<jsp:include page="form.jsp" />
				</form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
</html>