<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script language="javascript" src="${ctx}/widgets/lodop/LodopFuncs.js"></script>
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="${ctx}/widgets/lodop/install_lodop.exe"></embed>
	</object> 
	<script type="text/javascript">
	isUsingComonLayout = false;
	var hasInitHistory = false;
	$(document).ready(function() {
		$( "#tabs" ).tabs({
			show: function(event, ui) {
				if(ui.index==1&&!hasInitHistory){
					hasInitHistory = true;
					$("#tabs-2").load("${mfgctx}/inspection/made-inspection/history.htm?taskId=${workflowInfo.firstTaskId}",function(){
						$("#tabs-2").height($(window).height()-115);
					});
				}
			}
		});
		$("#tabs-1").height($(window).height()-115);
		initForm();
	});
	function closeBtn(){
		window.parent.$.colorbox.close();
	}
	function initForm(){
		$("input[name^='inspectDate']").each(function(index,obj){
			$(obj).datepicker({changeMonth:true,changeYear:true});
		});
		addFormValidate('${fieldPermission}', 'defectiveGoodsForm');
		var attachmentMap = {
			reviewAttachment : true,
			inspectorAttachment :true,
			qualityEngineerAttachment:true,
		};
		var fieldPermission = ${fieldPermission};
		var editDetailItemReadonly =false;
		if(fieldPermission.length==1&&fieldPermission[0].controlType=='allReadolny'){
			$("a").removeAttr("onclick");
			$("button[uploadBtn=true]").hide();
			$(":input[name]").attr("disabled","disabled");
		}
		for(var i=0;i<fieldPermission.length;i++){
			var obj=fieldPermission[i];
			if(obj.readonly=='true'){
				var $obj = $(":input[name="+obj.name+"]");
				if($obj.attr("type") != 'hidden'){
					$obj.attr("disabled","disabled");
				}else{
					if(attachmentMap[obj.name]){
						$obj.closest("td").find("button[uploadBtn=true]").hide();
					}
				}
				if(obj.name=="supplierMessageStr"){
					$(".mfgSupplierMessagesTr").find(":input").attr("disabled","disabled");
					$(".mfgSupplierMessagesTr").find("a").removeAttr("onclick");
				}else if(obj.name=="checkedItemStr"){
					$("#checkItemsParent").find(":input").attr("disabled","disabled");
					$("#checkItemsParent").find("a").removeAttr("onclick");
				}
			}
		};
	}
	</script>
</head>

<body>
	<div class="opt-body">
		<div class="opt-btn">
			<button class="btn" onclick="closeBtn();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
		</div>
		<div id="opt-content" class="form-bg">
			<form id="defaultForm1" name="defaultForm1" action="">
				<input type="hidden" name="id" id="id" value="${id}" />
				<input name="taskId" id="taskId" value="${taskId}" type="hidden" />
				<input id="selecttacheFlag" type="hidden" value="true" />
			</form>
			<div id="tabs">
				<ul>
					<li><a href="#tabs-1">表单信息</a>
					</li>
					<s:if test="workflowInfo.firstTaskId>0">
					<li><a href="#tabs-2">流转历史</a>
					</li>
					</s:if>
				</ul>
				<div id="tabs-1" style="background:#ECF7FB;text-align: center;overflow-y:auto;">
					<form id="feesapplyForm" name="feesapplyForm" method="post" action=""
						onsubmit="return false;">
						<input type="hidden" name="params.saveType" value="input" />
						<input type="hidden" name="assignee" id="assignee"></input>
						<input type="hidden" name="addSignPerson" id="addSignPerson" />
						<input type="hidden" id="id" name="id" value="${id}"></input>
						<input name="taskId" id="taskId" value="${taskId}" type="hidden" />
						<jsp:include page="input-form.jsp" />
					</form>
				</div>
				<div id="tabs-2"></div>
			</div>
		</div>
	</div>
</body>
</html>