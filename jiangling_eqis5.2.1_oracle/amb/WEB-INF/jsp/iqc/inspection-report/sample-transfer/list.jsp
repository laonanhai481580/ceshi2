<%@page import="com.ambition.iqc.entity.SampleTransferRecord"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
		function transferTargetFormatter(value,options,obj){
			if(obj.sourceRule){
				return '<b>' + obj.sourceRule + "</b> 至  <b>" + obj.targetRule + '</b>';
			}else{
				return '调整为 <b>' + obj.targetRule + '</b>';
			}
		}
		//同意
		function auditPass(){
			var ids=jQuery("#list").getGridParam('selarrrow');
			if(ids.length<=0){
				alert("请选择要审核的记录！");
			}else{
				$(".opt-btn .btn").attr("disabled",true);
				$("#message").html("正在执行操作,请稍候... ...");
				var params = {
					state : '<%=SampleTransferRecord.AUDITSTATE_PASS%>',
					ids : ids.join(",")
				};
				$.post("${iqcctx}/inspection-report/sample-transfer/audit.htm",params, function(result) {
					$(".opt-btn .btn").attr("disabled","");
					$("#message").html(result.message);
					if(result.error){
						alert(result.message);
					}else{
						jQuery("#list").trigger("reloadGrid");
					}
					setTimeout(function(){
						$("#message").html("");
					},3000);
				},"json");
			}
		}
		//不同意
		function auditFail(){
			var ids=jQuery("#list").getGridParam('selarrrow');
			if(ids.length<=0){
				alert("请选择要审核的记录！");
			}else{
				$(".opt-btn .btn").attr("disabled",true);
				$("#message").html("正在执行操作,请稍候... ...");
				var params = {
					state : '<%=SampleTransferRecord.AUDITSTATE_FAIL%>',
					ids : ids.join(",")
				};
				$.post("${iqcctx}/inspection-report/sample-transfer/audit.htm",params, function(result) {
					$(".opt-btn .btn").attr("disabled","");
					$("#message").html(result.message);
					if(result.error){
						alert(result.message);
					}else{
						jQuery("#list").trigger("reloadGrid");
					}
					setTimeout(function(){
						$("#message").html("");
					},3000);
				},"json");
			}
		}
		function addSampleTransfer(){
			$.colorbox({
				title : '添加转移方案',
				href : '${iqcctx}/inspection-report/sample-transfer/input.htm',
				iframe:true,
				innerWidth:700,
				innerHeight:500,
				onClosed:function(){
					jQuery("#list").trigger("reloadGrid");
				}
			});
		}
		function audit(obj){
			var ids = $("#list").jqGrid("getGridParam","selarrrow");
			if(ids.length==0){
				alert("请选择审核的方案");
				return;
			};
			if(ids.length>1){
				alert("只能选择一条方案");
				return;
			};	
			var state="";
			if(obj.id=="approve"){
				state ='<%=SampleTransferRecord.AUDITSTATE_PASS%>';
			}else{
				state ='<%=SampleTransferRecord.AUDITSTATE_FAIL%>';
			}
 			var html = '<div id="auditBody" class="opt-body" style="overflow-y:auto;"><div class="opt-btn" style="line-height:30px;">'
 			+'<button class="btn" type="button" onclick="saveAudit();"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>'
 			+'<span id="copyMessage" style="color:red;padding-left:4px;"></span></div>';
 			html += '<form id="auditForm"><table class="form-table-border-left" style="margin:4px;width:98%;">';
 			html += '<tr><td style="width:20%;">审核意见</td><td colspan="3"><input value="'+state+'" type="hidden" name="state"></input><input value="'+ids+'" type="hidden" name="ids"><textarea name="auditText" rows="2" cols="2"></textarea></td></tr>';
 			html += '</table></form>';
 			var height = $(window).height()<250?$(window).height()-50:250;
 			var width = $(window).width()-200>650?650:$(window).width();
 			$.colorbox({
 				title : '方案审核',
 				html : html,
 				iframe:false,
 				height : height,
 				width:width,
 				onComplete:function(){
 					$("#auditBody").height(height-50);
 					$("#auditForm").validate({});
 				}
 			});
		}
 		function saveAudit(){
 			if($("#auditForm").valid()){
 				var params = {};
 				$("#auditForm :input[name]").each(function(index,obj){
 					if(obj.type=='radio'){
 						if(obj.checked){
 							params[obj.name] = obj.value;
 						}
 					}else{
 						params[obj.name] = $(obj).val();
 					}
 				});
 				$("#auditBody").find("button").attr("disabled","");
 				$("#copyMessage").html("正在保存处理结果,请稍候... ...");	
 				$.post("${iqcctx}/inspection-report/sample-transfer/audit.htm",params,function(result){
 					$("#auditBody").find("button").attr("disabled","");
 					$("#copyMessage").html("");	
 					if(result.error){
 						alert(result.message);		
 					}else{
 						$.colorbox.close();
 						$("#list").trigger("reloadGrid");
 					};
 				},'json');
 			};
 		}		
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="inspectionReport";
		var thirdMenu="sampleTransfer";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-inspection-report-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="IQC_SAMPLE-TRANSFER_LIST_ADD">
					<button class='btn' onclick="addSampleTransfer();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>添加方案</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="IQC_SAMPLE-TRANSFER_LIST_DELETE">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>	
				</security:authorize>
<%-- 				<security:authorize ifAnyGranted="IQC_SAMPLE-TRANSFER_LIST_AUDIT-PASS">
					<button class='btn' type="button" onclick="auditPass();"><span><span><b class="btn-icons btn-icons-ok"></b>同意</span></span></button>	
				</security:authorize>
				<security:authorize ifAnyGranted="IQC_SAMPLE-TRANSFER_LIST_AUDIT-FAIL">
					<button class='btn' type="button" onclick="auditFail();"><span><span><b class="btn-icons btn-icons-cancel"></b>不同意</span></span></button>
				</security:authorize> --%>
				<security:authorize ifAnyGranted="IQC_SAMPLE-TRANSFER_LIST_AUDIT-PASS"> 
						<button  class="btn" id="approve" onclick="audit(this)"><span><span><b class="btn-icons btn-icons-ok"></b>同意</span></span></button>
						<button  class="btn" id="refuse" onclick="audit(this)"><span><span><b class="btn-icons btn-icons-cancel"></b>不同意</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="IQC_SAMPLE-TRANSFER_EXPORT"> 
					<button class="btn" onclick="iMatrix.export_Data('${iqcctx}/inspection-report/sample-transfer/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
				<span style="line-height:30px;margin-left:6px;color:red;" id="message"></span>
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post"  action="">
					<grid:jqGrid gridId="list" url="${iqcctx}/inspection-report/sample-transfer/list-datas.htm" code="IQC_SAMPLE_TRANSFER_RECORD" pageName="page"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>