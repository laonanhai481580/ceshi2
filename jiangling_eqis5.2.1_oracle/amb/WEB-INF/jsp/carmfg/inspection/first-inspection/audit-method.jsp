<%@page import="com.ambition.carmfg.entity.MfgCheckInspectionReport"%>
<%@page import="com.ambition.iqc.entity.IncomingInspectionActionsReport"%>
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
function auditPqc(obj,checkType){
	if(!obj){
		var ids = $("#dynamicInspection").jqGrid("getGridParam","selarrrow");
		if(ids.length==0){
			alert("请选择需要审核的数据!");
			return;
		}
		if(ids.length>1){
			alert("一次只能审核一条数据!");
			return;
		}
		obj = $("#dynamicInspection").jqGrid("getRowData",ids[0]);
		obj.id = ids[0];
	}
	if(obj.reportState!='<%=MfgCheckInspectionReport.STATE_AUDIT %>'){
		alert("只能审核状态为【<%=MfgCheckInspectionReport.STATE_AUDIT %>】的检验报告！");
		return;
	}
	var reportId = obj.id;
	var html = '<div id="auditBody" class="opt-body" style="overflow-y:auto;"><div class="opt-btn" style="width:400px;line-height:30px;">'
	+'<button style="margin-left:4px;" class="btn" type="button" onclick="processingResults('+reportId+',\''+checkType+'\');"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>'
	//+'<button style="margin-left:4px;" class="btn" type="button" onclick="processingResults(\''+checkType+'\')"><span><span><b class="btn-icons btn-icons-ok"></b>不发起不合格品处理</span></span></button>'
	//+'<button style="margin-left:4px;" class="btn" type="button" onclick="reCheck('+reportId+',\''+checkType+'\');"><span><span><b class="btn-icons btn-icons-cancel"></b>重新检验</span></span></button>'
	+'<span id="copyMessage" style="color:red;padding-left:4px;"></span></div>';
	html += '<form id="auditForm"><table class="form-table-border-left" style="margin:4px;width:98%;">';
	html += '<tr height=29><td>处置方式：<input type="radio" name="process_type" id="type_default" value="process" checked="checked"/><label for="type_default">发起不合格品处理流程</label></td>';
	html += '<tr height=29><td style="padding-left:77px;border-top:0px;"><input type="radio" name="process_type" id="type_no" value="no_process"/><label for="type_no" style="color:red;">不发起不合格品处理流程,审核完成</label></td>';
	html += '<tr height=29><td style="padding-left:77px;border-top:0px;"><input type="radio" name="process_type" id="type_back" value="back"/><label for="type_back">重新检验</label></td>';
	html += '<tr height=29><td>不发起不合格品处理流程的原因</td>';
	html += '<tr height=29><td><textarea name=opinion rows=8 style="width:99%;" disabled="disabled"></textarea></td>';
	html += '</table></form>';
	html += '</div>';
	var height = $(window).height()<400?$(window).height()-50:400;
	$.colorbox({
		title : '审核检验不合格报告',
		html : html,
		iframe:false,
		height : height,
		onComplete:function(){
			$("#auditBody").height(height-50);
			$("#auditForm").validate({});
			$("#auditForm :input[name=process_type]").bind("click",function(){
				if(this.value=='no_process'){
					$("#auditForm :input[name=opinion]").removeAttr("disabled").focus();
				}else{
					$("#auditForm :input[name=opinion]").attr("disabled","disabled");
				}
			});
		}
	});
}
function processingResults(reportId,checkType){
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
		if(params.process_type=='process'){
			beginDefectiveGoodsProcessForm(reportId);
		}else if(params.process_type=='back'){
			reCheck(reportId,checkType);
		}else if(confirm("确定不发起不合格品处理流程吗?")){
			$("#auditBody").find("button").attr("disabled","");
			$("#copyMessage").html("正在保存处理结果,请稍候... ...");
			var url = '${mfgctx}/inspection/first-inspection/audit.htm';
			if(checkType==''){
				url = '';
			}
			params.deleteIds=reportId;
			$.post(url,params,function(result){
				$("#auditBody").find("button").attr("disabled","");
				$("#copyMessage").html("");	
				if(result.error){
					alert(result.message);		
				}else{
					$.colorbox.close();
					if($("#dynamicInspection").length>0){
						$("#dynamicInspection").trigger("reloadGrid");
					}else{
						url = '${mfgctx}/inspection/first-inspection/input.htm?id=' + reportId;
						if(checkType=='storage'){
							url='${mfgctx}/inspection/storage-inspection/input.htm?id=' + reportId;
						}else if(checkType=='deliver'){
							url='${mfgctx}/inspection/deliver-inspection/input.htm?id=' + reportId;
						}
						window.location.href = url;
					}
				};
			},'json');
		};
	};
}
function reCheck(id,checkType){
	var ids = [];
	if(!id){
		ids = $("#dynamicInspection").jqGrid("getGridParam","selarrrow");
		if(ids.length==0){
			alert("请选择需要重新检验的数据!");
			return;
		}
	}else{
		ids.push(id);
	}
	if(confirm("确定要重新检验吗?")){
		$.colorbox.close();
		$("#message").html("正在提交请求,请稍候... ...");
		var url = '${mfgctx}/inspection/first-inspection/re-check.htm';
		if(checkType=='storage'){
			url='${mfgctx}/inspection/first-inspection/re-check.htm';
		}
		$.post(url,{deleteIds:ids.join(",")},function(result){
			if(result.error){
				$("#message").html("<b style='color:red;'>"+result.message+"</b>");
				alert(result.message);				
			}else{
				$("#message").html("<b style='color:red;'>操作成功!</b>");
				if($("#dynamicInspection").length>0){
					$("#dynamicInspection").trigger("reloadGrid");
				}else{
					url = '${mfgctx}/inspection/first-inspection/input.htm?id=${id}';
					if(checkType=='storage'){
						url='${mfgctx}/inspection/storage-inspection/input.htm?id=${id}';
					}else if(checkType=='deliver'){
						url='${mfgctx}/inspection/deliver-inspection/input.htm?id=${id}';
					}
					window.location.href = url;
				}
			}
		},'json');
	}
}